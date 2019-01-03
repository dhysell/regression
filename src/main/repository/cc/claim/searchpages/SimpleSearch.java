package repository.cc.claim.searchpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

public class SimpleSearch extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public SimpleSearch(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//input[contains(@id,':ClaimNumber-inputEl')]")
    public WebElement textBox_ClaimNumber;

    @FindBy(xpath = "//input[contains(@id,':PolicyNumber-inputEl')]")
    public WebElement textBox_PolicyNumber;

    public Guidewire8Select select_PartyInvolvedType() {
        return new Guidewire8Select(driver, "//table[@id='xpath']");
    }

    @FindBy(xpath = "//input[contains(@id,':Name-inputEl')]")
    public WebElement textBox_CompanyName;

    @FindBy(xpath = "//input[contains(@id,'LastName-inputEl')]")
    public WebElement textBox_LastName;

    @FindBy(xpath = "//input[contains(@id,'FirstName-inputEl')]")
    public WebElement textBox_FirstName;

    @FindBy(xpath = "//input[contains(@id,'FormerName-inputEl')]")
    public WebElement textBox_FormerName;

    @FindBy(xpath = "//input[contains(@id,'AlternateName-inputEl')]")
    public WebElement textBox_AlternateName;

    public Guidewire8Select select_SearchBySSNorTIN() {
        return new Guidewire8Select(driver, "//table[contains(@id,':TaxReportingOption-triggerWrap')]");
    }


    // Helpers

    public void inputClaimNumber(String cNum) {
        textBox_ClaimNumber.sendKeys(cNum);
    }


    private void inputPolicyNumber(String pNum) {
        textBox_PolicyNumber.sendKeys(pNum);
    }


    private void selectRandom_PartyInvolvedType() {
        Guidewire8Select mySelect = select_PartyInvolvedType();
        mySelect.selectByVisibleTextRandom();
    }


    private void selectSpecific_PartyInvolvedType(String type) {
        Guidewire8Select mySelect = select_PartyInvolvedType();
        mySelect.selectByVisibleTextPartial(type);
    }


    private void inputCompanyName(String company) {
        textBox_CompanyName.sendKeys(company);
    }


    private void inputLastName(String lName) {
        textBox_LastName.sendKeys(lName);
    }


    private void inputFirstName(String fName) {
        textBox_FirstName.sendKeys(fName);
    }

    private void inputFormerName(String formerName) {
        textBox_FormerName.sendKeys(formerName);
    }


    private void inputAlternateName(String altName) {
        textBox_AlternateName.sendKeys(altName);
    }


    private void selectRandom_SSNorTIN() {
        Guidewire8Select mySelect = select_SearchBySSNorTIN();
        mySelect.selectByVisibleTextRandom();
    }


    private void selectSpecific_SSNorTIN(String SSNorTIN) {
        Guidewire8Select mySelect = select_SearchBySSNorTIN();
        mySelect.selectByVisibleTextPartial(SSNorTIN);
    }

    @FindBy(css = "a[id='SimpleClaimSearch:SimpleClaimSearchScreen:SimpleClaimSearchDV:ClaimSearchAndResetInputSet:Search']")
    private WebElement searchButton;

    private void clickSearchButton() {
        try {
            clickSearch();
        } catch (Exception e) {
            this.waitUtils.waitUntilElementIsClickable(searchButton);
            searchButton.click();
        }
    }

    private void clickResetButton() {
        clickReset();
    }


    public void searchByFirstandLastName(String fName, String lName) {
        inputFirstName(fName);
        inputLastName(lName);
        clickSearchButton();
    }

    public void searchByCompanyName(String companyName) {
        inputCompanyName(companyName);
        clickSearchButton();
    }

    public void searchByClaimNumber(String claimNumber) {
        inputClaimNumber(claimNumber);
        clickSearchButton();
    }

    @FindBy(css = "a[id*=':SimpleClaimSearchResultsLV:0:ClaimNumber']")
    private WebElement firstResult;

    public void searchByClaimNumberAndSelect(String claimNumber) {

        try {
            clickResetButton();
        } catch (Exception e) {
            System.out.println("Nothing to Reset.");
        }

        inputClaimNumber(claimNumber);
        clickSearchButton();

        clickWhenClickable(firstResult);
    }


    public void searchByPolicyNumber(String policyNumber) {
        inputPolicyNumber(policyNumber);
        clickSearchButton();
    }

    public boolean checkThatResultsWereReturned() {
        return true;
    }

    public String SearchResultTable() {
        String resultTableXpath = "//div[contains(@contains,'SimpleClaimSearchResultsLV')]";
        return resultTableXpath;
    }


}
