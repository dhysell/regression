package regression.r2.noclock.billingcenter.search;
/**
 * Test Search Contacts
 * By: Jessica 
 * 
 * **/

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.summary.BCAccountSummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchContacts;
import repository.bc.search.BCSearchMenu;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class SearchTest extends BaseTest {
	private WebDriver driver;
	BCTopMenu menu;
	private String companyAddress;
	private ARUsers arUser = new ARUsers();
		
	public List<String> companyNameList = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
	{
		this.add("b");
		this.add("c");
		this.add("d");
		this.add("f");
		this.add("g");
		this.add("h");
		this.add("k");
		this.add("m");
		this.add("n");
		this.add("p");
		this.add("w");
	}};
	@Test(enabled=true)
	public void testSearchContacts() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		BCTopMenu menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchAccounts mySchAcct = new BCSearchAccounts(driver);
		String companyName = null,acctNum = null;
		int  n;
		mySchAcct.setBCSearchAccountsCompanyName(StringsUtils.getRandomStringFromList(companyNameList));
		mySchAcct.clickSearch();

		while(companyName==null ||companyName.length()<3){
			n = NumberUtils.generateRandomNumberInt(1, mySchAcct.getSearchTableRowCount());
			acctNum=mySchAcct.getSearchAccountTableAccountNumber(n);
			companyName=mySchAcct.getSearchAccountTableCompanyName(acctNum);
		}
		
		mySchAcct.clickAccountNumber(acctNum);
		BCAccountSummary acctSum = new BCAccountSummary(driver);
		companyAddress=acctSum.getBillingAddress();
		System.out.println("acct num is " + acctNum);
		System.out.println("company address is "+ companyAddress);
		menu.clickSearchTab();
		BCSearchMenu mySchMenu=new BCSearchMenu(driver);
		mySchMenu.clickSearchMenuContacts();
		BCSearchContacts mySchContact = new BCSearchContacts(driver);
		mySchContact.setSearchContactsType(ContactSubType.Company);
		mySchContact.setSearchContactsName(companyName);
		mySchContact.clickSearch();
		if (!mySchContact.contactsTableExist()) {
			Assert.fail("Could not find the Search Contacts results table.\n");
		}
		else {			
			boolean foundOrNot = mySchContact.verifyContactsTable(companyName, companyAddress);			
			if(!foundOrNot){
				Assert.fail("Could not find the correct contacts in Contacts screen.\n");
			}
		}
		
	}
}
