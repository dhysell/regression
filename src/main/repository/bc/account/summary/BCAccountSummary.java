package repository.bc.account.summary;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonSummary;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentAllocationPlan;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

public class BCAccountSummary extends BCCommonSummary {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCAccountSummary(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths
    // -----------------------------------------------
    @FindBy(xpath = "//div[contains(@id,'AccountDetailSummaryScreen:AccountPoliciesLV')]")
    private WebElement table_AccountOpenPolicyStatusTable;

    @FindBy(xpath = "//div[contains(@id,'AccountDetailSummaryScreen:AccountDBPaymentsLV')]")
    private WebElement table_AccountRecentPaymentsReceivedTable;

    @FindBy(xpath = "//div[contains(@id,':AccountDetailDV:BillingLevelInvoiceByAccount-inputEl')]")
    private WebElement label_BillingInfoInvoiceBy;

    @FindBy(xpath = "//div[contains(@id,':AccountDetailDV:BillingLevelSeparateIncomingFundsByAccount-inputEl')]")
    private WebElement label_BillingInfoSeparateIncomingFundsBy;

    @FindBy(xpath = "//div[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:AddressLineOne-inputEl']")
    public WebElement label_AccountSummaryBillingAddress;

    @FindBy(xpath = "//div[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDBPaymentsLV-body']/div/table/tbody/tr/td[10]/div")
    private WebElement label_FirstRecentPaymentAmount;

    @FindBy(xpath = "//input[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:ParentAccount-inputEl']")
    public WebElement editbox_AccountSummaryParentAccount;

    @FindBy(xpath = "//a[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:ParentAccount:AccountPicker']")
    public WebElement link_AccountSummaryAccountPicker;

    @FindBy(xpath = "//div[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:PaymentAllocationPlan-inputEl']")
    public WebElement link_AccountSummaryPaymentAllocationPlan;

    @FindBy(xpath = "//div[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:DelinquencyPlan-inputEl']")
    public WebElement link_AccountSummaryInsuredDelinquencyPlan;

    @FindBy(xpath = "//a[@id='AccountDetailSummary:AccountDetailSummaryScreen:CloseAccountButton']")
    public WebElement button_AccountSummaryCloseAccount;

    @FindBy(xpath = "//a[@id='AccountDetailSummary:AccountDetailSummaryScreen:StartDelinquencyButton']")
    public WebElement button_AccountSummaryStartDelinquency;

    @FindBy(xpath = "//input[contains(@id,':AccountDetailDV:BillingLevelInvoiceByAccount_true-inputEl')]")
    private WebElement radio_BillingInfoInvoiceByAccount;

    @FindBy(xpath = "//input[contains(@id,':AccountDetailDV:BillingLevelInvoiceByAccount_false-inputEl')]")
    private WebElement radio_BillingInfoInvoiceByPolicyGroup;

    @FindBy(xpath = "//input[contains(@id,':AccountDetailDV:BillingLevelSeparateIncomingFundsByAccount_true-inputEl')]")
    private WebElement radio_BillingInfoSeparateIncomingFundsByAccount;

    @FindBy(xpath = "//input[contains(@id,':AccountDetailDV:BillingLevelSeparateIncomingFundsByAccount_false-inputEl')]")
    private WebElement radio_BillingInfoSeparateIncomingFundsByPolicyGroup;

    public Guidewire8Select select_AccountSummaryServiceTier() {
        return new Guidewire8Select(driver, "//table[contains(@id,'AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:CustomerServiceTier-triggerWrap')]");
    }

    public Guidewire8Select select_AccountSummaryPaymentAllocationPlan() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PaymentAllocationPlanRangeInput-triggerWrap')]");
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements
    // -------------------------------------------------------

    public boolean isAvailable(String label) {
        return !finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]")).isEmpty();
    }

    public WebElement getAccountOpenPolicyStatusTable() {
        waitUntilElementIsVisible(table_AccountOpenPolicyStatusTable);
        return table_AccountOpenPolicyStatusTable;
    }

    public WebElement getAccountRecentPaymentsReceivedTable() {
        waitUntilElementIsVisible(table_AccountRecentPaymentsReceivedTable);
        return table_AccountRecentPaymentsReceivedTable;
    }

    public String getBillingInfoInvoiceBy() {
        return label_BillingInfoInvoiceBy.getText();
    }

    public String getFirstRecentPaymentAmount() {
        return label_FirstRecentPaymentAmount.getText();
    }

    public void clickBillingInfoInvoiceByAccount() {
        clickWhenVisible(radio_BillingInfoInvoiceByAccount);
        
    }

    public void clickBillingInfoInvoiceByPolicyGroup() {
        clickWhenVisible(radio_BillingInfoInvoiceByPolicyGroup);
        
    }

