package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardIMCoverageSelection extends GenericWorkorder {

    public GenericWorkorderStandardIMCoverageSelection(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, ':LineWizardStepSet:IMWizardStepGroup:CoverageSelectionScreen:') and contains(@id, 'LineSelectionDV') or contains(@id, 'CoverageSelectionScreen:CPPLineSelectionDV')]")
    private WebElement table_CoverageSelection;


    public void clickNext() {
        super.clickNext();
    }


    public void checkCoverage(boolean checked, String coverageName) {
        new TableUtils(getDriver()).setCheckboxInTableByText(table_CoverageSelection, coverageName, checked);
    }


    public void fillOutCoverageSelection(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(getDriver());
        sideMenu.clickSideMenuStandardIMCoverageSelection();
        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(getDriver());
        for (InlandMarine im : policy.standardInlandMarine.inlandMarineCoverageSelection_Standard_IM) {
            imSelection.checkCoverage(true, im.getValue());
        }//END FOR
    }//END fillOutCoverageSelection

}
