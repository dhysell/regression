package regression.r2.noclock.policycenter.change.subgroup4;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4138 :Policy Change quote screen Section III liability coverage messed up
 * @DATE Jan 23, 2017
 */
public class TestQuoteScreenSectionIIILiabilityCoverages extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void createPLAutoPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Contact driver1;
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK);
		coverages.setUninsuredLimit(UninsuredLimit.OneHundred);
		coverages.setUnderinsured(true);
		coverages.setUnderinsuredLimit(UnderinsuredLimit.OneHundred);

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		String randFirst = StringsUtils.generateRandomNumberDigits(4);				
		driver1 = new Contact("driver-"+randFirst, "motorcycle", Gender.Female,DateUtils.convertStringtoDate("06/01/1980", "MM/dd/YYYY"));				
		driversList.add(driver1);

		//Vehicles
		Vin vin = VINHelper.getRandomVIN();
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();				
		Vehicle veh1 = new Vehicle();
		veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		veh1.setEmergencyRoadside(true);
		veh1.setAdditionalLivingExpense(true);				
		veh1.setComprehensive(true);
		veh1.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		veh1.setCollision(true);
		veh1.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		veh1.setVin(vin.getVin());
		veh1.setDriverPL(driver1);				

		vin = VINHelper.getRandomVIN();
		Vehicle veh2 = new Vehicle();
		veh2.setVehicleTypePL(VehicleTypePL.Motorcycle);
		veh2.setComprehensive(true);				
		veh2.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		veh2.setCollision(true);
		veh2.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		veh2.setVin(vin.getVin());

		vin = VINHelper.getRandomVIN();
		Vehicle veh3 = new Vehicle();
		veh3.setVehicleTypePL(VehicleTypePL.Trailer);				
		veh3.setFireAndTheft(true);
		veh3.setTrailerType(TrailerTypePL.Livestock);
		veh3.setVin(vin.getVin());

		vehicleList.add(veh1);
		vehicleList.add(veh2);
		vehicleList.add(veh3);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setDriversList(driversList);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "Sec3BusRules")
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test (dependsOnMethods = {"createPLAutoPolicy"})
	public void verifyIssuanceVehicleCoverage() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.squire.getPolicyNumber());
        PolicySummary summary = new PolicySummary(driver);
		summary.clickCompletedTransaction(0);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickExpandedViewLink();	
		ArrayList<String> coverages = quote.getAllCellTextFromSpecificVehicleSpecificColoum(2, "Description");
		boolean liability, medical,  uninsuredMotoristBodilyInjury, underinsuredMotoristBodilyInjury;
		liability = false;
		medical = false;
		uninsuredMotoristBodilyInjury = false;
		underinsuredMotoristBodilyInjury = false;
		String errorMessage = "";

		for(String description : coverages){
			if(description.trim().contains("Liability"))
				liability = true;
			if(description.trim().contains("Medical"))
				medical = true;
			if(description.trim().contains("Uninsured Motorist - Bodily Injury"))
				uninsuredMotoristBodilyInjury = true;
			if(description.trim().contains(" Underinsured Motorist - Bodily Injury"))
				underinsuredMotoristBodilyInjury = true;
		}
		if(liability)
			errorMessage = errorMessage + "Liabilty coverage should not be displayed when Coverage Liability NO for vehicle. \n";	
		if(medical)
			errorMessage = errorMessage + "Medical coverage should not be displayed when Coverage Liability NO for vehicle. \n";
		if(uninsuredMotoristBodilyInjury)
			errorMessage = errorMessage + "Uninsured Motorist - Bodily Injury coverage should not be displayed when Coverage Liability NO for vehicle. \n";
		if(underinsuredMotoristBodilyInjury)
			errorMessage = errorMessage + "Underinsured Motorist - Bodily Injury coverage should not be displayed when Coverage Liability NO for vehicle. \n";
		if(errorMessage != "")
			Assert.fail(errorMessage);
	}

	@Test (dependsOnMethods = {"verifyIssuanceVehicleCoverage"})
	public void verifyEditPolicyChangesSquireAuto() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		//start policy change

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setLiabilityCoverage(LiabilityLimit.ThreeHundredHigh);
		coverages.setMedicalCoverage(MedicalLimit.TenK);
		coverages.setUninsuredCoverage(true, UninsuredLimit.ThreeHundred);
		coverages.setUnderinsuredCoverage(true, UnderinsuredLimit.ThreeHundred);
		coverages.clickNext();

        GenericWorkorderVehicles_CoverageDetails Vehicles = new GenericWorkorderVehicles_CoverageDetails(driver);
		Vehicles.selectDriverTableSpecificRowByText(2);

		//Vehicle Liability coverage remove and validate not displaying in quote screen Expanded view
		Vehicles.selectVehicleCoverageDetailsTab();
        Vehicles.setRadioLiabilityCoverage(false);
		Vehicles.clickOK();
		Vehicles.clickNext();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();		
		sideMenu.clickSideMenuRiskAnalysis();	
		sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickExpandedViewLink();	
		ArrayList<String> names = quote.getAllCellTextFromSpecificVehicleSpecificColoum(1, "Description");
		String errorMessage = "";
		boolean testFailed = false;
		Set<String> store = new HashSet<>();
		for (String name : names){ 
			if (store.add(name) == false) { 
				testFailed = true;
				errorMessage= errorMessage + "In policy change Vehicle 2 " + name.trim() + " change should not be displayed when Coverage Liability selected NO \n"; 
			}
		}

		//Add Vehicle Liability coverage back and validate date periods are displaying correctly in quote screen Expanded view
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		sideMenu.clickSideMenuPolicyInfo();
		polInfo.clickEditPolicyTransaction();

		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPACoverages(); 
		sideMenu.clickSideMenuPAVehicles();

		Vehicles.selectDriverTableSpecificRowByText(2);
        Vehicles.selectVehicleCoverageDetailsTab();
        Vehicles.setRadioLiabilityCoverage(true);
		Vehicles.clickOK();
		Vehicles.clickNext();

		sideMenu.clickSideMenuRiskAnalysis();	
		riskAnalysis.Quote();		
		sideMenu.clickSideMenuRiskAnalysis();	
		sideMenu.clickSideMenuQuote();	
		quote.clickExpandedViewLink();

		String[] coverage = {"Liability", "Medical", "Uninsured Motorist - Bodily Injury", "Underinsured Motorist - Bodily Injury"};

		for (String coverageType : coverage) {

			String effectiveDate, expiryDate, changeEfectiveDate, beforeChangeEffectiveDate, beforeChangeExpiryDate, afterChangeEffectiveDate, afterChangeExpiryDate   = "" ;

            effectiveDate = DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObjPL.squire.getEffectiveDate());
            expiryDate = DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObjPL.squire.getExpirationDate());
			changeEfectiveDate = DateUtils.dateFormatAsString("MM/dd/yyyy", changeDate);
			beforeChangeEffectiveDate = quote.getSectionIIISpecificVehicleCoverageValueBeforeChange(1, coverageType, "Eff Date");
			beforeChangeExpiryDate = quote.getSectionIIISpecificVehicleCoverageValueBeforeChange(1, coverageType, "Exp Date");
			afterChangeEffectiveDate = quote.getSectionIIISpecificVehicleCoverageValueAfterChange(1, coverageType, "Eff Date");
			afterChangeExpiryDate = quote.getSectionIIISpecificVehicleCoverageValueAfterChange(1, coverageType, "Exp Date");

			if (!effectiveDate.equals(beforeChangeEffectiveDate)) {
				testFailed = true;
				errorMessage = errorMessage + "Submission effective date "+effectiveDate+" and " +coverageType +" "+ beforeChangeEffectiveDate +"not same .\n";
			}
			if (!changeEfectiveDate.equals(beforeChangeExpiryDate)) {
				testFailed = true;
				errorMessage = errorMessage + "Change effective date "+changeEfectiveDate+" and " +coverageType + beforeChangeExpiryDate +"not same .\n";
			}
			if (!changeEfectiveDate.equals(afterChangeEffectiveDate)) {
				testFailed = true;
				errorMessage = errorMessage + "Change effective date "+changeEfectiveDate+" and " +coverageType + afterChangeEffectiveDate +"not same .\n";
			}
			if (!expiryDate.equals(afterChangeExpiryDate)) {
				testFailed = true;
				errorMessage = errorMessage + "Expiry date "+expiryDate+" and " +coverageType + afterChangeExpiryDate +"not same .\n";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);	
	}
}
