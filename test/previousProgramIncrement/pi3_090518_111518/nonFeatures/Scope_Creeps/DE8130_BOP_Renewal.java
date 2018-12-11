package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.IssuanceType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class DE8130_BOP_Renewal extends BaseTest {
    public GeneratePolicy myPolicyObj = null;
    private WebDriver driver;

    @Test(enabled = true)
    public void verifyBopRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorderQuote quote = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .withPolTermLengthDays(79)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
                myPolicyObj.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Businessowners);

        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);

        quote.clickQuote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }


        risk_UWIssues.requestAndApproveUwIssues(myPolicyObj);
        if (new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver).checkIfSpecialApproveButtonsExist()) { // handleBlockSubmit is not working due to current issue if the Uw is doesn't have the request Approval button  When  Special Approve button present
            new GuidewireHelpers(driver).logout();
            new Login(driver).loginAndSearchAccountByAccountNumber("acrawford",
                    "gw", myPolicyObj.accountNumber);
            pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Businessowners);
            pcSideMenu.clickSideMenuRiskAnalysis();
            risk_UWIssues.approveAll_IncludingSpecial();
            risk_UWIssues.clickReleaseLock();

        }
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Businessowners);

        pcSideMenu.clickSideMenuRiskAnalysis();
        pcSideMenu.clickSideMenuPayment();
        repository.pc.workorders.generic.GenericWorkorderPayment payment = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
        payment.clickAddDownPayment();
        payment.setDownPaymentType(repository.gw.enums.PaymentType.Cash);
        payment.setDownPaymentAmount(1000);
        payment.clickOK();

        pcSideMenu.clickSideMenuForms();
        payment.clickGenericWorkorderSubmitOptionsSubmit();

        completePage.clickAccountNumber();
        Underwriters uw1 = pcAccountSummaryPage.getAssignedToUW("Submitted Full Application");


        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw1.getUnderwriterUserName(), uw1.getUnderwriterPassword(), myPolicyObj.accountNumber);


        pcAccountSummaryPage.clickCurrentActivitiesSubject("Submitted Full Application");
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcWorkOrder.clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
        new GuidewireHelpers(driver).logout();

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObj.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        pcWorkOrder.clickNext();
        new GuidewireHelpers(driver).editPolicyTransaction();

        quote.clickQuote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnaysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
        }
        renewal.clickRenewPolicy(repository.gw.enums.RenewalCode.Renew_Good_Risk);

        Assert.assertTrue(completePage.getJobCompleteMessage().contains("is in the process of being renewed"), "Renewel is not Working");
    }
}
