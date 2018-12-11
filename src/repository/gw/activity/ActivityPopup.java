package repository.gw.activity;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Priority;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class ActivityPopup extends BasePage {

    private WebDriver driver;

    public ActivityPopup(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//*[@id='wsTabBar:wsTab_0-btnInnerEl']")
    static public WebElement text_ActivityPageTitle;

    // This xPath is incorrect. Please change if your test is not working
    @FindBy(xpath = "//*[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailScreen_UpdateButton']")
    public WebElement button_ActivityCloseWorksheet;

    // This xPath is incorrect. Please change if your test is not working
    @FindBy(xpath = "//*[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailScreen_UpdateButton']")
    public WebElement button_ActivitySendToPC;

    @FindBy(xpath = "//*[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDV:ActivityDetailsInputSet:contact-inputEl']")
    public WebElement link_ActivityContactName;

    @FindBy(xpath = "//input[contains(@id, ':NoteSubject-inputEl')]")
    private WebElement text_Subject;

    @FindBy(xpath = "//*[contains(@id, ':ActivityDetailNoteDV:Text-inputEl')]")
    private WebElement text_TextDescription;

    @FindBy(xpath = "//span[contains(@id, '_CompleteSendButton-btnEl')]")
    private WebElement button_SendToContactManager;

    @FindBy(xpath = "//span[contains(@id, ':ActivityDetailToolbarButtons_CloseButton-btnEl')]")
    private WebElement button_CloseWorksheet;

    public repository.gw.elements.Guidewire8Select select_ActivityPriority() {
        return new repository.gw.elements.Guidewire8Select(driver, "//table[contains(@id, ':ActivityDetailDV:ActivityDetailDV_Priority-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:ActivityDetailDV_TargetDate-inputEl')]")
    public WebElement editBox_ActivityDueDate;

    @FindBy(xpath = "//input[contains(@id,':ActivityDetailDV:ActivityDetailDV_EscalationDate-inputEl')] | //div[contains(@id,':ActivityDetailDV:ActivityTargetEscalationDatesInputSet:EscalationDate-inputEl')]")
    public WebElement editBox_ActivityEscalationDate;

    @FindBy(xpath = "//input[contains(@id,'ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailNoteDV:NoteSubject-inputEl') or contains(@id, 'ActivityDetailDV_Subject-inputEl')]")
    private WebElement editbox_ActivitySubject;

    @FindBy(xpath = "//textarea[contains(@id,'ActivityDetailDV_Description-inputEl') or contains(@id, ':ActivityDetailScreen:ActivityDetailDV:Description-inputEl')]")
    private WebElement editBox_ActivityDescription;

    @FindBy(xpath = "//a[contains(@id,':ActivityDetailScreen_CompleteButton') or contains(@id, ':ActivityDetailToolbarButtons_Complete') or contains(@id,':ApprovalDetailWorksheet_ApproveButton')]")
    public WebElement button_Complete;

    @FindBy(xpath = "//a[contains(@id,'ViewChangeConflictsWorksheet:PreemptionConflictScreen:CloseButton')]")
    public WebElement button_Close;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public boolean activityPageExists() {
        return checkIfElementExists(text_ActivityPageTitle, 3000);
    }


    public String getActivityText() {
        waitUntilElementIsVisible(text_ActivityPageTitle);
        String text = text_ActivityPageTitle.getText();
        return text;
    }

	public void setActivityText(String text) {
        clickWhenClickable(text_TextDescription);
        text_TextDescription.sendKeys(text);
        clickProductLogo();
        
	}

	
    public void clickActivityOK() {
        clickOK();
    }


    public void clickOkOrUpdate() {
        clickUpdate();
    }


    public void clickActivityComplete() {
        clickComplete();
        waitForPostBack();
    }


    public void clickActivityCancel() {
        clickCancel();
    }


    public void clickActivityCloseWorksheet() {
        clickWhenClickable(button_ActivityCloseWorksheet);
    }


    public void clickCloseWorkSheet() {
        
        if (closeWorksheetButtonExists())
            clickWhenClickable(button_CloseWorksheet);
    }


    public void clickActivitySendToPC() {
        clickWhenClickable(button_ActivitySendToPC);
    }


    public String getActivityContactName() {
        waitUntilElementIsClickable(link_ActivityContactName);
        String text = link_ActivityContactName.getText();
        return text;
    }


    public void clickSendToContactManager() {
        clickWhenClickable(button_SendToContactManager);

    }


    public void sendToContactManager(String text) {
        clickWhenClickable(text_Subject);
        text_Subject.sendKeys(text);
        text_TextDescription.sendKeys(text);
        clickSendToContactManager();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public void setActivitySubject(String subject) {
        clickWhenClickable(editbox_ActivitySubject);
        editbox_ActivitySubject.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ActivitySubject.sendKeys(subject);
    }


    public String getActivitySubject() {
        return editbox_ActivitySubject.getAttribute("value");
    }


    public void setActivityDescription(String description) {
        clickWhenClickable(editBox_ActivityDescription);
        editBox_ActivityDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_ActivityDescription.sendKeys(description);
    }


    public String getActivityDescription() {
        return editBox_ActivityDescription.getAttribute("value");
        //try get text if this doesn't work.
    }


    public void selectManualActivityPriority(Priority priority) {
        repository.gw.elements.Guidewire8Select mySelect = select_ActivityPriority();
        mySelect.selectByVisibleText(priority.getValue());
        
    }


    public String getManualActivityPriority() {
        Guidewire8Select mySelect = select_ActivityPriority();
        return mySelect.getText();
    }


    public void setActivityDueDate(Date dueDate) {
        clickWhenClickable(editBox_ActivityDueDate);
        editBox_ActivityDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_ActivityDueDate.sendKeys(repository.gw.helpers.DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
    }


    public void setActivityDueDate(String dueDate) {
        clickWhenClickable(editBox_ActivityDueDate);
        editBox_ActivityDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        if (dueDate.equals("")) {
            editBox_ActivityDueDate.sendKeys(Keys.BACK_SPACE);
            editBox_ActivityDueDate.sendKeys(Keys.TAB);
        } else {
            editBox_ActivityDueDate.sendKeys(dueDate);
        }
    }


    public Date getActivityDueDate() {
        try {
            return (repository.gw.helpers.DateUtils.convertStringtoDate(editBox_ActivityDueDate.getAttribute("value"), "MM/dd/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }


    public void setActivityEscalationDate(Date escalationDate) {
        clickWhenClickable(editBox_ActivityEscalationDate);
        editBox_ActivityEscalationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_ActivityEscalationDate.sendKeys(repository.gw.helpers.DateUtils.dateFormatAsString("MM/dd/yyyy", escalationDate));
    }


    public Date getActivityEscalationDate() {
        try {
            return (DateUtils.convertStringtoDate(editBox_ActivityEscalationDate.getText(), "MM/dd/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }


    public void setUWIssuanceActivity() {
        String activityName = "UW Approval for Issuance";
        clickWhenClickable(text_Subject);
        text_Subject.sendKeys(activityName);
        text_TextDescription.sendKeys(activityName);
        clickActivityOK();
    }


    public void clickCompleteButton() {
        clickWhenClickable(button_Complete);
        
    }


    public void clickCloseButton() {
        clickWhenClickable(button_Close);
    }


    public boolean closeWorksheetButtonExists() {
        return checkIfElementExists(button_CloseWorksheet, 2000);
    }
}