package regression.r2.noclock.policycenter.rewrite.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.activity.ActivityPopup;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LossRatioDiscounts;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4381: Reason for Change Should Only Appear When Manually Changed
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/75945099488">Link Text</a>
 * @Description
 * @DATE Feb 22, 2017
 */
public class TestSquireStdFireRewriteLossRatioManualChange extends BaseTest {

    private GeneratePolicy mySqPolObjPL;
    private Underwriters uw;
    private GeneratePolicy myStandardFirePol;

    private WebDriver driver;

    @Test()
    public void testIssueSquirePolWithLossRatio() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);
        myLiab.setMedicalLimit(SectionIIMedicalLimit.Limit_10000);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        SquirePersonalAutoCoverages autoCoverages = new SquirePersonalAutoCoverages();
        autoCoverages.setLiability(LiabilityLimit.CSL500K);
        autoCoverages.setUninsuredLimit(UninsuredLimit.CSL500K);
        autoCoverages.setUnderinsured(true);
        autoCoverages.setUnderinsuredLimit(UnderinsuredLimit.CSL500K);
        squirePersonalAuto.setCoverages(autoCoverages);


        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.mySqPolObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("Rewriteal", "LossRatio")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySqPolObjPL.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickCloseWorkSheet();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.approveAll();
        risk.clickLossRatioTab();

        // setting the loss ratio % for sections
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setPropertyReasonForChange("Testing purpose");

        risk.clickLossRatioLiabilityTab();
        risk.setLossRatioLiabilityDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setLiabilityReasonForChange("Testing purpose");

        risk.clickLossRatioAutoTab();
        risk.setLossRatioAutoDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setAutoReasonForChange("Testing purpose");

        risk.clickLossRatioInlandMarineTab();
        risk.setLossRatioIMDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setIMReasonForChange("Testing purpose");

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sidemenu.clickSideMenuQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        quotePage.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

    }

    @Test(dependsOnMethods = {"testIssueSquirePolWithLossRatio"})
    private void testSquireRewriteWithLossRatio() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObjPL.accountNumber);

        testCancelPolicy(mySqPolObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTermGuts(mySqPolObjPL);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        String errorMessage = "";

        // verifying and setting the loss ratios
        if (!risk.getLossRatioPropertyDiscount().contains(LossRatioDiscounts.FIFTEENDISCOUNT.getValue())) {
            errorMessage = errorMessage + "Loss Ratio Property discount is not defaulted  with " + LossRatioDiscounts.FIFTEENDISCOUNT.getValue() + "\n";
        }
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setPropertyReasonForChange("Testing purpose");

        risk.clickLossRatioLiabilityTab();
        if (!risk.getLossRatioLiabilityDiscount().contains(LossRatioDiscounts.FIFTEENDISCOUNT.getValue())) {
            errorMessage = errorMessage + "Loss Ratio Property discount is not defaulted  with " + LossRatioDiscounts.FIFTEENDISCOUNT.getValue() + "\n";
        }
        risk.setLossRatioLiabilityDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setLiabilityReasonForChange("Testing purpose");

        risk.clickLossRatioAutoTab();
        if (!risk.getLossRatioAutoDiscount().contains(LossRatioDiscounts.FIFTEENDISCOUNT.getValue())) {
            errorMessage = errorMessage + "Loss Ratio Property discount is not defaulted  with " + LossRatioDiscounts.FIFTEENDISCOUNT.getValue() + "\n";
        }
        risk.setLossRatioAutoDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setAutoReasonForChange("Testing purpose");

        risk.clickLossRatioInlandMarineTab();
        if (!risk.getLossRatioIMDiscount().contains(LossRatioDiscounts.FIFTEENDISCOUNT.getValue())) {
            errorMessage = errorMessage + "Loss Ratio Property discount is not defaulted  with " + LossRatioDiscounts.FIFTEENDISCOUNT.getValue() + "\n";
        }
        risk.setLossRatioIMDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.sendArbitraryKeys(Keys.TAB);
        risk.setIMReasonForChange("Testing purpose");

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();

        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnaysis.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }


    @Test()
    private void testStandardFirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myStandardFirePol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withPolTermLengthDays(80)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Rewrite", "LossRatio")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);


        new GuidewireHelpers(driver).logout();

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myStandardFirePol.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickCloseWorkSheet();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        prop.clickViewOrEditBuildingButton(1);
        prop.setRisk("A");
        prop.clickOk();
        sidemenu.clickSideMenuQualification();

        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.approveAll();
        risk.clickLossRatioTab();

        // setting the loss ratio % for sections
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.ZERODISCOUNT);
        risk.clickProductLogo();
        risk.setPropertyReasonForChange("Testing purpose");


        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sidemenu.clickSideMenuQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        quotePage.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

    }

    @Test(dependsOnMethods = {"testStandardFirePol"})
    private void testStandardFireRewriteLossRatioManualChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePol.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        testCancelPolicy(myStandardFirePol.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTermGuts(myStandardFirePol);

        new GuidewireHelpers(driver).editPolicyTransaction();


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        String errorMessage = "";

        // verifying and setting the loss ratios
        if (!risk.getLossRatioPropertyDiscount().contains(LossRatioDiscounts.FIFTEENDISCOUNT.getValue())) {
            errorMessage = errorMessage + "Loss Ratio Property discount is not defaulted  with " + LossRatioDiscounts.FIFTEENDISCOUNT.getValue() + "\n";
        }
        risk.setLossRatioPropertyDiscount(LossRatioDiscounts.ZERODISCOUNT);
        rewritePage.sendArbitraryKeys(Keys.TAB);
        risk.setPropertyReasonForChange("Testing purpose");


        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();

        }

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnaysis.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }


    private void testCancelPolicy(String accountNumber) {

        PolicySummary summary = new PolicySummary(driver);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(accountNumber);
        String errorMessage = "";
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null) {
            errorMessage = "Cancellation is not success!!!";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }


    }
}
