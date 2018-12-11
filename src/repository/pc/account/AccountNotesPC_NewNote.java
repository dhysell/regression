package repository.pc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Topic;
import repository.gw.generate.custom.Note;
import repository.pc.actions.ActionsPC;

public class AccountNotesPC_NewNote extends BasePage {

    private WebDriver driver;

    public AccountNotesPC_NewNote(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void createNewNote(Note note) {
        new ActionsPC(driver).click_Actions();
        new ActionsPC(driver).click_NewNote();
        waitForPostBack();
        selectTopic(note.getTopic());
        setSebject(note.getSubject());
        selectRelatedTo(note.getRelatedTo());
        setText(note.getText());
        clickUpdate();
    }





    private Guidewire8Select select_Topic() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':NewNoteDV:Topic-triggerWrap')]");
    }
    private void selectTopic(Topic topic) {
        Guidewire8Select mySelect = select_Topic();
        mySelect.selectByVisibleText(topic.getValue());
    }

    @FindBy(xpath = "//input[contains(@id, ':NewNoteDV:Subject-inputEl')]")
    private WebElement editbox_Subject;
    private void setSebject(String subject) {
        setText(editbox_Subject, subject);
    }

    private Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':RelatedToExt-triggerWrap')]");
    }
    private void selectRelatedTo(String related) {
        Guidewire8Select mySelect = select_RelatedTo();
        mySelect.selectByVisibleTextPartial(related);
    }

    @FindBy(xpath = "//textarea[contains(@id, ':NewNoteDV:Text-inputEl')]")
    private WebElement textArea_Text;
    private void setText(String text) {
        setText(textArea_Text, text);
    }
}
