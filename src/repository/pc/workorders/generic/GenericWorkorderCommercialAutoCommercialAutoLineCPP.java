package repository.pc.workorders.generic;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.CommercialAutoLine.*;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.HiredAutoState;
import repository.gw.generate.custom.NonOwnedLiabilityStates;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericWorkorderCommercialAutoCommercialAutoLineCPP extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderCommercialAutoCommercialAutoLineCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

//    private String radioElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes"
//                    + "']/preceding-sibling::input";
//        } else {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No"
//                    + "']/preceding-sibling::input";
//        }
//    }
//
//    private String checkElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes"
//                    + "']/parent::td[contains(@class, 'cb-checked')]";
//        } else {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No"
//                    + "']/parent::td[contains(@class, 'cb-checked')]";
//        }
//    }


    @FindBy(xpath = "//a[contains(@id, ':JobWizardToolbarButtonSet:BindOptions')]")
    public WebElement button_GenericWorkorderBindOptionsWrap;

    @FindBy(xpath = "//span[contains(@id, ':BALinePanelSet:CoveragesCardTab-btnEl')]")
    public WebElement link_CoveragesTab;

    @FindBy(xpath = "//span[contains(@id, ':BALinePanelSet:AdditionalCoveragesCardTab-btnEl')]")
    public WebElement link_AdditioanlCoveragesTab;

    @FindBy(xpath = "//span[contains(@id, ':BALinePanelSet:ExclusionsAndConditionsCardTab-btnEl')]")
    public WebElement link_ExclutionsAndConditionsTab;

    @FindBy(xpath = "//span[contains(@id, ':BALinePanelSet:AdditionalInsuredsCardTab-btnEl')]")
    public WebElement link_AdditionalInsuredsTab;

    @FindBy(xpath = "//span[contains(@id,'BALinePanelSet:UnderwritingQuestionsCardTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestions;


    // COMMERCIAL AUTO OWNED LIABILITY GROUP
    Guidewire8Select select_LiabilityLimit() {
        return new Guidewire8Select(driver, "//label[contains(text() , 'Liability Limit')]/parent::td/following-sibling::td[1]/table");
    }

    Guidewire8Select select_DeductibleLiabilityCoverageCA0301() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Deductible Liability Coverage CA 03 01')]/parent::td/following-sibling::td[1]/table");
    }

    Guidewire8Checkbox checkbox_MedicalPaymentsCA9903() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Medical Payments CA 99 03')]/preceding-sibling::table");
    }

    Guidewire8Select select_MedicalPaymentsLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Medical Payments CA 99 03')]/ancestor::legend/following-sibling::div/descendant::table[2]");
    }

    @FindBy(xpath = "//div[contains (@id, ':0:BALiabGroupInputSet:CoverageInputSet:CovPatternInputGroup:PackageTermInput-inputEl')]")
    private List<WebElement> text_LiabilityLimit;


    // COMMERCIAL AUTO HIRED AUTO GROUP
    Guidewire8Select select_HiredAutoLiabilityBI() {
        return new Guidewire8Select(driver, "//label[contains(text() , 'Liability BI')]/parent::td/following-sibling::td[1]/table");
    }

    Guidewire8Select select_HiredAutoCollisionDeductible() {
        return new Guidewire8Select(driver, "//label[contains(text() , 'Hired Auto Collision Deductible')]/parent::td/following-sibling::td[1]/table");
    }

    Guidewire8Checkbox checkbox_HiredAutoLiability() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Hired Auto Liability')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_HiredAutoCollision() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Hired Auto Collision')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_HiredAutoComprehensive() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Hired Auto Comprehensive')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//label[contains(text(), 'Hired Auto Comprehensive Deductible')]/parent::td/following-sibling::td/div")
    private WebElement textbox_HiredAutoCompDeductible;


    // COMMERCIAL AUTO NON-OWNED GROUP
    Guidewire8Select select_NonOwnedAutoLiabilityBI() {
        return new Guidewire8Select(driver, "//label[contains(text() , 'Liability Bodily Injury')]/parent::td/following-sibling::td[1]/table");
    }

    Guidewire8Checkbox checkbox_NonOwnedAutoLiability() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Non-Owned Auto Liability')]/preceding-sibling::table");
    }


    // HIRED AUTO STATES
    Guidewire8Select select_HireAutoState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':SelectStateHiredAuto-triggerWrap')]");
    }

    @FindBy(xpath = "//label[contains(text(), 'Hired Auto States')]/ancestor::table[1]/parent::div")
    private WebElement table_HiredAutoStates;

    @FindBy(xpath = "//span[contains(@id, ':BALineCoveragePanelSet:Add-btnEl')]")
    private WebElement button_AddHiredAutoState;


    //NON-OWNED LIABILITY STATES
    Guidewire8Select select_NonOwnedLiabilityState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':SelectStateNonowned-triggerWrap')]");
    }

    @FindBy(xpath = "//label[contains(text(), 'Non-owned Liability States')]/ancestor::table[1]/parent::div")
    private WebElement table_NonOwnedLiabilityStates;


    @FindBy(xpath = "//span[contains(@id, ':BALineCoveragePanelSet:AddNonownedState-btnEl')]")
    private WebElement button_NonOwnedLiabilityState;


    //ADDITIONAL COVERAGES

    //named driver
    @FindBy(xpath = "//div[contains(text(), 'Drive Other Car - Named Individuals')]/ancestor::legend/following-sibling::div")
    private WebElement table_OtherCarNamedIndividules;

    @FindBy(xpath = "//div[contains(text(), 'Drive Other Car - Named Individuals')]/ancestor::legend/following-sibling::div/span/div/child::table[1]/tbody/tr/child::td[1]/descendant::span[contains(@id, ':Remove-btnEl')]")
    private WebElement button_AddNamedIndividual;

    @FindBy(xpath = "//div[contains(text(), 'Drive Other Car - Named Individuals')]/ancestor::legend/following-sibling::div/span/div/child::table[1]/tbody/tr/child::td[1]/descendant::span[contains(@id, ':Add-btnInnerEl')]")
    private WebElement button_RemoveNamedIndividual;

    //Drive Other Car - Comprehensive CA 99 10
    Guidewire8Checkbox checkbox_OtherCarComprehensive() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Comprehensive')]/preceding-sibling::table");
    }

    Guidewire8Select select_OtherCarCompDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Drive Other Car - Comprehensive')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/table");
    }


    //Drive Other Car - Liability CA 99 10
    Guidewire8Checkbox checkbox_OtherCarLiability() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Liability')]/preceding-sibling::table");
    }

    //text box
    @FindBy(xpath = "//div[contains(text(), 'Drive Other Car - Liability')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/div")
    private WebElement table_OtherCarLiabilityLimit;


    //Drive Other Car - Medical Payments CA 99 10
    Guidewire8Checkbox checkbox_OtherCarMedicalPayment() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Medical Payments')]/preceding-sibling::table");
    }

    //textbox
    @FindBy(xpath = "//div[contains(text(), 'Drive Other Car - Medical Payments')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/div")
    private WebElement table_OtherCarMedicalPayments;


    //Drive Other Car - Underinsured Motorists CA 99 10
    Guidewire8Checkbox checkbox_OthercarUnderinsuredMotorist() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Underinsured Motorists')]/preceding-sibling::table");
    }

    Guidewire8Select select_OtherCarUnderinsuredMotoristLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Drive Other Car - Underinsured Motorists')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/table");
    }


    //Drive Other Car - Uninsured Motorists CA 99 10
    Guidewire8Checkbox checkbox_OtherCarUninsuredMotorist() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Uninsured Motorists')]/preceding-sibling::table");
    }

    Guidewire8Select select_OtherCarUninsuredMotoristLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Drive Other Car - Uninsured Motorists')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/table");
    }


    //Drive Other Car - Collision CA 99 10
    Guidewire8Checkbox checkbox_OtherCarCollision() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Drive Other Car - Collision')]/preceding-sibling::table");
    }

    Guidewire8Select select_OtherCarCollisionDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Drive Other Car - Collision')]/ancestor::legend/following-sibling::div/span/div/table/tbody/tr/child::td[2]/table");
    }


    //MCS-90
    Guidewire8Checkbox checkbox_MCS_90() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'MCS-90')]/preceding-sibling::table");
    }

    //CA 20 70
    @FindBy(xpath = "//div[contains(text(), 'Coverage For Certain Operations In Connection With Railroads CA 20 70')]/ancestor::legend/following-sibling::div")
    private WebElement table_CA_20_70;

    Guidewire8Checkbox checkbox_CA_20_70() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'CA 20 70')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//label[contains(text(), 'Hand rated premium')]/parent::td/following-sibling::td/input")
    private WebElement textbox_HandRatedPremium;

    @FindBy(xpath = "//div[contains(text(), 'Coverage For Certain Operations In Connection With Railroads CA 20 70')]/ancestor::legend/following-sibling::div/span/div/child::div/table/tbody/tr/child::td[1]/descendant::span[contains(@id, ':Add-btnInnerEl')]")
    private WebElement button_Add_CA_20_70_Details;

    @FindBy(xpath = "//div[contains(text(), 'Coverage For Certain Operations In Connection With Railroads CA 20 70')]/ancestor::legend/following-sibling::div/span/div/child::div/table/tbody/tr/child::td[1]/descendant::span[contains(@id, ':Remove-btnEl')]")
    private WebElement button_Remove_CA_20_70_Details;


    // UNDERWRITING QUESTIONS
    Guidewire8RadioButton radio_HasApplicantInsuredLeasedVehicleForMoreThanSixMonths() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Has applicant/insured leased a vehicle for more than 6 months')]/parent::td/following-sibling::td/child::div/table");
    }

    Guidewire8RadioButton radio_DoesApplicantInsuredRequireLimitsInExcessOfOneMillion() {
        return new Guidewire8RadioButton(driver, "");
    }

    Guidewire8RadioButton radio_DoesApplicantInsuredHaveHazardousPlacardOntheirVehicles() {
        return new Guidewire8RadioButton(driver, "");
    }

    Guidewire8RadioButton radio_DoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured() {
        return new Guidewire8RadioButton(driver, "");
    }


    //  HELPER METHODS

    ///////  TABS

    public void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        long endTime = new Date().getTime() + 5000;
        do {
            
        }
        while (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Commercial Auto Owned Liability Group')]")).isEmpty() && new Date().getTime() < endTime);
    }


    public void clickAdditionalCoveragesTab() {
        clickWhenClickable(link_AdditioanlCoveragesTab);
        long endTime = new Date().getTime() + 5000;
        do {
            
        }
        while (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Additional Coverages')]")).isEmpty() && new Date().getTime() < endTime);
    }


    public void clickExclutionsAndConditionsTab() {
        clickWhenClickable(link_ExclutionsAndConditionsTab);
        long endTime = new Date().getTime() + 5000;
        do {
            
        }
        while (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Exclusions')]")).isEmpty() && new Date().getTime() < endTime);
    }


    public void clickAditionalInsuredsTab() {
        clickWhenClickable(link_AdditionalInsuredsTab);
        long endTime = new Date().getTime() + 5000;
        do {
            
        }
        while (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Additional Insureds / Certificate Holders')]")).isEmpty() && new Date().getTime() < endTime);
    }


    public void clickUnderwriterQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestions);
        
    }


    ///////// COVERAGES PAGE

    /////   COMMERCIAL AUTO OWNED LIABILITY GROUP

    public void selectLiabilityLimit(LiabilityLimit liabilityLimit) {
        Guidewire8Select mySelect = select_LiabilityLimit();
        mySelect.selectByVisibleText(liabilityLimit.getValue());
    }


    public List<String> getLiabilityLimits() {
        Guidewire8Select mySelect = select_LiabilityLimit();
        return mySelect.getList();
    }


    public void selectDeductibleLiabilityCoverageCA0301(DeductibleLiabilityCoverage deductible) {
        Guidewire8Select mySelect = select_DeductibleLiabilityCoverageCA0301();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    public List<String> getLiabilityDeductible() {
        Guidewire8Select mySelect = select_DeductibleLiabilityCoverageCA0301();
        return mySelect.getList();
    }


    public void selectMedicalPaymentsCA9903(MedicalPaymentsLimit medLimit) {
        Guidewire8Select mySelect = select_MedicalPaymentsLimit();
        mySelect.selectByVisibleText(medLimit.getValue());
    }


    public void checkMedicalPaymentsCA9903(boolean checked) {
        checkbox_MedicalPaymentsCA9903().select(checked);
    }


    ////   COMMERCIAL AUTO HIRED AUTO GROUP


    public void checkHiredAutoLiability(boolean checked) {
        checkbox_HiredAutoLiability().select(checked);
        
    }


    public List<String> getHiredAutoLiabilityBI() {
        Guidewire8Select mySelect = select_HiredAutoLiabilityBI();
        return mySelect.getList();
    }


    public void setHiredAutoLiabilityBI(HiredAutoLiabilityLimitBI hiredAutoLiability) {
        Guidewire8Select mySelect = select_HiredAutoLiabilityBI();
        mySelect.selectByVisibleText(hiredAutoLiability.getValue());
    }


    public void checkHiredAutoCollision(boolean checked) {
        checkbox_HiredAutoCollision().select(checked);
        
    }


    public List<String> getHiredAutoCollision() {
        Guidewire8Select mySelect = select_HiredAutoCollisionDeductible();
        return mySelect.getList();
    }


    public void setHiredAutoCollisionDeductible(HiredAutoCollisionDeductible hiredAutoCollision) {
        Guidewire8Select mySelect = select_HiredAutoCollisionDeductible();
        mySelect.selectByVisibleText(hiredAutoCollision.getValue());
    }


    public void checkHiredAutoComprehensive(boolean checked) {
        checkbox_HiredAutoComprehensive().select(checked);
        
    }


    public void selectHiredAutoState(State state) {
        Guidewire8Select mySelect = select_HireAutoState();
        mySelect.selectByVisibleText(state.getName());
    }


    public void clickAddHiredAutoState() {
        clickWhenClickable(button_AddHiredAutoState);
    }


    public void setPrimaryCostOfHire(int costofHire) {
        tableUtils.setValueForCellInsideTable(table_HiredAutoStates, 1, "Primary Cost of Hire", "CostHire", String.valueOf(costofHire));
    }


    public void setExcessCostOfHire(int excessCostofHire) {
        tableUtils.setValueForCellInsideTable(table_HiredAutoStates, 1, "Excess Cost of Hire", "ExcessCostHire", String.valueOf(excessCostofHire));
    }


    public void setMaxValueOfVehicle(int vehicleValue) {
        tableUtils.setValueForCellInsideTable(table_HiredAutoStates, 1, "What is the maximum value of the vehicle?", "MaxVehicleValue", String.valueOf(vehicleValue));
    }


    public void setNumberOfMonthsLeased(int numberOfMonths) {
        tableUtils.setValueForCellInsideTable(table_HiredAutoStates, 1, "# of months leased / rented", "MonthsLeasedRented", String.valueOf(numberOfMonths));
    }


    ////   COMMERCIAL AUTO NON-OWNED GROUP

    public void checkNonOwnedAutoLiability(boolean checked) {
        checkbox_NonOwnedAutoLiability().select(checked);
        
    }


    public List<String> getNonOwnedAutoLiabilityBI() {
        Guidewire8Select mySelect = select_NonOwnedAutoLiabilityBI();
        return mySelect.getList();
    }


    public void setNonOwnedAutoLiability(NonOwnedAutoLiabilityBI nonOwnedLiability) {
        Guidewire8Select mySelect = select_NonOwnedAutoLiabilityBI();
        mySelect.selectByVisibleText(nonOwnedLiability.getValue());
    }


    public void selectNONHiredAutoState(State state) {
        Guidewire8Select mySelect = select_NonOwnedLiabilityState();
        mySelect.selectByVisibleText(state.getName());
    }


    public void clickAddNonOwnedLiabilityState() {
        clickWhenClickable(button_NonOwnedLiabilityState);
    }


    public void setNumberOfEmployees(int employees) {
        tableUtils.setValueForCellInsideTable(table_NonOwnedLiabilityStates, 1, "Total number of employees, partners, and members", "NumEmployees", String.valueOf(employees));
    }


    ////////// ADDITIONAL COVERAGES


    public void addNamedIndividuals(String name) {
        super.clickAdd();
//		button_AddNamedIndividual.click();
        
        tableUtils.setValueForCellInsideTable(table_OtherCarNamedIndividules, 1, "Driver", "c1", name);
    }


    public void checkOtherCarComp(boolean yesno) {
        
        checkbox_OtherCarComprehensive().select(yesno);
    }


    public void setOtherCarComp(OtherCarComprehensive comp) {
        Guidewire8Select mySelect = select_OtherCarCompDeductible();
        
        mySelect.selectByVisibleText(comp.getValue());
        
    }


    public void checkOtherCarLiability(boolean yesno) {
        checkbox_OtherCarLiability().select(yesno);
    }


    public String getLiabilityLimit() {
        return table_OtherCarLiabilityLimit.getText();
    }


    public void checkOtherCarMedicalPayments(boolean yesno) {
        checkbox_OtherCarMedicalPayment().select(yesno);
        
    }


    public String getmedicalPaymentLimit() {
        return table_OtherCarMedicalPayments.getText();
    }


    public void checkOtherCarUnderinsuredMotorist(boolean yesno) {
        checkbox_OthercarUnderinsuredMotorist().select(yesno);
        
    }


    public void setOtherCarUnderinsuredMotorist(OtherCarUnderinsuredMotorist underInsured) {
        Guidewire8Select mySelect = select_OtherCarUnderinsuredMotoristLimit();
        
        mySelect.selectByVisibleText(underInsured.getValue());
        
    }


    public void checkOtherCarUninsuredMotorist(boolean yesno) {
        checkbox_OtherCarUninsuredMotorist().select(yesno);
        
    }


    public void setOtherCarUninsuredMotorist(OtherCarUninsuredMotorist uninsured) {
        Guidewire8Select mySelect = select_OtherCarUninsuredMotoristLimit();
        
        mySelect.selectByVisibleText(uninsured.getValue());
        
    }


    public void checkOtherCarCollision(boolean yesno) {
        checkbox_OtherCarCollision().select(yesno);
        
    }


    public void setOtherCarCollision(OtherCarCollision collision) {
        Guidewire8Select mySelect = select_OtherCarCollisionDeductible();
        
        mySelect.selectByVisibleText(collision.getValue());
        
    }


    public void checkMCS_90(boolean yesno) {
        
        checkbox_MCS_90().select(yesno);
        
    }

    /**
     * @author bmartin
     * @Description - This is only usable by Under Writer. Click checkbox for "Coverage For Certain Operations In Connection With Railroads CA 20 70"
     * @DATE - Feb 23, 2016
     */
    public void checkCA_20_70(boolean yesno) {
        checkbox_CA_20_70().select(yesno);
    }


    public void setCA_20_70_HandRatedPremium(String premium) {
        setText(textbox_HandRatedPremium, premium);
    }


    public void clickAddCA_20_70_Details() {
        clickWhenClickable(button_Add_CA_20_70_Details);
    }


    public void clickRemoveCA_20_70_Details() {
        clickWhenClickable(button_Remove_CA_20_70_Details);
    }

    /**
     * @author bmartin
     * @Description - This is only usable by Under Writer. Fill out Scheduled Railroad text field under CA 20 70.
     * @DATE - Feb 23, 2016
     */
    public void setScheduledRailroad(String scheduledRailRoad) {
//		clickAddCA_20_70_Details();
        
        tableUtils.setValueForCellInsideTable(table_CA_20_70, 1, "Scheduled Railroad", "ScheduledRailRoad", scheduledRailRoad);
    }

    /**
     * @author bmartin
     * @Description - This is only usable by Under Writer. Fill out Designated Job Site text field under CA 20 70.
     * @DATE - Feb 23, 2016
     */
    public void setDesignatedJobSite(String designatedJobSite) {
//		clickAddCA_20_70_Details();
        
        tableUtils.setValueForCellInsideTable(table_CA_20_70, 1, "Designated Job Site", "DesignatedJobSite", designatedJobSite);
    }


    //// EXCLUSIONS AND CONDITIONS
    Guidewire8Checkbox checkbox_Exclusion_ExclusionOfFederalEmployeesCA0442() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Exclusion Of Federal Employees Using Autos In Government Business CA 04 42')]/preceding-sibling::table");
    }


    public void checkExclusionOfFederalEmployeesCA2397(boolean checked) {
        checkbox_Exclusion_ExclusionOfFederalEmployeesCA0442().select(checked);
    }

    Guidewire8Checkbox checkbox_Exclusion_ExplosivesCA2301() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Amphibious Vehicles CA 23 97')]/preceding-sibling::table");
    }


    public void check_Exclusion_ExplosivesCA2301(boolean checked) {
        checkbox_Exclusion_ExplosivesCA2301().select(checked);
    }

    Guidewire8Checkbox checkbox_Exclusion_WrongDeliveryOfLiquidCA2305() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Wrong Delivery Of Liquid Products CA 23 05')]/preceding-sibling::table");
    }


    public void check_Exclusion_WrongDeliveryOfLiquidProductsCA2305(boolean checked) {
        checkbox_Exclusion_WrongDeliveryOfLiquidCA2305().select(checked);
    }

    Guidewire8Checkbox checkbox_Exclusion_ProfessionalServicesNotCoveredCA2018() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Professional Services Not Covered CA 20 18')]/preceding-sibling::table");
    }


    public void check_Exclusion_ProfessionalServicesNotCoveredCA2018(boolean checked) {
        checkbox_Exclusion_ProfessionalServicesNotCoveredCA2018().select(checked);
    }


    Guidewire8Checkbox checkbox_Condition_AmphibiousVehiclesCA2397() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Amphibious Vehicles CA 23 97')]/preceding-sibling::table");
    }


    public void check_Condition_AmphibiousVehiclesCA2397(boolean checked) {
        checkbox_Condition_AmphibiousVehiclesCA2397().select(checked);
    }

    Guidewire8Checkbox checkbox_Condition_CommercialAutoManuscriptEndorsementIDCA313013() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Commercial Auto Manuscript Endorsement IDCA 31 3013')]/preceding-sibling::table");
    }


    public void check_Condition_CommercialAutoManuscriptEndorsementIDCA313013(boolean checked) {
        checkbox_Condition_CommercialAutoManuscriptEndorsementIDCA313013().select(checked);
    }

    Guidewire8Checkbox checkbox_Condition_EmployeeHiredAutosCA2054() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Employee Hired Autos CA 20 54')]/preceding-sibling::table");
    }


    public void check_Condition_EmployeeHiredAutosCA2054(boolean checked) {
        checkbox_Condition_EmployeeHiredAutosCA2054().select(checked);
    }


    //// UNDERWRITER QUESTIONS

    public void clickHasApplicantInsuredLeasedVehicleForMoreThanSixMonths(boolean radioValue) {
        tableUtils.setRadioByText("Has applicant/insured leased a vehicle for more than 6 months", radioValue);
    }


    public void clickDoesApplicantInsuredHaveHazardousPlacardOntheirVehicles(boolean radioValue) {
        tableUtils.setRadioByText("Does applicant/insured have hazardous placard on their vehicles", radioValue);
    }


    public void clickDoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured(boolean radioValue) {
        tableUtils.setRadioByText("Do we insure all owned and leased vehicles for the applicant/insured", radioValue);
    }


    public void clickDoesIndividualAllowFamilyMembersToDriveCar(boolean radioValue) {
        tableUtils.setRadioByText("Does the individual scheduled in the Drive Other Car Coverage allow family members other than the spouse to drive the assigned vehicle", radioValue);
    }


    public void clickDoesApplicantRequireLimitsInExcessOf1M(boolean radioValue) {
        tableUtils.setRadioByText("Does applicant/insured require limits in excess of $1,000,000", radioValue);
    }

    @FindBy(xpath = "//div[contains(text(), 'Indicate States in which the applicant/insured operates.')]/parent::td/following-sibling::td/div")
    private WebElement link_StateDiv;

    @FindBy(xpath = "//input[@name='c2']")
    private WebElement link_StateInput;


    public void setStatesOfOpperation(State state) {
        clickWhenClickable(link_StateDiv);
        link_StateInput.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        link_StateInput.sendKeys("Idaho");
        find(By.xpath("//div[contains(text(), 'Indicate States in which the applicant/insured operates')]")).click();
    }


    ////   PAGE LEVEL METHODS

    public void fillOutCommercialAutoLine(GeneratePolicy policy) {
        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            fillOutCommercialAutoCoverage(policy);
            fillOutAdditoinalCoverages(policy);
            fillOutExclutionsAndConditions(policy);
            fillOutAdditionalInsureds(policy);
            fillOutUnderwritingQuestions(policy);
        } else {
            fillOutUnderwritingQuestions(policy);
        }
    }


    public void fillOutAllCommercialAutoLine(GeneratePolicy policy) {
        fillOutCommercialAutoCoverage(policy);
        fillOutAdditoinalCoverages(policy);
        fillOutExclutionsAndConditions(policy);
        fillOutAdditionalInsureds(policy);
        fillOutUnderwritingQuestions(policy);
    }


    public void fillOutAllCommercialAutoLine_Change(GeneratePolicy policy) {
        fillOutCommercialAutoCoverage(policy);
        fillOutAdditoinalCoverages(policy);
        fillOutExclutionsAndConditions(policy);
        fillOutAdditionalInsureds(policy);
        fillOutUnderwritingQuestions(policy);
    }


    public void fillOutCommercialAutoCoverage(GeneratePolicy policy) {
        boolean setHiredAutoState = false;
        boolean setNonOwnedLiabilityState = false;
        clickCoveragesTab();
        

        selectLiabilityLimit(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getLiabilityLimit());
        
        selectDeductibleLiabilityCoverageCA0301(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getDeductibleLiabilityCoverageCA0301());
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isMedicalPaymentsCA9903()) {
            setHiredAutoState = true;
            
            checkMedicalPaymentsCA9903(true);
            
            selectMedicalPaymentsCA9903(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getMedicalPaymentsCA9903Limit());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isHiredAutoLiability()) {
            setHiredAutoState = true;
            checkHiredAutoLiability(true);
            
            //set drop down
            setHiredAutoLiabilityBI(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getHiredAutoLiabilityLiabilityBI());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isHiredAutoCollision()) {
            setHiredAutoState = true;
            checkHiredAutoCollision(true);
            
            //set dropdown
            setHiredAutoCollisionDeductible(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getHiredAutoCollisionDeductible());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isHiredAutoComprehensive()) {
            setHiredAutoState = true;
            checkHiredAutoComprehensive(true);
            
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isNonOwnedAutoLiabilityBI()) {
            setNonOwnedLiabilityState = true;
            checkNonOwnedAutoLiability(true);
            
            setNonOwnedAutoLiability(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getNonOwnedliabilityBI());
        }

        
        //fill out hired auto state
        if (setHiredAutoState) {
            List<State> existingStateList = new ArrayList<State>();
            WebElement stateList = find(By.xpath("//label[contains(text(), 'Hired Auto States')]/parent::td/parent::tr/following-sibling::tr/td/div/child::div[last()]"));
            for (WebElement stateRow : stateList.findElements(By.xpath(".//div/table/tbody/child::tr/child::td[2]/div"))) {
                existingStateList.add(State.valueOfName(stateRow.getText()));
            }
            for (HiredAutoState state : policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getHiredAutoState()) {
                if (!existingStateList.contains(state.getState())) {
                    selectHiredAutoState(state.getState());
                    
                    clickAddHiredAutoState();
                    
                    setPrimaryCostOfHire(state.getPrimaryCostOfHire());
                    
                    setExcessCostOfHire(state.getExcessCostOfHire());
                    
                    setMaxValueOfVehicle(state.getMaxValueOfVehicle());
                    
                    setNumberOfMonthsLeased(state.getNumberOfMonthsLeased());
                    
                }
            }
        }

        //fill out nonowned auto state
        if (setNonOwnedLiabilityState) {
            List<State> existingStateList = new ArrayList<State>();
            WebElement stateList = find(By.xpath("//label[contains(text(), 'Non-owned Liability States')]/parent::td/parent::tr/following-sibling::tr/td/div/child::div[last()]"));
            for (WebElement stateRow : stateList.findElements(By.xpath(".//div/table/tbody/child::tr/child::td[2]/div"))) {
                existingStateList.add(State.valueOfName(stateRow.getText()));
            }
            for (NonOwnedLiabilityStates state : policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().getNonOwnedLiabilityState()) {
                if (!existingStateList.contains(state.getState())) {
                    selectNONHiredAutoState(state.getState());
                    
                    clickAddNonOwnedLiabilityState();
                    
                    setNumberOfEmployees(state.getNumberOfEmployees());
                    
                }
            }
        }
    }


    public void fillOutAdditoinalCoverages(GeneratePolicy policy) {
        boolean addDriver = false;
        clickAdditionalCoveragesTab();
        

        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCompresensive()) {
            addDriver = true;
            checkOtherCarComp(true);
            
            setOtherCarComp(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().getComprehensive());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarLiability()) {
            addDriver = true;
            checkOtherCarLiability(true);
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarMedicalPayments()) {
            addDriver = true;
            checkOtherCarMedicalPayments(true);
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUnderinsuredMotorist()) {
            addDriver = true;
            checkOtherCarUnderinsuredMotorist(true);
            
            setOtherCarUnderinsuredMotorist(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().getUnderinsuredMotorist());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUninsuredMotorist()) {
            addDriver = true;
            checkOtherCarUninsuredMotorist(true);
            
            setOtherCarUninsuredMotorist(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().getUninsuredMotorist());
        }
        
        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCollision()) {
            addDriver = true;
            checkOtherCarCollision(true);
            
            setOtherCarCollision(policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().getCollision());
        }
        

        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isMcs_90()) {
            checkMCS_90(true);
        }
        
        if (addDriver) {
            for (String name : policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().getOtherCarNamedIndividuals()) {
                addNamedIndividuals(name);
            }
        }

    }


    public void fillOutExclutionsAndConditions(GeneratePolicy policy) {
        clickExclutionsAndConditionsTab();
        for (CALineExclutionsAndConditions exclutionCondition : policy.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions()) {
            switch (exclutionCondition) {
                case AmphibiousVehiclesCA2397:
                    check_Condition_AmphibiousVehiclesCA2397(true);
                    
                    break;
                case EmployeeHiredAutosCA2054:
                    check_Condition_EmployeeHiredAutosCA2054(true);
                    
                    break;
                case ExplosivesCA2301:
                    check_Exclusion_ExplosivesCA2301(true);
                    
                    break;
                case WrongDeliveryOfLiquidProductsCA2305:
                    check_Exclusion_WrongDeliveryOfLiquidProductsCA2305(true);
                    
                    break;
                case ProfessionalServicesNotCoveredCA2018:
                    check_Exclusion_ProfessionalServicesNotCoveredCA2018(true);
                    
                    break;
                default:
                    systemOut("NEED TO ADD CASE FOR: " + exclutionCondition.getValue());
                    break;
            }
        }
    }


    public void fillOutAdditionalInsureds(GeneratePolicy policy) {
        clickAditionalInsuredsTab();
        
    }

    //jlarsen this method need cleaned up by a switch statement when we have all the UW Questions and availability

    public void fillOutUnderwritingQuestions(GeneratePolicy policy) {
        clickUnderwriterQuestionsTab();
        
        
        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            clickHasApplicantInsuredLeasedVehicleForMoreThanSixMonths(policy.commercialAutoCPP.getCommercialAutoLine().isHasApplicantLeasedGreaterThan6Months());
            if (policy.commercialAutoCPP.isHasYouthfullDrivers()) {
                setStatesOfOpperation(State.Idaho);
                clickUnderwriterQuestionsTab();
                
            }
        } else {
            clickHasApplicantInsuredLeasedVehicleForMoreThanSixMonths(policy.commercialAutoCPP.getCommercialAutoLine().isHasApplicantLeasedGreaterThan6Months());
            if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCollision() ||
                    policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarCompresensive() ||
                    policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarLiability() ||
                    policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarMedicalPayments() ||
                    policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUnderinsuredMotorist() ||
                    policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isOtherCarUninsuredMotorist()) {
                clickDoesIndividualAllowFamilyMembersToDriveCar(policy.commercialAutoCPP.getCommercialAutoLine().isDoesIndividualFamilyMembersDrive());
            }
            if (policy.commercialAutoCPP.isHasYouthfullDrivers()) {
                setStatesOfOpperation(State.Idaho);
            }
        }

        if (policy.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().isMcs_90()) {
            clickDoesApplicantRequireLimitsInExcessOf1M(false);
            setStatesOfOpperation(State.Idaho);
            clickDoesApplicantInsuredHaveHazardousPlacardOntheirVehicles(false);
            clickDoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured(true);
        }


        
    }

    /**
     * @author bmartin
     * @Description - This checks to see if Liability Limit is blank.
     * This should only be used if it was originally filled out, otherwise it will fail because it was never filled out.
     * @DATE - March 3, 2016
     */
    public void checkLiabilityLimitExistence() throws Exception {
        
        if (text_LiabilityLimit.isEmpty()) {
            throw new Exception("Liability Limit is blank.");
        }
    }

}
