package regression.r2.noclock.policycenter.submission_auto.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4249: IllegalStateException on Vehicle for removing prefill vehicles and adding them back
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 5, 2016
 */
@QuarantineClass
public class TestSquireAutoPrefillAddRemoveVehicle extends BaseTest {

    private GeneratePolicy mySqObj = null;

    private WebDriver driver;

    @Test()
    public void testCreateSquireAutoFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        mySqObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withLexisNexisData(true, true, false, false, true, true, false)
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testCreateSquireAutoFA"})
    private void testAddRemoveVehicles() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySqObj.accountNumber);

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.removeAllVehicles();

        int rowCount = vehiclePage.addFromPrefillVehicles();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        sideMenu.clickSideMenuPAVehicles();

        if (rowCount > 0) {
            vehiclePage.removeVehicleByRowNumber(1);
            vehiclePage.addFromPrefillVehicles();

            for (int currentVehicle = 1; currentVehicle <= rowCount; currentVehicle++) {
                vehiclePage.editVehicleByRowNumber(currentVehicle);
                vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
                vehiclePage.selectGaragedAt(mySqObj.squire.squirePA.getVehicleList().get(0).getGaragedAt());
                if (vehiclePage.getModel().isEmpty()) {
                    vehiclePage.setModel("Honda");
                }
                if (currentVehicle == 1) {
                    vehiclePage.selectDriverToAssign(this.mySqObj.squire.squirePA.getDriversList().get(0));
                } else {
                    vehiclePage.setNoDriverAssigned(true);
                }
                vehiclePage.clickOK();
            }

        }
        vehiclePage.clickNext();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();

        new GuidewireHelpers(driver).logout();
    }
}
