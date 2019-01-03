package repository.pc.workorders.generic;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireInlandMarine_LineReview extends GenericWorkorderBuildings {

	private TableUtils tableUtils;
	
    public GenericWorkorderSquireInlandMarine_LineReview(WebDriver driver) {
        super(driver);
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    //Recreational Equipment
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:RecreationalEquipmentCVTab-btnInnerEl']")
    public WebElement tab_RecreationalEquipment;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetRecreationVehicle:PIMCoverageViewPanelSet:1')]")
    private WebElement recreationalEquipmentVehicleTable;

    //Watercraft
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:WatercraftCVTab-btnInnerEl']")
    public WebElement tab_Watercraft;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetWatercraft:PIMCoverageViewPanelSet:1')]")
    public WebElement watercraftTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSet:2')]")
    public WebElement watercraftVehicleTable;

    //Farm Equipment
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:FarmEquipmentCVTab-btnInnerEl']")
    public WebElement tab_FarmEquipment;

    @FindBy(xpath = "//div[contains(@id, 'farmEquipmentPanelIterator:0:PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:1')]")
    public WebElement farmEquipmentTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetFarmEquipment:PIMCoverageViewPanelSet:2')]")
    public WebElement farmEquipmentVehicleTable;

    //Personal Property
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:PersonalEquipmentCVTab-btnInnerEl']")
    public WebElement tab_PersonalProperty;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetPersonalProperty:PIMCoverageViewPanelSet')]")
    public WebElement personalPropertyTable;

    //Live Stock
    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:PIMLineReviewScreen:PIMLineReviewCV:PIMLineReviewCV:LivestockCVTab-btnIconEl']")
    public WebElement tab_Livestock;

    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetWatercraft:PIMCoverageViewPanelSet:1')]")
    public WebElement livestockTable;

    @FindBy(xpath = "//div[contains(@id, ':PIMLineReviewCV:PIMLineReviewCV:livestockPanelIterator:1:PIMCoverageViewPanelSetWatercraft:PIMCoverageViewPanelSet:1')]")
    public WebElement deathOfLivestockTable;


    @FindBy(xpath = "//div[contains(@id, ':PIMCoverageViewPanelSetWatercraft:PIMCoverageViewPanelSet:2')]")
    public WebElement livestockScheduleTable;

    @FindBy(xpath = "//span[contains(., 'Additional Insured')]/parent::span[contains(@id, 'AdditionalInsuredsTab-btnEl')]")
    public WebElement link_IMLineReviewAdditionalInsureds;

    @FindBy(xpath = "//label[contains(., 'Inland Marine Additional Insureds')]/ancestor::tr[2]/following-sibling::tr/td/div[contains(@id, 'LineReview')]")
    public WebElement divContainer_IMLineReviewAdditionalInsureds;


    public void clickRecreationalEquipmentTab() {
        clickWhenClickable(tab_RecreationalEquipment);
    }

    public void clickWatercraftTab() {
        clickWhenClickable(tab_Watercraft);
    }

    public void clickFarmEquipmentTab() {
        clickWhenClickable(tab_FarmEquipment);
    }


    public void clickPersonalPropertyTab() {
        clickWhenClickable(tab_PersonalProperty);
    }


    public void clickLivestockTab() {
        clickWhenClickable(tab_Livestock);
    }

    public int getRecreationalEquipmentTableRowCount() {
        return tableUtils.getRowCount(recreationalEquipmentVehicleTable);
    }

    public String getRecreationalEquimentVehicleCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(recreationalEquipmentVehicleTable, row, columnName);
    }

    public String getFarmEquipmentTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(farmEquipmentTable, row, columnName);
    }


    public String getFarmEquipmentVehicleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(farmEquipmentVehicleTable, row, columnName);
    }

    public String getWatercraftTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(watercraftTable, row, columnName);
    }


    public String getWatercraftVehicleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(watercraftVehicleTable, row, columnName);
    }

    public String getPersonalPropertyTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(personalPropertyTable, row, columnName);
    }


    public String getLiveStockTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(livestockTable, row, columnName);
    }


    public String getLiveStockScheduleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(livestockScheduleTable, row, columnName);
    }


    public String getDeathLiveStockTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(deathOfLivestockTable, row, columnName);
    }


    public void clickAdditionalInsureds() {
        clickWhenClickable(link_IMLineReviewAdditionalInsureds);
    }


    public ArrayList<String> getAdditionalInsureds() {
        clickAdditionalInsureds();
        return tableUtils.getAllCellTextFromSpecificColumn(divContainer_IMLineReviewAdditionalInsureds, "Name");
    }

}
