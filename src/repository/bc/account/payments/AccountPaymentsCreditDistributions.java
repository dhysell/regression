package repository.bc.account.payments;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.wizards.NegativeWriteoffReversalWizard;
import repository.bc.wizards.NegativeWriteoffWizard;
import repository.bc.wizards.NewTransferWizard;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.TransferReason;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AccountPaymentsCreditDistributions extends AccountPayments {

    private TableUtils tableUtils;

    public AccountPaymentsCreditDistributions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths
    // -----------------------------------------------

    @FindBy(xpath = "//div[@id='AccountCreditDistributions:AccountDetailPaymentsScreen:DirectBillCreditDistributionListDetail:AccountDBPaymentsLV']")
    public WebElement table_PaymentsCreditDistribution;

    @FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:PaymentDetailsTab')]")
    public WebElement tab_PaymentDetailsTab;

    @FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:NegativeWriteoffDetailsTab')]")
    public WebElement tab_NegativeWriteOffTab;

    @FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:TransferDetailsTab')]")
    public WebElement tab_TransferTab;

    @FindBy(xpath = "//a[contains(@id,':AccountPaymentDistributionItemsCV:DisbursementsTab')]")
    public WebElement tab_DisbursementsTab;

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Payment Details Tab
    // -----------------------------------------------

    @FindBy(xpath = "//div[@id='AccountCreditDistributions:AccountDetailPaymentsScreen:DirectBillCreditDistributionListDetail:AccountPaymentDistributionItemsCV:DirectBillDistItemsLV']")
    public WebElement table_PaymentsDetailsTab;

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Negative Writeoff Tab
    // -----------------------------------------------

    @FindBy(xpath = "//div[@id='AccountCreditDistributions:AccountDetailPaymentsScreen:DirectBillCreditDistributionListDetail:AccountPaymentDistributionItemsCV:4']")
    public WebElement table_NegativeWriteOff;

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Transfers Tab
    // -----------------------------------------------

    @FindBy(xpath = "//div[@id='AccountCreditDistributions:AccountDetailPaymentsScreen:DirectBillCreditDistributionListDetail:AccountPaymentDistributionItemsCV:TransferPaymentsLV']")
    public WebElement table_Transfers;

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Disbursement Tab
    // -----------------------------------------------

    @FindBy(xpath = "//div[@id='AccountCreditDistributions:AccountDetailPaymentsScreen:DirectBillCreditDistributionListDetail:AccountPaymentDistributionItemsCV:DisbursementsLV:1']")
    public WebElement table_Disbursements;

    @FindBy(xpath = ".//a[contains(@id,':DisbursementsLV:DisbursementDetailDV_tb:Edit')]")
    public WebElement button_DisbursementsEdit;

    @FindBy(xpath = "//a[contains(@id,':DisbursementDetailDV_tb:Reject')]")
    public WebElement button_DisbursementsReject;

    // -------------------------------------------------------
    // Helper Methods for Credit Distribution Elements
    // -------------------------------------------------------

    public void clickPaymentDetailsTab() {
        clickWhenVisible(tab_PaymentDetailsTab);
    }

    public void clickNegativeWriteOffTab() {
        clickWhenVisible(tab_NegativeWriteOffTab);
    }

    public void clickTransferTab() {
        clickWhenVisible(tab_TransferTab);
    }

    public void clickDisbursementsTab() {
        clickWhenVisible(tab_DisbursementsTab);
    }

    public boolean verifyPaymentCreditDistribution(Date paymentDate, PaymentInstrumentEnum instrument, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        columnRowKeyValuePairs.put("Payment Instrument", instrument.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_PaymentsCreditDistribution, columnRowKeyValuePairs);
        return allRows.size() == 1;
    }

    //click a row in Credit Distribution table so the Payment Details for that row will display below
    public void clickRowOfCreditDistribution(Date paymentDate, PaymentInstrumentEnum instrument, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        columnRowKeyValuePairs.put("Payment Instrument", instrument.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        tableUtils.getRowInTableByColumnsAndValues(table_PaymentsCreditDistribution, columnRowKeyValuePairs).click();
    }

    public void makeNegativeWriteoff(Date paymentDate, PaymentInstrumentEnum instrument, Double amount, Double amountToWriteoff) {
        clickRowOfCreditDistribution(paymentDate, instrument, amount);
        
        find(By.xpath(".//div/a[contains(@id, ':ActionButton:NegativeWriteOff-itemEl')]")).click();
        
        NegativeWriteoffWizard writeoff = new NegativeWriteoffWizard(getDriver());
        writeoff.setNegativeWriteoffAmount(amountToWriteoff);
        
        writeoff.clickNext();
        
        writeoff.clickFinish();
    }

    public void makeNegativeWriteoffReversal(Date paymentDate, Double amount, Double amountDistributed, Double amountToWriteoff) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        columnRowKeyValuePairs.put("Amount Distributed", StringsUtils.currencyRepresentationOfNumber(amountDistributed));
        tableUtils.getRowInTableByColumnsAndValues(table_PaymentsCreditDistribution, columnRowKeyValuePairs).click();
        
        find(By.xpath(".//div/a[contains(@id, ':ActionButton:NegativeWriteOffReversal-itemEl')]")).click();
        
        NegativeWriteoffReversalWizard reversal = new NegativeWriteoffReversalWizard(getDriver());
        reversal.clickNegativeWriteoffReversalLinkByDateAndAmount(paymentDate, amountToWriteoff);
        
        reversal.clickFinish();
    }

    public void clickActionsTransfer(Date paymentDate, PaymentInstrumentEnum instrument, Double amount) {
        clickRowOfCreditDistribution(paymentDate, instrument, amount);
        
        find(By.xpath(".//div/a[contains(@id, ':ActionButton:Transfer-itemEl')]")).click();
        
    }

    // -------------------------------------------------------
    // Helper Methods for Payment Details Tab elements
    // -------------------------------------------------------

    public boolean verifyPaymentDetails(Date invoiceDate, String owner, String payer, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDate));
        columnRowKeyValuePairs.put("Owner", owner);
        columnRowKeyValuePairs.put("Payer", payer);
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_PaymentsDetailsTab, columnRowKeyValuePairs);
        return allRows.size() == 1;
    }

    //for each row of the Credit Distribution, the Owners in Payment Details table should be the same
    public boolean verifyPaymentDetailsOwner() {
        String nextOwner, paymentdetailsTBLOwnerGrid;
        boolean ownerAreSame = true, multiplePages = true;
        int creditdistributionsTableRows, paymentdetailsTableRowCount;
        creditdistributionsTableRows = tableUtils.getRowCount(table_PaymentsCreditDistribution);
        for (int i = 0; i < creditdistributionsTableRows; i++) {
            //click each row of Credit Distribution to make the Payment Details table for that row display
            table_PaymentsCreditDistribution.findElement(By.xpath(".//tr[@data-recordindex='" + i + "']")).click();
            
            //if the Payment Details exist, verify the owners;
            if (checkIfElementExists(table_PaymentsDetailsTab, 200)) {
                paymentdetailsTBLOwnerGrid = tableUtils.getGridColumnFromTable(table_PaymentsDetailsTab, "Owner");
                paymentdetailsTableRowCount = tableUtils.getRowCount(table_PaymentsDetailsTab);
                //get the Owner of each row in Payment Details table, compare them with the Owner in first row,
                //if Payment Details table only have one row, doesn't need to verify the owner
                if (paymentdetailsTableRowCount > 1) {
                    String theFirstOwner = table_PaymentsDetailsTab.findElement(By.xpath(".//tr[@data-recordindex='0']/td[contains(@class,'" + paymentdetailsTBLOwnerGrid + "')]/div/a")).getText();
                    for (int k = 1; k < paymentdetailsTableRowCount; k++) {
                        nextOwner = table_PaymentsDetailsTab.findElement(By.xpath(".//tr[@data-recordindex='" + k + "']/td[contains(@class,'" + paymentdetailsTBLOwnerGrid + "')]/div/a")).getText();

                        if (!nextOwner.equals(theFirstOwner)) {
                            System.out.println("In Page #" + tableUtils.getCurrentTablePageNumber(table_PaymentsDetailsTab) + ", the Payment Details table has different owners for Payment Row #" + (i + 1) + " in Payment Results table.");
                            ownerAreSame = false;
                        }
                    }
                }
            }
        }
        while (tableUtils.incrementTablePageNumber(table_PaymentsDetailsTab)) {
            creditdistributionsTableRows = tableUtils.getRowCount(table_PaymentsCreditDistribution);
            for (int i = 0; i < creditdistributionsTableRows; i++) {
                //click each row of Credit Distribution to make the Payment Details table for that row display
                table_PaymentsCreditDistribution.findElement(By.xpath(".//tr[@data-recordindex='" + i + "']")).click();
                
                //if the Payment Details exist, verify the owners;
                if (checkIfElementExists(table_PaymentsDetailsTab, 200)) {
                    paymentdetailsTBLOwnerGrid = tableUtils.getGridColumnFromTable(table_PaymentsDetailsTab, "Owner");
                    paymentdetailsTableRowCount = tableUtils.getRowCount(table_PaymentsDetailsTab);
                    //get the Owner of each row in Payment Details table, compare them with the Owner in first row,
                    //if Payment Details table only have one row, doesn't need to verify the owner
                    if (paymentdetailsTableRowCount > 1) {
                        String theFirstOwner = table_PaymentsDetailsTab.findElement(By.xpath(".//tr[@data-recordindex='0']/td[contains(@class,'" + paymentdetailsTBLOwnerGrid + "')]/div/a")).getText();
                        for (int k = 1; k < paymentdetailsTableRowCount; k++) {
                            nextOwner = table_PaymentsDetailsTab.findElement(By.xpath(".//tr[@data-recordindex='" + k + "']/td[contains(@class,'" + paymentdetailsTBLOwnerGrid + "')]/div/a")).getText();

                            if (!nextOwner.equals(theFirstOwner)) {
                                System.out.println("In Page #" + tableUtils.getCurrentTablePageNumber(table_PaymentsDetailsTab) + ", the Payment Details table has different owners for Payment Row #" + (i + 1) + " in Payment Results table.");
                                ownerAreSame = false;
                            }
                        }
                    }
                }
            }
        }
        return ownerAreSame;
    }

    // -------------------------------------------------------
    // Helper Methods for Transfers Tab elements
    // -------------------------------------------------------

    public boolean verifyTransfer(Date transactionDate, Double amount, String targetAccount, TransferReason reason) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Transaction Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionDate));
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        columnRowKeyValuePairs.put("Target Account", targetAccount);
        columnRowKeyValuePairs.put("Transfer Reason", reason.getValue());
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_Transfers, columnRowKeyValuePairs);
        return allRows.size() == 1;
    }

    public void reverseTransfer(Date transactionDate, Double amount, String targetAccount, TransferReason reason) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Transaction Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionDate));
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        columnRowKeyValuePairs.put("Target Account", targetAccount);
        columnRowKeyValuePairs.put("Transfer Reason", reason.getValue());
        tableUtils.getRowInTableByColumnsAndValues(table_Transfers, columnRowKeyValuePairs).click();
        
        find(By.xpath(".//div/a[contains(@id, ':Reverse:item-itemEl')]")).click();
        
        NewTransferWizard transferReversal = new NewTransferWizard(getDriver());
        transferReversal.clickReverseTransferButton();
        
    }

    // -------------------------------------------------------
    // Helper Methods for Negative Write off Tab elements
    // -------------------------------------------------------

    public boolean verifyNegativeWriteoff(Date date, Double amount, String user, YesOrNo reversed) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        columnRowKeyValuePairs.put("user", user);
        columnRowKeyValuePairs.put("Reversed", reversed.getValue());
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_NegativeWriteOff, columnRowKeyValuePairs);
        return allRows.size() >= 1;
    }

    // -------------------------------------------------------
    // Helper Methods for Disbursements Tab elements
    // -------------------------------------------------------

    public void clickActionsCreateDisbursement(Date paymentDate, PaymentInstrumentEnum instrument, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
        columnRowKeyValuePairs.put("Payment Instrument", instrument.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        tableUtils.getRowInTableByColumnsAndValues(table_PaymentsCreditDistribution, columnRowKeyValuePairs).click();
        
        find(By.xpath(".//div/a[contains(@id, ':ActionButton:CreateDisbursement-itemEl')]")).click();
        
    }

    public boolean verifyDisbursement(DisbursementStatus status, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Status", status.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(table_Disbursements, columnRowKeyValuePairs);
        return allRows.size() >= 1;
    }

    public void clickDisbursement(DisbursementStatus status, Double amount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Status", status.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        tableUtils.getRowInTableByColumnsAndValues(table_Disbursements, columnRowKeyValuePairs).click();

    }

    public void clickDisbursementEdit() {
        clickWhenVisible(button_DisbursementsEdit);
        
    }

    public void rejectDisbursement() {
        clickDisbursementEdit();
        button_DisbursementsReject.click();
        
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }
}
