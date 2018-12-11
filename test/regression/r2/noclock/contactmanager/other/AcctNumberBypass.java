package regression.r2.noclock.contactmanager.other;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.DateUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement A test is needed to ensure that all account numbers from 260000 to 269999 are not used.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537d/detail/defect/80133067888">Link Text</a>
* @Description 
* @DATE Dec 9, 2016
*/
public class AcctNumberBypass extends BaseTest {
	private WebDriver driver;
	private int min = 260000;
//	private int max = 269999;
	private GenerateContact myContactObj = null;
	private AbUsers user = null;
	private int amount = 358;
//	private String acctNum = "258725";
	
    @BeforeMethod
    public void beforeMethod() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
    }
    
    public int getAccountNumbersToTest(){
    	Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager);
    	int day = Integer.parseInt(DateUtils.dateFormatAsString("dd", today)) - 1;
    	if(day>27){
    		day = day - 4;
    	}
		return (amount*day)+min;
//    	return (27 * amount)+min;  //testing
    }
    
	@Test
	public void makeContact() throws Exception{
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");

        this.myContactObj = new GenerateContact.Builder(driver)
		.withCreator(user)
		.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		boolean errorFound;
		for(int i = getAccountNumbersToTest(); i <= 10 || i>269999; i++){
			errorFound = contactPage.setContactDetailsBasicsAccountNumber(Integer.toString(i));
			contactPage.clickContactDetailsBasicsUpdateLink();
			if(errorFound){
				Assert.fail("An exception was not thrown when an invalid Account Number is input.");
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
}
