package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import repository.ab.administration.AdminMenu;
import repository.ab.contact.ContactDetailsBasicsAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.StartablePlugin;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.servertools.ServerToolsStartablePlugin;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

import java.util.ArrayList;


public class US15881_SolrErrorHandlingAb extends BaseTest {
	
	private String env = "qa";
	
	@Test(dependsOnMethods = {"testSolrMessagesBuildUpWhenQueueIsDisabledPC"})
	public void testSolrMessagesBuildUpWhenQueueIsDisabledAB() throws Exception {
		SoftAssert abSolrAssert = new SoftAssert();
		ApplicationOrCenter cCenter = ApplicationOrCenter.ContactManager;
		Config cf = new Config(cCenter, env);
		WebDriver driver = buildDriver(cf);
		ArrayList<MessageQueue> queues = new ArrayList<>();
		queues.add(MessageQueue.PolicyCenterContactBroadcast);
		queues.add(MessageQueue.BillingCenterContactBroadcast);
		queues.add(MessageQueue.SolrFBM_AB);
		
		ArrayList<String> messageCatagory = new ArrayList<String>();
		messageCatagory.add("Failed");
		messageCatagory.add("Retryable Error");
		messageCatagory.add("Unsent");
		
		ArrayList<String> abPCContactQueueCount = getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.PolicyCenterContactBroadcast, messageCatagory, driver);
		ArrayList<String> abBCContactQueueCount = getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.BillingCenterContactBroadcast, messageCatagory, driver);
		ArrayList<String> abSolrQueueCount = getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.SolrFBM_AB, messageCatagory, driver);

		stopSolrPlugin(cCenter, driver);
		try {
			createContactDeleteContact(driver);
			
			if(endQueueCounthigherThanBeginningQueueCount(MessageQueue.PolicyCenterContactBroadcast, abPCContactQueueCount, getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.PolicyCenterContactBroadcast, messageCatagory, driver))) {
				abSolrAssert.assertFalse(checkEventMessageError(ApplicationOrCenter.ContactManager, MessageQueue.PolicyCenterContactBroadcast, driver), "An error related to solr was found in the "+MessageQueue.PolicyCenterContactBroadcast.getValue()+".");
	        }
			
			if(endQueueCounthigherThanBeginningQueueCount(MessageQueue.BillingCenterContactBroadcast, abBCContactQueueCount, getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.BillingCenterContactBroadcast, messageCatagory, driver))) {
				abSolrAssert.assertFalse(checkEventMessageError(ApplicationOrCenter.ContactManager, MessageQueue.BillingCenterContactBroadcast, driver), "There should be at least two messages in the Solr Event Message.");
	        }
			
			abSolrAssert.assertTrue(endQueueCounthigherThanBeginningQueueCount(MessageQueue.SolrFBM_AB, abSolrQueueCount, getEventMessageCount(ApplicationOrCenter.ContactManager, MessageQueue.SolrFBM_AB, messageCatagory, driver)), "The end count of the Solr Queue should be higher than the beginning count when the plugin is turned off.");
	
		}
		catch(Exception e) {
			ServerToolsStartablePlugin serverTools = new ServerToolsStartablePlugin(driver);
			serverTools.loginAndStartStartablePlugin(StartablePlugin.ISolrSystemPluginFBM);
			abSolrAssert.fail(e.getMessage());
		}
		resumeSolrPlugin(cCenter, driver);
		abSolrAssert.assertAll();

	}
	
	@Test
	public void testSolrMessagesBuildUpWhenQueueIsDisabledPC() throws Exception {
		SoftAssert pcSolrAssert = new SoftAssert();
		ApplicationOrCenter pCenter = ApplicationOrCenter.PolicyCenter;
		Config cf = new Config(pCenter, env);
		WebDriver driver = buildDriver(cf);

		ArrayList<MessageQueue> queues = new ArrayList<>();
		queues.add(MessageQueue.Billing_System);
		queues.add(MessageQueue.Contact_Message_Transport_PC);
		queues.add(MessageQueue.SolrFBM);
		
		ArrayList<String> messageCatagory = new ArrayList<String>();
		messageCatagory.add("Failed");
		messageCatagory.add("Retryable Error");
		messageCatagory.add("Unsent");
		
		ArrayList<String> pcBcQueueCountBeginning = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.Billing_System, messageCatagory, driver);
		ArrayList<String> pcContactQueueCountBeginning = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.Contact_Message_Transport_PC, messageCatagory, driver);
		ArrayList<String> pcSolrQueueCountBeginning = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.SolrFBM, messageCatagory, driver);

		
		stopSolrPlugin(pCenter, driver);
		try {
	        new GeneratePolicy.Builder(driver)
	                .withInsCompanyName("Test BOP")
	                .withBusinessownersLine()
	                .withCreateNew(CreateNew.Create_New_Always)
	                .withPaymentPlanType(PaymentPlanType.Annual)
	                .withDownPaymentType(PaymentType.Cash)
	                .build(GeneratePolicyType.PolicySubmitted);  
	       
	        ArrayList<String> pcBcQueueCountEnd = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.Billing_System, messageCatagory, driver);
			ArrayList<String> pcContactQueueCountEnd = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.Contact_Message_Transport_PC, messageCatagory, driver);
			ArrayList<String> pcSolrQueueCountEnd = getEventMessageCount(ApplicationOrCenter.PolicyCenter, MessageQueue.SolrFBM, messageCatagory, driver);
	        
			if(endQueueCounthigherThanBeginningQueueCount(MessageQueue.Billing_System, pcBcQueueCountBeginning, pcBcQueueCountEnd)) {
	        	pcSolrAssert.assertFalse(checkEventMessageError(ApplicationOrCenter.PolicyCenter, MessageQueue.Billing_System, driver), "There should be at least two messages in the Solr Event Message.");
	        }
			
			if(endQueueCounthigherThanBeginningQueueCount(MessageQueue.Contact_Message_Transport_PC, pcContactQueueCountBeginning, pcContactQueueCountEnd)) {
	        	pcSolrAssert.assertFalse(checkEventMessageError(ApplicationOrCenter.PolicyCenter, MessageQueue.Contact_Message_Transport_PC, driver), "There should be at least two messages in the Solr Event Message.");
	        }
			
			pcSolrAssert.assertTrue(endQueueCounthigherThanBeginningQueueCount(MessageQueue.SolrFBM, pcSolrQueueCountBeginning, pcSolrQueueCountEnd), "The end count of the Solr Queue should be higher than the beginning count when the plugin is turned off.");	
		}
		catch(Exception e) {
			ServerToolsStartablePlugin serverTools = new ServerToolsStartablePlugin(driver);
			serverTools.loginAndStartStartablePlugin(StartablePlugin.ISolrSystemPluginFBM);
			pcSolrAssert.fail(e.getMessage());
		}
		
		resumeSolrPlugin(pCenter, driver);
		pcSolrAssert.assertAll();
		
	}

    public void stopSolrPlugin(ApplicationOrCenter center, WebDriver driver) {
		ServerToolsStartablePlugin serverTools = new ServerToolsStartablePlugin(driver);
		serverTools.loginAndStopStartablePlugin(StartablePlugin.ISolrSystemPluginFBM);
		new GuidewireHelpers(driver).logout();
	}

    public void resumeSolrPlugin(ApplicationOrCenter center, WebDriver driver) {
		ServerToolsStartablePlugin serverTools = new ServerToolsStartablePlugin(driver);
		serverTools.loginAndStartStartablePlugin(StartablePlugin.ISolrSystemPluginFBM);
		new GuidewireHelpers(driver).logout();
	}
	
	public void createContactDeleteContact(WebDriver driver) throws Exception {
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");
        new GenerateContact.Builder(driver)
				.withCreator(user)
				.build(GenerateContactType.Company);
        
       ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
       detailsPage.clickContactDetailsBasicsDeleteLink();
		new GuidewireHelpers(driver).logout();
      
	}

	public boolean checkEventMessageError(ApplicationOrCenter center, MessageQueue queues, WebDriver driver) throws Exception {
		AbUsers user = AbUserHelper.getRandomDeptUser("su");
		AdminMenu adminMenu = new AdminMenu(driver);
		adminMenu.getToEventMessages(user);
		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		ArrayList<String> errors = eventMessages.getErrorMessageColumn(queues);
		for(String error : errors) {
			if(error.contains("Solr")) {
				System.out.println("The "+ queues + "contains a Solr error, please investigate.");
				return true;
			}
		} 
		return false;
	}
	
	public ArrayList<String> getEventMessageCount(ApplicationOrCenter center, MessageQueue queue, ArrayList<String> messageCatagory, WebDriver driver) throws Exception {
		AbUsers user = AbUserHelper.getRandomDeptUser("su");
		AdminMenu adminMenu = new AdminMenu(driver);
		adminMenu.getToEventMessages(user);
		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		ArrayList<String> queueCount = new ArrayList<>();
		for(String catagory : messageCatagory) {
			queueCount.add(String.valueOf(eventMessages.getQueueCount(queue, catagory)));
		}
		new GuidewireHelpers(driver).logout();
		return queueCount;
	}
	
	public boolean endQueueCounthigherThanBeginningQueueCount(MessageQueue queue, ArrayList<String> beginningMessageCounts, ArrayList<String> endMessageCounts) {
		for(int i = 0; i<beginningMessageCounts.size(); i++ ) {
			if(Integer.parseInt(beginningMessageCounts.get(i)) < Integer.parseInt(endMessageCounts.get(i))) {
				return true;
			}
		}
		return false;
	}
}
