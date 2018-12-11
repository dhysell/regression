package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
 * @Author JQU
 * @Requirement 	DE6963 -- Created scheduled invoice instead of shortage when changing payment plan from monthly
 * 				Create a monthly policy, pay down with CC in PC
				Move clock 20 days run invoice, invoice due
				Change payment plan from monthly to annual
				Created a new shortage invoice
				Move clock to bill date and run invoice
				Actual: Invoice went bill 9/22, no document produced to send to insured.
				Expected: Should have received shortage invoice document for invoice billed 9/22
 * 				
 * @RequirementsLink 
 * @DATE May 3rd, 2018
 */
@Test(groups = {"ClockMove"})
public class DE6963DocumentAfterPaymentPlanChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();

	private void quotePolicy() throws Exception {
		//		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		//		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();				

		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);		

		locOnePropertyList.add(loc1Bldg1);			
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Doc", "Test")
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);				
	}
	@Test
	public void changePaymentPlanAndVerify() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		//		try {
		quotePolicy();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//			Assert.fail("Unable to generate policy. Test cannot continue.");
		//		}

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));


		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
		//wait for trouble ticket to come
		BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickBCMenuTroubleTickets();
		BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.waitForTroubleTicketsToArrive(90);
		bcPolicyMenu.clickTopInfoBarAccountNumber();

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		List<Date> dueDatesList = invoice.getListOfDueDates();
		//pay down payment
		BatchHelpers batchHelpers = new BatchHelpers(driver);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
		//move clock and change payment plan type
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 20);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);

		accountMenu.clickAccountMenuPolicies();
		AccountPolicies accountPolicy = new AccountPolicies(driver);
		accountPolicy.clickPolicyNumberInPolicyTableRow(myPolicyObj.accountNumber, null, myPolicyObj.squire.getEffectiveDate(), null, null, null, null, null, null, null);

		bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickPaymentSchedule();
		PolicyPaymentSchedule paymentSchedule = new PolicyPaymentSchedule(driver);
		paymentSchedule.changePaymentPlan(PaymentPlanType.Annual);

		//verify the shortage invoice
		bcPolicyMenu.clickTopInfoBarAccountNumber();
		accountMenu.clickAccountMenuInvoices();
		ClockUtils.setCurrentDates(driver, dueDatesList.get(1));
		for(int i =0; i<3; i++){
			batchHelpers.runBatchProcess(BatchProcess.Payment_Request);
		}

		Date shortageInvDate = DateUtils.dateAddSubtract(dueDatesList.get(2), DateAddSubtractOptions.Day, PaymentPlanType.Monthly.getInvoicingLeadTime());
		assertTrue(invoice.verifyInvoice(null, dueDatesList.get(2), null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, null), "didn't find the correct invoice");

		ClockUtils.setCurrentDates(driver, shortageInvDate);
		batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);

		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments document = new BCCommonDocuments(driver);
		assertTrue(document.verifyDocument("Shortage Bill", null, null, null, shortageInvDate, shortageInvDate), "Didn't find the expected document.");
	}
}

