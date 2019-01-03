package repository.pc.workorders.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.TableUtils;
import repository.pc.search.SearchAddressBookPC;

public class GenericWorkorderSquireFPPAdditionalInterest extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderSquireFPPAdditionalInterest(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//input[contains(@id,'PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:LoanNumber-inputEl')]")
    public WebElement editbox_SquireFPPAdditionalInterestLoanNumber;

    public Guidewire8Select select_SquireFPPAdditionalInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    public Guidewire8Checkbox check_SquireFPPAdditionalInterestType() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'EditAdditionalInterestPopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    public WebElement checkbox_SquireFPPAdditionalInterest(String addInterest, String loanNumber) {
        return find(By.xpath("//div[contains(., '" + loanNumber + "')]/ancestor::td/preceding-sibling::td/div[contains(., '" + addInterest + "')]/ancestor::td/preceding-sibling::td/div/img"));
    }

    public Guidewire8Select select_SquireFPPAdditionalInterestAddressListing() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewAdditionalInterestPopup:ContactDetailScreen:NewPolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':FarmPersonalPropertyAddlInterestItemsPanelSet:AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    public WebElement table_SquireFPPAdditionalInterestInterestList;

    @FindBy(xpath = "//*[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:FarmPersonalPropertyAddlInterestItemsPanelSet:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:Remove-btnEl')]")
    public WebElement button_SquireFPPAdditionalInterestRemove;

    @FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:fppCoveragesTab')]")
    public WebElement tab_FPPCoverages;

    private WebElement tableUtilsDiv_Items(FarmPersonalPropertyTypes type) {
        return find(By.xpath("//label[contains(text(), '" + type.getValue() + "')]/ancestor::tr[2]/following-sibling::tr[1]/td/div"));
    }

    private WebElement button_Add(FarmPersonalPropertyTypes type) {
        return find(By.xpath("//tr//label[.='" + type.getValue() + "']/ancestor::tr/following-sibling::tr[1]//a[contains(@id,':FPPAddlInterestItemsPanelSet:Add')]"));
    }

    private WebElement button_AddAdditionalInterestLastRow(FarmPersonalPropertyTypes type) {
        return tableUtilsDiv_Items(type).findElement(By.xpath(".//div[contains(@id, '-body')]//table[contains(@id, 'gridview')]/tbody/tr[last()]//td//table//a[.='Add']"));
    }

    @FindBy(xpath = "//div[contains(@id, 'FarmPersonalPropertyAddlInterestSelectPopup:0-body')]")
    public WebElement table_FPPAdditionalInterests;
    
    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
	private WebElement editBox_ContactAddress;
    
    @FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_ContactCity;
    
    private Guidewire8Select select_ContactState() {
 		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
	}
    
    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
	private WebElement editBox_ContactZip;
    
    private Guidewire8Select select_AddressType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
	}

    /*
     * 	methods
     */

    private void setWholeAddress(AddressInfo address) {
		if (address.getLine1() != null && address.getLine1() != "") {
			setAddress(address.getLine1());
		}

		if (address.getCity() != null && address.getCity() != "") {
			setCity(address.getCity());
		}

		if (address.getState() != null) {
			setState(address.getState());
		}

		if (address.getZip() != null && address.getZip() != "") {
			setZip(address.getZip());
		}
	}
    
    private void setAddress(String addressToFill) {
		clickWhenClickable(editBox_ContactAddress);
		editBox_ContactAddress.sendKeys(Keys.chord(Keys.CONTROL + "a"), addressToFill);
	}
    
    private void setCity(String cityToFill) {
		clickWhenClickable(editBox_ContactCity);
		editBox_ContactCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), cityToFill);
	}
    
    private void setState(State stateToSelect) {
		Guidewire8Select commonState = select_ContactState();
		commonState.selectByVisibleText(stateToSelect.getName());
	}
    
    private void setZip(String zipToFill) {
		clickWhenClickable(editBox_ContactZip);
		editBox_ContactZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipToFill);
	}
    
    private void setAddressType(AddressType type) {
		Guidewire8Select mySelect = select_AddressType();
		mySelect.selectByVisibleText(type.getValue());
	}

    public void clickAdd(FarmPersonalPropertyTypes type) {
        clickWhenClickable(button_Add(type));
    }


    public void setDescriptionValueSerialNum(FPPFarmPersonalPropertySubTypes type, int tableRow, String description, int value, String serialNumber) {
        int tabelRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableUtilsDiv_Items(type.getParentType()), "Item Type", type.getValue()));
        tableUtils.selectValueForSelectInTable(tableUtilsDiv_Items(type.getParentType()), tabelRow, "Item Type", type.getValue());
        tableUtils.setValueForCellInsideTable(tableUtilsDiv_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsDiv_Items(type.getParentType()), "Description"), "Description", "itemDescription", description);
        tableUtils.setValueForCellInsideTable(tableUtilsDiv_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsDiv_Items(type.getParentType()), "Value"), "Value", "itemValue", String.valueOf(value));
        tableUtils.setValueForCellInsideTable(tableUtilsDiv_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsDiv_Items(type.getParentType()), "Serial Number"), "Serial Number", "itemSerial", serialNumber);
    }


    public void addItem(FPPFarmPersonalPropertySubTypes type, String description, int value, String serialNumber, AdditionalInterest ai) {
        clickAdd(type.getParentType());
        int size = tableUtils.getNextAvailableLineInTable(tableUtilsDiv_Items(type.getParentType()));
        tableUtils.selectValueForSelectInTable(tableUtilsDiv_Items(type.getParentType()), size, "Item Type", type.getValue());
        setDescriptionValueSerialNum(type, size, description, value, serialNumber);
        addAdditionalInterest(type.getParentType(), ai);
    }


    public void selectItemType(FPPFarmPersonalPropertySubTypes type) {
        int size = tableUtils.getNextAvailableLineInTable(tableUtilsDiv_Items(type.getParentType()));
        tableUtils.selectValueForSelectInTable(tableUtilsDiv_Items(type.getParentType()), size, "Item Type", type.getValue());

    }

    public void clickFPPCoverages() {
        clickWhenClickable(tab_FPPCoverages);
    }


    public void setLoanNumber(String loanNumber) {
        clickWhenClickable(editbox_SquireFPPAdditionalInterestLoanNumber);
        editbox_SquireFPPAdditionalInterestLoanNumber.sendKeys(loanNumber);
    }


    public void setSelectSquireFPPAdditionalInterestType(AdditionalInterestType type) {
        select_SquireFPPAdditionalInterestType().selectByVisibleText(type.getValue());
    }


    public void addInterest(boolean basicSearch, AdditionalInterest ai) throws GuidewireException {
        repository.pc.search.SearchAddressBookPC searchMe = new SearchAddressBookPC(driver);
        clickSearch();
        boolean found = searchMe.searchAddressBookByCompanyName(basicSearch, ai.getCompanyName(), ai.getAddress().getLine1(), ai.getAddress().getCity(), ai.getAddress().getState(), ai.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
        
        completeFPPAdditionalInterest(ai, found);
        if (finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            super.clickOverride();
            
            clickOK();
        }
    }


    public void addAddressFPPAdditionalInterest(AdditionalInterest fppAddInterest) {
        
        clickWhenClickable(find(By.xpath("//a[contains(@id, ':AddressListing:AddressListingMenuIcon')]")));
        
        clickWhenClickable(find(By.xpath("//div[contains(@id, 'AddressListing:newAddressBuddy')]")));
        
        //		select_SquireFPPAdditionalInterestAddressListing().selectByVisibleTextPartial("New");
        setWholeAddress(fppAddInterest.getAddress());
    }


    public void completeFPPAdditionalInterest(AdditionalInterest fppAddInterest, boolean searchFound) {
        if (!searchFound) {
            addAddressFPPAdditionalInterest(fppAddInterest);
            setAddressType(AddressType.Business);
        }
        setLoanNumber(fppAddInterest.getLoanContractNumber());
        setSelectSquireFPPAdditionalInterestType(fppAddInterest.getAdditionalInterestType());
        
        clickGenericWorkorderUpdate();
    }


    public void addAdditionalInterest(FarmPersonalPropertyTypes type, AdditionalInterest addInterest) {
        
        clickWhenClickable(button_AddAdditionalInterestLastRow(type));
        
        if (addInterest.getCompanyOrInsured() == ContactSubType.Company) {
            tableUtils.setCheckboxInTable(table_FPPAdditionalInterests, tableUtils.getRowNumberInTableByText(table_FPPAdditionalInterests, addInterest.getCompanyName()), true);
        } else if (addInterest.getCompanyOrInsured() == ContactSubType.Person) {
            tableUtils.setCheckboxInTable(table_FPPAdditionalInterests, tableUtils.getRowNumberInTableByText(table_FPPAdditionalInterests, addInterest.getPersonFirstName() + " " + addInterest.getPersonLastName()), true);
        }
        
        clickOK();
    }


    public void removeAdditionalInterest(AdditionalInterest addInterest) {
        tableUtils.setCheckboxInTableByText(table_SquireFPPAdditionalInterestInterestList, addInterest.getCompanyName(), true);
        
        clickWhenClickable(button_SquireFPPAdditionalInterestRemove);
        String messageTxt = selectOKOrCancelFromPopup(OkCancel.OK);
        if (!messageTxt.contains("Removing this lien will remove them from all items on the Additional Interest tab, are you sure?")) {
            Assert.fail("The Removing this Lien message box does not contain the required text.");
        }

    }


    public boolean checkFPPAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_SquireFPPAdditionalInterestInterestList, name) > 0;
    }


    public void clickFPPAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_SquireFPPAdditionalInterestInterestList, tableUtils.getRowNumberInTableByText(table_SquireFPPAdditionalInterestInterestList, name), "Name");
        
    }


    public int getAdditionalInterstRowCount() {
        return tableUtils.getRowCount(table_SquireFPPAdditionalInterestInterestList);
    }


    public int getItemsCountByItemType(FarmPersonalPropertyTypes type) {
        return tableUtils.getRowCount(tableUtilsDiv_Items(type));
    }

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExisting;

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement link_AdditionalInterests_OtherContact;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterests_OtherContactArrow;


    public void addExistingOtherContactsAdditionalInterest(String name) {
        
        clickWhenClickable(button_AdditionalInterests_AddExisting);
        
        if (!checkIfElementExists(link_AdditionalInterests_OtherContact, 1000)) {
            clickWhenClickable(button_AdditionalInterests_AddExisting);
            
        }
        hoverOverAndClick(link_AdditionalInterests_OtherContact);
        
        hoverOverAndClick(link_AdditionalInterests_OtherContactArrow);
        
        String xPath = "//span[contains(text(), '" + name + "')]/parent::a/parent::div";
        WebElement lienHolder = find(By.xpath(xPath));
        clickWhenClickable(lienHolder);
    }
}
