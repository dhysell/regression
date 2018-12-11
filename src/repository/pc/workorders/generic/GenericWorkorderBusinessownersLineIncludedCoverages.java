package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.BusinessownersLine.*;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.search.SearchAddressBookPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderBusinessownersLineIncludedCoverages extends GenericWorkorderBusinessownersLine {

    private WebDriver driver;

    public GenericWorkorderBusinessownersLineIncludedCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void clickBusinessOwnersLineIncludedCoverages() {
        clickBusinessownersLine_IncludedCoverages();
        waitForPageLoad();
    }

    // BUSINESS OWNERS LINE INCLUDED COVERAGES
    public void fillOutIncludedCoverages(boolean basicSearch, PolicyBusinessownersLine busOwnLineObj) throws GuidewireException {
    	clickBusinessownersLine_IncludedCoverages();
        //fillOutPropertyCoverage(busOwnLineObj);
        //filOutLiabilityCoverages(busOwnLineObj);
//        addAdditionalInsureds(basicSearch, busOwnLineObj);
        setSmallBusinessType(busOwnLineObj.getSmallBusinessType());
    }


    void addAdditionalInsureds(boolean basicSearch, PolicyBusinessownersLine busOwnLineObj) throws GuidewireException {
        clickBusinessOwnersLineIncludedCoverages();
        ArrayList<PolicyBusinessownersLineAdditionalInsured> additionalInsuredList = busOwnLineObj.getAdditonalInsuredBOLineList();
        if (additionalInsuredList.size() > 0) {
            for (PolicyBusinessownersLineAdditionalInsured aiBoLine : busOwnLineObj.getAdditonalInsuredBOLineList()) {
                addAdditionalInsureds(basicSearch, aiBoLine);
            }
        }
    }


    //SET SMALL BUSINESS TYPE
    public void setSmallBusinessType(SmallBusinessType smallBusinessType) {
        select_SubmissionBusinessownersLineIncludedCoveragesSmallBusinessType().selectByVisibleText(smallBusinessType.getValue());
//        clickProductLogo();
    }

    public String getSmallBusinessType() {
        Guidewire8Select mySelect = select_SubmissionBusinessownersLineIncludedCoveragesSmallBusinessType();
        return mySelect.getText();
    }

    //FILL OUT PROPERTY COVERAGE
    public void fillOutPropertyCoverage(PolicyBusinessownersLine busOwnLineObj) {
        setBusinessownersLineIncludedCoveragesPropertyGlassDeductible(busOwnLineObj.getPropertyDeductible());
    }

    //FILL OUT LIABILITY COVERAGES
    public void filOutLiabilityCoverages(PolicyBusinessownersLine busOwnLineObj) {
        setBusinessownersLineIncludedCoveragesLiabilityLimits(busOwnLineObj.getLiabilityLimits());
        if (busOwnLineObj.isPdDeductibleChecked() == true && !isBusinessownersLineIncludedCoveragesPDDeductibleChecked()) {
            setBusinessownersLineIncludedCoveragesPDDeductible(busOwnLineObj.isPdDeductibleChecked());
            setBusinessownersLineIncludedCoveragesPDDeductibleAmount(busOwnLineObj.getPdDeductible());
        }
        if (checkIfElementExists(editbox_SubmissionBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou, 2000)) {
            setBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou(busOwnLineObj.getDamagePremisesAmount());
        }
        setBusinessownersLineIncludedCoveragesPremisesMedicalExpense(busOwnLineObj.getMedicalLimit());
    }

    //FILL OUT ADDITIONAL INSUREDS
    public void addAdditionalInsureds(boolean basicSearch, PolicyBusinessownersLineAdditionalInsured aiBoLine) throws GuidewireException {
    	new GuidewireHelpers(driver).clickProductLogo();
        clickBusinessownersLineIncludedCoveragesAdditionalInsuredsSearch();
        searchAndCreateContact(aiBoLine, findContact(basicSearch, aiBoLine));
        setAIRoleQuestions(aiBoLine);
        repository.pc.search.SearchAddressBookPC searchPC = new repository.pc.search.SearchAddressBookPC(driver);
        if(aiBoLine.getRelatedToPrimaryNamedInsured() && aiBoLine.getRelationship() != null && checkIfElementExists("//span[contains(@id,'ContactRelatedContactsCardTab-btnInnerEl')]", 500)) {
        	searchPC.addRelatedContact(aiBoLine.getRelatedContact(), aiBoLine.getRelationship());
        }
        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured(driver);
        editAIBoLinePage.clickEditAdditionalInsuredBOLineOK();
        repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured pcAdditionalInsuredPage = new GenericWorkorderAdditionalNamedInsured(getDriver());
        if (pcAdditionalInsuredPage.isSelectMatchingContactDisplayed()) {
//            pcAdditionalInsuredPage.clickSelectMatchingContact();
//            pcAdditionalInsuredPage.setReasonForContactChange("Testing");
        	clickWhenClickable(find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")));
            editAIBoLinePage.clickEditAdditionalInsuredBOLineOK();
        }
    }


    @FindBy(xpath = "//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    private static WebElement button_Override;

    private Guidewire8Select select_SubmissionBusinessownersLineIncludedCoveragesSmallBusinessType() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLineDV:SmallBusinessType-triggerWrap')]");
    }

    private Guidewire8Select select_SubmissionBusinessownersLineIncludedCoveragesPropertyGlassDeductible() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLinePropertyDV:0:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_SubmissionBusinessownersLineIncludedCoveragesLiabilityLimits() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLiabilityDV:BOPLiabilityCatIterator:0:CoverageInputSet:CovPatternInputGroup:BOPLiabilityLimit-triggerWrap')]");
    }

    private Guidewire8Checkbox PDDeductible() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPLiabPDDeductibleSelected')]");
    }

    private Guidewire8Select select_SubmissionBusinessownersLineIncludedCoveragesPDDeductibleAmount() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:BOPLiabPDDeductible-triggerWrap') or contains(@id, ':CoverageInputSet:CovPatternInputGroup:pdDedSet-triggerWrap') or (contains(@id,':BOPLinePropertyDV:') and contains(@id,':CovTermInputSet:OptionTermInput-triggerWrap'))]");
    }

    @FindBy(xpath = "//input[contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLiabilityDV:BOPLiabilityCatIterator:1:CoverageInputSet:CovPatternInputGroup:DirectTermInput-inputEl') or contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLiabilityDV:BOPLiabilityCatIterator:1:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:DirectTermInput-inputEl')]")
    private static WebElement editbox_SubmissionBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou;

    private Guidewire8Select select_SubmissionBusinessownersLineIncludedCoveragesPremisesMedicalExpense() {
        return new Guidewire8Select(driver, "//*[contains(@id, ':BOPScreen:BOPLinePanelSet:BOPLiabilityDV:BOPLiabilityCatIterator:2:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'BOPScreen:AdditionalInsuredLV_tb:ToolbarButton-btnInnerEl')]")
    private static WebElement button_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsSearch;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:BOPScreen:AdditionalInsuredLV')]")
    private WebElement table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults;
    
    @FindBy(xpath = "//span[contains(@id, ':ContactRelatedContactsCardTab-btnEl')]")
    private WebElement link_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts;
    
    @FindBy(xpath = "//div[contains(@id, ':RelatedContactsPanelSet:0') and not(contains(@id, '-body'))]")
    private WebElement table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts;
    

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    private void setBusinessownersLineIncludedCoveragesPropertyGlassDeductible(PropertyGlassDeductible propertyDeductible) {
        Guidewire8Select mySelect = select_SubmissionBusinessownersLineIncludedCoveragesPropertyGlassDeductible();
        mySelect.selectByVisibleText(propertyDeductible.getValue());
    }


    private void setBusinessownersLineIncludedCoveragesLiabilityLimits(LiabilityLimits liabilityLimits) {
        select_SubmissionBusinessownersLineIncludedCoveragesLiabilityLimits()
                .selectByVisibleText(liabilityLimits.getValue());
    }

    public String getBusinessownersLineIncludedCoveragesLiabilityLimits() {
        return select_SubmissionBusinessownersLineIncludedCoveragesLiabilityLimits().getText();
    }


    private void setBusinessownersLineIncludedCoveragesPDDeductible(boolean trueFalseChecked) {
        PDDeductible().select(trueFalseChecked);
    }


    public boolean isBusinessownersLineIncludedCoveragesPDDeductibleChecked() {
        return PDDeductible().isSelected();
    }


    private void setBusinessownersLineIncludedCoveragesPDDeductibleAmount(PDDeductibleAmount PDDeductible) {
        select_SubmissionBusinessownersLineIncludedCoveragesPDDeductibleAmount()
                .selectByVisibleText(PDDeductible.getValue());
    }


    public String getBusinessownersLineIncludedCoveragesPDDeductibleAmount() {
        return select_SubmissionBusinessownersLineIncludedCoveragesPDDeductibleAmount().getText();
    }


    private void setBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou(float amount) {
        waitUntilElementIsClickable(editbox_SubmissionBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou);
        setText(editbox_SubmissionBusinessownersLineIncludedCoveragesDamageToPremisesRentedToYou, String.valueOf(amount));
    }


    private void setBusinessownersLineIncludedCoveragesPremisesMedicalExpense(PremisesMedicalExpense medicalLimit) {
        select_SubmissionBusinessownersLineIncludedCoveragesPremisesMedicalExpense()
                .selectByVisibleText(medicalLimit.getValue());
    }
    
//    private void clickRelatedContactsTab() {
//    	waitUntilElementIsClickable(link_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts);
//    	link_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts.click();
//    }


//    public void clickBusinessownersLineIncludedCoveragesAdditionalCoverages() {
//        delay(50);
//        clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesAdditonalCoverages);
//        delay(500);
//    }
//
//
//    private void clickBusinessownersLineIncludedCoveragesExclusionsConditions() {
//        clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesExclusionsConditions);
//    }


    private void clickBusinessownersLineIncludedCoveragesAdditionalInsuredsSearch() {
        int i = 0;
        boolean resetFound = false;
    	do {
    		clickWhenClickable(button_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsSearch);
    		if(checkIfElementExists("//a[contains(@id, ':SearchLinksInputSet:Reset')]", 1000)) {
    			resetFound = true;
    		}
    		i++;
        }while(!resetFound && i <100000);
    	System.out.println("Complete");
    }


    public void addAdditionalInsureds(boolean basicSearch, PolicyBusinessownersLineAdditionalInsured aiBoLine, Boolean retry) throws GuidewireException {
        clickBusinessownersLineIncludedCoveragesAdditionalInsuredsSearch();

        searchAndCreateContact(aiBoLine, findContact(basicSearch, aiBoLine));

        setAIRoleQuestions(aiBoLine);
        
//        addRelatedContacts(aiBoLine);

        completeBOLAIpage();

        if (!verifyAI(aiBoLine) && retry) {
            addAdditionalInsureds(basicSearch, aiBoLine, false);
        }
    }

    private Boolean findContact(boolean basicSearch, PolicyBusinessownersLineAdditionalInsured aiBoLine) {
        repository.pc.search.SearchAddressBookPC searchAddressBookPage = new SearchAddressBookPC(driver);

        if (aiBoLine.getCompanyName() == null || aiBoLine.getCompanyName().equals("")) {
            switch (aiBoLine.getNewContact()) {
                case Create_New_Always:
                    aiBoLine.setPersonLastName(new StringsUtils().getUniqueName(aiBoLine.getPersonFirstName(), null, aiBoLine.getPersonLastName())[2]);
                    return searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, aiBoLine.getPersonFirstName(), aiBoLine.getPersonLastName(), aiBoLine.getAddress(), CreateNew.Create_New_Always);
                case Create_New_Only_If_Does_Not_Exist:
                    return searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, aiBoLine.getPersonFirstName(), aiBoLine.getPersonLastName(), aiBoLine.getAddress(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                case Do_Not_Create_New:
                    return searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, aiBoLine.getPersonFirstName(), aiBoLine.getPersonLastName(), aiBoLine.getAddress(), CreateNew.Do_Not_Create_New);
            }
        } else {
            switch (aiBoLine.getNewContact()) {
                case Create_New_Always:
                    aiBoLine.setCompanyName(new StringsUtils().getUniqueName(aiBoLine.getCompanyName()));
                    return searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, aiBoLine.getCompanyName(), aiBoLine.getAddress(), CreateNew.Create_New_Always);
                case Create_New_Only_If_Does_Not_Exist:
                    return searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, aiBoLine.getCompanyName(), aiBoLine.getAddress(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                case Do_Not_Create_New:
                    return searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, aiBoLine.getCompanyName(), aiBoLine.getAddress(), CreateNew.Do_Not_Create_New);
            }
        }
        return false;
    }// END findContact


    private void searchAndCreateContact(PolicyBusinessownersLineAdditionalInsured aiBoLine, Boolean found) {
        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured(getDriver());

        if (aiBoLine.getNewContact() == CreateNew.Create_New_Always || (aiBoLine.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist && !found)) {
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing("New...");
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressLine1(aiBoLine.getAddress().getLine1());
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressCity(aiBoLine.getAddress().getCity());
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressState(aiBoLine.getAddress().getState());
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressZipCode(aiBoLine.getAddress().getZip());
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressAddressType(aiBoLine.getAddress().getType());
        } else {
//			String addressListingToSelect = "(" + aiBoLine.getAddress().getState().getAbbreviation() + ") " + aiBoLine.getAddress().getCity() + " - " + aiBoLine.getAddress().getLine1();
            editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing(aiBoLine.getAddress().getDropdownAddressFormat());
        }
    }// END createContact

    private void setAIRoleQuestions(PolicyBusinessownersLineAdditionalInsured aiBoLine) {
        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured(driver);
        editAIBoLinePage.setEditAdditionalInsuredBOLineRole(aiBoLine.getAiRole());
        editAIBoLinePage.setEditAdditionalInsuredBOLineSpecialWording(aiBoLine.isSpecialWording());
        if (aiBoLine.isSpecialWording()) {
            editAIBoLinePage.setEditAdditionalInsuredBOLineSpecialWordingDescription(aiBoLine.getSpecialWordingDesc());
            editAIBoLinePage.setEditAdditionalInsuredBOLineSpecialWordingAcord101Description(
                    aiBoLine.getSpecialWordingAcord101Desc());
        }

        if (aiBoLine.getAiRole() != AdditionalInsuredRole.CertificateHolderOnly) {
            editAIBoLinePage.setEditAdditionalInsuredBOLineWaiverSubro(aiBoLine.isWaiverOfSubro());
        }

        if (aiBoLine.getAiRole() == AdditionalInsuredRole.Vendors) {
            editAIBoLinePage.setEditAdditionalInsuredBOLineListProducts(aiBoLine.getVendorRoleListProducts());
        }

        if ((aiBoLine.getAiRole() == AdditionalInsuredRole.CertificateHolderOnly)
                || (aiBoLine.getAiRole() == AdditionalInsuredRole.DesignatedPersonOrOrganization)) {
            editAIBoLinePage.setEditAdditionalInsuredBOLineWherePerformActivities(aiBoLine.getAddress().getState(),
                    true);
            editAIBoLinePage.setEditAdditionalInsuredBOLineWhatActivities("Activity1, Activity2, Activity3");
            editAIBoLinePage.setEditAdditionalInsuredBOLineIndustryAll(false);
        }

    }// END setAIRoleQuestions
    
