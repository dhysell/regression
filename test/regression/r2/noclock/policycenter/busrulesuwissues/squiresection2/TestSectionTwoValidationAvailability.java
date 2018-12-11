package regression.r2.noclock.policycenter.busrulesuwissues.squiresection2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_CoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;

/**
 * @Author nvadlamudi
 * @Requirement :Squire-Section I-Product-Model
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">Squire-Section I-Product-Model</a>
 * @Description :Section I - Avalability rules are added
 * @DATE Sep 11, 2017
 */
public class TestSectionTwoValidationAvailability extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	public String pgl0007ValMessage = "Policy must have at least 1 acre on an Idaho location in order to quote";
	public String pgl0017ValMessage = "The horses quantity on Section II is less than the sum of horses, mules, donkeys, llamas, and/or alpacas on FPP and/or Section IV. (PGL017)";
	public String pgl0018ValMessage = "The cows quantity on Section II is less than the sum of bulls, cows, steers, heifers, and/or calves on FPP and/or Section IV. (PGL018)";

	@Test
	public void testGenerateSquireWithSectionTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireEligibility squireType = ((new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(property1);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(8);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
		myLiab.setMedicalLimit(SectionIIMedicalLimit.Limit_5000);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;


        Squire mySquire = new Squire(squireType);
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Validation", "Two")
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionTwo" })
	private void testAddBlockBindQuoteIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

		boolean testFailed = false;
		String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// PGL001 General and auto liability limits
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setLiabilityCoverage(LiabilityLimit.CSL500K);

		// PGL002 General and auto medical limits
		coverages.setMedicalCoverage(MedicalLimit.TenK);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		propCoverages.clickSectionIICoveragesTab();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        if (!addCoverage.getGeneralLiabilityLimit().equals(LiabilityLimit.CSL500K.getValue())) {
			testFailed = true;
			errorMessage = errorMessage + "Expected Liability Limit : '" + LiabilityLimit.CSL500K.getValue()
					+ "' is not displayed. /n";
		}

		if (!addCoverage.getMedicalLimit().equals(MedicalLimit.TenK.getValue())) {
			testFailed = true;
			errorMessage = errorMessage + "Expected Medical Limit : '" + MedicalLimit.TenK.getValue()
					+ "' is not displayed. /n";
		}

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		// PGL004 No Seedsman E and O cov if liability limit over $300,000
		if (!this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.SeedsmanEAndO, 0, 0);
			addCoverage.addCoverages(myCoverages);
			risk.Quote();
            if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(
					"Cannot add seedsman E and O coverage if liability limits exceed 300,000 CSL or 300/500/100 split"))) {
				testFailed = true;
				errorMessage = errorMessage
						+ "Expected page validation : 'Cannot add seedsman E and O coverage if liability limits exceed 300,000 CSL or 300/500/100 split' is not displayed. /n";
			}			
		}
        addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100000_CSL);
		sideMenu.clickSideMenuSquirePropertyDetail();
		sideMenu.clickSideMenuSquirePropertyCoverages();
		propCoverages.clickSectionIICoveragesTab();
        // PGL016 Custom Farming and Custom Farming Fire
		addCoverage.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.CustomFarming, 0, 0));
		addCoverage.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.CustomFarmingFire, 0, 0));
        addCoverage.clickCustomFarmingCheckBox();

		coverages.clickNext();
		sideMenu.clickSideMenuSquirePropertyCoverages();
		propCoverages.clickSectionIICoveragesTab();

		if (addCoverage.checkCustomFarmingFireExists()) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Custom Farming Fire is displayed. /n";
		}

		// PGL006 Either Horse boarding or Horse boarding and pasturing.
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.HorseBoarding, 0, 1000);
		SectionIICoverages myCoverages2 = new SectionIICoverages(SectionIICoveragesEnum.HorseBoardingAndPasturing, 0, 1000);
		
		addCoverage.addCoverages(myCoverages);
		addCoverage.setQuantity(myCoverages);
		addCoverage.addCoverages(myCoverages2);
		addCoverage.setQuantity(myCoverages2);
		coverages.clickNext();
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(
				"A policy can either have Horse Boarding or Horse Boarding and Pasturing coverage on Section II and never both"))) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Expected page validation : 'A policy can either have Horse Boarding or Horse Boarding and Pasturing coverage on Section II and never both' is not displayed. /n";
		} else {
			addCoverage.clickHorseBoardingCheckbox();
		}

		// PGL017 Section II, IV, & FPP horses
		// PGL018 Section II, IV, & FPP Cows
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkSquireInlandMarine(true);
		sideMenu.clickSideMenuIMCoveragePartSelection();
        GenericWorkorderSquireInlandMarine_CoverageSelection imSelection = new GenericWorkorderSquireInlandMarine_CoverageSelection(driver);
		imSelection.checkCoverage(true, "Livestock");
		sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(driver);
		livestockListPage.clickAdd();
        GenericWorkorderSquireInlandMarine_Livestock livestockPage = new GenericWorkorderSquireInlandMarine_Livestock(driver);
		livestockPage.setType(LivestockType.Livestock);
		livestockPage.setDeductible(LivestockDeductible.Ded100);
		LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(LivestockType.Livestock,
				LivestockScheduledItemType.Horse, "Test Horse 1", 1000);
		LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(LivestockType.Livestock,
				LivestockScheduledItemType.Cow, "Test BullCow 1", "TAG123", "Brand1", "Breed1", 5000);

		livestockPage.addScheduledItem(livSchedItem1);
		livestockPage.addScheduledItem(livSchedItem2);
		livestockPage.clickOk();

		// PGL007 Min. acre for Squire policy
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setAcres(0);
//		propertyLocations.clickOK();
//
//		// PGL003 Allowed state location
//		sideMenu.clickSideMenuPropertyLocations();
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Nevada);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");
//		propertyLocations.clickOK();
//
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Oregon);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");		
//		propertyLocations.clickOK();
//
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Wyoming);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");		
//		propertyLocations.clickOK();
//
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Utah);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");		
//		propertyLocations.clickOK();
//
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Montana);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");
		propertyLocations.clickOK();

		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pgl0007ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pgl0007ValMessage + "' is not displayed. /n";
		}

		if (!risk.getValidationMessagesText().contains(pgl0017ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pgl0017ValMessage + "' is not displayed. /n";
		}

		if (!risk.getValidationMessagesText().contains(pgl0018ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pgl0018ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

}
