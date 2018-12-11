package repository.ab.contact;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.AbAccountType;
import repository.gw.generate.ab.AbAccounts;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.Date;


public class ContactDetailsAccountAB extends BasePage {

	private TableUtils tableUtils;
	
    public ContactDetailsAccountAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ***************************************************************************************************
    // Items Below are the Repository Items in the Contact Details Account Page.
    // ***************************************************************************************************

    @FindBy(xpath = "//div[contains(@id,'ContactDetail:ABContactDetailScreen:Accounts:AccountsLV') and not(contains(@id,'-body'))]")
    private WebElement div_ContactDetailsAccountPage;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:_msgs')]")
    private WebElement div_ContactDetailsAccountPageValidationMessage;

    // ***************************************************************************************************
    // Items Below are the Helper Methods to the Repository Items in the Contact
    // Details Account Page.
    // ***************************************************************************************************

    public void setAccountType() {
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAccountPage, tableUtils.getNextAvailableLineInTable(div_ContactDetailsAccountPage), "Type", "Umbrella");
    }

    public void changeStatus(String status, String cancelDate) {
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAccountPage, tableUtils.getRowCount(div_ContactDetailsAccountPage), "Status", status);
        tableUtils.setValueForCellInsideTable(div_ContactDetailsAccountPage, tableUtils.getRowCount(div_ContactDetailsAccountPage), "Cancel Date", "PolicyCancelDate", cancelDate);
    }

    public void changeStatusToCancelled() {
        clickEdit();
        changeStatus("Cancelled", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.ContactManager)));
        clickUpdate();
    }

    public String getPolicyStatus(String policy) {
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, policy);
        return tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Status");
    }

    public AbAccountType getAccountType(String policyNum) {
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, policyNum);
        String accountType = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Type");
        AbAccountType type = AbAccountType.getEnumFromStringValue(accountType);
        return type;
    }

    public AbAccounts getAbAccounts(String policyNum) throws ParseException {
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, policyNum);
        String acctNum = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Account #");
        String policyNumber = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Policy #");
        String policyType = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Type");
        String policyStatus = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Status");
        String cancelDate = tableUtils.getCellTextInTableByRowAndColumnName(div_ContactDetailsAccountPage, row, "Cancel Date");

        return new AbAccounts(acctNum.trim(), policyNumber.trim(), policyType.trim(), policyStatus.trim(), cancelDate.trim());

    }

    public void changeAcctType(String acctNumber, AbAccountType acctType) {
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, acctNumber);
        tableUtils.selectValueForSelectInTable(div_ContactDetailsAccountPage, row, "Type", acctType.getValue());
    }

    public void removeAccount(String accountNumber) {
        waitUntilElementIsVisible(div_ContactDetailsAccountPage);
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, accountNumber);
        tableUtils.setValueForCellInsideTable(div_ContactDetailsAccountPage, row, "Policy #", "PolicyNumber", " ");
    }

    public String getValidationError() {
        waitUntilElementIsVisible(div_ContactDetailsAccountPageValidationMessage);
        return div_ContactDetailsAccountPageValidationMessage.getText();

    }

    public void setRemoveDate(String accountNumber, Date removeDate) {
        waitUntilElementIsVisible(div_ContactDetailsAccountPage);
        int row = tableUtils.getRowNumberInTableByText(div_ContactDetailsAccountPage, accountNumber);
        //(WebElement tableDivElement, int tableRowNumber, String tableHeaderName, String cellInputName, String valueToSet
        tableUtils.setValueForCellInsideTable(div_ContactDetailsAccountPage, row, "Remove Date", "RemoveDate", DateUtils.dateFormatAsString("MM/dd/yyyy", removeDate));
        //WebElement tableDivElement, int tableRowNumber, String tableHeaderName, String cellInputName, String valueToSet
    }
}
