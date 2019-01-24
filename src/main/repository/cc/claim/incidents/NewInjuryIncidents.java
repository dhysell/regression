package repository.cc.claim.incidents;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.EditPerson;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.ClaimHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class NewInjuryIncidents extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public NewInjuryIncidents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select selectBoxInjuredPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Injured_Picker-triggerWrap')]");
    }

    public Guidewire8Select selectBoxAreaOfBody(String selectString) {
        return new Guidewire8Select( driver, selectString);
    }

    private void focusInjuryIncidentAreaOfBody(String selectString) {
        selectBoxAreaOfBody(selectString).sendKeys(Keys.ENTER);
    }

    private String setInjuryIncidentAreaOfBodyRandom(String selectString) {
        String selection = selectBoxAreaOfBody(selectString).selectByVisibleTextRandom();
        return selection;
    }

    private Guidewire8Select selectDetailedBodyPart(String selectString) {
        return new Guidewire8Select(driver, "" + selectString + "");
    }

    private void focusInjuryIncidentDetailedBodyPart(String selectString) {
        selectDetailedBodyPart(selectString).sendKeys(Keys.ENTER);
    }

    private String setInjuryIncidentDetailedBodyPartRandom(String selectString) {
        String selecion = selectDetailedBodyPart(selectString).selectByVisibleTextRandom();
        return selecion;
    }


    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    private WebElement elementInsuredName;

    @FindBy(xpath = "//textarea[contains(@id,':InjuryDescription-inputEl')]")
    private WebElement textAreaDescribeInjuries;

    @FindBy(xpath = "//a[contains(@id,':EditableBodyPartDetailsLV_tb:Add')]")
    private WebElement buttonAddBodyParts;

    private void clickAddBodyParts() {
        clickWhenClickable(buttonAddBodyParts);
    }

    @FindBy(xpath = "//div[contains(@id,':EditableBodyPartDetailsLV-body')]/div/table/tbody/tr/td[2]")
    private WebElement elementAreaOfBodyCell;

    private void clickAreaOfBodyCell() {
        clickWhenClickable(elementAreaOfBodyCell);
        
    }

    @FindBy(xpath = "//span[contains(text(),'Injury Incident')]")
    private WebElement elementInjuryIncidentPageHeader;

    private void clickInjuryIncidentPageHeader() {
        clickWhenClickable(elementInjuryIncidentPageHeader);
    }

    @FindBy(xpath = "//div[contains(@id,':EditableBodyPartDetailsLV-body')]/div/table/tbody/tr/td[3]")
    private WebElement elementDetailedBodyPartCell;

    private void clickDetailedBodyPartCell() {
        clickWhenClickable(elementDetailedBodyPartCell);
        
    }

    @FindBy(xpath = "//a[contains(@id,':Update')]")
    private WebElement buttonOk;

    private void clickOkButton() {
        clickWhenClickable(buttonOk);
    }

    @FindBy(xpath = "//a[contains(@id,':Injured_Picker:Injured_PickerMenuIcon')]")
    public WebElement pickerInjuredPerson;

    private void clickInjuredPersonPicker() {
        Actions build = new Actions(this.driver);
        
        build.moveToElement(pickerInjuredPerson).click().build().perform();
    }

    private Guidewire8Select selectInjuryIncidentSeverity() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Severity-triggerWrap')]");
    }

    private String setInjuryIncidentSeverityRandom() {
        String selection = selectInjuryIncidentSeverity().selectByVisibleTextRandom();
        return selection;
    }

    @FindBy(xpath = "//span[contains(text(),'View Contact Details')]")
    private WebElement linkViewContactDetails;

    private repository.cc.claim.EditPerson clickViewContactDetails() {
        clickWhenClickable(linkViewContactDetails);
        return new EditPerson(this.driver);
    }

    @FindBy(xpath = "//textArea[contains(@id,':InjuryDescription-inputEl')]")
    public WebElement textAreaInjuryIncidentDescribeInjuries;

    private void setInjuryIncidentDescribeInjuriesTextArea(String inputString) {
        textAreaInjuryIncidentDescribeInjuries.sendKeys(inputString);
    }

    private Guidewire8Select select_InjuryIncidentGeneralInjuryType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PrimaryInjuryType-triggerWrap')]");
    }

    private String setInjuryIncidentGeneralInjuryTypeRandom() {
        String selection = select_InjuryIncidentGeneralInjuryType().selectByVisibleTextRandom();
        return selection;
    }

    private Guidewire8Select selectInjuryIncidentDetailedInjuryType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':DetailedInjuryType-triggerWrap')]");
    }

    private String setInjuryIncidentDetailedInjuryTypeRandom() {
        String selection = selectInjuryIncidentDetailedInjuryType().selectByVisibleTextRandom();
        return selection;
    }

    @FindBy(xpath = "//span[contains(text(),'Injury Incident')]")
    private WebElement pageHeader;

    private Guidewire8Select selectFarmProductsProduced() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Liability_Subcause-triggerWrap')]");
    }

    private void setFarmProductsProducedRandom() {
        List<String> options = selectFarmProductsProduced().getList();
        selectFarmProductsProduced().selectByVisibleText(options.get(NumberUtils.generateRandomNumberInt(0, options.size() - 1)));
    }

    public repository.cc.claim.incidents.InjuryIncident newInjury(boolean isICDtest) {

        waitUntilElementIsVisible(By.cssSelector("input[id$='InjuryIncidentDV:Injured_Picker-inputEl']"), 20);

        // Variables
        String tableXpath = "";
        String tableID = "";

        // Create new Injury Incident object.
        repository.cc.claim.incidents.InjuryIncident injuryIncident = new InjuryIncident();

        // Set Injured Party
        String injuredParty = ClaimHelpers.setDynamicSelectBoxRestricted(selectBoxInjuredPerson(), "<none>");
        injuryIncident.setInjuredPerson(injuredParty);

        clickInjuredPersonPicker();
        clickViewContactDetails().validateContact();

        waitUntilElementIsClickable(By.cssSelector("textarea[id$=':InjuryDescription-inputEl']"));
        // Set Injury Severity
        String severity = setInjuryIncidentSeverityRandom();
        injuryIncident.setSeverity(severity);

        // Set Description
        injuryIncident.setInjuryDescription("Test Describe Injuries Text Area");
        setInjuryIncidentDescribeInjuriesTextArea(injuryIncident.getInjuryDescription());

        // Set Injury Details
        String generalInjuryType = setInjuryIncidentGeneralInjuryTypeRandom();
        injuryIncident.setGeneralInjuryType(generalInjuryType);
        String detailedInjuryType = setInjuryIncidentDetailedInjuryTypeRandom();
        injuryIncident.setDetailedInjuryType(detailedInjuryType);

        clickAddBodyParts();

        // Locate ComboBox in table, select random item
        clickAreaOfBodyCell();

        tableID = find(By.xpath("//table[starts-with(@id,'simplecombo-')]")).getAttribute("id");
        tableXpath = "//table[starts-with(@id,'" + tableID + "')]";
        

        focusInjuryIncidentAreaOfBody(tableXpath);
        
        String areaOfbody = setInjuryIncidentAreaOfBodyRandom(tableXpath);
        injuryIncident.setAreaOfBody(areaOfbody);
        

        // Focus the page
        clickInjuryIncidentPageHeader();

        // Locate ComboBox in table, select random item
        clickDetailedBodyPartCell();

        List<WebElement> hiddenComboBoxList = finds(By.xpath("//table[starts-with(@id,'simplecombo-')]"));
        tableID = hiddenComboBoxList.get(2).getAttribute("id");
        tableXpath = "//table[starts-with(@id,'" + tableID + "')]";
        

        focusInjuryIncidentDetailedBodyPart(tableXpath);
        
        String detailedBodyPart = setInjuryIncidentDetailedBodyPartRandom(tableXpath);
        injuryIncident.setDetailedBodyPart(detailedBodyPart);
        

        // Check and Set Farm Products Produced select box if it is visible.
        List<WebElement> farmProductsProduced = finds(By.cssSelector("table[id*='Liability_Subcause-triggerWrap']"));
        if (farmProductsProduced != null && farmProductsProduced.size() > 0) {
            setFarmProductsProducedRandom();
        }

        if (isICDtest) {
            icdCodeValidation();
        }

        
        clickOkButton();
        

        return injuryIncident;
    }

    @FindBy(css = "a[id*=':MedicalDiagnosisLV_tb:Add']")
    private WebElement addMedicalDiagnosisbutton;

    private void clickAddMedicalDiagnosis() {
        this.waitUtils.waitUntilElementIsClickable(addMedicalDiagnosisbutton);
        addMedicalDiagnosisbutton.click();
    }

    private String icdCodeValidation() {

        clickAddMedicalDiagnosis();
        
        find(By.cssSelector("div[id*=':MedicalDiagnosisLVInput:MedicalDiagnosisLV-body'] div table tbody tr td:nth-child(2)")).click();
        find(By.cssSelector("div[id*=':ICDCode:SelectICDCode']")).click();

        return null;
    }

    /*private Guidewire8Select farmProductsProduced() {
    	return new Guidewire8Select("//input[@id='NewInjuryIncidentPopup:NewInjuryIncidentScreen:InjuryIncidentDV:Liability_Subcause-inputEl']",driver);
    }*/

    @FindBy(xpath = "//input[@id='NewInjuryIncidentPopup:NewInjuryIncidentScreen:InjuryIncidentDV:Liability_Subcause-inputEl']")
    private WebElement inputFarmProductsProduced;

    public InjuryIncident newBasicInjuryIncident() {

        waitUntilElementIsVisible(By.cssSelector("input[id$='InjuryIncidentDV:Injured_Picker-inputEl']"), 20);

        // Variables
        String tableXpath = "";
        String tableID = "";

        // Create new Injury Incident object.
        repository.cc.claim.incidents.InjuryIncident injuryIncident = new InjuryIncident();

        // Set Injured Party
        String injuredParty = ClaimHelpers.setDynamicSelectBoxRestricted(selectBoxInjuredPerson(), "<none>");
        injuryIncident.setInjuredPerson(injuredParty);

        clickInjuredPersonPicker();
        clickViewContactDetails();

        EditPerson editPerson = new EditPerson(this.driver);
        this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Edit")).click();
        editPerson.checkName();
        editPerson.checkSsnValue();
        editPerson.checkDateOfBirthValue();
        editPerson.checkGenderValue();
        if (this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl")).getAttribute("value").equalsIgnoreCase("")) {
            this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl")).sendKeys("2085555555");
        }
        this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:primaryPhone-inputEl")).clear();
        this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:primaryPhone-inputEl")).sendKeys("Work");
        editPerson.clickOkButton();

        waitUntilElementIsClickable(By.cssSelector("textarea[id$=':InjuryDescription-inputEl']"));
        // Set Injury Severity
        String severity = setInjuryIncidentSeverityRandom();
        injuryIncident.setSeverity(severity);

        // Set Description
        injuryIncident.setInjuryDescription("Test Describe Injuries Text Area");
        setInjuryIncidentDescribeInjuriesTextArea(injuryIncident.getInjuryDescription());

        // Set Injury Details
        String generalInjuryType = setInjuryIncidentGeneralInjuryTypeRandom();
        injuryIncident.setGeneralInjuryType(generalInjuryType);
        String detailedInjuryType = setInjuryIncidentDetailedInjuryTypeRandom();
        injuryIncident.setDetailedInjuryType(detailedInjuryType);

        clickAddBodyParts();

        // Locate ComboBox in table, select random item
        clickAreaOfBodyCell();

        tableID = find(By.xpath("//table[starts-with(@id,'simplecombo-')]")).getAttribute("id");
        tableXpath = "//table[starts-with(@id,'" + tableID + "')]";


        focusInjuryIncidentAreaOfBody(tableXpath);

        String areaOfbody = setInjuryIncidentAreaOfBodyRandom(tableXpath);
        injuryIncident.setAreaOfBody(areaOfbody);


        // Focus the page
        clickInjuryIncidentPageHeader();

        // Locate ComboBox in table, select random item
        clickDetailedBodyPartCell();

        List<WebElement> hiddenComboBoxList = finds(By.xpath("//table[starts-with(@id,'simplecombo-')]"));
        tableID = hiddenComboBoxList.get(2).getAttribute("id");
        tableXpath = "//table[starts-with(@id,'" + tableID + "')]";


        focusInjuryIncidentDetailedBodyPart(tableXpath);

        String detailedBodyPart = setInjuryIncidentDetailedBodyPartRandom(tableXpath);
        injuryIncident.setDetailedBodyPart(detailedBodyPart);


        // Check and Set Farm Products Produced select box if it is visible.
        List<WebElement> farmProductsProduced = finds(By.cssSelector("table[id*='Liability_Subcause-triggerWrap']"));
        if (farmProductsProduced != null && farmProductsProduced.size() > 0) {
            setFarmProductsProducedRandom();
        }

        clickOkButton();

        return injuryIncident;
    }
    
    /*private void setFarmProductsProduced() {
    	farmProductsProduced().selectByVisibleTextRandom();
    }*/

}
