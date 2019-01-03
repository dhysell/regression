package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.helpers.DateUtils;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.Date;

//this page is pop up when hitting "Modify Invoice Items" button in Charges screen
public class ModifyInvoiceItemsWizard extends BasePage {

	private TableUtils tableUtils;
	
	public ModifyInvoiceItemsWizard(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath = "//div[@id='EditInvoiceItemsPopup:InvoiceItemsLV']")
	public WebElement table_InvoiceItems;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void setAmount(int tableRowNumber, double amount) {
		String amountGridColumnID = tableUtils.getGridColumnFromTable(table_InvoiceItems, "Amount");
		WebElement editBox_Amount = table_InvoiceItems.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + amountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_Amount);
		tableUtils.setValueForCellInsideTable(table_InvoiceItems, "Amount", String.valueOf(amount));
	}

	public void setPaidAmount(int tableRowNumber, double paidAmount) {
		String paidAmountGridColumnID = tableUtils.getGridColumnFromTable(table_InvoiceItems, "Paid Amount");
		WebElement editBox_PaidAmount = table_InvoiceItems.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + paidAmountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_PaidAmount);
		tableUtils.setValueForCellInsideTable(table_InvoiceItems, "Paid Amount", String.valueOf(paidAmount));
	}

	public void setEventDate(int tableRowNumber, Date eventDate) {
		String eventDateGridColumnID = tableUtils.getGridColumnFromTable(table_InvoiceItems, "Event Date");
		WebElement editBox_EventDate = table_InvoiceItems.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + eventDateGridColumnID + "')]/div"));
		clickWhenClickable(editBox_EventDate);
		tableUtils.setValueForCellInsideTable(table_InvoiceItems, "EventDate", DateUtils.dateFormatAsString("MM/dd/yyyy", eventDate));
	}
}
