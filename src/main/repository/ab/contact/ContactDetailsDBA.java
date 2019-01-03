package repository.ab.contact;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.List;

public class ContactDetailsDBA extends BasePage {
    
	public ContactDetailsDBA(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ***************************************************************************************************
    // Items Below are the Repository Items in the Contact Details DBA Page.
    // ***************************************************************************************************

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:ContactBasicsCardTab-btnEl')]")
    private WebElement link_ContactDetailsDBABasics;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:AddressesCardTab')]")
    private WebElement link_ContactDetailsDBAAddress;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsCardTab')]")
    private WebElement link_ContactDetailsDBARelatedContacts;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:AssociatedAccountsCardTab')]")
    private WebElement link_ContactDetailsDBAAccounts;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBACardTab-btnEl')]")
    private WebElement link_ContactDetailsDBADBAs;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:HistoryCardTab')]")
    private WebElement link_ContactDetailsDBAHistory;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:Edit-btnEl')]")
    private WebElement button_ContactDetailsDBAEdit;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:Update-btnEl')]")
    private WebElement button_ContactDetailsDBAUpdate;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:Cancel-btnEl')]")
    private WebElement button_ContactDetailsDBACancel;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:Add-btnEl')]")
    private WebElement button_ContactDetailsDBAAdd;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:Remove-btnEl')]")
    private WebElement button_ContactDetailsDBARemove;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:ABContactDetailScreen_RetireButton-btnEl')]")
    private WebElement button_ContactDetailsDBARetire;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV_tb:ABContactDetailScreen_UnretireButton-btnEl')]")
    private WebElement button_ContactDetailsDBAUnretire;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV-body')]/following-sibling::div/descendant::tbody/descendant::input")
    private WebElement editbox_ContactDetailsDBA;

    private WebElement editbox_ContactDetailsDBADescription(int row) {
        return find(By
                .xpath("//*[@id = 'NewContact:ABContactDetailScreen:DBAs:DoingBusinessAsLV-body']/descendant::tbody/descendant::tr["
                        + row + "]/td[2]/div"));
    }

    private WebElement checkbox_ContactDetailsDBA(int row) {
        return find(By
                .xpath("//*[@id = 'NewContact:ABContactDetailScreen:DBAs:DoingBusinessAsLV-body']/descendant::tbody/descendant::tr["
                        + row + "]/td[1]/div/img"));
    }

    private WebElement textbox_ContactDetailsDBARetiredOn(int row) {
        return find(By
                .xpath("//*[@id = 'NewContact:ABContactDetailScreen:DBAs:DoingBusinessAsLV-body']/descendant::tbody/descendant::tr["
                        + row + "]/td[1]/div/img"));
    }

    // ***************************************************************************************************
    // Items Below are the Helper Methods to the Repository Items in the Contact
    // Details DBA Page.
    // ***************************************************************************************************

    public void clickContactDetailsBasicLink() {
        boolean found = checkIfElementExists(link_ContactDetailsDBABasics, 5);
        if (found) {
            link_ContactDetailsDBABasics.click();
        }
    }

    private void clickContactDetailsDBAEditLink() {
        
        boolean found = checkIfElementExists(button_ContactDetailsDBAEdit, 5);
        if (found) {
            button_ContactDetailsDBAEdit.click();
            waitForPostBack();
        }
    }

    private void clickContactDetailsDBAUpdate() {
        waitUntilElementIsClickable(button_ContactDetailsDBAUpdate);
        button_ContactDetailsDBAUpdate.click();        
    }

    private void clickContactDetailsDBACancel() {
        waitUntilElementIsClickable(button_ContactDetailsDBACancel);
        button_ContactDetailsDBACancel.click();
    }

    private void clickContactDetailsDBAAdd() {
        waitUntilElementIsClickable(button_ContactDetailsDBAAdd);
        button_ContactDetailsDBAAdd.click();
        waitForPostBack();
    }

    private void clickContactDetailsDBARemove() {
        waitUntilElementIsClickable(button_ContactDetailsDBARemove);
        button_ContactDetailsDBARemove.click();
    }

    private void clickContactDetailsDBARetire() {
        waitUntilElementIsClickable(button_ContactDetailsDBARetire);
        button_ContactDetailsDBARetire.click();
    }

    private void clickContactDetailsDBAUnretire() {
        waitUntilElementIsClickable(button_ContactDetailsDBAUnretire);
        button_ContactDetailsDBAUnretire.click();
    }

    public void updateDBA(String oldDBA, String newDBA) {
        clickContactDetailsDBAEditLink();
        List<WebElement> changeDBA = finds(By.xpath("//div[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV-body')]/descendant::tbody/tr/td[contains(@text, '"
                + oldDBA + "']"));

        for (WebElement dba : changeDBA) {
            dba.click();
            dba.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            dba.sendKeys(newDBA);
        }
    }

    public void addDBA(String newDBA) {
        if (!checkIfElementExists("//*[contains(@id, ':Update-btnEl')]", 1000)) {
            clickContactDetailsDBAEditLink();
        }
        clickContactDetailsDBAAdd();
        
        List<WebElement> findEmptyDescription = finds(By.xpath(
                "//div[contains(@id, ':ABContactDetailScreen:DBAs:DoingBusinessAsLV-body')]/descendant::tbody/tr/td[2]/div"));
        findEmptyDescription.get(findEmptyDescription.size() - 1).click();
        editbox_ContactDetailsDBA.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsDBA.sendKeys(newDBA);
    }

}