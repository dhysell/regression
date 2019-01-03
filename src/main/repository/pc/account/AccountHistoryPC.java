package repository.pc.account;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class AccountHistoryPC extends BasePage {

    public AccountHistoryPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, 'user-inputEl')]")
    public WebElement editbox_User;

    @FindBy(xpath = "//a[contains(@id, 'UserBrowseMenuItem')]")
    public WebElement button_SelectUser;

    @FindBy(xpath = "//input[contains(@id, 'FromDate-inputEl')]")
    public WebElement editbox_FromDate;

    @FindBy(xpath = "//input[contains(@id, 'ToDate')]")
    public WebElement editbox_ToDate;


    public void setUser(String user) {
        waitUntilElementIsClickable(editbox_User);
        editbox_User.click();
        editbox_User.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_User.sendKeys(user);
    }


    public void clickSelectUser() {
        button_SelectUser.click();
    }


    public void selectRelatedTo(String relatedFilter) {
        // TODO Auto-generated method stub

    }


    public void setDateFrom(Date mmddyyyy) {
        waitUntilElementIsClickable(editbox_FromDate);
        editbox_FromDate.click();
        editbox_FromDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_FromDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }


    public void setDateTo(Date mmddyyyy) {
        waitUntilElementIsClickable(editbox_ToDate);
        editbox_ToDate.click();
        editbox_ToDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ToDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickReset() {
        super.clickReset();
    }

}
