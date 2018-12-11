package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class PartiesInvolvedContacts extends BasePage {

    private WebDriver driver;

    public PartiesInvolvedContacts(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV_tb:ClaimContacts_AddExistingButton']")
    public WebElement button_AddContact;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV_tb:ClaimContacts_DeleteButton']")
    public WebElement button_DeleteContact;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:BasicsCardTab']")
    public WebElement tab_Basics;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:AddressesCardTab']")
    public WebElement tab_Addresses;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:DBACardTab']")
    public WebElement tab_DBAS;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:RelatedContactsCardTab']")
    public WebElement tab_RelatedContacts;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Edit']")
    public WebElement button_EditContact;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:LinkContactToolbarButtonSet:LinkContactToolbarButtons_LinkButton']")
    public WebElement button_LinkContact;
    @FindBy(xpath = "//a[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactBasicsHeaderInputSet_TransferRolesButton']")
    public WebElement button_TransferRolesFromOtherContacts;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonNameInputSet:GlobalPersonNameInputSet:FirstName-inputEl']")
    public WebElement input_FirstName;

    // Edit Contact Elements
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonNameInputSet:GlobalPersonNameInputSet:LastName-inputEl']")
    public WebElement input_LastName;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:AddressLine1-inputEl']")
    public WebElement input_AddressLineOne;
    @FindBy(xpath = "//div[contains(@id,':AddressLine2-inputEl')]")
    public WebElement input_AddressLineTwo;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:City-inputEl']")
    public WebElement input_City;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:State-inputEl']")
    public WebElement input_State;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:PostalCode-inputEl']")
    public WebElement input_ZipCode;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Home:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    public WebElement input_HomePhone;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    public WebElement input_WorkPhone;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    public WebElement input_CellPhone;
    @FindBy(xpath = "//input[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:Primary-inputEl']")
    public WebElement input_Email;
    @FindBy(xpath = "//input[@id='AddressBookPickerPopup:AddressBookPickerSearchScreen:SearchPanel_FBMPanelSet:NameDenorm-inputEl']")
    public WebElement input_Name;
    @FindBy(xpath = "//a[contains(@id,':SearchPanel_FBMPanelSet:Search')]")
    public WebElement button_Search;
    @FindBy(xpath = "//a[contains(@id,':ClaimContacts_CreateNewContactButton')]")
    public WebElement button_CreateNew;
    @FindBy(xpath = "//span[contains(text(),'Person')]")
    public WebElement link_Person;
    @FindBy(xpath = "//div[contains(@id,':PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV-body')]//td/div[contains(text(),'Claimant')]")
    public WebElement element_ClaimantInTable;
    @FindBy(xpath = "//a[contains(@text,'Update')]")
    public WebElement buton_Update;

    ///////////////////////////////////////////////// Elements
    ///////////////////////////////////////////////// /////////////////////////////////////////////////////////////////////////////
    public Guidewire8Select select_ContactTypeFilter() {
        return new Guidewire8Select(
                driver, "//table[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV:PeopleInvolvedDetailedFilter-triggerWrap']");
    }

    //////////////////////////////////////////////////// Helpers
    //////////////////////////////////////////////////// //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void clickClaimantRow() {
        clickWhenClickable(element_ClaimantInTable);
    }

    public void clickEditButton() {
        clickWhenClickable(button_EditContact);
    }

    public void setBasicsAddress1() {
        input_AddressLineOne.sendKeys("151 Starbomb Dr");
    }

    public void setBasicsCity() {
        input_City.sendKeys("Pocatello");
    }

    public void setBasicsState() {
        input_State.sendKeys("Idaho");
    }

    public void setBasicsZip() {
        input_City.sendKeys("83204");
    }

    public void clickUpdateButton() {
        clickWhenClickable(buton_Update);
    }

    public void setAddressInfo() {
        clickClaimantRow();
        clickEditButton();

        setBasicsAddress1();
        setBasicsCity();
        setBasicsState();
        setBasicsZip();

        clickUpdateButton();
    }

    public void clickPersonLink() {
        clickWhenClickable(link_Person);
    }

    public void clickCreateNewButton() {
        clickWhenClickable(button_CreateNew);
    }

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void setName(String nameStringLastFirst) {
        input_Name.sendKeys(nameStringLastFirst);
    }

    public void selectSpecific_ContactType(String type) {
        Guidewire8Select mySelect = select_ContactTypeFilter();
        mySelect.selectByVisibleTextPartial(type);
    }

    public void clickAddContact() {
        clickWhenClickable(button_AddContact);
    }

    public void clickDeleteContact() {
        clickWhenClickable(button_DeleteContact);
    }

    public void clickBasicsTab() {
        clickWhenClickable(tab_Basics);
    }

    public void clickAddressesTab() {
        clickWhenClickable(tab_Addresses);
    }

    public void clickDBAsTab() {
        clickWhenClickable(tab_DBAS);
    }

    public void clickRelatedContactsTab() {
        clickWhenClickable(tab_RelatedContacts);
    }

    public void clickEditContact() {
        clickWhenClickable(button_EditContact);
    }

    public void clickLinkContact() {
        clickWhenClickable(button_LinkContact);
    }

    public void clickTransferRolesFromOtherContacts() {
        clickWhenClickable(button_TransferRolesFromOtherContacts);
    }

    public String getFirstNameValue() {
        return input_FirstName.getAttribute("value");

    }

    public String getLastNameValue() {
        return input_LastName.getAttribute("value");

    }

    public String getAddressLineOneValue() {
        return input_AddressLineOne.getAttribute("value");
    }

    public String getAddressLineTwoValue() {
        return input_AddressLineTwo.getText();
    }

    public String getCityValue() {
        return input_City.getAttribute("value");

    }

    public String getStateValue() {
        return input_State.getAttribute("value");

    }

    public String getZipCodeValue() {
        return input_ZipCode.getAttribute("value");

    }

    public String getHomePhoneValue() {
        return input_HomePhone.getAttribute("value");

    }

    public String getWorkPhoneValue() {
        return input_WorkPhone.getAttribute("value");

    }

    public String getCellPhoneValue() {
        return input_CellPhone.getAttribute("value");

    }

    public String getEmailValue() {
        return input_Email.getAttribute("value");

    }

    public void clickReporterName() {
        WebElement reporterName = find(By.xpath(
                "//div[@id='ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV-body']//div[contains(text(), 'Reporter')]"));

        clickWhenClickable(reporterName);
    }

    // Format String as "LastName, FirstName"
    public void createThirdParty(String lastFirstName) {
        clickAddContact();

        setName(lastFirstName);
        clickSearchButton();

        clickCreateNewButton();
        clickPersonLink();

    }
}
