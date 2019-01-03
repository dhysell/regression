package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsCreditDistributions;
import repository.bc.account.payments.AccountPaymentsTransfers;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountPolicyLoanBalances extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public AccountPolicyLoanBalances(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:balancesLV')]")
    public WebElement table_PolicyLoanBalances;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:ChargesLV')]")
    public WebElement table_PolicyLoanBalancesCharges;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:AccountDBPaymentsLV')]")
    public WebElement table_PolicyLoanBalancesPayments;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:creditsTab:LHAccountDBPaymentsLV')]")
    public WebElement table_PolicyLoanBalancesCredits;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:creditDetail:AccountPaymentDistributionItemsCV:DirectBillDistItemsLV')]")
    public WebElement table_PolicyLoanBalancesCreditsPaymentDetails;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:DisbursementsLV:1')]")
    public WebElement table_PolicyLoanBalancesDisbursements;

    @FindBy(xpath = "//div[contains(@id,':AccountPolicyBalancesScreen:policyLoanBalanceLV:AccountTransfers_FBMLV')]")
    public WebElement table_PolicyLoanBalancesTransfers;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:chargesTab')]")
    public WebElement tab_Charges;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:paymentsTab')]")
    public WebElement tab_Payments;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:creditsTab')]")
    public WebElement tab_Credits;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:disbursementsTab')]")
    public WebElement tab_Disbursements;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:transfersTab')]")
    public WebElement tab_Transfers;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:migrate')]")
    public WebElement button_MigrateAccount;
    
    @FindBy(xpath = "//a[contains(@id, ':ToolbarButton')]")
	private WebElement button_ClearFilters;

    @FindBy(xpath = "//a[contains(@id,'AccountPolicyBalances:AccountPolicyBalancesScreen:recreate')]")
    public WebElement button_RecreateBalances;

    @FindBy(xpath = "//a[contains(@id,':AccountDetailChargesScreen:editHold')]")
    public WebElement button_EditHolds;

    Guidewire8Select select_PolicyFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountPolicyBalancesScreen:policyfilter-triggerWrap')]");
    }

    Guidewire8Select select_LoanNumberFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountPolicyBalancesScreen:loanfilter-triggerWrap')]");
    }

    ///////////////////////////////////////
    // Helper Methods for Above Elements //
    ///////////////////////////////////////

    public WebElement getPolicyLoanBalancesTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalances);
        return table_PolicyLoanBalances;
    }

    public WebElement getPolicyLoanBalancesChargesTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesCharges);
        return table_PolicyLoanBalancesCharges;
    }

    public WebElement getPolicyLoanBalancesPaymentsTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesPayments);
        return table_PolicyLoanBalancesPayments;
    }

    public WebElement getPolicyLoanBalancesCreditsTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesCredits);
        return table_PolicyLoanBalancesCredits;
    }

    public WebElement getPolicyLoanBalancesCreditsPaymentDetailsTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesCreditsPaymentDetails);
        return table_PolicyLoanBalancesCreditsPaymentDetails;
    }

    public WebElement getPolicyLoanBalancesDisbursementsTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesDisbursements);
        return table_PolicyLoanBalancesDisbursements;
    }

    public WebElement getPolicyLoanBalancesTransfersTable() {
        waitUntilElementIsVisible(table_PolicyLoanBalancesTransfers);
        return table_PolicyLoanBalancesTransfers;
    }

    public WebElement getAccountPolicyLoanBalancesTableRow(String policyNumber, String loanNumber, Double chargesAmount, Double balanceOutstandingAmount, Double paymentsCreditsAmount, Double disbursedAmount, Double transfersAmount, Double totalCashAmount, Double excessCashAmount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (loanNumber != null) {
            columnRowKeyValuePairs.put("Loan Number", loanNumber);
        }
        if (chargesAmount != null) {
            columnRowKeyValuePairs.put("Charges", StringsUtils.currencyRepresentationOfNumber(chargesAmount));
        }
        if (balanceOutstandingAmount != null) {
            columnRowKeyValuePairs.put("Balance Outstanding", StringsUtils.currencyRepresentationOfNumber(balanceOutstandingAmount));
        }
        if (paymentsCreditsAmount != null) {
            columnRowKeyValuePairs.put("Payments / Credits", StringsUtils.currencyRepresentationOfNumber(paymentsCreditsAmount));
        }
        if (disbursedAmount != null) {
            columnRowKeyValuePairs.put("Disbursed", StringsUtils.currencyRepresentationOfNumber(disbursedAmount));
        }
        if (transfersAmount != null) {
            columnRowKeyValuePairs.put("Transfers", StringsUtils.currencyRepresentationOfNumber(transfersAmount));
        }
        if (totalCashAmount != null) {
            columnRowKeyValuePairs.put("Total Cash", StringsUtils.currencyRepresentationOfNumber(totalCashAmount));
        }
        if (excessCashAmount != null) {
            columnRowKeyValuePairs.put("Excess Cash", StringsUtils.currencyRepresentationOfNumber(excessCashAmount));
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_PolicyLoanBalances, columnRowKeyValuePairs);
    }

	public boolean verifyPolicyLoanBalance(String policyNumber, String loanNumber, Double chargesAmount, Double balanceOutstandingAmount, Double paymentsCreditsAmount, Double disbursedAmount, Double transfersAmount, Double totalCashAmount, Double excessCashAmount) {
		boolean found = false;
		try {
			getAccountPolicyLoanBalancesTableRow(policyNumber, loanNumber, chargesAmount, balanceOutstandingAmount, paymentsCreditsAmount, disbursedAmount, transfersAmount, totalCashAmount, excessCashAmount);
			found = true;
		} catch (Exception e) {
			found = false;
		}
		return found;
	}
	
	public boolean verifyPolicyLoanBalancesChargesTableRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
		boolean found = false;
		repository.bc.account.AccountCharges charges = new repository.bc.account.AccountCharges(driver);
		try{
			charges.getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions);
			found = true;
		}catch(Exception e){
			found = false;
		}
		return found;
	}

    public WebElement getAccountPolicyLoanBalancesChargesTableRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
        repository.bc.account.AccountCharges charges = new AccountCharges(getDriver());
        return charges.getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions);
    }

    public WebElement getAccountPolicyLoanBalancesPaymentsTableRow(Date paymentDate, Date createDate, String policyNumber, String loanNumber, PaymentReversalReason reversalReason, Date reversalDate, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, String referenceNumber, String unappliedFund, Double amount, Double amountDistributed, Double amountUndistributed) {
        AccountPayments payments = new AccountPayments(getDriver());
        return payments.getPaymentsAndCreditDistributionsTableRow(paymentDate, createDate, policyNumber, loanNumber, reversalReason, reversalDate, paymentInstrument, paymentLocation, referenceNumber, unappliedFund, amount, amountDistributed, amountUndistributed);
    }

    public WebElement getAccountPolicyLoanBalancesCreditsTableRow(Date paymentDate, Date createDate, String policyNumber, String loanNumber, PaymentReversalReason reversalReason, Date reversalDate, PaymentInstrumentEnum paymentInstrument, PaymentLocation paymentLocation, String referenceNumber, String unappliedFund, Double amount, Double amountDistributed, Double amountUndistributed) {
        AccountPaymentsCreditDistributions credits = new AccountPaymentsCreditDistributions(getDriver());
        return credits.getPaymentsAndCreditDistributionsTableRow(paymentDate, createDate, policyNumber, loanNumber, reversalReason, reversalDate, paymentInstrument, paymentLocation, referenceNumber, unappliedFund, amount, amountDistributed, amountUndistributed);
    }

    public WebElement getAccountPolicyLoanBalancesCreditsPaymentDetailsTableRow(String invoiceNumber, Date invoiceDate, Date invoiceDateDue, String paymentOwner, String paymentPayer, String policyNumber, String loanNumber, String paymentType, Double amount, Double unpaidAmount, Double grossAmountApplied) {
        AccountPaymentsCreditDistributions credits = new AccountPaymentsCreditDistributions(getDriver());
        return credits.getPaymentsAndCreditDistributionsDetailsTableRow(invoiceNumber, invoiceDate, invoiceDateDue, paymentOwner, paymentPayer, policyNumber, loanNumber, paymentType, amount, unpaidAmount, grossAmountApplied);
    }

    public WebElement getAccountPolicyLoanBalancesDisbursementsTableRow(Date dateIssued, Date dateRejected, String disbursementNumber, String unappliedFund, DisbursementStatus disbursementStatus, String trackingStatus, Double disbursementAmount, String payTo, String referenceNumber, String assignee) {
        repository.bc.account.AccountDisbursements disbursements = new repository.bc.account.AccountDisbursements(getDriver());
        return disbursements.getDisbursementsTableRow(dateIssued, dateRejected, disbursementNumber, unappliedFund, disbursementStatus, trackingStatus, disbursementAmount, payTo, referenceNumber, assignee);
    }

    public void selectAccountPolicyLoanBalancesDisbursementsDateRangeFilter(DisbursementClosedStatusFilter closedStatus) {
        repository.bc.account.AccountDisbursements disbursements = new repository.bc.account.AccountDisbursements(getDriver());
        disbursements.selectDisbursementsDateRangeFilter(closedStatus);
    }

    public void selectAccountPolicyLoanBalancesDisbursementsStatusFilter(DisbursementStatusFilter disbursementStatus) {
        repository.bc.account.AccountDisbursements disbursements = new AccountDisbursements(getDriver());
        disbursements.selectDisbursementsStatusFilter(disbursementStatus);
    }

    public WebElement getAccountPolicyLoanBalancesTransfersTableRow(Date transactionDate, String transactionNumber, Double transferAmount, String sourceAccount, String sourcePolicy, String sourceLoanNumber, String targetAccount, String targetPolicy, String targetLoanNumber, TransferReason transferReason) {
        AccountPaymentsTransfers transfers = new AccountPaymentsTransfers(getDriver());
        return transfers.getAccountPaymentsTransfersTableRow(transactionDate, transactionNumber, transferAmount, sourceAccount, sourcePolicy, sourceLoanNumber, targetAccount, targetPolicy, targetLoanNumber, transferReason);
    }

    public List<String> getPolicyFilterListContents() {
        Guidewire8Select mySelect = select_PolicyFilter();
        return mySelect.getList();
    }

    public void selectPolicyFilter(String policyNumber) {
        Guidewire8Select mySelect = select_PolicyFilter();
        mySelect.selectByVisibleTextPartial(policyNumber);
    }

    public List<String> getloanNumberFilterListContents() {
        Guidewire8Select mySelect = select_LoanNumberFilter();
        return mySelect.getList();
    }

    public void selectLoanNumberFilter(String loanNumber) {
        Guidewire8Select mySelect = select_LoanNumberFilter();
        mySelect.selectByVisibleTextPartial(loanNumber);
    }

    public void clickMigrateAccount() {
        clickWhenClickable(button_MigrateAccount);
    }
    
    public void clickClearFilters() {
		clickWhenClickable(button_ClearFilters);
	}

    public boolean isMigrateAccountButtonVisible() {
        try {
            waitUntilElementIsVisible(button_MigrateAccount, 500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickRecreateBalances() {
        clickWhenClickable(button_RecreateBalances);
    }

    public void clickPolicyLoanBalancesChargesTab() {
        clickWhenClickable(tab_Charges);
    }

    public void clickPolicyLoanBalancesPaymentsTab() {
        clickWhenClickable(tab_Payments);
    }

    public void clickPolicyLoanBalancesCreditsTab() {
        clickWhenClickable(tab_Credits);
    }

    public void clickPolicyLoanBalancesDisbursementsTab() {
        clickWhenClickable(tab_Disbursements);
    }

    public void clickPolicyLoanBalancesTransfersTab() {
        clickWhenClickable(tab_Transfers);
    }

    // Actions links in Policy Loan Balances Table

    public void clickActionsOrReversedButton(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_PolicyLoanBalances, rowNumber);
    }

    public void clickActionsNewDirectBillPaymentLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_PolicyLoanBalances, rowNumber, "New Direct Bill Payment");
    }

    public void clickActionsDisbursementLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_PolicyLoanBalances, rowNumber, "Disbursement");
    }

    public void clickActionsTransferLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_PolicyLoanBalances, rowNumber, "Transfer");
    }

    public void clickActionsRecaptureLink(int rowNumber) {
        tableUtils.clickActionsLinkInSpecficRowInTable(table_PolicyLoanBalances, rowNumber, "Recapture");
    }
    public int getPolicyLoanBalancesTableRowCount() {
        return tableUtils.getRowCount(table_PolicyLoanBalances);
    }
}
