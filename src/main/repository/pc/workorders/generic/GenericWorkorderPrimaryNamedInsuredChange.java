package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.PersonPrefix;
import repository.gw.enums.Suffix;

public class GenericWorkorderPrimaryNamedInsuredChange extends GenericWorkorder {
	
	private WebDriver driver;

    public GenericWorkorderPrimaryNamedInsuredChange(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id, 'UWNameChangeActivityPopup:Update-btnEl')]")
    private WebElement button_PrimaryNamedInsuredChangeUpdate;

    @FindBy(xpath = "//*[contains(@id, 'UWNameChangeActivityPopup:Cancel-btnEl')]")
    private WebElement button_PrimaryNamedInsuredChangeCancel;

    public Guidewire8Select select_PrimaryNamedInsuredChangeCompanyOrPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:ContactTypeDenorm_FBM-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:CompanyNameDenorm_FBM-inputEl')]")
    private WebElement input_PrimaryNamedInsuredChangeName;

    public Guidewire8Select select_PrimaryNamedInsuredChangePersonPrefix() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:PrefixDenorm_FBM-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:FirstNameDenorm_FBM-inputEl')]")
    private WebElement input_PrimaryNamedInsuredChangeFirstName;

    @FindBy(xpath = "//input[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:MiddleNameDenorm_FBM-inputEl')]")
    private WebElement input_PrimaryNamedInsuredChangeMiddleName;

    @FindBy(xpath = "//input[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:LastNameDenorm_FBM-inputEl')]")
    private WebElement input_PrimaryNamedInsuredChangeLastName;

    public Guidewire8Select select_PrimaryNamedInsuredChangePersonSuffix() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:SuffixDenorm_FBM-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id, 'UWNameChangeActivityPopup:NewActivityDV:NameChangeReason_FBM-inputEl')]")
    private WebElement textArea_PrimaryNamedInsuredChangeReason;

    @FindBy(xpath = "//input[contains(@id,'GlobalPersonNameInputSet:MiddleName')]")
    public WebElement editbox_PersonMiddleName;


    public void setCompanyOrPerson(GenerateContactType compOrPerson) {
        
        select_PrimaryNamedInsuredChangeCompanyOrPerson().selectByVisibleText(compOrPerson.toString());

    }


    public void setPersonPrefix(PersonPrefix prefix) {
        select_PrimaryNamedInsuredChangePersonPrefix().selectByVisibleTextPartial(prefix.getValue());
        
    }


    public void setFirstNameEditbox(String firstName) {
        waitUntilElementIsClickable(input_PrimaryNamedInsuredChangeFirstName);
        input_PrimaryNamedInsuredChangeFirstName.click();
        input_PrimaryNamedInsuredChangeFirstName.sendKeys(firstName);
        input_PrimaryNamedInsuredChangeFirstName.sendKeys(Keys.TAB);
        waitForPostBack();
        
    }


    public void setPersonSuffix(Suffix suffix) {
        select_PrimaryNamedInsuredChangePersonSuffix().selectByVisibleTextPartial(suffix.getValue());
        
    }


    public void setReasonEditbox(String reason) {
        waitUntilElementIsClickable(textArea_PrimaryNamedInsuredChangeReason);
        textArea_PrimaryNamedInsuredChangeReason.click();
        textArea_PrimaryNamedInsuredChangeReason.sendKeys(reason);
        
    }


    public void setLastNameEditbox(String lastName) {
        waitUntilElementIsClickable(input_PrimaryNamedInsuredChangeLastName);
        input_PrimaryNamedInsuredChangeLastName.click();
        input_PrimaryNamedInsuredChangeLastName.sendKeys(lastName);
        

    }


    public void setMiddleNameEditbox(String middleName) {
        waitUntilElementIsClickable(editbox_PersonMiddleName);
        editbox_PersonMiddleName.click();
        editbox_PersonMiddleName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_PersonMiddleName.sendKeys(middleName);
    }


    public void clickPrimaryNamedInsuredChangeReturnToSummary() {
        // TODO Auto-generated method stub

    }


    public void clickPrimaryNamedInsuredChangeUpdate() {
        waitUntilElementIsClickable(button_PrimaryNamedInsuredChangeUpdate);
        button_PrimaryNamedInsuredChangeUpdate.click();
        
    }


    public void clickPrimaryNamedInsuredChangeCancel() {
        waitUntilElementIsClickable(button_PrimaryNamedInsuredChangeCancel);
        button_PrimaryNamedInsuredChangeCancel.click();
        


    }


    public void setCompanyName(String compName) {
        
        waitUntilElementIsClickable(input_PrimaryNamedInsuredChangeName);
        input_PrimaryNamedInsuredChangeName.click();
        input_PrimaryNamedInsuredChangeName.sendKeys(compName);
        
    }


    public void setPersonName(GenerateContactType compOrPerson, PersonPrefix prefix, String firstName, String middleName, String lastName, Suffix suffix) {
        setCompanyOrPerson(compOrPerson);
        setPersonPrefix(prefix);
        setFirstNameEditbox(firstName);
        setMiddleNameEditbox(middleName);
        setPersonSuffix(suffix);

    }

}
