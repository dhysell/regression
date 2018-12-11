package repository.ab.contact;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.PhoneType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsAddressesAB extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;
    public ContactDetailsAddressesAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ***************************************************************************************************
    // Items Below are the Repository Items in the Contact Details Addresses
    // Page.
    // ***************************************************************************************************

    @FindBy(xpath = "//*[@id='ContactDetail:ABContactDetailScreen:ttlBar']")
    private WebElement text_ViewContactPageTitle;

    @FindBy(xpath = "//a[contains(@id, 'ABContactDetailPopup:__crumb__')]")
    private WebElement link_ContactDetailsAddressesReturnToAddressSearch;

    @FindBy(xpath = "//*[contains(@id,':ABAddressesLV_tb:Edit-btnEl')]")
    private WebElement button_ContactDetailsAddressesEdit;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV_tb:ABContactDetailScreen_DeleteButton')] | //span[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:ToolbarButton-btnEl') and contains(.,'Retired')]")
    private WebElement button_ContactDetailsAddressesRetired;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:BusinessPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl')]")
    private WebElement text_ContactDetailsAddressesBusinessPhone;

    @FindBy(xpath = "//div[contains(@id,':ABContactDetailScreen:ABAddressesLV')]")
    private WebElement tableDiv_ContactDetailsAddressesAddressTable;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:0')]")
    private WebElement div_ContactDetailsAddressesDeliveryOptionsUnedit;

    // ***************************************************************************************************
    // Items Below are the Helper methods for the Contact Details Addresses Edit
    // Page Repository Items.
    // ***************************************************************************************************

    @FindBy(xpath = "//div[contains(@id,':ABContactDetailScreen:ABAddressesLV')]")
    private WebElement tableDiv_AddressesTable;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ABAddressesLV_tb:Update-btnEl')]")
    private WebElement button_ContactDetailsAddressesUpdate;

    @FindBy(xpath = "//*[contains(@id,'ContactDetail:ABContactDetailScreen:ContactBasicsDV_tb:Cancel')]")
    private WebElement button_ContactDetailsAddressesCancel;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ABAddressesLV_tb:Add-btnEl')]")
    private WebElement button_ContactDetailsAddressesAdd;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:Remove')]")
    private WebElement button_ContactDetailsAddressesRemove;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:ABContactDetailScreen_RetireButton-btnEl')]")
    private WebElement button_ContactDetailsAddressesRetire;

    @FindBy(xpath = "//*[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:ABContactDetailScreen_UnretireButton-btnEl')]")
    private WebElement button_ContactDetailsAddressesUnretire;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:ReturnMail-btnEl')]")
    private WebElement button_ContactDetailsAddressesReturnMail;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV_tb:ToolbarButton-btnEl')]")
    private WebElement button_ContactDetailsAddressesActive;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV-body')]/descendant::table")
    private WebElement table_ContactDetailsAddressesAddresses;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:OfficeNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesLocationNumber;

    private Guidewire8Select select_ContactDetailsOfficeNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id,':globalAddressContainer:GlobalAddressInputSet:OfficeNumberList-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id,':ABAddressDetailDV:AddressOwnerInputSet:Remove-btnEl')]")
    private WebElement button_ContactDetailsAddressesDeliveryOptionsRemove;


    private Guidewire8Select select_ContactDetailsAddressesCountry() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:Country-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine1-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesAddressLine1;

    @FindBy(xpath = "//input[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine2-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesAddressLine2;

    @FindBy(xpath = "//input[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesCity;

    @FindBy(xpath = "//input[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:County-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesCounty;

    private Guidewire8Select select_ContactDetailsAddressesState() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:State-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesZip;

    private Guidewire8Select select_ContactDetailsAddressesAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Address_AddressType')]");
    }

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Address_Description-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesDescription;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Address_ValidUntil-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesValidUntil;

    @FindBy(xpath = "//input[contains(@id,':Latitude-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesLatitude;

    @FindBy(xpath = "//input[contains(@id,':Longitude-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesLongitude;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneBusiness;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneWork;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Home:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneHome;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneMobile;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Fax:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneFax;

    private Guidewire8Select select_ContactDetailsAddressesPrimaryPhone() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:PrimaryPhone')]");
    }

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:SpeedDial-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesPhoneSpeedDial;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:OfficeEmail-inputEl')]")
    private WebElement editbox_ContactDetailsAddressesEmail;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Add-btnEl')]")
    private WebElement button_ContactDetailsAddressesDeliveryOptionsAdd;

    @FindBy(xpath = "//*[contains(@id,':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Remove-btnEl')]")
    private WebElement editbox_ContactDetailsAddressesDeliveryOptionsRemove;

    @FindBy(xpath = "//div[contains(@id, ':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:0-body')]/descendant::table/descendant/tr/td[2]/div")
    private WebElement text_ContactDetailsAddressesDeliveryOptionsPicker;

    private Guidewire8Select select_ContactDetailsAddressesDeliveryOptions() {
        return new Guidewire8Select(driver, "//div[contains(@id, ':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:0-body')]/following-sibling::div/table/descendant::table");
    }

    @FindBy(xpath = "//div[contains(@id, ':ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:0-body')]/descendant::table/descendant::tr/td[3]/descendant::div")
    private WebElement editbox_ContactDetailsAddressesDeliveryOptionsDescription;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:') or (contains(@id, 'NewContact:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:0') and not(contains(@id, '-body')))]")
    private WebElement div_ContactDetailsAddressesDeliveryOptions;

    private WebElement editbox_ContactDetailsAddressesDeliveryOption(String deliveryOption) {
        return find(By.xpath("//div[contains(.,'" + deliveryOption + "')]/"));
    }

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:ABAddressesLV') and not(contains(@id, '-body')) or contains(@id, 'NewContact:ABContactDetailScreen:ABAddressesLV')]")
    private WebElement tableDiv_ContactDetailsAddresses;

    private Guidewire8RadioButton radio_ContactDetailsAddressesMortgage1st() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:MortgageFirst-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesMortgage2nd() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:MortgageSecond-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesMortgageEscrow() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:MortgageEscrow-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesMortgage() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Mortgage-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesAutoLeased() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:AutoLeased-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesAuto() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Auto-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesCommercialEscrow() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:CommercialEscrow-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesCommercial() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:Commercial-containerEl')]/table");
    }

    private Guidewire8RadioButton radio_ContactDetailsAddressesFarmEquip() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id,'ContactDetail:ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:FarmEquipment-containerEl')]/table");
    }

    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
    private WebElement editbox_AddressLineOne;

    // *****************************************************************************
    // Methods below are the helper methods to the Contact Details Addresses
    // Page.
    // *****************************************************************************

    // 	// public void clickWorkPlan(){
    // clickWhenClickable(link_WorkPlan);
    // 
    // IContactWorkPlan activity = ContactFactory.getContactWorkPlanPage();
    // while (!activity.assignButtonExists()){
    // link_WorkPlan.click();
    // }
    // }

    public void clickReturnToAddressSearch() {
        clickWhenClickable(link_ContactDetailsAddressesReturnToAddressSearch);
    }

    public int getNumberOfAddresses() {
        return tableUtils.getRowCount(tableDiv_ContactDetailsAddresses);
    }

    public void checkAddressByLineOne(AddressInfo address) {
        if (address == null) {
            tableUtils.setCheckboxInTable(tableDiv_AddressesTable, 1, true);
        } else {
            tableUtils.setCheckboxInTableByText(tableDiv_AddressesTable, address.getLine1(), true);
        }
    }

    public void getToReturnMail(AddressInfo address) {
        checkAddressByLineOne(address);
        clickWhenClickable(button_ContactDetailsAddressesReturnMail);
    }

    public String getContactPageTitle() {
        waitUntilElementIsVisible(text_ViewContactPageTitle);
        String text = text_ViewContactPageTitle.getText();
        return text;
    }

    public boolean clickContactDetailsAdressesEditLink() {
        
        if (checkIfElementExists(button_ContactDetailsAddressesEdit, 10)) {
            waitUntilElementIsClickable(button_ContactDetailsAddressesEdit);
            button_ContactDetailsAddressesEdit.click();
            
            return true;
        }
        return false;
    }

    public void clickContactDetailsAddressesRetiredLink() {
        clickWhenClickable(button_ContactDetailsAddressesRetired);
    }

    public void clickContactDetailsAddressesUpdate() {
        waitUntilElementIsClickable(button_ContactDetailsAddressesUpdate);
            button_ContactDetailsAddressesUpdate.click();
        waitForPostBack();
        }

    public void clickContactDetailsAddressesCancel() {
    	clickWhenClickable(button_ContactDetailsAddressesCancel);
    }

    public void clickContactDetailsAddressesAdd() {
        clickWhenClickable(button_ContactDetailsAddressesAdd);
    }

    public void clickContactDetailsAddressesRemove() {
    	clickWhenClickable(button_ContactDetailsAddressesRemove);
    }

    public void clickContactDetailsAddressesRetire() {
        clickWhenClickable(button_ContactDetailsAddressesRetire);
    }

    public void retireAddress(String line1) {
        tableUtils.setCheckboxInTableByText(tableDiv_AddressesTable, line1, true);
        clickContactDetailsAddressesRetire();
        
    }

    public void clickContactDetailsAddressesUnretire() {
        waitUntilElementIsClickable(button_ContactDetailsAddressesUnretire);
        button_ContactDetailsAddressesUnretire.click();
    }

    public ArrayList<String> getAddresses() {
        waitUntilElementIsVisible(tableDiv_ContactDetailsAddressesAddressTable);
        return tableUtils.getAllCellTextFromSpecificColumn(tableDiv_ContactDetailsAddressesAddressTable, "Address");
    }

    public AddressInfo getHighlightedAddress() {
        int highlightedRow = tableUtils.getHighlightedRowNumber(tableDiv_ContactDetailsAddressesAddressTable);
        return parseAddressFromAddressesString(tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactDetailsAddressesAddressTable, highlightedRow, "Address"));
    }

    public AddressInfo getAddressByType(String type) throws Exception {
        waitUntilElementIsVisible(tableDiv_ContactDetailsAddressesAddressTable);
        if (tableDiv_ContactDetailsAddressesAddressTable.findElements(By.xpath(".//div[.='" + type + "']/parent::td/parent::tr")).size() > 0) {
            String dataRecordId = tableDiv_ContactDetailsAddressesAddressTable.findElement(By.xpath("//div[.='" + type + "']/parent::td/parent::tr")).getAttribute("id");
            ArrayList<String> addressLineInfo = new ArrayList<>();

            String gridColumn = tableUtils.getGridColumnFromTable(tableDiv_ContactDetailsAddressesAddressTable, "Type");
            gridColumn = gridColumn.replace("-textEl", "");
            addressLineInfo.add(tableDiv_ContactDetailsAddressesAddressTable.findElement(By.xpath("//tr[contains(@id, '" + dataRecordId + "')]/td[contains(@class,'" + gridColumn + "')]")).getText());

            gridColumn = tableUtils.getGridColumnFromTable(tableDiv_ContactDetailsAddressesAddressTable, "Office");
            gridColumn = gridColumn.replace("-textEl", "");
            addressLineInfo.add(tableDiv_ContactDetailsAddressesAddressTable.findElement(By.xpath("//tr[contains(@id, '" + dataRecordId + "')]/td[contains(@class,'" + gridColumn + "')]")).getText());

            gridColumn = tableUtils.getGridColumnFromTable(tableDiv_ContactDetailsAddressesAddressTable, "Address");
            gridColumn = gridColumn.replace("-textEl", "");
            addressLineInfo.add(tableDiv_ContactDetailsAddressesAddressTable.findElement(By.xpath("//tr[contains(@id, '" + dataRecordId + "')]/td[contains(@class,'" + gridColumn + "')]")).getText());
            return parseAddressFromAddressTable(addressLineInfo);
        } else {
            return null;
        }
    }

    public String getTypeByAddress(String line1) {
    	waitForPageLoad();
        
        waitUntilElementIsVisible(tableDiv_ContactDetailsAddressesAddressTable);
        return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactDetailsAddressesAddressTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactDetailsAddressesAddressTable, "Address", line1), "Type");
    }

    public String getOfficeByAddress(String line1) {
        
        waitUntilElementIsVisible(tableDiv_ContactDetailsAddressesAddressTable);
        return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactDetailsAddressesAddressTable, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactDetailsAddressesAddressTable, "Address", line1), "Office");
    }

    public void removeAddress(String addressLine1) {
        tableUtils.setCheckboxInTableByText(tableDiv_ContactDetailsAddressesAddressTable, addressLine1, true);
        clickRemove();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public void clickActive() {
    	waitUntilElementIsClickable(button_ContactDetailsAddressesActive);
    	button_ContactDetailsAddressesActive.click();
    	waitUntilElementIsNotVisible(button_ContactDetailsAddressesActive);
    }

    public void unretireAddress(String addressLine1) {
        tableUtils.setCheckboxInTableByText(tableDiv_ContactDetailsAddressesAddressTable, addressLine1, true);
        clickContactDetailsAddressesUnretire();
    }

    private AddressInfo parseAddressFromAddressTable(ArrayList<String> addressLineInfo) throws Exception {
        AddressType addressType = null;
        String officeNumber;
        String line1;
        String city;
        State state;
        String zip;

//		String[] stringArray = text.split("\\r?\\n");
        addressType = AddressType.valueOf(addressLineInfo.get(0));
        officeNumber = addressLineInfo.get(1);
        AddressInfo address = parseAddressFromAddressesString(addressLineInfo.get(2));

        address.setType(addressType);
        address.setNumber(Integer.parseInt(officeNumber));
        return address;
    }

    private AddressInfo parseAddressFromAddressesString(String _address) {
        String[] addressArray = _address.split(",");
        String line1 = addressArray[0].trim();
        String city = addressArray[1].trim();
        addressArray[2] = addressArray[2].trim();
        String[] stateZip = addressArray[2].split(" ");
        State state = State.valueOfAbbreviation(stateZip[0]);
        String zip = stateZip[1].trim();

        AddressInfo address = new AddressInfo(line1, city, state, zip);
        return address;
    }

    public ArrayList<AddressInfo> returnAddressInfo() throws Exception {
        ArrayList<AddressInfo> returnAddresses = new ArrayList<AddressInfo>();
        ArrayList<String> addressTypes = tableUtils.getAllCellTextFromSpecificColumn(tableDiv_ContactDetailsAddressesAddressTable, "Type");
        
        for (String type : addressTypes) {
            returnAddresses.add(getAddressByType(type));
        }
        return returnAddresses;
    }

/*	
		public void setCheckboxAddressTable(String address) {
		
	}
*/
    public String getAddressDescriptionByAddress(AddressInfo address) {
    	TableUtils tableUtils = new TableUtils(driver);
    	return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactDetailsAddresses, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactDetailsAddresses, "Address", address.getLine1()), "Description");
    	
    }

    public void setContactDetailsAddressesLocation(String location) {
        
        Guidewire8Select mySelect = select_ContactDetailsOfficeNumber();
        mySelect.selectByVisibleText(location);
        
    }

    public List<String> getOfficeNumbers() {
        return select_ContactDetailsOfficeNumber().getList();
    }

    public void clickRemoveDeliveryOptions() {
        
        clickWhenClickable(button_ContactDetailsAddressesDeliveryOptionsRemove);
        
    }

    public void setContactDetailsAddressCountry(String country) {
        select_ContactDetailsAddressesCountry().selectByVisibleText(country);
    }

    public void setContactDetailsAddressesAddressLine1(String addressLine1) {
        clickWhenClickable(editbox_ContactDetailsAddressesAddressLine1);
        setText(editbox_ContactDetailsAddressesAddressLine1, addressLine1);
    }

    public String getAddressLine1() {
        return editbox_ContactDetailsAddressesAddressLine1.getAttribute("value");
    }

    public void setContactDetailsAddressesAddressLine2(String addressLine2) {
        
        clickWhenClickable(editbox_ContactDetailsAddressesAddressLine2);
        editbox_ContactDetailsAddressesAddressLine2.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesAddressLine2.sendKeys(Keys.DELETE);
        editbox_ContactDetailsAddressesAddressLine2.sendKeys(addressLine2);
        editbox_ContactDetailsAddressesAddressLine2.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesCity(String city) {
        
        waitUntilElementIsVisible(editbox_ContactDetailsAddressesCity);
        
        editbox_ContactDetailsAddressesCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_ContactDetailsAddressesCity.sendKeys(city);
        
        editbox_ContactDetailsAddressesCity.sendKeys(Keys.TAB);
        
    }

    public String getCity() {
        return getTextOrValueFromElement(editbox_ContactDetailsAddressesCity);
    }

    public void setContactDetailsAddressesCounty(String county) {
        clickWhenClickable(editbox_ContactDetailsAddressesCounty);
        editbox_ContactDetailsAddressesCounty.sendKeys(county);
    }

    public void setContactDetailsAddressesState(State state) {
        select_ContactDetailsAddressesState().selectByVisibleText(state.toString());
    }
    
    public State getContactDetailsAddressesState() {
        return State.valueOf(select_ContactDetailsAddressesState().getText());
    }

    public void setContactDetailsAddressesZipCode(String zip) {
        
        clickWhenClickable(editbox_ContactDetailsAddressesZip);
        editbox_ContactDetailsAddressesZip.sendKeys(Keys.CONTROL + "a");
        editbox_ContactDetailsAddressesZip.sendKeys(zip);
        
        editbox_ContactDetailsAddressesZip.sendKeys(Keys.TAB);
        
    }
    
    public String getContactDetailsAddressesZipCode() {
		//if its an input
		if(!finds(By.xpath(":ABContactDetailScreen:ABAddressDetailDV:AddressOwnerInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl")).isEmpty()) {
			return editbox_ContactDetailsAddressesZip.getAttribute("value");
		} else {
			return editbox_ContactDetailsAddressesZip.getText();
		}
	}

    public void setContactDetailsAddressType(AddressType inputAddressType) {
        
        select_ContactDetailsAddressesAddressType().selectByVisibleText(inputAddressType.getValue());
        
    }

    public void setContactDetailsAddressesDescription(String description) {
        clickWhenClickable(editbox_ContactDetailsAddressesDescription);
        editbox_ContactDetailsAddressesDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesDescription.sendKeys(Keys.DELETE);
        editbox_ContactDetailsAddressesDescription.sendKeys(description);
        editbox_ContactDetailsAddressesDescription.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesValidUntilDate(String validUntilDate) {
        clickWhenClickable(editbox_ContactDetailsAddressesValidUntil);
        editbox_ContactDetailsAddressesValidUntil.sendKeys(validUntilDate);
        editbox_ContactDetailsAddressesValidUntil.sendKeys(Keys.TAB);
    }

    public boolean setLat(String latitude) {
        if (checkIfElementExists(editbox_ContactDetailsAddressesLatitude, 2000)) {
            clickWhenClickable(editbox_ContactDetailsAddressesLatitude);
            editbox_ContactDetailsAddressesLatitude.sendKeys(Keys.CONTROL + "a");
            editbox_ContactDetailsAddressesLatitude.sendKeys(Keys.DELETE);
            editbox_ContactDetailsAddressesLatitude.sendKeys(latitude);
            
            editbox_ContactDetailsAddressesLatitude.sendKeys(Keys.TAB);
            
            return true;
        } else {
            return false;
        }
    }

    public boolean setLong(String longitude) {
        checkIfElementExists(editbox_ContactDetailsAddressesLongitude, 2000);
        if (checkIfElementExists(editbox_ContactDetailsAddressesLongitude, 2000)) {
            clickWhenClickable(editbox_ContactDetailsAddressesLongitude);
            editbox_ContactDetailsAddressesLongitude.sendKeys(Keys.CONTROL + "a");
            editbox_ContactDetailsAddressesLongitude.sendKeys(Keys.DELETE);
            editbox_ContactDetailsAddressesLongitude.sendKeys(longitude);
            
            editbox_ContactDetailsAddressesLongitude.sendKeys(Keys.TAB);
            
            return true;
        } else {
            return false;
        }
    }

    public String getLatitude() {
        return editbox_ContactDetailsAddressesLatitude.getAttribute("value");
    }

    public String getLongitude() {
        return editbox_ContactDetailsAddressesLongitude.getAttribute("value");
    }

    public String getSpatialPoint() {
        
        return find(By.xpath("//div[contains(@id,':Address_Spatial-inputEl')]")).getText();
    }

    public void setContactDetailsAddressesBusinessPhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneBusiness);
        editbox_ContactDetailsAddressesPhoneBusiness.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesPhoneBusiness.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneBusiness.sendKeys(Keys.TAB);
    }

    public String getContactDetailsAddressesBusinessPhone() {
        String businessPhone = text_ContactDetailsAddressesBusinessPhone.getText();
        return businessPhone;
    }

    public void setContactDetailsAddressesWorkPhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneWork);
        editbox_ContactDetailsAddressesPhoneWork.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesPhoneWork.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneWork.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesHomePhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneHome);
        editbox_ContactDetailsAddressesPhoneHome.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesPhoneHome.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneHome.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesMobilePhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneMobile);
        editbox_ContactDetailsAddressesPhoneMobile.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesPhoneMobile.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneMobile.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesFaxPhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneFax);
        editbox_ContactDetailsAddressesPhoneFax.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ContactDetailsAddressesPhoneFax.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneFax.sendKeys(Keys.TAB);
    }

    public void select_ContactDetailsAddressesPrimaryPhone(PhoneType phoneType) {
        waitUntilElementIsClickable(editbox_ContactDetailsAddressesPhoneWork);
        select_ContactDetailsAddressesPrimaryPhone().selectByVisibleText(phoneType.getValue());
    }

    public void setContactDetailsAddressesSpeedDialPhone(String phone) {
        clickWhenClickable(editbox_ContactDetailsAddressesPhoneSpeedDial);
        editbox_ContactDetailsAddressesPhoneSpeedDial.sendKeys(phone);
        editbox_ContactDetailsAddressesPhoneSpeedDial.sendKeys(Keys.TAB);
    }

    public void setContactDetailsAddressesEmail(String email) {
        clickWhenClickable(editbox_ContactDetailsAddressesEmail);
        editbox_ContactDetailsAddressesEmail.sendKeys(email);
        editbox_ContactDetailsAddressesEmail.sendKeys(Keys.TAB);
    }

    public void clickContactDetailsAddressesDeliveryOptionsAdd() {
        clickWhenClickable(button_ContactDetailsAddressesDeliveryOptionsAdd);
        
    }

    public void updateDeliveryOption(DeliveryOptionType option, String description) {
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowNumberInTableByText(div_ContactDetailsAddressesDeliveryOptions, option.getTypeValue()), "Type", option.getTypeValue());
        tableUtils.setValueForCellInsideTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowNumberInTableByText(div_ContactDetailsAddressesDeliveryOptions, option.getTypeValue()), "Description", "Description", description);
    }

    public void addDeliveryOption(DeliveryOptionType option, String description) throws Exception {
        clickContactDetailsAddressesDeliveryOptionsAdd();
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowCount(div_ContactDetailsAddressesDeliveryOptions), "Type", option.getTypeValue());
        tableUtils.setValueForCellInsideTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowCount(div_ContactDetailsAddressesDeliveryOptions), "Description", "Description", description);
    }

    public String addDeliveryOptionCheckDescription(DeliveryOptionType option) throws Exception {
        clickContactDetailsAddressesDeliveryOptionsAdd();
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowCount(div_ContactDetailsAddressesDeliveryOptions), "Type", option.getTypeValue());
        int row = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(div_ContactDetailsAddressesDeliveryOptions, "Type", option.getTypeValue()));
        return tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAddressesDeliveryOptions, row, "Description");
    }

    public ArrayList<String> getDeliveryOptions() {
        
        waitUntilElementIsVisible(div_ContactDetailsAddressesDeliveryOptionsUnedit);
        return tableUtils.getAllCellTextFromSpecificColumn(div_ContactDetailsAddressesDeliveryOptionsUnedit, "Description");

    }

    public String getPhoneOnAddress(String type, String address) {
        
        tableUtils.clickCellInTableByRowAndColumnName(tableDiv_ContactDetailsAddresses, tableUtils.getRowNumberInTableByText(tableDiv_ContactDetailsAddresses, address), "Address");
        return find(By.xpath("//label[contains(., '" + type + "')]/parent::td/following-sibling::td/div")).getText();

    }

    public void removeDeliveryOption(String description) {
        tableUtils.setCheckboxInTable(div_ContactDetailsAddressesDeliveryOptions, tableUtils.getRowNumberInTableByText(div_ContactDetailsAddressesDeliveryOptions, description), true);
        clickRemoveDeliveryOptions();
    }

    public void updateAddressTypeInAddressTable(String line1, AddressType type) {
        
        waitUntilElementIsClickable(tableDiv_ContactDetailsAddresses);
        int row = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactDetailsAddresses, "Address", line1));
        
        tableUtils.selectValueForSelectInTableDoubleClick(tableDiv_ContactDetailsAddresses, row, "Type", type.getValue());
    }

    public void deleteAddresses(ArrayList<AddressInfo> addresses) {
        
        waitUntilElementIsVisible(tableDiv_ContactDetailsAddresses);
        int row = 0;
        for (int i = 0; i < addresses.size(); i++) {
            row = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_AddressesTable, "Address", addresses.get(i).getLine1()));
            tableUtils.setCheckboxInTable(tableDiv_AddressesTable, row, true);
            
        }
        
        clickRemove();
    }

    public void addAddresses(ArrayList<AddressInfo> addresses) {
        int i = 0;
        for (i = 0; i < addresses.size(); i++) {
            
            setContactDetailsAddressesAddressLine1(addresses.get(i).getLine1());
            if (!(addresses.get(i).getLine2() == null))
                setContactDetailsAddressesAddressLine2(addresses.get(i).getLine2());
            setContactDetailsAddressesState(addresses.get(i).getState());
            setContactDetailsAddressesCity(addresses.get(i).getCity());

            // addAddress.setContactDetailsAddressesCounty(this.primaryAddress.getCounty());
            setContactDetailsAddressType(addresses.get(i).getType());
            setContactDetailsAddressesZipCode(addresses.get(i).getZip());
            setContactDetailsAddressesDescription(addresses.get(i).getDescription());
            if (addresses.get(i).getValidUntil() != null) {
                setContactDetailsAddressesValidUntilDate(DateUtils.dateFormatAsString("MM/dd/yyyy", addresses.get(i).getValidUntil()));
            }
            select_ContactDetailsAddressesPrimaryPhone(addresses.get(i).getPhonePrimary());
            setContactDetailsAddressesBusinessPhone(addresses.get(i).getPhoneBusinessFormatted());
	/*					addAddress.setContactDetailsAddressesWorkPhone(this.primaryAddress.getPhoneWorkFormatted());
			addAddress.setContactDetailsAddressesHomePhone(this.primaryAddress.getPhoneHomeFormatted());
			addAddress.setContactDetailsAddressesMobilePhone(this.primaryAddress.getPhoneMobileFormatted());
			addAddress.setContactDetailsAddressesFaxPhone(this.primaryAddress.getPhoneFaxFormatted());
	
			addAddress.setContactDetailsAddressesSpeedDialPhone(this.primaryAddress.getPhoneSpeedDial());
			addAddress.setContactDetailsAddressesEmail(this.primaryAddress.getEmailAddress().getAddress().toString());
	*/
            if (i != addresses.size() - 1) {
                
                clickContactDetailsAddressesAdd();
            }
        }
    }


    public ArrayList<String> getDeliveryOption() {
        return tableUtils.getAllCellTextFromSpecificColumn(div_ContactDetailsAddressesDeliveryOptions, "Description");
    }

    public AddressInfo getWorkAddress() {
        
        clickWhenClickable(tableDiv_ContactDetailsAddresses);
        String address = tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_ContactDetailsAddresses, tableUtils.getRowInTableByColumnNameAndValue(tableDiv_ContactDetailsAddresses, "Type", "Work"), "Address");
        return StringsUtils.addressStringParser(address);
    }

    public void setLoanTypeInfoMortgage1st(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':MortgageFirst-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesMortgage1st();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoMortgage2nd(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':MortgageSecond-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesMortgage2nd();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoMortgageEscrow(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, 'MortgageEscrow-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesMortgageEscrow();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoMortgage(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':Mortgage-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesMortgage();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoAutoLeased(boolean trueFalse) {
        
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, 'AutoLeased-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesAutoLeased();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoAuto(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':Auto-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesAuto();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoCommercialEscrow(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':CommercialEscrow-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesCommercialEscrow();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoCommercial(boolean trueFalse) {
        waitUntilElementIsVisible(find(By.xpath("//label[contains(@id, ':Commercial-labelEl')]")));
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesCommercial();
        myRadio.select(trueFalse);
        
    }

    public void setLoanTypeInfoFarmEquipment(boolean trueFalse) {
        Guidewire8RadioButton myRadio = radio_ContactDetailsAddressesFarmEquip();
        myRadio.select(trueFalse);
        
    }

    public void setAllLoanTypeInfoTrue() throws Exception {
//        gwCommon.scrollToElement(find(By.xpath("//label[contains(@id, ':Commercial-labelEl')]")));
        setLoanTypeInfoMortgage(true);
        setLoanTypeInfoMortgage1st(true);
        setLoanTypeInfoMortgage2nd(true);
        setLoanTypeInfoMortgageEscrow(true);
//        gwCommon.scrollToElement(find(By.xpath("//label[contains(@id, ':FarmEquipment-labelEl')]")));
        setLoanTypeInfoAuto(true);
        setLoanTypeInfoAutoLeased(true);
        setLoanTypeInfoCommercial(true);
        setLoanTypeInfoCommercialEscrow(true);
//        gwCommon.scrollToElement(find(By.xpath("//label[contains(@id, ':FarmEquipment-labelEl')]")));
        setLoanTypeInfoFarmEquipment(true);
    }


}
