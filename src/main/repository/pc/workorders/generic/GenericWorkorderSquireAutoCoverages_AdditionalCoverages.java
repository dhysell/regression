package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.DocFormEvents;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquireAutoCoverages_AdditionalCoverages extends GenericWorkorderSquireAutoCoverages {
	
	private WebDriver driver;

    public GenericWorkorderSquireAutoCoverages_AdditionalCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillOutAdditionalCoverages(GeneratePolicy policy) {
        clickAdditionalCoveragesTab();
        policy.squire.squirePA.getCoverages().setAccidentalDeathCount(setAccidentalDeath(policy.squire.squirePA.getCoverages().hasAccidentalDeath()));
        setDriveOtherCar(policy.squire.squirePA.getCoverages().hasDriveOtherCar());

        if (policy.squire.squirePA.getCoverages().hasDriveOtherCar()) {
            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_Auto_DriveOtherCar);
        }
    }

    private String additionalCoveragesTableXpath = "//div[contains(@id, 'PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:PALineAdditionalCoveragesDV_ref')]/table";

    public String setAccidentalDeath(boolean hasCoverage) {
        Guidewire8Checkbox myCheckbox = new Guidewire8Checkbox(driver, "//div[contains(text(), 'Accidental Death')]/preceding-sibling::table");
        myCheckbox.select(hasCoverage);
		if(hasCoverage) {
			waitUntilElementIsVisible(textbox_accidentalDeathCount);
			return getAccidentalDeathCount();
    }
		return null;
    }


    public boolean getAccidentalDeath() {
        Guidewire8Checkbox tableCheckbox = new Guidewire8Checkbox(driver, additionalCoveragesTableXpath + "//legend//*[contains(text(),'Accidental Death')]/ancestor::div/table[contains(@class, 'checkbox')]");
        return tableCheckbox.isSelected();
    }

    @FindBy(xpath = "//div[text()='Accidental Death']/ancestor::legend/following-sibling::div/descendant::label[text()='Count']/parent::td/following-sibling::td/div")
    private WebElement textbox_accidentalDeathCount;
    public String getAccidentalDeathCount() {
    	return textbox_accidentalDeathCount.getText();
    }
    public void setDriveOtherCar(boolean hasCoverage) {
        Guidewire8Checkbox myCheckbox = new Guidewire8Checkbox(driver, "//div[contains(text(), 'Drive Other Car')]/preceding-sibling::table");
        myCheckbox.select(hasCoverage);
//        setCheckboxInFieldsetByText(additionalCoveragesTableXpath, "Drive Other Car", hasCoverage);
    }


}
