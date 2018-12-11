package regression.r2.noclock.policycenter.renewaltransition.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.DocFormEvents.PolicyCenter;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.RenewalCode;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement :US4770: [Part II] PL - Renewal Notifications
 * @Description : Issue Squire, Standard FL policies, Generate Renewal and add a policy change between 50 to 22th day
 * with effective renewal date and add Forms/Exclusion & Condition per requirements, check for add forms and also check
 * policy declaration forms that are not generated during 50 to 22 days.
 * @DATE Sep 30, 2016
 */
public class TestRenewalNotification2 extends BaseTest {

    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal2 = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private GeneratePolicy stdFireObj;
    private Underwriters uw;

    private WebDriver driver;

//	@Test
//	public void testGenerateSquirePoicy() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
//		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
//		coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
//		
//		// Vehicle
//		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
//		Vehicle toAdd = new Vehicle();
//		toAdd.setComprehensive(false);
//		toAdd.setCostNew(10000.00);
//		toAdd.setCollision(false);
//		toAdd.setAdditionalLivingExpense(false);
//		vehicleList.add(toAdd);
//
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//		locToAdd.setPlNumResidence(5);
//		locationsList.add(locToAdd);
//
//		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
//		squirePersonalAuto.setCoverages(coverages);
//		squirePersonalAuto.setVehicleList(vehicleList);		
//		SquireLiability liabilitySection = new SquireLiability();		
//		liabilitySection.setCoveragePrivateLandingStrips(true);
//		liabilitySection.setCoveragePrivateLandingStripsQuantity(1);
//		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = liabilitySection;
//
//
//        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
//        mySquire.squirePA = squirePersonalAuto;
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//		Squire_PolicyObj = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//				.withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
//				.withPolOrgType(OrganizationType.Individual)
//				.withPolTermLengthDays(50)
//				.withInsFirstLastName("Test", "Renewal")					
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);
//
//	}
//
//	@Test(dependsOnMethods = { "testGenerateSquirePoicy" })
//	public void testSquire_RenewalJob() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		//
//		// Login with UW	  
//		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());
//
//		//
//        StartRenewal renewal = new StartRenewal(driver);
//		renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk);
//	} 
//
//	@Test(dependsOnMethods = { "testSquire_RenewalJob" })
//	public void squirePolicyChange() throws Exception{
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());
//		
//		//		Date currentSystemDate = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);
//		//		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);
//
//		//start policy change
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//		policyChangePage.startPolicyChange("First policy Change", changeDate);
//
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
//		
//
//        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
//        exclusions.clickCoveragesExclusionsAndConditions();
//		// 205 form
//		ArrayList<String> descs = new ArrayList<String>();
//		descs.add("test1desc");
//		exclusions.clickSpecialEndorsementForLiability205(descs);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecialEndorsementLiabiliy);
//
//		//207 Form
//		exclusions.clickVendorAsAdditionalInsuredEndorsement207(descs);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_VendorAdditionalInsuredEndorsement);
//
//		//291 form
//		exclusions.clickAdditionalInsuredLandlordEndorsement291("testname","testDescr");
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_AdditionalInsuredLandlordEndorsement);
//
//		//Coverages - Property Exclusions & Conditions
//		exclusions.clickCoveragesTab();
//		propertyCoverages.clickBuildingsExclusionsAndConditions();
//
//
//		//107 Form	
//		propertyCoverages.clickDetachedGarageEndorsement(true);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageADetachedGarageStorageShed);
//
//		//106 Form
//		propertyCoverages.clickCashValueLimitationForRoof();
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_ActualCashValueLimitationRoofing);
//
//		//110 Form
//		ArrayList<String> descs2 = new ArrayList<String>();
//		descs2.add("test1desc2");
//		propertyCoverages.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);		
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecifiedPropertyExclusionEndorsement);
//		sendArbitraryKeys(Keys.TAB);
//
//		//134 Form
//		propertyCoverages.clickLimitedRoofCoverage();
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_LimitedRoofCoverageEndorsement);
//
//		
//        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
//        fpp.clickFarmPersonalProperty();
//        fpp.checkCoverageD(true);
//		fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
//		fpp.selectDeductible(FPPDeductible.Ded_1000);		
//		fpp.selectCoverages(FarmPersonalPropertyTypes.Livestock);
//		fpp.addItem(FPPFarmPersonalPropertySubTypes.Cows, 200, 10000, "Testing purpose");
//		fpp.selectCoverages(FarmPersonalPropertyTypes.Machinery);
//		fpp.addItem(FPPFarmPersonalPropertySubTypes.Tractors, 1, 1000, "Testing Description");
//		fpp.selectOptionalCoverageType(FPPOptionalCoverages.FreezingLivestock);
//		fpp.setAdditionalCoveragesLimit(FPPOptionalCoverages.FreezingLivestock, 2000);
//		fpp.setFarmPersonalPropertyRisk("A");
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_FreezingLivestockEndorsement);
//
//		fpp.clickSectionIICoveragesTab();
//        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
//		section2Covs.addCoverages(SectionIICoverages.Livestock);
//		section2Covs.setLivestockTypeAndQuantity(LivestockScheduledItemType.Cow, 200);
//		fpp.clickFarmPersonalProperty();
//
//		fpp.clickCoveragesExclusionsAndConditions();
//		//105 Form		
//		exclusions.clickSpecialEndorsementForProperty105(descs);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_SpecialEndorsementProperty);
//
//		exclusions.clickCoverageDExtensionEndorsement108("Test1Desc");
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageDExtensionEndorsement);
//
//		exclusions.setCoverageDAdditionalInsuredEndo140("TestName","Test1Desc");
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_CoverageDAdditionalInsuredEndorsement);
//
//		exclusions.setAdditionalLivestockEndorsement143("cows", 100, 1000, 10000, "Test1Desc");
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Liab_AdditionalLivestockEndorsement);	
//		sideMenu.clickSideMenuSquirePropertyDetail();
//
//        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
//		sideMenu.clickSideMenuSquirePropertyDetail();		
//		propertyDetails.clickViewOrEditBuildingButton(1); 
//		//		AddressInfo bankAddress = new AddressInfo();
//
//		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(4), "LessorPerson", bankAddress);
//		loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
//		loc2Bldg1AddInterest.setAddress(bankAddress);		
//		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
//        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
//		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
//		//        SearchAddressBookPC search = new SearchAddressBookPC(driver);
//		search.searchForContact(loc2Bldg1AddInterest);
//		//		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
//		additionalInterests.setAddressListing("New...");
//		//		additionalInterests.setAddress(loc2Bldg1AddInterest.getAddress().getLine1());
//		additionalInterests.setCity(loc2Bldg1AddInterest.getAddress().getCity());
//		//		additionalInterests.setState(loc2Bldg1AddInterest.getAddress().getState());
//		//		additionalInterests.setZip(loc2Bldg1AddInterest.getAddress().getZip());
//		//		additionalInterests.setAddressType(loc2Bldg1AddInterest.getAddress().getType());
//		//		additionalInterests.clickRelatedContactsTab();
//		//		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//		//		propertyDetails.clickOk();
//		//		sideMenu.clickSideMenuPADrivers();
//		sideMenu.clickSideMenuPACoverages();
//		GenericWorkorderSquireAutoCoverages_AdditionalCoverages additioanlcoverage = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
//		additioanlcoverage.clickAdditionalCoveragesTab();			
//		additioanlcoverage.setDriveOtherCar(true);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Auto_DriveOtherCar);
//
//		// adding Vehicle Level coverages to get specific Forms
//		// Add Comprehensive coverage
//		sideMenu.clickSideMenuPAVehicles();
//        GenericWorkorderVehicles vehicalTab = new GenericWorkorderVehicles(driver);
//		vehicalTab.clickLinkInVehicleTable("Edit");
//		//		vehicalTab.setFactoryCostNew(20000);
//		vehicalTab.selectVehicleCoverageDetailsTab();
//		//		vehicalTab.setComprehensive(true);
//		vehicalTab.setCollision(true);
//		vehicalTab.setAdditionalLivingExpense(true);
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Auto_AdditionalLivingExpense);			
//		vehicalTab.clickOK();
//		//Add Vehicle
//		sideMenu.clickSideMenuPAVehicles();
//		//		vehicalTab.createVehicleManually();
//		vehicalTab.createGenericVehicle(VehicleTypePL.ShowCar);	
//		vehicalTab.setVIN("ABCD4543543");
//		vehicalTab.setModelYear(2000);
//		vehicalTab.setMake("Honda");
//		vehicalTab.setModel("Accord");
//		vehicalTab.setStatedValue(20000);
//		vehicalTab.selectGaragedAtZip(Squire_PolicyObj.pniContact.getAddress().getCity());
//		vehicalTab.selectVehicleCoverageDetailsTab();
//		//		vehicalTab.setComprehensive(true);
//		vehicalTab.setCollision(true);
//		vehicalTab.clickExclusionsAndConditionsTab();	 
//
//		vehicalTab.clickOK();
//		this.eventsHitDuringSubmissionCreationPlusLocal1.add(DocFormEvents.PolicyCenter.Squire_Auto_ClassicCarLimitationCoverage);
//
//        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
//		sideMenuStuff.clickSideMenuRiskAnalysis();
//        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//		//		risk.Quote();
//		//		sideMenuStuff.clickSideMenuQuote();
//
//		sideMenuStuff.clickSideMenuForms();		
//		//        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
//		formsPage.verifyFormExists_Obsolete("Farm & Ranch Policy Declarations");	
//
//		String errorMessage = "";		
//		this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();		
//		this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal1);		
//		this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);
//		
//		if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
//			errorMessage = errorMessage + "ERROR: Documents  - Is Expected But not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
//		}		
//		//UW Issues Approval for Expiry date
//		sideMenuStuff.clickSideMenuRiskAnalysis();
//		//        risk.specialApproveAll();
//
//        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
//		quote.clickIssuePolicy();
//		if(isAlertPresent()) {
//			selectOKOrCancelFromPopup(OkCancel.OK);
//		}	
//		//        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
//		waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
//		completePage.clickApplyPolicyChangeToFuturePeriodsLink();		
//		//		risk.Quote();
//		//		sideMenuStuff.clickSideMenuForms();
//		//		formsPage.verifyFormDeclarationNotExists_Obsolete("Farm & Ranch Policy Declarations");
//		if (!errorMessage.equals("")) {
//			Assert.fail(errorMessage);
//		}
//	}

    //Standard Fire
    @Test()
    public void testGenerateStdFirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        stdFireObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("Guy", "Stdfire")
                .withPolTermLengthDays(50)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testGenerateStdFirePolicy"})
    public void testStdFire_RenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        // Login with UW
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireObj.standardFire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, stdFireObj);
    }

    @Test(dependsOnMethods = {"testStdFire_RenewalJob"})
    public void stdFirePolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireObj.standardFire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

        //start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireProperty();
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);


        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        excConds.clickCoveragesExclusionsAndConditions();
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        descs.add("test2desc");
        excConds.clickSpecialEndorsementForProperty105(descs);
        excConds.sendArbitraryKeys(Keys.TAB);
        excConds.clickCoveragesTab();
        this.eventsHitDuringSubmissionCreationPlusLocal2.add(PolicyCenter.StdFire_AddExclusionConditionSpecialEndorsemenForProperty105);

        covs.clickBuildingsExclusionsAndConditions();
        ArrayList<String> descs2 = new ArrayList<String>();
        descs2.add("test1desc2");
        descs2.add("test2desc2");
        covs.fillOutSpecifiedPropertyExclusionEndorsement_110(descs2);
        excConds.sendArbitraryKeys(Keys.TAB);
        covs.clickLimitedRoofCoverage();
        this.eventsHitDuringSubmissionCreationPlusLocal2.add(PolicyCenter.StdFire_AddExclusionConditionSpecifiedPropertyExclusionEndorsement110);
        this.eventsHitDuringSubmissionCreationPlusLocal2.add(PolicyCenter.StdFire_AddExclusionConditionLimitedRoofCoverageEndorsement134);

        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
        sideMenuStuff.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenuStuff.clickSideMenuQuote();

        sideMenuStuff.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        formsPage.verifyFormExists_Obsolete("Standard Fire Policy Declarations");
        String errorMessage = "";
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal2);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            errorMessage = errorMessage + "ERROR: Documents for 105_110_134 Is Expected But not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        //UW issues Approval for Expiration date.
        sideMenuStuff.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickIssuePolicy();

        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.clickApplyPolicyChangeToFuturePeriodsLink();


        risk.Quote();
        sideMenuStuff.clickSideMenuForms();
        formsPage.verifyFormDeclarationNotExists_Obsolete("Standard Fire Policy Declarations");

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }
    }
}
