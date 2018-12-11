package repository.bc.desktop.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class DesktopActionsLienholderMutipleAccountPaymentWorkflow extends BasePage  {
	
	private TableUtils tableUtils;
	
	public DesktopActionsLienholderMutipleAccountPaymentWorkflow(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	////////////////////////
	// Recorded WebElements//
	////////////////////////

	@FindBy(xpath = "//div[contains(@id,'MultipleAccountLienholderPaymentWizard:AddPolicyPaymentScreen:DirectBillMoneyDetailsLV') or contains(@id,'MultipleAccountLienholderPaymentWizard:AddAccountPaymentScreen:DirectBillMoneyDetailAccountPaymentLV')]")
	public WebElement table_DesktopActionsLienholderMultipleAccountPayments;

	@FindBy(xpath = "//div[@id='LienholderLoanNumberPopup:1']")
	public WebElement table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker;

	@FindBy(xpath = "//div[contains(@id,':AppliedTotal-inputEl')]")
	public WebElement text_DesktopActionsLienholderMultipleAccountPaymentsAppliedTotal;

	@FindBy(xpath = "//div[contains(@id,':ToReconcile-inputEl')]")
	public WebElement text_DesktopActionsLienholderMultipleAccountPaymentsAmountToReconcile;
	
	@FindBy(xpath = "//a[contains(@id,'LienholderLoanNumberPopup:__crumb__')]")
	public WebElement link_ReturnToAddPolicyPayments;
	
	@FindBy(xpath = "//span[contains(@id,':AddPolicyPaymentScreen:NotEqualWarning')]")
	public WebElement banner_NotEqualWarning;

	//////////////////
	// Helper Methods//
	//////////////////

	
	public WebElement getDesktopActionsLienholderMultipleAccountPaymentsTable() {
		waitUntilElementIsVisible(table_DesktopActionsLienholderMultipleAccountPayments);
		return table_DesktopActionsLienholderMultipleAccountPayments;
	}
	
	
	public void clickAdd() {
		super.clickAdd();
	}

	
	public void clickRemove() {
		super.clickRemove();
	}

	
	public void clickCancel() {
		super.clickCancel();
	}

	
	public void clickNext() {
		super.clickNext();
	}

	
	public void clickFinish() {
		super.clickFinish();
	}

	
	public void clickBack() {
		super.clickBack();
	}

	
	public void clickReturnToPolicyPaymentsLink() {
		clickWhenClickable(link_ReturnToAddPolicyPayments);
		
	}	
	
	public boolean notEqualWarningBannerExists() {
		return checkIfElementExists(banner_NotEqualWarning, 500);
	}
	
	
	public String getNotEqualWarningBannerText() {
		waitUntilElementIsVisible(banner_NotEqualWarning);
		return banner_NotEqualWarning.getText();
	}
	
	
	public boolean isNextButtonDisabled() {
        return isElementDisabled(find(By.cssSelector("a[id$=':Next']")));
	}

	
	public double getAmountAppliedToTotal() {
		waitUntilElementIsVisible(text_DesktopActionsLienholderMultipleAccountPaymentsAppliedTotal);
		return NumberUtils.getCurrencyValueFromElement(text_DesktopActionsLienholderMultipleAccountPaymentsAppliedTotal);
	}

	
	public double getRemainingAmountToReconcile() {
		waitUntilElementIsVisible(text_DesktopActionsLienholderMultipleAccountPaymentsAmountToReconcile);
		return NumberUtils.getCurrencyValueFromElement(text_DesktopActionsLienholderMultipleAccountPaymentsAmountToReconcile);
	}


    @FindBy(css = "input[id$=':amount-inputEl']")
    private WebElement input_Amount;

	public void setAmount(double amountToApply) {
        setText(input_Amount, String.valueOf(amountToApply));
		clickProductLogo();
	}

	
	public WebElement getLienholderMultipleAccountPaymentsTableRow(Double appliedAmount, Date paymentDate, PaymentInstrumentEnum paymentInstrument, String policyNumber, String loanNumber, String lienholderName, String lienholderAccountNumber, String insuredName) {
		StringBuilder xpathBuilder = new StringBuilder();
		if (!(appliedAmount == null)) {
			String appliedAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Applied Amount");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + appliedAmountGridColumnID + "') and contains(.,'" + StringsUtils.currencyRepresentationOfNumber(appliedAmount) + "')]");
		}
		if (!(paymentDate == null)) {
			String paymentDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "Date");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate) + "')]");
		}
		if (!(paymentInstrument == null)) {
			String paymentInstrumentGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "Payment Instrument");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + paymentInstrumentGridColumnID + "') and contains(.,'" + paymentInstrument.getValue() + "')]");
		}
		if (!(policyNumber == null)) {
			String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Policy");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + policyNumberGridColumnID + "') and contains(.,'" + policyNumber + "')]");
		}
		if (!(loanNumber == null)) {
			String loanNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Loan Number");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + loanNumberGridColumnID + "') and contains(.,'" + loanNumber + "')]");
		}
		if (!(lienholderName == null)) {
			String lienholderNameGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "Lienholder Name");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + lienholderNameGridColumnID + "') and contains(.,'" + lienholderName + "')]");
		}
		if (!(lienholderAccountNumber == null)) {
			String lienholderAccountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "Lienholder Account");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + lienholderAccountNumberGridColumnID + "') and contains(.,'" + lienholderAccountNumber + "')]");
		}
		if (!(insuredName == null)) {
			String insuredNameGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "Insured Name");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + insuredNameGridColumnID + "') and contains(.,'" + insuredName + "')]");
		}

		xpathBuilder.replace(0, 9, ".//");
		WebElement tableRow = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath(xpathBuilder.toString()));
		return tableRow;
	}

	
	public void selectLienholderMultipleAccountPaymentsTableItemCheckbox(Double appliedAmount, Date paymentDate, PaymentInstrumentEnum paymentInstrument, String policyNumber, String loanNumber, String lienholderName, String lienholderAccountNumber, String insuredName) {
		int tableRow = tableUtils.getRowNumberFromWebElementRow(getLienholderMultipleAccountPaymentsTableRow(appliedAmount, paymentDate, paymentInstrument, policyNumber, loanNumber, lienholderName, lienholderAccountNumber, insuredName).findElement(By.xpath(".//parent::td/parent::tr")));
		tableUtils.setCheckboxInTable(table_DesktopActionsLienholderMultipleAccountPayments, tableRow, true);
	}

	
	public void removeAllLinesFromTable() {
		tableUtils.setTableTitleCheckAllCheckbox(table_DesktopActionsLienholderMultipleAccountPayments, true);
		clickRemove();
	}

	
	public void removeLineFromTable(String policyNumber, Double appliedAmount, String loanNumber) {
		selectLienholderMultipleAccountPaymentsTableItemCheckbox(appliedAmount, null, null, policyNumber, loanNumber, null, null, null);
		clickRemove();
	}
	
	
	public void setPaymentDate(Date paymentDate, int tableRowNumber) {
		tableUtils.setValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, tableRowNumber, "Date", "PaymentDate", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
		
	}
	
	
	public void setPaymentInstrument(PaymentInstrumentEnum paymentInstrument, int tableRowNumber) {
		tableUtils.selectValueForSelectInTable(table_DesktopActionsLienholderMultipleAccountPayments, tableRowNumber, "Payment Instrument", paymentInstrument.getValue());
		
	}

	
	public void setPolicyNumber(String policyNumber, int tableRowNumber) {
		String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Policy");
		WebElement editBox_DesktopActionsLienholderMultipleAccountPaymentsPolicyNumber = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + policyNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsLienholderMultipleAccountPaymentsPolicyNumber);
		tableUtils.setValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, "policy", policyNumber);
		
	}

	
	public void setAccountNumber(String accountNumber, int tableRowNumber) {
		String accountNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Account");
		WebElement editBox_DesktopActionsLienholderMultipleAccountPaymentsAccountNumber = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + accountNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsLienholderMultipleAccountPaymentsAccountNumber);
		if (accountNumber.equalsIgnoreCase("random")) {
			WebElement button_DesktopActionsLienholderMultipleAccountPaymentsAccountPicker = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath("//div[@id='MultipleAccountLienholderPaymentWizard:AddAccountPaymentScreen:DirectBillMoneyDetailAccountPaymentLV:" + (tableRowNumber - 1) + ":Account:SelectAccount']"));
			clickWhenClickable(button_DesktopActionsLienholderMultipleAccountPaymentsAccountPicker);
			
			repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
			accountSearch.pickRandomAccount("98");
			
		} else {
			tableUtils.setValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, "Account", accountNumber);
		}
		
	}

	
	public void setAppliedAmount(double appliedAmount, int tableRowNumber) {
		String appliedAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Applied Amount");
		WebElement editBox_DesktopActionsLienholderMultipleAccountPaymentsAppliedAmount = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + appliedAmountGridColumnID + "')]/div"));
		clickProductLogo();
		clickWhenClickable(editBox_DesktopActionsLienholderMultipleAccountPaymentsAppliedAmount);
		
		tableUtils.setValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, "Amount", String.valueOf(appliedAmount));
		
	}

	
	public String getLienholderName(int tableRowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopActionsLienholderMultipleAccountPayments, tableRowNumber, "Lienholder Name");
	}
	
	
	public void setNotes(String notes, int tableRowNumber) {
		tableUtils.setValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, tableRowNumber, "Notes", "Notes", notes);
		
	}
	
	
	public void setPayRecaptureOption(boolean payRecaptureTrueFalse, int tableRowNumber) {
		tableUtils.setRadioValueForCellInsideTable(table_DesktopActionsLienholderMultipleAccountPayments, tableRowNumber, "Pay Recapture Only", payRecaptureTrueFalse);
		
	}

	
	public void setLoanNumber(String loanNumber, int tableRowNumber) {
		String loanNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Loan Number");
		WebElement editBox_DesktopActionsLienholderMultipleAccountPaymentsLoanNumber = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + loanNumberGridColumnID + "')]/div"));
		clickProductLogo();
		clickWhenClickable(editBox_DesktopActionsLienholderMultipleAccountPaymentsLoanNumber);
		WebElement button_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker = table_DesktopActionsLienholderMultipleAccountPayments.findElement(By.xpath("//div[@id='MultipleAccountLienholderPaymentWizard:AddPolicyPaymentScreen:DirectBillMoneyDetailsLV:" + (tableRowNumber - 1) + ":LoanNumber:SelectLoanNumber']"));
		clickWhenClickable(button_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker);
		
		
		if (loanNumber.equalsIgnoreCase("latestPolicyPeriod")) {
			int loanNumberPickerTableRowCount = tableUtils.getRowCount(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker);
			tableUtils.clickSelectLinkInTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, loanNumberPickerTableRowCount);//adding this because to select the last row which brings you latest policy period , If we do random you may end up selecting canceled Policy Period
			
		} else if (loanNumber.equalsIgnoreCase("random")) {
			int loanNumberPickerTableRowCount = tableUtils.getRowCount(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker);
			tableUtils.clickSelectLinkInTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, NumberUtils.generateRandomNumberInt(1, loanNumberPickerTableRowCount));
			
		} else {
			String loanPickerLoanNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, "Loan Number");
			int loanNumberRow = Integer.valueOf(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker.findElement(By.xpath(".//tr/td[contains(@class,'" + loanPickerLoanNumberGridColumnID + "') and contains(.,'" + loanNumber + "')]/parent::tr")).getAttribute("data-recordindex")) + 1;
			tableUtils.clickSelectLinkInTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, loanNumberRow);
		}
	}

	
	public int fillOutNextLineOnPolicyPaymentsTable(Double appliedAmount, Date paymentDate, PaymentInstrumentEnum paymentInstrument, String policyNumber, String loanNumber) {
		int newestRow = tableUtils.getNextAvailableLineInTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Applied Amount");
		if (appliedAmount != null) {
			setAppliedAmount(appliedAmount, newestRow);
		}
		if (paymentDate != null) {
			setPaymentDate(paymentDate, newestRow);
		}
		if (paymentInstrument != null) {
			setPaymentInstrument(paymentInstrument, newestRow);
		}
		if (policyNumber != null) {
			setPolicyNumber(policyNumber, newestRow);
		}
		if (loanNumber != null) {
			setLoanNumber(loanNumber, newestRow);
		}
		return newestRow;
	}

	
	public int fillOutNextLineOnAdditionalPaymentsTable(String accountNumber, Double appliedAmount, PaymentInstrumentEnum paymentInstrument, String notes, Boolean payRecaptureTrueFalse) {
		int newestRow = tableUtils.getNextAvailableLineInTable(table_DesktopActionsLienholderMultipleAccountPayments, "*Account");
		if (accountNumber != null) {
			setAccountNumber(accountNumber, newestRow);
		}
		if (appliedAmount != null) {
			setAppliedAmount(appliedAmount, newestRow);
		}
		if (paymentInstrument != null) {
			setPaymentInstrument(paymentInstrument, newestRow);
		}
		if (notes != null) {
			setNotes(notes, newestRow);
		}
		if (payRecaptureTrueFalse != null) {
			setPayRecaptureOption(payRecaptureTrueFalse, newestRow);
		}
		return newestRow;
	}
	public String getTopInfo (WebDriver driver){
		return driver.findElement(By.xpath(".//div[contains(@id, '_msgs')]/div[1]")).getText();
	}
}
