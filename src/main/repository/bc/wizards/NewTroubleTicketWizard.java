package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Priority;
import repository.gw.enums.TroubleTicketType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class NewTroubleTicketWizard extends BasePage {
	
	private WebDriver driver;

	public NewTroubleTicketWizard(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for New Trouble Ticket Wizard - Step
	// 1 of 5
	// -----------------------------------------------
	public Guidewire8Select comboBox_NewTicketType() {
		return new Guidewire8Select(driver, "//table[contains(@id,':NewTroubleTicketInfoDV:TicketType-triggerWrap')]");
	}

	@FindBy(xpath = "//input[@id='CreateTroubleTicketWizard:CreateTroubleTicketInfoScreen:NewTroubleTicketInfoDV:Subject-inputEl']")
	public WebElement editbox_NewTroubleTicketSubject;

	@FindBy(xpath = "//textarea[@id='CreateTroubleTicketWizard:CreateTroubleTicketInfoScreen:NewTroubleTicketInfoDV:DetailedDescription-inputEl']")
	public WebElement editbox_NewTroubleTicketDetails;
	
	@FindBy(xpath = "//input[@label='Latest Date' or contains(@id, ':HoldDV:0:ReleaseDate-inputEl')]")
	private WebElement editBox_CommonBCReleaseDateOfDelinquencyHold;
	
	@FindBy(xpath = "//input[@label='Latest Date' or contains(@id, ':HoldDV:1:ReleaseDate-inputEl')]")
	private WebElement editBox_CommonBCReleaseDateOfInvoiceSending;

	public Guidewire8Select comboBox_NewTroubleTicketPriority() {
		return new Guidewire8Select(driver, "//table[contains(@id,':NewTroubleTicketInfoDV:Priority-triggerWrap')]");
	}
	
	private Guidewire8Select select_BCNewTTWizardPriority() {
		return new Guidewire8Select(driver, "//table[contains(@id,':Priority-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[@label='Latest Date' or contains(@id, ':DueDate-inputEl')]")
	private WebElement editBox_BCNewTTWizardDueDate;

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for New Trouble Ticket Wizard - Step
	// 2 of 5
	// -----------------------------------------------
	@FindBy(xpath = "//div[@id='CreateTroubleTicketWizard:CreateTroubleTicketEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV']")
	public WebElement table_AccountNewTroubleTicketStep2Entrities;

	@FindBy(xpath = "//a[@id='CreateTroubleTicketWizard:CreateTroubleTicketEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddAccountsButton']")
	public WebElement button_NewTroubleTicketStep2AddAccounts;

	@FindBy(xpath = "//a[@id='CreateTroubleTicketWizard:CreateTroubleTicketEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddPoliciesButton']")
	public WebElement button_NewTroubleTicketStep2AddPolicies;

	@FindBy(xpath = "//a[@id='CreateTroubleTicketWizard:CreateTroubleTicketEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddPolicyPeriodsButton']")
	public WebElement button_NewTroubleTicketStep2AddPolicyperiods;

	@FindBy(xpath = "//div[@id='CreateTroubleTicketWizard:CreateTroubleTicketHoldsScreen:HoldDV']")
	public WebElement hold_AccountNewTroubleTicketHold;

	// -------------------------------------------------------
	// Helper Methods for Above Elements - New Trouble Ticket Wizard - Step 1 of
	// 5
	// -------------------------------------------------------

	public void setNewTroubleTicketType(TroubleTicketType type) {
		comboBox_NewTicketType().selectByVisibleText(type.getValue());
	}

	public void setNewTroubleTicketSubject(String subject) {
		clickWhenVisible(editbox_NewTroubleTicketSubject);
		editbox_NewTroubleTicketSubject.sendKeys(subject);
	}

	public void setNewTroubleTicketDetails(String details) {
		clickWhenVisible(editbox_NewTroubleTicketDetails);
		editbox_NewTroubleTicketDetails.sendKeys(details);
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements - New Trouble Ticket Wizard - Step 2 of
	// 5
	// -------------------------------------------------------

	public void clickCheckboxByAccountNumber(String acctNumber) {
		String namegridColumnID = new TableUtils(getDriver()).getGridColumnFromTable(table_AccountNewTroubleTicketStep2Entrities, "Name");
		String xpathToCheck = ".//tr/td[contains(@class,'" + namegridColumnID + "') and contains(.,'" + acctNumber + "')]/parent::tr/td/div[contains(@class, '-cell-inner-checkcolumn')]";
		table_AccountNewTroubleTicketStep2Entrities.findElement(By.xpath(xpathToCheck)).click();
		
	}

	public void clickDelinquencyCheckbox() {
		find(By.xpath(".//label[text()='Delinquency']")).click();
		
	}

	public void clickInvoiceStatementCheckbox() {
		find(By.xpath(".//label[text()='Invoice / Statement Sending']")).click();
		
	}
	
	public void setPriority(String priorityToFill) {
		select_BCNewTTWizardPriority().selectByVisibleText(priorityToFill);
	}
	
	public void setDueDate(Date dueDate) {
		clickWhenVisible(editBox_BCNewTTWizardDueDate);
		editBox_BCNewTTWizardDueDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_BCNewTTWizardDueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
	}
	
	public void setReleaseDateOnDelinquencyHold(Date dueDate) {
		clickWhenVisible(editBox_CommonBCReleaseDateOfDelinquencyHold);
		
		editBox_CommonBCReleaseDateOfDelinquencyHold.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		
		editBox_CommonBCReleaseDateOfDelinquencyHold.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		
	}
	
	public void setReleaseDateOnInvoicing(Date dueDate) {
		clickWhenVisible(editBox_CommonBCReleaseDateOfInvoiceSending);
		
		editBox_CommonBCReleaseDateOfInvoiceSending.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		
		editBox_CommonBCReleaseDateOfInvoiceSending.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
		
	}

	public void createNewTroubleTicket(TroubleTicketType type, Priority priority, Date dueDate, String acctNumber) {
		setNewTroubleTicketType(type);
		
		setNewTroubleTicketSubject("creating a new trouble ticket");
		setNewTroubleTicketDetails("creating a new trouble ticket");
		setPriority(priority.getValue());
		setDueDate(dueDate);
		
		clickNext();
		
		clickCheckboxByAccountNumber(acctNumber);
		
		clickNext();
		
		clickDelinquencyCheckbox();
		
		setReleaseDateOnDelinquencyHold(dueDate);
		
		clickInvoiceStatementCheckbox();
		
		setReleaseDateOnInvoicing(dueDate);
		
		clickNext();
		
		clickFinish();
	}
}