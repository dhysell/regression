package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class GenericWorkorderCommercialInlandMarine_CoveragePartSection extends BasePage {

    public GenericWorkorderCommercialInlandMarine_CoveragePartSection(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id, 'RemoveButton-btnInnerEl')]")
    private WebElement button_RemoveCoveragePart;

    @FindBy(xpath = "//span[contains(@id, ':IMCoveragePartTab-btnInnerEl')]")
    private WebElement link_CoveragesPartSectionTab;

    @FindBy(xpath = "//span[contains(@id, ':IMExclAndCondTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    @FindBy(xpath = "//div[contains(@id, ':IMPartSelectionScreen:IMCoveragePartSelectionDV')]/table")
    private WebElement table_CoveragesPart;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------
	
	/*private void clickRemoveCoveragePart() {
		genericHelpers.waitUntilElementIsClickable(button_RemoveCoveragePart);
		button_RemoveCoveragePart.click();
	}
	
	private void clickCoveragesPartSectionTab() {
		genericHelpers.waitUntilElementIsClickable(link_CoveragesPartSectionTab);
		button_RemoveCoveragePart.click();
	}
	
	private void clickExclusionsAndConditionsTab() {
		genericHelpers.waitUntilElementIsClickable(link_ExclusionsAndConditionsTab);
		button_RemoveCoveragePart.click();
	}*/


}
