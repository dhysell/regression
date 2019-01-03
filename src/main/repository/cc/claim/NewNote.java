package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class NewNote extends BasePage {

    private WebDriver driver;

    public NewNote(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================
    @FindBy(xpath = "//a[@id='NewNoteWorksheet:NewNoteScreen:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='NewNoteWorksheet:NewNoteScreen:Cancel']")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='NewNoteWorksheet:NewNoteScreen:NewNoteWorksheet_UseTemplateButton']")
    public WebElement button_UseTemplate;

    @FindBy(xpath = "//input[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:Subject-inputEl']")
    public WebElement textbox_Subject;

    @FindBy(xpath = "//textarea[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:Body-inputEl']")
    public WebElement textbox_Text;

    @FindBy(xpath = "//input[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:Confidential_true-inputEl']")
    public WebElement radio_Yes;

    @FindBy(xpath = "//input[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:Confidential_false-inputEl']")
    public WebElement radio_No;

    @FindBy(xpath = "//div[@id='x-form-el-ClaimNotes:NotesSearchScreen:ClaimNotesLV:0:Subject']")
    public WebElement text_Subject;

    public Guidewire8Select select_Topic() {
        return new Guidewire8Select(driver,"//table[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:Topic-triggerWrap']");
    }

    public Guidewire8Select select_SecurityType() {
        return new Guidewire8Select(
                driver, "//table[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:SecurityType-triggerWrap']");
    }

    public Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver,"//table[@id='NewNoteWorksheet:NewNoteScreen:NoteDetailDV:RelatedTo-triggerWrap']");
    }

    // Helpers
    // ===============================================================================

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickUseTemplate() {
        clickWhenClickable(button_UseTemplate);
    }

    public void selectSpecificTopic(String topic) {
        select_Topic().selectByVisibleText(topic);
    }

    public void selectRandomTopic() {
        select_Topic().selectByVisibleTextRandom();
    }

    public void sendSubject(String subject) {
        textbox_Subject.sendKeys(subject);
    }

    public void sendTextboxText(String text) {
        textbox_Text.sendKeys(text);
    }

    public void selectRadioYes() {
        clickWhenClickable(radio_Yes);
    }

    public void selectRadioNo() {
        clickWhenClickable(radio_No);
    }
}
