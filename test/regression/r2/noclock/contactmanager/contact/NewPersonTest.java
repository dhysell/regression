// Steve Broderick
package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.DBA;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
public class NewPersonTest extends BaseTest {
	private WebDriver driver;
	private AbUsers user;
	private String firstName = "Stor";
	private String middleName = "Adran";
	private String lastName = "Broderman";
	private DBA newDBA = new DBA("theScripter");
	// private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
	public GenerateContact myContactObj = null;
	private ArrayList<DBA> myDBAs = new ArrayList<DBA>();
	private AddressInfo myAddress = new AddressInfo("261 Collins St", "", "Blackfoot", State.Idaho, "83221",
			CountyIdaho.Bingham, "United States", AddressType.Work);
	private String ssn;

	// private AddressInfo newAddress = new AddressInfo("1018 Jefferson", "",
	// "Idaho Falls",State.Idaho, "83402",CountyIdaho.Bonneville,"United
	// States",AddressType.Mailing);	
	
	@Test
	public void testNewPerson() throws Exception {
		
		this.ssn = StringsUtils.getValidSSN();
		this.ssn = StringsUtils.formatSSN(ssn);
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		myDBAs.add(newDBA);
		// this.rolesToAdd.add(ContactRole.Vendor);
		// this.rolesToAdd.add(ContactRole.Lienholder);

        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName(this.firstName, this.middleName, this.lastName).withDba(this.myDBAs)
				.withPrimaryAddress(myAddress)
				.withAgent(AgentsHelper.getRandomAgent().getAgentUserName())
				.withGenerateAccountNumber(true)
				.withSSN(ssn + "")
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsAddressLink();
		String popupText = contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		if(!popupText.contains("Are you sure you want to cancel?")){
			Assert.fail("No popup on Address Tab.");
		}
		contactPage.clickContactDetailsBasicsRelatedContactsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsAccountsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsDBAsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsPaidDuesLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);

		TopInfo logMeOut = new TopInfo(driver);
		logMeOut.clickTopInfoLogout();

        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName,
				myContactObj.addresses.get(0).getLine1());

        contactPage = new ContactDetailsBasicsAB(driver);
		String pageTitle = contactPage.getContactPageTitle();
		if (!pageTitle.equals(myContactObj.firstName + " " + myContactObj.lastName)) {
			throw new Exception("The page title does not match the name of the contact that was to be created.");
		}
		
		System.out.println(myContactObj.accountNumber);

	}
	
	public GenerateContact getPersonContact(){
		return this.myContactObj;
	}
	
	/**
	* @Author sbroderick
	* @Requirement 
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537ud/detail/userstory/42399918204">US 5595 - Add Umbrella to the Account Type</a>
	* @Description 
	* @DATE Apr 18, 2016
	* @throws Exception
	*/
	
	@Test(dependsOnMethods = {"testNewPerson"})
	public void typeUmbrella() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName,
				myContactObj.addresses.get(0).getLine1());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		String pageTitle = contactPage.getContactPageTitle();
		if (!pageTitle.equals(myContactObj.firstName + " " + myContactObj.lastName)) {
			throw new Exception("The page title does not match the name of the contact that was to be created.");
		}
		contactPage.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB acct = new ContactDetailsAccountAB(driver);
		acct.clickEdit();
		acct.clickAdd();
		acct.setAccountType();
	}
	
	@Test(dependsOnMethods = {"testNewPerson"})
	public void sameSSNTest() throws Exception{
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		myDBAs.add(newDBA);

        GenerateContact ssnContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withSSN(ssn + "")
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + ssnContactObj.accountNumber);
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		if(!contactPage.checkForErrorMessage("Duplicate SSN found")){
			Assert.fail("An invalid SSN message should be there.");
		}
		contactPage.setContactDetailsBasicsSSN("");
		contactPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		String deliveryOption = addressPage.addDeliveryOptionCheckDescription(DeliveryOptionType.ISAOAorATIMA);
		if(deliveryOption.contains("and Or Assignes") || deliveryOption.contains("As their Interests May Appear")){
			Assert.fail("The ISAOAorATIMA should be abbreviated!");
		}
		addressPage.clickContactDetailsAddressesUpdate();
        contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.clickContactDetailsBasicsAddressLink();
		
		addressPage.addDeliveryOptionCheckDescription(DeliveryOptionType.AsTheirInterestsMayAppear);
		addressPage.clickContactDetailsAddressesUpdate();
		addressPage.clickContactDetailsAdressesEditLink();
		addressPage.addDeliveryOptionCheckDescription(DeliveryOptionType.ItsSuccessorsAndOrAssigns);
		addressPage.clickContactDetailsAddressesUpdate();
		ArrayList<String> uiDeliveryOptions = addressPage.getDeliveryOptions();
		if(!uiDeliveryOptions.get(0).equals(DeliveryOptionType.ISAOAorATIMA.getDescValue())){
			Assert.fail("The Delivery Options should be in the correct order");
		}
		System.out.println(DeliveryOptionType.AsTheirInterestsMayAppear.getDescValue());
		if(!uiDeliveryOptions.get(1).equals(DeliveryOptionType.AsTheirInterestsMayAppear.getDescValue())){
			Assert.fail("The Delivery Options should be in the correct order");
		}
		
		if(!uiDeliveryOptions.get(2).equals(DeliveryOptionType.ItsSuccessorsAndOrAssigns.getDescValue())){
			Assert.fail("The Delivery Options should be in the correct order");
		}		
	}
	

}
