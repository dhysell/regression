package regression.r2.noclock.policycenter.documents;

import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.desktop.DesktopMyOtherWorkOrders;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US12328: COMMON - Make all activities and jobs go to same
 * person
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20CommonAdmin%20-%20Policy%20File%20-%20Summary.xlsx">
 * Story card</a>
 * @Description
 * @DATE Jan 25, 2018
 */
@QuarantineClass
public class TestCheckActivitiesJobsAssignedUser extends BaseTest {
    Underwriters uw;
    private GeneratePolicy myPolicyObj;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private WebDriver driver;

    @Test
    public void testIssueSquireAutoPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Activity", "Assigned")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquireAutoPol"})
    public void testCheckRequestedApproved() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(),
                myPolicyObj.agentInfo.getAgentPassword(), this.myPolicyObj.squire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        linkExistingDocument(DocumentType.Photos);
        linkExistingDocument(DocumentType.Cost_Estimator);
        linkExistingDocument(DocumentType.GoodStudentDiscount);

        PolicySummary pSummary = new PolicySummary(driver);
        String errorMessage = "";
        String user = pSummary.getActivityAssignment("Photos Received");
        if (!user.equalsIgnoreCase(pSummary.getActivityAssignment("Cost Estimator Received"))
                && !user.equalsIgnoreCase(pSummary.getActivityAssignment("Good Student Doc Received"))) {
            errorMessage = errorMessage
                    + "Cost Estimator Received and Good Student Doc Received activities are not assigned to " + user
                    + "/n";
        }

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", currentSystemDate);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        policyChangePage.clickSubmitPolicyChange();
        UWActivityPC activityCreate = new UWActivityPC(driver);
        activityCreate.setText("pol change");
        activityCreate.setChangeReason(ChangeReason.MultipleChanges);
        activityCreate.clickSendRequest();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarAccountNumber();
        AccountSummaryPC aSumm = new AccountSummaryPC(driver);
        aSumm.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        if (!user.equalsIgnoreCase(pSummary.getActivityAssignment("Submitted policy change"))) {
            errorMessage = errorMessage + "Submitted Policy change activity not assigned to " + user + "/n";
        }
        StartCancellation cancelPol = new StartCancellation(driver);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, comment, currentSystemDate,
                true);
        activityCreate.setText("pol cancel");
        activityCreate.clickSendRequest();
        infoBar.clickInfoBarAccountNumber();
        aSumm.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        if (!user.equalsIgnoreCase(pSummary.getActivityAssignment("Submitted cancellation"))) {
            errorMessage = errorMessage + "Submitted cancellation activity not assigned to " + user + "/n";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    @Test(dependsOnMethods = {"testCheckRequestedApproved"})
    public void testUWTrasactionsValidations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObj.squire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("UW policy Change", currentSystemDate);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();
        PolicySummary pSummary = new PolicySummary(driver);
        String transactionNum = pSummary.getPendingPolicyTransactionByColumnName("UW policy Change", "Transaction #");
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickDesktopTab();

        DesktopSideMenuPC myOtherWorkOrders = new DesktopSideMenuPC(driver);
        myOtherWorkOrders.clickMyOtherWorkOrders();
        DesktopMyOtherWorkOrders otherWorkOrders = new DesktopMyOtherWorkOrders(driver);
        if (!otherWorkOrders.getTypeByTransactionNum(transactionNum).equalsIgnoreCase("Policy Change")) {
            Assert.fail("Expected workorder for recent policy change is not available. ");
        }
    }

    private void linkExistingDocument(DocumentType documentType) {
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Doc Type: " + documentType);
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        // delay required to upload the documents
        doc.selectRelatedTo("Policy");
        doc.selectDocumentType(documentType);
        doc.sendArbitraryKeys(Keys.TAB);
        doc.clickUpdate();

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickAccountTab();

        AccountSummaryPC aSummary = new AccountSummaryPC(driver);
        aSummary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);

    }
}
