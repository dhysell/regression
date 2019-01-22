package guidewire.claimcenter.systemMaintenance;

import gwclockhelpers.ApplicationOrCenter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class US17492_FlagshipProcessSkipForNonUSCitizens extends BaseOperations {

    @BeforeMethod
    public void setupTest() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String injuryIncidentClaim = interact.withDB.getRandomInjuryIncidentClaim();
                cc.accessClaim(injuryIncidentClaim);
                isSuccessful = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
        }
        interact.withElement(CCIDs.Claim.SideMenu.LOSS_DETAILS).click();
        List<UITableRow> rows = interact.withTable(CCIDs.Claim.LossDetails.INJURIES_INCIDENTS).getRows();
        rows.get(NumberUtils.generateRandomNumberInt(0, rows.size()-1)).getCell(2).clickLink();
        interact.withElement(CCIDs.Claim.Incidents.InjuryIncident.EDIT_BUTTON).click();
    }

    @Test()
    public void setInjuryIncident() {
        interact.withElement(CCIDs.Claim.Incidents.InjuryIncident.IS_INJURED_PARTY_US_CITIZEN_NO).click();
        interact.withElement(CCIDs.Claim.Incidents.InjuryIncident.UPDATE).click();
        if (interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            String message = interact.withOptionalElement(CCIDs.ERROR_MESSAGE).screenGrab();
            Assert.fail(message);
        }
    }
}
