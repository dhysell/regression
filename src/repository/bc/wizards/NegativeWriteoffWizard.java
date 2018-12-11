package repository.bc.wizards;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class NegativeWriteoffWizard extends BasePage {

	public NegativeWriteoffWizard(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[contains(@id,':NewNegativeWriteoffDetailsDV:Amount-inputEl')]")
	public WebElement editbox_NegativeWriteoffAmount;

	public void setNegativeWriteoffAmount(double amount) {
		editbox_NegativeWriteoffAmount.sendKeys(Keys.CONTROL + "a");
		editbox_NegativeWriteoffAmount.sendKeys(String.valueOf(amount));

	}

}
