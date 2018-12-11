package previousProgramIncrement.pi3_090518_111518.f276_BC_Document_Cleanup;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author JQU
* * @Requirement 	US16073	-- BC CancellationAPI needs to include a description for "Other" reason
* Acceptance criteria:
		Ensure that when "Other" is chosen as a return check reason BC is sending over the description that show in the UI "Did not honor - please contact bank for details"
* Steps: 					
* 		Create a policy, submit and issue
					Make the down payment, run invoice and invoice due batches
					Move clock to one day past the invoice due date and make sure the invoice and moved to a "due" status
					Return the payment for "Return Payment - Other:Did not honor - please contact bank for details"
					Make sure the cancellation job in PolicyCenter has a Reason, Reason Explanation, and Reason Description are all filled out.
					Make sure the cancellation document roundtrips. You will need to mark it proof of mail and mail it in the UI. 
					Go to PolicyCenter and look for the document "NSF Cancel".		
					
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Requirements/Payments/Supporting%20Work%20Items/Return%20Check.docx&action=default">Return Check</a>
* @DATE September 10, 2018*/
@Test(groups = {"ClockMove"})
public class US16073BCCancellationAPISendPCDescriptionForOtherReason extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	
	private void generatePolicy() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();			
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();		


        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Reversal", "Reason")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test
	public void makePaymentAndReverseIt() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();	
		driver.quit();
		
		
		cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

      
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for TT to come
		BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickBCMenuTroubleTickets();
		BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.waitForTroubleTicketsToArrive(60);
		bcPolicyMenu.clickTopInfoBarAccountNumber();
		
		//pay insured and reverse the payment
		BatchHelpers batchHelper = new BatchHelpers(driver);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
		//reversal the payment with Other reason
		accountMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		payment.reversePaymentAtFault(null, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPremium().getDownPaymentAmount(), 0.0, PaymentReturnedPaymentReason.OtherDidNotHonor);
	}
	@Test(dependsOnMethods = {"makePaymentAndReverseIt"})
	public void verifyReversalReasonInPolicyCenter() throws Exception {	
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());

		PolicySummary policycenterPolicySummary = new PolicySummary(driver);
		policycenterPolicySummary.clickPendingTransactionNumberByText(policycenterPolicySummary.getTransactionNumber(TransactionType.Cancellation));
		StartCancellation cancellation = new StartCancellation(driver);			
		
		if(!(cancellation.getSourceValue()==Cancellation.CancellationSource.Carrier)){
			Assert.fail("Cancellation Source is incorrect. It should be "+ Cancellation.CancellationSource.Carrier.toString() +", but it's "+ cancellation.getSourceValue() + " on UI.");
		}
		if(!cancellation.getCancellationReasonValue().equals(Cancellation.CancellationSourceReasonPL.NSF.toString())){
			Assert.fail("Cancellation Reason is incorrect. It should be "+ Cancellation.CancellationSourceReasonPL.NSF.toString() +", but it's "+ cancellation.getCancellationReasonValue() + " on UI.");
		}
		
		if(!cancellation.getCancellationReasonExplanationValue().equals(Cancellation.PendingCancellationSourceReasonExplanation.OtherNSF.getExplanationValue())){
			Assert.fail("Cancellation Reason Explanation is incorrect. It should be "+ Cancellation.PendingCancellationSourceReasonExplanation.OtherNSF.getExplanationValue() +", but it's "+ cancellation.getCancellationReasonExplanationValue() + " on UI.");
		}
		
		if(!PaymentReturnedPaymentReason.OtherDidNotHonor.getValue().contains(cancellation.getCancellationReasonDescriptionValue())){
			Assert.fail("Cancellation Reason Description is incorrect. It should be "+ PaymentReturnedPaymentReason.OtherDidNotHonor.getValue() +", but it's "+ cancellation.getCancellationReasonDescriptionValue() + " on UI.");
		}		
	}
}
