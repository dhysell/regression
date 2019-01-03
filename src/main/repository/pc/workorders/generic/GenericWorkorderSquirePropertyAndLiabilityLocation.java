package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Location.LocationType;
import repository.gw.enums.PLLocationType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyAndLiabilityLocation extends GenericWorkorder {
    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquirePropertyAndLiabilityLocation(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }
    
    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
	private WebElement editBox_ContactAddress;
    
    @FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_ContactCity;

    @FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:ttlBar')]")
    private WebElement text_GenericWorkorderSquirePropertyLocationsPageTitle;

    @FindBy(xpath = "//a[contains(@id, ':LocationsEdit_DP_tb:addLocationButton') and contains(.,'Add Existing Location')]")
    private WebElement button_GenericWorkorderSquirePropertyLocationAddExistingLocation;

    @FindBy(xpath = "//a[contains(@id, ':LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:HOLocationsHOEPanelSet:LocationsEdit_DP_tb:Add')]")
    private WebElement button_GenericWorkorderSquirePropertyLocationNewLocation;

    @FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:LocationsEdit_DP_tb:ToolbarButton:AddExisting:0:UnassignedAccountLocation')]/a/span")
    private List<WebElement> button_GenericWorkorderSquirePropertyLocations;

    @FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:LocationsEdit_DP_tb:ToolbarButton:AddExisting-textEl')]")
    private WebElement button_GenericWorkorderSquirePropertyLocationsAddExisting;

    @FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:LocationsEdit_DP_tb:setToPrimary') or contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:HOLocationsHOEPanelSet:LocationsEdit_DP_tb:setToPrimary')]")
    private WebElement button_SetAsPrimary;

    @FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:WarningNotEnoughAcres')]")
    private WebElement page_ValidationMessages;

    /*******************************************************************************************************
     * Location Information Repository Items
     *******************************************************************************************************/

    @FindBy(xpath = "//input[contains(@id,'HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:HOLocationInputSet:AddressLine1-inputEl') or contains(@id, ':AddressLine1-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationAddressLine1;

    @FindBy(xpath = "//input[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:HOLocationInputSet:AddressLine2-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationAddressLine2;

    @FindBy(xpath = "//input[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:HOLocationAddressFBMInputSet:AddressLine3-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationAddressLine3;

    @FindBy(xpath = "//input[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:HOLocationAddressFBMInputSet:City-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationCity;

    @FindBy(xpath = "//input[contains(@id, ':County-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationCounty;

    @FindBy(xpath = "//input[contains(@id,':PostalCode-inputEl')]")
    private WebElement editbox_GenericWorkorderSquirePropertyLocationInformationZipCode;

    @FindBy(xpath = "//a[contains(@id, ':addSTR_RowButton')]")
    private WebElement link_AddRow;

    @FindBy(xpath = "//div[contains(@id, 'HOLocationFBMPopup:_msgs')]")
    private WebElement locations_Validations;

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationCounty() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':County-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id,':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:LocationsEdit_DP_tb:ToolbarButton:AddExisting:')]/ancestor::div[3]/descendant::span")
    private List<WebElement> menuLink_GenericWorkorderSquirePropertyLocationAddExistingOptions;

    private WebElement editbox_GenericWorkorderSquirePropertyLocationSection(String row, String column) {
        return find(By.xpath("//input[contains(@id, ':SectionTownshipRangeInputSet:" + row + ":" + column + ":strVal:strVal_Arg-inputEl')]"));
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationTownshipNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:Township-triggerWrap') or contains(@id, 'HOLocation_FBMDetailsDV:HOLocationInputSet:Township-triggerWrap')]");
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationTownshipDirection() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:TownshipDirection-triggerWrap') or contains(@id, 'HOLocationInputSet:TownshipDirection-triggerWrap')]");
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationRangeNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:Range-triggerWrap') or contains(@id, 'HOLocationInputSet:Range-triggerWrap')]");
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationRangeDirection() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'HOLocationFBMPopup:HOLocation_FBMCV:RangeDirection-triggerWrap') or contains(@id, 'HOLocationInputSet:RangeDirection-triggerWrap')]");
    }

    private Guidewire8Select select_GenericWorkorderSquirePropertyLocationInformationAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LocationType-triggerWrap') or contains(@id, ':SelectedAddress-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':HOLocationsHOEPanelSet:LocationsEdit_DP:LocationsEdit_LV')]")
    private WebElement table_Locations;

    private WebElement button_EditLocation(int locNumber) {
        return tableUtils.getLinkInSpecficRowInTable(table_Locations, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_Locations, "Loc. #", String.valueOf(locNumber))));
    }

    private List<WebElement> buttonAddressExisting(String line1) {
        return finds(By.xpath("//span[contains(text(), '" + line1 + "')]/parent::a[contains(@id, 'UnassignedAccountLocation-itemEl')]"));
    }

    @FindBy(xpath = "//textarea[contains(@id, ':legalAddressLine-inputEl')]")
    private WebElement text_Legal;

    @FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:HOLocationsHOEPanelSet:LocationsEdit_DP:LocationsEdit_LV:') and (contains(@id,'EditLink') or contains(@id,':viewEdit_LinkAsBtn'))]")
    private List<WebElement> buttons_AllLocationEdits;


    @FindBy(xpath = "//span[contains(@id, ':StandardizeAddress-btnEl')]")
    private WebElement button_StandardizeAddress;

    @FindBy(xpath = "//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    private WebElement locationsOverrideButton;

    /*******************************************************************************************************
     * Location Information Methods
     *******************************************************************************************************/
    
    public void fillOutPropertyLocations(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
			if(location.getNumber() == -1) {
				policy.squire.summedNumAcres = policy.squire.summedNumAcres + location.getPlNumAcres();
				location.setNumber(addNewOrSelectExistingLocationQQ(location));
				if (!isPrimaryLocationSet()) {
					setLocationRowToPrimary(1);
				}//END IF
			}
        }//END FOR
    }
    
    public void clickEditLocation(int locationNumber) {
        clickWhenClickable(button_EditLocation(locationNumber));
        long endTime = new Date().getTime() + 5000;
        boolean onpage = !finds(By.xpath("//span[contains(text(),'Location Information')]")).isEmpty();
        while (!onpage && new Date().getTime() < endTime) {
            sleep(1); // Unsure if this is still needed.
            onpage = !finds(By.xpath("//span[contains(text(),'Location Information')]")).isEmpty();
        }
        if (!onpage) {
            Assert.fail("UNABLE TO GET TO LOCATION INFORMATION PAGE AFTER CLICKIGN EDIT");
        }
    }

    private void clickEditLocation(WebElement editToClick) {
        clickWhenClickable(editToClick);
    }

