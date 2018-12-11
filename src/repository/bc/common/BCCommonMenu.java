package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCCommonMenu extends BasePage {

	public BCCommonMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	
	@FindBy(xpath = "//a[contains(@id, 'Actions')]")
	public WebElement link_MenuActions;
	
	// elements for Actions Drop Down
	@FindBy(xpath = "//a[contains (@id, 'MenuActions_NewNote-itemEl')]")
	public WebElement link_MenuActionsNewNote;

	@FindBy(xpath = "//a[contains(@id,'MenuActions_NewActivityReminder-itemEl')]")
	public WebElement link_MenuActionsManualActivity;
	
	@FindBy(xpath = "//a[contains(@id,':NewDocument-itemEl')]")
	public WebElement link_MenuActionsNewDocument;

	@FindBy(xpath = "//a[contains(@id,'NewDocumentFromTemplate-itemEl')]")
	public WebElement link_MenuActionsNewDocumentCreateNewDocumentFromTemplate;

	@FindBy(xpath = "//a[contains(@id,':NewDocumentByUpload-itemEl')]")
	public WebElement link_MenuActionsNewDocumentUploadDocument;	
	//End Actions Drop Down Block

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailSummary') or contains (@id, ':PolicyGroup_PolicyDetailSummary')]")
	public WebElement link_MenuSummary;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailContacts') or contains (@id, ':PolicyGroup_PolicyDetailContacts')]")
	public WebElement link_MenuContacts;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailCharges') or contains (@id, ':PolicyGroup_PolicyDetailCharges')]")
	public WebElement link_MenuCharges;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailTransactions') or contains (@id, ':PolicyGroup_PolicyDetailTransactions')]")
	public WebElement link_MenuTransactions;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailHistory') or contains (@id, ':PolicyGroup_PolicyDetailHistory')]")
	public WebElement link_MenuHistory;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailTroubleTickets') or contains (@id, ':PolicyGroup_PolicyDetailTroubleTickets')]")
	public WebElement link_MenuTroubleTickets;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailDocuments') or contains (@id, ':PolicyGroup_PolicyDetailDocuments')]")
	public WebElement link_MenuDocuments;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailLedger') or contains (@id, ':PolicyGroup_PolicyDetailLedger')]")
	public WebElement link_MenuLedger;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailJournal') or contains (@id, ':PolicyGroup_PolicyDetailJournal')]")
	public WebElement link_MenuJournal;

	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountDetailDelinquencies') or contains (@id, ':PolicyGroup_PolicyDetailDelinquencies')]")
	public WebElement link_MenuDelinquencies;
	
	@FindBy(xpath = "//td[contains (@id, 'AccountGroup:MenuLinks:AccountGroup_AccountActivitiesPage') or contains (@id,':activities')]")
	public WebElement link_MenuActivities;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickBCMenuActions() {
		
		waitUntilElementIsClickable(link_MenuActions);
		link_MenuActions.click();
	}

	public void clickBCMenuSummary() {
		clickWhenVisible(link_MenuSummary);
		
	}

	public void clickBCMenuActivities() {
		clickWhenVisible(link_MenuActivities);
		
	}

	public void clickBCMenuContacts() {
		clickWhenVisible(link_MenuContacts);
		
	}

	public void clickBCMenuDocuments() {
		clickWhenVisible(link_MenuDocuments);
		
	}

	public void clickBCMenuTroubleTickets() {
		clickWhenVisible(link_MenuTroubleTickets);
		
	}

	public void clickBCMenuHistory() {
		clickWhenVisible(link_MenuHistory);
		
	}

	public void clickBCMenuCharges() {
		clickWhenVisible(link_MenuCharges);
		
	}

	public void clickBCMenuActionsNewNote() {
		clickBCMenuActions();
		
		hoverOver(link_MenuActionsNewNote);
		
		link_MenuActionsNewNote.click();
	}

	public void clickBCMenuActionsManualActivity() {
		clickBCMenuActions();
		
		hoverOver(link_MenuActionsManualActivity);
		
		link_MenuActionsManualActivity.click();
	}

	public void clickBCMenuActionsCreateNewDocumentFromTemplate() {
		clickBCMenuActions();
		
		hoverOver(link_MenuActionsNewDocument);
		
		link_MenuActionsNewDocument.click();
		
		hoverOver(link_MenuActionsNewDocumentCreateNewDocumentFromTemplate);
		
		link_MenuActionsNewDocumentCreateNewDocumentFromTemplate.click();
		
	}

	public void clickBCMenuActionsUploadNewDocument() {
		clickBCMenuActions();
		
		hoverOver(link_MenuActionsNewDocument);
		
		link_MenuActionsNewDocument.click();
		
		hoverOver(link_MenuActionsNewDocumentUploadDocument);
		
		link_MenuActionsNewDocumentUploadDocument.click();
		
	}

	public void clickBCMenuDelinquencies() {
		clickWhenVisible(link_MenuDelinquencies);
		
	}

	public boolean ledgerLinkExist() {
		return link_MenuLedger.isDisplayed();
	}

	public void clickBCMenuLedger() {
		link_MenuLedger.click();
	}

	public boolean journalLinkExist() {
		return link_MenuJournal.isDisplayed();
	}

	public void clickBCMenuJournal() {
		link_MenuJournal.click();
	}
}
