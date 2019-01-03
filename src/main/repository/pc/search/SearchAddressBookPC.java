package repository.pc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.submission.SubmissionCreateAccount;

import java.util.List;

public class SearchAddressBookPC extends BasePage {

    private WebDriver driver;

    public SearchAddressBookPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    
    @FindBy(xpath = "//div[(contains(@id, 'AccountSearchResultsLV')) or (contains(@id, 'DocumentTemplateSearchResultsLV')) or (contains(@id, 'PolicySearchResultsLV')) or (contains(@id, 'PolicySearch_ResultsLV')) or (contains(@id, 'InvoiceSearchResultsLV')) or (contains(@id, 'LienholderSearchPanelSet:2')) or (contains(@id, 'ContactSearchLV_FBMPanelSet:1')) or (contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV')) or (contains(@id, ':SolrContactSearchLVPanelSet:1')) or (contains(@id, 'AddlNamedInsuredContactSearchPopup:ContactSearchScreen:3')) or (contains(@id, ':ContactSearchScreen:ContactSearchLV_FBMPanelSet'))]")
    private WebElement table_SearchAddressBookPCSearchResults;
    
    private WebElement getTable_SearchAddressBookPCSearchResults() {
    	WizardSteps foo = new SideMenuPC(driver).getselectedWizardStep();
    	if(foo == null) {//                   FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SolrContactSearchLVPanelSet_ref
    		return find(By.xpath("//div[(@id='FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SolrContactSearchLVPanelSet_ref') or (@id='FBNewSubmission:ContactSearchScreen:ContactSearchLV_FBMPanelSet_ref') or (@id='ContactSearch:ContactSearchScreen:ContactSearchLV_FBMPanelSet_ref') or (@id='FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SolrContactSearchLVPanelSet_ref') or contains(@id, 'AddlInterestContactSearchPopup:LienholderSearchScreen:LienholderSearchPanelSet:2') or contains(@id, 'ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet:1')]"));
    	}
    	
    	switch (foo) {
		case Buildings:
			return find(By.xpath("//div[contains(@id, 'AddlInterestContactSearchPopup:LienholderSearchScreen:LienholderSearchPanelSet:2')]"));
		case BusinessownersLine:
			return find(By.xpath("//div[(@id='AddlInsuredContactSearchPopup:ContactSearchScreen:3') or (@id='ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet:1')]"));
		case CommercialAuto_AutoLine:
			break;
		case CommercialAuto_CoveredVehicles:
			break;
		case CommercialAuto_Drivers:
			break;
		case CommercialAuto_Garagekeepers:
			break;
		case CommercialAuto_Modifiers:
			break;
		case CommercialAuto_StateInfo:
			break;
		case CommercialAuto_Vehicles:
			break;
		case CommercialInlandMarine_AccountsReceivable:
			break;
		case CommercialInlandMarine_BaileesCustomers:
			break;
		case CommercialInlandMarine_CameraAndMusicalInstrumentDealers:
			break;
		case CommercialInlandMarine_CommercialArticles:
			break;
		case CommercialInlandMarine_ComputerSystems:
			break;
		case CommercialInlandMarine_ContractorsEquipment:
			break;
		case CommercialInlandMarine_Exhibition:
			break;
		case CommercialInlandMarine_Installation:
			break;
		case CommercialInlandMarine_MiscellaneousArticles:
			break;
		case CommercialInlandMarine_MotorTruckCargo:
			break;
		case CommercialInlandMarine_Signs:
			break;
		case CommercialInlandMarine_TripTransit:
			break;
		case CommercialInlandMarine_ValuablePapers:
			break;
		case CommercialProperty_Modifiers:
			break;
		case CommercialProperty_Property:
			break;
		case CommercialProperty_PropertyLine:
			break;
		case Commercial_Auto:
			break;
		case Commercial_InlandMarine:
			break;
		case Commercial_Property:
			break;
		case Forms:
			break;
		case GeneralLiability_Coverages:
			break;
		case GeneralLiability_Exposures:
			break;
		case GeneralLiability_Modifiers:
			break;
		case General_Liability:
			break;
		case InsuranceScore:
			break;
		case Locations:
			return find(By.xpath("//div[(@id='AddlInsuredLocContactSearchPopup:ContactSearchScreen:3') or (@id ='ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet:1')]"));
		case Modifiers:
			break;
		case PayerAssignment:
			break;
		case Payment:
			break;
		case PolicyInfo:
			//                                AddlNamedInsuredContactSearchPopup:ContactSearchScreen:3_ref
			//                                AddlNamedInsuredContactSearchPopup:ContactSearchScreen:3_ref
			return find(By.xpath("//div[(@id='AddlNamedInsuredContactSearchPopup:ContactSearchScreen:3_ref') or (@id='ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet_ref')]"));
		case PolicyMembers:
			return find(By.xpath("//div[(@id='ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet_ref')]"));
		case PropertyLiability_CLUEProperty:
			break;
		case PropertyLiability_Coverages:
			break;
		case PropertyLiability_LineReiew:
			break;
		case PropertyLiability_Locations:
			break;
		case PropertyLiability_PropertyDetail:
			return find(By.xpath("//div[(@id='AddlInterestContactSearchPopup:LienholderSearchScreen:LienholderSearchPanelSet:2_ref')]"));
		case Qualifications:
			break;
		case Quote:
			break;
		case RiskAnalysis:
			break;
		case Supplemental:
			break;
		case Vehicles:
			return find(By.xpath("//div[(@id='AddlInterestContactSearchPopup:LienholderSearchScreen:LienholderSearchPanelSet:2')]"));
		case Cargo:
			break;
		case FarmEquipment:
		case RecreationalEquipment:
		case Watercraft:
			return find(By.xpath("//div[(@id='AddlInterestContactSearchPopup:LienholderSearchScreen:LienholderSearchPanelSet:2_ref')]"));
		case Livestock:
			break;
		case PersonalProperty:
			break;
		case SectionIV_InlandMarine_ExclusionsAndConditions:
			break;
		case Members:
			return find(By.xpath("//div[(@id='ContactSearchPopup:ContactSearchScreen:ContactSearchLV_FBMPanelSet_ref')]"));
		case MembershipType:
			break;
		}
    	return null;
    }
    
    
    
    

