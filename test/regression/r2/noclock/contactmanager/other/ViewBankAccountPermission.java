package regression.r2.noclock.contactmanager.other;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
 * @author Steve Broderick
 * @Requirement - With the correct permissions a user should be able to see bank
 * account information.
 * @Link - <a href=
 * "https://rally1.rallydev.com/#/10075774537d/detail/task/47470538141">
 * Rally Link</a>
 * @Description - A User of ContactManager, with the View Full Bank Number
 * permission should be able to see the full bank account number.
 * Users that do not have this permission should not be able to
 * view the full bank account number.
 * @DATE - 11/30/2015
 */
@QuarantineClass
public class ViewBankAccountPermission extends BaseTest {
	private WebDriver driver;
	private AbUsers userPermission;
	private AbUsers userNoPerm;
	private String password = "gw";
	private ArrayList<String> bankAcct;

    public ArrayList<String> initiateTest(String user) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getUserByUserName(user);
		new AdvancedSearchAB(driver).loginAndSearchCounty(abUser, CountyIdaho.Ada);
        ContactDetailsBasicsAB bankPage = new ContactDetailsBasicsAB(driver);
		bankPage.getContactPageTitle();
		return bankPage.getEftAccountNumber();
	}

	@Test
	public void viewBankAccount() throws Exception {
		this.userPermission = AbUserHelper.getRandomDeptUser("IS");
		this.bankAcct = initiateTest(userPermission.getUserName());
		for(String acct : bankAcct){
			if (acct.contains("*")) {
				Assert.fail("The user should be able to see bank account numbers in the EFT information section. Check the permissions.");
			}
		}
	}

	@Test(dependsOnMethods = { "viewBankAccount" })
	public void viewBankAccountDenied() throws Exception {
		this.userNoPerm = AbUserHelper.getRandomDeptUser("Policy Service");
		this.bankAcct = initiateTest(userNoPerm.getUserName());
		String partialBankAccount = null;
		for(String acct : this.bankAcct){
			partialBankAccount = "****" + acct.substring(acct.length() - 3);
			if (!partialBankAccount.equals(acct)) {
				Assert.fail("The user should be able to see the full bank account numbers in the EFT information section. Check the permissions.");
			}

		}
		
	}
}
