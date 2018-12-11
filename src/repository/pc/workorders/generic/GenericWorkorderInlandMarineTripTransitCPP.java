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
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.enums.InlandMarineCPP.InlandMarine_Cargo;
import repository.gw.enums.InlandMarineCPP.TripTransitCoverageForm_IH_00_78_Deductible;
import repository.gw.enums.InlandMarineCPP.TripTransitCoverageForm_IH_00_78_DistanceInMiles;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPInlandMarineTripTransit;
import repository.gw.generate.custom.CPPInlandMarineTripTransit_Trip;
import repository.gw.helpers.GuidewireHelpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class GenericWorkorderInlandMarineTripTransitCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderInlandMarineTripTransitCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(css = "span[id$=':IMPartScreen:miscArticlesPartPanelSet:TripTransitPartPanelSet:coveragesTab-btnEl']")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:miscArticlesPartPanelSet:TripTransitPartPanelSet:exclCondTab-btnEl']")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:miscArticlesPartPanelSet:TripTransitPartPanelSet:addtInterestTab-btnEl']")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:miscArticlesPartPanelSet:TripTransitPartPanelSet:uwQuestionsTab-btnEl']")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    //TRIP LOCATIONS

    @FindBy(css = "span[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:Add-btnEl']")
    private WebElement button_NewLocation;

    private void clickNewLocation() {
        clickWhenClickable(button_NewLocation);
        
    }

    @FindBy(css = "textarea[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:fromAddress-inputEl']")
    private WebElement textarea_FromStreetAddress;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:fromCity-inputEl']")
    private WebElement input_FromCity;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:fromState-inputEl']")
    private WebElement input_FromState;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:fromZipCode-inputEl']")
    private WebElement input_FromZipCode;

    private void setFromAddressInfo(AddressInfo address) {
        textarea_FromStreetAddress.clear();
        textarea_FromStreetAddress.sendKeys(address.getLine1() + Keys.TAB);
        waitForPostBack();
        
        input_FromCity.clear();
        input_FromCity.sendKeys(address.getCity() + Keys.TAB);
        waitForPostBack();
        
        input_FromState.clear();
        input_FromState.sendKeys(address.getState().getName() + Keys.TAB);
        waitForPostBack();
        
        input_FromZipCode.clear();
        input_FromZipCode.sendKeys(address.getZip() + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(css = "textarea[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:toAddress-inputEl']")
    private WebElement textarea_ToStreetAddress;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:toCity-inputEl']")
    private WebElement input_ToCity;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:toState-inputEl']")
    private WebElement input_ToState;

    @FindBy(css = "input[id$=':TripTransitPartPanelSet:TripTransitDestinationPanelSet:toZipCode-inputEl']")
    private WebElement input_ToZipCode;

    private void setToAddressInfo(AddressInfo address) {
        textarea_ToStreetAddress.clear();
        textarea_ToStreetAddress.sendKeys(address.getLine1() + Keys.TAB);
        waitForPostBack();
        
        input_ToCity.clear();
        input_ToCity.sendKeys(address.getCity() + Keys.TAB);
        waitForPostBack();
        
        input_ToState.clear();
        input_ToState.sendKeys(address.getState().getName() + Keys.TAB);
        waitForPostBack();
        
        input_ToZipCode.clear();
        input_ToZipCode.sendKeys(address.getZip() + Keys.TAB);
        waitForPostBack();
        
    }

    //COVERAGE VALUES

    @FindBy(css = "input[id$=':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl']")
    private WebElement input_Limit;

    private void setLimit(String value) {
        input_Limit.clear();
        input_Limit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(TripTransitCoverageForm_IH_00_78_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_DistanceInMiles() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Distance in miles')]/parent::td/following-sibling::td/table");
    }

    private void setDistanceInMiles(TripTransitCoverageForm_IH_00_78_DistanceInMiles distance) {
        Guidewire8Select mySelect = select_DistanceInMiles();
        mySelect.selectByVisibleText(distance.getValue());
        
    }

    @FindBy(xpath = "//a[contains(text(),'Select Property')]")
    private WebElement button_SelectProperty;

    private void clickSelectProperty() {
        clickWhenClickable(button_SelectProperty);
        
    }

    @FindBy(xpath = "//span[contains(@id, 'IMPropertyListPopup:Update-btnEl')]")
    private WebElement button_ConfirmCargoSelection;

    private void clickOkayCargoSelection() {
        clickWhenClickable(button_ConfirmCargoSelection);
        
    }

    @FindBy(css = "input[id$=':CoverageInputSet:CovPatternInputGroup:covStartsAtTermInput-inputEl']")
    private WebElement input_CoverageBeginsDate;

    private void setCoverageBeginsDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dateString = date.format(formatter);
        input_CoverageBeginsDate.clear();
        input_CoverageBeginsDate.sendKeys(dateString + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(css = "input[id$=':CoverageInputSet:CovPatternInputGroup:covEndsAtTermInput-inputEl']")
    private WebElement input_CoverageEndsDate;

    private void setCoverageEndsDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dateString = date.format(formatter);
        input_CoverageEndsDate.clear();
        input_CoverageEndsDate.sendKeys(dateString + Keys.TAB);
        waitForPostBack();
        
    }

    private boolean checkModeOfTransportation(boolean toCheck, String mode) {
        WebElement checkbox = find(By.xpath("//label[contains(text(),'" + mode + "')]/parent::td/following-sibling::td/div/input"));
        Guidewire8Checkbox gwCheckbox = new Guidewire8Checkbox(driver,"//label[contains(text(),'" + mode + "')]/parent::td/following-sibling::td/div/input/ancestor::table[1]");

        if (!gwCheckbox.isSelected() && toCheck) {
            clickAndHoldAndRelease(checkbox);
        }
        
        return toCheck;

    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void filloutTripTransit(GeneratePolicy policy) {

        CPPInlandMarineTripTransit tripTransit = policy.inlandMarineCPP.getTripTransit();

        //COVERAGES
        clickCoveragesTab();

        for (CPPInlandMarineTripTransit_Trip trip : tripTransit.getTrips()) {
            clickNewLocation();
            setFromAddressInfo(trip.getFromAddress());
            setToAddressInfo(trip.getToAddress());
        }

        setLimit("" + tripTransit.getLimit());
        setDeductible(tripTransit.getDeductible());
        setDistanceInMiles(tripTransit.getDistanceInMiles());

        //Select covered property
        if (!tripTransit.getCargoList().isEmpty()) {
            clickSelectProperty();
            for (InlandMarine_Cargo cargo : tripTransit.getCargoList()) {
                dragAndDrop(find(By.xpath("//div[contains(text(), '" + cargo.getValue() + "')]/parent::td/following-sibling::td/div/img")), 2, 0);
                
            }
            clickOkayCargoSelection();
            
        }

        setCoverageBeginsDate(tripTransit.getCoverageBegins());
        setCoverageEndsDate(tripTransit.getCoverageEnds());

        if (tripTransit.isContractCarrier()) {
            checkModeOfTransportation(true, "Contract Carrier");
        }
        if (tripTransit.isByMessanger()) {
            checkModeOfTransportation(true, "By Messenger");
        }
        if (tripTransit.isByRailRoad()) {
            checkModeOfTransportation(true, "By Railroad");
        }
        if (tripTransit.isByAirCarrier()) {
            checkModeOfTransportation(true, "By Air Carrier");
        }
        if (tripTransit.isByYourVehicle()) {
            checkModeOfTransportation(true, "By Your Vehicle");
        }

        //EXCLUSIONS AND CONDITIONS
        clickExclusionsAndConditionsTab();

        //ADDITIONAL INTEREST
        clickAdditionalInterestTab();

        //UNDERWRITING QUESTIONS
        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.TripTransit_IH_00_78), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
