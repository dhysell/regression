package regression.r2.noclock.policycenter.rewrite.subgroup5;

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
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
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
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@QuarantineClass
public class TestRewriteRiskAnalysisLossRatio extends BaseTest {
    private GeneratePolicy SquPolicy;
    private Underwriters uw;

    private WebDriver driver;


    @Test()
    private void testIssueSquirePolicy() throws Exception {

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

        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -8);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.SquPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolEffectiveDate(newEff)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withPolTermLengthDays(85)
                .withInsFirstLastName("Rewrite", "LossRatio")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testCreateClaimPayments() throws Exception {

        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.ClaimCenter)).minusDays(2);
        System.out.println("Current policy  " + this.SquPolicy.squire.getPolicyNumber());
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = new DriverBuilder().buildGWWebDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw").withSpecificIncident("Random")
                .withLossDescription("Loss Descrption Test").withLossCause("Random")
                .withDateOfLoss(dateOfLoss).withLossRouter("Random").withAdress("Random")
                .withPolicyNumber(this.SquPolicy.squire.getPolicyNumber()).build(GenerateFNOLType.Auto);
        new GuidewireHelpers(driver).logout();

        new GenerateExposure.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber).withCoverageType("Comprehensive")
                .withCoverageType("Auto Property Damage - Property").build();

        ArrayList<ReserveLine> line = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        line1.setReserveAmount("52000");
        line.add(line1);

        new GenerateReserve.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber).withReserveLines(line).build();

        new GenerateCheck.Builder(driver).withCreatorUserNamePassword(ClaimsUsers.gmurray, "gw")
                .withClaimNumber(myFNOLObj.claimNumber).withDeductible(false).withDeductibleAmount("250")
                .withPaymentType(CheckLineItemType.INDEMNITY).withCategoryType(CheckLineItemCategory.INDEMNITY).withPaymentAmount("51000")
                .withCompanyCheckBook("Farm Bureau").build(GenerateCheckType.Regular);

    }

    @Test(dependsOnMethods = {"testCreateClaimPayments"})
    private void testCancelSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                SquPolicy.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(SquPolicy.accountNumber);
        String errorMessage = "";

        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not success!!!";

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testCancelSquirePolicy"})
    private void testRewriteRiskAnalysisLossRatio() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                SquPolicy.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTermGuts(SquPolicy);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.clickLossRatioTab();
    }
}
