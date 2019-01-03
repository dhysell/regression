package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonMenu;

public class BCAccountMenu extends BCCommonMenu {

	public BCAccountMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	
	// elements for Actions Drop Down
	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountDetailMenuActions_Payments-itemEl']")
	public WebElement link_AccountMenuActionsNewPayment;
	
//	@FindBy(xpath = "//div[contains(@id, ':AccountsMenuActions_NewActivityReminder']")
//	public WebElement link_AccountMenuActionsManualActivity;
	
	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountsMenuActions_NewActivityReminder-itemEl']")
	public WebElement link_AccountMenuActionsManualActivity;

	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountDetailMenuActions_Payments:AccountDetailMenuActions_NewDirectBillPayment-itemEl']")
	public WebElement link_AccountMenuActionsNewDirectBillPayment;

	@FindBy(xpath = "//a[contains(@id, ':AccountDetailMenuActions_NewDirectBillCreditDistribution-itemEl')]")
	public WebElement link_AccountMenuActionsNewDirectBillCreditDistribution;

	@FindBy(xpath = "//a[contains(@id, ':AccountDetailMenuActions_NewPaymentRequest-itemEl')]")
	public WebElement link_AccountMenuActionsNewPaymentRequest;

	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountDetailMenuActions_Payments:LienHolderPayments-itemEl']")
	public WebElement link_AccountMenuActionsNewLienholderPayment;
	
	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountDetailMenuActions_NewTransaction-itemEl']")
	public WebElement link_AccountActionsNewTransaction;
	
	@FindBy(xpath = "//a[@id='AccountGroup:AccountDetailMenuActions:AccountDetailMenuActions_NewTransaction:AccountDetailMenuActions_Disbursement-itemEl']")
	public WebElement link_AccountActionsNewTransactionDisbursement;
	
	@FindBy(xpath = "//a[contains(@id, ':AccountDetailMenuActions_ChargeReversal-itemEl')]")
	public WebElement link_AccountActionsNewTransactionChargeReversal;
	
	@FindBy(xpath = "//a[contains(@id,':AccountDetailMenuActions_Transfer-itemEl')]")
	public WebElement link_AccountActionsNewTransactionTransfer;
	
	@FindBy(xpath = "//a[contains(@id,':AccountDetailMenuActions_Recapture-itemEl')]")
	public WebElement link_AccountActionsNewTransactionRecapture;
	
	@FindBy(xpath = "//a[contains(@id,':AccountDetailMenuActions_Credit-itemEl')]")
	public WebElement link_AccountActionsNewTransactionCredit;

	@FindBy(xpath = "//a[contains(@id,':AccountDetailMenuActions_CreditReversal-itemEl')]")
	public WebElement link_AccountActionsNewTransactionCreditReversal;
	//End Actions Drop Down Block
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailFundsTracking']")
	public WebElement link_AccountMenuFundsTracking;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountPolicyBalances']")
	public WebElement link_AccountMenuPolicyLoanBalances;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments']")
	public WebElement link_AccountMenuPayments;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments:AccountDetailPayments_AccountPayments']")
	public WebElement link_AccountMenuPaymentsPayments;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments:AccountDetailPayments_AccountCreditDistributions']")
	public WebElement link_AccountMenuPaymentsCreditDistributions;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments:AccountDetailPayments_AccountPaymentRequests']")
	public WebElement link_AccountMenuPaymentsPaymentRequests;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments:AccountDetailPayments_AccountPromisedPayments']")
	public WebElement link_AccountMenuPaymentsPromisedPayments;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPayments:AccountDetailPayments_AccountTransfers_FBM']")
	public WebElement link_AccountMenuPaymentsTransfers;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailDisbursements']")
	public WebElement link_AccountMenuDisbursements;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailPolicies']")
	public WebElement link_AccountMenuPolicies;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailEvaluation']")
	public WebElement link_AccountMenuEvaluation;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailInvoices']")
	public WebElement link_AccountMenuInvoices;
	
	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountDetailNotes']")
	public WebElement link_AccountMenuAccountNotes;

