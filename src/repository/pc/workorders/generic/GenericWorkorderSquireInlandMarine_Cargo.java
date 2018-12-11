package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_Cargo extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquireInlandMarine_Cargo(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void filloutCargo(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.Cargo)) {
            sideMenu.clickSideMenuIMCargo();
            for (SquireIMCargo goods : policy.squire.inlandMarine.cargo_PL_IM) {
                if (goods.getCargoClass() == CargoClass.ClassIII) {
                    policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddCargoClassIII);
                    inputCargoClassIII(goods);
                } else {
                    policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddCargoClassIorII);
                    inputCargo(goods);
                }
            }
        }
    }


    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:CargoScreen:CargoPanelSet:CoverableListDetailPanel_tb:ToolbarButton-btnEl']")
    public WebElement button_CargoAdd;


    public void clickCargoAdd() {
        clickWhenClickable(button_CargoAdd);
    }


    public Guidewire8Select select_Class() {
        return new Guidewire8Select(driver, "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:class-triggerWrap']");
    }


    public void setClass(CargoClass _cargoClass) {
        Guidewire8Select mySelect = select_Class();
        mySelect.selectByVisibleText(_cargoClass.getValue());
    }

    public Guidewire8RadioButton set_DeductiblePercentage() {
        return new Guidewire8RadioButton(driver, "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:coverageRequired-containerEl']/table");
    }


    public void setCoverageRequired(boolean coverageRequired) {
        
        set_DeductiblePercentage().select(coverageRequired);
        
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:haulPrincupal-inputEl']")
    public WebElement editbox_CargoHaulsPrincipally;


    public void setApplicantHaulPrincipally(String haulOwner) {
        clickWhenClickable(editbox_CargoHaulsPrincipally);
        editbox_CargoHaulsPrincipally.sendKeys(haulOwner);
        editbox_CargoHaulsPrincipally.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoAddInsuredDV:AdditionalInsuredLV_tb:Add-btnEl']")
    public WebElement button_CargoAdditionalInsuredsAdd;

    @FindBy(xpath = "//*[contains(@id,':CargoAddInsuredDV:AdditionalInsuredLV')]")
    public WebElement table_CargoAdditionalInsuredsTable;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;


    public void addAdditionalInsureds(String insured) {
        clickWhenClickable(button_CargoAdditionalInsuredsAdd);
        int row = tableUtils.getNextAvailableLineInTable(table_CargoAdditionalInsuredsTable, "Name");
        tableUtils.setValueForCellInsideTable(table_CargoAdditionalInsuredsTable, row, "Name", "Name", insured);
        
    }


    public void removeAdditionalInsuredByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_CargoAdditionalInsuredsTable, name);
        tableUtils.setCheckboxInTable(table_CargoAdditionalInsuredsTable, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CargoAdditionalInsuredsTable, row, "Name");
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel_tb:Add-btnEl']")
    public WebElement button_CargoScheduledItemsAdd;

    public Guidewire8Select select_VehicleType() {
        return new Guidewire8Select(driver, "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:vehicleType-triggerWrap']");
    }


    public void setVehicleType() {
        Guidewire8Select mySelect = select_VehicleType();
        mySelect.selectByVisibleTextRandom();
    }


    public void setVehicleType(String vehicleType) {
        Guidewire8Select mySelect = select_VehicleType();
        mySelect.selectByVisibleTextPartial(vehicleType);
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:year-inputEl']")
    public WebElement editbox_ScheduledItemYear;


    public void setScheduledItemsYear(String year) {
        clickWhenClickable(editbox_ScheduledItemYear);
        editbox_ScheduledItemYear.sendKeys(year);
        editbox_ScheduledItemYear.sendKeys(Keys.TAB);
    }


    public boolean isScheduleItemsYearEditable() {

        try {
            clickWhenClickable(editbox_ScheduledItemYear);
            return editbox_ScheduledItemYear.isEnabled();
        } catch (Exception e) {
            return false;
        }

    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:make-inputEl']")
    public WebElement editbox_ScheduledItemMake;


    public void setScheduledItemsMake(String make) {
        clickWhenClickable(editbox_ScheduledItemMake);
        editbox_ScheduledItemMake.sendKeys(make);
        editbox_ScheduledItemMake.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:model-inputEl']")
    public WebElement editbox_ScheduledItemModel;


    public void setScheduledItemsModel(String model) {
        clickWhenClickable(editbox_ScheduledItemModel);
        editbox_ScheduledItemModel.sendKeys(model);
        editbox_ScheduledItemModel.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:vinSerial-inputEl']")
    public WebElement editbox_ScheduledItemVin;


    public void setScheduledItemsVin(String vin) {
        clickWhenClickable(editbox_ScheduledItemVin);
        editbox_ScheduledItemVin.sendKeys(vin);
        editbox_ScheduledItemVin.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:limit-inputEl']")
    public WebElement editbox_ScheduledItemLimit;


    public void setScheduledCargoLimit(String limit) {
        
        clickWhenClickable(editbox_ScheduledItemLimit);
        editbox_ScheduledItemLimit.sendKeys(limit);
        editbox_ScheduledItemLimit.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//textarea[contains(@id,':Description-inputEl')]")
    public WebElement editbox_description;


    public void setCargoDescription(String desc) {
        
        clickWhenClickable(editbox_description);
        editbox_description.sendKeys(desc);
        editbox_description.sendKeys(Keys.TAB);
    }


    public void setScheduledItem(Cargo _vehicle) {
        clickWhenClickable(button_CargoScheduledItemsAdd);
        if (_vehicle.getVehicleType().getValue() != "") {
            
            setVehicleType(_vehicle.getVehicleType().getValue());
        } else {
            setVehicleType();
        }

        if (isScheduleItemsYearEditable()) {
            setScheduledItemsYear(String.valueOf(_vehicle.getModelYear()));
            setScheduledItemsMake(_vehicle.getMake());
            setScheduledItemsModel(_vehicle.getModel());
            setScheduledItemsVin(_vehicle.getVin());
        }
        setScheduledCargoLimit(_vehicle.getLimit());
    }


    public void setScheduledItems(List<Cargo> vehicles) {
        for (Cargo trailer : vehicles) {
            setScheduledItem(trailer);
        }
    }


    public void inputCargo(SquireIMCargo cargo) {
        clickCargoAdd();
        setClass(cargo.getCargoClass());
        setCoverageRequired(cargo.getCargoCoverage());
        if (cargo.getCargoCoverage() == true) {
            setApplicantHaulPrincipally(cargo.getCustomerOwnsHaul());
        }

        if (cargo.getCargoAdditionalInsured() != null) {
            for (String ai : cargo.getCargoAdditionalInsured()) {
                addAdditionalInsureds(ai);
            }
        }
        
        setScheduledItems(cargo.getScheduledItemList());
        if (!(cargo.getCargoDescription() == null || cargo.getCargoDescription().equals("") || cargo.getCargoDescription().isEmpty())) {
            setCityCargoDescription(cargo.getCargoDescription());
        }
        clickOK();
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:radius-inputEl']")
    public WebElement editbox_Radius;


    public void setRadius(String radius) {
        clickWhenClickable(editbox_Radius);
        editbox_Radius.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Radius.sendKeys(radius);
        editbox_Radius.sendKeys(Keys.TAB);
    }

    public Guidewire8Select select_CargoType() {
        return new Guidewire8Select(driver, "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:type-triggerWrap']");
    }


    public void setCargoType(ClassIIICargoTypes _cargoType) {
        Guidewire8Select mySelect = select_CargoType();
        mySelect.selectByVisibleText(_cargoType.getValue());
    }


    public void inputCargoClassIII(SquireIMCargo cargo) {
        inputCargo(cargo);
        
        setCargoType(cargo.getCargoType());
        
        setRadius(String.valueOf(cargo.getRadius()));
        clickOK();
    }

    @FindBy(xpath = "//*[@id='CargoPopup:CoverableDetailsCV:CargoDetailsDV:Limit-inputEl']")
    public WebElement text_CargoLimit;


    public String getLimit() {
        return text_CargoLimit.getText();
    }

    @FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:CargoScreen:CargoPanelSet:CoverableListDetailPanel:CoverableLV') or contains(@id, ':IMWizardStepGroup:CargoScreen:CargoPanelSet:CoverableListDetailPanel:CoverableLV')]")
    public WebElement cargoCoverageTable;


    public void clickEditButtonByRowInCargoCoverageTable(int row) {
        
        tableUtils.clickLinkInSpecficRowInTable(cargoCoverageTable, row);
        
    }


    public void setCheckBoxByRowinCargoCoverageTable(int row) {
        tableUtils.setCheckboxInTable(cargoCoverageTable, row, true);
    }


    public void clickOKButton() {
        super.clickOK();
        

    }

    @FindBy(xpath = "//textarea[contains(@id, 'CargoPopup:CoverableDetailsCV:CargoDetailsDV:Description-inputEl')]")
    public WebElement cargoDescription;


    public void setCityCargoDescription(String description) {
        clickWhenClickable(cargoDescription);
        cargoDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        cargoDescription.sendKeys(description);
        cargoDescription.sendKeys(Keys.TAB);

    }


    public void clickEditButton() {
        super.clickEdit();
    }


    public void clickRemoveButton() {
        super.clickRemove();
        
    }


    public int getCargoItemNumberByType(CargoClass type) {
        int returnValue = 0;
        String findingValue = type.getValue();
        if (findingValue.contains("Class III Owner")) {
            findingValue = "Class III Owner";
        }
        int row = tableUtils.getRowNumberInTableByText(cargoCoverageTable, findingValue);
        returnValue = Integer.parseInt(tableUtils.getCellTextInTableByRowAndColumnName(cargoCoverageTable, row, "Cargo #"));
        return returnValue;
    }
}

