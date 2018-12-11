package repository.ab.administration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class UsersPage extends BasePage {

	private TableUtils tableUtils;
	
    public UsersPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//input[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchDV:Username-inputEl')]")
    private WebElement editbox_UsersUsername;

    @FindBy(xpath = "//input[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchDV:FirstName-inputEl')]")
    private WebElement editbox_UsersFirstName;

    @FindBy(xpath = "//input[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchDV:LastName-inputEl')]")
    private WebElement editbox_UsersLastName;

    @FindBy(xpath = "//a[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search')]")
    private WebElement button_UsersSearch;

    @FindBy(xpath = "//div[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV')]")
    private WebElement div_UsersSearchResults;


    private void setUserName(String userName) {
        
        clickWhenClickable(editbox_UsersUsername);
        editbox_UsersUsername.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UsersUsername.sendKeys(userName);
        
        editbox_UsersUsername.sendKeys(Keys.TAB);
    }

    private void setFirstName(String firstName) {
        
        clickWhenClickable(editbox_UsersFirstName);
        editbox_UsersFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UsersFirstName.sendKeys(firstName);
        
        editbox_UsersFirstName.sendKeys(Keys.TAB);
    }

    private void setLastName(String lastName) {
        
        clickWhenClickable(editbox_UsersLastName);
        editbox_UsersLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_UsersLastName.sendKeys(lastName);
        
        editbox_UsersLastName.sendKeys(Keys.TAB);
    }

    public void clickSearch() {
        clickWhenClickable(button_UsersSearch);
    }

    public ArrayList<String> getSearchResultsUserNames(String userName) {
        setUserName(userName);
        clickSearch();
        return tableUtils.getAllCellTextFromSpecificColumn(div_UsersSearchResults, "Username");
    }

    public AbUsers getSearchResultsByName(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        clickSearch();
        int row = tableUtils.getRowCount(div_UsersSearchResults);
        String name = tableUtils.getCellTextInTableByRowAndColumnName(div_UsersSearchResults, row, "Name");
        String userName = tableUtils.getCellTextInTableByRowAndColumnName(div_UsersSearchResults, row, "Username");
        String groupName = tableUtils.getCellTextInTableByRowAndColumnName(div_UsersSearchResults, row, "Group Name");
        groupName = groupName.replace("[", "");
        groupName = groupName.replace("]", "");
        AbUsers newUser = new AbUsers(userName);
        newUser.setUserFirstName(firstName);
        newUser.setUserLastName(lastName);
        newUser.setUserPassword("gw");
        newUser.setUserDepartment(groupName);
        return newUser;
    }
}
