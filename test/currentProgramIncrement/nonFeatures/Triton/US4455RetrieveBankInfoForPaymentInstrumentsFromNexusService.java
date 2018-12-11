package currentProgramIncrement.nonFeatures.Triton;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.wizards.NewPaymentInstrumentWizard;
import repository.driverConfiguration.Config;
import services.enums.Broker;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.PaymentUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import services.helpers.com.idfbins.routingnumber.RoutingNumberHelper;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import services.services.com.idfbins.routingnumber.RoutingNumber;


	/**
	* @Author JQU
	* @Requirement 	US4455 -- Retrieve Bank Name for Payment Instruments from the Nexus Service instead of ContactManager
	* 				As a system when validating and pulling information from the fields "Routing Number" and "Bank Name / Address" I want the system to want check "Nexus" instead of "ContactManager" so that the information that is provided is more actuate.
					
					Steps to get there:
						In BillingCenter log into an insureds account.
						Click into an insureds policy.
						Click on the "Summary" screen.
						Click the "Edit" button.
						On the field "Payment Instrument" click "New"
						Now you are on the screen "New Payment Instrument" on the field "Payment Method" select the field option of "ACH/EFT".
						In the field "Routing Number" type in a routing number.
						You will notice that the field 
	* @DATE 	November 15th, 2018
	* 
	* */
public class US4455RetrieveBankInfoForPaymentInstrumentsFromNexusService extends BaseTest{
		private GeneratePolicy myPolicyObj;
		private WebDriver driver;
		private ARUsers arUser = new ARUsers();		
		
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
	                .withInsFirstLastName("Payment", "Instrument")
	                .withPaymentPlanType(PaymentPlanType.Monthly)
	                .withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);	       
		}
		@Test
		public void verifyPaymentInstrument() throws Exception {	
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			generatePolicy();		
			//find random routing number			
			driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));			
			String newRoutingNumber = new PaymentUtils(driver).getRandomRoutingNumber();			
			RoutingNumber rn = new RoutingNumberHelper(Broker.DEV).validateRoutingNumber(newRoutingNumber);			
	        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
	       
			new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);			
			BCPolicySummary policySummary = new BCPolicySummary(driver);
			policySummary.clickEdit();
			policySummary.clickNewPaymentInstrument();
			NewPaymentInstrumentWizard instrumentWizard = new NewPaymentInstrumentWizard(driver);
			instrumentWizard.selectPaymentMethod(PaymentInstrumentEnum.ACH_EFT);
			instrumentWizard.setRoutingNumber(newRoutingNumber);
			new GuidewireHelpers(driver).clickProductLogo();
			String bankInfoOnUI = instrumentWizard.getBankNameAddress();
			if((!bankInfoOnUI.contains(rn.getCustomerName()) || (!bankInfoOnUI.contains(rn.getCity())))){
				Assert.fail("Bank informant on New Payment Instrument is not correct for Rounting Number: " + newRoutingNumber + ". The Bank name is : "+ rn.getCustomerName() +". The City is: "+ rn.getCity()+".");
			}else{
				System.out.println("Bank information is correctly retrieved.");
			}			
		}
	}

