package regression.r2.noclock.contactmanager.rolesandpermissions;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
@QuarantineClass
public class ViewPermission extends BaseTest {
	private WebDriver driver;
	
	@Test
	public void viewOnly() throws Exception {
		Agents randomAgent = AgentsHelper.getRandomAgent();
		AbUsers user = AbUserHelper.getRandomDeptUser("Training");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchAgentContact(randomAgent);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		if(basicsPage.clickContactDetailsBasicsEditLink()) {
			Assert.fail("The edit button was found while logged in as a training user.  Please investigate.");
		} 
	}
	
	@Test
	public void viewSensitiveInfo() throws Exception {
		AbUsers uwUser = AbUserHelper.getRandomDeptUser("UW");
		int row = 5;
		int column = 5;
		
		String[][] userArray = new String[column][row]; 
		userArray[0][0] = uwUser.getUserName();
		userArray[0][1] = "false";

		userArray[1][0] = AbUserHelper.getRandomDeptUser("Transition").getUserName();
		userArray[1][1] = "true";
		
		userArray[2][0] = AbUserHelper.getRandomDeptUser("UserAdmin").getUserName();
		userArray[2][1] = "edit";
		
		userArray[3][0] = AbUserHelper.getRandomDeptUser("Policy Service").getUserName();
		userArray[3][1] = "edit";
		
		userArray[4][0] = AbUserHelper.getRandomDeptUser("Claims").getUserName();
		userArray[4][1] = "edit";
		
		for(int i = 0; i<userArray.length; i++) {
			System.out.println("i=" + i);
			Config cf = new Config(ApplicationOrCenter.ContactManager);
	        driver = buildDriver(cf);
			Agents agent = AgentsHelper.getRandomAgent();
            Login logMeIn = new Login(driver);
			logMeIn.login(userArray[i][0], uwUser.getUserPassword());

            TopMenuAB getToSearch = new TopMenuAB(driver);
			getToSearch.clickSearchTab();

            AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
			searchMe.searchAgentContact(agent);

            ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
			String ssn = basicsPage.getSsn();
			if(ssn.contains("###-##-") && userArray[i][1].contains("true")) {
				Assert.fail("The ssn should show when the user has the view sensitive information permission.");
			} else if(!ssn.contains("###") && userArray[i][1].contains("false")) {
				Assert.fail("The ssn should not show when the user does not have the view sensitive information permission.");
			} else if(userArray[i][1].equals("edit")) {
                basicsPage = new ContactDetailsBasicsAB(driver);
				basicsPage.clickContactDetailsBasicsEditLink();
				basicsPage.setContactDetailsBasicsSSN(ssn);
				basicsPage.clickContactDetailsBasicsUpdateLink();
			}
		}
	}
}
