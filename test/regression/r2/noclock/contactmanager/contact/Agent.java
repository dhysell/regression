package regression.r2.noclock.contactmanager.contact;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.contact.AgentDetailsAB;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SearchAgentSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.ab.ContactHistoryChange;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.BatchHelpers;
import com.idfbins.helpers.EmailUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.administration.Users;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class Agent extends BaseTest {
	private WebDriver driver;
    private GenerateContact myContactObj;
    private Agents newAgent;
    private Agents agent;
    private AbUsers abUser;

    //This test does not use a dynamic agent because (QA) does not currently keep track of Terminated Agents.
    @Test
    public void testAgentTerminatedMessage() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.agent = AgentsHelper.getAgentByName("Chad", "Cox");
        this.abUser = AbUserHelper.getRandomDeptUser("IS");
        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        ContactDetailsBasicsAB agentPage = searchPage.loginAndSearchAgentByAgentNumber(this.abUser, "391", "Cox");
        String text = agentPage.getContactPageTitle();

        if (!agentPage.getAgentTerminatedText().contains("Farm Bureau Agent: Bob Cox (391) is terminated")) {
        	driver.quit();
        	cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);
            searchPage = new SearchAgentSearchAB(driver);
            agentPage = searchPage.loginAndSearchAgentByAgentNumber(this.abUser, "898", "Cox");
            if (agentPage.getContactDetailsBasicsAgent().contains("391")) {
                Assert.fail("If Bob Cox is still Chad Cox agent, then the message banner should exist stating that Bob Cox is terminated and exists as an agent.");
            }

        }
    }

    @Test
    public void agentPage() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.abUser = AbUserHelper.getRandomDeptUser("IS Programmers");
        this.agent = AgentsHelper.getRandomAgent();
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAgentSearchLink();

        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        searchPage.setSearchAgentSearchAgentNumber(this.agent.agentNum);
        searchPage.clickSearchAgentSearchSearch();
        searchPage.clickSearchDetailsButton(this.agent.getAgentLastName(), this.agent.getAgentNum());

        AgentDetailsAB agentDeets = new AgentDetailsAB(driver);
//		String agentNum = agentDeets.getAgentNumber();
        agentDeets.getCustomerAges();
        agentDeets.clickPrint();

    }

    @Test
    public void agentChange() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        this.myContactObj = new GenerateContact.Builder(driver)
                .withCreator(abUser)
                .withAgent(AgentsHelper.getRandomAgent().getAgentUserName())
                .build(GenerateContactType.Company);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchCompanyByName(myContactObj.companyName, myContactObj.addresses.get(0).getLine1(), myContactObj.addresses.get(0).getState());

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        //Agent is not set by numbering previously.
/*		String farmBureauAgent = basicsPage.getContactDetailsBasicsAgent();
		String[] agentInfoArray = farmBureauAgent.split(" ");
		this.prevAgent = AgentsHelper.getAgentByName(agentInfoArray[2], agentInfoArray[3]);
*/
        basicsPage.clickContactDetailsBasicsEditLink();
/*		int lcv = 0;
		do{
*/
        this.newAgent = AgentsHelper.getRandomAgent();
