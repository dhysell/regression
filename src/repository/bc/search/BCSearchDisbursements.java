package repository.bc.search;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.PaymentType;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class BCSearchDisbursements extends BasePage {
	
	private WebDriver driver;

	public BCSearchDisbursements(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	private Guidewire8Select select_BCSearchDisbursementsDisbursementType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DisbursementSearchCriteriaInputSet:disbursementSubtype-triggerWrap')]");
	}
	
	private Guidewire8Select select_BCSearchDisbursementsStatus() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DisbursementSearchCriteriaInputSet:disbursementSubtype-triggerWrap')]");
	}
	
	private Guidewire8Select select_BCSearchDisbursementsReconStatus() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DisbursementSearchCriteriaInputSet:disbursementSubtype-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchDV:DisbursementSearchCriteriaInputSet:EarliestIssueDateCriterion-inputEl']")
	private WebElement editbox_BCSearchDisbursementsEarliestIssueDate;

	@FindBy(xpath = "//input[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchDV:DisbursementSearchCriteriaInputSet:LatestIssueDateCriterion-inputEl']")
	private WebElement editbox_BCSearchDisbursementsLatestIssueDate;

	private Guidewire8Select select_BCSearchDisbursementsPaymentMethod() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DisbursementSearchCriteriaInputSet:MethodCriterion-triggerWrap')]");
	}

	@FindBy(xpath = "//input[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchDV:DisbursementSearchCriteriaInputSet:TokenCriterion-inputEl']")
	private WebElement editbox_BCSearchDisbursementsPaymentInstrumentToken;

	@FindBy(xpath = "//input[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchDV:DisbursementSearchCriteriaInputSet:CheckNumberCriterion-inputEl']")
	private WebElement editbox_BCSearchDisbursementsPaymentCheckNumber;

	@FindBy(xpath = "//input[contains(@id, ':MinAmountCriterion-inputEl')]")
	private WebElement editBox_BCSearchDisbursementsMinimumAmount;

	@FindBy(xpath = "//input[contains(@id, ':MaxAmountCriterion-inputEl')]")
	private WebElement editBox_BCSearchDisbursementsMaximumAmount;
	
	@FindBy(xpath = "//input[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchDV:DisbursementSearchCriteriaInputSet:PayeeCriterion-inputEl']")
	private WebElement editbox_BCSearchDisbursementsPayee;
	
	private Guidewire8Select select_BCSearchDisbursementsReason() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':ReasonCriterion-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':AccountNumberCriterion-inputEl')]")
	private WebElement editbox_BCSearchDisbursementsAccountNumber;

	@FindBy(xpath = "//div[@id='DisbursementSearch:DisbursementSearchScreen:DisbursementSearchResultsLV-body']/div/table")
	private WebElement table_BCSearchDisbursementsSearchResults;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void setBCSearchDisbursementsDisbursementType(String type) {
		select_BCSearchDisbursementsDisbursementType().selectByVisibleText(type);
	}
	
	public void setBCSearchDisbursementsStatus(DisbursementStatus disbursementStatus) {
		select_BCSearchDisbursementsStatus().selectByVisibleText(disbursementStatus.getValue());
	}
	
	public void setBCSearchDisbursementsReconStatus(String reconStatus) {
		select_BCSearchDisbursementsReconStatus().selectByVisibleText(reconStatus);
	}

	public void setBCSearchDisbursementsEarliestIssueDate(Date date) {
		waitUntilElementIsVisible(editbox_BCSearchDisbursementsEarliestIssueDate);
		editbox_BCSearchDisbursementsEarliestIssueDate.sendKeys(Keys.CONTROL + "a");
		editbox_BCSearchDisbursementsEarliestIssueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}

	public void setBCSearchDisbursementsLatestIssueDate(Date date) {
		waitUntilElementIsVisible(editbox_BCSearchDisbursementsLatestIssueDate);
		editbox_BCSearchDisbursementsLatestIssueDate.sendKeys(Keys.CONTROL + "a");
		editbox_BCSearchDisbursementsLatestIssueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}

	public void setBCSearchDisbursementsPaymentMethod(PaymentType method) {
		select_BCSearchDisbursementsPaymentMethod().selectByVisibleText(method.getValue());
	}

	public void setBCSearchDisbursementsPaymentInstrumentToken(String token) {
		waitUntilElementIsVisible(editbox_BCSearchDisbursementsPaymentInstrumentToken);
		editbox_BCSearchDisbursementsPaymentInstrumentToken.sendKeys(Keys.CONTROL + "a");
		editbox_BCSearchDisbursementsPaymentInstrumentToken.sendKeys(token);
	}

	public void setBCSearchDisbursementsCheckNumber(String number) {
		waitUntilElementIsVisible(editbox_BCSearchDisbursementsPaymentCheckNumber);
		editbox_BCSearchDisbursementsPaymentCheckNumber.sendKeys(Keys.CONTROL + "a");
		editbox_BCSearchDisbursementsPaymentCheckNumber.sendKeys(number);
	}
	
	public void setBCSearchDisbursementsMinimumAmount(double minimumAmount) {
		clickWhenVisible(editBox_BCSearchDisbursementsMinimumAmount);
		editBox_BCSearchDisbursementsMinimumAmount.sendKeys(Keys.CONTROL + "a");
		editBox_BCSearchDisbursementsMinimumAmount.sendKeys(String.valueOf(minimumAmount));
	}

	public void setBCSearchDisbursementsMaximumAmount(double maximumAmount) {
		clickWhenVisible(editBox_BCSearchDisbursementsMaximumAmount);
		editBox_BCSearchDisbursementsMaximumAmount.sendKeys(Keys.CONTROL + "a");
		editBox_BCSearchDisbursementsMaximumAmount.sendKeys(String.valueOf(maximumAmount));
	}

	public void setBCSearchDisbursementsPayee(String payee) {
		waitUntilElementIsVisible(editbox_BCSearchDisbursementsPayee);
		editbox_BCSearchDisbursementsPayee.sendKeys(Keys.CONTROL + "a");
		editbox_BCSearchDisbursementsPayee.sendKeys(payee);
	}
	
	public void setBCSearchDisbursementsReason(DisbursementReason disbursementReason) {
		select_BCSearchDisbursementsReason().selectByVisibleText(disbursementReason.getValue());
	}
	
	public void setBCSearchDisbursementsAccountNumber(String accountNumber) {
		clickWhenClickable(editbox_BCSearchDisbursementsAccountNumber);
		editbox_BCSearchDisbursementsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
	}

	public WebElement getBCSearchDisbursementsSearchResultsTable() {
		return table_BCSearchDisbursementsSearchResults;
	}

}
