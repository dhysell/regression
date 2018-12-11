package currentProgramIncrement.nonFeatures.Triton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	US16129 -- Return reasons should not trigger bank fee or change payment restriction.				
				Acceptance criteria:
					Ensure that when the below return reasons are selected the return fee is not charged. (Req 12-13-04)
					Ensure that when the below return reasons are selected the payment restriction is not changed. (Req 12-13-03)
					Steps to get there:
						Make a payment 
						Reverse payment
						Make another payment so the payment restriction is on one
						Return the payment with a non-fault reason
						Make sure the return check fee is not charged
						Make sure the payment restriction remains on the same status
					
* @DATE 	November 15th, 2018
* 
* */
public class US16129ReturnReasonShouldNotTriggerBankFeeOrChangePaymentRestriction extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private double payment1 = NumberUtils.generateRandomNumberInt(20, 100);
	private double payment2 = NumberUtils.generateRandomNumberInt(101, 200);
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
	public void verifyPaymentInstrument() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();
		
		List<String> newReturnReasons = new ArrayList<String>();	
//		newReturnReasons = Arrays.asList("AccountFrozen", "InvalidAccount", "NoAccountUnableToLocateAccount", "UnauthorizedDebit", "UncollectedFunds", "CustomerAdvisesNotAuthorized", 
//				"CheckTruncationEntryReturnStaleDate", "BranchSoldToAnotherDFI", "AccountHolderDeceased", "BeneficiaryDeceased", "NonTransactionAccount", "CorporateCustomerAdvisesNotAuthorized", "InvalidRoutingNumber");
//		
		//put "UnauthorizedDebit", "UncollectedFunds" back when DE8245 is fixed
		newReturnReasons = Arrays.asList("AccountFrozen", "InvalidAccount", "NoAccountUnableToLocateAccount", "CheckTruncationEntryReturnStaleDate", "BranchSoldToAnotherDFI", "AccountHolderDeceased", "BeneficiaryDeceased", "NonTransactionAccount", "InvalidRoutingNumber");
				
		List<String> nonFaultReversalReasons = new ArrayList<String>();
		nonFaultReversalReasons = Arrays.asList("Associated_Disbursement_Reversed_or_Void","Payment_Modification","Payment_Moved", "Processing_Error_Went_To_Bank");
		
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);        
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		//make payment then reverse it with one of the new return reason (at fault)
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payment1, myPolicyObj.accountNumber);		
		accountMenu.clickAccountMenuPaymentsPayments();
		AccountPayments payment = new AccountPayments(driver);		
		payment.reversePaymentAtFault(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), payment1, 0.0, payment1, PaymentReturnedPaymentReason.valueOf(newReturnReasons.get(NumberUtils.generateRandomNumberInt(1, newReturnReasons.size()-1))));
		
		//make second payment and reverse it with non-fault reason		
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();		
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(payment2, myPolicyObj.accountNumber);
		
		payment.reversePayment(payment2, 0.0, payment2, PaymentReversalReason.valueOf(nonFaultReversalReasons.get(NumberUtils.generateRandomNumberInt(0, nonFaultReversalReasons.size()-1))));		
	}
	//verify that Reversal fee is not charged, restriction is not changed
	@Test(dependsOnMethods = {"verifyPaymentInstrument"})
	public void verifyReversalFeeAndPaymentRestriction() throws Exception {	
		
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
    	new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
    	BCAccountMenu accountMenu = new BCAccountMenu(driver);
    	//reversal fee should not be charged
    	accountMenu.clickBCMenuCharges();
    	AccountCharges charge = new AccountCharges(driver);
    	try{
    		charge.getChargesOrChargeHoldsPopupTableRow(null, null, null, ChargeCategory.Policy_Payment_Reversal_Fee, null, null, null, null, null, null, null, null, null, null, null, null);
    		Assert.fail("Payment Reversal Fee for account "+myPolicyObj.accountNumber+" is charged which is not expected.");
    	}catch (Exception e) {
			System.out.println("Payment Reversal Fee is not charged which is expected");
		}    	
    	//restriction should not change
    	charge.clickPolicyNumberInChargesTableRow(null, myPolicyObj.accountNumber, null, ChargeCategory.Premium, null, null, null, null, null, null, null, null, null, null, null, null);
    	
    	BCPolicySummary bcPolicySummary = new BCPolicySummary(driver);
    	String tmp = bcPolicySummary.getPaymentRestrictionValue().getValue();
    	System.out.println("Pmt Restriction is "+ tmp);
    	if(!bcPolicySummary.getPaymentRestrictionValue().getValue().equals("None")){
    		Assert.fail("Payment Restriction is changed to  "+ bcPolicySummary.getPaymentRestrictionValue() +"  which is not expected.");
    	}        
	}
}