	@FindBy(xpath = "//td[@id='AccountGroup:MenuLinks:AccountGroup_AccountView_Ext']")
	public WebElement link_AccountMenu360AccountView;	
	
	@FindBy(xpath = "//a[contains(@id, ':AccountDetailMenuActions_NegativeWriteoff-itemEl')]")
	public WebElement link_AccountMenuActionsNewTransactionsNegativeWriteoff;	
	
	@FindBy(xpath = "//a[contains(@id, ':AccountDetailMenuActions_NegativeWriteoffReversal-itemEl')]")
	public WebElement link_AccountMenuActionsNewTransactionsNegativeWriteoffReversal;
	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickAccountMenuActionsNewTransaction() {
		clickBCMenuActions();
		clickWhenVisible(link_AccountActionsNewTransaction);
		
	}
	
	public void clickAccountMenuActionsManualActivity() {
		clickBCMenuActions();
		clickWhenVisible(link_AccountMenuActionsManualActivity);
	}


	public void clickAccountMenuActionsNewTransactionDisbursement() {
		clickBCMenuActions();
		
		hoverOver(link_AccountActionsNewTransaction);
		
		link_AccountActionsNewTransaction.click();
		
		hoverOver(link_AccountActionsNewTransactionDisbursement);
		
		link_AccountActionsNewTransactionDisbursement.click();
		
	}
	
	public void clickAccountMenuActionsNewTransactionChargeReversal() {
		clickBCMenuActions();
		waitForPostBack();
		clickWhenVisible(link_AccountActionsNewTransaction);		
		clickWhenVisible(link_AccountActionsNewTransactionChargeReversal);
		waitForPostBack();
	}

	public void clickAccountMenuFundsTracking() {
		clickWhenVisible(link_AccountMenuFundsTracking);
		
	}

	public void clickAccountMenuPolicyLoanBalances() {
		clickWhenVisible(link_AccountMenuPolicyLoanBalances);
		
		// This section ensures that if a Lienholder Account can be converted to use the Policy Loan Balances page and hasn't been yet, that it is automatically converted.
		AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(getDriver());
		if (policyLoanBalances.isMigrateAccountButtonVisible()) {
			policyLoanBalances.clickMigrateAccount();
			
		}
	}

	public void clickAccountMenuInvoices() {
		clickWhenVisible(link_AccountMenuInvoices);
		
	}

	public void clickAccountMenuEvaluation() {
		clickWhenVisible(link_AccountMenuEvaluation);
		
	}

	public void clickAccountMenuAccountNotes() {
		clickWhenVisible(link_AccountMenuAccountNotes);
		
	}

	public void clickAccountMenuDisbursements() {
		clickWhenVisible(link_AccountMenuDisbursements);
		
	}

	public void clickAccountMenuPolicies() {
		clickWhenVisible(link_AccountMenuPolicies);
		
	}

	public void clickAccountMenuActionsNewLienholderPayment() {
		clickBCMenuActions();
		
		hoverOver(link_AccountMenuActionsNewPayment);
		
		link_AccountMenuActionsNewPayment.click();
		
		hoverOver(link_AccountMenuActionsNewLienholderPayment);
		
		link_AccountMenuActionsNewLienholderPayment.click();
		
	}

	public void clickAccountMenuActionsNewDirectBillCreditDistribution() {
		clickBCMenuActions();
		
		hoverOver(link_AccountMenuActionsNewPayment);
		
		link_AccountMenuActionsNewPayment.click();
		
		hoverOver(link_AccountMenuActionsNewDirectBillCreditDistribution);
		
		link_AccountMenuActionsNewDirectBillCreditDistribution.click();
		
	}

	public void clickAccountMenuActionsNewPaymentRequest() {
		clickBCMenuActions();
		
		hoverOver(link_AccountMenuActionsNewPayment);
		
		link_AccountMenuActionsNewPayment.click();
		
		hoverOver(link_AccountMenuActionsNewPaymentRequest);
		
		link_AccountMenuActionsNewPaymentRequest.click();
		
	}

