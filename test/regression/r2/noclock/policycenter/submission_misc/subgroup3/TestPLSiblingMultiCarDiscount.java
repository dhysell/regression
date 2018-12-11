package regression.r2.noclock.policycenter.submission_misc.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@QuarantineClass
public class TestPLSiblingMultiCarDiscount extends BaseTest {
	private GeneratePolicy squirePolicyObj;
	private GeneratePolicy siblingPolicyObj;
	private Underwriters genericPLUnderwriter;
    private WebDriver driver;

	@Test()  
	private void testIssuedSquirePol() throws Exception  {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Test", "SqForSib")
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = {"testIssuedSquirePol"})  
	private void testFullAppSiblingPol() throws Exception  {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		
		Vehicle passengerVehicle = new Vehicle();
		passengerVehicle.setEmergencyRoadside(true);
		vehicleList.add(passengerVehicle);
		
		Vehicle utilityTrailer = new Vehicle();
		utilityTrailer.setVehicleTypePL(VehicleTypePL.Trailer);
		utilityTrailer.setTrailerType(TrailerTypePL.Utility);
		utilityTrailer.setCostNew(26521);
		utilityTrailer.setFireAndTheft(true);
		vehicleList.add(utilityTrailer);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL75K, MedicalLimit.TenK, true, UninsuredLimit.CSL75K, false, UnderinsuredLimit.CSL75K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        siblingPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
				.withSiblingPolicy(squirePolicyObj, "Test", "Sibling")
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = {"testFullAppSiblingPol"})
    private void testValidateSiblingSubmission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		genericPLUnderwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(genericPLUnderwriter.getUnderwriterUserName(), genericPLUnderwriter.getUnderwriterPassword(), siblingPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuPAModifiers();
        GenericWorkorderModifiers modifiers = new GenericWorkorderModifiers(driver);
		try {
			WebElement sectionModifiersTable = modifiers.getPLSquireSectionModifiersTable();
            ArrayList<String> modifierCategories = new TableUtils(driver).getAllCellTextFromSpecificColumn(sectionModifiersTable, "Category");
			if (modifierCategories.contains("Multi-car discount")) {
				Assert.fail("The Multi-car discount is available, but should not be. Test failed.");
			}
		} catch (Exception e) {
            System.out.println("The Section Modifiers table was not available, this is expected.");
		}
		
        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehicles = new GenericWorkorderVehicles_Details(driver);
		Vehicle secondPassengerVehicle = new Vehicle();
		secondPassengerVehicle.setEmergencyRoadside(true);
		vehicles.fillOutVehicleDetails_QQ(siblingPolicyObj.basicSearch, secondPassengerVehicle);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAModifiers();

        modifiers = new GenericWorkorderModifiers(driver);
        TableUtils tableUtils = new TableUtils(driver);
		try {
            WebElement tableRow = tableUtils.getRowInTableByColumnNameAndValue(modifiers.getPLSquireSectionModifiersTable(), "Category", "Multi-car discount");
            String eligibilityNumber = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Eligibility");
            String discountValue = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Discounts (-) / Surcharges (+)");
			if (!eligibilityNumber.equals("2")) {
				Assert.fail("The Multi-car discount eligibility count is not 2, as expected. Test failed.");
			}
			if (!discountValue.equals("-4")) {
				Assert.fail("The Multi-car discount value is not -4, as expected. Test failed.");
			}
		} catch (Exception e) {
			Assert.fail("There was a problem getting the modifiers table. Please investigate.");
		}

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();

        vehicles = new GenericWorkorderVehicles_Details(driver);
		ArrayList<Vehicle> vehiclesToAdd = new ArrayList<Vehicle>();
		
		Vehicle thirdPassengerVehicle = new Vehicle();
		thirdPassengerVehicle.setEmergencyRoadside(true);
		vehiclesToAdd.add(thirdPassengerVehicle);
		
		Vehicle forthPassengerVehicle = new Vehicle();
		forthPassengerVehicle.setEmergencyRoadside(true);
		vehiclesToAdd.add(forthPassengerVehicle);
		
		Vehicle fifthPassengerVehicle = new Vehicle();
		fifthPassengerVehicle.setEmergencyRoadside(true);
		vehiclesToAdd.add(fifthPassengerVehicle);
		
		for(Vehicle vehicle : vehiclesToAdd) {
			vehicles.fillOutVehicleDetails_QQ(siblingPolicyObj.basicSearch, vehicle);
		}


        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAModifiers();

        modifiers = new GenericWorkorderModifiers(driver);
		try {
            WebElement tableRow = tableUtils.getRowInTableByColumnNameAndValue(modifiers.getPLSquireSectionModifiersTable(), "Category", "Multi-car discount");
            String eligibilityNumber = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Eligibility");
            String discountValue = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Discounts (-) / Surcharges (+)");
			if (!eligibilityNumber.equals("5")) {
				Assert.fail("The Multi-car discount eligibility count is not 5, as expected. Test failed.");
			}
			if (!discountValue.equals("-10")) {
				Assert.fail("The Multi-car discount value is not -10, as expected. Test failed.");
			}
		} catch (Exception e) {
			Assert.fail("There was a problem getting the modifiers table. Please investigate.");
		}
		
		//Remove vehicles
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();

        vehicles = new GenericWorkorderVehicles_Details(driver);
		vehicles.removeVehicleByVehicleNumber(fifthPassengerVehicle.getVehicleNumber());
		
		sideMenu.clickSideMenuPAModifiers();

        modifiers = new GenericWorkorderModifiers(driver);
		try {
            WebElement tableRow = tableUtils.getRowInTableByColumnNameAndValue(modifiers.getPLSquireSectionModifiersTable(), "Category", "Multi-car discount");
            String eligibilityNumber = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Eligibility");
            String discountValue = tableUtils.getCellTextInTableByRowAndColumnName(modifiers.getPLSquireSectionModifiersTable(), tableRow, "Discounts (-) / Surcharges (+)");
			if (!eligibilityNumber.equals("4")) {
				Assert.fail("The Multi-car discount eligibility count is not 4, as expected. Test failed.");
			}
			if (!discountValue.equals("-4")) {
				Assert.fail("The Multi-car discount value is not -4, as expected. Test failed.");
			}
		} catch (Exception e) {
			Assert.fail("There was a problem getting the modifiers table. Please investigate.");
		}
		sideMenu.clickSideMenuPAVehicles();

        vehicles = new GenericWorkorderVehicles_Details(driver);
		vehicles.removeVehicleByVehicleNumber(forthPassengerVehicle.getVehicleNumber());
		vehicles.removeVehicleByVehicleNumber(thirdPassengerVehicle.getVehicleNumber());
		vehicles.removeVehicleByVehicleNumber(secondPassengerVehicle.getVehicleNumber());
		
		sideMenu.clickSideMenuPAModifiers();

        modifiers = new GenericWorkorderModifiers(driver);
		try {
			WebElement sectionModifiersTable = modifiers.getPLSquireSectionModifiersTable();
            ArrayList<String> modifierCategories = tableUtils.getAllCellTextFromSpecificColumn(sectionModifiersTable, "Category");
			if (modifierCategories.contains("Multi-car discount")) {
				Assert.fail("The Multi-car discount is available, but should not be. Test failed.");
			}
		} catch (Exception e) {
            System.out.println("The Section Modifiers table was not available, this is expected.");
		}
		
        new GuidewireHelpers(driver).logout();
	}
}
