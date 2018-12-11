// Steve Broderick
package regression.r2.noclock.contactmanager.search;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.SearchAgentSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author sbroderick
 * @Requirement: History Item created when Agent contact is viewed and not when popup is viewed.
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Create%20Contact%20-%20Create%20new%20contact.xlsx">ContactManager StoryCard</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Search%20Contacts%20-%20Search%20for%20Contacts.xlsx">ContactManager StoryCard</a>
 * @Description After an agent search, when the user clicks the details link, a history item is not created. When the user clicks the link or agent name, the user should be taken to the agent contact and a history item created.
 * @DATE Oct 20, 2017
 */
public class AgentSearchTest extends BaseTest {
	private WebDriver driver;
	private Agents agent = null;
	
	public Agents getAgent() {
		return this.agent;
	}

	@Test
	public void AgentSearchTestMain() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
		searchAgent(agent, user);
		String found = verifyAgent(agent, user);
		if(found.contains("not found")) {
			Assert.fail("There should be a history item for " + user.getUserName() + " viewing the contact.");
		}
	}
	
	@Test
	public void agentSearchPopup() throws Exception {
		Agents agent = AgentsHelper.getRandomAgent();
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
		searchAgent(agent,user);
        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        searchPage.clickSearchDetailsButton(agent.getAgentLastName(), agent.getAgentNum());
		new GuidewireHelpers(driver).logout();
		AbUsers salesUser = AbUserHelper.getRandomDeptUser("Sales");
		searchAgent(agent, salesUser);
		String found = verifyAgent(agent, salesUser);
		if(found.contains("not found")) {
			Assert.fail("There should be a history item for " + user.getUserName() + " viewing the contact.");
		}

        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		if(!historyPage.verifyHistoryNoChangeDetail("Contact Viewed", "This contact was viewed by " + user.getUserFirstName() +" "+user.getUserLastName(), DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager))).contains("not found")){
			Assert.fail("There should not be a history item for " + user.getUserName() + " viewing the agent contact.");
		}
	}

    public void searchAgent(Agents agent, AbUsers user) throws Exception {
		
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
		lp.login(user.getUserName(), user.getUserPassword());
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
		menu.clickSearch();

        SidebarAB sidebar = new SidebarAB(driver);
		sidebar.clickSidebarAgentSearchLink();

        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        searchPage.setSearchAgentSearchAgentNumber(agent.agentNum);
        searchPage.clickSearchAgentSearchSearch();

		
	}
	
	public String verifyAgent(Agents agent, AbUsers user) throws Exception {

        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
		searchPage.clickSearchAgentSearchAgentLink(agent.getAgentLastName(), agent.getAgentNum());
        ContactDetailsBasicsAB agentPage = new ContactDetailsBasicsAB(driver);
		String text = agentPage.getContactPageTitle();
		if (!text.contains(agent.getAgentFirstName()) && text.contains(agent.getAgentLastName())) {
			throw new Exception("The search for " + agent.getAgentFirstName() + " " + agent.getAgentLastName() + " brought up the "
					+ text + " page.");
		}
        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		return historyPage.verifyHistoryNoChangeDetail("Contact Viewed", "This contact was viewed by " + user.getUserFirstName() +" "+user.getUserLastName(), DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager)));
	}
}
