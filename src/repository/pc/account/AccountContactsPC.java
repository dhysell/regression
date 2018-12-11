package repository.pc.account;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Role;

public class AccountContactsPC extends BasePage {

    private WebDriver driver;
    public AccountContactsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ROLES FILTER DROPDOWN
    // "Display All Roles", "Account Holder", "Additional Interest",
    // "Billing Contact", "Named Insured"
    public Guidewire8Select select_ContactRole() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV:roleFilters-triggerWrap']");
    }

    // PERSON COMPANY DROPDOWN
    // "Persons and Companies", "Persons", "Companies"
    public Guidewire8Select select_PersonCompany() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV:personCompanyFilters-triggerWrap']");
    }

    // BUTTON CREATE NEW
    @FindBy(xpath = "//span[contains(@id, 'addContactButton-btnEl')]")
    public WebElement button_CreateNew;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:0:roleType')]")
    public WebElement link_AccountingContact;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:1:roleType')]")
    public WebElement link_InspectionContact;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:2:roleType')]")
    public WebElement link_Others;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:3:roleType')]")
    public WebElement link_OwnerOfficer;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:4:roleType')]")
    public WebElement link_PowerOfAttorney;

    // FIX WITH A MORE GENARIC XPATH
    // *[@id="AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:4:roleType:Search"]
    // *[@id="AccountFile_Contacts:AccountFile_ContactsScreen:AccountContactsLV_tb:addContactButton:0:roleType:Search"]

    @FindBy(xpath = "//div[contains(@id, 'roleType:Search')]/parent::div/parent::div/parent::div/parent::div[not(contains(@style, 'visibility: hidden'))]")
    public WebElement fromAddressBook;

    // REMOVE CONTACT
    @FindBy(xpath = "//a[contains(@id, 'removeContact')]")
    public WebElement button_RemoveContact;

    // CONTACT TABLE CHECKBOX

    // CONTACT TABLE NAME

    // CONTACT DETAIL TAB
    @FindBy(xpath = "//span[contains(@id, 'AccountContactDetailCardTab')]")
    public WebElement contactDetailTab;

    @FindBy(xpath = "//div[contains(@id, 'FBAccountInfoInputSet:AssignedAgent-inputEl')]")
    public WebElement textbox_AssignedAgent;

    // PC ROLES TAB
    @FindBy(xpath = "//span[contains(@id, 'RolesCardTab')]")
    public WebElement pcRolesTab;

    // ADDRESSES TAB
    @FindBy(xpath = "//span[contains(@id, 'AddressesCardTab')]")
    public WebElement addressesTab;

    // ASSOCIATED TRANSACTIONS
    @FindBy(xpath = "//span[contains(@id, 'JobsCardTab')]")
    public WebElement associatedTransactions;

    // ASSOCIATED POLICIES
    @FindBy(xpath = "//span[contains(@id, 'PoliciesCardTab')]")
    public WebElement associatedPoliciesTab;

    // AGENTS
    @FindBy(xpath = "//span[contains(@id, 'AgentsCardTab')]")
    public WebElement agentsTab;

    @FindBy(xpath = "//div[contains(@id, 'AccountContactCV:AccountPCsLV-body')]/descendant::table")
    public WebElement table_Agents;

    @FindBy(xpath = "//div[contains(@id, 'ContactNameInputSet:SSN-inputEl') or contains(@id, ':OfficialIDDV_SSN-inputEl')]")
    public WebElement textbox_SSN;


    public String getSSN() {
        return textbox_SSN.getText();
    }


    public void selectContactRole(String role) {
        Guidewire8Select mySelect = select_ContactRole();
        mySelect.selectByVisibleText(role);
    }


    public void selectPersonCompany(String type) {
        Guidewire8Select mySelect = select_PersonCompany();
        mySelect.selectByVisibleText(type);
    }


    public void clickCreateNew() {
        clickWhenClickable(button_CreateNew);
        
    }


    public void clickContactDetailTab() {
        clickWhenClickable(contactDetailTab);
        
    }


    public void clickPCRolesTab() {
        clickWhenClickable(pcRolesTab);
        
    }


    public void clickAddressesTab() {
        clickWhenClickable(addressesTab);
        
    }


    public void clickAssociatedWorkOrdersTab() {
        clickWhenClickable(associatedTransactions);
        
    }


    public void clickAssociatedPoliciesTab() {
        clickWhenClickable(associatedPoliciesTab);
        
    }


    public void clickAgentsTab() {
        clickWhenClickable(agentsTab);
        
    }


    public void clickFromAddressBook() {
        clickWhenClickable(fromAddressBook);
        
    }


    public void clickAccountingContact() {
        clickWhenClickable(link_AccountingContact);
        
    }


    public void clickInspectionContact() {
        clickWhenClickable(link_InspectionContact);
        
    }


    public void clickOthers() {
        clickWhenClickable(link_Others);
        
    }


    public void clickOwnerOfficer() {
        clickWhenClickable(link_OwnerOfficer);
        
    }


    public void clickPowerOfAttorney() {
        clickWhenClickable(link_PowerOfAttorney);
        
    }


    public String getAssignedAgent() {
        return textbox_AssignedAgent.getText();
    }


    public String getAccountContactsNameByRole(Role role) {
        return table_Agents.findElement(By.xpath(".//descendant::tbody/child::tr/child::td/div[contains(text(), '"
                + role.getValue() + "')]/parent::td/preceding-sibling::td[1]/div")).getText();

    }


    public String getAccountContactsNumberByRole(Role role) {
        return table_Agents.findElement(By.xpath(".//descendant::tbody/child::tr/child::td/div[contains(text(), '"
                + role.getValue() + "')]/parent::td/preceding-sibling::td[2]/div")).getText();

    }

}
