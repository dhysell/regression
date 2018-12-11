package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author nvadlamudi
 * @Requirement :Squire-Section I-Product-Model
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">
 * Squire-Section I-Product-Model</a>
 * @Description: to validate all the section one business validations
 * @DATE Aug 24, 2017
 */
@QuarantineClass
public class TestSectionOneValidations extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	String pr036ValMessage = "Modular/Manufactured Vacation Home cannot be older than 1984.(PR036)";
	String pr014ValMessage = "Waive Glass coverages has been changed for all applicable properties";
	String pr016ValMessage = "Limit : Coverage C limit must be greater or equal to $15,000. (PR016)";
	String pr027ValMessage = "Modular/Manufactured Residence Premises cannot be older than 1985";
	String pr030ValMessage = "the square feet must be greater or equal to 400. If not then please move the property to Coverage E. (PR030)";
	String pr032ValMessage = "Frame Condominium Residence Premises cannot be older than 1954";
	String pr043ValMessage = "Modular/Manufactured Dwelling Premises cannot be older than 1985. (PR043)";
	String pr079ValMessage = "The buindling does not qualify for Coverage A. Please change the property to a Coverage Premises. (PR079)";
	String pr089ValMessage = " is the PNI and cannot be removed";
	String pr092ValMessage = "A property exists on location #1. Please remove it before removing the location. (PR092)";
	String pr093ValMessage = "as the garaged location and must be changed before this location can be removed. (PR093)";
	String pr094ValMessage = "and must be changed before this location can be removed. (PR094)";
	String pr088ValMessage = "All habitable buildings must have owners assigned. (PR088)";
	String pr091ValMessage = "must be added to at least one item on FPP. (PR091)";

	@Test
	public void testGenerateSquireWithSectionOne() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireEligibility squireType = (new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.City
				: ((new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setOwner(true);
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		property2.setOwner(true);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumResidencePremise);
		property3.setOwner(true);
		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property4.setOwner(true);
		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstruction);
		property5.setOwner(true);
		property5.setYearBuilt(DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy"));

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);
		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(15);
		location1.setPlNumResidence(15);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(squireType);
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("busRule", "One")
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionOne" })
	private void testCheckScreenValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

		boolean testFailed = false;
		String errorMessage = "";

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();

		// PR014 - Coverage A Glass deductible waived
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);

		coverages.setCoverageAWaiveGlassDeductible(false);
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr014ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr014ValMessage + "' is not displayed. /n";
		}

		coverages.clickSpecificBuilding(1, 2);

		coverages.setCoverageAWaiveGlassDeductible(true);
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr014ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr014ValMessage + "' is not displayed. /n";
		}

		// PR016 - Coverage C Min Value - this is not required as it is not
		PLPolicyLocationProperty property6 = new PLPolicyLocationProperty(PropertyTypePL.Contents);
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.fillOutPropertyDetails_QQ(myPolicyObjPL.basicSearch, property6);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setFoundationType(FoundationType.FullBasement);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		propertyDetail.clickOk();
		propertyDetail.clickNext();

		coverages.clickSpecificBuilding(1, 6);
		coverages.setCoverageCLimit(14000);
		coverages.setCoverageCValuation(property6.getPropertyCoverages());
        coverages.selectCoverageCCoverageType(CoverageType.BroadForm);
		coverages.clickNext();

		if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr016ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr016ValMessage + "' is not displayed. /n";
		}

		coverages.setCoverageCLimit(25000);

		// PR027 - Residence Modular/Manufactured Min year
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setYearBuilt(1980);
		constructionPage.clickOK();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		if (!risk.getValidationMessagesText().contains(pr027ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr027ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
        }
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();
        // PR030 - Mobile or Modular min sq. ft.
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setSquareFootage(300);
		constructionPage.clickOK();
        if (!risk.getValidationMessagesText().contains(pr030ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr030ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
        }
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(900);
		constructionPage.clickOK();

		// PR032 - Condo Residence Frame Non- Frame Min year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumResidencePremise);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setYearBuilt(1950);
		constructionPage.clickOK();
        if (!risk.getValidationMessagesText().contains(pr032ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr032ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();
        // PR036 - Vacation home modular min year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setYearBuilt(1980);
		constructionPage.clickOK();
        if (!risk.getValidationMessagesText().contains(pr036ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr036ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();

		// PR043 - Dwelling premises modular min year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setYearBuilt(1980);
		constructionPage.clickOK();
        if (!risk.getValidationMessagesText().contains(pr043ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr043ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();

		// PR048 - Dwelling Under Construction year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingUnderConstruction);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		constructionPage.setYearBuilt(yearField - 3);
		constructionPage.clickOK();

		String pr048ValMessage = "Dwelling Under Construction cannot be older than " + (yearField - 1) + ". (PR046)";
		if (!risk.getValidationMessagesText().contains(pr048ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr048ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		constructionPage.setYearBuilt(yearField - 1);
		constructionPage.clickOK();

		// PR089 - Cannot remove PNI or ANI
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
		houseHoldMember.clickRemoveMember(this.myPolicyObjPL.pniContact.getLastName());
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr089ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr089ValMessage + "' is not displayed. /n";
		}

		// PR092 - Property on location
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderLocations myLoc = new GenericWorkorderLocations(driver);
		myLoc.removeAllLocations();
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr092ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr092ValMessage + "' is not displayed. /n";
		}
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = { "testCheckScreenValidations" })
	private void testCheckQuoteValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        boolean testFailed = false;
		String errorMessage = "";

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPropertyLocations();

        AddressInfo address = new AddressInfo(true);
        GenericWorkorderLocations myLoc = new GenericWorkorderLocations(driver);
		myLoc.clickLocationsNewLocation();
        myLoc.setLocationsLocationAddress("New...");
        myLoc.setLocationsAddressLine1(address.getLine1());
        myLoc.setLocationsCity(address.getCity());
		myLoc.setLocationsZipCode(address.getZip());
		myLoc.setLocationsCounty(address.getCounty());
        myLoc.locationStandardizeAddress();
		myLoc.clickLocationsOk();

		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
		vehiclePage.setOdometer(5000);
		vehiclePage.selectTruckType(VehicleTruckTypePL.FourPlus);
		vehiclePage.setGVW(52000);
		vehiclePage.setMake("honda");
		vehiclePage.setModel("accord");
		vehiclePage.setModelYear(2002);
		vehiclePage.selectGaragedAtZip(address.getLine1());
		vehiclePage.clickOK();

		// PR093 - Vehicle garaged at location
		sideMenu.clickSideMenuPropertyLocations();
        myLoc.removeAllLocations();
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr093ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr093ValMessage + "' is not displayed. /n";
		}
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);

		// PR094 - Farm equipment on location
		myLoc.clickLocationsNewLocation();
        ArrayList<String> sections = new ArrayList<String>();
		sections.add("01");
		sections.add("02");
		sections.add("03");
		sections.add("04");
		sections.add("05");
		sections.add("06");
		sections.add("07");
		propertyLocations.selectLocationAddress("New");
//        propertyLocations.setAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
//		propertyLocations.setCity(myPolicyObjPL.pniContact.getAddress().getCity());
//        propertyLocations.setZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
//        propertyLocations.selectCounty(myPolicyObjPL.pniContact.getAddress().getCounty());
//        propertyLocations.addSection(sections);
//		propertyLocations.clickStandardizeAddress();
//        TownshipRange townshipRange = TownshipRangeHelper
//				.getRandomTownshipRangeForCounty(myPolicyObjPL.pniContact.getAddress().getCounty());
//		propertyLocations.selectTownshipNumber(townshipRange.getTownship());
//		propertyLocations.selectTownshipDirection(townshipRange.getTownshipDirection());
//		propertyLocations.selectRangeNumber(townshipRange.getRange());
//		propertyLocations.selectRangeDirection(townshipRange.getRangeDirection());
//        propertyLocations.setAcres(10);
//		propertyLocations.setNumberOfResidence(2);
		propertyLocations.clickOK();

		sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.clickEditButton();
        farmEquipment.setLocationByPartialText("01");
		farmEquipment.clickOk();
		sideMenu.clickSideMenuPropertyLocations();
        myLoc.removeAllLocations();
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr094ValMessage))) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr094ValMessage + "' is not displayed. /n";
		}

		PLPolicyLocationProperty property7 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.fillOutPropertyDetails_FA(property7);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.fillOutPropertyConstrustion_FA(property7);
		constructionPage.setLargeShed(false);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		constructionPage.clickProtectionDetailsTab();
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property7);
		protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 7);
        coverages.setCoverageALimit(120000);
		coverages.setCoverageAValuation(ValuationMethod.ActualCashValue);
		coverages.setCoverageCAdditionalValue(120000);
		coverages.setCoverageCValuation(property7.getPropertyCoverages());
        coverages.clickNext();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr088ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr088ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		if (!this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			sideMenu.clickSideMenuSquirePropertyCoverages();
			coverages.clickFarmPersonalProperty();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
			
			fppCovs.clickFPPAdditionalInterests();
            AddressInfo bankAddress = new AddressInfo();
			AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Test" + StringsUtils.generateRandomNumberDigits(5), "Per" + StringsUtils.generateRandomNumberDigits(8), bankAddress);
			loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
			loc2Bldg1AddInterest.setAddress(bankAddress);
			loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

            GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
			additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
            SearchAddressBookPC search = new SearchAddressBookPC(driver);
			search.searchForContact(myPolicyObjPL.basicSearch, loc2Bldg1AddInterest);
            additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(
					loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
			additionalInterests.setAddressListing("New...");
            additionalInterests.setContactEditAddressLine1(loc2Bldg1AddInterest.getAddress().getLine1());
			additionalInterests.setContactEditAddressCity(loc2Bldg1AddInterest.getAddress().getCity());
            additionalInterests.setContactEditAddressState(loc2Bldg1AddInterest.getAddress().getState());
            additionalInterests.setContactEditAddressZipCode(loc2Bldg1AddInterest.getAddress().getZip());
            additionalInterests.setContactEditAddressAddressType(loc2Bldg1AddInterest.getAddress().getType());
			additionalInterests.setAdditionalInterestsLoanNumber(loc2Bldg1AddInterest.getLoanContractNumber());
            additionalInterests.clickRelatedContactsTab();
			additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

            risk.Quote();

            if (!risk.getValidationMessagesText().contains(pr091ValMessage)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected page validation : '" + pr091ValMessage
						+ "' is not displayed. /n";
			}else{
				risk.clickClearButton();
			}			
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		if (testFailed) {
			Assert.fail(errorMessage);
		}

	}
}
