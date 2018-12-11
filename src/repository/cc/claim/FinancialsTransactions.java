package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class FinancialsTransactions extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    private WaitUtils waitUtils;

    public FinancialsTransactions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,':ClaimFinancialsTransactionsDetail_UpLink')]")
    public WebElement link_UpToFinancials;

    @FindBy(xpath = "//div[@id='ClaimFinancialsTransactions:ClaimFinancialsTransactionsScreen:TransactionsLV']")
    public WebElement table_Transactions;

    public void clickUpToFinancialsLink() {
        clickWhenClickable(link_UpToFinancials);
    }

    public String getCostCategory(int rowNum) {

        String categoryText = null;

        waitUtils.waitUntilElementIsVisible(By.xpath("//div[@id='ClaimFinancialsTransactions:ClaimFinancialsTransactionsScreen:TransactionsLV-body']//td[5]"), 10);

        List<WebElement> costCat = finds(By.xpath("//div[@id='ClaimFinancialsTransactions:ClaimFinancialsTransactionsScreen:TransactionsLV-body']//td[5]"));
        if (costCat.size() > 0) {
            categoryText = costCat.get(rowNum).getText();
        } else {
            Assert.fail(getCurrentUrl() + "There didnt' seem to be any reserves here, please make sure that a reserve had been created");
        }

        return categoryText;
    }

    // Elements
    public Guidewire8Select select_TransActionType() {
        return new Guidewire8Select(driver, "//table[@id='ClaimFinancialsTransactions:ClaimFinancialsTransactionsScreen:TransactionsLVRangeInput-triggerWrap']");
    }

    public void selectRandomTransactionType() {
        Guidewire8Select myselect = select_TransActionType();
        myselect.selectByVisibleTextRandom();
    }

    // Helpers
    public void selectReserves() {
        select_TransActionType().selectByVisibleText("Reserves");
    }

    public void selectSpecificTransactionType(String type) {
        select_TransActionType().selectByVisibleText(type);
    }

    public long getReserveAmountUsingCostCategoryName(String costCategory) {
        int rowWithCostCat = tableUtils.getRowNumberInTableByText(table_Transactions, costCategory);
        String reserveAmount = tableUtils.getCellTextInTableByRowAndColumnName(table_Transactions, rowWithCostCat, "Amount");
        reserveAmount = reserveAmount.replace("$", "");
        reserveAmount = reserveAmount.replace(",", "");
        return (long) Double.parseDouble(reserveAmount);

    }

    @FindBy(css = "div[id*='ClaimFinancialsTransactions:ClaimFinancialsTransactionsScreen:TransactionsLV']")
    private WebElement recoveriesTable;

    public RecoveryDetails selectRecovery() {
        TableUtils table = new TableUtils(getDriver());
        // ClaimFinancialsTransactions
        table.clickLinkInTableByRowAndColumnName(recoveriesTable, 1, "Amount");
        return new RecoveryDetails(getDriver());
    }
}
