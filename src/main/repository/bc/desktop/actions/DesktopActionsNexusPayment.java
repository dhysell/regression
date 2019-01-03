package repository.bc.desktop.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class DesktopActionsNexusPayment extends BasePage  {
	
	private TableUtils tableUtils;
	
	public DesktopActionsNexusPayment(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}
	@FindBy(xpath = "//div[@id='NexusMultiPaymentEntryWizard:NewNexusMultiPaymentScreen:NewMultiPaymentLV']")
	public WebElement table_DesktopActionsNexusPaymentEntry;

	
	public void clickNext() {
		super.clickNext();
	}

	
	public void clickFinish() {
		super.clickFinish();
	}

	
	public void setPolicyNumber(String policyNumber, int tableRowNumber) {
		String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsNexusPaymentEntry,
				"Policy #");
		WebElement editBox_DesktopActionsNexusPaymentsPolicyNumber = table_DesktopActionsNexusPaymentEntry
				.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1)
						+ "')]/td[contains(@class,'" + policyNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsNexusPaymentsPolicyNumber);
		if (policyNumber.equalsIgnoreCase("random")) {
			WebElement button_DesktopActionsMultiplePaymentsPolicyPicker = table_DesktopActionsNexusPaymentEntry
					.findElement(By
							.xpath("//div[@id='NexusMultiPaymentEntryWizard:NewNexusMultiPaymentScreen:NewMultiPaymentLV:"
									+ (tableRowNumber - 1) + ":PolicyPeriod:SelectPolicyPeriod']"));
			clickWhenClickable(button_DesktopActionsMultiplePaymentsPolicyPicker);
			
			repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
			accountSearch.pickRandomAccount("98");
			
		} else {
			tableUtils.setValueForCellInsideTable(table_DesktopActionsNexusPaymentEntry, "PolicyPeriod", policyNumber);
		}
		
	}

	
	public void setPaymentAmount(int rowNumber, double amountToFill) {
		String amountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsNexusPaymentEntry, "Amount");
		WebElement editBox_DesktopActionsNexusPaymentsAppliedAmount = table_DesktopActionsNexusPaymentEntry
				.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (rowNumber - 1)
						+ "')]/td[contains(@class,'" + amountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsNexusPaymentsAppliedAmount);
		tableUtils.setValueForCellInsideTable(table_DesktopActionsNexusPaymentEntry, "Amount",
				String.valueOf(amountToFill));
		
	}

	
	public void setPaymentMethodInTable(int rowNumber, PaymentInstrumentEnum methodToSelect) {

		tableUtils.selectValueForSelectInTable(table_DesktopActionsNexusPaymentEntry, rowNumber, "Payment Method",
				methodToSelect.getValue());
		
	}
	
	
	public void setPaymentLocationInTable(int rowNumber, PaymentLocation locationToSelect) {

		tableUtils.selectValueForSelectInTable(table_DesktopActionsNexusPaymentEntry, rowNumber, "Payment Location",
				locationToSelect.getValue());
		
	}

	
	public String getDate(int rowNumber) {
		String dateInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsNexusPaymentEntry,
				"Date");
		
		return table_DesktopActionsNexusPaymentEntry
				.findElement(By.xpath(
						".//tbody/tr[" + rowNumber + "]/td[contains(@class,'" + dateInstrumentGridColumnID + "')]/div"))
				.getText();

	}

	
	public void setDate(int rowNumber, Date date) {
		String dateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsNexusPaymentEntry, "Date");
		WebElement editBox_DesktopActionsMultiplePaymentsAppliedAmount = table_DesktopActionsNexusPaymentEntry
				.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (rowNumber - 1)
						+ "')]/td[contains(@class,'" + dateGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiplePaymentsAppliedAmount);
		tableUtils.setValueForCellInsideTable(table_DesktopActionsNexusPaymentEntry, "Date",
				DateUtils.dateFormatAsString("MM/dd/yyyy", date));
		
	}

}
