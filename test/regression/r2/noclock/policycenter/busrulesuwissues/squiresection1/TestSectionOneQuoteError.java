package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.Measurement;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

public class TestSectionOneQuoteError extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	String pr012ValMessage = "Section I deductible of $5,000 or more requires at least 1 Coverage A Residence Premises or Condominium Residence Premises with limit greater or equal to $200,000. (PR012)";
	String pr018ValMessage = "Please correct the number of residence for this location. (PR018)";
	String pr019ValMessage = "quantity on Section II is less than the sum of bulls, cows, steers, heifers, and/or calves on FPP and/or Section IV.";
	String pr020ValMessage = "Livestock added to Section II liability, and FPP coverage type is blanket include without Livestock. Add Livestock to FPP. (PR019)";
	String pr025ValMessage = "For Frame or Non Frame Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E";
	String pr026ValMessage = "For Modular/Manufactered Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR026)";
	String pr028ValMessage = "For Mobile Home with foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR028)";
	String pr029ValMessage = "For Mobile Home without foundation Residence Premises to qualify for Coverage A, the limit must be greater or equal to $4,000. If not then please move the property to Coverage E. (PR029)";
	String pr031ValMessage = "For Frame Condominium Residence Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. (PR031)";
	String pr034ValMessage = "For For Frame Vacation Home to qualify for Coverage A, the limit must be greater or equal to $15,000. If not then please move the property to Coverage E. (PR034)";
	String pr035ValMessage = "For Modular/Manufactered Vacation Home to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR035)";
	String pr039ValMessage = "For Frame Condominium Vacation Home to qualify for Coverage A, the limit must be greater or equal to $20,000. If not then please move the property to Coverage E. (PR039)";
	String pr041ValMessage = "For Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $20,000. If not then please move the property to Coverage E. (PR041)";
	String pr044ValMessage = "For Frame Condominium Dwelling Premises to qualify for Coverage A, the limit must be greater or equal to $40,000. (PR044)";
	String pr046ValMessage = "For Dwelling Under Construction to qualify for Coverage A, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR046)";
	String pr049ValMessage = "Residence Premises to qualify for Coverage A built after 1968, the limit must be greater or equal to $40,000. If not then please move the property to Coverage E. (PR049)";
	String pr050ValMessage = "Policy must have at least 1 acre on an Idaho location in order to quote";
	String pr076ValMessage = "Cov E habitable min limit";

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
		property1.setConstructionType(ConstructionTypePL.Frame);
		property1.setNumberOfUnits(NumberOfUnits.OneUnit);

		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		property2.setOwner(true);

		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumResidencePremise);
		property3.setOwner(true);
		property3.setConstructionType(ConstructionTypePL.Frame);
		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property4.setOwner(true);
		property4.setConstructionType(ConstructionTypePL.Frame);
		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome);
		property5.setOwner(true);
		property5.setConstructionType(ConstructionTypePL.Frame);
		PLPolicyLocationProperty property6 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumDwellingPremises);
		property6.setOwner(true);
		property6.setConstructionType(ConstructionTypePL.Frame);
		PLPolicyLocationProperty property7 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstruction);
		property7.setOwner(true);
		property7.setConstructionType(ConstructionTypePL.Frame);
		property7.setYearBuilt(DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy"));
		PLPolicyLocationProperty property8 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
		property8.setOwner(true);
		property8.setConstructionType(ConstructionTypePL.Frame);
		property8.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);
		locOnePropertyList.add(property6);
		locOnePropertyList.add(property7);
		locOnePropertyList.add(property8);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(8);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

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
                .withInsFirstLastName("SQOne", "Rules")
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionOne" })
	private void testCheckQuoteErrors() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

		boolean testFailed = false;
		String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
		// PR018 - No. of Residence and properties
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOk();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr018ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr018ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.setUnits(NumberOfUnits.OneUnit);
		propertyDetail.clickOk();

		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(5);
		propertyLocations.clickOK();
        risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr018ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr018ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuPropertyLocations();
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(15);
		propertyLocations.clickOK();

		// PR025 - Residence Frame Non- Frame Min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(14000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr025ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr025ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		// PR026 - Residence Modular/Manufactured Min limit
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		propertyDetail.clickOk();
		propertyDetail.clickNext();
        coverages.setCoverageALimit(35000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr026ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr026ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		// PR028 - Residence Mobile w/ foundation Min limit
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setFoundationType(FoundationType.FullBasement);
		propertyDetail.clickOk();
		propertyDetail.clickNext();
        coverages.setCoverageALimit(14000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr028ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr028ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		// PR029 - Residence Mobile w/o foundation Min limit
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.setFoundationType(FoundationType.None);

		constructionPage.setSerialNumber("1234");
		constructionPage.setMake("TestMake2");
		constructionPage.setModel("TestModel2");
		propertyDetail.clickOk();
        propertyDetail.clickNext();
        coverages.setCoverageALimit(3000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr029ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr029ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		// PR049 - Residence Frame Non- Frame Min limit on newer homes
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setFoundationType(FoundationType.FullBasement);
		constructionPage.setYearBuilt(1969);
		propertyDetail.clickOk();
        risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr049ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr049ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.setCoverageALimit(123000);

		// PR031 - Condo Residence Frame Non- Frame Min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumResidencePremise));
        coverages.setCoverageALimit(23000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr031ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr031ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumResidencePremise));
        coverages.setCoverageALimit(123000);

		// PR034 - Vacation home Frame Non- Frame Min limit
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.VacationHome));
        coverages.setCoverageALimit(13000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr034ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr034ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		// PR035 - Residence Modular/Manufactured Min limit
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		propertyDetail.clickOk();
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr035ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr035ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.VacationHome));
        coverages.setCoverageALimit(125000);
		// PR039 - Condo vacation frame non-frame min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumVacationHome));
        coverages.setCoverageALimit(12000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr039ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr039ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumVacationHome));
        coverages.setCoverageALimit(102000);

		// PR041 - Dwelling Premises min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingPremises));
        coverages.setCoverageALimit(18000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr041ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr041ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingPremises));
        coverages.setCoverageALimit(102000);

		// PR044 - Condo Dwelling Frame Non-Frame min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumDwellingPremises));
        coverages.setCoverageALimit(18000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr044ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr044ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumDwellingPremises));
        coverages.setCoverageALimit(102000);

		// PR046 - Dwelling Under Construction min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingUnderConstruction));
        coverages.setCoverageALimit(18000);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr046ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr046ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingUnderConstruction));
        coverages.setCoverageALimit(102000);

		// PR076 - Cov E habitable min limit
		/*
		 * sideMenu.clickSideMenuSquirePropertyCoverages();
		 * coverages.clickSpecificBuilding(1,
		 * coverages.getBuildingNumber(PropertyTypePL.DwellingPremisesCovE));
         *  coverages.setCoverageELimit(12000); risk.Quote();
         *  if
		 * (!risk.getValidationMessagesText().contains(pr076ValMessage)) {
		 * testFailed = true; errorMessage = errorMessage +
		 * "Expected page validation : '" + pr076ValMessage +
		 * "' is not displayed. /n"; } else { risk.clickClearButton(); }
		 * sideMenu.clickSideMenuSquirePropertyCoverages();
		 * coverages.clickSpecificBuilding(1,
		 * coverages.getBuildingNumber(PropertyTypePL.DwellingPremisesCovE));
         *  coverages.setCoverageELimit(102000);
		 */

		// PR050 - Property and acres
		sideMenu.clickSideMenuPropertyLocations();
        propertyLocations.clickEditLocation(1);