    private Guidewire8Select select_SearchAddressBookPCContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactType-triggerWrap') or contains(@id, ':ContactSubtype-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl') or contains(@id, ':CompanyName-inputEl') or contains(@id, ':GlobalContactNameInputSet:Name-inputEl') or contains(@id, ':Keyword-inputEl')]")
    private WebElement editBox_SearchAddressBookPCCompanyName;

    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editBox_SearchAddressBookPCFirstName;

    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl') or contains(@id, ':Keyword-inputEl')]")
    private WebElement editBox_SearchAddressBookPCLastName;
        
    @FindBy(xpath = "//input[contains(@id, ':searchStreet-inputEl')]")
    private WebElement editBox_SearchAddressBookPcStreet;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl') or contains(@id, ':searchCity-inputEl')]")
    private WebElement editBox_SearchAddressBookPCCity;

    private Guidewire8Select select_SearchAddressBookPCState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap') or contains(@id, ':searchState-inputEl')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl') or contains(@id, ':searchPostalCode-inputEl')]")
    private WebElement editBox_SearchAddressBookPCZip;

    public Guidewire8Select select_SearchAddressBookPCSSNTIN() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':TaxReportingOption-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':TaxID-inputEl') or contains(@id, ':SSN-inputEl') or contains(@id, ':TIN-inputEl')]")
    public WebElement editbox_SearchAddressBookPCSSNTIN;

    @FindBy(xpath = "//a[(substring(@id, string-length(@id) - string-length('NewAccountButton_container') +1) = 'NewAccountButton_container') or contains(@id, ':NewAccountButton')]")
    public WebElement button_SearchAddressBookCreateNew;

    @FindBy(xpath = "//div[contains(@id, 'AddlInterestContactSearchPopup:LienholderSearchPanelSet:2')]")
    public WebElement tableDiv_SearchAddressBook;

    protected boolean row_NameFound(String firstName, String lastName) {
        return checkIfElementExists("//td/div[contains(., '" + lastName + "') and contains(., '" + firstName + "') and contains(@class, 'cell')]", 2000);
    }

    protected boolean row_NameFound(String companyNameOrSSNTINLast4) {
        return checkIfElementExists("//td/div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyNameOrSSNTINLast4) + ") and contains(@class, 'cell')]", 2000);
    }
    
    @FindBy(xpath = "//div[contains(@id, ':RelatedContactsPanelSet:') and not(contains(@id,'-body')) or contains(@id,'MembershipMemberPopup:MembershipMemberCV:1') and not(contains(@id,'-body'))]")
    private WebElement div_RelatedContactsContainer;
    
    @FindBy(xpath = "//span[contains(@id, ':PolicyContactRoleDetailCardTab-btnEl') or contains(@id, ':BasicMemberTabTab-btnEl')]")
    private WebElement link_RelatedContactsBasicsOrContactDetail;

    private boolean row_NameAndAddress(String companyNameOrSSNTINLast4, String addressLine1) {
    	boolean found = false;
    	if (addressLine1 == null || addressLine1.equals("")) {
    		if (checkIfElementExists("//td/div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyNameOrSSNTINLast4) + ")]", 2000)) {
                found = true;
            }
    	} else {
        if (checkIfElementExists("//td/div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyNameOrSSNTINLast4) + ") and contains(@class, 'cell')]/parent::td/parent::tr/td/div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(addressLine1) + ")]", 2000)) {
                found = true;
        }
    }
        return found;
    }

    @FindBy(xpath = "//a[contains(@id, ':__crumb__')]")
    private WebElement link_ReturnToPolicyInfo;

    @FindBy(xpath = "//a[(contains(@id,'NewAccountButton')) and (contains(@id,'_Person'))]")
    public WebElement button_SearchAddressBookCreateNewPerson;

    @FindBy(xpath = "//a[(contains(@id,'NewAccountButton')) and (contains(@id, '_Company'))]")
    public WebElement button_SearchAddressBookCreateNewCompany;
    
    @FindBy(xpath = "//span[contains(@id,':ContactSearchScreen:dbSearchTab-btnEl')]")
    public WebElement link_SearchAddressBookNewSubmissionsAdvanced;
    
    @FindBy(xpath = "//span[contains(@id,'ContactRelatedContactsCardTab-btnInnerEl')]")
    private WebElement link_RelatedContactsTab;
    
/*    private void clickAdvancedSearchTab() {
        new WaitUtils(driver).waitUntilElementIsClickable(link_SearchAddressBookNewSubmissionsAdvanced);
        if(checkIfElementExists(link_SearchAddressBookNewSubmissionsAdvanced, 1)){
        	link_SearchAddressBookNewSubmissionsAdvanced.click();
        }
    }
 */   
//    private WebElement button_SearchAddressBookResultsBySSNTIN(String ssntin) {
//        WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath(".//a[.= 'Select']")).get(0);
//        return selectButton;
//    }
    
