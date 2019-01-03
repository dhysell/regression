package repository.pc.search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.PC_Users;
import repository.gw.enums.CreateNew;
import repository.gw.enums.TaxReportingOption;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;



public class SearchContactsPC extends SearchAddressBookPC{
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public SearchContactsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }
    
    @FindBy(xpath = "//div[contains(@id, ':ContactSearchLV_FBMPanelSet:1') and not(contains(@id, '-body'))]")
    private WebElement link_SearchContactsSearchResults;
    
/*    
    private Guidewire8Select select_SearchContactsPCContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactType-triggerWrap') or contains(@id, ':ContactSubtype-triggerWrap')]");
    }
    	
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:Keyword-inputEl')]")
    private WebElement editbox_SearchContactsName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:Keyword-inputEl')]")
    private WebElement editbox_SearchContactsLastName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:FirstName-inputEl')]")
    private WebElement editbox_SearchContactsFirstName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:MiddleName-inputEl')]")
    private WebElement editbox_SearchContactsMiddleName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:FormerName-inputEl')]")
    private WebElement editbox_SearchContactsFormerName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:AlternateName-inputEl')]")
    private WebElement editbox_SearchContactsAlternateName;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:SSN-inputEl')]")
    private WebElement editbox_SearchContactsSSN;
    
    @FindBy(xpath = "//input[contains(@id, ':ContactSearchScreen:TIN-inputEl')]")
    private WebElement editbox_SearchContactsTIN;
    
    //Methods
    
    private void setContactType(ContactSubType type) {
    	waitForPageLoad();
    	select_SearchContactsPCContactType().selectByVisibleText(type.getValue());
    	waitForPostBack();
    }
    
    private void setContactCompanyName(String companyName) {
    	setText(editbox_SearchContactsName, companyName);
    }
     
    private void setContactLastName(String lastName) {
    	setText(editbox_SearchContactsLastName, lastName);
    }
    
    private void setContactFirstName(String firstName) {
    	setText(editbox_SearchContactsFirstName, firstName);
    }
    
    private void setContactMiddleName(String middleName) {
    	setText(editbox_SearchContactsMiddleName, middleName);
    }
    
    private void setContactformerName(String formerName) {
    	setText(editbox_SearchContactsFormerName, formerName);
    }
    
    private void setContactAltName(String altName) {
    	setText(editbox_SearchContactsAlternateName, altName);
    }
    
    private void setSSNTIN(String socialSecurityNumber, TaxReportingOption ssn) {
        new Guidewire8Select(driver, "//table[contains(@id,':TaxReportingOption-triggerWrap')]").selectByVisibleText(ssn.getValue());
        if (ssn.equals(TaxReportingOption.TIN)) {
            setText(editbox_SearchContactsTIN, socialSecurityNumber);
        } else if (ssn.equals(TaxReportingOption.SSN)) {
            setText(editbox_SearchContactsSSN, socialSecurityNumber);
        }
    }
 */   
    public void getToContactSearch(PC_Users user) {
 		new Login(driver).login(user.getUserName(), user.getUserName());
 		SearchSidebarPC searchSideMenu = new SearchSidebarPC(driver);
 		searchSideMenu.clickContacts();
    }
   
    public void searchSSN(String ssn) {
    	searchAddressBookBySSNTIN(true, ssn, TaxReportingOption.SSN, CreateNew.Do_Not_Create_New);
    }
    
    public void searchTIN(String tin) {
    	searchAddressBookBySSNTIN(true, null, TaxReportingOption.TIN, CreateNew.Do_Not_Create_New);
    }
    
    public void searchCompany(String companyName, AddressInfo address) {
    	searchAddressBookByCompanyName(true, companyName, address.getLine1(), address.getCity(), address.getState(), address.getPostalCode(), CreateNew.Do_Not_Create_New);
    }
    
    public boolean searchPerson(String firstName, String lastName, AddressInfo address) {
    	boolean found = searchAddressBookByFirstLastName(true, firstName, lastName, address, CreateNew.Do_Not_Create_New);
    	if(found) {
    		clickWhenClickable(link_SearchContactsSearchResults.findElements(By.xpath(".//a[contains(., '"+lastName+"') and contains(.,'"+firstName+"')]")).get(0));
    		return true;
    	} else {
    		return false;
    	}
    }  
}
