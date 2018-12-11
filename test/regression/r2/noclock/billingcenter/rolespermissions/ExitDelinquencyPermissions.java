package regression.r2.noclock.billingcenter.rolespermissions;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.administration.AdminUsers;
import repository.bc.administration.BCAdministrationMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonSummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.OpenClosed;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

/**
* @Author sgunda
* @Requirement DE4251 Permissions / Delinquency -- Cannot Manually Exit Delinquency
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/15%20-%20Roles%20Permissions/15-01%20Roles%20Permissions.xlsx ">Link Text</a>
* 					<a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/15%20-%20Roles%20Permissions/15-02%20Billing%20Clerical%20Advanced%20Role.docx">Link Text</a>
* @DATE Mar 10, 2017
*/
public class ExitDelinquencyPermissions extends BaseTest {
	private WebDriver driver;
	private String username="su";
	private String password="gw";
	private BCTopMenu menu;
	private String acctNum;
	private List<String> roles=new ArrayList<String>(), roleUsernames = new ArrayList<String>();
	private int randNum;		
	private BCSearchAccounts mySearch;
	
	@Test
	public void getRolesUsernamesAndFindAccountPolicyNumbers() throws Exception {
        //login as su to randomly select Billing Clerical, Billing Clerical Advanced, Billing Manager and General Admin
		roles.add("Billing Clerical Advanced");
		roles.add("Billing Manager");
		roles.add("General Admin");
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(username, password);
		menu = new BCTopMenu(driver);
		menu.clickAdministrationTab();
		BCAdministrationMenu adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuUsersAndSecurityUsers();
		AdminUsers adminUsers = new AdminUsers(driver);
		
		//login as su to randomly select Billing Clerical, Billing Clerical Advanced, Billing Manager and General Admin	
		for(int i=0;i<roles.size(); i++){
			adminUsers.setAdminUsersRole(roles.get(i));
			adminUsers.clickSearch();

			randNum=NumberUtils.generateRandomNumberInt(2, adminUsers.getAdminUsersTableRowCount());
			getQALogger().info("random number is "+randNum);
			getQALogger().info(roles.get(i)+ " user name is : "+adminUsers.getAdminUsersTableUsername(randNum)+"\n");
			roleUsernames.add(adminUsers.getAdminUsersTableUsername(randNum));			
		}
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		mySearch=new BCSearchAccounts(driver);
		acctNum= mySearch.findDelinquentAccount();
		getQALogger().info("acctount number is : "+acctNum);
	}
	
	@Test(dependsOnMethods = { "getRolesUsernamesAndFindAccountPolicyNumbers" })
	public void manuallyExitDelinquencyFieldOnRoles() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		int errCount=0;
		for(int i=0;i<roles.size(); i++){
			new Login(driver).loginAndSearchAccountByAccountNumber(roleUsernames.get(i), password, acctNum);

			BCCommonSummary accountSummary = new BCCommonSummary(driver);
			accountSummary.clickDelinquencyAlertMessage();
            BCCommonDelinquencies accountDelinquencies = new BCCommonDelinquencies(driver);
			new BasePage(driver).clickWhenClickable(accountDelinquencies.getDelinquencyTableRow(OpenClosed.Open, null, null, null, null, null, null, null));
						
			if(isExitDelinquencyButtonDisabled(accountDelinquencies)){
				errCount++;
				getQALogger().error("Exit Delinquency Button is not Enabled for  "+roleUsernames.get(i));
			}
					
			new GuidewireHelpers(driver).logout();
		}
		
		if(errCount>0){
			Assert.fail("found errors, please see the log for details\n");
		}
	}
	
	public boolean isExitDelinquencyButtonDisabled(BCCommonDelinquencies accountDelinquencies){
		try{
			return accountDelinquencies.getExitDelinquencyButton().getAttribute("class").contains("x-disabled");
		}catch(NoSuchElementException e){
			return true;
		}
		
	}

}


