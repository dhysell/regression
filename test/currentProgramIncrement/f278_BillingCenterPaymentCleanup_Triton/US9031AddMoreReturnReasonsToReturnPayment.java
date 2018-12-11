package currentProgramIncrement.f278_BillingCenterPaymentCleanup_Triton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	US9031 -- Add more return reasons in the options for putting on a return payment
* 				Acceptance criteria:
					Ensure that the new reasons are shown in the drop down on the "Confirm Payment Reversal" (see attachment) (Req 12-13-01)
					Ensure that the new reasons are printing on the NSF cancellation document. (Req 12-13-02)
					Ensure reasons are also added to PolicyCenter drop down.
					Ensure that cancellation started in PolicyCenter also shows the correct reason on document and the cancellation goes to BillingCenter. 
				Steps to get there:
					Go to payments
					Click on the action button on the payment you are wanting to reverse
					Click Reverse
					Click Return Payment on the drop down menu
					Choose the return reason from the drop down menu.
					Make sure it prints on the cancellation
					Notes:
					Below is the additional reasons:
					No account/ unable to locate account 
					Unauthorized Debit 
					Uncollected funds
					Customer advises not authorized 
					Check truncation entry return/ stale date 
					Branch sold to another DFI
					Account-holder deceased 
					Beneficiary deceased
					Non-transaction account
					Corporate customer advises not authorized 
					Invalid routing number 
* @DATE 	November 15th, 2018
* 
* */
@Test(groups = {"ClockMove"})
public class US9031AddMoreReturnReasonsToReturnPayment extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private double payment = NumberUtils.generateRandomNumberInt(20, 200);
	private List<String> newReturnReasons = new ArrayList<String>();
	private void generatePolicy() throws Exception {		

		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();

		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
		// Farm Equipment
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
				"Manly Farm Equipment", 100000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

	
        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Return", "Reasons")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);       
	}
	@Test
	public void verifyNewReturnPaymentReasons() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();	
		newReturnReasons = Arrays.asList("AccountFrozen", "InvalidAccount", "NoAccountUnableToLocateAccount", "UnauthorizedDebit", "UncollectedFunds", "CustomerAdvisesNotAuthorized", 
				"CheckTruncationEntryReturnStaleDate", "BranchSoldToAnotherDFI", "AccountHolderDeceased", "BeneficiaryDeceased", "NonTransactionAccount", "CorporateCustomerAdvisesNotAuthorized", "InvalidRoutingNumber");
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		//make payment then reverse it with one of the new return reason (at fault)
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payment, myPolicyObj.accountNumber);		
		accountMenu.clickAccountMenuPaymentsPayments();
		AccountPayments payment = new AccountPayments(driver);		
		payment.clickActionsReverseLink(1);
		payment.setPaymentReversalReason(PaymentReversalReason.Return_Payment);
		for(String returnReason: newReturnReasons){
			try{
				payment.setPaymentReversalReasonAtFault(PaymentReturnedPaymentReason.valueOf(returnReason));
			}catch (Exception e) {
				e.printStackTrace();	           
				Assert.fail("Couldn't find the following return reason: "+ returnReason + " in Return Reason Dropdown.");
			}
		}
	}	
	@Test(dependsOnMethods = {"verifyNewReturnPaymentReasons"})
	public void verifyReturnReasonInPolicyCenter() throws Exception {	
		
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
    	new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
    	ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
    	StartCancellation cancellation = new StartCancellation(driver);
    	
    	CancellationSourceReasonExplanation randomReturnReason = CancellationSourceReasonExplanation.valueOf(newReturnReasons.get(NumberUtils.generateRandomNumberInt(0, newReturnReasons.size()-1)));
    	
    	cancellation.cancelPolicy(randomReturnReason, "Cancellation as NSF", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), true, 100);
		GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
	    complete.clickViewYourPolicy();
	    PolicySummary policySummary = new PolicySummary(driver);
	    policySummary.clickCompletedTransactionByType(TransactionType.Cancellation);
	    if(!cancellation.getCancellationReasonExplanationValue().equals(randomReturnReason.getExplanationValue())){
	    	Assert.fail("The NSF Cancellation Explanation is :" + randomReturnReason.getExplanationValue()+", but it doesn't display on Cancellaton Entry Screen.");
	    }
	}
}
