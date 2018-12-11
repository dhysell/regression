package regression.r2.noclock.policycenter.submission_auto.subgroup1;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4153 : Rated age calculation
 * @Description -  Rated age calculation should be calculating based on the transaction effective date of the new submission, renewal, rewrite, or policy change.
 * Issue policy,
 * @DATE Nov 21, 2016
 */
public class TestRatedAge4 extends BaseTest {
    private GeneratePolicy SquPolicy;
    private Underwriters underwriterInfo;

    private WebDriver driver;

    //Rated Age check on Rewrite New Term
    @Test
    public void testGenerateSquireAutoNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -320);
        SquPolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Auto", "RewriteNewTerm")
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoNewTerm"})
    public void testRewriteNewTerm() throws Exception {
        underwriterInfo = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), SquPolicy.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -320);
        String comment = "For Rewrite New term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), SquPolicy.accountNumber);
//      PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		policyMenu.clickRewriteNewTerm();
        new StartRewrite(driver).startRewriteNewTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        String effectiveDate = DateUtils.dateFormatAsString("MM/dd/yyyy", currentDate);
        polInfo.setPolicyInfoEffectiveDate(effectiveDate);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        //Validating Rated Age
        validateRatedAge();

    }

    private void validateRatedAge() throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);

        //Get policy Info Transaction effective date
        sideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);

        //Get Policy Member Rated Age
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
        hmember.clickPolicyHouseHoldTableCell(1, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        String policyMemberRatedAge = householdMember.getRatedAge();
        Date DOB = householdMember.getDateOfBirth();
        householdMember.clickOK();

        sideMenu.clickSideMenuPolicyInfo();
        //Calculate Rated age depending on the transaction effective date and DOB
        int ratedAge = DateUtils.getDifferenceBetweenDates(DOB, polInfo.getPolicyInfoEffectiveDate(), DateDifferenceOptions.Year);

        //Get Drivers Rated age
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTable(1);

        String driversRatedAge = paDrivers.getDriverRatedAge();

        //validate rated age calculated depending upon transaction effective date
        boolean testFailed = false;
        String errorMessage = "";

        if (ratedAge != Integer.parseInt(policyMemberRatedAge)) {
            testFailed = true;
            errorMessage = errorMessage + "Policy Members Rated age is not calculated based upon transaction effective date.\n";
        }
        if (ratedAge != Integer.parseInt(driversRatedAge)) {
            testFailed = true;
            errorMessage = errorMessage + "Drivers Rated age is not calculated based upon transaction effective date.";
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }

}
