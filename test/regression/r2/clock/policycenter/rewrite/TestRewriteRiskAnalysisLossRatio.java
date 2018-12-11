package regression.r2.clock.policycenter.rewrite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
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
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import repository.pc.workorders.renewal.StartRenewal;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US5698: Risk Analysis - Loss Ratio
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Loss%20Ratio.xlsx">Link
 * Text</a>
 * @Description
 * @DATE Nov 22, 2016
 */
@QuarantineClass
public class TestRewriteRiskAnalysisLossRatio extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myClaimPolicyObjPL;
    private Underwriters uw;
    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";

    @Test()
    public void testIssueInitialSquirePolicy() throws Exception {
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

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myProperty;
        mySquire.inlandMarine = myInlandMarine;


        this.myClaimPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Claim", "Rewrite")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Creating claims on initial term
     * @DATE Nov 22, 2016
     */
    @Test(dependsOnMethods = {"testIssueInitialSquirePolicy"})
    private void testCreateClaimPayments() throws Exception {
        String claimNumber = testCreateClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber(), "Collision and Rollover",
                "Auto Property Damage - Property");
        testCreateReserveChecks(claimNumber, "5100", "2010");

        String propertyClaim = testCreatePropertClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim, "5100", "2000");

        String iMClaims = testCreateIMClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims, "500", "200");
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Cancel policy and validate rewrite - risk analysis
     * @DATE Nov 22, 2016
     */
    @Test(dependsOnMethods = {"testCreateClaimPayments"})
    private void testYear1Rewrite() throws Exception {

        // cancel the policy
        testCancelSquirePolicy();

        // rewrite the policy
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myClaimPolicyObjPL.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();

        sideMenu.clickSideMenuLineSelection();
        sideMenu.clickSideMenuQualification();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);

        rewritePage.rewriteFullTermGuts(myClaimPolicyObjPL);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);

        risk.clickLossRatioTab();
        double lossRatioPercentage = (int) ((risk.getLossRatioPolicyLossesByYear("Year 1") * 100)
                / risk.getLossRatioPolicyLossesPremiumByYear("Year 1"));

        String errorMessage = "";
        if (risk.getLossRatioPolicyLossesLRByYear("Year 1") < lossRatioPercentage) {
            errorMessage = errorMessage + "Loss Ratio calculation is not matched \n";
        }
        risk.clickLossRatioAutoTab();
        String autoLossRatioDS = risk.getAutoDefaultDiscountAmount();

        if (risk.getLossRatioAutoSummaryTotalLossRatio() >= 70 && !autoLossRatioDS.contains("10% discount")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 10% discount is not displayed. \n";
        } else if (risk.getLossRatioAutoSummaryTotalLossRatio() < 70 && !autoLossRatioDS.contains("15% discount")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 15% discount is not displayed. \n";
        }
        risk.clickLossRatioPropertyTab();
        String propertyLossRatioDS = risk.getPropertyDefaultDiscountAmount();

        if (this.myClaimPolicyObjPL.squire.isCity()
                || this.myClaimPolicyObjPL.squire.isCountry()) {
            if (risk.getLossRatioPropertySummaryTotalLossRatio() >= 70
                    && !propertyLossRatioDS.contains("0% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 0% discount is not displayed. \n";
            } else if (risk.getLossRatioPropertySummaryTotalLossRatio() < 70
                    && !propertyLossRatioDS.contains("15% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 15% discount is not displayed. \n";
            }
        } else {
            if (risk.getLossRatioPropertySummaryTotalLossRatio() >= 70
                    && !propertyLossRatioDS.contains("5% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 5% discount is not displayed. \n";
            } else if (risk.getLossRatioPropertySummaryTotalLossRatio() < 70
                    && !propertyLossRatioDS.contains("15% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 15% discount is not displayed. \n";
            }
        }

        risk.clickLossRatioInlandMarineTab();
        String IMLossRatioDS = risk.getInlandMarineDefaultDiscountAmount();

        if (risk.getLossRatioIMSummaryTotalLossRatio() >= 70 && !IMLossRatioDS.contains("0%")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 0% is not displayed. \n";
        } else if (risk.getLossRatioIMSummaryTotalLossRatio() < 70 && !IMLossRatioDS.contains("15% discount")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 15% discount is not displayed. \n";
        }

        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
            SideMenuPC sideMenuStuff = new SideMenuPC(driver);
            sideMenuStuff.clickSideMenuQuote();
        }
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Validate multiple year rewrites.
     * @DATE Nov 22, 2016
     */
    @Test(dependsOnMethods = {"testYear1Rewrite"})
    private void testMultipleYearRewrites() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        testClockMove(290);

        // run the batch job to get renewal job
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);

        renewSquirePolicy();
        // run the clock move to get the policy inforce
        testClockMove(80);

        String claimNumber1 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(),
                "Collision and Rollover", "Auto Property Damage - Property");
        testCreateReserveChecks(claimNumber1, "5100", "2510");

        String propertyClaim1 = testCreatePropertClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim1, "5100", "900");

        String iMClaims1 = testCreateIMClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims1, "500", "200");

        testCancelSquirePolicy();

        testRewriteRiskAnalysisLossRatio("Year 1");

        testClockMove(290);

        // run the batch job to get renewal job
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);

        renewSquirePolicy();
        // run the clock move to get the policy inforce
        testClockMove(80);

        String claimNumber2 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(),
                "Collision and Rollover", "Auto Property Damage - Property");
        testCreateReserveChecks(claimNumber2, "5100", "2610");

        String propertyClaim2 = testCreatePropertClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim2, "5100", "900");

        String iMClaims2 = testCreateIMClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims2, "500", "200");

        testCancelSquirePolicy();

        testRewriteRiskAnalysisLossRatio("Year 2");

        testClockMove(290);

        // run the batch job to get renewal job
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);

        renewSquirePolicy();
        // run the clock move to get the policy inforce
        testClockMove(80);

        String claimNumber3 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(),
                "Collision and Rollover", "Auto Property Damage - Property");
        testCreateReserveChecks(claimNumber3, "5100", "2610");

        String propertyClaim3 = testCreatePropertClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim3, "5100", "900");

        String iMClaims3 = testCreateIMClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims3, "500", "200");

        testCancelSquirePolicy();

        testRewriteRiskAnalysisLossRatio("Year 3");

    }

    private void renewSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal,
                UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myClaimPolicyObjPL.squire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, myClaimPolicyObjPL);
        new GuidewireHelpers(driver).logout();
    }

    private void testClockMove(int days) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
    }

    private String testCreateClaimWithExposure(String policyNumber, String lossCause, String exposureType)
            throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withDateOfLoss(dateOfLoss).withLossRouter(lossRouter)
                .withAdress("Random").withPolicyNumber(policyNumber).build(GenerateFNOLType.Auto);
        new GuidewireHelpers(driver).logout();

        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber).withCoverageType(exposureType)
                .build();

        return myFNOLObj.claimNumber;
    }

    private String testCreatePropertClaimWithExposure(String policyNumber) throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withDateOfLoss(dateOfLoss).build(GenerateFNOLType.Property);

        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(user.toString(), password).withClaimNumber(myFNOLObj.claimNumber)
                .build();

        return myFNOLObj.claimNumber;
    }

    private String testCreateIMClaimWithExposure(String policyNumber) throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withDateOfLoss(dateOfLoss).build(GenerateFNOLType.InlandMarine);

        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(user.toString(), password).withClaimNumber(myFNOLObj.claimNumber)
                .build();

        return myFNOLObj.claimNumber;
    }

    public String testCreateGeneralLiabilityClaimWithExposure(String policyNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.GeneralLiability);
        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(user.toString(), password).withClaimNumber(myFNOLObj.claimNumber)
                .build();

        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(user.toString(), password).withClaimNumber(myFNOLObj.claimNumber)
                .build();
        return myFNOLObj.claimNumber;
    }

    private void testCreateReserveChecks(String claimNumber, String reserveAmount, String paymentAmount)
            throws Exception {
        ArrayList<ReserveLine> line = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        line1.setReserveAmount(reserveAmount);
        line.add(line1);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        new GenerateReserve.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(claimNumber).withReserveLines(line).build();

        new GenerateCheck.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray, "gw").withClaimNumber(claimNumber)
                .withDeductible(false).withDeductibleAmount("250").withPaymentType(CheckLineItemType.INDEMNITY)
                .withCategoryType(CheckLineItemCategory.INDEMNITY).withPaymentAmount(paymentAmount).withCompanyCheckBook("Farm Bureau")
                .build(GenerateCheckType.Regular);
    }

    private void testCancelSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myClaimPolicyObjPL.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        PolicySummary summary = new PolicySummary(driver);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(this.myClaimPolicyObjPL.accountNumber);
        String errorMessage = "";

        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not success!!!";



        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    private void testRewriteRiskAnalysisLossRatio(String yearValue) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myClaimPolicyObjPL.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPolicyTermByLatestStatus();

        StartRewrite rewritePage = new StartRewrite(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();

        sideMenu.clickSideMenuLineSelection();
        sideMenu.clickSideMenuQualification();

        qualificationPage.setSquireGeneralFullTo(false);

        rewritePage.rewriteFullTermGuts(myClaimPolicyObjPL);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);

        sideMenu.clickSideMenuRiskAnalysis();
        risk.clickLossRatioTab();

        int calculatedValue = (int) ((risk.getLossRatioPolicyLossesByYear("Year 1") * 100)
                / risk.getLossRatioPolicyLossesPremiumByYear("Year 1"));
        double lossRatioPercentage = calculatedValue;

        String errorMessage = "";
        if (risk.getLossRatioPolicyLossesLRByYear("Year 1") < lossRatioPercentage) {
            errorMessage = errorMessage + "Loss Ratio calculation is not matched \n";
        }
        risk.clickLossRatioAutoTab();

        String autoLossRatioDS = risk.getAutoDefaultDiscountAmount();
        int autoGreater70 = 0;
        for (int currentYear = 1; currentYear <= Integer
                .parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
            if (risk.getLossRatioAutoSummaryLossRatioByYear("Year " + currentYear) >= 70) {
                autoGreater70 = autoGreater70 + 1;
            }
        }

        if (autoGreater70 == 2 && !autoLossRatioDS.contains("10% surcharge")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 10% surchange is not displayed. \n";
        } else if (autoGreater70 >= 3 && !autoLossRatioDS.contains("40% surcharge")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 40% surchange is not displayed. \n";
        } else if (autoGreater70 == 1 && !autoLossRatioDS.contains("10% discount")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 10% discount is not displayed. \n";
        } else if (autoGreater70 == 0 && !autoLossRatioDS.contains("15% discount")) {
            errorMessage = errorMessage + "Expected Auto Loss Ratio 15% discount is not displayed. \n";
        }

        risk.clickLossRatioPropertyTab();

        String propertyLossRatioDS = risk.getPropertyDefaultDiscountAmount();
        int propertyGreater70 = 0;
        for (int currentYear = 1; currentYear <= Integer
                .parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
            if (risk.getLossRatioPropertySummaryLossRatioByYear("Year " + currentYear) >= 70) {
                propertyGreater70 = propertyGreater70 + 1;
            }
        }

        if (propertyGreater70 == 2 && !propertyLossRatioDS.contains("25% surcharge")) {
            errorMessage = errorMessage + "Expected Property Loss Ratio 25% surchange is not displayed. \n";
        } else if (propertyGreater70 >= 3 && !propertyLossRatioDS.contains("70% surcharge")) {
            errorMessage = errorMessage + "Expected Property Loss Ratio 70% surchange is not displayed. \n";
        } else {
            if (this.myClaimPolicyObjPL.squire.isCity()
                    || this.myClaimPolicyObjPL.squire.isCountry()) {
                if (propertyGreater70 == 1 && !propertyLossRatioDS.contains("0% discount")) {
                    errorMessage = errorMessage + "Expected Property Loss Ratio 0% discount is not displayed. \n";
                } else if (propertyGreater70 == 0 && !propertyLossRatioDS.contains("15% discount")) {
                    errorMessage = errorMessage + "Expected Property Loss Ratio 15% discount is not displayed. \n";
                }
            } else {
                if (propertyGreater70 == 1 && !propertyLossRatioDS.contains("5% discount")) {
                    errorMessage = errorMessage + "Expected Property Loss Ratio 5% discount is not displayed. \n";
                } else if (propertyGreater70 == 0 && !propertyLossRatioDS.contains("15% discount")) {
                    errorMessage = errorMessage + "Expected Property Loss Ratio 15% discount is not displayed. \n";
                }
            }
        }

        risk.clickLossRatioInlandMarineTab();

        String imLossRatioDS = risk.getInlandMarineDefaultDiscountAmount();
        int imGreater70 = 0;
        for (int currentYear = 1; currentYear <= Integer
                .parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
            if (risk.getLossRatioIMSummaryLossRatioByYear("Year " + currentYear) >= 70) {
                imGreater70 = imGreater70 + 1;
            }
        }

        if (imGreater70 == 2 && !imLossRatioDS.contains("20% surcharge")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 20% surchange is not displayed. \n";
        } else if (imGreater70 >= 3 && !imLossRatioDS.contains("70% surcharge")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 70% surchange is not displayed. \n";
        } else if (imGreater70 == 1 && !imLossRatioDS.contains("0%")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 0% is not displayed. \n";
        } else if (imGreater70 == 0 && !imLossRatioDS.contains("15% discount")) {
            errorMessage = errorMessage + "Expected IM Loss Ratio 15% discount is not displayed. \n";
        }

        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
            SideMenuPC sideMenuStuff = new SideMenuPC(driver);
            sideMenuStuff.clickSideMenuQuote();
        }
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }
}
