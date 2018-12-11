package regression.r2.clock.billingcenter.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Status;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class TestPaymentRequests extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	private String arUsername;
	private String arPassword;
	private GeneratePolicy squirePolicyObj = null;
	@SuppressWarnings("unused")
	private GeneratePolicy standardFirePolicyObj = null;
	private Date invoiceDate;	
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private AccountPaymentsPaymentRequests payRequest;
	private DesktopActionsMultiplePayment makePayment;
	private BCDesktopMenu desktopMenu;
	private BCTopMenu topMenu;
	
	@Test ()
	public void createPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// GENERATE POLICY
        squirePolicyObj = new GeneratePolicy.Builder(driver)
			.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
			.withCreateNew(CreateNew.Create_New_Always)
			.withInsPersonOrCompany(ContactSubType.Person)
			.withInsFirstLastName("Squire", "Test")
			.withInsAge(26)
			.withPolOrgType(OrganizationType.Individual)
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Check)
			.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test (enabled = false, 
			dependsOnMethods = {"createPolicy"})
	public void generateStandardFire() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        standardFirePolicyObj = new GeneratePolicy.Builder(driver)
				.withAgent(squirePolicyObj.agentInfo)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withSquireEligibility(SquireEligibility.Country)
				.withCreateNew(CreateNew.Do_Not_Create_New)
				.withInsPersonOrCompany(squirePolicyObj.pniContact.getPersonOrCompany())
				.withInsFirstLastName(squirePolicyObj.pniContact.getFirstName(), squirePolicyObj.pniContact.getLastName())
				.withInsAge(26)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(squirePolicyObj.paymentPlanType)
				.withDownPaymentType(squirePolicyObj.downPaymentType)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(description = "Makes Insured Payment on account",
			dependsOnMethods = { "createPolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		arUsername = arUser.getUserName();
		arPassword = arUser.getPassword();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUsername, arPassword);

        makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(squirePolicyObj, squirePolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test (dependsOnMethods = {"makeInsuredDownPayment"})
	public void verifyPaymentRequests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, squirePolicyObj.accountNumber);

		//Get date of first Invoice Date
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		invoiceDate = acctInvoice.getListOfInvoiceDates().get(1);
		
		//Move to first Payment Request date.
		ClockUtils.setCurrentDates(cf, invoiceDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		//Check that Payment Request is Open
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		payRequest = new AccountPaymentsPaymentRequests(driver);
		payRequest.checkPaymentRequestStatus(invoiceDate, null, null, null, Status.Created);
		
		
	}


	/**
	* @author bmartin
	 * @throws Exception 
	* @requirement 12-02 Payment Requests: Requirements 12-02-11
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-02%20Payment%20Requests.docx">12-02 Payment Requests</a>
	* @Description Should be able to edit Payment Request Amount 
	* @DATE April 3, 2017
     */
	@Test (enabled = true, 
			dependsOnMethods = {"verifyPaymentRequests"})
	public void editPaymentRequestAmount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, squirePolicyObj.accountNumber);

		//Get date of first Invoice Date
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		invoiceDate = acctInvoice.getListOfInvoiceDates().get(1);
		
		//Check that Payment Request is Open
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		payRequest = new AccountPaymentsPaymentRequests(driver);
		payRequest.editPaymentAmount(invoiceDate, null, null, null, "100.00");
	}

	
	/**
	* @Author bmartin
	* @Requirement 12-02 Payment Requests: Requirements 12-02-11
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-02%20Payment%20Requests.docx">12-02 Payment Requests: Requirements 12-02-11</a>
	* @Description Payment Request should close when total due is below $10. 
	* @DATE Mar 2, 2017
	* @throws Exception
	*/
	@Test (enabled = true, 
			dependsOnMethods = {"verifyPaymentRequests"})
	public void verifyPaymentRequestCloses() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, squirePolicyObj.accountNumber);

		//Reduce payment owed below $10.00
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		payRequest = new AccountPaymentsPaymentRequests(driver);
		double paymentRequestAmount = payRequest.getPaymentRequestAmount(Status.Created, invoiceDate, null, null);
		double truncatedPaymentAmount = paymentRequestAmount - 9.99;
		Double paymentAmount = BigDecimal.valueOf(truncatedPaymentAmount).setScale(2, RoundingMode.UP).doubleValue();
		
		//Make payment on policy
		topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();
        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
        makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeMultiplePayment(squirePolicyObj.squire.getPolicyNumber(), PaymentInstrumentEnum.Cash, paymentAmount);
		
		//check that Payment Request is now Closed
        BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
		accountSearchBC.searchAccountByAccountNumber(squirePolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsPaymentRequests();
		payRequest = new AccountPaymentsPaymentRequests(driver);
		payRequest.checkPaymentRequestStatus(invoiceDate, null, null, null, Status.Created);
	}

}
