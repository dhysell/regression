package production.thunderhead.repository.batches;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.DateUtils;

public class Batches extends BasePage {
	private WebDriver driver;
	
    public Batches(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	
	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath="//form[(@action='?method=batches&offset=-20') or (@action='?method=batchesByStatus&batchStatus=ERRORED&offset=-20')]")
	public WebElement text_BatchesNumberPages;
	
	@FindBy(xpath="//table[@class='grid']")
	public WebElement table_BatchesBatchInfo;
	
	@FindBy(xpath="//a/img[@title='First Page']")
	public WebElement button_BatchesBatchInfoTableFirstPage;
	
	@FindBy(xpath="//a/img[@title='Previous Page']")
	public WebElement button_BatchesBatchInfoTablePreviousPage;
	
	@FindBy(xpath="//a/img[@title='Next Page']")
	public WebElement button_BatchesBatchInfoTableNextPage;
	
	@FindBy(xpath="//a/img[contains(@title, 'Last Page')]")
	public WebElement button_BatchesBatchInfoTableLastPage;
	
	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------
	public void clickBatchesBatchInfoTableFirstPage() {
		clickWhenClickable(button_BatchesBatchInfoTableFirstPage);
	}
	
	public void clickBatchesBatchInfoTablePreviousPage() {
		clickWhenClickable(button_BatchesBatchInfoTablePreviousPage);
	}
	
	public void clickBatchesBatchInfoTableNextPage() {
		clickWhenClickable(button_BatchesBatchInfoTableNextPage);
	}
	
	public void clickBatchesBatchInfoTableLastPage() {
		clickWhenClickable(button_BatchesBatchInfoTableLastPage);
	}
	
	public Integer getBatchesNumberPages() {
		waitUntilElementIsVisible(text_BatchesNumberPages);
		String allText = text_BatchesNumberPages.getText();
		String shortText = allText.replace("/ ", "");
		Integer numPages = Integer.parseInt(shortText);
		return numPages;
	}

	public int getNumPagesForTodaysDate() throws ParseException {
		boolean found = false;
		int numPages = 0;
		List<WebElement> allStartDateElements = new ArrayList<WebElement>();
		WebElement firstDateElement = null;
		Date firstDate = null;
		Date todayDate = new Date();
		while(!found) {
			allStartDateElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[6]"));
			firstDateElement = allStartDateElements.get(0);
			firstDate = DateUtils.convertStringtoDate(firstDateElement.getText(), "MM/dd/yy h:m a");
			if(firstDate.before(todayDate) && firstDate.after(DateUtils.getDateValueOfFormat(todayDate, "MM/dd/yyyy"))) {
				numPages++;
				allStartDateElements.clear();
				firstDateElement = null;
				firstDate = null;
				clickBatchesBatchInfoTableNextPage();
			} else {
				found = true;
			}
		}
		return numPages;
	}
	
	public String[][] getBatchStatusesAndBatchIdsForPageOfTable() {
		List<WebElement> allIdElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[1]"));
		List<WebElement> allStatusElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[3]"));
		ArrayList<String> allIds = new ArrayList<String>();
		ArrayList<String> allStatuses = new ArrayList<String>();
		for(int i = 0; i < allStatusElements.size(); i++) {
			allIds.add(allIdElements.get(i).getText());
			allStatuses.add(allStatusElements.get(i).getText());
		}
		String[] allIdsArray = allIds.toArray(new String[allIds.size()]);
		String[] allStatusesArray = allStatuses.toArray(new String[allStatuses.size()]);
		String[][] allIdsAndStatusesArray = new String[allIds.size()][allStatuses.size()];
		for(int i = 0; i < allIdsArray.length; i++) {
			allIdsAndStatusesArray[i][0] = allIdsArray[i];
			allIdsAndStatusesArray[i][1] = allStatusesArray[i];
		}
		return allIdsAndStatusesArray;
	}
	
