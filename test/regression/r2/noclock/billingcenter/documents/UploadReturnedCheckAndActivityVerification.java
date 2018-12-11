package regression.r2.noclock.billingcenter.documents;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.actions.BCCommonActionsUploadNewDocument;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.Priority;
import repository.gw.enums.SecurityLevel;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import regression.r2.noclock.policycenter.issuance.TestIssuance;
/**
* @Author jqu
* @Description Uploading Returned Check generates an activity and goes to appropriate queue.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-03%20Returned%20Check%20Activity.docx">Returned Check Activity</a>
* @Test Environment: CPP/PL branch
* @DATE Oct 23, 2015
*/

//import com.idfbins.driver.BaseTest;
@QuarantineClass
public class UploadReturnedCheckAndActivityVerification extends BaseTest {
	private WebDriver driver;
	private String username="sbrunson";
	private String password="gw";
	private GeneratePolicy myPolicyObj;	
	private String escalationDate,dueDate;
	private BCAccountMenu acctMenu;	
	private BCPolicyMenu bcMenu;		
	private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents",documentName="just_a_doc";
	private String subject="Returned Check";	
	private int errorCount=0;	
	private Date effectiveDate;
	private Map<String, String> activityShouldBe = new HashMap<String, String>();	
	private boolean findActivity, passVerify;
	
	@Test
	public void Generate() throws Exception {
		TestIssuance issuance = new TestIssuance();
        issuance.testBasicIssuanceInsuredOnly();
        this.myPolicyObj = issuance.myPolicyObjInsuredOnly;
        effectiveDate = myPolicyObj.busOwnLine.getEffectiveDate();
		dueDate=DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(effectiveDate, DateAddSubtractOptions.Day, 7));
		escalationDate=DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(effectiveDate, DateAddSubtractOptions.Day, 8));
	}
	
	@Test(dependsOnMethods = { "Generate" })
    public void uploadReturnedCheckOnAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(username, password, myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActionsUploadNewDocument();
		BCCommonActionsUploadNewDocument acctDoc = new BCCommonActionsUploadNewDocument(driver);
		acctDoc.uploadAccountLevelDocument(documentDirectoryPath, documentName, DocumentStatus.Draft, SecurityLevel.Unrestricted, DocumentType.Returned_Check, myPolicyObj.accountNumber);		
	}
	
	@Test(dependsOnMethods = { "uploadReturnedCheckOnAccount" })
    public void verifyActivityInfoOnAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(username, password, myPolicyObj.accountNumber);
		//activityShouldBe.put("Description", "Returned Check Payment was Received");
		activityShouldBe.put("Description", "Returned Check Document was Received");
		activityShouldBe.put("Priority", Priority.High.getValue());
		activityShouldBe.put("Due Date", dueDate);
		activityShouldBe.put("Escalation Date", escalationDate);
        if (myPolicyObj.busOwnLine.getPolicyNumber().substring(0, 3).equals("08-")) {
			activityShouldBe.put("Assigned To", "AR General Western Community - AR Western Community");	
		}else{			 
			activityShouldBe.put("Assigned To", "AR General Farm Bureau - AR Farm Bureau");	
		}

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActivities();
		BCCommonActivities activities = new BCCommonActivities(driver);
		findActivity=activities.verifyActivityTable(effectiveDate, OpenClosed.Open, subject);
		if(findActivity){
			activities.clickActivityTableSubject(effectiveDate, OpenClosed.Open, subject);
			passVerify = activities.verifyActivityInfo(activityShouldBe);
			if(!passVerify){
				getQALogger().error("Activity info is not correct for "+myPolicyObj.accountNumber+".");
				errorCount++;
			}
		}
		else{
			getQALogger().error("Didn't find Returned Check activity for "+myPolicyObj.accountNumber+".");
			errorCount++;	
		}
				
	}
	//test Returned Check on policy level
	@Test(dependsOnMethods = { "verifyActivityInfoOnAccount" })
    public void uploadReturnedCheckOnPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(username, password, myPolicyObj.accountNumber);
        bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuActionsUploadNewDocument();
		BCCommonActionsUploadNewDocument policyDoc = new BCCommonActionsUploadNewDocument(driver);
		policyDoc.uploadPolicyLevelDocument(documentDirectoryPath, documentName, DocumentStatus.Draft, SecurityLevel.Unrestricted, DocumentType.Returned_Check);		
	}
	
	@Test(dependsOnMethods = { "uploadReturnedCheckOnPolicy" })
	public void verifyActivityAndQueueOnPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(username, password, myPolicyObj.accountNumber);
        bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuActivities();
		BCCommonActivities policyActivities = new BCCommonActivities(driver);
		findActivity=policyActivities.verifyActivityTable(effectiveDate, OpenClosed.Open, subject);
		if(findActivity){
			policyActivities.clickActivityTableSubject(effectiveDate, OpenClosed.Open, subject);

			passVerify=policyActivities.verifyActivityInfo(activityShouldBe);
			if(!passVerify){
				getQALogger().error("Activity info is not correct for " + myPolicyObj.busOwnLine.getPolicyNumber() + ".");
				errorCount++;
			}
		}
		else{
			getQALogger().error("Didn't find Returned Check activity for " + myPolicyObj.busOwnLine.getPolicyNumber() + ".");
			errorCount++;	
		}
		if (errorCount>0) {
			Assert.fail("found errors, please see the output for the detail.");
		}
	}	
}