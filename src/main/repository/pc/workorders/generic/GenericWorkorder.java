package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.ReasonNotTaken;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.contact.ContactEditPC;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericWorkorder extends BasePage {

    private WebDriver driver;

    public GenericWorkorder(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    // Jon Larsen 4/15/2015
    // changed all Webelements from "a" to "span" for GW8

    @FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:Next-btnInnerEl')]")
    private WebElement button_GenericWorkorderNext;

    @FindBy(xpath = "//span[contains(@id, ':InsBridgeQuoute-btnEl')or contains(@id, ':QuoteOrReview-btnEl') or contains(@id, 'RenewalQuote-btnEl') or contains(@id, ':QuoteOrReview-btnEl')]")
    private WebElement button_GenericWorkorderQuote;

    @FindBy(xpath = "//span[contains(@id, 'QuoteScreen:ttlBar')]")
    private WebElement titleBar_GenericWorkorderQuoteTitle;

    @FindBy(xpath = "//a[contains(@id, ':JobWizardToolbarButtonSet:BindOptions')]")
    protected WebElement button_GenericWorkorderSubmitOptionsWrap;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:BindOptions-btnEl')]")
    protected WebElement button_GenericWorkorderSubmitOptionsArrow;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:QuotePrintOptions-btnEl')]")
    private WebElement button_GenericWorkorderQuoteOptionsArrow;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:QuotePrintOptions:QuoteMenuItem-textEl')]")
    private WebElement button_GenericWorkOrderQuoteOptionQuote;

    @FindBy(xpath = "//span[contains(@id, ':QuoteWithoutDocuments-btnEl')]")
    private WebElement button_GenericWorkOrderQuoteOptionQuoteWithOutDocuments;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:BindOptions:BindOnly')]")
    private WebElement button_GenericWorkorderSubmitOptionsSubmitOnly;
    
    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:BindOptions:BindAndIssue')]")
	protected WebElement button_GenericWorkorderSubmitOptionsIssuePolicy;


    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:BindOptions:SendToRenewal-textEl')]")
    private WebElement button_GenericWorkorderSubmitOptionsRenew;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:RequestApproval') or contains(@id,':JobWizardToolbarButtonSet:BindOptions:BindOnly-textEl')]")
    private WebElement button_GenericWorkorderSubmitOptionsSubmit;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:Issue-btnEl')]")
    private WebElement button_GenericWorkorderIssueOptionsArrow;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:Issue:PolicyChange')]")
    private WebElement button_GenericWorkorderIssueOptionsIssueWithPolicyChange;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:Issue:PolicyCancel')]")
    private WebElement button_GenericWorkorderIssueOptionsIssueWithPolicyCancel;

    @FindBy(xpath = "//div[contains(@id, 'JobWizardToolbarButtonSet:Issue:NoActionRequired')]")
    private WebElement button_GenericWorkorderIssueOptionsIssueNoActionRequired;

    @FindBy(xpath = "//span[contains(@id, 'Standardize')]")
    private WebElement button_GenericWorkorderStandardize;

    @FindBy(xpath = "//span[contains(@id, 'Update')]")
    protected WebElement button_GenericWorkorderOverride;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:ConvertToFullApp')]")
    private WebElement button_GenericWorkorderFullApp;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:Draft-btnEl')]")
    private WebElement button_GenericWorkorderSaveDraft;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:WithdrawJob-btnEl')]")
    private WebElement button_WithdrawTransaction;

//    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:EditPolicy-btnEl') or contains(@id, ':JobWizardToolbarButtonSet:EditPolicyWorkflow-btnEl') or contains(@id, ':JobWizardToolbarButtonSet:EditPolicyPremiumStability-btnEl')]")
//    private WebElement button_EditPolicyTransaction;

    @FindBy(xpath = "//span[contains(@id, ':CloseOptions-btnWrap')]")
    private WebElement button_GenericWorkorderCloseOptions;

    @FindBy(xpath = "//a[contains(@id, ':CloseOptions:NotTakenJob-itemEl') or contains(@id, ':CloseOptions:SendToNotTaken-itemEl')]")
    private WebElement button_GenericWorkorderCloseOptionsNotTaken;
    
    @FindBy(xpath = "//a[contains(@id, 'CloseOptions:SendToNonRenewal-itemEl')]")
    private WebElement button_GenericWorkorderCloseOptionsNonRenew;    

    @FindBy(xpath = "//a[contains(@id, ':CloseOptions:Decline-itemEl')]")
    private WebElement button_GenericWorkorderCloseOptionsDeclined;

    @FindBy(xpath = "//span[contains(@id, ':RejectScreen:Update-btnEl')]")
    private WebElement button_NotTakenButton;

    @FindBy(xpath = "//span[contains(@id, 'WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton-btnEl')]")
    private WebElement button_Clear;

    // The following are for name change and policy change
    @FindBy(xpath = "//*[contains(@id, 'ChangeReason_FBM-inputEl')]")
    private WebElement reasonForChange;

    @FindBy(xpath = "//*[contains(@id, ':StartPolicyChangeDV:Description-inputEl')]")
    private WebElement Description;

    @FindBy(xpath = "//*[contains(@id, ':ExpirationDate-inputEl')]")
    private WebElement expirationDate;

    @FindBy(xpath = "//tr[contains(@id,'FBM_PolicyContactDetailsDV:Country-inputRow')]")
    private WebElement dropbox_AdditionalCountry;

    @FindBy(xpath = "//div[contains(@class,'x-grid-cell-inner')]/a[contains(@id,'Insured') and contains(@id,'LV:0:Name')]")
    private WebElement link_AdditionalInsured;

    @FindBy(xpath = "//div[@class='message']")
    private List<WebElement> validationErrors;


    @FindBy(xpath = "//a[contains(@id, ':InsBridgeQuouteCheck')]")
    private WebElement button_QuoteBlockingStep;

    public Guidewire8Select select_NotTakenReason() {
        return new Guidewire8Select(driver, "//table[contains(@id,':RejectReason-triggerWrap')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, ':RejectReasonText-inputEl')]")
    private WebElement textbox_NotTakenComments;

    @FindBy(xpath = "//span[contains(@id, ':BindPolicyChange-btnEl') or contains(@id, ':BindRewrite-btnEl')]")
    private WebElement button_GenericWorkorderPolicyChangeIssueOnly;


    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:CloseOptions-btnEl')]")
    protected WebElement button_GenericWorkorderCloseOptionsArrow;


    public void clickIssuePolicyButton() {
        clickWhenClickable(button_GenericWorkorderPolicyChangeIssueOnly);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    @FindBy(xpath = "//a[contains(@id, '__crumb__') or contains(@id, 'HOLocationFBMPopup:__crumb__')]")
    private WebElement link_ReturnTo;
    
    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:EmailAddress1-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:EmailAddress1-inputEl'))]")
	protected WebElement emailAddress;
    
    @FindBy(xpath = "//div[contains(@id, ':AddressLine1-inputEl')]")
    private WebElement text_MembershipRecordAddressLine1;
    
    @FindBy(xpath = "//div[contains(@id, ':City-inputEl')]")
    private WebElement text_MembershipRecordCity;
    
    @FindBy(xpath = "//div[contains(@id, ':State-inputEl')]")
    private WebElement text_MembershipRecordState;
    
    @FindBy(xpath = "//div[contains(@id, ':PostalCode-inputEl')]")
    private WebElement text_MembershipRecordZip;
  
    
    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setReasonNotTakenCode(ReasonNotTaken reasonCode) {
        Guidewire8Select mySelect = select_NotTakenReason();
        mySelect.selectByVisibleText(reasonCode.getValue());
    }


    public void setNotTakenComments(String comment) {
        waitUntilElementIsClickable(textbox_NotTakenComments);
        textbox_NotTakenComments.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textbox_NotTakenComments.sendKeys(comment);
    }


    public void clickButtonNotTaken() {
        button_NotTakenButton.click();
    }


    public String getValidationResults() {
        List<WebElement> validatinResults = finds(By.xpath("//span[contains(text(), 'Validation Results')]/ancestor::tr[1]/following-sibling::tr/child::td/child::div/child::div[@class= 'message']"));
        if (!validatinResults.isEmpty()) {
            String text = validatinResults.get(0).getText();
            clickWhenClickable(button_Clear);
            return text;
        }
        return "";
    }
    
    public boolean validationMessagesContains(String validation) {
        List<WebElement> validatinResults = finds(By.xpath("//span[contains(text(), 'Validation Results')]/ancestor::tr[1]/following-sibling::tr/child::td/child::div/child::div[@class= 'message']"));
        for(WebElement message : validatinResults) {
        	if(message.getText().contains(validation)) {
        		return true;
        	}
        }
        return new GuidewireHelpers(driver).containsErrorMessage(validation);
    }


    public List<WebElement> getValidationResultsList() {
        return validationErrors;
    }
    
    public List<String> getValidationMessagesAlternate() {
    	List<WebElement> messages = getValidationResultsList();
    	List<String> errors = new ArrayList<>();
    	for(WebElement element : messages) {
    		errors.add(element.getText());
    	}
    	
    	return errors;
    }


    public void clickClear() {
        clickWhenClickable(button_Clear);
    }


    public void requestApproval(String approvalMessage) {
        Agents currentLoggedIn = null;

        TopInfo topInfoStuff = new TopInfo(driver);
        try {
            currentLoggedIn = topInfoStuff.getCurrentLoggedIn();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        clickProductLogo();
        String nullAgent = topInfoStuff.getCurrentlyLoggedIn();
        String accountNumber = new InfoBar(driver).getInfoBarAccountNumber();
        
        
        clickGenericWorkorderSubmitOptionsSubmit();
        repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
        activity.setText(approvalMessage);
        activity.setChangeReason(ChangeReason.Other);
        activity.clickSendRequest();

        repository.pc.workorders.generic.GenericWorkorderComplete complete = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();

        repository.pc.policy.PolicySummary summary = new repository.pc.policy.PolicySummary(driver);
        summary.clickAccountNumber();

        repository.pc.account.AccountSummaryPC accountSummary = new repository.pc.account.AccountSummaryPC(driver);
        Underwriters assignedTo = accountSummary.getAssignedToUW(approvalMessage);
        
        if(assignedTo == null) {
        	assignedTo = accountSummary.getAssignedToUW("Submitted policy change");
        	approvalMessage = "Submitted policy change";
        }

        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        gwHelpers.logout();

        Login login = new Login(driver);
        login.loginAndSearchAccountByAccountNumber(assignedTo.getUnderwriterUserName(), assignedTo.getUnderwriterPassword(), accountNumber);
        accountSummary = new repository.pc.account.AccountSummaryPC(driver);
        // search for activity
        waitForPostBack();
        accountSummary.clickActivitySubject(approvalMessage);

        repository.pc.workorders.change.StartPolicyChange policyChange = new repository.pc.workorders.change.StartPolicyChange(driver);
        policyChange.clickIssuePolicy();

        gwHelpers.logout();
        if(currentLoggedIn != null) {
        	login.login(currentLoggedIn.getAgentUserName(), currentLoggedIn.getAgentPassword());
        } else {
        	String[] split = nullAgent.split(" ");
        	login.login(split[0].substring(0, 1), split[split.length -1]);
        }
        

    }
    

    public void requestApproval(String approvalMessage, String accountNumber) {
        Agents currentLoggedIn = null;

        TopInfo topInfoStuff = new TopInfo(driver);
        try {
            currentLoggedIn = topInfoStuff.getCurrentLoggedIn();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        clickGenericWorkorderSubmitOptionsSubmit();
        repository.pc.activity.UWActivityPC activity = new UWActivityPC(driver);
        activity.setText(approvalMessage);
        activity.setChangeReason(ChangeReason.Other);
        activity.clickSendRequest();

        repository.pc.workorders.generic.GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();

        repository.pc.policy.PolicySummary summary = new PolicySummary(driver);
        summary.clickAccountNumber();

        repository.pc.account.AccountSummaryPC accountSummary = new repository.pc.account.AccountSummaryPC(driver);
        Underwriters assignedTo = accountSummary.getAssignedToUW(approvalMessage);


        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        gwHelpers.logout();

        Login login = new Login(driver);
        login.loginAndSearchAccountByAccountNumber(assignedTo.getUnderwriterUserName(), assignedTo.getUnderwriterPassword(),
                accountNumber);

        accountSummary = new AccountSummaryPC(driver);
        // search for activity
        accountSummary.clickActivitySubject(approvalMessage);

        repository.pc.workorders.change.StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.clickIssuePolicy();

        topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

        login.login(currentLoggedIn.getAgentUserName(), currentLoggedIn.getAgentPassword());

    }


    public void clickNotTaken() {
        hoverOverAndClick(button_GenericWorkorderCloseOptions);
        hoverOverAndClick(button_GenericWorkorderCloseOptionsNotTaken);
    }


    public void clickNonRenew() {
        hoverOverAndClick(button_GenericWorkorderCloseOptions);
        hoverOverAndClick(button_GenericWorkorderCloseOptionsNonRenew);
    }
    
    public boolean checkNotTaken() {
        if (!checkIfElementExists(button_GenericWorkorderCloseOptionsNotTaken, 2)) {
            clickWhenClickable(button_GenericWorkorderCloseOptions);
        }
        return checkIfElementExists(button_GenericWorkorderCloseOptionsNotTaken, 2);
    }

    public boolean checkDeclined() {
        if (!checkIfElementExists(button_GenericWorkorderCloseOptionsDeclined, 2)) {
            clickWhenClickable(button_GenericWorkorderCloseOptions);
        }
        return checkIfElementExists(button_GenericWorkorderCloseOptionsDeclined, 2);
    }


    public void setChangeReason(String reason) {
        clickWhenClickable(reasonForChange);
        reasonForChange.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        reasonForChange.sendKeys(reason);
    }


    public void setDescription(String description) {
        clickDescription();
        Description.sendKeys(description);
    }


    public void clickDescription() {
        clickWhenClickable(Description);
    }


    public void clickGenericWorkorderSubmitOptionsRenew() {
        waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsArrow).click();
        waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsRenew).click();
    }


    public void clickGenericWorkorderSubmitOptionsSubmitOnly() {
        clickWhenClickable(button_GenericWorkorderSubmitOptionsArrow);
        waitForPostBack();
        button_GenericWorkorderSubmitOptionsSubmitOnly.click();
        waitForPostBack();
    }
    
    public boolean checkGenericWorkorderSubmitOptionsSubmitOnlyOption() {
        clickWhenClickable(button_GenericWorkorderSubmitOptionsArrow);
        return checkIfElementExists(button_GenericWorkorderSubmitOptionsSubmitOnly, 1);
    }
    
    public boolean checkGenericWorkorderSubmitOptionsIssuePolicyOption() {
//        boolean IssuePolicyFound = false;
        clickWhenClickable(button_GenericWorkorderSubmitOptionsArrow);
        return checkIfElementExists(button_GenericWorkorderSubmitOptionsIssuePolicy, 1);
//        clickWhenClickable(button_GenericWorkorderSubmitOptionsArrow);
//        return IssuePolicyFound;
    }
    

    public void clickGenericWorkorderSubmitOptionsSubmit() {
        if (checkIfElementExists(button_GenericWorkorderSubmitOptionsArrow, 2)) {
            waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsArrow).click();
            waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsSubmit).click();
        } else {
            find(By.xpath("//span[contains(@id, ':JobWizardToolbarButtonSet:RequestApproval-btnEl')]")).click();
        }

    }


    public void clickGenericWorkorderCancel() {
        super.clickCancel();
    }


    public void clickGenericWorkorderIssue(IssuanceType type) {
        try {
            waitUntilElementIsClickable(button_GenericWorkorderIssueOptionsArrow).click();
        } catch (Exception e) {
            waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsWrap).click();
        }


        switch (type) {
            case FollowedByPolicyChange:
                waitUntilElementIsClickable(button_GenericWorkorderIssueOptionsIssueWithPolicyChange).click();
                break;
            case FollowedByPolicyCancel:
                waitUntilElementIsClickable(button_GenericWorkorderIssueOptionsIssueWithPolicyCancel).click();
                break;
            case NoActionRequired:
                waitUntilElementIsClickable(button_GenericWorkorderIssueOptionsIssueNoActionRequired).click();
                break;
            default:
                waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsIssuePolicy).click();
                break;
        }
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public String selectOKOrCancelFromPopup(OkCancel okOrCancel) {
        String popUpTextContents = "";
        try {
            popUpTextContents = super.selectOKOrCancelFromPopup(okOrCancel);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return popUpTextContents;
    }

    public void clickGenericWorkorderUpdate() {
        super.clickUpdate();
    }


    public void clickGenericWorkorderBack() {
        super.clickBack();
    }


    public void clickPolicyChangeNext() {
        super.clickNext();
    }


    public boolean checkGenericWorkorderNextExists() {
        if (checkIfElementExists("//span[contains(@id, ':Next-btnEl') or contains(@id, ':Next-btnInnerEl') or contains(@id, ':nextButton')]", 1)) {
            return true;
        } else {
            return checkIfElementExists("//a[contains(@id, ':Next')]", 1);
        }
    }


    public void clickGenericWorkorderQuote() {
        //jlarsen 9/27/2016
        //R2 change that has "Quote" and "Quote without documents" as options on policy change.
        try {
            clickWhenClickable(button_GenericWorkorderQuote);
        } catch (Exception e) {
            if (checkIfElementExists(find(By.xpath("//a[contains(@id, ':InsBridgeQuoute')]")), 1)) {
                clickWhenClickable(find(By.xpath("//a[contains(@id, ':InsBridgeQuoute')]")));
            } else {
                clickWhenClickable(find(By.xpath("//span[contains(@id, ':QuotePrintOptions-btnEl')]")));
                clickWhenClickable(find(By.xpath("//*[contains(@id, ':QuotePrintOptions:QuoteMenuItem')]")));
            }
        }
    }


    public void clickGenericWorkorderQuoteWithoutDocuments() {
        //jlarsen 9/27/2016
        //R2 change that has "Quote" and "Quote without documents" as options on policy change.
        clickWhenClickable(button_GenericWorkOrderQuoteOptionQuoteWithOutDocuments);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public boolean genericWorkorderQuoteWithoutDocumentsExists() {
        System.out.println("Quote without Document was found " + checkIfElementExists("//span[contains(@id, ':QuoteWithoutDocuments-btnEl')]", 1));
        return checkIfElementExists("//span[contains(@id, ':QuoteWithoutDocuments-btnEl')]", 1);
    }


    public boolean genericWorkorderQuoteWithoutDocumentsUnselectable() {
        System.out.println("Quote without Document was found " + checkIfElementExists("//span[contains(@id, ':QuoteWithoutDocuments-btnEl')]", 1));
        if (checkIfElementExists("//span[contains(@id, ':QuoteWithoutDocuments-btnEl')]", 1)) {
            clickWhenClickable(button_GenericWorkOrderQuoteOptionQuoteWithOutDocuments);
            return find(By.xpath("//a[contains(@id, ':QuoteWithoutDocuments')]")).getAttribute("unselectable").equals("on");
        }
        return false;
    }


    public boolean isQuotable() {
        //jlarsen 2/18/2016
        //swapped this out for the config because the webelement call kept returning null :(
        List<WebElement> quotable = finds(By.xpath("//span[contains(@id, ':InsBridgeQuoute-btnEl') or contains(@id, ':QuoteOrReview-btnEl')]/ancestor::a[1]"));
        if (quotable.isEmpty()) {
            return true;
        } else {
            String elementClasses = quotable.get(0).getAttribute("class");
            return !elementClasses.contains("x-btn-disabled");
        }
    }


    public boolean isSubmittable() {
        try {
            String elementClasses = button_GenericWorkorderSubmitOptionsWrap.getAttribute("class");
            boolean isSubmittable = !elementClasses.contains("x-btn-disabled");
            return isSubmittable;
        } catch (Exception e) {
            String elementClasses = button_GenericWorkorderSubmitOptionsSubmit.findElement(By.xpath(".//ancestor::a")).getAttribute("class");
            boolean isSubmittable = !elementClasses.contains("x-btn-disabled");
            return isSubmittable;
        }
    }


    public void clickGenericWorkorderFullApp() {
        clickWhenClickable(button_GenericWorkorderFullApp);
    }


    public void clickGenericWorkorderSaveDraft() {
        clickWhenClickable(button_GenericWorkorderSaveDraft);
    }


    public void clickWithdrawTransaction() {
        if (!checkIfElementExists(button_WithdrawTransaction, 2)) {
            clickWhenClickable(button_GenericWorkorderCloseOptions);
        }
        hoverOverAndClick(button_WithdrawTransaction);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

//jlarsen saem method exists in guidewire helpers
//    public void clickEditTransaction() {
//        clickWhenClickable(button_EditPolicyTransaction);
//        selectOKOrCancelFromPopup(OkCancel.OK);
//    }
//
//
//    public void ifExistsClickEditTransaction() {
//        if (checkIfElementExists(button_EditPolicyTransaction, 1)) {
//            clickWhenClickable(button_EditPolicyTransaction);
//            selectOKOrCancelFromPopup(OkCancel.OK);
//        }
//    }


    public void setExpirationDate(Date expDate) {
    	setText(expirationDate, DateUtils.dateFormatAsString("MM/dd/yyyy", expDate));
        clickProductLogo();
    }


    public void checkAdditionalCountry() {
        if (checkIfElementExists(dropbox_AdditionalCountry, 1)) {
            Assert.fail("ERROR: Additional Country is displayed on page");
        }
    }


    public boolean checkWithdrawPolicy() {
        if (!checkIfElementExists(button_WithdrawTransaction, 2)) {
            clickWhenClickable(button_GenericWorkorderCloseOptions);
        }
        return checkIfElementExists(button_WithdrawTransaction, 2);
    }


    public void clickAdditionalInsuredName() {
        clickWhenClickable(link_AdditionalInsured);
    }


    public boolean checkIfVersionExists() {
        return finds(By.xpath("//a[contains(@id, 'SubmissionWizard:Next-btnInnerEl')]/ancestor::div[1]/descendant::a[contains(., 'Version')]")).size() > 0;
    }


    public void clickGenericWorkorderQuoteOptionsQuote() {
        waitUntilElementIsClickable(button_GenericWorkorderQuoteOptionsArrow).click();
        waitUntilElementIsClickable(button_GenericWorkOrderQuoteOptionQuote).click();
    }


    public void clickReturnTo() {
        clickWhenClickable(link_ReturnTo);
    }


    public boolean SubmitOptionsButtonExists() {
        return checkIfElementExists(button_GenericWorkorderSubmitOptionsArrow, 2);

    }


    public void clickQuoteBlockingStep() {
        clickWhenClickable(button_QuoteBlockingStep);
    }

    public boolean checkCloseOptionsExists() {
        if (checkIfElementExists(button_GenericWorkorderCloseOptions, 1)) {
            return true;
        } else {
            return checkIfElementExists("//a[contains(@id, ':CloseOptions')]", 1);
        }
    }
    
    public void setNewAddress(AddressInfo address) {
    	repository.pc.contact.ContactEditPC editContact = new ContactEditPC(this.driver);
    	editContact.setNewAddress(address);
    }
    
    public AddressInfo getDesignatedAddress() {
    	return new AddressInfo(text_MembershipRecordAddressLine1.getText(), text_MembershipRecordCity.getText(), State.valueOfName(text_MembershipRecordState.getText()), text_MembershipRecordZip.getText());
    }
}





















