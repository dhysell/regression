package regression.r2.noclock.policycenter.documents.squiresection1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FPPOptionalCoverages;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.Measurement;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/84420444948">Link Text</a>
 * @Description
 * @DATE Apr 12, 2017
 */
@QuarantineClass
public class TestSectionIPolicyChangeFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySqSectionIFRPol;

	// **************************************************************************
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
	private Underwriters uw;
	private SquireEligibility squireType;
	private String FPPAdditionalInterestLastName = "FPPPerson" + StringsUtils.generateRandomNumberDigits(7);

	@Test
	public void testIssueSquire() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
		prop1.setpropertyType(PropertyTypePL.ResidencePremises);
		prop1.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty prop2 = new PLPolicyLocationProperty();
		prop2.setpropertyType(PropertyTypePL.CondominiumDwellingPremises);

		PLPolicyLocationProperty prop3 = new PLPolicyLocationProperty();
		prop3.setpropertyType(PropertyTypePL.VacationHome);

		PLPolicyLocationProperty prop4 = new PLPolicyLocationProperty();
		prop4.setpropertyType(PropertyTypePL.Barn);

		PLPolicyLocationProperty prop5 = new PLPolicyLocationProperty();
		prop5.setpropertyType(PropertyTypePL.DairyComplex);

		locOnePropertyList.add(prop1);
		locOnePropertyList.add(prop2);
		locOnePropertyList.add(prop3);
		locOnePropertyList.add(prop4);
		locOnePropertyList.add(prop5);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);

		locToAdd.setPlNumAcres(12);
		locToAdd.setPlNumResidence(10);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySqSectionIFRPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withSquireEligibility(new GuidewireHelpers(driver).getRandomEligibility())
                .withInsFirstLastName("FormInference", "SectionI")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

		eventsHitDuringSubmissionCreationPlusLocal.addAll(this.mySqSectionIFRPol.policyForms.eventsHitDuringSubmissionCreation);
		formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal
				.addAll(this.mySqSectionIFRPol.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);
		formsOrDocsActualFromUISubmissionPlusLocal.addAll(this.mySqSectionIFRPol.policyForms.formsOrDocsActualFromUISubmission);
		actualExpectedDocDifferencesSubmissionPlusLocal.setInExpectedNotInUserInterface(
				this.mySqSectionIFRPol.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface());
		actualExpectedDocDifferencesSubmissionPlusLocal.setInUserInterfaceNotInExpected(
				this.mySqSectionIFRPol.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected());
	}

	@Test(dependsOnMethods = { "testIssueSquire" })
	public void testAddSectionICoverages() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				mySqSectionIFRPol.accountNumber);

        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);

		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_PersonalLinesApplication);		
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_CondoUnitEndorsement);
		
		if(this.squireType.equals(SquireEligibility.City)){
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_CitySquirePolicy);
		}else if(this.squireType.equals(SquireEligibility.Country)){
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_CountrySquirePolicy);
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_LimitedPollutionCoverageEndorsement);
		}else{
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_FarmRanchSquirePolicyBooklet);
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_LimittedPollutionBasicCoverage);
		}
		
		sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickCoveragesExclusionsAndConditions();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);

		// 105 Form
		ArrayList<String> descs = new ArrayList<String>();
		descs.add("test1desc");
		exclusions.clickSpecialEndorsementForProperty105(descs);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecialEndorsementProperty);

		// 205 form
		exclusions.clickSpecialEndorsementForLiability205(descs);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecialEndorsementLiabiliy);

		// 207 Form
		exclusions.clickVendorAsAdditionalInsuredEndorsement207(descs);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_VendorAdditionalInsuredEndorsement);

		// 280 form
		exclusions.clickCanineExclusionEndorsement280();
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_CanineExclusionEndorsement);

		// 291 form
		exclusions.clickAdditionalInsuredLandlordEndorsement291("testname", "testDescr");
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_AdditionalInsuredLandlordEndorsement);
		// Coverages - Property Exclusions & Conditions
		exclusions.clickCoveragesTab();
        // 126, 127 Earthquake
		coverages.setIncludeMasonry(true);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_EarthquakeDamageEndorsementMasonry);

		// Property exclusions and conditions
		coverages.clickBuildingsExclusionsAndConditions();

		// 107 Form
		coverages.clickDetachedGarageEndorsement(true);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageADetachedGarageStorageShed);

		// 106 Form
		coverages.clickCashValueLimitationForRoof();
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);

		// 110 Form
		ArrayList<String> descs2 = new ArrayList<String>();
		descs2.add("test1desc2");
		coverages.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecifiedPropertyExclusionEndorsement);

		// 134 Form
		coverages.clickLimitedRoofCoverage();
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_LimitedRoofCoverageEndorsement);

		coverages.clickBuildingsCoverages();
        coverages.clickSpecificBuilding(1, 2);
        coverages.setIncludeMasonry(true);
		
		// 128 - loss of income
		coverages.clickSpecificBuilding(1, 5);
        // 145
		coverages.setCoverageECoverageType(CoverageType.SpecialForm);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageESpecialFormEndorsment);

		// 128
		coverages.setLossIncomeExtraExpense(1000);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_LossOfIncomeExtraExpenseEndorsement);

		if (squireType.equals(SquireEligibility.Country) || squireType.equals(SquireEligibility.FarmAndRanch)) {
			sideMenu.clickSideMenuHouseholdMembers();
            GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
			household.clickSearch();
            GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
            SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
			addressBook.searchAddressBookByFirstLastName(this.mySqSectionIFRPol.basicSearch, "FPPADD", this.FPPAdditionalInterestLastName,
					this.mySqSectionIFRPol.pniContact.getAddress().getLine1(),
					this.mySqSectionIFRPol.pniContact.getAddress().getCity(),
					this.mySqSectionIFRPol.pniContact.getAddress().getState(),
					this.mySqSectionIFRPol.pniContact.getAddress().getZip(), CreateNew.Create_New_Always);
            householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));

			householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
			householdMember.setSSN("1"+StringsUtils.generateRandomNumberDigits(8));
            householdMember.setNewPolicyMemberAlternateID(this.FPPAdditionalInterestLastName);
			
			householdMember.selectNotNewAddressListingIfNotExist(mySqSectionIFRPol.pniContact.getAddress());

			householdMember.clickRelatedContactsTab();
			householdMember.clickBasicsContactsTab();

			householdMember.clickOK();

			sideMenu.clickSideMenuSquirePropertyCoverages();
			coverages.clickFarmPersonalProperty();

            GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
			fpp.checkCoverageD(true);
			fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
			fpp.selectDeductible(FPPDeductible.Ded_1000);
			fpp.selectCoverages(FarmPersonalPropertyTypes.Livestock);
			fpp.addItem(FPPFarmPersonalPropertySubTypes.Cows, 200, 10000, "Testing purpose");
			coverages.setFarmPersonalPropertyRisk("A");

			fpp.selectCoverages(FarmPersonalPropertyTypes.Machinery);
			fpp.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 1, 1000, "Testing Description");

			// 163
			fpp.selectOptionalCoverageType(FPPOptionalCoverages.FreezingLivestock);
			fpp.setAdditionalCoveragesLimit(FPPOptionalCoverages.FreezingLivestock, 2000);
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_FreezingLivestockEndorsement);

			// 763
			fpp.clickFPPAdditionalInterests();
            GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
			fppInts.addExistingOtherContactsAdditionalInterest(this.FPPAdditionalInterestLastName);
            GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
			additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
			additionalInterests.setAdditionalInterestsLoanNumber("1234567");
			additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
            AdditionalInterest ai = new AdditionalInterest(ContactSubType.Person);
			ai.setPersonFirstName("FPPADD");
			ai.setPersonLastName(this.FPPAdditionalInterestLastName);
			ai.setLoanContractNumber("1234567");
			fppInts.addItem(FPPFarmPersonalPropertySubTypes.Tractors, "Testing Purpose", 100, "13324435435", ai);
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_LeasongEndorsmentSectionISectionII);

			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_AdditionalInterestDeclarations);

			coverages.clickSectionIICoveragesTab();
            GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
			section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
			section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Cow, 200);
			coverages.clickFarmPersonalProperty();

			coverages.clickCoveragesExclusionsAndConditions();

			exclusions.clickCoverageDExtensionEndorsement108("Test1Desc");
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageDExtensionEndorsement);

			// 209
			exclusions.clickAccessYesEndorsement209();
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_AccessYesEndorsement);

			// 140
			exclusions.setCoverageDAdditionalInsuredEndo140("TestName", "Test1Desc");
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageDAdditionalInsuredEndorsement);

			// 143
			exclusions.setAdditionalLivestockEndorsement143("cows", 100, 1000, 10000, "Test1Desc");
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_AdditionalLivestockEndorsement);

			//
			this.eventsHitDuringSubmissionCreationPlusLocal
					.add(DocFormEvents.PolicyCenter.Squire_Liab_RawMilkExclusionEndorsement);

		}
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);

		// 168
		propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setFoundationType(FoundationType.None);
        constructionPage.setSerialNumber("1234");
		constructionPage.setMake("TestMake2");
		constructionPage.setModel("TestModel2");
		propertyDetail.clickOk();
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_ModularMobileHomeEndorsement);

		// 169
		propertyDetail.clickViewOrEditBuildingButton(3);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setFoundationType(FoundationType.None);
		propertyDetail.clickOk();
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_MobileHomeEndorsement);

		// 173
		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.Shop);
		property.setConstructionType(ConstructionTypePL.Frame);
		property.setFoundationType(FoundationType.FullBasement);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_OpenFlameWarrantyEndorsment);
		propertyDetail.clickAdd();
		propertyDetail.setPropertyType(property.getpropertyType());
		propertyDetail.setRisk("A");
		propertyDetail.clickPropertyConstructionTab();
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
		coverages.clickSpecificBuilding(1, BuildingNumber);
		coverages.setCoverageELimit(3000);
		coverages.setCoverageECoverageType(CoverageType.SpecialForm);
		coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
		this.eventsHitDuringSubmissionCreationPlusLocal
				.add(DocFormEvents.PolicyCenter.Squire_Liab_OpenFlameWarrantyEndorsment);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();
        boolean testfailed = false;
		String errorMessage = "";
		sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);

		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);
		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + "ERROR: Documents for Issuance In UserInterface But Not in Expected - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected() + "\n";
		}

		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + "ERROR: Documents for Issuance In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}

		sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

		if (testfailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = { "testAddSectionICoverages" })
	private void testValidateSectionIDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.mySqSectionIFRPol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Change");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		List<String> documentList = new ArrayList<String>();
		documentList.add("Special Endorsement for Property");
		documentList.add("Actual Cash Value Limitation for Roofing Endorsement");
		documentList.add("Coverage A (Dwellings) Detached Garage and Storage Shed");
		documentList.add("Specified Property Exclusion Endorsement");
		documentList.add("Earthquake Damage Endorsement - Masonry");
		documentList.add("Loss of Income and Extra Expense Endorsement");
		documentList.add("Limited Roof Coverage Endorsement");
		documentList.add("Coverage E Special Endorsement");
		documentList.add("Modular Mobile Home Endorsement - Excluding Peril 17");
		documentList.add("Mobile Home Endorsement");
		documentList.add("Open Flame Warranty Endorsement");

		if (this.squireType.equals(SquireEligibility.Country)
				|| this.squireType.equals(SquireEligibility.FarmAndRanch)) {
			documentList.add("Freezing of Livestock Endorsement");
			documentList.add("Leasing Endorsement - Sections I & II");
			documentList.add("Coverage D Extension Endorsement");
			documentList.add("Access Yes Endorsement");
			documentList.add("Coverage D Additional Insured Endorsement");
			documentList.add("Additional Livestock Endorsement");

		}

		boolean testFailed = false;
		String errorMessage = "Account Number: " + mySqSectionIFRPol.accountNumber;
		for (String document : documentList) {
			boolean documentFound = false;
			for (String desc : descriptions) {
				if (desc.equals(document) || desc.contains(document)) {
					documentFound = true;
					break;
				}
			}

			if (!documentFound) {
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '" + document
						+ "' not available in documents page. \n";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);

	}

}