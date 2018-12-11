package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderStandardIMLineReview extends GenericWorkorder {

	private TableUtils tableUtils;
	
    public GenericWorkorderStandardIMLineReview(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //Farm Equipment
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:FarmEquipmentCVTab-btnInnerEl']")
    private WebElement tab_FarmEquipment;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:farmEquipmentPanelIterator:0:PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:1')]")
    private WebElement farmEquipmentTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:farmEquipmentPanelIterator:0:PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:2')]")
    private WebElement farmEquipmentVehicleTable;

    //Personal Property
    @FindBy(xpath = "//a[contains(@id,':PersonalEquipmentCVTab')]")
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
}
