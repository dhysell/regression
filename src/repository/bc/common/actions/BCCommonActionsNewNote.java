package repository.bc.common.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicyNotes.RelatedTo;
import repository.gw.enums.PolicyNotes.Topic;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class BCCommonActionsNewNote extends BasePage  {
	
	private WebDriver driver;
	
	public BCCommonActionsNewNote(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	public Guidewire8Select comboBox_NewNoteRelatedTo() {
		return new Guidewire8Select(driver, "//table[contains (@id, ':NewNoteDV:RelatedTo-triggerWrap')]");
	}

	public Guidewire8Select comboBox_NewNoteTopic() {
		return new Guidewire8Select(driver, "//table[contains (@id, ':NewNoteDV:Topic-triggerWrap')]");
	}

	@FindBy(xpath = "//input[contains (@id, 'NewNoteScreen:NewNoteDV:Subject-inputEl')]")
	public WebElement editbox_NewNoteSubject;

	@FindBy(xpath = "//textarea[contains (@id, 'NewNoteScreen:NewNoteDV:Text-inputEl')]")
	public WebElement textarea_NewNoteText;

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	
	public void clickCancel() {
		super.clickCancel();
	}

	
	public void clickUpdate() {
		super.clickUpdate();
	}

	
	public void setNewNoteRelatedTo(RelatedTo relatedTo) {
		comboBox_NewNoteRelatedTo().selectByVisibleText(relatedTo.getValue());
	}

	// randomly select a item in RelatedTo dropdown list and return it
	
	public RelatedTo randomSetNewNoteRelatedTo() {
		List<String> theList = comboBox_NewNoteRelatedTo().getList();
		int listSize = theList.size();
		int currentRandom = NumberUtils.generateRandomNumberInt(2, listSize - 1);
		comboBox_NewNoteRelatedTo().selectByVisibleTextPartial(theList.get(currentRandom - 1));
		return RelatedTo.valueOfRelatedToString(theList.get(currentRandom - 1));
	}
	
	public void setNewNoteTopic(Topic topic) {
		comboBox_NewNoteTopic().selectByVisibleText(topic.getValue());
	}
	
	public Topic randomSetNewNoteTopic() {
		List<String> theList = comboBox_NewNoteTopic().getList();
		int listSize = theList.size();
		int currentRandom = NumberUtils.generateRandomNumberInt(2, listSize - 1);
		comboBox_NewNoteTopic().selectByVisibleTextPartial(theList.get(currentRandom - 1));
		return Topic.valueOfTopicString(theList.get(currentRandom - 1));
	}
	
	public void setNewNoteSubject(String subject) {
		editbox_NewNoteSubject.sendKeys(subject);
	}
	
	public void setNewNoteText(String text) {
		textarea_NewNoteText.sendKeys(text);
	}
	
	public RelatedTo getCurrentItemInRelatedToCombobox() {
		return RelatedTo.valueOfRelatedToString(comboBox_NewNoteRelatedTo().getText());
	}
	
	public Topic getCurrentItemInTopicCombobox() {
		return Topic.valueOfTopicString(comboBox_NewNoteTopic().getText());
	}
}
