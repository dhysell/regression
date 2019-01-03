package repository.gw.generate.cc;

import org.openqa.selenium.WebDriver;
import repository.cc.claim.incidents.EditVehicleIncidents;
import repository.cc.claim.incidents.NewVehicleIncidents;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.Incidents;

public class GenerateIncident extends BasePage {

    private WebDriver driver;

    public GenerateIncident(Incidents incidentType, WebDriver driver) {
        super(driver);
        this.driver = driver;
        switch (incidentType) {
            case newThirdPartyVehicle:
                newThirdPartyVehicle();
                break;
            case newVehicle:
                newVehicle();
                break;
            case newThirdPartyProperty:
                newThirdPartyProperty();
                break;
            case newProperty:
                newProperty();
                break;
            case newRecVehicle:
                newRecVehicle();
                break;
            case newInjury:
                newInjury();
                break;
            case newOther:
                newOther();
                break;
            case editThirdPartyVehicle:
                editThirdPartyVehicle();
                break;
            case editVehicle:
                editVehicle();
                break;
            case editThirdPartyProperty:
                editThirdPartyProperty();
                break;
            case editProperty:
                editProperty();
                break;
            case editRecVehicle:
                editRecVehicle();
                break;
            case editInjury:
                editInjury();
                break;
            case editOther:
                editOther();
                break;
            default:
                break;

        }
    }

    private void editOther() {
        // TODO Auto-generated method stub

    }

    private void editInjury() {
        // TODO Auto-generated method stub

    }

    private void editRecVehicle() {
        EditVehicleIncidents editIncident = new EditVehicleIncidents(this.driver);
        editIncident.recVehicle();
    }

    private void editProperty() {
        // TODO Auto-generated method stub

    }

    private void editThirdPartyProperty() {
        // TODO Auto-generated method stub

    }

    private void editThirdPartyVehicle() {
        // TODO Auto-generated method stub

    }

    private void newOther() {
        // TODO Auto-generated method stub

    }

    private void newInjury() {
        // TODO Auto-generated method stub

    }

    private void newRecVehicle() {
        // TODO Auto-generated method stub

    }

    private void newProperty() {
        // TODO Auto-generated method stub

    }

    private void newThirdPartyProperty() {
        // TODO Auto-generated method stub

    }

    private void newVehicle() {
        // TODO Auto-generated method stub

    }

    private void newThirdPartyVehicle() {
        NewVehicleIncidents newIncident = new NewVehicleIncidents(this.driver);
        newIncident.thirdPartyVehicle();
    }

    private void editVehicle() {
        EditVehicleIncidents editIncident = new EditVehicleIncidents(this.driver);
        editIncident.vehicle();
    }

}