/*			lcv++;
		}while(farmBureauAgent.contains(this.agent.agentLastName) && lcv<10);
*/
        //Changes Agent
        basicsPage.clickContactDetailsBasicsAgentSearchLink();
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.setAgent(this.newAgent.getAgentNum(),
                this.newAgent.getAgentFirstName() + " " + this.newAgent.getAgentLastName());
        basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsUpdateLink();

        basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.clickContactDetailsBasicsAgentSearchLink();
        contactPage = new ContactDetailsBasicsAB(driver);
        this.newAgent = AgentsHelper.getRandomAgent();
        
        contactPage.setAgent(this.newAgent.getAgentNum(),
                this.newAgent.getAgentFirstName() + " " + this.newAgent.getAgentLastName());
        basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsUpdateLink();
        String uiAgent = basicsPage.getContactDetailsBasicsAgent();
        ContactHistoryChange agentChangeData = checkHistory();
        Assert.assertEquals(agentChangeData.getNewValue(), uiAgent);
    }

    public ContactHistoryChange checkHistory() {
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
        historyPage.clickChangesByDetails("Farm Bureau Agent Changed");
        return historyPage.getHistoryItem("Farm Bureau Agent");
    }

    @Test
    public void testAgentInput() throws Exception {

        Agents randomAgent = AgentsHelper.getRandomAgent();

        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
                .build(GenerateContactType.Company);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.setContactDetailsBasicsAgent(randomAgent.getAgentNum(), State.Idaho);
        contactPage.clickContactDetailsBasicsUpdateLink();
        String agentString = contactPage.getContactDetailsBasicsAgent();
        Assert.assertTrue(agentString.contains(randomAgent.getAgentNum()) && agentString.contains(randomAgent.getAgentLastName()));
        contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.setAgentGarbage(randomAgent.getAgentNum(), State.Idaho);
        ErrorHandling errors = new ErrorHandling(driver);
        Assert.assertTrue(errors.validationMessageExists("Invalid format for agent: ###,XX"));
    }

    @Test
    public void testAgentAddressChange() throws Exception {
        //This arrayList should contain counties with more than three addresses.
        ArrayList<String> counties = new ArrayList<String>();
        counties.add("Ada");

        Agents randomAgent = AgentsHelper.getRandomAgentFromCounty(counties.get(0));
        System.out.println("********************************************************************************************************************************************************************************************************************************************");
        System.out.println("Please ensure that agent " + randomAgent.getAgentFullName() + " is put back together by matching the info with other environments information.");
        System.out.println("******************************************************************************************************************************************************************************************************************************");
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchAgentContact(randomAgent);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        ArrayList<AddressInfo> agentAddresses = addressPage.returnAddressInfo();
        //Finds first work address to retire it.
        String firstWorkAddressOfficeNumber = null;
        AddressInfo retiredAddress = null;
        int row;
        for (row = 0; row < agentAddresses.size(); row++) {
            if (agentAddresses.get(row).getType().equals(AddressType.Work)) {
                firstWorkAddressOfficeNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV')]")), row + 1, "Office");
                break;
            }
        }
        if (firstWorkAddressOfficeNumber == null) {
            //ensure that the primary address appears in the admin data.
        }
        retiredAddress = agentAddresses.get(row);
        addressPage.retireAddress(agentAddresses.get(row).getLine1());
        addressPage.clickContactDetailsAddressesAdd();
        addressPage.setContactDetailsAddressType(AddressType.Work);
        List<String> officeNumbers = addressPage.getOfficeNumbers();
        boolean foundOffice = true;
        officeLoop:
        for (String office : officeNumbers) {
            if (!office.contains("none")) {
                for (AddressInfo agentAdd : agentAddresses) {
                    System.out.println("Office Number is: " + office);
                    System.out.println("agent office number = " + agentAdd.getNumber());
                    System.out.println("Office in dropdown is " + Integer.parseInt(office));
                    if ((agentAdd.getNumber() != Integer.parseInt(office))) {
                        foundOffice = false;
                    } else {
                        foundOffice = true;
                        break;
                    }
                }
                if (!foundOffice) {
                    addressPage.clickProductLogo();
                    addressPage.setContactDetailsAddressesLocation(office);
                    break officeLoop;
                }
            }
        }
        //get the address and add it to agentAddresses.
        agentAddresses.add(new AddressInfo(addressPage.getAddressLine1(), addressPage.getCity(), addressPage.getContactDetailsAddressesState(), addressPage.getContactDetailsAddressesZipCode()));
        addressPage.clickContactDetailsAddressesUpdate();
        ErrorHandling errorCheck = new ErrorHandling(driver);
        if (!errorCheck.getValidationMessages().isEmpty())
            if (errorCheck.getValidationMessages().get(0).getText().contains("primary phone type")) {
                addressPage.setContactDetailsAddressesWorkPhone("2082394369");
                addressPage.clickContactDetailsAddressesUpdate();
            }
        AddressInfo pcAddress = checkAgentAddressPC(randomAgent);
        unRetireAgentAddress(randomAgent, agentAddresses.get(agentAddresses.size() - 1), retiredAddress);
        if (!pcAddress.getLine1().equals(agentAddresses.get(agentAddresses.size() - 1).getLine1())) {
            sendEmail(randomAgent, CountyIdaho.Ada);
            Assert.fail("The Addresses did not match. Investigate.");
        }

    }

    public AddressInfo checkAgentAddressPC(Agents agent) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Agent_Information_Update);
        new Login(driver).login("su", "gw");
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickUserSearch();
        Users uiInfo = new Users(driver);
        return uiInfo.getAgentAddressFromUserProfile(agent.getAgentUserName());
    }

    public void sendEmail(Agents agent, CountyIdaho county) {

        ArrayList<String> emailsToSendTo = new ArrayList<String>();
        emailsToSendTo.add("sbroderick@idfbins.com");

        String emailBody = "Steve:<br/><br/><p>The \"Agents\" test method: testAgentAddressChange failed. "
                + "Please ensure that agent " + agent.getAgentFullName() + " is put back together by matching the info with other environments information."
                + "Please also ensure that " + county.getValue() + " County is put back together";

        new EmailUtils().sendEmail(emailsToSendTo, "The \"Agents\" test method: testAgentAddressChange failed. ", emailBody, null);
    }

    public void unRetireAgentAddress(Agents agent, AddressInfo addressToRemove, AddressInfo unretireAddress) throws Exception {
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchAgentContact(agent);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        addressPage.removeAddress(addressToRemove.getLine1());
        addressPage.clickContactDetailsAddressesUpdate();
        addressPage.clickContactDetailsAddressesRetiredLink();
        addressPage.clickContactDetailsAdressesEditLink();
        addressPage.unretireAddress(unretireAddress.getLine1());
        addressPage.clickContactDetailsAddressesUpdate();
    }

    @Test
    public void ensureAgentRelationships() throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.getToAgentContact(AbUserHelper.getRandomDeptUser("Policy Services"), AgentsHelper.getRandomAgent());
        contactPage.clickContactDetailsBasicsRelatedContactsLink();
        ContactDetailsRelatedContactsAB relatedTab = new ContactDetailsRelatedContactsAB(driver);
        relatedTab.removeRelationship(RelationshipsAB.County);
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
        relatedTab.removeRelationship(RelationshipsAB.AgentOf);
        Contact agentCounty = new Contact(false);
        agentCounty.setCompanyName(agent.agentCounty);
        agentCounty.setContactIsPNI(false);
        agentCounty.setPersonOrCompany(ContactSubType.Company);
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
        relatedTab.setRelationship(RelationshipsAB.County, agentCounty);
        Contact agentRegion = new Contact(false);
        agentRegion.setCompanyName(agent.getAgentRegion());
        agentCounty.setContactIsPNI(false);
        agentCounty.setPersonOrCompany(ContactSubType.Company);
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
        relatedTab.setRelationship(RelationshipsAB.AgentOf, agentRegion);

    }
}
