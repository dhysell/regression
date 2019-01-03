package repository.ab.newactivity;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class SendActivityToPC extends BasePage {

    public SendActivityToPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id,'SendActivityPopup:Cancel-btnEl')]")
    public WebElement button_SendActivityCancel;

    @FindBy(xpath = "//*[contains(@id,'SendActivityPopup:ToolbarButton-btnEl')]")
    public WebElement button_SendActivitySend;

    @FindBy(xpath = "//*[contains(@id,'SendActivityPopup:0')]")
    public WebElement selectAccountTable;

    @FindBy(xpath = "//input[contains(@id,'SendActivityPopup:Subject-inputEl')]")
    public WebElement editbox_SendActivityActivitySubject;

    @FindBy(xpath = "//textarea[contains(@id,'SendActivityPopup:Description-inputEl')]")
    public WebElement textarea_SendActivityActivityDescription;

    @FindBy(xpath = "//*[contains(@id,'SendActivityPopup:Cancel-btnEl')]")
    public WebElement removeAccountTable;

    @FindBy(xpath = "//*[contains(@id,'SendActivityPopup:Select-btnEl')]")
    public WebElement button_Select;

    //Methods

    public void clickSend() {
        clickWhenClickable(button_SendActivitySend);
    }

    public void clickCancel() {
        super.clickCancel();
    }

    public void setFirstPolicy() {
        new TableUtils(getDriver()).setCheckboxInTable(selectAccountTable, 1, true);
        clickWhenClickable(button_Select);
    }

    public void setSubject(String subject) {
        clickWhenClickable(editbox_SendActivityActivitySubject);
        editbox_SendActivityActivitySubject.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SendActivityActivitySubject.sendKeys(subject);
        editbox_SendActivityActivitySubject.sendKeys(Keys.TAB);
    }

    public void setActivityDescription(String activityDescription) {
        clickWhenClickable(textarea_SendActivityActivityDescription);
        textarea_SendActivityActivityDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textarea_SendActivityActivityDescription.sendKeys(activityDescription);
        textarea_SendActivityActivityDescription.sendKeys(Keys.TAB);
    }

    public void sendActivityToFirstPolicy(String description, String subject) {
        setFirstPolicy();
        setSubject(subject);
        setActivityDescription(description);
        clickSend();
    }
/*	
		public void selectActivity(String account){
		TableUtils.setCheckboxInTable(selectAccountTable, TableUtils.getRowNumberInTableByText(selectAccountTable, account), true);
		clickSelect
	}
	
*/


}
