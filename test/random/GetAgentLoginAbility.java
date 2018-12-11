package random;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
public class GetAgentLoginAbility extends BaseTest {
	
	GeneratePolicy myPolicyObj = null;
	private WebDriver driver;
	
	@Test(enabled=false)
    public void verifyAgentLogin() throws Exception {
		List<Agents> agentsList = AgentsHelper.getAllAgents();
		List<Agents> agentsToRemove = new ArrayList<Agents>();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		for(Agents agent : agentsList) {
			
			new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
            if (new BasePage(driver).finds(By.xpath("//span[contains(text(), 'My Activities')]")).isEmpty()) {
				getQALogger().warn(agent.getAgentFullName() + " FAILED TO LOG IN.");
				agentsToRemove.add(agent);
			} else {
				new GuidewireHelpers(driver).logout();
			}
		}
		
		getQALogger().warn("AGENTS THAT FAILED TO LOG IN!!");
		for(Agents agent : agentsToRemove) {
			getQALogger().warn(agent.getAgentFullName());
		}
		
	}
	
	
	
	
	
	
	
}