//    public List<WebElement> getAllLocationEdits() {
//        return buttons_AllLocationEdits;
//    }

    public void clickNewLocation() {
        clickWhenClickable(button_GenericWorkorderSquirePropertyLocationNewLocation);

    }

//    public int addNewOrSelectExistingLocationQQ(PLLocationType locationType, AddressInfo insAddress, int numAcres, int numResidence) throws Exception {
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//        sideMenu.clickSideMenuLocations();
//        clickWhenClickable(button_GenericWorkorderSquirePropertyLocationAddExistingLocation);
//        List<WebElement> allbuttons = buttonAddressExisting(insAddress.getLine1());
//        boolean locationFound = false;
//        WebElement oneButton = null;
//        if (allbuttons.size() > 0) {
//            locationFound = true;
//            oneButton = allbuttons.get(0);
//        }
//
//        if (!locationFound) {
//            clickNewLocation();
//            //            selectAddressType(locationType.getName());
//            addLocationInfoQQ(insAddress, numAcres, numResidence);
//        } else {
//            clickWhenClickable(oneButton);
//            setAcres(numAcres);
//            setNumberOfResidence(numResidence);
//            clickStandardizeAddress();
//        }
//
//        clickStandardizeAddress();
//        new GuidewireHelpers(driver).verifyAddress(insAddress);
//
//        super.clickOK();
//
//        clickGenericWorkorderSaveDraft();
//
//        return getLastRowLocationNumber();
//    }


    public int addNewOrSelectExistingLocationQQ(PolicyLocation location) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();
        clickWhenClickable(button_GenericWorkorderSquirePropertyLocationAddExistingLocation);
        List<WebElement> allbuttons = buttonAddressExisting(location.getAddress().getLine1());
        boolean locationFound = false;
        WebElement oneButton = null;
        if (allbuttons.size() > 0) {
            locationFound = true;
            oneButton = allbuttons.get(0);
        }

        if (!locationFound) {
            clickNewLocation();
            selectAddressType(location.getPlLocationType().getName());
            selectLocationAddress("New...");
            addLocationInfoQQ(location);
        } else {
            clickWhenClickable(oneButton);
        }

        setAcres(location.getPlNumAcres());
        setNumberOfResidence(location.getPlNumResidence());
        clickStandardizeAddress();
        new GuidewireHelpers(driver).verifyAddress(location.getAddress());
		/* GenericWorkorderLocations genericLocations = new GenericWorkorderLocations(driver);
        genericLocations.locationStandardizeAddress(location);*/
        //clickStandardizeAddress();

        clickOK();

        clickGenericWorkorderSaveDraft();

        return getLastRowLocationNumber();
    }


