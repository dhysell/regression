package regression.r2.noclock.contactmanager.changes;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.addresstandardization.AddressStandardizationError;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.generate.GenerateContact;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import regression.r2.noclock.contactmanager.contact.NewCompanyTest;

public class CompanyNameChange extends BaseTest {
	private WebDriver driver;
	private GenerateContact myCompany = null;
	private AbUsers abUser;

	@Test
	public void changeCompanyName() throws Exception {
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		System.out.println("made it to changeCompany");
		NewCompanyTest newCompany = new NewCompanyTest();
		newCompany.testNewCompany();
		this.myCompany = newCompany.myContactObj;

        TopInfo logMeOut = new TopInfo(driver);
		logMeOut.clickTopInfoLogout();

        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchCompanyByName(this.myCompany.companyName, this.myCompany.addresses.get(0).getLine1(), this.myCompany.addresses.get(0).getState());

        ContactDetailsBasicsAB changeName = new ContactDetailsBasicsAB(driver);
		changeName.clickContactDetailsBasicsEditLink();
        changeName = new ContactDetailsBasicsAB(driver);
		changeName.setContactDetailsBasicsName(myCompany.companyName.substring(5));
		changeName.clickContactDetailsBasicsUpdateLink();

		AddressStandardizationError addressError = new AddressStandardizationError(driver);
		if (new BasePage(driver).checkIfElementExists("//*[contains(@id,'AddressStandardizationPopup:LocationScreen:Update-btnEl')]", 5)) {
			addressError.clickAddressStandardizationErrorOverrideLink();
		}

        logMeOut = new TopInfo(driver);
		logMeOut.clickTopInfoLogout();
	}

	@Test(dependsOnMethods = { "changeCompanyName" })
    public void checkCompanyName() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(this.abUser.getUserName(), this.abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchCompanyByName(myCompany.companyName.substring(5), myCompany.addresses.get(0).getLine1(), myCompany.addresses.get(0).getState());
	}

}
