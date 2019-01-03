package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.constants.CoverageTypes;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @Author Denver Hysell
 * @Requirement US16740
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/userstory/261806542004">Rally Link</a>
 * @Description : Testing each character of the MBI number
 * @DATE 10/31/2018
 */

public class US16740_NewFlagshipMBINumber extends BaseOperations {

    @BeforeMethod
    public void beforeMethod() {
        super.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.tmakinson);
        boolean isSuccessful = false;
        while (!isSuccessful) {
            try {
                String randomInjuryIncidentClaim = interact.withDB.getRandomInjuryIncidentClaim();
                cc.accessClaim(randomInjuryIncidentClaim);
                cc.accessInjuryIncidentFromExposure(new String[]{CoverageTypes.LIABILITY_AUTO_BODILY_INJURY, CoverageTypes.MEDICAL_PAYMENTS}, false);
                interact.withElement(CCIDs.Claim.Incidents.InjuryIncident.EDIT_BUTTON).click();
                isSuccessful = true;
                System.out.println(randomInjuryIncidentClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Claim.");
            }
        }
    }

    @Test
    public void mbiNumberTest() {
        // Happy path
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJ5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be no error message.  Found: " + interact.withOptionalElement(CCIDs.ERROR_MESSAGE).screenGrab());
        }

        // Check 1st character is always numeric.
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("EEM3-DJ5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 2nd character is always alpha
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("62M3-DJ5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 3rd character is always alphanumeric
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6E*3-DJ5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 4th character is always numeric.
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EMA-DJ5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 5th character is always alpha
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-1J5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 6th character is always alphanumeric
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-D*5-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 7th character is always numeric
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJJ-XX76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 8th character is always alpha
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJ5-1X76");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 9th character is always alpha
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJ5-X776");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 10th character is always numeric
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJ5-XXX6");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

        // check 11th character is always numeric
        interact.withTexbox(CCIDs.Claim.Incidents.InjuryIncident.MBI).fill("6EM3-DJ5-XX7X");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        if (!interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            Assert.fail("There should be an error message.  Found: Nothing");
        }

    }

}
