package regression.r2.noclock.policycenter.documents.standardfire;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.Wiring;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
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
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE Mar 29, 2016
 */
@QuarantineClass
public class TestStandardFirePolicyChangeFormsInference extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy stdFirePolObj, stdFireLiab_Squire_PolicyObj;
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();	
	private Underwriters uw;

	@Test
	public void testCreateStdFireIssuance() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty();
		property1.setpropertyType(PropertyTypePL.VacationHomeCovE);
		property1.setDwellingVacant(true);				

		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty();
		property2.setpropertyType(PropertyTypePL.DwellingPremisesCovE);
		property2.setDwellingVacant(true);		

		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty();
		property3.setpropertyType(PropertyTypePL.AlfalfaMill);
		property3.setDwellingVacant(true);		

		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty();
		property4.setpropertyType(PropertyTypePL.CondominiumDwellingPremisesCovE);
		
		locOnePropertyList.add(property1);		
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumResidence(12);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);

        stdFirePolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Test", "PolicyChange")
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test (dependsOnMethods = { "testCreateStdFireIssuance" })
	public void testStdFirePolicyChangeForms() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),stdFirePolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		sideMenu.clickSideMenuSquireProperty();
		sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		
		//UW I105
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        excConds.clickCoveragesExclusionsAndConditions();
        ArrayList<String> descs = new ArrayList<String>();
		descs.add("test1desc");
		descs.add("test2desc");
		excConds.clickSpecialEndorsementForProperty105(descs);
		excConds.clickCoveragesTab();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionSpecialEndorsemenForProperty105);

        covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		covs.clickBuildingsExclusionsAndConditions();
		ArrayList<String> descs2 = new ArrayList<String>();
		descs2.add("test1desc2");		
		covs.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);
		//UW I110, UW I134
		covs.clickLimitedRoofCoverage();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134);
		//UW I106, UW I120
		covs.clickCashValueLimitationForRoof();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeCondo);


		//Property
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetails.clickAdd();
		propertyDetails.setPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetails.setDwellingVacantRadio(false);
		propertyDetails.setHowOrByWhomIsThisOccupied("Supernaturally");
		propertyDetails.setUnits(NumberOfUnits.OneUnit);		
		propertyDetails.setSwimmingPoolRadio(false);
		propertyDetails.setWaterLeakageRadio(false);
		propertyDetails.setExoticPetsRadio(false);

		propertyDetails.setRisk("A");

		AddressInfo bankAddress = new AddressInfo();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc2Bldg1AddInterest.setAddress(bankAddress);		
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchAddressBookByCompanyName(stdFirePolObj.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

		propertyDetails.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setYearBuilt(1993);
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);

		constructionPage.setBathClass(KitchenBathClass.Basic);
		constructionPage.setSquareFootage(1700);
		constructionPage.setStories(NumberOfStories.One);
		constructionPage.setBasementFinished("90");
		constructionPage.setGarage(Garage.AttachedGarage);

		constructionPage.setCoveredPorches(false);
		constructionPage.setFoundationType(FoundationType.None);
		constructionPage.setRoofType(RoofType.WoodShingles);
		constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
		constructionPage.setPlumbing(Plumbing.Copper);
		constructionPage.setWiring(Wiring.Copper);
		constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
		constructionPage.setAmps(100);
		constructionPage.setKitchenClass(KitchenBathClass.Basic);
		constructionPage.clickOK();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 5);
		coverages.setCoverageALimit(200200);
		coverages.setCoverageCLimit(20000);
		coverages.selectOtherCoverageCValuation(ValuationMethod.ActualCashValue);	
		coverages.clickNext();
		//UW I168, UW I117, UW I131
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeModularWithoutFoundation168);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_CosmeticRoofDamageEndorsement117);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_LimitedFungiWetorDryRotorBacteriaEndorsement131);		
		sideMenu.clickSideMenuSquireProperty();
		sideMenu.clickSideMenuSquirePropertyDetail();		

		propertyDetails.clickViewOrEditBuildingButton(1);
        additionalInterests.addExistingAdditionalInterest(loc2Bldg1AddInterest.getCompanyName());
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetails.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setFoundationType(FoundationType.None);
        constructionPage.setSerialNumber("1234");
		constructionPage.setMake("TestMake2");
		constructionPage.setModel("TestModel2");
		constructionPage.clickOK();
		//UW I169
		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 1);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeMobileWithoutFoundation169);
		//UW I146
		covs.clickSpecificBuilding(1, 3);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.BroadForm);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_CoverageEBroadFormEndorsement146);

		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetails.clickViewOrEditBuildingButton(2);		
		propertyDetails.setPropertyType(Property.PropertyTypePL.Shop);
        additionalInterests.addExistingAdditionalInterest(loc2Bldg1AddInterest.getCompanyName());
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetails.clickPropertyConstructionTab();	

		constructionPage.setYearBuilt(2013);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(1234);
		constructionPage.setFoundationType(FoundationType.Slab);
		constructionPage.setRoofType(RoofType.CompositionShingles);
		constructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "Sandwich Test");
		constructionPage.clickOK();	

		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetails.clickViewOrEditBuildingButton(3);
        additionalInterests.addExistingAdditionalInterest(loc2Bldg1AddInterest.getCompanyName());
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetails.clickOk();

		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetails.clickViewOrEditBuildingButton(4);
        additionalInterests.addExistingAdditionalInterest(loc2Bldg1AddInterest.getCompanyName());
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetails.clickOk();

		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 2);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);
		//UW I173
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeShopWithPolyNoSandwich173);
		//ID SF 03 01
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_StandardFirePolicyDeclarations);
		//PL 01 01
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_PersonalLinesApplication);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
		sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		String errorMessage = "Account Number: " + stdFirePolObj.accountNumber;
		boolean testfailed = false;
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

	@Test(dependsOnMethods = {"testStdFirePolicyChangeForms"})
	private void testStdFirePolicyChangeDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirePolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Change");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {			
				"Standard Fire Policy Declarations",					
				"Personal Lines Application",			
				"Special Endorsement for Property",				
				"Actual Cash Value Limitation for Roofing Endorsement",				
				"Specified Property Exclusion Endorsement",			
				"Cosmetic Roof Damage Endorsement",		
				"Condo Unit Endorsement", 	
				"Limited Fungi, Wet or Dry Rot, or Bacteria Endorsement",			
				"Limited Roof Coverage Endorsement",			
				"Coverage E Broad Form Endorsement",	
				"Modular Mobile Home Endorsement - Excluding Peril 17",			
				"Mobile Home Endorsement",				
		        "Open Flame Warranty Endorsement"};

		boolean testFailed = false;		String errorMessage = "Account Number: " + stdFirePolObj.accountNumber;
		for (String document : documents) {	
			boolean documentFound = false;
			for(String desc: descriptions){
				if(desc.equals(document)){
					documentFound = true;
					break;
				}
			}
			if(!documentFound){
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '"+document+ "' not available in documents page. \n";
			}
		}
		if(testFailed)
			Assert.fail(errorMessage);
	}

	//Trellised Hops Form 136

	@Test
	public void testCreateStandardFireWithSquire()  throws Exception {

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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("SQ", "Hops")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		//standard fire
		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.TrellisedHops));
		PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

		locToAdd1.setPlNumAcres(11);
		locationsList1.add(locToAdd1);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setStdFireCommodity(true);
        myStandardFire.setLocationList(locationsList1);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
        stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
	}

	@Test (dependsOnMethods = {"testCreateStandardFireWithSquire"})
	private void validateHopsPolicyChangeFormInference() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		propDet.clickViewOrEditBuildingButton(1);

		AddressInfo bankAddress = new AddressInfo();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc2Bldg1AddInterest.setAddress(bankAddress);		
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchAddressBookByCompanyName(stdFireLiab_Squire_PolicyObj.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propDet.clickOk();		
		sideMenu.clickSideMenuSquirePropertyCoverages();		

		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_HopsPropertyCoverage);		
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionSpecialEndorsemenForProperty105);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeModularWithoutFoundation168);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeMobileWithoutFoundation169);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeShopWithPolyNoSandwich173);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_CoverageEBroadFormEndorsement146);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeCondo);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_CosmeticRoofDamageEndorsement117);
		this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_LimitedFungiWetorDryRotorBacteriaEndorsement131);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
		sideMenu.clickSideMenuRiskAnalysis();
//		risk.specialApproveAll();
		risk.approveAll();

		sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + stdFireLiab_Squire_PolicyObj.accountNumber;
		boolean testfailed = false;
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

	@Test(dependsOnMethods = {"validateHopsPolicyChangeFormInference"})
	private void testStdFireTrellisedHopsDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Policy Change");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {"Standard Fire Policy Declarations","Hops Property Coverage Part Value Reporting Basis"};

        boolean testFailed = false;
        String errorMessage = "Account Number: " + stdFireLiab_Squire_PolicyObj.accountNumber;
		for (String document : documents) {	
			boolean documentFound = false;
			for(String desc: descriptions){
				if(desc.equals(document)){
					documentFound = true;
					break;
				}
			}
			if(!documentFound){
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '"+document+ "' not available in documents page. \n";
			}
		}
		if(testFailed)
			Assert.fail(errorMessage);
	}
}
