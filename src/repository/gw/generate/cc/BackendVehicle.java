package repository.gw.generate.cc;

import com.idfbins.enums.State;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;



public class BackendVehicle {
    private String                 vehicleNameToSelect    = "FORD F350 2008";
    private String                 vehicleColor           = "Black";
    private String                 vehicleLicensePlateNum = "1B 1kla2";
    private String                 vehicleMake            = "Tesla";
    private String                 vehicleModel           = "Model S";
    private State                  vehicleState           = State.Idaho;
    private String                 vehicleVin             = "32a1sd65f11651d51d3216";
    private String                 vehicleYear            = "2015";
    
    private repository.gw.generate.cc.BackendIncidentDetails incidentDetails        = new repository.gw.generate.cc.BackendIncidentDetails();
    private ArrayList<BackendPeople>      peopleList             = new ArrayList<BackendPeople>();

    public ArrayList<BackendPeople> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(ArrayList<BackendPeople> peopleList) {
        this.peopleList = peopleList;
    }

    public repository.gw.generate.cc.BackendIncidentDetails getIncidentDetails() {
        return incidentDetails;
    }

    public void setIncidentDetails(repository.gw.generate.cc.BackendIncidentDetails incidentDetails) {
        this.incidentDetails = incidentDetails;
    }

    public String getVehicleNameToSelect() {
        return vehicleNameToSelect;
    }

    public void setVehicleNameToSelect(String vehicleNameToSelect) {
        this.vehicleNameToSelect = vehicleNameToSelect;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleLicensePlateNum() {
        return vehicleLicensePlateNum;
    }

    public void setVehicleLicensePlateNum(String vehicleLicensePlateNum) {
        this.vehicleLicensePlateNum = vehicleLicensePlateNum;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public State getVehicleState() {
        return vehicleState;
    }

    public void setVehicleState(State vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public BackendVehicle() {

    }

    // Used for creating a new vehicle.
    public BackendVehicle(String vehicleNameToSelect, String vehicleColor, String vehicleLicensePlateNum, String vehicleMake, String vehicleModel, State vehicleState, String vehicleVin, String vehicleYear, repository.gw.generate.cc.BackendIncidentDetails incidentDetails, ArrayList<BackendPeople> peopleList) {
        this.vehicleNameToSelect = vehicleNameToSelect;
        this.vehicleColor = vehicleColor;
        this.vehicleLicensePlateNum = vehicleLicensePlateNum;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleState = vehicleState;
        this.vehicleVin = vehicleVin;
        this.vehicleYear = vehicleYear;
        this.incidentDetails = incidentDetails;
        this.peopleList = peopleList;
    }

    // Used to Select a Vehicle that already exists.
    public BackendVehicle(String vehicleNameToSelect, BackendIncidentDetails incidentDetails, ArrayList<BackendPeople> peopleList) {
        this.vehicleNameToSelect = vehicleNameToSelect;
        
        String[] splitVehicleNameParts = StringUtils.split(vehicleNameToSelect);
        this.vehicleColor = "";
        this.vehicleLicensePlateNum = "";
        this.vehicleMake = splitVehicleNameParts[0];
        this.vehicleModel = splitVehicleNameParts[1];
        this.vehicleYear = splitVehicleNameParts[2];
        this.vehicleState = null;
        this.vehicleVin = "";
        this.incidentDetails = incidentDetails;
        this.peopleList = peopleList;
    }
}
