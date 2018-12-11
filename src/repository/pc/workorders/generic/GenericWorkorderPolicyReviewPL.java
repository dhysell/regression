package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.OkCancel;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderPolicyReviewPL extends GenericWorkorder {

	private TableUtils tableUtils;
	
    public GenericWorkorderPolicyReviewPL(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_ReviewSummaryDV:Product-inputEl')]")
    private WebElement input_PolicyReviewProduct;

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:1')]")
    private WebElement table_StandardFireDeductible;

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:0:HOSectionILineReviewLV')]")
    private WebElement table_StandardFirePropertyDetails;

    @FindBy(xpath = "//div[contains(@id, ':ReviewSummaryCV:0:PolicyLineSummaryPanelSet:HOSectionIILineReviewLV')]")
    private WebElement table_StandardFireSectionIICoverages;

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:3')]")
    private WebElement table_StandardFireLocations;


    @FindBy(xpath = "//span[contains(@id, ':OOSConflictsTab-btnEl')]")
    private WebElement link_ChangeConflicts;

    @FindBy(xpath = "//div[contains(@id, ':OOSConflictPanelSet:ConflictTableLV')]")
    private WebElement table_changeconflicts;

    @FindBy(xpath = "//span[contains(@id, ':OverrideAll-btnEl')]")
    private WebElement button_OverrideAll;


    @FindBy(xpath = "//a[contains(@id, ':OOSConflictPanelSet:Done')]")
    private WebElement button_ChangeConflictsSubmit;

    @FindBy(xpath = "//a[contains(@id, ':ApplyChanges')]")
    private WebElement button_ApplyAllChanges;

    @FindBy(xpath = "//span[contains(., 'Additional Insureds')]/parent::span[contains(@id, 'AdditionalInsuredsTab-btnEl')]")
    private WebElement link_PolicyReivewAdditionalInsureds;

    @FindBy(xpath = "//label[contains(., 'Additional Insureds')]/ancestor::tr[2]/following-sibling::tr/td/div")
    private WebElement divContainer_PolicyReviewAdditionalInsureds;


    public String getProductName() {
        return input_PolicyReviewProduct.getText();
    }


    public Double getDeductibleAmount() {
        List<WebElement> section1Coverages = tableUtils.getAllTableRows(table_StandardFireDeductible);
        String amount = "";
        for (WebElement element : section1Coverages) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains("Deductible")) {
                amount = tempArray[1].trim();
                break;
            }
        }
        return Double.parseDouble(amount);
    }


    public String getStandardLiabilityMedicalbyType(String type) {
        List<WebElement> section1Coverages = tableUtils.getAllTableRows(table_StandardFireDeductible);
        String amount = "";
        for (WebElement element : section1Coverages) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains(type)) {
                amount = tempArray[1].trim();
                break;
            }
        }
        return amount;
    }


    public String getPropertyAddressByLocationNumber(int locNumber) {
        WebElement element = find(By.xpath("//div[contains(@id, 'SubmissionWizard:SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:LocationsIterator:" + (locNumber - 1) + ":0')]"));
        return element.getText();
    }


    public double getStandardFirePolicyReviewProductDetailsLimit(int locNumber, String coverageType) {
        String xpath = "SubmissionWizard:SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:LocationsIterator:" + (locNumber - 1) + ":HOSectionILineReviewLV";
        WebElement element = find(By.xpath("//div[contains(@id,'" + xpath + "')]"));
        int coverageRow = getStandardFireCoverageRowNumber(element, coverageType);
        
        return Double.parseDouble(tableUtils.getCellTextInTableByRowAndColumnName(element, coverageRow, "Limit").replace(",", ""));
    }


    public String getStandardFirePolicyReviewProductDetailsType(int locNumber, String coverageType) {
        String xpath = "SubmissionWizard:SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:LocationsIterator:" + (locNumber - 1) + ":HOSectionILineReviewLV";
        WebElement element = find(By.xpath("//div[contains(@id,'" + xpath + "')]"));
        int coverageRow = getStandardFireCoverageRowNumber(element, coverageType);
        
        return tableUtils.getCellTextInTableByRowAndColumnName(element, coverageRow, "Type");
    }

    private int getStandardFireCoverageRowNumber(WebElement tableElement, String coverageType) {
        int returnRow = 0;
        int rowCount = tableUtils.getRowCount(tableElement);
        for (int cRow = 1; cRow <= rowCount; cRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(tableElement, cRow, "Property Type");
            if (desc.contains(coverageType)) {
                returnRow = cRow;
                break;
            }
        }
        return returnRow;
    }


    public String getStandardLiabilityQuantityAmountByDescription(String description) {
        String quantity = "";
        int rowCount = tableUtils.getRowCount(table_StandardFireSectionIICoverages);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_StandardFireSectionIICoverages,
                    currentRow, "Description");
            if (desc.trim().contains(description)) {
                quantity = tableUtils.getCellTextInTableByRowAndColumnName(table_StandardFireSectionIICoverages,
                        currentRow, "Quantity/Amount");
                break;
            }
        }
        return quantity;
    }


    public String getStandardFireLocationsByRowColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_StandardFireLocations, row, columnName);
    }


    //Farm Equipment
    //@FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:FarmEquipmentCVTab-btnInnerEl']")
    @FindBy(xpath = "//*[@id='SubmissionWizard:SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:PIMLineReviewCV:PIMLineReviewCV:FarmEquipmentCVTab-btnInnerEl']")
    private WebElement tab_FarmEquipment;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:farmEquipmentPanelIterator:0:PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:1')]")
    private WebElement farmEquipmentTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:farmEquipmentPanelIterator:0:PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:2')]")
    private WebElement farmEquipmentVehicleTable;

    //Personal Property
    //@FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:PersonalEquipmentCVTab-btnInnerEl']")
    @FindBy(xpath = "//*[@id='SubmissionWizard:SubmissionWizard_PolicyReviewScreen:ReviewSummaryCV:0:PolicyLineSummaryPanelSet:PIMLineReviewCV:PIMLineReviewCV:PersonalEquipmentCVTab-btnInnerEl']")
    private WebElement tab_PersonalProperty;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:personalEquipmentPanelIterator:0:PIMCoverageViewPanelSetPersonalProperty:PIMCoverageViewPanelSet:1')]")
    private WebElement personalPropertyTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:personalEquipmentPanelIterator:0:PIMCoverageViewPanelSetPersonalProperty:PIMCoverageViewPanelSet:2')]")
    private WebElement personalPropertyScheduleTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:1')]")
    private List<WebElement> farmEquipmentTables;


    public void clickFarmEquipmentTab() {
        clickWhenClickable(tab_FarmEquipment);
    }

    public void clickPersonalPropertyTab() {
        clickWhenClickable(tab_PersonalProperty);
    }

    public int getFarmEquipmentTableRowCount() {
        return tableUtils.getRowCount(farmEquipmentTable);
    }

    public String getFarmEquipmentTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(farmEquipmentTable, row, columnName);
    }

    public String getFarmEquipmentVehicleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(farmEquipmentVehicleTable, row, columnName);
    }

    public String getPersonalPropertyTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(personalPropertyTable, row, columnName);
    }

    public String getPersonalPropertyScheduleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(personalPropertyScheduleTable, row, columnName);
    }

    public int getFarmEquipmentsCount() {
        return farmEquipmentTables.size();
    }


    public void clickChangeConflictsTab() {
        clickWhenClickable(link_ChangeConflicts);
        
    }


    public int getChangeConflictTableRowCount() {
        return tableUtils.getRowCount(table_changeconflicts);
    }


    public String getChangeConflictTableByRowNumberColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_changeconflicts, row, columnName);
    }


    public String getChangeConflictByItemAndColumnName(String item, String columnName) {
        int row = tableUtils.getRowNumberInTableByText(table_changeconflicts, item);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_changeconflicts, row, columnName);
    }


    public boolean checkChangeConflictsByValues(String item, String latestChangeColumnName, String latestChange, String previousChange) {
        int row = tableUtils.getRowNumberInTableByText(table_changeconflicts, item);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_changeconflicts, row, latestChangeColumnName).replace(",", "").contains(latestChange) && tableUtils.getCellTextInTableByRowAndColumnName(table_changeconflicts, row, "Conflict").replace(",", "").contains(previousChange);
    }


    public void clickOverrideAll() {
        clickWhenClickable(button_OverrideAll);
        
    }


    public void clickChangeConflictsSubmitButton() {
        clickWhenClickable(button_ChangeConflictsSubmit, 1000);
        
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }


    public void clickApplyAllChanges() {
        clickWhenClickable(button_ApplyAllChanges);
        
    }


    public void clickAdditionalInsureds() {
        clickWhenClickable(link_PolicyReivewAdditionalInsureds);
        
    }


    public ArrayList<String> getAdditionalInsureds() {
        clickAdditionalInsureds();
        
        return tableUtils.getAllCellTextFromSpecificColumn(divContainer_PolicyReviewAdditionalInsureds, "Name");
    }

}
