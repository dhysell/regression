package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.framework.init.ClaimCenterBaseTest;
import repository.cc.framework.init.Environments;
import repository.cc.framework.utils.helpers.CCIDs;
import repository.gw.enums.ClaimsUsers;
import org.junit.Test;
import org.testng.annotations.BeforeMethod;

/**
 * @Author Denver Hysell
 * @Requirement US16068 - Change County Lookup for Recoveries
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/userstory/248231983940"Link>Rally Requirements</a>
 * @Description : Testing that new method for selecting a county in recoveries causes no problems.
 * @DATE 9/20/2018
 */

public class US16068_ChangeCountyLookupForRecoveries extends ClaimCenterBaseTest {

    @BeforeMethod
    public void beforeMethod() {
        super.initDriverOn(Environments.DEV);
        super.init();
    }

    @Test
    public void ChangeCountyLookupForRecoveries() {
        actions.login(ClaimsUsers.abatts);
        actions.propertyFNOL.create();

        // Click link to view new claim
        actions.findElement(CCIDs.NewClaim.NewClaimSaved.Elements.VIEW_NEW_CLAIM).click();

        actions.getDriver().quit();
    }

}