    public String getBillingInfoSeparateIncomingFundsBy() {
        return label_BillingInfoSeparateIncomingFundsBy.getText();
    }

    @FindBy(xpath = "//div[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountDBPaymentsLV-body']")
    public WebElement table_PaymentsReceived;

    public boolean waitUntilRecievedAndGetFirstRecentPaymentAmount(String downPaymentType) {
        boolean found = false;
        long secondsRemaining = 60;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            found = isAvailable(downPaymentType);
            if (found == false) {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for payments again.
                secondsRemaining = secondsRemaining - delayInterval;
                refreshPage();
            }
        }

        return found;
    }

    public void clickBillingInfoSeparateIncomingFundsByAccount() {
        clickWhenVisible(radio_BillingInfoSeparateIncomingFundsByAccount);
        
    }

    public void clickBillingInfoSeparateIncomingFundsByPolicyGroup() {
        clickWhenVisible(radio_BillingInfoSeparateIncomingFundsByPolicyGroup);
        
    }

    public void clickAccountSummaryCloseAccount() {
        clickWhenVisible(button_AccountSummaryCloseAccount);
        
    }

    public void clickAccountSummaryStartDelinquency() {
        clickWhenVisible(button_AccountSummaryStartDelinquency);
        
    }

    public void clickAccountPicker() {
        clickWhenVisible(link_AccountSummaryAccountPicker);
        
    }

    public void setAccountSummaryParentAccount(String acct) {
        waitUntilElementIsVisible(editbox_AccountSummaryParentAccount);
        editbox_AccountSummaryParentAccount.sendKeys(acct);
        
    }

    public void setAccountSummaryServiceTier(String tierToSelect) {
        select_AccountSummaryServiceTier().selectByVisibleText(tierToSelect);
    }

    public boolean checkIfPaymentAllocationPlanLinkIsActive() {
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(link_AccountSummaryPaymentAllocationPlan);
    }

    public PaymentAllocationPlan getPaymentAllocationPlan() {
        return PaymentAllocationPlan.valueOf(link_AccountSummaryPaymentAllocationPlan.getText());
    }

    public void setAccountSummaryPaymentAllocationPlan(PaymentAllocationPlan paymentAllocationPlan) {
        select_AccountSummaryPaymentAllocationPlan().selectByVisibleText(paymentAllocationPlan.getValue());
    }

    public void clickActionsLinkInReceivedPaymentsTable(int rowNumber) {
        tableUtils.clickLinkInSpecficRowInTable(table_AccountRecentPaymentsReceivedTable, rowNumber);
    }

    public void clickAccountSummaryInsuredDelinquencyPlan() {
        clickWhenVisible(link_AccountSummaryInsuredDelinquencyPlan);
        
    }

    public void clickDefaultUpgradePaymentAllocationPlan() {
        clickWhenVisible(link_AccountSummaryPaymentAllocationPlan);
        
    }

    public String getBillingAddress() {
        return label_AccountSummaryBillingAddress.getText();
    }

    public boolean checkIfPolicyNumberLinkInOpenPolicyStatusTableIsActive() {
        WebElement policyNumberLinkElement = tableUtils.getCellWebElementInTableByRowAndColumnName(table_AccountOpenPolicyStatusTable, 1, "Policy Number");
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(policyNumberLinkElement);
    }

    public double getUnappliedFundByPolicyNumber(String policyNumber) {
        String cellText = find(By.xpath("//label[contains(@id,':UnappliedFunds-labelEl') and text()='" + policyNumber + "']/parent::td/following-sibling::td/div")).getText();
        if(cellText.equals("-")){
        	return 0;
        }else{        	
        	return NumberUtils.getCurrencyValueFromElement(cellText);
        }
    }

    // click the first policy number which contains the account number

    public void clickPolicyNumberInOpenPolicyStatusTable(String acctNum) {
        // @author ecoleman: 5/16/18 looks good
        tableUtils.clickLinkInTableByRowAndColumnName(table_AccountOpenPolicyStatusTable, tableUtils.getRowNumberInTableByText(table_AccountOpenPolicyStatusTable, acctNum), "Policy Number");
    }

    // get the first policy number which contains the 6 digit account number

    public String getPolicyNumberFromOpenPolicyStatusTable(String acctNum) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AccountOpenPolicyStatusTable, tableUtils.getRowNumberInTableByText(table_AccountOpenPolicyStatusTable, acctNum.substring(0, 6)), "Policy Number");
    }

    // @author ecoleman: Above 2 do not work, tableUtils looks for wrong stuff, so here's something that works!

    public void clickFirstPolicyInOpenPolicyStatusTable() {
        clickWhenClickable(find(By.xpath("//*[@id='AccountDetailSummary:AccountDetailSummaryScreen:AccountPoliciesLV:0:PolicyNumber'][1]")));
    }
}
