package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class US15882_ContactSelect extends BaseTest{
	private WebDriver driver;	
	GenerateContact contactToSearch;
	String env = "dev";
	
	@Test
	public void testContactSelect() throws Exception {
		createContactToSelect();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		driver = buildDriver(cf);
        
        Agents agent = AgentsHelper.getRandomAgent();
        new Login(driver).login(agent.agentUserName, agent.agentPassword);
        new TopMenuPolicyPC(driver).clickNewSubmission();
        SubmissionNewSubmission submissionPage = new SubmissionNewSubmission(driver);
        List<String> searchedName = submissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Do_Not_Create_New, ContactSubType.Person, this.contactToSearch.lastName, this.contactToSearch.firstName, this.contactToSearch.middleName, "", this.contactToSearch.addresses.get(0).getCity(), this.contactToSearch.addresses.get(0).getState(), this.contactToSearch.addresses.get(0).getZip());
        if(!searchedName.get(3).equals("found")){
        	Assert.fail("Unable to find a contact that was created in ContactManager.");
        }
	}
	
	public void createContactToSelect() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager, env);
        driver = buildDriver(cf);
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");
        this.contactToSearch = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withUniqueName(true)
                .withTIN(StringsUtils.generateRandomNumberDigits(9))
                .withFirstLastName("Search", "Me")
                .build(GenerateContactType.Person);
        driver.quit();
        
	}
}
