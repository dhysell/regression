package regression.r2.noclock.policycenter.submission_auto.subgroup1;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;

/**
 * @Author skandibanda
 * @Requirement : DE4153 : Rated age calculation
 * @Description -  Rated age calculation should be calculating based on the transaction effective date of the new submission, renewal, rewrite, or policy change.
 * Issue policy,
 * @DATE Nov 21, 2016
 */
public class TestRatedAge extends BaseTest {
    private GeneratePolicy myPolicyObjQQ;

    private WebDriver driver;

    //Rated Age check on Submission
    @Test()
    public void testGenerateSquireAutoQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -320);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjQQ = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Guy", "Auto")
                .withPolEffectiveDate(newEff)
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.QuickQuote);


    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoQQ"})
    public void verifyRatedAgeINQQ() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObjQQ.agentInfo.getAgentUserName(), myPolicyObjQQ.agentInfo.getAgentPassword(), myPolicyObjQQ.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();

        //Validating Rated Age
        validateRatedAge();
    }


    private void validateRatedAge() throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);

        //Get policy Info Transaction effective date
        sideMenu.clickSideMenuPolicyInfo();
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
