package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
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

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.globaldatarepo.entities.Agents;

@QuarantineClass
public class VerifyChargestoIssueSR22 extends BaseTest {

    private String agentUserName;
    private String agentPassword;
    private String accountNumber;
    private String firstName = "SR22";
    private String lastName = "Charges";
    private SideMenuPC sideMenu;
    private GenericWorkorderSquireAutoDrivers_ContactDetail driverPage;

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
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Scion", "But Goodie");
        vehicleList.add(vehicle);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
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

        new Login(driver).loginAndSearchJob(agentUserName, agentPassword, accountNumber);

        // edit transaction
        GenericWorkorder wo = new GenericWorkorder(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        // go to drivers
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        // check sr-22
        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);

        // check each driver for sr-22
        int numOfDrivers = driverPage.getDriverTableRowsCount();

        for (int i = 0; i < numOfDrivers; i++) {
            driverPage.clickEditButtonInDriverTable(i);

            driverPage.setSR22Checkbox(true);
            driverPage.setSR22EffectiveDate(new Date());

            if (driverPage.isSR22FilingFeeEditable() || driverPage.isSR22FilingChargeDateEditable()) {
                Assert.fail("The agent should not be able to edit the filing fee or filing charge date on the SR-22 coverage.");
            }

            driverPage.clickOk();

        }

        // save draft
        wo.clickGenericWorkorderSaveDraft();

        // logout and login as UW
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchJob("pdye", "gw", accountNumber);

        // go to drivers
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_ContactDetail driversPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        driversPage.clickEditButtonInDriverTable(2);

        // Verifying sr-22
        driverPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);

        if (driverPage.isSR22CheckboxChecked()) {


            driverPage.setSR22FilingFee(SR22FilingFee.Charged);
            driverPage.setSR22FilingChargeDate(new Date());

            driverPage.clickOk();

            sideMenu.clickSideMenuRiskAnalysis();
            wo.clickGenericWorkorderQuote();
            GenericWorkorderQuote genericWorkorderQuote = new GenericWorkorderQuote(driver);
            Double sr22Charge = genericWorkorderQuote.getQuoteSR22Charge();

            if (sr22Charge != 25) {
                Assert.fail("The SR 22Charge amount is not displayed.");
            }

            wo.clickGenericWorkorderSaveDraft();

        } else {
            Assert.fail("The underwriter should be able to check the SR-22 coverage.");
        }

        // logout and back in as agent
		/*logout();
		loginAndSearchJob(agentUserName, agentPassword, accountNumber);

		// check sr-22
		sideMenu = SideMenuFactory.getMenu();
		sideMenu.clickSideMenuPADrivers();
		driverPage = new GenericWorkorderSquireDrivers();
		driversPage.clickLinkInDriverByName(firstName + " " + lastName);
		if (driverPage.sr22TextField() == "") {
			Assert.fail("The agent should be able to see what the underwriter set the SR-22 coverage to.");*/
    }

}


