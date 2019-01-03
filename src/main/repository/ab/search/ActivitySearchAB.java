package repository.ab.search;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.helpers.NumberUtils;

import java.util.List;

//import org.openqa.selenium.support.ui.Select;

public class ActivitySearchAB extends BasePage {
	
	private WebDriver driver;

    public ActivitySearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // *******************************************************************************
    // Repository Items
    // *******************************************************************************

    @FindBy(xpath = "//span[contains(@id, 'ActivitySearch:ttlBar')]")
    private WebElement text_ActivitySearchPageTitle;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchDV:LastName-inputEl']")
    private WebElement editbox_ActivitySearchLastName;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchDV:FirstName-inputEl']")
    private WebElement editbox_ActivitySearchFirstName;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchDV:Username-inputEl']")
    private WebElement editbox_ActivitySearchUserName;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchDV:Search']")
    private WebElement button_ActivitySearchSearch;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchDV:Reset']")
    private WebElement button_ActivitySearchReset;

    @FindBy(xpath = "//*[@id='ActivitySearch:ActivitySearchResultsLV:ActivitiesFilter-inputEl']")
    private WebElement combo_ActivitySearchActivityFilter;

    @FindBy(xpath = "//div[contains(@id, 'ActivitySearch:ActivitySearchResultsLV-body')]/descendant::table")
    private WebElement table_ActivitySearchActivityList;

    private List<WebElement> link_ActivitySearchActivitySubject(String subject, String contact) {
        return finds(By.xpath("//a[text()='" + contact + "']/../../../td/div/a[text()='" + subject + "']"));
    }

    private List<WebElement> link_ActivitySearchActivitySubjectByUser(String subject, String user) {
        return finds(By.xpath("//a[text()='" + user + "']/../../../td/div/a[text()='" + subject + "']"));
    }

    private WebElement text_ActivitySearchFirstRowSearchResults(String user) {
        return find(By.xpath("//table/tbody/tr[1]/td/div/span[contains(., '" + user + "')]"));
    }

    private WebElement link_ActivitySearchFindFirstActivity() {
        return find(By.xpath(
                "//*[@id='ActivitySearch:ActivitySearchResultsLV-body']/descendant::tbody/tr[1]/descendant::a[contains(., 'Review')]"));
    }

    private WebElement link_ActivitySearchFirstActivityContact() {
        return find(
                By.xpath("//*[@id='ActivitySearch:ActivitySearchResultsLV-body']/descendant::tbody/tr[1]/td[3]"));
    }

    private List<WebElement> text_ActivitySearchActivityAssignedUser(String name) {
        return finds(By.xpath("//span[text()='" + name + "']/ancestor::tr[1]/td[2]/div/a"));
    }

    // *******************************************************************************
    // Methods
    // *******************************************************************************

    public boolean isActivitySearch() {
        
        return checkIfElementExists(text_ActivitySearchPageTitle, 1000);
    }

    private void setLastName(String name) {
        editbox_ActivitySearchLastName.sendKeys(name);
    }

    private void setFirstName(String name) {
        editbox_ActivitySearchFirstName.sendKeys(name);
    }

    public void setUserName(String name) {
        
        editbox_ActivitySearchUserName.sendKeys(name);
    }

    public void clickSearch() {
        super.clickSearch();
    }

    public void clickReset() {
        super.clickReset();
    }

    private void select_ActivitySearchActivityFilter(AbActivitySearchFilter filterBy) {
        
        Guidewire8Select activityFilter = new Guidewire8Select(driver, "//table[contains(@id, 'ActivitySearch:ActivitySearchResultsLV:ActivitiesFilter-triggerWrap')]");
        activityFilter.selectByVisibleText(filterBy.getValue());
        
    }

    private void clickActivitySearchActivitySubjectLink(String subject, String contact) {
        clickWhenClickable(link_ActivitySearchActivitySubject(subject, contact).get(0));
        
    }
    
    public void clickActivitySearchActivitySubjectLink(AbUsers user, String subject, String contact) {
    	select_ActivitySearchActivityFilter(AbActivitySearchFilter.AllOpen); 
    	setUserName(user.getUserName());
        clickSearch();
        clickActivitySearchActivitySubjectLink("Review Contact", contact);
    }

    public String getActivitySearchSearchResultsUser(String user) {
        waitUntilElementIsVisible(text_ActivitySearchFirstRowSearchResults(user));
        String userName = text_ActivitySearchFirstRowSearchResults(user).getText();
        
        return userName;
    }

    public List<WebElement> getAssignedUser() {
        List<WebElement> assignedUsers = finds(By.xpath(
                "//*[@id='ActivitySearch:ActivitySearchResultsLV-body']/descendant::span[contains(@id, 'AssignedUser')]"));
        return assignedUsers;
    }

    public String getFirstActivityContact() {
        waitUntilElementIsVisible(link_ActivitySearchFirstActivityContact());
        String contact = link_ActivitySearchFirstActivityContact().getText();
        return contact;
    }

    public void clickFirstActivity() {
        waitUntilElementIsClickable(link_ActivitySearchFindFirstActivity());
        link_ActivitySearchFindFirstActivity().click();
    }

    public boolean checkActivitiesExist() {
        boolean found = checkIfElementExists(table_ActivitySearchActivityList,1);
        return found;
    }
    
    public boolean checkActivitiesExistForUser(AbUsers user) {
    	 select_ActivitySearchActivityFilter(AbActivitySearchFilter.AllOpen);
    	 setUserName(user.getUserName());
         clickSearch();
         return checkActivitiesExist();

    }

    public void clickActivityByName(String name) {
        
        List<WebElement> subject = text_ActivitySearchActivityAssignedUser(name);
        subject.get(NumberUtils.generateRandomNumberInt(0, subject.size())).click();
        
    }

    public ContactDetailsBasicsAB clickFirstActivityForUser(String lastName) {
        
        setLastName(lastName);
        select_ActivitySearchActivityFilter(AbActivitySearchFilter.AllActivities);
        clickSearch();
        
        clickFirstActivity();
        
        return new ContactDetailsBasicsAB(getDriver());
    }
}
