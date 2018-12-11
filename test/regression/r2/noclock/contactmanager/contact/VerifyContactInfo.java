package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.helpers.StringsUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import regression.r2.noclock.contactmanager.search.AgentSearchTest;
public class VerifyContactInfo extends BaseTest {
	private WebDriver driver;
	Agents agent;
	String county;
	String agency;
	String speedDial;
	private Contacts contact = null;
	
	@Test
	public void verifyAgentInformation() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		AgentSearchTest searchAgent = new AgentSearchTest();
		searchAgent.AgentSearchTestMain();
		this.agent = searchAgent.getAgent();
        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsLink();
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		this.county = contactPage.getAgentCounty().substring(0, contactPage.getAgentCounty().length()-7);
		this.agency = contactPage.getAgentAgency();
		this.speedDial = contactPage.getAgentSpeedDial();
		
		if(!(agent.agentCounty.startsWith(county))){
			Assert.fail("The agent county in the QA table did not match the agent county displayed.");
		}
			
		if(!agency.equals(agent.getAgentRegion())){
			Assert.fail("The agent Region in the QA table did not match the agent Region displayed.");
		
		}
/*		
		if(!speedDial.matches("\\d{3}")){
			throw new GuidewireContactManagerException(Configuration.getWebDriver().getCurrentUrl(), agent.agentFullName, "The agent speed dial was not three digits.");
		}
*/
    }
	
	@Test
	public void ensureElementsOnPageForUsers() throws Exception {
		List<AbUsers> users = AbUserHelper.getAllUsers();
		this.contact = ContactsHelpers.getContactWithPhone();
		for(AbUsers user : users) {
			System.out.println(user.getUserName());
			Config cf = new Config(ApplicationOrCenter.ContactManager);
	        driver = buildDriver(cf);
            AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
			if(this.contact.isContactIsCompany()) {
				//loginAndSearchContact(AbUsers abUser, String firstName, String lastName, String address, State state)
				searchMe.loginAndSearchContact(user, "", this.contact.getContactName(), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
			} else {
				ArrayList<String> contactName = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
				searchMe.loginAndSearchContact(user, contactName.get(0), contactName.get(contactName.size()-1), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
			}
            ContactDetailsBasicsAB contactDetails = new ContactDetailsBasicsAB(driver);
			String abUID = contactDetails.getaddressBookUID();
			if(!abUID.contains("prd")) {
				Assert.fail("The contact needs to show the Addressbook UID");
			}
			String updateDate = contactDetails.getUpdateByText();
			if(updateDate == null || updateDate.equals("Could not find the Update By text.")) {
				Assert.fail("The update by was not found.");
			}
			
			String createDate = contactDetails.getCreateByText();
			if(createDate == null || createDate.contains("Could not find the Create By text.")) {
				Assert.fail("The create by was not found.");
			}			
		}
	}
}
