package repository.pc.workorders.generic;


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
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPInlandMarineAccountsReceivable;
import repository.gw.generate.custom.CPPInlandMarineAccountsReceivable_LocationBlanket;
import repository.gw.helpers.GuidewireHelpers;


public class GenericWorkorderInlandMarineAccountsReceivableCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderInlandMarineAccountsReceivableCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMARPanelSet:coveragesTabTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMARPanelSet:exclCondTabTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMARPanelSet:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_RiskClassification() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CovTermInputSet:TypekeyTermInput-triggerWrap')]");
    }

    private void setRiskClassification(AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk classification) {
        Guidewire8Select mySelect = select_RiskClassification();
        mySelect.selectByVisibleText(classification.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setCoinsurance(AccountsReceivableCoverageForm_CM_00_66_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    // Accounts Receivable - Away From Your Premises

    @FindBy(xpath = "//div[contains(text(), 'Accounts Receivable � Away From Your Premises')]/preceding-sibling::table//input")
    private WebElement checkbox_AwayFromYourPremises;

    private Guidewire8Checkbox checkbox_AwayFromYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Accounts Receivable � Away From Your Premises')]/preceding-sibling::table");
    }

    private boolean checkAwayFromYourPremises(Boolean checked) {
        if (!checkbox_AwayFromYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_AwayFromYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//input[contains(@id,':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_AwayFromYourPremisesLimit;

    private void setAwayFromYourPremisesLimit(String value) {
        input_AwayFromYourPremisesLimit.click();
        input_AwayFromYourPremisesLimit.sendKeys(value + Keys.TAB);
        
    }

    // Location Blanket Coverage

    @FindBy(xpath = "//span[contains(@id, ':IMPartScreen:IMARPanelSet:IMARScheduledItems:Add-btnInnerEl')]")
    private WebElement button_AddLocationBlanketCoverage;

    private void clickAddLocationBlanketCoverage() {
        clickWhenClickable(button_AddLocationBlanketCoverage);
        
    }

    private Guidewire8Select select_Location() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMARScheduledItems:location-triggerWrap')]");
    }

    private void setLocation(CPPCommercialPropertyProperty property) {
        Guidewire8Select mySelect = select_Location();
        mySelect.selectByVisibleTextPartial(property.getAddress().getDropdownAddressFormat());
        
    }

    private Guidewire8Select select_Property() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMARScheduledItems:property-triggerWrap')]");
    }

    private void setProperty(CPPCommercialProperty_Building building) {
        Guidewire8Select mySelect = select_Property();
        mySelect.selectByVisibleTextPartial("Property " + building.getNumber() + ":");
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMARPanelSet:IMARScheduledItems:limit-inputEl')]")
    private WebElement input_BlanketCoverageLimit;

    private void setBlanketCoverageLimit(String value) {
        input_BlanketCoverageLimit.click();
        input_BlanketCoverageLimit.sendKeys(value + Keys.TAB);
        
    }

    private Guidewire8Select select_ReceptacleType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMARScheduledItems:receptacle-triggerWrap')]");
    }

    private void setReceptacleType(RecepticalType receptacleType) {
        Guidewire8Select mySelect = select_ReceptacleType();
        mySelect.selectByVisibleText(receptacleType.getValue());
        
    }

    private Guidewire8Select select_PercentDuplicated() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMARScheduledItems:percentDuplicated-triggerWrap')]");
    }

    private void setPercentDuplicated(PercentDuplicated percent) {
        Guidewire8Select mySelect = select_PercentDuplicated();
        mySelect.selectByVisibleText(percent.getValue());
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutAccountsReceivable(GeneratePolicy policy) {

        CPPInlandMarineAccountsReceivable accountsReceivable = policy.inlandMarineCPP.getAccountsReceivable();

        //COVERAGES

        clickCoveragesTab();

        setRiskClassification(accountsReceivable.getClassificationOfRisk());
        setCoinsurance(accountsReceivable.getCoinsurance());

        if (accountsReceivable.isAccountsReceivable_AwayFromYourPremises()) {
            checkAwayFromYourPremises(true);
            setAwayFromYourPremisesLimit(accountsReceivable.getAccountsReceivable_AwayFromYourPremises_Limit());
        }

        // Location blanket coverage items
        if (!accountsReceivable.getLocationBlankets().isEmpty()) {
            for (CPPInlandMarineAccountsReceivable_LocationBlanket locationBlanket : accountsReceivable.getLocationBlankets()) {

                clickAddLocationBlanketCoverage();
                setLocation(locationBlanket.getProperty());
                setProperty(locationBlanket.getProperty().getCPPCommercialProperty_Building_List().get(0));

                setBlanketCoverageLimit(locationBlanket.getLimit());
                setReceptacleType(locationBlanket.getRecepticalType());
                setPercentDuplicated(locationBlanket.getPercentDuplicated());

            }
        }

        //EXCLUSIONS AND CONDITIONS

        clickExclusionsAndConditionsTab();

        //UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.AccountsReceivable_CM_00_66), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
