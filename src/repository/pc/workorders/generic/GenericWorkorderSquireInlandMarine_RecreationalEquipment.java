package repository.pc.workorders.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.OkCancel;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_RecreationalEquipment extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquireInlandMarine_RecreationalEquipment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutRecreationEquipment(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.RecreationalEquipment)) {
            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_RecEquipmentOrWheelchair);

            sideMenu.clickSideMenuIMRecreationVehicle();
            for (RecreationalEquipment recVehicle : policy.squire.inlandMarine.recEquipment_PL_IM) {
                recEquip(recVehicle);
            }

            //Go to Section II and add specific Rec Equipment Coverage
            try {
                sideMenu.clickSideMenuSquirePropertyCoverages();
            } catch (Exception e) {
                ErrorHandling softMsg = new ErrorHandling(getDriver());
                if (checkIfElementExists(softMsg.text_ErrorHandlingErrorBanner(), 1000)) {
                    sideMenu.clickSideMenuSquirePropertyCoverages();
                }
            }

            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(getDriver());
            coverages.clickSectionIICoveragesTab();
            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());

            int allTerrainVehicle = 0;
            int recVehicle = 0;
            int offRoadMotocycle = 0;
            int snowmobile = 0;


            for (RecreationalEquipment toy : policy.squire.inlandMarine.recEquipment_PL_IM) {
                if (toy.getRecEquipmentType() == RecreationalEquipmentType.AllTerrainVehicle) {
                    allTerrainVehicle++;
                } else if (toy.getRecEquipmentType() == RecreationalEquipmentType.OffRoadMotorcycle) {
                    offRoadMotocycle++;
                } else if (toy.getRecEquipmentType() == RecreationalEquipmentType.Snowmobile) {
                    snowmobile++;
                } else {
                    recVehicle++;
                }
            }

            if (allTerrainVehicle > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.AllTerrainVehicles, 0, 0));
                
                sectionII.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.AllTerrainVehicles, 0, allTerrainVehicle));
            }
            if (offRoadMotocycle > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, 0));
                sectionII.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, offRoadMotocycle));
            }
            if (snowmobile > 0) {
                sectionII = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Snowmobiles, 0, 0));
                sectionII.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.Snowmobiles, 0, snowmobile));
            }

            if (recVehicle > 0) {
                sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.GolfCart, 0, 0));
                sectionII.setQuantity(new SectionIICoverages(SectionIICoveragesEnum.GolfCart, 0, recVehicle));
            }
        }
    }


    @FindBy(xpath = "//div[contains(@id, ':IMWizardStepGroup:RecreationVehicleScreen:RecreationVehiclePanelSet:CoverableListDetailPanel:CoverableLV')]")
    public WebElement reCreatonalEquipmentTable;

    @FindBy(xpath = "//*[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:IMWizardStepGroup:RecreationVehicleScreen:RecreationVehiclePanelSet:CoverableListDetailPanel_tb:Remove']")
    public WebElement button_Remove;

    public void setRETableCheckBoxByTypeAndRemove(String linkText) {
        tableUtils.setCheckboxInTableByText(reCreatonalEquipmentTable, linkText, true);
        
        clickWhenClickable(button_Remove);
    }

    public String getRETableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(reCreatonalEquipmentTable, row, columnName);
    }


    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Year-inputEl')]")
    private WebElement editbox_IMRecEquipmentYear;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Make-inputEl')]")
    private WebElement editbox_IMRecEquipmentMake;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Model-inputEl')]")
    private WebElement editbox_IMRecEquipmentModel;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Serial-inputEl')]")
    private WebElement editbox_IMRecEquipmentVINSerial;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Description-inputEl')]")
    private WebElement editbox_IMRecEquipmentDescription;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Limit-inputEl')]")
    private WebElement editbox_IMRecEquipmentLimit;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:TitleOwner-inputEl')]")
    private WebElement editbox_IMRecEquipmentRegistration;

    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:HasExistingDamage-inputEl')]")
    private WebElement editbox_IMRecEquipmentDamageDescription;


    public Guidewire8Select select_IMRecEquipmentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Type-triggerWrap')]");
    }

    public Guidewire8Select select_IMRecEquipmentDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:deduct-triggerWrap')]");
    }

    public Guidewire8RadioButton radio_IMRecEquipmentInspected() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:Inspected-containerEl')]/table");
    }

    public Guidewire8RadioButton radio_IMRecEquipmentExistingDamage() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:ExistingDamage-containerEl')]/table");
    }

    @FindBy(xpath = "//span[contains(@id, 'RecreationVehiclePopup:PIMRecreationVehicleAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnEl')]")
    private WebElement button_IMRecEquipmentAddAdditionalInsureds;

    @FindBy(xpath = "//div[contains(@id, ':PIMRecreationVehicleAdditionalInsuredDV:AdditionalInsuredLV')]")
    private WebElement table_IMRecEquipmentAddAdditionalInsureds;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;

    @FindBy(xpath = "//div[contains(@id, 'messagebox') and contains(., 'The deductible was changed on ')]")
    private WebElement text_IMRecEquipmentDeductibleMessagebox;

    @FindBy(xpath = "//div[contains(@id, ':IMWizardStepGroup:RecreationVehicleScreen:RecreationVehiclePanelSet:CoverableListDetailPanel:CoverableLV')]")
    private WebElement RecEquipmentTable;


    @FindBy(xpath = "//input[contains(@id, 'RecreationVehiclePopup:RecreationVehicleDetailsDV:PhotoYear-inputEl')]")
    private WebElement photoYear;

    @FindBy(xpath = "//span[contains(@id, 'RecreationVehiclePopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton-btnWrap')]")
    private WebElement additionalInterestAddExisting;

    @FindBy(xpath = "//a[contains(@id, 'RecreationVehiclePopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement additionalInterestAddExistingOtherContacts;

    @FindBy(xpath = "//div[contains(@id, 'AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private WebElement RecEquipmentAdditionalInterestTable;

    public WebElement list_additionalInterestExistingBySpecific(String name) {
        return find(By.xpath("//a[contains(@id,':acctContact-itemEl')and contains(.,'" + name + "')]"));
    }

    public Guidewire8Select select_SquireRecreationalEquipmentdditionalInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, 'FinishPCR')]")
    private WebElement additionalInterestUpdate;


    public void selectRecEquipmentType(RecreationalEquipmentType recType) {
        
        select_IMRecEquipmentType().selectByVisibleText(recType.getValue());
        
    }


    public void setRecEquipmentYear(String year) {
        clickWhenClickable(editbox_IMRecEquipmentYear);
        editbox_IMRecEquipmentYear.sendKeys(year);
        editbox_IMRecEquipmentYear.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentMake(String make) {
        clickWhenClickable(editbox_IMRecEquipmentMake);
        editbox_IMRecEquipmentMake.sendKeys(make);
        editbox_IMRecEquipmentMake.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentModel(String model) {
        clickWhenClickable(editbox_IMRecEquipmentModel);
        editbox_IMRecEquipmentModel.sendKeys(model);
        editbox_IMRecEquipmentModel.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentVIN(String vin) {
        clickWhenClickable(editbox_IMRecEquipmentVINSerial);
        editbox_IMRecEquipmentVINSerial.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_IMRecEquipmentVINSerial.sendKeys(vin);
        editbox_IMRecEquipmentVINSerial.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentDescription(String description) {
        clickWhenClickable(editbox_IMRecEquipmentDescription);
        editbox_IMRecEquipmentDescription.sendKeys(description);
        editbox_IMRecEquipmentDescription.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentLimit(String limit) {
        clickWhenClickable(editbox_IMRecEquipmentLimit);
        editbox_IMRecEquipmentLimit.sendKeys(limit);
        editbox_IMRecEquipmentLimit.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentDamageDescription(String description) {
        clickWhenClickable(editbox_IMRecEquipmentDamageDescription);
        editbox_IMRecEquipmentDamageDescription.sendKeys(description);
        editbox_IMRecEquipmentDamageDescription.sendKeys(Keys.TAB);
    }


    public void setRecEquipmentOwner(String owner) {
        
        clickWhenClickable(editbox_IMRecEquipmentRegistration);
        
        editbox_IMRecEquipmentRegistration.sendKeys(owner);
        editbox_IMRecEquipmentRegistration.sendKeys(Keys.TAB);
    }


    public void selectDeductible(String deductible) {
        
        select_IMRecEquipmentDeductible().selectByVisibleText(deductible);
    }


    public String getDeductibleChangeMessage() {
        String txt = text_IMRecEquipmentDeductibleMessagebox.getText();
        selectOKOrCancelFromPopup(OkCancel.OK);
        return txt;
    }


    public void setRecEquipmentInspectedRadio(boolean trueFalse) {
        
        radio_IMRecEquipmentInspected().select(trueFalse);
        
    }


    public void setRecEquipmentExistingDamageRadio(boolean trueFalse) {
        
        radio_IMRecEquipmentExistingDamage().select(trueFalse);
        
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickAdd() {
        super.clickAdd();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickOK() {
        super.clickOK();
    }

    //Fills out just required fields.

    public void recEquip(RecreationalEquipment recEquip) {
        clickAdd();
        
        selectRecEquipmentType(recEquip.getRecEquipmentType());
        setRecEquipmentInspectedRadio(recEquip.isInspected());
        setRecEquipmentYear(recEquip.getModelYear() + "");
        setRecEquipmentMake(recEquip.getMake());
        setRecEquipmentModel(recEquip.getModel());
        setRecEquipmentVIN(recEquip.getVin());
        setRecEquipmentLimit(recEquip.getLimit());
        if (recEquip.getDescribeExistingDamage() == null || recEquip.getDescribeExistingDamage().equals("")) {
            setRecEquipmentExistingDamageRadio(false);
        } else {
            setRecEquipmentDamageDescription(recEquip.getDescribeExistingDamage());
            setRecEquipmentExistingDamageRadio(true);
            
        }
        setRecEquipmentOwner(recEquip.getOwner());
        selectDeductible(recEquip.getDeductible().getValue());

        if (recEquip.getRecEquipmentType().equals(RecreationalEquipmentType.WagonsCarriages)) {
            setRecEquipmentDescription(recEquip.getDescription());
        }
        clickOK();
    }


    public void clickAddAdditionalInsureds() {
        
        clickWhenClickable(button_IMRecEquipmentAddAdditionalInsureds);
        
    }


    public void addAdditionalInsured(String name) {
        
        clickAddAdditionalInsureds();
        
        tableUtils.setValueForCellInsideTable(table_IMRecEquipmentAddAdditionalInsureds, tableUtils.getNextAvailableLineInTable(table_IMRecEquipmentAddAdditionalInsureds), "Name", "FirstName", name);
    }


    public void removeAdditionalInsuredByName(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_IMRecEquipmentAddAdditionalInsureds, name);
        tableUtils.setCheckboxInTable(table_IMRecEquipmentAddAdditionalInsureds, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_IMRecEquipmentAddAdditionalInsureds, row, "Name");
    }


    public void clickLinkInRecreationalEquipmentTable(int row) {
        tableUtils.clickLinkInSpecficRowInTable(RecEquipmentTable, row);
    }


    public void clickSearchAdditionalInterests() {
        super.clickSearch();
        

    }


    public void addExistingAdditionalInterest(String firstName, AdditionalInterestType type) {

        String nameXpath = "//span[contains(text(), '" + firstName + "')]/parent::a[contains(@id, ':acctContact')]";
        clickWhenClickable(additionalInterestAddExisting);
        hoverOver(additionalInterestAddExistingOtherContacts);
        clickWhenClickable(By.xpath(nameXpath));
        
        select_SquireRecreationalEquipmentdditionalInterestType().selectByVisibleTextPartial(type.getValue());
        clickWhenClickable(additionalInterestUpdate);
    }


    public void setPhotoYear(String value) {
        waitUntilElementIsVisible(photoYear);
        photoYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        photoYear.sendKeys(value);
        photoYear.sendKeys(Keys.TAB);
        
    }


    public void clickRecEquipmentAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(RecEquipmentAdditionalInterestTable, tableUtils.getRowNumberInTableByText(RecEquipmentAdditionalInterestTable, name), "Name");
    }


    public boolean checkRecEquipmentAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(RecEquipmentAdditionalInterestTable, name) > 0;
    }


    public String recEquipchooseOKOnConfirmation() {
        
        return selectOKOrCancelFromPopup(OkCancel.OK);
    }


}
