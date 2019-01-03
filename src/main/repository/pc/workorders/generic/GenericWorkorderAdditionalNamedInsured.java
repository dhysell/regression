package repository.pc.workorders.generic;


import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.helpers.TableUtils;
import repository.pc.contact.ContactEditPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderAdditionalNamedInsured extends GenericWorkorder {
	
	private WebDriver driver;

    public GenericWorkorderAdditionalNamedInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:GlobalPersonNameInputSet:FirstName-inputEl')]")
    private WebElement text_AdditionalInsuredFirstName;
    public void setAdditionalInsuredFirstName(String firstName) {
        setText(text_AdditionalInsuredFirstName, firstName);
    }
    
    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:GlobalPersonNameInputSet:MiddleName-inputEl')]")
    private WebElement text_AdditionalInsuredMiddleName;
    public void setAdditionalInsuredMiddleName(String middleName) {
        setText(text_AdditionalInsuredMiddleName, middleName);
    }
    
    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:GlobalPersonNameInputSet:LastName-inputEl')]")
    private WebElement text_AdditionalInsuredLastName;
    
    public void setAdditionalInsuredLastName(String lastName) {
        setText(text_AdditionalInsuredLastName, lastName);
    }

    private String additionalInsuredRelationship = "//table[contains(@id, ':PolicyContactRoleInputSet:Relationship-triggerWrap')]";


    public void selectAdditionalInsuredRelationship(RelationshipToInsured relationship) {
        Guidewire8Select relationshipSelect = new Guidewire8Select(driver, additionalInsuredRelationship);
        relationshipSelect.selectByVisibleText(relationship.getValue());

    }

    private String additionalInsuredAddressListing = "//table[contains(@id, ':FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]";


    public void selectAddtionalInsuredAddress(String addressline) {
        if (addressline.contains("New")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
                return;
            }
        }
        Guidewire8Select relationshipSelect = new Guidewire8Select(driver, additionalInsuredAddressListing);
        relationshipSelect.selectByVisibleTextPartial(addressline);

    }


    public void clickUpdate() {
        super.clickUpdate();
    }

    @FindBy(xpath = "//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")
    private WebElement link_ReturnToEditAdditionalNamedInsured;


    public void clickReturnToEditAdditionalInsured() {
        clickWhenClickable(link_ReturnToEditAdditionalNamedInsured);
    }


    public boolean isExistsReturnToEditAdditionalInsured() {
        return checkIfElementExists(link_ReturnToEditAdditionalNamedInsured, 5000);
    }

    @FindBy(xpath = "//a[contains(@id,'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV:') and contains(.,'Select')]")
    public WebElement link_selectMatchingContact;


    public void clickSelectMatchingContact() {
        clickWhenClickable(link_selectMatchingContact);
    }

    @FindBy(xpath = "//textarea[contains(@id,':ContactChangeReason_FBM-inputEl')]")
    public WebElement editbox_ReasonForContactChange;


    public void setReasonForContactChange(String reason) {
        setText(editbox_ReasonForContactChange, reason);
    }


    public boolean isSelectMatchingContactDisplayed() {
        return checkIfElementExists(link_selectMatchingContact, 5000);
    }

    @FindBy(xpath = "//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    private WebElement overrideButton;

    public void clickOverrideButton() {
        clickWhenClickable(overrideButton);
    }


    public boolean isOverrideButtonDisplayed() {
        return checkIfElementExists(overrideButton, 5000);
    }
    
    public ArrayList<String> getAdditionalInsuredsName() {
    	waitUntilElementIsClickable(By.xpath("//div[contains(@id, ':AdditionalInsuredLV') and not(contains(@id,'-body'))]"));
    	TableUtils tableHelp = new TableUtils(driver);
    	return tableHelp.getAllCellTextFromSpecificColumn(driver.findElement(By.xpath("//div[contains(@id, ':AdditionalInsuredLV') and not(contains(@id,'-body'))]")), "Name");
    }
    
    public boolean importAddress(String lastNameOfImportee, String firstName, String companyNameOrLastName, String address, String city, State state, String zip) {
    	repository.pc.contact.ContactEditPC editContact = new ContactEditPC(this.driver);
    	return editContact.importAddress(lastNameOfImportee, firstName, companyNameOrLastName, address, city, state, zip);
    }
    
    @FindBy(xpath = "//span[contains(@id, ':PolicyContactRoleDetailsCV:ContactRelatedContactsCardTab-btnEl')]")
    private WebElement link_EditAdditionalNamedInsuredRelatedContact;
    
    @FindBy(xpath = "//span[contains(@id, ':ChangeRelatedToButton')]")
    private WebElement button_EditAdditionalNamedInsuredRelatedContactChangeRelatedContact;
        
    public boolean ensureRelatedContactIsUnEditable() {
    	clickWhenClickable(link_EditAdditionalNamedInsuredRelatedContact);
    	if(checkIfElementExists(button_EditAdditionalNamedInsuredRelatedContactChangeRelatedContact, 1)) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
