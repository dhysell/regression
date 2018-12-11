package currentProgramIncrement.f273_ActivitiesCleanUp_Triton;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonActivities;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
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
* @Requirement 	DE6015 -- Recapture activity due date is incorrect
				Acceptance criteria:
					Ensure that the activity due date is 30 days from the creation date and that it escalates at 31 days from the creation date (Req 02-01-03)
					Steps to get there:
					Create a Standard Auto Policy 
					Pay whole policy on multiple payment screen
					Move clock a couple of days- run invoice and invoice due
					Make another payment for the same amount as previous amount, this makes an overpayment sitting in unapplied funds
					Create a disbursement for the excess money
					Run "disbursement batch" disbursement should be sent
					Move clock another day or 2, run invoice and invoice due, and bounce one of the payments that was made.
					This should give you an activity for "recapture" 
				Actual: The activity has a due date of 7 days from creation and it escalates in 14 days from creation.
				Expected: According to the requirements "Recapture Activity",  " 02-01-03" should have a due date of 30 days from creation date and escalates in 31 days from creation.
					
* @DATE 	December 4th, 2018
* 
* */
@Test(groups={"ClockMove", "BillingCenter"})
public class DE6015RecaptureActivityDueDateVerification extends BaseTest{
	
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	
	private ARUsers arUser = new ARUsers();
	private String subject = "Recapture";
	
    
    private void generatePolicy() throws Exception {		

    	SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .isDraft()
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("Recapture", "Date")
                .build(GeneratePolicyType.PolicyIssued);
	}
	@Test
	public void createDisbursement() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial); 
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BatchHelpers batchHelper = new BatchHelpers(driver);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPremium().getTotalCostToInsured(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 2);
		
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
		//make another payment to disburse
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();		
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(myPolicyObj.squire.getPremium().getTotalCostToInsured(), myPolicyObj.accountNumber);
		batchHelper.runBatchProcess(BatchProcess.Automatic_Disbursement);
		//verify disbursement is created and change approve date to current date
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		assertTrue(disbursement.verifyDisbursements(myPolicyObj.accountNumber, DisbursementStatus.Awaiting_Approval, myPolicyObj.squire.getPremium().getTotalCostToInsured()), "Didn't find the disbursement for "+myPolicyObj.squire.getPolicyNumber());
		disbursement.clickEdit();
		disbursement.setDisbursementsDueDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
		disbursement.clickAccountDisbursementsApprove();
		
		batchHelper.runBatchProcess(BatchProcess.Disbursement);
		accountMenu.clickBCMenuSummary();
		accountMenu.clickAccountMenuDisbursements();
		assertTrue(disbursement.verifyDisbursements(myPolicyObj.accountNumber, DisbursementStatus.Sent, myPolicyObj.squire.getPremium().getTotalCostToInsured()), "The disbursement for "+myPolicyObj.squire.getPolicyNumber() + " should be sent but it was not.");
		//reverse disbursed payment
		accountMenu.clickAccountMenuPaymentsPayments();
		AccountPayments payment = new AccountPayments(driver);
		payment.reversePaymentAtFault(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, null, PaymentReturnedPaymentReason.InvalidAccount);
	}
	
	@Test(dependsOnMethods = {"createDisbursement"})
	public void verifyRecapture() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuActivities();
		BCCommonActivities activity = new BCCommonActivities(driver);
		assertTrue(activity.verifyActivityTable(null, null, subject), "Didn't find the Recapture for account "+ myPolicyObj.accountNumber);
		activity.clickActivityTableSubject(null, null, null, null, subject);
		//verify Recapture Due and Escalation dates
		//Due date is 30 days from creation date and Escalation is 31 days
		
		System.out.println("Due date should be "+DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 30)));
		System.out.println("Due date from UI is "+ DateUtils.dateFormatAsString("MM/dd/yyyy", activity.getDueDate()));
		
		if(!DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 30)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", activity.getDueDate()))){
			Assert.fail("Recapture Activity Due date should be 30 days from Creation date.");
		}
		if(!DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 31)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", activity.getEscalationDate()))){
			Assert.fail("Recapture Activit Escalation date should be 31 days from Creation date.");
		}		
	}
}
