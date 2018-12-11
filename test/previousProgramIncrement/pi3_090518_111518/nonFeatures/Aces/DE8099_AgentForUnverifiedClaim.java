package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @Author Denver Hysell
 * @Requirement DE8099 - Agent for Unverified Claim
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/defect/262564534516">Rally Link</a>
 * @Description :  Adds Agent through new search feature during unverified FNOL.
 * @DATE 10/29/2018
 */

public class DE8099_AgentForUnverifiedClaim extends BaseOperations {

    @BeforeMethod
    public void testSetup() {
        super.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                interact.withTabArrow(CCIDs.NavBar.CLAIM_ARROW);
                interact.withElement(CCIDs.NavBar.NEW_CLAIM).click();
                interact.withElement(CCIDs.NewClaim.SearchOrCreatePolicy.CREATE_UNVERIFIED_POLICY).click();
                interact.withSelectBox(CCIDs.NewClaim.SearchOrCreatePolicy.UnverifiedPolicy.TYPE).select("City");
                isSuccessful = true;
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Policy.");
            }
        }
    }

    @Test
    public void createUnverifiedClaimWithAgent() {
        interact.withElement(CCIDs.NewClaim.SearchOrCreatePolicy.UnverifiedPolicy.AGENT_NAME_PICKER).click();
        interact.withElement(CCIDs.NewClaim.SearchOrCreatePolicy.UnverifiedPolicy.AGENT_NAME_SEARCH).click();
        String userFirstName = "Doug";
        String userLastName = "Johnson";
        interact.withTexbox(CCIDs.UserSearch.FIRST_NAME).fill(userFirstName);
        interact.withTexbox(CCIDs.UserSearch.LAST_NAME).fill(userLastName);
        interact.withTexbox(CCIDs.UserSearch.SEARCH_BUTTON).click();
        interact.withTable(CCIDs.UserSearch.USERS_TABLE).getRowWithText(userFirstName + " " + userLastName).clickSelectButton();
        String newAgent = interact.withTexbox(CCIDs.NewClaim.SearchOrCreatePolicy.UnverifiedPolicy.AGENT_NAME).screenGrab();
        Assert.assertTrue(newAgent.equalsIgnoreCase(userFirstName + " " + userLastName), newAgent + " does not equal " + userFirstName + " " + userLastName);
    }

}
