package scratchpad.jon.templates;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.MessageQueue;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;


/**
 * this is to create small test to later be moved to small modular tests
 * <p>
 * verify that spoilage coverage and brakdown is correct
 * <p>
 * modify uneditable for agents
 *
 * @author jlarsen
 */
public class SmallTestsTemp extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    private WebDriver driver;


//	@Test(enabled=true)
//	public void createPolicy() throws Exception {
//
//		GenerateTestPolicy policy = new GenerateTestPolicy();
//		policy.generateRandomPolicy(1,1,1,"Small Tests Temp");
//		this.myPolicyObj = policy.myPolicy;
//
//		System.out.println(myPolicyObj.accountNumber);
//		System.out.println(myPolicyObj.agentInfo.getAgentUserName());
//	}


    @Test()
    public void createActivity() throws Exception {
        //SUSPEND EMAILS IN ENV
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // login a SU
        new Login(driver).login("su", "gw");
        // suspend email
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickMessageQueues();
        AdminEventMessages mQues = new AdminEventMessages(driver);
        mQues.suspendQueue(MessageQueue.Email_PC);
        // logout
        new GuidewireHelpers(driver).logout();


        //create activity on account
        new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.requestActivity(ActivtyRequestType.SignatureNeeded);

        GenericActivityPC activity = new GenericActivityPC(driver);
        activity.setSubject("Escalation Email Activity");
        activity.setText("this activity is to test the Activities functionality");
        activity.setTargetDueDate(DateUtils.dateFormatAsString("ddMMyyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        activity.setEscalationDate(DateUtils.dateFormatAsString("ddMMyyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1)));
        activity.clickOK();

        //verify activity was created
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        sideMenu.clickSideMenuToolsSummary();

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickActivity("Signature Needed");

        new GuidewireHelpers(driver).logout();

    }

