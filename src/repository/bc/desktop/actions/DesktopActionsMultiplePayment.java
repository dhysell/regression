package repository.bc.desktop.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.desktop.BCDesktopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.MultiPaymentType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.*;

import java.text.ParseException;
import java.util.Date;

public class DesktopActionsMultiplePayment extends BasePage  {
	
	private TableUtils tableUtils;
	
	public DesktopActionsMultiplePayment(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	////////////////////////
	// Recorded WebElements//
	////////////////////////

	@FindBy(xpath = "//div[@id='MultiPaymentEntryWizard:NewMultiPaymentScreen:NewMultiPaymentLV']")
	public WebElement table_DesktopActionsMultiPaymentEntry;

	//////////////////
	// Helper Methods//
	//////////////////
	
	public WebElement getMultiPaymentsTableRow(String policyNumber, MultiPaymentType paymentType, Date paymentDate, PaymentInstrumentEnum paymentInstrument, Double paymentAmount, String paymentNotes) {
		StringBuilder xpathBuilder = new StringBuilder();
		if (!(policyNumber == null)) {
			String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Policy #");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + policyNumberGridColumnID + "') and contains(.,'" + policyNumber + "')]");
		}
		if (!(paymentType == null)) {
			String paymentTypeGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Type");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentTypeGridColumnID + "') and contains(.,'" + paymentType.getValue() + "')]");
		}
		if (!(paymentDate == null)) {
			String paymentDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Date");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate) + "')]");
		}
		if (!(paymentInstrument == null)) {
			String paymentInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Payment Instrument");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentInstrumentGridColumnID + "') and contains(.,'" + paymentInstrument.getValue() + "')]");
		}
		if (!(paymentAmount == null)) {
			String paymentAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Amount");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentAmountGridColumnID + "') and contains(.,'" + StringsUtils.currencyRepresentationOfNumber(paymentAmount) + "')]");
		}
		if (!(paymentNotes == null)) {
			String paymentNotesGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Notes");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentNotesGridColumnID + "') and contains(.,'" + paymentNotes + "')]");
		}

		xpathBuilder.replace(0, 9, ".//");
		WebElement tableRow = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(xpathBuilder.toString()));
		return tableRow;
	}

	
	public void selecMultiPaymentsTableItemCheckbox(String policyNumber, MultiPaymentType paymentType, Date paymentDate, PaymentInstrumentEnum paymentInstrument, Double paymentAmount, String paymentNotes) {
		int tableRow = Integer.valueOf(getMultiPaymentsTableRow(policyNumber, paymentType, paymentDate, paymentInstrument, paymentAmount, paymentNotes).findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex") + 1);
		tableUtils.setCheckboxInTable(table_DesktopActionsMultiPaymentEntry, tableRow, true);
	}

	
	public void removeAllLinesFromMultiPaymentTable() {
		tableUtils.setTableTitleCheckAllCheckbox(table_DesktopActionsMultiPaymentEntry, true);
		clickRemove();
	}

	
	public void removeLineFromMultiPaymentTable(String policyNumber, Date paymentDate, Double paymentAmount) {
		selecMultiPaymentsTableItemCheckbox(policyNumber, null, paymentDate, null, paymentAmount, null);
		clickRemove();
	}

	
	public void setMultiPaymentPolicyNumber(int tableRowNumber, String policyNumber) {
		policyNumber = policyNumber.substring(0, 12);
		String policyNumberGridColumnID = null;
		policyNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Policy #");	
		WebElement editBox_DesktopActionsMultiPaymentsPolicyNumber = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + policyNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiPaymentsPolicyNumber);
		if (policyNumber.equalsIgnoreCase("random")) {
			WebElement button_DesktopActionsMultiPaymentsPolicyPicker = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath("//div[@id='MultiPaymentEntryWizard:NewMultiPaymentScreen:NewMultiPaymentLV:" + (tableRowNumber - 1) + ":PolicyPeriod:SelectPolicyPeriod']"));
			clickWhenClickable(button_DesktopActionsMultiPaymentsPolicyPicker);
			
			// Need to finish method to chose a random Policy if this option is needed.
			// ISearchPolicies policySearch = SearchFactory.getSearchPoliciesPage();
			
		} else {
			tableUtils.setValueForCellInsideTable(table_DesktopActionsMultiPaymentEntry, "PolicyPeriod", policyNumber);
		}
		
	}

	
	public void setMultiPaymentPaymentType(int tableRowNumber, MultiPaymentType paymentType) {
		String paymentTypeGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Type");
		WebElement editBox_DesktopActionsMultiPaymentsPaymentType = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + paymentTypeGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiPaymentsPaymentType);
		tableUtils.selectValueForSelectInTable(table_DesktopActionsMultiPaymentEntry, tableRowNumber, "Type", paymentType.getValue());
		
	}

	
	public void setMultiPaymentPaymentDate(int tableRowNumber, Date paymentDate) {
		String paymentDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Date");
		WebElement editBox_DesktopActionsMultiPaymentsPaymentDate = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + paymentDateGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiPaymentsPaymentDate);
		tableUtils.setValueForCellInsideTable(table_DesktopActionsMultiPaymentEntry, "Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
		
	}

	
	public void setMultiPaymentPaymentInstrument(int tableRowNumber, PaymentInstrumentEnum paymentInstrument) {
		tableUtils.selectValueForSelectInTable(table_DesktopActionsMultiPaymentEntry, tableRowNumber, "Payment Instrument", paymentInstrument.getValue());
		
	}

	
	public void setMultiPaymentPaymentAmount(int tableRowNumber, double paymentAmount) {
		String paymentAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Amount");
		WebElement editBox_DesktopActionsMultiPaymentsAppliedAmount = table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + paymentAmountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiPaymentsAppliedAmount);
		tableUtils.setValueForCellInsideTable(table_DesktopActionsMultiPaymentEntry, "Amount", String.valueOf(paymentAmount));
		
	}

	
	public int fillOutNextLineOnMultiPaymentTable(String policyNumber, PaymentInstrumentEnum paymentInstrument, Double paymentAmount) {
		int newestRow = tableUtils.getNextAvailableLineInTable(table_DesktopActionsMultiPaymentEntry, "Amount");
		if (!(policyNumber == null)) {
			setMultiPaymentPolicyNumber(newestRow, policyNumber);
		}
		if (!(paymentInstrument == null)) {
			setMultiPaymentPaymentInstrument(newestRow, paymentInstrument);
		}
		if (!(paymentAmount == null)) {
			setMultiPaymentPaymentAmount(newestRow, paymentAmount);
		}
		return newestRow;
	}

		public int fillOutNextLineOnMultiPaymentTable(int newestRow, String policyNumber, PaymentInstrumentEnum paymentInstrument, Double paymentAmount) {
		if (!(policyNumber == null)) {
			setMultiPaymentPolicyNumber(newestRow, policyNumber);
		}
		if (!(paymentInstrument == null)) {
			setMultiPaymentPaymentInstrument(newestRow, paymentInstrument);
		}
		if (!(paymentAmount == null)) {
			setMultiPaymentPaymentAmount(newestRow, paymentAmount);
		}
		return newestRow;
	}


	public void makeMultiplePayment(String policyNumber, PaymentInstrumentEnum paymentInstrument, double paymentAmount) throws Exception {
		repository.bc.desktop.BCDesktopMenu desktopMenu = new BCDesktopMenu(getDriver());
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
		
		fillOutNextLineOnMultiPaymentTable(policyNumber, paymentInstrument, paymentAmount);
		
		clickNext();
		
		clickFinish();
	}


    public void makeInsuredMultiplePaymentDownpayment(GeneratePolicy policyObject, String policyNumber) throws Exception {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);

		if ((policyObject.downPaymentType != PaymentType.ACH_EFT) || (policyObject.downPaymentType != PaymentType.Credit_Debit)) {
            if (new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).getDownPaymentAmount() != 0.00) {

				
				PaymentInstrumentEnum paymentInstrument;
				switch (policyObject.downPaymentType) {
				case Cash:
					paymentInstrument = PaymentInstrumentEnum.Cash;
					break;
				case Check:
					paymentInstrument = PaymentInstrumentEnum.Check;
					break;
				case Cash_Equivalent:
					paymentInstrument = PaymentInstrumentEnum.Check_Cash_Equivalent;
					break;
				case Title_Company: 
					paymentInstrument = PaymentInstrumentEnum.Check_Title_Company;
					break;
				default:
					paymentInstrument = PaymentInstrumentEnum.Cash;
				}

                makeMultiplePayment(policyNumber, paymentInstrument, new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).getDownPaymentAmount());
				
			}

            new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice_Due);
		}
	}

	
	public String getMultiPayementPaymentInstrument(String policyNumber, MultiPaymentType paymentType, Date paymentDate, PaymentInstrumentEnum paymentInstrument, Double paymentAmount, String paymentNotes) {
		String paymentInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Payment Instrument");
		WebElement paymentInstrumentRow = getMultiPaymentsTableRow(policyNumber, paymentType, paymentDate, paymentInstrument, paymentAmount, paymentNotes);
		
		return paymentInstrumentRow.findElement(By.xpath(".//parent::tr/td[contains(@class,'" + paymentInstrumentGridColumnID + "')]/div")).getText();
	}

	
	public String getMultiPayementPaymentInstrument(int rowNumber) {
		String paymentInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Payment Instrument");
		
		return table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[" + rowNumber + "]/td[contains(@class,'" + paymentInstrumentGridColumnID + "')]")).getText();
	}

	
	public Date getMultiPaymentPaymentDate(String policyNumber, MultiPaymentType paymentType, Date paymentDate, PaymentInstrumentEnum paymentInstrument, Double paymentAmount, String paymentNotes) {
		String dateInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Date");
		WebElement paymentDateRow = getMultiPaymentsTableRow(policyNumber, paymentType, paymentDate, paymentInstrument, paymentAmount, paymentNotes);
		
		Date multiPaymentDate = null;
		try {
			multiPaymentDate = DateUtils.convertStringtoDate(paymentDateRow.findElement(By.xpath(".//parent::tr/td[contains(@class,'" + dateInstrumentGridColumnID + "')]/div")).getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
			systemOut("The method was unable to correctly parse the string returned into a date. Please investigate and fix.");
			multiPaymentDate = null;
		}
		return multiPaymentDate;
	}

	
	public Date getMultiPaymentPaymentDate(int rowNumber) {
		String dateInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsMultiPaymentEntry, "Date");
		
		Date multiPaymentDate = null;
		try {
			multiPaymentDate = DateUtils.convertStringtoDate(table_DesktopActionsMultiPaymentEntry.findElement(By.xpath(".//tbody/tr[" + rowNumber + "]/td[contains(@class,'" + dateInstrumentGridColumnID + "')]/div")).getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
			systemOut("The method was unable to correctly parse the string returned into a date. Please investigate and fix.");
			multiPaymentDate = null;
		}
		return multiPaymentDate;
	}
}
