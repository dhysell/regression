package repository.bc.account;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DisbursementClosedStatusFilter;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.DisbursementStatusFilter;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountDisbursements extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
	public AccountDisbursements(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
		this.driver = driver;
	}
	
	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//div[(@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:0') or (@id='AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:DisbursementsLV:0')]")
	public WebElement table_AccountDisbursementsTable;
	
	@FindBy(xpath = "//input[@id= 'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV:dueDate-inputEl']")
	public WebElement editbox_AccountDisbursementsDueDate;

	@FindBy(xpath = "//input[@id= 'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV:PolicyNumber-inputEl']")
	public WebElement editbox_AccountDisbursementsPolicyNumber;
	
	@FindBy(xpath = "//input[contains(@id, ':DisbursementsLV:DisbursementDetailDV:specialHandling-inputEl')]")
	public WebElement editbox_SpecialHandling;
	
	@FindBy(xpath = "//input[contains(@id, ':DisbursementsLV:DisbursementDetailDV:payTo')]")
	public WebElement editbox_PayTo;
	
	@FindBy(xpath = "//div[@id= 'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV:requestedBy-inputEl']")
	public WebElement label_RequestedBy;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:reason-inputEl')]")
	public WebElement label_DisbursementReason;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:reason-inputEl')]")
	public WebElement label_SpecialHandling;	

	@FindBy(xpath = "//div[contains(@id, ':DisbursementDetailDV:addressLIne1-inputEl')]")
	public WebElement label_AddressLine1;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementDetailDV:city-inputEl')]")
	public WebElement label_AddressCity;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementDetailDV:state-inputEl')]")
	public WebElement label_AddressState;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementDetailDV:postalCode-inputEl')]")
	public WebElement label_AddressPostalCode;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Edit']")
	public WebElement button_AccountDisbursementsEdit;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Update']")
	public WebElement button_AccountDisbursementsUpdate;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Cancel']")
	public WebElement button_AccountDisbursementsCancel;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Approve']")
	public WebElement button_AccountDisbursementsApprove;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Reject']")
	public WebElement button_AccountDisbursementsReject;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:RejectWithHold']")
	public WebElement button_AccountDisbursementsRejectAndHoldFutureAutomaticDisbursements;

	@FindBy(xpath = "//a[@id='AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV_tb:Void']")
	public WebElement button_AccountDisbursementsVoid;

	@FindBy(xpath = "//div[@id=ext-gen2004]")
	public WebElement button_AccountDisbursementsButton;
	
	public Guidewire8Select comboBox_DisbursementsCloseDateFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:FilterByDateRangeSelector-triggerWrap')] | //table[contains(@id,'policyLoanBalanceLV:DisbursementsLV:FilterByDateRangeSelector-triggerWrap')]");
	}

	public Guidewire8Select comboBox_DisbursementsStatusFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementStatusFilterID-triggerWrap')] | //table[contains(@id,'policyLoanBalanceLV:DisbursementsLV:DisbursementStatusFilterID-triggerWrap')]");
	}

	public Guidewire8Select comboBox_DisbursementsPolicyFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:FilterByPolicySelector-triggerWrap')]");
	}

	public Guidewire8Select comboBox_DisbursementsLoanNumberFilter() {
		return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:FilterByLoanNumberSelector-triggerWrap')]");
	}
	
	public Guidewire8Select comboBox_DisbursementsEditDropdownReason() {
		return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailDisbursements:AccountDetailDisbursementsScreen:DisbursementsLV:DisbursementDetailDV:reason-triggerWrap')]");
	}

	// ------------------------------------------------------------------------------- //
	// The following elements are for Apron Details area //
	// ------------------------------------------------------------------------------- //
	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:loanNo-inputEl')]")
	public WebElement label_ApronDetailLoanNumber;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:insuredName-inputEl')]")
	public WebElement label_ApronDetailInsuredName;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:chargeGrp-inputEl')]")
	public WebElement label_ApronDetailChargeGroup;

	@FindBy(xpath = "//div[contains(@id, ':DisbursementsLV:DisbursementDetailDV:reasonForDisbursment-inputEl')]")
	public WebElement label_ApronDetailDisbursementReason;

	// ------------------------------------------------------------------------------- //
	// The following elements are for Free Form Box which is under Apron Details area //
	// ------------------------------------------------------------------------------- //
	// this disbursement reason is from disbursement Detail tab

	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline1-inputEl')]")
	public WebElement editbox_ApronFreeFormDisbursementReason;

	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline2-inputEl')]")
	public WebElement editbox_ApronFreeFormInsuredName;

	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline3-inputEl')]")
	public WebElement editbox_ApronFreeFormAccountAndLoanNumbers;

	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline4-inputEl')]")
	public WebElement editbox_ApronFreeFormChargeGroup;
	//// this disbursement reason is from Apron Details area
	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline5-inputEl')]")
	public WebElement editbox_ApronFreeFormDisbursementReasonDetails;

	@FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:aprondetailsline6-inputEl')]")
	public WebElement editbox_ApronFreeFormSpecialHandling;

	@FindBy(xpath = "//textarea[contains(@id, ':DisbursementDetailDV:aprondetailstextinput-inputEl')]")
	public WebElement textArea_ApronFreeFormBoxWholdeDetails;

	@FindBy(xpath = "//a[contains(@id, ':DisbursementDetailDV:copyclearaprondetails')]")
	public WebElement button_CopyClearApronDetail;

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public String getDisbursementReason() {
		waitUntilElementIsVisible(label_DisbursementReason);
		return label_DisbursementReason.getText();
	}

	public String getDisbursementSpecialHandling() {
		waitUntilElementIsVisible(label_SpecialHandling);
		return label_SpecialHandling.getText();
	}

	public void setDisbursementSpecialHandling(String specialHandling) {
		waitUntilElementIsVisible(editbox_SpecialHandling);
		editbox_SpecialHandling.sendKeys(Keys.CONTROL + "a");
		editbox_SpecialHandling.sendKeys(specialHandling);
	}

	public WebElement getDisbursementsTable() {
		waitUntilElementIsVisible(table_AccountDisbursementsTable);
		return table_AccountDisbursementsTable;
	}

	public WebElement getDisbursementsTableRow(Date dateIssued, Date dateRejected, String disbursementNumber, String unappliedFund, DisbursementStatus disbursementStatus, String trackingStatus, Double disbursementAmount, String payTo, String referenceNumber, String assignee) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (dateIssued != null) {
			columnRowKeyValuePairs.put("Date Issued", DateUtils.dateFormatAsString("MM/dd/yyyy", dateIssued));
		}
		if (dateRejected != null) {
			columnRowKeyValuePairs.put("Date Rejected", DateUtils.dateFormatAsString("MM/dd/yyyy", dateRejected));
		}
		if (disbursementNumber != null) {
			columnRowKeyValuePairs.put("Disbursement #", disbursementNumber);
		}
		if (unappliedFund != null) {
			columnRowKeyValuePairs.put("Unapplied Fund", unappliedFund);
		}
		if (disbursementStatus != null) {
			columnRowKeyValuePairs.put("Status", disbursementStatus.getValue());
		}		
		if (trackingStatus != null) {
			columnRowKeyValuePairs.put("Tracking Status", trackingStatus);
		}
		if (disbursementAmount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(disbursementAmount));
		}
		if (payTo != null) {
			columnRowKeyValuePairs.put("Pay To", payTo);
		}
		if (referenceNumber != null) {
			columnRowKeyValuePairs.put("Ref #", referenceNumber);
		}
		if (assignee != null) {
			columnRowKeyValuePairs.put("Assignee", assignee);
		}
		return tableUtils.getRowInTableByColumnsAndValues(table_AccountDisbursementsTable, columnRowKeyValuePairs);
	}

	public boolean verifyDisbursement(Date dateIssued, Date dateRejected, String disbursementNumber, String unappliedFund, DisbursementStatus disbursementStatus, String trackingStatus, Double disbursementAmount, String payTo, String referenceNumber, String assignee) {
		boolean found = false;
		try {
			getDisbursementsTableRow(dateIssued, dateRejected, disbursementNumber, unappliedFund, disbursementStatus, trackingStatus, disbursementAmount, payTo, referenceNumber, assignee);
			found = true;
		} catch (Exception e) {
			found = false;
		}
		return found;
	}

	public void selectDisbursementsDateRangeFilter(DisbursementClosedStatusFilter closedStatus) {
		Guidewire8Select mySelect = comboBox_DisbursementsCloseDateFilter();
		mySelect.selectByVisibleTextPartial(closedStatus.getValue());
	}

	public List<String> getAllOptionsOnDisbursementsDateRangeFilter() {
		Guidewire8Select mySelect = comboBox_DisbursementsCloseDateFilter();
		List<String> availableOptions = mySelect.getList();
		return availableOptions;
	}

	public void selectDisbursementsStatusFilter(DisbursementStatusFilter dibursementStatus) {
		Guidewire8Select mySelect = comboBox_DisbursementsStatusFilter();
		mySelect.selectByVisibleTextPartial(dibursementStatus.getValue());
	}

	public List<String> getAllOptionsOnDisbursementsStatusFilter() {
		Guidewire8Select mySelect = comboBox_DisbursementsStatusFilter();
		List<String> availableOptions = mySelect.getList();
		return availableOptions;
	}

	public List<String> getAllOptionsOnDisbursementsPolicyFilter() {
		Guidewire8Select mySelect = comboBox_DisbursementsPolicyFilter();
		List<String> availableOptions = mySelect.getList();
		return availableOptions;
	}

	public List<String> getAllOptionsOnDisbursementsLoanNumberFilter() {
		Guidewire8Select mySelect = comboBox_DisbursementsLoanNumberFilter();
		List<String> availableOptions = mySelect.getList();
		return availableOptions;
	}

	public void clickRowInDisbursementsTableByText(String textInTable) {
		tableUtils.clickRowInTableByText(table_AccountDisbursementsTable, textInTable);
	}

	public void clickAccountDisbursementsEdit() {
		clickWhenVisible(button_AccountDisbursementsEdit);
	}

	public void clickAccountDisbursementsUpdate() {
		clickWhenVisible(button_AccountDisbursementsUpdate);
	}

	public void clickAccountDisbursementsCancel() {
		clickWhenVisible(button_AccountDisbursementsCancel);
	}

	public void clickAccountDisbursementsApprove() {
		clickWhenVisible(button_AccountDisbursementsApprove);
	}

	public void clickAccountDisbursementsReject() {
		clickWhenVisible(button_AccountDisbursementsReject);
		try {
			selectOKOrCancelFromPopup(OkCancel.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clickAccountDisbursementsRejectAndHoldFutureDisburements() {
		clickWhenVisible(button_AccountDisbursementsRejectAndHoldFutureAutomaticDisbursements);
	}

	public void clickAccountDisbursementsVoid() {
		clickWhenVisible(button_AccountDisbursementsVoid);
	}

	public int getDisbursementTableRowCount() {
		return tableUtils.getRowCount(table_AccountDisbursementsTable);
	}

	public List<WebElement> getDisbursements(double amount) {
		List<WebElement> disbursementRowsWithAmount = tableUtils.getRowsInTableByColumnNameAndValue(table_AccountDisbursementsTable, "Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		return disbursementRowsWithAmount;
	}

	public List<WebElement> getDisbursements(String disbursementNum) {
		List<WebElement> disbursementRowsWithAmount = tableUtils.getRowsInTableByColumnNameAndValue(table_AccountDisbursementsTable, "Disbursement #", disbursementNum);
		return disbursementRowsWithAmount;
	}

	public void clickAccountDisbursementsEditReasonDropDown() {
		clickWhenVisible(button_AccountDisbursementsEdit);
	}

	public void selectDisbursementReason(DisbursementReason reason) {
		Guidewire8Select mySelect = comboBox_DisbursementsEditDropdownReason();
		mySelect.selectByVisibleText(reason.getValue());
	}

	public void setDisbursementsDueDate(Date date) {
		editbox_AccountDisbursementsDueDate.sendKeys(Keys.CONTROL + "a");
		editbox_AccountDisbursementsDueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}

	public void setDisbursementsPolicyNumber(String policyNumber) {
		waitUntilElementIsVisible(editbox_AccountDisbursementsPolicyNumber);
		editbox_AccountDisbursementsPolicyNumber.sendKeys(Keys.CONTROL + "a");
		editbox_AccountDisbursementsPolicyNumber.sendKeys(policyNumber);
	}

	public boolean verifyDisbursements(DisbursementStatus status, double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Status", status.getValue());
		columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_AccountDisbursementsTable, columnRowKeyValuePairs);
		return allRows.size() >= 1;
	}

	public boolean verifyDisbursements(String unappliedFund, DisbursementStatus status, Double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Unapplied Fund", unappliedFund);
		columnRowKeyValuePairs.put("Status", status.getValue());
		if (amount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		}
		try {
			tableUtils.getRowInTableByColumnsAndValues(table_AccountDisbursementsTable, columnRowKeyValuePairs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUser() {
		return label_RequestedBy.getText();
	}

	public String getAddressLine1() {
		return label_AddressLine1.getText();
	}

	public String getAddressCity() {
		return label_AddressCity.getText();
	}

	public String getAddressState() {
		return label_AddressState.getText();
	}

	public String getAddressPostalCode() {
		return label_AddressPostalCode.getText();
	}

	public void setPayTo(String payTo) {
		editbox_PayTo.sendKeys(payTo);
	}

	// -----------------------------------------------------------------------------------------
	// get the 6 edit box lines' contents of the free form box which is below Apron Details area
	// -----------------------------------------------------------------------------------------

	public String getFreeFormWholeDetails() {
		waitUntilElementIsVisible(textArea_ApronFreeFormBoxWholdeDetails);
		try {
			return textArea_ApronFreeFormBoxWholdeDetails.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// first line of the Free Form Box

	public String getFreeFormDisbursementReason() {
		waitUntilElementIsVisible(editbox_ApronFreeFormDisbursementReason);
		try {
			return editbox_ApronFreeFormDisbursementReason.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// second line of the Free Form Box

	public String getFreeFormInsuredName() {
		waitUntilElementIsVisible(editbox_ApronFreeFormInsuredName);
		try {
			return editbox_ApronFreeFormInsuredName.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// third line of the Free Form Box

	public String getFreeFormAccountAndLoanNumber() {
		waitUntilElementIsVisible(editbox_ApronFreeFormAccountAndLoanNumbers);
		try {
			return editbox_ApronFreeFormAccountAndLoanNumbers.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// Fourth line of the Free Form Box

	public String getFreeFormChargeGroup() {
		waitUntilElementIsVisible(editbox_ApronFreeFormChargeGroup);
		try {
			return editbox_ApronFreeFormChargeGroup.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// fifth line of the Free Form Box

	public String getFreeFormDetailedDisbursementReason() {
		waitUntilElementIsVisible(editbox_ApronFreeFormDisbursementReasonDetails);
		try {
			return editbox_ApronFreeFormDisbursementReasonDetails.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// sixth line of the Free Form Box

	public String getFreeFormSpecialHandling() {
		waitUntilElementIsVisible(editbox_ApronFreeFormSpecialHandling);
		try {
			return editbox_ApronFreeFormSpecialHandling.getAttribute("value");
		} catch (Exception e) {
			return null;
		}
	}

	// ----------------------------------//
	// methods for Apron Details area //
	// ----------------------------------//

	public String getApronDetailsLoanNumber() {
		waitUntilElementIsVisible(label_ApronDetailLoanNumber);
		try {
			return label_ApronDetailLoanNumber.getText();
		} catch (Exception e) {
			return null;
		}
	}

	public String getApronDetailsInsuredName() {
		waitUntilElementIsVisible(label_ApronDetailInsuredName);
		try {
			return label_ApronDetailInsuredName.getText();
		} catch (Exception e) {
			return null;
		}
	}

	public String getApronDetailsChargeGroup() {
		waitUntilElementIsVisible(label_ApronDetailChargeGroup);
		try {
			return label_ApronDetailChargeGroup.getText();
		} catch (Exception e) {
			return null;
		}
	}

	public String getApronDetailsDisbursementReason() {
		waitUntilElementIsVisible(label_ApronDetailDisbursementReason);
		try {
			return label_ApronDetailDisbursementReason.getText();
		} catch (Exception e) {
			return null;
		}

	}

	public void clickCopyClearApronDetails() {
		clickWhenVisible(button_CopyClearApronDetail);
	}
}
