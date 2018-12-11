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
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
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
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
public class InvoiceRollUp extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	private BCAccountMenu acctMenu;
	Underwriters uw;
	GeneratePolicy myPolicyObj;
	Date cancellationDate = null;
	
	/**
	* @Author sgunda
	* @Requirement : DE4557 Cancellation of renewal term creates credit and rollup invoice on wrong date
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-08%20Cancellation%20Installment%20Scheduling.docx">Link Text</a>
	* @Description :	The invoice date is "today" instead of policy (or charge) effective date.
	*					=======================
	*					Renewal / Reinstate -- Invoice Roll-up on Cancellation of Renewal Term
	*					Scenario is as follows:
	*					1 -- BIND a policy
	*					2 -- Get to day -80 and renewal job starts
	*					3 -- Get to day -50 and charges send to BillingCenter
	*					4 -- Get to day -21 and run invoice
	*					5 -- Get to day -15 and cancel the current term (thus cancelling the future scheduled term as well) (really you just have to be any time after day 50 when the renewal becomes a policy term)
	*					Action Items: Roll up renewal correctly upon cancellation
	*					US9967 was filed in the integrations project. 
	* @DATE Mar 3, 2017
	*/
	
	@Test
	public void issueSquirePolicy() throws Exception {
		
		// Issuing policy for 50 days
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		
		locToAdd.setPlNumAcres(10);
		locationsList.add(locToAdd);
		
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("RollUp", "Invoice")
				.withPolTermLengthDays(50)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "issueSquirePolicy" })
	public void downPayment() throws Exception {	 		 
		//Down Payment
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
				
	}
	
	@Test(dependsOnMethods = { "downPayment" })
	public void manualRenewal() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartRenewal manualRenewal = new StartRenewal(driver);
		manualRenewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);	
	}
	
	
	@Test(dependsOnMethods = { "manualRenewal" })
    public void moveClocksRunBatchInBC() throws Exception {
		//For making invoices Planned to billed
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 29);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
	}
	
	@Test(dependsOnMethods = { "moveClocksRunBatchInBC" })
    public void moveClockAndCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		// Moving clock for cancellation 
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 6);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancelling Policy", null, true);
		
		cancellationDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
	}

	@Test (dependsOnMethods = { "moveClockAndCancelPolicy" })
    public void verifyInvoices() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		//verify invoices on billing center		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(),myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices tableData = new AccountInvoices(driver);
		WebElement myInvoiceTable= tableData.getAccountInvoicesTable();
		TableUtils tableUtils = new TableUtils(driver);
        WebElement lastRowOfInvoiceTable = tableUtils.getRowInTableByColumnNameAndValue(myInvoiceTable, "Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObj.squire.getExpirationDate()));
		if (lastRowOfInvoiceTable != null) {
			String invoiceType = tableUtils.getCellTextInTableByRowAndColumnName(myInvoiceTable,
					tableUtils.getRowNumberFromWebElementRow(lastRowOfInvoiceTable), "Invoice Type");
			String invoiceDate = tableUtils.getCellTextInTableByRowAndColumnName(myInvoiceTable,
					tableUtils.getRowNumberFromWebElementRow(lastRowOfInvoiceTable), "Invoice Date");
			System.out.println("InvoiceDate------->" + invoiceDate + "InvoiceType------->" + invoiceType);
			Assert.assertEquals(invoiceType, "Shortage", "Invoice Type  is Not equal to Shortage");
            Assert.assertEquals(invoiceDate, DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObj.squire.getExpirationDate()),
					"Invoice Date is Not equal to Expiry Date");
		}
		else {
			Assert.fail(" Row not found ");
		}
		
	}
		
}