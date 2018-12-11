package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.StateInfo.Un_UnderInsuredMotoristLimit;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.Addresses;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.entities.TeritoryCodes;
import persistence.globaldatarepo.helpers.AddressHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import persistence.globaldatarepo.helpers.TeritoryCodesHelper;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description Verify that zip codes are associated to the correct Teritory
 * Codes. with get 10 random codes from database table
 * @DATE Dec 2, 2015
 */
public class VerifyTerritoryCode extends BaseTest {

    public GeneratePolicy myPolicy = null;

    List<String> failedZipCodes = new ArrayList<String>();
    List<Addresses> addresses = new ArrayList<Addresses>();
    List<Addresses> filteredAddresses = new ArrayList<Addresses>();

    List<Contacts> allContacts = new ArrayList<Contacts>();
    List<Contacts> filteredcontacts = new ArrayList<Contacts>();

    List<TeritoryCodes> codes = new ArrayList<TeritoryCodes>();

    List<Addresses> addressesTemp = new ArrayList<Addresses>();
    SoftAssert softAssert = new SoftAssert();

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void generateQQ() throws Exception {

        addresses = AddressHelper.getAddresses();
        allContacts = ContactsHelpers.getContactList();
        for (int i = 0; i < 5; i++) {
            codes.add(TeritoryCodesHelper.getRandomAddress());
        }

        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{// LOCATION BULDING
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        // COMMERCIAL AUTO LINE
        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine());
            this.setVehicleList(new ArrayList<Vehicle>() {{
                this.add(new Vehicle() {{
                    this.setVehicleType(VehicleType.PrivatePassenger);
                    this.setMake("VW");
                }});
            }});
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo(true, Un_UnderInsuredMotoristLimit.FiftyThousand50K, true, Un_UnderInsuredMotoristLimit.FiftyThousand50K));
            this.setDriversList(new ArrayList<Contact>() {{
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                }});
            }});
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("TerritoryCode")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }// END of test


    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generateQQ"}, enabled = true)
    public void testTerritoryCodes() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        for (TeritoryCodes code : codes) {
            //if it can't find an address with that Zip skip it.
            try {
                System.out.println("trying : " + code.getZip());
                addressesTemp.add(AddressHelper.getRandomAddressByZip(code.getZip()));
            } catch (Exception e) {
            }
        }


        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            for (Addresses tempAddress : addressesTemp) {
                this.add(new PolicyLocation(new AddressInfo(tempAddress.getAddress(), null, tempAddress.getCity(), State.Idaho, tempAddress.getZip().substring(0, 5), CountyIdaho.Bannock, null, AddressType.Billing), false) {{
                    // LOCATION BULDING
                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                        this.add(new PolicyLocationBuilding() {{
                        }}); // END BUILDING
                    }}); // END BUILDING LIST
                }});// END POLICY LOCATION
            } // END LOCATIONS LIST LOOP
        }}; // END LOCATION LIST

        new Login(driver).loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations cppLocation = new GenericWorkorderLocations(driver);
        for (PolicyLocation location : locationsLists) {
            cppLocation = new GenericWorkorderLocations(driver);
            cppLocation.clickLocationsNewLocation();
            // locations address - new
            cppLocation = new GenericWorkorderLocations(driver);
            cppLocation.setLocationsLocationAddress("New...");
            // fil out address line 1 city zip
            cppLocation.setLocationsAddressLine1(location.getAddress().getLine1());
            cppLocation.setLocationsCity(location.getAddress().getCity());
            cppLocation.setLocationsZipCode(location.getAddress().getZip());
            cppLocation.setLocationsCounty(location.getAddress().getCounty());
            cppLocation.setNonSpecificAddress(location.isNonSpecificLocation());
            if (!location.isNonSpecificLocation()) {
                cppLocation.clickWhenClickable(cppLocation.find(By.xpath("//span[contains(@id, ':StandardizeAddress-btnEl')]")));
                guidewireHelpers.overrideAddressStandardization();
                guidewireHelpers.verifyAddress(location.getAddress());
            }
            cppLocation.clickLocationsOk();
        }//END FOR

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();

        GenericWorkorderVehicles_Details vehicles = new GenericWorkorderVehicles_Details(driver);
        vehicles.createVehicleManually();

        for (Addresses location : addressesTemp) {
        	vehicles.selectGaragedAtZip(location.getZip());
            softAssert.assertTrue(String.valueOf(location.getTerritoryCode()).equals(vehicles.getTerritoryCode()), location.getZip() + " SET TO: " + vehicles.getTerritoryCode() + " NOT: " + location.getTerritoryCode());
        }

        softAssert.assertAll();
    }


    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generateQQ"})
    public void verifyTerritoryCodeUpdates() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        softAssert = new SoftAssert();

        new Login(driver).loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        PolicyLocation location = new PolicyLocation(new AddressInfo(true), new ArrayList<PolicyLocationBuilding>() {{
            this.add(new PolicyLocationBuilding());
        }});

        GenericWorkorderLocations cppLocation = new GenericWorkorderLocations(driver);
        cppLocation = new GenericWorkorderLocations(driver);
        cppLocation.clickLocationsNewLocation();
        // locations address - new
        cppLocation = new GenericWorkorderLocations(driver);
        cppLocation.setLocationsLocationAddress("New...");
        // fil out address line 1 city zip
        cppLocation.setLocationsAddressLine1(location.getAddress().getLine1());
        cppLocation.setLocationsCity(location.getAddress().getCity());
        cppLocation.setLocationsZipCode(location.getAddress().getZip());
        cppLocation.setLocationsCounty(location.getAddress().getCounty());
        cppLocation.setNonSpecificAddress(location.isNonSpecificLocation());
        if (!location.isNonSpecificLocation()) {
            cppLocation.find(By.xpath("//span[contains(@id, ':StandardizeAddress-btnEl')]")).click();
            if (!cppLocation.finds(By.xpath("//span[contains(text(), 'Override')]")).isEmpty()) {
                cppLocation.find(By.xpath("//span[contains(text(), 'Override')]")).click();
            }
            guidewireHelpers.verifyAddress(location.getAddress());
        }
        cppLocation.clickLocationsOk();

        String previousTerritoryCode = TeritoryCodesHelper.getAddressByZip(location.getAddress().getZip().substring(0, 5)).getCode();

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();

        GenericWorkorderVehicles_Details vehiclesPage = new GenericWorkorderVehicles_Details(driver);
        vehiclesPage.editVehicleByVin(myPolicy.commercialAutoCPP.getVehicleList().get(0).getVin());
        vehiclesPage.selectGaragedAt(location.getAddress());
        vehiclesPage.clickOK();

        //new location

        sideMenu.clickSideMenuLocations();
        cppLocation.editLocationByAddress(location.getAddress().getZip());
        //		TeritoryCodesHelper.getAddressByCode(previousTerritoryCode, false).getZip();
        //get new address
        PolicyLocation newLocation = null;
        try {
            Addresses myAddress = AddressHelper.getRandomAddressByZip(TeritoryCodesHelper.getAddressByCode(previousTerritoryCode, false).getZip());

            newLocation = new PolicyLocation() {{
                this.setAddress(new AddressInfo() {{
                    this.setLine1(myAddress.getAddress());
                    this.setCity(myAddress.getCity());
                    this.setZip(myAddress.getZip());
                }});
            }};
        } catch (Exception e) {
            System.out.println("No Address for ZIP: " + TeritoryCodesHelper.getAddressByCode(previousTerritoryCode, false).getZip());
            Assert.fail("Test was skipped due to no address for ZIP in Addresses Database Table. Please Add One. Thank You");
        }

        cppLocation.setLocationsLocationAddress("New...");
        // fil out address line 1 city zip
        cppLocation.setLocationsAddressLine1(newLocation.getAddress().getLine1());
        cppLocation.setLocationsCity(newLocation.getAddress().getCity());
        cppLocation.setLocationsZipCode(newLocation.getAddress().getZip());
        cppLocation.setLocationsCounty(newLocation.getAddress().getCounty());
        cppLocation.locationStandardizeAddress(newLocation);
        cppLocation.clickLocationsOk();
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();
        vehiclesPage.editVehicleByVin(myPolicy.commercialAutoCPP.getVehicleList().get(0).getVin());


        if (previousTerritoryCode.equals(vehiclesPage.getTerritoryCode())) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "Territory code failed to update when location address was changed.");
        }
    }


}
















