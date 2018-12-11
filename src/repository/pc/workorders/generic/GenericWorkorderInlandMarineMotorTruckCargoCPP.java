package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlandMarineMotorTruckCargo;
import repository.gw.generate.custom.CPPInlandMarineMotorTruckCargo_ScheduledCargo;
import repository.gw.generate.custom.CPPInlandMarineUWQuestion;
import repository.gw.generate.custom.Vehicle;

import java.util.List;

public class GenericWorkorderInlandMarineMotorTruckCargoCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderInlandMarineMotorTruckCargoCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:MotorCoveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:ExclusionsAndConditionsCardTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMPartScreen:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private void clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:TypekeyTermInput-triggerWrap')]");
    }

    private void setDeductible(MotorTruckCargo_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    // Scheduled Cargo

    @FindBy(xpath = "//span[contains(@id, ':IMPartScreen:CPLocationsLV_tb:Add-btnInnerEl')]")
    private WebElement buttonAddScheduledCargo;

    private void clickAddScheduledCargo() {
        clickWhenClickable(buttonAddScheduledCargo);
        
    }

    private Guidewire8Select select_Vehicle() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMPartScreen:selectedVehicleINput-triggerWrap')]");
    }

    private void setVehicle(Vehicle vehicle) {
        Guidewire8Select mySelect = select_Vehicle();
        mySelect.selectByVisibleTextPartial(vehicle.getModelYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
        
    }

    private Guidewire8Select select_RadiusOfOperation() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMPartScreen:RadiusCardInput-triggerWrap')]");
    }

    private void setRadiusOfOperation(MotorTruckCargo_RadiusOfOperation radiusOfOperation) {
        Guidewire8Select mySelect = select_RadiusOfOperation();
        mySelect.selectByVisibleTextPartial(radiusOfOperation.getValue());
        
    }

    @FindBy(xpath = "//a[contains(@id, ':IMPartScreen:GroupClassifactionCardInput')]")
    private WebElement buttonSelectCargo;

    private void clickSelectCargo() {
        clickWhenClickable(buttonSelectCargo);
        
    }

    @FindBy(xpath = "//span[contains(@id, 'IMCargoListPopup:Update-btnInnerEl')]")
    private WebElement buttonConfirmCargoSelection;

    private void clickOkayCargoSelection() {
        clickWhenClickable(buttonConfirmCargoSelection);
        
    }

    @FindBy(xpath = "//input[contains(@id,':IMPartScreen:LmitCardInput-inputEl')]")
    private WebElement input_ScheduledCargoLimit;

    private void setScheduledCargoLimit(String value) {
        input_ScheduledCargoLimit.click();
        input_ScheduledCargoLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_Coverage() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMPartScreen:MOtorTruckCargo-triggerWrap')]");
    }

    private void setCoverage(MotorTruckCargo_Coverage coverage) {
        Guidewire8Select mySelect = select_Coverage();
        mySelect.selectByVisibleTextPartial(coverage.getValue());
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutMotorTruckCargo(GeneratePolicy policy) {

        // COVERAGES

        clickCoveragesTab();

        CPPInlandMarineMotorTruckCargo motorTruckCargo = policy.inlandMarineCPP.getMotorTruckCargo();

        setDeductible(motorTruckCargo.getDeductible());

        List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> scheduledCargoList = motorTruckCargo.getScheduledCargoList();

        if (!scheduledCargoList.isEmpty()) {
            for (CPPInlandMarineMotorTruckCargo_ScheduledCargo scheduledCargo : scheduledCargoList) {

                clickAddScheduledCargo();
                setVehicle(scheduledCargo.getVehicle());
                setRadiusOfOperation(scheduledCargo.getRadiusOfOperation());

                if (!scheduledCargo.getCargoList().isEmpty()) {
                    clickSelectCargo();
                    for (InlandMarine_Cargo cargo : scheduledCargo.getCargoList()) {
                        dragAndDrop(find(By.xpath("//div[contains(text(), '" + cargo.getValue() + "')]/parent::td/following-sibling::td/div/img")), 2, 0);
                    }
                    clickOkayCargoSelection();
                }

                setScheduledCargoLimit(scheduledCargo.getLimit());
                setCoverage(scheduledCargo.getCoverage());

            }
        }

        // EXCLUSIONS AND CONDITIONS

        clickExclusionsAndConditionsTab();

        // UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab();

        List<CPPInlandMarineUWQuestion> uwQuestions = policy.inlandMarineCPP.getUwQuestions();

        // Handle parent questions
        for (CPPInlandMarineUWQuestion question : uwQuestions) {
            if (question.getCoveragePart().equals(InlandMarineCoveragePart.MotorTruckCargo) && !question.isDependantQuestion()) {
                answerQuestion(question, true);
            }
        }

        // Handle child questions
        for (CPPInlandMarineUWQuestion question : uwQuestions) {
            if (question.getCoveragePart().equals(InlandMarineCoveragePart.MotorTruckCargo) && question.isDependantQuestion()) {
                answerQuestion(question, true);
            }
        }

    }

    ///////////////////////
    //  PRIVATE METHODS  //
    ///////////////////////

    //TODO move all these question answering utilities for underwriting questions to a single generic workorder

    private void answerQuestion(CPPInlandMarineUWQuestion question, boolean isCorrect) {

        //Don't answer a dependent question if it does not appear (Temporary fix) TODO modify this for negative testing
        if (question.isDependantQuestion() && finds(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr")).isEmpty()) {
            System.out.println("Question with text: '" + question.getQuestionText() + "' was NOT found");
        } else {
            switch (question.getFormatType()) {
                case BooleanRadio:
                    answerBooleanRadioQuestion(question, isCorrect);
                    break;
                case StringTextBox:
                    break;
                case ChoiceSelect:
                    break;
                case StringField:
                    break;
                case BooleanCheckbox:
                    break;
                case IntegerField:
                    break;
                case DateField:
                    break;
                default:
                    break;
            }
        }


    }

    private void answerBooleanRadioQuestion(CPPInlandMarineUWQuestion question, boolean isCorrect) {
        WebElement questionRow = find(By.xpath("//div[contains(text(), '" + question.getQuestionText() + "')]/parent::td/parent::tr"));

        WebElement radioYes = questionRow.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'Yes')]/parent::td/input"));
        WebElement radioNo = questionRow.findElement(By.xpath("./td/div/table/tbody/tr/td/label[contains(text(),'No')]/parent::td/input"));

        if (isCorrect) {
            if (question.getCorrectAnswer().equals("Yes")) {
                clickAndHoldAndRelease(radioYes);
            } else {
                clickAndHoldAndRelease(radioNo);
            }
        } else {
            if (question.getCorrectAnswer().equals("Yes")) {
                clickAndHoldAndRelease(radioNo);
            } else {
                clickAndHoldAndRelease(radioYes);
            }
        }

    }

}
