package regression.r2.noclock.contactmanager.changes;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.addresstandardization.AddressStandardizationError;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import regression.r2.noclock.contactmanager.contact.NewPersonTest;

public class PersonAddressChange extends BaseTest {
	private WebDriver driver;
	private GenerateContact myPerson;
	private AbUsers abUser;
	private AddressInfo newAddress = new AddressInfo("1018 Jefferson", "", "Idaho Falls", State.Idaho, "83402",
			CountyIdaho.Bonneville, "United States", AddressType.Mailing);

	@Test
	public void createPerson() throws Exception {
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		NewPersonTest newPerson = new NewPersonTest();
		newPerson.testNewPerson();
		this.myPerson = newPerson.myContactObj;
	}

	@Test(dependsOnMethods = { "createPerson" })
	public void changePersonAddress() throws Exception {
		// TopInfo logMeOut =
        // new TopInfo(driver);
		// logMeOut.clickTopInfoLogout();

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByFirstLastName(myPerson.firstName, myPerson.lastName,
				myPerson.addresses.get(0).getLine1());

        ContactDetailsBasicsAB editAddress = new ContactDetailsBasicsAB(driver);
		editAddress.clickContactDetailsBasicsEditLink();
        editAddress = new ContactDetailsBasicsAB(driver);
		editAddress.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB changeAddress = new ContactDetailsAddressesAB(driver);

		changeAddress.setContactDetailsAddressesAddressLine1(newAddress.getLine1());
		changeAddress.setContactDetailsAddressesAddressLine2(newAddress.getLine2());
		changeAddress.setContactDetailsAddressesCity(newAddress.getCity());
		changeAddress.setContactDetailsAddressesZipCode(newAddress.getZip());

		changeAddress.clickContactDetailsAddressesUpdate();

		AddressStandardizationError addressError = new AddressStandardizationError(driver);
		if (new BasePage(driver).checkIfElementExists("//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]",
				5000)) {
			addressError.clickAddressStandardizationErrorOverrideLink();
		}

        TopInfo logMeOut = new TopInfo(driver);
		logMeOut.clickTopInfoLogout();
	}

	@Test(dependsOnMethods = { "changePersonAddress" })
	public void checkPersonAddress() throws Exception {

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByFirstLastName(myPerson.firstName, myPerson.lastName,
				newAddress.getLine1());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		String address = contactPage.getContactDetailsBasicsContactAddress();
		if (newAddress.getLine2().equals("")) {
			if (!address.equals(newAddress.getLine1() + "\n" + newAddress.getCity() + ", "
					+ newAddress.getState().getAbbreviation() + " " + newAddress.getZip())) {
				Assert.fail("The Address on Contact does not match the intended new address.");
			}
		} else if (!address.equals(newAddress.getLine1() + "\n" + newAddress.getLine2() + "\n" + newAddress.getCity()
				+ ", " + newAddress.getState().getAbbreviation() + " " + newAddress.getZip())) {
			Assert.fail("The Address on Contact does not match the intended new address.");
		}

	}

}
