//Steve Broderick
//Tests US 4895 - Adding a New Entity "Routing" in AB

package regression.r2.noclock.contactmanager.other;


import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRoutingNumbersAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class RoutingNumber extends BaseTest {
	private WebDriver driver;
	private AbUsers user;
	private String bank = "First Bank";
	private String bankAddress = "11 North Wall Street";
	private State bankState = State.Washington;

	@Test
	public void testRoutingNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");

        Login lp = new Login(driver);
		lp.login(user.getUserName(), user.getUserPassword());
		navigateToRoutingNumberPage();

        ContactDetailsRoutingNumbersAB testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);

		testRoutingNumbers.clickContactDetailRoutingNumberEditButton();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);
		List<String> numbers = testRoutingNumbers.getContactDetailsRoutingNumbers();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);

		for (Iterator<String> num = numbers.iterator(); num.hasNext();) {
			String number = num.next();
			System.out.println(number);
		}
		testRoutingNumbers.clickContactDetailRoutingNumberCancelButton();
		testRoutingNumbers.retireRoutingNumber(numbers.get(numbers.size() - 1));
		navigateToRoutingNumberPage();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);
		testRoutingNumbers.unretireRoutingNumber(numbers.get(numbers.size() - 1));
		navigateToRoutingNumberPage();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);
		testRoutingNumbers.addRoutingNumber("124000054");
		navigateToRoutingNumberPage();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);
		testRoutingNumbers.removeRoutingNumber("124000054");
		navigateToRoutingNumberPage();
        testRoutingNumbers = new ContactDetailsRoutingNumbersAB(driver);
	}

    public void navigateToRoutingNumberPage() {

        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();

        AdvancedSearchAB bankSearch = new AdvancedSearchAB(driver);
		bankSearch.searchCompanyByName(bank, bankAddress, bankState);

        ContactDetailsBasicsAB getToRoutingNumbers = new ContactDetailsBasicsAB(driver);
		getToRoutingNumbers.clickContactDetailsBasicsRoutingLink();

	}

}
