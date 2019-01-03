package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlandMarineContractorsEquipment;
import repository.gw.generate.custom.CPPInlandMarineContractorsEquipment_ScheduledItem;
import repository.gw.helpers.GuidewireHelpers;


public class GenericWorkorderInlandMarineContractorsEquipmentCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderInlandMarineContractorsEquipmentCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':ContractorsEquipPartPanelSet:coveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ContractorsEquipPartPanelSet:scheduleArticlesTab-btnInnerEl')]")
    private WebElement link_ScheduledItemsTab;

    private void clickScheduledItemsTab() {
        clickWhenClickable(link_ScheduledItemsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ContractorsEquipPartPanelSet:exclCondTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ContractorsEquipPartPanelSet:ContractorsAddlInterestTab-btnInnerEl')]")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ContractorsEquipPartPanelSet:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    // Contractors Equipment Coverage Form IH 00 68

    private Guidewire8Select select_ContractorType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CovTermInputSet:TypekeyTermInput-triggerWrap')]");
    }

    private void setContractorType(ContractorsEquipmentCoverageForm_IH_00_68_ContractorType contractorType) {
        Guidewire8Select mySelect = select_ContractorType();
        mySelect.selectByVisibleText(contractorType.getValue());
        
    }

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':1:CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setDeductible(ContractorsEquipmentCoverageForm_IH_00_68_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':2:CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setCoinsurance(ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    // Contractors Rented Equipment

    @FindBy(xpath = "//div[contains(text(), 'Contractors Rented Equipment')]/preceding-sibling::table//input")
    private WebElement checkbox_ContractorsRentedEquipment;

    private Guidewire8Checkbox checkbox_ContractorsRentedEquipment() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Contractors Rented Equipment')]/preceding-sibling::table");
    }

    private boolean checkContractorsRentedEquipment(Boolean checked) {
        //checkbox_MusicalInstrumentsBlanket().select(checked);
        if (!checkbox_ContractorsRentedEquipment().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_ContractorsRentedEquipment);
        }
        
        return checked;
    }

    @FindBy(xpath = "//input[contains(@id, ':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_ContractorsRentedEquipmentLimit;

    private void setContractorsRentedEquipmentLimit(String limit) {
        input_ContractorsRentedEquipmentLimit.click();
        input_ContractorsRentedEquipmentLimit.sendKeys(limit + Keys.TAB);
        waitForPostBack();
        
    }

    // Miscellaneous Items Blanket Coverage IH 68 02

    @FindBy(xpath = "//div[contains(text(), 'Miscellaneous Items Blanket Coverage IH 68 02')]/preceding-sibling::table//input")
    private WebElement checkbox_MiscellaneousItemsBlanket;

    private Guidewire8Checkbox checkbox_MiscellaneousItemsBlanket() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Miscellaneous Items Blanket Coverage IH 68 02')]/preceding-sibling::table");
    }

    private boolean checkMiscellaneousItemsBlanket(Boolean checked) {
        //checkbox_MusicalInstrumentsBlanket().select(checked);
        if (!checkbox_MiscellaneousItemsBlanket().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_MiscellaneousItemsBlanket);
        }
        
        return checked;
    }

    @FindBy(xpath = "//input[contains(@id, ':2:CoverageInputSet:CovPatternInputGroup:limitTermInput-inputEl')]")
    private WebElement input_MiscellaneousItemsLimit;

    private void setMiscellaneousItemsLimit(String limit) {
        input_MiscellaneousItemsLimit.click();
        input_MiscellaneousItemsLimit.sendKeys(limit + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_MiscellaneousItemsDeductible() {
        return new Guidewire8Select(driver, "//fieldset//div[contains(string(),'Miscellaneous Items Blanket')]/ancestor::fieldset//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setMiscellaneousItemsDeductible(MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible deductible) {
        Guidewire8Select mySelect = select_MiscellaneousItemsDeductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    // Tools And Clothing Belonging To Your Employees IH 68 01

    @FindBy(xpath = "//div[contains(text(), 'Tools And Clothing Belonging To Your Employees IH 68 01')]/preceding-sibling::table//input")
    private WebElement checkbox_ToolsAndClothing;

    private Guidewire8Checkbox checkbox_ToolsAndClothing() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Tools And Clothing Belonging To Your Employees IH 68 01')]/preceding-sibling::table");
    }

    private boolean checkToolsAndClothing(Boolean checked) {
        //checkbox_MusicalInstrumentsBlanket().select(checked);
        if (!checkbox_ToolsAndClothing().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_ToolsAndClothing);
        }
        
        return checked;
    }

    @FindBy(xpath = "//input[contains(@id, ':3:CoverageInputSet:CovPatternInputGroup:limitTermInput-inputEl')]")
    private WebElement input_ToolsAndClothingLimit;

    private void setToolsAndClothingLimit(String limit) {
        input_ToolsAndClothingLimit.click();
        input_ToolsAndClothingLimit.sendKeys(limit + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_ToolsAndClothingDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':3:CoverageInputSet:CovPatternInputGroup:deductibleTermInput-triggerWrap')]");
    }

    private void setToolsAndClothingDeductible(ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible deductible) {
        Guidewire8Select mySelect = select_ToolsAndClothingDeductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    ///////////////////////
    //  Scheduled Items  //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id, ':ContractorsEquipPartPanelSet:ContractorsScheduledPanelSet:Add-btnInnerEl')]")
    private WebElement buttonAddScheduledItem;

    private void clickAddScheduledItem() {
        clickWhenClickable(buttonAddScheduledItem);
        
    }

    private Guidewire8Select select_ScheduledItemEquipmentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContractorsScheduledPanelSet:equipmentType-triggerWrap')]");
    }

    private void setScheduledItemEquipmentType(ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType equipmentType) {
        Guidewire8Select mySelect = select_ScheduledItemEquipmentType();
        mySelect.selectByVisibleText(equipmentType.getValue());
        
    }

    @FindBy(xpath = "//input[contains(@id, ':ContractorsEquipPartPanelSet:ContractorsScheduledPanelSet:limit-inputEl')]")
    private WebElement input_ScheduledItemLimit;

    private void setScheduledItemLimit(String limit) {
        input_ScheduledItemLimit.click();
        input_ScheduledItemLimit.sendKeys(limit + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_ScheduledItemRentedToOthers() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContractorsScheduledPanelSet:rentedToOthers-triggerWrap')]");
    }

    private void setScheduledItemRentedToOthers(boolean isRentedToOthers) {
        Guidewire8Select mySelect = select_ScheduledItemRentedToOthers();
        if (isRentedToOthers) {
            mySelect.selectByVisibleText("Yes");
        } else {
            mySelect.selectByVisibleText("No");
        }
        
    }

    @FindBy(xpath = "//div[contains(@id, ':ContractorsScheduledPanelSet:rentedToOth-containerEl')]")
    private WebElement div_rentedFromOthers;

    private void setScheduledItemRentedFromOthers(boolean isRentedFromOthers) {

        if (isRentedFromOthers) {
            clickAndHoldAndRelease(div_rentedFromOthers.findElement(By.xpath(".//label[contains(text(),'Yes')]//preceding-sibling::input")));
        } else {
            clickAndHoldAndRelease(div_rentedFromOthers.findElement(By.xpath(".//label[contains(text(),'No')]//preceding-sibling::input")));
        }

    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutContractorsEquipment(GeneratePolicy policy) {

        // COVERAGES

        clickCoveragesTab();

        CPPInlandMarineContractorsEquipment contractorsEquipment = policy.inlandMarineCPP.getContractorsEquipment();

        setContractorType(contractorsEquipment.getContractorType());
        setDeductible(contractorsEquipment.getDeductible());
        setCoinsurance(contractorsEquipment.getCoinsurance());

        if (contractorsEquipment.isContractorsRentedEquipment()) {
            checkContractorsRentedEquipment(true);
            setContractorsRentedEquipmentLimit(contractorsEquipment.getContractorsRentedEquipmentLimit());
        }

        if (contractorsEquipment.isMiscellaneousItemsBlanketCoverage()) {
            checkMiscellaneousItemsBlanket(true);
            setMiscellaneousItemsLimit(contractorsEquipment.getMiscellaneousItemsLimit());
            setMiscellaneousItemsDeductible(contractorsEquipment.getMiscellaneousItemsDeductible());
        }

        if (contractorsEquipment.isToolsAndClothing()) {
            checkToolsAndClothing(true);
            setToolsAndClothingLimit(contractorsEquipment.getToolsAndClothingLimit());
            setToolsAndClothingDeductible(contractorsEquipment.getToolsAndClothingDeductible());
        }

        // SCHEDULED ITEMS

        clickScheduledItemsTab();

        if (!contractorsEquipment.getScheduledItems().isEmpty()) {

            for (CPPInlandMarineContractorsEquipment_ScheduledItem item : contractorsEquipment.getScheduledItems()) {

                clickAddScheduledItem();
                setScheduledItemEquipmentType(item.getEquipmentType());
                setScheduledItemLimit(item.getLimit());
                setScheduledItemRentedToOthers(item.isRentedToOthers());
                setScheduledItemRentedFromOthers(item.isRentedFromOthers());

            }

        }

        // EXCLUSIONS & CONDITIONS

        clickExclusionsAndConditionsTab();

        // ADDITIONAL INTEREST

        clickAdditionalInterestTab();

        // UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.ContractorsEquipment_IH_00_68), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
