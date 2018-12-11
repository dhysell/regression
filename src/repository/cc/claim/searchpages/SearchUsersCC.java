package repository.cc.claim.searchpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.List;

public class SearchUsersCC extends BasePage {

    private WebDriver driver;

    public SearchUsersCC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//input[@id='UserSearchPopup:UserSearchPopupScreen:UserSearchDV:Username-inputEl']")
    public WebElement editBox_UserName;

    @FindBy(xpath = "//a[@id='UserSearchPopup:UserSearchPopupScreen:UserSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search']")
    public WebElement button_Search;

    // HELPERS
    // ==============================================================================

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void setUserNameEditBox(String userName) {
        editBox_UserName.sendKeys(userName);
    }

    public void selectUser(String userName) {

        setUserNameEditBox(userName);
        clickSearchButton();

        List<WebElement> resultsRows = finds(By.xpath(
                "//div[@id='UserSearchPopup:UserSearchPopupScreen:UserSearchResultsLV-body']/div/table/tbody/tr"));
        for (WebElement resultRow : resultsRows) {
            int rowNum = Integer.parseInt(resultRow.getAttribute("data-recordindex"));
            String userCell = resultRow.findElement(By.xpath("//td[4]")).getText();

            if (userCell.equalsIgnoreCase(userName)) {
                WebElement selectButton = resultRow.findElement(By.xpath("//a[@itemindex='" + rowNum + "']"));
                selectButton.click();
                break;
            }
        }

    }

}
