package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author sreddy
* @Requirement : DE5160ShortageCreditInvoicePushedForwardWhenThePolicyIsDelinquentButNotCancelled 
* @Description 1. Issue a Quarterly Squaire Policy and make down payment
*              2. Delinquent the First Invoice and make a negative policy change
*              3. verify Invoices for a shortage invoice
* @DATE April 2, 2017
*/
public class DE5160ShortageCreditInvoicePushedForwardWhenThePolicyIsDelinquentButNotCancelled extends BaseTest {
	private WebDriver driver;
	 private GeneratePolicy mySQPolicyObjPL = null;

	private Underwriters uw;
	private ARUsers arUser ;  //= new ARUsers();
	
	
  @Test 
  private void generatePolicy() throws  Exception{
		 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		 driver = buildDriver(cf);

 
		 ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
			locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
			PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
			locToAdd.setPlNumAcres(11);
			locationsList.add(locToAdd);
			
			SquireLiability myLiab = new SquireLiability();
			myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

      SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
      myPropertyAndLiability.locationList = locationsList;
      myPropertyAndLiability.liabilitySection = myLiab;


      Squire mySquire = new Squire(SquireEligibility.Country);
      mySquire.propertyAndLiability = myPropertyAndLiability;

      mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withInsFirstLastName("DE5160", "x")
					.withPaymentPlanType(PaymentPlanType.Quarterly)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			
	}
  
  
	@Test (dependsOnMethods = { "generatePolicy" }) 
	public void makeInsuredDownPayment() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySQPolicyObjPL, mySQPolicyObjPL.squire.getPremium().getDownPaymentAmount(), mySQPolicyObjPL.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}
  
	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void makeFirstInvoiceDue() throws Exception {
				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);
		int xx = 95;
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, xx);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		
		Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null,startDatex ), "Delinquency Expected");
		
		new GuidewireHelpers(driver).logout();
	}
  
	
	@Test (dependsOnMethods = { "makeFirstInvoiceDue" })
	public void negetivePolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
		//loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), "01-271386-03");		
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changePLPropertyCoverage(70000, currentSystemDate);
		new GuidewireHelpers(driver).logout();
	}
 
	@Test (dependsOnMethods = { "negetivePolicyChange" })
    public void verifyShortageInvoiceAfterPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), mySQPolicyObjPL.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		Date today = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        AccountInvoices myInvoices = new AccountInvoices(driver);
		
		try{
			WebElement myRow = myInvoices.getAccountInvoiceTableRow(today, today, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, null);
			String ShortageAmount = new TableUtils(driver).getCellTextInTableByRowAndColumnName(myInvoices.getAccountInvoicesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(myRow), "Amount");
			System.out.println("On negetive Policy change Shortage Invoice Successfully created as expected with shortage amount :- " + ShortageAmount);
		}catch (Exception e) {

			Assert.fail("Shortage Invoice Not created as expected");
		}
		
	}
		
}
