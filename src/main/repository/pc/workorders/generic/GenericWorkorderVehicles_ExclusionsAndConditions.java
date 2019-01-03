package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderVehicles_ExclusionsAndConditions extends GenericWorkorderVehicles {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderVehicles_ExclusionsAndConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }


    public void fillOutExclusionsAndConditions_QQ(Vehicle vehicle) {

    }

    public void fillOutExclusionsAndConditions_FA(Vehicle vehicle) {

    }

    public void fillOutExclusionsAndConditions(Vehicle vehicle) {
        clickExclusionsAndConditionsTab();
        setDeletionOfMaterial310(true);
        clickAdd();
        selectLocationOfDamageDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
        selectDamagedItemDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
        selectDamageDescriptionDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
    }


    private Guidewire8Checkbox checkbox_DeletionMaterial310() {
        return new Guidewire8Checkbox(driver, "//div[contains(., 'Deletion of Material Damage Coverage Endorsement 310')]/ancestor::fieldset/descendant::table");
    }

    public void setDeletionOfMaterial310(boolean checked) {
        Guidewire8Checkbox newCheck = checkbox_DeletionMaterial310();
        newCheck.select(checked);
        
    }

    private Guidewire8Checkbox checkbox_ShowCarDriverExclusion303() {
        return new Guidewire8Checkbox(driver, "//div[contains(., 'Show Car Driver Exclusion Endorsement 303')]/ancestor::fieldset/descendant::table");
    }

    @FindBy(xpath = "//span[contains(@id, 'PAVehiclePopup:GenericExclusionCondition_ExtPanelSet:ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:Add-btnEl')]")
    private WebElement button_ShowCarDriverExclusionAdd;
    @FindBy(xpath = "//div[contains(@id, 'PAVehiclePopup:GenericExclusionCondition_ExtPanelSet:ExclDV:0:LineExcl:CoverageInputSet:CovPatternInputGroup:0')]")
    private WebElement table_ShowCarDriverExclusion;

    public void addShowCarDriverExclusionEndorsement303(String insFirstName) {
        Guidewire8Checkbox newCheck = checkbox_ShowCarDriverExclusion303();
        newCheck.select(true);
        
        clickWhenClickable(button_ShowCarDriverExclusionAdd);
        
        tableUtils.selectValueForSelectInTable(table_ShowCarDriverExclusion, 1, "Policy Member Name", insFirstName);
    }

    @FindBy(xpath = "//div[contains(text(),'Deletion of Material Damage Coverage Endorsement 310')]/ancestor::fieldset")
    private WebElement table_DeletionOfMaterialDamageCoverageEndorsement_310;

    private void selectLocationOfDamageDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
        tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Location Of Damage"), "Location Of Damage", "c1", vehicle.getLocationOfDamage());
        
    }


    private void selectDamagedItemDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
        tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Damaged Item"), "Damaged Item", "c2", vehicle.getDamageItem());
        
    }


    private void selectDamageDescriptionDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
        tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Damage Description"), "Damage Description", "c3", vehicle.getDamageDescription());
        
    }


}


















