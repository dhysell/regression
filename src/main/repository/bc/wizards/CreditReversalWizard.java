package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class CreditReversalWizard extends BasePage {

	public CreditReversalWizard(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	///////////////////////
	// "Recorded Elements"//
	///////////////////////

	@FindBy(xpath = "//div[@id='AccountNewCreditReversalWizard:CreditReversalCreditSearchScreen:CreditReversalCreditLV']")
	public WebElement table_CreditReversalTable;
	
	@FindBy(xpath = "//input[@label='Minimum Amount' or contains(@id, ':MinAmountCriterion-inputEl')]")
	private WebElement editBox_CommonBCMinimumAmount;

	@FindBy(xpath = "//input[@label='Maximum Amount' or contains(@id, ':MaxAmountCriterion-inputEl')]")
	private WebElement editBox_CommonBCMaximumAmount;

	//////////////////
	// Helper Methods//
	//////////////////

	public void setMinimumAmount(double amountToFill) {
		clickWhenVisible(editBox_CommonBCMinimumAmount);
		editBox_CommonBCMinimumAmount.sendKeys(Keys.HOME);
		editBox_CommonBCMinimumAmount.sendKeys(String.valueOf(amountToFill));
	}

	public void setMaximumAmount(double amountToFill) {
		clickWhenVisible(editBox_CommonBCMaximumAmount);
		editBox_CommonBCMaximumAmount.sendKeys(Keys.HOME);
		editBox_CommonBCMaximumAmount.sendKeys(String.valueOf(amountToFill));
	}
	
	public void selectCreditToReverseByRowNumber(int rowNumber) {
		table_CreditReversalTable.findElement(By.xpath(".//a[contains(@id,'AccountNewCreditReversalWizard:CreditReversalCreditSearchScreen:CreditReversalCreditLV:" + String.valueOf(rowNumber - 1) + ":Select')]")).click();
	}
}
