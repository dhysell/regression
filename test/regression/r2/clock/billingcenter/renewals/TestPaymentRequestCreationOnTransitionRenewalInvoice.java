package regression.r2.clock.billingcenter.renewals;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.driverConfiguration.Config;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
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
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sgunda
* @Requirement DE5245  Payment request not created on a transition renewal
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-02%20Payment%20Requests.docx">Link Text</a>
* @Description 	Bind a transition renewal in PC that is a monthly payment plan 
* 				Get new renewal charges in BC at bind (-50 day)
* 				Move clock to day -21 run invoice and invoice due 
* 				Payment request should have been created 
* @DATE Apr 11, 2017
*/
public class TestPaymentRequestCreationOnTransitionRenewalInvoice extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	private BCAccountMenu acctMenu;
	private Date invoiceDate2;
	private AccountInvoices acctInvoice;
	private AccountPaymentsPaymentRequests acctPaymentReq ;
	GeneratePolicy myPolicyObj;	
	
	@Test
	public void issueSquirePolicy() throws Exception {
		
		// Issuing policy for 30 days
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		
		locToAdd.setPlNumAcres(35);
		locationsList.add(locToAdd);
		
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("DE5245-PayReq", "Renewal")
				.withPolTermLengthDays(30)
				.withPaymentPlanType(repository.gw.enums.PaymentPlanType.Monthly)
				.withDownPaymentType(repository.gw.enums.PaymentType.ACH_EFT)
				.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "issueSquirePolicy" })
	public void downPayment() throws Exception {	 		 
		
		//Down Payment
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
		double dueAmt= acctInvoice.getInvoiceAmountByRowNumber(1);
		
		if(dueAmt !=0.0 ){
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();

            NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(dueAmt, myPolicyObj.accountNumber);
		}
		else{
				System.out.println("Payment already made by PC ");
		}
	}
	
		
	@Test(dependsOnMethods = { "downPayment" })
	public void manualRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
			summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

			boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
			if (preRenewalDirectionExists) {
				preRenewalPage.clickViewInPreRenewalDirection();
				preRenewalPage.closeAllPreRenewalDirectionExplanations();
				preRenewalPage.clickClosePreRenewalDirection();
				preRenewalPage.clickReturnToSummaryPage();
            }
        }
        StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(repository.gw.enums.RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}

	
	@Test (dependsOnMethods = { "manualRenewal" })
	public void moveClockForNxtInvoiceDate() throws Exception {	 		 
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
		invoiceDate2 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfInvoiceDates().get(1), "MM/dd/yyyy");
		
		ClockUtils.setCurrentDates(cf, invoiceDate2);
		
	}
	
	@Test(dependsOnMethods = { "moveClockForNxtInvoiceDate" })
	public void chechPaymentRequest() throws Exception {	 		 
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.accountNumber);
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		
		acctPaymentReq = new AccountPaymentsPaymentRequests(driver);
		acctPaymentReq.checkPaymentRequestStatus(invoiceDate2, null, null, null, repository.gw.enums.Status.Created);

	}

}
