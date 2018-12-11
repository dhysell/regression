package regression.r2.noclock.policycenter.change.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * Test 2 - Standard Fire will fail due to DE3793 - Nullpointer recevied - OSS policy change
 *
 */
/**
 * @Author nvadlamudi
 * @Requirement :US4780: PL - Out of sequence Transaction
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jul 25, 2016
 */
public class TestPLOutOfSequencePolicyChange2 extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myStandardLiabPol;
    private Underwriters uw;



//    @Test()
//    public void testIssueSquirePolicy() throws GuidewirePolicyCenterException, Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//        locationsList.add(locToAdd);
//
//		// Farm Equipment
//        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
//                "Manly Farm Equipment", 1000);
//		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
//		farmEquip.add(farmThing);
//        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
//                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
//		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
//		allFarmEquip.add(imFarmEquip);
//
//		// Rec Equip
//		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
//                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "55000",
//                PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle, "16000",
//                PAComprehensive_CollisionDeductible.Fifty50, "Test Automation"));
//
//        SquireLiability liability = new SquireLiability();
//		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
//		imTypes.add(InlandMarine.FarmEquipment);
//		imTypes.add(InlandMarine.RecreationalEquipment);
//
//		ArrayList<Contact> driversList = new ArrayList<Contact>();
//		Contact person = new Contact();
//		driversList.add(person);
//		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
//		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
//		vehicleList.add(vehicle);
//
//        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
//                MedicalLimit.TenK);
//		coverages.setUnderinsured(false);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = liability;
//
//        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
//        myInlandMarine.recEquipment_PL_IM = recVehicle;
//        myInlandMarine.farmEquipment = allFarmEquip;
//        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
//
//        Squire mySquire = new Squire(SquireEligibility.Country);
//        mySquire.inlandMarine = myInlandMarine;
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//		mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//				.withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
//				.withInsFirstLastName("SQ", "Change")
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.PolicyIssued);
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
//                Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
//                mySQPolicyObjPL.squire.getPolicyNumber());
//		//
//        // First Change
//        Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
//                DateAddSubtractOptions.Day, 20);
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//        policyChangePage.startPolicyChange("First Policy Change", firstChangeDate);
//
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageScreen = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
//		coverageScreen.setCoverageALimit(300000);
//		coverageScreen.selectSectionIDeductible(SectionIDeductible.OneThousand);
//
//        issueFirstPolicyChange();
//
//        // Second Change
//        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
//        policySearchPC.searchPolicyByPolicyNumber(mySQPolicyObjPL.squire.getPolicyNumber());
//		//
//        Date secondChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
//                DateAddSubtractOptions.Day, 10);
//        policyChangePage.startPolicyChange("Second Policy Change", secondChangeDate);
//		//
//		sideMenu.clickSideMenuPolicyInfo();
//		//		String errorMessage = "";
//        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
//        String warning = "Your Policy Change is an out-of-sequence transaction. There are future transactions";
//        if (!policyInfo.getPageTopWarningMessage().contains(warning)) {
//            errorMessage = errorMessage + "Expected Warning message : " + warning + " is not displayed.\n";
//        }
//        sideMenu.clickSideMenuSquirePropertyCoverages();
//        //        coverageScreen.setCoverageALimit(200000);
//		coverageScreen.selectSectionIDeductible(SectionIDeductible.FiveHundred);
//        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
//		qualificationPage.clickQuote();
//		//		GWCommon common = new GWCommon();
//		String valMessage = "There are out-of-sequence conflicts that must be resolved prior to quoting. Please visit the Policy Review step to resolve the conflicts";
//		if (!common.topBannerExists() && (!common.getTopBanner().contains(valMessage))) {
//			errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
//		}
//
//		sideMenu.clickSideMenuPolicyChangeReview();
//		//        GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
//		polReview.clickChangeConflictsTab();
//		//        if (!polReview.checkChangeConflictsByValues("Coverage A Limit on Property #1",
//                "Policy Change Eff. " + DateUtils.dateFormatAsString("MM/dd/yyyy", secondChangeDate), "200000",
//                "300000"))
//			errorMessage = errorMessage + "Expected Coverage A Limit is not displayed. \n";
//		polReview.clickOverrideAll();
//		//		polReview.clickChangeConflictsSubmitButton();
//		//        issueFirstPolicyChange();
//
//        if (errorMessage != "")
//			Assert.fail(errorMessage);
//    }

    /**
     * @throws GuidewirePolicyCenterException
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Testing the Standard Fire - Out of Sequence transactions
     * @DATE Jul 25, 2016
     */
    @Test()
    private void testIssueStandardLiabilityPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myStandardLiabPol = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Fire", "Change").withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myStandardLiabPol.standardFire.getPolicyNumber());
        // First Change
        Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, 20);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First Policy Change", firstChangeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageScreen = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverageScreen.setCoverageALimit(300000);
		coverageScreen.selectSectionIDeductible(SectionIDeductible.OneThousand);

        issueFirstPolicyChange();

        // Second Change
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myStandardLiabPol.standardFire.getPolicyNumber());

        Date secondChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, 10);
        policyChangePage.startPolicyChange("Second Policy Change", secondChangeDate);

		sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        String warning = "Your Policy Change is an out-of-sequence transaction. There are future transactions";
        if (!policyInfo.getPageTopWarningMessage().contains(warning)) {
            errorMessage = errorMessage + "Expected Warning message : " + warning + " is not displayed.\n";
        }
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverageScreen.setCoverageALimit(200000);
		coverageScreen.selectSectionIDeductible(SectionIDeductible.FiveHundred);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        String valMessage = "There are out-of-sequence conflicts that must be resolved prior to quoting. Please visit the Policy Review step to resolve the conflicts";
		if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(valMessage))) {
			errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
		}

		sideMenu.clickSideMenuPolicyChangeReview();
        GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
		polReview.clickChangeConflictsTab();
        if (!polReview.checkChangeConflictsByValues("Coverage A Limit on Property",
                "Policy Change Eff. " + DateUtils.dateFormatAsString("MM/dd/yyyy", secondChangeDate), "200000",
                "300000"))
			errorMessage = errorMessage + "Expected Coverage A Limit on Property is not displayed. \n";
		polReview.clickOverrideAll();
		polReview.clickChangeConflictsSubmitButton();
        issueFirstPolicyChange();

        if (errorMessage != "")
			Assert.fail(errorMessage);
    }

