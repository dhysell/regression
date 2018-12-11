package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.incidents.NewUnverifiedVehicle;
import repository.cc.claim.searchpages.SearchAddressBook;
import repository.cc.entities.InvolvedParty;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FindPolicyUnverified extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public FindPolicyUnverified(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[id*='FNOLWizardFindPolicyPanelSet:PolicyNumber-inputEl']")
    private WebElement inputPolicyNumber;

    public void setPolicyNumber(String policyNumber) {
        waitUtils.waitUntilElementIsVisible(inputPolicyNumber);
        inputPolicyNumber.sendKeys(policyNumber);
    }

    @FindBy(css = "span[id*=':NewClaimVehiclesLV_tb:Add-btnInnerEl']")
    private WebElement addVehiclesButton;

    private NewUnverifiedVehicle clickAddVehicle() {
        clickWhenClickable(addVehiclesButton);
        return new NewUnverifiedVehicle(this.driver);
    }

    public void addNewVehicle() {
        NewUnverifiedVehicle vehicle = clickAddVehicle();
        vehicle.addNewVehicleRandom();
    }

    private Guidewire8Select typeSelect() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Type-triggerWrap')]");
    }

    public String setType(String policyType) {
        typeSelect().selectByVisibleText(policyType);
        return policyType;
    }

    private Guidewire8Select insuredNameSelect() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Insured_Name-triggerWrap')]");
    }

    public String setInsuredName(String insuredName) {
        waitForPageLoad();
        insuredNameSelect().selectByVisibleTextPartial(insuredName);
        return insuredName;
    }

    public void setTypeOfClaim(String fnolType) {

        WebElement selectionRadio;

        if (!fnolType.contains("Glass")) {
            selectionRadio = find(By.xpath("//label[contains(text(),'" + fnolType + "') and not(contains(text(),'Glass'))]/parent::div/input"));
        } else {
            selectionRadio = find(By.xpath("//label[contains(text(),'" + fnolType + "')]/parent::div/input"));
        }

        waitUtils.waitUntilElementIsClickable(policyNumberInput, 15);

        selectionRadio.click();
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        return date.format(formatter);
    }

    @FindBy(xpath = "//span[contains(text(),'Step 1 of 4: Search or Create Policy')]")
    public WebElement pageHeader;

    @FindBy(css = "input[id*=':Claim_LossDate-inputEl']")
    private WebElement dateOfLossInput;

    public void setDateOfLoss(LocalDate dateOfLoss) {
        waitUtils.waitUntilElementIsClickable(dateOfLossInput);
        clickWhenClickable(dateOfLossInput);
        dateOfLossInput.sendKeys(formatDate(dateOfLoss));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }

    @FindBy(css = "input[id*=':EffectiveDate-inputEl']")
    private WebElement effectiveDateInput;

    public void setEffectiveDate(LocalDate effectiveDate) {
        waitUtils.waitUntilElementIsClickable(effectiveDateInput);
        clickWhenClickable(effectiveDateInput);
        effectiveDateInput.sendKeys(formatDate(effectiveDate));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }

    @FindBy(css = "input[id*=':ExpirationDate-inputEl']")
    private WebElement expirationDateInput;

    public void setExpirationDate(LocalDate expirationDate) {
        waitUtils.waitUntilElementIsClickable(expirationDateInput);
        clickWhenClickable(expirationDateInput);
        expirationDateInput.sendKeys(formatDate(expirationDate));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }

    @FindBy(css = "a[id*=':Insured_Name:Insured_NameMenuIcon']")
    private WebElement insuredNamePicker;

    private void clickInsuredNamePicker() {
        clickWhenClickable(insuredNamePicker);
    }

    @FindBy(css = "span[id*=':Insured_Name:MenuItem_Search-textEl']")
    private WebElement searchInsuredNameLink;

    private SearchAddressBook clickSearchInsuredNameLink() {
        clickWhenClickable(searchInsuredNameLink);
        return new SearchAddressBook(this.driver);
    }

    public InvolvedParty searchInsuredNameCreateContact(String role) {
        String[] userData = {"User", "Test" + NumberUtils.generateRandomNumberDigits(8)};
        String userFullName = userData[1] + " " + userData[0];

        clickInsuredNamePicker();
        SearchAddressBook abSearch = clickSearchInsuredNameLink();
        abSearch.setName(userFullName);
        abSearch.clickSearchButton();

        NewPerson newPerson = abSearch.clickCreateNewPerson();
        InvolvedParty newContact = newPerson.buildNewPersonByName(userData, role);

        clickReturn();

        return newContact;
    }

    @FindBy(css = "a[id*='AddressBookPickerPopup:__crumb__']")
    private WebElement returnLink;

    private void clickReturn() {
        clickWhenClickable(returnLink);
    }

    @FindBy(css = "input[id*=':PolicyNumber-inputEl']")
    private WebElement policyNumberInput;

    public void waitForPageToLoad() {
        waitUtils.waitUntilElementIsClickable(policyNumberInput);
    }

}
