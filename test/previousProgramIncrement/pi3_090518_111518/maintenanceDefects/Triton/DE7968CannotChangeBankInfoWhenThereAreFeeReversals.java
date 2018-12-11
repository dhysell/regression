package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Triton;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.wizards.ChargeReversalWizard;
import repository.bc.wizards.NewPaymentInstrumentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BankAccountType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment;
import repository.gw.enums.InlandMarineTypes;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentScheduleItemsToProcess;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.RoutingNumbersHelper;

/**
 * @Author JQU
 * @Description  DE7968 -- **HOTFIX** Cannot change bank info when there are fee reversals
 * 							Start with non-monthly payment plan
							Pay down and make it go bill and due
							Create a payment reversal fee or make sure there is a Dues fee
							Reverse the fee
							Change the payment plan to monthly
							Change the payment instrument non-responsive
							Actual: User get error and cannot change payment instrument
							Expected: User should be able to change payment instrument

 * @RequirementsLink <a href=""> </a>
 * @DATE September 27, 2018
 */
public class DE7968CannotChangeBankInfoWhenThereAreFeeReversals extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private BatchHelpers batchHelper;
	private double downPayment;
	private double paymentReversalFee =20.0;
	private void generatePolicy() throws Exception {

		 ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarineTypes.InlandMarine>();

         inlandMarineCoverageSelection_PL_IM.add(InlandMarineTypes.InlandMarine.FarmEquipment);
         IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler","Manly Farm Equipment", 30000);
         ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
         farmEquip.add(scheduledItem1);
         FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipment.IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,IMFarmEquipment.IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
         ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
         allFarmEquip.add(imFarmEquip1);

         StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
         myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
         myStandardInlandMarine.farmEquipment = allFarmEquip;        
         
         myPolicyObj = new GeneratePolicy.Builder(driver)
                 .withProductType(ProductLineType.StandardIM)
                 .withLineSelection(LineSelection.StandardInlandMarine)
                 .withStandardInlandMarine(myStandardInlandMarine)
                 .withCreateNew(CreateNew.Create_New_Always)
                 .withInsPersonOrCompany(ContactSubType.Person)
                 .withInsFirstLastName("Payment", "Instrument")
                 .withPolOrgType(OrganizationType.Individual)
                 .withPaymentPlanType(PaymentPlanType.Quarterly)
                 .withDownPaymentType(PaymentType.Cash)
                 .build(GeneratePolicyType.PolicyIssued); 
         
         downPayment = myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount();
	}
	@Test
	public void triggerReversalFeeAndReverseIt() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
 		driver = buildDriver(cf);
		generatePolicy();		
		driver.quit();
		
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		//wait for TT to come
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickBCMenuTroubleTickets();
		BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.waitForTroubleTicketsToArrive(60000);
		
		bcPolicyMenu.clickTopInfoBarAccountNumber();		
		batchHelper = new BatchHelpers(cf);
		batchHelper.runBatchProcess(BatchProcess.Invoice);

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		
		//make payment and reverse the payment, do this twice to trigger Reversal Fee
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		
		directPayment.makeDirectBillPaymentExecute(downPayment, myPolicyObj.accountNumber);
		accountMenu.clickAccountMenuPaymentsPayments();
		AccountPayments payment = new AccountPayments(driver);
		
		payment.reversePaymentAtFault(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), downPayment, downPayment, 0.0, PaymentReturnedPaymentReason.InvalidAccount);
		
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		directPayment = new NewDirectBillPayment(driver);
		
		directPayment.makeDirectBillPaymentExecute(downPayment, myPolicyObj.accountNumber);
		accountMenu.clickAccountMenuPaymentsPayments();
		payment = new AccountPayments(driver);
		
		payment.reversePaymentAtFault(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), downPayment, downPayment, 0.0, PaymentReturnedPaymentReason.InvalidAccount);
		
		//verify the Reversal Fee
		accountMenu.clickBCMenuCharges();
		BCCommonCharges charge = new BCCommonCharges(driver);
		charge.clickChargeRow(null, null, null, ChargeCategory.Policy_Payment_Reversal_Fee, null, null, null, null, null, paymentReversalFee, null, null, null, null, null, null);
		//reversal Payment Reversal Fee
		accountMenu.clickAccountMenuActionsNewTransactionChargeReversal();
		ChargeReversalWizard chargeReversal = new ChargeReversalWizard(driver);
		chargeReversal.clickSearch();
		chargeReversal.clickSelectButtonInChargeReversalTable(null, ChargeCategory.Policy_Payment_Reversal_Fee.getValue(), null, paymentReversalFee);
		chargeReversal.clickFinish();
		//verify -20 reversal amount
		accountMenu.clickBCMenuCharges();
		charge.clickChargeRow(null, null, null, ChargeCategory.Policy_Payment_Reversal_Fee, null, null, null, null, null, paymentReversalFee*(-1), null, null, null, null, null, null);
		
				
	}
	@Test(dependsOnMethods = {"triggerReversalFeeAndReverseIt"})
	public void changePaymentPlanType() throws Exception {			
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
		bcPolicyMenu.clickPaymentSchedule();
		PolicyPaymentSchedule schedule = new PolicyPaymentSchedule(driver);
		schedule.clickEditSchedule();
		schedule.setRadioItemsToProcess(PaymentScheduleItemsToProcess.All_Items);
		schedule.selectNewPaymentPlan(PaymentPlanType.Monthly);
		schedule.clickExecute();
	}
	@Test(dependsOnMethods = {"changePaymentPlanType"})
	public void changePaymentInstrument() throws Exception {	
		String newRoutingNumber = RoutingNumbersHelper.getRoutingNumber_Random().getRoutingNumber();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCPolicySummary policySummary = new BCPolicySummary(driver);
		policySummary.clickEdit();
		policySummary.clickNewPaymentInstrument();
		NewPaymentInstrumentWizard instrumentWizard = new NewPaymentInstrumentWizard(driver);
		instrumentWizard.selectPaymentMethod(PaymentInstrumentEnum.ACH_EFT);
		instrumentWizard.selectAccountType(BankAccountType.Business_Checking);
		instrumentWizard.setBankAccountHolderName("HQ");
		instrumentWizard.setRoutingNumber(newRoutingNumber);
		instrumentWizard.setAccountNumber("1234567");
		instrumentWizard.setCopyPrimaryContactDetailsCheckBox(true);
		instrumentWizard.clickOK();		
		policySummary.clickUpdate();
		policySummary.clickAccountNumberLink();
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicies();
		AccountPolicies policyScreen = new AccountPolicies(driver);		
		Assert.assertFalse(policyScreen.getPaymentInstrument(1, "Payment Instrument").equals(PaymentInstrumentEnum.Responsive), "The Payment Instrument is not correctly update.");
	}
}
