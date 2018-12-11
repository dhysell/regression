package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class GenericWorkorderComplete extends GenericWorkorder {

    public GenericWorkorderComplete(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // JobComplete:JobWizardInfoBar:Underwriter//span//span[3]
    // JobComplete:JobWizardInfoBar:PolicyNumber//span//span[3]

    // Jon Larsen added xpath to account for smaller screen size where not all
    // infobar elements are visible
    @FindBy(xpath = "//*[contains(@id, 'InfoBar:AccountNumber-btnInnerEl')]/span[2]")
    private WebElement AccountNumber;

    @FindBy(xpath = "//*[contains(@eventid, 'InfoBar:AccountNumber')]/a/span/child::span[2]")
    private WebElement altAccountNumber;

    @FindBy(xpath = "//*[contains(@id, 'InfoBar:PolicyNumber-btnInnerEl')]/span[2]")
    private WebElement PolicyNumber;

    @FindBy(xpath = "//*[contains(@eventid, 'InfoBar:PolicyNumber')]/a/span/child::span[2]")
    private WebElement altPolicyNumber;

    @FindBy(xpath = "//div[contains(@id, 'JobComplete:JobCompleteScreen:JobCompleteDV:ViewPolicy-bodyEl')]")
    private WebElement link_PolicyChangeQuotedPolicyNumber;

    @FindBy(xpath = "//*[contains(@id, 'InfoBar:Underwriter-btnInnerEl')]/span[2]")
    private WebElement UnderWriter;

    @FindBy(xpath = "//*[contains(@id, 'InfoBar:Agent-btnInnerEl')]/span[2]")
    private WebElement Agent;

    @FindBy(xpath = "//div[contains(@eventid, 'InfoBar:Underwriter')]/a/span/child::span[2]")
    private WebElement altUnderWriter;

    @FindBy(xpath = "//div[contains(@id, 'JobComplete:JobCompleteScreen:JobCompleteDV')]")
    public WebElement label_JobCompleteTitleBar;

    @FindBy(xpath = "//label[contains(@id, ':JobCompleteScreen:Message')]")
    public WebElement label_JobCompleteMessage;

    @FindBy(xpath = "//span[contains(@id, 'infoBar-menu-trigger-btnEl')]")
    public WebElement button_InfobarMenu;

    @FindBy(xpath = "//div[contains(@eventid, 'JobComplete:JobWizardInfoBar:PaymentPlan')]/parent::div/parent::div/parent::div/parent::div[contains(@style, 'visibility: hidden')]")
    public WebElement menuPaymentPlan;

    @FindBy(xpath = "//div[contains(@id, 'JobCompleteDV:ViewPolicy-inputEl')]")
    public WebElement link_ViewYourPolicy;

    @FindBy(xpath = "//div[contains(@id, 'JobCompleteDV:ViewJob-inputEl')]")
    public WebElement link_ViewYourSubmission;

    @FindBy(xpath = "//div[contains(@id, 'JobCompleteDV:ReturnToDesktop-inputEl')]")
    public WebElement link_ViewDesktop;

    @FindBy(xpath = "//div[contains(@id, ':JobCompleteDV:ResolveWithFutureUnboundPeriods-inputEl') or contains(@id, ':JobCompleteDV:ResolveWithFutureBoundPeriods-inputEl')]")
    public WebElement link_ApplyChangeToFuturePeriods;

    @FindBy(xpath = "//span[contains(@id, ':JobLabel-btnEl')]")
    public WebElement link_CurrentJobLevel;

    @FindBy(xpath = "//div[contains(@id, 'JobComplete:JobCompleteScreen:JobCompleteDV')]")
    public WebElement div_JobCompleteOptions;


    public boolean woCompletePageExists() {
        return checkIfElementExists(div_JobCompleteOptions, 2000);
    }


    public String getAccountNumber() {
        if (AccountNumber.getText() == null || AccountNumber.getText().equals("")) {
            openInfoMenu();
            
            return altAccountNumber.getText();
        } else {
            return AccountNumber.getText();
        }
    }


    public void clickInfobarMenu() {
        button_InfobarMenu.click();
    }


    public String getPolicyNumber() {
        if (PolicyNumber.getText() == null || PolicyNumber.getText().equals("")) {
            openInfoMenu();
            
            return altPolicyNumber.getText();
        } else {
            return PolicyNumber.getText();
        }
    }


    public String getUnderwriter() {
        if (UnderWriter.getText() == null || UnderWriter.getText().equals("")) {
            openInfoMenu();
            
            return altUnderWriter.getText();
        } else {
            return UnderWriter.getText();
        }
    }


    public Underwriters getUnderwriterInfo() {
        String underWriter = "";
        int counter = 0;
        do {
            underWriter = getUnderwriter();
        } while (counter++ < 10 && (underWriter.equals("") || underWriter == null || underWriter.length() == 0));
        Underwriters underWriterInfo = null;
        try {
            underWriterInfo = UnderwritersHelper.getUnderwriterInfoByFullName(underWriter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return underWriterInfo;
    }


    public WebElement getJobCompleteTitleBar() {
        return label_JobCompleteTitleBar;
    }

    private void openInfoMenu() {
    	clickWhenClickable(button_InfobarMenu);
        if (finds(By.xpath("//div[contains(@eventid, 'JobComplete:JobWizardInfoBar:PaymentPlan')]/parent::div/parent::div/parent::div/parent::div[contains(@style, 'visibility: hidden;')]")).size() > 0) {
        	clickWhenClickable(button_InfobarMenu);
        }
    }


    public void clickAccountNumber() {
        clickWhenClickable(AccountNumber);
    }


    public void clickViewYourSubmission() {
        clickWhenClickable(link_ViewYourSubmission);
        
    }


    public void clickViewYourPolicy() {
    		clickWhenClickable(link_ViewYourPolicy);
    }


    public void clickApplyPolicyChangeToFuturePeriodsLink() {
        clickWhenClickable(link_ApplyChangeToFuturePeriods);
        
    }

    public void clickPolicyNumber() {
        clickWhenClickable(PolicyNumber);

    }


    public String getJobCompleteMessage() {
        return label_JobCompleteMessage.getText();
    }


    public String getViewYourPolicyLinkText() {
        return link_ViewYourPolicy.getText();
    }


    public String getViewCurrentJobText() {
        return link_ViewYourSubmission.getText();
    }


    public boolean isViewDesktopLinkExists() {
        return link_ViewDesktop.isDisplayed();
    }


    public String getCurrentJobLevel() {
        return link_CurrentJobLevel.getText().trim();
    }
}
