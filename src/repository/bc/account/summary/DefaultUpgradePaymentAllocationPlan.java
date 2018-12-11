package repository.bc.account.summary;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class DefaultUpgradePaymentAllocationPlan extends BasePage  {
	
	public DefaultUpgradePaymentAllocationPlan(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for Insured Delinquency Plan
	// -----------------------------------------------
	@FindBy(xpath = "//input[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:PaymentAllocationPlanDetailDV:Name-inputEl']")
	public WebElement editbox_DefaultUpgradePaymentAllocationPlanName;

	@FindBy(xpath = "//input[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:PaymentAllocationPlanDetailDV:Description-inputEl']")
	public WebElement editbox_DefaultUpgradePaymentAllocationPlanDescription;

	@FindBy(xpath = "//input[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:PaymentAllocationPlanDetailDV:EffectiveDate-inputEl']")
	public WebElement editbox_DefaultUpgradePaymentAllocationPlanEffectiveDate;

	@FindBy(xpath = "//input[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:PaymentAllocationPlanDetailDV:ExpirationDate-inputEl']")
	public WebElement editbox_DefaultUpgradePaymentAllocationPlanExpirationDate;
	
	@FindBy(xpath = "//a[@id='PaymentAllocationPlanDetailPopup:__crumb__']")
	public WebElement link_DefaultUpgradePaymentAllocationPlanReturnToSummary;

	@FindBy(xpath = "//a[@id='AddDistributionFilterPopup:__crumb__' or @id='AddInvoiceItemOrderingPopup:__crumb__']")
	public WebElement link_ReturnToDefaultUpgradePaymentAllocationPlan;

	@FindBy(xpath = "//a[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:DistributionFilterLV_tb:AddDistributionFilter']")
	public WebElement button_DefaultUpgradePaymentAllocationPlanAddInvoiceItemFilter;

	@FindBy(xpath = "//a[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:DistributionFilterLV_tb:RemoveDistributionFilter']")
	public WebElement button_DefaultUpgradePaymentAllocationPlanRemoveInvoiceItemFilter;

	@FindBy(xpath = "//a[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:InvoiceItemOrderingLV_tb:AddInvoiceItemOrdering']")
	public WebElement button_DefaultUpgradePaymentAllocationPlanAddOrderToPay;

	@FindBy(xpath = "//a[@id='PaymentAllocationPlanDetailPopup:PaymentAllocationPlanDetailScreen:DistributionFilterLV_tb:RemoveDistributionFilter']")
	public WebElement button_DefaultUpgradePaymentAllocationPlanRemoveOrderToPay;

	// -------------------------------------------------------
	// Helper Methods for Above Elements for Insured Delinquency Plan
	// -------------------------------------------------------
	
	public void setDefaultUpgradePaymentAllocationPlanName(String name) {
		clickWhenVisible(editbox_DefaultUpgradePaymentAllocationPlanName);
		editbox_DefaultUpgradePaymentAllocationPlanName.sendKeys(Keys.CONTROL + "a");
		editbox_DefaultUpgradePaymentAllocationPlanName.sendKeys(name);
		
	}

	public void setDefaultUpgradePaymentAllocationPlanDescription(String description) {
		clickWhenVisible(editbox_DefaultUpgradePaymentAllocationPlanDescription);
		editbox_DefaultUpgradePaymentAllocationPlanDescription.sendKeys(Keys.CONTROL + "a");
		editbox_DefaultUpgradePaymentAllocationPlanDescription.sendKeys(description);
		
	}
	
	public void setDefaultUpgradePaymentAllocationPlanEffectiveDate(String date) {
		clickWhenVisible(editbox_DefaultUpgradePaymentAllocationPlanEffectiveDate);
		editbox_DefaultUpgradePaymentAllocationPlanEffectiveDate.sendKeys(Keys.CONTROL + "a");
		editbox_DefaultUpgradePaymentAllocationPlanEffectiveDate.sendKeys(date);
		
	}
	
	public void setDefaultUpgradePaymentAllocationPlanExpirationDate(String date) {
		clickWhenVisible(editbox_DefaultUpgradePaymentAllocationPlanExpirationDate);
		editbox_DefaultUpgradePaymentAllocationPlanExpirationDate.sendKeys(Keys.CONTROL + "a");
		editbox_DefaultUpgradePaymentAllocationPlanExpirationDate.sendKeys(date);
		
	}
	
	public void clickDefaultUpgradePaymentAllocationPlanAddInvocieItem() {
		clickWhenVisible(button_DefaultUpgradePaymentAllocationPlanAddInvoiceItemFilter);
		
	}
	
	public void clickDefaultUpgradePaymentAllocationPlanRemoveInvocieItem() {
		clickWhenVisible(button_DefaultUpgradePaymentAllocationPlanRemoveInvoiceItemFilter);
		
	}
	
	public void clickDefaultUpgradePaymentAllocationPlanAddOrderToPay() {
		clickWhenVisible(button_DefaultUpgradePaymentAllocationPlanAddOrderToPay);
		
	}
	
	public void clickDefaultUpgradePaymentAllocationPlanRemoveOrderToPay() {
		clickWhenVisible(button_DefaultUpgradePaymentAllocationPlanRemoveOrderToPay);
		
	}
	
	public void clickReturnToSummary() {
		clickWhenVisible(link_DefaultUpgradePaymentAllocationPlanReturnToSummary);
		
	}
	
	public void clickReturnToDefaultUpgradePaymentAllocationPlan() {
		clickWhenVisible(link_ReturnToDefaultUpgradePaymentAllocationPlan);
		
	}
}
