package repository.ab.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import repository.ab.contact.ReviewContacts;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class PotentialDuplicateContacts extends BasePage {
    private WebDriver driver;
    private TableUtils tableUtils;

    public PotentialDuplicateContacts(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchDV:Name-inputEl')]")
    private WebElement editbox_PotentialDuplicateContactsNameLastName;

    private Guidewire8Select select_PotentialDuplicateContactsMatchType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchDV:MatchType-triggerWrap')]");
    }
    
    private Guidewire8Select select_PotentialDuplicateContactsTagType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchDV:contactTagType-triggerWrap')]");
    }
    							 
    @FindBy(xpath = "//div[@id= 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchLV']")
    private WebElement tableDiv_PotentialDuplicateContactsResults;

    private WebElement button_PotentialDuplicateContactsReview(int row) {
        String xpath = "//a[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchLV:" + row + ":Review')]";
        waitUntilElementIsClickable(By.xpath(xpath));
        return find(By.xpath(xpath));
    }
    
    @FindBy(xpath = "//table[contains(@id, 'PairSearchDV:LastRun')]")
    private WebElement table_PotentialDuplicateContactsLastRunOnly;
    
    @FindBy(xpath = "//input[contains(@id, ':DuplicateContactPairSearchDV:LastRun-inputEl')]")
    private WebElement checkbox_PotentialDuplicateContactsLastRunOnly;
    
    
   

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Helper Methods
    //------------------------------------------------------------------------------------------------------------------------------------------

    private void setName(String name) {
        waitUntilElementIsVisible(editbox_PotentialDuplicateContactsNameLastName);
        editbox_PotentialDuplicateContactsNameLastName.click();
        editbox_PotentialDuplicateContactsNameLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_PotentialDuplicateContactsNameLastName.sendKeys(Keys.DELETE);
        editbox_PotentialDuplicateContactsNameLastName.sendKeys(name);
        editbox_PotentialDuplicateContactsNameLastName.sendKeys(Keys.TAB);
    }

    private void setMatchType(String matchType) {
        waitUntilElementIsVisible(By.xpath("//table[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchDV:MatchType-triggerWrap')]"), 2000);
        select_PotentialDuplicateContactsMatchType().selectByVisibleText(matchType);
    }
    
    private void setLastRunOnly(boolean checked) {
    	int i = 0;
    	waitUntilElementIsClickable(checkbox_PotentialDuplicateContactsLastRunOnly);
    	waitUntilElementIsVisible(table_PotentialDuplicateContactsLastRunOnly);
    	if(checked) {
    		while(!table_PotentialDuplicateContactsLastRunOnly.getAttribute("class").contains("checked") && i < 10) {
    			checkbox_PotentialDuplicateContactsLastRunOnly.click();
    			waitForPostBack();
    			i++;
    		}
    	}else {
    		while(table_PotentialDuplicateContactsLastRunOnly.getAttribute("class").contains("checked")&& i < 10) {
    			checkbox_PotentialDuplicateContactsLastRunOnly.click();
    			waitForPostBack();
    			i++;
    		}
    	}
    	if(i==10) {
    		System.out.println("There is a chance that the last Run checkbox wasn't set to " + checked +"."); 
    	}
    	
    	
    }
    
    private ReviewContacts clickReview() {
    	clickWhenClickable(driver.findElement(By.xpath("//a[contains(@id, 'Review')]")));
    	return new ReviewContacts(driver);
    }

    private ReviewContacts clickReview(String contactName, String contactAddress) {
    	TableUtils tableUtils =  new TableUtils(driver);
    	if(tableUtils.hasMultiplePages(tableDiv_PotentialDuplicateContactsResults)) {
    		int totalPages = tableUtils.getNumberOfTablePages(tableDiv_PotentialDuplicateContactsResults);
    		for(int i=0; i<totalPages; i++) {
    			if(checkIfElementExists("//a[contains(., '"+contactName+"')]/ancestor::tr[2]/child::td//div[contains(., '"+contactAddress+"')]/ancestor::tr[2]/child::td/div/a[contains(@id, 'Review')]", 1)) {
    				clickWhenClickable(driver.findElement(By.xpath("//a[contains(., '"+contactName+"')]/ancestor::tr[2]/child::td//div[contains(., '"+contactAddress+"')]/ancestor::tr[2]/child::td/div/a[contains(@id, 'Review')]")));
    				break;
    			} else {
    				if(!tableUtils.incrementTablePageNumber(tableDiv_PotentialDuplicateContactsResults)) {
    					button_PotentialDuplicateContactsReview(1).click();
    				}
    			}
    		}
    		
    	} else {
    		if(checkIfElementExists("//a[contains(., '"+contactName+"')]/ancestor::tr[2]/child::td//div[contains(., '"+contactAddress+"')]/ancestor::tr[2]/child::td/div/a[contains(@id, 'Review')]", 1)) {
				clickWhenClickable(driver.findElement(By.xpath("//a[contains(., '"+contactName+"')]/ancestor::tr[2]/child::td//div[contains(., '"+contactAddress+"')]/ancestor::tr[2]/child::td/div/a[contains(@id, 'Review')]")));
    		}else {
    			Assert.fail("Unable to find the specified Duplicate contact.");
    		}
    	}
        return new ReviewContacts(driver);
    }

    public ReviewContacts reviewPotentialDuplicateContact(AbUsers user, String name, String address, String matchType, boolean lastRun) {
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.clickMergeContacts(user);
        setName(name);
        setLastRunOnly(lastRun);
        setMatchType(matchType);
        clickSearch();
        return clickReview(name, address);
    }
    
    public ReviewContacts clickRandomMatch() throws Exception {
    	 SidebarAB sideMenu = new SidebarAB(driver);
         sideMenu.clickMergeContacts(AbUserHelper.getUserByUserName("su"));
         setLastRunOnly(false);
         clickSearch();
         return clickReview();
    }

    public ArrayList<String> getTypes(){
    	 waitUntilElementIsVisible(By.xpath("//table[contains(@id, ':contactTagType-triggerWrap')]"), 2000);
         return select_PotentialDuplicateContactsTagType().getListItems();
    }
}
