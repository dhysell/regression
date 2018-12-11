package regression.r2.noclock.policycenter.cancellation;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author nvadlamudi
 * @Requirement : DE4124: Unable to cancel a policy - Stack overflow error displayed
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 10, 2016
 */
@QuarantineClass
public class TestPLSquireAutoOnlyFullCancel extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL = null;

    @Test()
    public void issueSquireAutoOnly() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withProductType(ProductLineType.Squire)
				.withInsFirstLastName("Auto", "Flat")
				.withPolEffectiveDate(newEff)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber("lbarber", "gw", mySQPolicyObjPL.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");

        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickCloseWorkSheet();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        //Handles prequote issues for PL while they work on the
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
            risk.approveAll_IncludingSpecial();
            qualificationPage.clickQuote();
        }

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();
    }


    @Test(dependsOnMethods = {"issueSquireAutoOnly"})
    private void testCancelSquireAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber("lbarber", "gw", mySQPolicyObjPL.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.CloseDidNotHappen, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(mySQPolicyObjPL.accountNumber);
        String errorMessage = "";

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not done!!!";

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

}
