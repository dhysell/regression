package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Coinsurance;
import repository.gw.enums.InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Deductible;
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.*;
import repository.gw.helpers.GuidewireHelpers;

import java.util.List;


public class GenericWorkorderInlandMarineComputerSystemsCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineComputerSystemsCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':compSystemsPartPanelSet:IMCompSystemsPartPanelSet:coveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':compSystemsPartPanelSet:IMCompSystemsPartPanelSet:scheduleArticlesTab-btnInnerEl')]")
    private WebElement link_ScheduledItemsTab;

    private void clickPortablComputersTab() {
        clickWhenClickable(link_ScheduledItemsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':compSystemsPartPanelSet:IMCompSystemsPartPanelSet:exclCondTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':compSystemsPartPanelSet:IMCompSystemsPartPanelSet:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table[contains(@id, ':CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setDeductible(ComputerSystemsCoverageForm_IH_00_75_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Coinsurance')]/parent::td/following-sibling::td/table[contains(@id, ':CovTermInputSet:OptionTermInput-triggerWrap')]");
    }

    private void setCoinsurance(ComputerSystemsCoverageForm_IH_00_75_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    //This contains index 0: yes, and index 1: no
    @FindBy(xpath = "//label[contains(text(),'Has applicant/insured purchased a commercial grade anti-virus program?')]/parent::td/parent::tr//input")
    private List<WebElement> radioButtons_purchasedCommercialGradeAntivirus;

    private void setPurchasedCommercialGradeAntivirus(boolean isPurchased) {

        if (isPurchased) {
            clickAndHoldAndRelease(radioButtons_purchasedCommercialGradeAntivirus.get(0));
        } else {
            clickAndHoldAndRelease(radioButtons_purchasedCommercialGradeAntivirus.get(1));
        }
        

    }

    //VIRUS HARMFUL CODE OR SIMILAR

    @FindBy(xpath = "//div[contains(text(), 'Virus, Harmful Code Or Similar Instruction')]/preceding-sibling::table//input")
    private WebElement checkbox_VirusHarmfulCodeOrSimilar;

    private Guidewire8Checkbox checkbox_VirusHarmfulCodeOrSimilar() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Virus, Harmful Code Or Similar Instruction')]/preceding-sibling::table");
    }

    private boolean checkVirusHarmfulCodeOrSimilar(Boolean checked) {
        if (!checkbox_VirusHarmfulCodeOrSimilar().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_VirusHarmfulCodeOrSimilar);
        }
        
        return checked;
    }

    @FindBy(xpath = "//input[contains(@id,':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_VirusHarmfulCodeOrSimilarLimit;

    private void setVirusHarmfulCodeOrSimilarLimit(String value) {
        input_VirusHarmfulCodeOrSimilarLimit.click();
        input_VirusHarmfulCodeOrSimilarLimit.clear();
        input_VirusHarmfulCodeOrSimilarLimit.sendKeys(value + Keys.TAB);
        
    }

    //LOCATION BLANKET COVERAGES

    @FindBy(xpath = "//span[contains(@id, ':IMCompSystemsPartPanelSet:IMCompSystemsLocations:Add-btnEl')]")
    private WebElement button_AddLocationBlanket;

    private void clickAddLocationBlanket() {
        clickWhenClickable(button_AddLocationBlanket);
        
    }

    private Guidewire8Select select_Location() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Location')]/parent::td/following-sibling::td/table");
    }

    private void setLocation(CPPCommercialPropertyProperty property) {
        Guidewire8Select mySelect = select_Location();
        mySelect.selectByVisibleTextPartial(property.getAddress().getLine1() + " " + property.getAddress().getCity() + " " + property.getAddress().getState().getName());
        
    }

    private Guidewire8Select select_Property() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Property')]/parent::td/following-sibling::td/table");
    }

    private void setProperty(CPPCommercialProperty_Building building) {
        Guidewire8Select mySelect = select_Property();
        mySelect.selectByVisibleTextPartial("Property " + building.getNumber() + ":");
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMCompSystemsLocations:equipLimit-inputEl')]")
    private WebElement input_ComputerEquipmentLimit;

    private void setComputerEquipmentLimit(String value) {
        input_ComputerEquipmentLimit.click();
        input_ComputerEquipmentLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMCompSystemsLocations:dataLimit-inputEl')]")
    private WebElement input_MediaAndDataLimit;

    private void setMediaAndDataLimit(String value) {
        input_MediaAndDataLimit.click();
        input_MediaAndDataLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ////////////////////////
    // PORTABLE COMPUTERS //
    ////////////////////////

    @FindBy(xpath = "//span[contains(@id, ':IMCompSystemsPartPanelSet:IMCompSystemsComputers:Add-btnEl')]")
    private WebElement button_AddPortableComputer;

    private void clickAddPortableComputer() {
        clickWhenClickable(button_AddPortableComputer);
        
    }

    @FindBy(xpath = "//label[contains(text(),'Model Year')]/parent::td/following-sibling::td/input")
    private WebElement input_ModelYear;

    private void setModelYear(String value) {
        input_ModelYear.click();
        input_ModelYear.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Make')]/parent::td/following-sibling::td/input")
    private WebElement input_Make;

    private void setMake(String value) {
        input_Make.click();
        input_Make.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[text()='Model']/parent::td/following-sibling::td/input")
    private WebElement input_Model;

    private void setModel(String value) {
        input_Model.click();
        input_Model.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Equipment ID')]/parent::td/following-sibling::td/input")
    private WebElement input_EquipmentID;

    private void setEquipmentID(String value) {
        input_EquipmentID.click();
        input_EquipmentID.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Limit')]/parent::td/following-sibling::td/input")
    private WebElement input_PortableComputerLimit;

    private void setPortableComputerLimit(String value) {
        input_PortableComputerLimit.click();
        input_PortableComputerLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutComputerSystems(GeneratePolicy policy) {

        CPPInlandMarineComputerSystems compSystems = policy.inlandMarineCPP.getComputerSystems();

        //COVERAGES
        clickCoveragesTab();

        setDeductible(compSystems.getDeductible());
        setCoinsurance(compSystems.getCoinsurance());
        setPurchasedCommercialGradeAntivirus(compSystems.isPurchasedCommercialGradeAntiVirus());

        if (compSystems.isVirusHarmfulCodeOrSimilar()) {
            checkVirusHarmfulCodeOrSimilar(true);
            setVirusHarmfulCodeOrSimilarLimit("" + compSystems.getVirusHarmfulCodeOrSimilarLimit());
        }

        for (CPPInlandMarineComputerSystems_LocationBlanket locationBlanket : compSystems.getLocationBlankets()) {
            clickAddLocationBlanket();
            setLocation(locationBlanket.getLocationProperty());
            setProperty(locationBlanket.getLocationProperty().getCPPCommercialProperty_Building_List().get(0));
            setComputerEquipmentLimit("" + locationBlanket.getComputerEquipmentLimit());
            setMediaAndDataLimit("" + locationBlanket.getMediaAndDataLimit());
        }

        //PORTABLE COMPUTERS
        clickPortablComputersTab();

        for (CPPInlandMarineComputerSystems_PortableComputer portableComputer : compSystems.getPortableComputers()) {
            clickAddPortableComputer();
            setModelYear(portableComputer.getModelYear());
            setMake(portableComputer.getMake());
            setModel(portableComputer.getModel());
            setEquipmentID(portableComputer.getEquipmentID());
            setPortableComputerLimit("" + portableComputer.getLimit());
        }

        //EXCLUSIONS & CONDITIONS
        clickExclusionsAndConditionsTab();

        //UNDERWRITING QUESTIONS
        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.ComputerSystems_IH_00_75), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