//	"No. of Residence (Location) <
//	no. of units on (
//			Residence Premises,
//			Dwelling Premises,
//			Vacation Home,
//			Condominium Dwelling Premises,
//			Dwelling Under Construction,
//			Condominium Residences Premises, and
//			Condominium Vacation Home) and
//	no. of properties (
//			Contents,
//			Dwelling Premises Cov E,
//			Residence Premises Cov E,
//			Vacation Home Cov E,
//			Dwelling Under Construction Cov E,
//			Condo Vacation Home Cov E, and
//			Bunk House) created on a location"


	public void updateLocationToCorrectNumberOFResidents(GeneratePolicy policy) throws GuidewireNavigationException {
		for(PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
			new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuPropertyLocations();
			clickEditLocation(location.getNumber());

			int numberOfResidents = 0;
			for(PLPolicyLocationProperty property : location.getPropertyList()) {
				switch(property.getpropertyType()) {
				case ResidencePremises:
				case DwellingPremises:
				case VacationHome:
				case CondominiumDwellingPremises:
				case DwellingUnderConstruction:
				case CondominiumResidencePremise:
				case CondominiumVacationHome:
					numberOfResidents = Integer.valueOf(property.getNumberOfUnits().getValue().replace("Units", "").replace("Unit", "").trim());
					break;
				case Contents:
				case DwellingPremisesCovE:
				case ResidencePremisesCovE:
				case VacationHomeCovE:
				case DwellingUnderConstructionCovE:
				case CondoVacationHomeCovE:
				case BunkHouse:
					numberOfResidents++;
					break;
				default:
					break;
				}
			}
			setNumberOfResidence(numberOfResidents);
			clickOK();
		}
	}

	public int addNewOrSelectExistingLocationFA(PLLocationType locationType, AddressInfo insAddress, int numAcres, int numResidence) {
		clickWhenClickable(button_GenericWorkorderSquirePropertyLocationAddExistingLocation);
		List<WebElement> allbuttons = buttonAddressExisting(insAddress.getLine1());
		boolean locationFound = false;
		WebElement oneButton = null;
		if (allbuttons.size() > 0) {
			locationFound = true;
			oneButton = allbuttons.get(0);
		}

        if (!locationFound) {
            clickNewLocation();
            //            selectAddressType(locationType.getName());
            addLocationInfoFA(insAddress, numAcres, numResidence);
            clickStandardizeAddress();
            new GuidewireHelpers(driver).verifyAddress(insAddress);
            clickOK();
        } else {
            clickWhenClickable(oneButton);
            setAcres(numAcres);
            setNumberOfResidence(numResidence);
            
            clickStandardizeAddress();
            new GuidewireHelpers(driver).verifyAddress(insAddress);
            clickOK();
        }

        clickGenericWorkorderSaveDraft();

        return getLastRowLocationNumber();
    }

    private int getLastRowLocationNumber() {
        List<WebElement> allrows = tableUtils.getAllTableRows(table_Locations);
        return Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_Locations, allrows.size(), "Loc. #"));
    }


    public void addSection(ArrayList<String> sections) {
        int sectionSize = sections.size();
        int rowCount = (sectionSize / 6) + 1;
        int toClickAdd = 0;
        int currentValue = 0;
        
        if (!(sections == null || sections.isEmpty())) {
            for (int currentRow = 0; currentRow < rowCount; currentRow++) {

                if (toClickAdd != currentRow) {
                    clickWhenClickable(link_AddRow);
                    toClickAdd = currentRow;
                    
                }
                for (int x = 0; x < 6; x++) {
                    currentValue++;
                    if (currentValue <= sectionSize) {
                        clickWhenClickable(editbox_GenericWorkorderSquirePropertyLocationSection(Integer.toString(currentRow), Integer.toString(x)));
                        
                        editbox_GenericWorkorderSquirePropertyLocationSection(Integer.toString(currentRow), Integer.toString(x)).clear();
                        
                        editbox_GenericWorkorderSquirePropertyLocationSection(Integer.toString(currentRow), Integer.toString(x)).sendKeys(sections.get(currentValue - 1));
                        
                        editbox_GenericWorkorderSquirePropertyLocationSection(Integer.toString(currentRow), Integer.toString(x)).sendKeys(Keys.TAB);
                    } else
                        break;
                }
            }
        } else {
            clickWhenClickable(editbox_GenericWorkorderSquirePropertyLocationSection("0", "0"));
            
            editbox_GenericWorkorderSquirePropertyLocationSection("0", "0").sendKeys("5");
            
            editbox_GenericWorkorderSquirePropertyLocationSection("0", "0").sendKeys(Keys.TAB);
        }
    }


