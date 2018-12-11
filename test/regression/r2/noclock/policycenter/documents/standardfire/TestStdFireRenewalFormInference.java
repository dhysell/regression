package regression.r2.noclock.policycenter.documents.standardfire;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.DocFormEvents.PolicyCenter;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.renewal.StartRenewal;
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
public class TestStdFireRenewalFormInference extends BaseTest {
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
		property1.setpropertyType(PropertyTypePL.DwellingPremises);
		property1.setConstructionType(ConstructionTypePL.Frame);
		property1.setFoundationType(FoundationType.FullBasement);		

		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty();
		property2.setpropertyType(PropertyTypePL.VacationHomeCovE);					

		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty();
		property3.setpropertyType(PropertyTypePL.DwellingPremisesCovE);			

		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty();
		property4.setpropertyType(PropertyTypePL.AlfalfaMill);

		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty();
		property5.setpropertyType(PropertyTypePL.CondominiumDwellingPremises);			

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);		
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumResidence(12);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);

        stdFirePolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Test", "Renewal")
				.withPolTermLengthDays(79)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test (dependsOnMethods = { "testCreateStdFireIssuance" })
	public void testStdFireRenewalForms() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),stdFirePolObj.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(stdFirePolObj);

        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
		searchJob.searchJobByAccountNumber(stdFirePolObj.accountNumber, "003");
        SideMenuPC sideMenu = new SideMenuPC(driver);

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

		this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionSpecialEndorsemenForProperty105);
		//UW I110, UW I134
        covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		covs.clickBuildingsExclusionsAndConditions();
		ArrayList<String> descs2 = new ArrayList<String>();
		descs2.add("test1desc2");
		descs2.add("test2desc2");
		covs.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);
		covs.clickLimitedRoofCoverage();

		this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134);
		//UW I106, UW I120
		covs.clickCashValueLimitationForRoof();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeCondo);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		propDet.clickViewOrEditBuildingButton(1);		
		propDet.clickPropertyConstructionTab();

		//UW I168, UW I117, UW I131
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setFoundationType(FoundationType.None);		
		constructionPage.clickOK();		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeModularWithoutFoundation168);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_CosmeticRoofDamageEndorsement117);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_LimitedFungiWetorDryRotorBacteriaEndorsement131);

		sideMenu.clickSideMenuSquireProperty();
		sideMenu.clickSideMenuSquirePropertyDetail();

		propDet.clickViewOrEditBuildingButton(2);	
		propDet.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setFoundationType(FoundationType.None);
        constructionPage.setSerialNumber("1234");
		constructionPage.setMake("TestMake2");
		constructionPage.setModel("TestModel2");
		constructionPage.clickOK();

		//UW I169
		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 2);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);			
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeMobileWithoutFoundation169);

		//UW I146
		covs.clickSpecificBuilding(1, 4);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.BroadForm);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_CoverageEBroadFormEndorsement146);

		sideMenu.clickSideMenuSquirePropertyDetail();
		propDet.clickViewOrEditBuildingButton(3);		
		propDet.setPropertyType(Property.PropertyTypePL.Shop);
		propDet.clickPropertyConstructionTab();	

		constructionPage.setYearBuilt(2013);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(1234);
		constructionPage.setFoundationType(FoundationType.Slab);
		constructionPage.setRoofType(RoofType.CompositionShingles);
		constructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "Sandwich Test");
		constructionPage.clickOK();

		//UW I173
		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 3);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeShopWithPolyNoSandwich173);
		//UW 10 18
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AdverseActionLetter);		
		//ID SF 03 01
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_StandardFirePolicyDeclarations);
		//LG 10 01
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_NoticeOfPolicyOnPrivacy);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();


		sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				this.formsOrDocsActualFromUISubmissionPlusLocal,
				this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		String errorMessage = "Account Number: " + stdFirePolObj.accountNumber;
		boolean testfailed = false;
		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + " ERROR: Documents for Renewal In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}
		sideMenu.clickSideMenuQuote();
        StartRenewal renewal = new StartRenewal(driver);
		renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);

        new GuidewireHelpers(driver).logout();

		if (testfailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = {"testStdFireRenewalForms"})
	private void testStdFireRenewalDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirePolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Renewal");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {	
				"Standard Fire Policy Declarations",			
				"Notice of Policy on Privacy", 			
				"Adverse Action Letter",			
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
					.withPolTermLengthDays(79)
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
            stdFireLiab_Squire_PolicyObj.standardFire.setPolTermLengthDays(79);
            stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
            stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
		}

		@Test (dependsOnMethods = {"testCreateStandardFireWithSquire"})
		private void validateHopsRenewalFormInference() throws Exception{
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
            new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());
            SideMenuPC sideMenu = new SideMenuPC(driver);
            PolicyMenu policyMenu = new PolicyMenu(driver);
			policyMenu.clickRenewPolicy();

            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            preRenewalPage.closePreRenewalExplanations(stdFireLiab_Squire_PolicyObj);

            SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
            searchJob.searchJobByAccountNumberAndJobType(stdFireLiab_Squire_PolicyObj.accountNumber, TransactionType.Renewal);

			sideMenu.clickSideMenuSquireProperty();
			sideMenu.clickSideMenuSquirePropertyCoverages();

            GenericWorkorderSquirePropertyAndLiabilityCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
			covs.clickCoveragesExclusionsAndConditions();		
            //UW I136
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
			this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdFire_PropertyPolicyBooklet);

            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
			sideMenu.clickSideMenuRiskAnalysis();
            risk.Quote();


			sideMenu.clickSideMenuForms();

            GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
			this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
			this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
					.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
			this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
					this.formsOrDocsActualFromUISubmissionPlusLocal,
					this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

            String errorMessage = "Account Number: " + stdFireLiab_Squire_PolicyObj.accountNumber;
			boolean testfailed = false;
			if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
				testfailed = true;
				errorMessage = errorMessage + "ERROR: Documents for Renewal In Expected But Not in UserInterface - "
						+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
			}
			sideMenu.clickSideMenuQuote();
            StartRenewal renewal = new StartRenewal(driver);
			renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);

            new GuidewireHelpers(driver).logout();

			if (testfailed) {
				Assert.fail(errorMessage);
			}

		}

		@Test(dependsOnMethods = {"validateHopsRenewalFormInference"})
		private void testStdFireTrellisedHopsDocuments() throws Exception {
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);

            new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());
            SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuToolsDocuments();
            PolicyDocuments docs = new PolicyDocuments(driver);
			docs.selectRelatedTo("Renewal");
			docs.clickSearch();
            ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

			String[] documents = {"Standard Fire Policy Declarations",
					               "Hops Property Coverage Part Value Reporting Basis",			
					               "Notice of Policy on Privacy", 			
					               "Adverse Action Letter",};

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


