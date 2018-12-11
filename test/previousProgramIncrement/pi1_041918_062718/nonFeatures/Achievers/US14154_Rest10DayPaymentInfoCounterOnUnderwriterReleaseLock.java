package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;


import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author jlarsen
 * @Requirement Steps to get there:
 * Create a Submission, Rewrite New Term, or Rewrite Full Term
 * Quote
 * Visit payment screen
 * Enter payment information (Down Payment and/or Payment Info)
 * 'Request Approval' to Home Office
 * Home Office user reviews the job but has it for more then 10 days
 * 'Release Lock' the job back to the agent
 * Agent waits until the next day to review the job again - The "Removed Saved Payment Info" batch ran over night and the payment information is not gone
 * We need the 10 day clock to start over when the 'Release Lock' is used to allow the agent the full 10 days to finish the job and submit it to home office.  See Payment Story Card:  UI Mockup, .pcf
 * <p>
 * Acceptance Criteria:
 * <p>
 * On a new submission enter payment information and "Request Approval." Move clock 10 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * On a new submission enter payment information and "Request Approval." Move clock 30 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite New Term and enter payment information.  "Request Approval." Move clock 10 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite New Term and enter payment information.  Move clock 2 days and then "Request Approval."  Move clock 10 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite New Term, enter payment information and "Request Approval." Move clock 30 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite New Term and enter payment information.  Move clock 9 days and then "Request Approval."  Move clock 30 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite Full Term and enter payment information.  "Request Approval." Move clock 10 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite Full Term and enter payment information.  Move clock 2 days and then "Request Approval."  Move clock 10 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite Full Term, enter payment information and "Request Approval." Move clock 30 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * Quote a Rewrite Full Term and enter payment information.  Move clock 9 days and then "Request Approval."  Move clock 30 days and Release Lock.  The screen must display the 10 day message.  Move clock 10 days and payment information must still display, move the clock 1 more day and the payment information must be expired and no longer visible.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/199264899040">US14154</a>
 * @Description When payment info is entered and transaction is released back to agent, start the 10 day count over
 * @DATE May 8, 2018
 */
@Test(groups = {"ClockMove"})
public class US14154_Rest10DayPaymentInfoCounterOnUnderwriterReleaseLock extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    @Test(enabled = true)
    public void verify10DayCounterReset() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));
        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .isDraft()
                .build(GeneratePolicyType.FullApp);
        Login login = new Login(driver);

        login.loginAndSearchSubmission(myPolicyObject);
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        new GenericWorkorder(driver).clickGenericWorkorderQuote();
        new GenericWorkorderPayment(driver).fillOutPaymentPage(myPolicyObject);

        new GenericWorkorderRiskAnalysis(driver).requestApproval();
        new GuidewireHelpers(driver).logout();
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Remove_Saved_PaymentInfo);
        Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
        login.loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        new GenericWorkorderRiskAnalysis(driver).approveAll();
        new GenericWorkorderRiskAnalysis(driver).clickReleaseLock();
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Remove_Saved_PaymentInfo);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchSubmission(myPolicyObject);
        new SideMenuPC(driver).clickSideMenuPayment();
        Assert.assertFalse(driver.findElements(By.xpath("//label[contains(@id, 'SubmissionWizard_PaymentScreen:BillingAdjustmentsDV') and text()='Payment Info']")).isEmpty(), "10 DAY MESSAGE WAS NOT VISIBLE AFTER MOVING 11 DAYS WITH THE AGENT RELEASING THE LOCK AT DAY 10");
        new GuidewireHelpers(driver).logout();
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 11);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Remove_Saved_PaymentInfo);
        login.loginAndSearchSubmission(myPolicyObject);
        new SideMenuPC(driver).clickSideMenuPayment();
        Assert.assertTrue(driver.findElements(By.xpath("//label[contains(@id, 'SubmissionWizard_PaymentScreen:BillingAdjustmentsDV') and text()='Payment Info']")).isEmpty(), "10 DAY MESSAGE WAS VISIBLE AFTER MOVING 21 DAYS WITH THE AGENT RELEASING THE LOCK AT DAY 10");
    }
}















