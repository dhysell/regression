package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.generate.custom.PLPolicyLocationProperty;

public class GenericWorkorderSquirePropertyDetailProtectionDetails extends GenericWorkorder {
    private WebDriver driver;

    public GenericWorkorderSquirePropertyDetailProtectionDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8Select select_SprinklerSystemType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:SprinklerSystemType-triggerWrap')]");
    }

    Guidewire8RadioButton radio_FireExtinguishers() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:FireExtinguisher-containerEl')]/table");
    }

    Guidewire8RadioButton radio_SmokeAlarm() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:SmokeAlarm-containerEl')]/table");
    }

    Guidewire8RadioButton radio_NonSmoker() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:NonSmoker-containerEl')]/table");
    }

    Guidewire8RadioButton radio_DeadBoltLocks() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:Deadbolt-containerEl')]/table");
    }

    Guidewire8RadioButton radio_DefensibleSpace() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingProtectionDetailsHOEDV:DefensibleSpace-containerEl')]/table");
    }

    /*
     * Methods
     */


    public void setSprinklerSystemType(SprinklerSystemType type) {
        
        select_SprinklerSystemType().selectByVisibleText(type.getValue());
        
    }


    public void setFireExtinguishers(boolean trueFalse) {
        
        radio_FireExtinguishers().select(trueFalse);
        
    }


    public void setSmokeAlarm(boolean trueFalse) {
        
        radio_SmokeAlarm().select(trueFalse);
        
    }

    public boolean getSmokeAlarm(boolean trueFalse) {
        return radio_SmokeAlarm().isSelected(trueFalse);
    }

    public void setNonSmoker(boolean trueFalse) {
        
        radio_NonSmoker().select(trueFalse);
        
    }

    public boolean getNonSmoker(boolean trueFalse) {
        return radio_NonSmoker().isSelected(trueFalse);
    }

    public void setDeadBoltLocks(boolean trueFalse) {
        
        radio_DeadBoltLocks().select(trueFalse);
        
    }

    public boolean getDeadBoltLocks(boolean trueFalse) {
        return radio_DeadBoltLocks().isSelected(trueFalse);
    }

    public void setDefensibleSpace(boolean trueFalse) {
        
        radio_DefensibleSpace().select(trueFalse);
        
    }


    public void clickOK() {
        super.clickOK();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickNext() {
        super.clickNext();
    }


    public void setProtectionPageQQ(PLPolicyLocationProperty property) {
        setSmokeAlarm(property.getSmokeAlarms());
        setNonSmoker(property.getNonSmoker());
        setDeadBoltLocks(property.getDeadboltLocks());
        
    }


    public void editProtectionPageFA(PLPolicyLocationProperty property) {
        setSprinklerSystemType(property.getSprinkerSystem());
        setFireExtinguishers(property.getFireExtinguishers());
        setDefensibleSpace(property.getDefensibleSpace());
    }
}
