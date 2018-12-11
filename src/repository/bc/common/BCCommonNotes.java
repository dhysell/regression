package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicyNotes.DateRange;
import repository.gw.enums.PolicyNotes.RelatedTo;
import repository.gw.enums.PolicyNotes.SortBy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.Date;

public class BCCommonNotes extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
	public BCCommonNotes(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.tableUtils = new TableUtils(driver);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//div[contains(@id,'DetailNotesScreen:NotesLV')]")
	public WebElement table_NotesSearchResults;
	
	@FindBy(xpath = "//input[contains(@id,':PolicyDetailNotesScreen:NoteSearchDV:TextSearch-inputEl')]")
	public WebElement editbox_FindText;

	@FindBy(xpath = "//input[contains(@id,':DateSearch2:DateSearch2StartDate-inputEl')]")
	public WebElement editbox_FromDate;

	@FindBy(xpath = "//input[contains(@id,':NoteSearchDV:DateSearch2:DateSearch2EndDate-inputEl')]")
	public WebElement editbox_ToDate;
	
	@FindBy(xpath = "//input[contains(@id, ':DateSearch2:DateSearch2RangeChoice_Choice-inputEl')]")
	public WebElement radio_Since;

	@FindBy(xpath = "//input[contains(@id, ':DateSearch2:DateSearch2DirectChoice_Choice-inputEl')]")
	public WebElement radio_FromTo;
	
	@FindBy(xpath = "//input[contains(@id, ':SortAscending_true-inputEl')]")
	public WebElement radio_Ascending;

	@FindBy(xpath = "//input[contains(@id, ':SortAscending_false-inputEl')]")
	public WebElement radio_Descending;
	
	Guidewire8Select select_Author() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyDetailNotesScreen:NoteSearchDV:Author-triggerWrap')]");
	}

	Guidewire8Select select_RelatedTo() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':NoteSearchDV:RelatedToSearch-triggerWrap')]");
	}
	
	Guidewire8Select select_SinceDate() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':NoteSearchDV:DateSearch2:DateSearch2RangeValue-triggerWrap')]");
	}
	
	Guidewire8Select select_SortBy() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':NoteSearchDV:SortByOption-triggerWrap')]");
	}
	
	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////
	
	public WebElement getNotesSearchResultsTable() {
		waitUntilElementIsVisible(table_NotesSearchResults);
		return table_NotesSearchResults;
	}
	
	public void setFindText(String text) {
		editbox_FindText.sendKeys(text);
	}

	public void selectAuthor(String author) {
		select_Author().selectByVisibleText(author);
	}

	public void selectRelatedTo(RelatedTo relatedTo) {
		select_RelatedTo().selectByVisibleText(relatedTo.getValue());
	}
	
	public void clickRadioSince() {
		radio_Since.click();
	}

	public void clickRadioFromTo() {
		radio_FromTo.click();
	}

	public void selectSinceDate(DateRange since) {
		select_SinceDate().selectByVisibleText(since.getValue());
	}

	public void setFromDate(String fromDate) {
		editbox_FromDate.click();
		editbox_FromDate.clear();
		editbox_FromDate.sendKeys(fromDate);
	}

	public void setToDate(String toDate) {
		editbox_ToDate.click();
		editbox_ToDate.clear();
		editbox_ToDate.sendKeys(toDate);
	}

	public void selectSortBy(SortBy sortBy) {
		select_SortBy().selectByVisibleText(sortBy.getValue());
	}

	public void clickRadioAscending() {
		radio_Ascending.click();
	}

	public void clickRadioDescending() {
		radio_Descending.click();
	}
	
	public String getNotesBy(int rowNum) {
		return tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Info", "Author");
	}
	
	public String getNotesTopic(int rowNum) {
		return tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Info", "Topic");
	}

	public RelatedTo getNotesRelatedTo(int rowNum) {
		return RelatedTo.valueOf(tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Info", "RelatedTo"));
	}
	
	public Date getNotesDateCreated(int rowNum) {
		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Details", "AuthoringDate"), "MMM dd, yyyy h:mm a");
		} catch (ParseException e) {
			Assert.fail("There was an error parsing the Date.");
		}
		dateToReturn = DateUtils.getDateValueOfFormat(dateToReturn, "MM/dd/yyyy");
		return dateToReturn;
	}

	public String getNotesSubject(int rowNum) {
		return tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Details", "Subject");
	}
	
	public String getNotesBody(int rowNum) {
		return tableUtils.getGridCellTextInTableByRowAndColumnName(table_NotesSearchResults, rowNum, "Details", "Body");
	}
}
