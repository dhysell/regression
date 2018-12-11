package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Triton;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonHistory;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	DE8076 -- ** Hotfix ** Null pointer exception when reversing a payment at the account level
* @Requirements 
* 				Acceptance criteria:
					Ensure that reversing a payment applied to default unapplied a Null Pointer is not triggered
					Ensure that the cash ledger receives a debit and credit transaction
				Steps to get there:
					Make a payment from direct bill pay to default unapplied
					Reverse payment as processing error reason
					Received Null pointer
				Actual: When reversing payment applied to default unapplied a Null Pointer error is triggered
				Expected: When reversing payment that was applied to default unapplied reversal should go through and hit intran correctly. Debit/Crediting the cash ledger
* @DATE October 19, 2018*/
public class DE8067NullPointerExceptionWhenReversingPaymentAtAccountLevel extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	private WebDriver driver;
	private Config cf;
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
                .withInsFirstLastName("Payment", "Reversal")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);        
	}
	@Test
	public void makePaymentToDefaultAndReverse() throws Exception{
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);	
		generatePolicy();

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.makeDirectBillPaymentExecuteWithoutDistribution(myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount());
		//verify payment and reverse it
		accountMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		assertTrue(payment.verifyPaymentAndClick(null, null, null, null, null, null, null, null, null, "Default", myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount(), null, null), 
				"Didn't find the expected payment for Default for account "+ myPolicyObj.accountNumber);
		
		payment.reversePaymentByRandomReason(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount(), null, null);
		//verify Payment reversal
		assertTrue(payment.verifyPaymentAndClick(null, null, null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, null, "Default", myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount(), null, null), 
				"Payment is not successfully converted for account "+ myPolicyObj.accountNumber);	
		//verify history
		String historyItem = "Payment reversed from this account";
		accountMenu.clickBCMenuHistory();
		BCCommonHistory history = new BCCommonHistory(driver);
		assertTrue(history.verifyHistoryTable(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), historyItem), 
				"Didn't find reversal history for account "+ myPolicyObj.accountNumber);
		
	}
}