//	@Test(dependsOnMethods = {"createPolicy"})
//	public void emailAgentOnActivity() throws GuidewirePolicyCenterException {
//		//verify agent was emailed about activity with account info
//		IAdministrationMenu admin = AdministrationFactory.getAdministrationMenu();
//		admin.clickMessageQueues();
//		
//		IMessageQueues mQues = AdministrationFactory.getMessageQueue();
//		mQues.clickEmail();
//		mQues.selectUnfinishedMessages();
//		mQues.clickEmailAccount(myPolicyObj.accountNumber);
//		int listSize = mQues.getDestinationList().size();
//		for(int i = 0; i<listSize; i++) {
//			// get list
//			List<WebElement> myList = mQues.getDestinationList();
//			// enter i in list
//			myList.get(i).click();
//			// get mesage payload text
//			mQues = AdministrationFactory.getMessageQueue();
//			String payload = mQues.getMessagePayload();
//			//click return to account
//			mQues.returnTo();
//			//CHECK PAYLOAD TEXT
//			if(!payload.contains(myPolicyObj.agentInfo.getAgentFirstName()) || !payload.contains(myPolicyObj.agentInfo.getAgentLastName())) {
//				throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObj.accountNumber, "Activity email didn't go to correct Agent");
//			}
//			
//			if(!payload.contains(myPolicyObj.accountNumber)) {
//				throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObj.accountNumber, "Activity body didn't contain account number.");
//			}
//			
//			if(!payload.contains(myPolicyObj.insCompanyName)) {
//				throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObj.accountNumber, "Activity didn't contain Insures Name");
//			}
//			
///*			<?xml version="1.0"?>
//			<emailmessage subject="PolicyCenter - Activity Assigned">
//			  <sender emailaddress="guidewire@idfbins.com" name="Guidewire PolicyCenter"/>
//			  <replyto emailaddress="qawizpro@idfbins.com" name="Heidi Hill"/>
//			  <body>You have received an activity in PolicyCenter for Account #244582 Named Insured "OOS Change". 
//
//			Please go to PolicyCenter and review.</body>
//			  <recipient emailaddress="qawizpro@idfbins.com" name="Bill Terry" type="TO"/>
//			</emailmessage>*/
//		}
//	}
//	
//	@Test(dependsOnMethods = {"createPolicy"})
//	public void escalateActivity() {
//		//move clock and verify activity is escalated
//	}
//	
//	
//	
//	@Test(dependsOnMethods = {"createPolicy"})
//	public void emailAgencyManagerOnActivity() {
//		//verify agency manager got eamil with account info
//	}
//	
//	@Test(dependsOnMethods = {"createPolicy"})
//	public void completeActivity() {
//		//complete agent activity and verify escalated activity gone
//	}
//	
//	
//	
//	/**
//	* @author jlarsen
//	* @Requirement -  verify that Agent gets email and Agency Manager get email when activity is created and escalated.
//	* @Link - <a href="http:// ">Link Text</a>
//	* @Description - verify that Agent gets email and Agency Manager get email when activity is created and escalated. when Agent activity is completed escelated activty is also closed.
//	* @DATE - Sep 23, 2015
//	* @throws Exception
//	*/
//	@Test(dependsOnMethods = {"createPolicy"}, enabled = false)
//	public void escalateActivity2() throws Exception {
//
//		Configuration.setProduct(Product.PolicyCenter);
//		
//		// login a SU
//		login("su", "gw");
//		// suspend email
//		IAdministrationMenu admin = AdministrationFactory.getAdministrationMenu();
//		admin.clickMessageQueues();
//		IMessageQueues mQues = AdministrationFactory.getMessageQueue();
//		mQues.checkEmail();
//		mQues.clickSuspend();
//		// logout
//		logout();
//		//
//		loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);
//		
//		IActions actions = ActionsFactory.getActionsMenu();
//		actions.requestActivity(ActivtyRequestType.SignatureNeeded);
//		
//		IGenericActivity activity = ActivityFactory.getNewActivity();
//		activity.setSubject("Escalation Email Activity");
//		activity.setText("this activity is to test the Activities functionality");
//		activity.setTargetDueDate(DateUtils.dateFormatAsString("ddMMyyyy",DateUtils.getCenterDate(Product.PolicyCenter)));
//		activity.setEscalationDate(DateUtils.dateFormatAsString("ddMMyyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(Product.PolicyCenter),DateAddSubtractOptions.Day , 1)));
//		activity.clickOK();
//		
//		admin = AdministrationFactory.getAdministrationMenu();
//		admin.clickMessageQueues();
//		mQues = AdministrationFactory.getMessageQueue();
//		mQues.clickEmail();
//		mQues.selectUnfinishedMessages();
//		mQues.clickEmailAccount(myPolicyObj.accountNumber);
//		int listSize = mQues.getDestinationList().size();
//		for(int i = 0; i<listSize; i++) {
//			// get list
//			List<WebElement> myList = mQues.getDestinationList();
//			// enter i in list
//			myList.get(i).click();
//			// get mesage payload text
//			mQues = AdministrationFactory.getMessageQueue();
//			String payload = mQues.getMessagePayload();
//			//click return to account
//			mQues.returnTo();
//			//CHECK PAYLOAD TEXT
//			if(!payload.contains(myPolicyObj.agentInfo.getAgentFirstName()) || !payload.contains(myPolicyObj.agentInfo.getAgentLastName())) {
//				
//			}
//			
//			if(!payload.contains(myPolicyObj.accountNumber)) {
//				
//			}
//			
//			if(!payload.contains(myPolicyObj.insCompanyName)) {
//				
//			}
//			
///*			<?xml version="1.0"?>
//			<emailmessage subject="PolicyCenter - Activity Assigned">
//			  <sender emailaddress="guidewire@idfbins.com" name="Guidewire PolicyCenter"/>
//			  <replyto emailaddress="qawizpro@idfbins.com" name="Heidi Hill"/>
//			  <body>You have received an activity in PolicyCenter for Account #244582 Named Insured "OOS Change". 
//
//			Please go to PolicyCenter and review.</body>
//			  <recipient emailaddress="qawizpro@idfbins.com" name="Bill Terry" type="TO"/>
//			</emailmessage>*/
//		}
//		
//		


//	}

}













