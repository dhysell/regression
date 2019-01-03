package repository.pc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Select;

public class SearchAddressBookPC_basics extends SearchAddressBookPC {
	private WebDriver driver;

    public SearchAddressBookPC_basics(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    
    @FindBy(xpath = "//span[contains(@id, ':solrSearchTab-btnEl')]")
    private WebElement link_SearchAddressBookPC_basicsBasic;
    					
    @FindBy(xpath = "//span[contains(@id, ':dbSearchTab-btnEl')]")
    private WebElement link_SearchAddressBookPC_basicsAdvanced;
    
    @FindBy(xpath = "//input[contains(@id, ':searchName-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCName;
    							//FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SSN-inputEl
    @FindBy(xpath = "//input[contains(@id, ':SSN-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCssn;
    
    @FindBy(xpath = "//input[contains(@id, ':TIN-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCtin;
    
    @FindBy(xpath = "//input[contains(@id, ':searchPhone-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCPhone;

    @FindBy(xpath = "//input[contains(@id, ':searchStreet-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCStreet;
    
    @FindBy(xpath = "//input[contains(@id, ':searchCity-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCCity;
    
    private Guidewire8Select select_SearchAddressBookBasicPCState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }
    
    @FindBy(xpath = "//input[contains(@id, ':searchPostalCode-inputEl')]")
    private WebElement input_SearchAddressBookBasicsPCZipCode;
    
    @FindBy(xpath = "//div[contains(@id, ':searchPostalCode-inputEl')]")
    private WebElement table_SearchAddressBookBasicsSearchResultsTable;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //					Methods
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    private void setName(String name) {
    	setText(input_SearchAddressBookBasicsPCName, name);
    }
    
    private void setSSN(String ssn) {
    	setText(input_SearchAddressBookBasicsPCssn, ssn);
    }
    
    private void setTIN(String tin) {
    	setText(input_SearchAddressBookBasicsPCtin, tin);
    }
    
    private void setPhone(String phone) {
    	setText(input_SearchAddressBookBasicsPCPhone, phone);
    }
    
    private void setStreet(String street) {
    	setText(input_SearchAddressBookBasicsPCStreet, street);
    }
    
    private void setCity(String city) {
    	setText(input_SearchAddressBookBasicsPCCity, city);
    }
    
    private void setState(State stateToSelect) {
        Guidewire8Select commonState = select_SearchAddressBookBasicPCState();
        commonState.selectByVisibleText(stateToSelect.getName());
    }
    
    private void setZip(String zip) {
    	waitForPostBack();
    	setText(input_SearchAddressBookBasicsPCZipCode, zip);
    }
    
    public void clickBasicSearch() {
    	super.clickSearch();
    }
    
    public boolean basicSearchExists() {
    	if(checkIfElementExists(link_SearchAddressBookPC_basicsBasic, 2000)) {
    		return true;
    	} else {
    		return false;
    	}    	
    }
    
    public boolean advancedSearchExists() {
    	if(checkIfElementExists(link_SearchAddressBookPC_basicsAdvanced, 2000)) {
    		return true;
    	} else {
    		return false;
    	}    	
    }
   
    public boolean clickAdvancedSearch() {
    	if(advancedSearchExists()) {
    		clickWhenClickable(link_SearchAddressBookPC_basicsAdvanced, 200);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void setSearchCriteria(String tin, String ssn, String companyName, String firstName, String lastName, String street, String city, State state, String zip, String phone) {
    	waitForPageLoad();
    	if (companyName != null) {
            setName(companyName);
        } else if (firstName != null && lastName != null) {
            setName(firstName +" "+lastName);
        } else if(tin != null){
        	setTIN(tin);
        } else if(ssn != null) {
        	setSSN(ssn);
        } else {
            Assert.fail("Must pass in a company name, a first and last name, TIN or SSN");
        }
    	if(street !=null) {
    		setStreet(street);
    	}
    		
        if (city != null) {
            setCity(city);
        }
        if (state != null) {
            setState(state);
        }
        if (zip != null) {
            setZip(zip);
        }
    }
}