//    private void addLocationInfoQQ(AddressInfo address, int numAcres, int numResidence) {
//        setAddressLine1(address.getLine1());
//        
//        setAddressLine2(address.getLine2());
//        
//        selectCounty(address.getCounty());
//        
//        setCity(address.getCity());
//        
//        setZipCode(address.getZip());
//        
//        setAcres(numAcres);
//        
//        setNumberOfResidence(numResidence);
//    }


    private void addLocationInfoQQ(PolicyLocation location) {
        if (location.getLocationType() == LocationType.Address) {
            setAddressLine1(location.getAddress().getLine1());
            if (location.getAddress().getLine2() != null) {
                setAddressLine2(location.getAddress().getLine2());
            }
            setCity(location.getAddress().getCity());
            setZipCode(location.getAddress().getZip());
            selectCounty(location.getAddress().getCounty());
        } else if (location.getLocationType() == LocationType.SectionTownshipRange) {
            selectCounty(location.getTownshipRange().getCounty());
            selectTownshipNumber(location.getTownshipRange().getTownship());
            selectTownshipDirection(location.getTownshipRange().getTownshipDirection());
            selectRangeNumber(location.getTownshipRange().getRange());
            selectRangeDirection(location.getTownshipRange().getRangeDirection());
            addSection(location.getTownshipRange().getSections());
        } else {
            System.out.println("What is a legal address?");
        }

    }


    public void addLocationInfoFA(AddressInfo address, int numAcres, int numResidence) {
        
        setAddressLine1(address.getLine1());
        
        setAddressLine2(address.getLine2());
        
        selectCounty(address.getCounty());
        
        setCity(address.getCity());
        
        setZipCode(address.getZip());
        
        setAcres(numAcres);
        
        setNumberOfResidence(numResidence);
    }


    private void selectState(State state) {
        Guidewire8Select mySelect = select_GenericWorkorderSquirePropertyLocationInformationState();
        mySelect.selectByVisibleTextPartial(state.toString());
    }


    private void setAddressLine1(String line1) {
    	setText(editbox_GenericWorkorderSquirePropertyLocationInformationAddressLine1, line1);        
    }


    private void setAddressLine2(String line2) {
        if (line2 != null) {
        	setText(editbox_GenericWorkorderSquirePropertyLocationInformationAddressLine2, line2);
        }
    }
    
    public String getAddressLine1() {
    	//if its an input
		if(finds(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")).isEmpty()) {
			return editBox_ContactAddress.getAttribute("value");
		} else {
			return editBox_ContactAddress.getText();
		}
    }

    public String getCity() {
		//if its an input
		if(!finds(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")).isEmpty()) {
			return editBox_ContactCity.getAttribute("value");
		} else {
			return editBox_ContactCity.getText();
		}
	}
    
    private void setCity(String cityToFill) {
		clickWhenClickable(editBox_ContactCity);
		editBox_ContactCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), cityToFill);
		clickProductLogo();
	}


    private void setCounty(String countyText) {
    	setText(editbox_GenericWorkorderSquirePropertyLocationInformationCounty, countyText);
    }


    private void setZipCode(String zipCode) {
    	setText(editbox_GenericWorkorderSquirePropertyLocationInformationZipCode, zipCode);
    }


    private void selectCounty(String county) {
        if (county.contains("Owyhee")) county = "Owyhee";//there is no longer such thing as East and West Owyhee.
        select_GenericWorkorderSquirePropertyLocationInformationCounty().selectByVisibleTextPartial(county);
    }


    private void selectTownshipNumber(String number) {
        if (number == null || number == "") {
            select_GenericWorkorderSquirePropertyLocationInformationTownshipNumber().selectByVisibleTextRandom();
        } else {
            select_GenericWorkorderSquirePropertyLocationInformationTownshipNumber().selectByVisibleText(number);
        }
    }


    private void selectTownshipDirection(String direction) {
        if (direction == null || direction == "") {
            select_GenericWorkorderSquirePropertyLocationInformationTownshipDirection().selectByVisibleTextRandom();
        } else {
            select_GenericWorkorderSquirePropertyLocationInformationTownshipDirection().selectByVisibleText(direction);
        }
    }


    private void selectRangeNumber(String number) {
        if (number == null || number == "") {
            select_GenericWorkorderSquirePropertyLocationInformationRangeNumber().selectByVisibleTextRandom();
        } else {
            select_GenericWorkorderSquirePropertyLocationInformationRangeNumber().selectByVisibleText(number);
        }
    }


    private void selectRangeDirection(String direction) {
        if (direction == null || direction == "") {
            select_GenericWorkorderSquirePropertyLocationInformationRangeDirection().selectByVisibleTextRandom();
        } else {
            select_GenericWorkorderSquirePropertyLocationInformationRangeDirection().selectByVisibleText(direction);
        }
    }


    private void selectAddressType(String dropdownValue) {
        Guidewire8Select addressType = select_GenericWorkorderSquirePropertyLocationInformationAddressType();
        addressType.selectByVisibleTextPartial(dropdownValue);
        clickProductLogo();
    }


    public ArrayList<String> getAddresses() {
        ArrayList<String> addresses = new ArrayList<String>();
        for (WebElement local : menuLink_GenericWorkorderSquirePropertyLocationAddExistingOptions) {
            addresses.add(local.getText());
        }
        return addresses;
    }

    @FindBy(xpath = "//input[contains(@id,'HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:acres')]")
    private WebElement editbox_Acres;


    private void setAcres(int acres) {
    	setText(editbox_Acres, String.valueOf(acres));
    }


    public int getAcres() {
    	waitUntilElementIsVisible(editbox_Acres);
        return Integer.parseInt(editbox_Acres.getAttribute("value"));
    }

    @FindBy(xpath = "//input[contains(@id,'HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:NumberOfResidence-inputEl')]")
    private WebElement editbox_NumberOfResidence;


    private void setNumberOfResidence(int residences) {
    	setText(editbox_NumberOfResidence, String.valueOf(residences));
    }
    
    public void setAcresAndResidents(int acres, int residents) {
    	setNumberOfResidence(residents);
    	setAcres(acres);
    }

    public int getNumberOfResidence(){
		waitUntilElementIsVisible(editbox_NumberOfResidence);
		return Integer.parseInt(editbox_NumberOfResidence.getAttribute("value"));
	}


    public String getSpecificLocationsColumnByRowNumber(int rowNumber, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Locations, rowNumber, columnName);
    }


    public boolean isPrimaryLocationSet() {
        return tableUtils.verifyRowExistsInTableByColumnsAndValues(table_Locations, "Primary", "X");
    }


    public void setLocationRowToPrimary(int rowNumber) {
        tableUtils.setCheckboxInTable(table_Locations, rowNumber, true);
        clickWhenClickable(button_SetAsPrimary);
    }


