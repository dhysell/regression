package repository.pc.workorders.generic;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.SRPIncident;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireAutoDrivers_SelfReportingIncidents extends GenericWorkorderSquireAutoDrivers {
	
	private WebDriver driver;

    public GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutSelfReportingIncidents_QQ(Contact driver) {

    }

    public void fillOutSelfReportingIncidents_FA(Contact driver) {

    }

    public void fillOutSelfReportingIncidents(Contact driver) {

    }


    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:0:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsALS;

    private void setALSIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsALS, occurance);
    }

    @FindBy(xpath = "//div[contains(@id, 'SelfReportedIncidentsTabDV:srp-inputEl') or contains(@id, 'SelfReportedIncidentsTabDV:OverriddenSRP-inputEl')]")
    private WebElement editbox_SRPValue;

    public String getSRPValue() {
        return editbox_SRPValue.getText();
    }


    private void setSRP(WebElement element, int value) {
        clickWhenClickable(element);
        element.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        element.sendKeys(String.valueOf(value));
    }


    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:4:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSpeed;

    private void setSpeedIncident(int speed) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSpeed, speed);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:6:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnpaidInfraction;

    private void setUnpaidInfraction(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnpaidInfraction, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:8:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversInattentiveDriving;

    private void setInattentiveDriving(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversInattentiveDriving, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:5:incidentRowColumnOne')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsTrafficSignal;

    private void setTrafficSignalIncidents(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsTrafficSignal, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:10:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsNegligentDriving;

    private void setNegligentDriving(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsNegligentDriving, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:2:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRacing;

    private void setDragRacingIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRacing, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:6:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsEludingPolice;

    private void setPoliceIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsEludingPolice, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:2:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRecklessDriving;

    private void setRecklessDriving(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRecklessDriving, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:3:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUI;

    private void setDUIIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUI, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:0:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsOther;

    private void setOtherIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsOther, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:5:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUIJudgement;

    private void setDUIJudgementIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUIJudgement, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:1:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsChemicalTestRefused;

    private void setChemicalIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsChemicalTestRefused, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:3:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRevoked;

    private void setRevokedIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRevoked, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:4:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDrivingWithoutLicense;

    private void setNoDLIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDrivingWithoutLicense, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:8:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsInternationalDL;

    public void setInternationalDLIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsInternationalDL, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:1:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSchoolBus;

    private void setSchoolBusIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSchoolBus, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:9:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsLeavingAccident;

    private void setLeavingAccidentIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsLeavingAccident, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:7:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsHomicide;

    private void setVehicularHomicideIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsHomicide, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnOne:7:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsManslaughter;

    private void setFelonyManslaughterIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsManslaughter, occurance);
    }

    @FindBy(xpath = "//*[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:10:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnverifiableDrivingRecord;

    public void setUnverifiableDrivingRecordIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnverifiableDrivingRecord, occurance);
    }

    @FindBy(xpath = "//input[contains(@id, 'SelfReportedIncidentsTabDV:TypekeyColumnTwo:9:incidentRowColumnOne-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsProofInsurance;

    public void setNoProofInsuranceIncident(int occurance) {
        setSRP(editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsProofInsurance, occurance);
    }


    public void setSRPIncident(SRPIncident incident, int value) {
        switch (incident) {
            case Speed:
                setSpeedIncident(value);
                break;
            case UnpaidInfraction:
                setUnpaidInfraction(value);
                break;
            case InattentiveDriving:
                setInattentiveDriving(value);
                break;
            case StopOrYield:
                setTrafficSignalIncidents(value);
                break;
            case ALS:
                setALSIncident(value);
                break;
            case NegligentDriving:
                setNegligentDriving(value);
                break;
            case DragRacing:
                setDragRacingIncident(value);
                break;
            case EludingPolice:
                setPoliceIncident(value);
                break;
            case RecklessDriving:
                setRecklessDriving(value);
                break;
            case DUI:
                setDUIIncident(value);
                break;
            case DUIWithheld:
                setDUIJudgementIncident(value);
                break;
            case ChemicalTestRefused:
                setChemicalIncident(value);
                break;
            case RevokedSuspendedLicense:
                setRevokedIncident(value);
                break;
            case DrivingWithoutLicense:
                setNoDLIncident(value);
                break;
            case International:
                setInternationalDLIncident(value);
                break;
            case PassingSchoolBus:
                setSchoolBusIncident(value);
                break;
            case LeavingAccident:
                setLeavingAccidentIncident(value);
                break;
            case VehicularHomicide:
                setVehicularHomicideIncident(value);
                break;
            case FelonyManslaughter:
                setFelonyManslaughterIncident(value);
                break;
            case UnverifiableDrivingRecord:
                setUnverifiableDrivingRecordIncident(value);
                break;
            case NoProof:
                setNoProofInsuranceIncident(value);
                break;
            case Other:
                setOtherIncident(value);
                break;
            default:
                break;
        }
    }

    public int getSRPIncident(SRPIncident incident) {
        String value;
        switch (incident) {
            case Speed:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSpeed.getText();
                break;
            case UnpaidInfraction:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnpaidInfraction.getText();
                break;
            case InattentiveDriving:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversInattentiveDriving.getText();
                break;
            case StopOrYield:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsTrafficSignal.getText();
                break;
            case ALS:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsALS.getText();
                break;
            case NegligentDriving:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsNegligentDriving.getText();
                break;
            case DragRacing:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRacing.getText();
                break;
            case EludingPolice:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsEludingPolice.getText();
                break;
            case RecklessDriving:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRecklessDriving.getText();
                break;
            case DUI:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUI.getText();
                break;
            case DUIWithheld:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUIJudgement.getText();
                break;
            case ChemicalTestRefused:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsChemicalTestRefused.getText();
                break;
            case RevokedSuspendedLicense:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRevoked.getText();
                break;
            case DrivingWithoutLicense:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDrivingWithoutLicense.getText();
                break;
            case International:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsInternationalDL.getText();
                break;
            case PassingSchoolBus:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSchoolBus.getText();
                break;
            case LeavingAccident:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsLeavingAccident.getText();
                break;
            case VehicularHomicide:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsHomicide.getText();
                break;
            case FelonyManslaughter:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsManslaughter.getText();
                break;
            case UnverifiableDrivingRecord:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnverifiableDrivingRecord.getText();
                break;
            case NoProof:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsProofInsurance.getText();
                break;
            case Other:
                value = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsOther.getText();
                break;
            default:
                value = "0";
                break;
        }

        if (value == null || value.equals("")) {
            value = "0";
        }

        return Integer.parseInt(value);
    }

    public boolean checkSRPFieldsEditableQQ() {

        boolean editable = false;
        boolean value1, value2, value3, value4, value5, value6, value7, value8, value9, value10, value11, value12, value13;
        boolean value14, value15, value16, value17, value18, value19, value20, value21, value22;

        value1 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSpeed.isEnabled();
        value2 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnpaidInfraction.isEnabled();
        value3 = editbox_GenericWorkorderPolicyPersonalAutoDriversInattentiveDriving.isEnabled();
        value4 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsTrafficSignal.isEnabled();
        value5 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsALS.isEnabled();
        value6 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsNegligentDriving.isEnabled();
        value7 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRacing.isEnabled();
        value8 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsEludingPolice.isEnabled();
        value9 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRecklessDriving.isEnabled();
        value10 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUI.isEnabled();
        value11 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDUIJudgement.isEnabled();
        value12 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsChemicalTestRefused.isEnabled();
        value13 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsRevoked.isEnabled();
        value14 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsDrivingWithoutLicense.isEnabled();
        value15 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsInternationalDL.isEnabled();
        value16 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsSchoolBus.isEnabled();
        value17 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsLeavingAccident.isEnabled();
        value18 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsHomicide.isEnabled();
        value19 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsManslaughter.isEnabled();
        value20 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnverifiableDrivingRecord.isEnabled();
        value21 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsProofInsurance.isEnabled();
        value22 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsOther.isEnabled();


        editable = value1 || value2 || value3 || value4 || value5 || value6 || value7 || value8 || value9 || value10 || value11 || value12 || value13 || value14 || value15 || value16 || value17 || value18 || value19 || value20 || value21 || value22;


        if (editable)
            return editable;

        return editable;
    }


    public boolean checkSRPFieldsEditableFA() {

        boolean editable = false;
        boolean value15, value21, value20;

        value15 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsInternationalDL.isEnabled();
        value20 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsUnverifiableDrivingRecord.isEnabled();
        value21 = editbox_GenericWorkorderPolicyPersonalAutoDriversIncidentsProofInsurance.isEnabled();

        editable = value15 || value21 || value20;

        if (editable)
            return editable;

        return editable;
    }


    private Guidewire8Checkbox checkBox_OverrideSRP() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'SelfReportedIncidentsTabDV:OverrideSRPCheckBox')]");
    }

    public void setOverrideSRPCheckbox(boolean trueFalseChecked) {
        checkBox_OverrideSRP().select(trueFalseChecked);
    }

    @FindBy(xpath = "//input[contains(@id, 'SelfReportedIncidentsTabDV:reasonForOverrideTextBox-inputEl')]")
    private WebElement editbox_ReasonForSRPOverride;

    public void setReasonForSRPOverride(String reason) {
        clickWhenClickable(editbox_ReasonForSRPOverride);
        editbox_ReasonForSRPOverride.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ReasonForSRPOverride.sendKeys(reason);
    }

    @FindBy(xpath = "//input[contains(@id, 'SelfReportedIncidentsTabDV:OverriddenSRP-inputEl')]")
    private WebElement editbox_SRP;

    public void setSRP(int srpValue) {
        clickWhenClickable(editbox_SRP);
        editbox_SRP.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SRP.sendKeys(String.valueOf(srpValue));
    }

    @FindBy(xpath = "//div[contains(@id, 'PADriversPanelSet:DriversListDetailPanel:DriversLV')]")
    private WebElement table_driverDetails;

    public int getSRPValue(String driver) {
        int srpValue = Integer.valueOf(new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_driverDetails, new TableUtils(getDriver()).getRowInTableByColumnNameAndValue(table_driverDetails, "Name", driver), "SRP"));
        return srpValue;
    }


}


















