package regression.r2.noclock.policycenter.submission_auto.subgroup5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
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
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Agents;

public class TestSquireAutoLocalService1TonVehicleSymbols extends BaseTest {

    private GeneratePolicy plPolicyObj;

    private WebDriver driver;

    /**
     * @throws Exception
     * @Author iclouser
     * @Requirement DE3316 PL - Local Service 1 Ton Vehicle symbol
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/51504068524">Rally Story</a>
     * @Description
     * @DATE Mar 10, 2016
     */
    @Test
    public void validateSymbolCodesAutoPL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        plPolicyObj = generatePLAuto(GeneratePolicyType.FullApp);
        Agents agent = plPolicyObj.agentInfo;

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), plPolicyObj.accountNumber);

        // Make sure comp coverage is added to the vehicle
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicleDetails = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        new GuidewireHelpers(driver).editPolicyTransaction();
        vehicleDetails.clickLinkInVehicleTable("Edit");
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);

        // Get back to the details page
        vehicleDetails.clickVehicleDetailsTab();

        // Initialize the map with the costs and their corresponding symbols.
        Map<String, String> costMap = setUpPriceAndSymbolMap();

        // Validate all the different costs generate the correct codes in the
        // UI.
        for (String cost : costMap.keySet()) {
            vehicleDetails.setFactoryCostNew(Double.parseDouble(cost));
            // Trigger postback
            vehicleDetails.sendArbitraryKeys(Keys.TAB);

            // Find the Element the symbol code that was generated for the
            // number inputed.

            String symbol = vehicleDetails.find(By.xpath("//div[contains(@id,'VehicleFBSymbol-inputEl')]")).getText();

            Assert.assertTrue(costMap.get(cost).equals(symbol), "Expected the symbol for the amount: " + cost + " to be: " + costMap.get(cost) + " but got: " + symbol);
        }

    }

    private GeneratePolicy generatePLAuto(GeneratePolicyType pType) throws Exception {

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(VehicleTypePL.LocalService1Ton);
        toAdd.setVin("make new vin");
        toAdd.setEmergencyRoadside(true);
        toAdd.setAdditionalLivingExpense(true);
        toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        return plPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Bob", "Saget")
                .build(pType);

    }

    private Map<String, String> setUpPriceAndSymbolMap() {
        Map<String, String> factoryCostAndSymbol = new HashMap<String, String>();
        factoryCostAndSymbol.put("0", "F");
        factoryCostAndSymbol.put("2999", "F");
        factoryCostAndSymbol.put("3200", "F");

        factoryCostAndSymbol.put("3201", "G");
        factoryCostAndSymbol.put("3599", "G");
        factoryCostAndSymbol.put("3600", "G");

        factoryCostAndSymbol.put("3601", "H");
        factoryCostAndSymbol.put("3999", "H");
        factoryCostAndSymbol.put("4000", "H");

        factoryCostAndSymbol.put("4001", "I");
        factoryCostAndSymbol.put("4499", "I");
        factoryCostAndSymbol.put("4500", "I");

        factoryCostAndSymbol.put("4501", "J");
        factoryCostAndSymbol.put("5449", "J");
        factoryCostAndSymbol.put("5500", "J");

        factoryCostAndSymbol.put("5501", "K");
        factoryCostAndSymbol.put("6449", "K");
        factoryCostAndSymbol.put("6500", "K");

        factoryCostAndSymbol.put("6501", "L");
        factoryCostAndSymbol.put("7449", "L");
        factoryCostAndSymbol.put("7500", "L");

        factoryCostAndSymbol.put("7501", "M");
        factoryCostAndSymbol.put("8449", "M");
        factoryCostAndSymbol.put("8500", "M");

        factoryCostAndSymbol.put("8501", "N");
        factoryCostAndSymbol.put("9449", "N");
        factoryCostAndSymbol.put("9500", "N");

        factoryCostAndSymbol.put("9501", "O");
        factoryCostAndSymbol.put("9999", "O");
        factoryCostAndSymbol.put("10000", "O");

        factoryCostAndSymbol.put("10001", "P");
        factoryCostAndSymbol.put("14999", "P");
        factoryCostAndSymbol.put("15000", "P");

        factoryCostAndSymbol.put("15001", "Q");
        factoryCostAndSymbol.put("19999", "Q");
        factoryCostAndSymbol.put("20000", "Q");

        factoryCostAndSymbol.put("20001", "R");
        factoryCostAndSymbol.put("24999", "R");
        factoryCostAndSymbol.put("25000", "R");

        factoryCostAndSymbol.put("25001", "S");
        factoryCostAndSymbol.put("29999", "S");
        factoryCostAndSymbol.put("30000", "S");

        factoryCostAndSymbol.put("30001", "T");
        factoryCostAndSymbol.put("39999", "T");
        factoryCostAndSymbol.put("40000", "T");

        factoryCostAndSymbol.put("40001", "U");
        factoryCostAndSymbol.put("49999", "U");
        factoryCostAndSymbol.put("50000", "U");

        factoryCostAndSymbol.put("50001", "V");
        factoryCostAndSymbol.put("59999", "V");
        factoryCostAndSymbol.put("60000", "V");

        factoryCostAndSymbol.put("60001", "W");
        factoryCostAndSymbol.put("69999", "W");
        factoryCostAndSymbol.put("70000", "W");

        factoryCostAndSymbol.put("70001", "X");
        factoryCostAndSymbol.put("79999", "X");
        factoryCostAndSymbol.put("80000", "X");

        factoryCostAndSymbol.put("80001", "Y");
        factoryCostAndSymbol.put("89999", "Y");
        factoryCostAndSymbol.put("90000", "Y");

        factoryCostAndSymbol.put("90001", "Z");
        factoryCostAndSymbol.put("99999", "Z");
        factoryCostAndSymbol.put("100000", "Z");

        factoryCostAndSymbol.put("100001", "2");
        factoryCostAndSymbol.put("109999", "2");
        factoryCostAndSymbol.put("110000", "2");

        factoryCostAndSymbol.put("110001", "3");
        factoryCostAndSymbol.put("119999", "3");
        factoryCostAndSymbol.put("120000", "3");

        factoryCostAndSymbol.put("120001", "4");
        factoryCostAndSymbol.put("129999", "4");
        factoryCostAndSymbol.put("130000", "4");

        factoryCostAndSymbol.put("130001", "5");
        factoryCostAndSymbol.put("139999", "5");
        factoryCostAndSymbol.put("140000", "5");

        factoryCostAndSymbol.put("140001", "6");
        factoryCostAndSymbol.put("149999", "6");
        factoryCostAndSymbol.put("150000", "6");

        factoryCostAndSymbol.put("150001", "7");
        factoryCostAndSymbol.put("159999", "7");
        factoryCostAndSymbol.put("160000", "7");

        factoryCostAndSymbol.put("160001", "8");
        factoryCostAndSymbol.put("169999", "8");
        factoryCostAndSymbol.put("170000", "8");

        factoryCostAndSymbol.put("180000", "8");
        factoryCostAndSymbol.put("190000", "8");
        factoryCostAndSymbol.put("200000", "8");
        factoryCostAndSymbol.put("210000", "8");
        factoryCostAndSymbol.put("220000", "8");
        factoryCostAndSymbol.put("230000", "8");
        factoryCostAndSymbol.put("240000", "8");
        factoryCostAndSymbol.put("250000", "8");
        factoryCostAndSymbol.put("260000", "8");
        factoryCostAndSymbol.put("270000", "8");
        factoryCostAndSymbol.put("280000", "8");
        factoryCostAndSymbol.put("290000", "8");
        factoryCostAndSymbol.put("300000", "8");
        factoryCostAndSymbol.put("310000", "8");
        factoryCostAndSymbol.put("320000", "8");
        factoryCostAndSymbol.put("330000", "8");
        factoryCostAndSymbol.put("340000", "8");

        return factoryCostAndSymbol;

    }

}
