package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.helpers.NumberUtils;

public class CarryOverCharge extends BasePage {

	private WebDriver driver;

	public CarryOverCharge(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//input[contains(@id,':TransitionRenewalCarryOverChargeDetailScreen:Amount-inputEl')]")
	public WebElement editbox_CarryOverChargeAmount;

	@FindBy(xpath = "//input[contains(@id,':TransitionRenewalCarryOverChargeDetailScreen:OLIEPolicyNumber-inputEl')]")
	public WebElement editbox_CancelledOLIEPolicyNumber;
	
	public Guidewire8Checkbox checkBox_CreateInsuredsCarryOverCharge() {
        
		return new Guidewire8Checkbox(driver, "//table[contains(@id,'TransitionRenewalCarryOverChargeDetailScreen:InsuredPayer')]");
	}

	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////
	
	public void setCarryOverAmount(double carryOverAmount) {
		editbox_CarryOverChargeAmount.sendKeys(String.valueOf(carryOverAmount));
	}

	public void setCancelledOLIEPolicyNumber(String OLIEPolicyNumber) {
		editbox_CancelledOLIEPolicyNumber.sendKeys(OLIEPolicyNumber);
	}

	public void setCreateInsuredsCarryOverCharge(boolean trueOrFalse) {
		checkBox_CreateInsuredsCarryOverCharge().select(trueOrFalse);
		
	}

    public void createCarryOverChargeWithOLIEPolicyNumber(boolean trueOrFalse, double carryOverAmount, String OLIEPolicyNumber) {
        
		setCreateInsuredsCarryOverCharge(trueOrFalse);
		
		setCarryOverAmount(carryOverAmount);
		
		setCancelledOLIEPolicyNumber(OLIEPolicyNumber);
		
		clickUpdate();
	}

    public void createCarryOverChargeWithRandomOLIEPolicyNumber(boolean trueOrFalse, double carryOverAmount) {
        
        setCreateInsuredsCarryOverCharge(trueOrFalse);
        
        setCarryOverAmount(carryOverAmount);
        
        setCancelledOLIEPolicyNumber("01-".concat(String.valueOf(NumberUtils.generateRandomNumberInt(100000, 300000))).concat("-01"));
        
        clickUpdate();
    }

	public void createCarryOverCharge(double carryOverAmount) {
        
		setCarryOverAmount(carryOverAmount);
		
		clickUpdate();
		
	}
}
