package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.BaileesCustomersCoverageForm_IH_00_85_Deductible;
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.BaileesCustomer;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPInlandMarineBaileesCustomers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;


public class GenericWorkorderInlandMarineBaileesCustomersCPP extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderInlandMarineBaileesCustomersCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMBaileesCustomersDetailCV:CoveragesCovTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMBaileesCustomersDetailCV:CPBuildingExclCondCardPropertyTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMBaileesCustomersDetailCV:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setDeductible(BaileesCustomersCoverageForm_IH_00_85_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    //PROPERTY AT YOUR PREMISES//

    @FindBy(xpath = "//div[contains(text(),'Property At Your Premises')]/ancestor::fieldset//span[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_AddPropertyAtYourPremises;

    private void clickAddPropertyAtYourPremises() {
        clickWhenClickable(button_AddPropertyAtYourPremises);
        
    }

    @FindBy(xpath = "//div[contains(text(),'Property At Your Premises')]/ancestor::fieldset//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]/div/div/parent::div/parent::div")
    private WebElement div_PropertyAtYourPremisesTable;

    private void selectPropertyAtYourPremisesLocation(BaileesCustomer customer, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_PropertyAtYourPremisesTable, rowIndex, "Location", customer.getProperty().getAddress().getDropdownAddressFormat());
    }

    private void selectPropertyAtYourPremisesProperty(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_PropertyAtYourPremisesTable, rowIndex, "Property", "Property " + property.getPropertyLocationNumber() + ":");
    }

    private void setPropertyAtYourPremisesLimit(int limit, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAtYourPremisesTable, rowIndex, "Limit", "Limit", "" + limit + Keys.TAB);
    }

    //PROPERTY IN STORAGE AT YOUR PREMISES//

    @FindBy(xpath = "//div[contains(text(), 'Property In Storage At Your Premises')]/preceding-sibling::table//input")
    private WebElement checkbox_PropertyInStorageAtYourPremises;

    private Guidewire8Checkbox checkbox_PropertyInStorageAtYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property In Storage At Your Premises')]/preceding-sibling::table");
    }

    private boolean checkPropertyInStorageAtYourPremises(Boolean checked) {
        if (!checkbox_PropertyInStorageAtYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_PropertyInStorageAtYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(),'Property In Storage At Your Premises')]/ancestor::fieldset//span[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_AddPropertyInStorageAtYourPremises;

    private void clickAddPropertyInStorageAtYourPremises() {
        clickWhenClickable(button_AddPropertyInStorageAtYourPremises);
        
    }

    @FindBy(xpath = "//div[contains(text(),'Property In Storage At Your Premises')]/ancestor::fieldset//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]/div/div/parent::div/parent::div")
    private WebElement div_PropertyInStorageAtYourPremisesTable;

    private void selectPropertyInStorageAtYourPremisesLocation(BaileesCustomer customer, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_PropertyInStorageAtYourPremisesTable, rowIndex, "Location", customer.getProperty().getAddress().getDropdownAddressFormat());
    }

    private void selectPropertyInStorageAtYourPremisesProperty(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_PropertyInStorageAtYourPremisesTable, rowIndex, "Property", "Property " + property.getPropertyLocationNumber() + ":");
    }

    private void setPropertyInStorageAtYourPremisesLimit(int limit, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyInStorageAtYourPremisesTable, rowIndex, "Limit", "Limit", "" + limit + Keys.TAB);
    }

    //PROPERTY AWAY FROM YOUR PREMISES//

    @FindBy(xpath = "//div[contains(text(), 'Property Away From Your Premises')]/preceding-sibling::table//input")
    private WebElement checkbox_PropertyAwayFromYourPremises;

    private Guidewire8Checkbox checkbox_PropertyAwayFromYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property Away From Your Premises')]/preceding-sibling::table");
    }

    private boolean checkPropertyAwayFromYourPremises(Boolean checked) {
        if (!checkbox_PropertyAwayFromYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_PropertyAwayFromYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(),'Property Away From Your Premises')]/ancestor::fieldset//span[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_AddPropertyAwayFromYourPremises;

    private void clickAddPropertyAwayFromYourPremises() {
        clickWhenClickable(button_AddPropertyAwayFromYourPremises);
        
    }

    @FindBy(xpath = "//div[contains(text(),'Property Away From Your Premises')]/ancestor::fieldset//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]/div/div/parent::div/parent::div")
    private WebElement div_PropertyAwayFromYourPremisesTable;

    private void setPropertyAwayFromYourPremisesStreetAddress(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAwayFromYourPremisesTable, rowIndex, "Street Address", "StreetAddress", property.getAddress().getLine1() + Keys.TAB);
    }

    private void setPropertyAwayFromYourPremisesCity(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAwayFromYourPremisesTable, rowIndex, "City", "City", property.getAddress().getCity() + Keys.TAB);
    }

    private void setPropertyAwayFromYourPremisesState(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAwayFromYourPremisesTable, rowIndex, "State", "State", property.getAddress().getState().getName() + Keys.TAB);
    }

    private void setPropertyAwayFromYourPremisesZipCode(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAwayFromYourPremisesTable, rowIndex, "Zip Code", "ZipCode", property.getAddress().getZip() + Keys.TAB);
    }

    private void setPropertyAwayFromYourPremisesLimit(int limit, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_PropertyAwayFromYourPremisesTable, rowIndex, "Limit", "Limit", "" + limit + Keys.TAB);
    }

    ///////////////////////////
    //Exclusions & Conditions//
    ///////////////////////////

    @FindBy(xpath = "//div[contains(text(), 'Water Exclusion IH 99 18')]/preceding-sibling::table//input")
    private WebElement checkbox_WaterExlcusion;

    private Guidewire8Checkbox checkbox_WaterExlcusion() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Water Exclusion IH 99 18')]/preceding-sibling::table");
    }

    private boolean checkWaterExclusion(Boolean checked) {
        if (!checkbox_WaterExlcusion().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_WaterExlcusion);
        }
        
        return checked;
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutBaileesCustomers(GeneratePolicy policy) {

        //COVERAGES

        clickCoveragesTab();

        CPPInlandMarineBaileesCustomers baileesCustomers = policy.inlandMarineCPP.getBaileesCustomers();
        CPPCommercialPropertyProperty property = policy.commercialPropertyCPP.getCommercialPropertyList().get(0);

        setDeductible(baileesCustomers.getDeductible());

        //Property At Your Premises
        if (!baileesCustomers.getPropertyAtYourPremisesList().isEmpty()) {
            int customerCount = 1;
            for (BaileesCustomer customer : baileesCustomers.getPropertyAtYourPremisesList()) {
                clickAddPropertyAtYourPremises();
                selectPropertyAtYourPremisesLocation(customer, customerCount);
                selectPropertyAtYourPremisesProperty(property, customerCount);
                setPropertyAtYourPremisesLimit(customer.getLimit(), customerCount);
                customerCount++;
            }
        }

        //Property In Storage At Your Premises
        if (!baileesCustomers.getPropertyInStorageAtYourPremisesList().isEmpty()) {
            int customerCount = 1;
            checkPropertyInStorageAtYourPremises(true);
            for (BaileesCustomer customer : baileesCustomers.getPropertyInStorageAtYourPremisesList()) {
                clickAddPropertyInStorageAtYourPremises();
                selectPropertyInStorageAtYourPremisesLocation(customer, customerCount);
                selectPropertyInStorageAtYourPremisesProperty(property, customerCount);
                setPropertyInStorageAtYourPremisesLimit(customer.getLimit(), customerCount);
                customerCount++;
            }
        }

        //Property Away From Your Premises
        if (!baileesCustomers.getPropertyAwayFromYourPremisesList().isEmpty()) {
            int customerCount = 1;
            checkPropertyAwayFromYourPremises(true);
            for (BaileesCustomer customer : baileesCustomers.getPropertyAwayFromYourPremisesList()) {
                clickAddPropertyAwayFromYourPremises();
                setPropertyAwayFromYourPremisesStreetAddress(property, customerCount);
                setPropertyAwayFromYourPremisesCity(property, customerCount);
                setPropertyAwayFromYourPremisesState(property, customerCount);
                setPropertyAwayFromYourPremisesZipCode(property, customerCount);
                setPropertyAwayFromYourPremisesLimit(customer.getLimit(), customerCount);
                customerCount++;
            }
        }

        //EXCLUSIONS AND CONDITIONS

        clickExclusionsAndConditionsTab();

        checkWaterExclusion(baileesCustomers.isWaterExclusion_IH_99_18());

        //UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.BaileesCustomers_IH_00_85), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
