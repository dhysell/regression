package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.ZipCodesIdaho;
import persistence.globaldatarepo.helpers.ContactsHelpers;


/*
 * Jon larsen 6/25/2015
 * used to screen scrape Contact Manager for Persons/Companies to be
 * used for for Additional Insureds/Interests etc.
 */

@SuppressWarnings({"unused", "serial"})
public class GetAddressBookContacts extends BaseTest {

    private List<WebElement> contacts = new ArrayList<WebElement>();
    private List<String> prefix = new ArrayList<String>() {{
        this.add("ab");
        this.add("bc");
        this.add("cd");
        this.add("ef");
        this.add("gh");
        this.add("ij");
        this.add("jk");
        this.add("lm");
        this.add("mn");
        this.add("op");
        this.add("rs");
        this.add("uv");
        this.add("vw");
        this.add("mills");
        this.add("jones j");
        this.add("bills");
        this.add("willy");
        this.add("wells");
        this.add("arms");
        this.add("ireland");
        this.add("key");
        this.add("america");
        this.add("Kelley");
        this.add("bank of a");
        this.add("bank of b");
        this.add("bank of c");
    }};

    private List<ZipCodesIdaho> idahoZipCodes = new ArrayList<ZipCodesIdaho>();
    private List<String> failedZips = new ArrayList<String>();
    private boolean failedAddresses = false;
    private boolean addedAtLeastOne = false;

    private String name = "";
    private String addressLine1 = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private Boolean isCompany = true;
    private String roles = "";
    private String lienNumber = "";
    private String phone = "";
    private WebDriver driver;

    @Test
    public void getContacts() throws Exception {

        idahoZipCodes = ZipCodesIdaho.getALLZipCodes();

        Config cf = new Config(ApplicationOrCenter.ContactManager, "UAT");
        driver = buildDriver(cf);


        Login login = new Login(driver);
        login.login("su", "gw");

        TopMenuAB topMenu = new TopMenuAB(driver);
        topMenu.clickSearchTab();

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
        for (String myString : prefix) {
//		for(ZipCodesIdaho zipcode : idahoZipCodes) {
            addedAtLeastOne = false;
            search.clickReset();
            search.setCompanyName(myString);
//			search.selectSpecificComboBox_State(State.Idaho);
            //			search.setCity(zipcode.getCityList().get(0));
            //			search.setPostalCode(String.valueOf(zipcode.getZip()));
            search.clickSearch();
            //			if(!finds(By.xpath("//div[contains(text(), 'More than 300 results found')]")).isEmpty()) {
//				search.setCompanyName("ab");
//				//				search.clickSearch();
//				//				if(!finds(By.xpath("//div[contains(text(), 'More than 300 results found')]")).isEmpty()) {
//					systemOut("\n////////////////\nTOO MANY RESULTS FOR " + prefix + "\n\n/////////////////////");
////					failedZips.add(String.valueOf(zipcode.getZip()));
////					failedAddresses = true;
//					continue;
//				}
//			}
            List<WebElement> results = search.finds(By.xpath("//div[contains(@id, 'ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV-body')]/div/table/tbody/child::tr"));
            if (!results.isEmpty()) {
                for (WebElement element : results) {
                    name = element.findElement(By.xpath(".//child::td[4]")).getText().replace("\\n", " ");
                    if (name.length() < 25) {
                        addressLine1 = element.findElement(By.xpath(".//child::td[5]")).getText().replace("\\n", " ");
                        city = element.findElement(By.xpath(".//child::td[6]")).getText().replace("\\n", " ");
                        state = element.findElement(By.xpath(".//child::td[7]")).getText().replace("\\n", " ");
                        zip = element.findElement(By.xpath(".//child::td[8]")).getText().replace("\\n", " ");
                        phone = element.findElement(By.xpath(".//child::td[9]")).getText().replace("\\n", " ");

                        isCompany = element.findElement(By.xpath(".//child::td[10]")).getText().equalsIgnoreCase("C");
                        roles = element.findElement(By.xpath(".//child::td[11]")).getText().replace("\\n", " ");
                        lienNumber = element.findElement(By.xpath(".//child::td[2]")).getText().replace("\\n", " ");

                        if (!state.equals("Idaho")) {
                            continue;
                        }

                        if (addressLine1.contains("PO Box") || addressLine1.contains("LOT") || addressLine1.contains("RR")) {
                            continue;
                        }

                        System.out.println(name);
                        System.out.println(addressLine1);
                        System.out.println(city);
                        System.out.println(state);
                        System.out.println(zip);
                        System.out.println(roles);
                        System.out.println(lienNumber);
                        System.out.println(isCompany.toString());
                        System.out.println(phone);

//						int code = Integer.valueOf(TeritoryCodesHelper.getAddressByZip(String.valueOf(zipcode.getZip())).getCode());

//						AddressHelper.createNewAddress(new Addresses(addressLine1, city, state,	zip.substring(0, 5), null, zipcode.getCounty(), code));
                        ContactsHelpers.createNewContact(name, addressLine1, city, state, zip, lienNumber, isCompany, roles, phone);
                        addedAtLeastOne = true;
                    }

                    resetFields();
                }

            } else {
//				failedZips.add(String.valueOf(zipcode.getZip()));
//				failedAddresses = true;
            }
            if (!addedAtLeastOne) {
//				failedZips.add(String.valueOf(zipcode.getZip()));
            }
        }
        if (failedAddresses) {
//			systemOut("Failed Zips");
//			for(String zip : failedZips) {
//				systemOut(zip);
//			}
        }
    }


    private void resetFields() {
        name = "";
        addressLine1 = "";
        city = "";
        state = "";
        zip = "";
        isCompany = true;
        roles = "";
        lienNumber = "";
    }


}

























