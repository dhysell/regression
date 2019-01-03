package repository.bc.desktop.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class DesktopActionsElectronicPaymentEntry extends BasePage  {
	
	private TableUtils tableUtils;
	public DesktopActionsElectronicPaymentEntry(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}
	@FindBy(xpath = "//div[@id='NexusMultiPaymentEntryWizard:NewNexusMultiPaymentScreen:NewMultiPaymentLV']")
	public WebElement table_DesktopActionsElectronicPaymenEntry;


	public void clickNext() {
		super.clickNext();
	}

	
	public void clickFinish() {
		super.clickFinish();
	}

	
	public void setPolicyNumber(int tableRowNumber, String policyNumber) {
		tableUtils.clickCellInTableByRowAndColumnName(table_DesktopActionsElectronicPaymenEntry, tableRowNumber, "Policy #");
		if (policyNumber.equalsIgnoreCase("random")) {
			WebElement button_DesktopActionsMultiplePaymentsPolicyPicker = table_DesktopActionsElectronicPaymenEntry.findElement(By.xpath("//div[@id='NexusMultiPaymentEntryWizard:NewNexusMultiPaymentScreen:NewMultiPaymentLV:" + (tableRowNumber - 1) + ":PolicyPeriod:SelectPolicyPeriod']"));
			clickWhenClickable(button_DesktopActionsMultiplePaymentsPolicyPicker);
			
			repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
			accountSearch.pickRandomAccount("98");
			
		} else {
			tableUtils.setValueForCellInsideTable(table_DesktopActionsElectronicPaymenEntry, "PolicyPeriod", policyNumber);
		}
		
	}

	
	public void setPaymentAmount(int rowNumber, double amountToFill) {
		tableUtils.setValueForCellInsideTable(table_DesktopActionsElectronicPaymenEntry, tableUtils.getCellWebElementInTableByRowAndColumnName(table_DesktopActionsElectronicPaymenEntry, rowNumber, "Amount"), "Amount", String.valueOf(amountToFill));
		
	}

	
	public void setPaymentMethodInTable(int rowNumber, PaymentInstrumentEnum methodToSelect) {
		tableUtils.selectValueForSelectInTable(table_DesktopActionsElectronicPaymenEntry, rowNumber, "Payment Method", methodToSelect.getValue());
		
	}
	
	public void setPaymentLocationInTable(int rowNumber, PaymentLocation locationToSelect) {
		tableUtils.selectValueForSelectInTable(table_DesktopActionsElectronicPaymenEntry, rowNumber, "Payment Location", locationToSelect.getValue());
		
	}
	
	public String getDate(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopActionsElectronicPaymenEntry, rowNumber, "Date");
	}
	
	public void setDate(int rowNumber, Date date) {
		tableUtils.setValueForCellInsideTable(table_DesktopActionsElectronicPaymenEntry, tableUtils.getCellWebElementInTableByRowAndColumnName(table_DesktopActionsElectronicPaymenEntry, rowNumber, "Date"), "Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
		
	}
	
	public int fillOutNextLineOnElectronicPaymentTable(String policyNumber, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, Double paymentAmount) {
		int newestRow = tableUtils.getNextAvailableLineInTable(table_DesktopActionsElectronicPaymenEntry, "Amount");
		if (!(policyNumber == null)) {
			setPolicyNumber(newestRow, policyNumber);
		}
		if (!(paymentInstrument == null)) {
			setPaymentMethodInTable(newestRow, paymentInstrument);
		}
		if (!(paymentLocation == null)) {
			setPaymentLocationInTable(newestRow, paymentLocation);
		}
		if (!(paymentAmount == null)) {
			setPaymentAmount(newestRow, paymentAmount);
		}
		return newestRow;
	}

	public int fillOutNextLineOnElectronicPaymentTable(int newestRow, String policyNumber, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, Double paymentAmount) {
		if (!(policyNumber == null)) {
			setPolicyNumber(newestRow, policyNumber);
		}
		if (!(paymentInstrument == null)) {
			setPaymentMethodInTable(newestRow, paymentInstrument);
		}
		if (!(paymentLocation == null)) {
			setPaymentLocationInTable(newestRow, paymentLocation);
		}
		if (!(paymentAmount == null)) {
			setPaymentAmount(newestRow, paymentAmount);
		}
		return newestRow;
	}

	public void makeMultiplePayment(String policyNumber, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, double paymentAmount) throws Exception {
		repository.bc.desktop.BCDesktopMenu desktopMenu = new BCDesktopMenu(getDriver());
		desktopMenu.clickDesktopMenuActionsElectronicPayment();
		
		fillOutNextLineOnElectronicPaymentTable(policyNumber, paymentInstrument, paymentLocation, paymentAmount);
		
		clickNext();
		
		clickFinish();
	}
}
