package regression.r2.clock.policycenter.renewaltransition;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.BatchProcess;
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
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_LossRatios;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US5698: PL - PA - Risk Analysis Loss Ratio Calculation
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Loss%20Ratio.xlsx">PC8 - Squire - QuoteApplication - Loss Ratio</a>
 * @Description
 * @DATE Nov 20, 2016
 */
@QuarantineClass
public class TestPLRenewalRiskAnalysisLossRatio extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myClaimPolicyObjPL;
    private ClaimsUsers user = ClaimsUsers.rburgoyne;
    private String password = "gw";
    Underwriters uw;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Minor Incident";
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
        // Set vehicle to have collision and comprehensive
        squirePersonalAuto.getVehicleList().get(0).setCollision(true);
        squirePersonalAuto.getVehicleList().get(0).setComprehensive(true);
        SquirePersonalAutoCoverages autoCoverages = new SquirePersonalAutoCoverages();
        autoCoverages.setLiability(LiabilityLimit.CSL500K);
        autoCoverages.setUninsuredLimit(UninsuredLimit.CSL500K);
        autoCoverages.setUnderinsured(true);
        autoCoverages.setUnderinsuredLimit(UnderinsuredLimit.CSL500K);
        squirePersonalAuto.setCoverages(autoCoverages);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);

        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -290);

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myProperty;
        mySquire.inlandMarine = myInlandMarine;
        mySquire.squirePA = squirePersonalAuto;

        this.myClaimPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolEffectiveDate(newEff)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Claim", "PreRenewal")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueInitialSquirePolicy"})
    private void testCreateClaimPayments() throws Exception {
        String claimNumber = testCreateClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber(), "Collision and Rollover", "Collision - Vehicle Damage");
        testCreateReserveChecks(claimNumber, "5100", "2010");

        String propertyClaim = testCreatePropertyClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim, "5100", "2000");

        String iMClaims = testCreateIMClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims, "500", "200");
    }

    @Test(dependsOnMethods = {"testCreateClaimPayments"})
    private void testYear1RenewalRiskAnalysisLossRatio() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myClaimPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myClaimPolicyObjPL);


        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(myClaimPolicyObjPL.accountNumber, "003");

        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(myClaimPolicyObjPL);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sideMenu.clickSideMenuQuote();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();
        double lossRatioPercentage = (int) ((risk.getLossRatioPolicyLossesByYear("Year 1") * 100) / risk.getLossRatioPolicyLossesPremiumByYear("Year 1"));

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

        if (this.myClaimPolicyObjPL.squire.isCity() || this.myClaimPolicyObjPL.squire.isCountry()) {
            if (risk.getLossRatioPropertySummaryTotalLossRatio() >= 70 && !propertyLossRatioDS.contains("0% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 0% discount is not displayed. \n";
            } else if (risk.getLossRatioPropertySummaryTotalLossRatio() < 70 && !propertyLossRatioDS.contains("15% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 15% discount is not displayed. \n";
            }
        } else {
            if (risk.getLossRatioPropertySummaryTotalLossRatio() >= 70 && !propertyLossRatioDS.contains("5% discount")) {
                errorMessage = errorMessage + "Expected Property Loss Ratio 5% discount is not displayed. \n";
            } else if (risk.getLossRatioPropertySummaryTotalLossRatio() < 70 && !propertyLossRatioDS.contains("15% discount")) {
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

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myClaimPolicyObjPL);


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    @Test(dependsOnMethods = {"testYear1RenewalRiskAnalysisLossRatio"})
    private void testMultipleYearRenewal() throws Exception {
        testClockMove(365);

        String claimNumber1 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(), "Collision and Rollover", "Collision - Vehicle Damage");
        testCreateReserveChecks(claimNumber1, "5100", "2510");

        String propertyClaim1 = testCreatePropertyClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim1, "5100", "900");

        String iMClaims1 = testCreateIMClaimWithExposure(myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims1, "500", "200");

        validateLossRatioForYear("Year 2", "004");


        testClockMove(365);

        String claimNumber2 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(), "Collision and Rollover", "Collision - Vehicle Damage");
        testCreateReserveChecks(claimNumber2, "5100", "2610");

        String propertyClaim2 = testCreatePropertyClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim2, "5100", "900");

        String iMClaims2 = testCreateIMClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims2, "500", "200");

        validateLossRatioForYear("Year 3", "005");

        testClockMove(365);

        String claimNumber3 = testCreateClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber(), "Collision and Rollover", "Collision - Vehicle Damage");
        testCreateReserveChecks(claimNumber3, "5100", "2510");

        String propertyClaim3 = testCreatePropertyClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(propertyClaim3, "5100", "900");

        String iMClaims3 = testCreateIMClaimWithExposure(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        testCreateReserveChecks(iMClaims3, "500", "200");

        validateLossRatioForYear("Year 3", "006");

    }

    private void validateLossRatioForYear(String yearValue, String jobNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myClaimPolicyObjPL.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(this.myClaimPolicyObjPL.accountNumber);
        acctSummary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        PolicySummary summaryPage = new PolicySummary(driver);
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (preRenewalDirectionExists) {
                preRenewalPage.clickViewInPreRenewalDirection();
                preRenewalPage.closeAllPreRenewalDirectionExplanations();
                preRenewalPage.clickClosePreRenewalDirection();
            }
        }

        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(this.myClaimPolicyObjPL.accountNumber, jobNumber);

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sideMenu.clickSideMenuQuote();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_LossRatios risk = new GenericWorkorderRiskAnalysis_LossRatios(driver);
        risk.clickLossRatioTab();

        int calculatedValue = (int) ((risk.getLossRatioPolicyLossesByYear("Year 1") * 100) / risk.getLossRatioPolicyLossesPremiumByYear("Year 1"));
        double lossRatioPercentage = calculatedValue;

        String errorMessage = "";
        if (risk.getLossRatioPolicyLossesLRByYear("Year 1") <= lossRatioPercentage) {
            errorMessage = errorMessage + "Loss Ratio calculation is not matched \n";
        }
        risk.clickLossRatioAutoTab();

        String autoLossRatioDS = risk.getAutoDefaultDiscountAmount();
        int autoGreater70 = 0;
        for (int currentYear = 1; currentYear <= Integer.parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
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
        for (int currentYear = 1; currentYear <= Integer.parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
            if (risk.getLossRatioPropertySummaryLossRatioByYear("Year " + currentYear) >= 70) {
                propertyGreater70 = propertyGreater70 + 1;
            }
        }


        if (propertyGreater70 == 2 && !propertyLossRatioDS.contains("25% surcharge")) {
            errorMessage = errorMessage + "Expected Property Loss Ratio 25% surchange is not displayed. \n";
        } else if (propertyGreater70 >= 3 && !propertyLossRatioDS.contains("70% surcharge")) {
            errorMessage = errorMessage + "Expected Property Loss Ratio 70% surchange is not displayed. \n";
        } else {
            if (this.myClaimPolicyObjPL.squire.isCity() || this.myClaimPolicyObjPL.squire.isCountry()) {
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
        for (int currentYear = 1; currentYear <= Integer.parseInt(yearValue.replace("Year ", "").trim()); currentYear++) {
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

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myClaimPolicyObjPL);
        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

    }

    private void testClockMove(int days) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        //delay required to create a claim
    }

    private String testCreateClaimWithExposure(String policyNumber, String lossCause, String exposureType) throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(this.driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withtopLevelCoverage(lossCause)
                .withDateOfLoss(dateOfLoss)
                .withLossRouter(lossRouter)
                .withAdress("Random")
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        new GuidewireHelpers(driver).logout();

        new GenerateExposure.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.rburgoyne.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber)
                .withCoverageType(exposureType)
                .build();

        return myFNOLObj.claimNumber;
    }


    private String testCreatePropertyClaimWithExposure(String policyNumber) throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(this.driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withDateOfLoss(dateOfLoss)
                .build(GenerateFNOLType.Property);


        new GenerateExposure.Builder(this.driver).withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(myFNOLObj.claimNumber).build();

        return myFNOLObj.claimNumber;
    }

    private String testCreateIMClaimWithExposure(String policyNumber) throws Exception {
        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(this.driver).withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .withDateOfLoss(dateOfLoss)
                .build(GenerateFNOLType.InlandMarine);


        new GenerateExposure.Builder(this.driver).withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(myFNOLObj.claimNumber).build();

        return myFNOLObj.claimNumber;
    }

    private void testCreateReserveChecks(String claimNumber, String reserveAmount, String paymentAmount) throws Exception {
        ArrayList<ReserveLine> line = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        line1.setReserveAmount(reserveAmount);
        line.add(line1);
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        this.driver = new DriverBuilder().buildGWWebDriver(cf);
        new GenerateReserve.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.rburgoyne.toString(), "gw")
                .withClaimNumber(claimNumber)
                .withReserveLines(line)
                .build();

        new GenerateCheck.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray, "gw")
                .withClaimNumber(claimNumber)
                .withDeductible(false)
                .withDeductibleAmount("250")
                .withPaymentType(CheckLineItemType.INDEMNITY)
                .withCategoryType(CheckLineItemCategory.INDEMNITY)
                .withPaymentAmount(paymentAmount)
                .withCompanyCheckBook("Farm Bureau")
                .build(GenerateCheckType.Regular);
    }


}
