package repository.cc.claim;

import com.idfbins.enums.State;
import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PointOfFirstImpact;
import repository.gw.generate.cc.BackendPeople;
import repository.gw.generate.cc.BackendVehicle;
import repository.gw.generate.cc.PropertyIncidentDetails;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ClaimInfoValidation extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ClaimInfoValidation(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // Reporter/Contact Info
    private String contactFirstName = "";
    private String contactLastName = "";
    private String contactAddressLine1 = "";
    private String contactAddressLine2 = "";
    private String contactCity = "";
    private String contactZip = "";
    private State contactState = null;
    private String contactCell = "";
    private String contactHome = "";
    private String contactWork = "";
    private String contactEmail = "";

    // Where Did this happen details.

    private String whereDidItHappenAddressLine1 = "";
    private String whereDidItHappenAddressLine2 = "";
    private String whereDidItHappenCity = "";
    private String whereDidItHappenZip = "";
    private State whereDidItHappenState = null;
    private String describeWhatHappened = "";

    // Vehicle Validation

    private String vehicleNameToSelect = "";
    private String vehicleColor = "";
    private String vehicleLicensePlateNum = "";
    private String vehicleMake = "";
    private String vehicleModel = "";
    private State vehicleState = null;
    private String vehicleVin = "";
    private String vehicleYear = "";

    private YesOrNo wasVehicleParked = null;
    private YesOrNo safeToDrive = null;
    private YesOrNo didAirbagDeploy = null;
    private YesOrNo equipmentFailure = null;
    private YesOrNo vehicleTowed = null;
    private String whereVehicleWasTowed = "";
    private YesOrNo rentalCarNeed = null;
    private PointOfFirstImpact poi = null;

    private ArrayList<BackendVehicle> backendVehicles = new ArrayList<>();

    private ArrayList<PropertyIncidentDetails> properties = new ArrayList<>();

    private ArrayList<BackendPeople> injuredParties = new ArrayList<>();

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactAddressLine1() {
        return contactAddressLine1;
    }

    public void setContactAddressLine1(String contactAddressLine1) {
        this.contactAddressLine1 = contactAddressLine1;
    }

    public String getContactAddressLine2() {
        return contactAddressLine2;
    }

    public void setContactAddressLine2(String contactAddressLine2) {
        this.contactAddressLine2 = contactAddressLine2;
    }

    public String getContactCity() {
        return contactCity;
    }

    public void setContactCity(String contactCity) {
        this.contactCity = contactCity;
    }

    public String getContactZip() {
        return contactZip;
    }

    public void setContactZip(String contactZip) {
        this.contactZip = contactZip;
    }

    public State getContactState() {
        return contactState;
    }

    public void setContactState(State contactState) {
        this.contactState = contactState;
    }

    public String getContactCell() {
        return contactCell;
    }

    public void setContactCell(String contactCell) {
        this.contactCell = contactCell;
    }

    public String getContactHome() {
        return contactHome;
    }

    public void setContactHome(String contactHome) {
        this.contactHome = contactHome;
    }

    public String getContactWork() {
        return contactWork;
    }

    public void setContactWork(String contactWork) {
        this.contactWork = contactWork;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWhereDidItHappenAddressLine1() {
        return whereDidItHappenAddressLine1;
    }

    public void setWhereDidItHappenAddressLine1(String whereDidItHappenAddressLine1) {
        this.whereDidItHappenAddressLine1 = whereDidItHappenAddressLine1;
    }

    public String getWhereDidItHappenAddressLine2() {
        return whereDidItHappenAddressLine2;
    }

    public void setWhereDidItHappenAddressLine2(String whereDidItHappenAddressLine2) {
        this.whereDidItHappenAddressLine2 = whereDidItHappenAddressLine2;
    }

    public String getWhereDidItHappenCity() {
        return whereDidItHappenCity;
    }

    public void setWhereDidItHappenCity(String whereDidItHappenCity) {
        this.whereDidItHappenCity = whereDidItHappenCity;
    }

    public String getWhereDidItHappenZip() {
        return whereDidItHappenZip;
    }

    public void setWhereDidItHappenZip(String whereDidItHappenZip) {
        this.whereDidItHappenZip = whereDidItHappenZip;
    }

    public State getWhereDidItHappenState() {
        return whereDidItHappenState;
    }

    public void setWhereDidItHappenState(State whereDidItHappenState) {
        this.whereDidItHappenState = whereDidItHappenState;
    }

    public String getDescribeWhatHappened() {
        return describeWhatHappened;
    }

    public void setDescribeWhatHappened(String describeWhatHappened) {
        this.describeWhatHappened = describeWhatHappened;
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

    public YesOrNo getWasVehicleParked() {
        return wasVehicleParked;
    }

    public void setWasVehicleParked(YesOrNo wasVehicleParked) {
        this.wasVehicleParked = wasVehicleParked;
    }

    public YesOrNo getSafeToDrive() {
        return safeToDrive;
    }

    public void setSafeToDrive(YesOrNo safeToDrive) {
        this.safeToDrive = safeToDrive;
    }

    public YesOrNo getDidAirbagDeploy() {
        return didAirbagDeploy;
    }

    public void setDidAirbagDeploy(YesOrNo didAirbagDeploy) {
        this.didAirbagDeploy = didAirbagDeploy;
    }

    public YesOrNo getEquipmentFailure() {
        return equipmentFailure;
    }

    public void setEquipmentFailure(YesOrNo equipmentFailure) {
        this.equipmentFailure = equipmentFailure;
    }

    public YesOrNo getVehicleTowed() {
        return vehicleTowed;
    }

    public void setVehicleTowed(YesOrNo vehicleTowed) {
        this.vehicleTowed = vehicleTowed;
    }

    public String getWhereVehicleWasTowed() {
        return whereVehicleWasTowed;
    }

    public void setWhereVehicleWasTowed(String whereVehicleWasTowed) {
        this.whereVehicleWasTowed = whereVehicleWasTowed;
    }

    public YesOrNo getRentalCarNeed() {
        return rentalCarNeed;
    }

    public void setRentalCarNeed(YesOrNo rentalCarNeed) {
        this.rentalCarNeed = rentalCarNeed;
    }

    public PointOfFirstImpact getPoi() {
        return poi;
    }

    public void setPoi(PointOfFirstImpact poi) {
        this.poi = poi;
    }

    public ArrayList<BackendVehicle> getBackendVehicles() {
        return backendVehicles;
    }

    public void setBackendVehicles(ArrayList<BackendVehicle> backendVehicles) {
        this.backendVehicles = backendVehicles;
    }

    public ArrayList<PropertyIncidentDetails> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<PropertyIncidentDetails> properties) {
        this.properties = properties;
    }

    public ArrayList<BackendPeople> getInjuredParties() {
        return injuredParties;
    }

    public void setInjuredParties(ArrayList<BackendPeople> partiesInvolved) {
        this.injuredParties = partiesInvolved;
    }


    public void gatherReporterInfoMation(ClaimInfoValidation claim) {

        repository.cc.claim.PartiesInvolvedContacts partiesInvolved = new repository.cc.claim.PartiesInvolvedContacts(this.driver);

        System.out.println(partiesInvolved.getFirstNameValue());
        claim.setContactFirstName(partiesInvolved.getFirstNameValue());

        System.out.println(partiesInvolved.getLastNameValue());
        claim.setContactLastName(partiesInvolved.getLastNameValue());

        System.out.println(partiesInvolved.getAddressLineOneValue());
        claim.setContactAddressLine1(partiesInvolved.getAddressLineOneValue());

        System.out.println(partiesInvolved.getAddressLineTwoValue());
        claim.setContactAddressLine2(partiesInvolved.getAddressLineTwoValue());

        System.out.println(partiesInvolved.getCityValue());
        claim.setContactCity(partiesInvolved.getCityValue());

        System.out.println(partiesInvolved.getStateValue());
        claim.setContactState(State.valueOf(partiesInvolved.getStateValue()));

        System.out.println(partiesInvolved.getZipCodeValue());
        claim.setContactZip(partiesInvolved.getZipCodeValue());

        System.out.println(partiesInvolved.getHomePhoneValue());
        claim.setContactHome(partiesInvolved.getHomePhoneValue());

        System.out.println(partiesInvolved.getWorkPhoneValue());
        claim.setContactWork(partiesInvolved.getWorkPhoneValue());

        System.out.println(partiesInvolved.getCellPhoneValue());
        claim.setContactCell(partiesInvolved.getCellPhoneValue());

        System.out.println(partiesInvolved.getEmailValue());
        claim.setContactEmail(partiesInvolved.getEmailValue());
    }

    public void gatherClaimLocationAndDescription(ClaimInfoValidation claim) {
        LossDetails lossDetails = new LossDetails(this.driver);
        waitForPageLoad();
        System.out.println(lossDetails.getLlAddressLineOne());
        claim.setWhereDidItHappenAddressLine1(lossDetails.getLlAddressLineOne());
//        System.out.println(lossDetails.getLlAddressLineTwo());
//        claim.setWhereDidItHappenAddressLine2(lossDetails.getLlAddressLineTwo());
        System.out.println(lossDetails.getLlCity());
        claim.setWhereDidItHappenCity(lossDetails.getLlCity());
        System.out.println(lossDetails.getLlState());
        claim.setWhereDidItHappenState(State.valueOf(lossDetails.getLlState()));
        System.out.println(lossDetails.getLlZipCode());
        claim.setWhereDidItHappenZip(lossDetails.getLlZipCode());
        System.out.println(lossDetails.getLlLossDescription());
        claim.setDescribeWhatHappened(lossDetails.getLlLossDescription());
    }

    public void gatherPropertyIncidents(ClaimInfoValidation claim) {
        new TopMenu(this.driver);
        new SideMenuCC(this.driver);
        new repository.cc.claim.PartiesInvolvedContacts(this.driver);
        new LossDetails(this.driver);
        repository.cc.claim.Incidents incidentsPage = new repository.cc.claim.Incidents(this.driver);

//        String propertyTableXPath = "//div[@id='ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:Claim_Properties:EditableFixedPropertyIncidentsLV']";
//        int propertyIncidents = gwtableUtils.getRowCount(find(By.xpath(propertyTableXPath)));
        List<WebElement> links = finds(By.cssSelector("a[id*='EditableFixedPropertyIncidentsLV'][id$='Address1']"));

        for (int i = 0; i < links.size(); i++) {

            clickWhenClickable(links.get(i));

            PropertyIncidentDetails property = new PropertyIncidentDetails();

            incidentsPage.clickEditButton();

            property.setPropertyDescription(incidentsPage.getPropertyDescription());
            property.setDamageDescription(incidentsPage.getPropertyDamageDescription());
            property.setAddressLine1(incidentsPage.getPropertyAddressLine1());
            property.setAddressLine2("");
            property.setCity(incidentsPage.getPropertyCity());
            property.setState(incidentsPage.getPropertyState());
            property.setZipCode(incidentsPage.getPropertyZipCode());

            incidentsPage.clickCancelButton();
            properties.add(property);
        }
        claim.setProperties(properties);
    }

    public void gatherInjuredPeople(ClaimInfoValidation claim) {
        new TopMenu(this.driver);
        new SideMenuCC(this.driver);
        new PartiesInvolvedContacts(this.driver);
        new LossDetails(this.driver);
        repository.cc.claim.Incidents incidentsPage = new Incidents(this.driver);
        String injuredTableXpath = "//div[@id='ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableInjuryIncidentsLV']";
        int injuredIncidents = tableUtils.getRowCount(find(By.xpath(injuredTableXpath)));

        for (int i = 0; i < injuredIncidents; i++) {
            tableUtils.clickLinkInSpecficRowInTable(find(By.xpath(injuredTableXpath)), i + 1);
            BackendPeople injuredPerson = new BackendPeople();

            incidentsPage.clickEditButton();
            injuredPerson.setNameInDropdown(incidentsPage.getInjuredPerson());
            injuredPerson.setInjured(YesOrNo.Yes);
            injuredParties.add(injuredPerson);

            incidentsPage.clickCancelButton();
        }
        claim.setInjuredParties(injuredParties);
    }

}