/*  Steve Broderick 8/7/2018 - I changed the method above to return the first select.  Ideally you should have only one result when searching ssn or tin.
 * But I left this here so that we can revert back to it.  
    private WebElement button_SearchAddressBookResultsBySSNTIN(String ssntin) {
        WebElement selectButton = table_SearchAddressBookPCSearchResults.findElement(By.xpath(".//span[contains(@value, '" + ssntin
                + "' )]/../preceding-sibling::td/a[contains(@id, 'AccountNumber')) and (contains(@id, 'select'))]"));
        return selectButton;
    }
*/
    private WebElement button_SearchAddressBookResultsByCompanyName(String companyName) {
        
        List<WebElement> myList = finds(By.xpath("//div[(contains(@id, 'AccountSearchResultsLV')) or (contains(@id, 'DocumentTemplateSearchResultsLV')) or (contains(@id, 'PolicySearchResultsLV')) or (contains(@id, 'PolicySearch_ResultsLV')) or (contains(@id, 'InvoiceSearchResultsLV')) or (contains(@id, 'LienholderSearchPanelSet:2')) or (contains(@id, ':SolrAddlInterestSearchLVPanelSet:1')) or (contains(@id, 'ContactSearchPopup:ContactSearchScreen')) or (contains(@id, 'ContactSearchLV_FBMPanelSet:1')) or (contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV')) or (contains(@id, ':ContactSearchScreen:ContactSearchLV_FBMPanelSet'))  ]//div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyName) + ")]/../preceding-sibling::td//a[(contains(@id, 'select')) or contains(., 'Select')]"));
//        List<WebElement> myList = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath(".//div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyName) + ")]/../preceding-sibling::td//a[(contains(@id, 'select')) or contains(., 'Select')]"));
        if (myList.size() > 0) {
        	return myList.get(0);
//            WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//div[contains(., " + StringsUtils.xPathSpecialCharacterEscape(companyName) + ")]/../preceding-sibling::td//a[(contains(@id, 'select')) or contains(., 'Select')]"));
//            return selectButton;
        }
        return null;
    }
    
    ///div[contains(., 'Bcs Roofing LLC')]/../preceding-sibling::td//a[(contains(@id, 'select')) or contains(., 'Select')]

    private WebElement button_SearchAddressBookResultsByCompanyNameWithAddress(String companyName, String address) {
        List<WebElement> myList2 = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath(".//tbody/child::tr"));
        for (WebElement element : myList2) {
            // Jon Larsen 8/1/2015 Added the split to CompanyName because some names extend multiple lFsettyines and search only inputs first line of name
            if (element.getText().contains(address) && element.getText().contains(companyName.split("\n")[0])) {
                List<WebElement> selectButton = element.findElements(By.xpath(".//child::td/child::div/child::a[contains(text(), 'Select')]"));
                if (selectButton.size() > 0) {
                    return selectButton.get(0);
                }
            }
        }
        WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(address) + ")]/../preceding-sibling::td/div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(companyName) + ")]/../preceding-sibling::td//a[(contains(@id, 'select'))]"));
        return selectButton;
    }

    private WebElement button_SearchAddressBookResultsByPersonName(String firstName, String lastName) {
        WebElement selectButton;
        try {
            selectButton = finds(By.xpath("//div[contains(., '" + firstName + "' )]/../../td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'Select'))]")).get(0);
        } catch (Exception e) {
            List<WebElement> selectButtonList = finds(By.xpath(".//child::tbody/child::tr/child::td/div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ") and contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(lastName) + ")]/parent::td/parent::tr/td/div/a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'Select'))]"));
            return selectButtonList.get(0);
        }
        return selectButton;
    }

    private WebElement button_SearchAddressBookResultsByPersonNameWithAddress(String firstName, String lastName, String address) {
        List<WebElement> selectButtonList = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath(".//tr[contains(., " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ")]/td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'Select'))]"));
        List<WebElement> anotherSelectButtonList = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath(".//span[contains(., " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ")]/../../td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'select'))]"));
        List<WebElement> altSelectbutton = finds(By.xpath("//div[(contains(@id, 'AccountSearchResultsLV')) or (contains(@id, 'DocumentTemplateSearchResultsLV')) or (contains(@id, 'PolicySearchResultsLV')) or (contains(@id, 'PolicySearch_ResultsLV')) or (contains(@id, 'InvoiceSearchResultsLV')) or (contains(@id, 'LienholderSearchPanelSet:2')) or (contains(@id, 'ContactSearchPopup:ContactSearchScreen')) or (contains(@id, 'ContactSearchLV_FBMPanelSet:1')) or (contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV')) or (contains(@id, ':ContactSearchScreen:ContactSearchLV_FBMPanelSet'))]//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ") and contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(lastName) + ")]/parent::td/parent::tr/child::td/div/a[contains(@id, 'select') or contains(., 'Select')]"));

        if (selectButtonList.size() > 0) {
            WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//tr[contains(., " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ")]/td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'Select'))]"));
            return selectButton;
        } else if (anotherSelectButtonList.size() > 0) {
            WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//span[contains(., " + StringsUtils.xPathSpecialCharacterEscape(firstName) + ")]/../../td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(., 'select'))]"));
            return selectButton;
        } else if (altSelectbutton.size() > 0) {
            return altSelectbutton.get(0);
        }
        return null;
    }
    
    private WebElement button_MembershipMemberRelatedContactsChangeRelatedTo(int row) {
    	row = row - 1;
    	return driver.findElement(By.xpath("//a[contains(@id, ':" +row +":ChangeRelatedToButton')]"));
    }
    
    private WebElement button_MembershipMemberRelatedContactsSearchPerson(int row) {
    	row = row - 1;
    	return driver.findElement(By.xpath("//div[contains(@id, ':" +row +":ChangeRelatedToButton:SearchPerson')]"));
    }

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickSearchAddressBookCreateNew(ContactSubType contactType) {
        hoverOverAndClick(button_SearchAddressBookCreateNew);
        if (contactType == ContactSubType.Company) {
            hoverOverAndClick(button_SearchAddressBookCreateNewCompany);
        } else if (contactType == ContactSubType.Person) {
            hoverOverAndClick(button_SearchAddressBookCreateNewPerson);
        }
    }

    public void setSearchCriteria(String companyName, String firstName, String lastName, String city, State state, String zip) {
    	SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
    	basicSearch.clickAdvancedSearch();
    	if (companyName != null) {
        	setAddressBookType(ContactSubType.Company);
            setCompanyName(companyName);
        } else if (firstName != null && lastName != null) {
        	setAddressBookType(ContactSubType.Person);
            setFirstName(firstName);
            setLastName(lastName);
        } else {
            Assert.fail("Must pass in a company name or a first and last name");
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

    private void setCompanyName(String nameToFill) {
    	// clickWhenVisible(editBox_CommonCompanyName);
 		// jon larsen 8/10/2015
 		// must limit search name field to <30 char or CM will return zero
 		// results.
        waitUntilElementIsClickable(editBox_SearchAddressBookPCCompanyName);
 		if (nameToFill.length() > 30) {
 			editBox_SearchAddressBookPCCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"),nameToFill.substring(0, 29));
 		} else {
 			editBox_SearchAddressBookPCCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), nameToFill);
 		}
 		clickProductLogo();
        //editBox_SearchAddressBookPCCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), new StringsUtils().getUniqueName(nameToFill));
    }

    private void setFirstName(String nameToFill) {
    	setText(editBox_SearchAddressBookPCFirstName, nameToFill);
    }

    private void setLastName(String nameToFill) {
    	setText(editBox_SearchAddressBookPCLastName, nameToFill);
    }

    private void setCity(String cityToFill) {
    	setText(editBox_SearchAddressBookPCCity, cityToFill);
    }

    private void setZip(String zipToFill) {
    	setText(editBox_SearchAddressBookPCZip, zipToFill);
    }

    private void setState(State stateToSelect) {
        Guidewire8Select commonState = select_SearchAddressBookPCState();
        commonState.selectByVisibleText(stateToSelect.getName());
    }

    private void setAddressBookType(ContactSubType companyOrPerson) {
    	if(!finds(By.xpath("//table[contains(@id, ':ContactType-triggerWrap') or contains(@id, ':ContactSubtype-triggerWrap')]")).isEmpty()) {
    		Guidewire8Select commonType = select_SearchAddressBookPCContactType();
            commonType.selectByVisibleText(companyOrPerson.getValue());
    	}
    }

    // SSN TIN

    public boolean searchAddressBookBySSNTIN(boolean basicSearch, String social, TaxReportingOption ssntin, CreateNew createNew) {
        return searchAddressBookBy(basicSearch, social, ssntin, null, null, null, null, null, null, null, createNew);
    }

    // COMPANY NAME

    public boolean searchAddressBookByCompanyName(boolean basicSearch, String companyName, String address, String city, State state, String zip, CreateNew createNew) {
        return searchAddressBookBy(basicSearch, null, null, null, null, companyName, address, city, state, zip, createNew);
    }


    public boolean searchAddressBookByCompanyName(boolean basicSearch, String companyName, AddressInfo address, CreateNew createNew) {
        return searchAddressBookBy(basicSearch, null, null, null, null, companyName, address.getLine1(), address.getCity(), address.getState(), address.getZip(), createNew);
    }

    // FIRST LAST NAME

    public boolean searchAddressBookByFirstLastName(boolean basicSearch, String firstName, String lastName, String address, String city, State state, String zip, CreateNew createNew) {
        return searchAddressBookBy(basicSearch, null, null, firstName, lastName, null, address, city, state, zip, createNew);
    }

    // FIRST LAST NAME

    public boolean searchAddressBookByFirstLastName(boolean basicSearch, String firstName, String lastName, AddressInfo address, CreateNew createNew) {
        return searchAddressBookBy(basicSearch,null, null, firstName, lastName, null, address.getLine1(), address.getCity(), address.getState(), address.getZip(), createNew);
    }

    public SearchResultsReturnPC searchAddressBook(boolean basicSearch, String social, TaxReportingOption ssntin, String firstName, String lastNameOrCompanyName, String address, String city, State state, String zip, CreateNew createNew) {
    	SearchAddressBookPC_basics basicSearchPage = new SearchAddressBookPC_basics(driver);
    	if(firstName == null) {
	    	if(!basicSearch) {
	    		setSearchCriteria(lastNameOrCompanyName, null, null, city, state, zip);
	    	} else if(!basicSearchPage.basicSearchExists()){
	    		setSearchCriteria(lastNameOrCompanyName, null, null, city, state, zip);
	    	} else {	
	    		basicSearchPage.setSearchCriteria(null, null, lastNameOrCompanyName, null, null, null, city, state, zip, null);
	    	}
    	} else {
    		if(!basicSearch) {
        		
        		basicSearchPage.clickAdvancedSearch();
        		try {
    		            setAddressBookType(ContactSubType.Person);
    		        } catch (NullPointerException npe) {
    		        } catch (Exception e) {
    		            throw e;
    		        }
    		        setSearchCriteria(null, firstName, lastNameOrCompanyName, city, state, zip);
    		        System.out.println("Non Solr Search");
        	}else if(!basicSearchPage.basicSearchExists()) {
        		basicSearchPage.clickAdvancedSearch();
        		try {
    	            setAddressBookType(ContactSubType.Person);
    	        } catch (NullPointerException npe) {
    	        } catch (Exception e) {
    	            throw e;
    	        }
    	        setSearchCriteria(null, firstName, lastNameOrCompanyName, city, state, zip);
        	} else {
        			basicSearchPage.setSearchCriteria(null, null, null, firstName, lastNameOrCompanyName, null, city, state, zip, null);
        	}
    	}
    	clickSearch();

        SearchResultsReturnPC toReturn = new SearchResultsReturnPC(getDriver());
        toReturn.setFound(row_NameAndAddress(lastNameOrCompanyName, address));
        toReturn.setSelectToClick(button_SearchAddressBookResultsByCompanyNameWithAddress(lastNameOrCompanyName, address));
        return toReturn;
    }
    

    /**
     * Generic search method. Can search by Company Name; First and Last Name;
     * or SSN/TIN Searches in that order
     * <p>
     * This method does not click the search results see searchAddressBookBy below.
     *
     * @param social
     * @param ssntin
     * @param firstName
     * @param lastName
     * @param companyName
     * @throws Exception
     */
    public SearchResultsReturnPC searchAddressBook(boolean basicSearch, String social, TaxReportingOption ssntin, String firstName, String lastName, String companyName, String address, String city, State state, String zip, CreateNew createNew) {
        SearchResultsReturnPC results = null;
        clickReset();

        if (companyName != null) {
            results = searchCompany(basicSearch, companyName, address, city, state, zip, createNew);
        } else if (firstName != null && lastName != null) {
            results = searchPerson(basicSearch, firstName, lastName, address, city, state, zip, createNew);

        } else if (social != null && ssntin != null) {
            results = searchSSNTIN(basicSearch, social, ssntin, createNew);

        } else {
            Assert.fail("Not enough information was given to search for a Contact");
        }
        return results;

    }
    
    public boolean searchCompanyByNameAndAddress(boolean basicSearch, String companyName, String address, String city, State state, String zip) {
    	searchCompany(basicSearch, companyName, address, city, state, zip, CreateNew.Do_Not_Create_New); 	
    	return row_NameAndAddress(companyName, address);
    }
    

    /**
     * Generic search method. Can search by Company Name; First and Last Name;
     * or SSN/TIN Searches in that order
     *
     * @param social
     * @param ssntin
     * @param firstName
     * @param lastName
     * @param companyName
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected boolean searchAddressBookBy(boolean basicSearch, String social, TaxReportingOption ssntin, String firstName, String lastName, String companyName, String address, String city, State state, String zip, CreateNew createNew) {
        SearchResultsReturnPC contactSearchSelectButton = searchAddressBook(basicSearch, social, ssntin, firstName, lastName, companyName, address, city, state, zip, createNew);

        if (contactSearchSelectButton.isFound() && contactSearchSelectButton.getSelectToClick() == null && !(createNew.equals(CreateNew.Create_New_Always))) {
            return contactSearchSelectButton.isFound();
        } else if (!contactSearchSelectButton.isFound() && contactSearchSelectButton == null) {
            if (createNew.equals(CreateNew.Do_Not_Create_New)) {
                Assert.fail("Could not find existing contact and Create New was set to Do Not Create New.");
            }
            createNew(companyName, firstName, lastName);
            return contactSearchSelectButton.isFound();
        } else {
            boolean isFound = searchMultiplePages(contactSearchSelectButton.getSelectToClick());

            if (isFound && createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                clickWhenClickable(contactSearchSelectButton.getSelectToClick());
            } else if (!isFound && createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                createNew(companyName, firstName, lastName);
            } else if (isFound && (createNew == CreateNew.Do_Not_Create_New)) {
                clickWhenClickable(contactSearchSelectButton.getSelectToClick());
            } else if (createNew == CreateNew.Create_New_Always) {
                createNew(companyName, firstName, lastName);
            } else {
            	System.out.println("No Result Was Found in the Search When Searching for the contact" + firstName + " " + lastName + companyName + " at " +address);
//                Assert.fail("No Result Was Found in the Search When Searching for Contact");
            }
            waitForPageLoad();
            return isFound;
        }
    }

    // search for company contact
    private SearchResultsReturnPC searchCompany(boolean basicSearch, String companyName, String address, String city, State state, String zip, CreateNew createNew) {
    	SearchAddressBookPC_basics basicSearchPage = new SearchAddressBookPC_basics(driver);
    	if(!basicSearch) {
    		setSearchCriteria(companyName, null, null, city, state, zip);
    	} else if(!basicSearchPage.basicSearchExists()){
    		setSearchCriteria(companyName, null, null, city, state, zip);
    	} else {	
    		basicSearchPage.setSearchCriteria(null, null, companyName, null, null, null, city, state, zip, null);
    	}
    	clickSearch();

        SearchResultsReturnPC toReturn = new SearchResultsReturnPC(getDriver());
        toReturn.setFound(row_NameFound(companyName));

        if (createNew == CreateNew.Do_Not_Create_New || createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
            if (address != null) {
                // search result may or may-not have the company address so if it cannot find by name+address it searches results by name only
                try {
                    toReturn.setSelectToClick(button_SearchAddressBookResultsByCompanyNameWithAddress(companyName, address));
                    return toReturn;
                } catch (Exception e) {
                    toReturn.setSelectToClick(button_SearchAddressBookResultsByCompanyName(companyName));
                    return toReturn;
                }
            } else {
                toReturn.setSelectToClick(button_SearchAddressBookResultsByCompanyName(companyName));
                return toReturn;
            }
        }

        return toReturn;
    }

    // search for person contact
    private SearchResultsReturnPC searchPerson(boolean basicSearch, String firstName, String lastName, String address, String city, State state, String zip, CreateNew createNew) {

    	SearchAddressBookPC_basics basicSearchPage = new SearchAddressBookPC_basics(driver);
    	if(!basicSearch) {
    		
    		basicSearchPage.clickAdvancedSearch();
    		try {
		            setAddressBookType(ContactSubType.Person);
		        } catch (NullPointerException npe) {
		        } catch (Exception e) {
		            throw e;
		        }
		        setSearchCriteria(null, firstName, lastName, city, state, zip);
		        System.out.println("Non Solr Search");
    	}else if(!basicSearchPage.basicSearchExists()) {
    		basicSearchPage.clickAdvancedSearch();
    		try {
	            setAddressBookType(ContactSubType.Person);
	        } catch (NullPointerException npe) {
	        } catch (Exception e) {
	            throw e;
	        }
	        setSearchCriteria(null, firstName, lastName, city, state, zip);
    	} else {
    			basicSearchPage.setSearchCriteria(null, null, null, firstName, lastName, null, city, state, zip, null);
    	}
        clickSearch();
        waitForPageLoad();
        SearchResultsReturnPC toReturn = new SearchResultsReturnPC(getDriver());
        toReturn.setFound(row_NameFound(firstName, lastName));

        if (createNew == CreateNew.Do_Not_Create_New || createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
            if (address != null) {
                // search result may or may not have the company address so if it cannot find by name + address combo it searches results by name only.
                //				try {
                systemOut("Search by Name and Address");
                toReturn.setSelectToClick(button_SearchAddressBookResultsByPersonNameWithAddress(firstName, lastName, address));
                return toReturn;
                //				} catch (Exception e) {
                //									//					toReturn.setSelectToClick(button_SearchAddressBookResultsByPersonName(firstName, lastName));
                //					return toReturn;
                //				}
            } else {
                systemOut("Search by Name2");
                toReturn.setSelectToClick(button_SearchAddressBookResultsByPersonName(firstName, lastName));
                return toReturn;
            }
        }
        return toReturn;
    }

    public SearchResultsReturnPC searchContactWithoutAddress(boolean basicSearch, String firstName, String lastNameOrCompanyName, CreateNew createNew) throws GuidewireException {
        SearchResultsReturnPC toReturn = new SearchResultsReturnPC(getDriver());
        SearchAddressBookPC_basics basicSearchPage = new SearchAddressBookPC_basics(driver);
        if(basicSearch && basicSearchPage.basicSearchExists()) {
        			//setSearchCriteria(String companyName, String firstName, String lastName, String city, State state, String zip)
        	basicSearchPage.setSearchCriteria(lastNameOrCompanyName, firstName, lastNameOrCompanyName, null, null, null);
        } else {
	        if (firstName == null || firstName.equals("")) {
	            setAddressBookType(ContactSubType.Company);
	            //setSearchCriteria(String companyName, String firstName, String lastName, String city, State state, String zip)
	            setSearchCriteria(lastNameOrCompanyName, null, null, null, null, null);
	            clickSearch();
	
	            toReturn.setFound(row_NameAndAddress(lastNameOrCompanyName, null));
	            if (toReturn.isFound()) {
	                toReturn.setSelectToClick(button_SearchAddressBookResultsByCompanyNameWithAddress(lastNameOrCompanyName, null));
	                toReturn.getSelectToClick().click();
	            }
	            return toReturn;
	        } else {
	            try {
	                setAddressBookType(ContactSubType.Person);
	            } catch (NullPointerException npe) {
	            } catch (Exception e) {
	                throw e;
	            }
	            setSearchCriteria(null, firstName, lastNameOrCompanyName, null, null, null);
	            clickSearch();
	
	            toReturn.setFound(row_NameFound(firstName, lastNameOrCompanyName));
	            if (toReturn.isFound()) {
	                toReturn.setSelectToClick(button_SearchAddressBookResultsByPersonNameWithAddress(firstName, lastNameOrCompanyName, null));
	                toReturn.getSelectToClick().click();
	            }
	            return toReturn;
	        }
        }return toReturn;
    }
    
    public void selectSSNTIN(TaxReportingOption SSNTIN) {
        Guidewire8Select commonSSNTIN = select_SearchAddressBookPCSSNTIN();
        // clickWhenVisible(commonSSNTIN);
        commonSSNTIN.selectByVisibleText(SSNTIN.getValue());
    }

    public void setTaxID(String taxIDToFill) {
        clickWhenVisible(editbox_SearchAddressBookPCSSNTIN);
        editbox_SearchAddressBookPCSSNTIN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SearchAddressBookPCSSNTIN.sendKeys(taxIDToFill);
    }

    // search for contact via SSN/TIN
    private SearchResultsReturnPC searchSSNTIN(boolean basicSearch, String social, TaxReportingOption ssntin, CreateNew createNew) {
        SearchAddressBookPC_basics basicSearchPage = new SearchAddressBookPC_basics(driver);
        if(!basicSearch) {
        	basicSearchPage.clickAdvancedSearch();
        	selectSSNTIN(ssntin);
        	setTaxID(social);
        } else if(!basicSearchPage.basicSearchExists()) {
        	basicSearchPage.clickAdvancedSearch();
        	selectSSNTIN(ssntin);
        	setTaxID(social);
        } else {
        	if(ssntin.equals(TaxReportingOption.SSN)) {
        		basicSearchPage.setSearchCriteria(null, social, null, null, null, null, null, null, null, null);
        	} else {
        		basicSearchPage.setSearchCriteria(social, null, null, null, null, null, null, null, null, null);
        	}  
        }	
        
        clickSearch();

        SearchResultsReturnPC toReturn = new SearchResultsReturnPC(getDriver());
/*      toReturn.setFound(row_NameFound(social));   
 		Changed to click the first select when searching by SSN-TIN because the Social or tin are not displayed.
*/		toReturn.setFound(new TableUtils(driver).getRowCount(getTable_SearchAddressBookPCSearchResults()) > 0);
        if ((createNew == CreateNew.Do_Not_Create_New || createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) && toReturn.isFound()) {
        	toReturn.setSelectToClick(new TableUtils(driver).getSelectLinkInTable(getTable_SearchAddressBookPCSearchResults(), 1));
            return toReturn;
        } else {
        	toReturn.setSelectToClick(null);
        }

        return toReturn;
    }

    // search multiple pages
    private Boolean searchMultiplePages(WebElement contactSearchSelectButton) {

        boolean multiplePages = false;
        boolean isFound = false;
        do {
            isFound = checkIfElementExists(contactSearchSelectButton, 2000);

            try {
                if (!isFound) {
                    multiplePages = new TableUtils(getDriver()).incrementTablePageNumber(getTable_SearchAddressBookPCSearchResults());
                }
            } catch (Exception e) {
                multiplePages = false;
            }
        } while (!isFound && multiplePages);

        return isFound;
    }

    // create new contact

    public void createNew(String companyName, String firstName, String lastName) {
    	waitForPageLoad();
        if (companyName != null) {
            clickSearchAddressBookCreateNew(ContactSubType.Company);
        } else if (firstName != null && lastName != null) {
            clickSearchAddressBookCreateNew(ContactSubType.Person);
        } else {
            Assert.fail("You cannot create a new contact without specifying a company or person name.");
        }

    }


    public boolean checkIfSelectbuttonExists(WebElement contactSearchSelectButton) {
        return checkIfElementExists(contactSearchSelectButton, 2000);
    }


    public void ClickReturnToPolicyInfo() {
        clickWhenClickable(link_ReturnToPolicyInfo);

    }

    public boolean checkIfCreateNewButtonExists() {
        return checkIfElementExists(button_SearchAddressBookCreateNew, 2000);
    }


    public boolean checkContactSearchFoundByFirstName(String firstName) {
        int count = 0;
        try {
            WebElement selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//div[contains(., '" + firstName + "' )]/../../td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(@id, 'Select'))]"));
            return selectButton.isDisplayed();
        } catch (Exception e) {
            count = new TableUtils(getDriver()).getRowCount(getTable_SearchAddressBookPCSearchResults());
            return count > 0;
        }

    }


    public WebElement searchAddressBookByPersonDetails(String firstName, String lastName, String city, State state, String zip) {
        WebElement selectButton = null;
        //        try {
        if (finds(By.xpath("//div[contains(@id, ':ContactSubtype-inputEl') and contains(@role, 'textbox') and text()='Person']")).isEmpty()) {
            setAddressBookType(ContactSubType.Person);
        }
        setSearchCriteria(null, firstName, lastName, city, state, zip);
        clickSearch();
        try {
            selectButton = getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//div[contains(., '" + firstName + "' )]/../../td//a[(contains(@id, 'ContactSearchScreen:')) and (contains(@id, 'Select'))]"));

        } catch (Exception e) {

        }

        return selectButton;
    }


    public void clickSelectButton() {
        clickWhenClickable(getTable_SearchAddressBookPCSearchResults().findElement(By.xpath(".//a[contains(@id, ':select') or contains(., 'Select')]")));
    }


    public boolean searchForContact(boolean basicSearch, AdditionalInterest addInterest) {

        if (addInterest.getCompanyOrInsured() == ContactSubType.Company) {
// 					****			Modified Additional Interest search because create new is going away for Lienholders. Steve Broderick 5/9/2018      ****
//			switch (addInterest.getNewContact()) {
//			case Create_New_Always:
//				addInterest.setCompanyName(getUniqueName(addInterest.getCompanyName()));
//				return searchAddressBookByCompanyName(addInterest.getCompanyName(), addInterest.getAddress(), CreateNew.Create_New_Always);
//			case Create_New_Only_If_Does_Not_Exist:
//				String lienHolderNameToCreate = addInterest.getCompanyName();
//				if (lienHolderNameToCreate.length() > 30) {
//					lienHolderNameToCreate = lienHolderNameToCreate.substring(0, 29);
//				}
//				addInterest.setCompanyName(lienHolderNameToCreate);
//				return searchAddressBookByCompanyName(lienHolderNameToCreate, addInterest.getAddress(), CreateNew.Create_New_Only_If_Does_Not_Exist);
//			case Do_Not_Create_New:
            return searchAddressBookByCompanyName(basicSearch, addInterest.getCompanyName(), addInterest.getAddress(), CreateNew.Do_Not_Create_New);
//			}
        } else {
//			switch (addInterest.getNewContact()) {
//			case Create_New_Always:
//				addInterest.setPersonLastName(getUniqueName(addInterest.getPersonFirstName(), null, addInterest.getPersonLastName())[2]);
//				return searchAddressBookByFirstLastName(addInterest.getPersonFirstName(), addInterest.getPersonLastName(), addInterest.getAddress(), CreateNew.Create_New_Always);
//			case Create_New_Only_If_Does_Not_Exist:
//				String lienHolderLastNameToCreate = addInterest.getPersonLastName();
//				String fullName = addInterest.getPersonFirstName() + " " + lienHolderLastNameToCreate;
//				if (fullName.length() > 30) {
//					int numberOfCharactersToRemove = fullName.length() - 30;
//					int lastNameLength = lienHolderLastNameToCreate.length();
//					lienHolderLastNameToCreate = lienHolderLastNameToCreate.substring(0, lastNameLength - numberOfCharactersToRemove);
//				}
//				addInterest.setPersonLastName(lienHolderLastNameToCreate);
//				return searchAddressBookByFirstLastName(addInterest.getPersonFirstName(), lienHolderLastNameToCreate, addInterest.getAddress(), CreateNew.Create_New_Only_If_Does_Not_Exist);
//			case Do_Not_Create_New:
            return searchAddressBookByFirstLastName(basicSearch, addInterest.getPersonFirstName(), addInterest.getPersonLastName(), addInterest.getAddress(), CreateNew.Do_Not_Create_New);
//			}
        }
//		return false;
    }

    public boolean searchForContact(boolean basicSearch, Contact contactToSearchFor) throws GuidewireException {

        if (contactToSearchFor.getPersonOrCompany() == ContactSubType.Company) {
            //Always do CreateNewIfDoesNotExist
            String contactNameToCreate = contactToSearchFor.getCompanyName();
            if (contactNameToCreate.length() > 30) {
                contactNameToCreate = contactNameToCreate.substring(0, 29);
            }
            contactToSearchFor.setCompanyName(contactNameToCreate);
            return searchAddressBookByCompanyName(basicSearch, contactNameToCreate, contactToSearchFor.getAddressList().get(0), CreateNew.Create_New_Only_If_Does_Not_Exist);
        } else {
            //Always do CreateNewIfDoesNotExist
            String contactLastNameToCreate = contactToSearchFor.getLastName();
            String fullName = contactToSearchFor.getFirstName() + " " + contactLastNameToCreate;
            if (fullName.length() > 30) {
                int numberOfCharactersToRemove = fullName.length() - 30;
                int lastNameLength = contactLastNameToCreate.length();
                contactLastNameToCreate = contactLastNameToCreate.substring(0, lastNameLength - numberOfCharactersToRemove);
            }
            contactToSearchFor.setLastName(contactLastNameToCreate);
            return searchAddressBookByFirstLastName(basicSearch, contactToSearchFor.getFirstName(), contactLastNameToCreate, (contactToSearchFor.getAddressList().isEmpty()) ? contactToSearchFor.getAddress() : contactToSearchFor.getAddressList().get(0), CreateNew.Create_New_Only_If_Does_Not_Exist);
        }
    }

    public String getSearchResultDescription(String text) {
        WebElement row = new TableUtils(getDriver()).getRowInTableByColumnNameAndValue(tableDiv_SearchAddressBook, "Description", text);
        return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(tableDiv_SearchAddressBook, row, "Description");
    }
    
    public boolean searchResultsExistWithOtherNameButSameAccountString(String acctNum, String firstName, String companyNameOrLastName) {
    	clickReset();
    	if(firstName == null) {
    		setCompanyName(companyNameOrLastName);
    	} else {
    		setAddressBookType(ContactSubType.Person);
    		setFirstName(firstName);
    		setLastName(companyNameOrLastName);
    		clickSearch();
    	}
    	List<WebElement> acctSearchResults = getTable_SearchAddressBookPCSearchResults().findElements(By.xpath("./tr/td/div[.='" + acctNum + "']/parent::td/parent::tr"));
    	for(WebElement searchResult : acctSearchResults) {
    		if(!(searchResult.findElement(By.xpath("./div[contains(.,'" + companyNameOrLastName +"')]")).getText().contains(companyNameOrLastName) && (searchResult.findElement(By.xpath("./div[contains(.,'" + firstName +"')]")).getText().contains(companyNameOrLastName)))){
    			return true;
    		}
    	}
    	return false;
    }   
    
    public void addRelatedContact(Contact relatedContact, RelationshipsAB relationship ) throws GuidewireException {
    	if(checkIfElementExists(link_RelatedContactsTab, 500)) {
    		clickWhenClickable(link_RelatedContactsTab);
    	}
    	clickAdd();
    	TableUtils tableUtils = new TableUtils(driver);
    	int row = tableUtils.getNextAvailableLineInTable(div_RelatedContactsContainer);
    	tableUtils.selectValueForSelectInTable(div_RelatedContactsContainer, row, "Relationship", relationship.getValue());
    	if(checkIfElementExists("//div[contains(@id, ':RelatedContactsPanelSet:') and not(contains(@id,'-body')) or contains(@id,'MembershipMemberPopup:MembershipMemberCV:1') and not(contains(@id,'-body'))]//span[.='Name']", 500)) {
    		tableUtils.clickCellInTableByRowAndColumnName(div_RelatedContactsContainer, row, "Name");
    	} else {
    		tableUtils.clickCellInTableByRowAndColumnName(div_RelatedContactsContainer, row, "Related To");
    	}
    	
    	if(checkIfElementExists("//div[contains(@id, ':pick:Selectpick')]", 500)) {
	    	clickWhenClickable(By.xpath("//div[contains(@id, ':pick:Selectpick')]"));
	    	
    	} else {
    		clickWhenClickable(button_MembershipMemberRelatedContactsChangeRelatedTo(row));
 	    	clickWhenClickable(button_MembershipMemberRelatedContactsSearchPerson(row));	
    	}
    	
    	SearchAddressBookPC searchPC = new SearchAddressBookPC(getDriver());
 	    boolean found = searchPC.searchForContact(true, relatedContact);
 	    waitForPostBack();
        if (!found) {
            SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(getDriver());
            createAccountPage.createNewContact(relatedContact);
        }
		clickWhenClickable(link_RelatedContactsBasicsOrContactDetail); 
    	
    }
    
    /*
     *  public void addMembershipDuesRelatedContacts(boolean basicSearch, Contact contactToAdd) throws GuidewireException {
    	clickAdd();
 	    
 	    int tableRow = tableUtils.getNextAvailableLineInTable(table_MembershipMemberRelatedContacts);
 	    tableUtils.selectValueForSelectInTable(table_MembershipMemberRelatedContacts, tableRow, "Relationship", contactToAdd.getContactRelationshipToPNI().getValue());
 	    if(checkIfElementExists(button_MembershipMemberRelatedContactsChangeRelatedTo(tableRow), 500)) {
 	    	clickWhenClickable(button_MembershipMemberRelatedContactsChangeRelatedTo(tableRow));
 	    	clickWhenClickable(button_MembershipMemberRelatedContactsSearchPerson(tableRow));	
 	    } else {
 	    	tableUtils.clickCellInTableByRowAndColumnName(table_MembershipMemberRelatedContacts, tableRow, "Name");
 	 	    WebElement button_MembershipMemberNamePicker = table_MembershipMemberRelatedContacts.findElement(By.xpath("//div[contains(@id,'MembershipMemberPopup:MembershipMemberCV:" + (tableRow - 1) + ":pick:Selectpick')]"));
 	 	    clickWhenClickable(button_MembershipMemberNamePicker);
 	    }
 	    SearchAddressBookPC searchPC = new SearchAddressBookPC(getDriver());
 	    boolean found = searchPC.searchForContact(basicSearch, contactToAdd);
 	    waitForPostBack();
        if (!found) {
            SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(getDriver());
            createAccountPage.createNewContact(contactToAdd);
        }
    }
     * */
}
