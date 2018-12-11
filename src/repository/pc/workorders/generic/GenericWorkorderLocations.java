package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.LocationType;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.errorhandling.ErrorHandlingLocations;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderLocations extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderLocations(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//span[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:AdditionalCoveragesCardFBTab-btnEl']")
    private WebElement tab_SubmissionLocationInformationDetailsAdditionalCoverages;

    @FindBy(xpath = "//span[contains(@id, 'GeneralInfoCardTab-btnEl')]")
    private WebElement tab_SubmissionLocationInformationDetails;

    @FindBy(xpath = "//span[contains(@id,'InputSet:StandardizeAddress-btnEl')]")
    private WebElement button_SubmissionLocationInformationDetailsStandardizeAddress;

    @FindBy(xpath = "//input[contains(@id, ':LocationName-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsLocationName;

    private Guidewire8Select select_SubmissionLocationInformationDetailsLocationAddress() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':SelectedAddress-triggerWrap')]");
    }

    private Guidewire8Select select_SubmissionLocationInformationDetailsLocationAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'LocationDetailInputSet:LocationType-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id, 'BOPLocationsScreen:BOPLocationsPanelSet:LocationsEdit_DP_tb:Remove-btnInnerEl')]")
    private WebElement Remove;

    @FindBy(xpath = "//div[contains(@id, 'PolicyChangeWizard:LOBWizardStepGroup:LineWizardStepSet:BOPLocationsScreen:BOPLocationsPanelSet:LocationsEdit_DP:LocationsEdit_LV') or contains(@id, ':HOLocationsScreen:HOLocationsHOEPanelSet:LocationsEdit_DP:LocationsEdit_LV')]")
    private WebElement table_Locations;

    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsAddressLine1;

    @FindBy(xpath = "//input[contains(@id, ':AddressLine2-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsAddressLine2;

    @FindBy(xpath = "//div[contains(@id, ':AddressLine1-inputEl')]")
    private WebElement div_SubmissionLocationInformationDetailsAddressLine1;

    @FindBy(xpath = "//textarea[contains(@id, 'InputSet:PolicyLocationType_FBMInputSet:PolicyLocation_FBMInputSet:Legal')]")
    private WebElement editbox_SubmissionLocationInformationDetailsLegal;

    private String getSectionXpath(int row, int column) {
        int rowUsed = row - 1;
        int colunmUsed = column - 1;
        return "//table[contains(@id, 'PolicyLocationType_FBMInputSet:PolicyLocationType_FBMInputSet:SectionTownshipRangeInputSet:'" + rowUsed + "':'" + colunmUsed + "':strVal:strVal_Arg')]";
    }

    Guidewire8Select select_SubmissionLocationInformationDetailsSection(int row, int column) {
        return new Guidewire8Select(driver, getSectionXpath(row, column));
    }

    @FindBy(xpath = "//a [contains(text(),'Add Row')]")
    private WebElement button_SubmissionLocationInformationDetailsStandardizeSectionAddRow;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsCity;

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsZipCode;

    @FindBy(xpath = "//input[contains(@id, ':County-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsCounty;

    @FindBy(xpath = "//input[contains(@id, ':County-inputEl')]")
    private WebElement select_SubmissionLocationInformationDetailsCounty;

    @FindBy(xpath = "//input[contains(@id, ':Acres-inputEl')]")
    private WebElement select_SubmissionLocationInformationDetailsSectionAcres;

    @FindBy(xpath = "//input[contains(@id, ':Phone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_SubmissionLocationInformationDetailsPhone;

    private Guidewire8Select select_SubmissionLocationInformationDetailsManualProtectionClassCode() {
        return new Guidewire8Select(driver, "//table[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:FireProtectionClass-triggerWrap']");
    }

    private Guidewire8Select select_SubmissionLocationInformationDetailsAutoIncreaseLimitPercentage() {
        return new Guidewire8Select(driver, "//table[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:0:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:TypekeyTermInput-triggerWrap']");
    }

    @FindBy(xpath = "//span[contains(@id, 'LocationScreen:LocationDetailPanelSet:fred:Update-btnEl')]")
    private WebElement locationOKButton;

    @FindBy(xpath = "//input[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:AnnualGrossReceipts-inputEl']")
    private WebElement editbox_SubmissionLocationInformationDetailsAnnualGrossReceipts;

    @FindBy(xpath = "//div[contains(@id, 'BOPLocationsScreen:BOPLocationsPanelSet:LocationsEdit_DP:LocationsEdit_LV-body')]/div/table")
    private WebElement table_SubmissionLocationInformationDetailsLocations;

    @FindBy(xpath = "//span[contains(@id, ':Add-btnEl')]")
    private WebElement button_SubmissionLocationInformationDetailsNewLocation;

    Guidewire8RadioButton radio_NonSpecificLocation() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'LocationDetailInputSet:NonSpecificLocation')]");
    }

    private WebElement button_SubmissionLocationInformationDetailsLocationEdit(int locationNumber) {
        return find(By.xpath("//div[contains(@id, 'BOPLocationsPanelSet:LocationsEdit_DP:LocationsEdit_LV-body')]/div/table/tbody/child::tr/child::td/div/a[text()='" + Integer.toString(locationNumber) + "']/parent::div/parent::td/preceding-sibling::td/div/a[text()='Edit']"));
    }

    private WebElement text_SubmissionLocationInformationDetailsLocationNumberCellLast(AddressInfo address) {
        List<WebElement> allRowsWithThatAddress = new ArrayList<WebElement>();
        String addressToSearch = "";

        allRowsWithThatAddress = finds(By.xpath("//div[contains(@id, 'BOPLocationsScreen:BOPLocationsPanelSet:LocationsEdit_DP:LocationsEdit_LV-body')]/div/table/child::tbody/child::tr/child::td/div/a[contains(@id, ':Address') and (contains(text(), '" + addressToSearch + "'))]/parent::div/parent::td/preceding-sibling::td[4]/child::div/child::a"));
        return allRowsWithThatAddress.get(allRowsWithThatAddress.size() - 1);
    }

    private Guidewire8RadioButton radio_Playground() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:PlaygroundQuestion']");
    }

    private Guidewire8RadioButton radio_Pool() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:SwimmingPoolQuestion']");
    }

    private Guidewire8RadioButton radio_SeasonalOperation() {
        return new Guidewire8RadioButton(driver, "//table[@id='BOPLocationPopup:LocationScreen:LocationDetailPanelSet:LocationDetailCV:LocationDetailDV:LineLocationDetailInputSet:SeasonalOperationQuestion']");
    }

    @FindBy(xpath = "//div[contains(@id, 'messagebox-1001')]")
    private WebElement popup_AreYouSure;

    @FindBy(xpath = "//a[contains(@id, ':LocationsEdit_DP:LocationsEdit_LV:0:EditLink') or contains(@id, ':LocationsEdit_DP:LocationsEdit_LV:0:Loc')]")
    private WebElement button_PrimaryLocationEdit;

    @FindBy(xpath = "//a[contains(@id, 'BOPLocationPopup:__crumb__') and contains(., 'Return to Locations')]")
    private WebElement button_ReturnToLocations;

    @FindBy(xpath = "//div[contains(@id,'targetEl')]//span[contains(@id, 'Blank_tb:LocationToolbarButtonSet:Update-btnInnerEl')]")
    private List<WebElement> button_BottomPageOK;

    @FindBy(xpath = "//div[contains(@id,'targetEl')]//span[contains(@id, 'Blank_tb:LocationToolbarButtonSet:ToolbarButton-btnInnerEl')]")
    private List<WebElement> button_BottomPageCancel;

    @FindBy(xpath = "//div[contains(@id,'PolicyLocationType_FBMInputSet:State-inputEl')]")
    private List<WebElement> text_StaticStateIdaho;

    @FindBy(xpath = "//span[contains(@id, ':addAllLocationsButton-btnEl')]")
    private WebElement button_AddAllExistingLocations;

