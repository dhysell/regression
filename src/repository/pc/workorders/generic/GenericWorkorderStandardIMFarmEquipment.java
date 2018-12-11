package repository.pc.workorders.generic;


import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardIMFarmEquipment extends GenericWorkorder {
	
    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderStandardIMFarmEquipment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutFarmEquipment(GeneratePolicy policy) throws Exception {
        if (policy.standardInlandMarine.inlandMarineCoverageSelection_Standard_IM.contains(InlandMarine.FarmEquipment)) {
            new SideMenuPC(driver).clickSideMenuStandardIMFarmEquipment();
            for (FarmEquipment farmThing : policy.standardInlandMarine.farmEquipment) {
                addFarmEquip(policy.basicSearch, farmThing);
            }//END FOR
        }//END IF
        clickGenericWorkorderSaveDraft();
    }


    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:FarmEquipmentScreen:FarmEquipmentPanelSet:CoverableListDetailPanel_tb:ToolbarButton-btnEl' or contains(@id, ':FarmEquipmentPanelSet:CoverableListDetailPanel_tb:ToolbarButton-btnInnerEl')]")
    public WebElement button_FarmEquipmentAdd;


    public void clickAdd() {
        clickWhenClickable(button_FarmEquipmentAdd);
    }


    public void clickOk() {
        super.clickOK();
    }

    public Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:type-triggerWrap']");
    }


    public void setType(IMFarmEquipmentType type) {
        Guidewire8Select mySelect = select_Type();
        mySelect.selectByVisibleText(type.getValue());
    }

    public Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:2:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap']");
    }


    public void setDeductible(IMFarmEquipmentDeductible _deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(_deductible.getValue());
    }

    public String getDeductible(){
    	return select_Deductible().getText();
    }

    public Guidewire8RadioButton radio_Inspected() {
        return new Guidewire8RadioButton(driver, "//div[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:inspected-containerEl']/table");
    }


    public void setInspected(Boolean trueFalse) {
        
        radio_Inspected().select(trueFalse);
    }

    public Guidewire8RadioButton radio_ExistingDamage() {
        return new Guidewire8RadioButton(driver, "//div[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:exisitingDamage-containerEl']/table");
    }


    public void setExistingDamage(Boolean _damage) {
        
        radio_ExistingDamage().select(_damage);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:titleOwner-inputEl']")
    public WebElement editbox_owner;


    public void setOwner(String _owner) {
        clickWhenClickable(editbox_owner);
        editbox_owner.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_owner.sendKeys(_owner);
        editbox_owner.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipAddInsuredDV:AdditionalInsuredLV_tb:Add-btnEl']")
    public WebElement button_FarmEquipmentAddtionalInsuredAdd;

    //nonscheduled

    public void clickAdditionalInsuredsAdd() {
        clickWhenClickable(button_FarmEquipmentAddtionalInsuredAdd);
    }


    public void setlistAdditionalInsured(ArrayList<String> insureds) {
        for (String insured : insureds) {
            setAdditionalInsureds(insured);
        }
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipAddInsuredDV:AdditionalInsuredLV']")
    public WebElement table_FarmEquipmentAddtionalInsured;


    public void setAdditionalInsureds(String addInsured) {
        clickAdditionalInsuredsAdd();
        tableUtils.setValueForCellInsideTable(table_FarmEquipmentAddtionalInsured, tableUtils.getNextAvailableLineInTable(table_FarmEquipmentAddtionalInsured), "Name", "Name", addInsured);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel_tb:Add-btnEl']")
    public WebElement button_FarmEquipmentScheduledEquipmentAdd;


    public void clickAddScheduledEquipment() {
        clickWhenClickable(button_FarmEquipmentScheduledEquipmentAdd);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:year-inputEl']")
    public WebElement editbox_Year;


    public void setYear(String _year) {
        clickWhenClickable(editbox_Year);
        editbox_Year.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_Year.sendKeys(_year);
        editbox_Year.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:make-inputEl']")
    public WebElement editbox_Make;


    public void setMake(String _make) {
        clickWhenClickable(editbox_Make);
        editbox_Make.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_Make.sendKeys(_make);
        editbox_Make.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:typeOfEquipment-inputEl']")
    public WebElement editbox_TypeOfEquipment;


    public void setTypeOfEquipment(String equip) {
        clickWhenClickable(editbox_TypeOfEquipment);
        editbox_TypeOfEquipment.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_TypeOfEquipment.sendKeys(equip);
        editbox_TypeOfEquipment.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:vinSerial-inputEl']")
    public WebElement editbox_VIN;


    public void setVin(String _vin) {
        clickWhenClickable(editbox_VIN);
        editbox_VIN.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_VIN.sendKeys(_vin);
        editbox_VIN.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:description-inputEl']")
    public WebElement editbox_Description;


    public void setDescription(String _description) {
        clickWhenClickable(editbox_Description);
        editbox_Description.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_Description.sendKeys(_description);
        editbox_Description.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:limit-inputEl']")
    public WebElement editbox_Limit;


    public void setLimit(String _limit) {
        clickWhenClickable(editbox_Limit);
        editbox_Limit.sendKeys(Keys.chord(Keys.CONTROL + "A"));
        editbox_Limit.sendKeys(_limit);
        editbox_Limit.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:FEScheduledEquipAddInsuredDV:AdditionalInsuredLV_tb:Add-btnEl']")
    public WebElement button_scheduledItemAdditionalInsuredAdd;

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:FEScheduledEquipAddInsuredDV:AdditionalInsuredLV']")
    public WebElement table_scheduledItemAdditionalInsured;


    public void addScheduledItemAdditionalInsured(String addInsured) {
        clickWhenClickable(button_scheduledItemAdditionalInsuredAdd);
        tableUtils.setValueForCellInsideTable(table_scheduledItemAdditionalInsured, tableUtils.getNextAvailableLineInTable(table_scheduledItemAdditionalInsured), "Name", "Name", addInsured);
    }


    public void setScheduledItem(IMFarmEquipmentScheduledItem item) {
        clickAddScheduledEquipment();
        setYear(item.getYear());
        setMake(item.getMake());
        setTypeOfEquipment(item.getTypeOfEquipment());
        setVin(item.getVin());
        setDescription(item.getItemDescription());
        setLimit(Integer.toString(item.getLimit()));
    }


    public void setListScheduledItem(ArrayList<IMFarmEquipmentScheduledItem> items) {
        for (IMFarmEquipmentScheduledItem item : items) {
            setScheduledItem(item);
        }
    }


    public void addFarmEquip(boolean basicSearch, FarmEquipment farmStuff) throws Exception {
        clickAdd();
        
        setType(farmStuff.getIMFarmEquipmentType());
        setDeductible(farmStuff.getDeductible());
        setInspected(farmStuff.getInspected());
        setExistingDamage(farmStuff.getExistingDamage());
        setOwner(farmStuff.getOwner());
        setlistAdditionalInsured(farmStuff.getAdditionalInsured());
        setAdditionalInterest(basicSearch, farmStuff.getAdditionalInterests());
        setListScheduledItem(farmStuff.getScheduledFarmEquipment());
        clickOK();
        
    }

    private void setAdditionalInterest(boolean basicSearch, ArrayList<AdditionalInterest> additionalInterests) throws Exception {
        for (AdditionalInterest additionalInterest : additionalInterests) {
            repository.pc.workorders.generic.GenericWorkorderAdditionalInterests aInterest = new GenericWorkorderAdditionalInterests(driver);
            aInterest.fillOutAdditionalInterest(basicSearch, additionalInterest);
        }
    }

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:FarmEquipmentScreen:FarmEquipmentPanelSet:CoverableListDetailPanel:CoverableLV')]")
    private WebElement farmEquipmentCoveragesTable;

    public void clickFarmEqipmentTableSpecificCheckbox(int rowNumber, boolean setCheckboxTrueFalse) {
        tableUtils.setCheckboxInTable(farmEquipmentCoveragesTable, rowNumber, setCheckboxTrueFalse);
    }

    public void ClickRemove() {
        
        clickRemove();
        
    }


    public int getFarmEquipmentCoverageTableRowCount() {
        return tableUtils.getRowCount(farmEquipmentCoveragesTable);
    }


    public String getFarmEquipmentTableByRowColumnName(int row, String column) {
        return tableUtils.getCellTextInTableByRowAndColumnName(farmEquipmentCoveragesTable, row, column);
    }

    @FindBy(xpath = "//div[contains(@id, ':CovTermPOCHOEInputSet:OptionTermInput-inputEl')]")
    private WebElement farmEquipmentCoverageType;


    public String getFarmEquipmentCoverageType() {
        return farmEquipmentCoverageType.getText();
    }
}
