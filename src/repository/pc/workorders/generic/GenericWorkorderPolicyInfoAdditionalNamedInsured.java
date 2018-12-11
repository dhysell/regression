package repository.pc.workorders.generic;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.State;

import repository.gw.addressstandardization.AddressStandardization;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.RelationshipsAB;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.contact.ContactEditPC;
import repository.pc.search.SearchAddressBookPC;

public class GenericWorkorderPolicyInfoAdditionalNamedInsured extends ContactEditPC {

    private WebDriver driver;

    public GenericWorkorderPolicyInfoAdditionalNamedInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//select[contains(@id, 'FBM_PolicyContactDetailsDV:PolicyContactRoleNameInputSet:PolicyContactRoleInputSet:Relationship')]")
    private static WebElement select_SubmissionEditAdditionalNamedInsuredRelationship;

    @FindBy(xpath = "//a[contains(@id, ':ContactRelatedContactsCardTab')]")
    private WebElement tab_RelatedContactsTab;

    @FindBy(xpath = "//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")
    private WebElement link_ReturnToNewAdditionalNamedInsured;

    private Guidewire8Select select_EditRelationship() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Relationship-triggerWrap') or contains(@id, ':RelationTypekey-triggerWrap')]");
    }
    
    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editbox_NewANIFirstName;

    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
    private WebElement editbox_NewANILastName;
    
    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editbox_NewANICompanyName;

    @FindBy(xpath = "//input[contains(@id, 'PCRIdentificationInputSet:InputWithHelpTextInputSet:Input-inputEl')]")
    private WebElement editbox_NewANIAlternateID;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:DateOfBirth-inputEl')]")
    private WebElement editbox_DOB;

    @FindBy(xpath = "//input[contains(@id, ':SSN-inputEl')]")
    private WebElement editbox_SSN;

    @FindBy(xpath = "//input[contains(@id, ':TIN-inputEl')]")
    private WebElement editbox_TIN;
    
    @FindBy(xpath = "//div[contains(@id, ':RelatedContactsPanelSet:')]")
    private WebElement div_RelatedContactsContainer;


    
    
    
    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    
    
    public void createNewContact(boolean basicSearch, PolicyInfoAdditionalNamedInsured ani) throws GuidewireException {

		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		if (ani.getNewContact() == CreateNew.Create_New_Always) {
			findContact(basicSearch, ani);
			waitForPageLoad();
			setContactInfo(ani);
		} else if (ani.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
			if (!findContact(basicSearch, ani)) {
				waitForPageLoad();
				setContactInfo(ani);
			}
		} else {
			findContact(basicSearch, ani);
			
			waitForPageLoad();
			editANIPage.setEditAdditionalNamedInsuredAddressListing(ani.getAddress());
		}
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(ani.getAniDOB());
		editANIPage.setEditAdditionalNamedInsuredAlternateID(ani.getPersonLastName());
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
		editANIPage.setEditAdditionalNamedInsuredRelationship(ani.getRelationshipToPNI());
		editANIPage.setReasonForChange("If you add or change the address, you must supply a Reason for contact Change.");
		// jlarsen 12/12/2016 tab no longer exists in this instance
		if (!finds(By.xpath("//a[contains(@id, ':ContactRelatedContactsCardTab')]")).isEmpty()) {
			editANIPage.clickRelatedContactsTab();
		}
		if(ani.getRelatedContact() != null && ani.getRelationship() != null) {
			addRelatedContact(ani.getRelatedContact(), ani.getRelationship());
		}
		

		editANIPage.clickEditAdditionalNamedInsuredUpdate();
		AddressStandardization standardizeAddress = new AddressStandardization(driver);
		boolean addressStandardPage = standardizeAddress.handleAddressStandardization(ani.getAddress().getLine1());
		if (addressStandardPage) {
			editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
			editANIPage.clickEditAdditionalNamedInsuredUpdate();
		}
		

		if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]"))
				.isEmpty()) {
			clickWhenClickable(find(By.xpath("//a[contains(@id, '__crumb__')]")));
		}
		
		try {
			editANIPage.clickEditAdditionalNamedInsuredUpdate();
		} catch (Exception e) {
		}
		
	}
    
    
    

    public void createAniNoID(boolean basicSearch, PolicyInfoAdditionalNamedInsured ani) throws GuidewireException {

		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		if (ani.getNewContact() == CreateNew.Create_New_Always) {
			findContact(basicSearch, ani);
			setContactInfo(ani);
		} else if (ani.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
			if (!findContact(basicSearch, ani)) {
				setContactInfo(ani);
			}
		} else {
			findContact(basicSearch, ani);
			editANIPage.setEditAdditionalNamedInsuredAddressListing(ani.getAddress());
		}
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(ani.getAniDOB());
		editANIPage.setEditAdditionalNamedInsuredRelationship(ani.getRelationshipToPNI());
		// jlarsen 12/12/2016 tab no longer exists in this instance
		if (!finds(By.xpath("//a[contains(@id, ':ContactRelatedContactsCardTab')]"))
				.isEmpty()) {
			
			editANIPage.clickRelatedContactsTab();
		}
		

		editANIPage.clickEditAdditionalNamedInsuredUpdate();
		AddressStandardization standardizeAddress = new AddressStandardization(driver);
		boolean addressStandardPage = standardizeAddress.handleAddressStandardization(ani.getAddress().getLine1());
		if (addressStandardPage) {
			editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
			editANIPage.clickEditAdditionalNamedInsuredUpdate();
		}
		

		if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
			clickWhenClickable(find(By.xpath("//a[contains(@id, '__crumb__')]")));
		}
		
		try {
			editANIPage.clickEditAdditionalNamedInsuredUpdate();
		} catch (Exception e) {
		}
		
	}
    
    private void setContactInfo(PolicyInfoAdditionalNamedInsured ani) {
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		editANIPage.setEditAdditionalNamedInsuredAddressListing(ani.getAddress());
	}
    
    public boolean findContact(boolean basicSearch, PolicyInfoAdditionalNamedInsured ani) throws GuidewireException {
		repository.pc.search.SearchAddressBookPC searchAddressBookPage = new repository.pc.search.SearchAddressBookPC(driver);
		if (ani.getCompanyOrPerson() == ContactSubType.Person) {
			return searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, ani.getPersonFirstName(),
					ani.getPersonLastName(), ani.getAddress().getLine1(), ani.getAddress().getCity(),
					ani.getAddress().getState(), ani.getAddress().getZip(), ani.getNewContact());
		} else {
			return searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, ani.getCompanyName(),
					ani.getAddress().getLine1(), ani.getAddress().getCity(), ani.getAddress().getState(),
					ani.getAddress().getZip(), ani.getNewContact());
		}
	}
    
    @FindBy(xpath = "//a[contains(@id, 'NamedInsuredsInputSet:NamedInsuredsLV_tb:ToolbarButton')]")
	private WebElement button_SubmissionPolicyInfoAdditionalNamedInsuredsSearch;
    public void clicknPolicyInfoAdditionalNamedInsuredsSearch() {
		waitUntilElementIsClickable(button_SubmissionPolicyInfoAdditionalNamedInsuredsSearch);
		button_SubmissionPolicyInfoAdditionalNamedInsuredsSearch.click();
		
	}
    
    public void addAdditionalNamedInsuredNoStandardizing(boolean basicSearch, PolicyInfoAdditionalNamedInsured ani) throws GuidewireException {
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		
		clicknPolicyInfoAdditionalNamedInsuredsSearch();
		editANIPage.findContact(basicSearch, ani);
		
		editANIPage.setContactInfo(ani);
		editANIPage.setEditAdditionalNamedInsuredRelationship(ani.getRelationshipToPNI());
		
		editANIPage.setEditAdditionalNamedInsuredAlternateID(StringsUtils.generateRandomNumberDigits(12));
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -30));
		editANIPage.setReasonForChange("More Information.");
		editANIPage.clickRelatedContactsTab();
		editANIPage.clickEditAdditionalNamedInsuredUpdate();
	}
    
    public void setName(String firstName, String lastNameOrCompanyName) {
    	waitForPageLoad();
    	if(checkIfElementExists(editbox_NewANIFirstName, 1000)) {
    		setText(editbox_NewANIFirstName, firstName);
    		setText(editbox_NewANILastName, lastNameOrCompanyName);
    	} else {
    		setText(editbox_NewANICompanyName, lastNameOrCompanyName);
    	}
    }
    
    public boolean checkIfNameEditable() {
    	waitForPageLoad();
    	if(!checkIfElementExists(editbox_NewANIFirstName, 5)) {
    		if(!checkIfElementExists(editbox_NewANILastName, 5)){
    			if(!checkIfElementExists(editbox_NewANICompanyName, 5)) {
    				return false;
    			}
    		}
    	} return true;
    }

    public void setEditAdditionalNamedInsuredDateOfBirth(Date dob) {
        if (checkIfElementExists(editbox_DOB, 1000)) {
            clickWhenClickable(editbox_DOB);
            
            editbox_DOB.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            
            editbox_DOB.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dob));
        }
    }


    public void setEditAdditionalNamedInsuredAlternateID(String altID) {
        waitUntilElementIsClickable(editbox_NewANIAlternateID);
        editbox_NewANIAlternateID.click();
        editbox_NewANIAlternateID.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewANIAlternateID.sendKeys(altID);
    }


    public void setEditAdditionalNamedInsuredSSN(String ssn) {
        waitUntilElementIsClickable(editbox_SSN);
        editbox_SSN.click();
        editbox_SSN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SSN.sendKeys(ssn + "");
    }


    public void setEditAdditionalNamedInsuredTIN(String tin) {
        waitUntilElementIsClickable(editbox_TIN);
        editbox_TIN.click();
        editbox_TIN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_TIN.sendKeys(tin);
        clickProductLogo();
    }


    public void setEditAdditionalNamedInsuredRelationship(AdditionalNamedInsuredType relationship) {
        
        Guidewire8Select selectRelationship = select_EditRelationship();
        Long endTime = new Date().getTime() + (15 * 1000);
        do {
            
            selectRelationship.selectByVisibleTextPartial(relationship.getType());
            
        } while (!selectRelationship.getText().equals(relationship.toString()) && new Date().getTime() < endTime);
    }


    public void clickRelatedContactsTab() {
        clickWhenClickable(tab_RelatedContactsTab);
        
    }
    
    public void addRelatedContact(Contact relatedContact, RelationshipsAB relationship ) throws GuidewireException {
    	
   /*	clickAdd();
    	TableUtils tableUtils = new TableUtils(driver);
    	int row = tableUtils.getNextAvailableLineInTable(div_RelatedContactsContainer);
    	tableUtils.selectValueForSelectInTable(div_RelatedContactsContainer, row, "Relationship", relationship.getValue());
    	tableUtils.clickCellInTableByRowAndColumnName(div_RelatedContactsContainer, row, "Related To");
    	if(checkIfElementExists("//div[contains(@id, ':pick:Selectpick')]", 500)) {
    		clickWhenClickable(By.xpath("//div[contains(@id, ':pick:Selectpick')]"));
	    	SearchAddressBookPC searchAddressBookPage = new SearchAddressBookPC(driver);
			if (relatedContact.getPersonOrCompany() == ContactSubType.Person) {
				searchAddressBookPage.searchAddressBookByFirstLastName(true, relatedContact.getFirstName(),
						relatedContact.getLastName(), relatedContact.getAddress().getLine1(), relatedContact.getAddress().getCity(),
						relatedContact.getAddress().getState(), relatedContact.getAddress().getZip(), CreateNew.Do_Not_Create_New);
			} else {
				searchAddressBookPage.searchAddressBookByCompanyName(true, relatedContact.getCompanyName(),
						relatedContact.getAddress().getLine1(), relatedContact.getAddress().getCity(), relatedContact.getAddress().getState(),
						relatedContact.getAddress().getZip(), CreateNew.Do_Not_Create_New);
			}
    	} else {
    		SearchAddressBookPC relateContacts = new SearchAddressBookPC(driver);
    		relateContacts.addRelatedContact(relatedContact, relationship);
    	}
		clickUpdate(); 
	*/ 
    	repository.pc.search.SearchAddressBookPC relateContacts = new SearchAddressBookPC(driver);
    	relateContacts.addRelatedContact(relatedContact, relationship);
    	
    }

    @FindBy(xpath = "//a[contains(@id, 'NewAdditionalNamedInsuredPopup:ContactDetailScreen:NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing:AddressListingMenuIcon')]")
    private static WebElement button_NewAdditionalNamedInsuredAddressListingCreateNew;

    @FindBy(xpath = "//a[contains(@id, 'NewAdditionalNamedInsuredPopup:ContactDetailScreen:NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing:newAddressBuddy-itemEl')]")
    private static WebElement button_NewAdditionalNamedInsuredAddressListingNewLocation;

    public void setEditAdditionalNamedInsuredAddressListing(AddressInfo address) {
    	repository.pc.workorders.generic.GenericWorkorder wo = new GenericWorkorder(driver);
    	wo.setNewAddress(address);
    }
    
    @FindBy(xpath = "//textarea[contains(@id, ':ContactChangeReason_FBM-inputEl')]")
    private static WebElement textArea_NewAdditionalNamedInsuredReasonForContactChange;
    
    public void setReasonForChange(String reason) {
    	if(checkIfElementExists(textArea_NewAdditionalNamedInsuredReasonForContactChange, 1000)) {
    		setText(textArea_NewAdditionalNamedInsuredReasonForContactChange, reason);
    	}
    }


    public void clickEditAdditionalNamedInsuredUpdate() {
        long end = new Date().getTime() + 10000;
        do {
            clickUpdate();
            try {
                
                clickWhenClickable(link_ReturnToNewAdditionalNamedInsured);
                clickUpdate();
                
            } catch (Exception e) {

            }
            
        }
        while (finds(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")).size() > 0 && new Date().getTime() < end);
        
    }

    public void setEditAdditionalNamedInsuredAddressLine1(String address) {
        setContactEditAddressLine1(address);
        
    }

    public void setEditAdditionalNamedInsuredAddressCity(String city) {
        setContactEditAddressCity(city);
        
    }

    public void setEditAdditionalNamedInsuredAddressState(State state) {
        setContactEditAddressState(state);
        
    }

    public void setEditAdditionalNamedInsuredAddressZipCode(String zip) {
        setContactEditAddressZipCode(zip);
        
    }

    public void setEditAdditionalNamedInsuredAddressAddressType(AddressType addType) {
        setContactEditAddressAddressType(addType);
        
    }
}
