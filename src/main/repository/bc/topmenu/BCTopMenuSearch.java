package repository.bc.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class BCTopMenuSearch extends BCTopMenu {

	public BCTopMenuSearch(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_AccountSearch-itemEl')]")
	public WebElement link_MenuSearchAccounts;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_ActivitySearch-itemEl')]")
	public WebElement link_MenuSearchActivities;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_ContactSearch-textEl')]")
	public WebElement link_MenuSearchContacts;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_DelinquencyProcessSearch-itemEl')]")
	public WebElement link_MenuSearchDelinquencyProcess;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_DisbursementSearch-itemEl')]")
	public WebElement link_MenuSearchDisbursements;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_InvoiceSearch-itemEl')]")
	public WebElement link_MenuSearchInvoices;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_PaymentSearch-itemEl')]")
	public WebElement link_MenuSearchPayments;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_PaymentRequestSearch-itemEl')]")
	public WebElement link_MenuSearchPaymentRequests;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_PolicySearch-itemEl')]")
	public WebElement link_MenuSearchPolicies;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_TransactionSearch')]")
	public WebElement link_MenuSearchTransactions;

	@FindBy(how = How.XPATH, using = "//a[contains(@id,'SearchGroup_TroubleTicketSearch')]")
	public WebElement link_MenuSearchTroubleTickets;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickAccounts() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchAccounts);
		
	}

	public void clickActivities() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchActivities);
		
	}

	public void clickContacts() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchContacts);
		
	}

	public void clickDelinquencyProcess() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchDelinquencyProcess);
		
	}

	public void clickDisbursements() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchDisbursements);
		
	}

	public void clickInvoices() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchInvoices);
		
	}

	public void clickPayments() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchPayments);
		
	}

	public void clickPaymentRequests() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchPaymentRequests);
		
	}

	public void clickPolicies() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchPolicies);
		
	}

	public void clickTransactions() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchTransactions);
		
	}

	public void clickTroubleTickets() {
		super.clickSearchArrow();
		
		clickWhenVisible(link_MenuSearchTroubleTickets);
		
	}

}