//    public String getValidationMessage() {
//        try {
//            return page_ValidationMessages.getText();
//        } catch (Exception e) {
//            return null;
//        }
//    }


    private void setAddressLegal(String legalValue) {
    	setText(text_Legal, String.valueOf(legalValue));
    }


//    public String getLocationsValidations() {
//    	return getTextOrValueFromElement(locations_Validations);
//    }


    public void SelectLocationsCheckboxByRowNumber(int cRow) {
        tableUtils.setCheckboxInTable(table_Locations, cRow, true);

    }


    public void clickRemoveButton() {
        super.clickRemove();
    }


    public void clickStandardizeAddress() {
        GenericWorkorderLocations genericLocations = new GenericWorkorderLocations(driver);
        genericLocations.clickLocationsStandardizeAddress();
        //check for override
        if (!finds(By.xpath("//span[contains(text(), 'Address not found')]")).isEmpty()) {
            super.clickOverride();
        }
    }


    public void selectLocationAddress(String address) {
        if (address.contains("New...")) {
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':SelectedAddress:newAddressBuddy')]")));
                return;
            }
        }
    }


    public void clickLocationInformationOverride() {
        clickWhenClickable(locationsOverrideButton);
    }


//    public boolean isAddExistingLocationEnabled() {
//    	waitUntilElementIsVisible(button_GenericWorkorderSquirePropertyLocationAddExistingLocation);
//        boolean isEnabled = button_GenericWorkorderSquirePropertyLocationAddExistingLocation.isSelected();
//        return isEnabled;
//    }


    public int getTownshipNo() {
        return Integer.parseInt(find(By.xpath("//div[contains(@id, 'HOLocationInputSet:Township')]")).getText());
    }


    public int getRangeNo() {
        return Integer.parseInt(find(By.xpath("//div[contains(@id, 'HOLocationInputSet:Range')]")).getText());
    }


    public String getSection() {
        return find(By.xpath("//div[contains(@id, ':HOLocationInputSet:SectionTownshipRangeInputSet:uneditedSection')]")).getText();
    }


    public void editPLLocationFA(PolicyLocation location, WebElement editToClick) {
        clickEditLocation(editToClick);
        setAddressLine1(location.getAddress().getLine1());
        setAddressLine2(location.getAddress().getLine2());
        selectCounty(location.getAddress().getCounty());
        setCity(location.getAddress().getCity());
        setZipCode(location.getAddress().getZip());
        clickStandardizeAddress();
        clickOK();
    }//END editPLLocationFA

	public void updateExistingLocation(PolicyLocation policyLocation) throws GuidewireNavigationException {
		new SideMenuPC(getDriver()).clickSideMenuPropertyLocations();
		clickEditLocation(policyLocation.getNumber());
		setAcres(policyLocation.getPlNumAcres());
		setNumberOfResidence(policyLocation.getPlNumResidence());
		clickOK();
	}


}
