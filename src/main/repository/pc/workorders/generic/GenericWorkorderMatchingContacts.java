package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderMatchingContacts extends GenericWorkorder {

    public GenericWorkorderMatchingContacts(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]")
    public WebElement table_MatchingContacts;

    @FindBy(xpath = "//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]/descendant::span[contains(., 'Alternate')]")
    public WebElement text_MatchingContactsAlternateHeading;

    @FindBy(xpath = "//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]/descendant::span[contains(., 'Former')]")
    public WebElement text_MatchingContactsFormerHeading;

    @FindBy(xpath = "//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")
    public WebElement link_MatchingContactsReturnToCreateAcct;


    public void clickReturnToCreateAccount() {
        clickWhenClickable(link_MatchingContactsReturnToCreateAcct);
    }


    public boolean alternateColumnHeadingExists() {
        return checkIfElementExists("//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]/descendant::span[contains(., 'Alternate')]", 15);
    }


    public boolean formerColumnHeadingExists() {
        return checkIfElementExists("//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]/descendant::span[contains(., 'Former')]", 15);
    }


    public ArrayList<String> getMessages() {
        ArrayList<String> matchingPageMessages = new ArrayList<String>();
        ErrorHandling errorHandling = new ErrorHandling(getDriver());
        List<WebElement> errorMessages = errorHandling.text_ErrorHandlingErrorBannerMessages();
        for (WebElement msg : errorMessages) {
            matchingPageMessages.add(msg.getText());
        }
        return matchingPageMessages;
    }


    public int exactMatch(String lastFourSSN) {
        String gridColumn = new TableUtils(getDriver()).getGridColumnFromTable(find(By.xpath("//div[contains(@id,'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]")), "Match Type");
        List<WebElement> exactMatch = finds(By.xpath("//div[contains(., 'Exact')]/parent::td[contains(@class, '" + gridColumn + "')]/ancestor::tr[1]/td/div[contains(.,'###-##-" + lastFourSSN + "')]/ancestor::tr[1]"));
        return exactMatch.size();
    }

}
