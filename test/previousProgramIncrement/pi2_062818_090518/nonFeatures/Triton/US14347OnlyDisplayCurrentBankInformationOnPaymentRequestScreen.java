package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.wizards.NewPaymentInstrumentWizard;
import repository.bc.wizards.PaymentRequestWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BankAccountType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.RoutingNumbersHelper;

import java.util.ArrayList;
/**
* @Author JQU
* @Requirement 	US14347 -- Only display the current bank information on "Payment Request" screen
* 				Ensure that when the field �Policy #� is selected with a policy number the field �Payment Instrument� will display the respective bank information that is associated with that policy number. (Req. 12-02-23)
				Ensure that when the field "Policy #" is blank or <none> then there is no bank information in the field "Payment Instrument". 
				Ensure that the field "Payment Instrument" is defaulting to the respective bank information.
				Ensure that the field "Payment Instrument" is un-editable.
* @DATE 	August 20th, 2018
* 
* */
public class US14347OnlyDisplayCurrentBankInformationOnPaymentRequestScreen extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL = null;
	private ARUsers arUser = new ARUsers();
	private String currentPaymentInstrument;
	private String pmtInstrumentDisplayed;
	
	private void getCurrentPaymentInstrumentToVerifyInPaymentRequestScreen(){
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicies();
		AccountPolicies policyScreen = new AccountPolicies(driver);
		currentPaymentInstrument = policyScreen.getPaymentInstrument(1, "Payment Instrument");
		
		//verify payment instrument displayed in Payment Request wizard
		accountMenu.clickAccountMenuActionsNewPaymentRequest();
		PaymentRequestWizard wizard = new PaymentRequestWizard(driver);
		pmtInstrumentDisplayed = wizard.getPaymentInstrument();
		Assert.assertTrue(currentPaymentInstrument.equals(pmtInstrumentDisplayed), "The Payment Instrument displayed in Payment Request Wizard is not the same as the new payment instrument.");
	}
	
	private void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

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

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Payment", "Instrument")
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
	}
	@Test
	public void verifyPaymentInstrument() throws Exception {	
		generatePolicy();		

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObjPL.accountNumber);
		
		getCurrentPaymentInstrumentToVerifyInPaymentRequestScreen();	
	}
	@Test(dependsOnMethods = {"verifyPaymentInstrument"})

	public void changePolicyPaymentInstrumentAndVerify() throws Exception {			
        String newRoutingNumber = RoutingNumbersHelper.getRoutingNumber_Random().getRoutingNumber();

		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObjPL.accountNumber);
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
		
		getCurrentPaymentInstrumentToVerifyInPaymentRequestScreen();
	}
}
