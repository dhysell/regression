package repository.pc.actions;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ActivityReminderType;
import repository.gw.enums.ActivtyRequestType;
import repository.pc.activity.GenericActivityPC;

import java.util.ArrayList;
import java.util.List;

public class ActionsPC extends BasePage {

    public ActionsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':DesktopMenuActions-btnEl') or (contains(@id, ':AccountFileMenuActions-btnEl')) or contains(@id, ':PolicyFileMenuActions-btnEl') or contains(@id, 'MenuActions-btnEl')]")
    private WebElement button_Actions;

    @FindBy(xpath = "//div[contains(@id, ':DesktopMenuActions_NewPayment')]")
    public WebElement link_NewPayment;

    @FindBy(xpath = "//div[contains(@id, 'MenuActions_NewNote')]")
    public WebElement link_NewNote;

    @FindBy(xpath = "//span[contains(@id, '_NewActivity-textEl')]")
    public WebElement link_NewActivity;

    @FindBy(xpath = "//a[contains(@id, ':PolicyFileMenuActions_ExpirationDateChange-itemEl')]")
    public WebElement link_ExpirationDateChange;

    // @FindBy(xpath = "//div[contains(@id, ':WizardMenuActions_NewDocument')]")
    // public WebElement link_NewDocument;

    @FindBy(xpath = "//span[contains( @id, ':WizardMenuActions_NewDocument-textEl')] | //div[contains(@id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewDocument')]")
    public WebElement link_NewDocument;

    @FindBy(xpath = "//div[contains(@id, ':PolicyPeriodNewDocumentMenuItemSet_Linked')] | //span[contains (@id, ':PolicyPeriodNewDocumentMenuItemSet_Linked-textEl')]")
    public WebElement link_ToExistingDocument;

    @FindBy(xpath = "//div[contains(@id, ':PolicyPeriodNewDocumentMenuItemSet_Template')]")
    public WebElement link_CreateNewDocument;

    @FindBy(xpath = "//div[contains(@id, ':PolicyPeriodNewDocumentMenuItemSet_AttachMultipleDocuments')]")
    public WebElement link_AttachMultipleDocuments;

    @FindBy(xpath = "//span[contains(@id, 'StartRewriteMenuItemSet:RewriteFullTerm')]")
    public WebElement link_RewriteFullTerm;

    @FindBy(xpath = "//span[contains(@id, 'PolicyFileMenuActions_CopySubmission')] | //div[contains(@id, 'WizardMenuActions_CopySubmission')]")
    public WebElement link_CopySubmission;

    @FindBy(xpath = "//span[contains(@id, ':AccountFileMenuActions_NewSubmission-textEl')]")
    public WebElement link_NewSubmission;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFileMenuActions_ReinstatePolicy')]")
    public WebElement link_ReinstatePolicy;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFileMenuActions_RenewPolicy')]")
    public WebElement link_RenewPolicy;

    @FindBy(xpath = "//a[contains(@id, ':PolicyPeriodNewDocumentMenuItemSet_Template-itemEl')]")
    public WebElement link_CreateNewDocumentFromTemplate;

    // @FindBy(xpath="//span[contains(@id,
    // 'AccountFile:AccountFileMenuActions:AccountFileMenuActions_Create:AccountFileMenuActions_NewActivity:NewActivityMenuItemSet:4:NewActivityMenuItemSet_Category-textEl')]")
    // public WebElement link_Request;

    @FindBy(xpath = "//span[contains(text(), 'New mail')]/parent::a/parent::div")
    public WebElement link_NewMail;

    @FindBy(xpath = "//span[contains(text(), 'Review new mail')]/parent::a/parent::div")
    public WebElement link_ReviewNewMail;

    @FindBy(xpath = "//span[contains(text(), 'Request')]/parent::a/parent::div")
    public WebElement link_Request;

    @FindBy(xpath = "//span[contains(text(), 'Reminder')]/parent::a/parent::div")
    public WebElement link_Reminder;

    // @FindBy(xpath="//span[contains(@id,
    // 'AccountFile:AccountFileMenuActions:AccountFileMenuActions_Create:AccountFileMenuActions_NewActivity:NewActivityMenuItemSet:4:NewActivityMenuItemSet_Category:16:item-textEl')]")
    // public WebElement link_SignatureNeeded;

    @FindBy(xpath = "//span[contains(text(), 'Signature Needed')]/parent::a/parent::div")
    public WebElement link_SignatureNeeded;

    @FindBy(xpath = "//span[contains(text(), 'General reminder')]/parent::a/parent::div")
    public WebElement link_GeneralReminder;


    @FindBy(xpath = "//span[contains( @id, 'AccountFile:AccountFileMenuActions:AccountFileMenuActions_Create:AccountFileMenuActions_NewActivity:NewActivityMenuItemSet:4:NewActivityMenuItemSet_Category:14:item-textEl')]")
    private WebElement link_LegalReview;

    @FindBy(xpath = "//span[contains( @id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity:NewActivityMenuItemSet:5:NewActivityMenuItemSet_Category:26:item-textEl') and contains(., 'Request Additional Information')]/ancestor::div[1]")
    private WebElement link_ActionsNewActivityRequestAdditionalInformation;

    @FindBy(xpath = "//span[contains( @id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity:NewActivityMenuItemSet:5:NewActivityMenuItemSet_Category:12:item') and contains(., 'Request Additional Information')]/ancestor::div[1]")
    private WebElement link_ActionsNewActivityRequestGetMissingInformation;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile:PolicyFileMenuActions:')]")
    private List<WebElement> text_ActionsOptions;


    @FindBy(xpath = "//*[contains(@id, ':AccountFileMenuActions_RewritePolicies-itemEl')]")
    private WebElement AccountFileMenuActions_RewritePoliciesToThisAccount;


    public void requestActivity(ActivtyRequestType type) {
        
        click_Actions();
        
        click_NewActivity();
        
        click_Request();
        
//        scrollToElement(find(By.xpath("//span[text() = '" + type.getValue() + "']")));
        clickWhenClickable(find(By.xpath("//span[text() = '" + type.getValue() + "']")));
        GenericActivityPC activity = new GenericActivityPC(getDriver());
        activity.getActivityTitle();


//		switch (type) {
//		case SubmitToUnderwriting:
//			break;
//		case SignatureNeeded:
//			scrollToElement(link_SignatureNeeded);
//			link_SignatureNeeded.click();
//			break;
//		case RequestAdditionalInformation:
//			link_ActionsNewActivityRequestAdditionalInformation.click();
//			break;
//		case PolicyChangeWithClaim:
//			break;
//		case PleaseReturnRenewalPaperwork:
//			break;
//		case GetMissingInformation:
//			link_ActionsNewActivityRequestGetMissingInformation.click();
//		}
        waitForPageLoad();
    }


    public void click_Actions() {
//		
        clickWhenClickable(button_Actions);
    }


    public void click_ExpirationDateChange() {
//		
        clickWhenClickable(link_ExpirationDateChange);
    }


    public void click_NewPayment() {
//		
        clickWhenClickable(link_NewPayment);
    }


    public void click_NewNote() {
//		
        clickWhenClickable(link_NewNote);
    }


    public void click_NewActivity() {
//		
        hoverOverAndClick(link_NewActivity);
    }


    public void click_NewDocument() {
//		
        hoverOverAndClick(link_NewDocument);
    }


    public void click_LinkToExistingDoc() {
//		
        hoverOver(link_ToExistingDocument);
        hoverOverAndClick(link_ToExistingDocument);
    }


    public void click_CreateNewDocument() {
//		
        hoverOver(link_ToExistingDocument);
        hoverOverAndClick(link_CreateNewDocument);
    }


    public void click_CreateNewDocumentFromTemplate() {
//		
        hoverOver(link_CreateNewDocumentFromTemplate);
        hoverOverAndClick(link_CreateNewDocumentFromTemplate);
    }


    public void click_AttachMultipleDocuments() {
//		
        hoverOver(link_ToExistingDocument);
        hoverOverAndClick(link_AttachMultipleDocuments);
    }


    public void click_RewriteFullTerm() {
//		
        clickWhenClickable(link_RewriteFullTerm);
    }


    public void click_CopySubmission() {
//		
        clickWhenClickable(link_CopySubmission);
    }


    public void click_NewSubmission() {
//		
        clickWhenClickable(link_NewSubmission);
    }


    public void click_ReinstatePolicy() {
//		
        clickWhenClickable(link_ReinstatePolicy);
    }


    public void click_RenewPolicy() {
//		
        clickWhenClickable(link_RenewPolicy);
    }


    public void click_RewritePoliciesToThisAccount() {
        clickWhenVisible(AccountFileMenuActions_RewritePoliciesToThisAccount);
    }


    public void click_NewMail() {
        
        hoverOverAndClick(link_NewMail);
    }


    public void click_ReviewNewMail() {
        
        hoverOverAndClick(link_ReviewNewMail);
    }


    public void click_Request() {
        
        hoverOverAndClick(link_Request);
    }


    public void click_SignatureNeeded() {
//		
        clickWhenClickable(link_SignatureNeeded);
    }


    public void click_LegalReview() {
//		
        clickWhenClickable(link_LegalReview);
    }


    public void clickToRequest() throws Exception {

        boolean success = false;
        int numTries = 0;
        while (success == false && numTries < 25) {
            numTries++;
            click_Actions();
            
            click_NewActivity();
            
            try {
                hoverOver(link_Request);
                
                click_Request();
                
                success = true;
            } catch (Exception incidentError) {
                if (checkIfElementExists(button_Actions, 600)) {
                    
                    click_Actions();
                    success = false;
                }
                success = false;

            }

        }
        if (numTries >= 25) {
            throw new Exception("Tried 25 times and couldn't make it threw the actions menu to choose Request.");
        }
        waitForPostBack();
    }


    public void clickToNewMail() throws Exception {

        boolean success = false;
        int numTries = 0;
        while (success == false && numTries < 25) {
            numTries++;
            click_Actions();
            
            click_NewActivity();
            
            try {
                hoverOver(link_NewMail);
                
                click_NewMail();
                
                success = true;
            } catch (Exception incidentError) {
                if (checkIfElementExists(button_Actions, 600)) {
                    
                    click_Actions();
                    success = false;
                }
                success = false;

            }

        }
        if (numTries >= 25) {
            throw new Exception("Tried 25 times and couldn't make it threw the actions menu to choose Request.");
        }
        waitForPostBack();
    }


    public boolean copySubmission() {
        click_Actions();
        if (!checkIfElementExists(link_CopySubmission, 2000)) {
            return false;
        }
        click_CopySubmission();
        return true;
    }


    public void rewriteFullTerm() {
        if (!checkIfElementExists(link_RewriteFullTerm, 2000)) {
            click_Actions();
        }
        click_RewriteFullTerm();
    }


    public void renewPolicy() throws Exception {
        if (!checkIfElementExists(link_RenewPolicy, 2000)) {
            click_Actions();
        }
        try {
            click_RenewPolicy();
            selectOKOrCancelFromPopup(OkCancel.OK);
        } catch (Exception e) {
            throw new Exception("The Renew Policy button was not found.\n" + e);
        }

    }


    public ArrayList<String> getlinks() {
        click_Actions();
        ArrayList<String> actionOptions = new ArrayList<String>();
        for (WebElement actionsElement : text_ActionsOptions) {
            actionOptions.add(actionsElement.getText());
        }
        return actionOptions;
    }


    public void newReminderActivity(ActivityReminderType activityType) {
        
        click_Actions();
        
        click_NewActivity();
        
        clickReminder();
        
        switch (activityType) {
            case GeneralReminder:
//			scrollToElement(link_GeneralReminder);
                link_GeneralReminder.click();
                break;
            case ReviewProducer:
                break;
            case VerifyCoverage:
                break;
        }
    }


    public void clickReminder() {
        
        hoverOverAndClick(link_Reminder);
    }


    public boolean checkRewritePoliciesToThisAccountExists() {
        return checkIfElementExists(AccountFileMenuActions_RewritePoliciesToThisAccount, 2000);
    }


    public boolean copySubmissionExists() {
        return checkIfElementExists(link_CopySubmission, 2000);
    }
}
