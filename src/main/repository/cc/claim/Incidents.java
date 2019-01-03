package repository.cc.claim;

import com.idfbins.enums.State;
import com.idfbins.enums.YesOrNo;
import org.fluttercode.datafactory.impl.DataFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;
import repository.cc.enums.CoverageType;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PointOfFirstImpact;
import repository.gw.generate.cc.GenerateIncident;
import repository.gw.helpers.ClaimHelpers;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Incidents extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public Incidents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//input[contains(@id,':PropertyDescription-inputEl')]")
    private WebElement editBox_propertyDescription;

    @FindBy(xpath = "//textArea[contains(@id,':Description-inputEl')]")
    private WebElement editBox_propertyDamageDescription;

    @FindBy(xpath = "//textArea[contains(@id,':Description-inputEl')]")
    private WebElement editBox_VehicleDamageDescription;

    @FindBy(xpath = "//span[contains(@id,'Insured-btnInnerEl')]/span[@class='infobar_elem_val']")
    private WebElement element_InsuredName;

    @FindBy(xpath = "//a/span/span/span[text()='OK']")
    private WebElement button_OK;

    @FindBy(xpath = "//a[contains(@id,':Property_IncidentMenuIcon')]")
    private WebElement picker_PropertyIncident;

    @FindBy(xpath = "//span[text()='Edit Incident Details...']")
    private WebElement link_EditIncidentDetails;

    @FindBy(xpath = "//a[@id='WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton']")
    private WebElement button_Clear;

    @FindBy(xpath = "//a[@id='FNOLVehicleIncidentPopup:FNOLVehicleIncidentScreen:OccupantLV_tb:AddDriverButton']")
    private WebElement button_AddDriver;

    @FindBy(xpath = "//a[@id='NewInjuryIncidentPopup:NewInjuryIncidentScreen:InjuryIncidentDV:Injured_Picker:Injured_PickerMenuIcon']")
    private WebElement link_InjuredPersonPicker;

    @FindBy(xpath = "//span[contains(text(),'View Contact Details')]")
    private WebElement link_ViewContactDetails;

    @FindBy(xpath = "//a[contains(@id, ':Edit')]")
    private WebElement button_Edit;

    @FindBy(xpath = "//span[@id='EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Edit-btnWrap']")
    private WebElement button_EditIncident;

    @FindBy(xpath = "//span[@id='EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Update-btnInnerEl']")
    private WebElement button_Update;

    @FindBy(css = "span[id*='EditVehicleIncidentPopup:EditVehicleIncidentScreen:Update-btnInnerEl']")
    private WebElement updateIncidentButton;

    @FindBy(xpath = "//a[contains(@id, ':Cancel')]")
    private WebElement button_Cancel;

    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:GlobalPersonNameInputSet:FirstName-inputEl']")
    private WebElement input_fName;

    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:GlobalPersonNameInputSet:LastName-inputEl']")
    private WebElement input_lName;

    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:SSN-inputEl']")
    private WebElement input_Ssn;

    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:DateOfBirth-inputEl']")
    private WebElement input_Dob;

    @FindBy(xpath = "//textarea[@id='EditCropIncidentPopup:EditCropIncidentScreen:CropIncidentDetailDV:CropIncidentDV:Description-inputEl']")
    private WebElement textarea_CropDamageDescription;

    @FindBy(xpath = "//a[@id='EditCropIncidentPopup:EditCropIncidentScreen:CropIncidentDetailDV:CropIncidentDV:EditableCropHailLV_tb:Add']")
    private WebElement button_AddCrop;

    @FindBy(xpath = "//textarea[@id='NewInjuryIncidentPopup:NewInjuryIncidentScreen:InjuryIncidentDV:InjuryIncidentInputSet:InjuryDescription-inputEl']")
    private WebElement editBox_DescribeInjuries;

    // Vehicle Incident Page (Edit)

    @FindBy(xpath = "//input[contains(@id,':Vehicle_Picker-inputEl')]")
    private WebElement input_VehicleName;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_Year-inputEl')]")
    private WebElement input_VehicleYear;

    @FindBy(css = "input[id*=':Vehicle_Make']")
    private WebElement input_VehicleMake;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_Model-inputEl')]")
    private WebElement input_VehicleModel;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_Color-inputEl')]")
    private WebElement input_VehicleColor;

    @FindBy(xpath = "//input[contains(@id, ':CollisionPoint-inputEl')]")
    private WebElement input_VehiclePOI;

    @FindBy(xpath = "//textarea[@id = 'NewOtherIncidentPopup:NewFixedPropertyIncidentScreen:OtherIncidentDetailDV:Description-inputEl']")
    private WebElement textArea_OtherClaimDescription;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_VIN-inputEl')]")
    private WebElement input_VehicleVin;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_State-inputEl')]")
    private WebElement input_VehicleState;

    @FindBy(xpath = "//input[contains(@id, ':Vehicle_LicensePlate-inputEl')]")
    private WebElement input_VehicleLicensePlate;

    @FindBy(xpath = "//input[contains(@id, ':VehIncidentDetailDV:VehicleIncidentDV:Driver_Picker-inputEl')]")
    private WebElement input_DriverName;

    @FindBy(xpath = "//input[contains(@id, ':PropertyDescription-inputEl')]")
    private WebElement input_PropertyDescription;

    @FindBy(xpath = "//textarea[contains(@id, ':Description-inputEl')]")
    private WebElement textarea_PropertyDamageDescription;

    @FindBy(xpath = "//input[contains(@id,':AddressLine1-inputEl')]")
    private WebElement input_PropertyAddressLine1;

    @FindBy(xpath = "//input[contains(@id, 'AddressLine2-inputEl')]")
    private WebElement input_PropertyAddressLine2;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement input_PropertyCity;

    @FindBy(xpath = "//input[contains(@id, ':State-inputEl')]")
    private WebElement input_PropertyState;

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement input_PropertyZipCode;

    @FindBy(xpath = "//span[contains(text(),'Injury Incident')]")
    private WebElement webElement_InjuryIncidentPageHeader;

    @FindBy(xpath = "//textArea[contains(@id,':InjuryDescription-inputEl')]")
    private WebElement textArea_InjuryIncidentDescribeInjuries;

    @FindBy(xpath = "//a[contains(@id,':EditableBodyPartDetailsLV_tb:Add')]")
    private WebElement button_AddBodyParts;

    @FindBy(xpath = "//div[contains(@id,':EditableBodyPartDetailsLV-body')]/div/table/tbody/tr/td[2]")
    private WebElement webElement_AreaOfBodyCell;

    @FindBy(xpath = "//div[contains(@id,':EditableBodyPartDetailsLV-body')]/div/table/tbody/tr/td[3]")
    private WebElement webElement_DetailedBodyPartCell;

    @FindBy(xpath = "//textarea[contains(@id,'Description-inputEl')]")
    private WebElement OtherIncidentClaimDescriptionBox;

    @FindBy(xpath = "//input[contains(@id,'PropertyDescription-inputEl')]")
    private WebElement inputPropertyDescription;

    @FindBy(xpath = "//textarea[contains(@id,':Description-inputEl')]")
    private WebElement inputDamageDescription;

    @FindBy(xpath = "//input[contains(@id,':DescriptionExt-inputEl')]")
    private WebElement inputItemDescription;

    @FindBy(xpath = "//a[@id='NewFixedPropertyIncidentPopup:NewFixedPropertyIncidentScreen:"
            + "FixPropIncidentDetailDV:FixedPropertyIncidentDV:Owner_Picker:Owner_PickerMenuIcon']")
    private WebElement pickerOwner;

    @FindBy(xpath = "//a[@id='NewFixedPropertyIncidentPopup:NewFixedPropertyIncidentScreen:FixPropIncidentDetailDV:"
            + "FixedPropertyIncidentDV:Owner_Picker:MenuItem_Search-itemEl'")
    private WebElement linkOwnerSearch;

    private Guidewire8Select selectNewOtherIncidentClaimant() {
        return new Guidewire8Select(driver, "//table[contains(@id,':OtherIncidentDetailDV:OtherDescription-triggerWrap') or contains(@id,':Other_Picker-triggerWrap')]");
    }

    // Property Incident Fields (for portal validation)

    public void setNewOtherIncidentClaimantRandom() {
        selectNewOtherIncidentClaimant().selectByVisibleTextRandom();
    }

    public Guidewire8Select select_Vehicle() {
        return new Guidewire8Select(driver, "//table[@id='NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Vehicle_Incident-triggerWrap']");
    }

    public Guidewire8Select select_PropertyOwner() {
        return new Guidewire8Select(driver, "//table[@id='FNOLEditPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Owner_Picker-triggerWrap']");
    }

    public Guidewire8Select select_Owner() {
        return new Guidewire8Select(driver, "//table[@id='FNOLEditPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Owner_Picker-triggerWrap']");
    }

    public Guidewire8Select select_EquipmentBreakdown() {
        return new Guidewire8Select(driver, "//table[contains(@id,':EquipmentBreakdownSubcause_FBM-triggerWrap')]");
    }

    public Guidewire8Select select_Peril() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PeriSubcause_FBM-triggerWrap')]");
    }

    public Guidewire8Select select_ResultantDamage() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ResultantDmgSubcause_FBM-triggerWrap')]");
    }


    // HELPERS
    // ==============================================================================

    public Guidewire8Select select_ContributingFactor() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ContributFactSubcause_FBM-triggerWrap')]");
    }

    public Guidewire8Select select_MoldInvolved() {
        return new Guidewire8Select(driver, "//table[contains(@id,':LossLocation_Mold-triggerWrap')]");
    }

    public Guidewire8Select select_WaterSubCause() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Water_Subcause-triggerWrap')]");
    }

    public Guidewire8Select select_Why() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Water_Subcause_Why-triggerWrap')]");
    }

    public Guidewire8Select select_Mysterious() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Mysterious_Theft-triggerWrap')]");
    }

    public Guidewire8Select select_Fire() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Fire_Subcause-triggerWrap')]");
    }

    public Guidewire8Select select_WhereFrom() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Water_Subcause_Where-triggerWrap')]");
    }

    public Guidewire8Select select_Driver() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Driver_Picker-triggerWrap')]");
    }

    public Guidewire8Select select_InjuredPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Injured_Picker-triggerWrap')]");
    }

    public Guidewire8Select select_DriverName() {
        return new Guidewire8Select(driver, "//table[@id='FBM_FNOLDriverPopup:FNOLContactScreen:ContactDV:Driver_Picker-triggerWrap']");
    }

    public Guidewire8Select select_Gender() {
        return new Guidewire8Select(driver, "//table[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:Gender-triggerWrap']");
    }

    public void clickUpdateButton() {
        try {
            clickWhenClickable(button_Update);
        } catch (Exception e) {
            hoverOverAndClick(updateIncidentButton);
        }
    }

    public void setInjuredPerson() {
        select_InjuredPerson().selectByVisibleTextRandom();
    }

    public void setInjuredPerson(String selectString) {
        select_InjuredPerson().selectByVisibleText(selectString);
    }

    public String getInjuredPerson() {
        return (select_InjuredPerson().getText());
    }

    public void setDescribeInjuries(String textInput) {
        editBox_DescribeInjuries.sendKeys(textInput);
    }

    public void setDamageDescription() {
        textarea_CropDamageDescription.sendKeys("Description of Damage.");
    }

    public void selectRandomGender() {
        select_Gender().selectByVisibleTextRandom();
    }

    public void checkName() {

        String insNameString = find(By.xpath("//span[@id='Claim:ClaimInfoBar:Insured-btnWrap']//span[@class='infobar_elem_val']")).getText();

        String insName = insNameString.replaceAll("[0-9]", "").trim();
        String[] nameArray = insName.split(" ");

        if (input_fName.getAttribute("value").equalsIgnoreCase("")) {
            input_fName.sendKeys(nameArray[0]);
        }

        if (input_lName.getAttribute("value").equalsIgnoreCase("")) {
            input_lName.sendKeys(nameArray[1]);
        }
    }

    public void checkSsnValue() {

        if (input_Ssn.getAttribute("value").equalsIgnoreCase("")) {
            String numString = repository.gw.helpers.StringsUtils.generateRandomNumberDigits(9);
            String ssnString = repository.gw.helpers.StringsUtils.formatSSN(numString);
            input_Ssn.sendKeys(ssnString);
        }
    }

    public void resetSSN() {
        input_Ssn.clear();
        String numString = repository.gw.helpers.StringsUtils.generateRandomNumberDigits(9);
        String ssnString = repository.gw.helpers.StringsUtils.formatSSN(numString);
        input_Ssn.sendKeys(ssnString);
    }

    public void checkDobValue() {

        if (input_Dob.getAttribute("value").equalsIgnoreCase("")) {
            DataFactory generateDataFactory = new DataFactory();
            String birthDate = repository.gw.helpers.DateUtils.dateFormatAsString("MM/dd/yyyy", generateDataFactory.getBirthDate());
            input_Dob.sendKeys(birthDate);
        }
    }

    public void checkGenderValue() {

        if (select_Gender().getText().equalsIgnoreCase("<none>")) {
            selectRandomGender();
        }
    }

    public void clickEditButton() {
        clickWhenClickable(button_Edit);
        waitForPageLoad();
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void clickEditIncidentButton() {
        clickWhenClickable(button_EditIncident);
    }

    public void clickViewContactDetails() {
        clickWhenClickable(link_ViewContactDetails);
    }

    public void clickInjuredPersonPicker() {
        Actions build = new Actions(this.driver);
        
        build.moveToElement(link_InjuredPersonPicker).click().build().perform();
    }

    public void selectRandomDriverName() {
        select_DriverName().selectByVisibleTextRandom();
    }

    public void selectOwner(String selectString) {
        select_Owner().selectByVisibleText(selectString);
    }

    public void clickEditIncidentDetailsLink() {
        clickWhenClickable(link_EditIncidentDetails);
    }

    public void clickPropertyIncidentPicker() {
        clickWhenClickable(picker_PropertyIncident);
    }

    public String getNameOfInsured() {
        String insrdName = element_InsuredName.getText();

        return insrdName;
    }

    public void clickOkButton() {
        clickWhenClickable(button_OK);
        waitUtils.waitUntilElementIsNotVisible(button_OK, 20);
    }

    public void clickAddCropButton() {
        clickWhenClickable(button_AddCrop);
        waitUntilElementIsClickable(By.cssSelector("input[id$='RealTotalFieldAcresExt-inputEl']"));
    }

    public void setSelectPropertyOwner(String selectString) {
        select_PropertyOwner().selectByVisibleText(selectString);

    }

    public void setPropertyDescriptionEditBox(String inputText) {
        editBox_propertyDescription.sendKeys(inputText);
    }

    public void setPropertyDamageDescriptionEditBox(String inputText) {
        editBox_propertyDamageDescription.sendKeys(inputText);
    }

    public void setVehicleDamageDescription(String inputText) {
        editBox_VehicleDamageDescription.sendKeys(inputText);
    }

    public void setCropDamageDescription(String cropDescription) {
        textarea_CropDamageDescription.sendKeys(cropDescription);
    }

    public void selectRandom_EquipmentBreakdown() {
        select_EquipmentBreakdown().selectByVisibleTextRandom();
    }

    public void selectRandom_Peril() {
        select_Peril().selectByVisibleTextRandom();
    }

    public void selectRandom_ResultantDamage() {
        select_ResultantDamage().selectByVisibleTextRandom();
    }

    // EVERYTHING RELATED TO CREATE NEW INJURY INCIDENT **************************************************************************************************** START

    public void selectRandom_ContributingFactor() {
        select_ContributingFactor().selectByVisibleTextRandom();
    }

    public void selectRandom_MoldInvolved() {
        select_MoldInvolved().selectByVisibleTextRandom();
    }

    public void selectRandom_WaterSubcause() {
        select_WaterSubCause().selectByVisibleTextRandom();
    }

    public void selectRandom_Why() {
        select_Why().selectByVisibleTextRandom();
    }

    public void selectRandom_Mysterious() {
        select_Mysterious().selectByVisibleTextRandom();
    }

    public void selectRandom_Fire() {
        select_Fire().selectByVisibleTextRandom();
    }

    public void selectRandom_WhereFrom() {
        select_WhereFrom().selectByVisibleTextRandom();
    }

    public void checkSpecialCircumstance() {

        this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        try {

            List<WebElement> mold = finds(By.xpath("//table[contains(@id,':LossLocation_Mold-triggerWrap')]"));
            if (mold.size() > 0) {
                selectRandom_MoldInvolved();
            }

            List<WebElement> waterSubcause = finds(By.xpath("//table[contains(@id,':Water_Subcause-triggerWrap')]"));
            if (waterSubcause.size() > 0) {
                selectRandom_WaterSubcause();
            }

            List<WebElement> why = finds(By.xpath("//table[contains(@id,':Water_Subcause_Why-triggerWrap')]"));
            if (why.size() > 0) {
                selectRandom_Why();
            }

            List<WebElement> mysterious = finds(By.xpath("//table[contains(@id,':Mysterious_Theft-triggerWrap')]"));
            if (mysterious.size() > 0) {
                selectRandom_Mysterious();
            }

            List<WebElement> fire = finds(By.xpath("//table[contains(@id,':Fire_Subcause-triggerWrap')]"));
            if (fire.size() > 0) {
                selectRandom_Fire();
            }

            List<WebElement> whereFrom = finds(By.xpath("//table[contains(@id,':Water_Subcause_Where-triggerWrap')]"));
            if (whereFrom.size() > 0) {
                selectRandom_WhereFrom();
            }

        } catch (Exception e) {
            System.out.println("No Special Circumstances");
        }

        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // try {
        // 
        // selectRandom_EquipmentBreakdown();
        // 
        // selectRandom_Peril();
        // 
        // selectRandom_ResultantDamage();
        // 
        // selectRandom_ContributingFactor();
        // 
        // selectRandom_Mysterious();
        // 
        // selectRandom_Fire();
        // } catch (Exception e) {
        //
        // }
    }

    private Guidewire8Select selectFarmProductsProduced() {
        return new Guidewire8Select(driver, "table[id*='Liability_Subcause-triggerWrap']");
    }

    private void setFarmProductsProducedRandom() {
        List<String> options = selectFarmProductsProduced().getList();
        selectFarmProductsProduced().selectByVisibleText(options.get(repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, options.size() - 1)));
    }

    public void buildIncident(String fnolType, String insuredName) {

        if (fnolType.equalsIgnoreCase("Auto") || fnolType.equalsIgnoreCase("Auto - ERS or Glass")) {
            new GenerateIncident(repository.gw.enums.Incidents.newThirdPartyVehicle, this.driver);
        } else if (fnolType.equalsIgnoreCase("General Liability")) {
            setSelectPropertyOwner(insuredName);
            
            setPropertyDescriptionEditBox("Test - Incident Item");
            
            setPropertyDamageDescriptionEditBox("Test - Damage Comment Here");
            checkSpecialCircumstance();

            // Check and Set Farm Products Produced select box if it is visible.
            List<WebElement> farmProductsProduced = finds(By.cssSelector("table[id*='Liability_Subcause-triggerWrap']"));
            if (farmProductsProduced != null && farmProductsProduced.size() > 0) {
                setFarmProductsProducedRandom();
            }
        } else if (fnolType.equalsIgnoreCase("Inland Marine")) {
            List<WebElement> property = finds(By.xpath("//span[contains(text(),'Property Incident')]"));
            List<WebElement> vehicle = finds(By.xpath("//span[contains(text(),'Vehicle Details')]"));
            if (property.size() > 0) {
                setSelectPropertyOwner(insuredName);
                
                setPropertyDescriptionEditBox("Test - Incident Item");
                
                setPropertyDamageDescriptionEditBox("Test - Damage Comment Here");
                checkSpecialCircumstance();
            } else if (vehicle.size() > 0) {
                setVehicleDamageDescription("Test - Damage Description");
            }
        } else if (fnolType.equalsIgnoreCase("Property - Residential Glass") || fnolType.equalsIgnoreCase("Property")) {
            setSelectPropertyOwner(insuredName);
            
            setPropertyDescriptionEditBox("Test - Incident Item");
            
            setPropertyDamageDescriptionEditBox("Test - Damage Comment Here");
            checkSpecialCircumstance();
        } else if (fnolType.equalsIgnoreCase("Crop")) {
            System.out.println("Crop");
        }
    }

    public void setEquipmentBreakdown() {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        LossDetails lossDetails = new LossDetails(this.driver);

        sideMenu.clickLossDetailsLink();

        for (int i = 0; i < lossDetails.getIncidentList().size(); i++) {
            lossDetails.clickIncidentsInTable(i);
            
            clickEditIncidentButton();
            

            selectRandom_EquipmentBreakdown();
            selectRandom_Peril();
            selectRandom_ResultantDamage();
            selectRandom_ContributingFactor();

            try {
                selectRandom_Mysterious();
                
                selectRandom_Fire();
            } catch (Exception e) {

            }

            clickUpdateButton();
        }

        
    }

    public String getVehicleNameToSelect() {
        return input_VehicleName.getAttribute("value");
    }

    public Guidewire8Select select_InjuryIncidentInjuredPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Injured_Picker-triggerWrap')]");
    }

    public String getInjuryIncidentInjuredPerson() {
        String selectBoxString = select_InjuryIncidentInjuredPerson().getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentInjuredPerson(String selectString) {
        select_InjuryIncidentInjuredPerson().selectByVisibleText(selectString);
    }

    public void setInjuryIncidentInjuredPersonRandom() {
        select_InjuryIncidentInjuredPerson().selectByVisibleTextRandom();
    }

    public void setInjuryIncidentInjuredPersonContains(String partialText) {
        select_InjuryIncidentInjuredPerson().selectByVisibleTextPartial(partialText);
    }

    public Guidewire8Select select_InjuryIncidentSeverity() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Severity-triggerWrap')]");
    }

    public String getInjuryIncidentSeverity() {
        String selectBoxString = select_InjuryIncidentSeverity().getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentSeverity(String selectString) {
        select_InjuryIncidentSeverity().selectByVisibleText(selectString);
    }

    public void setInjuryIncidentSeverityRandom() {
        select_InjuryIncidentSeverity().selectByVisibleTextRandom();
    }

    public void setInjuryIncidentSeverityContains(String partialText) {
        select_InjuryIncidentSeverity().selectByVisibleTextPartial(partialText);
    }

    public Guidewire8Select select_InjuryIncidentGeneralInjuryType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PrimaryInjuryType-triggerWrap')]");
    }

    public String getInjuryIncidentGeneralInjuryType() {
        String selectBoxString = select_InjuryIncidentGeneralInjuryType().getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentGeneralInjuryType(String selectString) {
        select_InjuryIncidentGeneralInjuryType().selectByVisibleText(selectString);
    }

    public void setInjuryIncidentGeneralInjuryTypeRandom() {
        select_InjuryIncidentGeneralInjuryType().selectByVisibleTextRandom();
    }

    public void setInjuryIncidentGeneralInjuryTypeContains(String partialText) {
        select_InjuryIncidentGeneralInjuryType().selectByVisibleTextPartial(partialText);
    }

    public Guidewire8Select select_InjuryIncidentDetailedInjuryType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':DetailedInjuryType-triggerWrap')]");
    }

    public String getInjuryIncidentDetailedInjuryType() {
        String selectBoxString = select_InjuryIncidentDetailedInjuryType().getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentDetailedInjuryType(String selectString) {
        select_InjuryIncidentDetailedInjuryType().selectByVisibleText(selectString);
    }

    public void setInjuryIncidentDetailedInjuryTypeRandom() {
        select_InjuryIncidentDetailedInjuryType().selectByVisibleTextRandom();
    }

    public void setInjuryIncidentDetailedInjuryTypeContains(String partialText) {
        select_InjuryIncidentDetailedInjuryType().selectByVisibleTextPartial(partialText);
    }

    public Guidewire8Select select_AreaOfBody(String idString) {
        return new Guidewire8Select(driver, "" + idString + "");
    }

    public String getInjuryIncidentAreaOfBody(String idString) {
        String selectBoxString = select_AreaOfBody(idString).getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentAreaOfBody(String idString, String selectString) {
        select_AreaOfBody(idString).selectByVisibleText(selectString);
    }

    public void setInjuryIncidentAreaOfBodyRandom(String idString) {
        select_AreaOfBody(idString).selectByVisibleTextRandom();
    }

    public void setInjuryIncidentAreaOfBodyContains(String idString, String partialText) {
        select_AreaOfBody(idString).selectByVisibleTextPartial(partialText);
    }

    public void focusInjuryIncidentAreaOfBody(String idString) {
        select_AreaOfBody(idString).sendKeys(Keys.ENTER);
    }

    public Guidewire8Select select_DetailedBodyPart(String idString) {
        return new Guidewire8Select(driver, "" + idString + "");
    }

    public String getInjuryIncidentDetailedBodyPart(String idString) {
        String selectBoxString = select_DetailedBodyPart(idString).getText();
        return (selectBoxString);
    }

    public void setInjuryIncidentDetailedBodyPart(String idString, String selectString) {
        select_DetailedBodyPart(idString).selectByVisibleText(selectString);
    }

    public void setInjuryIncidentDetailedBodyPartRandom(String idString) {
        select_DetailedBodyPart(idString).selectByVisibleTextRandom();
    }

    public void setInjuryIncidentDetailedBodyPartContains(String idString, String partialText) {
        select_DetailedBodyPart(idString).selectByVisibleTextPartial(partialText);
    }

    public void focusInjuryIncidentDetailedBodyPart(String idString) {
        select_DetailedBodyPart(idString).sendKeys(Keys.ENTER);
    }

    // EVERYTHING RELATED TO CREATE NEW INJURY INCIDENT **************************************************************************************************** END

    public void clickInjuryIncidentPageHeader() {
        clickWhenClickable(webElement_InjuryIncidentPageHeader);
    }

    public void setInjuryIncidentDescribeInjuriesTextArea(String inputString) {
        textArea_InjuryIncidentDescribeInjuries.sendKeys(inputString);
    }

    public void clickAddBodyParts() {
        clickWhenClickable(button_AddBodyParts);
    }

    public void clickAreaOfBodyCell() {
        clickWhenClickable(webElement_AreaOfBodyCell);
        
    }

    public void clickDetailedBodyPartCell() {
        clickWhenClickable(webElement_DetailedBodyPartCell);
        
    }

    public void setOtherIncidentClaimDescription(String inputString) {
        waitForPageLoad();
        setText(OtherIncidentClaimDescriptionBox, inputString);
    }

    public void createOtherIncident() {
        waitUtils.waitUntilElementIsVisible(OtherIncidentClaimDescriptionBox);
        setOtherIncidentClaimDescription("Test Description");
        ClaimHelpers.setDynamicSelectBoxRestricted(selectNewOtherIncidentClaimant(), "<none>");
        clickOkButton();
    }

    public String getVehicleYear() {
        return input_VehicleYear.getAttribute("value");
    }

    public String getVehicleModel() {
        return input_VehicleModel.getAttribute("value");
    }

    public String getVehicleMake() {
        return input_VehicleMake.getAttribute("value");
    }

    public String getVehicleColor() {
        return input_VehicleColor.getAttribute("value");
    }

    public String getVehicleVin() {
        return input_VehicleVin.getAttribute("value");
    }

    public State getVehicleState() {
        return State.valueOf(input_VehicleState.getAttribute("value"));
    }

    public String getVehicleLicensePlate() {
        return input_VehicleLicensePlate.getAttribute("value");
    }

    public PointOfFirstImpact getVehiclePOI() {
        return PointOfFirstImpact.stringToEnum(input_VehiclePOI.getAttribute("value"));
    }

    public YesOrNo getWasTheVehicleParked() {
        return YesOrNo.valueOf(find(By.xpath("//table[contains(@class, 'cb-checked') and contains(@id, ':VehicleParked_')]//div/label")).getText());
    }

    public YesOrNo getDidAirbagsDeploy() {
        return YesOrNo.valueOf(find(By.xpath("//table[contains(@class, 'cb-checked') and contains(@id, ':Exposure_AirbagsDeployed')]//div/label")).getText());
    }

    public YesOrNo getIsTheVehicleSafeToDrive() {
        return YesOrNo.valueOf(find(By.xpath("//table[contains(@class, 'cb-checked') and contains(@id, ':Operable')]//div/label")).getText());
    }

    public YesOrNo getWasThereEquipmentFailure() {
        return YesOrNo.valueOf(find(By.xpath("//table[contains(@class, 'cb-checked') and contains(@id, ':Exposure_EquipmentFailure')]//div/label")).getText());
    }

    public String getDriverName() {
        return input_DriverName.getAttribute("value");
    }

    public String getPropertyDescription() {
        return input_PropertyDescription.getAttribute("value");
    }

    public void setPropertyDescription(String inputString) {
        waitUntilElementIsClickable(By.cssSelector("input[id$=':PropertyDescription-inputEl']"), 20);
        inputPropertyDescription.sendKeys(inputString);
    }

    public String getPropertyDamageDescription() {
        return textarea_PropertyDamageDescription.getText();
    }

    public String getPropertyAddressLine1() {
        return input_PropertyAddressLine1.getAttribute("value");
    }

    public String getPropertyAddressLine2() {
        return input_PropertyAddressLine2.getAttribute("value");
    }

    public String getPropertyCity() {
        return input_PropertyCity.getAttribute("value");
    }

    public State getPropertyState() {
        return State.valueOf(input_PropertyState.getAttribute("value"));
    }

    public String getPropertyZipCode() {
        return input_PropertyZipCode.getAttribute("value");
    }

    public void selectBoxModifier(Guidewire8Select selectBox, String desiredSelection, boolean isCoverage) {

        String coverageString = "";

        if (isCoverage) {
            WebElement coverageSelect = find(By.xpath("//input[contains(@id,'Exposure_Coverage-inputEl')]"));
            coverageString = coverageSelect.getAttribute("value");
        }

        List<String> selectBoxOptions = selectBox.getList();
        boolean foundSelection = false;
        for (String item : selectBoxOptions) {
            if (item.equalsIgnoreCase(desiredSelection)) {
                selectBox.selectByVisibleText(desiredSelection);
                foundSelection = true;
                break;
            } else if (!coverageString.equals("") && item.equalsIgnoreCase(desiredSelection)) {
                selectBox.selectByVisibleText(desiredSelection);
                foundSelection = true;
                break;
            }
        }

        if (!foundSelection) {
            selectBox.selectByVisibleTextRandom();
        }
    }

    public void setDamageDescription(String inputString) {
        inputDamageDescription.sendKeys(inputString);
    }

    public Guidewire8Select selectPropertyName() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ruProperty_Picker-triggerWrap')]");
    }

    public String getPropertyName() {
        String selectBoxString = selectPropertyName().getText();
        return (selectBoxString);
    }

    public void setPropertyName(String selectString) {
        selectPropertyName().selectByVisibleText(selectString);
    }

    public void setPropertyNameRandom() {
        selectPropertyName().selectByVisibleTextRandom();
    }

    public Guidewire8Select selectPropertyOwner() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Owner_Picker-triggerWrap')]");
    }

    public String getPropertyOwner() {
        String selectBoxString = selectPropertyOwner().getText();
        return (selectBoxString);
    }

    public void setPropertyOwner(String selectString) {
        selectPropertyOwner().selectByVisibleText(selectString);
    }

    public void setPropertyOwnerRandom() {
        selectPropertyOwner().selectByVisibleTextRandom();
    }

    public void setItemDescription(String inputString) {
        inputItemDescription.clear();
        
        inputItemDescription.sendKeys(inputString);
    }

    public void createPropertyIncident() {

        setPropertyDescription("New Property Incident");
        setDamageDescription("This is why we can't have nice things.");
        
        setPropertyName("New...");
        
        setItemDescription("Bat Cave");
        
        setPropertyOwner(getNameOfInsured());
        
        clickOkButton();
        
    }

    @FindBy(css = "a[id*=':CreditCard:']")
    private WebElement buttonAddCreditCards;

    @FindBy(css = "a[id*=':Jewelry:']")
    private WebElement buttonAddJewelry;

    @FindBy(css = "a[id*=':Textiles:']")
    private WebElement buttonAddTextiles;

    private void addCreditCards() {

        String description = "//div[contains(@id,':CreditCard:')]//table/tbody/tr/td[2]";
        String amount = "//div[contains(@id,':CreditCard:')]//table/tbody/tr/td[3]";
        String descriptionText = "Visa";

        addSpecifics(buttonAddCreditCards, description, amount, descriptionText);
    }

    private void addJewelry() {

        String description = "//div[contains(@id,':Jewelry:')]//table/tbody/tr/td[2]";
        String amount = "//div[contains(@id,':Jewelry:')]//table/tbody/tr/td[3]";
        String descriptionText = "Watch";

        addSpecifics(buttonAddJewelry, description, amount, descriptionText);
    }

    private void addTextiles() {

        String description = "//div[contains(@id,':Textiles:')]//table/tbody/tr/td[2]";
        String amount = "//div[contains(@id,':Textiles:')]//table/tbody/tr/td[3]";
        String descriptionText = "Rug";

        addSpecifics(buttonAddTextiles, description, amount, descriptionText);
    }

    private void addSpecifics(WebElement addButton, String descriptionInput, String amountInput, String descriptionText) {
        clickWhenClickable(addButton);
        
        waitUntilElementIsClickable(By.xpath(descriptionInput));
        WebElement description = find(By.xpath(descriptionInput));
        WebElement amount = find(By.xpath(amountInput));

        try {
            clickWhenClickable(description);
            
            sendArbitraryKeys(descriptionText);
        } catch (Exception e) {
            
            clickWhenClickable(description);
            
            sendArbitraryKeys(descriptionText);
        }

        try {
            
            clickAmountField(amount);
            clickAmountField(amount);
            
            sendArbitraryKeys(Integer.toString(repository.gw.helpers.NumberUtils.generateRandomNumberInt(500, 1000)));
        } catch (Exception e) {
            
            clickAmountField(amount);
            clickAmountField(amount);
            
            sendArbitraryKeys(Integer.toString(repository.gw.helpers.NumberUtils.generateRandomNumberInt(500, 1000)));
        }
        
    }

    private void clickAmountField(WebElement amount) {

        Actions builder = new Actions(this.driver);

        builder.moveToElement(amount)
                .click(amount)
                .moveToElement(amount, 1, 1);
        Action runMe = builder.build();
        runMe.perform();
    }

    public void addImpliedCoverageDetails(String coverageType) {

        CoverageType covType = convertCoverageString(coverageType);

        

        switch (covType) {
            case jewelry:
                addJewelry();
                break;
            case textiles:
                addTextiles();
                break;
            case creditcards:
                addCreditCards();
                break;
            default:
        }

        List<WebElement> mysteriousTheft = new ArrayList<>();
        mysteriousTheft = finds(By.cssSelector("input[id*='Mysterious_Theft-inputEl']"));

        if (mysteriousTheft.size() > 0) {
            selectRandom_Mysterious();
        }

        setPropertyOwner(getNameOfInsured());

        clickOkButton();
    }

    private CoverageType convertCoverageString(String coverage) {

        CoverageType type = CoverageType.getEnumByString(coverage);

        return type;
    }

    @FindBy(css = "input[id*='VehicleIncidentDV:Collision_Indicator_true-inputEl']")
    private WebElement vehicleIsDamagedRadioTrue;

    private void clickVehicleIsDamaged() {
        clickWhenClickable(vehicleIsDamagedRadioTrue);
    }

    private Guidewire8Select point1stImpactSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':VehicleIncidentDV:CollisionPoint-triggerWrap')]");
    }

    private void setPoint1stImpactRandomSelect() {
        point1stImpactSelect().selectByVisibleTextRandom();
    }

    private Guidewire8Select point2ndImpactSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':VehicleIncidentDV:SecondCollisionPoint-triggerWrap')]");
    }

    private void setPoint2ndImpactRandomSelect() {
        point2ndImpactSelect().selectByVisibleTextRandom();
    }

    private Guidewire8Select lossTypeSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':VehIncidentDetailDV:VehicleIncidentDV:LossType-triggerWrap')]");
    }

    private void setLossTypeRandomSelect() {
        lossTypeSelect().selectByVisibleTextRandom();
    }

    @FindBy(css = "input[id*=':OdometerReading-inputEl']")
    private WebElement odometerReadingInput;

    private void setOdometerReading(String keysToSend) {
        odometerReadingInput.sendKeys(keysToSend);
    }

    @FindBy(css = "a[id*=':VehIncidentDetailDV:Incident_AppraisalTab']")
    private WebElement cccAppraisalTab;

    private void clickCCCAppraisalTab() {
        waitUtils.waitUntilElementIsClickable(cccAppraisalTab, 5000).click();
    }

    private Guidewire8Select vehicleAddressSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Insured_PrimaryAddress-triggerWrap')]");
    }

    private void setVehicleAddressSelect() {
        List<String> validOptions = new ArrayList<>();
        List<String> options = vehicleAddressSelect().getList();

        for (String option : options) {
            if (!option.equalsIgnoreCase("New...") && !option.equalsIgnoreCase("<none>")) {
                validOptions.add(option);
            }
        }

        String selectedOption = validOptions.get(repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        vehicleAddressSelect().selectByVisibleText(selectedOption);
    }

    @FindBy(css = "a[id*=':VehicleIncidentAppraisalAssignmentDV:moiSuggestion']")
    private WebElement evaluateButton;

    private void clickEvaluateButton() {
        waitUtils.waitUntilElementIsClickable(evaluateButton);
        clickWhenClickable(evaluateButton);
    }

    @FindBy(css = "span[id*='AssignmentSearchPopup:ttlBar']")
    private WebElement searchForAppraisersHeader;

    private void waitForAppraiserPopup() {
        waitUtils.waitUntilElementIsVisible(searchForAppraisersHeader);
    }

    /*private void setAirbagsDeployed(boolean isDeployed) {

        WebElement yesRadio = find(By.cssSelector("input[id*=':Exposure_AirbagsDeployed_true-inputEl']"));
        WebElement noRadio = find(By.cssSelector("input[id*=':Exposure_AirbagsDeployed_false-inputEl']"));

        if (isDeployed) {
            clickWhenClickable(yesRadio);
        } else {
            clickWhenClickable(noRadio);
        }
    }*/

    private void setAirbagsDeployed() {

        WebElement yesRadio = find(By.cssSelector("input[id*=':Exposure_AirbagsDeployed_true-inputEl']"));
        WebElement noRadio = find(By.cssSelector("input[id*=':Exposure_AirbagsDeployed_false-inputEl']"));

        int rand = repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, 1);
        if (rand == 0) {
            clickWhenClickable(yesRadio);
        } else {
            clickWhenClickable(noRadio);
        }
    }

    /*private void setOperable(boolean isDeployed) {

        WebElement yesRadio = find(By.cssSelector("input[id*=':VehicleIncidentDV:Operable_true-inputEl']"));
        WebElement noRadio = find(By.cssSelector("input[id*=':VehicleIncidentDV:Operable_false-inputEl']"));

        if (isDeployed) {
            clickWhenClickable(yesRadio);
        } else {
            clickWhenClickable(noRadio);
        }
    }*/

    private void setOperable() {

        WebElement yesRadio = find(By.cssSelector("input[id*=':VehicleIncidentDV:Operable_true-inputEl']"));
        WebElement noRadio = find(By.cssSelector("input[id*=':VehicleIncidentDV:Operable_false-inputEl']"));

        int rand = repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, 1);
        if (rand == 0) {
            clickWhenClickable(yesRadio);
        } else {
            clickWhenClickable(noRadio);
        }
    }

    private Guidewire8Select makeSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':DamageVehicleMake-triggerWrap')]");
    }

    /*private void setMake(String make) {
        makeSelect().selectByVisibleText(make);
    }*/

    private void setMakeRandom() {
        makeSelect().selectByVisibleTextRandom();
    }

    @FindBy(css = "input[id*=':Vehicle_VIN-inputEl']")
    private WebElement vinInput;

    private void setVIN(String vin) {
        vinInput.sendKeys(vin);
    }

    private Guidewire8Select driverNameSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':VehicleIncidentDV:Driver_Picker-triggerWrap')]");
    }

    private void setDriverName() {
        driverNameSelect().selectByVisibleTextRandom();
    }

    private Guidewire8Select relationToInsuredSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':RelationToInsured-triggerWrap')]");
    }

    @FindBy(css = "input[id*=':TotalLoss_false-inputEl']")
    private WebElement totalLossNoRadio;

    @FindBy(css = "input[id*=':TotalLoss_true-inputEl']")
    private WebElement totalLossYesRadio;

    private void clickTotalLossNo() {
        clickWhenClickable(totalLossNoRadio);
    }

    private void clickTotalLossYes() {
        clickWhenClickable(totalLossYesRadio);
    }

    private void setRelationToInsured() {
        relationToInsuredSelect().selectByVisibleTextRandom();
    }

    public String addDataForCCC() {

        Vin vehicleObject = new Vin();
        String cccReferenceID = "";

        try {
            vehicleObject = VINHelper.getRandomVIN();
        } catch (Exception e) {
            Assert.fail("Unable to retrieve VIN.");
            e.printStackTrace();
        }

        clickVehicleIsDamaged();

        clickTotalLossNo();

        
        setPoint1stImpactRandomSelect();
        
        setPoint2ndImpactRandomSelect();
        
        setLossTypeRandomSelect();
        
        setAirbagsDeployed();
        
        setOperable();
        
        try {
            setVIN(vehicleObject.getVin());
        } catch (Exception e) {
            System.out.println("Vin is set.");
        }
        sendArbitraryKeys(Keys.ESCAPE);
        
        setOdometerReading(Integer.toString(repository.gw.helpers.NumberUtils.generateRandomNumberInt(1000, 200000)));
        sendArbitraryKeys(Keys.ESCAPE);
        
        setDriverName();
        
        setRelationToInsured();
        clickCCCAppraisalTab();
        
        setMakeRandom();
        
        setVehicleAddressSelect();
        
        try {
            clickEvaluateButton();
        } catch (Exception e) {
            Assert.fail(getAppraisalAssignmentIssues());
        }
        
        waitForAppraiserPopup();
        
        searchForAppraisers();
        
        try {
            clickSendAssignmentToCCC();
        } catch (Exception e) {

        }
        
        try {
            clickUpdateCCCAssignment();
        } catch (Exception e) {

        }

        
        cccReferenceID = getReferenceID();
        clickUpdateButton();

        return cccReferenceID;
    }

    @FindBy(css = "a[id*='VehIncidentDetailDV:VehicleDamage_VehicleSalvageCardTab']")
    private WebElement tabVehicleSalvage;

    private VehicleSalvageTab clickVehicleSalvageTab() {
        waitUtils.waitUntilElementIsClickable(tabVehicleSalvage);
        tabVehicleSalvage.click();
        return new VehicleSalvageTab(getDriver());
    }

    public void addDataForCopart() {

        Vin vehicleObject = new Vin();
        String cccReferenceID = "";

        try {
            vehicleObject = VINHelper.getRandomVIN();
        } catch (Exception e) {
            Assert.fail("Unable to retrieve VIN.");
            e.printStackTrace();
        }

        clickVehicleIsDamaged();

        clickTotalLossYes();

        
        setPoint1stImpactRandomSelect();
        
        setPoint2ndImpactRandomSelect();
        
        setLossTypeRandomSelect();
        
        setAirbagsDeployed();
        
        setOperable();
        
        try {
            setVIN(vehicleObject.getVin());
        } catch (Exception e) {
            System.out.println("Vin is set.");
        }
        sendArbitraryKeys(Keys.ESCAPE);
        
        setOdometerReading(Integer.toString(repository.gw.helpers.NumberUtils.generateRandomNumberInt(1000, 200000)));
        sendArbitraryKeys(Keys.ESCAPE);
        
        setDriverName();
        
        setRelationToInsured();

        VehicleSalvageTab salvage = clickVehicleSalvageTab();
        salvage.createCopartDefaultAssignment();

    }

    @FindBy(css = "table[id*=':VehicleIncidentAppraisalAssignmentDV:0:message']")
    private WebElement appraisalAssignmentIssues;

    private String getAppraisalAssignmentIssues() {
        return appraisalAssignmentIssues.getText();
    }

    @FindBy(css = "div[id*=':CCCExternalReferenceID-inputEl']")
    private WebElement cccReferenceIDElement;

    private String getReferenceID() {
        return cccReferenceIDElement.getText();
    }

    @FindBy(css = "a[id*=':sendAssignmentToCCC']")
    private WebElement sendAssignmentToCCCButton;

    private void clickSendAssignmentToCCC() {
        clickWhenClickable(sendAssignmentToCCCButton);
    }

    @FindBy(css = "a[id*=':resendAssignmentToCCC']")
    private WebElement updateCCCAssignmentButton;

    private void clickUpdateCCCAssignment() {
        clickWhenClickable(updateCCCAssignmentButton);
    }

    private Guidewire8Select searchRadiusSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ProximitySearchType-triggerWrap')]");
    }

    private void setSearchRadius(String miles) {
        searchRadiusSelect().selectByVisibleText(miles);
    }

    @FindBy(css = "input[id*=':ProximityPostalCode-inputEl']")
    private WebElement zipCodeInput;

    private void setZipCode(String zipCode) {
        zipCodeInput.sendKeys(zipCode);
    }

    @FindBy(css = "a[id*=':doCCCActionButton']")
    private WebElement searchButton;

    private void clickSearchButton() {
        clickWhenClickable(searchButton);
    }

    @FindBy(css = "td[id*=':totalLossLbl-bodyEl'] div")
    private WebElement cccRecommendation;

    private String getCCCRecommedation() {
        return cccRecommendation.getText();
    }

    @FindBy(css = "a[id*=':doCCCActionButton']")
    private WebElement confirmTotalLossButton;

    private void clickConfirmTotalLoss() {
        clickWhenClickable(confirmTotalLossButton);
    }

    private void searchForAppraisers() {

        if (!getCCCRecommedation().contains("Total Loss")) {
            setSearchRadius("150");
            setZipCode("83201");
            clickSearchButton();
            selectRandomShop();
        } else {
            clickConfirmTotalLoss();
        }
    }

    private void selectRandomShop() {
        List<WebElement> selectButtons = finds(By.cssSelector("div[id*=':AppraiserResultLV-body'] a"));
        WebElement selectedButton = selectButtons.get(repository.gw.helpers.NumberUtils.generateRandomNumberInt(0, selectButtons.size() - 1));
        selectedButton.click();
    }
}
