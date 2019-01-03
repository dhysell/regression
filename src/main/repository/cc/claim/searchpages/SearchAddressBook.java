package repository.cc.claim.searchpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.NewPerson;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class SearchAddressBook extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public SearchAddressBook(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "id[*='CountyContact:MenuItem_Search-textEl']")
    private WebElement pickerSearchLink;

    public void clickSearchLink() {
        waitUntilElementIsClickable(pickerSearchLink);
        pickerSearchLink.click();
    }

    @FindBy(xpath = "//input[contains(@id,':NameDenorm-inputEl')]")
    private WebElement inputName;

    public void setName(String input) {
        waitUtils.waitUntilElementIsClickable(inputName, 10);
        inputName.sendKeys(input);
    }

    @FindBy(css = "a[id*=':SearchPanel_FBMPanelSet:Search']")
    private WebElement buttonSearch;

    public void clickSearchButton() {
        clickWhenClickable(buttonSearch);
    }

    @FindBy(css = "a[id*=':ContactSearchToolbarButtonSet:ClaimContacts_CreateNewContactButton']")
    private WebElement createNewButton;

    @FindBy(css = "a[id*=':ClaimContacts_CreateNewContactButton:Parties_NewPerson-itemEl']")
    private WebElement personLink;

    public repository.cc.claim.NewPerson clickCreateNewPerson() {
        clickWhenClickable(createNewButton);
        clickWhenClickable(personLink);
        return new NewPerson(this.driver);
    }

    @FindBy(xpath = "//a[contains(text(),'Select')]")
    private WebElement buttonSelect;

    public void selectFirstResult() {
        clickWhenClickable(buttonSelect);
    }

    public void selectDefaultThirdParty() {
        setName("Thompson John");
        clickSearchButton();
        selectFirstResult();
    }
}
