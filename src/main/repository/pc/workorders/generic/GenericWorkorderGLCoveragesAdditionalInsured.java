package repository.pc.workorders.generic;

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
import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.search.SearchAddressBookPC;

import java.util.List;

public class GenericWorkorderGLCoveragesAdditionalInsured extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderGLCoveragesAdditionalInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:InterestType')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'NewAdditionalInsuredLinePanelSet:WhatActivities')]")
    public WebElement editBoxDescActivitiesInsuredDoingForAI;

    Guidewire8RadioButton radio_SpecialWording() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:SpecialWording')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'NewAdditionalInsuredLinePanelSet:SpecialWordDescr')]")
    public WebElement editboxSpecialWordingDescription;

    @FindBy(xpath = "//textarea[contains(@id, 'NewAdditionalInsuredLinePanelSet:Accord101Descr')]")
    public WebElement editboxAcord101Description;

    Guidewire8RadioButton radio_WaiverOfSubroCA2404() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:WaiverofSubro')]");
    }

    Guidewire8RadioButton radio_DesignatedLocationsLimitCG2504() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:DesignatedCon') or contains(@id, ':NewAdditionalInsuredLinePanelSet:DesigLocationGenAgg_FBM')]");
    }

    Guidewire8RadioButton radio_DesignatedLocationsLimitCG2503() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:DesignatedCon') or contains(@id, ':NewAdditionalInsuredLinePanelSet:DesigLocationGenAgg_FBM')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'NewAdditionalInsuredLinePanelSet:EquipmentDescription')]")
    public WebElement editboxDescOfLeasedEquipment;


    Guidewire8RadioButton radio_OilAndGas() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInOilandGasIndustry')]");
    }

    Guidewire8RadioButton radio_UndergroundTanks() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInUndergroundTanksIndustry')]");
    }

    Guidewire8RadioButton radio_AircraftAndAirport() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInAircraftandAirportIndustry')]");
    }

    Guidewire8RadioButton radio_BridgeConstruction() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInBridgeConstructionIndustr')]");
    }

    Guidewire8RadioButton radio_DamsAndReservois() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInDamsandReservoirsIndustry')]");
    }

    Guidewire8RadioButton radio_Firearms() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInFirearmsIndustry')]");
    }

    Guidewire8RadioButton radio_RailRoads() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsRailroadsIndustry')]");
    }

    Guidewire8RadioButton radio_Mining() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'NewAdditionalInsuredLinePanelSet:IsInMiningIndustry')]");
    }

    @FindBy(xpath = "//span[contains(@id, ':FillInAdd-btnEl')]")
    public WebElement button_AddLocation;

    @FindBy(xpath = "//span[contains(@id, 'AddlInsuredOpLocPanelSet:PreLocPop-btnEl')]")
    public WebElement button_AddExistingLocation;

    @FindBy(xpath = "//textarea[contains(@id, ':ListProducts-inputEl')]")
    public WebElement editbox_ListOfProducts;

    @FindBy(xpath = "//label[contains(text(), 'Location(s) of Covered Operations')]/parent::td/parent::tr/parent::tbody/parent::table/parent::div/parent::td/parent::tr/following-sibling::tr/td/div")
    public WebElement table_LocationsOfCoveredOperations;

    @FindBy(xpath = "//span[contains(text(), 'Description Of Completed Operations') or contains(text(), 'Description of Designated Construction Project')]/parent::div/parent::div/parent::div/parent::div/parent::div/parent::div/ancestor::table")
    public WebElement tableNewLocations;

    @FindBy(xpath = "//span[contains(text(), 'Address') or contains(text(), 'Description of Designated Construction Project')]/parent::div/parent::div/parent::div/parent::div/parent::div/parent::div/ancestor::table")
    public WebElement tableNewAddress;

    @FindBy(xpath = "//span[contains(@id, ':InsuredAddRemoveToolbarButtonSet:ToolbarButton-btnEl')]")
    public WebElement buttonSearch;


    public void clickUpdate() {
        super.clickUpdate();
    }


    public void clickOverride() {
        super.clickOverride();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickSearch() {
        clickWhenClickable(buttonSearch);
    }


    public void selectType(AdditionalInsuredTypeGL type) {
        Guidewire8Select mySelect = select_Type();
        mySelect.selectByVisibleText(type.getValue());
        
    }


    public boolean setSpecialWording(boolean yesno) {
        radio_SpecialWording().select(yesno);
        
        return yesno;
    }


    public void setSpecialWordingDescription(String desc) {
        clickWhenClickable(editboxSpecialWordingDescription);
        editboxSpecialWordingDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxSpecialWordingDescription.sendKeys(desc);
    }


    public void setAcord101Description(String desc) {
        clickWhenClickable(editboxAcord101Description);
        editboxAcord101Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxAcord101Description.sendKeys(desc);
    }


    public void setDescriptionOfLeasedEquipment(String desc) {
        clickWhenClickable(editboxDescOfLeasedEquipment);
        editboxDescOfLeasedEquipment.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxDescOfLeasedEquipment.sendKeys(desc);
    }


    public boolean setWaiverOfSubroCA2404(boolean yesno) {
        radio_WaiverOfSubroCA2404().select(yesno);
        
        return yesno;
    }


    public void setDesignatedLocationsAggLimitCG2504(boolean yesno) {
        radio_DesignatedLocationsLimitCG2504().select(yesno);
    }


    public void setDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03(boolean yesno) {
        radio_DesignatedLocationsLimitCG2503().select(yesno);
    }


    public boolean addExistingLocation(PolicyLocation location) {
        clickWhenClickable(button_AddExistingLocation);
        
        if (!finds(By.xpath("//span[contains(text() , '" + location.getAddress().getLine1() + "')]")).isEmpty()) {
            find(By.xpath("//span[contains(text() , '" + location.getAddress().getLine1() + "')]")).click();
            return true;
        } else {
            return false;
        }
    }


    public void addLocation(PolicyLocation location) {
        clickWhenClickable(button_AddLocation);
        
        //Don't change the order of these or it will break. You Change it, You Fix It.
        setContractJobNumber("LN7783");
        setDescriptionOfCompletedOperation("Foo for Brett");
        setAddress(location);
    }


    public void setAddress(String locationAddress) {
        tableUtils.setValueForCellInsideTable(tableNewAddress, 1, "Address", "address", locationAddress);
    }


    public void setAddress(PolicyLocation location) {
        String addressString = location.getAddress().getLine1() + " " + location.getAddress().getCity() + " " + location.getAddress().getZip();
        tableUtils.setValueForCellInsideTable(tableNewAddress, 1, "Address", "address", addressString);
    }


    public void setDescriptionOfCompletedOperation(String desc) {
        try {
            tableUtils.setValueForCellInsideTable(tableNewAddress, tableUtils.getNextAvailableLineInTable(tableNewAddress), "Description Of Completed Operations", "DeisgnatedConstructionDeac", desc);
        } catch (Exception e) {
            tableUtils.setValueForCellInsideTable(tableNewAddress, tableUtils.getNextAvailableLineInTable(tableNewAddress), "Description of Designated Construction Project", "DeisgnatedConstructionDeac", desc);
        }
    }


    public void setContractJobNumber(String number) {
        tableUtils.setValueForCellInsideTable(tableNewAddress, tableUtils.getNextAvailableLineInTable(tableNewAddress), "Contract or Job Number", "ContractOrJobNumber", number);
    }


    public void removeLocation(PolicyLocation location) {
        tableUtils.setCheckboxInTable(tableNewLocations, tableUtils.getRowNumberInTableByText(tableNewLocations, location.getAddress().getLine1()), true);
        
        super.clickRemove();
    }


    public void selectStatesWhereInsuredPerformsActivities(List<State> stateList) {
        for (State state : stateList) {
            //check if its already checked
            if (finds(By.xpath("//label[contains(text(), '" + state.getName() + "')]/parent::div/parent::td/parent::tr/parent::tbody/parent::table[contains(@class, 'cb-checked')]")).isEmpty()) {
                find(By.xpath("//label[contains(text(), '" + state.getName() + "')]/preceding-sibling::input")).click();
            }
        }
    }


    public void setDescriptionOfActivityies(String desc) {
        clickWhenClickable(editBoxDescActivitiesInsuredDoingForAI);
        editBoxDescActivitiesInsuredDoingForAI.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBoxDescActivitiesInsuredDoingForAI.sendKeys(desc);
    }


    public void setOilandGas(boolean yesno) {
        radio_OilAndGas().select(yesno);
        
    }


    public void setUndergroundTanks(boolean yesno) {
        radio_UndergroundTanks().select(yesno);
        
    }


    public void setAircraftAndAirport(boolean yesno) {
        radio_AircraftAndAirport().select(yesno);
        
    }


    public void setBridgeConstruction(boolean yesno) {
        radio_BridgeConstruction().select(yesno);
        
    }


    public void setDamsAndReservoirs(boolean yesno) {
        radio_DamsAndReservois().select(yesno);
        
    }


    public void setFireamrs(boolean yesno) {
        radio_Firearms().select(yesno);
        
    }


    public void setRailroads(boolean yesno) {
        radio_RailRoads().select(yesno);
        
    }


    public void setMining(boolean yesno) {
        radio_Mining().select(yesno);
        
    }


    public void setListOfProducts(String productList) {
        clickWhenClickable(editbox_ListOfProducts);
        editbox_ListOfProducts.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ListOfProducts.sendKeys(productList);
    }


    public void fillOutAdditionalInsureds(GeneratePolicy policy) {
        //search for additonal insured
        String newCompName;
        String newFirstName;
        boolean found = false;

        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            repository.pc.search.SearchAddressBookPC searchAddressBookPage = new repository.pc.search.SearchAddressBookPC(getDriver());
            searchAddressBookPage = new repository.pc.search.SearchAddressBookPC(getDriver());
            for (CPPGLCoveragesAdditionalInsureds additionalInsured : policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist()) {
                clickSearch();

                if (additionalInsured.getCompanyPerson() == ContactSubType.Person) {
                    if (additionalInsured.getNewContact() == CreateNew.Create_New_Always) {
                        newFirstName = additionalInsured.getFirstName();// + dateString;
                        found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch, newFirstName, additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Always);
                    } else if (additionalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                        newFirstName = additionalInsured.getFirstName();// + dateString;
                        found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch, newFirstName, additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                    } else {
                        found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch,additionalInsured.getFirstName(), additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Do_Not_Create_New);
                    }
                } else {
                    if (additionalInsured.getNewContact() == CreateNew.Create_New_Always) {
                        newCompName = additionalInsured.getCompanyName();// + dateString;
                        found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, newCompName, additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Always);
                    } else if (additionalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                        newCompName = additionalInsured.getCompanyName();// + dateString;
                        found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, newCompName, additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                    } else if (additionalInsured.getNewContact() == CreateNew.Do_Not_Create_New) {
                        found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, additionalInsured.getCompanyName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Do_Not_Create_New);
                    }
                }//END ELSE

                repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured(getDriver());
                if (!found) {
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing("New...");
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressLine1(additionalInsured.getAddress().getLine1());
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressCity(additionalInsured.getAddress().getCity());
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressState(additionalInsured.getAddress().getState());
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressZipCode(additionalInsured.getAddress().getZip());
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressAddressType(additionalInsured.getAddress().getType());
                } else {
                    
                    editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing(additionalInsured.getFormatedAddress());
                }//END ELSE

                selectType(additionalInsured.getType());

                setTypeQuestions(additionalInsured);

                clickUpdate();
                if (!finds(By.xpath("//span[contains(text(), 'Location Information')]")).isEmpty()) {
                    clickOverride();
                    
                    clickUpdate();
                }//END IF

                //Duplicate contacts page handling
                if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
                    clickWhenClickable(find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")));
                    
                    clickUpdate();
                    
                }//END IF
            }//END FOR
        }
    }


    public void setTypeQuestions(CPPGLCoveragesAdditionalInsureds additionalInsured) {
        switch (additionalInsured.getType()) {
            case CertificateHolderOnly:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                break;
            case AdditionalInsured_CoownerOfInsuredPremises_CG_20_27:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());

                for (PolicyLocation location : additionalInsured.getLocationList()) {
                    addExistingLocation(location);
                }
                break;
            case AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03:
            case AdditionalInsured_ControllingInterest_CG_20_05:
            case AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32:
            case AdditionalInsured_GrantorOfFranchise_CG_20_29:
            case AdditionalInsured_GrantorOfLicenses_CG_20_36:
            case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12:
            case AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                break;
            case AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26:
                selectStatesWhereInsuredPerformsActivities(additionalInsured.getStateList());
                setDescriptionOfActivityies(additionalInsured.getDescriptionOfActivities());
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                setIndustries(additionalInsured);
                break;
            case AdditionalInsured_LessorOfLeasedEquipment_CG_20_28:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                setDescriptionOfLeasedEquipment(additionalInsured.getDescOfLeasedEquipment());
                break;
            case AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11:
            case AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24:
                setDescriptionOfActivityies(additionalInsured.getDescriptionOfActivities());
            case AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                setDesignatedLocationsAggLimitCG2504(additionalInsured.isDesignatedLocationsAggeragateLimitCG2504());
                for (PolicyLocation location : additionalInsured.getLocationList()) {
                    addExistingLocation(location);
                }
                break;
            case AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                for (PolicyLocation location : additionalInsured.getLocationList()) {
                    addLocation(location);
                }
                break;
            case AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10:
                selectStatesWhereInsuredPerformsActivities(additionalInsured.getStateList());
                setDescriptionOfActivityies(additionalInsured.getDescriptionOfActivities());
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                setIndustries(additionalInsured);
                setDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03(additionalInsured.isDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03());
                for (PolicyLocation location : additionalInsured.getLocationList()) {
                    addLocation(location);
                }
                break;
            case AdditionalInsured_Vendors_CG_20_15:
                if (setSpecialWording(additionalInsured.isSpecialWording())) {
                    setSpecialWordingDescription(additionalInsured.getSpecialWordingDesc());
                }
                setWaiverOfSubroCA2404(additionalInsured.isWaiverOfsubroCA2404());
                setListOfProducts(additionalInsured.getLsitOfProduct());
                break;
            default:
                break;
        }
    }


    public void setIndustries(CPPGLCoveragesAdditionalInsureds additionalInsured) {
        setOilandGas(additionalInsured.isOilAndGas());
        setUndergroundTanks(additionalInsured.isUndergroundTanks());
        setAircraftAndAirport(additionalInsured.isAircraftAndAirport());
        setBridgeConstruction(additionalInsured.isBridgeConstruction());
        setDamsAndReservoirs(additionalInsured.isDamsAndReservoirs());
        setFireamrs(additionalInsured.isFirearms());
        setRailroads(additionalInsured.isRailroads());
        setMining(additionalInsured.isMining());
    }


    public void checkAdditionalInsuredsAddress(GeneratePolicy policy) {
        //search for additonal insured
        String newCompName;
        String newFirstName;
        boolean found = false;

        repository.pc.search.SearchAddressBookPC searchAddressBookPage = new repository.pc.search.SearchAddressBookPC(getDriver());
        searchAddressBookPage = new SearchAddressBookPC(getDriver());
        for (CPPGLCoveragesAdditionalInsureds additionalInsured : policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist()) {
            clickSearch();

            if (additionalInsured.getCompanyPerson() == ContactSubType.Person) {
                if (additionalInsured.getNewContact() == CreateNew.Create_New_Always) {
                    newFirstName = additionalInsured.getFirstName();// + dateString;
                    found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch, newFirstName, additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Always);
                } else if (additionalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                    newFirstName = additionalInsured.getFirstName();// + dateString;
                    found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch, newFirstName, additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                } else {
                    found = searchAddressBookPage.searchAddressBookByFirstLastName(policy.basicSearch, additionalInsured.getFirstName(), additionalInsured.getLastName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Do_Not_Create_New);
                }
            } else {
                if (additionalInsured.getNewContact() == CreateNew.Create_New_Always) {
                    newCompName = additionalInsured.getCompanyName();// + dateString;
                    found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, newCompName, additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Always);
                } else if (additionalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                    newCompName = additionalInsured.getCompanyName();// + dateString;
                    found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, newCompName, additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
                } else if (additionalInsured.getNewContact() == CreateNew.Do_Not_Create_New) {
                    found = searchAddressBookPage.searchAddressBookByCompanyName(policy.basicSearch, additionalInsured.getCompanyName(), additionalInsured.getAddress().getLine1(), additionalInsured.getAddress().getCity(), additionalInsured.getAddress().getState(), additionalInsured.getAddress().getZip(), CreateNew.Do_Not_Create_New);
                }
            }

            repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured editAIBoLinePage = new GenericWorkorderBusinessownersLineAdditionalInsured(getDriver());
            if (!found) {
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing("New...");
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressLine1(additionalInsured.getAddress().getLine1());
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressCity(additionalInsured.getAddress().getCity());
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressState(additionalInsured.getAddress().getState());
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressZipCode(additionalInsured.getAddress().getZip());
                
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressAddressType(additionalInsured.getAddress().getType());
            } else {
                editAIBoLinePage.setEditAdditionalInsuredBOLineAddressListing(additionalInsured.getFormatedAddress());
            }

            selectType(additionalInsured.getType());

            setTypeQuestions(additionalInsured);

            clickUpdate();
            if (!finds(By.xpath("//span[contains(text(), 'Location Information')]")).isEmpty()) {
                clickOverride();
                
                clickUpdate();
            }

            //Duplicate contacts page handling
            if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
                find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")).click();
                
                clickUpdate();
                
            }
        }
    }


}






























