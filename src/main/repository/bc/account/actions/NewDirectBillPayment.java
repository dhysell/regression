package repository.bc.account.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.*;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * "Modify Payment" screen (Payments ->Actions->Modify Distribution) has the
 * same elements as New Direct Bill Payment screen, so they share the same repository.
 */
public class NewDirectBillPayment extends BasePage  {
	
	private WebDriver driver;	
	private TableUtils tableUtils;
	
	public NewDirectBillPayment(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}
	
	/////////////////////////
	//Elements and Locators//
	/////////////////////////
	
	@FindBy(xpath = "//input[contains(@id, ':Amount-inputEl') or contains(@id, ':amount-inputEl')]")
	public WebElement editbox_BCNewDirectBillPaymentAmount;
	
	@FindBy(xpath = "//div[contains(@id,':EditDBPaymentScreen:DistributionAmountsLV')]")
	public WebElement table_DistributionAmounts;
	
	@FindBy(xpath = "//div[@id='LienholderLoanNumberPopup:1']")
	public WebElement table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker;
	
	@FindBy(xpath = "//input[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:policy-inputEl')]")
	public WebElement editBox_PolicyNumber;
	
	@FindBy(xpath = "//input[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:loannumber-inputEl')]")
	public WebElement editBox_LoanNumber;
	
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:policy-inputEl')]")
	public WebElement label_PolicyNumber;
	
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:loannumber-inputEl')]")
	public WebElement label_LoanNumber;
	
	/**
	 * the following elements are for "This Payment" or "This Credit Distribution" areas
	 */
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:AmountAvailableToDistribute-inputEl')]")
	public WebElement label_AmountAvailableToDistribute;
	
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:DistributedAmount-inputEl')]")
	public WebElement label_DistributedAmount;
	
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:CollateralAmount-inputEl')]")
	public WebElement label_CollateralAmount;
	
	@FindBy(xpath = "//div[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:RemainingAmount-inputEl')]")
	public WebElement label_RemainingAmount;
	
	@FindBy(xpath = "//div[@id='ModifyDirectBillPaymentPopup:EditDBPaymentScreen:PaymentDetailsDV:loanNumber:SelectloanNumber']")
	public WebElement link_SearchLoanNumber;
	
	@FindBy(xpath = "//a[contains(@id, 'PaymentInstrument:CreateNewPaymentInstrument')]")
	public WebElement link_NewPaymentInstrument;
	
	@FindBy(xpath = "//a[contains(@id,':EditDBPaymentScreen:distributionCardTab')]")
	public WebElement tab_NewDirectBillPaymentDistributionTab;
	
	@FindBy(xpath = "//a[contains(@id,':EditDBPaymentScreen:Update')]")
	public WebElement button_NewDirectBillPaymentExecute;

	@FindBy(xpath = "//a[contains(@id,':EditDBPaymentScreen:ExecuteWithoutDistribution')]")
	public WebElement button_NewDirectBillPaymentExecuteWithoutDistribution;

	@FindBy(xpath = "//a[contains(@id,':EditDBPaymentScreen:OverrideModeButton')]")
	public WebElement button_NewDirectBillPaymentOverrideDistribution;

	@FindBy(xpath = "//input[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:UseUnappliedFunds-inputEl')]")
	public WebElement checkbox_UseUnappliedFundAmount;	
	
