package regression.r2.noclock.contactmanager.address;


import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ReturnMailPopupAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.ReturnMailReason;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class ReturnedMail extends BaseTest {
	private WebDriver driver;
    private GenerateContact newContact;
    private AbUsers numbering;

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement Returned Mail.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537d/detail/userstory/57154124035">Returned Mail</a>
     * @Description Test Returned Mail.
     * @DATE June 14, 2016
     */

//    @Test
    public void testReturnedMail() throws Exception {
        this.numbering = AbUserHelper.getRandomDeptUser("Policy Services");
//		Configuration.overrideServerToRun("DEV");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        this.newContact = new GenerateContact.Builder(driver)
                .withFirstLastName("Bat", "Man")
                .withMembershipType(ContactMembershipType.Associate)
                .withCreator(numbering)
                .build(GenerateContactType.Person);

        returnedMail(newContact.addresses.get(0));

    }

    public void returnedMail(AddressInfo address) throws GuidewireNavigationException {
        ContactDetailsBasicsAB contact = new ContactDetailsBasicsAB(driver);
        contact.clickContactDetailsBasicsEditLink();
        contact.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        addressPage.getToReturnMail(address);

        ReturnMailPopupAB returnMail = new ReturnMailPopupAB(driver);
        returnMail.clickMarkReturnMail(ReturnMailReason.Moved);
        addressPage = new ContactDetailsAddressesAB(driver);
        addressPage.clickContactDetailsAddressesUpdate();
        new GuidewireHelpers(driver).logout();
    }

    @Test
    public void testReturnedMailClaims() throws Exception {
        searchByRole("Vendor");
        returnedMail(null);
        searchByRole("Claim Party");
        returnedMail(null);
    }

    public void searchByRole(String role) throws Exception {
//		Configuration.overrideServerToRun("DEV");
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        searchContactRole(role, "abc");
        SearchAB searchMe = new SearchAB(driver);
        searchMe.clickSearch();
        AdvancedSearchAB advancedSearchPage = new AdvancedSearchAB(driver);
        advancedSearchPage.clickRandomSearchResult();
    }

    public int[] getRandomNumbers(int max) {
        int[] randomNumbers = {(int) Math.random() * max + 1, (int) Math.random() * max + 1, (int) Math.random() * max + 1, (int) Math.random() * max + 1, (int) Math.random() * max + 1};
        return randomNumbers;
    }

    public void searchContactRole(String role, String searchName) throws Exception {
        this.numbering = AbUserHelper.getRandomDeptUser("Claims");
        this.numbering = AbUserHelper.getUserByUserName("abatts");
        Login lp = new Login(driver);
        lp.login(this.numbering.getUserName(), this.numbering.getUserPassword());
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        AdvancedSearchAB roleSearch = new AdvancedSearchAB(driver);
        if (!role.equals("Agency")) {
            roleSearch.setCompanyName(searchName);
        }
        roleSearch.setRoles(role);
    }
}


