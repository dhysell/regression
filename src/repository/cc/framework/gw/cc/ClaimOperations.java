package repository.cc.framework.gw.cc;

import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.table.UITable;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.gw.enums.ClaimsUsers;

public class ClaimOperations {

    private UIActions interact;

    ClaimOperations(UIActions uiActions) {
        this.interact = uiActions;
    }

    public void loginAs(ClaimsUsers user) {
        interact.withTexbox(CCIDs.Login.USER_NAME).fill(user.toString());
        interact.withTexbox(CCIDs.Login.PASSWORD).fill(user.getPassword());
        interact.withElement(CCIDs.Login.LOG_IN).click();
    }

    public void accessClaim(String claimNumber) {
        interact.withTabArrow(CCIDs.NavBar.CLAIM_ARROW);
        interact.withTexbox(CCIDs.NavBar.CLAIM_NUMBER).fill(claimNumber);
        interact.withElement(CCIDs.NavBar.FIND_CLAIM).click();
    }

    public void accessExposure(String[] exposureTypes) {
        boolean isFound = false;
        interact.withElement(CCIDs.Claim.SideMenu.EXPOSURES).click();
        UITable exposuresTable = interact.withTable(CCIDs.Claim.Exposures.EXPOSURES_TABLE);
        for (String exposureType : exposureTypes) {
            UITableRow selectedRow = exposuresTable.getRowWithText(exposureType);
            if (selectedRow != null) {
                selectedRow.getCell(1).clickLink();
                isFound = true;
                break;
            }
        }
    }

    public void accessVehicleIncidentFromExposure(String[] exposureTypes, boolean inEditMode) {
        accessExposure(exposureTypes);
        interact.withElement(CCIDs.Claim.Exposures.INCIDENT_PICKER).click();
        if (inEditMode) {
            interact.withElement(CCIDs.Claim.Exposures.EDIT_INCIDENT_DETAILS).click();
        } else {
            interact.withElement(CCIDs.Claim.Exposures.VIEW_INCIDENT_DETAILS).click();
        }
    }

    public void accessInjuryIncidentFromExposure(String[] exposureTypes, boolean inEditMode) {
        accessExposure(exposureTypes);
        interact.withElement(CCIDs.Claim.Exposures.INCIDENT_PICKER).click();
        if (inEditMode) {
            interact.withElement(CCIDs.Claim.Exposures.EDIT_INCIDENT_DETAILS).click();
        } else {
            interact.withElement(CCIDs.Claim.Exposures.VIEW_INCIDENT_DETAILS).click();
        }
    }
}
