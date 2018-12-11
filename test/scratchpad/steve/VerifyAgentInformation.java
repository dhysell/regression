package scratchpad.steve;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import repository.ab.contact.AgentDetailsAB;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.enums.RelatedTo;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.SearchAgentSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbAgents;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbAgentsHelper;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class VerifyAgentInformation extends BaseTest {

    private String errorString = "";
    private String log = "";
    private AbUsers abUser;
    private List<AbAgents> activeAgents;
    private ArrayList<AbAgents> inactiveAgents;
    private WebDriver driver;
	private ArrayList<String> verificationPoints = new ArrayList<String>();
	
	//Need to acertain what is on the PC doc and we should at the very least verify this. Extra will be a bonus.
	/*Name
	 * preferred name
	 * address
	 * phone
	 *	
	 * 
	 * */
	public void setVerificationPoints() {
		verificationPoints.add("");
		
	}
	
    @AfterClass
    public void afterClass() throws Exception {

    }

    //	@Test
    public void verifyActiveAgentDeets() throws Exception {
        this.activeAgents = AbAgentsHelper.getActiveAgents();
        verifyAgentDeets(activeAgents, "Active Agents");
    }

    @Test
    public void verifyInactiveAgentDeets() throws Exception {
        this.inactiveAgents = AbAgentsHelper.getInactiveAgentsAsOfYear(2014);
        verifyAgentDeets(inactiveAgents, "Inactive Agents");
    }


    public void verifyAgentDeets(List<AbAgents> agents, String agentType) throws Exception {
        this.errorString = "";
        this.log = "";
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        try {
            log += "The " + agentType + " are about to be verified.";
            for (int i = 0; i < agents.size(); i++) {
                log += "\r\n*********************************************************************************************************************\r\n About to test " + agents.get(i).getAgtname().trim() + " which is agent number " + i + " in the active agents list.\r\n";
                TopMenuSearchAB menu = new TopMenuSearchAB(driver);
                menu.clickSearch();
                SidebarAB sidebar = new SidebarAB(driver);
                sidebar.clickSidebarAgentSearchLink();

                SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
                if (agents.get(i).getAgt().length() <= 2) {
                    searchPage.setSearchAgentSearchAgentNumber("0" + agents.get(i).getAgt());
                } else {
                    searchPage.setSearchAgentSearchAgentNumber(agents.get(i).getAgt());
                }
                searchPage.clickSearchAgentSearchSearch();
                String agentLastName = agents.get(i).getLastName();
                boolean found = false;
                switch (agentLastName) {
                    case "D'AMBRA":
                        found = searchPage.clickSearchAgentSearchAgentLink("D Ambra", agents.get(i).getAgt());
                        break;
                    case "MCCLURE":
                        found = searchPage.clickSearchAgentSearchAgentLink("McClure", agents.get(i).getAgt());
                        break;
                    case "VON BRETHORST":
                        found = searchPage.clickSearchAgentSearchAgentLink("Von Brethorst", agents.get(i).getAgt());
                        break;
                    default:
                        found = searchPage.clickSearchAgentSearchAgentLink(StringsUtils.capitalizeName(agents.get(i).getLastName()), agents.get(i).getAgt());
                        break;
                }

                if (found) {
                    //Alt Name - Title
                    ContactDetailsBasicsAB agentPage = new ContactDetailsBasicsAB(driver);
//					String text = agentPage.getAltName();
//					compare(agents.get(i),text,StringsUtils.capitalizeName(agents.get(i).getFirstName().trim())+" "+StringsUtils.capitalizeName(agents.get(i).getLastName().trim()), "Alternate Name");	
//					log += "Alternate Name for agent "+agents.get(i).getAgtname().trim()+" has been checked.\r\n";
                    //Hire Date
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String hireDateDB = agents.get(i).getEffdate().replace("-", "/").trim();
                    String hireDate = agentPage.getAgentHireDate();
                    compare(agents.get(i), hireDate, hireDateDB, "Hire Date");
                    log += "HireDate for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Termination Date
                    agentPage = new ContactDetailsBasicsAB(driver);
                    if (agents.get(i).getTerminationdate().contains("00")) {
                        String termDate = agentPage.checkIfTerminationDateExists();
                        if (!termDate.equals("false")) {
                            log += "A termination Date exists for " + agents.get(i).getAgtname().trim() + ".\r\n";
                        }
                    }
                    log += "Termination Date for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Blue Cross
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String blueCrossNumber = agentPage.getAgentNumber("Blue Cross Number");
                    compare(agents.get(i), blueCrossNumber, agents.get(i).getBc(), "Blue Cross Number");
                    log += "Blue Cross Number for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Blue Shield
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String blueShieldNumber = agentPage.getAgentNumber("Blue Shield Number");
                    if (blueShieldNumber.contains("does not exist")) {
                        errorString += "The Blue Shield Number does not exist for " + agents.get(i).getAgtname().trim() + ".\r\n";
                        log += "The Blue Shield Number does not exist for " + agents.get(i).getAgtname() + ".";
                    } else {
                        int bs = Integer.parseInt(blueShieldNumber);
                        compare(agents.get(i), bs + "", agents.get(i).getBs(), "Blue Shield Number");
                    }
                    log += "Blue Shield Number for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Farm Bureau Life Number
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String farmBureauLifeNumber = agentPage.getAgentNumber("Farm Bureau Life Number");
                    compare(agents.get(i), farmBureauLifeNumber, agents.get(i).getFblife(), "Farm Bureau Life Number");
                    log += "Farm Bureau Life Number for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //IDFB Agent Number
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String idfbAgentNumber = agentPage.getAgentNumber("IDFB Agent Number");
                    compare(agents.get(i), idfbAgentNumber, agents.get(i).getAgt(), "IDFB Agent Number");
                    log += "Idaho Farm Bureau Agent Number for agent, " + agents.get(i).getAgtname().trim() + ", has been checked.\r\n";

                    //State License Number
                    agentPage = new ContactDetailsBasicsAB(driver);
                    String stateLicenseNumber = agentPage.getAgentNumber("State License Number");
                    if (stateLicenseNumber.contains("does not exist")) {
                        log += "State License Number for agent " + agents.get(i).getAgtname().trim() + " does not exist. \r\n";
                        errorString += "State License Number for agent " + agents.get(i).getAgtname().trim() + " does not exist. \r\n";

                    } else {
                        int sl = Integer.parseInt(stateLicenseNumber);
                        compare(agents.get(i), sl + "", agents.get(i).getLicense(), "State License Number");
                    }
                    log += "State License Number for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Manager
                    agentPage = new ContactDetailsBasicsAB(driver);
                    if (agentPage.getAgentManager().length() > 1 && agents.get(i).getManager().trim().length() > 2) {
                        compare(agents.get(i), getAgencyManagerAltName(StringsUtils.capitalizeName(agentPage.getAgentManager().trim())), StringsUtils.capitalizeName(agents.get(i).getManager().trim()), "Manager");
                    } else {
                        errorString += agents.get(i).getAgtname().trim() + " check to make sure this Agent has a valid manager. \r\n";
                    }
                    log += "Agents Manager for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //CountyName
                    agentPage = new ContactDetailsBasicsAB(driver);
                    compare(agents.get(i), agentPage.getAgentCounty().trim().toUpperCase(), agents.get(i).getCountyname().trim().toUpperCase(), "County");
                    log += "Agents County for agent " + agents.get(i).getAgtname().trim() + " has been checked.\r\n";

                    //Speed Dial
                    agentPage = new ContactDetailsBasicsAB(driver);
                    compare(agents.get(i), agentPage.getAgentSpeedDial(), agents.get(i).getSd(), "Speed Dial");
                    log += agents.get(i).getAgtname().trim() + "'s speed dial has been checked.\r\n";

                    //Agency
                    agentPage = new ContactDetailsBasicsAB(driver);
                    compare(agents.get(i), agentPage.getAgentAgency(), getRegion(agents.get(i).getAgc()), "Region");
                    log += agents.get(i).getAgtname().trim() + "'s Agency has been checked.\r\n";

                    //Work Address
                    agentPage = new ContactDetailsBasicsAB(driver);
                    agentPage.clickContactDetailsBasicsAddressLink();
                    ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
                    AddressInfo workAddress = addressPage.getAddressByType("Work");
                    if (workAddress != null) {
                        compare(agents.get(i), StringsUtils.capitalizeName(workAddress.getLine1().trim()), StringsUtils.capitalizeName(agents.get(i).getAddress().trim()), "Address Line 1");
                        compare(agents.get(i), StringsUtils.capitalizeName(workAddress.getCity().trim()), StringsUtils.capitalizeName(agents.get(i).getCity().trim()), "City");
                        compare(agents.get(i), workAddress.getZip().substring(0, 5), agents.get(i).getZip(), "Zip");
                    } else {
                        log += agents.get(i).getAgtname().trim() + " has no work address listed.\r\n";
                        errorString += agents.get(i).getAgtname().trim() + " has no work address listed.\r\n";
                    }
                    log += agents.get(i).getAgtname().trim() + "'s work address has been checked.\r\n";

                    PageLinks contactLinks = new PageLinks(driver);
                    contactLinks.clickContactDetailsBasicsRelatedContactsLink();
                    //Related Contact County
                    ContactDetailsRelatedContactsAB relatedPage = new ContactDetailsRelatedContactsAB(driver);
                    String uiCounty = relatedPage.findNameBasedOnRelationship(RelatedTo.County);
                    if (uiCounty.contains("does not exist")) {
                        errorString += "The County relationship does not exist for agent: " + agents.get(i).getAgtname().trim() + ".\r\n ";
                        log += "The County relationship does not exist for agent: " + agents.get(i).getAgtname().trim() + ".\r\n ";
                    } else {
                        compare(agents.get(i), uiCounty.toUpperCase(), agents.get(i).getCountyname().trim().toUpperCase(), "Related To County");
                        log += "The County relationship has been checked for agent: " + agents.get(i).getAgtname().trim() + ".\r\n ";
                    }
                    //Related Contact Agent of:
                    String uiRegion = relatedPage.findNameBasedOnRelationship(RelatedTo.AgentOf);
                    compare(agents.get(i), uiRegion, getRegion(agents.get(i).getAgc()), "Agent of Region");
                    log += "The Region on the Related to Tab has been checked for agent, " + agents.get(i).getAgtname().trim() + ".\r\n ";

                    //Are Contacts Associated to Terminated Agents.
                    if (!agents.get(i).getTerminationdate().contains("00-00-0000")) {
                        contactAssociatedWithTermAgent(agents.get(i));
                        log += agents.get(i).getAgtname() + ", has been checked for existing contacts associated with the agent. \r\n";
                    }
                    System.out.println(log);
                } else {
                    log += "Agent: " + agents.get(i).getAgtname() + " was not found when searching by last name and agent number. \r\n";
                    errorString += "Agent: " + agents.get(i).getAgtname() + " was not found when searching by last name and agent number. \r\n";
                }
            }
        } catch (Exception e) {
            outputFile(log, agentType + " Verification Log");
            outputFile(errorString, agentType + " Verification Error file");
            throw e;
        }
        outputFile(log, "Agent Verification Log");
        outputFile(errorString, "Agent Verification Error file");
    }

    public void contactAssociatedWithTermAgent(AbAgents agent) {

        int terminationYear = Integer.parseInt(agent.getTerminationdate().trim().substring(agent.getTerminationdate().length() - 5));
        if (terminationYear > 1970) {
            TopMenuSearchAB menu = new TopMenuSearchAB(driver);
            menu.clickSearch();
            SidebarAB sideMenu = new SidebarAB(driver);
            sideMenu.clickSidebarAgentSearchLink();
            SearchAgentSearchAB agentSearch = new SearchAgentSearchAB(driver);
            AgentDetailsAB agentDetailsPage;
            if (agent.getLastName().trim().contains("MCC")) {
                agentDetailsPage = agentSearch.searchAgentDetails(agent.getAgt(), "McClure");
            } else {
                agentDetailsPage = agentSearch.searchAgentDetails(agent.getAgt(), StringsUtils.capitalizeName(agent.getLastName().trim()));
            }
            if (agentDetailsPage.contactAssociated()) {
                errorString += "\r\n" + agent.getAgtname() + " has contacts associated.\r\n";
                log += "The View Agent Details for " + agent.getAgtname() + " found at least one contact associated the the agent.";
                System.out.println(errorString);
            }
        }
    }

    public void compare(AbAgents agent, String abValue, String mainFrameValue, String field) {
        abValue = abValue.trim();
        mainFrameValue = mainFrameValue.trim();
        if (!abValue.equals(mainFrameValue)) {
            log += "On Agent, " + agent.getAgtname().trim() + " The " + field + " did not match what was expected. \nThe actual value: " + abValue + "\n expected value: " + mainFrameValue + ". \r\n\r\n";
            errorString += "On Agent " + agent.getAgtname().trim() + " The " + field + " did not match what was expected. \nThe actual value: " + abValue + "\n expected value: " + mainFrameValue + ". \r\n\r\n";
        }
    }

    public String getRegion(String regionNumber) {
        if (regionNumber.length() > 2) {
            regionNumber.substring(regionNumber.length() - 2);
        }
        if (regionNumber.equals("10")) {
            return "Region 1 Eastern Idaho";
        } else if (regionNumber.equals("20")) {
            return "Region 2 Magic Valley";
        } else if (regionNumber.equals("30")) {
            return "Region 3 Treasure Valley";
        } else if (regionNumber.equals("40")) {
            return "Region 4 Northern Idaho";
        } else if (regionNumber.equals("93")) {
            return "Region 4 Northern Idaho";
        } else if (regionNumber.equals("99")) {
            return "Life Specialists";
        } else {
            return "Noncommissionable";
        }
    }

    public String getAgencyManagerAltName(String manager) {
        String managerAltName = "";
        switch (manager) {
            case "Michael willits":
                managerAltName = "Mike willits";
                break;
            case "Vance nielsen":
                managerAltName = "Vance nielsen";
                break;
            case "Scott badger":
                managerAltName = "Scott badger";
                break;
            case "Ben rae":
                managerAltName = "Ben rae";
                break;
            default:
                managerAltName = "Please provide real Regional Manager Names";
        }
        return managerAltName;
    }

    public void outputFile(String text, String fileName) throws IOException {

        fileName = fileName + "-" + DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "yyyyMMddHHmm");
        RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
        FileChannel channel = stream.getChannel();
        String value = text;
        byte[] strBytes = value.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
        buffer.put(strBytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();

    }

}
