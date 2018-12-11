package regression.r2.noclock.policycenter.documents.standardfire;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
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

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE APR 04, 2016
 */
public class TestStandardFireSubmissionFormInference extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy stdirePolObj, stdFireLiab_Squire_PolicyObj;
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
	private Underwriters uw;
	
	@Test
	public void testCreateStdFireFullApp() throws Exception {
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
		property2.setDwellingVacant(true);
		property2.setNumberOfUnits(NumberOfUnits.FourUnits);
		property2.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);

		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty();
		property3.setpropertyType(PropertyTypePL.DwellingPremisesCovE);
		property3.setDwellingVacant(true);
		property3.setNumberOfUnits(NumberOfUnits.FourUnits);
		property3.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);


		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty();
		property4.setpropertyType(PropertyTypePL.AlfalfaMill);
		property4.setDwellingVacant(true);
		property4.setNumberOfUnits(NumberOfUnits.FourUnits);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);

		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty();
		property5.setpropertyType(PropertyTypePL.CondominiumDwellingPremises);		
		property5.setNumberOfUnits(NumberOfUnits.FourUnits);

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);		
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumResidence(12);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);

        stdirePolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Test", "Issuance")
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.FullApp);
	}
	
	@Test (dependsOnMethods = { "testCreateStdFireFullApp" })
	public void testStdValidateStdFireSubmissionForms() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),stdirePolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		sideMenu.clickSideMenuSquireProperty();
		sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		
		//105 Form not exists for submission
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        excConds.clickCoveragesExclusionsAndConditions();
        ArrayList<String> descs = new ArrayList<String>();
		descs.add("test1desc");
		descs.add("test2desc");
		excConds.clickSpecialEndorsementForProperty105(descs);
		excConds.clickCoveragesTab();
		//this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionSpecialEndorsemenForProperty105);
		//110,134,106 Form
        covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		covs.clickBuildingsExclusionsAndConditions();
		ArrayList<String> descs2 = new ArrayList<String>();
		descs2.add("test1desc2");
		descs2.add("test2desc2");
		covs.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);
		covs.clickLimitedRoofCoverage();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134);
		covs.clickCashValueLimitationForRoof();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeCondo);		
		//168 Form
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		propDet.clickViewOrEditBuildingButton(1);
		propDet.setRisk("A");
		propDet.clickPropertyConstructionTab();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setFoundationType(FoundationType.None);		
		constructionPage.clickOK();
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeModularWithoutFoundation168);
		
		sideMenu.clickSideMenuSquireProperty();
		sideMenu.clickSideMenuSquirePropertyDetail();
		//169 Form
		propDet.clickViewOrEditBuildingButton(2);
		propDet.setRisk("A");
		propDet.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.MobileHome);
		constructionPage.setFoundationType(FoundationType.None);
        constructionPage.setSerialNumber("1234");
		constructionPage.setMake("TestMake2");
		constructionPage.setModel("TestModel2");
		constructionPage.clickOK();

		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 2);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_AddConstructionTypeMobileWithoutFoundation169);
		//146 Form
		covs.clickSpecificBuilding(1, 4);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.BroadForm);		
		this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdFire_CoverageEBroadFormEndorsement146);
		//173 Form
		sideMenu.clickSideMenuSquirePropertyDetail();
		propDet.clickViewOrEditBuildingButton(3);
		propDet.setRisk("A");
		propDet.setPropertyType(Property.PropertyTypePL.Shop);
		propDet.clickPropertyConstructionTab();	

		constructionPage.setYearBuilt(2013);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(1234);
		constructionPage.setFoundationType(FoundationType.Slab);
		constructionPage.setRoofType(RoofType.CompositionShingles);
		constructionPage.setPolyurethaneAndSandwichAndDescription(true, false, "Sandwich Test");
		constructionPage.clickOK();
        propDet.clickViewOrEditBuildingButton(4);
		propDet.setRisk("A");
		constructionPage.clickOK();

		propDet.clickViewOrEditBuildingButton(5);
		propDet.setRisk("A");
		constructionPage.clickOK();

		sideMenu.clickSideMenuSquirePropertyCoverages();
		covs.clickSpecificBuilding(1, 3);
		covs.setCoverageELimit(123123);
		covs.setCoverageECoverageType(CoverageType.Peril_1Thru8);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();		
		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

		String errorMessage = "Account Number: " + stdirePolObj.accountNumber;
		boolean testfailed = false;
		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
			testfailed = true;
			errorMessage = errorMessage + " ERROR: Documents for Submission In Expected But Not in UserInterface - "
					+ this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
		}
		
		sideMenu.clickSideMenuQuote();
        quote.clickSaveDraftButton();
		new GuidewireHelpers(driver).logout();

		if (testfailed) {
			Assert.fail(errorMessage);
		}		
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
            stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
		}

		@Test (dependsOnMethods = {"testCreateStandardFireWithSquire"})
		private void validateHopsFormInference() throws Exception{
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
            uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

            new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);
            SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPolicyInfo();

            GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
			polInfo.clickEditPolicyTransaction();
			sideMenu.clickSideMenuSquireProperty();

            GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
			quote.clickSaveDraftButton();	
			quote.clickQuote();	
			//UW I136
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

			if (testfailed) {
				Assert.fail(errorMessage);
			}
		}
		
}