//    private WebElement link_LocationAdditionalInsured(String addInsured) {
//        return find(By.xpath("//a[contains(., '" + addInsured + "')]"));
//    }

    @FindBy(xpath = "//div[contains(@id,'CPPLocations_FBMPanelSet:LocationsEdit_DP:LocationsEdit_LV-body')]/div/table/tbody/tr[last()]/td[4]")
    private WebElement tdLastLocationNumber;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    ////////////////////////////////////////////////////////////
    // Main Helper Methods (Designed to do the 'Heavy Lifting')//
    ////////////////////////////////////////////////////////////

    
    public void fillOutBOPLocation_QQ(GeneratePolicy generatePolicy) throws Exception {
    	new repository.pc.sidemenu.SideMenuPC(generatePolicy.getWebDriver()).clickSideMenuLocations();
		for(PolicyLocation location : generatePolicy.busOwnLine.locationList) {
			if(!finds(By.xpath("//span[@id='BOPLocationPopup:LocationScreen:0']")).isEmpty()) {
				clickCancel();
				selectOKOrCancelFromPopup(OkCancel.OK);
			}
			if(location.getNumber() == -1) {
				clickLocationsNewLocation();
		        setQuickQuoteQuestions(location);
		        addLocationAdditionalInsureds(false, location);
		        if (!generatePolicy.busOwnLine.getCurrentPolicyType().equals(GeneratePolicyType.QuickQuote)) {
		            setFullAppQuestions(location);
		        }
		        clickLocationsAdditionalCoverages();
		        new GenericWorkorderLocationsAdditionalCoverages(generatePolicy.getWebDriver()).fillOutLocationsAdditionalCoverages(location);
		        clickLocationsOk();
		        if (!finds(By.xpath("//*[contains(text(), 'Please attempt to standardize the address before leaving the page')]")).isEmpty()) {
		            clickLocationsDetails();
		            locationStandardizeAddress(location);
		            clickLocationsOk();
		        }
		        location.setNumber(getLocationsLocationNumberCellLast(location.getAddress()));
			}
		}
	}
    
    
    
    
    
    
    
    
    
    

    public void setNonSpecificAddress(boolean yesno) {
        radio_NonSpecificLocation().select(yesno);
    }

    private void setFullAppQuestions(PolicyLocation location) {
        setLocationsSeasonalOperation(location.isSeasonalYes());
        setLocationsSwimmingPool(location.isPoolYes());
        setLocationsPlayground(location.isPlaygroundYes());
        setLocationsAnnualGrossReceipts(location.getAnnualGrossReceipts());
        clickProductLogo();
    }


    private void setQuickQuoteQuestions(PolicyLocation loc) {
        Guidewire8Select address = select_SubmissionLocationInformationDetailsLocationAddress();
        List<String> addresses = address.getList();
        if (addresses.contains(loc.getAddress().getDropdownAddressFormat())) {
            setLocationsLocationAddress(loc.getAddress().getDropdownAddressFormat());
        } else {
            setLocationsLocationAddress("New...");
            setLocationsAddressLine1(loc.getAddress().getLine1());
            if (loc.getAddress().getLine2() != null) {
                setLocationsAddressLine2(loc.getAddress().getLine2());
            }
            setLocationsCity(loc.getAddress().getCity());
            setLocationsZipCode(loc.getAddress().getZip());
            setLocationsCounty(loc.getAddress().getCounty());
        }
        locationStandardizeAddress(loc);
        setLocationsAutoIncreaseLimitPercentage(loc.getAutoIncrease());
    }


    public void addNewLocationAndBuildings(boolean basicSearch, PolicyLocation loc, boolean quickQuote, boolean retry) throws Exception {
        setQuickQuoteQuestions(loc);
        addLocationAdditionalInsureds(basicSearch, loc);
        if (!quickQuote) {
            setFullAppQuestions(loc);
        }
        clickLocationsAdditionalCoverages();
        GenericWorkorderLocationsAdditionalCoverages location = new GenericWorkorderLocationsAdditionalCoverages(getDriver());
        location.fillOutLocationsAdditionalCoverages(loc);
        clickLocationsOk();
        if (!finds(By.xpath("//*[contains(text(), 'Please attempt to standardize the address before leaving the page')]")).isEmpty()) {
            clickLocationsDetails();
            locationStandardizeAddress(loc);
            clickLocationsOk();
        }
        loc.setNumber(getLocationsLocationNumberCellLast(loc.getAddress()));

        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.addBuildingsPerLocation(basicSearch, loc, quickQuote, true);
    }

    private void addLocationAdditionalInsureds(boolean basicSearch, PolicyLocation loc) throws Exception {
        if (loc.getAdditionalInsuredLocationsList().size() > 0) {
            for (PolicyLocationAdditionalInsured aiLoc : loc.getAdditionalInsuredLocationsList()) {
                repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured aiLocation = new repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured(driver);
                aiLocation.addAdditionalInsured(basicSearch, aiLoc, true);
            }
        }
    }


    public void fillOutLocationsBuildingsSubmissionQuick(boolean basicSearch, ArrayList<PolicyLocation> locationsToSet) throws Exception {
    	repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();
        int locationCounter = 1;
        for (PolicyLocation loc : locationsToSet) {
            if (locationCounter == 1) {
                addNewLocationAndBuildings(basicSearch, loc, true, true);
            } else if (checkIfElementExists(button_SubmissionLocationInformationDetailsNewLocation, 1000)) {
                clickLocationsNewLocation();
                addNewLocationAndBuildings(basicSearch, loc, true, true);
            }
            locationCounter++;
        }

        repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenuStuff.clickSideMenuBuildings();
    }


    public void fillOutLocationsBuildingSubmissionRemaining(ArrayList<PolicyLocation> locationsToFix) throws Exception {
        for (PolicyLocation loc : locationsToFix) {
            clickLocationsLocationEdit(loc.getNumber());

            setFullAppQuestions(loc);

            clickLocationsOk();

            ErrorHandlingLocations errorHandling = new ErrorHandlingLocations(getDriver());
            errorHandling.errorHandlingLocationsPage(1000, loc);
        }
        repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenuStuff.clickSideMenuBuildings();

        for (PolicyLocation loc : locationsToFix) {
            GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
            buildings.completeEachBuildingsPagePerLocation(loc, true);
        }
    }


    public void addAdditionalInsuredLocation(boolean basicSearch, PolicyLocationAdditionalInsured newLocationInsured) throws Exception {
        clickLocationsLocationEdit(1);

        repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured addtlLocInsured = new GenericWorkorderLocationAdditionalInsured(driver);
        addtlLocInsured.addAdditionalInsured(basicSearch, newLocationInsured, true);
        clickOK();
    }


    public void removeLocationByLocNumber(int locNum) {
        String locationNumberGridColumn = tableUtils.getGridColumnFromTable(table_Locations, "Loc. #");
        clickWhenClickable(table_Locations.findElement(By.xpath(".//tr/td[contains(@class,'" + locationNumberGridColumn
                + "') and contains(.,'" + locNum + "')]/parent::tr/td[contains(@class,'-checkcolumn')]")));
        clickRemove();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public void removeAllLocations() {
        tableUtils.setTableTitleCheckAllCheckbox(table_Locations, true);
        clickRemove();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Supporting Helper Methods (Designed to Carry Out Minimal Functions to Support Above Methods)//
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private int getLocationsLocationNumberCellLast(AddressInfo address) {
        return Integer.valueOf(text_SubmissionLocationInformationDetailsLocationNumberCellLast(address).getText());
    }


    public void setLocationsAnnualGrossReceipts(double annualGrossReceipts) {
    	setText(editbox_SubmissionLocationInformationDetailsAnnualGrossReceipts, String.valueOf(annualGrossReceipts));
    }


    public void setLocationsLocationAddress(String address) {
        if (address.contains("New...")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':SelectedAddress:newAddressBuddy')]")));
                return;
            }
        }

        Guidewire8Select mySelect = select_SubmissionLocationInformationDetailsLocationAddress();
        mySelect.selectByVisibleText(address);
    }


    private void setLocationsLocationType(LocationType locationType) {
        select_SubmissionLocationInformationDetailsLocationAddressType().selectByVisibleText(locationType.getValue());
    }


    public void setLocationsManualProtectionClassCode(ProtectionClassCode manualProtectionClassCode) {
        select_SubmissionLocationInformationDetailsManualProtectionClassCode().selectByVisibleText(manualProtectionClassCode.getValue());
    }


    public void setLocationsAutoIncreaseLimitPercentage(AutoIncreaseBlgLimitPercentage autoIncrease) {
        select_SubmissionLocationInformationDetailsAutoIncreaseLimitPercentage().selectByVisibleText(autoIncrease.getValue());
    }


    public void setLocationsPlayground(boolean radioValue) {
        radio_Playground().select(radioValue);
    }


    public void setLocationsSwimmingPool(boolean radioValue) {
        radio_Pool().select(radioValue);
    }


    public void setLocationsSeasonalOperation(boolean radioValue) {
        radio_SeasonalOperation().select(radioValue);
    }


    public void clickLocationsCancel() {
        super.clickCancel();
    }


    public void clickLocationsOk() {
        super.clickOK();
    }


    public void clickLocationsAdditionalCoverages() {
        clickWhenClickable(tab_SubmissionLocationInformationDetailsAdditionalCoverages);
    }


    private void clickLocationsDetails() {
        clickWhenClickable(tab_SubmissionLocationInformationDetails);
    }


    public void setLocationsAddressLine1(String addressLine1) {
        clickWhenClickable(editbox_SubmissionLocationInformationDetailsAddressLine1);
        editbox_SubmissionLocationInformationDetailsAddressLine1.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionLocationInformationDetailsAddressLine1.sendKeys(addressLine1);
    }


    private void setLocationsAddressLine2(String addressLine2) {
        clickWhenClickable(editbox_SubmissionLocationInformationDetailsAddressLine2);
        editbox_SubmissionLocationInformationDetailsAddressLine2.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionLocationInformationDetailsAddressLine2.sendKeys(addressLine2);
    }


    public void setLocationsCity(String city) {
        clickWhenClickable(editbox_SubmissionLocationInformationDetailsCity);
        editbox_SubmissionLocationInformationDetailsCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionLocationInformationDetailsCity.sendKeys(city);
    }


    public void setLocationsZipCode(String zipCode) {
        clickWhenClickable(editbox_SubmissionLocationInformationDetailsZipCode);
        editbox_SubmissionLocationInformationDetailsZipCode.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionLocationInformationDetailsZipCode.sendKeys(zipCode);
    }


    public void setLocationsCounty(String county) {
        clickWhenClickable(editbox_SubmissionLocationInformationDetailsCounty);
        editbox_SubmissionLocationInformationDetailsCounty.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SubmissionLocationInformationDetailsCounty.sendKeys(county);
    }


    public void clickLocationsStandardizeAddress() {
    	clickWhenClickable(button_SubmissionLocationInformationDetailsStandardizeAddress);
    }


    public void locationStandardizeAddress(PolicyLocation loc) {
    	clickLocationsStandardizeAddress();
        if (!finds(By.xpath("//span[contains(text(), 'Address not found')]")).isEmpty()) {
            super.clickOverride();
        } else {
            //this is a flag that is for use on the buildings page for geo mapping
            loc.setAddressIsStandardized(true);
            new GuidewireHelpers(driver).verifyAddress(loc.getAddress());
        }
    }
    


    public void clickLocationsLocationEdit(int locationNumber) {
        clickWhenClickable(button_SubmissionLocationInformationDetailsLocationEdit(locationNumber));
    }


    public void editLocationByAddress(String address) {
        clickWhenClickable(find(By.xpath("//a[contains(text(), '" + address + "')]/parent::div/parent::td/parent::tr/child::td/div/a[contains(text(), 'Edit')]")));
    }


    public void clickLocationsNewLocation() {
        clickWhenClickable(button_SubmissionLocationInformationDetailsNewLocation);
    }


    public void clickPrimaryLocationEdit() {
        clickWhenClickable(button_PrimaryLocationEdit);
    }


    public void checkLocationAddressExistsInDropdown(String loc) throws Exception {
        Guidewire8Select address = select_SubmissionLocationInformationDetailsLocationAddress();
        List<String> addresses = address.getList();
        for (String add : addresses) {
            if (add.contains(loc)) {
                throw new Exception("Address dropdown includes an address it should not");
            }
        }
    }

    public void locationStandardizeAddress() {
        clickLocationsStandardizeAddress();
        //check for override
        if (!finds(By.xpath("//span[contains(text(), 'Address not found')]")).isEmpty()) {
            super.clickOverride();
        }
    }


    public void checkTownshipDefaultsToIdaho() throws Exception {
        setLocationsLocationType(LocationType.SectionTownshipRange);
        if (text_StaticStateIdaho.size() == 0) {
            throw new Exception(
                    "State did not default to Idaho and is unchangable for Section, Township, Range Location Type.");
        }
    }

    public String getAddressLine1() {
        return div_SubmissionLocationInformationDetailsAddressLine1.getText();
    }


    private int getLastLocationNumber() {

        int locationNumber = 0;

        try {
            locationNumber = Integer.parseInt(tdLastLocationNumber.getText());
        } catch (Exception e) {
            return -1;
        }

        return locationNumber;
    }


    public void fillOutCPPLocations(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();
        GenericWorkorderLocations cppLocation = new GenericWorkorderLocations(driver);
        boolean firstTimeThru = true;
        for (PolicyLocation location : policy.commercialPackage.locationList) {
            if (!firstTimeThru) {
                cppLocation = new GenericWorkorderLocations(driver);
                cppLocation.clickLocationsNewLocation();
            }
            // locations address - new
            cppLocation = new GenericWorkorderLocations(driver);
            cppLocation.setLocationsLocationAddress("New...");
            // fil out address line 1 city zip
            cppLocation.setLocationsAddressLine1(location.getAddress().getLine1());
            cppLocation.setLocationsCity(location.getAddress().getCity());
            cppLocation.setLocationsZipCode(location.getAddress().getZip());
            cppLocation.setLocationsCounty(location.getAddress().getCounty());
            cppLocation.setNonSpecificAddress(location.isNonSpecificLocation());
            if (!location.isNonSpecificLocation()) {
                clickWhenClickable(find(By.xpath("//span[contains(@id, ':StandardizeAddress-btnEl')]")));
                if (!finds(By.xpath("//span[contains(text(), 'Override')]")).isEmpty()) {
                    clickWhenClickable(find(By.xpath("//span[contains(text(), 'Override')]")));
                }//END IF
                if (new GuidewireHelpers(driver).verifyAddress(location.getAddress())) {

                }//END IF
            }//END IF
            cppLocation.clickLocationsOk();

            // set policy object location number
            int locationNumber = cppLocation.getLastLocationNumber();
            location.setNumber(locationNumber);

            clickGenericWorkorderSaveDraft();
            firstTimeThru = false;
        }//END FOR
    }//END fillOutCPPLocations

	

}
