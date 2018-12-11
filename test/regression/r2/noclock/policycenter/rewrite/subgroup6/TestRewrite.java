package regression.r2.noclock.policycenter.rewrite.subgroup6;


import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.rewrite.StartRewrite;
import regression.r2.noclock.policycenter.cancellation.TestCancellation;

@QuarantineClass
public class TestRewrite extends BaseTest {

    private GeneratePolicy myPolicyObjFullTerm = null;

    private WebDriver driver;

    @Test
    public void testFullTerm() throws Exception {

        TestCancellation cancelled = new TestCancellation();
        cancelled.testFlatCancelInsuredBoundOnAccident();
        this.myPolicyObjFullTerm = cancelled.myPolicyObjFlatCancelled;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login myloginPage = new Login(driver);
        myloginPage.login(this.myPolicyObjFullTerm.underwriterInfo.getUnderwriterUserName(), this.myPolicyObjFullTerm.underwriterInfo.getUnderwriterPassword());

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
        policySearch.searchPolicyByAccountNumber(this.myPolicyObjFullTerm.accountNumber);

        StartRewrite rewritePol = new StartRewrite(driver);
        rewritePol.rewriteFullTerm(myPolicyObjFullTerm);


        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
        testRenewalRateDate(myPolicyObjFullTerm, TransactionType.Rewrite, myPolicyObjFullTerm.squire.getPolicyNumber());
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement: Rate Date must match the effective Date
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20CancelReinstateRewrite%20-%20Rewrite%20All%20Together.xlsx">Link Text</a>
     * @Description
     * @DATE Jun 6, 2017
     */
    private void testRenewalRateDate(GeneratePolicy myPol, TransactionType transactionType, String policyNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPol.underwriterInfo.getUnderwriterUserName(), myPol.underwriterInfo.getUnderwriterPassword(), policyNumber);
        PolicySummary summary = new PolicySummary(driver);
        Date rewriteEffectiveDate = summary.getCompletedTransactionEffectiveDate(transactionType);
        summary.clickCompletedTransactionByType(transactionType);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo pInfo = new GenericWorkorderPolicyInfo(driver);
        Date rateDate = pInfo.getPolicyInfoRateDate();
        if (!rateDate.equals(rewriteEffectiveDate)) {
            Assert.fail(transactionType.getValue() + " should have the Policy Rate Date be the same as the " + transactionType.getValue() + " Effective Date.");
        }
    }

}
