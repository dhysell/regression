package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class NewEmail extends BasePage {
    public NewEmail(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:ToolbarButton0']")
    public WebElement button_SendEmail;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:ToolbarButton1']")
    public WebElement button_CancelEmail;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:EmailWorksheet_UseTemplateButton']")
    public WebElement button_UseTemplate;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:ToRecipientLV_tb:Add']")
    public WebElement button_AddRecipients;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:ToRecipientLV_tb:Remove']")
    public WebElement button_RemoveRecipients;

    @FindBy(xpath = "//div[@id='EmailWorksheet:CreateEmailScreen:ToRecipientLV-body']//td[last()]")
    public WebElement box_RecipEmailAddress;

    @FindBy(xpath = "//input[@name='ToEmail']")
    public WebElement textbox_RecipEmailAddress;

    @FindBy(xpath = "//input[@id='EmailWorksheet:CreateEmailScreen:SaveAsDocument-inputEl']")
    public WebElement checkbox_SaveNewDoc;

    @FindBy(xpath = "//input[@name='ToName']")
    public WebElement textbox_RecipientName;

    @FindBy(xpath = "//input[@id='EmailWorksheet:CreateEmailScreen:TextInput1-inputEl']")
    public WebElement textbox_SenderName;

    @FindBy(xpath = "//input[@id='EmailWorksheet:CreateEmailScreen:TextInput2-inputEl']")
    public WebElement textbox_SenderEmail;

    @FindBy(xpath = "//input[@id='EmailWorksheet:CreateEmailScreen:TextInput0-inputEl']")
    public WebElement textbox_Subject;

    @FindBy(xpath = "//textarea[@id='EmailWorksheet:CreateEmailScreen:TextAreaInput0-inputEl']")
    public WebElement textbox_Body;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:AttachedDocumentsLV_tb:AddDocumentButton']")
    public WebElement button_AddAttachments;

    @FindBy(xpath = "//a[@id='EmailWorksheet:CreateEmailScreen:AttachedDocumentsLV_tb:Remove']")
    public WebElement button_RemoveAttachments;

    @FindBy(xpath = "//img[@id='EmailWorksheet:CreateEmailScreen:ToRecipientLV:0:ABPickerLink']")
    public WebElement button_SearchRecipName;

    // Helpers
    // =============================================================================

    public void clickSendEmailButton() {
        clickWhenClickable(button_SendEmail);
    }

    public void clickCancelEmailButton() {
        clickWhenClickable(button_CancelEmail);
    }

    public void clickUseTemplate() {
        clickWhenClickable(button_UseTemplate);
    }

    public void clickAddRecipient() {
        clickWhenClickable(button_AddRecipients);
    }

    public void clickRemoveRecipient() {
        clickWhenClickable(button_RemoveAttachments);
    }

    public void addRecipientName(String name) {
        textbox_RecipientName.sendKeys(name);
    }

    public void addRecipientEmail(String email) {
        clickWhenClickable(box_RecipEmailAddress);
        textbox_RecipEmailAddress.sendKeys(email);
    }

    public void addSenderName(String name) {
        textbox_SenderName.clear();
        textbox_SenderName.sendKeys(name);
    }

    public void addSenderEmail(String email) {
        textbox_SenderEmail.clear();
        textbox_SenderEmail.sendKeys(email);
    }

    public void addSubject(String subject) {
        textbox_Subject.sendKeys(subject);
    }

    public void addBodyText(String body) {
        textbox_Body.sendKeys(body);
    }
}
