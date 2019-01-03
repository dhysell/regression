package repository.cc.claim;

import com.idfbins.driver.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

public class NewMatter extends BaseTest {

    private WebDriver driver;
    private TableUtils tableUtils;
    private WaitUtils waitUtils;

    public NewMatter(WebDriver driver) {
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void constructNewMatter() {
        setNameDefault();
        setExposureRandom();
        clickUpdateButton();
    }

    @FindBy(css = "input[id*='NewMatterDV:Matter_Name-inputEl']")
    private WebElement inputName;

    private void setNameDefault() {
        waitUtils.waitUntilElementIsVisible(inputName);
        inputName.sendKeys("Test Matter");
    }

    @FindBy(css = "div[id*='NewMatter:NewMatterScreen:NewMatterDV:NewMatterExposuresLV']")
    private WebElement tableExposure;

    private void setExposureRandom() {
        tableUtils.selectRandomValueForSelectInTable(tableExposure, 1, "Exposure");
    }

    @FindBy(css = "a[id*='NewMatterDV_tb:Update']")
    private WebElement buttonUpdate;

    private void clickUpdateButton() {
        waitUtils.waitUntilElementIsVisible(buttonUpdate);
        buttonUpdate.click();
    }

    @FindBy(css = "div[class*='message']")
    private WebElement elementErrorMessage;

    private String getErrorMessage() {
        waitUtils.waitUntilElementIsVisible(elementErrorMessage, 10);
        return elementErrorMessage.getText();
    }

    public boolean isErrorConfirmed() {
        waitUtils.waitUntilElementIsVisible(elementErrorMessage, 10);
        return getErrorMessage().contains("Litigation status MUST be set to Litigated" +
                " or Represented with no lawsuit");
    }

    @FindBy(css = "a[id*='MatterDetailPage:MatterDetailPage_UpLink']")
    private WebElement linkUpToLitigation;

    public void clickUpToLitigation() {
        waitUtils.waitUntilElementIsVisible(linkUpToLitigation);
        linkUpToLitigation.click();
    }

}