	public String[][] getErroredBatchStatusesAndBatchIds(String[][] stringsToCheck) {
		ArrayList<String> tempList = new ArrayList<String>();
		for (int i = 0; i < stringsToCheck.length; i++) {
			if(stringsToCheck[i][1].matches("(?i)(e|E)(rrored).*")) {
				tempList.add(stringsToCheck[i][0]);
			}
		}
		String[][] listClone = new String[tempList.size()][2]; 
		for (int i = 0; i < tempList.size(); i++) {
			listClone[i][0] = tempList.get(i);
			listClone[i][1] = "Errored";
		}
        return listClone;
	}
	
	public UIStuffToReturn getErroredBatchIdsAssumingAllErroredForDate(Date dateToCheckFor) throws ParseException {
		int numPages = getBatchesNumberPages();
		System.out.println("numPages Received");
		List<WebElement> allIdElements = new ArrayList<WebElement>();
		List<WebElement> allNameElements = new ArrayList<WebElement>();
		List<WebElement> allDateElements = new ArrayList<WebElement>();
		WebElement lastDateElement = null;
		Date lastDate = DateUtils.dateAddSubtract(dateToCheckFor, DateAddSubtractOptions.Hour, -6);
		ArrayList<String> allIds = new ArrayList<String>();
		ArrayList<String> allNames = new ArrayList<String>();
		ArrayList<Date> allDates = new ArrayList<Date>();
		String[] allIdsArray = null;
		String[] allNamesArray = null;
		Date[] allDatesArray = null;
		ArrayList<Integer> indexesToCheck = new ArrayList<Integer>();
		String[] batchIdsToReturn = null;
		String[] batchNamesToReturn = null;
		int pageCounter = 1;
		while(lastDate.before(dateToCheckFor) && lastDate.after(DateUtils.dateAddSubtract(dateToCheckFor, DateAddSubtractOptions.Day, -1))) {
			System.out.println("About to get the Id, Name and Date Elements (page " + pageCounter + ")...");
			allIdElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[1]"));
			allNameElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[2]"));
			allDateElements = table_BatchesBatchInfo.findElements(By.xpath(".//td[6]"));
			System.out.println("Found Id, Name and Date Elements...");
			lastDateElement = allDateElements.get(allDateElements.size() - 1);
			lastDate = DateUtils.convertStringtoDate(lastDateElement.getText(), "MM/dd/yy h:m a");
			for(int i = 0; i < allDateElements.size(); i++) {
				allIds.add(allIdElements.get(i).getText());
				allNames.add(allNameElements.get(i).getText());
				allDates.add(DateUtils.convertStringtoDate(allDateElements.get(i).getText(), "MM/dd/yy h:m a"));
			}
			if(numPages > 1) {
				System.out.println("More pages so clicking into next page (page " + (pageCounter + 1) + ")...");
				clickBatchesBatchInfoTableNextPage();
			}			
			pageCounter++;
		}
		allIdsArray = allIds.toArray(new String[allIds.size()]);
		allNamesArray = allNames.toArray(new String[allNames.size()]);
		allDatesArray = allDates.toArray(new Date[allDates.size()]);
		System.out.println("About to check each date for the passed in date (page " + pageCounter + ")...");
		for(int i = 0; i < allDatesArray.length; i++) {
			if(allDatesArray[i].before(dateToCheckFor) && allDatesArray[i].after(DateUtils.dateAddSubtract(dateToCheckFor, DateAddSubtractOptions.Day, -1))) {
				indexesToCheck.add(i);
			}
		}
		batchIdsToReturn = new String[indexesToCheck.size()];
		batchNamesToReturn = new String[indexesToCheck.size()];
		System.out.println("About to put each one into new list (page " + pageCounter + ")...");
		for(int i = 0; i < batchIdsToReturn.length; i++) {
			batchIdsToReturn[i] = allIdsArray[indexesToCheck.get(i)];
			batchNamesToReturn[i] = allNamesArray[indexesToCheck.get(i)];
		}
		UIStuffToReturn toReturn = new UIStuffToReturn(driver);
		toReturn.setBatchIds(batchIdsToReturn);
		toReturn.setBatchNames(batchNamesToReturn);
		return toReturn;
	}
}