//		propertyLocations.setAcres(0);
		propertyLocations.clickOK();
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr050ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr050ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		if (!myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			// PR020 - FPP livestock and no Sec II livestock
			sideMenu.clickSideMenuSquirePropertyCoverages();
			coverages.clickSectionIICoveragesTab();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
			section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
			section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Donkey, 2);
			risk.Quote();
            if (!risk.getValidationMessagesText().contains(pr020ValMessage)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected page validation : '" + pr020ValMessage
						+ "' is not displayed. /n";
			} else {
				risk.clickClearButton();
			}
			section2Covs.clickLiveStockCheckBox();
            // PR019 - Blanket Include and general liability livestock
			coverages.clickFarmPersonalProperty();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
			fppCovs.selectCoverages(FarmPersonalPropertyTypes.Livestock);
			fppCovs.addItem(FPPFarmPersonalPropertySubTypes.Bulls, 2, 1000, "Testing Type");

			risk.Quote();
            if (!risk.getValidationMessagesText().contains(pr019ValMessage)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected page validation : '" + pr019ValMessage
						+ "' is not displayed. /n";
			} else {
				risk.clickClearButton();
			}
		}

		// PR012 - Property deductible over $5,000
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.removeBuilding("8");
		propertyDetail.removeBuilding("7");
		propertyDetail.removeBuilding("6");
		propertyDetail.removeBuilding("5");
		propertyDetail.removeBuilding("4");
		propertyDetail.removeBuilding("3");
		propertyDetail.removeBuilding("2");
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);
		risk.Quote();
        if (!risk.getValidationMessagesText().contains(pr012ValMessage)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected page validation : '" + pr012ValMessage + "' is not displayed. /n";
		} else {
			risk.clickClearButton();
		}

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

	// US11463 - Add Dwelling Under Construction Coverage A to PR011

	@Test(dependsOnMethods = { "testCheckQuoteErrors" })
	private void testCheckPR011Changes() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		// PR018 - No. of Residence and properties
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.removeBuilding("1");
		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.Shop);
		property.setConstructionType(ConstructionTypePL.Frame);
		property.setFoundationType(FoundationType.FullBasement);
		propertyDetail.clickAdd();
		propertyDetail.setPropertyType(property.getpropertyType());
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
		propertyDetail.clickPropertyConstructionTab();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);

		constructionPage.setYearBuilt(property.getYearBuilt());
		constructionPage.setConstructionType(property.getConstructionType());
		constructionPage.setSquareFootage(property.getSquareFootage());
		constructionPage.setFoundationType(property.getFoundationType());
		constructionPage.setRoofType(property.getRoofType());
		constructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "Testing Sandwich");
        constructionPage.setMeasurement(Measurement.SQFT);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		
		coverages.clickSpecificBuilding(1, BuildingNumber);
		coverages.setCoverageELimit(3000);
		coverages.setCoverageECoverageType(CoverageType.SpecialForm);
		coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
		coverages.selectSectionIDeductible(SectionIDeductible.OneThousand);

		if(!myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)){
			coverages.clickFarmPersonalProperty();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
			fppCovs.selectCoverages(FarmPersonalPropertyTypes.Livestock);
            coverages.clickSectionIICoveragesTab();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
			section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
			section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Cow, 2);
		}
		
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//		propertyLocations.setAcres(10);
		propertyLocations.clickOK();


        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()){
			quote.clickPreQuoteDetails();
        }
		
		sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		
		if(!uwIssues.isInList(PLUWIssues.AtLeastOneResidence.getLongDesc()).equals(UnderwriterIssueType.BlockQuote)){
            Assert.fail("Expected error Bind Bind : " + PLUWIssues.AtLeastOneResidence.getShortDesc() + " is not displayed");
		}
	}

}
