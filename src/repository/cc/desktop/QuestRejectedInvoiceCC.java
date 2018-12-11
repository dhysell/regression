package repository.cc.desktop;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class QuestRejectedInvoiceCC extends BasePage {

    public QuestRejectedInvoiceCC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//textarea[contains(@id,':DesktopQuestRejectedInvoiceDetailDV:Comments-inputEl')]")
    WebElement textArea_Comments;

    public void setCommentsTextArea(String comments) {
        textArea_Comments.sendKeys(comments);
    }

    public void clearCommentsTextArea() {
        textArea_Comments.clear();
    }

    @FindBy(xpath = "//a[contains(@id,':DesktopSGRejectedInvoiceDetailScreen:Update')]")
    WebElement button_Update;

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

}
