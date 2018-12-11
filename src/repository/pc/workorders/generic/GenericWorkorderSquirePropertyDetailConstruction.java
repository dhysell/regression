package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.RoofType;
import repository.gw.generate.custom.PLPolicyLocationProperty;

public class GenericWorkorderSquirePropertyDetailConstruction extends GenericWorkorder {

	private WebDriver driver;

    public GenericWorkorderSquirePropertyDetailConstruction(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8Checkbox check_SquirePropertyHeatingUpgrade() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:HeatingUpgrade')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:HeatingUpgradeDate-inputEl')]")
    public WebElement editbox_SquirePropertyConstructionHeatingUpgradedYear;

    Guidewire8Checkbox check_SquirePropertyPlumbingUpgrade() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PlumbingUpgrade')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:PlumbingUpgradeDate-inputEl')]")
    public WebElement editbox_SquirePropertyConstructionPlumbingUpgradedYear;

    Guidewire8Checkbox check_SquirePropertyRoofingUpgrade() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:RoofingUpgrade-inputEl')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:RoofingUpgradeDate-inputEl')]")
    public WebElement editbox_SquirePropertyConstructionRoofingUpgradedYear;

    Guidewire8Checkbox check_SquirePropertyWiringUpgrade() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ElectricalSystemUpgrade-inputEl')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ElectricalUpgradeDate-inputEl')]")
    public WebElement editbox_SquirePropertyConstructionWiringUpgradedYear;

    @FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:DwellingSingleProtectionIdTab-btnEl')]")
    public WebElement link_SquirePropertyProtectionDetails;

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:HODwellingMSPhotoYearsInputSet:CostEstimatorYear-inputEl']")
    public WebElement editbox_MSYear;

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:HODwellingMSPhotoYearsInputSet:PhotoYear-inputEl']")
    public WebElement editbox_PhotoYear;

    @FindBy(xpath = "//input[@id='HOBuilding_FBMPopup:HODwellingConstructionDetailsHOEDV:ShedDescription-inputEl']")
    public WebElement editbox_ShedDescription;

    @FindBy(css = "input[id$=':YearBuilt-inputEl']")
    private WebElement input_YearBuilt;

    private Guidewire8Select select_ConstructionType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ConstructionType-triggerWrap')]");
    }

    @FindBy(css = "input[id$=':ApproxSqFoot-inputEl']")
    private WebElement input_SizeSqureFootage;

    private Guidewire8Select select_FoundationType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':FoundationType-triggerWrap')]");
    }

    private Guidewire8Select select_RoofType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':RoofType-triggerWrap')]");
    }


    public void setYearBuilt(int yearBuilt) {
        setText(input_YearBuilt, String.valueOf(yearBuilt));
    }

    public void setConstructionType(ConstructionTypePL constructionType) {
        select_ConstructionType().selectByVisibleText(constructionType.getValue());
    }

    public void setSquareFootage(int footage) {
        setText(input_SizeSqureFootage, String.valueOf(footage));
    }

    public void setFoundationType(FoundationType type) {
        select_FoundationType().selectByVisibleText(type.getValue());
    }

    public void setRoofType(RoofType type) {
        select_RoofType().selectByVisibleText(type.getValue());
    }
//     shopConstructionPage.setYearBuilt(property.getYearBuilt());
//        shopConstructionPage.setConstructionType(property.getConstructionType());
//        shopConstructionPage.setSquareFootage(property.getSquareFootage());
//        shopConstructionPage.setFoundationType(property.getFoundationType());
//        shopConstructionPage.setRoofType(property.getRoofType());
//        shopConstructionPage.setPolyurethane(property.getPolyurethane());
//        shopConstructionPage.setPolyurethaneSandwiched(property.getSandwichedPolyurethane());
//        shopConstructionPage.setPolyurethaneSandwichedDescription(property.getSandwichedPolyurethaneDescription());


    public void setMSYear(String msYear) {
        editbox_MSYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_MSYear.sendKeys(msYear);
        clickProductLogo();
    }


    public void setPhotoYear(String photoYear) {
        editbox_PhotoYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_PhotoYear.sendKeys(photoYear);
        clickProductLogo();
    }


    public void setShedDescription(String desc) {
        waitUntilElementIsVisible(editbox_ShedDescription);
        editbox_ShedDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ShedDescription.sendKeys(desc);
        clickProductLogo();
    }
    
    public boolean MSPhotoYearEditable() {
    	return editbox_MSYear.isEnabled();
    }
    
    public boolean PhotoYearEditable() {
    	return editbox_PhotoYear.isEnabled();
    }


    public boolean MSPhotoYearExists() {
        try {
            boolean found = editbox_MSYear.isDisplayed();
            if (!found)
                found = editbox_PhotoYear.isDisplayed();
            return found;
        } catch (Exception e) {
            return false;
        }
    }


    public void setHeatingUpgrade(boolean trueFalse) {
        check_SquirePropertyHeatingUpgrade().select(trueFalse);
    }


    public void setHeatingUpgradeYear(int upgradeYear) {
        editbox_SquirePropertyConstructionHeatingUpgradedYear.sendKeys(Integer.toString(upgradeYear));
    }


    public void setPlumbingUpgrade(boolean trueFalse) {
        check_SquirePropertyPlumbingUpgrade().select(trueFalse);
    }


    public void setPlumbingUpgradeYear(int upgradeYear) {
        editbox_SquirePropertyConstructionPlumbingUpgradedYear.sendKeys(Integer.toString(upgradeYear));
    }


    public void setRoofingUpgrade(boolean trueFalse) {
        check_SquirePropertyRoofingUpgrade().select(trueFalse);
    }


    public void setRoofingUpgradeYear(int upgradeYear) {
        editbox_SquirePropertyConstructionRoofingUpgradedYear.sendKeys(Integer.toString(upgradeYear));
    }


    public void setWiringUpgrade(boolean trueFalse) {
        check_SquirePropertyWiringUpgrade().select(trueFalse);
    }


    public void setWiringUpgradeYear(int upgradeYear) {
        editbox_SquirePropertyConstructionWiringUpgradedYear.sendKeys(Integer.toString(upgradeYear));
    }


    public void clickOK() {
        clickOK();
    }


    public void clickCancel() {
        clickCancel();
    }


    public void clickProtectionDetails() {
        clickWhenClickable(link_SquirePropertyProtectionDetails);
    }


    public boolean isProtectionDetailsTabExists() {
        try {
            return checkIfElementExists(link_SquirePropertyProtectionDetails, 200);
        } catch (Exception e) {
            return false;
        }
    }


    public void setCoverageAPropertyDetailsQQ(PLPolicyLocationProperty property) {

    }//END setCoverageAPropertyDetailsQQ(PLPolicyLocationProperty property)


    public void setCoverageAPropertyDetailsFA(PLPolicyLocationProperty property) {

    }//END setCoverageAPropertyDetailsFA(PLPolicyLocationProperty property)


        }
