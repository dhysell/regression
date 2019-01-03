package repository.pc.contact;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.CreateNew;
import repository.gw.generate.custom.AddressInfo;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchResultsReturnPC;

import java.util.ArrayList;
import java.util.List;

public class ContactEditPC extends BasePage {


    private WebDriver driver;

    public ContactEditPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[id$=':AddressLine1-inputEl']")
    private WebElement input_AddressLineOne;

    @FindBy(css = "input[id$=':City-inputEl']")
    private WebElement input_City;

    @FindBy(css = "input[id$=':PostalCode-inputEl']")
    private WebElement input_ZipCode;
    
/*    @FindBy(css = "input[id$=':DeleteBtn-btnEl']")
    private WebElement button_ContactFileDetailsDelete;
*/
    @FindBy(xpath = "//*[contains(@id,':DeleteBtn-btnEl') or contains(@id, ':DeleteBtn-btnInnerEl')]")
    public WebElement button_ContactFileDetailsDelete;

    
    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------


    public Guidewire8Select select_ContactEditAddressListing() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AddressListing-triggerWrap')]");
    }

    public Guidewire8Select select_ContactEditAddressAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
    }

    public Guidewire8Select select_ContactEditAddressState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }


    public void setContactEditAddressListing(String addressListing) {
        if (addressListing.contains("New...")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':AddressListing:AddressListingMenuIcon') or contains(@id, ':LinkAddressMenu:LinkAddressMenuMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy') or contains(@id,':AddAddressMenuItem')]")));
                return;
            }
        } else if(checkIfElementExists(button_AddressListingNewAddress, 1000)){
	        clickWhenClickable(button_AddressListingNewAddress);
	    	for(WebElement contact : button_AddressListingContactOptions) {
	    		clickWhenClickable(contact);
	    		for(WebElement address : button_AddressListing) {
	    			if(address.getText().contains(addressListing)) {
	    				waitUntilElementIsClickable(address);
	    				clickWhenVisible(address);
	    			}
	    		}
	    	}
        	
        } else {
	        Guidewire8Select selectAddressListing = select_ContactEditAddressListing();
	        selectAddressListing.selectByVisibleTextPartial(addressListing);
        }
    }
    
    @FindBy(xpath = "//a[contains(@id, ':AddressListingMenuIcon') or contains(@id, ':LinkAddressMenuMenuIcon')]")
   	protected WebElement button_AddressListingNewAddress;
    
    @FindBy(xpath = "//div[contains(@id, ':AddressListing:newAddressBuddy')]")
   	protected WebElement button_AddressListingNewLocation;
    
    @FindBy(xpath = "//div[contains(@id, ':LinkAddressMenuMenuIcon-fieldMenu-targetEl')]/div/a/span[contains(.,'New Location')]/parent::a/parent::div")
   	protected WebElement button_AddressListingOtherLocationsNewLocation;
    
    @FindBy(xpath = "//div[contains(@id, ':LinkAddressMenuMenuIcon-fieldMenu-targetEl')]/div/a/span")
   	protected List<WebElement> button_AddressListingAllLocations;
    
    @FindBy(xpath = "//div[contains(@id, ':LinkAddressMenuMenuIcon-fieldMenu-targetEl')]")
   	protected List<WebElement> button_AddressListingContactOptions; 
    
    @FindBy(xpath = "//span[contains(@id, 'contactDetail:PrimaryAddress-textEl')]")
   	protected List<WebElement> button_AddressListing;
    
    private WebElement link_ContactAddressOptions(String option) {
    	return driver.findElement(By.xpath("//div[contains(@id, ':LinkAddressMenuMenuIcon-fieldMenu-targetEl')]/div/a/span[contains(.,'"+option+"')]"));
    }
    
    @FindBy(xpath = "//div[contains(@id, 'EditAdditionalNamedInsuredPopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:LinkedAddressFBMInputSet:LinkAddressMenu:LinkAddressMenuMenuIcon-fieldMenu-targetEl')]")
    private List<WebElement> button_AdditionalNamedInsuredImportAddress;
    
    private WebElement link_AddressOptions(String addressOption) {
    	return driver.findElement(By.xpath("//div[contains(@id, ':contactDetail:PrimaryAddress')]/a/span[contains(.,'"+addressOption+"')]"));
    }
    
    private List<WebElement> link_AllAddressOptions() {
    	return driver.findElements(By.xpath("//span[contains(@id, 'Address-textEl')]"));
    }
    
    //This needs to work for more than one address.
    public ArrayList<String> getAddressListings(){
    	Guidewire8Select selectAddressListing = select_ContactEditAddressListing();
    	if(select_ContactEditAddressListing().checkIfElementExists(1000)) {
    		return selectAddressListing.getListItems();
    	} else {
    		ArrayList<String> addresses = new ArrayList<>();
    		clickWhenClickable(button_AddressListingNewAddress);
    		List<WebElement> webList = button_AddressListingAllLocations;
    		for(WebElement loc : webList) {
    			if(!loc.getText().equals("New Location")) {
    				clickWhenClickable(link_ContactAddressOptions(loc.getText()));
    				for(WebElement contact : button_AddressListing) {
    					if(!contact.getText().equals("New Location")) {
    						addresses.add(contact.getText());
    					}
    				}
    			}
    		}
    		
    		/*List<WebElement> addressOptions = link_AllAddressOptions();
    		for(WebElement address : addressOptions) {
    			addresses.add(address.getText());
    		}*/
    		return addresses;
    	}
    }
    
    public boolean addressLine1Exists() {
    	if(checkIfElementExists(input_AddressLineOne, 1000)) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public void setContactEditAddressLine1(String address) {
        setText(input_AddressLineOne, address);
    }


    public void setContactEditAddressCity(String city) {
        setText(input_City, city);
    }


    public void setContactEditAddressState(State state) {
        select_ContactEditAddressState().selectByVisibleText(state.getName());
    }


    public void setContactEditAddressZipCode(String zip) {
        setText(input_ZipCode, zip);
    }


    public void setContactEditAddressAddressType(AddressType addType) {
        Guidewire8Select selectAddressType = select_ContactEditAddressAddressType();
        selectAddressType.selectByVisibleTextPartial(addType.getValue());
    }
    
    public void clickDelete() {
    	clickWhenClickable(button_ContactFileDetailsDelete);
    	selectOKOrCancelFromPopup(OkCancel.OK);
    }
    
    
    public void setNewAddress(AddressInfo address) {
    	clickWhenClickable(button_AddressListingNewAddress);
    	if(checkIfElementExists(button_AddressListingNewLocation, 1000)) {
    		clickWhenClickable(button_AddressListingNewLocation);
    	}else {
    		clickWhenClickable(button_AddressListingOtherLocationsNewLocation);
    	}
    	ContactEditPC newAddress= new ContactEditPC(driver);
    	newAddress.inputNewAddress(address);
    }
    
    public void inputNewAddress(AddressInfo address) {
    	setContactEditAddressLine1(address.getLine1());
    	setContactEditAddressCity(address.getCity());
    	setContactEditAddressState(address.getState());
    	setContactEditAddressZipCode(address.getZip());
    	Guidewire8Select selectAddressType = select_ContactEditAddressAddressType();
    	if(selectAddressType.checkIfElementExists(1000)) {
    		setContactEditAddressAddressType(address.getType());
    	}
    }
    
    @FindBy(xpath = "//span[contains(@id, ':ImportAddressesButton-btnEl')]")
    private WebElement button_ImportAddress;
    
    private void clickImportAddress() {
    	clickWhenClickable(button_ImportAddress);
    	waitForPageLoad();
    }
        
    public boolean importAddress(String lastNameOfImportee, String firstName, String companyNameOrLastName, String address, String city, State state, String zip) {
    	clickImportAddress();
    	System.out.println("find Contact to import");
    	repository.pc.search.SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
    	SearchResultsReturnPC searchResultsReturnPC = searchPC.searchAddressBook(false, null, null, firstName, companyNameOrLastName, null, address,  city,  state,  zip, CreateNew.Do_Not_Create_New);
    	clickWhenClickable(searchResultsReturnPC.getSelectToClick());
    	waitForPageLoad();
    	clickWhenClickable(button_AddressListingNewAddress);
    	clickWhenClickable(link_ContactAddressOptions(lastNameOfImportee)); //The import address test fails here 
    	List<WebElement> addressOptions = link_AllAddressOptions();
    	for(WebElement option : addressOptions) {
    		if(option.getText().contains(address)) {
    			return true;
    		}
    	} return false;
    }

}
