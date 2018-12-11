package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireInlandMarine_CoverageSelection extends GenericWorkorder {

    public GenericWorkorderSquireInlandMarine_CoverageSelection(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, ':LineWizardStepSet:IMWizardStepGroup:CoverageSelectionScreen:') and contains(@id, 'LineSelectionDV')]")
    private WebElement table_CoverageSelection;

    public void checkCoverage(boolean checked, String coverageName) {
        new TableUtils(getDriver()).setCheckboxInTableByText(table_CoverageSelection, coverageName, checked);
    }
}
