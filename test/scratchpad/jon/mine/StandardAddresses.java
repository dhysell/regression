package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.addressstandardization.AddressStandardization;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import persistence.globaldatarepo.entities.Addresses;
import persistence.globaldatarepo.helpers.AddressHelper;

public class StandardAddresses extends BaseTest {

    List<Addresses> addressList = new ArrayList<Addresses>();
    ArrayList<persistence.globaldatarepo.entities.StandardAddresses> standardizedAddresses = new ArrayList<persistence.globaldatarepo.entities.StandardAddresses>();


    private WebDriver driver;

    @Test
    public void verifyAddressStandarized() throws Exception {


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        addressList = AddressHelper.getAddresses();
        //get addresses

        new Login(driver).loginAndSearchJob("hhill", "gw", "245445");

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
        locations.clickLocationsNewLocation();

        locations.setLocationsLocationAddress("New...");

        for (int i = 0; i <= 75; i++) {
            final int j = i;
            locations.setLocationsAddressLine1(addressList.get(i).getAddress());
            locations.setLocationsCity(addressList.get(i).getCity());
            locations.setLocationsZipCode(addressList.get(i).getZip());

            locations.clickLocationsStandardizeAddress();
            AddressStandardization stdAddress = new AddressStandardization(driver);
            if (!stdAddress.isStandardAddress()) {
                locations.clickLocationsCancel();
                //save address with standard address false
                standardizedAddresses.add(new persistence.globaldatarepo.entities.StandardAddresses() {{
                    this.setAddress(addressList.get(j).getAddress());
                    this.setCity(addressList.get(j).getCity());
                    this.setCounty(addressList.get(j).getCounty());
                    this.setStandardizable(false);
                    this.setState(addressList.get(j).getState());
                    this.setZip(addressList.get(j).getZip());
                    this.setZip4(addressList.get(j).getZip4());
                }});
            } else if (stdAddress.finds(By.xpath("//table[contains(@class, 'cb-checked') and contains(@id, 'PolicyLocation_FBMInputSet:Verified')]")).size() > 0) {
                //save address with standard address true
                standardizedAddresses.add(new persistence.globaldatarepo.entities.StandardAddresses() {{
                    this.setAddress(addressList.get(j).getAddress());
                    this.setCity(addressList.get(j).getCity());
                    this.setCounty(addressList.get(j).getCounty());
                    this.setStandardizable(true);
                    this.setState(addressList.get(j).getState());
                    this.setZip(addressList.get(j).getZip());
                    this.setZip4(addressList.get(j).getZip4());
                }});
            } else {
                //save address as null
                standardizedAddresses.add(new persistence.globaldatarepo.entities.StandardAddresses() {{
                    this.setAddress(addressList.get(j).getAddress());
                    this.setCity(addressList.get(j).getCity());
                    this.setCounty(addressList.get(j).getCounty());
                    this.setStandardizable(false);
                    this.setState(addressList.get(j).getState());
                    this.setZip(addressList.get(j).getZip());
                    this.setZip4(addressList.get(j).getZip4());
                }});
            }


        }

        for (persistence.globaldatarepo.entities.StandardAddresses sAddress : standardizedAddresses) {
            persistence.globaldatarepo.helpers.StandardAddressesHelper.createStandardAddress(sAddress);
        }


    }


}





















