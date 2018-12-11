package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.TaxReportingOption;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class US15533_SolrSearchSsnTin extends BaseTest{

	GenerateContact contactToSearch;
	String env = "IT";
	private Agents agent;
	
	public void createContactToSelect() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager, env);
        WebDriver driver = buildDriver(cf);
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");
        this.agent = AgentsHelper.getRandomAgent();
        this.contactToSearch = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withUniqueName(true)
                .withTIN(StringsUtils.generateRandomNumberDigits(9))
                .withFirstLastName("Search", "Me")
                .build(GenerateContactType.Person);
        driver.quit();
        
	}
	
	@Test
	public void testContactSelectTin() throws Exception {
		createContactToSelect();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);
        new Login(driver).login(this.agent.agentUserName, this.agent.agentPassword);
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SubmissionNewSubmission submissionPage = new SubmissionNewSubmission(driver);
        boolean found = submissionPage.searchAddressBookByTINSSN(true, contactToSearch.tin, TaxReportingOption.TIN, CreateNew.Do_Not_Create_New);
        Assert.assertTrue(found, "Searching by Tin, the contact was not found in ContactManager");
	}
	
	@Test
	public void testContactSelectTinWithAgent() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager, env);
        WebDriver driver = buildDriver(cf);
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");
        this.agent = AgentsHelper.getRandomAgent();
        this.contactToSearch = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withUniqueName(true)
                .withAgent(agent.getAgentUserName())
                .withTIN(StringsUtils.generateRandomNumberDigits(9))
                .withFirstLastName("Search", "Me")
                .build(GenerateContactType.Person);
        
//        driver.quit();
//		cf = new Config(ApplicationOrCenter.PolicyCenter, env);
//		driver = buildDriver(cf);
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
        new Login(driver).login(this.agent.agentUserName, this.agent.agentPassword);
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SubmissionNewSubmission submissionPage = new SubmissionNewSubmission(driver);
        boolean found = submissionPage.searchAddressBookByTINSSN(true, contactToSearch.tin, TaxReportingOption.TIN, CreateNew.Do_Not_Create_New);
        Assert.assertTrue(found, "Searching by Tin, the contact was not found in ContactManager");
	}
}
