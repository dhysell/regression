package regression.r2.noclock.policycenter.rescind;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement This set of tests does three main things: first it issues a squire auto only policy, then it logs in as an UW and schedules a cancellation, then it logs in as the UW and rescinds the cancellation.  If the confirmation page appears then the test passes.
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jun 16, 2016
 */
public class TestSquireRescindCancellation extends BaseTest {
    private GeneratePolicy mySQPolicyObjPL = null;
    private Underwriters underwriter = new Underwriters();

    private WebDriver driver;

    @Test()
    public void testCreateSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("SQ", "Rescind")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testCreateSquirePolicy"})
    private void testSquireScheduleCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(this.underwriter.getUnderwriterUserName(), this.underwriter.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date cancelDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10);
        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, cancelDate, false);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testSquireScheduleCancellation"})
    private void testSquireRescindCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(this.underwriter.getUnderwriterUserName(), this.underwriter.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRescindCancellation();
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.clickNextButton();
        cancelPol.clickCloseOptionsRescindCancellation();

        if (!cancelPol.getCancellationJobCompleteTitle().contains("Cancellation Rescinded")) {
            Assert.fail("Rescind Cancellation is not done");
        }

        new GuidewireHelpers(driver).logout();
    }
}
