package repository.cc.administration;


import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import java.util.List;

public class MessageQueuesCC extends BasePage {

    private WebDriver driver;

    public MessageQueuesCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//a[contains(text(),'Email')]")
    public WebElement link_Email;

    @FindBy(xpath = "//a[contains(text(),'messages')]")
    public WebElement link_NonSafeMessages;

    public Guidewire8Select select_Filter() {
        return new Guidewire8Select(driver, "//table[contains(@id,':SOOMessageFilter-triggerWrap')]");
    }

    @FindBy(xpath = "//a[text()='Outbound E-mail']")
    public List<WebElement> links_OutBoundEmail;

    @FindBy(xpath = "//a[contains(text(), 'Return to Claim')]")
    public WebElement link_ReturnToClaim;

    @FindBy(xpath = "//a[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationControlList_SuspendButton']")
    public WebElement button_Suspend;

    @FindBy(xpath = "//a[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationControlList_ResumeButton']")
    public WebElement button_Resume;

    @FindBy(xpath = "//span[@id='MessagingDestinationControlList:MessagingDestinationControlListScreen:MessagingDestinationControlList_RestartButton']")
    public WebElement button_Restart;

    @FindBy(xpath = "//span[@id='MessageControlForSOOList:MessageControlForSOOListScreen:MessageControlForSOOListScreen_SkipButton-btnEl']")
    public WebElement button_Skip;

    @FindBy(xpath = "//a[contains(text(),'OK']")
    public WebElement button_OK;

    @FindBy(xpath = "//a[@id='MessageControlForDestinationList:MessageControlForDestinationListScreen:MessageControlForDestinationListScreen_RetryButton']")
    public WebElement button_Retry;

    @FindBy(xpath = "//a[@id='MessageControlForSOOList:MessageControlForSOOList_UpLink']")
    public WebElement link_UpToDestination;

    @FindBy(xpath = "//a[@id='MessageControlForDestinationList:MessageControlForDestinationList_UpLink']")
    public WebElement link_UpToMessageQueue;

    // HELPER METHODS
    // =======================================================================

    public void clickEmail() {
        clickWhenClickable(link_Email);
    }

    public void clickUpToDestination() {
        clickWhenClickable(link_UpToDestination);
    }

    public void clickNonSafeMessages() {
    	clickWhenClickable(link_NonSafeMessages);
    }

    public void selectSpecificFilter() {
        select_Filter().selectByVisibleText("Claims/Contacts with any unfinished messages");
    }

    public void checkAllRows() {
        WebElement checkAll = find(
                By.xpath("//div[contains(@id,'rowcheckcolumn-')]/span[contains(@id,'rowcheckcolumn-')]/div"));
        clickWhenClickable(checkAll);
    }

    public void clickSkipButton() {
        clickWhenClickable(button_Skip);
    }

    public void clickOKButton() {

        WebElement okButton = find(By.xpath("//div[contains(@id,'-toolbar-targetEl')]/a[contains(@id,'button-')]"));
        clickWhenClickable(okButton);
    }

    public void clearMessages() {

        List<WebElement> messageRows = finds(By.xpath("//div[contains(@id,':MessageControlForSOOListLV-body')]/div/table/tbody/tr"));
        int rowCount = messageRows.size();

        while (rowCount > 1) {
            AdminSideMenuCC asm = new AdminSideMenuCC(this.getDriver());

            messageRows = finds(
                    By.xpath("//div[contains(@id,':MessageControlForSOOListLV-body')]/div/table/tbody/tr"));
            rowCount = messageRows.size();

            try {
                checkAllRows();
                
                clickSkipButton();
                
                clickOKButton();
                
            } catch (Exception e) {
                asm.clickMonitoringLink();
                
                asm.clickMessageQueues();
                
                clickEmail();
                
                selectSpecificFilter();
                
                clickNonSafeMessages();
                
            }

        }
    }

    public boolean checkMessagePayload(String textToCheck) {

        boolean validPayload = false;

        for (int i = 0; i < links_OutBoundEmail.size(); i++) {
            links_OutBoundEmail.get(i).click();
            
            try {
                if (find(By.xpath("//textarea[contains(@id, 'Payload-inputEl')]")).getText()
                        .contains(textToCheck)) {
                    validPayload = true;
                    

                }
            } catch (Exception e) {
                clickReturnToClaim();
            }
            clickReturnToClaim();
        }
        return validPayload;
    }

    public void clickSpecificClaimNum(String cNum) {
        WebElement claimNumberLink = find(By.xpath("//a[contains(text(), '" + cNum + "')]"));
        clickWhenClickable(claimNumberLink);
    }

    public void checkSpecificClaimNum(String cNum) {
        WebElement checkbox = find(By.xpath("//a[contains(text(), '" + cNum + "')]/../../../td[1]/div"));
        checkbox.click();
    }

    public void checkSpecificDestination(String dest) {
        WebElement checkbox = find(By.xpath("//a[contains(text(),'" + dest + "')]/../../../td[1]/div"));
        checkbox.click();
    }

    public void clickRetryButton() {
        clickWhenClickable(button_Retry);
    }

    public void clickSuspendButton() {
        clickWhenClickable(button_Suspend);
    }

    public void clickResumeButton() {
        clickWhenClickable(button_Resume);
    }

    public void clickRestart() {
        clickWhenClickable(button_Restart);
    }

    public void clickReturnToClaim() {
        clickWhenClickable(link_ReturnToClaim);
    }

    public void clickUpToMessageQueues() {
        clickWhenClickable(link_UpToMessageQueue);

    }

    public void suspendEmailMessageQue() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ClaimCenter);
    	WebDriver batchDriver = new DriverBuilder().buildGWWebDriver(cf);
        
        Login login = new Login(batchDriver);

        login.login("su", "gw");

        TopMenu topMenu = new TopMenu(batchDriver);
        repository.cc.administration.AdminMenuCC admin = new repository.cc.administration.AdminMenuCC(batchDriver);
        MessageQueuesCC queues = new MessageQueuesCC(batchDriver);

        topMenu.clickAdministrationTab();
        
        admin.clickAdminMenuMonitoryingMessageQueues();
        
        queues.checkSpecificDestination("Email");
        
        queues.clickSuspendButton();

        new GuidewireHelpers(batchDriver).logout();
        
        batchDriver.quit();
    }

    public void navigateToClaimInEventMessaging(String claimNumber) {
        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.administration.AdminMenuCC admin = new AdminMenuCC(this.driver);
        MessageQueuesCC queues = new MessageQueuesCC(this.driver);
        topMenu.clickAdministrationTab();
        
        admin.clickAdminMenuMonitoryingMessageQueues();
        
        queues.clickEmail();
        
        queues.selectSpecificFilter();
        
        this.driver.navigate().refresh();
        
        queues.clickSpecificClaimNum(claimNumber);
        
    }

    public void resumeEmailMessageQueue() {
        MessageQueuesCC queues = new MessageQueuesCC(this.driver);
        queues.clickUpToDestination();
        
        queues.clickUpToMessageQueues();
        
        queues.checkSpecificDestination("Email");
        
        queues.clickResumeButton();
    }
}
