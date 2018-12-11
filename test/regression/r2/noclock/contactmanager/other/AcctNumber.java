package regression.r2.noclock.contactmanager.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.administration.RolesPage;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.desktop.Desktop;
import repository.ab.search.ClaimVendorSearchAB;
import repository.driverConfiguration.Config;
import services.enums.Broker;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.VendorType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AbUserActivity;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import services.helpers.com.idfbins.membernumber.MemberNumberHelper;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import services.services.com.idfbins.membernumber.MembershipQueryResponse;

/**
 * @Author sbroderick
 * @Requirement A test is needed to ensure that all account numbers from 260000 to 269999 are not used.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537d/detail/defect/80133067888">Link Text</a>
 * @Description
 * @DATE Dec 9, 2016
 */
@QuarantineClass
public class AcctNumber extends BaseTest {
	private WebDriver driver;
	private int min = 260000;
//	private int max = 269999;
	private GenerateContact myContactObj = null;
	private AbUsers user = null;
	private int amount = 358;
//	private String acctNum = "258725";
	private String vendorNumber = "";
	private ArrayList<AbUsers> taxUsers = new ArrayList<AbUsers>();
	
    public int getAccountNumbersToTest(){
    	Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager);
    	int day = Integer.parseInt(DateUtils.dateFormatAsString("dd", today)) - 1;
    	if(day>27){
    		day = day - 4;
    	}
		return (amount*day)+min;
//    	return (27 * amount)+min;  //testing
    }
    
	
	public void makeContact() throws Exception{
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");

        this.myContactObj = new GenerateContact.Builder(driver)
		.withCreator(user)
		.withFirstLastName("B", "Troll")
		.withGenerateAccountNumber(false)
		.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
	}
	
