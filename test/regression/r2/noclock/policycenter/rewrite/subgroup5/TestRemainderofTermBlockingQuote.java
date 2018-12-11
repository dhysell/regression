package regression.r2.noclock.policycenter.rewrite.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE5126: PL Rewrite Remainder of the Term: Blocking Quote - General Pre Qualification Questions are not copied over and showed an error because they were missing and on a Rewrite Remainder of term job.
 * @Description :
 * @DATE Apr 10, 2017
 */
public class TestRemainderofTermBlockingQuote extends BaseTest {
    private GeneratePolicy stdLibObj;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void testGenerateStandardLiability() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -90);
        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation propLoc = new PolicyLocation();
        propLoc.setPlNumAcres(3);
        propLoc.setPlNumResidence(2);
        locationsList.add(propLoc);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        stdLibObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateStandardLiability"})
    public void testCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibObj.accountNumber);

        Date currentDate = stdLibObj.standardLiability.getEffectiveDate();
        Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 60);
        StartCancellation cancelPol = new StartCancellation(driver);

        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
                cancellationDate, true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testCancellation"})
    private void testValidateRewriteReminderOfTermQuoted() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibObj.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteRemainderOfTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQualificationNext();

        boolean testFailed = false;
        String errorMessage = "";

        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        for (WebElement errorMessages : errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages()) {
            String errorString = errorMessages.getText();
            if (errorString.contains("Have you ever had insurance cancelled, refused or declined?")) {
                testFailed = true;
                errorMessage = errorMessage + "Have you ever had insurance cancelled, refused or declined? Question not filled.\n";
            }

            if (errorString.contains("Have you filed for bankruptcy in the last five (5) years?")) {
                testFailed = true;
                errorMessage = errorMessage + "Have you filed for bankruptcy in the last five (5) years?";
            }

            testFailed = true;
            errorMessage = errorMessage + "Expected Pending Transaction status Quoted is not displayed. \n";
            GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
            for (int i = 0; i < 4; i++) {
                eligibilityPage.clickNext();
            }

            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
            risk.Quote();
            new GuidewireHelpers(driver).logout();
            new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibObj.accountNumber);

            PolicySummary summaryPage = new PolicySummary(driver);
            if (!summaryPage.getPendingPolicyTransactionByColumnName(TransactionType.Renewal.getValue(), "Status").contains("Quoted")) {
                testFailed = true;
                errorMessage = errorMessage + "Expected Pending Transaction status Quoted is not displayed. \n";
            }
            if (testFailed)
                Assert.fail(errorMessage);
        }
    }
}


