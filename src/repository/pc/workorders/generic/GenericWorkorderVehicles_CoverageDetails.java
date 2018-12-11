package repository.pc.workorders.generic;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.custom.Vehicle;

public class GenericWorkorderVehicles_CoverageDetails extends GenericWorkorderVehicles {


    private WebDriver driver;

    public GenericWorkorderVehicles_CoverageDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutCoverageDetails_QQ(Vehicle vehicle) {
        selectVehicleCoverageDetailsTab();
        
        if (setComprehensive(vehicle.hasComprehensive())) {
            setCollision(vehicle.hasCollision());
        }

        if (vehicle.getVehicleTypePL().equals(VehicleTypePL.Trailer) && !vehicle.hasComprehensive()) {
//			@editor ecoleman 5/11/18 : for trailers, I'm not seeing this check-box on my local, will test more elsewhere
//			Note: It seems that if Comprehensive is checked, fireandtheft is replaced..
            setFireTheftCoverage(vehicle.hasFireAndTheft());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
        }
    }

    public void fillOutCoverageDetails_FA(Vehicle vehicle) {
        selectVehicleCoverageDetailsTab();
        
    }

    public void fillOutCoverageDetails(Vehicle vehicle) {
        selectVehicleCoverageDetailsTab();
        
    }


    private Guidewire8Checkbox checkbox_FireTheft() {
        return new Guidewire8Checkbox(driver, "//div[contains(.,'Fire & Theft')]/ancestor::fieldset/descendant::table");
    }

    public void setFireTheftCoverage(boolean checked) {
        Guidewire8Checkbox newCheck = checkbox_FireTheft();
        newCheck.select(checked);
        
    }

    private Guidewire8Checkbox checkbox_Comprehensive() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Comprehensive')]/preceding-sibling::table");
    }

    public boolean setComprehensive(boolean checked) {
        checkbox_Comprehensive().select(checked);
        
        return checked;
    }

    private Guidewire8Checkbox checkbox_Collision() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Collision')]/preceding-sibling::table");
    }

    public boolean setCollision(boolean checked) {
        checkbox_Collision().select(checked);
        
        return checked;
    }

    private Guidewire8Checkbox checkbox_AdditionalLivingExpense() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Additional Living Expense')]/preceding-sibling::table");
    }

    public void setAdditionalLivingExpense(boolean checked) {
        checkbox_AdditionalLivingExpense().select(checked);
        
    }

    public boolean checkIfLiabilityCoverageExists() {
        return checkIfElementExists("//table[contains(@id, 'VehicleDV:liabilityCoverage')]", 2000);
    }


    public boolean checkIfComprehensiveCoverageExists() {
        return checkIfElementExists("//div[contains(.,'Comprehensive')]/ancestor::fieldset/descendant::table", 2000);
    }

    public boolean checkIfFireTheftCoverageExists() {
        return checkIfElementExists("//div[contains(.,'Fire & Theft')]/ancestor::fieldset/descendant::table", 2000);
    }

    private Guidewire8Select select_Comprehensive() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleCoverageDetailDV:0:CoverageInputSet:CovPatternInputGroup:OptionTermInput-triggerWrap')]");
    }

    public void setComprehensiveDeductible(String deductible) {
        
        setComprehensive(true);
        
        Guidewire8Select comprehensiveDropdown = select_Comprehensive();
        comprehensiveDropdown.selectByVisibleTextPartial(deductible);
    }

    private Guidewire8Select select_Collision() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleCoverageDetailDV:1:CoverageInputSet:CovPatternInputGroup:OptionTermInput-triggerWrap')]");
    }

    public void setCollisionDeductible(String deductible) {
        
        Guidewire8Select collisionDropdown = select_Collision();
        collisionDropdown.selectByVisibleTextPartial(deductible);
    }

    private Guidewire8Select select_RentalReimbursement() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleCoverageDetailDV:2:CoverageInputSet:CovPatternInputGroup:OptionTermInput-triggerWrap')]");
    }

    public void setRentalReimbursementDeductible(String deductible) {
        
        Guidewire8Select rentalReimbursementDropdown = select_RentalReimbursement();
        rentalReimbursementDropdown.selectByVisibleTextPartial(deductible);
    }

    @FindBy(xpath = "//input[contains(@id, ':0:CoverageInputSet:CovPatternInputGroup:OptionTermInput-inputEl')]")
    private WebElement dropdown_ComprehensiveDeductible;

    public String getComprehensiveCoverageLimit() {
        return dropdown_ComprehensiveDeductible.getAttribute("value");
    }

    @FindBy(xpath = "//input[contains(@id, ':1:CoverageInputSet:CovPatternInputGroup:OptionTermInput-inputEl')]")
    private WebElement dropdown_CollisionDeductible;

    public String getCollisionCoverageLimit() {
        return dropdown_CollisionDeductible.getAttribute("value");
    }


    @FindBy(xpath = "//input[contains(@id, ':2:CoverageInputSet:CovPatternInputGroup:OptionTermInput-inputEl')]")
    private WebElement dropdown_RentalReimbursement;

    public String getRentalReimbursementLimit() {
        return dropdown_RentalReimbursement.getAttribute("value");
    }

    @FindBy(xpath = "//div[contains(@id, ':4:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:OptionTermInput-inputEl')]")
    private WebElement additionalLivingExpensesLimit;

    public String getAdditionalLivingExpense() {
        return additionalLivingExpensesLimit.getText();
    }


    private Guidewire8Select select_CollisionDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Collision')]/ancestor::legend//following-sibling::div/span/div/table/tbody/child::tr[1]/child::td[2]/table");
    }

    public void selectCollisionDeductible(Comprehensive_CollisionDeductible deductible) {
        Guidewire8Select mySelect = select_CollisionDeductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    private Guidewire8Select select_ComprehensiveDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Comprehensive')]/ancestor::legend/following-sibling::div/span/div/table/tbody/child::tr[1]/child::td[2]/table");
    }

    public void selectComprehensiveDeductible(Comprehensive_CollisionDeductible deductible) {
        Guidewire8Select mySelect = select_ComprehensiveDeductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }

    private Guidewire8RadioButton radio_LiabilityCoverage() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'VehicleDV:liabilityCoverage') or contains(@id, 'PAVehiclePopup:excludesLiability')]");
    }

    public void setRadioLiabilityCoverage(boolean yesNo) {
        radio_LiabilityCoverage().select(yesNo);
    }


}



















