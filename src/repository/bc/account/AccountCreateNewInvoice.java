package repository.bc.account;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InvoiceType;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class AccountCreateNewInvoice extends BasePage {
	
	private WebDriver driver;

	public AccountCreateNewInvoice(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	///////////////////////
	// "Recorded Elements"//
	///////////////////////

	@FindBy(xpath = "//input[@id='NewInvoice:InvoiceDate-inputEl']")
	public WebElement editbox_InvoiceDate;

	@FindBy(xpath = "//input[@id='NewInvoice:DueDate-inputEl']")
	public WebElement editbox_DueDate;
	
	public Guidewire8Select select_InvoiceType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':invoicetype-triggerWrap')]");
	}

	//////////////////
	// Helper Methods//
	//////////////////

	public void selectInvoiceType(InvoiceType type) {
		select_InvoiceType().selectByVisibleText(type.getValue());
	}

	public void setInvoiceDate(Date invoiceDate) {
		editbox_InvoiceDate.sendKeys(Keys.CONTROL + "a");
		editbox_InvoiceDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		clickProductLogo();
	}

	public void setDueDate(Date dueDate) {
		editbox_DueDate.sendKeys(Keys.CONTROL + "a");
		editbox_DueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		clickProductLogo();
	}
}
