package scratchpad.steve.utilities;


import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class DeleteContact extends BaseTest {

    private String name = "Test Company";
    private String addressLine1 = "261 Collins St";
    private int loopTimes = 30;
    private String firstName = "Stor";
    private String middleName = "Broderman";
    private String lastName = "Andan";
    private WebDriver driver;

    //	@Test(enabled = false)
    public void deleteCompanyContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Service");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchCompanyByName(name, addressLine1, State.Idaho);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsDeleteLink();

        for (int i = 0; i < loopTimes; i++) {
            searchMe.clickAdvancedSearchCompanySearchResults(name, addressLine1, State.Idaho);
            contactPage = new ContactDetailsBasicsAB(driver);
            contactPage.clickContactDetailsBasicsDeleteLink();
        }
    }

    @Test(enabled = true)
    public void deletePerson() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Service");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByFirstNameLastNameAnyAddress(firstName, lastName);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsDeleteLink();

        for (int i = 0; i < loopTimes; i++) {
            searchMe.searchByFirstNameLastNameAnyAddress(firstName, lastName);
            contactPage = new ContactDetailsBasicsAB(driver);
            contactPage.clickContactDetailsBasicsDeleteLink();
        }
    }
}
