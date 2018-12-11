package repository.bc.search;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCSearchMenu extends BasePage {

	public BCSearchMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_AccountSearch']")
	public WebElement link_SearchMenuAccounts;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_PolicySearch']")
	public WebElement link_SearchMenuPolicies;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_ContactSearch']")
	public WebElement link_SearchMenuContacts;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_InvoiceSearch']")
	public WebElement link_SearchMenuInvoices;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_PaymentSearch']")
	public WebElement link_SearchMenuPayments;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_TransactionSearch']")
	public WebElement link_SearchMenuTransactions;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_ActivitySearch']")
	public WebElement link_SearchMenuActivities;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_TroubleTicketSearch']")
	public WebElement link_SearchMenuTroubleTickets;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_DelinquencyProcessSearch']")
	public WebElement link_SearchMenuDelinquency;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_DisbursementSearch']")
	public WebElement link_SearchMenuDisbursements;

	@FindBy(xpath = "//td[@id='SearchGroup:MenuLinks:SearchGroup_PaymentRequestSearch']")
	public WebElement link_SearchMenuPaymentRequests;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickSearchMenuAccounts() {
		clickWhenVisible(link_SearchMenuAccounts);
		
	}

	public void clickSearchMenuPolicies() {
		clickWhenVisible(link_SearchMenuPolicies);
		
	}

	public void clickSearchMenuContacts() {
		clickWhenVisible(link_SearchMenuContacts);
		
	}

	public void clickSearchMenuInvoices() {
		clickWhenVisible(link_SearchMenuInvoices);
		
	}

	public void clickSearchMenuPayments() {
		clickWhenVisible(link_SearchMenuPayments);
		
	}

	public void clickSearchMenuTransactions() {
		clickWhenVisible(link_SearchMenuTransactions);
		
	}

	public void clickSearchMenuActivities() {
		clickWhenVisible(link_SearchMenuActivities);
		
	}

	public void clickSearchMenuTroubleTicket() {
		clickWhenVisible(link_SearchMenuTroubleTickets);
		
	}

	public void clickSearchMenuDelinquencyProcess() {
		clickWhenVisible(link_SearchMenuDelinquency);
		
	}

	public void clickSearchMenuDisbursements() {
		clickWhenVisible(link_SearchMenuDisbursements);
		
	}

	public void clickSearchMenuPaymentRequests() {
		clickWhenVisible(link_SearchMenuPaymentRequests);
		
	}
}
