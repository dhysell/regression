package regression.r2.noclock.billingcenter.rolespermissions;
/**
* @Author jqu
* @Description This test is used to test role permissions on Ledger/Journal for Billing Clerical, Billing Manager, and Information Service users
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/15%20-%20Roles%20Permissions/15-01%20Roles%20Permissions.xlsx">Roles Permissions</a>
* @Test Environment: CPP/PL branch
* @DATE Sep 23, 2015
*/

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.administration.AdminUsers;
import repository.bc.administration.BCAdministrationMenu;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
public class RolesPermissionOnLedgerJournalTest extends BaseTest {
	private WebDriver driver;
	private String username="su";
	private String password="gw";
	private BCTopMenu menu;
	private String acctNum, pcNumber;
	private BCAccountMenu acctMenu;	
	private BCPolicyMenu bcMenu;
	private ArrayList<String> roles=new ArrayList<String>(3), roleUsernames = new ArrayList<String>(3);
	private int randNum;		
	private BCAccountSummary acctSum;
	private BCSearchAccounts mySearch;
	
	@Test
	public void randomlyGetRolesUsrnamesAndFindAccountPolicyNumbers() throws Exception {	
		//login as su to randomly select Billing Clerical, Billing Manager, and Information Service user names.		
		roles.add("Billing Clerical");
		roles.add("Billing Manager");
		roles.add("Information Services");
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(username, password);
		menu = new BCTopMenu(driver);
		menu.clickAdministrationTab();
		BCAdministrationMenu adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuUsersAndSecurityUsers();
		AdminUsers adminUsers = new AdminUsers(driver);
		
		//randomly select usrnames from Billing Clerical, Billing Manager, and Information Service roles
		for(int i=0;i<roles.size(); i++){
			adminUsers.setAdminUsersRole(roles.get(i));
			adminUsers.clickSearch();

			randNum=NumberUtils.generateRandomNumberInt(2, adminUsers.getAdminUsersTableRowCount()-1);
			getQALogger().info("random number is "+randNum);
			getQALogger().info(roles.get(i)+ " user name is : "+adminUsers.getAdminUsersTableUsername(randNum)+"\n");
			roleUsernames.add(adminUsers.getAdminUsersTableUsername(randNum));			
		}
		
		//find an existing account and policy number
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		mySearch=new BCSearchAccounts(driver);
		acctNum= mySearch.findAccountInGoodStanding("08-");
		getQALogger().info("acctount number is : "+acctNum);
	}
	
	@Test(dependsOnMethods = { "randomlyGetRolesUsrnamesAndFindAccountPolicyNumbers" })
	public void verifyJournalAndLedgerOnRoles() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		int errCount=0;
		for(int i=0;i<roles.size(); i++){
			new Login(driver).loginAndSearchAccountByAccountNumber(roleUsernames.get(i), password, acctNum);
            acctMenu = new BCAccountMenu(driver);
			if(!acctMenu.ledgerLinkExist()){
				errCount++;
				getQALogger().error(acctNum+": Ledger link doesn't exist with "+ roleUsernames.get(i)+" as "+roles.get(i));
			}
			else{
				acctMenu.clickBCMenuLedger();
			}
			//need to initial acctMenu again after Ledger is clicked
            acctMenu = new BCAccountMenu(driver);
			if(!acctMenu.journalLinkExist()){
				errCount++;
				System.out.println(acctNum+": Journal link doesn't exist with "+ roleUsernames.get(i)+" as "+roles.get(i));
			}
			else{
				acctMenu.clickBCMenuJournal();
			}
			//test policy level
            acctMenu = new BCAccountMenu(driver);
			acctMenu.clickBCMenuSummary();
			acctSum = new BCAccountSummary(driver);
			acctSum.clickPolicyNumberInOpenPolicyStatusTable(acctNum.substring(0, 5));
			bcMenu = new BCPolicyMenu(driver);
			if(!bcMenu.ledgerLinkExist()){
				errCount++;
				System.out.println(pcNumber+": Ledger link doesn't exist with "+ roleUsernames.get(i)+" as "+roles.get(i));
			}
			else{
				bcMenu.clickBCMenuLedger();
			}
			bcMenu = new BCPolicyMenu(driver);
			if(!bcMenu.journalLinkExist()){
				errCount++;
				System.out.println(pcNumber+": Journal link doesn't exist with "+ roleUsernames.get(i)+" as "+roles.get(i));
			}
			else{
				bcMenu.clickBCMenuJournal();
			}
			
			new GuidewireHelpers(driver).logout();
		}
		
		if(errCount>0){
			Assert.fail("found errors, please see the log for details\n");
		}
	}
	
}
