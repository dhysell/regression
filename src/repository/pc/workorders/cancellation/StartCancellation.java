package repository.pc.workorders.cancellation;

import com.idfbins.enums.OkCancel;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Cancellation;
import repository.gw.enums.Cancellation.CancellationSource;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.generate.custom.CancellationObject;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.pc.policy.PolicyMenu;
import repository.pc.workorders.generic.GenericWorkorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartCancellation extends GenericWorkorder {

    private WebDriver driver;

    public StartCancellation(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select select_Source() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Source-triggerWrap')]");
    }
    
    @FindBy(xpath = "//div[contains(@id, ':CancelPolicyDV:Source-inputEl')]")
    private WebElement label_CancellationSource;

    private Guidewire8Select select_Reason() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Reason-triggerWrap') or contains(@id, ':reason-triggerWrap')]");
    }
    
    @FindBy(xpath = "//div[contains(@id, 'PolicyDV:ReasonForMembership-inputEl') or contains(@id, ':CancelPolicyDV:ReasonForNonMembership-inputEl')]")
    private WebElement label_CancellationReason;

    private Guidewire8Select select_Explanation() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Explanation-triggerWrap') or contains(@id, ':explanation-triggerWrap')]");
    }
    
    @FindBy(xpath = "//div[contains(@id, 'PolicyDV:ExplanationMembership-inputEl') or contains(@id, ':CancelPolicyDV:ReasonExplanationNonMembership-inputEl')]")
    private WebElement label_CancellationReasonExplanation;

    @FindBy(xpath = "//textarea[contains(@id, ':ReasonDescription-inputEl') or contains(@id, ':reasonDescription-inputEl')]")
    private WebElement editbox_Description;

    @FindBy(xpath = "//textarea[contains(@id, 'PolicyDV:ReasonDescription-inputEl') or contains(@id, ':CancelPolicyDV:ReasonDescription-inputEl')]")
    private WebElement label_CancellationReasonDescription;
    
    @FindBy(xpath = "//input[contains(@id, ':CancelDate_date-inputEl') or contains(@id, ':CancelDate-inputEl')]")
    private WebElement editbox_EffectiveDate;

    @FindBy(xpath = "//div[contains(@id, ':CancelDate_date-inputEl') or contains(@id, ':CancelDate-inputEl')]")
    private WebElement text_EffectiveDate;

    @FindBy(xpath = "//a[contains(@id, 'StartCancellation:StartCancellationScreen:NewCancellation')]")
    private WebElement button_StartCancellation;

    @FindBy(xpath = "//div[contains(@id, ':BindOptions:CancelNow') or contains(@id, 'CancellationWizard:CancellationWizard_QuoteScreen:JobWizardToolbarButtonSet:BindOptions:CancelNow') or contains(@id, ':CancellationWizard_MultiLine_QuoteScreen:JobWizardToolbarButtonSet:BindOptions:CancelNow-itemEl')]")
    private WebElement button_SubmitOptionsCancelNow;

    @FindBy(xpath = "//*[contains(@id, ':BindOptions:SubmitCancellation-textEl') or contains(@id, ':BindOptions:SubmitCancellation-itemEl')]")
    private WebElement button_SubmitOptionsScheduleCancellation;

    @FindBy(xpath = "//*[contains(@id, ':JobWizardToolbarButtonSet:CloseOptions:RescindCancellation-textEl') or contains(@id, ':JobWizardToolbarButtonSet:CloseOptions:RescindCancellation-itemEl')]")
    private WebElement button_CloseOptionsRescindCancellation;

    @FindBy(xpath = "//*[contains(@id, ':JobWizardToolbarButtonSet:CloseOptions:RescindCancellationWhithoutDocuments-textEl') or contains(@id, ':JobWizardToolbarButtonSet:CloseOptions:RescindCancellationWhithoutDocuments-itemEl')]")
    private WebElement button_CloseOptionsRescindCancellationWithOutDocumentation;

    @FindBy(xpath = "//label[contains(@id, 'ErrorMessage')]")
    private WebElement cancellationError;

    @FindBy(xpath = "//input[contains(@id, ':delinquentAmount-inputEl') or contains(@id, 'EntryScreen:amount-inputEl')]")
    private WebElement editbox_DelinquentAmount;

    @FindBy(xpath = "//span[contains(@id, ':Finish-btnEl')]")
    private WebElement button_Finish;

    @FindBy(xpath = "//span[contains(@id, 'JobComplete:JobCompleteScreen:ttlBar')]")
    private WebElement input_JobCompleteTitle;

    @FindBy(xpath = "//span[contains(@id, 'CancellationWizard_EntryScreen:ttlBar')]")
    private WebElement label_CancellationEntryTitle;


    public void setSource(String sourceToSelect) {
        
        Guidewire8Select source = select_Source();
        source.selectByVisibleText(sourceToSelect);
    }

    public CancellationSource getSourceValue() {
    	waitUntilElementIsVisible(label_CancellationSource);
        return CancellationSource.valueOf(label_CancellationSource.getText());
    }

    public List<String> getSource() {
        Guidewire8Select source = select_Source();
        return source.getList();
    }


    public void setCancellationReason(String reasonToSelect) {
        Guidewire8Select reason = select_Reason();
        reason.selectByVisibleText(reasonToSelect);
    }

    public String getCancellationReasonValue() {
    	waitUntilElementIsVisible(label_CancellationReason);
        return label_CancellationReason.getText();
    }
    
    public List<String> getCancellationReason() {
        Guidewire8Select reason = select_Reason();
        return reason.getList();
    }


    public void setExplanation(String explanationToSelect) {
        
        Guidewire8Select explanation = select_Explanation();
        explanation.selectByVisibleText(explanationToSelect);
    }
    
    public String getCancellationReasonExplanationValue() {
    	waitUntilElementIsVisible(label_CancellationReasonExplanation);
        return label_CancellationReasonExplanation.getText();
    }
    
    public List<String> getCancellationReasonExplanation() {
        
        Guidewire8Select reasonExplanation = select_Explanation();
        return reasonExplanation.getList();
    }


    public void clickNext() {
        super.clickNext();
    }


    public void clickFinsh() {
        clickWhenClickable(button_Finish);
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation explanationToSelect) {
        setSource(explanationToSelect.getSourceValue());
        setCancellationReason(explanationToSelect.getReasonValue());
        setExplanation(explanationToSelect.getExplanationValue());
    }
    public String getCancellationReasonDescriptionValue() {
    	waitUntilElementIsVisible(label_CancellationReasonDescription);
        return label_CancellationReasonDescription.getText();
    }

    public void setPendingSourceReasonAndExplanation(Cancellation.PendingCancellationSourceReasonExplanation explanationToSelect) {
        
        setSource(explanationToSelect.getSourceValue());
        
        setCancellationReason(explanationToSelect.getReasonValue());
        
        setExplanation(explanationToSelect.getExplanationValue());
    }


    public void setDescription(String description) {
        waitUntilElementIsVisible(editbox_Description);
        
        editbox_Description.sendKeys(description);
    }


    public void setEffectiveDate(Date date) {
        if (checkIfElementExists(editbox_EffectiveDate, 1000)) {
            waitUntilElementIsVisible(editbox_EffectiveDate);
            
            editbox_EffectiveDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            editbox_EffectiveDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
            
            clickWhenClickable(editbox_Description);
            
        }
    }


    public Date getEffectiveDate() {
        
        Date dateToReturn = null;
        try {
	        if (checkIfElementExists(editbox_EffectiveDate, 1000)) {
	            waitUntilElementIsVisible(editbox_EffectiveDate);
	            
	            dateToReturn = DateUtils.convertStringtoDate(editbox_EffectiveDate.getAttribute("value"), "MM/dd/yyyy");
	        } else {
	            dateToReturn =  DateUtils.convertStringtoDate(text_EffectiveDate.getText(), "MM/dd/yyyy");
	        }
        } catch (Exception e) {
        	Assert.fail("There was an error parsing the date.");
        }
        return dateToReturn;
    }


    public void setDelinquentAmount(double delinquentAmount) {
        clickWhenClickable(editbox_DelinquentAmount);
        
        editbox_DelinquentAmount.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DelinquentAmount.sendKeys(String.valueOf(NumberUtils.round(delinquentAmount, 2)));
    }


    public void clickStartCancellation() {
        
        clickWhenClickable(button_StartCancellation);
    }


    public void clickSubmitOptionsCancelNow() {
        if (checkIfElementExists(button_GenericWorkorderSubmitOptionsArrow, 1000)) {
            waitUntilElementIsVisible(button_GenericWorkorderSubmitOptionsArrow).click();
            waitUntilElementIsVisible(button_SubmitOptionsCancelNow).click();
            selectOKOrCancelFromPopup(OkCancel.OK);
        } else {	
            button_GenericWorkorderSubmitOptionsWrap.click();
        }
    }

    public String getErrorMessage() {
        String message = null;
        boolean errorExists = checkIfElementExists(cancellationError, 1000);
        if (errorExists) {
            message = cancellationError.getText();
        }
        return message;
    }


    public void clickSubmitOptionsScheduleCancellation() {
        waitUntilElementIsVisible(button_GenericWorkorderSubmitOptionsArrow).click();
        waitUntilElementIsVisible(button_SubmitOptionsScheduleCancellation).click();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public CancellationObject cancelPolicy(Cancellation.CancellationSourceReasonExplanation explanationToSelect, String description, Date date, boolean cancelNow) {
    	
    	CancellationObject toReturn = new CancellationObject(explanationToSelect, description, date, cancelNow, null);
    	
        repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
        policyMenu.clickMenuActions();
        
        policyMenu.clickCancelPolicy();
        
        setSourceReasonAndExplanation(explanationToSelect);
        if (description != null) {
            setDescription(description);
        }
        
        if (date != null) {
            setEffectiveDate(date);
        }

        clickStartCancellation();
        
      //CancellationWizard:0_header_hd-textEl     Cancellation 0823465801-0003
        String tempString = find(By.xpath("//span[contains(@id, 'CancellationWizard:0_header_hd-textEl')]")).getText();
        toReturn.setTransactionNumber(tempString.substring(tempString.indexOf("0"), tempString.indexOf("\n")));
        
        waitUntilElementIsVisible(label_CancellationEntryTitle, 60);
        try {
        clickNext();
		} catch (Exception e) {
			//EMPTY TRY CATCH CUS I DON'T HAVE TIME RIGH TNOW TO GO THRU ALL THE CANCELS AND SEE IF THIS IS NEEDED. 
		}
        
        if (cancelNow) {
            clickSubmitOptionsCancelNow();
        } else {
            clickSubmitOptionsScheduleCancellation();
        }
        
        
        
        return toReturn;
    }


    public void pendingCancelPolicy(Cancellation.PendingCancellationSourceReasonExplanation explanationToSelect, String description, Date date, boolean cancelNow) {
        repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickPendingCancel();
        
        setPendingSourceReasonAndExplanation(explanationToSelect);
        if (description != null) {
            setDescription(description);
        }

        if (date != null) {
            setEffectiveDate(date);
        }

        clickStartCancellation();
        
        clickNext();

        
        if (cancelNow) {
            clickSubmitOptionsCancelNow();
        } else {
            clickSubmitOptionsScheduleCancellation();
        }
    }


    public void cancelPolicy(Cancellation.CancellationSourceReasonExplanation explanationToSelect, String description, Date date, boolean cancelNow, double delinquentAmount) {
        repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickCancelPolicy();
        setSourceReasonAndExplanation(explanationToSelect);
        if (description != null) {
            setDescription(description);
        }

        if (date != null) {
            setEffectiveDate(date);
        }

        setDelinquentAmount(delinquentAmount);

        clickStartCancellation();

        
        if (cancelNow) {
            clickSubmitOptionsCancelNow();
        } else {
            clickSubmitOptionsScheduleCancellation();
        }
    }


    public void pendingCancelPolicy(Cancellation.PendingCancellationSourceReasonExplanation explanationToSelect, String description, Date date, boolean cancelNow, double delinquentAmount) {
        repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickPendingCancel();
        setPendingSourceReasonAndExplanation(explanationToSelect);
        if (description != null) {
            setDescription(description);
        }

        if (date != null) {
            setEffectiveDate(date);
        }

        setDelinquentAmount(delinquentAmount);

        clickFinsh();
    }


    public boolean verifyInsuredReasons() {
        setSource("Insured");
        List<String> reasons = getCancellationReason();
        return new GuidewireHelpers(driver).compareEnumValuesWithListValues(Cancellation.AgentCancellationSource.class, reasons, "Insured Cancellation Reasons");
    }


    public boolean verifyInsuredReasonExplanations(String source, String reason) {
        setSource(source);
        setCancellationReason(reason);
        List<String> reasonExplanations = getCancellationReasonExplanation();
        List<String> explanationsEnum = new ArrayList<String>();
        for (Cancellation.CancellationSourceReasonExplanationPL explanation : Cancellation.CancellationSourceReasonExplanationPL.values()) {
            if (Cancellation.CancellationSourceReasonExplanationPL.valueOf(reason).equals(explanation)) {
                explanationsEnum.add(explanation.getExplanationValue());
            }
        }
        try {
        	new GuidewireHelpers(driver).verifyLists(reasonExplanations, explanationsEnum);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

//	private void eraseThis(){
//		
//		setCancellationReason("Policy not-taken");
//		setSource("Insured");
//		setCancellationReason("Insured Requested");
//		reasonExplanations = getCancellationReasonExplanation();
//		match[1] = compareEnumValuesWithListValues(Cancellation.CancellationReasonExplanationPolicyNotTakenPL.class, reasonExplanations, "Insured Cancellation Reason Explanations");
//		
//		setCancellationReason("Rewritten to another policy");
//		setSource("Insured");
//		setCancellationReason("Insured Requested");
//		reasonExplanations = getCancellationReasonExplanation();
//		match[2] = compareEnumValuesWithListValues(Cancellation.CancellationReasonExplanationRewrittenPL.class, reasonExplanations, "Insured Cancellation Reason Explanations");
//		return error;			
//	}


    public void verifyReasonExplanation() {

        for (Cancellation.CancellationSourceReasonPL reason : Cancellation.CancellationSourceReasonPL.values()) {
            setSource(reason.getSourceInner().toString());
            setCancellationReason(reason.getReasonValueInner());
            System.out.println("String Source is " + reason.getSourceInner().toString() + " and Starting Reason is " + reason.getReasonValueInner());
            List<String> uiExplanations = getCancellationReasonExplanation();
            if (uiExplanations.get(0).equals("<none>")) {
                uiExplanations.remove(0);
            }
            List<String> cancellationExplanations = new ArrayList<String>();
            for (Cancellation.CancellationSourceReasonExplanationPL searchExplanation : Cancellation.CancellationSourceReasonExplanationPL.values()) {
                if (searchExplanation.getReasonValue().equals(reason.getReasonValueInner())) {
                    cancellationExplanations.add(searchExplanation.getExplanationValue());
                }
            }
            new GuidewireHelpers(driver).verifyLists(uiExplanations, cancellationExplanations);
        }
    }


    public ArrayList<String> cancellationEffectiveDates(Date inceptionDate) {
        ArrayList<String> errors = new ArrayList<String>();
        Date currentDate = DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.PolicyCenter);
        for (Cancellation.CancellationSourceReasonPL explanation : Cancellation.CancellationSourceReasonPL.values()) {
            setSource(explanation.getSourceInner().toString());
            setCancellationReason(explanation.getReasonValueInner());
//			setExplanation(explanation.getExplanationValue());
            Date effectiveDate = getEffectiveDate();
            int daysBeforeCancel = DateUtils.getDifferenceBetweenDates(currentDate, effectiveDate, DateDifferenceOptions.Day);
            daysBeforeCancel++;
            switch (explanation.getDays()) {
                case 30:
                    if (!(daysBeforeCancel == 30)) {
                        errors.add("Reason: " + explanation.getReasonValueInner() + ", " + " the cancellation date must be 30 days from today.");
                    }
                    break;
                case 20:
                    if (!(daysBeforeCancel == 20)) {
                        errors.add("Reason: " + explanation.getReasonValueInner() + ", " + " the cancellation date must be 20 days from today.");
                    }
                    break;
                case 0:
                    if (!(daysBeforeCancel == 0)) {
                        errors.add("Reason: " + explanation.getReasonValueInner() + " the cancellation effective date must be today.");
                    }
                    break;
                case -1:
                    if (!(inceptionDate.equals(effectiveDate))) {
                        errors.add("Reason: " + explanation.getReasonValueInner() + ", " + " the cancellation date must default to the inception date of the policy. \n Currently, the requirement is "
                                + effectiveDate + " and the requirement is " + explanation.getDays());
                    }
                    break;
                default:
                    break;
            }
        }
        return errors;
    }


    public void clickNextButton() {
        clickNext();
        
    }


    public void clickCloseOptionsRescindCancellation() {
        waitUntilElementIsVisible(button_GenericWorkorderCloseOptionsArrow).click();
        waitUntilElementIsVisible(button_CloseOptionsRescindCancellation).click();
    }


    public void clickCloseOptionsRescindCacellationWithoutDocumentation() {
        waitUntilElementIsVisible(button_GenericWorkorderCloseOptionsArrow).click();
        waitUntilElementIsVisible(button_CloseOptionsRescindCancellationWithOutDocumentation).click();
    }


    public String getCancellationJobCompleteTitle() {
        return input_JobCompleteTitle.getText();
    }
    
    public void cancelRescind(boolean withDocuments){
		repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(getDriver());
		policyMenu.clickMenuActions();
		policyMenu.clickRescindCancellation();
		clickNextButton();
		if (withDocuments) {
			clickCloseOptionsRescindCancellation();
		} else {
			clickCloseOptionsRescindCacellationWithoutDocumentation();
		}
		

    }
    
    public void clickViewPolicyLink() {
    	clickWhenClickable(By.xpath("//*[contains(text(), 'View your policy')]"));
    }

}
