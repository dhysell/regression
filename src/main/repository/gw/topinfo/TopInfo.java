package repository.gw.topinfo;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.BatchProcess;
import repository.gw.helpers.PopUpWindow;
import repository.gw.login.Login;

public class TopInfo extends BasePage {
    private WebDriver driver;

    public TopInfo(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[(@id='QuickJump-inputEl')]")
    public WebElement editbox_TopInfoQuickJump;

    @FindBy(xpath = "//span[contains(@id, 'TabBar:UnsavedWorkTabBarLink-btnEl')]")
    public WebElement button_Save;

    public WebElement link_UnsavedWork(String workOrder) {
        return find(By.xpath("//a[contains(@id, 'TabBar:UnsavedWorkTabBarLink:') and contains(., '" + workOrder + "')]"));
    }

    @FindBy(xpath = "//span[contains(@id, ':TabLinkMenuButton-btnEl')]")
    public WebElement button_Gear;

    @FindBy(xpath = "//a[contains(@id,'TabBar:HelpTabBarLink-itemEl')]")
    public WebElement link_TopInfoHelp;

    @FindBy(xpath = "//div[(@id='TabBar:AboutTabBarLink')]")
    public WebElement link_TopInfoAbout;

    @FindBy(xpath = "//div[(@id='TabBar:BuildInfoTabBarLink')]")
    public WebElement link_TopInfoBuildInfo;

    @FindBy(xpath = "//div[(@id='TabBar:PrefsTabBarLink')]")
    public WebElement link_TopInfoPreferences;
//                                       TabBar:LogoutTabBarLink
    @FindBy(xpath = "//div[contains(@id,'TabBar:LogoutTabBarLink')]")
    public WebElement link_TopInfoLogOut;

    @FindBy(xpath = "//div[contains(@id, '_msgs')]/div[1]")
    public WebElement label_BatchProcessInfoBar;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    //
    // public void clickTopInfoHelp(){
    // waitUntilElementIsClickable(button_Gear);
    // button_Gear.click();
    // waitUntilElementIsClickable(link_Help);
    // link_Help.click();
    // }


    public repository.gw.helpers.PopUpWindow clickTopInfoHelp() throws InterruptedException {
        waitUntilElementIsClickable(button_Gear);
        hoverOverAndClick(button_Gear);
        waitUntilElementIsClickable(link_TopInfoHelp);
        return new repository.gw.helpers.PopUpWindow(driver, link_TopInfoHelp);
    }


    public repository.gw.helpers.PopUpWindow clickTopInfoAbout() throws InterruptedException {
        waitUntilElementIsClickable(button_Gear);
        hoverOverAndClick(button_Gear);
        waitUntilElementIsClickable(link_TopInfoAbout);
        return new PopUpWindow(driver, link_TopInfoAbout);
    }


    public void clickTopInfoBuildInfo() {
        waitUntilElementIsClickable(button_Gear);
        hoverOverAndClick(button_Gear);
        waitUntilElementIsClickable(link_TopInfoBuildInfo);
        link_TopInfoBuildInfo.click();
    }


    public void clickTopInfoPreferences() {
        waitUntilElementIsClickable(button_Gear);
        hoverOverAndClick(button_Gear);
        waitUntilElementIsClickable(link_TopInfoPreferences);
        link_TopInfoPreferences.click();
    }


    public void clickTopInfoLogout() {
//        waitUntilElementIsVisible(button_Gear);
        hoverOverAndClick(button_Gear);
        if(finds(By.xpath("//div[contains(@id,'TabBar:LogoutTabBarLink')]")).isEmpty()) {
        	hoverOverAndClick(button_Gear);
        }
        clickWhenClickable(link_TopInfoLogOut);
        if (isAlertPresent()) {
            selectOKOrCancelFromPopup(OkCancel.OK);
        }
        makeSureLoggedOut();
    }


    public String getAgentName() {
        waitUntilElementIsVisible(button_Gear);
        hoverOverAndClick(button_Gear);
        waitUntilElementIsClickable(link_TopInfoLogOut);
        return link_TopInfoLogOut.getText();
    }

    private void makeSureLoggedOut() {
        boolean found = false;
        long secondsRemaining = 120;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            repository.gw.login.Login loginPage = new Login(getDriver());
            if (checkIfElementExists(loginPage.getLoginUsername(), 10)) {
                found = true;
            } else {
                secondsRemaining = secondsRemaining - delayInterval;
                hoverOverAndClick(button_Gear);
                waitUntilElementIsClickable(link_TopInfoLogOut);
                clickWhenClickable(link_TopInfoLogOut);
                if (isAlertPresent()) {
                    selectOKOrCancelFromPopup(OkCancel.OK);
                }
            }
        }
    }

    private void clickTopInfoQuickJump() {
        waitUntilElementIsVisible(editbox_TopInfoQuickJump);
        editbox_TopInfoQuickJump.click();
    }

    @Deprecated
    public void runBatchProcess(BatchProcess batchProcess) {
        clickTopInfoQuickJump();
        editbox_TopInfoQuickJump.sendKeys("RunBatchProcess ");
        editbox_TopInfoQuickJump.sendKeys(batchProcess.getQuickJumpValue());
        editbox_TopInfoQuickJump.sendKeys(Keys.ENTER);
        waitForPostBack();
        waitUntilElementIsVisible(label_BatchProcessInfoBar, 100000);
    }


    public Agents getCurrentLoggedIn() throws Exception {
        waitUntilElementIsVisible(button_Gear);
        hoverOverAndClick(button_Gear);
        String logoutText = link_TopInfoLogOut.findElement(By.xpath(".//a/span")).getText();
        logoutText = logoutText.replace("Log Out ", "");
        String[] split = logoutText.split(" ");
        return AgentsHelper.getAgentByName(split[0], split[split.length - 1]);
    }
    
    public String getCurrentlyLoggedIn() {
    	waitUntilElementIsVisible(button_Gear);
        hoverOverAndClick(button_Gear);
        String logoutText = link_TopInfoLogOut.findElement(By.xpath(".//a/span")).getText();
        return logoutText.replace("Log Out ", "");
    }


    public void saveWork(String wo) {
        clickWhenClickable(button_Save);
        clickWhenClickable(link_UnsavedWork(wo));
    }


    @FindBy(xpath = "//div[contains(@id, 'BuildInfoPopup:BuildId-inputEl')]")
    public WebElement text_BuildID;


    public String getBuildID() {
        return text_BuildID.getText();
    }

    @FindBy(xpath = "//div[contains(@id, 'BuildInfoPopup:Branch-inputEl')]")
    public WebElement text_SVNBranch;


    public String getSVNBranch() {
        return text_SVNBranch.getText();
    }

    @FindBy(xpath = "//div[contains(@id, 'BuildInfoPopup:Revision-inputEl')]")
    public WebElement text_SVNRevision;


    public String getSVNRevision() {
        return text_SVNRevision.getText();
    }


}
