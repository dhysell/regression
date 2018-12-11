package scratchpad.jon.mine;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.ContactsHelpers;

@SuppressWarnings({"unused", "serial"})
public class GetLienHolders extends BaseTest {

    private List<WebElement> contacts = new ArrayList<WebElement>();
    private List<String> prefix = new ArrayList<String>() {{
        this.add("Wells Fargo");
        this.add("Mountain America Credit Union");
        this.add("Ireland Bank");

    }};

    private String name = "";
    private String addressLine1 = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private Boolean isCompany = true;
    private String roles = "";
    private String lienNumber = "";


    List<String> lhAcountumbers = new ArrayList<String>();
    List<Contacts> contactList = new ArrayList<Contacts>();


    private WebDriver driver;


    @Test
    public void getLienHolders() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);


        Login login = new Login(driver);
        login.login("sbrunson", "gw");

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickSearchTab();

        BCSearchAccounts searchAccounts = new BCSearchAccounts(driver);
        searchAccounts.setBCSearchAccountsAccountNumber("98");
        searchAccounts.clickSearch();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        TableUtils tableUtils = new TableUtils(driver);
        do {
            List<WebElement> pageList = searchAccounts.table_BCSearchAccountsSearchResultsTable.findElements(By.xpath(".//tbody/child::tr/td[3]/div/a"));

            for (WebElement foo : pageList) {
                lhAcountumbers.add(foo.getText());
            }
            tableUtils.setTablePageNumber(searchAccounts.table_BCSearchAccountsSearchResultsTable, tableUtils.getCurrentTablePageNumber(searchAccounts.table_BCSearchAccountsSearchResultsTable) + 1);
            guidewireHelpers.clickProductLogo();
        }
        while (tableUtils.getCurrentTablePageNumber(searchAccounts.table_BCSearchAccountsSearchResultsTable) < tableUtils.getNumberOfTablePages(searchAccounts.table_BCSearchAccountsSearchResultsTable));


        for (String boo : lhAcountumbers) {
            topMenu.clickSearchTab();

            searchAccounts.setBCSearchAccountsAccountNumber(boo);
            searchAccounts.clickSearch();
            Contacts myContact = new Contacts();
            myContact.setContactName(searchAccounts.find(By.xpath("//div[contains(@id, ':AccountName-inputEl')]")).getText());
            AddressInfo myAddress = new AddressInfo();
            myAddress.setLine1(searchAccounts.find(By.xpath("//div[contains(@id, ':AddressLineOne-inputEl')]")).getText());
            String cityStateZip = searchAccounts.find(By.xpath("//div[contains(@id, ':AddressLineStateCityZip-inputEl')]")).getText();
            myAddress.setCity(cityStateZip.substring(0, cityStateZip.indexOf(",")));
            myAddress.setState(State.valueOfAbbreviation(cityStateZip.substring(cityStateZip.indexOf(",") + 2, cityStateZip.indexOf(",") + 4)));
            myAddress.setPostalCode(cityStateZip.substring(cityStateZip.indexOf(",") + 5));
            myContact.setContactAddressLine1(myAddress.getLine1());
            myContact.setContactCity(myAddress.getCity());
            myContact.setContactState(myAddress.getState().getName());
            myContact.setContactZip(myAddress.getPostalCode());
            myContact.setContactIsCompany(true);
            myContact.setContactRoles("Lienholder");
            myContact.setContactPhone(null);
            myContact.setContactNumber(boo);

            contactList.add(myContact);
        }

        ContactsHelpers.createnewContactFromList(contactList);


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
