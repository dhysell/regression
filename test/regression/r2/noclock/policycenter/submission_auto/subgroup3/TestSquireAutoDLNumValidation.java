package regression.r2.noclock.policycenter.submission_auto.subgroup3;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.DriverLicenseNumbers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.DriverLicenseNumbersHelpers;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquireAutoDLNumValidation extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void testGenerateAutoPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Driverslicense")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"testGenerateAutoPolicy"})
    public void testDriversLicenseNumberValidationsAllStates_ValidNums() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(this.uw.getUnderwriterUserName(), this.uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_ContactDetail drivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        drivers.clickEditButtonInDriverTable(1);

        ArrayList<State> states = State.valuesOriginal50();
        State stateToCheck;

        for (State st : states) {
            stateToCheck = st;
            drivers.setLicenseNumber("");
            drivers.sendArbitraryKeys(Keys.TAB);
            drivers.selectLicenseState(stateToCheck);
            drivers.sendArbitraryKeys(Keys.TAB);
            List<DriverLicenseNumbers> results = DriverLicenseNumbersHelpers.getDLNumbers(st);
            for (DriverLicenseNumbers dln : results) {
                String validNumber = dln.getDlnumber();
                drivers.setLicenseNumber(validNumber); // valid number
                drivers.sendArbitraryKeys(Keys.TAB);
                drivers.clickContactDetailTab();
                Assert.assertEquals(drivers.checkIfInvalidDriversLicenseMessageExists(), false, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " Error message was showing and should not");
                Assert.assertEquals(drivers.checkIfMVRButtonIsClickable(), true, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " MVR Button is not available and should be");
            }
        }

    }

    @Test(dependsOnMethods = {"testDriversLicenseNumberValidationsAllStates_ValidNums"})
    public void testDriversLicenseNumberValidationsAllStates_InvalidNums() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(this.uw.getUnderwriterUserName(), this.uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);

        try {
            GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
            polInfo.clickEditPolicyTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_ContactDetail drivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        drivers.clickEditButtonInDriverTable(1);

        ArrayList<State> states = State.valuesOriginal50();
        State stateToCheck;

        for (State st : states) {
            stateToCheck = st;
            drivers.setLicenseNumber("");
            drivers.sendArbitraryKeys(Keys.TAB);
            drivers.selectLicenseState(stateToCheck);
            drivers.sendArbitraryKeys(Keys.TAB);
            List<DriverLicenseNumbers> results = DriverLicenseNumbersHelpers.getDLNumbers(st);
            for (DriverLicenseNumbers dln : results) {
                String invalidNumber = dln.getDlnumber() + "Z1Y2X3"; // invalid number
                drivers.setLicenseNumber(invalidNumber);
                drivers.sendArbitraryKeys(Keys.TAB);
                Assert.assertEquals(drivers.checkIfInvalidDriversLicenseMessageExists(), true, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Invalid Number: " + invalidNumber + " Error message was not showing and should");
                //Assert.assertEquals(drivers.checkIfMVRButtonIsClickable(), false, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Invalid Number: " + invalidNumber + " MVR Button is available and should not be");
            }
        }

    }

}
