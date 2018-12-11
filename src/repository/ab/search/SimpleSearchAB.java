package repository.ab.search;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.gw.enums.ContactSubType;
import repository.gw.login.Login;

import java.util.List;

public class SimpleSearchAB extends SearchAB {

    private WebDriver driver;

    public SimpleSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[@id='SimpleABContactSearch:SimpleSearchScreen:NameDenorm-inputEl']")
    public WebElement editbox_SimpleSearchName;

    @FindBy(xpath = "//span[contains(@id,'SimpleABContactSearch:SimpleSearchScreen:ttlBar')]")
    public WebElement text_SimpleSearchPageTitle;

    @FindBy(xpath = "//tr[contains(@class, 'x-grid-row-selected x-grid-row-focused')]")
    public WebElement text_SimpleSearchHighlightedTableRow;


    @FindBy(xpath = "//a[contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:Search')]")
    private static WebElement button_SimpleSearchSearch;

    @FindBy(xpath = "//div[contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/descendant::table")
    public WebElement table_SimpleSearchSearchResults;


    //
    // @FindBy(xpath =
    // "//a[@id='SimpleABContactSearch:SimpleSearchScreen:Reset']")
    // public WebElement button_SimpleSearchReset;
    //
    // @FindBy(xpath =
    // "//a[@id='SimpleABContactSearch:SimpleSearchScreen:ClaimContacts_CreateNewContactButton']")
    // public WebElement button_SimpleSearchCreateNew;
    //
    // @FindBy(xpath =
    // "//a[@id='SimpleABContactSearch:SimpleSearchScreen:ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewCompanyMenuItem-itemEl']")
    // public WebElement link_SimpleSearchCreateNewCompany;
    //
    // @FindBy(xpath =
    // "//a[@id='SimpleABContactSearch:SimpleSearchScreen:ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewPersonMenuItem-itemEl']")
    // public WebElement link_SimpleSearchCreateNewPerson;
    //
    // @FindBy(xpath = "//div[contains(@id,
    // 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/desendant::tbody/tr")
    // public List<WebElement> text_SimpleSearchSearchResults;
    //
    // @FindBy(xpath = "//div[contains(@id,
    // 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/descendant::table")
    // public WebElement table_SimpleSearchSearchResults;
    //
    // public WebElement link_Agent(String fullName) {
    // return find(By.xpath("//a[. ='" +
    // fullName +"']"));
    // }

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public boolean isSimpleSearch() {
        
        waitUntilElementIsVisible(text_SimpleSearchPageTitle);
        if (!text_SimpleSearchPageTitle.getText().equals("Search")) {
            return false;
        }
        return text_SimpleSearchHighlightedTableRow.getText().equals("Simple Search");
    }

    public void setEditbox_SimpleSearchName(String nameToSearch) {
        waitUntilElementIsVisible(editbox_SimpleSearchName);
        editbox_SimpleSearchName.sendKeys(nameToSearch);
    }

    /*
            public void clickSimpleSearchSearch() {
            clickWhenClickable(button_SimpleSearchSearch);
        }
    */
    public ContactDetailsBasicsAB simpleSearchCompany(String nameToSearch, String address) {
        super.clickReset();
        
        setEditbox_SimpleSearchName(nameToSearch);
        
        super.clickSearch();
        
        super.clickSimpleSearchCompanySearchResults(nameToSearch, address);
        
        return new ContactDetailsBasicsAB(driver);
    }

    public List<WebElement> getSingleSimpleSearchResult(String nameToSearch, String linkToFind) {
        setEditbox_SimpleSearchName(nameToSearch);
        super.clickSearch();
        return super.getSingleSimpleSearchResult(linkToFind);
    }

    // 	// public void clickSimpleSearchSearch() {
    // waitUntilElementIsClickable(button_SimpleSearchSearch);
    // button_SimpleSearchSearch.click();
    // }
    //
    // 	// public void clickSimpleSearchReset() {
    // waitUntilElementIsClickable(button_SimpleSearchReset);
    // button_SimpleSearchReset.click();
    // }
    //
    // 	// public void clickSimpleSearchCreateNew() {
    // waitUntilElementIsClickable(button_SimpleSearchCreateNew);
    // button_SimpleSearchCreateNew.click();
    // }
    //
    // 	// public void clickSimpleSearchCreateNewCompany() {
    // clickWhenClickable(link_SimpleSearchCreateNewCompany);
    // }
    //
    // 	// public void clickSimpleSearchCreateNewPerson() {
    // waitUntilElementIsClickable(link_SimpleSearchCreateNewPerson);
    // link_SimpleSearchCreateNewPerson.click();
    // }

    public List<WebElement> getSimpleSearchResults(String nameToSearch) {
        super.clickReset();
        
        setEditbox_SimpleSearchName(nameToSearch);
        
        super.clickSearch();
        
        waitUntilElementIsVisible(super.table_SimpleSearchSearchResults);
        List<WebElement> searchResults = super.getSearchResultsRows();
        return searchResults;
    }

    public List<WebElement> getSimpleSearchResultsName(String nameToSearch) {
        super.clickReset();
        
        setEditbox_SimpleSearchName(nameToSearch);
        
        super.clickSearch();
        
        List<WebElement> searchResults = super.table_SimpleSearchSearchResults.findElements(By.xpath(".//a"));
        return searchResults;
    }


    private void loginAndGetToSimpleSearch(AbUsers user) {
        Login lp = new Login(getDriver());
        lp.login(user.getUserName(), user.getUserPassword());
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        SidebarAB sidebarLinks = new SidebarAB(driver);
        sidebarLinks.clickSidebarSimpleSearchLink();
    }

    public ContactDetailsBasicsAB getSimpleSearchResultsVendor(AbUsers user, String name, ContactSubType type) {
        loginAndGetToSimpleSearch(user);
        super.clickReset();
        
        setEditbox_SimpleSearchName(name);
        
        super.clickSearch();
        
//		waitUntilElementIsVisible(table_SimpleSearchSearchResults);
        super.clickVendorSimpleSearchResults(type);
        return new ContactDetailsBasicsAB(driver);
    }

    public ContactDetailsBasicsAB getSimpleSearchVendor(AbUsers user, String searchCriterion, String resultsName, String addressLineOne) {
        loginAndGetToSimpleSearch(user);
        super.clickReset();
        
        setEditbox_SimpleSearchName(searchCriterion);
        
        super.clickSearch();
        
//		waitUntilElementIsVisible(table_SimpleSearchSearchResults);
        super.clickSimpleSearchCompanySearchResults(resultsName, addressLineOne);
        return new ContactDetailsBasicsAB(driver);
    }
}
