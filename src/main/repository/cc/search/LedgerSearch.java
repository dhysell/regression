package repository.cc.search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class LedgerSearch extends BasePage {

    private WaitUtils waitUtils;

    public LedgerSearch(WebDriver webDriver) {
        super(webDriver);
        this.waitUtils = new WaitUtils(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public void sendAllFieldCheckOrDraftToEAS() {
        setSearchForDate("Last 30 Days");
        setTransactionType("Field Checks and Drafts");
        clickPageHeader();
        clickSearchButton();
        clickAllSendToEASButtons();
    }

    private Guidewire8Select searchForDate() {
        return new Guidewire8Select(getDriver(), "//table[contains(@id,'DateRangeChoice-triggerWrap')]");
    }

    private void setSearchForDate(String option) {
        waitUtils.waitUntilElementIsVisible(By.xpath("//table[contains(@id,'DateRangeChoice-triggerWrap')]"));
        searchForDate().selectByVisibleText(option);
    }

    private Guidewire8Select transactionType() {
        return new Guidewire8Select(getDriver(), "//table[contains(@id,'TransactionType-triggerWrap')]");
    }

    private void setTransactionType(String option) {
        waitUtils.waitUntilElementIsVisible(By.xpath("//table[contains(@id,'TransactionType-triggerWrap')]"));
        transactionType().selectByVisibleText(option);
    }

    @FindBy(css = "span[id*='LedgerSearch:ttlBar']")
    private WebElement pageHeader;

    private void clickPageHeader() {
        waitUtils.waitUntilElementIsClickable(pageHeader, 15);
        pageHeader.click();
    }

    @FindBy(css = "a[class='bigButton']")
    private WebElement buttonSearch;

    private void clickSearchButton() {
        waitUtils.waitUntilElementIsClickable(buttonSearch, 10);
        buttonSearch.click();
    }

    private void clickAllSendToEASButtons() {
        waitUtils.waitUntilElementIsVisible(pageHeader, 15);
        pageHeader.click();
        List<WebElement> sendEASButtons = getDriver().findElements(By.cssSelector("a[id*=':SendToEAS']"));

        while (sendEASButtons.size() > 0) {
            sendEASButtons = getDriver().findElements(By.cssSelector("a[id*=':SendToEAS']"));
            try {
                waitUtils.waitUntilElementIsClickable(sendEASButtons.get(0));
                sendEASButtons.get(0).click();
            } catch (Exception e) {
                System.out.println("Detected Button Unavailable.");
            }
            sendEASButtons.remove(0);
        }
    }

    public void clickClaimNumberLink(String claimNumber) {
        waitUtils.waitUntilElementIsVisible(pageHeader, 15);
        pageHeader.click();

        List<WebElement> links;
        String xpathString = "//a[contains(text(),'"+claimNumber+"')]";

        try {
            links = finds(By.xpath(xpathString));
        } catch(Exception e) {
            pageHeader.click();
            links = finds(By.xpath(xpathString));
        }

        if (links.size() > 0) {
            links.get(0).click();
        } else {
            Assert.fail("!!! No available checks !!!");
        }

    }
}
