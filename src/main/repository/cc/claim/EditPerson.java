package repository.cc.claim;

import org.fluttercode.datafactory.impl.DataFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class EditPerson extends BasePage {

    private WebDriver driver;

    public EditPerson(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,':ContactDetailToolbarButtonSet:Edit')]")
    private WebElement buttonEdit;

    private void clickEditButton() {
        clickWhenClickable(buttonEdit);
    }

    @FindBy(xpath = "//input[contains(@id,':AddressLine1-inputEl')]")
    private WebElement inputPrimaryAddressLine1;

    private void setPrimaryAddressLine1(String address) {
        inputPrimaryAddressLine1.sendKeys(address);
    }

    @FindBy(xpath = "//input[contains(@id,':GlobalAddressInputSet:City-inputEl')]")
    public WebElement inputPrimaryAddressCity;

    private void setPrimaryAddressCity(String city) {
        inputPrimaryAddressCity.sendKeys(city);
    }

    @FindBy(xpath = "//input[contains(@id,':GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement inputPrimaryAddressZip;

    private void setPrimaryAddressZip(String zip) {
        inputPrimaryAddressZip.sendKeys(zip);
    }

    @FindBy(xpath = "//a[contains(@id,':ContactDetailToolbarButtonSet:Update')]")
    private WebElement buttonOk;

    private void clickOkButton() {
        clickWhenClickable(buttonOk);
        waitUntilElementIsNotVisible(By.cssSelector("a[id$='EditableClaimContactRelationshipsLV_tb:Add']"));
    }

    @FindBy(xpath = "//input[contains(@id,':GlobalPersonNameInputSet:FirstName-inputEl')]")
    public WebElement inputFirstName;

    @FindBy(xpath = "//input[contains(@id,':GlobalPersonNameInputSet:LastName-inputEl')]")
    public WebElement inputLastName;

    private void checkName() {

        String insNameString = waitUntilElementIsClickable(find(By.xpath("//span[@id='Claim:ClaimInfoBar:Insured-btnWrap']//span[@class='infobar_elem_val']"))).getText();

        String insName = insNameString.replaceAll("[0-9]", "").trim();
        String[] nameArray = insName.split(" ");
        waitUntilElementIsClickable(inputFirstName);
        if (inputFirstName.getAttribute("value").equalsIgnoreCase("")) {
            inputFirstName.sendKeys(nameArray[0]);
        }

        if (inputLastName.getAttribute("value").equalsIgnoreCase("")) {
            inputLastName.sendKeys(nameArray[1]);
        }
    }

    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:SSN-inputEl']")
    public WebElement inputSSN;

    private void checkSsnValue() {

        if (inputSSN.getAttribute("value").equalsIgnoreCase("")) {
            String numString = StringsUtils.generateRandomNumberDigits(9);
            String ssnString = StringsUtils.formatSSN(numString);
            inputSSN.sendKeys(ssnString);
        }
    }

    @FindBy(xpath = "//input[contains(@id,':PersonNameInputSet:DateOfBirth-inputEl')]")
    public WebElement inputDateOfBirth;

    private void checkDateOfBirthValue() {

        if (inputDateOfBirth.getAttribute("value").equalsIgnoreCase("")) {
            DataFactory generateDataFactory = new DataFactory();
            String birthDate = DateUtils.dateFormatAsString("MM/dd/yyyy", generateDataFactory.getBirthDate());
            inputDateOfBirth.sendKeys(birthDate);
        }
    }

    public Guidewire8Select selectGender() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PersonNameInputSet:Gender-triggerWrap')]");
    }

    private void selectRandomGender() {
        selectGender().selectByVisibleTextRandom();
    }

    public void checkGenderValue() {
        if (selectGender().getText().equalsIgnoreCase("<none>")) {
            selectRandomGender();
        }
        sendArbitraryKeys(Keys.ESCAPE);
    }

    public void resetSSN() {
        waitUntilElementIsVisible(inputSSN).clear();
        String numString = StringsUtils.generateRandomNumberDigits(9);
        String ssnString = StringsUtils.formatSSN(numString);
        waitUntilElementIsClickable(inputSSN).sendKeys(ssnString);
    }

    private void fillOutBankName() {
        String tableXpath = "//div[contains(@id,':ContactEFTLV') and not(contains(@id,'-body'))]";
        WebElement tableEle = find(By.xpath(tableXpath));
        new TableUtils(this.driver).setValueForCellInsideTable(tableEle, 1, "Bank Name", "BankName", "Nigerian Prince Bank");

    }

    private void fixErrorsPreventingUpdate() {
        List<WebElement> errorMsgs = finds(By.className("message"));
        boolean errorsExist = errorMsgs.size() > 0;

        if (errorsExist) {
            
            for (WebElement ele : errorMsgs) {
                if (ele.getText().contains("SSN : Invalid SSN:")) {
                    resetSSN();
                } else if (ele.getText().contains("Missing required field \"Bank Name\"")) {
                    fillOutBankName();
                }
            }
            clickOkButton();
            fixErrorsPreventingUpdate();
        } else {
            
            systemOut("No Incident Errors Found");
            
        }
    }

    public void addDefaultAddress() {
        clickEditButton();
        setPrimaryAddressLine1("275 Tierra Vista");
        setPrimaryAddressCity("Pocatello");
        setPrimaryAddressZip("83201");
        clickOkButton();
    }

    @FindBy(css = "input[id*=':Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    private WebElement inputContactInfoWork;

    private void setRandomWorkPhoneNumber() {
        waitUntilElementIsClickable(inputContactInfoWork);
        inputContactInfoWork.clear();
        inputContactInfoWork.sendKeys("208555" + NumberUtils.generateRandomNumberInt(1000, 9999) + "");
        sendArbitraryKeys(Keys.ESCAPE);
    }

    private Guidewire8Select selectPrimaryPhone() {
        return new Guidewire8Select(driver, "//table[contains(@id,':primaryPhone-triggerWrap')]");
    }

    private void setPrimaryPhone(String selection) {
        selectPrimaryPhone().selectByVisibleText(selection);
    }

    public void validateContact() {
        clickEditButton();
        
        checkName();
        
        checkSsnValue();
        
        checkDateOfBirthValue();
        
        checkGenderValue();
        
        setRandomWorkPhoneNumber();
        
        setPrimaryPhone("Work");
        
        clickOkButton();
        fixErrorsPreventingUpdate();
        
    }


}
