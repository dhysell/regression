package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;

public class GenericWorkorderSquireIMExclusionsConditions extends GenericWorkorder {

    private WebDriver driver;

    public GenericWorkorderSquireIMExclusionsConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Checkbox checkbox_PAExclusionsSpecialEndorsementForIM405() {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'Special Endorsement for Inland Marine 405')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//div[contains(., 'Special Endorsement for Inland Marine 405')]/ancestor::legend/following-sibling::div/descendant::table/descendant::span[contains(@id,':LineCond:CoverageInputSet:CovPatternInputGroup:Add-btnEl')]")
    private WebElement button_IMConditionsSpecialEndorsement405Add;

    @FindBy(xpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:0-body')]/div/table/tbody/tr/td[2]/div")
    private WebElement text_IMConditionsSpecialEndorsement405Desc;

    @FindBy(xpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:0-body')]/following-sibling::div/table/tbody/tr/td[2]/textarea")
    private WebElement textarea_IMConditionsSpecialEndorsement405Desc;

    @FindBy(xpath = "//div[contains(@id, 'policyLevelExclusionsAndConditions:GenericExclusionCondition_ExtPanelSet:CondDV:0:LineCond:CoverageInputSet:CovPatternInputGroup:ScheduleLV')]")
    private WebElement table_ConditionsSpecialEndorsement405;


    public void addSpecialEndorsementForIM405(String description) {
        checkbox_PAExclusionsSpecialEndorsementForIM405().select(true);
        clickWhenClickable(button_IMConditionsSpecialEndorsement405Add);
        clickWhenClickable(text_IMConditionsSpecialEndorsement405Desc);
        textarea_IMConditionsSpecialEndorsement405Desc.sendKeys(description);
        textarea_IMConditionsSpecialEndorsement405Desc.sendKeys(Keys.TAB);
    }
}
