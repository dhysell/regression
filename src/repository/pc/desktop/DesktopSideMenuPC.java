package repository.pc.desktop;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class DesktopSideMenuPC extends BasePage {

    public DesktopSideMenuPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopActivities')]")
    public WebElement link_DesktopMyActivities;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopSubmissions')]")
    public WebElement link_DesktopMySubmissions;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopRenewals')]")
    public WebElement link_DesktopMyrenewals;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopOtherWorkOrders')]")
    public WebElement link_DesktopMyOtherWorkOrders;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopAssignableQueues')]")
    public WebElement link_DesktopMyQueues;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopUnattachedDocumentWorkLists')]")
    public WebElement link_DesktopUnattachedDocuments;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopProofOfMail')]")
    public WebElement link_DesktopProofOfMail;

    @FindBy(xpath = "//td[contains(@id,'Desktop:MenuLinks:Desktop_DesktopBulkAgentUpdate')]")
    public WebElement link_DesktopBulkAgentChange;

    @FindBy(xpath = "//td[contains(@id,'DesktopProofOfMail:DesktopProofOfMailScreen:PLDesktopProofOfMailLV:DesktopProofOfMailLV')]")
    public WebElement div_tableWrapper;


    public void clickMyActivities() {
        
        clickWhenClickable(link_DesktopMyActivities);
        
    }


    public void clickMySubmissions() {
    	clickWhenClickable(link_DesktopMySubmissions);
    	waitForPostBack();
    }


    public void clickMyRenewals() {
        waitUntilElementIsClickable(link_DesktopMyrenewals);
        clickWhenClickable(link_DesktopMyrenewals);
        
    }


    public void clickMyOtherWorkOrders() {
        waitUntilElementIsClickable(link_DesktopMyOtherWorkOrders);
        clickWhenClickable(link_DesktopMyOtherWorkOrders);
        
    }


    public void clickMyQueues() {
        waitUntilElementIsClickable(link_DesktopMyQueues);
        clickWhenClickable(link_DesktopMyQueues);
        
    }


    public void clickUnattachedDocuments() {
        waitUntilElementIsClickable(link_DesktopUnattachedDocuments);
        clickWhenClickable(link_DesktopUnattachedDocuments);
        
    }


    public void clickProofOfMail() {
        waitUntilElementIsClickable(link_DesktopProofOfMail);
        clickWhenClickable(link_DesktopProofOfMail);
        waitUntilElementIsVisible(By.xpath("//span[contains(text(), 'Personal Lines Proof Of Mail')]"));
    }


    public void clickBulkAgentChange() {
        waitUntilElementIsClickable(link_DesktopBulkAgentChange);
        clickWhenClickable(link_DesktopBulkAgentChange);
        
    }
}
