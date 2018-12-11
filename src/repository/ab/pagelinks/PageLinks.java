package repository.ab.pagelinks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class PageLinks extends BasePage {

    public PageLinks(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ttlBar')]")
    public WebElement text_ViewContactPageTitle;

    @FindBy(xpath = "//span[contains(@id,':ContactBasicsCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsBasics;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:AddressesCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsAddress;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:RelatedContactsCardTab')]")
    public WebElement link_ContactDetailsBasicsRelatedContacts;

    @FindBy(xpath = "//span[contains(@id,'ContactDetail:ABContactDetailScreen:AssociatedAccountsCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsAccounts;

    @FindBy(xpath = "//*[contains(@id,':PaidDuesCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsPaidDues;
	
    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:DBACardTab')]")
    public WebElement link_ContactDetailsBasicsDBAs;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:RoutingCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsRoutingNumbers;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:HistoryCardTab-btnEl')]")
    public WebElement link_ContactDetailsBasicsHistory;


    public void clickContactDetailsBasicsLink() {
        
        waitUntilElementIsClickable(link_ContactDetailsBasicsBasics);
        link_ContactDetailsBasicsBasics.click();
        
    }

    public void clickContactDetailsBasicsAddressLink() {
        
        waitUntilElementIsClickable(link_ContactDetailsBasicsAddress);
        link_ContactDetailsBasicsAddress.click();
        
    }

    public void clickContactDetailsBasicsRelatedContactsLink() {
        
        waitUntilElementIsClickable(link_ContactDetailsBasicsRelatedContacts, 200000);
        link_ContactDetailsBasicsRelatedContacts.click();
    }

    public void clickContactDetailsBasicsAccountsLink() {
        clickWhenClickable(link_ContactDetailsBasicsAccounts);
        
    }

    public boolean clickContactDetailsBasicsPaidDuesLink() {
        
        if (checkIfElementExists(link_ContactDetailsBasicsPaidDues, 2000)) {
            waitUntilElementIsClickable(link_ContactDetailsBasicsPaidDues);
            link_ContactDetailsBasicsPaidDues.click();
            return true;
        } else {
            return false;
        }
    }

    public void clickContactDetailsBasicsDBAsLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsDBAs);
        link_ContactDetailsBasicsDBAs.click();
    }

    public void clickContactDetailsBasicsRoutingLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsRoutingNumbers);
        link_ContactDetailsBasicsRoutingNumbers.click();
    }

    public void clickContactDetailsBasicsHistoryLink() {
        
        clickWhenClickable(link_ContactDetailsBasicsHistory);
        
    }


}