//    /**
//     * @throws Exception
//     * @Author nvadlamudi
//     * @Description : Testing Umbrella policy with Out of Sequence Policy change
//     * transactions
//     * @DATE Jul 25, 2016
//     */
//    @Test()
//    private void testIssueUmbrellaOSSChanges() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//		locationsList.add(locToAdd);
//
//		SquireLiability myLiab = new SquireLiability();
//		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = myLiab;
//
//        Squire mySquire = new Squire(SquireEligibility.Country);
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//        myUmbSqPol = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withInsFirstLastName("SQ", "Cancel")
//                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);
//
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
//		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_3000000);
//
//        myUmbSqPol.squireUmbrellaInfo = squireUmbrellaInfo;
//        myUmbSqPol.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
//                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
//                myUmbSqPol.squireUmbrellaInfo.getPolicyNumber());
//        //        // First Change
//        Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
//                DateAddSubtractOptions.Day, 20);
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//        policyChangePage.startPolicyChange("First Policy Change", firstChangeDate);
//
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuSquireUmbrellaCoverages();
//
//        GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);
//		umbCovs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_4000000);
//
//        issueFirstPolicyChange();
//
//        // Second Change
//        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
//        policySearchPC.searchPolicyByPolicyNumber(myUmbSqPol.squireUmbrellaInfo.getPolicyNumber());
//        //
//        Date secondChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
//                DateAddSubtractOptions.Day, 10);
//        policyChangePage.startPolicyChange("Second Policy Change", secondChangeDate);
//		//
//		sideMenu.clickSideMenuPolicyInfo();
//		//		String errorMessage = "";
//        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
//        String warning = "Your Policy Change is an out-of-sequence transaction. There are future transactions";
//        if (!policyInfo.getPageTopWarningMessage().contains(warning)) {
//            errorMessage = errorMessage + "Expected Warning message : " + warning + " is not displayed.\n";
//        }
//        sideMenu.clickSideMenuSquireUmbrellaCoverages();
//        umbCovs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_5000000);
//        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
//		qualificationPage.clickQuote();
//		//		GWCommon common = new GWCommon();
//		String valMessage = "There are out-of-sequence conflicts that must be resolved prior to quoting. Please visit the Policy Review step to resolve the conflicts";
//		if (!common.topBannerExists() && (!common.getTopBanner().contains(valMessage))) {
//			errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
//		}
//
//		sideMenu.clickSideMenuPolicyChangeReview();
//        //        GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
//		polReview.clickChangeConflictsTab();
//        //        if (!polReview.checkChangeConflictsByValues("Increased Limit on Liability",
//                "Policy Change Eff. " + DateUtils.dateFormatAsString("MM/dd/yyyy", secondChangeDate), "500000",
//                "400000"))
//			errorMessage = errorMessage + "Expected Increased Limit on Liability is not displayed. \n";
//		polReview.clickOverrideAll();
//		polReview.clickChangeConflictsSubmitButton();
//        //        issueFirstPolicyChange();
//
//        if (errorMessage != "")
//			Assert.fail(errorMessage);
//    }

    private void issueFirstPolicyChange() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
			sideMenu.clickSideMenuRiskAnalysis();
			riskAnalysis.approveAll_IncludingSpecial();
            qualificationPage.clickQuote();
			sideMenu.clickSideMenuQuote();
		}
        sideMenu.clickSideMenuRiskAnalysis();
		riskAnalysis.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();

        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }
}
