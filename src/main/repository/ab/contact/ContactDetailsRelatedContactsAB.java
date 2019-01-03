package repository.ab.contact;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.enums.RelatedTo;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.RelatedContacts;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsRelatedContactsAB extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ContactDetailsRelatedContactsAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:ContactBasicsCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasics;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:PaidDuesCardTab-btnEl')]")
    private WebElement link_ContactDetailsBasicsPaidDues;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:RelatedContactsLV_tb:Update-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsUpdate;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:RelatedContactsLV_tb:Cancel-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsCancel;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:RelatedContactsLV_tb:Add-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsAdd;

    @FindBy(xpath = "//span[contains(@id,':ABContactDetailScreen:RelatedContactsLV_tb:Remove-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsRemove;

    @FindBy(xpath = "//span[contains(@id,'ContactDetail:ABContactDetailScreen:RelatedContactsLV_tb:Edit-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsEdit;

    @FindBy(xpath = "//span[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV_tb:ToolbarButton-btnEl')]")
    private WebElement button_ContactDetailsRelatedContactsAddDependents;

    @FindBy(xpath = "//span[contains(@id,'AddDependentsPopup:DependentContactsLV_tb:Add-btnEl')]")
    private WebElement button_ContactDetailsAddDependentsAdd;

    @FindBy(xpath = "//span[contains(@id,'AddDependentsPopup:DependentContactsLV:0:Contact:ContactMenuIcon')]")
    private WebElement button_ContactDetailsAddDependentsNameIcon;

    private WebElement button_ContactDetailsAddDependentsNameIcon(int row) {
        return find(By.xpath("//img[contains(@id, 'ContactsLV:" + Integer.toString(row) + ":Contact:ContactMenuIcon')]/parent::div"));
    }

    @FindBy(xpath = "//div[contains(@id,'AddDependentsPopup:PeopleLV')]")
    private WebElement table_ContactDetailsAddDependents;

    @FindBy(xpath = "//span[contains(@id,'Contact:MenuItem_Search')]")
    private WebElement button_ContactDetailsAddDependentsNameSearch;

    @FindBy(xpath = "//div[contains(@id,'AddDependentsPopup:DependentContactsLV') or contains(@id,'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]")
    private WebElement table_ContactDetailsAddDependentsNameSearch;

    private Guidewire8Select select_ContactDetailsRelatedContactsSpouse() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'AddDependentsPopup:Spouse-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'AddDependentsPopup:PeopleLV_tb:SelectButton-btnEl')]")
    private WebElement button_ContactDetailsAddDependentsSelect;

    @FindBy(xpath = "//div[contains(@id,':ABContactDetailScreen:RelatedContactsLV') or contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]")/*ContactDetail:ABContactDetailScreen:0*/
    private WebElement table_ContactDetailsRelatedContacts;

    /*
     * 				Methods
     * */

    public void clickEdit() {
        super.clickEdit();
    }

    public boolean clickContactDetailsBasicsLink() {
        
        if (checkIfElementExists(link_ContactDetailsBasics, 2000)) {
            clickWhenClickable(link_ContactDetailsBasics);
            return true;
        } else {
            return false;
        }
    }

    public boolean clickContactDetailsBasicsPaidDuesLink() {
        
        if (checkIfElementExists(link_ContactDetailsBasicsPaidDues, 2000)) {
            clickWhenClickable(link_ContactDetailsBasicsPaidDues);
            return true;
        } else {
            return false;
        }
    }

    private void clickAddDependents() {
        waitUntilElementIsClickable(button_ContactDetailsRelatedContactsAddDependents);
        button_ContactDetailsRelatedContactsAddDependents.click();
    }

    private void clickAddDependentsAdd() {
        waitUntilElementIsClickable(button_ContactDetailsAddDependentsAdd);
        button_ContactDetailsAddDependentsAdd.click();
    }

    private void clickDependentsSelect() {
        waitUntilElementIsClickable(button_ContactDetailsAddDependentsSelect);
        button_ContactDetailsAddDependentsSelect.click();
    }

    public void addSpouse(Contact spouse) {
        clickEdit();
        if (checkIfElementExists("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]/div[2]/div/div/div/div/span/div", 1200)) {
            find(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]/div[2]/div/div/div/div/span/div")).click();
            clickWhenClickable(button_ContactDetailsRelatedContactsRemove);
        }
        clickAdd();
        
        tableUtils.selectValueForSelectInTable(table_ContactDetailsRelatedContacts, tableUtils.getRowCount(table_ContactDetailsRelatedContacts), "Relationship", "Spouse");
        int row = tableUtils.getRowCount(table_ContactDetailsRelatedContacts);
        button_ContactDetailsAddDependentsNameIcon(row - 1).click();
        clickSearch();
        repository.ab.search.AdvancedSearchAB search = new repository.ab.search.AdvancedSearchAB(driver);
        search.searchByFirstLastNameSelect(spouse.getFirstName(), null, spouse.getLastName(), spouse.getAddress().getLine1());
//		tableUtils.selectValueForSelectInTable(table_ContactDetailsAddDependentsNameSearch, tableUtils.getNextAvailableLineInTable(table_ContactDetailsAddDependentsNameSearch, "Relationship"), "Relationship", gw.enums.RelatedContacts.Spouse.getValue());
        clickUpdate();
    }

    public void addSpouseByAcct(String acctNum) {
        if (checkIfElementExists("//a[contains(@id, ':EditLink') or contains(@id, ':Edit') or contains(@id, ':editbutton') or contains(@id, 'viewEdit_LinkAsBtn')]", 2000)) {
            clickEdit();
        }
        if (checkIfElementExists("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]/div[2]/div/div/div/div/span/div", 1200)) {
            find(By.xpath("//div[contains(@id, 'ContactDetail:ABContactDetailScreen:RelatedContactsLV')]/div[2]/div/div/div/div/span/div")).click();
            clickWhenClickable(button_ContactDetailsRelatedContactsRemove);
        }
        clickAdd();
        
        tableUtils.selectValueForSelectInTable(table_ContactDetailsRelatedContacts, tableUtils.getRowCount(table_ContactDetailsRelatedContacts), "Relationship", "Spouse");
        int row = tableUtils.getRowCount(table_ContactDetailsRelatedContacts);
        button_ContactDetailsAddDependentsNameIcon(row - 1).click();
        clickSearch();
        repository.ab.search.AdvancedSearchAB search = new repository.ab.search.AdvancedSearchAB(driver);
        search.searchByAccountNumber(acctNum);
        clickUpdate();
    }

    private void selectSpouse(String spouse) {
        Guidewire8Select spouseSelect = select_ContactDetailsRelatedContactsSpouse();
        spouseSelect.selectByVisibleText(spouse);
    }

    private void selectSpouse(List<RelatedContacts> contactsArray) {
        for (RelatedContacts contact : contactsArray) {
            if (contact.getRelation().equals(repository.gw.enums.RelatedContacts.Spouse)) {
                selectSpouse(contact.getFirstName() + " " + contact.getLastName());
            }
            
        }
    }

    /*
        public void addDependents(List<RelatedContacts> dependentsToAdd) {
            for (RelatedContacts contact : dependentsToAdd) {
                if ((contact.getLastNameAddressMatch() == true) && (contact.getRelation().equals(gw.enums.RelatedContacts.ChildWard))) {
                    
                    tableUtils.setCheckboxInTableByText(table_ContactDetailsAddDependents, contact.getFirstName(), true);
                    clickDependentsSelect();
                    tableUtils.selectValueForSelectInTable(table_ContactDetailsAddDependentsNameSearch, tableUtils.getNextAvailableLineInTable(table_ContactDetailsAddDependentsNameSearch, "Relationship"), "Relationship", gw.enums.RelatedContacts.ChildWard.getValue());
                }
            }
        }
    */
    public void addDependentsSearch(List<RelatedContacts> dependentsToAdd) {
        if (checkIfElementExists(button_ContactDetailsRelatedContactsAddDependents, 1000)) {
            clickAddDependents();
        }
        for (RelatedContacts contact : dependentsToAdd) {
            if (contact.getLastNameAddressMatch() == false) {
                clickAdd();
                int row = tableUtils.getNextAvailableLineInTable(table_ContactDetailsAddDependentsNameSearch, "Relationship");
                button_ContactDetailsAddDependentsNameIcon(tableUtils.getNextAvailableLineInTable(table_ContactDetailsAddDependentsNameSearch, "Relationship") - 1).click();
                clickSearch();
                repository.ab.search.AdvancedSearchAB search = new repository.ab.search.AdvancedSearchAB(driver);
                search.searchByFirstLastNameSelect(contact.getFirstName(), contact.getMiddleInitial(), contact.getLastName(), contact.getAddress().getLine1());
                tableUtils.selectValueForSelectInTable(table_ContactDetailsAddDependentsNameSearch, tableUtils.getNextAvailableLineInTable(table_ContactDetailsAddDependentsNameSearch, "Relationship"), "Relationship", repository.gw.enums.RelatedContacts.ChildWard.getValue());
            }
        }
    }

    public void addAllDependents(List<RelatedContacts> dependentsToAdd) throws Exception {
        addDependentsSearch(dependentsToAdd);
        clickUpdate();
    }

    public void removeDependants() {
        clickEdit();
        
        tableUtils.setTableTitleCheckAllCheckbox(table_ContactDetailsRelatedContacts, true);
        clickRemove();
        clickUpdate();
    }

    public String getRelatedContactsRelationship(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ContactDetailsRelatedContacts, row, "Relationship");
    }

    public String getRelatedContactsName(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ContactDetailsRelatedContacts, row, "Name");
    }

    public String findNameBasedOnRelationship(RelatedTo relationship) {
        waitUntilElementIsVisible(table_ContactDetailsRelatedContacts);
        if (checkIfElementExists("//div[contains(., '" + relationship.getValue() + "')]", 200)) {
            return tableUtils.getCellTextInTableByRowAndColumnName(table_ContactDetailsRelatedContacts, tableUtils.getRowInTableByColumnNameAndValue(table_ContactDetailsRelatedContacts, "Relationship", relationship.getValue()), "Name");
        } else {
            return "The relationship of " + relationship.getValue() + " does not exist.";
        }
    }

    public void removeRelationship(RelationshipsAB relationship) {
        clickEdit();
        waitUntilElementIsVisible(table_ContactDetailsRelatedContacts);
        int agentOfRow = tableUtils.getRowNumberInTableByText(table_ContactDetailsRelatedContacts, relationship.getValue());
        
        tableUtils.setCheckboxInTable(table_ContactDetailsRelatedContacts, agentOfRow, true);
        clickRemove();
        clickUpdate();
    }

    public void removeAllRelationships() {
        
        waitUntilElementIsVisible(table_ContactDetailsRelatedContacts);
        ArrayList<String> relationships = tableUtils.getAllCellTextFromSpecificColumn(table_ContactDetailsRelatedContacts, "Relationship");
        for (String relationship : relationships) {
            removeRelationship(RelationshipsAB.getEnumFromStringValue(relationship));
        }

    }

    public void setRelationship(RelationshipsAB relationship, Contact relatedContact) throws Exception {
        clickEdit();
        clickAdd();
        tableUtils.selectValueForSelectInTable(table_ContactDetailsRelatedContacts, tableUtils.getNextAvailableLineInTable(table_ContactDetailsRelatedContacts), "Relationship", relationship.getValue());
        int row = tableUtils.getRowNumberInTableByText(table_ContactDetailsRelatedContacts, relationship.getValue());
        button_ContactDetailsAddDependentsNameIcon(tableUtils.getRowNumberInTableByText(table_ContactDetailsRelatedContacts, relationship.getValue()) - 1).click();
        clickSearch();
        repository.ab.search.AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        switch (relationship) {
            case Agent:
                searchMe.selectContactByCompanyName(relatedContact.getCompanyName());
                break;

            case AgentOf:
                searchMe.selectContactByName(relatedContact.getCompanyName());
                break;
	            
/*	        case AssignedCase : 
	        	searchMe.selectContactByCompanyName(relatedContact.getCompanyName());
	        	break;
*/
            case AssignedCase:
            case CollectionAgency:
            case CommonOwnership:
            case DBA:
            case DbaFor:
            case ThirdPartyInsured:
            case ThirdPartyInsurer:
            case CountyFor:
                searchMe.selectContactByCompanyName(relatedContact.getCompanyName());
                break;

            case County:
                searchMe.selectContactByRoleName(ContactRole.County, relatedContact.getCompanyName() + " County");
                break;

            default:
                if (relatedContact.getPersonOrCompany().equals(ContactSubType.Person)) {
                    searchMe.searchByFirstLastNameSelect(relatedContact.getFirstName(), relatedContact.getMiddleName(), relatedContact.getLastName(), relatedContact.getAddress().getLine1());
                } else {
                    searchMe.searchCompanyByNameSelect(relatedContact.getCompanyName(), relatedContact.getAddress().getLine1());
                }
        }
        clickUpdate();
    }

    public void changeRelationship(RelationshipsAB oldRelationship, RelationshipsAB newRelationship) {
        int row = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_ContactDetailsRelatedContacts, "Relationship", oldRelationship.getValue()));
        tableUtils.selectValueForSelectInTable(table_ContactDetailsRelatedContacts, row, "Relationship", newRelationship.getValue());
        clickUpdate();
    }


}
