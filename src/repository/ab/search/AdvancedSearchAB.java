package repository.ab.search;


import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.topmenu.TopMenuAB;
import repository.gw.elements.Guidewire8MultiLineSelect;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.TaxReportingOption;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

//This class will also be used when searching in the Select a Contact Page.
public class AdvancedSearchAB extends SearchAB {
    private WebDriver driver;

    public AdvancedSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id, 'ABContactSearch:ABContactSearchScreen:ttlBar')]")
    public WebElement text_AdvancedSearchPageTitle;

    private Guidewire8Select select_AdvancedSearchContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ContactSubtype-triggerWrap')]");
    }

    @FindBy(xpath = "//label[contains(@id, 'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:MembershipNumber-labelEl')]")
    private WebElement label_AdvancedSearchAccountNumber;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:MembershipNumber-inputEl')]")
    private WebElement editbox_AdvancedSearchAccountNumber;

    @FindBy(xpath = "//label[contains(@id,'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:LienholderNumber-labelEl')]")
    private WebElement label_AdvancedSearchLienholderNumber;

    @FindBy(xpath = "//input[contains(@id,':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:LienholderNumber-inputEl')]")
    private WebElement editbox_AdvancedSearchLienholderNumber;

    @FindBy(xpath = "//label[contains(@id, 'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:TaxReportingOption-labelEl')]")
    private WebElement label_AdvancedSearchSearchBySSNTIN;

    @FindBy(xpath = "//label[contains(@id, 'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ClaimVendorNumber-labelEl')]")
    private WebElement label_AdvancedSearchClaimVendorNumber;

    @FindBy(xpath = "//input[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ClaimVendorNumber-inputEl']")
    private WebElement editbox_AdvancedSearchClaimVendorNumber;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:GlobalPersonNameInputSet:LastName-inputEl') or contains(@id, 'ContactNameInputSet:Name-inputEl')]")
    private WebElement editbox_AdvancedSearchContactNameLastName;

    @FindBy(xpath = "//input[contains(@id,':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:GlobalContactNameInputSet:Name-inputEl')]")
    private WebElement editbox_AdvancedSearchContactName;

    @FindBy(xpath = "//label[contains(@id, 'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:GlobalContactNameInputSet:Name-labelEl')]")
    private WebElement label_AdvancedSearchNameLastName;

    private Guidewire8Select select_AdvancedSearchSearchBySSNTIN() {
        return new Guidewire8Select(driver, "//table[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:TaxReportingOption-triggerWrap']");
    }

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:TaxReportingOption-inputEl']")
    private WebElement TaxReportingOption;

    @FindBy(xpath = "//input[contains(@id,':ABContactSearchScreen:ContactSearchDV:AddressSearchInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl')]")
    private WebElement City;

    @FindBy(xpath = "//input[contains(@id,':ABContactSearchScreen:ContactSearchDV:AddressSearchInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement PostalCode;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:AddressSearchInputSet:globalAddressContainer:GlobalAddressInputSet:Country-inputEl']")
    private WebElement Country;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:Tags']")
    private WebElement Tags;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:AllTagsRequired_true-inputEl']")
    private WebElement AllTagsRequired_true;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:AllTagsRequired_false-inputEl']")
    private WebElement AllTagsRequired_false;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:SSN-inputEl']")
    private WebElement SSN;

    @FindBy(xpath = "//*[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:TIN-inputEl']")
    private WebElement TIN;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:GlobalPersonNameInputSet:FirstName-inputEl')]")
    private WebElement FirstName;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:MiddleName-inputEl')]")
    private WebElement MiddleName;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:FormerName-inputEl')]")
    private WebElement FormerName;

    @FindBy(xpath = "//input[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:AlternateName-inputEl')]")
    private WebElement AlternateName;

    private Guidewire8Select comboBox_ContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ContactSubtype-triggerWrap')]");
    }

    @FindBy(xpath = "//label[contains(@id, 'ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ContactSubtype-labelEl')]")
    private WebElement label_AdvancedSearchContactType;

    private Guidewire8Select comboBox_SSNTN() {
        return new Guidewire8Select(driver, "//table[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:TaxReportingOption-triggerWrap']");
    }

    private Guidewire8Select comboBox_State() {
        return new Guidewire8Select(driver, "//table[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:AddressSearchInputSet:globalAddressContainer:GlobalAddressInputSet:State-triggerWrap']");
    }

    private Guidewire8Select comboBox_Country() {
        return new Guidewire8Select(driver, "//table[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:AddressSearchInputSet:globalAddressContainer:GlobalAddressInputSet:Country-triggerWrap']");
    }

    private Guidewire8MultiLineSelect comboBoxMulti_Roles() {
        return new Guidewire8MultiLineSelect(driver, "//div[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:Tags-containerEl')]");
    }

    @FindBy(xpath = "//*[contains(@id, 'ABContactSearch:ABContactSearchScreen:_msgs')]")
    private WebElement text_AdvancedSearchMessages;

    private WebElement button_SelectAContact(String name, String address, String role) {
        return find(By.xpath("//div[contains(., '" + address + "')]/ancestor::tr/td/div[contains(.,'" + name + "')]/ancestor::tr/td/div/a[contains(., 'Select')]"));
        //return find(By.xpath(".//div[contains(., '"+ role +"')]/ancestor::tr/td/div[contains(., '"+address+"')]/ancestor::tr/td/div[contains(.,'"+name+"')]/ancestor::tr/td/div/a[contains(., 'Select')]"));
    }

    @FindBy(xpath = "//*[contains(@id, 'ABContactSearchPopup:ABContactSearchScreen:FBContactSearchResultsLV')]")
    private WebElement div_AdvancedSearchSelectAContactSearchResultsContainer;

    private Guidewire8Select select_CommonContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactType-triggerWrap') or contains(@id, ':ContactSubtype-triggerWrap')]");
    }

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    // Input Methods

    public String getAdvancedSearchPageTitle() {
        waitUntilElementIsVisible(text_AdvancedSearchPageTitle);
        return text_AdvancedSearchPageTitle.getText();
    }

    public void setAdvancedSearchContactType(ContactSubType contactType) {
        if (checkIfElementExists("//table[contains(@id,':ContactSubtype-triggerWrap')]", 1000)) {
            comboBox_ContactType().selectByVisibleText(contactType.getValue());
        }
    }

    /*
        public void setContactSubType(ContactSubType person) {
                        Guidewire8Select mySelect = select_AdvancedSearchContactType();
            mySelect.selectByVisibleText(person.getValue());
        }
    */
    public boolean setRoles(String role) {
    	boolean found = false;
    	if(checkIfElementExists("//div[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:Tags-containerEl')]", 1)) {
    		waitUntilElementIsClickable(By.xpath("//div[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:Tags-containerEl')]"));
    		List<String> itemList = comboBoxMulti_Roles().getListStrings();
    		for(String item : itemList) {
	    		if(item.equals(role)) {
	    			comboBoxMulti_Roles().selectListItemByText(role);
	    			found = true;
	    			return found;
	    		} else {
	    			found = false;
	    			return found;
	    		}
    		}
    	}
		return found;
    }

    /*
     *  public Map<String, ComboBox> getComboBoxes(){ ComboBoxHelper
     * comboBox = new ComboBoxHelper(); return comboBox.getComboBoxes(driver); }
     */

    public void setLastName(String name) {
        clickWhenClickable(editbox_AdvancedSearchContactNameLastName);
        editbox_AdvancedSearchContactNameLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AdvancedSearchContactNameLastName.sendKeys(Keys.DELETE);
        editbox_AdvancedSearchContactNameLastName.sendKeys(name);
        editbox_AdvancedSearchContactNameLastName.sendKeys(Keys.TAB);
    }

    public void setCompanyName(String name) {
        clickWhenClickable(editbox_AdvancedSearchContactName);
        editbox_AdvancedSearchContactName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AdvancedSearchContactName.sendKeys(Keys.DELETE);
        editbox_AdvancedSearchContactName.sendKeys(name);
        editbox_AdvancedSearchContactName.sendKeys(Keys.TAB);
    }

    public void setFirstName(String name) {
        clickWhenClickable(FirstName);
        FirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        FirstName.sendKeys(Keys.DELETE);
        FirstName.sendKeys(name);
        FirstName.sendKeys(Keys.TAB);
    }

    public void setMiddleName(String name) {
        waitUntilElementIsClickable(MiddleName);
        MiddleName.sendKeys(name);
        MiddleName.sendKeys(Keys.TAB);
    }

    public void setFormerName(String name) {
        FormerName.sendKeys(name);
    }

    public void setAlternateName(String name) {
        AlternateName.sendKeys(name);
    }

    public void setTaxReportingOption(TaxReportingOption type) {
        TaxReportingOption.click();
        TaxReportingOption.sendKeys(Keys.HOME, type.getValue(), Keys.TAB);
    }

    public void setSSNNumber(String ssn) {
        waitUntilElementIsVisible(SSN);
        SSN.sendKeys(ssn);
        SSN.sendKeys(Keys.TAB);
    }

    public void setTINNumber(String tin) {
        // setTaxReportingOption(gw.enums.TaxReportingOption.TIN);
        TIN.sendKeys(tin);
    }

    public void setAccountNumber(String accountNumber) {
        waitUntilElementIsClickable(editbox_AdvancedSearchAccountNumber);
        editbox_AdvancedSearchAccountNumber.sendKeys(accountNumber);
    }

    public void setClaimVendorNumber(String claimVendorNumber) {
        editbox_AdvancedSearchClaimVendorNumber.sendKeys(claimVendorNumber);

    }

    public void setLienholderNumber(String lienholderNumber) {
        editbox_AdvancedSearchLienholderNumber.sendKeys(lienholderNumber);
    }

    public void setCity(String city) {
        // waitUntilElementIsVisible(City);
        clickWhenClickable(City);
        City.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        City.sendKeys(city);
        City.sendKeys(Keys.TAB);
    }

    public void setPostalCode(String postalCode) {
        waitUntilElementIsVisible(PostalCode);
        PostalCode.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        PostalCode.sendKeys(postalCode);
        PostalCode.sendKeys(Keys.TAB);
    }

    public void setCountry(String country) {
        Country.sendKeys(country, Keys.TAB);
    }

    public void setSearchBySSNTIN(TaxReportingOption searchBy) {
        //        try {
        comboBox_SSNTN().selectByVisibleText(searchBy.getValue());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void selectRandomComboBox_ContactType() {
        comboBox_ContactType().selectByVisibleTextRandom();

    }

    public void selectRandomComboBox_SSNTN() {
        comboBox_SSNTN().selectByVisibleTextRandom();

    }

    public void selectRandomComboBox_State() {
        comboBox_State().selectByVisibleTextRandom();

    }

    public void selectRandomComboBox_Country() {
        comboBox_Country().selectByVisibleTextRandom();

    }

    public void selectSpecificComboBox_ContactType(String selectString) {
        comboBox_ContactType().selectByVisibleText(selectString);

    }

    public void selectSpecificComboBox_SSNTN(String selectString) {
        comboBox_SSNTN().selectByVisibleText(selectString);

    }

    public void selectSpecificComboBox_State(State specificState) {
        comboBox_State().selectByVisibleText(specificState.getName());
    }

    public void selectSpecificComboBox_Country(String selectString) {
        comboBox_Country().selectByVisibleText(selectString);

    }

    // Click Buttons
    public void clickReset() {
        clickWhenClickable(button_SearchReset);
    }

    public void clickCreateNew() {
    	waitUntilElementIsClickable(By.xpath("//span[contains(@id,'ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV_tb:ClaimContacts_CreateNewContactButton-btnEl')]"));
        clickWhenClickable(button_SearchCreateNew);
    }
    
    public boolean createNewExists() {
    	String createNewXpath = "//span[contains(@id, 'tb:ClaimContacts_CreateNewContactButton-btnEl') and not(@unselectable)]";
    	waitUntilElementIsClickable(By.xpath("//div[contains(@id, ':FBContactSearchResultsLV')]"));
    	return checkIfElementExists(createNewXpath, 100);
    }
    
    public void clickComboBox_State() {
        clickWhenClickable(comboBox_State().getSelectButtonElement());
    }

    public void clickComboBox_Country() {
        clickWhenClickable(comboBox_Country().getSelectButtonElement());
    }

    public boolean clickCreateNewPerson() {
      clickCreateNew();
        if(checkIfElementExists(link_SearchCreateNewPerson, 2000)){
        	waitUntilElementIsClickable(By.xpath("//div[contains(@id, ':ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewPersonMenuItem')]"));
        	clickWhenClickable(link_SearchCreateNewPerson);
        	return true; 
        } else {
        	return false;
        }
    }

    // Click Search Results
    public boolean clickAdvancedSearchPersonSearchResults(String firstName, String lastName, String address) {
        if (link_SearchResultsPerson(firstName, lastName, address).size() > 0) {
            clickWhenClickable(link_SearchResultsPerson(firstName, lastName, address).get(0));
//            gwCommon.new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and contains(@id, ':ABContactDetailScreen:ttlBar')]", 10, "UNABLE TO GET TO CONTACT PAGE AFTER CLICKING THEIR NAME LINK ON THE SEARCH RESULTS PAGE.");
            return true;
        } else {
            return false;
        }

    }
    
    private ArrayList<AdvancedSearchResults> getSearchResults() throws Exception {
    	ArrayList<AdvancedSearchResults> searchResultsToReturn = new ArrayList<AdvancedSearchResults>();
    	TableUtils tableUtils = new TableUtils(driver);
    	int resultsCount = tableUtils.getRowCount(div_SearchAdvancedSearchResultsContainer);
    	for(int i = 1; i <= resultsCount; i++) {
    			searchResultsToReturn.add(setAdvancedSearchResults(i));
    	}
    	return searchResultsToReturn;
    }

    public boolean clickAdvancedSearchCompanySearchResults(String company, String address, State state) {
        if (checkIfElementExists(link_SearchResultsCompany(company, address, state.getName()), 100)) {
            clickWhenClickable(link_SearchResultsCompany(company, address, state.getName()));
            return true;
        } else {
            return false;
        }

    }

    private void clickAdvancedSearchPersonSearchResultsSelect(String firstName, String lastName, String address) {
        clickWhenClickable(link_SearchResultsPersonClickSelect(firstName, lastName, address));
    }

    public void clickAdvancedSearchPersonSearchResultsSelect(String firstName, String middleName, String lastName,
                                                             String address) {
        if (middleName == null || middleName.equals("")) {
            clickAdvancedSearchPersonSearchResultsSelect(firstName, lastName, address);
        } else {
            clickWhenClickable(link_SearchResultsPersonClickSelect(lastName, firstName, middleName, address));
        }
    }

    // Full Search Methods

    public AdvancedSearchResults getSearchResultWithSSN(String lastName) throws Exception {
        clickReset();
        setLastName(lastName);
        clickSearch();
        return clickSearchResultWithSsn();
    }

    public void searchByFirstLastNameSelect(String firstName, String middleName, String lastName, String address) {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Person);
        setLastName(lastName);
        setFirstName(firstName);
        clickSearch();
        clickAdvancedSearchPersonSearchResultsSelect(firstName, middleName, lastName, address);
    }

    public boolean searchByFirstLastName(String firstName, String lastName, String address) throws GuidewireNavigationException {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Person);
        setLastName(lastName);
        setFirstName(firstName);
        clickSearch();
        // waitUntilElementIsVisible(table_SearchSearchResults);
        boolean found = clickAdvancedSearchPersonSearchResults(firstName, lastName, address);
        return found;

    }

    public AdvancedSearchResults getSearchResultsWithSSN(String lastName) throws Exception {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Person);
        setLastName(lastName);
        clickSearch();
        return clickRandomSearchResultWithSSN();
    }

    public AdvancedSearchResults getSearchResultsWithoutSSN(String lastName) throws Exception {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Person);
        setLastName(lastName);
        clickSearch();
        return clickRandomSearchResultWithoutSSN();
    }

    public AdvancedSearchResults searchByFirstNameLastNameAnyAddress(String firstName, String lastName) throws Exception {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Person);
        setLastName(lastName);
        setFirstName(firstName);
        clickSearch();
        return clickRandomAdvancedSearchResult();
    }

    public boolean searchCompanyByName(String companyName, String address, State state) {
        clickReset();
        setAdvancedSearchContactType(ContactSubType.Company);
        setCompanyName(companyName);
        clickSearch();
        boolean found = clickAdvancedSearchCompanySearchResults(companyName, address, state);
        return found;
    }

    public void searchCompanyByNameSelect(String companyName, String address) {
        clickReset();
        if (finds(By
                .xpath("//table[@id='ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ContactSubtype-triggerWrap']"))
                .size() > 0) {
            setAdvancedSearchContactType(ContactSubType.Company);
        }
        setCompanyName(companyName);
        clickSearchResultsSelect(companyName, address);
    }

    public void searchByAccountNumber(String accountNumber) {
        clickReset();
        setAccountNumber(accountNumber);
        clickSearch();
        clickSearchResults();
    }

    public void loginAndSearchByAccountNumber(AbUsers user, String accountNumber, String name) {
        loginAndGetToSearch(user);
        searchByAccountNumberClickName(accountNumber, name);
    }

    public void clickAdvancedSearchAccountNumberSearchResultsSelect(String acctNum) {
        clickAdvancedSearchAccountNumberSearchResultsSelect(acctNum);
    }

    public void searchByAccountNumberClickName(String accountNumber, String name) {
        clickReset();
        setAccountNumber(accountNumber);
        clickSearch();
        table_SearchSearchResults.findElement(By.xpath("//*[text()[contains(., '" + accountNumber + "')]]/ancestor::tr/td/div/a[contains(@id, ':ABContactSearchScreen:FBContactSearchResultsLV') and contains(text(),'" + name + "')]")).click();
    }
    
    public ArrayList<AdvancedSearchResults> searchAccountGetResults(AbUsers user, String accountNumber) throws Exception{
    	 loginAndGetToSearch(user);
    	 clickReset();
         setAccountNumber(accountNumber);
         clickSearch();
         return getSearchResults();
    }

    private void clickSearchResults() {
        table_SearchSearchResults.findElement(By.xpath("//a[(contains(@id, '_Select') or contains(@id, ':DisplayName')) and contains(@id, 'FBContactSearchResults')]")).click();
    }

    public void tinSearch(String tinNumber) {
        clickReset();
        setSearchBySSNTIN(repository.gw.enums.TaxReportingOption.TIN);
        setTINNumber(tinNumber);
        clickSearch();
    }

    public void tinSearch(ContactSubType personCompany, String tinNumber) {
        clickReset();
        setAdvancedSearchContactType(personCompany);
        setSearchBySSNTIN(repository.gw.enums.TaxReportingOption.TIN);
        setTINNumber(tinNumber);
        clickSearch();
    }

    public AdvancedSearchResults searchLienholderNumber(String lienNumber) throws Exception {
        clickReset();
        setLienholderNumber(lienNumber);
        clickSearch();
        return clickAdvancedSearchResult(1);

    }


    // I created an advanced search results object that you may contain the search results to be used later.
    public AdvancedSearchResults getRandomLienholderByName(ContactSubType lienType, String lienholderName) throws Exception {
        clickReset();
        setRoles(ContactRole.Lienholder.getValue());
        if (lienType.equals(ContactSubType.Company)) {
            select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
            setText(input_Name, lienholderName);
        } else {
            select_CommonContactType().selectByVisibleText(ContactSubType.Person.getValue());
            setText(input_LastName, lienholderName);
        }
        clickSearch();
        return clickRandomAdvancedSearchResult();
    }

    public AdvancedSearchResults getClaimParty(String claimParty) throws Exception {
        clickReset();
        setRoles(ContactRole.ClaimParty.getValue());
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, claimParty);
        clickSearch();
        return clickRandomAdvancedSearchResult();
    }

    public AdvancedSearchResults getRandomClaimParty(String claimParty) throws Exception {
        clickReset();
        setRoles(ContactRole.ClaimParty.getValue());
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, claimParty);
        clickSearch();
        return clickRandomAdvancedSearchResult();
    }

    public AdvancedSearchResults getRandomClaimPartyWithPhone(String claimParty) throws Exception {
        clickReset();
        setRoles(ContactRole.ClaimParty.getValue());
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, claimParty);
        clickSearch();
        return clickRandomAdvancedSearchResultWithPhone();
    }

    public AdvancedSearchResults getRandomVendor(String vendor) throws Exception {
        clickReset();
        setRoles(ContactRole.Vendor.getValue());
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, vendor);
        clickSearch();
        AdvancedSearchResults advancedSearchResult = clickRandomAdvancedSearchResult();
        return advancedSearchResult;
    }

    public AdvancedSearchResults getVendor(String vendor, int row) throws Exception {
        clickReset();
        setRoles(ContactRole.Vendor.getValue());
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, vendor);
        clickSearch();
        AdvancedSearchResults advancedSearchResult = clickAdvancedSearchResult(row);
        return advancedSearchResult;
    }

    public void ssnSearch(String ssnNumber) {
        clickReset();
        setSearchBySSNTIN(repository.gw.enums.TaxReportingOption.SSN);
        setSSNNumber(ssnNumber);
        clickSearch();
    }

    public void accountSearch(String acctNum) {
        clickReset();
        setAccountNumber(acctNum);
        clickSearch();
    }

    public ArrayList<String> validateAccountNumberNotAssignedToKid(AbUsers abUser, String acctNumber) {
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab(abUser);
        accountSearch(acctNumber);
        return getSearchResultsNameColumn();
    }

    public List<WebElement> advancedSearchResultsByName(String companyName, String lastName, String firstName,
                                                        String middleName) {
        clickReset();
        if (companyName.isEmpty()) {
            setLastName(lastName);
            setFirstName(firstName);
            setMiddleName(middleName);
        } else {
            setCompanyName(companyName);
        }
        clickSearch();
        List<WebElement> searchResultsName = searchResultsName();
        return searchResultsName;
    }

    public void selectAContactByRoleNameAddress(String name, String address, String role) {
//		setRoles(role);
        if (name.contains(",")) {
            String[] firstLast = name.split(", ");
            setLastName(firstLast[0]);
        } else {
            setCompanyName(name);
        }
        clickSearch();
        waitUntilElementIsClickable(div_AdvancedSearchSelectAContactSearchResultsContainer);
        clickWhenClickable(button_SelectAContact(name, address, role));
    }

    public List<WebElement> checkSearchResultByNameAddress(String lastName, String firstName, String AddressLineOne) {
        return checkSearchResultByNameAddress(lastName, firstName, AddressLineOne);
    }

    public void clickRandomSearchResult() {
        clickSearch();
        getClickableSearchResults()
                .get(NumberUtils.generateRandomNumberInt(0, getClickableSearchResults().size() - 1)).click();
    }

    public AdvancedSearchResults clickRandomAdvancedSearchResultWithPhone(AbUsers abUser, String name) throws Exception {
        loginAndGetToSearch(abUser);
        clickReset();
        select_CommonContactType().selectByVisibleText(ContactSubType.Company.getValue());
        setText(input_Name, name);
        clickSearch();
        return clickRandomAdvancedSearchResultWithPhone();
    }

    public String getSearchMessage() {
        boolean errorMessage = checkIfElementExists(text_AdvancedSearchMessages, 10);
        if (errorMessage) {
            return text_AdvancedSearchMessages.getText();
        } else {
            return "";
        }
    }

    public List<WebElement> getSearchResults(String lienholderNumber) {
        clickReset();
        setLienholderNumber(lienholderNumber);
        clickSearch();
        List<WebElement> searchResults = getSearchResultsRows();
        return searchResults;

    }

    public int getYCoordinate(WebElement label) {
        return label.getLocation().getY();
    }

    public void searchAgentContact(Agents agent) {
        clickReset();
        setRoles("Agent");
        select_CommonContactType().selectByVisibleText(ContactSubType.Person.getValue());
        setFirstName(agent.getAgentFirstName());
        setLastName(agent.getAgentLastName());
        clickSearch();
        clickRandomSearchResult();
    }

    public ContactDetailsBasicsAB loginAndSearchContactByAcct(AbUsers abUser, String acct, String name) {
        loginAndGetToSearch(abUser);
        searchByAccountNumberClickName(acct, name);
        return new ContactDetailsBasicsAB(getDriver());
    }

    public boolean loginAndSearchContact(AbUsers abUser, String firstName, String lastName, String address, State state) throws GuidewireNavigationException {
        loginAndGetToSearch(abUser);
        boolean found = false;
        if (firstName == null || firstName == "" || firstName.equals("")) {
            found = searchCompanyByName(lastName, address, state);
        } else {
            found = searchByFirstLastName(firstName, lastName, address);
        }
        return found;
    }
    
    public boolean multipleResults(AbUsers abUser, String firstName, String lastName, String address, State state) throws Exception {
    	loginAndGetToSearch(abUser);
        clickReset();
        if (firstName == null || firstName == "" || firstName.equals("")) {
            setAdvancedSearchContactType(ContactSubType.Company);
            setCompanyName(lastName);
            clickSearch();
        } else {
        	 clickReset();
             setAdvancedSearchContactType(ContactSubType.Person);
             setLastName(lastName);
             setFirstName(firstName);
             clickSearch();
        }
        ArrayList<AdvancedSearchResults> searchResults = getSearchResults();
        if(searchResults.size()<2) {
        	return false;
        }
        for(int i = 0; i< searchResults.size(); i++) {
        	for(int x = 1; i<searchResults.size(); x++) {
        		if(searchResults.get(i).getLastNameOrCompanyName().contains(searchResults.get(x).getLastNameOrCompanyName())) {
        			if(searchResults.get(i).getAddress().getLine1().contains(searchResults.get(x).getAddress().getLine1())) {
        				return true;
        			}
        		}
        	}
        }
        return false;
    }

    public void loginAndSearchCounty(AbUsers abUser, CountyIdaho county) {
        loginAndGetToSearch(abUser);
        setRoles(ContactRole.County.getValue());
        setLastName(county.getValue());
        clickSearch();
        clickSearchCompanySearchResults(county.getValue(), "", State.Idaho);
    }

    public void selectContactByRoleName(ContactRole role, String contactName) {
        clickReset();
        setRoles(role.getValue());
        setLastName(contactName);
        clickSearch();
        selectContactWithRoleName(role.getValue(), contactName);
    }

    public void selectContactByName(String name) {
        clickReset();
        setLastName(name);
        clickSearch();
        selectContactByName(name);
    }

    public void selectContactByCompanyName(String name) {
        clickReset();
        setCompanyName(name);
        clickSearch();
        selectContactByName(name);
    }

    public boolean findSameNameDifferentAcctNum(String firstName, String lastName, String acctNum) {
        try {
            clickWhenClickable(link_SearchResultsPersonNotAcctNum(firstName, lastName, acctNum));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void loginAndGetToSearch(AbUsers abUser) {
        TopMenuAB getToSearch = new TopMenuAB(getDriver());
        getToSearch.clickSearchTab(abUser);
    }

    public void loginAndSearchNewPerson(AbUsers abUser, String name) {
        loginAndGetToSearch(abUser);
        setLastName(name);
        clickSearch();
    }
    
    public boolean loginAndCreateNewPerson(AbUsers abUser, String name) {
    	loginAndSearchNewPerson(abUser, name);
        return clickCreateNewPerson();
    }
    
    public void loginAndGetRandomContact(AbUsers abUser, String name) {
    	loginAndGetToSearch(abUser);
    	setLastName(name);
    	clickSearch();
    	getRandomSearchResults(name).click();
    }
    
    public boolean searchAndCreateNewPerson(String name) {
    	setLastName(name);
        clickSearch();
        return clickCreateNewPerson();
    }
    
    public void searchAndCreateNewCompany(String name) {
    	setCompanyName(name);
        clickSearch();
        clickCreateNewCompany(); 
    }

    public List<Integer> getYCoordinateNumbering() {
        // Account
        int acctLabelCoord = getYCoordinate(label_AdvancedSearchAccountNumber);
        int acctEditboxCoord = getYCoordinate(editbox_AdvancedSearchAccountNumber);

        // Lienholder
        int lienholderLabelCoord = getYCoordinate(label_AdvancedSearchLienholderNumber);
        int lienholderEditboxCoord = getYCoordinate(editbox_AdvancedSearchLienholderNumber);

        // Search by SSN / TIN
        int ssnLabelCoord = getYCoordinate(label_AdvancedSearchSearchBySSNTIN);
        int ssnComboboxCoord = getYCoordinate(select_AdvancedSearchSearchBySSNTIN().getSelectButtonElement());

        // Claim Vendor #
        int claimLabelCoord = getYCoordinate(label_AdvancedSearchClaimVendorNumber);
        int claimEditboxCoord = getYCoordinate(editbox_AdvancedSearchClaimVendorNumber);

        // Contact Type
        int contactLabelCoord = getYCoordinate(label_AdvancedSearchContactType);
        label_AdvancedSearchContactType.click();
        int contactComboboxCoord = getYCoordinate(comboBox_ContactType().getSelectButtonElement());

        List<Integer> yCoordinates = new ArrayList<Integer>();
        yCoordinates.add(acctLabelCoord);
        yCoordinates.add(acctEditboxCoord);
        yCoordinates.add(lienholderLabelCoord);
        yCoordinates.add(lienholderEditboxCoord);
        yCoordinates.add(ssnLabelCoord);
        yCoordinates.add(ssnComboboxCoord);
        yCoordinates.add(claimLabelCoord);
        yCoordinates.add(claimEditboxCoord);
        yCoordinates.add(contactLabelCoord);
        yCoordinates.add(contactComboboxCoord);

        return yCoordinates;

    }

    public List<Integer> getYCoordinateClaims() {

        // Search by SSN / TIN
        int ssnLabelCoord = getYCoordinate(label_AdvancedSearchSearchBySSNTIN);
        int ssnComboboxCoord = getYCoordinate(select_AdvancedSearchSearchBySSNTIN().getSelectButtonElement());

        // Contact Type
        int contactLabelCoord = getYCoordinate(label_AdvancedSearchContactType);
        int contactComboboxCoord = getYCoordinate(comboBox_ContactType().getSelectButtonElement());

        // Name / LastName
        int nameLabelCoord = getYCoordinate(label_AdvancedSearchNameLastName);
        int nameEditboxCoord = getYCoordinate(editbox_AdvancedSearchContactName);

        // Claim Vendor #
        int claimLabelCoord = getYCoordinate(label_AdvancedSearchClaimVendorNumber);
        int claimEditboxCoord = getYCoordinate(editbox_AdvancedSearchClaimVendorNumber);

        // Account Number
        int acctLabelCoord = getYCoordinate(label_AdvancedSearchAccountNumber);
        int acctEditboxCoord = getYCoordinate(editbox_AdvancedSearchAccountNumber);

        // Lienholder
        int lienholderLabelCoord = getYCoordinate(label_AdvancedSearchLienholderNumber);
        int lienholderEditboxCoord = getYCoordinate(editbox_AdvancedSearchLienholderNumber);

        List<Integer> yCoordinates = new ArrayList<Integer>();
        yCoordinates.add(ssnLabelCoord);
        yCoordinates.add(ssnComboboxCoord);
        yCoordinates.add(contactLabelCoord);
        yCoordinates.add(contactComboboxCoord);
        yCoordinates.add(nameLabelCoord);
        yCoordinates.add(nameEditboxCoord);
        yCoordinates.add(claimLabelCoord);
        yCoordinates.add(claimEditboxCoord);
        yCoordinates.add(acctLabelCoord);
        yCoordinates.add(acctEditboxCoord);
        yCoordinates.add(lienholderLabelCoord);
        yCoordinates.add(lienholderEditboxCoord);

        return yCoordinates;
    }
}
