package regression.r2.noclock.policycenter.submission_auto.subgroup1;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
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
public class TestRatedAge3 extends BaseTest {
    private GeneratePolicy Squire_PolicyObj;
    private Underwriters underwriterInfo;

    private WebDriver driver;


    //Rated Age check on Renewal
    @Test()
    public void testGenerateSquireAutoRenewal() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Guy", "Auto")
                .withPolTermLengthDays(80)
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoRenewal"})
    public void testSquire_RenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        underwriterInfo = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(Squire_PolicyObj);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicyByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), Squire_PolicyObj.accountNumber);
        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(Squire_PolicyObj.accountNumber, "003");


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
        GenericWorkorderPolicyMembers policyMembers = new GenericWorkorderPolicyMembers(driver);
        policyMembers.clickPolicyHouseHoldTableCell(1, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        String policyMemberRatedAge = householdMember.getRatedAge();
        Date DOB = householdMember.getDateOfBirth();
        householdMember.clickCancel();

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
