package repository.pc.policy;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.helpers.GuidewireHelpers;

public class PolicyMenu extends BasePage {

    public PolicyMenu(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, 'Actions')]")
    public WebElement link_MenuActions;

    @FindBy(xpath = "//*[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_CancelPolicy']")
    private WebElement PolicyFileMenuActions_CancelPolicy;

    @FindBy(xpath = "//*[contains(@id,'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:pendingCancel')]")
    private WebElement PolicyFileMenuActions_PendingCancel;

    @FindBy(xpath = "//a[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:StartRewriteMenuItemSet:RewriteFullTerm-itemEl']")
    private WebElement PolicyFileMenuActions_RewriteFullTerm;

    @FindBy(xpath = "//*[contains(@id,'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:StartRewriteMenuItemSet:RewriteNewTerm')]")
    private WebElement PolicyFileMenuActions_RewriteNewTerm;

    @FindBy(xpath = "//*[contains(@id,'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:StartRewriteMenuItemSet:RewriteRemainderOfTerm')]")
    private WebElement PolicyFileMenuActions_RewriteRemainderOfTerm;

    @FindBy(xpath = "//*[contains(@id, ':PolicyFileMenuActions_ChangePolicy')]")
    private WebElement PolicyFileMenuActions_ChangePolicy;

    @FindBy(xpath = "//*[contains(@id, ':PolicyFileMenuActions_RenewPolicy-itemEl')]")
    private WebElement PolicyFileMenuActions_RenewPolicy;

    @FindBy(xpath = "//*[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_ExpirationDateChange-textEl']")
    private WebElement PolicyFileMenuActions_ExpirationDateChange;

    @FindBy(xpath = "//*[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_NameChange']")
    private WebElement PolicyFileMenuActions_NameChange;

    @FindBy(xpath = "//*[contains(@id,'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_CopySubmission')]")
    private WebElement PolicyFileMenuActions_CopySubmission;

    @FindBy(xpath = "//*[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_IssueSubmission']")
    private WebElement policyFileMenuActions_IssuePolicy;

    @FindBy(xpath = "//*[contains(@id,'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_RescindCancellation')]")
    private WebElement PolicyFileMenuActions_RescindCancellation;

    @FindBy(xpath = "//*[@id='PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_NewWorkOrder:PolicyFileMenuActions_ReinstatePolicy']")
    private WebElement PolicyFileMenuActions_ReinstatePolicy;


    public void clickMenuActions() {
        clickWhenClickable(link_MenuActions);
    }


    public void clickPendingCancel() {
        clickWhenClickable(PolicyFileMenuActions_PendingCancel);
    }


    public void clickCopySubmission() {
        clickWhenVisible(PolicyFileMenuActions_CopySubmission);
    }

    public void clickRewriteNewTerm() throws GuidewireNavigationException {
        clickWhenVisible(PolicyFileMenuActions_RewriteNewTerm);
        new GuidewireHelpers(getDriver()).isOnPage("//span[(@class='g-title') and (text()='Start Rewrite New Term')]", 5000, "UNABLE TO GET TO REWRITE NEW TERM AFTER SELCTING REWRITE NEW TERM FROM ACTIONS MENU");
    }

    public void clickRewriteRemainderOfTerm() {
        clickWhenVisible(PolicyFileMenuActions_RewriteRemainderOfTerm);
    }


    public void clickRewriteFullTerm() {
        clickWhenVisible(PolicyFileMenuActions_RewriteFullTerm);
    }


    public boolean checkRewriteFullTermExist() {

        return checkIfElementExists(PolicyFileMenuActions_RewriteFullTerm, 1000);
    }


    public boolean checkRewriteRemainderOfTermExist() {

        return checkIfElementExists(PolicyFileMenuActions_RewriteRemainderOfTerm, 1000);
    }


    public boolean checkRewriteNewTermExist() {

        return checkIfElementExists(PolicyFileMenuActions_RewriteNewTerm, 1000);
    }


    public void clickCancelPolicy() {
        clickWhenVisible(PolicyFileMenuActions_CancelPolicy);
    }


    public void clickReinstatePolicy() {
        clickWhenVisible(PolicyFileMenuActions_ReinstatePolicy);
    }


    public void clickChangePolicy() {
        clickWhenVisible(PolicyFileMenuActions_ChangePolicy);
    }


    public void clickExpirationDateChange() {
        clickWhenVisible(PolicyFileMenuActions_ExpirationDateChange);
    }


    public void clickNameChange() {
        clickWhenVisible(PolicyFileMenuActions_NameChange);
    }


    public void clickIssuePolicy() {
        clickWhenVisible(policyFileMenuActions_IssuePolicy);
    }


    public void clickRescindCancellation() {
        clickWhenVisible(PolicyFileMenuActions_RescindCancellation);
        WebElement arrowElement = find(By.xpath("//div[contains(@id, ':PolicyFileMenuActions_RescindCancellation-arrowEl')]"));
        hoverOverAndClick(arrowElement);
        WebElement firstElement = find(By.xpath("//a[contains(@id, ':PolicyFileMenuActions_RescindCancellation:0:item-itemEl')]"));
        clickWhenVisible(firstElement);
    }


    public void clickRenewPolicy() {
        clickMenuActions();
        clickWhenClickable(PolicyFileMenuActions_RenewPolicy);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public boolean checkRenewPolicyOption() {
        clickMenuActions();
        return checkIfElementExists(PolicyFileMenuActions_RenewPolicy, 2000);
    }

}
