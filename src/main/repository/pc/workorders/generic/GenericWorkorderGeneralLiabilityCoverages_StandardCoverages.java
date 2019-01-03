package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GeneralLiability.StandardCoverages.*;

public class GenericWorkorderGeneralLiabilityCoverages_StandardCoverages extends BasePage {

    private WebDriver driver;

    public GenericWorkorderGeneralLiabilityCoverages_StandardCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8Select select_PolicyBasis() {
        return new Guidewire8Select(driver, "");
    }

    Guidewire8RadioButton radio_SplitBIPDLimits() {
        return new Guidewire8RadioButton(driver, "");
    }

    Guidewire8Select select_OccuranceLimit() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Occurrence Limit')]/parent::td/parent::tr/child::td/table");
    }

    Guidewire8Select select_MedicalPaymentsPerPerson() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Medical Payments per person')]/parent::td/parent::tr/child::td/table");
    }


    Guidewire8Select select_PersonalAndAdvertisingInjury() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Personal & Advertising Injury Limit')]/parent::td/parent::tr/child::td/table");
    }

    Guidewire8Select select_DamageToRentedPremises() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Occurrence Limit')]/parent::td/parent::tr/child::td/table");
    }

    Guidewire8Select select_AggregateLimit() {
        return new Guidewire8Select(driver, "");
    }

    Guidewire8Select select_ProductsCompOpsAggregate() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Completed Operations Aggregate Limit')]/parent::td/parent::tr/child::td/table");
    }

    Guidewire8Checkbox checkbox_OptionalDeductible() {
        return new Guidewire8Checkbox(driver, "");
    }

    Guidewire8Select select_DeductibleLiability() {
        return new Guidewire8Select(driver, "//label[contains(text(), 'Deductible Liability Insurance CG 03 00')]/parent::td/parent::tr/child::td/table");
    }

    Guidewire8Select select_DeductibleCSD() {
        return new Guidewire8Select(driver, "");
    }

    Guidewire8Checkbox checkbox_Arbitration() {
        return new Guidewire8Checkbox(driver, "");
    }

    Guidewire8Select select_ArbitrationType() {
        return new Guidewire8Select(driver, "");
    }


    public void selectOccuranceLimit(CG0001_OccuranceLimit ocuranceLimit) {
        Guidewire8Select mySelect = select_OccuranceLimit();
        mySelect.selectByVisibleText(ocuranceLimit.getValue());
    }


    public void selectMedicalPayments(CG0001_MedicalPaymentsPerPerson medicalPayments) {
        Guidewire8Select mySelect = select_MedicalPaymentsPerPerson();
        mySelect.selectByVisibleText(medicalPayments.getValue());
    }


    public void selectPersonalAdvertisingInjury(CG0001_PersonalAdvertisingInjury injury) {
        Guidewire8Select mySelect = select_PersonalAndAdvertisingInjury();
        mySelect.selectByVisibleText(injury.getValue());
    }


    public void selectProductsCompOpsAggregate(CG0001_OperationsAggregateLimit operationsLimit) {
        Guidewire8Select mySelect = select_ProductsCompOpsAggregate();
        mySelect.selectByVisibleText(operationsLimit.getValue());
    }


    public void selectDeductibleLiability(CG0001_DeductibleLiabilityInsCG0300 deductibleLiability) {
        Guidewire8Select mySelect = select_DeductibleLiability();
        mySelect.selectByVisibleText(deductibleLiability.getValue());
    }


}




