//    private void addRelatedContacts(boolean basicSearch, PolicyBusinessownersLineAdditionalInsured aiBoLine) {
//    	waitUntilElementIsClickable(link_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts);
//    	clickRelatedContactsTab();
//    	clickAdd();
//    	TableUtils tableHelp = new TableUtils(driver);
////    	tableHelp.clickCellInTableByRowAndColumnName(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts, tableHelp.getNextAvailableLineInTable(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts), "Relationship");
//    	tableHelp.selectValueForSelectInTable(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts, tableHelp.getNextAvailableLineInTable(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts), "Relationship", RelationshipsAB.Affiliation.getValue());
//    	tableHelp.clickCellInTableByRowAndColumnName(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts, tableHelp.getNextAvailableLineInTable(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsRelatedContacts), "Related To");
//    	clickWhenClickable(By.xpath("//div[contains(@id, 'pick:Selectpick')]"));
//    	SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
//    	searchPC.searchAddressBookByFirstLastName(basicSearch, aiBoLine.getRelatedContact().getFirstName(), aiBoLine.getRelatedContact().getLastName(), aiBoLine.getRelatedContact().getAddress(), CreateNew.Create_New_Only_If_Does_Not_Exist);    	
//    }

    private Boolean verifyAI(PolicyBusinessownersLineAdditionalInsured aiBoLine) {

        List<WebElement> additonalInsured = new ArrayList<WebElement>();

        if (aiBoLine.getCompanyOrPerson() == ContactSubType.Person) {
            additonalInsured = table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults.findElements(By.xpath("//tr[(contains(., '" + aiBoLine.getPersonFirstName() + "')) and (contains(., '" + aiBoLine.getAiRole().getRole() + "'))]"));
        } else {
            additonalInsured = table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults.findElements(By.xpath("//tr[(contains(., '" + aiBoLine.getCompanyName() + "')) and (contains(., '" + aiBoLine.getAiRole().getRole() + "'))]"));
        }

        if (additonalInsured.size() <= 0) {
            if (aiBoLine.getCompanyOrPerson() == ContactSubType.Person) {
                systemOut(getCurrentUrl() + " The contact: " + aiBoLine.getPersonFirstName() + " " + aiBoLine.getPersonLastName() + " was not added correctly as an Additional Insured on the Businessowners Line");

            } else {
                systemOut(getCurrentUrl() + "The contact: " + aiBoLine.getCompanyName() + " was not added correctly as an Additional Insured on the Businessowners Line");
            }
            return false;
        } else {
            return true;
        }
    }

    private void completeBOLAIpage() {

        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new GenericWorkorderBusinessownersLineAdditionalInsured(driver);
        editAIBoLinePage.clickEditAdditionalInsuredBOLineOK();
        if (finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")).size() > 0) {
        	clickWhenClickable(button_Override);
            editAIBoLinePage.clickEditAdditionalInsuredBOLineOK();
        }
        if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
        	clickWhenClickable(find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")));
            editAIBoLinePage.clickEditAdditionalInsuredBOLineOK();
        }
    }

    @SuppressWarnings("unused")
	private void resetBOLAI() {
        if (finds(By.xpath("//a[contains(@id, 'AddlInsuredContactSearchPopup:__crumb__')]")).size() > 0) {
            find(By.xpath("//a[contains(@id, 'AddlInsuredContactSearchPopup:__crumb__')]")).click();
            selectOKOrCancelFromPopup(OkCancel.OK);

        } else if (finds(By.xpath("//a[contains(@id, 'EditPolicyAddnlInsuredContactRolePopup:__crumb__')]")).size() > 0) {
        	clickWhenClickable(find(By.xpath("//a[contains(@id, 'EditPolicyAddnlInsuredContactRolePopup:__crumb__')]")));
        }
    }


    public void clickAdditionalInsuredByName(String name) {
        new TableUtils(driver).clickLinkInTableByRowAndColumnName(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults, new TableUtils(driver).getRowNumberInTableByText(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults, name), "Name");
    }

    public void clickAdditionalInsuredByRow(int row) {
        new TableUtils(driver).clickLinkInSpecficRowInTable(table_SubmissionBusinessownersLineIncludedCoveragesAdditionalInsuredsResults, row);
    }

}
