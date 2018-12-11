package repository.pc.activity;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;

import java.text.ParseException;
import java.util.Date;

public class GenericActivityPC extends BasePage {

    private WebDriver driver;

    public GenericActivityPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id, 'NewActivityWorksheet:NewActivityScreen:ttlBar')]")
    private WebElement text_GenericActivityTitle;

    @FindBy(xpath = "//*[contains(@id, '_UpdateButton-btnEl')]")
    private WebElement button_GenericActivityOk;

    @FindBy(xpath = "//div[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityScreen_CancelButton-btnEl')]")
    private WebElement button_GenericActivityCancel;

    @FindBy(xpath = "//div[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityDV:Subject-inputEl')]")
    public WebElement textbox_GenericActivitySubject;

    @FindBy(xpath = "//div[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityDV:Description-inputEl')]")
    public WebElement textbox_GenericActivityDescription;

    @FindBy(xpath = "//textarea[contains(@id, ':ActivityDetailDV:Description-inputEl')]")
    public WebElement textarea_GenericActivityDescription;

    public String getDescription() {
        return textarea_GenericActivityDescription.getText();
    }
    
    private Guidewire8RadioButton radioButton_Mandatory() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':NewActivityDV:Mandatory-containerEl')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV:Status-inputEl')]")
    private WebElement textbox_Status;

    public String getStatus() {
        return textbox_Status.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':Mandatory-inputEl')]")
    private WebElement textbox_Mandatory;
    
    @FindBy(xpath = "//div[contains(@id, ':NewActivityDV:Priority-inputEl')]")
    private WebElement textbox_Priority;
    
    private Guidewire8Select select_NewActivityPriority() {
    	return new Guidewire8Select(driver, "//table[contains(@id, ':Priority-triggerWrap')]");
    }

    public String getPriority() {
    	if(checkIfElementExists(textbox_Priority, 1)) {
    		return textbox_Priority.getText();
    	} else {
    		return select_NewActivityPriority().getText();
    	}
    }
    
    public boolean getMandatory() {
        return (!textbox_Mandatory.getText().equals("No"));
    }
    
    public boolean isMandatoryActivity(boolean yesNo) {
    	waitUntilElementIsVisible(radioButton_Mandatory().getWebElement());
    	return radioButton_Mandatory().isSelected(yesNo);
    }
    
    public String getMandatoryText() {
    	waitUntilElementIsVisible(textbox_Mandatory);
    	return textbox_Mandatory.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV:Mandatory-inputEl')]")
    private WebElement textbox_Recurring;

    public boolean getRecurring() {
        return (!textbox_Recurring.getText().equals("No"));
    }

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV:RecvdDate-inputEl')]")
    private WebElement textbox_ReceivedDate;

    public String getReceivedDate() {
        return textbox_ReceivedDate.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':ActivityTargetEscalationDatesInputSet:TargetDate-inputEl')]")
    private WebElement textbox_TargetDate;

    public String getTargetDate() {
        return textbox_TargetDate.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':ActivityTargetEscalationDatesInputSet:EscalationDate-inputEl')]")
    private WebElement textbox_EscalationDate;

    public String getEscalationDate2() {
        return textbox_EscalationDate.getText();
    }


    public Guidewire8Select select_Priority() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':Priority-triggerWrap')]");
    }

    public Guidewire8Select select_AssignTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityDV:SelectFromList-triggerWrap') or contains(@id,'NewActivityWorksheet:NewActivityScreen:NewActivityDV:AssignToInputSet:SelectFromList-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityDV:SelectFromList:AssigneePicker')]")
    public WebElement link_GenericActivityAssignToPicker;
    
    @FindBy(xpath = "//div[contains(@id, 'NewActivityWorksheet:NewActivityScreen:NewActivityDV:Subject-inputEl')]")
    public WebElement text_GenericActivityNewActivitySubject;

    @FindBy(css = "input[id*='NoteSubject']")
    public WebElement textbox_GenericActivityNewNoteSubject;

    @FindBy(xpath = "//*[contains(@id, 'NewActivityWorksheet:NewActivityScreen:ActivityDetailNoteDV:Text-inputEl')]")
    public WebElement textbox_GenericActivityText;

    @FindBy(xpath = "//td[@class='x-table-layout-cell ']/div[contains(@id, 'toolbar')]")
    private WebElement div_ActivityLinksTop;

    @FindBy(xpath = "//input[contains(@id, ':TargetDate-inputEl')]")
    private WebElement editbox_TargetDueDate;

    @FindBy(xpath = "//input[contains(@id, ':EscalationDate-inputEl')]")
    private WebElement editboxEscalationDate;

    @FindBy(xpath = "//span[contains(@id, '_CompleteButton-btnEl')]")
    private WebElement button_Complete;

    @FindBy(xpath = "//a[contains(@id, ':SelectFromList:AssigneePicker')]")
    private WebElement button_GenericActivityAssignToPicker;

    @FindBy(xpath = "//input[contains(@id, 'PCAssigneePickerPopup:AssigneePickerScreen:AssignmentSearchDV:AssignmentSearchInputSet:Username-inputEl')]")
    private WebElement editbox_GenericActivityUserName;

    @FindBy(xpath = "//a[contains(., 'Assign')]")
    private WebElement button_GenericActivityAssign;

    public WebElement button_AssignTo(String group) {
        return find(By.xpath("//div[contains(., '" + group + "')]/parent::td/preceding-sibling::td/preceding-sibling::td/child::div/child::a[contains(.,'Assign')]"));

    }

    /**********************************************************************************************************************************************************************************************************************
     * Helper Methods
     **********************************************************************************************************************************************************************************************************************************************************/


    public void clickAssignToByGroup(String group) {
        clickWhenClickable(button_AssignTo(group));
    }


    public void setTargetDueDate(String date) {
        clickWhenClickable(editbox_TargetDueDate);
        editbox_TargetDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_TargetDueDate.sendKeys(date);
    }


    public String getTargetDueDate() {
        return editbox_TargetDueDate.getAttribute("value");
    }


    public void setEscalationDate(String date) {
        clickWhenClickable(editboxEscalationDate);
        editboxEscalationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxEscalationDate.sendKeys(date);
    }


    public String getEscalationDate() {
        return editboxEscalationDate.getAttribute("value");
    }


    public String getActivityTitle() {
        waitUntilElementIsVisible(text_GenericActivityTitle);
        return text_GenericActivityTitle.getText();
    }


    public void setPriority(String priority) {
        Guidewire8Select mySelect = select_Priority();
        mySelect.selectByVisibleText(priority);
        
    }
    
    public boolean isMandatorySelected() {
    	return radioButton_Mandatory().isSelected(true);
    }


    public void setAssignTo(String assignTo) {
        clickWhenClickable(button_GenericActivityAssignToPicker);
        clickReset();
        clickWhenClickable(editbox_GenericActivityUserName);
        editbox_GenericActivityUserName.sendKeys(assignTo);
        editbox_GenericActivityUserName.sendKeys(Keys.TAB);
        waitForPostBack();
        super.clickSearch();
        clickWhenClickable(button_GenericActivityAssign);

    }


    public void setAssignTo(String assignTo, String group) {
        clickWhenClickable(button_GenericActivityAssignToPicker);
        
        clickWhenClickable(editbox_GenericActivityUserName);
        editbox_GenericActivityUserName.sendKeys(assignTo);
        editbox_GenericActivityUserName.sendKeys(Keys.TAB);
        waitForPostBack();
        super.clickSearch();
        clickAssignToByGroup(group);

    }
    
    public void setSubject(String subject) {
        clickWhenClickable(textbox_GenericActivityNewNoteSubject);
        textbox_GenericActivityNewNoteSubject.sendKeys(subject);
        textbox_GenericActivityNewNoteSubject.sendKeys(Keys.TAB);
        waitForPostBack();
    }


    public void setText(String text) {
        clickWhenClickable(textbox_GenericActivityText);
        textbox_GenericActivityText.sendKeys(text);
        textbox_GenericActivityText.sendKeys(Keys.TAB);
        new GuidewireHelpers(driver).clickProductLogo();
        waitForPostBack();
    }
    
    public String getText() {
    	waitUntilElementIsVisible(textbox_GenericActivityText);
    	return textbox_GenericActivityText.getAttribute("value");
    }

    public void clickOK() {
        clickWhenClickable(button_GenericActivityOk);
        
    }


    public String getAssignTo() {
        return select_AssignTo().getText();
    }


    public void fillOutActivity(String assignTo, String subject, String text) {
        waitUntilElementIsClickable(button_GenericActivityOk);
        setAssignTo(assignTo);
        setSubject(subject);
        setText(text);
        clickOK();
    }


    public void completeActivity() {
        clickWhenClickable(button_Complete);
        
    }


    public boolean verifyDate(String date, String startDate, int businessDays) throws ParseException {
        Date dateInUI = DateUtils.convertStringtoDate(date, "MM/dd/yyyy");
        Date sDate = DateUtils.convertStringtoDate(startDate, "MM/dd/yyyy");
        return DateUtils.dateAddSubtract(sDate, DateAddSubtractOptions.BusinessDay, businessDays).equals(dateInUI);
    }
    
    // New behavior for Core 4 Activity
    
    private Guidewire8RadioButton radio_NewActivityMissingPayment() {
    	return new Guidewire8RadioButton(driver, "//div[contains(@id, ':missingPayment-containerEl')]");
    }
    
    private Guidewire8RadioButton radio_NewActivityMissingSignature() {
    	return new Guidewire8RadioButton(driver, "//div[contains(@id, ':missingSignature-containerEl')]");
    }
    
    private Guidewire8RadioButton radio_NewActivityMissingMS() {
    	return new Guidewire8RadioButton(driver, "//div[contains(@id, ':missingMS-containerEl')]");
    }
    
    private Guidewire8RadioButton radio_NewActivityMissingPhoto() {
    	return new Guidewire8RadioButton(driver, "//div[contains(@id, ':missingPhotos-containerEl')]");
    }
    
    @FindBy(xpath = "//span[contains(@id, ':addMSDwelling-btnEl')]")
    private WebElement button_GenericActivityAddMSProperty;
    
    @FindBy(xpath = "//span[contains(@id, ':addPhotosDwelling-btnEl')]")
    private WebElement button_GenericActivityAddPropertyPhoto;

    private WebElement button_GenericActivityAddMSPropertyLocation(int location, String building) {
    	return driver.findElement(By.xpath("//div[contains(@id, ':addMSDwelling:"+ location +":msDwelling')]"));
    }
    
    private WebElement button_GenericActivityAddPhotoPropertyLocation(int location, String building) {
    	return driver.findElement(By.xpath("//div[contains(@id, ':addPhotosDwelling:"+location+":photosDwelling')]"));
    }
    
    @FindBy(xpath = "//span[contains(@id, ':addToNotesButton-btnEl')]")
    private WebElement button_GenericActivityAddInformationToNotes;
        
    public boolean setMissingPaymentRadio(boolean trueFalse) {
    	if(checkIfElementExists(radio_NewActivityMissingPayment().getWebElement(), 1)) {
    		radio_NewActivityMissingPayment().select(trueFalse);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean setMissingSignatureRadio(boolean trueFalse) {
    	if(checkIfElementExists(radio_NewActivityMissingSignature().getWebElement(), 1)) {
    		radio_NewActivityMissingSignature().select(trueFalse);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean setMissingMSRadio(boolean trueFalse) {
    	if(checkIfElementExists(radio_NewActivityMissingMS().getWebElement(), 1)) {
    		radio_NewActivityMissingMS().select(trueFalse);
    		waitForPostBack();
    		waitUntilElementIsVisible(button_GenericActivityAddMSProperty);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean setMissingPhotoRadio(boolean trueFalse) {
    	if(checkIfElementExists(radio_NewActivityMissingPhoto().getWebElement(), 1)) {
    		radio_NewActivityMissingPhoto().select(trueFalse);
    		waitForPostBack();
    		waitUntilElementIsVisible(button_GenericActivityAddPropertyPhoto);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void addPropertyMS(int location, String building) {
    	clickWhenClickable(button_GenericActivityAddMSProperty);
    	clickWhenClickable(button_GenericActivityAddMSPropertyLocation(location, building));
    }
    
    public void addDwellingPhoto(int location, String building) {
    	clickWhenClickable(button_GenericActivityAddPropertyPhoto);
    	clickWhenClickable(button_GenericActivityAddPhotoPropertyLocation(location, building));
    }
    
    public String clickAddInformationToNotes() {
    	clickWhenClickable(button_GenericActivityAddInformationToNotes);
    	return textbox_GenericActivityText.getAttribute("value");
    }
}
