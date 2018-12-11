package repository.cc.claim.searchpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.CheckDetails;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class SearchChecksCC extends BasePage {

    private WebDriver driver;

    public SearchChecksCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//input[contains(@id, ':ClaimNumber-inputEl')]")
    public WebElement input_ClaimNumber;

    public Guidewire8Select select_ApprovedByGroup() {
        return new Guidewire8Select(driver,"//table[contains(@id,':ApprovedByGroup-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':CheckNumber-inputEl')]")
    public WebElement input_CheckNumber;

    @FindBy(xpath = "//a[contains(@id, 'SearchLinksInputSet:Search')]")
    public WebElement button_Search;

    public Guidewire8Select select_CheckStatus() {
        return new Guidewire8Select(driver,"//table[contains(@id, ':Status-triggerWrap')]");
    }

    public Guidewire8Select select_DateSearchRange() {
        return new Guidewire8Select(driver,"//table[contains(@id, ':DateSearchRangeValue-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id,':SearchLinksInputSet:Reset')]")
    public WebElement button_Reset;


    // Helper Methods

    public WebElement getCheckLink(int rowNumber) {
        return find(By.xpath("//a[contains(@id, 'CheckSearchResultsLV:" + (rowNumber - 1) + ":CheckNumber')]"));
    }

    public String getPayTo(int rowNumber) {
        return find(By.xpath("//a[contains(@id, 'CheckSearchResultsLV:" + (rowNumber - 1) + ":PayTo')]")).getText();
    }

    public String getAmount(int rowNumber) {
        return find(By.xpath("//a[contains(@id, 'CheckSearchResultsLV:" + (rowNumber - 1) + ":Amount')]")).getText();
    }

    public String getClaim(int rowNumber) {
        return find(By.xpath("//a[contains(@id, 'CheckSearchResultsLV:" + (rowNumber - 1) + ":Claim')]")).getText();
    }

    public void searchVoidedChecks(String approvedBy, String checkStatus, String searchRange) {

        
        selectSpecific_ApprovedByGroup(approvedBy);
        
        selectSpecific_CheckStatus(checkStatus);
        
        selectSpecific_DateSearchRange(searchRange);
        
        clickSearchButton();
        
    }

    public repository.cc.claim.CheckDetails searchRegularChecks() {
        selectSpecific_ApprovedByGroup("Examiners");
        selectSpecific_CheckStatus("Issued");
        clickSearchButton();

        clickRandomCheckNumber();
        return new CheckDetails(getDriver());
    }

    public void selectSpecific_ApprovedByGroup(String group) {
        Guidewire8Select mySelect = select_ApprovedByGroup();
        
        mySelect.selectByVisibleTextPartial(group);
    }

    public void sendClaimNumber(String claimNum) {
        input_ClaimNumber.sendKeys(claimNum);
    }

    public void sendCheckNumber(String checkNum) {
        input_CheckNumber.sendKeys(checkNum);
    }

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void selectSpecific_CheckStatus(String status) {
        Guidewire8Select mySelect = select_CheckStatus();
        
        mySelect.selectByVisibleTextPartial(status);
    }

    public void selectSpecific_DateSearchRange(String range) {
        Guidewire8Select mySelect = select_DateSearchRange();
        mySelect.selectByVisibleTextPartial(range);
    }

    public void clickResetButton() {
        clickWhenClickable(button_Reset);
    }

    @FindBy(css = "div[id*='PaymentSearch:PaymentSearchScreen:PaymentSearchLV-body'] div table")
    private WebElement searchResultsTable;

    private void clickRandomCheckNumber() {

        waitUntilElementIsVisible(searchResultsTable, 15);

        List<WebElement> checkLinks = searchResultsTable.findElements(By.cssSelector(
                "div[id*='PaymentSearch:PaymentSearchScreen:PaymentSearchLV-body'] div table tr td:nth-child(1) a"));

        int size = checkLinks.size();
        int randNum = NumberUtils.generateRandomNumberInt(0, size - 1);

        checkLinks.get(randNum).click();
        waitUntilElementIsNotVisible(checkLinks.get(randNum));
    }

}
