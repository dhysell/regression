package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.PolicyActivities;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.RoleAgentChange;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author swathiAkarapu
 * @Requirement US16032
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/246879086756"> US16032</a>
 * @Description
 * As an agent, PA, or underwriter, I do not want an activity when policy member info gets changed so that I don't have to spend time deleting activities that I do not need to process.
 *
 * Steps to get there:
 * Have an existing policy
 * Do a policy change to edit a contact on the policy
 * issue policy change
 * Check for activities (ensure an activity is not created)
 * Acceptance criteria:
 * Ensure that a "Contact Detail Changed" activity is not created for a policy change where a contact is changed
 * Ensure that we check that this applies to contact details changed in varied areas of the policies: policy members screen, drivers screen, membership screen, policy members screen, additional insureds, PNIs, etc.
 * Ensure that this applies on rewrite jobs also (recreate this to see if it applies to rewrite jobs)  - It does as of 10/4/18
 *
 * @DATE october 11, 2018
 */


public class US16032ShutActivityWhenPolicyChange extends BaseTest  {
    private   GeneratePolicy myPolicyObject= null;
    private WebDriver driver;

    private  void generate() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("new", "NOActivity")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }
    @Test(enabled = true)
    public void testPolicyChangeWithContactDetailsChange() throws Exception {
       generate();

       RoleAgentChange agentChangeRoleLogin= RoleAgentChange.getRandom();;
       new Login(driver).loginAndSearchPolicyByAccountNumber(agentChangeRoleLogin.getUserName(), agentChangeRoleLogin.getPassword(),
                myPolicyObject.accountNumber);

        Agents agentInfo = AgentsHelper.getRandomAgentOutSideCounty(myPolicyObject.agentInfo.getAgentCounty());

        // start policy change
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change Agent out Side County", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.setAgentOfService(agentInfo);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();

        boolean  contactDetailsChange =false;
        PolicyActivities currentActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_BasicInfo());
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains("Contact") && activity.getSubject().contains("Changed") ) {
                contactDetailsChange = true;

                break;
            }
        }

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(contactDetailsChange, "The Contact Changed activity triggered but should not");
        softAssert.assertAll();
    }


    @Test(enabled = true)
    public void testReWriteWithContactDetailsChange() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("new", "NOActivity")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);


            new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);


        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        SoftAssert softAssert = new SoftAssert();

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);

        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();

        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("testing");
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();

        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteNewTerm();
        pcWorkOrder.clickNext();

        sideMenu.clickSideMenuQualification();
        repository.pc.workorders.generic.GenericWorkorderQualification pcQualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
        pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);
        sideMenu.clickPolicyContractSectionIIIAutoLine();
        sideMenu.clickSideMenuPADrivers();


        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(myPolicyObject.pniContact.getFirstName());
         paDrivers.setLicenseNumber("XW304757A");
        paDrivers.clickOk();
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.approveAll();

        StartRewrite rewriteWO = new StartRewrite(driver);
        rewriteWO.visitAllPages(myPolicyObject);

        pcWorkOrder.clickGenericWorkorderQuote();

        pcWorkOrder.clickIssuePolicyButton();

         new GuidewireHelpers(driver).waitUntilElementIsVisible(pcWorkorderCompletePage.getJobCompleteTitleBar());
        pcWorkorderCompletePage.clickPolicyNumber();
        boolean  contactDetailsChange =false;
        PolicyActivities currentActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_BasicInfo());
        for (Activity activity : currentActivities.getPolicyActivityList()) {
            if (activity.getSubject().contains("Contact") && activity.getSubject().contains("Changed") ) {
                contactDetailsChange = true;

                break;
            }
        }

        softAssert.assertFalse(contactDetailsChange, "The Contact Changed activity triggered but should not");
        softAssert.assertAll();
    }
}
