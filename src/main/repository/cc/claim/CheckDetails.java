package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.PopUpWindow;
import repository.gw.helpers.WaitUtils;


public class CheckDetails extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public CheckDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(getDriver());
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//a[contains(@id, '_VoidStopButton')]")
    public WebElement button_VoidStop;

    @FindBy(xpath = "//a[contains(@id, '_TransferButton')]")
    public WebElement button_Transfer;

    @FindBy(xpath = "//a[contains(@id,'VoidStopCheck_VoidButton')]")
    public WebElement button_VoidCheck;

    @FindBy(xpath = "//a[contains(@id, ':Cancel')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//input[contains(@id,'VoidStopCheckDV:Comments-inputEl')]")
    public WebElement input_ReasonForVoid;

    public void clickVoidStopButton() {
        clickWhenClickable(button_VoidStop);
    }

    public void clickTransferButton() {
        clickWhenClickable(button_Transfer);
    }

    public void clickVoidCheckButton() {
        clickWhenClickable(button_VoidCheck);
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void sendReasonVoid(String reason) {
        input_ReasonForVoid.sendKeys(reason);
    }

    public void validateVoidedName(ClaimsUsers user) {
        waitUtils.waitUntilElementNotClickable(button_VoidCheck, 10);
        String voidedBy = find(new By.ByCssSelector("div[id*='CheckDV:VoidedBy-inputEl']")).getText();
        Assert.assertTrue(voidedBy.equalsIgnoreCase(user.getName()), "Voided by name was: " + voidedBy + " expected: " + user.getName());
    }

    public void validateVoidedDate(String centerDate) {
        String date = find(By.xpath("//div[contains(@id,':UpdateTime-inputEl')]")).getText();
        Assert.assertTrue(date.equals(centerDate), "Voided date was: " + date + " expected " + centerDate);

    }

    private void setForReissue(Boolean yesNo) {
        if (yesNo == false) {
            find(By.cssSelector("input[id*='VoidStopCheck:VoidStopCheckScreen:ForReissue_false-inputEl']")).click();
        } else {
            find(By.cssSelector("input[id*='VoidStopCheck:VoidStopCheckScreen:ForReissue_true-inputEl']")).click();
        }
    }

    public void voidCheck() {
        clickVoidStopButton();
        setForReissue(false);
        sendReasonVoid("Test Void Checks.");
        clickVoidCheckButton();
        try {
            new PopUpWindow(getDriver(), find(By.xpath("//span[contains(text(), 'OK')]")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void voidForReissue() {
        clickVoidStopButton();
        setForReissue(true);
        sendReasonVoid("Test Void for Reissue Checks.");
        clickVoidCheckButton();
        try {
            new PopUpWindow(getDriver(), getDriver().findElement(By.xpath("//span[contains(text(), 'OK')]")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Tax Reporting text element
    @FindBy(xpath = "//div[@id='ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:CheckDV:TaxReporting-inputEl']")
    private WebElement element_TaxReporting;

    public String getTaxReporting() {
        return element_TaxReporting.getText();
    }

    // Mailing Address text element
    @FindBy(xpath = "//div[@id='ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:CheckDV:MailingAddress-inputEl']")
    private WebElement element_MailingAddress;

    public String getMailingAddress() {
        return element_MailingAddress.getText();
    }

    // Created By text element
    @FindBy(xpath = "//div[@id='ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:CheckDV:CreateUser-inputEl']")
    private WebElement element_CreatedBy;

    public String getCreatedByUser() {
        return element_CreatedBy.getText();
    }

    public String getExposureData(String checkAmount) {
        WebElement checkLink = find(By.xpath("//a[contains(text(),'" + checkAmount + "')]"));
        clickWhenClickable(checkLink);
        String exposureSummary = find(By.xpath("//div[contains(@id,':PaymentBasicsInputSet:Exposure-inputEl')]")).getText();
        return exposureSummary;
    }

}
