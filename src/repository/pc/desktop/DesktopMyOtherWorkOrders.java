package repository.pc.desktop;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

public class DesktopMyOtherWorkOrders extends BasePage {
    private WebDriver driver;
    public DesktopMyOtherWorkOrders(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }



    @FindBy(xpath = "//div[contains(@id,'DesktopOtherWorkOrders:DesktopOtherWorkOrdersScreen:DesktopOtherWorkOrdersLV')]")
    public WebElement table_DesktopMyOtherPolicyTransactions;

    public void setDesktopMyOtherWorkOrdersFilter(String statusToSelect) {
        waitUntilElementIsVisible(By.xpath("//table[contains(@id,'DesktopOtherWorkOrdersLV:workOrdersFilter-triggerWrap')]"));
        new Guidewire8Select(driver,"//table[contains(@id,'DesktopOtherWorkOrdersLV:workOrdersFilter-triggerWrap')]").selectByVisibleText(statusToSelect);
    }

    public String getTypeByTransactionNum(String transactionNum) {
        return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_DesktopMyOtherPolicyTransactions, new TableUtils(getDriver()).getRowNumberInTableByText(table_DesktopMyOtherPolicyTransactions, transactionNum), "Type");
    }

    public String getJobInitiatedByTransactionNum(String transactionNum) {
        return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_DesktopMyOtherPolicyTransactions, new TableUtils(getDriver()).getRowNumberInTableByText(table_DesktopMyOtherPolicyTransactions, transactionNum), "Job Initiated By");
    }

    public String getUnderUWReviewByTransactionNum(String transactionNum) {
        return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_DesktopMyOtherPolicyTransactions, new TableUtils(getDriver()).getRowNumberInTableByText(table_DesktopMyOtherPolicyTransactions, transactionNum), "Under UW Review");
    }
}