	public void clickAccountMenuActionsNewDirectBillPayment() {
		clickBCMenuActions();
		
		hoverOver(link_AccountMenuActionsNewPayment);
		
		link_AccountMenuActionsNewPayment.click();
		
		hoverOver(link_AccountMenuActionsNewDirectBillPayment);
		
		link_AccountMenuActionsNewDirectBillPayment.click();
		waitForPostBack();
	}

	public void clickAccountMenuActionsNewTransactionCredit() {
		clickBCMenuActions();
		
		hoverOver(link_AccountActionsNewTransaction);
		
		link_AccountActionsNewTransaction.click();
		
		hoverOver(link_AccountActionsNewTransactionCredit);
		
		link_AccountActionsNewTransactionCredit.click();
		
	}

	public void clickAccountMenuActionsNewTransactionCreditReversal() {
		clickBCMenuActions();
		
		hoverOver(link_AccountActionsNewTransaction);
		
		link_AccountActionsNewTransaction.click();
		
		hoverOver(link_AccountActionsNewTransactionCreditReversal);
		
		link_AccountActionsNewTransactionCreditReversal.click();
		
	}

	public void clickAccountMenuActionsNewNewTransactionRecapture() {
		clickBCMenuActions();
		
		hoverOver(link_AccountActionsNewTransaction);
		
		link_AccountActionsNewTransaction.click();
		
		hoverOver(link_AccountActionsNewTransactionRecapture);
		
		link_AccountActionsNewTransactionRecapture.click();
		
	}

	public void clickAccountMenuPayments() {
		clickWhenVisible(link_AccountMenuPayments);
		
	}

	public void clickAccountMenuPaymentsPayments() {
		clickAccountMenuPayments();
		clickWhenClickable(link_AccountMenuPaymentsPayments);
		
	}

	public void clickAccountMenuPaymentsCreditDistributions() {
		clickAccountMenuPayments();
		clickWhenClickable(link_AccountMenuPaymentsCreditDistributions);
		
	}

	public void clickAccountMenuPaymentsPaymentRequests() {
		clickAccountMenuPayments();
        clickWhenClickable(link_AccountMenuPaymentsPaymentRequests);
		
	}

	public void clickAccountMenuPaymentsPromisedPayments() {
		clickAccountMenuPayments();
		clickWhenClickable(link_AccountMenuPaymentsPromisedPayments);
		
	}

	public void clickAccountMenuPaymentsTransfers() {
		clickAccountMenuPayments();
		clickWhenClickable(link_AccountMenuPaymentsTransfers);
		
	}

	public void clickActionsNewTransactionTransfer() {
		clickBCMenuActions();
		
		hoverOver(link_AccountActionsNewTransaction);
		
		link_AccountActionsNewTransaction.click();
		
		hoverOver(link_AccountActionsNewTransactionTransfer);
		
		link_AccountActionsNewTransactionTransfer.click();
		
	}
	
	public void clickActionsNewTransactionNegativeWriteoff() {
		clickBCMenuActions();
		waitUntilElementIsClickable(link_AccountActionsNewTransaction);		
		link_AccountActionsNewTransaction.click();		
		waitUntilElementIsClickable(link_AccountMenuActionsNewTransactionsNegativeWriteoff);	
		link_AccountMenuActionsNewTransactionsNegativeWriteoff.click();		
	}
	
	public void clickActionsNewTransactionNegativeWriteoffReversal() {
		clickBCMenuActions();
		waitUntilElementIsClickable(link_AccountActionsNewTransaction);		
		link_AccountActionsNewTransaction.click();		
		waitUntilElementIsClickable(link_AccountMenuActionsNewTransactionsNegativeWriteoffReversal);	
		link_AccountMenuActionsNewTransactionsNegativeWriteoffReversal.click();		
	}
}
