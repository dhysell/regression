package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.enums.LivestockType;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireInlandMarine_LivestockList extends BasePage {

	private TableUtils tableUtils;
	
    public GenericWorkorderSquireInlandMarine_LivestockList(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, ':LivestockScreen:LivestockPanelSet:CoverableLV')]")
    private WebElement table_Livestock;

    @FindBy(xpath = "//div[contains(@id, ':LivestockScreen:LivestockPanelSet:CoverableDetailsCV:LivestockDetailsDV:limitPackageTerm-inputEl')]")
    private WebElement input_Limit;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:LivestockScreen:LivestockPanelSet:CoverableLV-body')]/div/table/tbody/tr/td/div[. = 'Livestock']/parent::td/following-sibling::td[1]/div")
    private WebElement text_liveStockLineItemLimit;


    public void clickAdd() {
        super.clickAdd();
    }


    public void removeExistingLiveStock(int i) {
        tableUtils.setCheckboxInTable(table_Livestock, i, true);
        super.clickRemove();
    }


    public void highLihtLivestockByType(LivestockType deathoflivestock) {
        int row = tableUtils.getRowNumberInTableByText(table_Livestock, deathoflivestock.getValue());
        tableUtils.clickRowInTableByRowNumber(table_Livestock, row);
    }


    public String getLimit() {
        return input_Limit.getText();
    }


    public String getLineItemLivestockLimit() {
        waitUntilElementIsVisible(text_liveStockLineItemLimit);
        return text_liveStockLineItemLimit.getText();
    }
}
