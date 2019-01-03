package repository.cc.desktop;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.ManualCheckRegister;
import repository.driverConfiguration.BasePage;

public class DesktopSidebarCC extends BasePage {

    private WebDriver driver;

    public DesktopSidebarCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(css = "td[id='Desktop:MenuLinks:Desktop_DesktopManualChecksGroup']")
    private WebElement manualChecksGroupLink;

    @FindBy(css = "td[id='Desktop:MenuLinks:Desktop_DesktopManualChecksGroup:DesktopManualChecksGroup_ManualCheckRegister']")
    public WebElement link_ManualcheckRegister;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopActivities']")
    public WebElement link_Activities;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopClaims']")
    public WebElement link_Claims;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopExposures']")
    public WebElement link_Exposures;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopAwaitingAssignment']")
    public WebElement link_PendingAssignment;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopQueuedActivities']")
    public WebElement link_Queues;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopCalendarGroup']")
    public WebElement link_Calendar;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_BulkPay']")
    public WebElement link_BulkInvoices;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DraftClaims']")
    public WebElement link_DraftClaims;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopSGRejectedInvoice']")
    public WebElement link_SafeliteGlassRejectedInvoices;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_ManualCheckDestroyed']")
    public WebElement link_ManualChecksCompromised;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopReturnEFT']")
    public WebElement link_ReturnEFTs;

    @FindBy(xpath = "//td[@id='Desktop:MenuLinks:Desktop_DesktopQuestRejectedInvoice']")
    public WebElement link_QuestRejectedInvoices;

    // HELPERS
    // ==============================================================================

    public QuestRejectedInvoicesCC clickQuestRejectedInvoices() {
        try {
            clickWhenClickable(link_QuestRejectedInvoices);
        } catch (Exception e) {
            systemOut("Unable to click link.  Ensure selected user has correct permissions.");
        }
        return new QuestRejectedInvoicesCC(this.driver);
    }

    public void clickActivities() {
        clickWhenClickable(link_Activities);
    }

    public void clickBulkInvoices() {
        clickWhenClickable(link_BulkInvoices);
    }

    public void clickCalendar() {
        clickWhenClickable(link_Calendar);
    }

    public void clickClaims() {
        clickWhenClickable(link_Claims);
    }

    public void clickDraftClaims() {
        clickWhenClickable(link_DraftClaims);
    }

    public void clickExposures() {
        clickWhenClickable(link_Exposures);
    }

    public repository.cc.claim.ManualCheckRegister clickManualcheckRegisterLink() {
        waitUntilElementIsClickable(manualChecksGroupLink);
        manualChecksGroupLink.click();
        clickWhenClickable(link_ManualcheckRegister);
        return new ManualCheckRegister(this.driver);
    }

    public void clickManualChecksCompromised() {
        clickWhenClickable(link_ManualChecksCompromised);
    }

    public void clickPendingAssignment() {
        clickWhenClickable(link_PendingAssignment);
    }

    public void clickQueues() {
        clickWhenClickable(link_Queues);
    }

    public void clickReturnEFTs() {
        clickWhenClickable(link_ReturnEFTs);
    }

    public void clickSafeliteGlassRejectedInvoices() {
        clickWhenClickable(link_SafeliteGlassRejectedInvoices);
    }
}
