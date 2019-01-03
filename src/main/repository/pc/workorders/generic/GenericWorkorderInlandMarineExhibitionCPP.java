package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Coinsurance;
import repository.gw.enums.InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Deductible;
import repository.gw.enums.InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_HazardousCategory;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlaneMarineExhibition;

public class GenericWorkorderInlandMarineExhibitionCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderInlandMarineExhibitionCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':ExhibitionPartPanelSet:coveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ExhibitionPartPanelSet:scheduledItemsTab-btnInnerEl')]")
    private WebElement link_ScheduledItemsTab;

    private void clickScheduledItemsTab() {
        clickWhenClickable(link_ScheduledItemsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ExhibitionPartPanelSet:exlusionsAndConditionsTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':ExhibitionPartPanelSet:addtInterestTab-btnInnerEl')]")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    @FindBy(xpath = "//input[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:limitInput-inputEl')]")
    private WebElement input_BlanketLimit;

    private void setBlanketLimit(String limit) {
        input_BlanketLimit.click();
        input_BlanketLimit.sendKeys(limit + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:deductibleInput-triggerWrap')]");
    }

    private void setDeductible(ExhibitionCoverageForm_IH_00_92_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:coinsuranceInput-triggerWrap')]");
    }

    private void setCoinsurance(ExhibitionCoverageForm_IH_00_92_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    @FindBy(xpath = "//span[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:Add-btnInnerEl')]")
    private WebElement button_AddLocationOfExhibition;

    private void clickAddExhibitionLocation() {
        clickWhenClickable(button_AddLocationOfExhibition);
        
    }

    @FindBy(xpath = "//input[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:city-inputEl')]")
    private WebElement input_ExhibitionCity;

    private void setExhibitionCity(String city) {
        input_ExhibitionCity.click();
        input_ExhibitionCity.sendKeys(city + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//input[contains(@id, ':ExhibitionPartPanelSet:IMExhibitionCoverages:state-inputEl')]")
    private WebElement input_ExhibitionState;

    private void setExhibitionState(String state) {
        input_ExhibitionState.click();
        input_ExhibitionState.sendKeys(state + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_HazardousCategory() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:OptionTermInput-triggerWrap')]");
    }

    private void setHazardousCategory(ExhibitionCoverageForm_IH_00_92_HazardousCategory category) {
        Guidewire8Select mySelect = select_HazardousCategory();
        mySelect.selectByVisibleText(category.getValue());
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutExhibition(GeneratePolicy policy) {

        // COVERAGES

        clickCoveragesTab();

        CPPInlaneMarineExhibition exhibition = policy.inlandMarineCPP.getExhibition();

        setBlanketLimit(exhibition.getBlanketLimit());
        setDeductible(exhibition.getDeductible());
        setCoinsurance(exhibition.getCoinsurance());

        clickAddExhibitionLocation();

        setExhibitionCity(exhibition.getCity());
        setExhibitionState(exhibition.getState());

        setHazardousCategory(exhibition.getHazardousCategory());

        // SCHEDULED ITEMS

        clickScheduledItemsTab();

        // EXCLUSIONS & CONDITIONS

        clickExclusionsAndConditionsTab();

        // ADDITIONAL INTEREST

        clickAdditionalInterestTab();

    }

}
