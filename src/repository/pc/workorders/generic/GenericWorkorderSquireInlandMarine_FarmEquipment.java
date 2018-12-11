package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_FarmEquipment extends GenericWorkorder {


    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderSquireInlandMarine_FarmEquipment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }


    public void fillOutFarmEquipment(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.FarmEquipment)) {
            sideMenu.clickSideMenuIMFarmEquipment();
            boolean addInsured = false;
            for (FarmEquipment farmThing : policy.squire.inlandMarine.farmEquipment) {
                if (farmThing.getCoverageType().equals(CoverageType.BroadForm)) {
                    policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_FarmEquipmentBroad);
                } else {
                    policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_FarmEquipmentSpecial);
                }
                addFarmEquip(policy.basicSearch, farmThing);
                if (farmThing.getAdditionalInsured() != null && addInsured == false) {
                    addInsured = true;
                }
            }
            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddLivestock);

        }
    }


    @FindBy(xpath = "//*[contains(@id, ':FarmEquipmentScreen:FarmEquipmentPanelSet:CoverableListDetailPanel_tb:ToolbarButton-btnEl')]")
    public WebElement button_FarmEquipmentAdd;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private WebElement table_FarmEquipmentAdditionalInterest;

    @FindBy(xpath = "//span[contains(@id, ':FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton-btnInnerEl')]")
    public WebElement button_FarmEquipmentScheduledAdditionalInterestAdd;

    @FindBy(xpath = "//span[contains(@id, ':FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-textEl')]")
    public WebElement link_FarmEquipmentScheduledItemAddExistingAdditionalInterest;


    public void clickAdd() {
        clickWhenClickable(button_FarmEquipmentAdd);
    }


    public void clickOk() {
        super.clickOK();
    }

    public Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:type-triggerWrap' or @id = 'FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:type-triggerWrap']");
    }


    public void setType(IMFarmEquipmentType type) {
        Guidewire8Select mySelect = select_Type();
        mySelect.selectByVisibleText(type.getValue());
    }


    public List<String> getTypeValues() {
        Guidewire8Select mySelect = select_Type();
        return mySelect.getList();
    }

    public Guidewire8Select select_CoverageType() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:0:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap']");
    }


    public void setCoverageType(CoverageType type) {
        if (checkIfElementExists("//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:0:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap']", 156)) {
            Guidewire8Select mySelect = select_CoverageType();
            mySelect.selectByVisibleTextPartial(type.getValue());
        }
    }

    public Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:2:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap' or @id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:2:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap']");
    }


    public void setDeductible(IMFarmEquipmentDeductible _deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(_deductible.getValue());
    }

    public Guidewire8RadioButton radio_Inspected() {
        return new Guidewire8RadioButton(driver, "//div[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:inspected-containerEl' or @id = 'FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:inspected-containerEl']/table");
    }


    public void setInspected(Boolean trueFalse) {
        
        radio_Inspected().select(trueFalse);
    }

    public Guidewire8RadioButton radio_ExistingDamage() {
        return new Guidewire8RadioButton(driver, "//div[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:exisitingDamage-containerEl' or @id = 'FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentDetailsDV:exisitingDamage-containerEl']/table");
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

    @FindBy(xpath = "//*[contains(@id,':FarmEquipAddInsuredDV:AdditionalInsuredLV')]")
    public WebElement table_FarmEquipmentAddtionalInsured;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;


    public void setAdditionalInsureds(String addInsured) {
        clickAdditionalInsuredsAdd();
        tableUtils.setValueForCellInsideTable(table_FarmEquipmentAddtionalInsured, tableUtils.getNextAvailableLineInTable(table_FarmEquipmentAddtionalInsured), "Name", "Name", addInsured);
    }


    public void removeAdditionalInsuredByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_FarmEquipmentAddtionalInsured, name);
        tableUtils.setCheckboxInTable(table_FarmEquipmentAddtionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_FarmEquipmentAddtionalInsured, row, "Name");
    }

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel_tb:Add-btnEl']")
    public WebElement button_FarmEquipmentScheduledEquipmentAdd;

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel_tb:Remove-btnEl']")
    public WebElement button_FarmEquipmentScheduledEquipmentRemove;


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

    public Guidewire8Select select_Location() {
        return new Guidewire8Select(driver, "//table[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableDetailsCV:location-triggerWrap']");
    }


    public void setLocation(String _location) {
        Guidewire8Select mySelect = select_Location();
        mySelect.selectByVisibleText(_location);
    }


    public void setLocationByPartialText(String _location) {
        Guidewire8Select mySelect = select_Location();
        mySelect.selectByVisibleTextPartial(_location);
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

    @FindBy(xpath = "//*[@id='FarmEquipmentPopup:CoverableDetailsCV:FarmEquipmentSchedItemsPanelSet:CoverableListDetailPanel:CoverableLV']")
    public WebElement table_scheduledEquipment;

    @FindBy(xpath = "//*[contains(@id, ':LineWizardStepSet:IMWizardStepGroup:FarmEquipmentScreen:FarmEquipmentPanelSet:CoverableListDetailPanel:CoverableLV')]")
    public WebElement table_farmEquipments;

    @FindBy(xpath = "//span[contains(@id, ':CoverableListDetailPanel_tb:Remove-btnEl')]")
    public WebElement link_RemoveFarmEquip;


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
        if (!(item.getLocation() == null || item.getLocation().equals(""))) {
            setLocation(item.getLocation());
        }
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
        
        if (!farmStuff.getIMFarmEquipmentType().equals(IMFarmEquipmentType.MovableSetSprinkler) && !farmStuff.getIMFarmEquipmentType().equals(IMFarmEquipmentType.WheelSprinkler)) {
            setCoverageType(farmStuff.getCoverageType());
        }
        setDeductible(farmStuff.getDeductible());
        setInspected(farmStuff.getInspected());
        setExistingDamage(farmStuff.getExistingDamage());
        setOwner(farmStuff.getOwner());
        setlistAdditionalInsured(farmStuff.getAdditionalInsured());
        setAdditionalInterest(basicSearch, farmStuff.getAdditionalInterests());        
        setListScheduledItem(farmStuff.getScheduledFarmEquipment());
        clickOK();
    }


    public void clickEditButton() {
        super.clickEdit();
    }


    public void clickSearchAdditionalInterests() {
        super.clickSearch();
        

    }


    public String getFarmEquipmentAdditionalInterestValues(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_FarmEquipmentAdditionalInterest, row, columnName);
    }


    public void removeScheduledItem(int row) {
        tableUtils.setCheckboxInTable(table_scheduledEquipment, row, true);
        clickWhenClickable(button_FarmEquipmentScheduledEquipmentRemove);
    }

    @FindBy(xpath = "//span[contains(@id, 'FarmEquipmentPopup:CoverableDetailsCV:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton-btnWrap')]")
    private WebElement additionalInterestAddExisting;

    @FindBy(xpath = "//span[contains(@id, 'FarmEquipmentPopup:CoverableDetailsCV:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddExistingContact-textEl')]")
    private WebElement additionalInterestAddExistingOtherContacts;

    @FindBy(xpath = "//span[contains(@id, 'FarmEquipmentPopup:CoverableDetailsCV:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-textEl')]")
    private WebElement additionalInterestAddOtherContacts;

    @FindBy(xpath = "//div[contains(@id, ':AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterestsOtherContactArrow;


    public WebElement list_additionalInterestExistingBySpecific(String name) {
        return find(By.xpath("//a[contains(@id,':acctContact-itemEl')and contains(.,'" + name + "')]"));
    }

    public Guidewire8Select select_SquireWatercraftdditionalInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'FinishPCR')]")
    private WebElement additionalInterestUpdate;


    public void addFarmEquipmentExistingAdditionalInterest(String firstName, AdditionalInterestType type) {
        String nameXpath = "//span[contains(text(), '" + firstName + "')]/parent::a[contains(@id, 'ExistingAdditionalInterest-itemEl')]";
        clickWhenClickable(additionalInterestAddExisting);
        hoverOverFirstToClickSecond(additionalInterestAddExistingOtherContacts, nameXpath);
        
        select_SquireWatercraftdditionalInterestType().selectByVisibleTextPartial(type.getValue());
        clickWhenClickable(additionalInterestUpdate);
    }


    public void addFarmEquipmentOtherAdditionalInterest(String firstName, AdditionalInterestType type) {
        clickWhenClickable(additionalInterestAddExisting);
        
        hoverOverAndClick(additionalInterestAddOtherContacts);
        hoverOverAndClick(link_AdditionalInterestsOtherContactArrow);
        
        clickWhenClickable(additionalInterestAddOtherContacts);
        
        clickWhenClickable(list_additionalInterestExistingBySpecific(firstName));
        
        select_SquireWatercraftdditionalInterestType().selectByVisibleTextPartial(type.getValue());
        clickWhenClickable(additionalInterestUpdate);
    }


    public void clickFarmEquipmentAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_FarmEquipmentAdditionalInterest, tableUtils.getRowNumberInTableByText(table_FarmEquipmentAdditionalInterest, name), "Name");
    }


    public boolean checkFarmEquipmentAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_FarmEquipmentAdditionalInterest, name) > 0;
    }


    public void addFarmEquipmentScheduledItemExistingAdditionalInterest(String firstName, AdditionalInterestType type) {
        String nameXpath = "//span[contains(text(), '" + firstName
                + "')]/parent::a[contains(@id, ':AddContactsButton:AddExistingContact')]";
        clickWhenClickable(button_FarmEquipmentScheduledAdditionalInterestAdd);
        hoverOverFirstToClickSecond(link_FarmEquipmentScheduledItemAddExistingAdditionalInterest, nameXpath);
    }


    public void addFarmEquipWithAdditionalInterst(boolean basicSearch, FarmEquipment farmStuff, AdditionalInterestType interestType) throws Exception {
        clickAdd();
        
        setType(farmStuff.getIMFarmEquipmentType());
        
        if (!farmStuff.getIMFarmEquipmentType().equals(IMFarmEquipmentType.MovableSetSprinkler) && !farmStuff.getIMFarmEquipmentType().equals(IMFarmEquipmentType.WheelSprinkler)) {
            setCoverageType(farmStuff.getCoverageType());
        }
        setDeductible(farmStuff.getDeductible());
        setInspected(farmStuff.getInspected());
        setExistingDamage(farmStuff.getExistingDamage());
        setOwner(farmStuff.getOwner());
        setlistAdditionalInsured(farmStuff.getAdditionalInsured());
        setListScheduledItem(farmStuff.getScheduledFarmEquipment());
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.searchAndAddExistingCompanyAsAdditionalInterest(basicSearch, AdditionalInterestType.LessorPL);

    }

    private void setAdditionalInterest(boolean basicSearch, ArrayList<AdditionalInterest> additionalInterests) throws Exception {
        for (AdditionalInterest additionalInterest : additionalInterests) {
            GenericWorkorderAdditionalInterests aInterest = new GenericWorkorderAdditionalInterests(driver);
            aInterest.fillOutAdditionalInterest(basicSearch, additionalInterest);
        }
    }
    public void removeFarmEquipmentByRowinTable(int row) {
        tableUtils.setCheckboxInTable(table_farmEquipments, row, true);
        
        clickWhenClickable(link_RemoveFarmEquip);
        
    }

}
