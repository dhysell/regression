package repository.cc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class UserSearchCC extends BasePage {

    private WebDriver driver;

    public UserSearchCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[id$='Username-inputEl']")
    private WebElement input_UserName;

    @FindBy(css = "a[id$='Search']")
    private WebElement button_Search;

    @FindBy(css = "a[id$='DisplayName']")
    private WebElement link_Name;

    @FindBy(css = "div[id$='AccountLocked-inputEl']")
    private WebElement div_UserLocked;

    private void searchByUserName(String uName) {
        waitUntilElementIsClickable(input_UserName, 20).sendKeys(uName);
        clickWhenClickable(button_Search);

    }

    public boolean searchForUserCheckIfLocked(String userName) {
        searchByUserName(userName);
        clickWhenClickable(link_Name);
       waitUntilElementIsClickable(div_UserLocked);
        return div_UserLocked.getText().equalsIgnoreCase("Yes");
    }
}
