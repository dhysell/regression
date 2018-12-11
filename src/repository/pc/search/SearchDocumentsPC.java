package repository.pc.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.pc.topmenu.TopMenuPC;

import java.util.Iterator;
import java.util.List;

public class SearchDocumentsPC extends BasePage {
	
	private WebDriver driver;

    public SearchDocumentsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	
	// method check first in docNameDescription is in Name fields if not checks
	// Description fields
	// if it finds a match it run through a switch by column name then returns
	// element text by xpath and number of steps
	// to the needed element.
	// only way to get date and author elements for they are all dynamically
	// generated ID's
	public String getDocumentValue(String docNameDescription, String returnValue) {
		WebElement element = checkForElement(docNameDescription, "Name");
		if (element == null) {
			element = checkForElement(docNameDescription, "Description");
			if (element == null) {
				Assert.fail("Document with the name/Description: " + docNameDescription + " does not Exist. Sorry :(");
			} else {
				switch (returnValue) {
				case "Date":
					return element.findElement(By.xpath("ancestor::tr/child::td[6]")).getText();
				case "Date Printed":
					return element.findElement(By.xpath("ancestor::tr/child::td[7]")).getText();
				case "Date Will Print":
					return element.findElement(By.xpath("ancestor::tr/child::td[8]")).getText();
				case "Author":
					return element.findElement(By.xpath("ancestor::tr/child::td[10]")).getText();
				}
			}
		} else {
			switch (returnValue) {
			case "Date":
				return element.findElement(By.xpath("ancestor::tr/child::td[6]")).getText();
			case "Date Printed":
				return element.findElement(By.xpath("ancestor::tr/child::td[7]")).getText();
			case "Date Will Print":
				return element.findElement(By.xpath("ancestor::tr/child::td[8]")).getText();
			case "Author":
				return element.findElement(By.xpath("ancestor::tr/child::td[10]")).getText();
			}
		}
		return null;
	}

	
	public WebElement verifyDocumentExistsNameElement(String documentName) {
		return checkForElement(documentName, "Name");
	}

	
	public WebElement verifyDocumentExistsDescriptionElement(String documentDesc) {
		return checkForElement(documentDesc, "Description");
	}
	
	public boolean verifyDocumentExistsName(String documentName) {
		if (checkForElement(documentName, "Name") == null) {
			return false;
		} else
			return true;
	}
	
	public boolean verifyDocumentExistsDescription(String documentDesc) {
		if (checkForElement(documentDesc, "Description") == null) {
			return false;
		} else
			return true;
	}

	// check if on policy documents screen
	private void onPolicyTab() {
		if (finds(By.xpath("//span[@id='TabBar:PolicyTab-btnEl']/ancestor::a[contains(@class, 'small-menu-active')]")).size() <= 0) {
			System.out.println("Switching to Policy Tab");
            TopMenuPC topMenu = new TopMenuPC(driver);
			topMenu.clickPolicyTab();
		}
		clickWhenClickable(By.xpath("//td[@id='PolicyFile:MenuLinks:PolicyFile_PolicyFile_Documents']"));
	}

	// ensure results page is set to page 1
	private void restResultsPage() {
		clickReset();
		clickSearch();
	}

	// if multiple pages switch to next page
	private boolean nextSearchPage() {
		// *[@id="PolicyFile_Documents:Policy_DocumentsScreen:DocumentsLV:_ListPaging-inputEl"]
		Integer currentPage = Integer.parseInt(find(By.xpath("//input[@id='PolicyFile_Documents:Policy_DocumentsScreen:DocumentsLV:_ListPaging-inputEl']")).getAttribute("value"));
		String pages = find(By.xpath("//table[contains(@id, ':DocumentsLV:_ListPaging')]/following-sibling::div[1]")).getText();
		Integer pageCount = Integer.parseInt(pages.substring(pages.length() - 1, pages.length()));

		if (currentPage < pageCount) {
			// set page to next select option
			try {
				WebElement page = find(By.xpath("//input[contains(@id, '_ListPaging-inputEl')]"));
				page.sendKeys(Keys.chord(Keys.CONTROL + "a"));
				page.sendKeys(String.valueOf(currentPage + 1));
				page.sendKeys(Keys.ENTER);
				waitForPostBack();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	// get a List of all the documents displayed on the current page
	private List<WebElement> getDocuments(String getBy) {
		return finds(By.xpath("//div[contains(@id, ':DocumentsLV')]/descendant::table/descendant::tr/descendant::a[contains(@id, ':" + getBy + "')]"));
	}

	// loop through the List provided from getDocuments() and returns the First
	// element
	// found that matches the search criteria
	private WebElement checkForElement(String compareTo, String getBy) {
		onPolicyTab();
		restResultsPage();
		boolean endOfResults = false;
		while (endOfResults == false) {
			List<WebElement> documentTable = getDocuments(getBy);
			Iterator<WebElement> documentIterator = documentTable.iterator();
			while (documentIterator.hasNext()) {
				WebElement currentNode = documentIterator.next();
				String documentSearch = currentNode.getText();
				System.out.println(documentSearch);
				if (documentSearch.equals(compareTo)) {
					return currentNode;
				}
			}
			endOfResults = nextSearchPage();
		}
		return null;
	}

}
