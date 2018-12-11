package scratchpad.steve.utilities;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.administration.AdminMenu;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueuesFilterSelectOptions;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement 
* 
* 	There is no requirement.  I wrote this test to help me find contacts that are stuck in the message Queue (for test environments only) when there are 75 pages of contacts to go through and you can't sort them.

* @DATE Jun 28, 2018
*/
public class FindStuckMessage extends BaseTest{
	
	private String contactCompanyName = "TitleOne";
	private String contactFirstName = "Daniel";
	private String contactLastName = "Johnson";
	private String acct = "166698";
	private WebDriver driver = null;
	
	@Test
	public void findEventMessage() throws Exception {
		AbUsers user = AbUserHelper.getUserByUserName("su");
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
        driver = buildDriver(cf);
		AdminMenu adminMenu = new AdminMenu(driver);
		adminMenu.getToEventMessages(user);
		adminMenu.clickAdminMenuMonitoringMessageQueues();
		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		eventMessages.clickMessageQueue(MessageQueue.Billing_System);
		eventMessages.selectMessageFilterOption(MessageQueuesFilterSelectOptions.Unfinished_Messages);
//		eventMessages.clickContactInSafeOrderObjectTable(acct);
	}

}
