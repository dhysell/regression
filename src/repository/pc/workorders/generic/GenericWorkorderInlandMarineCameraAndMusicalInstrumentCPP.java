package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance;
import repository.gw.enums.InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Deductible;
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers;
import repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket;
import repository.gw.helpers.GuidewireHelpers;


public class GenericWorkorderInlandMarineCameraAndMusicalInstrumentCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineCameraAndMusicalInstrumentCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMCamMusDlrsPartPanelSet:coveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMCamMusDlrsPartPanelSet:exclCondTabTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMCamMusDlrsPartPanelSet:addlInterestTabTab-btnInnerEl')]")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:IMCamMusDlrsPartPanelSet:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(CameraAndMusicalInstrumentDealers_CM_00_21_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Coinsurance')]/parent::td/following-sibling::td/table");
    }

    private void setCoinsurance(CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    //PROPERTY AWAY FROM YOUR PREMISES//

    @FindBy(xpath = "//div[contains(text(), 'Property away from your premises in the care, custody or control of you or your employees')]/preceding-sibling::table//input")
    private WebElement checkbox_PropertyAwayFromYourPremises;

    private Guidewire8Checkbox checkbox_PropertyAwayFromYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property away from your premises in the care, custody or control of you or your employees')]/preceding-sibling::table");
    }

    private boolean checkPropertyAwayFromYourPremises(Boolean checked) {
        if (!checkbox_PropertyAwayFromYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_PropertyAwayFromYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(),'Property away from your premises in the care')]/ancestor::fieldset//input[contains(@id,':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_PropertyAwayFromYourPremisesLimit;

    private void setPropertyAwayFromYourPremisesLimit(String value) {
        input_PropertyAwayFromYourPremisesLimit.click();
        input_PropertyAwayFromYourPremisesLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    //PROPERTY IN TRANSIT//

    @FindBy(xpath = "//div[contains(text(), 'Property in transit')]/preceding-sibling::table//input")
    private WebElement checkbox_PropertyInTransit;

    private Guidewire8Checkbox checkbox_PropertyInTransit() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property in transit')]/preceding-sibling::table");
    }

    private boolean checkPropertyInTransit(Boolean checked) {
        if (!checkbox_PropertyInTransit().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_PropertyInTransit);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(),'Property in transit')]/ancestor::fieldset//input[contains(@id,':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_PropertyInTransitLimit;

    private void setPropertyInTransitLimit(String value) {
        input_PropertyInTransitLimit.click();
        input_PropertyInTransitLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    //PROPERTY NOT AT YOUR PREMISES//

    @FindBy(xpath = "//div[contains(text(), 'Property not at your premises')]/preceding-sibling::table//input")
    private WebElement checkbox_PropertyNotAtYourPremises;

    private Guidewire8Checkbox checkbox_PropertyNotAtYourPremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property not at your premises')]/preceding-sibling::table");
    }

    private boolean checkPropertyNotAtYourPremises(Boolean checked) {
        if (!checkbox_PropertyNotAtYourPremises().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_PropertyNotAtYourPremises);
        }
        
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(),'Property not at your premises')]/ancestor::fieldset//input[contains(@id,':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_PropertyNotAtYourPremises;

    private void setPropertyNotAtYourPremisesLimit(String value) {
        input_PropertyNotAtYourPremises.click();
        input_PropertyNotAtYourPremises.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    //LOCATION BLANKETS//

    @FindBy(xpath = "//span[contains(@id,':IMCamMusDlrsPartPanelSet:CamScheduledEquipmentPanelSet:Add-btnEl')]")
    private WebElement button_AddLocationBlanketCoverage;

    private void clickAddLocationBlanket() {
        clickWhenClickable(button_AddLocationBlanketCoverage);
        
    }

    private Guidewire8Select select_BlanketLocation() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Location')]/parent::td/following-sibling::td/table");
    }

    private void setBlanketLocation(CPPCommercialPropertyProperty property) {
        Guidewire8Select mySelect = select_BlanketLocation();
        mySelect.selectByVisibleTextPartial("Location " + property.getPropertyLocationNumber() + ": " + property.getAddress().getLine1() + " " + property.getAddress().getCity() + " " + property.getAddress().getState().getName());
        
    }

    private Guidewire8Select select_BlanketProperty() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Property')]/parent::td/following-sibling::td/table");
    }

    private void setBlanketProperty(CPPCommercialProperty_Building building) {
        Guidewire8Select mySelect = select_BlanketProperty();
        mySelect.selectByVisibleTextPartial("Property " + building.getNumber() + ":");
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMCamMusDlrsPartPanelSet:CamScheduledEquipmentPanelSet:cameraLimit-inputEl')]")
    private WebElement input_CameraBlanketLimit;

    private void setCameraBlanketLimit(String value) {
        input_CameraBlanketLimit.click();
        input_CameraBlanketLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMCamMusDlrsPartPanelSet:CamScheduledEquipmentPanelSet:musicLimit-inputEl')]")
    private WebElement input_MusicalInstrumentsLimit;

    private void setMusicalInstrumentsLimit(String value) {
        input_MusicalInstrumentsLimit.click();
        input_MusicalInstrumentsLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutCameraAndMusicalInstruments(GeneratePolicy policy) {

        // COVERAGES

        clickCoveragesTab();

        CPPInlandMarineCameraAndMusicalInstrumentDealers cameraMusical = policy.inlandMarineCPP.getCameraAndMusicalInstrumentDealers();

        setDeductible(cameraMusical.getDeductible());
        setCoinsurance(cameraMusical.getCoinsurance());

        if (cameraMusical.isPropertyAwayFromYourPremisesInTheCare()) {
            checkPropertyAwayFromYourPremises(true);
            setPropertyAwayFromYourPremisesLimit("" + cameraMusical.getPropertyAwayFromPremisesLimit());
        }

        if (cameraMusical.isPropertyInTransit()) {
            checkPropertyInTransit(true);
            setPropertyInTransitLimit("" + cameraMusical.getPropertyAwayFromPremisesLimit());
        }

        if (cameraMusical.isPropertyNotAtYourPremisesAndNotIncludedAbove()) {
            checkPropertyNotAtYourPremises(true);
            setPropertyNotAtYourPremisesLimit("" + cameraMusical.getPropertyNotAtPremisesLimit());
        }

        for (CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket locationBlanket : cameraMusical.getLocationBlankets()) {
            clickAddLocationBlanket();

            setBlanketLocation(locationBlanket.getProperty());
            setBlanketProperty(locationBlanket.getProperty().getCPPCommercialProperty_Building_List().get(0));
            setCameraBlanketLimit("" + locationBlanket.getCamerasBlanketLimit());
            setMusicalInstrumentsLimit("" + locationBlanket.getMusicalInstrumentsBlanketLimit());

        }

        // EXCLUSIONS & CONDITIONS

        clickExclusionsAndConditionsTab();

        // ADDITIONAL INTEREST

        clickAdditionalInterestTab();

        // UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.CameraAndMusicalInstrumentDealers_CM_00_21), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
