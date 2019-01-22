package repository.cc.claim;

import org.fluttercode.datafactory.impl.DataFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class EditPerson extends BasePage {

    @FindBy(xpath = "//input[contains(@id,':GlobalAddressInputSet:City-inputEl')]")
    public WebElement inputPrimaryAddressCity;
    @FindBy(xpath = "//input[contains(@id,':GlobalPersonNameInputSet:FirstName-inputEl')]")
    public WebElement inputFirstName;
    @FindBy(xpath = "//input[contains(@id,':GlobalPersonNameInputSet:LastName-inputEl')]")
    public WebElement inputLastName;
    @FindBy(xpath = "//input[@id='ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PersonNameInputSet:SSN-inputEl']")
    public WebElement inputSSN;
    @FindBy(xpath = "//input[contains(@id,':PersonNameInputSet:DateOfBirth-inputEl')]")
    public WebElement inputDateOfBirth;
    private WebDriver driver;
    @FindBy(xpath = "//a[contains(@id,':ContactDetailToolbarButtonSet:Edit')]")
    private WebElement buttonEdit;
    @FindBy(xpath = "//input[contains(@id,':AddressLine1-inputEl')]")
    private WebElement inputPrimaryAddressLine1;
    @FindBy(xpath = "//input[contains(@id,':GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement inputPrimaryAddressZip;
    @FindBy(xpath = "//a[contains(@id,':ContactDetailToolbarButtonSet:Update')]")
    private WebElement buttonOk;
    @FindBy(css = "input[id*=':Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    private WebElement inputContactInfoWork;
    @FindBy(id = "ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl")
    private WebElement inputContactInfoWorkAlternate;

    public EditPerson(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void clickEditButton() {
        clickWhenClickable(buttonEdit);
    }

    private void setPrimaryAddressLine1(String address) {
        inputPrimaryAddressLine1.sendKeys(address);
    }

    private void setPrimaryAddressCity(String city) {
        inputPrimaryAddressCity.sendKeys(city);
    }

    private void setPrimaryAddressZip(String zip) {
        inputPrimaryAddressZip.sendKeys(zip);
    }

    public void clickOkButton() {
        clickWhenClickable(buttonOk);
        waitUntilElementIsNotVisible(By.cssSelector("a[id$='EditableClaimContactRelationshipsLV_tb:Add']"));
    }

    public void checkName() {

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

    public void checkSsnValue() {

        if (inputSSN.getAttribute("value").equalsIgnoreCase("")) {
            String numString = StringsUtils.generateRandomNumberDigits(9);
            String ssnString = StringsUtils.formatSSN(numString);
            inputSSN.sendKeys(ssnString);
        }
    }

    public void checkDateOfBirthValue() {

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

    private void setPhoneNumbers() {
        String phoneNumber = "208555" + NumberUtils.generateRandomNumberInt(1000, 9999) + "";

        WebElement businessPhone = this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
        WebElement workPhone = this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
        WebElement homePhone = this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Home:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
        WebElement mobilePhone = this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));

        setPhoneNumber(businessPhone, phoneNumber);
        setPhoneNumber(workPhone, phoneNumber);
        setPhoneNumber(homePhone, phoneNumber);
        setPhoneNumber(mobilePhone, phoneNumber);


        if (selectPrimaryPhone().getText().contains("<none>")) {
            setPrimaryPhone("Business");
        }
    }

    private void setPhoneNumber(WebElement phoneNumberField, String phoneNumber) {
        waitUntilElementIsClickable(phoneNumberField);
        this.driver.findElement(By.xpath("//label[contains(text(),'Primary Address')]")).click();
        phoneNumberField.clear();
        waitUntilElementIsClickable(this.driver.findElement(By.id("//label[contains(text(),'Primary Address')]")));
        this.driver.findElement(By.id("//label[contains(text(),'Contact Info')]")).click();
        phoneNumberField.sendKeys(phoneNumber);
        waitUntilElementIsClickable(this.driver.findElement(By.id("//label[contains(text(),'Primary Address')]")));
        this.driver.findElement(By.id("//label[contains(text(),'Contact Info')]")).click();
    }

    private void setRandomWorkPhoneNumber() {

        String phoneNumber = "208555" + NumberUtils.generateRandomNumberInt(1000, 9999) + "";

        try {
            waitUntilElementIsClickable(inputContactInfoWork);
            inputContactInfoWork.clear();
            inputContactInfoWork.sendKeys(phoneNumber);
        } catch (Exception e) {
            waitUntilElementIsClickable(inputContactInfoWorkAlternate);
            inputContactInfoWorkAlternate.clear();
            inputContactInfoWorkAlternate.sendKeys(phoneNumber);
            try {
                WebElement workPhone = this.driver.findElement(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
                waitUntilElementIsClickable(workPhone);
                workPhone.clear();
                workPhone.sendKeys(phoneNumber);
            } catch (Exception f) {
                Assert.fail("Error setting phone number on contact.");
            }
        }
        sendArbitraryKeys(Keys.ESCAPE);
    }

    private Guidewire8Select selectPrimaryPhone() {
        return new Guidewire8Select(driver, "//table[contains(@id,':primaryPhone-triggerWrap')]");
    }

    private void setPrimaryPhone(String selection) {
        waitUntilElementIsVisible(selectPrimaryPhone().getSelectButtonElement());
        if (selectPrimaryPhone().getText().contains("<none>")) {
            selectPrimaryPhone().selectByVisibleText(selection);
        }
    }

    public void validateContact() {

        clickEditButton();
        checkName();
        checkSsnValue();
        checkDateOfBirthValue();
        checkGenderValue();
        setPhoneNumbers();
        clickOkButton();
        fixErrorsPreventingUpdate();
    }


}
