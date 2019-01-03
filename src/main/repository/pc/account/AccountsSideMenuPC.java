package repository.pc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class AccountsSideMenuPC extends BasePage {

    public AccountsSideMenuPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_Summary')]")
    public WebElement link_Summary;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_Contacts')]")
    public WebElement link_Contacts;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_Roles')]")
    public WebElement link_Participants;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_RelatedAccounts')]")
    public WebElement link_RelatedAccounts;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_Notes')]")
    public WebElement link_Notes;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_Claims')]")
    public WebElement link_Claims;

    @FindBy(xpath = "//td[contains(@id, 'AccountFile_AccountFile_History')]")
    public WebElement link_History;


    public void clickSummary() {
        clickWhenClickable(link_Summary);
    }


    public void clickContacts() {
        clickWhenClickable(link_Contacts);
    }


    public void clickParticipants() {
        clickWhenClickable(link_Participants);
    }


    public void clickRelatedAccounts() {
        clickWhenClickable(link_RelatedAccounts);
    }


    public void clickNotes() {
        clickWhenClickable(link_Notes);
    }


    public void clickClaims() {
        clickWhenClickable(link_Claims);
    }


    public void clickHistory() {
        clickWhenClickable(link_History);
    }

}
