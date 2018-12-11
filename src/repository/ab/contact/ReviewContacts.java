package repository.ab.contact;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.ab.enums.TaxFilingStatus;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.*;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.MergeContact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimPartyType;
import services.services.com.idfbins.emailphoneupdate.com.guidewire.ab.typekey.PrimaryPhoneType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReviewContacts extends BasePage {
	private WebDriver driver;
	
	MergeContact keep = new MergeContact();
	MergeContact retire = new MergeContact();
	MergeContact updatedContact = new MergeContact();
	
	public ReviewContacts(WebDriver driver) {
		super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//a[contains(@id, 'ReviewDuplicateContactsPopup:__crumb__')]")
    private WebElement link_ReviewContactForMergeReturnToPotentialDuplcateContacts;
    
    @FindBy(xpath = "//span[contains(@id, 'ReviewDuplicateContactsPopup:Update-btnEl')]")
    private WebElement button_ReviewContactForMergeMerge;
    
    @FindBy(xpath = "//span[contains(@id, 'ReviewDuplicateContactsPopup:MergeAndEditButton-btnEl')]")
    private WebElement button_ReviewContactForMergeMergeThenEdit;
    
    @FindBy(xpath = "//span[contains(@id, 'ReviewDuplicateContactsPopup:Ignore-btnEl')]")
    private WebElement button_ReviewContactForMergeIgnore;
    
    @FindBy(xpath = "//span[contains(@id, 'ReviewDuplicateContactsPopup:Cancel-btnEl')]")
    private WebElement button_ReviewContactForMergeCancel;
    
    @FindBy(xpath = "//div[contains(@id, 'ReviewDuplicateContactsPopup:ContactDetailTab:panelId')]")
    private WebElement tableDiv_ReviewContactsForMergeTable;
    
    @FindBy(xpath = "//div[contains(@id, 'ReviewDuplicateContactsPopup:2')]")
    private WebElement tableDiv_ReviewContactsForMergeTable2;
    
    @FindBy(xpath = "//span[contains(@id, ':AddressesTab-btnEl')]")
    private WebElement link_ReviewContactsAddressTab;
    
    @FindBy(xpath = "//span[contains(@id, ':RelatedContactsTab-btnEl')]")
    private WebElement link_ReviewContactsRelatedConacts;
    
    @FindBy(xpath = "//span[contains(@id, ':EFTInformationTab-btnEl')]")
    private WebElement link_ReviewContactsEFTInformation;
    
    @FindBy(xpath = "//span[contains(@id, ':PaidDuesTab-btnEl')]")
    private WebElement link_ReviewContactsPaidDues;
   
    
/*
    private Guidewire8Select select_PotentialDuplicateContactsMatchType() {
        return new Guidewire8Select(
                "//table[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchDV:MatchType-triggerWrap')]");
    }
    
   
    
    private WebElement button_PotentialDuplicateContactsReview(int row) {
    	String xpath = "//a[contains(@id, 'MergeContacts:DuplicateContactPairSearch:DuplicateContactPairSearchLV:"+ row +":Review')]";
    	waitForMe.waitUntilElementIsClickable(By.xpath(xpath));
    	return Configuration.getWebDriver().findElement(By.xpath(xpath));
    }
*/    
    //------------------------------------------------------------------------------------------------------------------------------------------
    // Helper Methods
    //------------------------------------------------------------------------------------------------------------------------------------------
   
    private void clickReturnToPotentialDups() {
    	waitUntilElementIsClickable(link_ReviewContactForMergeReturnToPotentialDuplcateContacts);
    	link_ReviewContactForMergeReturnToPotentialDuplcateContacts.click();
    }
    
    
    public ArrayList<MergeContact> clickMerge() {
    	waitUntilElementIsClickable(button_ReviewContactForMergeMerge);
    	button_ReviewContactForMergeMerge.click();
    	ArrayList<MergeContact> mergedContacts = new ArrayList<>();
    	mergedContacts.add(this.keep);
    	mergedContacts.add(this.retire);
    	mergedContacts.add(this.updatedContact);
    	return mergedContacts;
    }   
   
    private void clickMergeThenEdit() {
    	waitUntilElementIsClickable(button_ReviewContactForMergeMergeThenEdit);
    	button_ReviewContactForMergeMergeThenEdit.click();
    }
    
    private void clickIgnore() {
    	waitUntilElementIsClickable(button_ReviewContactForMergeIgnore);
    	button_ReviewContactForMergeIgnore.click();
    } 
    
    private boolean clickAddresses() {
    	if(checkIfElementExists(link_ReviewContactsAddressTab, 1)) {
    		clickWhenClickable(link_ReviewContactsAddressTab);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private void clickRelatedContacts() {
    	clickWhenClickable(link_ReviewContactsRelatedConacts);
    }
    
    private void clickEFTInfomration() {
    	clickWhenClickable(link_ReviewContactsEFTInformation);
    }
    
    private void clickPaidDues() {
    	clickWhenClickable(link_ReviewContactsPaidDues);
    }
    
    
    
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    // Contacts Tab
    //------------------------------------------------------------------------------------------------------------------------------------------
    
    private ArrayList<String> scrapeValues(String attribute) {
    	ArrayList<String> values = new ArrayList<>();
    	waitUntilElementIsClickable(tableDiv_ReviewContactsForMergeTable2);
    	values.add(tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//descendant::table//tr/td/div[contains(text(), '"+attribute+"')]/ancestor::td/following-sibling::td[contains(@class, '"+getContactGridColumns().get(0)+"')]/div")).getText());
    	values.add(tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//descendant::table//tr/td/div[contains(text(), '"+attribute+"')]/ancestor::td/following-sibling::td[contains(@class, '"+getContactGridColumns().get(1)+"')]/div")).getText());
    	values.add(tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//descendant::table//tr/td/div[contains(text(), '"+attribute+"')]/ancestor::td/following-sibling::td[contains(@class, '"+getContactGridColumns().get(2)+"')]/div")).getText());
    	return values;
    }
        
    private ArrayList<String> getContactGridColumns(){
    	ArrayList<String> gridColumns = new ArrayList<String>();
    	String gridColumn0 = tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//span[contains(text(), 'Kept')]")).getAttribute("id");
    	gridColumn0 = gridColumn0.substring(0, gridColumn0.length()-7);
    	gridColumns.add(gridColumn0);
//    	System.out.println(gridColumns.get(0));
    	
    	String gridColumn1 = tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//span[contains(text(), 'Retired')]")).getAttribute("id");
    	gridColumn1 = gridColumn1.substring(0, gridColumn1.length()-7);
    	gridColumns.add(gridColumn1);
//    	System.out.println(gridColumns.get(1));
    	
    	String gridColumn2 = tableDiv_ReviewContactsForMergeTable2.findElement(By.xpath(".//span[contains(text(), 'Updated Contact')]")).getAttribute("id");
    	gridColumn2 = gridColumn2.substring(0, gridColumn2.length()-7);
    	gridColumns.add(gridColumn2);
//    	System.out.println(gridColumns.get(2));
    	return gridColumns;
    }
    
    private void setCompanyOrPerson() {
    	List<WebElement> contactType = tableDiv_ReviewContactsForMergeTable2.findElements(By.xpath(".//descendant::table/descendant::tr/td/div[contains(text(), 'Person')]"));		
    	if(contactType.size()>0) {
    		this.keep.setIsCompany(false);
    		this.retire.setIsCompany(false);
    		this.updatedContact.setIsCompany(false);
    	} else {
    		this.keep.setIsCompany(true);
    		this.retire.setIsCompany(true);
    		this.updatedContact.setIsCompany(true);
    	}
    }
    
    private void setName() {
    	List<WebElement> contactType = tableDiv_ReviewContactsForMergeTable2.findElements(By.xpath(".//descendant::table/descendant::tr/td/div[contains(text(), 'Person')]"));		
    	if(contactType.size()>0) {
    		ArrayList<String> firstNames =scrapeValues("First name");
    		this.keep.setFirstName(firstNames.get(0));
    		this.retire.setFirstName(firstNames.get(1));
    		this.updatedContact.setFirstName(firstNames.get(2));
    		
    		ArrayList<String> middleNames =scrapeValues("Middle name");
    		this.keep.setMiddleName(middleNames.get(0));
    		this.retire.setMiddleName(middleNames.get(1));
    		this.updatedContact.setMiddleName(middleNames.get(2));
    		
    		ArrayList<String> lastNames =scrapeValues("Last name");
    		this.keep.setLastNameOrCompanyName(lastNames.get(0));
    		this.retire.setLastNameOrCompanyName(lastNames.get(1));
    		this.updatedContact.setLastNameOrCompanyName(lastNames.get(2));
    		
    		ArrayList<String> prefix =scrapeValues("Prefix");
    		if(prefix.get(0).length()>1) {
    			this.keep.setPrefix(PersonPrefix.valueOf(prefix.get(0)));
    		}
    		if(prefix.get(0).length()>1) {
    			this.retire.setPrefix(PersonPrefix.valueOf(prefix.get(1)));
    		}
    		if(prefix.get(0).length()>1) {
    			this.updatedContact.setPrefix(PersonPrefix.valueOf(prefix.get(2)));
    		}
    		
    		ArrayList<String> suffix =scrapeValues("Suffix");
    		if(suffix.get(0).length()>1) {
    			this.keep.setSuffix(Suffix.valueOf(suffix.get(0)));
    		}
    		if(suffix.get(0).length()>1) {
    			this.retire.setSuffix(Suffix.valueOf(suffix.get(1)));
    		}
    		if(suffix.get(0).length()>1) {
    			this.updatedContact.setSuffix(Suffix.valueOf(suffix.get(2)));
    		}
    		
    		ArrayList<String> formerNames =scrapeValues("Former name");
    		this.keep.setFormerName(formerNames.get(0));
    		this.retire.setFormerName(formerNames.get(1));
    		this.updatedContact.setFormerName(formerNames.get(2));
    			
    	} else {
    		ArrayList<String> names =scrapeValues("Name");
    		this.keep.setLastNameOrCompanyName(names.get(0));
    		this.retire.setLastNameOrCompanyName(names.get(1));
    		this.updatedContact.setLastNameOrCompanyName(names.get(2));
    	}
    }
    
    private void setRoles() {
    	ArrayList<String> roles =scrapeValues("Tags");
    	ArrayList<ContactRole> tags = new ArrayList<>();
    	
    	for(int i = 0; i<roles.size(); i++) {
    		if(roles.get(i).trim().contains(",")) {
    			String[] roleArray = roles.get(i).split(",");
    			for(String role: roleArray) {
    				tags.add(ContactRole.valueOf(role.trim()));
    			}
    			if(i==0) {
    				this.keep.setRoles(tags);
    				tags.clear();
    			} else if(i==1) {
    				this.retire.setRoles(tags);
    				tags.clear();
    			} else {
    				this.updatedContact.setRoles(tags);
    				tags.clear();
    			}    			
    		}
    	}
	}
    
    private void setPhoneNumbers() {
    	ArrayList<String> workPhone =scrapeValues("Work");
		this.keep.setWork(workPhone.get(0));
		this.retire.setWork(workPhone.get(1));
		this.updatedContact.setWork(workPhone.get(2));
		
		ArrayList<String> homePhone =scrapeValues("Home");
		this.keep.setHome(homePhone.get(0));
		this.retire.setHome(homePhone.get(1));
		this.updatedContact.setHome(homePhone.get(2));
		
		ArrayList<String> mobilePhone =scrapeValues("Mobile");
		this.keep.setMobile(mobilePhone.get(0));
		this.retire.setMobile(mobilePhone.get(1));
		this.updatedContact.setMobile(mobilePhone.get(2));
		
		ArrayList<String> fax =scrapeValues("Fax");
		this.keep.setMobile(fax.get(0));
		this.retire.setMobile(fax.get(1));
		this.updatedContact.setMobile(fax.get(2));
		
		ArrayList<String> primaryPhone =scrapeValues("Primary phone");
		if(primaryPhone.get(0).length()>1) {
			this.keep.setPrimaryPhone(PrimaryPhoneType.valueOf(primaryPhone.get(0)));
		}
		if(primaryPhone.get(1).length()>1) {
			this.retire.setPrimaryPhone(PrimaryPhoneType.valueOf(primaryPhone.get(1)));
		}
		if(primaryPhone.get(2).length()>1 && !primaryPhone.get(2).contains("<none>")) {
			this.updatedContact.setPrimaryPhone(PrimaryPhoneType.valueOf(primaryPhone.get(2)));
		}
    	
    }
    
    private void setEmail() {
    	ArrayList<String> mainEmail =scrapeValues("Main Email");
		this.keep.setEmail(mainEmail.get(0));
		this.retire.setEmail(mainEmail.get(1));
		this.updatedContact.setEmail(mainEmail.get(2));
		
		ArrayList<String> altEmail =scrapeValues("Alternate Email");
		this.keep.setAltEmail(altEmail.get(0));
		this.retire.setAltEmail(altEmail.get(1));
		this.updatedContact.setAltEmail(altEmail.get(2));
    }
    
    private void setSSN() {
    	ArrayList<String> ssnArray =scrapeValues("SSN");
    	if(ssnArray.get(0).matches("^[0-9]{9}$")) {
    		this.keep.setSSN(ssnArray.get(0));
    	}
    	if(ssnArray.get(1).matches("^[0-9]{9}$")) {
    		this.retire.setSSN(ssnArray.get(1));
    	}
    	if(ssnArray.get(2).matches("^[0-9]{9}$")) {
    		this.updatedContact.setSSN(ssnArray.get(2));
    	}
    }
    
    private void setTaxFilingStatus() {
    	ArrayList<String> taxFilingStatusArray =scrapeValues("Tax Filing Status");
    	if(taxFilingStatusArray.get(0).length()>5) {
    		this.keep.setTaxFilingStatus(TaxFilingStatus.valueOf(taxFilingStatusArray.get(0)));
    	}
    	if(taxFilingStatusArray.get(1).length()>5) {
    		this.retire.setTaxFilingStatus(TaxFilingStatus.valueOf(taxFilingStatusArray.get(1)));
    	}
    	if(taxFilingStatusArray.get(2).length()>5 && !taxFilingStatusArray.get(2).contains("<none>")) {
    		this.updatedContact.setTaxFilingStatus(TaxFilingStatus.valueOf(taxFilingStatusArray.get(2)));
    	}
    }
    
    private void setDateOfBirth() throws ParseException {
    	ArrayList<String> array =scrapeValues("Date of Birth");
    	if(array.get(0).length()>6) {
    		this.keep.setDateOfBirth(DateUtils.convertStringtoDate(array.get(0), "MM/dd/yyyy"));
    	}
    	if(array.get(1).length()>6) {
    		this.retire.setDateOfBirth(DateUtils.convertStringtoDate(array.get(1), "MM/dd/yyyy"));
    	}
    	if(array.get(2).length()>6) {
    		this.updatedContact.setDateOfBirth(DateUtils.convertStringtoDate(array.get(2), "MM/dd/yyyy"));
    	}
    }	

	public void setGender() throws Exception {
		ArrayList<String> array =scrapeValues("Gender");
    	if(array.get(0).length()>2) {
    		this.keep.setGender(Gender.valueOfString(array.get(0)));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setGender(Gender.valueOfString(array.get(1)));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setGender(Gender.valueOfString(array.get(2)));
    	}
	}
		
	public void setMaritalStatus() {
		ArrayList<String> array =scrapeValues("Marital status");
    	if(array.get(0).length()>2) {
    		this.keep.setMaritalStatus(MaritalStatus.valueOf(array.get(0)));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setMaritalStatus(MaritalStatus.valueOf(array.get(1)));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setMaritalStatus(MaritalStatus.valueOf(array.get(2)));
    	}
	}

	public void setCurrency() {
		ArrayList<String> array =scrapeValues("Preferred Currency");
    	if(array.get(0).length()>3) {
    		this.keep.setCurrency(Currency.valueOf(array.get(0)));
    	}
    	if(array.get(1).length()>3) {
    		this.retire.setCurrency(Currency.valueOf(array.get(1)));
    	}
    	if(array.get(2).length()>3 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setCurrency(Currency.valueOf(array.get(2)));
    	}
	}
	
	public void setOccupation() {
		ArrayList<String> array =scrapeValues("Occupation");
    	if(array.get(0).length()>2) {
    		this.keep.setOccupation(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setOccupation(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setOccupation(array.get(2));
    	}
	}

	public void setDLNumber() {
		ArrayList<String> array =scrapeValues("Number");
    	if(array.get(0).length()>2) {
    		this.keep.setDLNumber(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setDLNumber(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setDLNumber(array.get(2));
    	}
	}
	
	public void setDLState() {
		ArrayList<String> array =scrapeValues("State");
		if(array.get(0).length()>1) {
    		this.keep.setState(State.valueOfName(array.get(0)));
    	}
    	if(array.get(1).length()>1) {
    		this.retire.setState(State.valueOfName(array.get(1)));
    	}
    	if(array.get(2).length()>1 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setState(State.valueOfName(array.get(2)));
    	}
	}

	public void setNotes() {
		ArrayList<String> array =scrapeValues("Notes");
    	if(array.get(0).length()>2) {
    		this.keep.setNotes(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setNotes(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setNotes(array.get(2));
    	}
	}
	
	public void setAccountNumber() {
		ArrayList<String> array =scrapeValues("Account Number");
    	if(array.get(0).length()>2) {
    		this.keep.setAccountNumber(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setAccountNumber(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setAccountNumber(array.get(2));
    	}
	}
		
	public void setMembershipType() {
		ArrayList<String> array =scrapeValues("Membership Type");
    	if(array.get(0).length()>2) {
    		this.keep.setMembershipType(MembershipType.getEnumFromStringValue(array.get(0)));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setMembershipType(MembershipType.getEnumFromStringValue(array.get(1)));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setAccountNumber(array.get(2));
    	}
	}
		
	public void setClaimPartyType() {
		ArrayList<String> array =scrapeValues("Claim Party Type");
    	if(array.get(0).length()>2) {
    		this.keep.setClaimPartyType(ClaimPartyType.fromValue(array.get(0)));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setClaimPartyType(ClaimPartyType.fromValue(array.get(1)));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setClaimPartyType(ClaimPartyType.fromValue(array.get(2)));
    	}
	}
	
	public void setFarmBureauAgent() throws Exception {
		ArrayList<String> array =scrapeValues("Farm Bureau Agent");
    	if(array.get(0).length()>2) {
    		String[] agent = array.get(0).split(",");
    		this.keep.setFarmBureauAgent(AgentsHelper.getAgentByAgentNumber(agent[0]));
    	}
    	if(array.get(1).length()>2) {
    		String[] agent = array.get(1).split(",");
    		this.retire.setFarmBureauAgent(AgentsHelper.getAgentByAgentNumber(agent[0]));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		String[] agent = array.get(2).split(",");
    		this.updatedContact.setFarmBureauAgent(AgentsHelper.getAgentByAgentNumber(agent[0]));
    	}
	}
		
	public void setWebsite() {
		ArrayList<String> array =scrapeValues("Website");
    	if(array.get(0).length()>2) {
    		this.keep.setWebsite(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setWebsite(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setWebsite(array.get(2));
    	}

	}
		
	public void setCancelled() {
		ArrayList<String> array =scrapeValues("Cancelled");
		if(array.get(0).contains("No")) {
			this.keep.setCancelled(false);
		} else {
			this.keep.setCancelled(true);
		}
		if(array.get(1).contains("Yes")) {
			this.retire.setCancelled(true);
		} else {
			this.retire.setCancelled(false);
		}
		if(array.get(2).contains("No")) {
			this.updatedContact.setCancelled(false);
		} else {
			this.updatedContact.setCancelled(true);
		}
	}
	
	public void setLienholderNumber() {
		ArrayList<String> array =scrapeValues("Lienholder Number");
    	if(array.get(0).length()>2) {
    		this.keep.setLienNumber(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setLienNumber(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setLienNumber(array.get(2));
    	}
	}
	
	public void setPublicID() {
		
		ArrayList<String> array =scrapeValues("Public ID");
    	if(array.get(0).length()>2) {
    		this.keep.setPublicID(array.get(0));
    	}
    	if(array.get(1).length()>2) {
    		this.retire.setPublicID(array.get(1));
    	}
    	if(array.get(2).length()>2 && !array.get(2).contains("<none>")) {
    		this.updatedContact.setPublicID(array.get(2));
    	}
	}
	
    // ------------------------------------
    // Addresses Tab
    // ------------------------------------
	
	 @FindBy(xpath = "//div[contains(@id, 'ReviewDuplicateContactsPopup:5')]")
	    private WebElement tableDiv_ReviewContactForMergeAddressesKeep;
	 
	 @FindBy(xpath = "//div[contains(@id, 'ReviewDuplicateContactsPopup:7')]")
	    private WebElement tableDiv_ReviewContactForMergeAddressesRetire;
	 
	 
	
	private void setDuplicateAddress(ArrayList<String> keepAddresses) {
		
		WebElement tableDivKeep = tableDiv_ReviewContactForMergeAddressesKeep;
		WebElement tableDivRetire = tableDiv_ReviewContactForMergeAddressesRetire;
				
		TableUtils tableHelp = new TableUtils(driver);
		
		for(int i = 1; i<=tableHelp.getRowCount(tableDivRetire); i++) {
			String retiredAddress = tableHelp.getCellTextInTableByRowAndColumnName(tableDivRetire, i, "Address");
			for(String keepAddress : keepAddresses) {
				if(keepAddress.contains(retiredAddress)) {
					tableHelp.selectValueForSelectInTable(tableDivRetire, i, "Duplicate Address", "None");
				} else {
					tableHelp.setCheckboxInTable(tableDivRetire, i, true);
				}
			}
		}
	}
	
	private AddressInfo parseAddressString(String address) {
		String[] parsedAddress = address.split(",");
		String[] parsedState = parsedAddress[2].split(" ");
		return new AddressInfo(parsedAddress[0].trim(), parsedAddress[1].trim(), State.valueOfAbbreviation(parsedState[0].trim()), parsedState[1].trim());
		
	}
	
	public ArrayList<AddressInfo> setAddressInfo() {
		clickAddresses();
		TableUtils tableHelp = new TableUtils(driver);
		ArrayList<AddressInfo> keepAddresses = new ArrayList<>();
		ArrayList<AddressInfo> retireAddresses = new ArrayList<>();
		
		ArrayList<String> keepAddressesString = tableHelp.getAllCellTextFromSpecificColumn(tableDiv_ReviewContactForMergeAddressesKeep, "Address");
		for(String address : keepAddressesString) {
			keepAddresses.add(parseAddressString(address));
		}
		this.keep.setAddressInfo(keepAddresses);
		
		ArrayList<String> retireAddressesString = tableHelp.getAllCellTextFromSpecificColumn(tableDiv_ReviewContactForMergeAddressesKeep, "Address");
		for(String address : retireAddressesString) {
			retireAddresses.add(parseAddressString(address));
		}
		this.retire.setAddressInfo(retireAddresses);
		
		setDuplicateAddress(keepAddressesString);
		
		return keepAddresses;		
	}
	
	    
    // ------------------------------------
    // Related Contacts Tab
    // ------------------------------------
	
	
	public void setRelatedContacts() {
		
	}
	    
    
    // ------------------------------------
    // EFT Information
    // ------------------------------------
     
	
	public void setEFT() {
		
	}
    
    public void setMergedContactObjects() throws Exception {
    	setCompanyOrPerson();
    	setName();
//    	setRoles();
    	//differs between company and person Companies don't have a home phone.
//    	setPhoneNumbers();
    	setEmail();
//    	setSSN();
//    	setTaxFilingStatus();
//    	setDateOfBirth();
//    	setGender();
//    	setMaritalStatus();
//    	setCurrency();
//    	setOccupation();
//   	setDLNumber();
//    	setDLState();
    	setNotes();
    	setAccountNumber();
//    	setMembershipType();
//    	setClaimPartyType();
//    	setFarmBureauAgent();
    	setWebsite();
    	setCancelled();
//    	setLienholderNumber();
    	setPublicID();
    }
    
    // ------------------------------------
    // Dues
    // ------------------------------------
}
