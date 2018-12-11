package repository.pc.workorders.generic;

import com.idfbins.enums.State;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireException;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.contact.ContactEditPC;
import repository.pc.search.SearchAddressBookPC;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenericWorkorderAdditionalInterests extends ContactEditPC {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderAdditionalInterests(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutAdditionalInterest(boolean basicSearch, AdditionalInterest additionalInterest) throws GuidewireNavigationException {
        clickSearch();
        repository.pc.search.SearchAddressBookPC search = new repository.pc.search.SearchAddressBookPC(driver);
        search.searchForContact(basicSearch, additionalInterest);
        	// Commented out for Create new Lienholder change - Steve Broderick 5/9/2018
/*        if (additionalInterest.getNewContact() == CreateNew.Create_New_Always || (additionalInterest.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist && !found)) {
            setAddressListing("New...");
            setContactEditAddressLine1(additionalInterest.getAddress().getLine1());
            setContactEditAddressCity(additionalInterest.getAddress().getCity());
            setContactEditAddressState(additionalInterest.getAddress().getState());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            setContactEditAddressZipCode(additionalInterest.getAddress().getZip());
            setContactEditAddressAddressType(additionalInterest.getAddress().getType());
            if (additionalInterest.getPhone() == null) {
                additionalInterest.setPhone("208" + String.valueOf(NumberUtils.generateRandomNumberDigits(7)));
            }
            setBusinessPhone(additionalInterest.getPhone());
        }
*/
        //LH name to click on buildings page
        additionalInterest.setLienholderNameFromPolicyCenter(getAdditionalInterestsName());

        //Set AI Type
        switch (additionalInterest.getAdditionalInterestSubType()) {
            case BOPBuilding:
                setBOPBuilding(additionalInterest);
                break;
            case CAVehicles:
                setCAVehicles(additionalInterest);
                break;
            case PLSectionIProperty:
                setPLPropertyAI(additionalInterest);
                break;
            case PLFarmEquipment:
                setPLFarmEquipmemtAI(additionalInterest);
                break;
            case PLSectionIIIAuto:
                setPLAutoAI(additionalInterest);
                break;
            case PLSectionIVRecreationalEquipment:

                break;
            case PLSectionIVWatercraft:

                break;
            case CPProperty:
                setCPProperty(additionalInterest);
                break;
            case StandardFire:
                break;
            case StandardInlandMarine:
                selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestType().getValue());
                break;
            case StandardLiability:
                break;
        }

        setAdditionalInterestsLoanNumber(additionalInterest.getLoanContractNumber());

        if (!finds(By.xpath("//a[contains(@id, ':ContactRelatedContactsCardTab')]")).isEmpty()) {
            clickRelatedContactsTab();
        }
//        clickUpdate();
//        if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
//            clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        }
//        if (new GuidewireHelpers(driver).duplicateContacts()) {
//            clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        }
//
//        //return to AI page to validate information
//        clickBuildingsPropertyAdditionalInterestsLink(additionalInterest.getLienholderNameFromPolicyCenter());

        if (new GuidewireHelpers(driver).verifyAddress(additionalInterest.getAddress())) {
            //sendEmail(additionalInterest);
            System.out.println("Property Additional Interest Did Not Match Selected Additional Interest: " + additionalInterest.getCompanyName());
        }

        //This was the original setter for payer assignment. It has changed to now include the Location and Building Numbers in the string.
        //I don't yet have a great way to get these values, so I will leave the original, now inadequate method in use below and will change the
        //downstream verification method to not be so stringent for now. The commented out method below is correct and will be implemented at a later date.
        if (additionalInterest.getLoanContractNumber().equalsIgnoreCase("")) {
            additionalInterest.setLienholderPayerAssignmentString(getAdditionalInterestsName() + ": " + additionalInterest.getAddress().getLine1() + ", " + additionalInterest.getAddress().getCity() + ", " + additionalInterest.getAddress().getState().getName() + ": No Loan Number");
        } else {
            additionalInterest.setLienholderPayerAssignmentString(getAdditionalInterestsName() + ": " + additionalInterest.getAddress().getLine1() + ", " + additionalInterest.getAddress().getCity() + ", " + additionalInterest.getAddress().getState().getName() + ": " + additionalInterest.getLoanContractNumber());
        }
		/*if (additionalInterest.getLoanContractNumber().equalsIgnoreCase("")) {
			additionalInterest.setLienholderPayerAssignmentString(getAdditionalInterestsName() + ": " + additionalInterest.getAddress().getLine1() + ", " + additionalInterest.getAddress().getCity() + ", " + additionalInterest.getAddress().getState() + ": No Loan Number" + " (Loc " + String.valueOf(additionalInterest.getLocationNumberTiedToLH()) + "/Bldg " + String.valueOf(additionalInterest.getBuildingNumberTiedToLH()) + ")");
		} else {
			additionalInterest.setLienholderPayerAssignmentString(getAdditionalInterestsName() + ": " + additionalInterest.getAddress().getLine1() + ", " + additionalInterest.getAddress().getCity() + ", " + additionalInterest.getAddress().getState() + ": " + additionalInterest.getLoanContractNumber() + " (Loc " + String.valueOf(additionalInterest.getLocationNumberTiedToLH()) + "/Bldg " + String.valueOf(additionalInterest.getBuildingNumberTiedToLH()) + ")");
		}*/
        if (!finds(By.xpath("//a[contains(@id, ':ContactRelatedContactsCardTab')]")).isEmpty()) {
            clickRelatedContactsTab();
        }

        clickBuildingsPropertyAdditionalInterestsUpdateButton();
        if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
            clickBuildingsPropertyAdditionalInterestsUpdateButton();
        }
        if (new GuidewireHelpers(driver).duplicateContacts()) {
            clickBuildingsPropertyAdditionalInterestsUpdateButton();
        }
        //set LH information
        additionalInterest.setLienholderNumber(getAdditionalInterestAccountNumber(additionalInterest.getCompanyOrInsured().equals(ContactSubType.Company)? additionalInterest.getCompanyName() : additionalInterest.getPersonFirstName() + " " + additionalInterest.getPersonLastName()));
    }


    private void setCPProperty(AdditionalInterest additionalInterest) {
        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestTypeCPP().getValue());
    }


    private void setCAVehicles(AdditionalInterest additionalInterest) {
        if (finds(By.xpath("//table[contains(@id, ':InterestType-triggerWrap')]")).isEmpty()) {
            clickWhenClickable(find(By.xpath("//span[contains(@id, ':FinishPCR-btnEl')]")));
            clickWhenClickable(find(By.xpath("//a[contains(text(), '" + additionalInterest.getCompanyName() + "')]")));
        }
        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestTypeCPP().getValue());
    }

    private void setPLPropertyAI(AdditionalInterest additionalInterest) {

        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestType().getValue());

        checkBuildingsPropertyAdditionalInterestsFirstMortgage(additionalInterest.isFirstMortgage());
    }

    private void setPLFarmEquipmemtAI(AdditionalInterest additionalInterest) {

        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestType().getValue());
    }

    private void setBOPBuilding(AdditionalInterest additionalInterest) {
        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestType().getValue());

        if (!(additionalInterest.getAdditionalInterestType() == AdditionalInterestType.Additional_Insured_Building_Owner) && !(additionalInterest.getAdditionalInterestType() == AdditionalInterestType.Building_Owner_Loss_Payable)) {
            checkBuildingsPropertyAdditionalInterestsFirstMortgage(additionalInterest.isFirstMortgage());
            checkBuildingsPropertyAdditionalInterestsAppliedToBuilding(additionalInterest.isAppliedToBuilding());
            checkBuildingsPropertyAdditionalInterestsAppliedToBPP(additionalInterest.isAppliedToBPP());
        }
    }

    public void setPLAutoAI(AdditionalInterest additionalInterest) {
        selectBuildingsPropertyAdditionalInterestsInterestType(additionalInterest.getAdditionalInterestType().getValue());
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//textarea[contains(@id,':ContactChangeReason_FBM-inputEl')]")
    private WebElement textArea_ReasonForContactChange;

    @FindBy(xpath = "//a[contains(@id,'AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_SubmissionBuildingsPropertyAdditionalInterestsAddExisting;

    @FindBy(xpath = "//a[contains(@id,'AdditionalInterestLV_tb:AddContactsButton:AddOtherContact')]")
    private WebElement button_SubmissionBuildingsPropertyAdditionalInterestsOtherContacts;

    @FindBy(xpath = "//a[contains(@id,'AddContactsButton:AddOtherContact:0:acctContact')]")
    private WebElement button_SubmissionBuildingsPropertyAdditionalInterestsOtherContactsFirstOption;

    @FindBy(xpath = "//a[contains(@id,'AdditionalInterestLV_tb:Remove')]")
    private WebElement button_SubmissionBuildingsPropertyAdditionalInterestsRemove;

    @FindBy(xpath = "//div[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:AdditionalInterestDetailsDV') or contains(@id, 'HOBuilding_FBMPopup:AdditionalInterestDetailsDV:AdditionalInterestLV') or contains(@id, 'AdditionalInterestDetailsDV:AdditionalInterestLV') or contains(@id, ':HOCoveragesPanelSet:SectionTwoAdditionalInterestsRowSetPanelSet:0')]")
    public WebElement table_SubmissionBuildingsPropertyAdditionalInterestsResults;

    @FindBy(xpath = "//a[contains(@id, '__crumb__')]")
    private WebElement link_ReturnToCoverages;

    @FindBy(xpath = "//a[contains(@id, ':HOCoveragesPanelSet:SectionTwoAdditionalInterestsRowSetPanelSet:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExistingFromStdFire;

    @FindBy(xpath = "//a[contains(@id, ':HOCoveragesPanelSet:SectionTwoAdditionalInterestsRowSetPanelSet:AddContactsButton:0:stdFirePolicyLabel-itemEl')]")
    private WebElement link_AdditionalInterests_NonBoundStdFire;

    @FindBy(xpath = "//div[contains(@id, ':HOCoveragesPanelSet:SectionTwoAdditionalInterestsRowSetPanelSet:AddContactsButton:0:stdFirePolicyLabel-arrowEl')]")
    private WebElement link_AdditionalInterests_NonBoundStdFireArrow;

    @FindBy(xpath = "//a[contains(@id, ':ContactRelatedContactsCardTab')]")
    private WebElement tab_RelatedContactsTab;

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExisting;

    @FindBy(xpath = "//a[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-itemEl') or contains(@id, ':AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterest;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-arrowEl')or contains(@id, ':AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterests_ExistingAdditionalInterestArrow;


    public void clickCancel() {
        super.clickCancel();
    }


    public void setReasonForContactChange(String reason) {
        setText(textArea_ReasonForContactChange, reason);
    }

    public Guidewire8Select select_AdditionalInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':InterestType-triggerWrap')]");
    }

    public Guidewire8Select select_AdditionalInterestAddressListing() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]");
    }

    public Guidewire8Checkbox checkBox_AdditionalInterestFirstMortgage() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'AdditionalInterestInputSet:FirstMortgage') or contains(@id, 'FBM_PolicyContactDetailsDV:FirstMortgage') or contains(@id, 'AdditionalInterestInfoDV:FirstMortgage')]");
    }

    public Guidewire8Checkbox checkBox_AdditionalInterestAppliedToBuilding() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'FBAdditionalInterestInputSet:AppliedBuilding') or contains(@id, ':AdditionalInterestInfoDV:AppliedBuilding')]");
    }

    public Guidewire8Checkbox checkBox_AdditionalInterestAppliedToBPP() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'FBAdditionalInterestInputSet:AppliedContents') or contains(@id, 'AdditionalInterestInfoDV:AppliedContents')]");
    }

    @FindBy(xpath = "//div[contains(@id, 'GlobalAddressInputSet:AddressLine1-inputEl')] | //input[contains(@id, ':AddressLine1-inputEl')]")
    public WebElement label_AdditionalInterestlienholderAddressLine1;

    @FindBy(xpath = "//input[contains(@id, 'FBAdditionalInterestInputSet:ContractNumber-inputEl')] | //input[contains(@id, 'FBM_PolicyContactDetailsDV:LoanNumber-inputEl') or contains(@id, ':AdditionalInterestInfoDV:ContractNumber-inputEl')]")
    public WebElement editBox_AdditionalInterestLoanNumber;

    @FindBy(xpath = "//div[contains(@id, 'FBLienholderInputSet:LienholderNumber-inputEl')]")
    public WebElement label_AdditionalInterestLienHolderNumber;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:CountryInputSet:OfficeNumber-inputEl')]")
    public WebElement label_AdditionalInterestLienHolderOfficeNumber;

    @FindBy(xpath = "//input[contains(@id, 'GlobalContactNameInputSet:Name-inputEl')] | //div[contains(@id, 'GlobalContactNameInputSet:Name-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderName;

    @FindBy(xpath = "//div[contains(@id, ':GlobalAddressInputSet:State-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderState;

    @FindBy(xpath = "//div[contains(@id, ':GlobalAddressInputSet:City-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderCity;

    @FindBy(xpath = "//div[contains(@id, ':GlobalAddressInputSet:PostalCode-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderZip;

    @FindBy(xpath = "//div[contains(@id, ':GlobalAddressInputSet:County-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderCounty;

    @FindBy(xpath = "//div[contains(@id, 'FBM_PolicyContactDetailsDV:CountryInputSet:AddressType-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderAddressType;

    /**
     * bmartin 02/09/2016
     * Created List WebElements to be able to check if elements on a page is editable when it should or should not be.
     * Only needed a few at this time. Add more later if needed.
     */
    @FindBy(xpath = "//input[contains(@id, 'GlobalContactNameInputSet:Name-inputEl')]")
    public List<WebElement> listEditBox_AdditionalInterestLienHolderName;

    @FindBy(xpath = "//div[contains(@id, ':GlobalContactNameInputSet:Name-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderNameDIV;

    @FindBy(xpath = "//input[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderFirstName;

    @FindBy(xpath = "//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")
    public WebElement textBox_AdditionalInterestLienHolderFirstName;

    @FindBy(xpath = "//input[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderMiddleName;

    @FindBy(xpath = "//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")
    public WebElement textBox_AdditionalInterestLienHolderMiddleName;

    @FindBy(xpath = "//input[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")
    public WebElement editBox_AdditionalInterestLienHolderLastName;

    @FindBy(xpath = "//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")
    public WebElement textBox_AdditionalInterestLienHolderLastName;

    // jlarsen 11/9/2015
    @FindBy(xpath = "//input[contains(@id, 'PolicyContactRoleNameInputSet:SSN-inputEl')] | //div[contains(@id, 'PolicyContactRoleNameInputSet:SSN-inputEl') | //input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:SSN-inputEl')]")
    public WebElement editBox_AdditionalInterestSSN;

    @FindBy(xpath = "//input[contains(@id, 'PolicyContactRoleNameInputSet:SSN-inputEl')] | //div[contains(@id, 'PolicyContactRoleNameInputSet:SSN-inputEl') | //input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:SSN-inputEl')]")
    public List<WebElement> listEditBox_AdditionalInterestSSN;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:TIN-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:TIN-inputEl') | //input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:TIN-inputEl')]")
    public WebElement editBox_AdditionalInterestTIN;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:TIN-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:TIN-inputEl') | //input[contains(@id, ':PolicyContactRoleNameAdditionalInputsInputSet:TIN-inputEl')]")
    public List<WebElement> listEditBox_AdditionalInterestTIN;

    @FindBy(xpath = "//input[contains(@id, ':BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')] | //div[contains(@id, ':BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editBox_AdditionalInterestBusinessPhone;

    //bmartin 02/08/2016
    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress1-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress1-inputEl')]")
    private WebElement editBox_AdditionalInterestMainEmail;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress1-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress1-inputEl')]")
    private List<WebElement> listEditBox_AdditionalInterestMainEmail;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress2-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress2-inputEl')]")
    private WebElement editBox_AdditionalInterestAlternateEmail;

    @FindBy(xpath = "//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress2-inputEl')] | //div[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress2-inputEl')]")
    private List<WebElement> listEditBox_AdditionalInterestAlternateEmail;

    @FindBy(xpath = "//a[contains(@id,':FPPAddlInterestItemsPanelSet:Add')]")
    private WebElement button_BuildingsPropertyAdditionalInterestsMachineryAdd;

    @FindBy(xpath = "//div[contains(@id,'EditAdditionalInterestPopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:')]")
    public static List<WebElement> text_BuildingsPropertyAdditionalInterestsDeliveryOption;

    @FindBy(xpath = "//div[contains(@id, ':DeliveryOptionsInputSet:deliveryOption-inputEl')]")
    private WebElement text_PropertyAdditionalInterestsDeliveryOption;

    @FindBy(xpath = "//div[contains(@id,':PolicyContactRoleDetailsCV:RelatedContactsPanelSet:0')]")
    public WebElement table_RelatedContacts;

    @FindBy(xpath = "//span[contains(@id,'NewRelatedContactPopup:ContactDetailScreen:ForceDupCheckUpdate-btnEl')]")
    public WebElement click_RelatedContactsUpdate;

    @FindBy(xpath = "//a[contains(@id,':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:AddressListing:AddressListingMenuIcon')]")
    public WebElement button_PropertyAdditionlInterestCreateNewAddress;

    
    @FindBy(xpath = "//span[contains(@id,':ImportAddressesButton-btnEl')]")
    public WebElement button_PropertyAdditionlInterestImportAddress;
    


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public WebElement getAdditionalInterestTableRow(String name, AdditionalInterestType type, String loanContractNumber, Boolean yesNo, String relatedContact) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (name != null) {
            columnRowKeyValuePairs.put("Name", name);
        }
        if (type != null) {
            columnRowKeyValuePairs.put("Interest Type", type.getValue());
        }
        if (loanContractNumber != null) {
            columnRowKeyValuePairs.put("Loan or Contract Number", loanContractNumber);
        }
        if (yesNo != null) {
            if (yesNo) {
                columnRowKeyValuePairs.put("Invoice", "Yes");
            } else {
                columnRowKeyValuePairs.put("Invoice", "No");
            }

        }
        if (relatedContact != null) {
            columnRowKeyValuePairs.put("Amount", relatedContact);
        }

        return tableUtils.getRowInTableByColumnsAndValues(table_SubmissionBuildingsPropertyAdditionalInterestsResults, columnRowKeyValuePairs);
    }

    public boolean createNewAddressButtonExist() {
    	waitUntilElementIsVisible(By.xpath("//label[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:1') and contains(., 'Designated Address')]"));
    	return checkIfElementExists(button_PropertyAdditionlInterestCreateNewAddress, 2000);
    }
    
    public boolean importAddressbuttonExists() {
    	waitUntilElementIsVisible(By.xpath("//label[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:1') and contains(., 'Designated Address')]"));
		return checkIfElementExists(button_PropertyAdditionlInterestImportAddress, 2000);
    }
    
    public boolean changeAddressSelectListExists() {
    	waitUntilElementIsVisible(By.xpath("//label[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:1') and contains(., 'Designated Address')]"));
    	return checkIfElementExists("//table[contains(@id, ':FBM_PolicyContactDetailsDV:AddressListing-triggerWrap')]", 2000);
    }

    public void clickBuildingsPropertyAdditionalInterestsSearch() {
        super.clickSearch();
        long stopTime = new Date().getTime() + 2000;

        while (finds(By.xpath("//span[contains(text(), 'Search Address Book')]")).size() <= 0 && new Date().getTime() < stopTime) {
            super.clickSearch();
        }
    }


    public void clickBuildingsPropertyAdditionalInterestsUpdateButton() {
        super.clickUpdate();
    }


    public void clickBuildingsPropertyAdditionalInterestsFirstOtherContact() {
        clickWhenClickable(button_SubmissionBuildingsPropertyAdditionalInterestsAddExisting);
        clickWhenClickable(button_SubmissionBuildingsPropertyAdditionalInterestsOtherContacts);
        clickWhenClickable(button_SubmissionBuildingsPropertyAdditionalInterestsOtherContactsFirstOption);
    }


    public void clickBuildingsPropertyAdditionalInterestsRemove() {
        clickWhenClickable(button_SubmissionBuildingsPropertyAdditionalInterestsRemove);
    }


    public void clickBuildingsPropertyAdditionalInterestsLink(String additionalInterestName) {
        String[] splitName = additionalInterestName.split(" ");
        ArrayList<String> additionalInterestArray = new ArrayList<String>();
        additionalInterestArray.add(splitName[0]);
        additionalInterestArray.add(splitName[splitName.length - 1]);
        tableUtils.clickLinkInTable(table_SubmissionBuildingsPropertyAdditionalInterestsResults, additionalInterestArray);
        //clickWhenClickable(find(By.xpath("//a[contains(text(), '" + additionalInterestName.replace("\n", "").trim() + "')]")));
    }


    public String getAdditionalInterestAccountNumber(String additionalInterestName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SubmissionBuildingsPropertyAdditionalInterestsResults,tableUtils.getRowInTableByColumnNameAndValue(table_SubmissionBuildingsPropertyAdditionalInterestsResults,"Name",additionalInterestName),"Additional Interest Account #");
    }


    public void removeAdditionalInterest(String contactName) {
    	waitUntilElementIsClickable(By.xpath("//div[contains(@id, 'HOBuilding_FBMPopup:AdditionalInterestDetailsDV:AdditionalInterestLV') and not(contains(@id, '-body'))]//table//img"));
        int rowNumberContainingContactName = tableUtils.getRowNumberInTableByText(table_SubmissionBuildingsPropertyAdditionalInterestsResults, contactName);
        tableUtils.setCheckboxInTable(table_SubmissionBuildingsPropertyAdditionalInterestsResults, rowNumberContainingContactName, true);
        clickBuildingsPropertyAdditionalInterestsRemove();
    }

    @FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:AdditionalInterestDetailsDV:AdditionalInterestLV') and not(contains(@id, '-body'))]")
    private WebElement tableDiv_PropertyInformationAdditionalInterest;
    
    public String getAdditionalInterestNameFromTable(String name) {
    	return tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_PropertyInformationAdditionalInterest, tableUtils.getRowNumberInTableByText(tableDiv_PropertyInformationAdditionalInterest, name), "Name");
    }

    public void selectBuildingsPropertyAdditionalInterestsInterestType(String additionalInterestType) {
        Guidewire8Select mySelect = select_AdditionalInterestType();
        mySelect.selectByVisibleText(additionalInterestType);
    }


    public void checkBuildingsPropertyAdditionalInterestsFirstMortgage() {
        checkBox_AdditionalInterestFirstMortgage().select(true);
    }


    public void checkBuildingsPropertyAdditionalInterestsFirstMortgage(boolean checked) {
        checkBox_AdditionalInterestFirstMortgage().select(checked);
    }


    public void checkBuildingsPropertyAdditionalInterestsAppliedToBuilding() {
        checkBox_AdditionalInterestAppliedToBuilding().select(true);
    }


    public void checkBuildingsPropertyAdditionalInterestsAppliedToBuilding(boolean checked) {
        checkBox_AdditionalInterestAppliedToBuilding().select(checked);
    }


    public void checkBuildingsPropertyAdditionalInterestsAppliedToBPP() {
        checkBox_AdditionalInterestAppliedToBPP().select(true);
    }


    public void checkBuildingsPropertyAdditionalInterestsAppliedToBPP(boolean checked) {
        checkBox_AdditionalInterestAppliedToBPP().select(checked);
    }


    public String getBuildingsPropertyAdditionalInterestsLienHolderAddressLine1() {
        String lienHolderAddressLine1 = label_AdditionalInterestlienholderAddressLine1.getAttribute("value");
        if (lienHolderAddressLine1 == null) {
            return label_AdditionalInterestlienholderAddressLine1.getText();
        } else {
            return lienHolderAddressLine1;
        }

    }


    public void setAdditionalInterestsLoanNumber(String loanNumber) {
    	waitForPageLoad();
    	setText(editBox_AdditionalInterestLoanNumber, loanNumber);
    }


    public String getBuildingsPropertyAdditionalInterestsLienHolderNumber(AdditionalInterestSubType additionalInterestSubType) {
        waitUntilElementIsVisible(label_AdditionalInterestLienHolderNumber, 1);
        String lienHolderNumber = label_AdditionalInterestLienHolderNumber.getText();
        String lienHolderOfficeNumber = "";
        try {
            waitUntilElementIsVisible(label_AdditionalInterestLienHolderOfficeNumber, 1);
            lienHolderOfficeNumber = label_AdditionalInterestLienHolderOfficeNumber.getText();
        } catch (Exception e) {
            systemOut("There was an issue getting the office number. Most likely, this is because the office number has not been set. We will try to get it later.");
            e.printStackTrace();
            lienHolderOfficeNumber = "001";
        }
        if (additionalInterestSubType == AdditionalInterestSubType.BOPBuilding || additionalInterestSubType == AdditionalInterestSubType.CAVehicles) {
            return lienHolderNumber + "-008-" + StringUtils.right(lienHolderOfficeNumber, 2);
        } else {
            return lienHolderNumber + "-001-" + StringUtils.right(lienHolderOfficeNumber, 2);
        }
    }


    public String getAdditionalInterestsName() {
        String name = null;
        if (finds(By.xpath("//*[contains(@id, 'GlobalContactNameInputSet:Name-inputEl')]")).size() > 0) {
            try {
                name = editBox_AdditionalInterestLienHolderName.getAttribute("value").replace("  ", " ");
            } catch (NullPointerException e) {
                name = editBox_AdditionalInterestLienHolderName.getText();
            }
        } else if (finds(By.xpath("//*[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).size() > 0) {
            String firstName = null;
            try {
                firstName = editBox_AdditionalInterestLienHolderFirstName.getAttribute("value");
            } catch (NoSuchElementException e) {
                firstName = textBox_AdditionalInterestLienHolderFirstName.getText();
            }
            String middleName = null;
            try {
                middleName = editBox_AdditionalInterestLienHolderMiddleName.getAttribute("value");
            } catch (NoSuchElementException e) {
                middleName = textBox_AdditionalInterestLienHolderMiddleName.getText();
            }
            String lastName = null;
            try {
                lastName = editBox_AdditionalInterestLienHolderLastName.getAttribute("value");
            } catch (NoSuchElementException e) {
                lastName = textBox_AdditionalInterestLienHolderLastName.getText();
            }
            if (middleName != null && middleName.length() > 0) {
                name = firstName.trim() + " " + middleName.trim() + " " + lastName.trim();
            } else {
                name = firstName.trim() + " " + lastName.trim();
            }
        }
        return name;
    }


    public String getPropertyAdditionalInterestAddressListing() {
        Guidewire8Select mySelect = select_AdditionalInterestAddressListing();
        String originalValue = mySelect.getText();
        return originalValue;
    }
    
    public boolean addressListingExists() {
    	return select_AdditionalInterestAddressListing().checkIfElementExists(1);
    }

    public void setPropertyAdditionalInterestAddressListing(String address) {

        if (address.contains("New")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
            }
        }

        Guidewire8Select addressListing = super.select_ContactEditAddressListing();
        if (addressListing.isItemInList(address)) {
            addressListing.selectByVisibleTextPartial(address);
        } else {
            addressListing.selectByVisibleTextRandom();
        }
    }


    public void changePropertyAdditionalInterestAddressListing() {
        String originalValue = getPropertyAdditionalInterestAddressListing();
        Guidewire8Select mySelect = select_AdditionalInterestAddressListing();
        mySelect.selectByVisibleTextRandom();
        mySelect = select_AdditionalInterestAddressListing();
        while ((originalValue.equalsIgnoreCase(mySelect.getText())) || (mySelect.getText().equalsIgnoreCase("New. . ."))) {
            mySelect = select_AdditionalInterestAddressListing();
            mySelect.selectByVisibleTextRandom();
        }
    }


    public void changePropertyAdditionalInterestAddressListing(String newAddress) {
        Guidewire8Select mySelect = select_AdditionalInterestAddressListing();
        mySelect.selectByVisibleTextPartial(newAddress);
    }

    public boolean setAddressListing(String addListing) {
    	if(checkIfElementExists("//table[contains(@id, ':AddressListing-triggerWrap')]", 1)) {
    		super.setContactEditAddressListing(addListing);
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public String getOfficeNumber() {
    	if(checkIfElementExists("//span[contains(@id,'office')]", 1)) {
    		return driver.findElement(By.xpath("//span[contains(@id,'office')]")).getText();
    	}else {
    		return driver.findElement(By.xpath("//div[contains(@id, ':OfficeNumber-inputEl')]")).getText();
    	}
    }


    public void setSSN(String ssn) {
        waitUntilElementIsClickable(editBox_AdditionalInterestSSN);
        editBox_AdditionalInterestSSN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_AdditionalInterestSSN.sendKeys(ssn);
    }


    public void setTIN(String tin) {
        waitUntilElementIsClickable(editBox_AdditionalInterestTIN);
        editBox_AdditionalInterestTIN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_AdditionalInterestTIN.sendKeys(tin);
        editBox_AdditionalInterestTIN.sendKeys(Keys.TAB);
    }


    public void setBusinessPhone(String businessPhone) {
        waitUntilElementIsClickable(editBox_AdditionalInterestBusinessPhone);
        editBox_AdditionalInterestBusinessPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_AdditionalInterestBusinessPhone.sendKeys(businessPhone);
        editBox_AdditionalInterestBusinessPhone.sendKeys(Keys.TAB);
    }

    										
    @FindBy(xpath = "//span[contains(@id, ':DeliveryOptionsInputSet:DeliveryOptionListInputSet:ToolbarAddButton-btnEl')]")
    private WebElement button_PropertyAdditionalInterestsDeliveryOptionsAdd;
    
    public void clickAddDeliveryOptions() {
    	waitUntilElementIsClickable(button_PropertyAdditionalInterestsDeliveryOptionsAdd);
    	clickWhenClickable(button_PropertyAdditionalInterestsDeliveryOptionsAdd);
    }
    
    @FindBy(xpath = "//div[contains(@id, 'EditAdditionalInterestPopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:0') and not(contains(@id, '-body'))]")
    private WebElement tableDiv_PropertyAdditionalInterestsDeliveryOptions;
    
    
    public void addDeliverionOptions(ArrayList<DeliveryOptionType> deliveryOptions) {
    	for(int i = 0;i<deliveryOptions.size(); i++) {
    		clickAddDeliveryOptions();
    		int row = tableUtils.getNextAvailableLineInTable(tableDiv_PropertyAdditionalInterestsDeliveryOptions);
    		tableUtils.selectValueForSelectInTableDoubleClick(tableDiv_PropertyAdditionalInterestsDeliveryOptions, tableUtils.getNextAvailableLineInTable(tableDiv_PropertyAdditionalInterestsDeliveryOptions, "Type"), "Type", deliveryOptions.get(i).getTypeValue());
    		tableUtils.setCheckboxInTable(tableDiv_PropertyAdditionalInterestsDeliveryOptions, row, true);
    		tableUtils.clickLinkInSpecficRowInTable(tableDiv_PropertyAdditionalInterestsDeliveryOptions, row);
    	}
    }

    @SuppressWarnings("null")

    public ArrayList<String> getDeliveryOptions() {
        ArrayList<String> deliveryOptions = null;
        try {
            for (WebElement option : text_BuildingsPropertyAdditionalInterestsDeliveryOption)
                deliveryOptions.add(option.getText());
        } catch (Exception e) {
            return null;
        }

        return deliveryOptions;
    }


    public void checkIfLeinHolderPCRIsEditable() {
        if (finds(By.xpath("//input[contains(@id, 'GlobalContactNameInputSet:Name-inputEl')]")).size() > 0) {
            Assert.fail("ERROR: Lienholder's PCR NAME is Editable when it should not be.");
        } else {
            if (finds(By.xpath("//input[contains(@id, ':PolicyContactRoleNameInputSet:TIN-inputEl')]")).size() > 0) {
            	Assert.fail("ERROR: Lienholder's PCR TIN is Editable when it should not be.");
            } else {
                if (finds(By.xpath("//input[contains(@id, 'PolicyContactRoleNameInputSet:SSN-inputEl')]")).size() > 0) {
                	Assert.fail("ERROR: Lienholder's PCR SSN is Editable when it should not be.");
                } else {
                    if (finds(By.xpath("//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress1-inputEl')]")).size() > 0) {
                    	Assert.fail("ERROR: Lienholder's PCR Main E-MAIL is Editable when it should not be.");
                    } else {
                        if (finds(By.xpath("//input[contains(@id, ':PolicyContactRoleNameInputSet:EmailAddress2-inputEl')]")).size() > 0) {
                        	Assert.fail("ERROR: Lienholder's PCR ALTERNATE E-MAIL is Editable when it should not be.");
                        } else {
                            if (finds(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")).size() > 0) {
                            	Assert.fail("ERROR: Lienholder's PCR ADDRESS is Editable when it should not be.");
                            } else {
                                if (finds(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")).size() > 0) {
                                	Assert.fail("ERROR: Lienholder's PCR CITY is Editable when it should not be.");
                                } else {
                                    if (finds(By.xpath("//input[contains(@id, ':County-inputEl')]")).size() > 0) {
                                    	Assert.fail("ERROR: Lienholder's PCR COUNTY is Editable when it should not be.");
                                    } else {
                                        if (finds(By.xpath("//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")).size() > 0) {
                                        	Assert.fail("ERROR: Lienholder's PCR ZIP CODE is Editable when it should not be.");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public boolean checkInterestTypeExists() {
        return checkIfElementExists("//table[contains(@id, ':InterestType-triggerWrap')]", 2000);
    }


    public boolean checkFirstMortgageeExists() {
        return checkIfElementExists("//table[contains(@id, 'FBAdditionalInterestInputSet:FirstMortgage') or contains(@id, 'FBM_PolicyContactDetailsDV:FirstMortgage')]", 2000);
    }


    public boolean checkAppliedToBuildingExists() {
        return checkIfElementExists("//table[contains(@id, 'FBAdditionalInterestInputSet:AppliedBuilding')]", 2000);
    }


    public boolean checkAppliedToBPPExists() {
        return checkIfElementExists("//table[contains(@id, 'FBAdditionalInterestInputSet:AppliedContents')]", 2000);
    }


    public boolean checkContractNumberExists() {
        return checkIfElementExists("//input[contains(@id, 'FBAdditionalInterestInputSet:ContractNumber-inputEl')] | //input[contains(@id, 'FBM_PolicyContactDetailsDV:LoanNumber-inputEl') or contains(@id, ':AdditionalInterestInfoDV:ContractNumber-inputEl')]", 2000);
    }


    public void clickReturnToCoverages() {
        link_ReturnToCoverages.click();
    }


    public void addExistingFromStandardFire(String contactName) {
        clickWhenClickable(button_AdditionalInterests_AddExistingFromStdFire);
        hoverOverAndClick(link_AdditionalInterests_NonBoundStdFire);
        hoverOverAndClick(link_AdditionalInterests_NonBoundStdFireArrow);
        String xPath = "//span[contains(text(), '" + contactName + "')]/parent::a/parent::div";
        WebElement lienHolder = find(By.xpath(xPath));
        clickWhenClickable(lienHolder);
    }


    public String getZip() {
        return editBox_AdditionalInterestLienHolderZip.getText();
    }


    public String getState() {
        return editBox_AdditionalInterestLienHolderState.getText();
    }


    public String getCounty() {
        return editBox_AdditionalInterestLienHolderCounty.getText();
    }


    public String getCity() {
        return editBox_AdditionalInterestLienHolderCity.getText();
    }


    public String getAddressType() {
        return editBox_AdditionalInterestLienHolderAddressType.getText();
    }


    public void clickRelatedContactsTab() throws GuidewireNavigationException {
        clickWhenClickable(tab_RelatedContactsTab);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(text(), 'Relationship')]", 10000, "Unable to get to Related Contacts page.");
    }
    
    public boolean checkIfRelatedContactsAddButtonExists() {
    	waitUntilElementIsVisible(By.xpath("//span[contains(., 'Relationship')]"));
    	return checkIfElementExists("//span[contains(@id, ':RelatedContactsPanelSet:Add-btnEl')]", 1000);    	
    }


    public void addExistingAdditionalInterest(String contactName) {
        clickWhenClickable(button_AdditionalInterests_AddExisting);
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterest);
        hoverOverAndClick(link_AdditionalInterests_ExistingAdditionalInterestArrow);
        String xPath = "//span[contains(text(), '" + contactName + "')]/parent::a[contains(@id, ':AddExistingContact')]";
        WebElement lienHolder = find(By.xpath(xPath));
        clickWhenClickable(lienHolder);
    }


    public String getDeliveryOption() {
        return text_PropertyAdditionalInterestsDeliveryOption.getText();
    }


    public void searchAndAddExistingCompanyAsAdditionalInterest(boolean basicSearch, AdditionalInterestType interestType) throws Exception {
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAdditionalInterestType(interestType);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(getDriver());
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        repository.pc.search.SearchAddressBookPC search = new repository.pc.search.SearchAddressBookPC(getDriver());
        search.searchAddressBookByCompanyName(basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        if (!finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")).isEmpty()) {
            clickWhenClickable(find(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")));
            additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        }
    }


    public void addNewPersonRelatedContact(boolean basicSearch, RelatedContacts relationship, String firstName, String lastName, AddressInfo address) throws GuidewireException {
        super.clickAdd();
        int row = tableUtils.getRowCount(table_RelatedContacts);
        tableUtils.selectValueForSelectInTable(table_RelatedContacts, row, "Relationship", relationship.getValue());
        String gridColumnID = tableUtils.getGridColumnFromTable(table_RelatedContacts, "Related To");
        WebElement tableCell = table_RelatedContacts.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (row - 1) + "')]/td[contains(@class,'" + gridColumnID + "')]/div"));
        clickWhenClickable(tableCell);
        clickWhenClickable(find(By.xpath("//div[contains(@id,':RelatedContactsPanelSet:" + (row - 1) + ":pick:Selectpick')]")));
        repository.pc.search.SearchAddressBookPC addressBook = new SearchAddressBookPC(getDriver());
        addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address, CreateNew.Create_New_Always);
    }


    public void clickRelatedContactsUpdate() {
        clickWhenClickable(click_RelatedContactsUpdate);
    }

}
