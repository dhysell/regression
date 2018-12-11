package repository.pc.activity;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.Priority;
import repository.gw.enums.SecurityLevel;
import repository.gw.enums.Topic;

public class UWActivityPC extends GenericActivityPC {

    private WebDriver driver;

    public UWActivityPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, 'UWActivityPopup:Update') or contains(@id, 'UWActivityPolicyChangePopup:Update')]")
    private WebElement button_SendRequest;

    @FindBy(xpath = "//input[contains(@id, 'UWActivityPopup:NewActivityDV:Subject-inputEl')]")
    private WebElement editbox_UWActivityActivitySubject;

    @FindBy(xpath = "//textarea[contains(@id, 'UWActivityPopup:NewActivityDV:Description-inputEl')]")
    private WebElement editbox_UWActivityDescription;

    Guidewire8Select select_UWActivityPriority() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWActivityPopup:NewActivityDV:Priority')]");
    }

    Guidewire8Select select_UWActivityAssignTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWActivityPopup:NewActivityDV:SelectFromList-triggerWrap')]");
    }

    Guidewire8Select select_UWActivityChangeReason() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWActivityPolicyChangePopup:NewActivityDV:ChangeResaon-triggerWrap') or contains(@id, 'UWActivityPolicyChangePopup:NewActivityDV:ChangeReasonInputSet:ChangeResaon-triggerWrap')]");
    }

    Guidewire8RadioButton radio_UWActivityManditory() {
        return new Guidewire8RadioButton(driver,"//table[contains(@id, 'UWActivityPopup:NewActivityDV:Mandatory') or contains(@id, 'NewActivityScreen:NewActivityDV:Mandatory')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'UWActivityPopup:NewActivityDV:TargetDate-inputEl')]")
    private WebElement editbox_UWActivityTargetDueDate;

    @FindBy(xpath = "//input[contains(@id, 'UWActivityPopup:NewActivityDV:EscalationDate-inputEl')]")
    private WebElement editbox_UWActivityEscalationDate;

    Guidewire8Select select_UWActivityTopic() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWActivityPopup:ActivityDetailNoteDV:Topic-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'UWActivityPopup:ActivityDetailNoteDV:NoteSubject-inputEl') or contains(@id, 'NewActivityScreen:ActivityDetailNoteDV:NoteSubject-inputEl')]")
    private WebElement editbox_UWActivityNewNoteSubject;

    Guidewire8Select select_UWActivitySecurityLevel() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWActivityPopup:ActivityDetailNoteDV:SecurityLevel-triggerWrap')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'Popup:ActivityDetailNoteDV:Text-inputEl') or contains(@id, 'NewActivityScreen:ActivityDetailNoteDV:Text-inputEl')]")
    private WebElement editbox_UWActivityText;

    @FindBy(xpath = "//span[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityScreen_UpdateButton-btnInnerEl')]")
    private WebElement button_OK;

    @FindBy(xpath = "//span[contains(@id,'ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtons_CompleteButton-btnInnerEl')]")
    private WebElement button_Complete;

    @FindBy(xpath = "//div[contains(@class,'message')]")
    private WebElement button_ErrorMessage;

    @FindBy(xpath = "//a[contains(@id,':doCompleteAndAssignTo')]")
    public WebElement button_CompleteAndAssign;


    public void clickSendRequest() {
        clickWhenClickable(button_SendRequest);
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void setActivitySubject(String subject) {
        editbox_UWActivityActivitySubject.click();
        editbox_UWActivityActivitySubject.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UWActivityActivitySubject.sendKeys(subject);
    }


    public String getActivitySubject() {
        return editbox_UWActivityActivitySubject.getText();
    }


    public void setChangeReason(ChangeReason changeReason) {
        select_UWActivityChangeReason().selectByVisibleTextPartial(changeReason.getValue());
    }


    public void setDescription(String desc) {
        editbox_UWActivityDescription.click();
        editbox_UWActivityDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UWActivityDescription.sendKeys(desc);
    }


    public String getDescription() {
        return editbox_UWActivityDescription.getText();
    }


    public void setPriority(Priority priority) {
        Guidewire8Select mySelect = select_UWActivityPriority();
        mySelect.selectByVisibleText(priority.name());

    }


    public String getPriority() {
        Guidewire8Select mySelect = select_UWActivityPriority();
        return mySelect.getText();
    }


    public void setMandatory(Boolean yesno) {
        radio_UWActivityManditory().select(yesno);
    }


    public void setTargetDueDate(String date) {
        editbox_UWActivityTargetDueDate.click();
        editbox_UWActivityTargetDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UWActivityTargetDueDate.sendKeys(date);
        editbox_UWActivityTargetDueDate.sendKeys(Keys.TAB);

    }


    public String getTargetDueDate() {
        return editbox_UWActivityTargetDueDate.getText();
    }


    public void setEscalationDate(String date) {
        editbox_UWActivityEscalationDate.click();
        editbox_UWActivityEscalationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UWActivityEscalationDate.sendKeys(date);
    }


    public String getEscalationDate() {
        return editbox_UWActivityEscalationDate.getText();
    }


    public void setTopic(Topic topic) {
        Guidewire8Select mySelect = select_UWActivityTopic();
        mySelect.selectByVisibleText(topic.getValue());
    }


    public String getTopic() {
        return select_UWActivityTopic().getText();
    }


    public void setNewNoteSubject(String subject) {
        setText(editbox_UWActivityNewNoteSubject, subject);
    }


    public String getNewNoteSubject() {
        return editbox_UWActivityNewNoteSubject.getText();
    }


    public void setSecurityLevel(SecurityLevel securityLevel) {
        Guidewire8Select mySelect = select_UWActivitySecurityLevel();
        mySelect.selectByVisibleText(securityLevel.getValue());
    }


    public String getSecurityLevel() {
        return select_UWActivitySecurityLevel().getText();
    }

    public void setText(String text) {
        setText(editbox_UWActivityText, text);
    }


    public String getAssignTo() {
        waitUntilElementIsVisible(editbox_UWActivityText);
        return select_UWActivityAssignTo().getText();
    }


    public void setAssignTo(String assignToUserName) {
        super.setAssignTo(assignToUserName);
    }


    public String getText() {
        return editbox_UWActivityText.getText();
    }


    public void clickOK() {
        clickWhenClickable(button_OK);
    }


    public void clickComplete() {
        clickWhenClickable(button_Complete);
    }


    public String getErrorMessage() {
        return button_ErrorMessage.getText();
    }

    public void clickCompleteAndAssignTo() {
        clickWhenClickable(button_CompleteAndAssign);
    }

}
