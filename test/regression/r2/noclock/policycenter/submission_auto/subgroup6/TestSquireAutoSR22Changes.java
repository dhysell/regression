package regression.r2.noclock.policycenter.submission_auto.subgroup6;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SR22FilingFee;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquireAutoSR22Changes extends BaseTest {

    private String agentUserName;
    private String agentPassword;
    private String accountNumber;
    private String firstName = "SR22";
    private String lastName = "Changes";
    private SideMenuPC sideMenu;
    private GenericWorkorderSquireAutoDrivers_ContactDetail driverPage;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void testSquireAuto_FullApp() throws Exception {

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact toAdd = new Contact();
        toAdd.setRelationToInsured(RelationshipToInsured.Insured);
        driversList.add(toAdd);

        // vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // whole auto line
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName(firstName, lastName)
                .build(GeneratePolicyType.FullApp);

        Agents agent = myPolicyObj.agentInfo;
        agentUserName = agent.getAgentUserName();
        agentPassword = agent.getAgentPassword();
        accountNumber = myPolicyObj.accountNumber;
    }

    @Test(dependsOnMethods = {"testSquireAuto_FullApp"})
    public void testSquireAuto_SR22Changes() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.loginAndSearchJob(agentUserName, agentPassword, accountNumber);

        // edit transaction
        GenericWorkorder wo = new GenericWorkorder(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        // go to drivers
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        // check sr-22
        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driverPage.clickEditButtonInDriverTable(2);

        //Requirement: Any users will be able to select SR-22
        driverPage.setSR22Checkbox(true);
        if (!driverPage.isSR22CheckboxChecked()) {
            Assert.fail("The agent should be able to check the SR-22 coverage.");
        }
        driverPage.clickOk();
        wo.clickGenericWorkorderSaveDraft();
        // logout and login as UW Supervisor
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        login.loginAndSearchJob(uw.underwriterUserName, uw.getUnderwriterPassword(), accountNumber);

        // go to drivers
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers driversPage = new GenericWorkorderSquireAutoDrivers(driver);
        driversPage.clickEditButtonInDriverTable(2);

        // edit sr-22
        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driverPage.setSR22Checkbox(false);
        driverPage.setSR22Checkbox(true);
        driversPage.sendArbitraryKeys(Keys.TAB);
        if (driverPage.isSR22CheckboxChecked()) {
            driverPage.setSR22FilingFee(SR22FilingFee.Charged);

            driverPage.setSR22FilingFee(SR22FilingFee.Reversed);
            driverPage.clickOk();
        } else {
            Assert.fail("The underwriter  supervisor not able to select the SR-22 coverage.");
        }

        wo.clickGenericWorkorderSaveDraft();

        // logout and back in as agent
        guidewireHelpers.logout();
        login.loginAndSearchJob(agentUserName, agentPassword, accountNumber);

        // check sr-22
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driversPage.clickEditButtonInDriverTable(2);
        if (!driverPage.getSR22FilingFee().contains(SR22FilingFee.Reversed.getValue())) {
            Assert.fail("The agent should be able to see what the underwriter set the SR-22 coverage to.");
        }

    }

    @Test(dependsOnMethods = {"testSquireAuto_SR22Changes"})
    public void testValidateWithUWAndUWAssistantRoles() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        String errorMessage = "";
        //UW Assistant

        Login login = new Login(driver);
        login.loginAndSearchJob("jfullmer", "gw", accountNumber);

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_ContactDetail driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driverPage.clickEditButtonInDriverTable(2);

        // edit sr-22
        driverPage.setSR22Checkbox(false);

        if (driverPage.isSR22CheckboxChecked()) {
            errorMessage = errorMessage + " UW Assistant - SR22 checkbox can't unchecked\n";
        }
        driverPage.clickOk();
        GenericWorkorder wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();

        driverPage.clickEditButtonInDriverTable(2);
        driverPage.setSR22Checkbox(true);
        driverPage.sendArbitraryKeys(Keys.TAB);
        driverPage.setSR22FilingFee(SR22FilingFee.Charged);
        driverPage.sendArbitraryKeys(Keys.TAB);
        List<String> values = driverPage.getSR22FilingFee();
        for (String current : values) {
            if (current.equals(SR22FilingFee.Reversed.getValue())) {
                errorMessage = errorMessage + " UW Assistant - SR 22 - Filing Fee - Reversed option is available \n";
            }
        }

        if (!driverPage.isSR22CheckboxChecked()) {
            errorMessage = errorMessage + " UW Assistant - SR22 checkbox can't checked\n";
        }
        driverPage.clickOk();
        wo.clickGenericWorkorderSaveDraft();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        guidewireHelpers.logout();

        //login with UW role
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        login.loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), accountNumber);
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driverPage.clickEditButtonInDriverTable(2);


        // edit sr-22
        driverPage.setSR22Checkbox(false);
        if (driverPage.isSR22CheckboxChecked()) {
            errorMessage = errorMessage + " UW - SR22 checkbox can't unchecked\n";
        }
        driverPage.clickOk();
        wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();

        driverPage.clickEditButtonInDriverTable(2);
        driverPage.setSR22Checkbox(true);
        driverPage.sendArbitraryKeys(Keys.TAB);
        driverPage.setSR22FilingFee(SR22FilingFee.Charged);

        values = driverPage.getSR22FilingFee();
        for (String current : values) {
            if (current.equals(SR22FilingFee.Reversed.getValue())) {
                errorMessage = errorMessage + " UW  - SR 22 - Filing Fee - Reversed option is available \n";
            }
        }

        if (!driverPage.isSR22CheckboxChecked()) {
            errorMessage = errorMessage + " UW - SR22 checkbox can't checked\n";
        }
        driverPage.clickOk();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();


        //The below section is added part of the DEFECT DE4176: SR 22 charge is included in Section III premium and it shouldnt be
        quote.clickQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuQuote();

        if (quote.getQuoteSR22Charge() != 25)
            errorMessage = errorMessage + "SR22 Charges $25 is not shown on the quote page.\n";


        double sectionIIIPremium = 0;
        for (int currentVehicle = 1; currentVehicle <= quote.getSectionIIIAutoVehicleRowCount(); currentVehicle++) {
            sectionIIIPremium = sectionIIIPremium + quote.getSectionIIIVehiclePremiumByRow(currentVehicle);
        }

        if (sectionIIIPremium != quote.getSquirePremiumSummaryAmount("Section III", false, true, false)) {
            errorMessage = errorMessage + "Section III premium is not matched with summary vs. details displayed bottom of the page \n";
        }

        guidewireHelpers.logout();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

}
