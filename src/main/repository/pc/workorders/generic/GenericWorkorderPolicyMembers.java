package repository.pc.workorders.generic;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import repository.gw.elements.Guidewire8Checkbox;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.LineSelection;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.contact.ContactEditPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderPolicyMembers extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderPolicyMembers(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutPLHouseholdMembersQQ(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        defineHouseHoldMembers(policy);
    }//END fillOutPLHouseholdMembersQQ

    public void updatePLHouseholdMembersFA(GeneratePolicy policy) throws Exception {
        if (policy.squire.isFarmAndRanch()) {
            fillOutPLHouseholdMembersQQ(policy);
        }
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        editHouseHoldMembersFA(policy);
    }//END updatePLHouseholdMembersFA

    public void editHouseHoldMember(Contact policyMember) {
        String pmName = "";
        if (policyMember.getPersonOrCompany() == ContactSubType.Person) {
            if (policyMember.getMiddleName() != null && !policyMember.getMiddleName().equals("")) {
                pmName = policyMember.getFirstName() + " " + policyMember.getMiddleName() + " " + policyMember.getLastName();
            } else {
                pmName = policyMember.getFirstName() + " " + policyMember.getLastName();
            }
        } else if (policyMember.getPersonOrCompany() == ContactSubType.Company) {
            pmName = policyMember.getCompanyName();
        }

        waitUntilElementIsVisible(table_householdMembers);
        edithouseHoldNumber(pmName);
    }

    public void edithouseHoldNumber(String pmName) {
        int row = tableUtils.getRowNumberInTableByText(table_householdMembers, pmName);
        if (row == 0) {
            Assert.fail("The name: " + pmName + " was not found in the list");
        }
        tableUtils.clickLinkInSpecficRowInTable(table_householdMembers, row);
    }

    public void addPolicyMember(Contact member, Contact relatedTo) throws Exception {
    	new SideMenuPC(driver).clickSideMenuHouseholdMembers();
    	clickSearch();
        if (member.isCompany()) {
        	new repository.pc.search.SearchAddressBookPC(driver).searchAddressBookByCompanyName(false, member.getCompanyName(), member.getAddress().getLine1(), member.getAddress().getCity(), member.getAddress().getState(), member.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
        } else if (member.isPerson()) {
        	new repository.pc.search.SearchAddressBookPC(driver).searchAddressBookByFirstLastName(false, member.getFirstName(), member.getLastName(), member.getAddress().getLine1(), member.getAddress().getCity(), member.getAddress().getState(), member.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
        }

        setNewMemberInfoQQ(member, member);
    }


    // elements
    @FindBy(xpath = "//input[contains(@id, 'FBM_PolicyContactDetailsDV:DateOfBirth-inputEl')]")
    private WebElement editbox_birthDate;

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:RatedAge-inputEl')]")
    private WebElement editbox_RatedAge;

    @FindBy(xpath = "//div[contains(@id, ':FBM_PolicyContactDetailsDV:RatedAge-inputEl')]")
    private WebElement readOnly_RatedAge;

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine1-inputEl')]")
    private WebElement editbox_DesignatedAddressLine1;

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine2-inputEl')]")
    private WebElement editbox_DesignatedAddressLine2;

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl')]")
    private WebElement editbox_DesignatedAddressCity;

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:County-inputEl')]")
    private WebElement editbox_DesignatedAddressCounty;

    private String xpath_DesignatedAddressState = "//table[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:State-triggerWrap')]";

    @FindBy(xpath = "//input[contains(@id, ':FBM_PolicyContactDetailsDV:CountryInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement editbox_DesignatedAddressZip;

    private String relationshipToInsuredXpath = "//table[contains(@id, 'ContactPolicyRelationship-triggerWrap')]";

    private String notNewAddressListingXpath = "//table[contains(@id, 'AddressListingForNewContacts-triggerWrap')or contains(@id, 'AddressListingNotForNewContacts-triggerWrap') or contains(@id, 'PolicyContactDetailsDV:AddressListing-triggerWrap') or contains(@id, 'PCRDesignatedAddInputSet:AddressListing-triggerWrap')]";

    @FindBy(xpath = "//a[contains(@id, ':ContactDetailScreen:FinishPCR')]")
    private WebElement button_Update;

    @FindBy(xpath = "//a[contains(@id, ':ContactRelatedContactsCardTab')]")
    private WebElement tab_RelatedContactsTab;

    @FindBy(xpath = "//a[contains(@id, ':PolicyContactRoleDetailCardTab')]")
    private WebElement tab_BasicContactsTab;

    private Guidewire8RadioButton radio_AdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:PolicyMemberAdditionalInsuredInputSet:AdditionalInsured-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_IsThisATrust() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:PolicyMemberAdditionalInsuredInputSet:TrustAddlInsured-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_SectionIAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:PolicyMemberAdditionalInsuredInputSet:Section1AddlInsured-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_SectionIIAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:PolicyMemberAdditionalInsuredInputSet:Section2AddlInsured-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_SectionIIIAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':PolicyMemberAdditionalInsuredInputSet:Section3AddlInsured-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_SectionIVAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':PolicyMemberAdditionalInsuredInputSet:Section4AddlInsured-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, ':PCRIdentificationInputSet:SSN-inputEl')]")
    private WebElement textField_SSN;

    @FindBy(xpath = "//span[contains(@id, 'NewPolicyOtherContactPopup:ContactDetailScreen:PNIUnder18')]")
    private WebElement PNIUnder18;

    @FindBy(xpath = "//input[contains(@id, ':PCRIdentificationInputSet:InputWithHelpTextInputSet:Input-inputEl')]")
    private WebElement editbox_AlternateID;

    @FindBy(xpath = "//a[contains(@id, ':ToolbarButton')]")
    private WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id, ':HouseholdMembersLV_tb:AddContactsButton')]")
    private WebElement button_AddExisting;

    @FindBy(xpath = "//span[contains(text(), 'Existing') and contains(text(), 'Insured')]/parent::a[contains(@id, ':AddContactsButton:AddExistingContact')]")
    private WebElement button_ExistingInsured;

    @FindBy(xpath = "//span[contains(text(), 'From Prefill') and contains(text(), 'Report')]/parent::a[contains(@id, ':AddContactsButton:AddFromPrefill')]")
    private WebElement button_FromPrefillReport;

    @FindBy(xpath = "//a[contains(@id, ':HouseholdMembersLV_tb:Remove')]")
    private WebElement button_Remove;

    @FindBy(xpath = "//div[contains(@id, ':PolicyMemberDetailsDV:HouseholdMembersLV')]")
    private WebElement table_householdMembers;


    @FindBy(xpath = "//span[contains(@id,':ContactRelatedContactsCardTab-btnEl')]")
    private WebElement link_RelatedContactsTab;
    
    @FindBy(xpath = "//div[contains(@id, ':RelatedContactsPanelSet:0')]")
    private WebElement table_PolicyMemberRelatedContacts;
    
    @FindBy(xpath = "//div[contains(@id, ':pick:Selectpick')]")
    private WebElement link_PolicyMemberRelatedContactsSelectPicker;

    public void clickSearch() {
        clickWhenClickable(button_Search);
    }

    private void clickAddExisting() {
        clickWhenClickable(button_AddExisting);
    }

    private void addFromPrefill(String name) {
        String nameXpath = "//span[contains(text(), '" + name + "')]/parent::a[contains(@id, ':AddContactsButton:AddFromPrefill')]";
        clickAddExisting();
        hoverOver(button_FromPrefillReport);
        clickWhenClickable(By.xpath(nameXpath));
    }

    public void addExistingInsured(String name) {
        String nameXpath = "//span[contains(text(), '" + name + "')]/parent::a[contains(@id, 'ExistingAdditionalInterest-itemEl')]";
        clickAddExisting();
        hoverOver(button_ExistingInsured);
        clickWhenClickable(By.xpath(nameXpath));
    }

    public void clickRemoveMember(String pniLastName) {
        tableUtils.setCheckboxInTable(table_householdMembers, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_householdMembers, "Name", pniLastName)), true);
        clickWhenClickable(button_Remove);
        
    }

    private void setNewMemberInfoQQ(Contact person, Contact pniContact) throws Exception {
         GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListingIfNotExist(person.getAddress());
        
        Date dob = householdMember.getDateOfBirth();
        if (person.getDob() != null && dob == null) {
            householdMember.setDateOfBirth(person.getDob());
        }
        
        if(person.getMiddleName() != null) {
        	find(By.xpath("//input[contains(@id, ':MiddleName-inputE')]")).sendKeys(person.getMiddleName());
        }
        
        if ((person.isAdditionalInsured() != null || person.isAdditionalInsured() != null) && (person.isAdditionalInsured().size() != 0)) {
            householdMember.setAdditionalInsured(true);
            
            for (LineSelection line : person.isAdditionalInsured()) {
                if (line.equals(LineSelection.InlandMarineLinePL)) {
                    householdMember.setSectionIVAdditionalInsured(true);
                } else if (line.equals(LineSelection.PersonalAutoLinePL)) {
                    householdMember.setSectionIIIAdditionalInsured(true);
                } else if (line.equals(LineSelection.PropertyAndLiabilityLinePL)) {
                    householdMember.setSectionIAdditionalInsured(true);
                    householdMember.setSectionIIAdditionalInsured(true);
                } else if (line.equals(LineSelection.StandardFirePL)) {
                }
            }
        }

        sendArbitraryKeys(Keys.TAB);
        //Setting the Relationship To Insured and AlternativeID are required fields in QQ
        householdMember.selectRelationshipToInsured(person.getRelationToInsured());
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
//        householdMember.clickRelatedContactsTab();
        repository.pc.search.SearchAddressBookPC searchPC = new repository.pc.search.SearchAddressBookPC(driver);
        clickWhenClickable(link_RelatedContactsTab);
        if(person.getRelationshipAB()!=null) {
        	searchPC.addRelatedContact(pniContact, person.getRelationshipAB());
        }
        
        householdMember.clickOK();
        
        if (!finds(By.xpath("//span[contains(@class, 'g-title') and contains(text(), 'Location Information')]")).isEmpty()) {
            clickWhenClickable(find(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")));
            householdMember = new GenericWorkorderPolicyMembers(driver);
            householdMember.clickOK();
        }
        if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
            clickWhenClickable(find(By.xpath("//a[contains(@id, '__crumb__')]")));
            householdMember = new GenericWorkorderPolicyMembers(driver);
            householdMember.clickOK();
        }
    }

    private void defineHouseHoldMembers(GeneratePolicy policy) throws Exception {
        if (policy.lineSelection.contains(LineSelection.PersonalAutoLinePL) && policy.squire.squirePA.getDriversList().size() > 0) {
            for (Contact driver : policy.squire.squirePA.getDriversList()) {
                if (policy.pniContact.isPerson()) {
                    if (!driver.isContactIsPNI()) {
                        policy.squire.policyMembers.add(driver);
                    }
                }
            }
        }

        List<Contact> policyMemebersList = new ArrayList<Contact>();

        switch (policy.productType) {
            case Businessowners:
                break;
            case CPP:
                break;
            case Membership:
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                policyMemebersList = policy.squire.policyMembers;
                break;
            case StandardFire:
                policyMemebersList = policy.standardFire.getPolicyMembers();
                break;
            case StandardIM:
                policyMemebersList = policy.standardInlandMarine.policyMembers;
                break;
            case StandardLiability:
                policyMemebersList = policy.standardLiability.getPolicyMembers();
                break;
        }

        for (Contact pm : policyMemebersList) {
            if (!pm.isNamedInsured()) {
                repository.pc.search.SearchAddressBookPC addbookSearch = new repository.pc.search.SearchAddressBookPC(driver);

                if (pm.isFromPrefill()) {
                    addFromPrefill(pm.getFirstName());
//                    setNewMemberInfoQQ(pm, policy.pniContact);
                } else {
                    clickSearch();
                    if (policy.pniContact.isCompany()) {
                        addbookSearch.searchAddressBookByCompanyName(policy.basicSearch, pm.getCompanyName(), pm.getAddress().getLine1(), pm.getAddress().getCity(), pm.getAddress().getState(), pm.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                    } else if (policy.pniContact.isPerson()) {
                        addbookSearch.searchAddressBookByFirstLastName(policy.basicSearch, pm.getFirstName(), pm.getLastName(), pm.getAddress().getLine1(), pm.getAddress().getCity(), pm.getAddress().getState(), pm.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                    }
//                    setNewMemberInfoQQ(pm, policy.pniContact);
                }
                setNewMemberInfoQQ(pm, policy.pniContact);
            } else {
                if (pm.isPerson() && pm.isInsuredAlready()) {
                    
                    waitUntilElementIsVisible(table_householdMembers);
                    int row = tableUtils.getRowNumberInTableByText(table_householdMembers, pm.getFirstName().trim());
                    
                    tableUtils.clickLinkInSpecficRowInTable(table_householdMembers, row);
                    
                    GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
                    sendArbitraryKeys(Keys.TAB);
                    waitForPostBack();
                    householdMember.setDateOfBirth(pm.getDob());
                    householdMember.clickRelatedContactsTab();
                    householdMember.clickOK();
                }
            }
            if (!finds(By.xpath("//span[contains(@class, 'g-title') and contains(text(), 'Location Information')]")).isEmpty()) {
                clickWhenClickable(find(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")));
                GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
                householdMember.clickOK();
            }
            if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
                GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
                householdMember.clickOK();
                
            }

            if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
                find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")).click();
                
                GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
                householdMember.clickOK();
                
            }
        }
    }


    private void editHouseHoldMembersFA(GeneratePolicy policy) {
        for (Contact pm : policy.squire.policyMembers) {
            editHouseHoldMember(pm);
            GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
            if (policy.aniList.size() > 0 && pm.getPersonOrCompany() == ContactSubType.Person) {
                householdMember.setDateOfBirth("05/06/1985");
            }
            clickProductLogo();

            householdMember.clickOK();
        }
    }


    public int getPolicyHouseholdMembersTableRowCount() {
        return tableUtils.getRowCount(table_householdMembers);
    }

    public String getPolicyHouseHoldMemberTableCellValue(int rowNumber, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_householdMembers, rowNumber, columnName);
    }

    public void clickPolicyHouseHoldTableCell(int rowNumber, String columnName) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_householdMembers, rowNumber, columnName);
        
    }

    public int getPolicyHouseholdMembersTableRow(String name) {
        try {
            return tableUtils.getRowNumberInTableByText(table_householdMembers, name);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickPolicyHolderMembersByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_householdMembers, name);
        if (row > 0) {
            tableUtils.clickLinkInTableByRowAndColumnName(table_householdMembers, row, "Name");
        }
        
    }

    @FindBy(css = "input[id$=':FirstName-inputEl']")
    private WebElement input_FirstName;
    @FindBy(css = "input[id$=':LastName-inputEl")
    private WebElement input_LastName;
    @FindBy(css = "input[id$=':CompanyName-inputEl']")
    private WebElement input_CompanyName;

    public boolean checkFirstNameEditable() {
        return (checkIfElementExists(input_FirstName, 1000));
    }


    public boolean checkLastNameEditable() {
        return (checkIfElementExists(input_LastName, 1000));
    }


    public boolean checkCompanyNameEditable() {
        return (checkIfElementExists(input_CompanyName, 1000));
    }


    public void setDateOfBirth(String birthDate) {
        clickWhenClickable(editbox_birthDate);
        editbox_birthDate.sendKeys(Keys.CONTROL, "a");
        editbox_birthDate.sendKeys(birthDate);
    }


    public void setDateOfBirth(Date birthDate) {
        clickWhenClickable(editbox_birthDate);
        editbox_birthDate.sendKeys(Keys.CONTROL, "a");
        
        editbox_birthDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", birthDate));
        
        editbox_birthDate.sendKeys(Keys.TAB);
        waitForPostBack();
    }


    public void clickRelatedContactsTab() {
        clickWhenClickable(tab_RelatedContactsTab);
        
    }
    
    public boolean addRelatedContact(Contact relatedContact, RelationshipToInsured relationship) {
    	clickRelatedContactsTab();
    	clickAdd();
    	TableUtils tableUtils = new TableUtils(driver);
    	int currentRow = tableUtils.getNextAvailableLineInTable(table_PolicyMemberRelatedContacts);
    	tableUtils.selectValueForSelectInTable(table_PolicyMemberRelatedContacts, currentRow, "Relationship", relationship.getValue());
    	tableUtils.clickCellInTableByRowAndColumnName(table_PolicyMemberRelatedContacts, currentRow, "Related To");
    	clickWhenClickable(link_PolicyMemberRelatedContactsSelectPicker);
    	repository.pc.search.SearchAddressBookPC searchRelatedContacts = new SearchAddressBookPC(driver);
    	boolean found = searchRelatedContacts.searchAddressBookByFirstLastName(true, relatedContact.getFirstName(), relatedContact.getLastName(), relatedContact.getAddress(), CreateNew.Do_Not_Create_New);  
    	clickBasicsContactsTab();
    	return found;
    }


    public boolean checkRelatedContactsTab() {
        return checkIfElementExists(tab_RelatedContactsTab, 1000);
    }


    public void clickBasicsContactsTab() {
        clickWhenClickable(tab_BasicContactsTab);
        
    }

    public boolean selectRelationshipToInsured(RelationshipToInsured relationship) {
        //This check if exists looks for the guidewire8Select.
        //If not there it is assumed the insured text is present and the Select is not there.
        if (checkIfElementExists(relationshipToInsuredXpath, 1000)) {
            Guidewire8Select relationshipSelect = new Guidewire8Select(driver, relationshipToInsuredXpath);
            relationshipSelect.selectByVisibleText(relationship.getValue());
            try {
                clickWhenClickable(editbox_birthDate);
            } catch (Exception e) {
            }
            return true;
        } else {
        	return false;
        }
    }


    public void selectNotNewAddressListing(String addressLine1) {
        
        Guidewire8Select notNewAddress = new Guidewire8Select(driver, notNewAddressListingXpath);
        
        if (addressLine1 != null) {
            notNewAddress.selectByVisibleTextPartial(addressLine1);
        } else {
            notNewAddress.selectByVisibleTextRandom();
        }
        
    }
    
    public boolean notNewAddressListed(String addressLine1) {
        
        if(checkIfElementExists("//a[contains(@id, ':LinkAddressMenu:LinkAddressMenuMenuIcon')]", 1000)) {
	        repository.pc.contact.ContactEditPC editContact = new repository.pc.contact.ContactEditPC(driver);
	        ArrayList<String> addresses = editContact.getAddressListings();
	        for(String address : addresses) {
	        	if(address.contains(addressLine1)) {
	        		return true;
	        	}
	        } return false;
        } else {
	        Guidewire8Select notNewAddress = new Guidewire8Select(driver, notNewAddressListingXpath);
	        
	        for(String lineItem : notNewAddress.getListItems()) {
	        	if(lineItem.contains(addressLine1)) {
	        		return true;
	        	}
	        } return false;
        }
    }
    
    public void selectNotNewAddressListingIfNotExist(AddressInfo address) throws Exception {
    	boolean foundAddress = false;
//        Guidewire8Select notNewAddress = guidewire8Select(notNewAddressListingXpath);       
        if(!checkIfElementExists("//a[contains(@id, ':AddressListingMenuIcon') or contains(@id, ':LinkAddressMenuMenuIcon')]", 500)) {
	        Guidewire8Select notNewAddress = new Guidewire8Select(driver, "//table[contains(@id, 'AddressListingForNewContacts-triggerWrap')or contains(@id, 'AddressListingNotForNewContacts-triggerWrap') or contains(@id, 'PolicyContactDetailsDV:AddressListing-triggerWrap') or contains(@id, 'PCRDesignatedAddInputSet:AddressListing-triggerWrap')]");
	        if (address != null) {
	            if (notNewAddress.isItemInList(address.getLine1())) {
	                
	                notNewAddress.selectByVisibleTextPartial(address.getLine1());
	            } else {
	                
	                List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]"));
	                if (!newButton.isEmpty()) {
	                    clickWhenClickable(newButton.get(0));
	                    clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
	                }


                    setAddress(address);
	            }
	        } else {
	            throw new Exception("ERROR: You must have an address to choose.");
	        }
        } else {
        	repository.pc.contact.ContactEditPC editContact = new repository.pc.contact.ContactEditPC(driver);
        	if(checkIfElementExists(link_DesignatedAddress, 1)) {
	        	ArrayList<String> uiAddresses = editContact.getAddressListings();
	        	for(String uiAddress : uiAddresses) {
	        		if(uiAddress.contains(address.getLine1())) {
	        			editContact.setContactEditAddressListing(address.getLine1());
	        			foundAddress = true;
	        		}
	        	}
        	}
        	else {
	        	editContact.setNewAddress(address);
        	}
        }
        if(!foundAddress) {
        	repository.pc.contact.ContactEditPC editContact = new ContactEditPC(driver);
        	editContact.setNewAddress(address);
        }
	        
    }

    private void setAddress(AddressInfo address) {
        waitUntilElementIsClickable(editbox_DesignatedAddressLine1);
        editbox_DesignatedAddressLine1.sendKeys(address.getLine1());

        waitUntilElementIsClickable(editbox_DesignatedAddressLine2);
        if (address.getLine2() != null) {
            editbox_DesignatedAddressLine2.sendKeys(address.getLine2());
        }

        waitUntilElementIsClickable(editbox_DesignatedAddressCity);
        editbox_DesignatedAddressCity.sendKeys(address.getCity());

        waitUntilElementIsClickable(editbox_DesignatedAddressCounty);
        editbox_DesignatedAddressCounty.sendKeys(address.getCounty());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();

        Guidewire8Select select_DesignatedAddressState = new Guidewire8Select(driver, xpath_DesignatedAddressState);
        select_DesignatedAddressState.selectByVisibleText(address.getState().getName());


        waitUntilElementIsClickable(editbox_DesignatedAddressZip);
        editbox_DesignatedAddressZip.sendKeys(Keys.CONTROL + "a");
        editbox_DesignatedAddressZip.sendKeys(address.getZip());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]").selectByVisibleText(address.getType().getValue());
    }

    public AddressInfo getDesignatedAddress() {
    	waitUntilElementIsVisible(editbox_DesignatedAddressLine1);
    	String line1 = editbox_DesignatedAddressLine1.getAttribute("value");
    	String line2 = editbox_DesignatedAddressLine2.getAttribute("value");
    	String city = editbox_DesignatedAddressCity.getAttribute("value");
    	Guidewire8Select select_DesignatedAddressState = new Guidewire8Select(driver, xpath_DesignatedAddressState);
        State state = State.valueOfName(select_DesignatedAddressState.getText());
    	String zip = editbox_DesignatedAddressZip.getAttribute("value");
    	String county = editbox_DesignatedAddressCounty.getAttribute("value");
    	AddressType addressType = AddressType.valueOf(new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]").getText());
    	return new AddressInfo(line1, line2, city, state, zip, county, "USA", addressType);
    }


    public void clickOK() {
        new GuidewireHelpers(driver).clickOK();
        if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
        	new GuidewireHelpers(driver).clickOK();
        }

        long endtime = new Date().getTime() + 10000;
        boolean onPage = !finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Policy Members')]")).isEmpty();
        while (!onPage && new Date().getTime() < endtime) {
            
            onPage = !finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Policy Members')]")).isEmpty();
        }
        if (finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
            Assert.assertTrue(onPage, "Did not make it to the Policy Members page in less than 10 seconds after clicking OK");
        }
    }


    public void clickCancel() {
    	new GuidewireHelpers(driver).clickCancel();
        long endtime = new Date().getTime() + 10000;
        boolean onPage = !finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Policy Members')]")).isEmpty();
        while (!onPage && new Date().getTime() < endtime) {
            
            onPage = !finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Policy Members')]")).isEmpty();
        }
        Assert.assertTrue(onPage, "Did not make it to the Policy Members page in less than 10 seconds after clicking Cancel");
    }


    public Date getDateOfBirth() {
        try {
            String dateOfBirth = editbox_birthDate.getAttribute("value");
            return DateUtils.convertStringtoDate(dateOfBirth, "mm/dd/yyyy");
        } catch (ParseException e) {
            return null;
        }
    }

    public String getRatedAge() {
        try {
            return readOnly_RatedAge.getText();
        } catch (Exception e) {
            return editbox_RatedAge.getAttribute("value");
        }
    }


    public boolean getReadOnlyRatedAge() {
        try {
            readOnly_RatedAge.click();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public void clickUpdate() {
        clickWhenClickable(button_Update);
        
    }


    public void setAdditionalInsured(boolean trueFalse) {
        radio_AdditionalInsured().select(trueFalse);
    }


    public void setIsThisATrust(boolean trueFalse) {
        radio_IsThisATrust().select(trueFalse);
    }


    public void setSectionIAdditionalInsured(boolean trueFalse) {
        radio_SectionIAdditionalInsured().select(trueFalse);
    }


    public void setSectionIIAdditionalInsured(boolean trueFalse) {
        
        radio_SectionIIAdditionalInsured().select(trueFalse);
    }


    public void setSectionIIIAdditionalInsured(boolean trueFalse) {
        
        int lcv = 0;
        while (checkIfElementExists("//label[contains(@id,':PolicyMemberAdditionalInsuredInputSet:Section3AddlInsured-labelEl') and contains(., 'Section III - Auto')]", 100) == false && lcv < 100) {
            
            lcv++;
        }
        radio_SectionIIIAdditionalInsured().select(trueFalse);
    }


    private void setSectionIVAdditionalInsured(boolean trueFalse) {
        radio_SectionIVAdditionalInsured().select(trueFalse);
    }


    public void setSSN(String withoutdashes) {
        textField_SSN.click();
        
        setText(textField_SSN, withoutdashes);
    }


    public String getPNIUnder18Validation() {
        return PNIUnder18.getText();
    }

    @FindBy(xpath = "//label[contains(@id,'PolicyMemberAdditionalInsuredInputSet:TrustAddlInsured-labelEl')]")
    private WebElement Text_IsThisATrustQuestion;


    public boolean checkIsThisATrustQuestionExist() {

        return checkIfElementExists(Text_IsThisATrustQuestion, 1000);
    }


    public void setNewPolicyMemberAlternateID(String altID) {
        List<WebElement> alternatIDExists = finds(By.xpath("//div[contains(@id, ':InputWithHelpTextInputSet:Input-inputEl')]"));
        if (alternatIDExists.isEmpty()) {
            waitUntilElementIsClickable(editbox_AlternateID);
            editbox_AlternateID.click();
            editbox_AlternateID.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            editbox_AlternateID.sendKeys(altID);
        }
    }
    @FindBy(xpath = "//a[contains(@id,':FBM_PolicyContactDetailsDV:LinkedAddressFBMInputSet:LinkAddressMenu:LinkAddressMenuMenuIcon')]")
    public WebElement link_DesignatedAddress;

    @FindBy(xpath = "//span[contains(@id, 'FBM_PolicyContactDetailsDV:LinkedAddressFBMInputSet:LinkAddressMenu:') and (text()='New Location') ]")
    public WebElement click_new_DesignatedAddress;
    @FindBy(xpath = "//*[contains(@id,':FBM_PolicyContactDetailsDV:ContactChangeReason_FBM-inputEl')]")
    public WebElement textbox_GenericWorkOrderPolicyInfoContactReasonContactChange;
    public void setDesignatedNewAddress(AddressInfo newAddress) {
        newAddress.setType(AddressType.Mailing);
        clickWhenClickable(link_DesignatedAddress );

        clickWhenClickable(click_new_DesignatedAddress);
        setAddress(newAddress);

        setPolicyInfoContactChangeReason("New mailing Address");
    }
    public void setPolicyInfoContactChangeReason(String reason) {
        waitUntilElementIsVisible(textbox_GenericWorkOrderPolicyInfoContactReasonContactChange);
        textbox_GenericWorkOrderPolicyInfoContactReasonContactChange.click();
        textbox_GenericWorkOrderPolicyInfoContactReasonContactChange.sendKeys(reason);
    }
    public Guidewire8Checkbox checkBoxExcludeAccidentalDeath() {
        return new Guidewire8Checkbox(driver, "//table[contains (@id,':FBM_PolicyContactDetailsDV:accidentalDeathCheckbox')]");
    }

    public void checkBoxExcludeAccidentalDeath(boolean trueFalse) {
        checkBoxExcludeAccidentalDeath().select(trueFalse);
    }

    public void getCheckBoxExcludeAccidentalDeath(boolean trueFalse) {
        checkBoxExcludeAccidentalDeath().isSelected();
    }
    public boolean isExcludeAccidentalDeathExist() {
        try {
            return checkBoxExcludeAccidentalDeath().checkIfElementExists(100);
        } catch (Exception e) {
            return false;
        }

    }


}
