package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.framework.cc.constants.Activities;
import repository.cc.framework.init.ClaimCenterBaseTest;
import repository.cc.framework.init.Environments;
import repository.cc.framework.utils.helpers.CCIDs;
import repository.cc.framework.utils.helpers.ElementLocator;
import repository.gw.enums.ClaimsUsers;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class US16103_FollowUpOnSalvageActivity extends ClaimCenterBaseTest {

    @BeforeMethod
    public void beforeMethod() {
        super.initDriverOn(Environments.DEV);
        super.init();
    }

    @Test
    public void salvageActivity() {

        actions.login(ClaimsUsers.abatts);
        actions.autoFNOL.create();

        // Click link to view new claim
        actions.findElement(CCIDs.NewClaim.NewClaimSaved.Elements.VIEW_NEW_CLAIM).click();

        actions.navigator.navigateTo(CCIDs.Claim.LossDetails.Navigation.STEPS);
        actions.findElement(CCIDs.Claim.LossDetails.Elements.EDIT_BUTTON).click();
        actions.findSelect(CCIDs.Claim.LossDetails.Elements.SALVAGE_STATUS).select("Open");
        actions.findElement(CCIDs.Claim.LossDetails.Elements.UPDATE_BUTTON).click();

        actions.navigator.navigateTo(CCIDs.Claim.Workplan.Navigation.STEPS);
        ElementLocator rowWithText = actions.findTable(CCIDs.Claim.Workplan.Elements.ACTIVITIES_TABLE).findRowWithText(Activities.FOLLOW_UP_ON_SALVAGE);
        String salvageSpecialist = actions.db.getFullUserNameWithAttibute("Salvage Specialist");

        if (rowWithText != null) {
            String newDate = LocalDate.now().plusDays(30).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            Assert.assertNotNull("Activity Created, but assigned to wrong user.  Expected \"" + salvageSpecialist + "\"", rowWithText.findCellWithText(salvageSpecialist));
            Assert.assertNotNull("Due date on activity does not match " + newDate, rowWithText.findCellWithText(newDate));
        } else {
            Assert.fail("No Activity, please check activity pattern.");
        }
        actions.getDriver().quit();
    }
}