//	Previously generated accountnumber in dev = "985790"
/*      ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		boolean errorFound;
		for(int i = getAccountNumbersToTest(); i <= 10 || i>269999; i++){
			errorFound = contactPage.setContactDetailsBasicsAccountNumber(Integer.toString(i));
			contactPage.clickContactDetailsBasicsUpdateLink();
			if(errorFound){
				throw new GuidewireContactManagerException(Configuration.getWebDriver().getCurrentUrl(), "An exception was not thrown when an invalid Account Number is input.");
			}
			contactPage.clearAcctNum();
		}
	}

//	public void clipThroughAccts() throws Exception{
//		
//			Configuration.setProduct(ApplicationOrCenter.ContactManager);
//			this.user = AbUserHelper.getRandomDeptUser("Numbe");
//			Login logMeIn = new Login(driver);
//			logMeIn.login(user.getUserName(), user.getPassword());
//			ITopMenu getToSearch = TopMenuFactory.getMenu();
//			getToSearch.clickSearchTab();
//			IAdvancedSearch searchMe = SearchFactory.getAdvancedSearch();
//			searchMe.searchByAccountNumber(this.acctNum);
//			IContactDetailsBasics contactPage = ContactFactory.getContactPage();
//			contactPage.clickContactDetailsBasicsEditLink();
//			String accountNumber = null;
//		do{
//			contactPage = ContactFactory.getContactPage();
//			accountNumber = contactPage.clickContactDetailsBasicsAccountNumberGenerateLink();
//			System.out.println("Current Account Number is: " + accountNumber + ".");
//		}while(Integer.parseInt(accountNumber) < 259998);
//		
//	}
*/	
	
	public String getMemberNumberStatusCode(String accountNum, Broker broker) throws Exception {
		MemberNumberHelper helper = new MemberNumberHelper(broker);
		MembershipQueryResponse myResponse = helper.getContactRecords(accountNum);
		System.out.println("Survey Says: "+myResponse.getServiceStatus().getCode());
		return myResponse.getServiceStatus().getCode();
	}
	
	@Test
	public void testAccountNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		makeContact();
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.clickContactDetailsBasicsAccountNumberGenerateLink();
		String generatedAcctNumber = basicsPage.getContactDetailsBasicsAccountNumber();
		if(!getMemberNumberStatusCode(generatedAcctNumber, Broker.UAT).equals("100")) {
			Assert.fail("The received generated account number should not be in use in contactManager or Nexus.  Check Account Number "+generatedAcctNumber+".");
		}
		if(!getMemberNumberStatusCode("123456", Broker.UAT).equals("000")) {
			Assert.fail("One less than the generated account number should be in use in contactManager or Nexus.  Check Account Number 123456.");
		}
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickUpdate();
		List<String> errors = new GuidewireHelpers(driver).getErrorMessages();
		for(String error : errors) {
			if(error.contains("Account Number")){
				Assert.fail("Users should be able to enter an account number in the account number field.");
			}
		}
	}
	
	@Test(dependsOnMethods = {"testAccountNumber"})
	public void testLienNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.getToContactPage(this.user, this.myContactObj.firstName, this.myContactObj.lastName, this.myContactObj.addresses.get(0).getLine1(), this.myContactObj.addresses.get(0).getState());
		basicsPage.clickEdit();
		basicsPage.addContactDetailsBasicsRole(ContactRole.Lienholder);
		String generatedLienNumber = basicsPage.clickGenerateLienNumber();
		if(!getMemberNumberStatusCode(generatedLienNumber, Broker.UAT).equals("100")) {
			Assert.fail("The received generated account number should not be in use in contactManager or Nexus.  Check Account Number "+generatedLienNumber+".");
		}
		if(!getMemberNumberStatusCode("981959", Broker.UAT).equals("000")) {
			Assert.fail("One less than the generated account number should be in use in contactManager or Nexus.  Check Account Number 981959.");
		}
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickUpdate();
		List<String> errors = new GuidewireHelpers(driver).getErrorMessages();
		for(String error : errors) {
			if(error.contains("Lienholder")){
				Assert.fail("Users should be able to enter a Lien Number in the Lien Number field.");
			}
		}
		basicsPage.clickEdit();
		basicsPage.removeContactDetailsBasicsRole(ContactRole.Lienholder);
		basicsPage.clickUpdate();
	}
	
	@Test(dependsOnMethods = {"testAccountNumber"})
	public void testVendorNumber() throws Exception {  		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.user = AbUserHelper.getRandomDeptUser("Claim");
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.getToContactPage(this.user, this.myContactObj.firstName, this.myContactObj.lastName, this.myContactObj.addresses.get(0).getLine1(), this.myContactObj.addresses.get(0).getState());
		basicsPage.clickEdit();
		this.vendorNumber = basicsPage.clickGenerateVendorNumber();
		if(!getMemberNumberStatusCode(vendorNumber, Broker.UAT).equals("100")) {
			Assert.fail("The received generated account number should not be in use in contactManager or Nexus.  Check Account Number "+vendorNumber+".");
		}
		if(!getMemberNumberStatusCode("940000", Broker.UAT).equals("000")) {
			Assert.fail("One less than the generated account number should be in use in contactManager or Nexus.  Check Account Number 940000.");
		}
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.setVendorType("Other");
		basicsPage.setBasicsPageAddressAs1099Address("1099");
		basicsPage.clickUpdate();
        basicsPage = new ContactDetailsBasicsAB(driver);
		List<String> errors = new GuidewireHelpers(driver).getErrorMessages();
		for(String error : errors) {
			if(error.contains("Vendor")){
				Assert.fail("Users should be able to enter a Vendor Number in the Vendor Number field.");
			}
		}
		AbUserActivity activity = new AbUserActivity("Create Claim Vendor in OLIE", this.myContactObj.lastName +", "+ this.myContactObj.firstName, DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)), this.user.getUserFirstName() +" "+this.user.getUserLastName());
		checkForTaxControllerActivity(activity);
	}
	
	public void checkForTaxControllerActivity(AbUserActivity activity) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		ArrayList<AbUsers> users = getUsersWithRole("Tax Controller");
        Login login = new Login(driver);
		login.login(users.get(0).getUserName(), users.get(0).getUserPassword());
        Desktop desktopPage = new Desktop(driver);
		if(!desktopPage.activityExistsInTable(activity)) {
			Assert.fail("After Genterating a Vendor Number, the user should receive an activity in ContactManager.");
		}
	}
	
	@Test
	public void testDuplicateVendorNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ClaimVendorSearchAB vendorSearch = new ClaimVendorSearchAB(driver);
		ContactDetailsBasicsAB detailsPage = vendorSearch.loginAndSearchForVendorWithRangeOfNums(AbUserHelper.getRandomDeptUser("Claim"), "940000", "940100");
		String dupVendorNum = detailsPage.getVendorNumber();
		
		ArrayList<AddressInfo> addresses = new ArrayList<>();
		AddressInfo addressOne = new AddressInfo();
		addressOne.setType(AddressType.TenNinetyNine);
		addresses.add(addressOne);
		ArrayList<ContactRole> roles = new ArrayList<>();
		roles.add(ContactRole.Vendor);

        new GenerateContact.Builder(driver)
		.withCreator(AbUserHelper.getRandomDeptUser("Claim"))
		.withGenerateAccountNumber(false)
		.withVendorType(VendorType.Other)
		.withAddresses(addresses)
		.withRoles(roles)
		.build(GenerateContactType.Person);

        detailsPage = new ContactDetailsBasicsAB(driver);
		detailsPage.clickEdit();
		detailsPage.setVendorNumber(dupVendorNum);
		detailsPage.clickUpdate();
        ErrorHandling errorsReceived = new ErrorHandling(driver);
		List<WebElement> errors = errorsReceived.getValidationMessages();
		boolean vendorErrorFound = false;
		for(WebElement error : errors) {
			if(error.getText().contains("already exists") && error.getText().contains("Claim vendor number ")) {
				vendorErrorFound = true;
			}
		}
		if(!vendorErrorFound) {
			Assert.fail("If a duplicate Claim Vendor Number exists, AB should not be able to click update and the error Vendor Number : Claim Vendor Number ###### already exists.");
		}
	}
	
	@Test(dependsOnMethods = {"testVendorNumber"})
	public void onlyTaxControllersGenerateVendorNumbers() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ClaimVendorSearchAB vendorSearch = new ClaimVendorSearchAB(driver);
        ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
		List<AbUsers> allUsers = AbUserHelper.getAllUsers();
		for(AbUsers user : allUsers) {
            vendorSearch = new ClaimVendorSearchAB(driver);
			new GuidewireHelpers(driver).logout();
			vendorSearch.loginAndSearchVendor(user, this.vendorNumber);
            detailsPage = new ContactDetailsBasicsAB(driver);
			detailsPage.clickEdit();
			if(detailsPage.clickGenerateVendorNumber().equals("") && isTaxController(user)) {
				Assert.fail("Only users with the role of Tax Controller should be able to generate a vendor number.");
			} else if(!detailsPage.clickGenerateVendorNumber().equals("")  && !isTaxController(user)) {
				Assert.fail("Only users with the role of Tax Controller should be able to generate a vendor number.");
			}
		}
	}
	
	public ArrayList<AbUsers> getUsersWithRole(String role) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        RolesPage rolePage = new RolesPage(driver);
		return rolePage.loginAndScrapeUsersOfRole(role);
}
	
	public String getNextAcctNum(String acctNum) {
		int intAcctNum = Integer.parseInt(acctNum); 
		int newNumber = intAcctNum+1;
		return newNumber + "";
	}
	
	private boolean isTaxController(AbUsers user) {
		for(AbUsers taxUser : this.taxUsers) {
			if(taxUser.getUserName().equals(user.getUserName()));
			return true;
		}
		return false;
	}
}
