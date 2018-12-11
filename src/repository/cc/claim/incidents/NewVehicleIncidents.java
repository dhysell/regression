package repository.cc.claim.incidents;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.searchpages.SearchAddressBook;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;

public class NewVehicleIncidents extends BasePage {

    private WebDriver driver;

    public NewVehicleIncidents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // NEW VEHICLE INCIDENT ELEMENTS

    private Guidewire8Select selectVehicle() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Vehicle_Picker-triggerWrap')]");
    }

    private void setSelectVehicle(String selection) {
        selectVehicle().selectByVisibleText(selection);
    }

    private Guidewire8Select selectLossParty() {
        return new Guidewire8Select(driver,"//table[contains(@id,':VehicleIncidentDV:LossParty-triggerWrap')]");
    }

    private void setLossParty(String selection) {
        selectLossParty().selectByVisibleText(selection);
    }

    private Guidewire8Select selectVehicleType() {
        return new Guidewire8Select(driver,"//table[contains(@id,':VehicleIncidentDV:Vehicle_VehicleType-triggerWrap')]");
    }

    private void setVehicleType(String selection) {
        selectVehicleType().selectByVisibleText(selection);
    }

    private Guidewire8Select selectMake() {
        return new Guidewire8Select(driver,"//table[contains(@id,':VehicleIncidentDV:Vehicle_Make-triggerWrap')]");
    }

    private void setMake(String selection) {
        selectMake().selectByVisibleText(selection);
    }

    private Guidewire8Select selectOwner() {
        return new Guidewire8Select(driver,"//table[contains(@id,':VehicleIncidentDV:Owner_Picker-triggerWrap')]");
    }

    private void setOwner(String selection) {
        selectOwner().selectByVisibleText(selection);
    }

    private Guidewire8Select selectDriverName() {
        return new Guidewire8Select(driver,"//table[contains(@id,':VehicleIncidentDV:Driver_Picker-triggerWrap')]");
    }

    private void setDriverName(String selection) {
        selectDriverName().selectByVisibleText(selection);
    }

    private String getDriverName() {
        String name = selectDriverName().getText();
        selectDriverName().sendKeys(Keys.TAB);
        return name;
    }

    @FindBy(xpath = "//input[contains(@id,':VehicleIncidentDV:Vehicle_Year-inputEl')]")
    WebElement inputYear;

    private void setYear(String input) {
        waitUntilElementIsClickable(inputYear).sendKeys(input);
    }

    @FindBy(xpath = "//input[contains(@id,':VehicleIncidentDV:Vehicle_Model-inputEl')]")
    WebElement inputModel;

    private void setModel(String input) {
        inputModel.sendKeys(input);
    }

    @FindBy(xpath = "//a[contains(@id,':VehicleIncidentDV:Driver_Picker:Driver_PickerMenuIcon')]")
    WebElement pickerDriverName;

    private void clickDriverNamePicker() {
        clickWhenClickable(pickerDriverName);
    }

    @FindBy(xpath = "//a[contains(@id,':VehicleIncidentDV:Driver_Picker:MenuItem_Search-itemEl')]")
    WebElement linkDriverNamePickerSearch;

    private void clicklinkDriverNamePickerSearch() {
        clickWhenClickable(linkDriverNamePickerSearch);
    }

    @FindBy(xpath = "//textarea[contains(@id,':VehicleIncidentDV:Description-inputEl')]")
    WebElement textAreaDamageDescription;

    private void setDamageDescription(String input) {
        textAreaDamageDescription.sendKeys(input);
    }

    @FindBy(xpath = "//a[contains(@id,':NewVehicleIncidentScreen:Update')]")
    WebElement buttonOk;

    private void clickOkButton() {
        clickWhenClickable(buttonOk);
    }

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    private WebElement insuredNameElement;

    private String getIsuredName() {
        return insuredNameElement.getText();
    }

    public void thirdPartyVehicle() {

        int stamp = DateUtils.getCenterDateAsInt(getDriver(), ApplicationOrCenter.ClaimCenter, "sSSS");

        setSelectVehicle("New...");
        
        setLossParty("Third-party liability");
        
        setVehicleType("Owned");
        
        setYear("2016");
        
        setMake("TESLA");
        
        setModel("Model X " + stamp + "");
        

        clickDriverNamePicker();
        clicklinkDriverNamePickerSearch();

        SearchAddressBook searchAB = new SearchAddressBook(this.driver);

        

        searchAB.setName("Ball Anthony E");
        searchAB.clickSearchButton();
        searchAB.selectFirstResult();

        waitUntilElementIsClickable(By.cssSelector("textarea[id$='VehicleIncidentDV:Description-inputEl'"),30);
        String driver = getDriverName();

        setOwner(driver);
        
        setDamageDescription("Third Party Vehicle Incident.");
        
        clickOkButton();
    }

    public void vehicle() {

        int stamp = DateUtils.getCenterDateAsInt(getDriver(), ApplicationOrCenter.ClaimCenter, "sSSS");

        setSelectVehicle("New...");
        
        setLossParty("Insured's loss");
        
        setVehicleType("Owned");
        
        setYear("2016");
        
        setMake("TESLA");
        
        setModel("Model X " + stamp + "");
        

        String insuredName = getIsuredName();
        setDriverName(insuredName);

        
        setDamageDescription("New Vehicle Incident.");
        
        clickOkButton();
    }
}
