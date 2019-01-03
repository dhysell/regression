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
import repository.gw.enums.InlandMarineCPP.ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPInlandMarineValuablePapers;
import repository.gw.helpers.TableUtils;


public class GenericWorkorderInlandMarineValuablePapersCPP extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderInlandMarineValuablePapersCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(css = "span[id$=':IMPartScreen:CoveragesCardTab-btnEl']")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:ExclusionsTab-btnInnerEl']")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:uwQuestionsTab-btnInnerEl']")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(driver);
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    @FindBy(xpath = "//div[contains(text(), 'Valuable Papers � At Your Premises')]/preceding-sibling::table//input")
    private WebElement checkbox_ValuablePapersAtYourPremises;

    private Guidewire8Checkbox checkbox_ValuablePapersAtYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Valuable Papers � At Your Premises')]/preceding-sibling::table");
    }

    private boolean checkValuablePapersAtYourPremises(Boolean checked) {
        if (!checkbox_ValuablePapersAtYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_ValuablePapersAtYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Valuable Papers � Away From Your Premises')]/preceding-sibling::table//input")
    private WebElement checkbox_ValuablePapersAwayFromYourPremises;

    private Guidewire8Checkbox checkbox_ValuablePapersAwayFromYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Valuable Papers � Away From Your Premises')]/preceding-sibling::table");
    }

    private boolean checkValuablePapersAwayFromYourPremises(Boolean checked) {
        if (!checkbox_ValuablePapersAwayFromYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_ValuablePapersAwayFromYourPremises);
        }
        
        return checked;
    }

    @FindBy(css = "span[id$=':BALiabGroupInputSet:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add-btnEl']")
    private WebElement button_AddLocation;

    private void addLocationAtYourPremises() {
        clickWhenClickable(button_AddLocation);
        
    }

    @FindBy(xpath = "//div[contains(text(),'Valuable Papers � At Your Premises')]/ancestor::fieldset//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:ScheduleInputSet:')]/div/div/parent::div/parent::div")
    private WebElement div_ValuablePapersAtYourPremisesTable;

    private void selectValuablePapersAtYourPremisesLocation(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_ValuablePapersAtYourPremisesTable, rowIndex, "Location", property.getAddress().getLine1() + " " + property.getAddress().getCity() + " " + property.getAddress().getState().getName());
    }

    private void selectValuablePapersAtYourPremisesProperty(CPPCommercialPropertyProperty property, int rowIndex) {
        tableUtils.selectValueForSelectInTableDoubleClick(div_ValuablePapersAtYourPremisesTable, rowIndex, "Property", "Building #" + property.getCPPCommercialProperty_Building_List().get(0).getNumber());
    }

    private void setValuablePapersAtYourPremisesLimit(int limit, int rowIndex) {
        tableUtils.setValueForCellInsideTable(div_ValuablePapersAtYourPremisesTable, rowIndex, "Limit", "Limit", "" + limit + Keys.TAB);
    }

    @FindBy(css = "input[id$=':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl']")
    private WebElement input_ValuablePapersAwayFromYourPremisesLimit;

    private void setValuablePapersAwayFromYourPremisesLimit(String value) {
        input_ValuablePapersAwayFromYourPremisesLimit.click();
        input_ValuablePapersAwayFromYourPremisesLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ////////////////////////////
    // UNDERWRITING QUESITONS //
    ////////////////////////////

    @FindBy(xpath = "//div[contains(text(), 'Does applicant/insured rent or lend books in a library operation?')]/parent::td/parent::tr")
    private WebElement tableRow_libraryOperationQuestion;

    private void setLibraries(Boolean answer) {
        if (answer) {
            WebElement radioYes = tableRow_libraryOperationQuestion.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'Yes')]/parent::td/input"));
            clickAndHoldAndRelease(radioYes);
        } else {
            WebElement radioNo = tableRow_libraryOperationQuestion.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'No')]/parent::td/input"));
            clickAndHoldAndRelease(radioNo);
        }
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutValuablePapers(GeneratePolicy policy) {

        CPPInlandMarineValuablePapers valuablePapers = policy.inlandMarineCPP.getValuablePapers();

        //COVERAGES
        clickCoveragesTab();

        setDeductible(valuablePapers.getDeductible());
        if (valuablePapers.isValuablePapersAtYourPremises()) {
            checkValuablePapersAtYourPremises(true);

            addLocationAtYourPremises();
            selectValuablePapersAtYourPremisesLocation(valuablePapers.getProperty(), 1);
            selectValuablePapersAtYourPremisesProperty(valuablePapers.getProperty(), 1);
            setValuablePapersAtYourPremisesLimit(valuablePapers.getAtYourPremisesLimit(), 1);

        }
        if (valuablePapers.isValuablePapersAwayFromYourPremises()) {
            checkValuablePapersAwayFromYourPremises(true);

            setValuablePapersAwayFromYourPremisesLimit("" + valuablePapers.getAwayFromYourPremisesLimit());

        }

        //EXCLUSIONS AND CONDITIONS
        clickExclusionsAndConditionsTab();

        //UNDERWRITING QUESTIONS
        clickUnderwritingQuestionsTab();

        //This is temporary until UW Questions for IM are hooked up
        setLibraries(valuablePapers.isLibrariesCM_67_02());

    }

}
