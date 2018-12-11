package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.Date;

/**
 * @Author swathiAkarapu
 *  * @Requirement :DE7803
 *  * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/246632856848">Make 'editInceptionDateOverrideCommision' work for role of underwriter on rewrite jobs</a>
 *  * @Description :
 *          As agent start new submission
 *          Quote and submit/issue
 *          As underwriter issue and cancel (flat cancel) so can rewrite to fix the inception dates (follow steps below to cancel)
 *          As underwriter start the rewrite full term to fix inceptions dates
 *          Should be available for any underwriter on any job
 *              source: carrier
 *              reason: policy rewritten or replaced (flat cancel)
 *              reason explanation: other
 *              reason description: Correcting inception dates
 *  * @DATE August 21, 2018
 */
public class DE7803_rewrittenInceptionDate extends BaseTest {
    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void VerifyInceptiondateOnFlatCancel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
      myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("inceptionDate", "reWrite")
                .build(GeneratePolicyType.PolicyIssued);
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
       new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();

        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("Correcting inception dates");
        pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();

        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteFullTerm();
    try {
            GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
            pcSideMenu.clickSideMenuPolicyInfo();
            polInfo.setTransferedFromAnotherPolicy(true);
            polInfo.setCheckBoxInInceptionDateByRow(1, true);
            polInfo.setCheckBoxInInceptionDateByRow(2, true);
            polInfo.setCheckBoxInInceptionDateByRow(3, true);
            Date newEffectiveDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                    DateAddSubtractOptions.Day, 1);
            polInfo.setInceptionDateByRow(1, newEffectiveDate);
            polInfo.setInceptionDateByRow(2, newEffectiveDate);
            polInfo.setInceptionDateByRow(3, newEffectiveDate);
            polInfo.clickNext();
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Inception dates - not editable");
        }
    }
}
