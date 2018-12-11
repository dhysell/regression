package regression.r2.noclock.policycenter.renewaltransition.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US9112 : Sibling pre-renewal direction
 * @Description - Issued Squire Auto policy, created Squire Full app and then transferred issued policy to this and then issued second policy
 * system triggers Prerenewal diretion on selecting Renewal from policy actions, validate System will generate a pre-renewal direction if organization type change from Sibling to Individual
 * The pre-renewal direction will be assigned to Underwriter, also Underwriter will be able to close the pre-renewal direction
 * @DATE Feb 24, 2017
 */
public class TestSiblingPreRenewalDirections extends BaseTest {

    private GeneratePolicy squirePolicy, siblingSquirePolicy;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testIssueSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true,
                UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("SUSAN", "REDWINE")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePol"})
    private void testCreateSquireFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -295);


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        siblingSquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Test", "Sibling")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateSquireFA"})
    private void testChangeMaritalStatusAndDOB() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(siblingSquirePolicy.agentInfo.getAgentUserName(), siblingSquirePolicy.agentInfo.getAgentPassword(), siblingSquirePolicy.accountNumber);


        Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -24);
        Date Dob = DateUtils.dateAddSubtract(dob, DateAddSubtractOptions.Day, -358);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(siblingSquirePolicy.pniContact.getFirstName());
        paDrivers.setAutoDriversDOB(Dob);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.clickOk();


        sideMenu.clickSideMenuPolicyInfo();
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);
        policyInfo.sendArbitraryKeys(Keys.TAB);

        policyInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, squirePolicy.squire.getPolicyNumber());
        new GuidewireHelpers(driver).clickNext();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(siblingSquirePolicy);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            risk.handleBlockSubmit(siblingSquirePolicy);
        }

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"testChangeMaritalStatusAndDOB"})
    private void testIssueSiblingSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        siblingSquirePolicy.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSiblingSquirePolicy"})
    private void validatePreRenewalDirections() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), siblingSquirePolicy.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();


        // Verify System generated Pre-renewal direction organization type changed from Sibling to Individual
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(siblingSquirePolicy.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        StartRenewal renewal = new StartRenewal(driver);
        renewal.waitForPreRenewalDirections();
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review for Squire")) {
            summaryPage.clickViewPreRenewalDirection();

            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (!preRenewalDirectionExists) {
                Assert.fail("An Activity was found with no preRenewal Direction.");
            } else {
                preRenewalPage.clickViewInPreRenewalDirection();

                if (!preRenewalPage.checkPreRenewalDirectionWithExpectedCode("Sibling Policy Change")) {
                    Assert.fail("Expected Explanation Code:  Sibling Policy Change is not displayed.");
                }
            }
        } else
            Assert.fail("Expected Activity 'Pre-Renewal Review for Squire is not displayed.");

        //Verify Pre-Renewal directions assigned to Underwriter
        ArrayList<String> activityOwner = new ArrayList<String>();
        AccountSummaryPC accountSummary = new AccountSummaryPC(driver);
        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(siblingSquirePolicy.accountNumber);
        activityOwner = accountSummary.getActivityAssignedTo("Pre-Renewal Review for Squire");
        String underWriter = activityOwner.get(0);
        uw = UnderwritersHelper.getUnderwriterInfoByFullName(underWriter);
        new GuidewireHelpers(driver).logout();

        //Verify Assigned Underwriter able to close the Pre-renewal direction
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), siblingSquirePolicy.accountNumber);

        summaryPage = new PolicySummary(driver);
        preRenewalPage = new PolicyPreRenewalDirection(driver);
        renewal.waitForPreRenewalDirections();
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
            summaryPage.clickViewPreRenewalDirection();
            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (preRenewalDirectionExists) {
                preRenewalPage.clickViewInPreRenewalDirection();
                preRenewalPage.closeAllPreRenewalDirectionExplanations();
                preRenewalPage.clickClosePreRenewalDirection();
            }

            if (!underWriter.equals(preRenewalPage.getPreRenewalClosedByUW()) || !preRenewalPage.getPreRenewalClosedByUW().contains(uw.underwriterLastName))
                Assert.fail("Assigned Underwriter Unable to close the Pre-Renewal Directions");

            preRenewalPage.clickReturnToSummaryPage();
        }


        summaryPage = new PolicySummary(driver);
        summaryPage.clickPendingTransaction(TransactionType.Renewal);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

    }
}
