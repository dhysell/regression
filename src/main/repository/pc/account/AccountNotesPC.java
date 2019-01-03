package repository.pc.account;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Topic;
import repository.gw.generate.custom.Note;
import repository.gw.helpers.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountNotesPC extends BasePage {

    private WebDriver driver;

    public AccountNotesPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, 'TextSearch')]")
    public WebElement editbox_TextSearch;

    @FindBy(xpath = "//input[contains(@id, 'Author')]")
    public WebElement editbox_Author;

    @FindBy(xpath = "//a[contains(@id, 'UserBrowseMenuItem')]")
    public WebElement button_SelectUser;

    public Guidewire8Select select_SortBy() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Notes:NotesScreen:NoteSearchDV:SortBy-triggerWrap']");
    }

    @FindBy(xpath = "//input[contains(@id, 'sortAscending_true')]")
    public WebElement radio_SortOrderAscending;

    @FindBy(xpath = "//input[contains(@id, 'sortAscending_false')]")
    public WebElement radio_SortOrderDescending;

    public Guidewire8Select select_Topic() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Notes:NotesScreen:NoteSearchDV:Topic-triggerWrap']");
    }

    @FindBy(xpath = "//input[contains(@id, 'DateFrom')]")
    public WebElement editbox_DateRangeFrom;

    @FindBy(xpath = "//input[contains(@id, 'DateTo')]")
    public WebElement editbox_DateRangeTo;


    public void setTextSearch(String textSearch) {
        waitUntilElementIsClickable(editbox_TextSearch);
        editbox_TextSearch.click();
        editbox_TextSearch.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_TextSearch.sendKeys(textSearch);
    }


    public void setAuthor(String author) {
        waitUntilElementIsClickable(editbox_Author);
        editbox_Author.click();
        editbox_Author.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Author.sendKeys(author);
    }


    public void clickSelectUser() {
        button_SelectUser.click();
    }


    public void setSortBy(String sortType) {
        select_SortBy().selectByVisibleText(sortType);
    }


    public void setSortAscending() {
        radio_SortOrderAscending.click();
    }


    public void setSortDescending() {
        radio_SortOrderDescending.click();
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickReset() {
        super.clickReset();
    }


    public void setNotesFilter(String filterType) {
        // TODO Auto-generated method stub

    }


    public void setTopic(String topic) {
        select_Topic().selectByVisibleText(topic);
    }


    public void setDateFrom(Date mmddyyyy) {
        waitUntilElementIsClickable(editbox_DateRangeFrom);
        editbox_DateRangeFrom.click();
        editbox_DateRangeFrom.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateRangeFrom.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }


    public void setDateTo(Date mmddyyyy) {
        waitUntilElementIsClickable(editbox_DateRangeTo);
        editbox_DateRangeTo.click();
        editbox_DateRangeTo.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateRangeTo.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }

    public List<Note> getAccountNotes() {
        List<Note> returnList = new ArrayList<Note>();
        List<WebElement> notesList = finds(By.xpath("//div[contains(@id, ':NotesScreen:NotesLV-body')]/div/table/tbody/child::tr"));
        for(WebElement note : notesList) {
            List<WebElement> foo = note.findElements(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:Subject')]"));
            Note fooNote = new Note();
            fooNote.setAuthor(note.findElement(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:Author')]")).getText());
            fooNote.setTopic(Topic.getEnumFromStringValue(note.findElement(By.xpath("./td/descendant::div[contains(@id, ':NoteRowSet:Topic')]")).getText()));
            fooNote.setSecurityLevel(note.findElement(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:SecurityType')]")).getText());
            fooNote.setRelatedTo(note.findElement(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:RelatedTo')]")).getText());
            fooNote.setDate(note.findElement(By.xpath("./td/descendant::label[contains(@for, 'NoteRowSet:AuthoringDate')]")).getText());
            if(!foo.isEmpty()) {
                fooNote.setSubject(note.findElement(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:Subject')]")).getText());
            }
            fooNote.setText(note.findElement(By.xpath("./td/descendant::div[contains(@id, 'NoteRowSet:Body')]")).getText());
            returnList.add(fooNote);
        }
        return returnList;
    }

    public boolean containsNote(List<Note> noteList, Note myNote) {
        for(Note note : noteList) {
            if(note.getSubject().equals(myNote.getSubject())) {
                if(note.getText().equals(myNote.getText())) {
                    if(note.getTopic().equals(myNote.getTopic())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
