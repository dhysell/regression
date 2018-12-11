package previousProgramIncrement.pi2_062818_090518.f225_MyWorkorderImprovements;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.desktop.DesktopMyOtherWorkOrders;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;

import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement US15511, US15469
 * @Description Create a column to show who initiated the job in MOWO, Create a column to show "Under UW Review" in MOWO
 * @DATE August 06, 2018
 */
public class CheckMOWOJobInitiatedByAndUnderUWReviewColumn extends BaseTest {
    public GeneratePolicy myPolicyObj = null;
    private WebDriver driver;
    @Test(enabled=true)
    public void testMyOtherWorkOrder() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withProductType(ProductLineType.Businessowners)
                .withBusinessownersLine(new PolicyBusinessownersLine(BusinessownersLine.SmallBusinessType.Apartments))
                .withLineSelection(LineSelection.Businessowners)
                .build(GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObj);

        PolicySummary pSummary = new PolicySummary(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("Agent policy Change", currentSystemDate);
        GenericWorkorderBuildings pcBuildingsPage = new GenericWorkorderBuildings(driver);
        sideMenu.clickSideMenuBuildings();
        pcBuildingsPage.clickEdit();
        pcBuildingsPage.setBuildingLimit(260000);
        pcBuildingsPage.clickOK();
        policyChangePage.quoteAndSubmit(ChangeReason.BuildingLimitsChanged,"building limits changes");
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();
        String transactionNum = pSummary.getPendingPolicyTransactionByColumnName("Agent policy Change", "Transaction #");
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickDesktopTab();
        DesktopSideMenuPC myOtherWorkOrders = new DesktopSideMenuPC(driver);
        myOtherWorkOrders.clickMyOtherWorkOrders();
        DesktopMyOtherWorkOrders otherWorkOrders = new DesktopMyOtherWorkOrders(driver);
        new GuidewireHelpers(driver).isOnPage("//span[contains(@id, 'DesktopOtherWorkOrders:DesktopOtherWorkOrdersScreen') and contains(text(), 'My Other Policy Transactions')]", 10000, "UNABLE TO GET TO MY OTHER POLICY TRANSACTIONS PAGE");
        String jobInitiatedByTransactionNum = otherWorkOrders.getJobInitiatedByTransactionNum(transactionNum);
        String underUWReviewByTransactionNum = otherWorkOrders.getUnderUWReviewByTransactionNum(transactionNum);
        
        softAssert.assertEquals(jobInitiatedByTransactionNum,myPolicyObj.agentInfo.getAgentFullName(),"Agent started policy Change- Job Initiated By, and Agent Name, are not Matching");
        softAssert.assertEquals(underUWReviewByTransactionNum, "Yes","Under UW Review is not Yes before release lock");
        new TopInfo(driver).clickTopInfoLogout();
        policyChangePage.releaseLockForPolicyChangeSubmittedByUW(myPolicyObj);
//        loginWithAgent();
        new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObj);
        topMenu.clickDesktopTab();
        myOtherWorkOrders.clickMyOtherWorkOrders();
        new GuidewireHelpers(driver).isOnPage("//span[contains(@id, 'DesktopOtherWorkOrders:DesktopOtherWorkOrdersScreen') and contains(text(), 'My Other Policy Transactions')]", 10000, "UNABLE TO GET TO MY OTHER POLICY TRANSACTIONS PAGE");
        otherWorkOrders.setDesktopMyOtherWorkOrdersFilter("Created in past 7 days");
        String underUWReviewByTransactionNumAfterApproval = otherWorkOrders.getUnderUWReviewByTransactionNum(transactionNum);
        softAssert.assertEquals(underUWReviewByTransactionNumAfterApproval, "No","Under UW Review is not No after release lock");
        softAssert.assertAll();
    }

//    private void loginWithAgent() {
//        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
//                myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
//    }


}
