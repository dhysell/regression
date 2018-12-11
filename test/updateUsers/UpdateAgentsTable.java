package updateUsers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.AgentsBAK;

/**
 * @Author jlarsen
 * @Description This class overrides the driver to run against UAT. DROPS ALL content in the PC_AgentsMasterBAK table.
 * Class gets all the agents from the roles table in the UI and then loops thru the list getting all
 * required information.
 * info is then saved off to the PC_AgentsMasterBAK table to be transfered manually to the PC_AgentsMaster table.
 * @DATE Sep 19, 2017
 */
public class UpdateAgentsTable extends BaseTest {
    private List<String> userList = new ArrayList<String>();


    private WebDriver driver;

    @Test(enabled = true)
    public void getAgentsUsers() throws Exception {
        userList = new ArrayList<String>();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        this.driver = buildDriver(cf);

        AgentsBAK.dropTableRows(); //this is what deletes all table entried in PC_AgentMasterBAK

        Login login = new Login(driver);
        login.login("su", "gw");
        UpdateUsersCommon updateUsersCommon = new UpdateUsersCommon(driver);
        userList = updateUsersCommon.getUserList("Agent");

        for (String user : userList) {
            String userName = null;
            String agentFirstName = null;
            String agentMiddleName = null;
            String agentLastName = null;
            String agentNumber = null;
            String agentSA = null;
            String agentPA = null;
            String agentRegion = null;
            String agentCounty = null;
            String agentPreferredName = null;
            String agentCountyCode = null;
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (updateUsersCommon.userSearch(user) && updateUsersCommon.isAgent() && updateUsersCommon.isActive()) {
                userName = updateUsersCommon.getUserName();

                //USERS TO EXCLUDE
                if (userName.equals("brae") ||
                        userName.equals("kfowles") ||
                        userName.equals("Williamson") ||
                        userName.equals("dmartin") ||
                        userName.equals("bbmcclure") ||
                        userName.equals("btrumble") ||
                        userName.equals("mwells") ||
                        userName.equals("aanderson") ||
                        userName.equals("bashcraft") ||
                        userName.equals("kborgen") ||
                        userName.equals("dpaynter") ||
                        userName.equals("zbird") ||
                        userName.equals("bburbank") ||
                        userName.equals("cfaulkner") ||
                        userName.equals("lwest")) {
                    continue;
                }//end if

                agentFirstName = updateUsersCommon.getFirstName2();
                agentMiddleName = updateUsersCommon.getMiddleName2();
                agentLastName = updateUsersCommon.getLastName2();

                agentPreferredName = updateUsersCommon.getPreferredName();
                agentNumber = updateUsersCommon.getAgentNumber();
                agentSA = updateUsersCommon.getAgentSA();
                agentPA = updateUsersCommon.getAgentPA();
                agentRegion = updateUsersCommon.getUserRegion();
                agentCounty = updateUsersCommon.getUserCounty().replace("County", "").trim();
                agentCountyCode = agentCounty.replaceAll("[\\D]", "");
                agentCounty = updateUsersCommon.getUserCounty().replace("County", "").replace(" - ", "").replaceAll("[\\d]", "").trim();

                if (!userName.contains("r1") && !userName.contains("r2") && !userName.contains("r3") && !userName.contains("r4") && !userName.contains("noncommissionable") && !userName.contains("unassigned") && !userName.contains("catch")) {
                    if (userName != null && agentNumber != null && agentRegion != null) {
                        System.out.println("Adding: " + userName + " to the database");
                        try {
                            AgentsBAK.createNewAgentsUser(agentNumber, agentFirstName, agentMiddleName, agentLastName, userName, "gw", agentRegion, agentSA, agentPA, agentCounty, agentPreferredName, agentCountyCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } //end cathc
                    }//end if
                }//end if
            }//end if
        }//end for
    }//END getAgentsUsers()
}//EOF









