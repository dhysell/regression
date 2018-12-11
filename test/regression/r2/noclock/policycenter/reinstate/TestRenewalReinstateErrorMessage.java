package regression.r2.noclock.policycenter.reinstate;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4730 : After Renewal started, Reinstate is displaying error message
 * @Description - Issue Squire policy with short term  (80days), select renew policy from actions,
 * then  Cancel policy (Carrier, Lack of underwriting information, Loss runs),
 * Reinstate policy with reason payment received, verifying for any error message coming
 * @DATE Feb 28, 2017
 */
@QuarantineClass
public class TestRenewalReinstateErrorMessage extends BaseTest {
    private WebDriver driver;

    private GeneratePolicy squirePolObj;
    private Underwriters uw;

    @Test
    private void generateSquireAuto() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Reinstate")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generateSquireAuto"})
    private void validateReinstateErrorMessage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolObj.squire.getPolicyNumber());

        //Policy Renewal
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickPolicyNumber();

        //Cancel Policy
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

        complete.clickViewYourPolicy();

        //Reinstate Policy
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        StartReinstate reinstatePolicy = new StartReinstate(driver);
        reinstatePolicy.setReinstateReason("Payment received");

        cancelPol.setDescription("Testing purpose");

        reinstatePolicy.quoteAndIssue();
        ErrorHandlingHelpers errorHandling = new ErrorHandlingHelpers(driver);
        if (errorHandling.errorExists()) {
            if (errorHandling.getErrorMessage().contains("The object you are trying to update was") && errorHandling.getErrorMessage().contains("Please cancel and retry your change"))
                Assert.fail("Error Messsage " + errorHandling.getErrorMessage() + "should not be displayed.Job completed page should be displayed");
        }

    }
}
