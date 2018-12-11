package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;

public class GenericWorkorderSquireAutoCoverages extends GenericWorkorder {

    public GenericWorkorderSquireAutoCoverages(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutSquireAutoCoverages(GeneratePolicy policy) {
        fillOutSquireAutoCoverages_Coverages(policy);
        fillOutSquireAutoCoverages_AdditionalCoverages(policy);
        fillOutSquireAutoCoverages_ExclusionsAndConditions(policy);
    }

    public void fillOutSquireAutoCoverages_Coverages(GeneratePolicy policy) {
        GenericWorkorderSquireAutoCoverages_Coverages coverage = new GenericWorkorderSquireAutoCoverages_Coverages(getDriver());
        coverage.fillOutCoveragesTab(policy);
    }

    public void fillOutSquireAutoCoverages_AdditionalCoverages(GeneratePolicy policy) {
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalCoverages = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(getDriver());
        additionalCoverages.fillOutAdditionalCoverages(policy);
    }

    public void fillOutSquireAutoCoverages_ExclusionsAndConditions(GeneratePolicy policy) {
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions exclusionsConditions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(getDriver());
        exclusionsConditions.fillOutConditions(policy);
    }

    @FindBy(xpath = "//a[contains(@id, 'PersonalAutoScreen:PersonalAutoCoveragesPanelSet:CoveragesTab')]")
    private WebElement tab_lineCoverages;

    public void clickCoverageTab() {
        clickWhenClickable(tab_lineCoverages);
    }

    @FindBy(xpath = "//a[contains(@id, 'PersonalAutoScreen:PersonalAutoCoveragesPanelSet:AdditionalCoveragesTab')]")
    private WebElement tab_lineAdditionalCoverages;

    public void clickAdditionalCoveragesTab() {
        clickWhenClickable(tab_lineAdditionalCoverages);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(text(), 'Additional Coverages - Personal Auto Section')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'PersonalAutoScreen:PersonalAutoCoveragesPanelSet:exclusionsAndConditionsCardTab')]")
    private WebElement tab_lineExclusions;

    public void clickCoverageExclusionsTab() {
        clickWhenClickable(tab_lineExclusions);
    }


}
