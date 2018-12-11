package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.FPP;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FPPOptionalCoverages;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages extends GenericWorkorderSquirePropertyAndLiabilityCoverages {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutAdditionalSectionICoverages(GeneratePolicy policy) throws GuidewireException {
        if (policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentCheck()) {
            clickFarmPersonalProperty();
            selectOptionalCoverageType(FPPOptionalCoverages.NonOwnerEquipment);
            setAdditionalCoveragesLimit(FPPOptionalCoverages.NonOwnerEquipment, policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentLimit());
        }

        if ((policy.squire.isFarmAndRanch() || policy.squire.isCountry()) && !policy.squire.propertyAndLiability.squireFPP.getNonOwnedEquipmentCheck()) {
            if (policy.squire.propertyAndLiability.squireFPP != null) {
                if (!policy.squire.propertyAndLiability.squireFPP.getItems().isEmpty()) {
                    clickFarmPersonalProperty();

                    checkCoverageD(true);
                    
                    selectCoverageType(policy.squire.propertyAndLiability.squireFPP.getCoverageType());
                    selectDeductible(policy.squire.propertyAndLiability.squireFPP.getDeductible());

                    clickFPPAdditionalInterests();
                    GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
                    if (policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests() != null && policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests().size() > 0) {
                        for (AdditionalInterest ai : policy.squire.propertyAndLiability.squireFPP.getAdditionalInterests()) {
                            fppInts.addInterest(policy.basicSearch, ai);
                        }//END FOR
                    }//END IF

                    fppInts.clickFPPCoverages();

                    for (FarmPersonalPropertyTypes parentType : FarmPersonalPropertyTypes.values()) {
                        if (policy.squire.propertyAndLiability.squireFPP.getItems(parentType).size() > 0) {
                            selectCoverages(parentType);

                            ArrayList<SquireFPPTypeItem> allItems = policy.squire.propertyAndLiability.squireFPP.getItems(parentType);
                            for (SquireFPPTypeItem item : allItems) {
                                addItem(item.getType(), policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(item.getType()), policy.squire.propertyAndLiability.squireFPP.getTotalValuePerSubType(item.getType()), item.getDescription());
                            }//END FOR

                            clickFPPAdditionalInterests();
                            fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);

                            for (SquireFPPTypeItem item : policy.squire.propertyAndLiability.squireFPP.getItemsWithAdditionalInterests(parentType)) {
                                fppInts.addItem(item.getType(), item.getDescription(), item.getValue(), item.getSerialNumber(), item.getAdditionalInterest());
                            }//END FOR

                            fppInts.clickFPPCoverages();
                        }//END IF
                    }//END FOR
                }
            }
        }
    }


    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:addlInterestSpecificationTab-btnEl')]")
    private WebElement link_SquireFPPAdditionalInterests;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:FarmPersonalPropertyCoveragesPanelSet:covD:HOCoverageInputSet:CovPatternInputGroup-legend-innerCt')]")
    private WebElement text_FPPCoverageD;

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:FarmPersonalPropertyCoveragesPanelSet:NonOwned:HOCoverageInputSet:CovPatternInputGroup-legend-innerCt')]")
    private WebElement text_NonOwnedEquipment;

    private Guidewire8Checkbox checkbox_FarmPersonalPropertyFPPCoverages() {
        return new Guidewire8Checkbox(driver, "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyCoveragesPanelSet:covD:HOCoverageInputSet:CovPatternInputGroup-legend-innerCt')]/table");
    }

    private Guidewire8Checkbox checkbox_NonOwnedEquipment() {
        return new Guidewire8Checkbox(driver, "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyCoveragesPanelSet:NonOwned:HOCoverageInputSet:CovPatternInputGroup-legend-innerCt')]/table");
    }

    private Guidewire8Select Select_FarmPersonalPropertyFPPCoveragesCoverageType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyCoveragesPanelSet:covD:HOCoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:TypekeyTermInput-triggerWrap') or contains(@id, ':covD:HOCoverageInputSet:CovPatternInputGroup:0:CovTermPOCHOEInputSet:TypekeyTermInput-triggerWrap')]");
    }

    private Guidewire8Select Select_FarmPersonalPropertyFPPCoveragesDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyCoveragesPanelSet:covD:HOCoverageInputSet:CovPatternInputGroup:1:CovTermInputSet:OptionTermInput-triggerWrap') or contains(@id, ':covD:HOCoverageInputSet:CovPatternInputGroup:1:CovTermPOCHOEInputSet:OptionTermInput-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:2:CovTermPOCHOEInputSet:DirectTermInput-inputEl')]")
    private WebElement text_CoverageDLimit;

    private Guidewire8Checkbox checkbox_FarmPersonalPropertyFPPCoverages(FarmPersonalPropertyTypes property) {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'" + property.getValue() + "')]/preceding-sibling::table");
    }

    private Guidewire8Checkbox checkbox_FPPOptionalCoverages(FPPOptionalCoverages property) {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'" + property.getValue() + "')]/preceding-sibling::table");
    }

    private List<WebElement> editboxes_Limit(FPPOptionalCoverages coverage) {
        return finds(By.xpath("//fieldset//legend[.='" + coverage.getValue() + "']/following-sibling::div//table//td/label[contains(., 'Limit')]/parent::td/following-sibling::td//input"));
    }

    private WebElement button_Add(FarmPersonalPropertyTypes type) {
        return find(By.xpath("//fieldset/legend[.='" + type.getValue() + "']/parent::fieldset//a[contains(@id,':ScheduleInputSet:Add')]"));
    }

    private WebElement button_Remove(FarmPersonalPropertyTypes type) {
        return find(By.xpath("//fieldset/legend[.='" + type.getValue() + "']/parent::fieldset//a[contains(@id,':ScheduleInputSet:Remove')]"));
    }

    private WebElement tableUtilsWrapper_Items(FarmPersonalPropertyTypes type) {
        return find(By.xpath("//fieldset/legend[.='" + type.getValue() + "']/parent::fieldset//div[contains(@id,':ScheduleInputSet:')]/div[contains(@id, '-body')]/parent::div"));
    }


    /*
     * Methods
     */


    public void clickFPPAdditionalInterests() {
        
        clickWhenClickable(link_SquireFPPAdditionalInterests);
        
    }


    public void checkCoverageD(boolean trueFalse) {
        
        checkbox_FarmPersonalPropertyFPPCoverages().select(trueFalse);
        
    }


    public void checkNonOwnedEquipment(boolean trueFalse) {
        
        checkbox_NonOwnedEquipment().select(trueFalse);
        
    }


    public void selectCoverageType(FPPCoverageTypes type) {
        
        Select_FarmPersonalPropertyFPPCoveragesCoverageType().selectByVisibleText(type.getValue());
    }


    public void selectDeductible(FPP.FPPDeductible deductible) {
        
        Select_FarmPersonalPropertyFPPCoveragesDeductible().selectByVisibleText(deductible.getValue());
        
    }


    public void clickAdd(FarmPersonalPropertyTypes type) {
        clickWhenClickable(button_Add(type));
    }


    public void selectCoverages(FarmPersonalPropertyTypes property) {
        
        checkbox_FarmPersonalPropertyFPPCoverages(property).select(true);
        
    }


    public void unselectCoverages(FarmPersonalPropertyTypes property) {
        
        checkbox_FarmPersonalPropertyFPPCoverages(property).clear();
        
    }

    public void setQuantityValueDescription(FPPFarmPersonalPropertySubTypes type, int tableRow, int quantity, int value, String description) {
        tableUtils.setValueForCellInsideTable(tableUtilsWrapper_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsWrapper_Items(type.getParentType()), "Quantity"), "Quantity", "Quantity", String.valueOf(quantity));
        
        tableUtils.setValueForCellInsideTable(tableUtilsWrapper_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsWrapper_Items(type.getParentType()), "Value"), "Value", "Value", String.valueOf(value));
        
        tableUtils.setValueForCellInsideTable(tableUtilsWrapper_Items(type.getParentType()), tableUtils.getNextAvailableLineInTable(tableUtilsWrapper_Items(type.getParentType()), "Description"), "Description", "Description", description);
        
    }


    public void addItem(FPPFarmPersonalPropertySubTypes type, int quantity, int value, String description) {
        
        clickAdd(type.getParentType());
        
        int size = tableUtils.getNextAvailableLineInTable(tableUtilsWrapper_Items(type.getParentType()));
        tableUtils.selectValueForSelectInTable(tableUtilsWrapper_Items(type.getParentType()), size, "Item Type", type.getValue());
        setQuantityValueDescription(type, size, quantity, value, description);
    }


    public void selectOptionalCoverageType(FPPOptionalCoverages optionalCoverage) {
        
        checkbox_FPPOptionalCoverages(optionalCoverage).select(true);
    }


    public void setAdditionalCoveragesLimit(FPPOptionalCoverages optionalCoverage, int limit) {
        
        clickWhenClickable(editboxes_Limit(optionalCoverage).get(0));
        editboxes_Limit(optionalCoverage).get(0).sendKeys(String.valueOf(limit));
        editboxes_Limit(optionalCoverage).get(0).sendKeys(Keys.TAB);
        

    }


    public void removeItem(FPPFarmPersonalPropertySubTypes type) {

    }


    public void removeItem(FarmPersonalPropertyTypes type, int rowNumber) {
        tableUtils.setCheckboxInTable(tableUtilsWrapper_Items(type), rowNumber, true);
        clickWhenClickable(button_Remove(type));
    }


    public boolean checkCoverageDExists() {
        try {
            waitUntilElementIsVisible(text_FPPCoverageD, 1000);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean checkNonOwnedEquipmentExists() {
        try {
            waitUntilElementIsVisible(text_NonOwnedEquipment, 1000);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public int getTotalValueByType(FarmPersonalPropertyTypes type) {
        
        int row = tableUtils.getRowCount(tableUtilsWrapper_Items(type));
        String totalText = tableUtils.getCellTextInTableByRowAndColumnName(tableUtilsWrapper_Items(type), row, "Value");
        return Integer.parseInt(totalText.replace("Total: ", "").trim());
    }


    public int getCoverageDLimit() {
        return Integer.parseInt(text_CoverageDLimit.getText().trim().replace(",", ""));
    }


    public void setValue(FarmPersonalPropertyTypes type, int tableRow, String tableHeaderName, String cellInputName, String value) {
        tableUtils.setValueForCellInsideTable(tableUtilsWrapper_Items(type), tableRow, tableHeaderName, cellInputName, value);

    }
}