	public Guidewire8Select select_PaymentInstrument() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'PaymentDetailsDV:PaymentInstrument-triggerWrap') or contains(@id, 'NewPaymentInstrumentPopup:PaymentMethod-triggerWrap') ]");
	}
	
	public Guidewire8Select select_NewDirectBillPaymentUnappliedFund() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':PaymentDetailsDV:UnappliedFunds-triggerWrap')]");
	}
	
	public Guidewire8Select select_DistributionIncludeOnly() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditDBPaymentScreen:includeOnly-triggerWrap')]");
	}

	public Guidewire8Select select_PolicyNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:policy-triggerWrap')]");
	}
	
	public Guidewire8Select select_LoanNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':EditDBPaymentScreen:PaymentDetailsDV:loannumber-triggerWrap')]");
	}
	
	///////////////////////
	//Interaction Methods//
	///////////////////////
	
	public void setPaymentInstrument(PaymentInstrumentEnum inst) {
		select_PaymentInstrument().selectByVisibleTextPartial(inst.getValue());
	}
	
	public void clickNewPaymentInstrument() {
		clickWhenClickable(link_NewPaymentInstrument);
	}
	
	public void clickExecute() {
		clickWhenVisible(button_NewDirectBillPaymentExecute);
		
	}

	
	public void clickExecuteWithoutDistribution() {
		clickWhenVisible(button_NewDirectBillPaymentExecuteWithoutDistribution);
		
	}

	
	public void clickOverrideDistribution() {
		clickWhenVisible(button_NewDirectBillPaymentOverrideDistribution);
		
	}
	
	public void setAmount(double amount) {
		DecimalFormat df = new DecimalFormat("####.##");
		clickWhenClickable(editbox_BCNewDirectBillPaymentAmount);
		editbox_BCNewDirectBillPaymentAmount.sendKeys(Keys.CONTROL + "a");
		editbox_BCNewDirectBillPaymentAmount.sendKeys(df.format(amount));
		clickProductLogo();
		
	}

	
	public void setUnappliedFund(String policyNumberOrUnappliedFund) {
		List<WebElement> buttonElements = finds(By.xpath("//table[contains(@id, ':PaymentDetailsDV:UnappliedFunds-triggerWrap')]"));
		if (buttonElements.size() > 0) {
			select_NewDirectBillPaymentUnappliedFund().selectByVisibleTextPartial(policyNumberOrUnappliedFund);
		} else {
			System.out.println("The Unapplied Funds Select button was not on the payment page. If this is incorrect, please investigate.");
		}
	}
	
	public void makeDirectBillPaymentExecute(double amount, String policyNumberOrUnappliedFund) {
		setAmount(amount);
		
		setUnappliedFund(policyNumberOrUnappliedFund);
		
		clickExecute();
		
	}
	
	private void creditDebitOrACHPaymentGuts(String policyNumberOrUnappliedFund, PaymentInstrumentEnum type) {
		clickNewPaymentInstrument();
		
		setPaymentInstrument(type);
		
		repository.bc.account.actions.NewPaymentInstrument pmtInst = new repository.bc.account.actions.NewPaymentInstrument(getDriver());
		if(type.equals(PaymentInstrumentEnum.ACH_EFT)) {
			pmtInst.fillOutACHEFTDetails(BankAccountType.Business_Checking, "Test Payment", "124000054", "123456", policyNumberOrUnappliedFund);
		} else {
			pmtInst.fillOutCreditDebitDetails("4111111111111111", 12, Calendar.getInstance().get(Calendar.YEAR), 123, policyNumberOrUnappliedFund);
		}
	}
	
	public void makeDirectBillPaymentExecute(double amount, String policyNumberOrUnappliedFund, PaymentInstrumentEnum type, boolean withoutDistribution) {
		if(type.equals(PaymentInstrumentEnum.ACH_EFT) || type.equals(PaymentInstrumentEnum.Credit_Debit)) {
			creditDebitOrACHPaymentGuts(policyNumberOrUnappliedFund, type);
			clickOK();
		}
		
		setAmount(amount);
		
		setUnappliedFund(policyNumberOrUnappliedFund);
		
		if(withoutDistribution) {
			clickExecuteWithoutDistribution();
		} else {
			clickExecute();
		}
		
	}
	
	public void makeDirectBillPaymentExecute(double amount, String policyNumberOrUnappliedFund, PaymentInstrumentEnum type, String paymentEmailOverride, boolean withoutDistribution) {
		if(type.equals(PaymentInstrumentEnum.ACH_EFT) || type.equals(PaymentInstrumentEnum.Credit_Debit)) {
			creditDebitOrACHPaymentGuts(policyNumberOrUnappliedFund, type);
			repository.bc.account.actions.NewPaymentInstrument pmtInst = new NewPaymentInstrument(getDriver());
			pmtInst.setEmailAddress(paymentEmailOverride);
			clickOK();
		}
		
		setAmount(amount);
		
		setUnappliedFund(policyNumberOrUnappliedFund);
		
		if(withoutDistribution) {
			clickExecuteWithoutDistribution();
		} else {
			clickExecute();
		}
		
	}
	
	public void makeDirectBillPaymentExecute(String policyNumber, String loanNumber, double amount, PaymentInstrumentEnum paymentInstrument) {
		setAmount(amount);
		
		selectPolicyNumber(policyNumber);
		
		selectLoanNumber(loanNumber);
		
		setPaymentInstrument(paymentInstrument);
		
		clickExecute();
		
	}
	
	public void makeDirectBillPaymentExecute(String policyNumber, String loanNumber, double amount) {
		setAmount(amount);
		
		selectPolicyNumber(policyNumber);
		
		selectLoanNumber(loanNumber);
		
		clickExecute();
		
	}
	
	public void makeDirectBillPaymentExecute(double amount) {
		setAmount(amount);
		
		clickExecute();
		
	}
	
	public void makeDirectBillPaymentExecuteWithoutDistribution(double amount, String policyNumberOrUnappliedFund) {
		setAmount(amount);
		
		setUnappliedFund(policyNumberOrUnappliedFund);
		
		clickExecuteWithoutDistribution();
		
	}
	
	public void makeDirectBillPaymentExecuteWithoutDistribution(double amount) {
		setAmount(amount);
		
		clickExecuteWithoutDistribution();
		
	}
	
	public void makeDirectBillPaymentExecuteWithoutDistribution(String policyNumber, String loanNumber, double amount) {
		setAmount(amount);
		
		selectPolicyNumber(policyNumber);
		
		selectLoanNumber(loanNumber);
		
		clickExecuteWithoutDistribution();
		
	}
	
	public void clickDistributionTab() {
		clickWhenVisible(tab_NewDirectBillPaymentDistributionTab);
	}
	
	public void selectDistributionTabIncludeOnly(IncludeOnly includeOnly) {
		select_DistributionIncludeOnly().selectByVisibleText(includeOnly.getValue());
	}
	
	public void setOverrideAmount(String invoiceNumber, double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Invoice #", invoiceNumber);
		columnRowKeyValuePairs.put("Override Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		
		WebElement tableRow = tableUtils.getRowInTableByColumnsAndValues(table_DistributionAmounts, columnRowKeyValuePairs);
		tableUtils.setValueForCellInsideTable(table_DistributionAmounts, tableUtils.getRowNumberFromWebElementRow(tableRow), "Override Amount", "c14", String.valueOf(amount));
		
	}

	// when have multiple rows to fill out the override amount
	public void setOverrideAmount(String invoiceNumber, String accountNumber) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Invoice #", invoiceNumber);
		columnRowKeyValuePairs.put("Policy Period #", accountNumber);
		
		List<WebElement> rowsWithSameAcctNumAndInvNum = tableUtils.getRowsInTableByColumnsAndValues(table_DistributionAmounts, columnRowKeyValuePairs);
		

		int loop;
		for (loop = 0; loop < rowsWithSameAcctNumAndInvNum.size(); loop++) {
			// if there are multiple rows to fill out, after one row is filled,
			// the page refreshed automatically and all the gridColumn IDs will
			// change, so need to get them again
			if (loop != 0) {
				rowsWithSameAcctNumAndInvNum = tableUtils.getRowsInTableByColumnsAndValues(table_DistributionAmounts, columnRowKeyValuePairs);
			}
			
			WebElement row = rowsWithSameAcctNumAndInvNum.get(loop);
			double rowUnpaidAmount = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_DistributionAmounts, row, "Unpaid Amount"));
			tableUtils.setValueForCellInsideTable(table_DistributionAmounts, tableUtils.getRowNumberFromWebElementRow(row), "Override Amount", "c14", String.valueOf(rowUnpaidAmount));
			
		}
	}

	/**
	 * This method is used to make an insured downpayment. It will handle
	 * deciding if the downpayment is even necessary and act accordingly.
	 * 
	 * @param policyObject
	 *            The Policy object needed to determine what payment method was
	 *            used
	 * @param amount
	 *            Amount to use to make the payment, as a double
	 * @param policyNumberOrUnappliedFund
	 *            Used to choose which unapplied fund you would
	 *            like to use. If you want this to default, simply pass a null
	 *            argument here.
	 */
	public void makeInsuredDownpayment(GeneratePolicy policyObject, double amount, String policyNumberOrUnappliedFund) {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);
		if (!((policyObject.downPaymentType == PaymentType.ACH_EFT) || (policyObject.downPaymentType == PaymentType.Credit_Debit))) {
			if (amount != 0.00) {
				repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver());
				accountMenu.clickAccountMenuActionsNewDirectBillPayment();
	
				if (policyNumberOrUnappliedFund == null) {
					makeDirectBillPaymentExecuteWithoutDistribution(amount);
				} else {
					makeDirectBillPaymentExecute(amount, policyNumberOrUnappliedFund);
				}
			}
			//new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice_Due);
		}
	}
	
	//THIS METHOD WILL RUN THE INVOICE BATCH, BUT WILL NOT RUN THE INVOICE DUE BATCH.
	public void makeInsuredDownpaymentForPolicyLoaderCustom(GeneratePolicy policyObject, double amount, String policyNumberOrUnappliedFund) {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);
		if (!((policyObject.downPaymentType == PaymentType.ACH_EFT) || (policyObject.downPaymentType == PaymentType.Credit_Debit))) {
			if (amount != 0.00) {
				repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver());
				accountMenu.clickAccountMenuActionsNewDirectBillPayment();
	
				if (policyNumberOrUnappliedFund == null) {
					makeDirectBillPaymentExecuteWithoutDistribution(amount);
				} else {
					makeDirectBillPaymentExecute(amount, policyNumberOrUnappliedFund);
				}
			}
		}
	}
	
	public void makeLienHolderPaymentExecute(double amount, String policyNumber, String loanNumber, PaymentInstrumentEnum paymentInstrument) {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);
		if (amount != 0.00) {
			repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver());
			accountMenu.clickAccountMenuActionsNewDirectBillPayment();

			makeDirectBillPaymentExecute(policyNumber, loanNumber, amount, paymentInstrument);
		}
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	public void makeLienHolderPaymentExecute(double amount, String policyNumber, String loanNumber) {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);
		if (amount != 0.00) {
			repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver());
			accountMenu.clickAccountMenuActionsNewDirectBillPayment();

			makeDirectBillPaymentExecute(policyNumber, loanNumber, amount);
		}
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	public void makeLienHolderPaymentExecuteWithoutDistribution(double amount, String policyNumber, String loanNumber) {
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice);
		if (amount != 0.00) {
			repository.bc.account.BCAccountMenu accountMenu = new BCAccountMenu(getDriver());
			accountMenu.clickAccountMenuActionsNewDirectBillPayment();

			makeDirectBillPaymentExecuteWithoutDistribution(policyNumber, loanNumber, amount);
		}
		new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	public void setOverrideAmount(String invoiceNumber, double amount, String acctNum) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Invoice #", invoiceNumber);
		columnRowKeyValuePairs.put("Override Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		columnRowKeyValuePairs.put("Policy Period #", acctNum);
		
		WebElement amountInputRow = tableUtils.getRowInTableByColumnsAndValues(table_DistributionAmounts, columnRowKeyValuePairs);
		tableUtils.setValueForCellInsideTable(table_DistributionAmounts, tableUtils.getRowNumberFromWebElementRow(amountInputRow), "Override Amount", "c14' or contains (@name, 'c12", String.valueOf(amount));
		
	}

	// for Modify Distribution
	// this method is for the policy combobox under "Payment Detail"
	public void selectPolicyNumber(String policyNumber) {
		Guidewire8Select mySelect = select_PolicyNumber();
		mySelect.selectByVisibleTextPartial(policyNumber);
		
	}
	
	public void selectLoanNumber(String loanNumber) {
		Guidewire8Select mySelect = select_LoanNumber();
		mySelect.selectByVisibleTextPartial(loanNumber);
		
	}
	
	public void clickSearchLoanNumber() {
		clickWhenVisible(link_SearchLoanNumber);
	}
	
	public void setLoanNumber(String loanNumber) {
		clickSearchLoanNumber();
		if (loanNumber.equalsIgnoreCase("random")) {
			int loanNumberPickerTableRowCount = tableUtils.getRowCount(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker);
			tableUtils.clickSelectLinkInTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, NumberUtils.generateRandomNumberInt(0, loanNumberPickerTableRowCount));
			
		} else {
			String loanPickerLoanNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, "Loan Number");
			int loanNumberRow = Integer.valueOf(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker.findElement(By.xpath(".//tr/td[contains(@class,'" + loanPickerLoanNumberGridColumnID + "') and contains(.,'" + loanNumber + "')]/parent::tr")).getAttribute("data-recordindex")) + 1;
			tableUtils.clickSelectLinkInTable(table_DesktopActionsLienholderMultipleAccountPaymentsLoanNumberPicker, loanNumberRow);
		}
	}
	
	public void setOverrideAmount(String item, String policyPeriodNumber, String loanNumber, String invoiceNumber, Date invoiceDate, Date invoiceDueDate, Double amount, Double overrideAmount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (item != null) {
			columnRowKeyValuePairs.put("Item", item);
		}
		if (policyPeriodNumber != null) {
			columnRowKeyValuePairs.put("Policy Period #", policyPeriodNumber);
		}
		if (loanNumber != null) {
			columnRowKeyValuePairs.put("Loan Number", loanNumber);
		}
		if (invoiceNumber != null) {
			columnRowKeyValuePairs.put("Invoice #", invoiceNumber);
		}
		if (invoiceDate != null) {
			columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
		}
		if (invoiceDueDate != null) {
			columnRowKeyValuePairs.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDueDate));
		}
		if (amount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		}

		WebElement rowToSetOverrideAmount = tableUtils.getRowInTableByColumnsAndValues(table_DistributionAmounts, columnRowKeyValuePairs);
		tableUtils.setValueForCellInsideTable(table_DistributionAmounts, tableUtils.getRowNumberFromWebElementRow(rowToSetOverrideAmount), "Override Amount", "c14' or contains (@name, 'c12", String.valueOf(overrideAmount));
	}

	
	public void clickUseUnappliedFundAmount() {
		clickWhenVisible(checkbox_UseUnappliedFundAmount);
	}
	
	//The following block contains methods for working with the Payment Details section
	
	
	public String getPolicyNumber() {
		waitUntilElementIsVisible(label_PolicyNumber);
		return label_PolicyNumber.getText();
	}
	
	
	public boolean checkIfPolicyNumberIsEditable() {
		return checkIfElementExists(editBox_PolicyNumber, 1000);
	}
	
	
	public String getLoanNumber() {
		waitUntilElementIsVisible(label_LoanNumber);
		return label_LoanNumber.getText();
	}
	
	
	public boolean checkIfLoanNumberIsEditable() {
		return checkIfElementExists(editBox_LoanNumber, 1000);
	}
	
	/**
	 * the following methods are for "This Payment" or "This Credit Distribution" areas
	 */		
	
	
	public double getAvailableAmount() {
		return NumberUtils.getCurrencyValueFromElement(label_AmountAvailableToDistribute.getText());
	}
	
	
	public double getDistributedAmount() {
		return NumberUtils.getCurrencyValueFromElement(label_DistributedAmount.getText());
	}
	
	
	public double getCollateralAmount() {
		return NumberUtils.getCurrencyValueFromElement(label_CollateralAmount.getText());
	}
	
	
	public double getRemainingAmount() {
		return NumberUtils.getCurrencyValueFromElement(label_RemainingAmount.getText());
	}
}
