package repository.ab.contact;


import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.SearchAgentSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.TaxReportingOption;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDetailsBasicsAB extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ContactDetailsBasicsAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id, '_tb:importAddressesButton')]")
    private WebElement button_NewContactDetailsBasicsImportAddresses;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ttlBar')]")
    private WebElement text_ViewContactPageTitle;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsCardTab')]")
    private WebElement link_ContactDetailsBasicsBasics;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:AddressesCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsAddress;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:RelatedContactsCardTab')]")
    private WebElement link_ContactDetailsBasicsRelatedContacts;

    @FindBy(xpath = "//span[contains(@id,'ContactDetail:ABContactDetailScreen:AssociatedAccountsCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsAccounts;

    @FindBy(xpath = "//*[contains(@id,':PaidDuesCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsPaidDues;
    
    @FindBy(xpath = "//span[contains(@id,':CommoditiesCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsCommodities;
    
    @FindBy(xpath = "//span[contains(@id,'ABContactDetailScreen:DBACardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsDBAs;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:RoutingCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsRoutingNumbers;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:HistoryCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsHistory;

    @FindBy(xpath = "//span[contains(@id,':ContactBasicsDV_tb:Edit-btnEl')]")
    private WebElement button_ContactDetailsBasicsEdit;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV_tb:ABContactDetailScreen_DeleteButton')]")
    private WebElement button_ContactDetailsBasicsDelete;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:OrganizationName-inputEl') or contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FullName-inputEl')]")
    private WebElement text_ContactDetailsBasicsContactName;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ttlBar')]")
    private WebElement text_ContactDetailsBasicsContactPageTitle;

    @FindBy(xpath = "//div[contains(@id, ':AlternateName-inputEl')]")
    private WebElement text_ContactDetailsBasicsAltName;

    @FindBy(xpath = "//div[contains(@id, ':ContactBasicsDV:SSN-inputEl')]")
    private WebElement text_ContactDetailsBasicsSSN;

    @FindBy(xpath = "//div[contains(@id, ':ContactBasicsDV:Secondary-inputEl')]/a")
    private WebElement text_ContactDetailsBasicsAlternateEmail;

    @FindBy(xpath = "//*[contains(@id, ':ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:AddressSummary-inputEl')]")
    private WebElement text_ContactDetailsBasicsAddress;

    @FindBy(xpath = "//*[contains(@id, ':TagsInputSet:TagsNonEdit-inputEl')]")
    private WebElement text_ContactDetailsBasicsRole;

    @FindBy(xpath = "//div[contains(@id, ':BusinessPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsBasicsBusinessPhone;

    @FindBy(xpath = "//div[contains(@id, ':Work:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsBasicsWorkPhone;

    @FindBy(xpath = "//div[contains(@id, ':Home:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsBasicsHomePhone;

    @FindBy(xpath = "//div[contains(@id, ':Cell:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsBasicsMobilePhone;

    @FindBy(xpath = "//div[contains(@id, ':Fax:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsBasicsFaxPhone;

    @FindBy(xpath = "//div[contains(@id, ':FBAccountInfoInputSet:MembershipType-inputEl')]")
    private WebElement text_MembershipType;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:ContactEFTLV-body')]/div/table")
    private WebElement table_ContactDetailsBasicsEftInfo;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:ContactEFTLV-body')]/div/table/descendant::tr/td[4])]")
    private List<WebElement> text_ContactDetailsBasicsEftInfoAccountNumber;

    @FindBy(xpath = "//div[contains(@id, ':HireDate-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoHireDate;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:TerminationDate-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoTerminationDate;

    @FindBy(xpath = "//div[contains(@id, ':CropSpecialist-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoCropSpecialist;

    @FindBy(xpath = "//div[contains(@id, ':LifeSpecialist-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoLifeSpecialist;

    @FindBy(xpath = "//div[contains(@id, ':County-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoCounty;

    @FindBy(xpath = "//div[contains(@id, ':ContactBasicsDV:SpeedDial-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoSpeedDial;

    @FindBy(xpath = "//div[contains(@id, ':Agency-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoAgency;

    @FindBy(xpath = "//div[contains(@id, ':AgencyManager-inputEl')]")
    private WebElement text_ContactDetailsBasicsAgentInfoManager;

    @FindBy(xpath = "//input[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:LicenseNumber-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsDL;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:LicenseNumber-inputEl')]")
    private WebElement text_ContactDetailsBasicsDL;

    @FindBy(xpath = "//div[contains(@id, ':FBVendorInputSet:VendorNumber-inputEl')]")
    private WebElement text_ContactDetailsBasicsClaimVendorNumber;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBVendorInputSet:VendorType-inputEl')]")
    private WebElement text_ContactDetailsBasicsClaimVendorType;

    private Guidewire8Select select_ContactDetailsBasicsDlState() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:LicenseState-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:LicenseState-inputEl')]")
    private WebElement text_ContactDetailsBasicsDLState;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:NoActivePolicies')]")
    private WebElement text_ContactDetailsBasicsNoActivePolicies;

    @FindBy(xpath = "//div[contains(@id, 'DV:FBVendorInputSet:VendorName-inputEl')]")
    private WebElement text_ContactDetailsBasicsVendorName;

    @FindBy(xpath = "//div[contains(@id, ':FBNoteInputSet:PublicID-inputEl')]")
    private WebElement text_ContactDetailsBasicsPublicID;

    @FindBy(xpath = "//div[contains(@id, ':FBNoteInputSet:Update-inputEl')]")
    private WebElement text_ContactDetailsBasicsUpdateBy;

    @FindBy(xpath = "//div[contains(@id, ':FBNoteInputSet:Create-inputEl')]")
    private WebElement text_ContactDetailsBasicsCreateBy;

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
    private WebElement editBox_ContactDetailsZip;

    private Guidewire8Select select_ContactDetailsState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
    }

    // ***************************************************************************************************
    // Items Below are the Helper methods for the Contact Details Basics Edit
    // Page Repository Items.
    // ***************************************************************************************************

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ContactBasicsDV_tb:Update-btnEl')]")
    private WebElement button_ContactDetailsBasicsUpdate;

    @FindBy(xpath = "//span[contains(@id,'_CancelButton-btnEl')]")
    private WebElement button_ContactDetailsBasicsCancel;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ContactBasicsDV_tb:importAddressesButton']")
    private WebElement button_ContactDetailsBasicsImportAddress;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ContactBasicsDV:OrganizationName-inputEl']")
    private WebElement editbox_ContactDetailsBasicsCompanyName;

    @FindBy(xpath = "//*[contains(@id, ':ContactBasicsDV:FirstName-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsFirstName;

    @FindBy(xpath = "//input[contains(@id,':ContactBasicsDV:LastName-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsLastName;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ContactBasicsDV:Suffix-triggerWrap']")
    private WebElement combo_ContactDetailsBasicsSuffix;

    @FindBy(xpath = "//input[contains(@id, ':ContactBasicsDV:FormerName-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsFormerName;

    @FindBy(xpath = "//input[contains(@id, ':ContactBasicsDV:AlternateName-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAlternateName;

    @FindBy(xpath = "//input[contains(@id, ':ABContactDetailScreen:ContactBasicsDV:SSN-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsSSN;

    @FindBy(xpath = "//input[contains(@id, ':ABContactDetailScreen:ContactBasicsDV:TIN-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsTIN;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:AlternateID-inputEl')]")
    private WebElement text_ContactDetailsBasicsAlternateID;

    private Guidewire8Select select_ContactDetailsBasicsMaritalStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:MaritalStatus-triggerWrap')]");
    }

    private Guidewire8Select select_ContactDetailsBasicsGender() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactBasicsDV:Gender-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':DateOfBirth-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsDob;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:MinorIcon')]/img")
    private WebElement img_ContactDetailsBasicsDob;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:MinorText')]")
    private WebElement text_ContactDetailsBasicsDob;

    @FindBy(xpath = "//span[contains(@id, ':TagsInputSet:Add-btnEl')]")
    private WebElement button_ContactDetailsBasicsRolesAdd;

    @FindBy(xpath = "//label[. = 'Roles']/parent::td/following-sibling::td/div[contains(@id, ':ContactBasicsDV:TagsInputSet:')]")
    private WebElement table_ContactDetailRoles;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:TagsInputSet:Remove-btnEl')]")
    private WebElement button_ContactDetailsRolesRemove;

    // there must be only one role Combobox on the page at a time.
    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:TagsInputSet')]//div/table//table[contains(@id, 'triggerWrap')]")
    private WebElement combo_ContactDetailsBasicsRoles;

    @FindBy(xpath = "//*[contains(@id, 'ContactBasicsDV:EmailAddress1-inputEl') or contains(@id, ':ContactBasicsDV:Primary-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsMainEmail;

    @FindBy(xpath = "//input[contains(@id, ':ContactBasicsDV:EmailAddress2-inputEl') or contains(@id, ':ContactBasicsDV:Secondary-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAltEmail;

    @FindBy(xpath = "//*[contains(@id,'ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine1-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAddressLine1;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine2-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAddressLine2;

    @FindBy(xpath = "//*[contains(@id, ':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsCity;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:County-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsCounty;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:State-triggerWrap')]")
    private WebElement combo_ContactDetailsBasicsState;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsZipCode;

    private Guidewire8Select select_ContactDetailsBasicsAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PrimaryAddressInputSet:AddressOwnerInputSet:Address_AddressType-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id, ':ContactBasicsDV:FBAccountInfoInputSet:AccountNumber-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAccountNumber;

    @FindBy(xpath = "//*[contains(@id, ':FBAccountInfoInputSet:AccountNumber:AccountNumberGenerator')]")
    private WebElement link_ContactDetailsBasicsAccountNumberGenerate;

    @FindBy(xpath = "//input[contains(@id, ':Agent-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAgent;

    @FindBy(xpath = "//div[contains(@id, ':Agent-inputEl')]")
    private WebElement div_ContactDetailsBasicsAgent;

    @FindBy(xpath = "//div[contains(@id, ':ABContactDetailScreen:ContactBasicsDV:FBAccountInfoInputSet:Agent:SelectAgent')]")
    private WebElement link_ContactDetailsBasicsAgentSearch;


    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBAccountInfoInputSet:AccountNumber-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAccountNumber1;

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBAccountInfoInputSet:AccountNumber-inputEl']")
    private WebElement editbox_ContactDetailsBasicsAccountNumber2;

    private Guidewire8Select select_ContactDetailsMembershipType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactBasicsDV:FBAccountInfoInputSet:MembershipType-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'Contact:FBContactMenuActions-btnEl') or contains(@id, 'Contact:ContactMenuActions-btnInnerEl')]")
    private WebElement button_ContactDetailsBasicsActions;

    @FindBy(xpath = "//div[@id='Contact:ContactMenuActions:ContactActions:Transfer']")
    private WebElement button_ContactDetailsBasicsActionsTransfer;

    @FindBy(xpath = "//div[@id='Contact:ContactMenuActions:ContactActions:Transfer:AccountTransfer']")
    private WebElement button_ContactDetailsBasicsActionsAccountTransfer;

    private Guidewire8RadioButton radio_ContactDetailsBasicsMembershipOnly() {
        return new Guidewire8RadioButton(getDriver(), "//table[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBAccountInfoInputSet:MembershipOnly')]");
    }

    private Guidewire8Select select_ContactDetailsBasicsTaxReportingAs() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBTaxInputSet:TaxReporting-triggerWrap')]");
    }

    private Guidewire8RadioButton radio_ContactDetailsBasicsIrsLienTrue() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBTaxInputSet:IRSLien-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsBasicsIrsLienFalse() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBTaxInputSet:IRSLien-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBTaxInputSet:BackupWithholding1-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsBackupWithholding1;

    @FindBy(xpath = "//div[contains(@id, ':BrokerageAgent:SelectBrokerageAgent')]")
    private WebElement link_ContactDetailsBasicsBrokerageSearch;

    @FindBy(xpath = "//input[contains(@id, ':TIN-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsTin;

    // @FindBy(xpath = "//input[contains(@id,
    // 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBAccountInfoInputSet:MembershipOnly_true-inputEl')]")
    // public WebElement radio_ContactDetailsBasicsMembershipOnlyYes;

    @FindBy(xpath = "//input[contains(@id, ':FBAccountInfoInputSet:Website-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsWebsite;

    @FindBy(xpath = "//textarea[contains(@id, ':FBNoteInputSet:Notes-inputEl')]")
    private WebElement textbox_ContactDetailsBasicsComments;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:ContactEFTLV')]")
    private WebElement div_EftContainer;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:StandardizedDate-inputEl')]")
    private WebElement text_ContactDetailsStandardizedOn;

    @FindBy(xpath = "//input[@id='ContactDetail:ABContactDetailScreen:ContactBasicsDV:OrganizationName-inputEl']")
    private WebElement editbox_ContactDetailsBasicsName;

    @FindBy(xpath = "//input[contains(@id, ':BrokerageAgent-inputEl')]")
    private WebElement editbox_ContactDetailsBasicsAgentName;

    @FindBy(xpath = "//span[contains(@id, 'FarmBureauAgentTerminated')]")
    private WebElement text_ContactDetailAgentTerminated;

    @FindBy(xpath = "//input[contains(@id, ':ContactBasicsDV:FBVendorInputSet:VendorName-inputEl')]")
    private WebElement editbox_ContactDetailVendorName;

    @FindBy(xpath = "//input[contains(@id, ':FBLienholderInputSet:LienholderNumber-inputEl')]")
    private WebElement editbox_ContactDetailLienholderNumber;

    @FindBy(xpath = "//input[contains(@id, ':FBLienholderInputSet:TerminateDate-inputEl')]")
    private WebElement editbox_ContactDetailLienholderTerminationDate;

    @FindBy(xpath = "//div[contains(@id, ':FBLienholderInputSet:LienholderNumber-inputEl')]")
    private WebElement text_ContactDetailLienholderNumber;

    @FindBy(xpath = "//input[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:FBTaxInputSet:TIN-inputEl')]")
    private WebElement editbox_ContactDetailTaxTin;

    @FindBy(xpath = "//input[contains(@id, ':FBVendorInputSet:VendorNumber-inputEl')]")
    private WebElement editbox_ContactDetailVendorNumber;

    @FindBy(xpath = "//a[contains(@id, ':FBVendorInputSet:VendorNumber:AccountNumberGenerator')]")
    private WebElement link_ContactDetailVendorNumberGenerate;

    private Guidewire8Select select_ContactDetailsBasicsClaimVendorType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':FBVendorInputSet:VendorType-triggerWrap')]");
    }


    // **************************************************************************
    // Methods below are the helper methods to the Contact Details Basics Page.
    // ***************************************************************************

    public ContactDetailsBasicsAB getToContactPage(AbUsers user, String firstName, String lastName, String address, State state) throws GuidewireNavigationException {
        repository.ab.search.AdvancedSearchAB searchMe = new repository.ab.search.AdvancedSearchAB(driver);
        searchMe.loginAndSearchContact(user, firstName, lastName, address, state);
        return new ContactDetailsBasicsAB(driver);
    }

    public ContactDetailsBasicsAB getToAgentContact(AbUsers user, Agents agent) {
        repository.ab.search.SearchAgentSearchAB agentSearch = new repository.ab.search.SearchAgentSearchAB(driver);
        agentSearch.loginAndSearchAgent(user, agent);
        return new ContactDetailsBasicsAB(driver);
    }

    public String getAgentTerminatedText() {
        if (checkIfElementExists("//span[contains(@id, 'FarmBureauAgentTerminated')]", 2000))
            return text_ContactDetailAgentTerminated.getText();

        else return "";
    }

    public String getContactPageTitle() {
        String text = waitUntilElementIsVisible(text_ViewContactPageTitle).getText();
        return text;
    }

    private void clickNewContactDetailsBasicsImportAddressButton() {
        waitUntilElementIsClickable(button_NewContactDetailsBasicsImportAddresses);
        button_NewContactDetailsBasicsImportAddresses.click();
    }

    public void importAddresses(String importLastName, String importFirstName, String contactPrimaryAddressLine1) {
        clickNewContactDetailsBasicsImportAddressButton();
        repository.ab.search.AdvancedSearchAB importContactAddress = new AdvancedSearchAB(driver);
        if (importFirstName == null) {
            importContactAddress.searchCompanyByName(importLastName, contactPrimaryAddressLine1, State.Idaho);
        } else {
            importContactAddress.searchByFirstLastNameSelect(importFirstName, null, importLastName, contactPrimaryAddressLine1);
        }
    }

    public String getAltName() {
        
        if (checkIfElementExists(text_ContactDetailsBasicsAltName, 1000)) {
            return text_ContactDetailsBasicsAltName.getText();
        } else {
            return "Alt Name does not exist because it is the same as the Name.";
        }
    }

    public String getAlternateID() {
        
        waitUntilElementIsVisible(text_ContactDetailsBasicsAlternateID);
        return text_ContactDetailsBasicsAlternateID.getText();
    }

    public void clickContactDetailsBasicsAddressLink() throws GuidewireNavigationException {
    	waitForPageLoad();
        clickWhenClickable(link_ContactDetailsBasicsAddress);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':AddressDetailCardTab-btnInnerEl')]", 3000, "UNABLE TO GET TO ADDRESS TAB AFTER CLICKING THE TAB LINK");
//        waitUntilElementIsVisible(findElement(By.xpath("//span[contains(@id, ':AddressDetailCardTab-btnInnerEl')]")), 3000);
        
    }

    public void clickContactDetailsBasicsRelatedContactsLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsRelatedContacts, 200000);
        link_ContactDetailsBasicsRelatedContacts.click();
    }

    public void clickContactDetailsBasicsAccountsLink() {
        clickWhenClickable(link_ContactDetailsBasicsAccounts);
        
    }

    public boolean clickContactDetailsBasicsPaidDuesLink() {
        
        if (checkIfElementExists(link_ContactDetailsBasicsPaidDues, 2000)) {
            waitUntilElementIsClickable(link_ContactDetailsBasicsPaidDues);
            link_ContactDetailsBasicsPaidDues.click();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean clickCommoditiesTab() {
	    if (checkIfElementExists(link_ContactDetailsBasicsCommodities, 2000)) {
	        waitUntilElementIsClickable(link_ContactDetailsBasicsCommodities);
	        link_ContactDetailsBasicsCommodities.click();
	        return true;
	    } else {
	        return false;
	    }
    }

    public void clickContactDetailsBasicsDBAsLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsDBAs);
        link_ContactDetailsBasicsDBAs.click();
        waitForPageLoad();
    }

    public void clickContactDetailsBasicsRoutingLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsRoutingNumbers);
        link_ContactDetailsBasicsRoutingNumbers.click();
    }

    public void clickContactDetailsBasicsHistoryLink() {
        clickWhenClickable(link_ContactDetailsBasicsHistory);
    }

    public boolean clickContactDetailsBasicsEditLink() {
        
        if (checkIfElementExists(button_ContactDetailsBasicsEdit, 1000)) {
            waitUntilElementIsClickable(button_ContactDetailsBasicsEdit);
            button_ContactDetailsBasicsEdit.click();
            
            return true;
        } else {
            return false;
        }
    }

    public String getBrokerageAgent() {
        waitUntilElementIsVisible(editbox_ContactDetailsBasicsAgentName);
        return editbox_ContactDetailsBasicsAgentName.getAttribute("value");
    }

    public void addBrokerageAgent(String fullName, String agentNum) {
        clickWhenClickable(link_ContactDetailsBasicsBrokerageSearch);
        repository.ab.search.SearchAgentSearchAB mwAgentSearch = new repository.ab.search.SearchAgentSearchAB(driver);
        mwAgentSearch.clickSearchAgentSearchReset();
        mwAgentSearch.setSearchAgentSearchAgentNumber(agentNum);
        mwAgentSearch.clickSearchAgentSearchSearch();
        mwAgentSearch.clickSearchAgentSearchAgentSelectLink(fullName, agentNum);
    }

    public void clickContactDetailsBasicsDeleteLink() {
        waitUntilElementIsClickable(button_ContactDetailsBasicsDelete);
        button_ContactDetailsBasicsDelete.click();
        
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public List<WebElement> clickContactDetailsBasicsUpdateLink() {
        
        waitUntilElementIsClickable(button_ContactDetailsBasicsUpdate);
        button_ContactDetailsBasicsUpdate.click();
        
        try {
            ErrorHandling validationError = new ErrorHandling(getDriver());
            validationError.button_Clear().click();
            clickUpdate();
        } catch (NoSuchElementException e) {
        }

        ErrorHandling uiErrors = new ErrorHandling(getDriver());
        return uiErrors.text_ErrorHandlingErrorBannerMessages();

    }

    public String getContactDetailsBasicsContactName() {
        if (checkIfElementExists(text_ContactDetailsBasicsContactName, 1000)) {
            String contactName = text_ContactDetailsBasicsContactName.getText();
            int indexOfParenthesis = contactName.indexOf("(");
            return contactName.substring(0, indexOfParenthesis - 1);
        } else {
            checkIfElementExists(text_ContactDetailsBasicsContactPageTitle, 1000);
            String contactName = text_ContactDetailsBasicsContactPageTitle.getText();
            return contactName;
        }
    }

    public String getContactDetailsBasicsContactNameWithAge() {
        if (checkIfElementExists(text_ContactDetailsBasicsContactName, 1000)) {
            String contactName = text_ContactDetailsBasicsContactName.getText();
            return contactName;
        } else {
            checkIfElementExists(text_ContactDetailsBasicsContactPageTitle, 1000);
            String contactName = text_ContactDetailsBasicsContactPageTitle.getText();
            return contactName;
        }
    }

    public String getContactDetailsBasicsMainEmail() {
    	 
    	 waitUntilElementIsVisible(editbox_ContactDetailsBasicsMainEmail);
    	 return editbox_ContactDetailsBasicsMainEmail.getText();
    }

    public String getContactDetailsBasicsAltEmail() {
        
        if (checkIfElementExists(editbox_ContactDetailsBasicsAltEmail, 1000)) {
            return editbox_ContactDetailsBasicsAltEmail.getText();
        } else {
            waitUntilElementIsVisible(text_ContactDetailsBasicsAlternateEmail);
            return text_ContactDetailsBasicsAlternateEmail.getText();
        }
    }

    public String getContactDetailsBasicsContactAddress() {
        waitUntilElementIsVisible(text_ContactDetailsBasicsAddress);
        String contactAddress = text_ContactDetailsBasicsAddress.getText();
        return contactAddress;
    }

    public void clickContactDetailsBasicsCancel() {
        waitUntilElementIsClickable(By.xpath("//span[contains(@id,'_CancelButton-btnEl')]"));
        button_ContactDetailsBasicsCancel.click();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public String clickContactDetailsBasicsCancel(OkCancel okCancel) {
        super.clickCancel();
        String popupText = selectOKOrCancelFromPopup(okCancel);
        return popupText;
    }

    public void clickActionsButton() {
        clickWhenClickable(button_ContactDetailsBasicsActions);
        
    }

    public void clickTransferButton() {
        clickWhenClickable(button_ContactDetailsBasicsActionsTransfer);
        clickWhenClickable(button_ContactDetailsBasicsActionsAccountTransfer);
        
    }

    public String[] getRoles() {
        
        waitUntilElementIsVisible(text_ContactDetailsBasicsRole, 2500);
        String allRoles = text_ContactDetailsBasicsRole.getText();
        String[] roles = allRoles.split("\n");
        return roles;
    }

    public ArrayList<String> getEftAccountNumber() {
        return tableUtils.getAllCellTextFromSpecificColumn(div_EftContainer, "Account Number");
    }

    public String getAgentHireDate() {
        
        if (checkIfElementExists(text_ContactDetailsBasicsAgentInfoHireDate, 1000)) {
            return text_ContactDetailsBasicsAgentInfoHireDate.getText();
        } else {
            return "No Agent Hire Date found.";
        }
    }

    public String checkIfTerminationDateExists() {
        
        if (checkIfElementExists("//label[contains(.,'Termination Date') and contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:TerminationDate-labelEl')]", 2000)) {
            return text_ContactDetailsBasicsAgentInfoTerminationDate.getText();
        } else {
            return "false";
        }
    }

    public boolean getCropSpecialistDesignation() {
        String cropSpecialistText = text_ContactDetailsBasicsAgentInfoCropSpecialist.getText();
        return cropSpecialistText.equals("Yes");
        }

    public String getAgentManager() {
        if (checkIfElementExists(text_ContactDetailsBasicsAgentInfoManager, 2000)) {
            return text_ContactDetailsBasicsAgentInfoManager.getText();
        } else {
            return "The Agency Manager was not found on the page.";
        }
    }

    public String getAgentNumber(String type) {
        if (checkIfElementExists("//div[. = '" + type + "']/parent::td/parent::tr", 2000)) {
            String record = find(By.xpath("//div[. = '" + type + "']/parent::td/parent::tr")).getAttribute("data-recordid"); //ext-record-1653
            String gridColumn = find(By.xpath("//div[contains(@id,'ABContactDetailPopup:ABContactDetailScreen:ContactBasicsDV:9') or contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV:9') or contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV:7')]//span[contains(.,'Number')]")).getAttribute("id"); //gridcolumn-1348-textEl
            gridColumn = gridColumn.replace("-textEl", ""); //Make sure this step is hit.
            return find(By.xpath("//div[contains(@id,'ABContactDetailPopup:ABContactDetailScreen:ContactBasicsDV:9') or contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV:9') or contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV:7')]//tr[contains(@id,'" + record + "')]/td[contains(@class,'" + gridColumn + "')]/div")).getText();
        } else {
            return "The type " + type + " does not exist.";
        }
    }

    public String getAgentCounty() {
        if (checkIfElementExists(text_ContactDetailsBasicsAgentInfoCounty, 1000)) {
            return text_ContactDetailsBasicsAgentInfoCounty.getText();
        } else {
            return "Could not find the County.";
        }
    }

    public String getAgentSpeedDial() {
        return text_ContactDetailsBasicsAgentInfoSpeedDial.getText();
    }

    public String getAgentAgency() {
        if (checkIfElementExists(text_ContactDetailsBasicsAgentInfoAgency, 1000)) {
            return text_ContactDetailsBasicsAgentInfoAgency.getText();
        } else {
            return "Could not find the Region.";
        }
    }

    public String getaddressBookUID() {
    	
    	waitUntilElementIsVisible(text_ContactDetailsBasicsPublicID);
    	if (checkIfElementExists(text_ContactDetailsBasicsPublicID, 1000)) {
            return text_ContactDetailsBasicsPublicID.getText();
        } else {
            return "Could not find the Address Book UID.";
        }
    }

    public String getUpdateByText() {
    	
    	waitUntilElementIsVisible(text_ContactDetailsBasicsUpdateBy);
    	if (checkIfElementExists(text_ContactDetailsBasicsUpdateBy, 1000)) {
            return text_ContactDetailsBasicsUpdateBy.getText();
        } else {
            return "Could not find the Update By text.";
        }
    }

    public String getCreateByText() {
    	
    	waitUntilElementIsVisible(text_ContactDetailsBasicsCreateBy);
    	if (checkIfElementExists(text_ContactDetailsBasicsCreateBy, 1000)) {
            return text_ContactDetailsBasicsCreateBy.getText();
        } else {
            return "Could not find the Create By text.";
        }
    }

    // ***************************************************************************************************
    // Items Below are the Helper methods for the Contact Details Basics Edit
    // Page Repository Items.
    // ***************************************************************************************************

    public void updateContactName(String firstName, String lastNameOrCompanyName) {
        waitForPostBack();
        if (checkIfElementExists(button_ContactDetailsBasicsEdit, 1)) {
            clickWhenClickable(button_ContactDetailsBasicsEdit);
        }

        if (firstName == null || firstName.equals("")) {
            setContactDetailsBasicsName(lastNameOrCompanyName);
        } else {
            setContactDetailsBasicsFirstName(firstName);
            setContactDetailsBasicsLastName(lastNameOrCompanyName);
        }
        clickUpdate();
        waitForPostBack();
    }

    public void setContactDetailsBasicsFirstName(String firstName) {
    	clickWhenClickable(editbox_ContactDetailsBasicsFirstName);
    	editbox_ContactDetailsBasicsFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_ContactDetailsBasicsFirstName.sendKeys(Keys.DELETE);
    	editbox_ContactDetailsBasicsFirstName.sendKeys(firstName);
    	editbox_ContactDetailsBasicsFirstName.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsLastName(String lastName) {
        clickWhenClickable(editbox_ContactDetailsBasicsLastName);
    	editbox_ContactDetailsBasicsLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_ContactDetailsBasicsLastName.sendKeys(Keys.DELETE);
    	editbox_ContactDetailsBasicsLastName.sendKeys(lastName);
    	editbox_ContactDetailsBasicsLastName.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsName(String newName) {
        clickWhenClickable(editbox_ContactDetailsBasicsCompanyName);
        editbox_ContactDetailsBasicsCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsCompanyName.sendKeys(newName);
    }

    public void setContactDetailsBasicsSuffix(String suffix) {
        Guidewire8Select contactSuffix = new Guidewire8Select(driver, "//table[contains(@id,'ContactDetailPopup:ABContactDetailScreen:ContactBasicsDV:Suffix')]");
        contactSuffix.selectByVisibleText(suffix);
    }

    public List<String> getContactDetailsBasicsSuffix() {
        Guidewire8Select contactSuffix = new Guidewire8Select(driver, "//table[contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV:Suffix-triggerWrap')]");
        return contactSuffix.getList();
    }


    public void setContactDetailsBasicsFormerName(String formerName) {
        clickWhenClickable(editbox_ContactDetailsBasicsFormerName);
        editbox_ContactDetailsBasicsFormerName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsFormerName.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsFormerName.sendKeys(formerName);
        editbox_ContactDetailsBasicsFormerName.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsAlternateName(String alternateName) {
        clickWhenClickable(editbox_ContactDetailsBasicsAlternateName);
        editbox_ContactDetailsBasicsAlternateName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAlternateName.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAlternateName.sendKeys(alternateName);
        editbox_ContactDetailsBasicsAlternateName.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsSSN(String ssn) {
        clickWhenClickable(editbox_ContactDetailsBasicsSSN);
        editbox_ContactDetailsBasicsSSN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsSSN.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsSSN.sendKeys(ssn);
        editbox_ContactDetailsBasicsSSN.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsTIN(String tin) {

        editbox_ContactDetailsBasicsTIN.sendKeys(tin);
    }

    public void clickContactDetailsBasicsRolesAddLink() {
        waitUntilElementIsClickable(button_ContactDetailsBasicsRolesAdd);
        button_ContactDetailsBasicsRolesAdd.click();
    }

    public void setDateOfBirth(String dob) {
        clickWhenClickable(editbox_ContactDetailsBasicsDob);
        editbox_ContactDetailsBasicsDob.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsDob.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsDob.sendKeys(dob);
        editbox_ContactDetailsBasicsDob.sendKeys(Keys.TAB);
    }

    public void setMaritalStatus(String maritalStatus) {
        
        Guidewire8Select contactMaritalStatus = select_ContactDetailsBasicsMaritalStatus();
        contactMaritalStatus.selectByVisibleText(maritalStatus);
    }
    
    public ArrayList<String> getMaritalStatusOptions(){
    	waitForPostBack();
    	return select_ContactDetailsBasicsMaritalStatus().getListItems();
    }

    public void setGender(String gender) {
        
        Guidewire8Select contactGender = select_ContactDetailsBasicsGender();
        contactGender.selectByVisibleText(gender);
    }

    public String validateMinorChild(String dob) throws ParseException {
        Date dateOfBirth = DateUtils.convertStringtoDate(dob, "MM/dd/yyyy");
        int age = DateUtils.getDifferenceBetweenDates(dateOfBirth, DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.ContactManager), DateDifferenceOptions.Year);
        
        if (age < 18) {
            img_ContactDetailsBasicsDob.getAttribute("src");
            return text_ContactDetailsBasicsDob.getText();
        } else {
            return "Not minor child";
        }
    }

    private void setContactDetailsBasicsRole(String role) {
    	
        waitUntilElementIsClickable(table_ContactDetailRoles);
        
        tableUtils.clickRowInTableByText(table_ContactDetailRoles, "<none>");
        WebElement roleLineItem = find(By.xpath("//li[. = '" + role + "']"));
        clickWhenClickable(roleLineItem);

/*		Guidewire8Select newRole = new Guidewire8Select(
				"//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:TagsInputSet')]//div/table//table[contains(@id, 'triggerWrap')]");
		newRole.selectByVisibleText(role);
*/
    }

    public void addContactDetailsBasicsRole(ContactRole role) {
        waitUntilElementIsClickable(button_ContactDetailsBasicsRolesAdd);
        clickContactDetailsBasicsRolesAddLink();
        setContactDetailsBasicsRole(role.getValue());
        waitForPostBack();
    }

    public void removeContactDetailsBasicsRole(ContactRole role) {
        waitUntilElementIsClickable(table_ContactDetailRoles);
        
        tableUtils.setCheckboxInTableByText(table_ContactDetailRoles, role.getValue(), true);
        clickWhenClickable(button_ContactDetailsRolesRemove);
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }

    public void setMainEmail(String email) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsMainEmail);
        editbox_ContactDetailsBasicsMainEmail.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsMainEmail.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsMainEmail.sendKeys(email);
        editbox_ContactDetailsBasicsMainEmail.sendKeys(Keys.TAB);

    }

    public void setAltEmail(String email) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsAltEmail);
        editbox_ContactDetailsBasicsAltEmail.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAltEmail.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAltEmail.sendKeys(email);
        editbox_ContactDetailsBasicsAltEmail.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsAddressLine1(String addressLine1) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsAddressLine1);
        editbox_ContactDetailsBasicsAddressLine1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAddressLine1.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAddressLine1.sendKeys(addressLine1);
        editbox_ContactDetailsBasicsAddressLine1.sendKeys(Keys.TAB);
    }

    public void setContactDetailsBasicsAddressLine2(String addressLine2) {
        if (addressLine2 != null) {
            waitUntilElementIsClickable(editbox_ContactDetailsBasicsAddressLine2);
            editbox_ContactDetailsBasicsAddressLine2.sendKeys(addressLine2);
        }
    }

    public void setContactDetailsBasicsCity(String city) {
        clickWhenClickable(editbox_ContactDetailsBasicsCity);
        editbox_ContactDetailsBasicsCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsCity.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsCity.sendKeys(city);
        editbox_ContactDetailsBasicsCity.sendKeys(Keys.TAB);
        
    }

    public void setContactDetailsBasicsCounty(String county) {
        waitUntilElementIsClickable(editbox_ContactDetailsBasicsCounty);
        clickWhenClickable(editbox_ContactDetailsBasicsCounty);
        editbox_ContactDetailsBasicsCounty.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsCounty.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsCounty.sendKeys(county);
        editbox_ContactDetailsBasicsCounty.sendKeys(Keys.TAB);
        
    }

    public void setContactDetailsBasicsState(State state) {
        Guidewire8Select commonState = select_ContactDetailsState();
        commonState.selectByVisibleText(state.getName());
    }

    public void setContactDetailsBasicsZipCode(String zip) {
        clickWhenClickable(editBox_ContactDetailsZip);
        editBox_ContactDetailsZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zip);
    }

    public void setContactDetailsBasicsAddressType(String type) {
        
        Guidewire8Select addressType = new Guidewire8Select(driver, "//*[contains(@id, ':ABContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:AddressOwnerInputSet:Address_AddressType-triggerWrap')]");
        addressType.selectByVisibleText(type);
    }

    public void setAddresses(ArrayList<AddressInfo> addresses) throws GuidewireNavigationException {
        for (int i = 0; i < addresses.size(); i++) {
            if (i == 0) {
                setContactDetailsBasicsAddressLine1(addresses.get(i).getLine1());
                setContactDetailsBasicsAddressLine2(addresses.get(i).getLine2());
                setContactDetailsBasicsState(addresses.get(i).getState());
                setContactDetailsBasicsCity(addresses.get(i).getCity());
                setContactDetailsBasicsCounty(addresses.get(i).getCounty());
                setContactDetailsBasicsZipCode(addresses.get(i).getZip());
                setContactDetailsBasicsAddressType(addresses.get(i).getType().getValue());
            } else {
                clickContactDetailsBasicsAddressLink();
                ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
                addressPage.clickContactDetailsAddressesAdd();
//                addresses.remove(addresses.get(0));
                addressPage.addAddresses(addresses);
                PageLinks tabLinks = new PageLinks(driver);
                tabLinks.clickContactDetailsBasicsLink();
            }
        }
    }

    public ArrayList<String> getPhoneNumbers() {
        ArrayList<String> phone = new ArrayList<String>();
        if (text_ContactDetailsBasicsHomePhone.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            phone.add(text_ContactDetailsBasicsHomePhone.getText());
        }
        if (text_ContactDetailsBasicsBusinessPhone.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            phone.add(text_ContactDetailsBasicsBusinessPhone.getText());
        }
        if (text_ContactDetailsBasicsWorkPhone.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            phone.add(text_ContactDetailsBasicsWorkPhone.getText());
        }
        if (text_ContactDetailsBasicsMobilePhone.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            phone.add(text_ContactDetailsBasicsMobilePhone.getText());
        }
        if (text_ContactDetailsBasicsFaxPhone.getText().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
            phone.add(text_ContactDetailsBasicsFaxPhone.getText());
        }
        return phone;
    }

    public String getStandardizedDateTime() {
        
        return text_ContactDetailsStandardizedOn.getText();
    }

    public boolean setContactDetailsBasicsAccountNumber(String acctNum) {
        clickWhenClickable(editbox_ContactDetailsBasicsAccountNumber);
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(acctNum);
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(Keys.TAB);
        
        ErrorHandling acctError = new ErrorHandling(driver);
        List<WebElement> errorMessages = acctError.text_ErrorHandlingErrorBannerMessages();
        boolean errorFound = true;
        for (WebElement error : errorMessages) {
            if (error.getText().contains("Account Number : Invalid account number " + acctNum)) {
                errorFound = false;
                break;
            } else
                errorFound = true;
        }
        return errorFound;
    }

    public void clearAcctNum() {
        clickWhenClickable(editbox_ContactDetailsBasicsAccountNumber);
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAccountNumber.sendKeys(Keys.DELETE);

    }

    public String clickContactDetailsBasicsAccountNumberGenerateLink() {
        clickWhenClickable(link_ContactDetailsBasicsAccountNumberGenerate);
        return getContactDetailsBasicsAccountNumber();
    }

    public String getContactDetailsBasicsAccountNumber() {
        
        waitUntilElementIsVisible(editbox_ContactDetailsBasicsAccountNumber);
        
        String text;
        text = editbox_ContactDetailsBasicsAccountNumber.getAttribute("value");
        if(text == null) {
        	text = editbox_ContactDetailsBasicsAccountNumber.getText();
        }
        return text;
    }

    public String getContactDetailsBasicsAgent() {
        
	/*	if(checkIfElementExists("//div[contains(@id,':Agent-inputEl')]", 1000)){
			return
		}*/
        try {
            return editbox_ContactDetailsBasicsAgent.getAttribute("value");
        } catch (Exception e) {
            return div_ContactDetailsBasicsAgent.getText();
        }
    }

    public void clickContactDetailsBasicsAgentSearchLink() {
        waitUntilElementIsClickable(link_ContactDetailsBasicsAgentSearch);
        link_ContactDetailsBasicsAgentSearch.click();
    }

    public void setContactDetailsBasicsAgent(String agentNum, State state) {
        clickWhenClickable(editbox_ContactDetailsBasicsAgent);
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAgent.sendKeys(agentNum + "," + state.getAbbreviation().toUpperCase());
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.TAB);
    }

    public void setAgentGarbage(String agentNum, State state) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsAgent);
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsAgent.sendKeys(state.getAbbreviation().toUpperCase() + "," + agentNum);
        editbox_ContactDetailsBasicsAgent.sendKeys(Keys.TAB);
        
    }

    public Agents setAgent(String agentNumber, String agentName) throws Exception {
        repository.ab.search.SearchAgentSearchAB agentSearch = new repository.ab.search.SearchAgentSearchAB(driver);
        Agents agentInfo;
        if (agentName != null) {
            String[] parsedAgentName = agentName.split("[ ]");
            
            agentInfo = AgentsHelper.getAgentByName(parsedAgentName[0], parsedAgentName[1]);
        } else {
            agentInfo = AgentsHelper.getRandomAgent();
        }

        

        System.out.println("Agent Number = " + agentInfo.getAgentNum());
        System.out.println("Agent Name = " + agentInfo.getAgentFirstName() + " " + agentInfo.getAgentLastName());
        agentSearch.setSearchAgentSearchAgentNumber(agentInfo.getAgentNum());
        agentSearch.clickSearchAgentSearchSearch();
        
        agentSearch.clickSearchAgentSearchAgentSelectLink(agentInfo.getAgentLastName(), agentInfo.getAgentNum());

        // agentSearch.clickSearchAgentSearchReset();
        //
        //
        // agentSearch.setSearchAgentSearchAgentNumber(agentInfo.getAgentNum());
        // agentSearch.clickSearchAgentSearchSearch();
        // 
        // agentSearch.clickSearchAgentSearchAgentSelectLink(agentInfo.getAgentFirstName()
        // +" "+agentInfo.getAgentLastName(), agentInfo.getAgentNum());
        return agentInfo;
    }

    public void clickContactDetailsBasicsMembershipOnlyYes() {
        
        radio_ContactDetailsBasicsMembershipOnly().select(true);
    }

    public void setContactDetailsBasicsMembershipOnly(boolean membershipOnly) {
        
        radio_ContactDetailsBasicsMembershipOnly().select(membershipOnly);
    }

    public String getDLNumber() {
        if (checkIfElementExists(editbox_ContactDetailsBasicsDL, 2000)) {
            return editbox_ContactDetailsBasicsDL.getText();
        } else {
            return text_ContactDetailsBasicsDL.getText();
        }
    }

    public void setDL(String dl) {
        if (!checkIfElementExists("//input[contains(@id, 'ContactDetail:ABContactDetailScreen:ContactBasicsDV:LicenseNumber-inputEl')]", 1000)) {
            if (getDLState().contains("<none>")) {
                setDLState(State.Idaho.getName());
            }
        }
        
        clickWhenClickable(editbox_ContactDetailsBasicsDL);
        editbox_ContactDetailsBasicsDL.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsDL.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsDL.sendKeys(dl);
        editbox_ContactDetailsBasicsDL.sendKeys(Keys.TAB);
    }

    public String getDLState() {
        if (checkIfElementExists(text_ContactDetailsBasicsDLState, 2000)) {
            return text_ContactDetailsBasicsDLState.getText();
        } else {
            return select_ContactDetailsBasicsDlState().getText();
        }
    }

    public void setDLState(String dlState) {
        
        Guidewire8Select selectState = select_ContactDetailsBasicsDlState();
        selectState.selectByVisibleText(dlState);
        
    }

    public boolean checkIfInvalidDriversLicenseMessageExists() {
        ErrorHandling error = new ErrorHandling(driver);
        List<WebElement> messages = error.text_ErrorHandlingErrorBannerMessages();
        for (WebElement msg : messages) {
            if (msg.getText().contains("Drivers license number") || msg.getText().contains("invalid format")) {
                return true;
            }
        }
        return false;
    }


    public String getBusinessPhone() {
        
//		waitUntilElementIsVisible(text_ContactDetailsBasicsBusinessPhone);
        return text_ContactDetailsBasicsBusinessPhone.getText();
    }

    public String getWorkPhone() {
        
//		waitUntilElementIsVisible(text_ContactDetailsBasicsWorkPhone);
        return text_ContactDetailsBasicsWorkPhone.getText();
    }

    public String getHomePhone() {
        
//		waitUntilElementIsVisible(text_ContactDetailsBasicsHomePhone);
        return text_ContactDetailsBasicsHomePhone.getText();
    }

    public String getMobilePhone() {
        
//		waitUntilElementIsVisible(text_ContactDetailsBasicsMobilePhone);
        return text_ContactDetailsBasicsMobilePhone.getText();
    }

    public String getFaxPhone() {
        
//		waitUntilElementIsVisible(text_ContactDetailsBasicsFaxPhone);
        return text_ContactDetailsBasicsFaxPhone.getText();
    }

    public String getMembershipType() {
        
        if (checkIfElementExists(text_MembershipType, 2000)) {
            waitUntilElementIsVisible(text_MembershipType);
            return text_MembershipType.getText();
        } else {
            String type = select_ContactDetailsMembershipType().getText();
            clickProductLogo();
            return type;
        }
    }

    public boolean setMembershipType(ContactMembershipType type) {
    	if(checkIfElementExists("//table[contains(@id, ':ContactBasicsDV:FBAccountInfoInputSet:MembershipType-triggerWrap')]", 1)) {
	        waitUntilElementIsClickable(By.xpath("//table[contains(@id, ':ContactBasicsDV:FBAccountInfoInputSet:MembershipType-triggerWrap')]"));
	        select_ContactDetailsMembershipType().selectByVisibleText(type.getValue());
	        return true;
    	} else {
    		return false;
    	}
        
    }

    public List<String> checkMembershipTypeForCompany() {
        
        return select_ContactDetailsMembershipType().getList();
    }

    public void setIRSLien(boolean trueFalse) {
        
        if (trueFalse) {
            radio_ContactDetailsBasicsIrsLienTrue().select(true);
        } else {
            // clickWhenClickable(radio_ContactDetailsBasicsIrsLienFalse());
            radio_ContactDetailsBasicsIrsLienFalse().select(false);
        }
    }

    public void setBackupWithholdingDate1(Date dateWithholding) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsBackupWithholding1);
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.chord(Keys.DELETE));
        editbox_ContactDetailsBasicsBackupWithholding1
                .sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dateWithholding));
        
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.TAB);

    }

    public void clearBackupWithholdingDate1() {
        clickWhenClickable(editbox_ContactDetailsBasicsBackupWithholding1);
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.chord(Keys.DELETE));
        
        editbox_ContactDetailsBasicsBackupWithholding1.sendKeys(Keys.TAB);

    }

    public String getTin() {
    	if(checkIfElementExists(button_ContactDetailsBasicsEdit, 1000)) {
    		clickWhenClickable(button_ContactDetailsBasicsEdit);
    	}
        waitUntilElementIsVisible(editbox_ContactDetailsBasicsTin);
        return editbox_ContactDetailsBasicsTin.getAttribute("value");
    }

    public String getSsn() {
        
        if (checkIfElementExists(editbox_ContactDetailsBasicsSSN, 1000)) {
            waitUntilElementIsVisible(editbox_ContactDetailsBasicsSSN);
            return editbox_ContactDetailsBasicsSSN.getAttribute("value");
        } else {
            return text_ContactDetailsBasicsSSN.getText();
        }
    }

    public void selectTaxReportingAs(TaxReportingOption reportingAs) {
        select_ContactDetailsBasicsTaxReportingAs().selectByVisibleText(reportingAs.getValue());
        
    }

    public String getTaxInfoTin() {
        

        if (!checkIfElementExists(editbox_ContactDetailTaxTin, 1000)) {
            return "false";
        } else {
            return editbox_ContactDetailTaxTin.getAttribute("value");
        }
    }

    public boolean checkForErrorMessage(String partialMessage) {
        boolean errorMessageMatch = false;
        ErrorHandling ssnError = new ErrorHandling(driver);
        List<WebElement> errors = ssnError.text_ErrorHandlingErrorBannerMessages();
        for (WebElement error : errors) {
            if (error.getText().contains(partialMessage))
                errorMessageMatch = true;
        }
        return errorMessageMatch;
    }

    public String getNoPolicyNotification() {
        
        waitUntilElementIsVisible(text_ContactDetailsBasicsNoActivePolicies);
        return text_ContactDetailsBasicsNoActivePolicies.getText();
    }

    public String getVendorNumber() {
    	
        return text_ContactDetailsBasicsClaimVendorNumber.getText();
    }

    public String getVendorType() {
        return text_ContactDetailsBasicsClaimVendorType.getText();
    }

    public void setComments(String comments) {
        
        clickWhenClickable(textbox_ContactDetailsBasicsComments);
        textbox_ContactDetailsBasicsComments.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textbox_ContactDetailsBasicsComments.sendKeys(Keys.DELETE);
        textbox_ContactDetailsBasicsComments.sendKeys(comments);
        textbox_ContactDetailsBasicsComments.sendKeys(Keys.TAB);
    }

    public void setWebsite(String website) {
        
        clickWhenClickable(editbox_ContactDetailsBasicsWebsite);
        editbox_ContactDetailsBasicsWebsite.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsBasicsWebsite.sendKeys(Keys.DELETE);
        editbox_ContactDetailsBasicsWebsite.sendKeys(website);
        editbox_ContactDetailsBasicsWebsite.sendKeys(Keys.TAB);
    }

    public String getVendorName() {
        return text_ContactDetailsBasicsVendorName.getText();
    }

    public void setVendorName(String name) {
        
        clickWhenClickable(editbox_ContactDetailVendorName);
        editbox_ContactDetailVendorName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailVendorName.sendKeys(Keys.DELETE);
        editbox_ContactDetailVendorName.sendKeys(name);
        editbox_ContactDetailVendorName.sendKeys(Keys.TAB);
    }

    public void setVendorNumber(String num) {
        
        clickWhenClickable(editbox_ContactDetailVendorNumber);
        editbox_ContactDetailVendorNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailVendorNumber.sendKeys(Keys.DELETE);
        editbox_ContactDetailVendorNumber.sendKeys(num);
        editbox_ContactDetailVendorNumber.sendKeys(Keys.TAB);
    }

    public void setLienholderNumber(String lienNumber) {
        
        clickWhenClickable(editbox_ContactDetailLienholderNumber);
        editbox_ContactDetailLienholderNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailLienholderNumber.sendKeys(Keys.DELETE);
        editbox_ContactDetailLienholderNumber.sendKeys(lienNumber);
        editbox_ContactDetailLienholderNumber.sendKeys(Keys.TAB);
    }

    public void setLienholderTerminationDate(String date) {
        
        clickWhenClickable(editbox_ContactDetailLienholderTerminationDate);
        editbox_ContactDetailLienholderTerminationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailLienholderTerminationDate.sendKeys(Keys.DELETE);
        editbox_ContactDetailLienholderTerminationDate.sendKeys(date);
        editbox_ContactDetailLienholderTerminationDate.sendKeys(Keys.TAB);
    }

    public String clickGenerateLienNumber() {
        if (checkIfElementExists("//a[contains(@id, ':LienholderNumberGenerator')]/span", 1000)) {
            clickWhenClickable(find(By.xpath("//a[contains(@id, ':LienholderNumberGenerator')]/span")));
            waitForPostBack();
            return editbox_ContactDetailLienholderNumber.getAttribute("value");
        } else {
            return "";
        }
    }

    public String getLienNumber() {
    	
    	 if (checkIfElementExists(text_ContactDetailLienholderNumber, 1000)) {
    		 return text_ContactDetailLienholderNumber.getText();
    	 } else {
    		 return "";
    	 }
    }

    public String clickGenerateVendorNumber() {

        if(checkIfElementExists(link_ContactDetailVendorNumberGenerate, 1000)) {
    		clickWhenClickable(link_ContactDetailVendorNumberGenerate);
    		return editbox_ContactDetailVendorNumber.getAttribute("value");
    	} else {
    		addContactDetailsBasicsRole(ContactRole.Vendor);
    		if(checkIfElementExists(link_ContactDetailVendorNumberGenerate, 1000)) {
    			clickWhenClickable(link_ContactDetailVendorNumberGenerate);
                return editbox_ContactDetailVendorNumber.getAttribute("value");
            } else return "";
    		}
    	}

    public void setVendorType(String type) {
    	
    	select_ContactDetailsBasicsClaimVendorType().selectByVisibleText(type);
    	
    }

    public void setBasicsPageAddressAs1099Address(String type) {
    	
    	select_ContactDetailsBasicsAddressType().selectByVisibleText(type);
    	
    }

    public void loginAndSearchAgent(AbUsers abUser, Agents agent) {
        repository.ab.search.SearchAgentSearchAB agentSearch = new SearchAgentSearchAB(driver);
    	agentSearch.loginAndSearchAgent(abUser, agent);
    }
}
