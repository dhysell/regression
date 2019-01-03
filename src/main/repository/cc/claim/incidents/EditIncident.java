package repository.cc.claim.incidents;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.Incidents;

public class EditIncident extends BasePage {

    private WebDriver driver;

    public EditIncident(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void editIncidents(Incidents incidentType) {

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
            case editPropertyEquipmentBreakDown:
                editPropertyEquipmentBreakDown();
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

    private void editPropertyEquipmentBreakDown() {
        EditPropertyIncidents editIncident = new EditPropertyIncidents(this.driver);
        editIncident.propertyEquipmentBreakdown();
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
        EditPropertyIncidents editIncident = new EditPropertyIncidents(this.driver);
        editIncident.property();

    }

    private void editThirdPartyProperty() {
        EditPropertyIncidents editIncident = new EditPropertyIncidents(this.driver);
        editIncident.thirdPartyProperty();
    }

    private void editThirdPartyVehicle() {
        EditVehicleIncidents editIncident = new EditVehicleIncidents(this.driver);
        editIncident.thirdPartyVehicle();

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
