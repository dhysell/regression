package repository.pc.workorders.generic;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails extends GenericWorkorderSquirePropertyAndLiabilityPropertyDetail {


    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillOutPropertyProtectionDetails_QQ(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        clickProtectionDetailsTab();
//		setSmokeAlarm(property.getSmokeAlarms());
//		setNonSmoker(property.getNonSmoker());
//		setDeadBoltLocks(property.getDeadboltLocks());
    }

    public void fillOutPropertyProtectionDetails_FA(PLPolicyLocationProperty property) throws GuidewireNavigationException {
        if (!property.getpropertyType().equals(PropertyTypePL.Shed) 
        		&& !property.getpropertyType().equals(PropertyTypePL.Barn)
                && !property.getpropertyType().equals(PropertyTypePL.DwellingPremisesCovE) 
                && !property.getpropertyType().equals(PropertyTypePL.ResidencePremisesCovE)
                && !property.getpropertyType().equals(PropertyTypePL.VacationHomeCovE) 
                && !property.getpropertyType().equals(PropertyTypePL.DwellingUnderConstructionCovE)
                && !property.getpropertyType().equals(PropertyTypePL.Garage)
                && !property.getpropertyType().equals(PropertyTypePL.FarmOffice)
                && !property.getpropertyType().equals(PropertyTypePL.CondoVacationHomeCovE)
                && !property.getpropertyType().equals(PropertyTypePL.Contents)) {
            clickProtectionDetailsTab();
            setSprinklerSystemType(property.getSprinkerSystem());
            setFireExtinguishers(property.getFireExtinguishers());
            setDefensibleSpace(property.getDefensibleSpace());
        }
	}

    public void fillOutPropertyProtectionDetails(PLPolicyLocationProperty property) throws GuidewireNavigationException {
		isOnProperty(property);
		if (!property.getpropertyType().equals(PropertyTypePL.AlfalfaMill)) {
        clickProtectionDetailsTab();
        setSmokeAlarm(property.getSmokeAlarms());
        setNonSmoker(property.getNonSmoker());
        setDeadBoltLocks(property.getDeadboltLocks());
        setSprinklerSystemType(property.getSprinkerSystem());
        setFireExtinguishers(property.getFireExtinguishers());
        setDefensibleSpace(property.getDefensibleSpace());
    }
	}


	private void isOnProperty(PLPolicyLocationProperty property) {
		if(finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Property Information')]")).isEmpty()) {
			if(finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Property Detail')]")).isEmpty()) {
				new SideMenuPC(driver).clickSideMenuSquirePropertyDetail();//get to property wizard step
				clickViewOrEditBuildingButton(property.getPropertyNumber());
			} else {
				clickViewOrEditBuildingButton(property.getPropertyNumber());
				//now on property. move on
			}
		} else {
			//do nothing. assumed to be on right property
		}
	}

    public void setSmokeAlarm(boolean trueFalse) {
        radio_SmokeAlarm().select(trueFalse);
    }

    public void setNonSmoker(boolean trueFalse) {
        radio_NonSmoker().select(trueFalse);
    }

    public void setDeadBoltLocks(boolean trueFalse) {
        radio_DeadBoltLocks().select(trueFalse);
    }

    public void setSprinklerSystemType(SprinklerSystemType type) {
        select_SprinklerSystemType().selectByVisibleText(type.getValue());
    }

    public void setFireExtinguishers(boolean trueFalse) {
        radio_FireExtinguishers().select(trueFalse);
    }

    public void setDefensibleSpace(boolean trueFalse) {
        radio_DefensibleSpace().select(trueFalse);
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
    
    @FindBy(css = "div[id*='DefensibleSpace-inputEl']")
    public WebElement label_DefensibleSpaceMaintained;


}
