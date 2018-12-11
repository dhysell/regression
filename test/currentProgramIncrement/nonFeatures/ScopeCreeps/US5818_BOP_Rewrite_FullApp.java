package currentProgramIncrement.nonFeatures.ScopeCreeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PLForms;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQualification;

import java.util.Date;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US5818
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/44383115076">US5818</a>
 * @Description
 * When a Rewrite Full Term and Rewrite New Term are quoted we need to produce the document "Businessowners Full Application" (IDBP 01 0006 04 13) so that the agent can preview it from the forms and print the document as needed.
 *
 * When the job is issued, we need the form to drop into the documents but do not physically print the document (mimic the workings of the form like on a submission job)
 *     Also we need to fix the physical printing part on Rewrite New Term job.
 *
 *
 * Requirements:   11.6  BOP Product Model Spreadsheet IDBP 01 0006 04 13
 *
 * Acceptance Criteria:
 * Ensure that on a Rewrite Full Term job,
 * Ensure that when user quotes a Rewrite Full Term job, "Businessowners Full Application (IDCP 01 0006 04 13)" to be produced on the Forms page.
 * Ensure that when user issues a Rewrite Full Term job, "Businessowners Full Application (IDCP 01 0006 04 13)" appears on the Documents screen
 * Ensure that we don't physically print the document, but it should be AVAILABLE for the user to print if wanted (should be able to "preview," not go to view).
 * Ensure that on a Rewrite New Term job,
 * Ensure that when user quotes a Rewrite New Term job, "Businessowners Full Application (IDCP 01 0006 04 13)" to be produced on the Forms page.
 * Ensure that when user issues a Rewrite New Term job, "Businessowners Full Application (IDCP 01 0006 04 13)" appears on the Documents screen
 *
 * @DATE November 29, 2018
 */

public class US5818_BOP_Rewrite_FullApp extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generate(Date effectiveDate) throws Exception {
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .withPolEffectiveDate(effectiveDate)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test
    public void verifyBopRewriteFullTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);

        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        cancel(pcWorkorderCompletePage, pcPolicyMenu, pcCancellationPage , repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);

        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteFullTerm();
          while(!new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")){
            pcWorkOrder.clickNext();
        }

        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        softAssert.assertTrue(formsDescriptions.contains(repository.gw.enums.PLForms.IDBP010006_BusinessownersFullApplication.getName()), "After Quote 'Full Appplication' not found on Forms");
        pcWorkOrder.clickIssuePolicyButton();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcSideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments policyDocuments = new PolicyDocuments(driver);
        policyDocuments.selectRelatedTo("Rewrite Full Term");
        policyDocuments.clickSearch();
        softAssert.assertTrue( policyDocuments.getDocumentsDescriptionsFromTable().contains(repository.gw.enums.PLForms.IDBP010006_BusinessownersFullApplication.getName()), "After Issue 'Full Application' not Found on Documents");
        String printDate = policyDocuments.getOneColumnWithDocumentName(repository.gw.enums.PLForms.IDBP010006_BusinessownersFullApplication.getName(), "Date Will Print");
        softAssert.assertTrue(StringUtils.isBlank(printDate) , "Print Date should be empty but its not");
        softAssert.assertAll();

    }

    private void cancel(GenericWorkorderComplete pcWorkorderCompletePage, PolicyMenu pcPolicyMenu, StartCancellation pcCancellationPage , repository.gw.enums.Cancellation.CancellationSourceReasonExplanation cancellationSourceReasonExplanation) {
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(cancellationSourceReasonExplanation);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        pcWorkorderCompletePage.clickViewYourPolicy();
    }


    @Test
    public void verifyBopRewriteNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
         generate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -50));
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
       //cancel
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.OtherLackUWInfo);
        pcCancellationPage.setEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -40));
        pcCancellationPage.setDescription("test edit address after cancel");
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        // rewrite new term
        pcPolicyMenu.clickRewriteNewTerm();
        pcWorkOrder.clickNext();

        pcSideMenu.clickSideMenuQualification();
        GenericWorkorderQualification pcQualificationPage = new GenericWorkorderQualification(driver);
        pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);

        while(!new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")){
            pcWorkOrder.clickNext();
        }
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        softAssert.assertTrue(formsDescriptions.contains(repository.gw.enums.PLForms.IDBP010006_BusinessownersFullApplication.getName()), "After Quote 'Full Appplication' not found on Forms");
        pcWorkOrder.clickIssuePolicyButton();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcSideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments policyDocuments = new PolicyDocuments(driver);
        policyDocuments.selectRelatedTo("Rewrite New Term");
        policyDocuments.clickSearch();
        softAssert.assertTrue( policyDocuments.getDocumentsDescriptionsFromTable().contains(repository.gw.enums.PLForms.IDBP010006_BusinessownersFullApplication.getName()), "After Issue 'Full Application' not Found on Documents");
        String printDate = policyDocuments.getOneColumnWithDocumentName(PLForms.IDBP010006_BusinessownersFullApplication.getName(), "Date Will Print");
        softAssert.assertTrue(StringUtils.isBlank(printDate) , "Print Date should be empty but its not");
        softAssert.assertAll();
    }
}
