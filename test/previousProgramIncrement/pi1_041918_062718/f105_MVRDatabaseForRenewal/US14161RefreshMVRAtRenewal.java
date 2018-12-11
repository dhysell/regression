package previousProgramIncrement.pi1_041918_062718.f105_MVRDatabaseForRenewal;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class US14161RefreshMVRAtRenewal extends BaseTest {
	private GeneratePolicy clueMVRPol;
	private String MVRRefreshRequest = "MVR refresh request found no entries for this policy.";
	private WebDriver driver;

	@Test
	public void testCheckRefreshMVRInRenewal() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		testSquireAutoMVRCLUE();
		boolean testPassed = true;
		String errorMessage = "Policy Number: "+ clueMVRPol.squire.getPolicyNumber();
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				clueMVRPol.accountNumber);
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        gwHelpers.editPolicyTransaction();
        if (gwHelpers.errorMessagesExist()) {
			if (gwHelpers.getFirstErrorMessage().contains("The object you are trying to update was changed by another user.")) {
				gwHelpers.clickDiscardUnsavedChangesLink();
			} else {
				Assert.fail("There was an unexpected error. The error was: " + gwHelpers.getFirstErrorMessage());
			}
		}
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEdit();
		paDrivers.clickMotorVehicleRecord();
		GenericWorkorderSquireAutoDrivers_MotorVehicleRecord driver_MVR = new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
		driver_MVR.clickRefreshMVR();		
		
		if(gwHelpers.errorMessagesExist() && !gwHelpers.getFirstErrorMessage().contains(MVRRefreshRequest) && driver_MVR.checkMVRNotFoundMessage()){
			if(!DateUtils.convertStringtoDate(driver_MVR.MVRRequestedDate(), "MM/dd/yyyy").equals(DateUtils.getDateValueOfFormat(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), "MM/dd/YYY"))){
				testPassed = false;
					errorMessage = errorMessage	+ "Expected MVR Requested date : '"+DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter) +"' is not displayed after clicking refresh MVR. /n";
			}
			driver_MVR.selectMVRIncidents(SRPIncident.Waived);
		}	
		paDrivers.clickOK();
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, clueMVRPol);		
		Assert.assertTrue(testPassed,errorMessage);

	}
	private void testSquireAutoMVRCLUE() throws Exception {
		
		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle newVeh = new Vehicle();
		newVeh.setEmergencyRoadside(true);
		vehicleList.add(newVeh);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        clueMVRPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
                .withLexisNexisData(true, false, false, false, true, true, false)
				.withPolTermLengthDays(70)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
	}
}
