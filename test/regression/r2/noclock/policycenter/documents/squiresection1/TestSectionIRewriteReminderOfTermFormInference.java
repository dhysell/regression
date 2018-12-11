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
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FPPOptionalCoverages;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
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
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.rewrite.StartRewrite;
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
public class TestSectionIRewriteReminderOfTermFormInference extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySqSectionIFRPol;
	private Underwriters uw;
	private SquireEligibility squireType;
	private String FPPAdditionalInterestLastName = "FPPPerson" + StringsUtils.generateRandomNumberDigits(7);

	@Test
	public void testBoundSquire() throws Exception {
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

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -5);

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
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("FormInference", "SectionI")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
	}

	@Test(dependsOnMethods = { "testBoundSquire" })
	private void testIssueSquireWithSectionICoverages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.mySqSectionIFRPol.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();

		sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);


		GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        exclusions.clickCoveragesExclusionsAndConditions();
		// 105 Form
		ArrayList<String> descs = new ArrayList<String>();
		descs.add("test1desc");
		exclusions.clickSpecialEndorsementForProperty105(descs);
		
		// 205 form
		exclusions.clickSpecialEndorsementForLiability205(descs);
		
		// 207 Form
		exclusions.clickVendorAsAdditionalInsuredEndorsement207(descs);
		
		// 280 form
		exclusions.clickCanineExclusionEndorsement280();
		
		// 291 form
		exclusions.clickAdditionalInsuredLandlordEndorsement291("testname", "testDescr");
		// Coverages - Property Exclusions & Conditions
		exclusions.clickCoveragesTab();
        // 126, 127 Earthquake
		coverages.setIncludeMasonry(true);
		//this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_EarthquakeDamageEndorsement);
		
		// Property exclusions and conditions
		coverages.clickBuildingsExclusionsAndConditions();

		// 107 Form
		coverages.clickDetachedGarageEndorsement(true);
		
		// 106 Form
		coverages.clickCashValueLimitationForRoof();
		
		// 110 Form
		ArrayList<String> descs2 = new ArrayList<String>();
		descs2.add("test1desc2");
		coverages.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);

		// 134 Form
		coverages.clickLimitedRoofCoverage();
		
		coverages.clickBuildingsCoverages();
		// 128 - loss of income
		coverages.clickSpecificBuilding(1, 5);
        // 145
		coverages.setCoverageECoverageType(CoverageType.SpecialForm);
		
		// 128
		coverages.setLossIncomeExtraExpense(1000);
		
		if (squireType.equals(SquireEligibility.Country) || squireType.equals(SquireEligibility.FarmAndRanch)) {
			sideMenu.clickSideMenuHouseholdMembers();
            GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
			household.clickSearch();
            GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
			SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
			addressBook.searchAddressBookByFirstLastName(mySqSectionIFRPol.basicSearch, "FPPADD", this.FPPAdditionalInterestLastName,
					this.mySqSectionIFRPol.pniContact.getAddress().getLine1(),
					this.mySqSectionIFRPol.pniContact.getAddress().getCity(),
					this.mySqSectionIFRPol.pniContact.getAddress().getState(),
					this.mySqSectionIFRPol.pniContact.getAddress().getZip(), CreateNew.Create_New_Always);
            householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));

			householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
			householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
            householdMember.setNewPolicyMemberAlternateID(this.FPPAdditionalInterestLastName);
			householdMember.selectNotNewAddressListingIfNotExist(mySqSectionIFRPol.pniContact.getAddress());

			householdMember.clickRelatedContactsTab();
			householdMember.clickBasicsContactsTab();

			householdMember.clickOK();

			sideMenu.clickSideMenuSquirePropertyCoverages();


			GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
            fpp.clickFarmPersonalProperty();
            fpp.checkCoverageD(true);
			fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
			fpp.selectDeductible(FPPDeductible.Ded_1000);
			fpp.selectCoverages(FarmPersonalPropertyTypes.Livestock);
			fpp.addItem(FPPFarmPersonalPropertySubTypes.Cows, 200, 10000, "Testing purpose");
			fpp.setFarmPersonalPropertyRisk("A");
			
			fpp.selectCoverages(FarmPersonalPropertyTypes.Machinery);
			fpp.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 1, 1000, "Testing Description");

			// 163
			fpp.selectOptionalCoverageType(FPPOptionalCoverages.FreezingLivestock);
			fpp.setAdditionalCoveragesLimit(FPPOptionalCoverages.FreezingLivestock, 2000);
			
			// 763
			fpp.clickFPPAdditionalInterests();
			GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
			fppInts.addExistingOtherContactsAdditionalInterest(this.FPPAdditionalInterestLastName);
			GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
			additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
			additionalInterests.setAdditionalInterestsLoanNumber("1234567");			
			additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
            AdditionalInterest ai = new AdditionalInterest(
					ContactSubType.Person);
			ai.setPersonFirstName("FPPADD");
			ai.setPersonLastName(this.FPPAdditionalInterestLastName);
			ai.setLoanContractNumber("1234567");
			fppInts.addItem(FPPFarmPersonalPropertySubTypes.Tractors, "Testing Purpose", 100,
					"13324435435", ai);
		
			fpp.clickSectionIICoveragesTab();
			GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
			section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
			section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Cow, 200);
			fpp.clickFarmPersonalProperty();

			fpp.clickCoveragesExclusionsAndConditions();

			exclusions.clickCoverageDExtensionEndorsement108("Test1Desc");
			
			// 209
			exclusions.clickAccessYesEndorsement209();
			
			// 140
			exclusions.setCoverageDAdditionalInsuredEndo140("TestName", "Test1Desc");
		
			// 143
			exclusions.setAdditionalLivestockEndorsement143("cows", 100, 1000, 10000, "Test1Desc");
			
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
		
		// 169
		propertyDetail.clickViewOrEditBuildingButton(3);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setFoundationType(FoundationType.None);
		propertyDetail.clickOk();
		
		// 173
		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.Shop);
		property.setConstructionType(ConstructionTypePL.Frame);
		property.setFoundationType(FoundationType.FullBasement);
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
		sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        if (new ErrorHandlingHelpers(driver).errorExistsValidationResults()) {
			if (new ErrorHandlingHelpers(driver).getValidationResultsErrorText().contains("Risk and Category must be entered on all properties and Farm and Personal Property")) {
                GenericWorkorder workflow = new GenericWorkorder(driver);
				new GuidewireHelpers(driver).editPolicyTransaction();

				sideMenu.clickSideMenuSquirePropertyDetail();

				for(PolicyLocation location : this.mySqSectionIFRPol.squire.propertyAndLiability.locationList){
					propertyDetail.clickLocation(location);
					for (PLPolicyLocationProperty building :location.getPropertyList()) {
						propertyDetail.clickViewOrEditBuildingButton(building.getPropertyNumber());
                        propertyDetail.setRisk("A");
						propertyDetail.clickOk();
                    }
				}
                workflow = new GenericWorkorder(driver);
				workflow.clickGenericWorkorderQuote();
				if (quote.isPreQuoteDisplayed()) {
					quote.clickPreQuoteDetails();
				}
				sideMenu.clickSideMenuRiskAnalysis();
				risk.approveAll_IncludingSpecial();
                sideMenu.clickSideMenuQuote();

				
				quote.issuePolicy(IssuanceType.NoActionRequired);
            }
			
		}
        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
		new GuidewireHelpers(driver).logout();

		if (testfailed) {
			Assert.fail(errorMessage);
		}

	}
	
	@Test(dependsOnMethods = { "testIssueSquireWithSectionICoverages" })
	public void testCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqSectionIFRPol.accountNumber);

        Date currentDate = mySqSectionIFRPol.squire.getEffectiveDate();
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 2);
        StartCancellation cancelPol = new StartCancellation(driver);

		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
				cancellationDate, true);
        new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "testCancellation" })
	private void testRewriteReminderOfTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),this.mySqSectionIFRPol.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteRemainderOfTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();

        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		for (int i = 0; i < 8; i++) {
			eligibilityPage.clickNext();
        }

		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();
        coverages.clickCoveragesExclusionsAndConditions();

		for (int i = 0; i < 4; i++) {
			eligibilityPage.clickNext();
        }

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuForms();

		sideMenu.clickSideMenuQuote();
		rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        new GuidewireHelpers(driver).logout();

	}
	
	@Test(dependsOnMethods = { "testRewriteReminderOfTerm" })
	private void testValidateSectionIDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySqSectionIFRPol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Rewrite Remainder");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		List<String> documentList = new ArrayList<String>();
		documentList.add("Special Endorsement for Property");
		documentList.add("Actual Cash Value Limitation for Roofing Endorsement");
		documentList.add("Coverage A (Dwellings) Detached Garage and Storage Shed");
		documentList.add("Specified Property Exclusion Endorsement");
		documentList.add("Condo Unit Endorsement");
		documentList.add("Sewage or Sump System Backup Endorsement");
		documentList.add("Earthquake Damage Endorsement - Masonry");
		documentList.add("Loss of Income and Extra Expense Endorsement");
		documentList.add("Limited Roof Coverage Endorsement");
		documentList.add("Coverage E Special Endorsement");
		documentList.add("Modular Mobile Home Endorsement - Excluding Peril 17");
		documentList.add("Mobile Home Endorsement");
		documentList.add("Open Flame Warranty Endorsement");		
		
		if(this.squireType.equals(SquireEligibility.Country) || this.squireType.equals(SquireEligibility.FarmAndRanch)){
			documentList.add("Freezing of Livestock Endorsement");
			documentList.add("Leasing Endorsement - Sections I & II");
			documentList.add("Coverage D Extension Endorsement");
			documentList.add("Access Yes Endorsement");
			documentList.add("Coverage D Additional Insured Endorsement");
			documentList.add("Additional LIvestock Endorsement");		
			
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

			if (documentFound) {
				testFailed = true;
				errorMessage = errorMessage + "UnExpected document : '" + document
						+ "' available in documents page. \n";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);

	}
}
