package scratchpad.steve;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.search.SolrContactSearch;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.NickNames;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class SolrSearch extends BaseTest {
	private WebDriver driver;
	String contactFirstName = null;
	String contactLastName = "Williams";
	String searchCriteria = null;
	AbUsers user = null;
	GenerateContact myContactObj = null;
	
	public void generateContact() throws Exception {
		this.contactFirstName = NickNames.getRandomName().getGivenName();
		this.user = AbUserHelper.getRandomUserByTitle("Data Analyst");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		
		this.myContactObj = new GenerateContact.Builder(driver)
				.withFirstLastName(this.contactFirstName, this.contactLastName)
				.withUniqueName(true)
				.withPrimaryAddress(new AddressInfo(true))
				.withGenerateAccountNumber(true)
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test
	public void testSolr() throws Exception {
		generateContact();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		Login login = new Login(driver);
		login.login("su", "gw");
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.clickSolrSearch();
        SolrContactSearch solr = new SolrContactSearch(driver);
        assertTrue(solr.searchName(searchCriteria, contactFirstName, contactLastName));
	}

}
