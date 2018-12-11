package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.List;



/**
* @Author sbroderick
* @Requirement After solr implementation, users should be able to choose the correct contact.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:b:/s/TeamNucleus/ETey8DWIMJJLnGNJ9t0hL4wBtpHnHFF1HRg2dW9fuN0QHA?e=KDAu1q">Requirements Documentation</a>
* @Description 
* @DATE Aug 13, 2018
*/
public class US15526_RetrieveCorrectContactFromNewSubmission extends BaseTest {
	
	private String env = "it";
	
	@Test
	public void testSearchResultFromSolr() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager, env);
		WebDriver driver = buildDriver(cf);
        
		GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(AbUserHelper.getRandomDeptUser("Policy"))
				.withFirstLastName("Prave", null, "Frandborgh")
				.build(GenerateContactType.Person);
		
        driver.quit();
		
		Agents agentInfo = AgentsHelper.getRandomAgent();
		cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		driver = buildDriver(cf);
		new Login(driver).login(agentInfo.getAgentUserName(), agentInfo.getAgentPassword());
        if (new Login(driver).accountLocked()) {
        	agentInfo = new Login(driver).loginAsRandomAgent();
        }
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SubmissionNewSubmission newSubmission =  new SubmissionNewSubmission(driver);
        List<String> results = newSubmission.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Do_Not_Create_New, ContactSubType.Person, myContactObj.lastName, myContactObj.firstName, null, null, myContactObj.addresses.get(0).getCity(), myContactObj.addresses.get(0).getState(), myContactObj.addresses.get(0).getZip());
        if(!results.get(3).equals("found")) {
        	Assert.fail("The searched for contact was not found. Please check contactManager and ensure that the contact is in ab.");
        }
	}
}
