package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.pc.sidemenu.SideMenuPC;

import java.util.List;

public class GenericWorkorderSquirePropertyAndLiabilityPropertyDetail extends GenericWorkorder {

	private WebDriver driver;
	private TableUtils tableUtils;

	public GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.tableUtils = new TableUtils(driver);
		PageFactory.initElements(driver, this);
	}

	public void fillOutPropertyDetail_QQ(GeneratePolicy policy) throws Exception {
		new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuSquirePropertyDetail();
		for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
			for (PLPolicyLocationProperty property : location.getPropertyList()) {
				clickLocation(location);
				clickAdd();
				new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver).fillOutPropertyDetails_QQ(policy.basicSearch, property);
				new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver).fillOutPropertyConstruction_QQ(property);
				//new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_QQ(property);
				clickOk();
				
				clickOkayIfMSPhotoYearValidationShows();
				
				property.setPropertyNumber(getSelectedBuildingNum());
			}//END FOR
		}
	}

	public void fillOutPropertyDetail_FA(GeneratePolicy policy) throws Exception {
		new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuSquirePropertyDetail();
		if(policy.squire.isFarmAndRanch()) { //farm and ranch is special and needs its own flow grrrrrr
			for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
				for (PLPolicyLocationProperty property : location.getPropertyList()) {
					if(property.getPropertyNumber() == -1) {
						clickLocation(location);
						clickAdd();
					} else {
						clickViewOrEditBuildingButton(property.getPropertyNumber());
					}
					new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver).fillOutPropertyDetails(property);
					new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver).fillOutPropertyConstruction(property);
					new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
					clickOk();
					
					clickOkayIfMSPhotoYearValidationShows();
					
					property.setPropertyNumber(getSelectedBuildingNum());
				}//END FOR
			}
		} else {
			for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
    			clickLocation(location);
				for (PLPolicyLocationProperty property : location.getPropertyList()) {
					clickViewOrEditBuildingButton(property.getPropertyNumber());
					new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver).fillOutPropertyDetails_FA(property);
					new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver).fillOutPropertyConstrustion_FA(property);
					new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_FA(property);
					clickOk();
					clickOkayIfMSPhotoYearValidationShows();
				}//END FOR
			}
		}
	}
	
	public void clickOkayIfMSPhotoYearValidationShows() {
		Boolean msPhotoYearFound = false;
		for (WebElement ele : getValidationResultsList()) { // This validation will always show now after hitting okay without them filled
			if (ele.getText().contains("upload the MS") || ele.getText().contains("upload the photos")) msPhotoYearFound = true;
		}
		if (msPhotoYearFound == true) clickOk();
	}


    public void fillOutPropertyDetail(GeneratePolicy policy) throws Exception {
		new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuSquirePropertyDetail();
		for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
			for (PLPolicyLocationProperty property : location.getPropertyList()) {
				clickViewOrEditBuildingButton(property.getPropertyNumber());
				new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver).fillOutPropertyDetails(property);
				new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver).fillOutPropertyConstruction(property);
				new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
				clickOk();
				clickOkayIfMSPhotoYearValidationShows();
			}//END FOR
		}
	}

	@FindBy(xpath = "//div[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:propertyType:SelectpropertyType')]")
	public WebElement button_SquirePropertyDetailPropertyType;

	public void clickPropertyInformationDetailsTab() {
		clickWhenClickable(link_SquirePropertyDetailDetails);
	}


	public void clickPropertyConstructionTab() throws GuidewireNavigationException {
		clickWhenClickable(link_SquirePropertyDetailPropertyConstruction);
        new GuidewireHelpers(driver).isOnPage("//label[contains(@id, ':ConstructionType-labelEl')]", 10000, "UNABLE TO GET TO PROPERTY CONSTRUCTION TAB");
	}

	public void clickProtectionDetailsTab() throws GuidewireNavigationException {
		clickWhenClickable(link_SquirePropertyDetailProtectionDetails);
		new GuidewireHelpers(driver).isOnPage("//label[contains(@id, ':SprinklerSystemType-labelEl')]", 10000, "UNABLE TO GET TO PROTECTION DETAILS AFTER CLICKING THE TAB");
	}

	@FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:1')]")
	private WebElement table_PropertyDetailLocations;

	@FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV')]")
	private WebElement table_PropertyDetailProperties;


	@FindBy(xpath = "//span[contains(@id, ':propertyConstructionTab-btnE')]")
	public WebElement link_SquirePropertyDetailPropertyConstruction;

	@FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:DwellingSingleProtectionIdTab-btnEl')] | //span[contains(@id, 'HOBuilding_FBMPopup:DwellingSingleProtectionIdHOBuilding_FBMPopupTab-btnEl')]")
	public WebElement link_SquirePropertyDetailProtectionDetails;

	@FindBy(xpath = "//span[contains(@id, 'HOBuilding_FBMPopup:DwellingDetailsSingleIDTab-btnEl')]")
	public WebElement link_SquirePropertyDetailDetails;




	//there are two Remove buttons on this page, can't use the method from Common.java


	@FindBy(xpath = "//span[contains(@id, ':HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV_tb:Remove-btnInnerEl') or contains(@id, ':HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV_tb:Remove-btnInnerEl')]")
	public WebElement button_SquirePropertyDetailRemove;



	@FindBy(xpath = "//label[contains(@id, 'HOBuilding_FBMPopup:AdditionalInterestDetailsDV:0') and contains(., 'Property')]")
	public WebElement label_SquirePropertyDetailPropertyAdditionalInterests;



	@FindBy(xpath = "//label[contains(@id, 'HOBuilding_FBMPopup:2')]")
	private WebElement label_propertyPageValidationMessage;


	@FindBy(xpath = "//a[contains(text(), 'Return to Property Detail')]")
	private WebElement link_AdditionalInterests_ReturnToPropertyDetails;

	public void clickViewOrEditBuildingButton(int bldgNumber) {
		int buildingRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Building Number", String.valueOf(bldgNumber)));
		tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, buildingRowNumber);
	}


	public void clickOk() {
		super.clickOK();
		new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and contains(text(), 'Property Detail')]");
	}


	public void clickReturnToPropertyDetail() {
		clickWhenClickable(link_AdditionalInterests_ReturnToPropertyDetails);
	}


	public void clickCancel() {
		super.clickCancel();
		selectOKOrCancelFromPopup(OkCancel.OK);
		new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and contains(text(), 'Property Detail')]");
	}

	public void selectBuilding(String buildingNum) {
		tableUtils.setCheckboxInTable(table_PropertyDetailProperties, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Building Number", buildingNum)), true);
	}

	public void removeBuilding(String buildingNum) {
		selectBuilding(buildingNum);
		clickWhenClickable(button_SquirePropertyDetailRemove);
	}

	public void setAddtionalInsuredName(String additionalInsured) {
		tableUtils.setValueForCellInsideTable(table_addtionalInsured, tableUtils.getNextAvailableLineInTable(table_addtionalInsured, "Name"), "Name", "FirstName", additionalInsured);
	}

	public String getAdditionalInsuredNameByRowNumber(int row) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_addtionalInsured, row, "Name");
	}


	@FindBy(xpath = "//input[contains(@id, 'HOBuilding_FBMPopup:HODwellingDetailsHOEDV:OccupantIndicator-inputEl')]")
	public WebElement editbox_HowOrByWhomIsThisOccupied;

	@FindBy(xpath = "//div[contains(@id, ':AdditionalInsuredLV')]")
	private WebElement table_addtionalInsured;


	public WebElement getPropertyDetailLocationsTable() {
		waitUntilElementIsVisible(table_PropertyDetailLocations);
		return table_PropertyDetailLocations;
	}


	public WebElement getPropertyDetailPropertiesTable() {
		waitUntilElementIsVisible(table_PropertyDetailProperties);
		return table_PropertyDetailProperties;
	}


	public void setHowOrByWhomIsThisOccupied(String howOrByWhom) {
		waitUntilElementIsClickable(editbox_HowOrByWhomIsThisOccupied);
		editbox_HowOrByWhomIsThisOccupied.sendKeys(howOrByWhom);
	}


	public boolean isHowOrByWhomIsThisOccupiedExist() {
		return checkIfElementExists(editbox_HowOrByWhomIsThisOccupied, 5000);
	}

	public void clickViewOrEditBuildingButtonByRowNumber(int rowNumber) {
		tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, rowNumber);
	}


	public void clickViewOrEditBuildingByPropertyType(PropertyTypePL propertyType) {
		int buildingRowNumber = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_PropertyDetailProperties, "Property Type", propertyType.getValue()));
		tableUtils.clickLinkInSpecficRowInTable(table_PropertyDetailProperties, buildingRowNumber);
	}


	//    public String getAdditionalInterestsLabel() {
	//        waitUntilElementIsVisible(label_SquirePropertyDetailPropertyAdditionalInterests);
	//        return label_SquirePropertyDetailPropertyAdditionalInterests.getText();
	//    }


	public void clickLocation(PolicyLocation location) {
		switch (location.getLocationType()) {
		case Address:
			tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, tableUtils.getRowNumberInTableByText(table_PropertyDetailLocations, location.getFullAddressString()));
			break;
		case Legal:
			//Please insert Code here.
			System.out.println("Who knows what to do here?");
			break;
		case SectionTownshipRange:
			String rowToClick = location.getTownshipRange().getTownship() + location.getTownshipRange().getTownshipDirection().toLowerCase() +
			", " + location.getTownshipRange().getRange() + location.getTownshipRange().getRangeDirection().toLowerCase() + ", " + location.getTownshipRange().getCounty();
			System.out.println("Row to click is " + rowToClick);

			int row = Integer.valueOf(table_PropertyDetailLocations.findElement(By.xpath("//a[contains(.,'" + rowToClick + "')]/ancestor::tr[1]")).getAttribute("data-recordindex")) + 1;
			tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, row);
			break;
		default:
			break;
		}
	}

	public void clickLocation(int rowNumber) {
        new TableUtils(getDriver()).clickRowInTableByRowNumber(table_PropertyDetailLocations, rowNumber);
	}

	public int getSelectedBuildingNum() {
		waitUntilElementIsVisible(table_PropertyDetailProperties);
		int row = tableUtils.getHighlightedRowNumber(table_PropertyDetailProperties);
		return Integer.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_PropertyDetailProperties, row, "Building Number"));
	}

	public int getPropertiesCount() {
		return tableUtils.getRowCount(table_PropertyDetailProperties);
	}


	public void setCheckBoxByRowInPropertiesTable(int row, boolean trueFalse) {
		tableUtils.setCheckboxInTable(table_PropertyDetailProperties, row, trueFalse);
	}


	public void clickRemoveProperty() {
		clickWhenClickable(button_SquirePropertyDetailRemove);
	}


	public void highLightPropertyLocationByNumber(int row) {
		tableUtils.clickRowInTableByRowNumber(table_PropertyDetailLocations, row);
	}


	public String getPropertyValidationMessage() {
		waitUntilElementIsVisible(label_propertyPageValidationMessage);
		return label_propertyPageValidationMessage.getText();
	}

	public List<String> getPropertiesList() {
		return tableUtils.getAllCellTextFromSpecificColumn(table_PropertyDetailProperties, "Property Type");
	}


	public void highLightPropertiesByNumber(int row) {
		tableUtils.clickRowInTableByRowNumber(table_PropertyDetailProperties, row);
	}


	public void fillOutPLPropertyQQ(boolean basicSearch, PLPolicyLocationProperty property, SectionIDeductible deductible, PolicyLocation location) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		clickLocation(location);
		clickAdd();
		new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver).fillOutPropertyDetails_QQ(basicSearch, property);
		new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver).fillOutPropertyConstruction_QQ(property);

		repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);

		if (constructionPage.isProtectionDetailsTabExists()) {
			constructionPage.clickProtectionDetails();
			new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails_QQ(property);
		}
		repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages2 = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages2.clickOk();
		clickOkayIfMSPhotoYearValidationShows();
		property.setPropertyNumber(getSelectedBuildingNum());
	}//END fillOutPLPropertyQQ


}














