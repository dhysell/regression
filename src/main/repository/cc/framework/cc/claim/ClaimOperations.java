package repository.cc.framework.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import repository.cc.framework.ClaimCenterActions;
import repository.cc.framework.cc.constants.ExposureTypes;
import repository.cc.framework.utils.helpers.CCIDs;
import repository.cc.framework.utils.helpers.ElementLocator;

import java.util.ArrayList;
import java.util.List;

public class ClaimOperations {

    public ActionsMenu actionsMenu;
    private WebDriver driver;
    private ClaimCenterActions actions;

    public ClaimOperations(ClaimCenterActions actions) {
        this.driver = actions.getDriver();
        this.actions = actions;
        this.actionsMenu = new ActionsMenu(this.actions);
    }

    public ClaimOperations go(String claimNumber) {
        actions.navigator.navigateTo(CCIDs.Claim.Navigation.STEPS);
        actions.findElement(CCIDs.Claim.Elements.CLAIM_NUMBER).fill(claimNumber);
        actions.findElement(CCIDs.Claim.Elements.FIND_CLAIM).click();
        return this;
    }

    public class ActionsMenu {

        private ClaimCenterActions actions;

        public ActionsMenu(ClaimCenterActions actions) {
            this.actions = actions;
        }

        private void click() {
            actions.findElement(CCIDs.Claim.ActionsMenu.Elements.ACTIONS_BUTTON).click();
        }

        private String chooseByCoverage(List<String> vehicleStrings, String exposureType) {
            actions.findElement(CCIDs.Claim.ActionsMenu.Elements.CHOOSE_BY_COVERAGE).hover();
            ElementLocator vehicleMenuIncident = null;
            String vehicleIncidentString = null;
            try {
                for (String vehicleString : vehicleStrings) {
                    vehicleMenuIncident = this.actions.findElement(By.partialLinkText(vehicleString), 2);
                    if (vehicleMenuIncident.getElement() != null) {
                        vehicleIncidentString = vehicleString;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Did not find match");
            }

            if (vehicleMenuIncident == null) {
                Assert.fail("!!! Vehicle Menu Incident was not located !!!");
            }

            vehicleMenuIncident.hover();

            if (!exposureType.equalsIgnoreCase("Random")) {
                this.actions.findElement(By.partialLinkText(exposureType), 2).click();
            } else {
                boolean isFound = false;
                for (String expoType : ExposureTypes.getAllExposureTypes()) {
                    try {
                        this.actions.findElement(By.partialLinkText(expoType), 2).click();
                        isFound = true;
                        break;
                    } catch (Exception e) {
                        System.out.println("Coverage " + expoType + " not available.");
                    }
                }
                if (!isFound) {
                    Assert.fail("A usable coverage was not found.");
                }
            }


            return vehicleIncidentString;
        }

        public String createExposureByCoverage(String exposureType) {

            List<String> vehicleStrings = new ArrayList<>();

            actions.navigator.navigateTo(CCIDs.Claim.LossDetails.Navigation.STEPS);
            List<ElementLocator> vehicleIncidentRows = actions.findTable(CCIDs.Claim.LossDetails.Elements.VEHICLES_INCIDENTS).getRows();
            for (ElementLocator vehicleIncidentRow : vehicleIncidentRows) {
                List<WebElement> vehicleIncidentCells = vehicleIncidentRow.getElement().findElements(By.tagName("td"));
                vehicleStrings.add(vehicleIncidentCells.get(1).getText() + " " + vehicleIncidentCells.get(2).getText()
                        + " " + vehicleIncidentCells.get(3).getText() + " " + vehicleIncidentCells.get(4).getText());
            }

            this.click();
            return this.chooseByCoverage(vehicleStrings, exposureType);
        }
    }
}
