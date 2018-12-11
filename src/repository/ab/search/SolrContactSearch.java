package repository.ab.search;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class SolrContactSearch extends BasePage {
	private WebDriver driver;
	private TableUtils tableUtils;

    public SolrContactSearch(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }
	
	@FindBy(xpath = "//input[contains(@id, 'FBMSolrSearch:beep:FBMSolrSearchScreen:keywordCriteria-inputEl')]")
    private WebElement editbox_SolrContactSearchName;
	
	@FindBy(xpath = "//input[contains(@id, 'FBMSolrSearch:beep:FBMSolrSearchScreen:phoneCriteria-inputEl')]")
    private WebElement editbox_SolrContactSearchPhone;
	
	@FindBy(xpath = "//span[contains(@id, 'FBMSolrSearch:beep:FBMSolrSearchScreen:SolrSearchButton-btnEl')]")
    private WebElement button_SolrContactSearchSolrSearch;
	
	@FindBy(xpath = "//div[contains(@id, 'FBMSolrSearch:beep:FBMSolrSearchScreen:3')]")
    private WebElement tableDiv_SolrContactSearchResults;

    private void setName(String name) {
    	 waitUntilElementIsClickable(editbox_SolrContactSearchName);
         editbox_SolrContactSearchName.click();
         editbox_SolrContactSearchName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
         editbox_SolrContactSearchName.sendKeys(Keys.DELETE);
         editbox_SolrContactSearchName.sendKeys(name);
         editbox_SolrContactSearchName.sendKeys(Keys.TAB);
    }
    
    private void setPhone(String phone) {
    	waitUntilElementIsClickable(editbox_SolrContactSearchPhone);
    	editbox_SolrContactSearchPhone.click();
    	editbox_SolrContactSearchPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_SolrContactSearchPhone.sendKeys(Keys.DELETE);
    	editbox_SolrContactSearchPhone.sendKeys(phone);
    	editbox_SolrContactSearchPhone.sendKeys(Keys.TAB);
    }
    
    private void clickSolrSearch() {
    	waitUntilElementIsClickable(editbox_SolrContactSearchPhone);
    	button_SolrContactSearchSolrSearch.click();
    }
    
    public boolean searchName(String searchCriteria, String firstName, String lastName) {
    	setName(searchCriteria);
    	clickSolrSearch();
    	ArrayList<String> searchResults = tableUtils.getAllCellTextFromSpecificColumn(tableDiv_SolrContactSearchResults, "Name");
    	for(String result : searchResults) {
    		if(result.contains(firstName) && result.contains(lastName)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
}
