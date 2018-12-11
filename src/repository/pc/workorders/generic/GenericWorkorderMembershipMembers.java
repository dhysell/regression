package repository.pc.workorders.generic;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.search.SearchAddressBookPC;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericWorkorderMembershipMembers extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderMembershipMembers(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // elements
    @FindBy(xpath = "//a[contains(@id, ':MembershipMemberCV:BasicMemberTabTab')]")
    private WebElement tab_BasicMembershipRecord;

    @FindBy(xpath = "//a[contains(@id, ':MembershipMemberCV:relatedContactsCardTab')]")
    private WebElement tab_MemberRelatedContacts;
    
    @FindBy(xpath = "//span[contains(@id,'relatedContactsCardTab-btnEl')]")
    private WebElement link_MemberRelatedContacts;

    @FindBy(xpath = "//a[contains(@id, ':MembershipMemberCV:paidDuesCardTab')]")
    private WebElement tab_PaidDues;

    @FindBy(xpath = "//span[contains(@id, ':MembershipMembersPanelSet:ToolbarButton-btnEl')]")
    private WebElement button_MembershipMembersSearch;

    @FindBy(xpath = "//div[contains(@id, ':membershipMembersPanelSet:MembershipMembersPanelSet:0')]")
    private WebElement table_MembershipMembers;

    @FindBy(xpath = "//div[contains(@id, ':MembershipMemberCV:1') or contains(@id, ':MembershipMemberCV:8')]")
    private WebElement table_MembershipMemberRelatedContacts;
    
    private WebElement button_MembershipMemberRelatedContactsChangeRelatedTo(int row) {
    	row = row - 1;
    	return driver.findElement(By.xpath("//a[contains(@id, ':" +row +":ChangeRelatedToButton')]"));
    }
    
    private WebElement button_MembershipMemberRelatedContactsSearchPerson(int row) {
    	row = row - 1;
    	return driver.findElement(By.xpath("//div[contains(@id, ':" +row +":ChangeRelatedToButton:SearchPerson')]"));
    }
    
    
    @FindBy(xpath = "//a[contains(@id, ':EditLink') or contains(@id, ':Edit') or contains(@id, ':editbutton') or contains(@id, 'viewEdit_LinkAsBtn')]")
	private WebElement button_EditButton;
    
    
    @FindBy(xpath = "//a[contains(@id, ':MembershipContactDetailScreen:NewMembershipContactPopup')]")
    private WebElement tab_MemberOKButton;

    @FindBy(xpath = "//div[contains(@id, 'MSPaidDuesPanelSet:paidDuesListLV')]")
    private WebElement table_DuesHistory;

    private Guidewire8Select select_membershipMembersDuesCounty() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':MembershipContactBasicsInputSet:County-triggerWrap')]");
    }

    private Guidewire8Select select_membershipCurrentMemberDuesStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':MembershipContactBasicsInputSet:CurrentDuesStatusInput-triggerWrap')]");
    }

    private Guidewire8Select select_membershipRenewalMemberDuesStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':MembershipContactBasicsInputSet:RenewalDuesStatusInput-triggerWrap')]");
    }

    private Guidewire8Checkbox checkBox_FBQuarterly() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'memberWantsFBQuarterlyDeliveredToThem')]");
    }

    private Guidewire8Checkbox checkBox_GemStateProducer() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'memberWantsGemStateProducerSentToThem')]");
    }

    private Guidewire8Select select_MembershipMemberGender() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PersonNameInputSet:Gender-triggerWrap')]");
    }

    private Guidewire8Select select_MembershipType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':MSPolicyTypeScreen:MSPolicyType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PCRIdentificationInputSet:SSN-inputEl')]")
    private WebElement textField_SSN;

    @FindBy(xpath = "//input[contains(@id, ':PCRIdentificationInputSet:InputWithHelpTextInputSet:Input-inputEl')]")
    private WebElement editbox_AlternateID;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:PersonNameInputSet:DateOfBirth-inputEl')]")
    private WebElement editbox_DateOfBirth;

    private Guidewire8Select select_AddressListing() {
        return new Guidewire8Select(driver, "//table[contains(@id,'FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'OutOfStatePaidDate-inputEl')]")
    private WebElement editbox_OutOfStatePaidDate;

    @FindBy(xpath = "//input[contains(@id, 'membershipInceptionDate-inputEl')]")
    private WebElement editbox_InceptionDate;

    @FindBy(xpath = "//a[contains(@id, 'paidDuesListLV_tb:addFedRecordButton')]")
    private WebElement button_AddDuesHistory;

    @FindBy(xpath = "//a[contains(@id, 'paidDuesListLV_tb:removeThemWithARemoveButton')]")
    private WebElement button_RemoveDuesHistory;

    @FindBy(xpath = "//a[contains(@id, 'MSPaidDuesPanelSet:importPaidDuesButton')]")
    private WebElement button_ViewAvailableRecords;

    @FindBy(xpath = "//span[contains(@id, 'MSPaidDuesPanelSet:ImFedRecordButton-btnWrap')]")
    private WebElement button_ImportDuesHistoryDropdown;

    @FindBy(xpath = "//input[contains(@id, 'paidDuesLV_tb:accountNumberForUpdateFetching-inputEl')]")
    private WebElement editbox_AttachDuesHistoryAccountNumber;

    @FindBy(xpath = "//a[contains(@id, 'paidDuesLV_tb:fetchupdates')]")
    private WebElement button_ImportDuesHistoryFetchUpdates;

    @FindBy(xpath = "//a[contains(@id, 'MSPaidDuesPanelSet:removeImportedRecordsButton')]")
    private WebElement button_RemoveAllImportedDuesHistory;
    
    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
	private WebElement editBox_ContactAddress;
    
    @FindBy(xpath = "//input[contains(@id, ':AddressLine2-inputEl')]")
	private WebElement editBox_ContactAddressLine2;
    
    @FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_ContactCity;
    
    @FindBy(xpath = "//input[contains(@id, ':County-inputEl')]")
	private WebElement editBox_ContactCounty;
    
    private Guidewire8Select select_ContactState() {
 		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
	}
    
    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
	private WebElement editBox_ContactZip;
    
    private Guidewire8Select select_AddressType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
	}

    // methods
    private void setAddress(String addressToFill) {
		clickWhenClickable(editBox_ContactAddress);
		editBox_ContactAddress.sendKeys(Keys.chord(Keys.CONTROL + "a"), addressToFill);
		clickProductLogo();
	}
    
    private void setAddressLine2(String addressToFill) {
		clickWhenClickable(editBox_ContactAddressLine2);
		editBox_ContactAddressLine2.sendKeys(Keys.chord(Keys.CONTROL + "a"), addressToFill);
	}
    
    
    private void setCity(String cityToFill) {
		clickWhenClickable(editBox_ContactCity);
		editBox_ContactCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), cityToFill);
		clickProductLogo();
	}
    
    private void setCounty(String countyToFill) {
		clickWhenClickable(editBox_ContactCounty);
		editBox_ContactCounty.sendKeys(Keys.chord(Keys.CONTROL + "a"), countyToFill);
		clickProductLogo();
	}
    
    private void setState(State stateToSelect) {
		Guidewire8Select commonState = select_ContactState();
		commonState.selectByVisibleText(stateToSelect.getName());
		clickProductLogo();
	}
    
    private void setZip(String zipToFill) {
		clickWhenClickable(editBox_ContactZip);
		editBox_ContactZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipToFill);
		clickProductLogo();
	}
    
    private void setAddressType(AddressType type) {
		Guidewire8Select mySelect = select_AddressType();
		mySelect.selectByVisibleText(type.getValue());
		clickProductLogo();
	}
    
    public void clickMemberRelatedContactsTab() {
        clickWhenClickable(tab_MemberRelatedContacts);
        waitForPageLoad();
        
    }

    public boolean checkIfMemberRelatedContactsTabExists() {
        return checkIfElementExists(tab_MemberRelatedContacts, 1000);
    }

    public void clickBasicMembershipRecordTab() {
        clickWhenClickable(tab_BasicMembershipRecord);
        
    }

    public boolean checkIfBasicMemberhipRecordTabExists() {
        return checkIfElementExists(tab_BasicMembershipRecord, 1000);
    }

    public void clickDuesHistoryTab() {
        clickWhenClickable(tab_PaidDues);
        
    }

    public boolean checkIfDuesHistoryTabExists() {
        return checkIfElementExists(tab_PaidDues, 1000);
    }

    @Override
    public void clickSearch() {
    	if(!button_MembershipMembersSearch.findElements(By.xpath("./ancestor::a[contains(@class, '-disabled')]")).isEmpty()) {
    		Assert.fail("SEARCH BUTTON WAS DISABLED AND IS NOT CLICKABLE.");
    	}
        clickWhenClickable(button_MembershipMembersSearch);
        
    }
    
    public void fillOutAdditionalInfo(Contact member) {
    	selectMembershipMembersDuesCounty(member.getMembershipDuesCounty());
    	selectMembershipMembersCurrentStatus(member.getMembershipDuesCurrentChargeStatus());
    	selectMembershipMembersRenewalStatus(member.getMembershipDuesRenewalChargeStatus());
    }

    public void setMembershipMembers(GeneratePolicy policyObject, boolean buildMembershipListFromScratch) throws Exception {
    	List<Contact> membershipMembersList = new ArrayList<Contact>();
    	if (buildMembershipListFromScratch) {
	        
	        if (policyObject.pniContact.hasMembershipDues()) {
	            membershipMembersList.add(policyObject.pniContact);
	        }
	
	        if (!policyObject.aniList.isEmpty()) {
	            for (PolicyInfoAdditionalNamedInsured ani : policyObject.aniList) {
	                if (ani.hasMembershipDues()) {
	                    membershipMembersList.add(new Contact(ani));
	                }
	            }
	        }
	
	        if (!policyObject.additionalMembersToAddToMembershipList.isEmpty()) {
	            for (Contact additionalMembersToAdd : policyObject.additionalMembersToAddToMembershipList) {
	                membershipMembersList.add(additionalMembersToAdd);
	            }
	        }
	
	        //Set Members list on Membership Object with this complete member list.
	        policyObject.membership.setMembersList(membershipMembersList);
    	}

        int pniCounter = 0;
        for (Contact member : policyObject.membership.getMembersList()) {
            if (member.isContactIsPNI()) {
                pniCounter++;
            }
            if (member.getMembershipDuesCounty() == null) {
                if (CountyIdaho.valueOfName(member.getAddress().getCounty()) != null) {
                    member.setMembershipDuesCounty(CountyIdaho.valueOfName(member.getAddress().getCounty()));
                } else {
                    member.setMembershipDuesCounty(CountyIdaho.valueOfName(policyObject.agentInfo.agentCounty));
                }
            }
        }
        if (pniCounter > 1) {
        	Assert.fail("THE MEMBERSHIP MEMBERS LIST CONTAINS MORE THAN ONE CONTACT SET AS PNI. THERE CAN ONLY BE ONE CONTACT SET AS THE PNI ON A POLICY. PLEASE CORRECT THIS ERROR.");
        } else if (pniCounter == 0) {
        	Assert.fail("THE MEMBERSHIP MEMBERS LIST DOES NOT CONTAIN A CONTACT SET AS PNI. THERE MUST BE ONLY ONE CONTACT SET AS THE PNI ON A POLICY. PLEASE CORRECT THIS ERROR.");
        }

        for (Contact member : policyObject.membership.getMembersList()) {
        	//If the boolean for buildMembershipListFromScratch is false, we can assume that we are not in generate any more and the original setup has been completed already.
        	if (buildMembershipListFromScratch) {
	            if (member.isContactIsPNI()) {
	                // PNI should have been automatically added to table
	                clickEditMembershipMember(member);
	                selectMembershipMemberGender(member.getGender());
                selectMembershipMembersDuesCounty(member.getMembershipDuesCounty());                
	                selectMembershipMemberAddressListing(member.getAddress());
	                fillOutAdditionalInfo(member);
	                
	                clickOK();
	                
	            } else if (!member.getContactRelationshipToPNI().equals(ContactRelationshipToMember.None)) {
	                // edit PNI and add member as a member related contact
	                Contact pniMember = null;
	                for (Contact memberToSearchForPNI : membershipMembersList) {
	                    if (memberToSearchForPNI.isContactIsPNI()) {
	                        pniMember = memberToSearchForPNI;
	                        break;
	                    }
	                }
	                clickEditMembershipMember(pniMember);
	                clickMemberRelatedContactsTab();
	
	                addMembershipDuesRelatedContacts(policyObject.basicSearch, pniMember);
	
	                
	                clickOK();
	                
	            } else {
	                // create new membership record with dues.
	                clickSearch();
                repository.pc.search.SearchAddressBookPC searchPC = new repository.pc.search.SearchAddressBookPC(driver);
	                searchPC.searchForContact(policyObject.basicSearch, member);
	                
	                clickBasicMembershipRecordTab();
	                setMemberDOB(member.getDob());
	                selectMembershipMemberGender(member.getGender());
	                selectMembershipMemberAddressListing(member.getAddress());
	                selectMembershipMembersDuesCounty(member.getMembershipDuesCounty());
	                selectMembershipMembersRenewalStatus(member.getMembershipDuesRenewalChargeStatus());
	                clickOK();
	                
	            }
        	} else {
        		if (checkIfMembershipMemberExistsInTable(member)) {
        			//Do Nothing. Member is already in the table.
        		} else {
		            if (!member.getContactRelationshipToPNI().equals(ContactRelationshipToMember.None)) {
		                // edit PNI and add member as a member related contact
		                Contact pniMember = null;
		                for (Contact memberToSearchForPNI : membershipMembersList) {
		                    if (memberToSearchForPNI.isContactIsPNI()) {
		                        pniMember = memberToSearchForPNI;
		                        break;
		                    }
		                }
		                clickEditMembershipMember(pniMember);
		                clickMemberRelatedContactsTab();
		
		                addMembershipDuesRelatedContacts(policyObject.basicSearch, pniMember);
		
		                
		                clickOK();
		                
		            } else {
		                // create new membership record with dues.
		                clickSearch();
		                repository.pc.search.SearchAddressBookPC searchPC = new repository.pc.search.SearchAddressBookPC(getDriver());
		                searchPC.searchForContact(policyObject.basicSearch, member);
		                
		                clickBasicMembershipRecordTab();
		                setMemberDOB(member.getDob());
		                selectMembershipMemberGender(member.getGender());
		                selectMembershipMemberAddressListing(member.getAddress());
		                selectMembershipMembersDuesCounty(member.getMembershipDuesCounty());
		                selectMembershipMembersRenewalStatus(member.getMembershipDuesRenewalChargeStatus());
		                clickOK();
		                
		            }
        		}
        	}
        }
    }

    public boolean checkIfMembershipMemberExistsInTable (Contact membershipMember) throws Exception {
        String pmName = "";
        if (membershipMember.getPersonOrCompany() == ContactSubType.Person) {
            if (membershipMember.getMiddleName() != null && membershipMember.getMiddleName() != "") {
                pmName = membershipMember.getFirstName() + " " + membershipMember.getMiddleName() + " " + membershipMember.getLastName();
            } else {
                pmName = membershipMember.getFirstName() + " " + membershipMember.getLastName();
            }
        } else if (membershipMember.getPersonOrCompany() == ContactSubType.Company) {
            pmName = membershipMember.getCompanyName();
        }

        waitUntilElementIsVisible(table_MembershipMembers);
        int row = new TableUtils(getDriver()).getRowNumberInTableByText(table_MembershipMembers, pmName);
        if (row == 0) {
            return false;
        } else {
        	return true;
        }      
    }
    
    public void clickEditMembershipMember(Contact membershipMember) throws Exception {
        String pmName = "";
        if (membershipMember.getPersonOrCompany() == ContactSubType.Person) {
            if (membershipMember.getMiddleName() != null && !membershipMember.getMiddleName().equals("")) {
                pmName = membershipMember.getFirstName() + " " + membershipMember.getMiddleName() + " " + membershipMember.getLastName();
            } else {
                pmName = membershipMember.getFirstName() + " " + membershipMember.getLastName();
            }
        } else if (membershipMember.getPersonOrCompany() == ContactSubType.Company) {
            pmName = membershipMember.getCompanyName();
        }

        waitUntilElementIsVisible(table_MembershipMembers);
        int row = tableUtils.getRowNumberInTableByText(table_MembershipMembers, pmName);
        if (row == 0) {
        	Assert.fail("The name: " + pmName + " was not found in the list");
        }
        
        tableUtils.clickLinkInSpecficRowInTable(table_MembershipMembers, row);
    }

    public int getMembershipMembersTableRowCount() {
        return tableUtils.getRowCount(table_MembershipMembers);
    }

    public int getMembershipMembersTableRow(String name) {
        try {
            return tableUtils.getRowNumberInTableByText(table_MembershipMembers, name);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickMembershipMemberRowByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_MembershipMembers, name);
        if (row > 0) {
            tableUtils.clickRowInTableByRowNumber(table_MembershipMembers, row);
        }
        
    }

    private void setMembershipMemberCheckbox(String name) {
        tableUtils.setCheckboxInTable(table_MembershipMembers, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_MembershipMembers, "Name", name)), true);
    }

    public void removeMembershipMember(String pniLastName) {
        setMembershipMemberCheckbox(pniLastName);
        clickRemove();
    }

    public String getMembershipMembersTableCellValue(int rowNumber, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipMembers, rowNumber, columnName);
    }

    public void clickMembershipMembersTableCell(int rowNumber, String columnName) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_MembershipMembers, rowNumber, columnName);
        
    }

    public boolean isExistEditPolicyTransaction() {
        return checkIfElementExists(button_EditButton, 2000);
    }

    public void searchMembershipMember(boolean basicSearch, Contact person) throws Exception {
        clickSearch();
        repository.pc.search.SearchAddressBookPC addressBook = new repository.pc.search.SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, person.getFirstName(), person.getLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
        
    }

    public void setMemberSSN(String withoutdashes) {
        textField_SSN.click();
        
        setText(textField_SSN, withoutdashes);
    }

    public void setMemberAlternateID(String altID) {
        List<WebElement> alternatIDExists = finds(By.xpath("//div[contains(@id, ':InputWithHelpTextInputSet:Input-inputEl')]"));
        if (alternatIDExists.isEmpty()) {
            waitUntilElementIsClickable(editbox_AlternateID);
            editbox_AlternateID.click();
            editbox_AlternateID.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            editbox_AlternateID.sendKeys(altID);
        }
    }

    public void setMemberDOB(Date memberDOB) {
        clickWhenClickable(editbox_DateOfBirth);
        editbox_DateOfBirth.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateOfBirth.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", memberDOB));
    }

    public Date getMemberDOB() {
        Date dateToReturn = getDate(editbox_DateOfBirth);
        return dateToReturn;
    }

    private Date getDate(WebElement editbox_dateOfBirth) {
        Date dateToReturn = null;
        try {
            dateToReturn = DateUtils.convertStringtoDate(editbox_dateOfBirth.getText(), "MM/dd/yyyy");
        } catch (Exception e) {
            try {
                dateToReturn = DateUtils.convertStringtoDate(editbox_dateOfBirth.getAttribute("value"), "MM/dd/yyyy");
            } catch (ParseException e2) {
                Assert.fail("There was an error while attempting to parse the date string.");
            }
        }
        return dateToReturn;
    }

    public void selectMembershipType(MembershipType membershipType) {
        Guidewire8Select selectMembershipType = select_MembershipType();
        selectMembershipType.selectByVisibleTextPartial(membershipType.toString());
    }

    public void selectMembershipMemberGender(Gender gender) {
        Guidewire8Select selectGender = select_MembershipMemberGender();
        selectGender.selectByVisibleText(gender.toString());
    }

    public void selectMembershipMemberAddressListing(String addressLine1) {
        
        Guidewire8Select addressSelect = select_AddressListing();
        
        if (addressLine1 != null) {
            addressSelect.selectByVisibleTextPartial(addressLine1);
        } else {
            addressSelect.selectByVisibleTextRandom();
        }
        
    }

    public void selectMembershipMemberAddressListing(AddressInfo address) throws Exception {
        
        Guidewire8Select addressSelect = select_AddressListing();
        if (address != null) {
            if (addressSelect.isItemInList(address.getLine1())) {
                
                addressSelect.selectByVisibleTextPartial(address.getLine1());
            } else {
                
                List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]"));
                if (!newButton.isEmpty()) {
                    clickWhenClickable(newButton.get(0));
                    clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
                }

                
                setAddress(address.getLine1());
                if (address.getLine2() != null) {
                    setAddressLine2(address.getLine2());
                }
                setCounty(address.getCounty());
                setCity(address.getCity());
                clickProductLogo();
                setState(address.getState());
                
                setZip(address.getZip());
                clickProductLogo();
                

                setAddressType(address.getType());
            }
        } else {
            throw new Exception("ERROR: You must have an address to choose.");
        }
        
    }
    
    
    
    public void addMembershipDuesRelatedContacts(boolean basicSearch, Contact contactToAdd) throws GuidewireException {
    	if(checkIfElementExists(link_MemberRelatedContacts, 1000)) {
    		clickWhenClickable(link_MemberRelatedContacts);
    	}
    	repository.pc.search.SearchAddressBookPC searchRelatedContact = new SearchAddressBookPC(driver);
    	searchRelatedContact.addRelatedContact(contactToAdd, contactToAdd.getRelationshipAB());
    	
    	/*clickAdd();
 	    
 	    int tableRow = tableUtils.getNextAvailableLineInTable(table_MembershipMemberRelatedContacts);
 	    tableUtils.selectValueForSelectInTable(table_MembershipMemberRelatedContacts, tableRow, "Relationship", contactToAdd.getContactRelationshipToPNI().getValue());
 	    if(checkIfElementExists(button_MembershipMemberRelatedContactsChangeRelatedTo(tableRow), 500)) {
 	    	clickWhenClickable(button_MembershipMemberRelatedContactsChangeRelatedTo(tableRow));
 	    	clickWhenClickable(button_MembershipMemberRelatedContactsSearchPerson(tableRow));	
 	    } else {
 	    	tableUtils.clickCellInTableByRowAndColumnName(table_MembershipMemberRelatedContacts, tableRow, "Name");
 	 	    WebElement button_MembershipMemberNamePicker = table_MembershipMemberRelatedContacts.findElement(By.xpath("//div[contains(@id,'MembershipMemberPopup:MembershipMemberCV:" + (tableRow - 1) + ":pick:Selectpick')]"));
 	 	    clickWhenClickable(button_MembershipMemberNamePicker);
 	    }
 	    SearchAddressBookPC searchPC = new SearchAddressBookPC(getDriver());
 	    boolean found = searchPC.searchForContact(basicSearch, contactToAdd);
 	    waitForPostBack();
        if (!found) {
            SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(getDriver());
            createAccountPage.createNewContact(contactToAdd);
        }*/
    }

    public void selectMembershipMembersDuesCounty(CountyIdaho duesCounty) {
        Guidewire8Select myDues = select_membershipMembersDuesCounty();
        myDues.selectByVisibleText(duesCounty.getValue());
    }

    public CountyIdaho getMembershipMembersDuesCounty() {
        Guidewire8Select myDues = select_membershipMembersDuesCounty();
        return CountyIdaho.valueOfName(myDues.getText());
    }

    public void selectMembershipMembersCurrentStatus(MembershipCurrentMemberDuesStatus currentDuesStatus) {
        Guidewire8Select mySelect = select_membershipCurrentMemberDuesStatus();
        mySelect.selectByVisibleText(currentDuesStatus.getValue());
    }

    public void selectMembershipMembersRenewalStatus(MembershipRenewalMemberDuesStatus renewalDuesStatus) {
        Guidewire8Select mySelect = select_membershipRenewalMemberDuesStatus();
        mySelect.selectByVisibleText(renewalDuesStatus.getValue());
    }

    public boolean isFBQuarterlyCheckboxChecked() {
        return checkBox_FBQuarterly().isSelected();
    }

    public void setFBQuarterlyCheckbox(boolean trueFalseChecked) {
        checkBox_FBQuarterly().select(trueFalseChecked);
    }

    public boolean isGemStateProducerCheckboxChecked() {
        return checkBox_GemStateProducer().isSelected();
    }

    public void setGemStateProducerCheckbox(boolean trueFalseChecked) {
        checkBox_GemStateProducer().select(trueFalseChecked);
    }

    public void setOutOfStatePaidDate(Date dob) {
        clickWhenClickable(editbox_OutOfStatePaidDate);
        
        editbox_OutOfStatePaidDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_OutOfStatePaidDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dob));
    }

    public Date getMembershipInceptionDate() {
        Date dateToReturn = getDate(editbox_InceptionDate);
        return dateToReturn;
    }

    public void setMembershipInceptionDate(Date date) {
    	waitUntilElementIsVisible(editbox_InceptionDate);
        
        editbox_InceptionDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_InceptionDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        
    }

    public MembershipCurrentMemberDuesStatus getMembershipMembersCurrentStatus() {
        Guidewire8Select mySelect = select_membershipCurrentMemberDuesStatus();
        return MembershipCurrentMemberDuesStatus.getEnumFromStringValue(mySelect.getText());
    }

    public MembershipRenewalMemberDuesStatus getMembershipMembersRenewalStatus() {
        Guidewire8Select mySelect = select_membershipRenewalMemberDuesStatus();
        return MembershipRenewalMemberDuesStatus.getEnumFromStringValue(mySelect.getText());
    }

    public ContactRelationshipToMember getRelatedContactsRelationshipByName(String name) {
        return ContactRelationshipToMember.getEnumFromStringValue(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipMemberRelatedContacts, tableUtils.getRowNumberInTableByText(table_MembershipMemberRelatedContacts, name), "Relationship"));
    }

    public void removeMembershipRelatedContactsByRow(int row) {
        tableUtils.setCheckboxInTable(table_MembershipMemberRelatedContacts, row, true);
        super.clickRemove();
        
    }

    public void clickAddDuesHistory() {
        clickWhenClickable(button_AddDuesHistory);
        
    }

    public void clickRemoveDuesHistory() {
        clickWhenClickable(button_RemoveDuesHistory);
        
    }

    public void clickViewAvailableRecords() {
        clickWhenClickable(button_ViewAvailableRecords);
        
    }

    public void clickImportDuesHistoryDropdown() {
        clickWhenClickable(button_ImportDuesHistoryDropdown);
        
    }

    public void removeDuesHistoryByYear(String year) {
        removeDuesHistoryByRow(tableUtils.getRowNumberInTableByText(table_DuesHistory, year));
    }

    public void removeDuesHistoryByRow(int row) {
        tableUtils.setCheckboxInTable(table_DuesHistory, row, true);
        
        clickRemoveDuesHistory();
        
    }

    public void addMembershipDuesHistory(Date date, CountyIdaho county) {
        clickAddDuesHistory();
        
        int nextRow = tableUtils.getNextAvailableLineInTable(table_DuesHistory);
        tableUtils.setValueForCellInsideTable(table_DuesHistory, nextRow, "Year", "paidDuesYear", DateUtils.dateFormatAsString("yyyy", date));
        tableUtils.selectValueForSelectInTable(table_DuesHistory, nextRow, "County", county.getValue());
    }

    public int getMembershipDuesHistoryRows() {
        return tableUtils.getRowCount(table_DuesHistory);
    }

    public String getAttachDuesHistoryAccount() {
        return editbox_AttachDuesHistoryAccountNumber.getAttribute("value");
    }

    public void clickImportDuesHistoryFetchUpdates() {
        clickWhenClickable(button_ImportDuesHistoryFetchUpdates);
        
    }

    public void setAttachDuesHistoryAccount(String accountNum) {
        waitUntilElementIsClickable(editbox_AttachDuesHistoryAccountNumber);
        editbox_AttachDuesHistoryAccountNumber.click();
        editbox_AttachDuesHistoryAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AttachDuesHistoryAccountNumber.sendKeys(accountNum);
        
    }

    public void importDuesHistoryByIndividualRecordByName(String name) {
        clickImportDuesHistoryDropdown();
        
        WebElement indElement = find(By.xpath("//span[contains(text(), '" + name + "')]/parent::a[contains(@id, ':individualFedRecord-itemEl')]"));
        clickWhenClickable(indElement);
        
    }

    public void clickRemoveAllImportedDuesHistory() {
        clickWhenClickable(button_RemoveAllImportedDuesHistory);
        
    }
    
    
    public boolean isMemberOKButtonDisabled(){
    	return isElementDisabled(tab_MemberOKButton);
    }
    
    public String getMemberOKButtonTooltip(){
    	return tab_MemberOKButton.getAttribute("data-qtip");
    }
}
