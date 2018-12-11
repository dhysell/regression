package repository.pc.workorders.change;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.helpers.TableUtils;
import repository.pc.workorders.generic.GenericWorkorder;


public class PolicyChangeQuote extends GenericWorkorder {

	private TableUtils tableUtils;
	
    public PolicyChangeQuote(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, 'PolicyChangeWizard:PolicyChangeWizard_QuoteScreen:RatingTxDetailsPanelSet:2')]")
    private WebElement table_CostChangeDetail;

    @FindBy(xpath = "//a[contains(@id,'PolicyChangeWizard_Quote_TransactionCardTab')]")
    private WebElement tab_CostChangeDetail;


    public void clickCostChangeDetailsTab() {
        waitUntilElementIsClickable(tab_CostChangeDetail);
        tab_CostChangeDetail.click();
    }


    public String getCostChangeDetailsAmount(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeDetail, row, headerName);
    }


    public String getCostChangeDetailsAmountByCoverageType(String coverageType, String headerName) {
        int row = tableUtils.getRowNumberInTableByText(table_CostChangeDetail, coverageType);

        return tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeDetail, row, headerName);
    }


}
