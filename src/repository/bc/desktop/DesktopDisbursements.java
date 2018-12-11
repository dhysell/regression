package repository.bc.desktop;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class DesktopDisbursements extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public DesktopDisbursements(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    public Guidewire8Select select_DesktopDisbursementsStatuses() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DesktopDisbursementsLV_tb:disbursementDisplayStatus-triggerWrap')]");
    }

    public Guidewire8Select select_DesktopDisbursementsDateRange() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':disbursementDisplayDateRange-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='DesktopDisbursements:DesktopDisbursementsScreen:DesktopDisbursementsLV-body']/div/table[starts-with(@id,'gridview-')]")
    public WebElement table_DesktopDisbursementsResults;

    @FindBy(xpath = "//div[@id='DesktopDisbursements:DesktopDisbursementsScreen:DesktopDisbursementsLV']")
    public WebElement table_DesktopDisbursements;

    @FindBy(xpath = "//a[@id='DesktopDisbursements:DesktopDisbursementsScreen:DesktopDisbursementsLV_tb:NewDisbursement']")
    public WebElement button_DesktopDisbursementsNew;

    @FindBy(xpath = "//div[@id='DesktopDisbursements:DesktopDisbursementsScreen:DesktopDisbursementsLV']")
    public WebElement table_DesktopDisbursementsResultsWrapper;

    @FindBy(xpath = "//textarea[contains(@id,':DisbursementDetailDV:aprondetailstextinput-inputEl')]")
    public WebElement text_ApronFreeFormBoxWholdeDetails;

    @FindBy(xpath = "//a[contains(@id, ':DisbursementDetailDV:copyclearaprondetails')]")
    public WebElement button_CopyClearApronDetail;

	
	@FindBy(xpath = "//a[contains(@id, ':DisbursementDetailScreen:Reject')]")
	public WebElement button_Reject;

    @FindBy(xpath = "//input[contains(@id, ':DisbursementDetailDV:specialHandling-inputEl')]")
    public WebElement editbox_SpecialHandling;

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

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setDesktopDisbursementsStatus(String statusToSelect) {
        try {
            select_DesktopDisbursementsStatuses().selectByVisibleText(statusToSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDesktopDisbursementsDateRange(String dateToSelect) {
        try {
            select_DesktopDisbursementsDateRange().selectByVisibleText(dateToSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickDesktopDisbursementsNew() {
        clickWhenVisible(button_DesktopDisbursementsNew);
    }

    public boolean cellExistInDisbursementTableByText(String cellText) {
        return tableUtils.checkIfCellExistsInTableByText(table_DesktopDisbursementsResults, cellText);
    }

    public void clickDisbursementsTableTitleToSort(String titleName) {
        tableUtils.sortByHeaderColumn(table_DesktopDisbursementsResultsWrapper, titleName);
    }

    public String getFreeFormWholeDetails() {
        waitUntilElementIsVisible(text_ApronFreeFormBoxWholdeDetails);
        try {
            return text_ApronFreeFormBoxWholdeDetails.getAttribute("value");
        } catch (Exception e) {
            return null;
        }
    }

    public void setDisbursementSpecialHandling(String specialHandling) {
        waitUntilElementIsVisible(editbox_SpecialHandling);
        editbox_SpecialHandling.sendKeys(Keys.CONTROL + "a");
        editbox_SpecialHandling.sendKeys(specialHandling);
    }

    public void clickCopyClearApronDetails() {
        clickWhenVisible(button_CopyClearApronDetail);
    }

    public void clickDesktopDisbursementsEdit() {
        super.clickEdit();
    }

    public void clickDesktopDisbursementsUpdate() {
        super.clickUpdate();
    }

    public void clickDesktopDisbursementsCancel() {
        super.clickCancel();
    }
	public void clickDesktopDisbursementsReject() {
		clickWhenClickable(button_Reject);
	}

    // -----------------------------------------------------------------------------------------
    // get the 6 edit box lines' contents of the free form box which is below Apron Details area
    // -----------------------------------------------------------------------------------------

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

    public void setDesktopDisbursementsStatus(int indexToSelect) {
        // TODO Auto-generated method stub

    }

    public void setDesktopDisbursementsDateRange(int dateRangeToSelect) {
        // TODO Auto-generated method stub

    }

    public WebElement getDesktopDisbursementsTable() {
        // TODO Auto-generated method stub
        return null;
    }


    public WebElement getRecentlyDisbursementTableRow(Date dateCreate, Date dueDate, repository.gw.enums.Status status, repository.gw.enums.DisbursementReason disbursementReason, Double amount, String referenceNum, String accountNumber, String policyNumberUnappliedFund, String suspenseTxnNum, repository.gw.enums.ActivityQueuesBillingCenter assignee) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        new TableUtils(getDriver()).sortByHeaderColumn(table_DesktopDisbursements,"Due Date");
        new TableUtils(getDriver()).sortByHeaderColumn(table_DesktopDisbursements,"Date Created");
        new TableUtils(getDriver()).sortByHeaderColumn(table_DesktopDisbursements,"Date Created");

        if (dateCreate != null) {
            columnRowKeyValuePairs.put("Date Created", DateUtils.dateFormatAsString("MM/dd/yyyy", dateCreate));
        }
        if (dueDate != null) {
            columnRowKeyValuePairs.put("Due Created", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (disbursementReason != null) {
            columnRowKeyValuePairs.put("Reason", disbursementReason.getValue());
        }
        if (amount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        if (referenceNum != null) {
            columnRowKeyValuePairs.put("Ref #", referenceNum);
        }
        if (accountNumber != null) {
            columnRowKeyValuePairs.put("Account #", accountNumber);
        }
        if (policyNumberUnappliedFund != null) {
            columnRowKeyValuePairs.put("Unapplied Fund", policyNumberUnappliedFund);
        }
        if (suspenseTxnNum != null) {
            columnRowKeyValuePairs.put("Suspense Txn #", suspenseTxnNum);
        }
        if (assignee != null) {
            columnRowKeyValuePairs.put("Assignee", assignee.getValue());
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_DesktopDisbursements, columnRowKeyValuePairs);
    }

    public boolean verifyRecentlyMadeSuspensePayment(Date dateCreate, Date dueDate, repository.gw.enums.Status status, repository.gw.enums.DisbursementReason disbursementReason, Double amount, String referenceNum, String accountNumber, String policyNumberUnappliedFund, String suspenseTxnNum, ActivityQueuesBillingCenter assignee) {
        boolean found = false;
        try {
            getRecentlyDisbursementTableRow( dateCreate, dueDate, status, disbursementReason,amount, referenceNum, accountNumber, policyNumberUnappliedFund, suspenseTxnNum, assignee);
            found = true;
        } catch (Exception e) {
            found = false;
        }
        return found;
    }

}
