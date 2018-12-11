package repository.ab.search;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedAB extends BasePage {
	
	private WebDriver driver;
	
    public RecentlyViewedAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // *******************************************************************************
    // Repository Items
    // *******************************************************************************

    @FindBy(xpath = "//span[contains(@id,'RecentlyViewedSearch:ttlBar')]")
    public WebElement text_RecentlyViewedPageTitle;

    public Guidewire8Select select_RecentlyViewedSearchForDate() {
        return new Guidewire8Select(driver, "//table[@id='RecentlyViewedSearch:DateSearch-triggerWrap']");
    }

    @FindBy(xpath = "//div[contains(@id,'RecentlyViewedSearch:2-body')]/descendant::table/descendant::tr")
    public List<WebElement> text_RecentlyViewedSearchResults;

    // *******************************************************************************
    // Methods
    // *******************************************************************************

    // Search For Date utilizes:
    // 0 for today
    // 7 for Last 7 days
    // 14 for Last 14 says
    // 30 for Last 30 days
    // 90 for Last 90 days
    public void setSearchForDate(int searchFilter) {
        if (searchFilter == 0) {
            select_RecentlyViewedSearchForDate().selectByVisibleTextPartial("Today");
        } else {
            select_RecentlyViewedSearchForDate()
                    .selectByVisibleTextPartial("Last " + String.valueOf(searchFilter) + " days");
        }
    }

    public boolean isRecentlyViewedSearch() {
        return checkIfElementExists(text_RecentlyViewedPageTitle, 1000);
    }

    public void clickSearch() {
        super.clickSearch();
    }

    public void clickReset() {
        super.clickReset();
    }

    public List<WebElement> getSearchResults() {
        return text_RecentlyViewedSearchResults;
    }

    public ContactDetailsBasicsAB clickRandomSearchResult() {
        System.out.println(text_RecentlyViewedSearchResults.get(NumberUtils.generateRandomNumberInt(0, text_RecentlyViewedSearchResults.size() - 1)).findElement(By.xpath(".//descendant::a")).getText());
        text_RecentlyViewedSearchResults.get(NumberUtils.generateRandomNumberInt(0, text_RecentlyViewedSearchResults.size() - 1)).findElement(By.xpath(".//descendant::a")).click();
        return new ContactDetailsBasicsAB(getDriver());
    }

    public boolean nameExists(String name) {
        ArrayList<String> searchResults = new TableUtils(getDriver()).getAllCellTextFromSpecificColumn(find(By.xpath("//div[contains(@id, 'RecentlyViewedSearch:2')]")), "Name");
        for (String searchResult : searchResults) {
            if (searchResult.equals(name)) {
                return true;
            }
        }

        return false;


    }

}
