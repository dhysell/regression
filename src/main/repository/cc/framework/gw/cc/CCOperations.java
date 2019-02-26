package repository.cc.framework.gw.cc;

import org.openqa.selenium.WebDriver;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.table.UITable;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.gw.enums.ClaimsUsers;

public class CCOperations {

    private UIActions interact;

    public CCOperations(UIActions uiActions) {
        this.interact = uiActions;
    }

    public void loginAs(ClaimsUsers user) {
        login(user.name(), user.getPassword());
    }

    public void login(String userName, String password){
        interact.withTexbox(CCIDs.Login.USER_NAME).fill(userName);
        interact.withTexbox(CCIDs.Login.PASSWORD).fill(password);
        interact.withElement(CCIDs.Login.LOG_IN).click();
    }

    public void logout() {
        interact.withElement(CCIDs.NavBar.GEAR).click();
        interact.withElement(CCIDs.NavBar.LOG_OUT).click();
    }

    public void closeWindows(WebDriver driver) {
        driver.quit();
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

    public String retrieveClaimNumber() {
        return interact.withElement(CCIDs.NavBar.CLAIM).screenGrab().replaceAll("[^\\d.]", "");
    }

    public void approveActivity(String activity, String claimNumber) {
        interact.withElement(CCIDs.Claim.SideMenu.WORKPLAN).click();
        String assignedTo = interact.withTable(CCIDs.Claim.Workplan.ACTIVITIES_TABLE).getRowWithText(activity).getCell(10).getText();
        String userName = interact.withDB.getUserNameFor(assignedTo);
        logout();
        login(userName, "gw");
        accessClaim(claimNumber);
        interact.withElement(CCIDs.Claim.SideMenu.WORKPLAN).click();
        interact.withTable(CCIDs.Claim.Workplan.ACTIVITIES_TABLE).getRowWithText(activity).getCell(7).clickLink();
        interact.withElement(CCIDs.Claim.Workplan.APPROVE_BUTTON).click();
    }

    public void createReserve(String coverageType, String amount) {
        interact.withElement(CCIDs.Claim.SideMenu.EXPOSURES).click();
        interact.withTable(CCIDs.Claim.Exposures.EXPOSURES_TABLE).getRowWithText(coverageType).getCell(2).clickLink();
        interact.withElement(CCIDs.Claim.Exposures.ExposureDetailView.CREATE_RESERVE).click();
        if (!interact.withTable(CCIDs.Claim.SetReserves.RESERVES_TABLE).getRowWithText(coverageType).isPresent()) {
            interact.withElement(CCIDs.Claim.SetReserves.ADD_BUTTON).click();
            interact.withTable(CCIDs.Claim.SetReserves.RESERVES_TABLE).getRowWithText(coverageType).getCell(3).click();
            interact.withTexbox(CCIDs.Claim.SetReserves.COST_CATEGORY).fill("Vehicle Damage");
        }
        interact.withTexbox(CCIDs.Claim.SetReserves.NEW_AVAILABLE_RESERVES).fill(amount);
        interact.withElement(CCIDs.Claim.SetReserves.SAVE).click();
    }
}
