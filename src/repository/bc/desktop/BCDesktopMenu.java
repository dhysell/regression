package repository.bc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;

public class BCDesktopMenu extends BasePage {

    public BCDesktopMenu(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//a[contains(@id, 'Actions')]")
    public WebElement link_MenuActions;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransaction;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment-itemEl']")
    public WebElement link_DesktopMenuActionsNewPayment;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewDisbursementTransaction-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionDisbursement;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:DesktopMenuActions_MultiPaymentEntryWizard-itemEl']")
    public WebElement link_DesktopMenuActionsNewPaymentMultiplePayment;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:DesktopMenuActions_CountyMultiPaymentEntryWizard-itemEl']")
    public WebElement link_DesktopMenuActionsNewPmtCountCashPayment;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:DesktopMenuActions_NexusMultiPaymentEntryWizard-itemEl']")
    public WebElement link_DesktopMenuActionsNewPmtNexusPayment;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:MultiAccountPayment-itemEl']")
    public WebElement link_DesktopMenuActionsNewPaymentMultipleAccountLienholderPayment;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:DesktopMenuActions_NewPaymentRequest-itemEl']")
    public WebElement link_DesktopMenuActionsNewPmtPaymentRequest;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewPayment:DesktopMenuActions_NexusMultiPaymentEntryWizard-itemEl']")
    public WebElement link_DesktopMenuActionsNewPmtElectronicPaymentEntry;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewTransactionFee']")
    // this
    // one
    // is
    // gone
    // in
    // gw8?
    public WebElement link_DesktopMenuActionsNewTransactionFee;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewTransactionTransfer-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionTransfer;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewTransactionWriteoff-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionWriteOff;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewTransactionNegativeWriteoff-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionNegativeWriteOff;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewChargeReversal-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionChargeReversal;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewWriteoffReversal-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionWriteOffReversal;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewNegativeWriteoffReversal-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionNegativeWriteOffReversal;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_NewTransaction:DesktopMenuActions_NewFundsTransferReversal-itemEl']")
    public WebElement link_DesktopMenuActionsNewTransactionTransferFundRev;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity-itemEl']")
    public WebElement link_DesktopMenuNewAssignedActivity;

    @FindBy(xpath = "//span[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category']")
    public WebElement link_DesktopMenuActionsNewAssignedActApproval;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category-itemEl']")
    public WebElement link_DesktopMenuActionsNewAssignedActReminder;

    @FindBy(xpath = "//span[contains(@id,'DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:') and contains(@id,':NewActivityMenuItemSet_Category-textEl') and contains(text(),'Request')]")
    public WebElement link_DesktopMenuActionsNewAssignedActRequest;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category:0:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category:1:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq2;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category:2:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq3;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:1:NewActivityMenuItemSet_Category:0:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActReminderActivityReminder;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category:0:item-itemEl']")
    public WebElement link_DesktopMenuActionsNewAssignedActReminderNotification;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:0:NewActivityMenuItemSet_Category:1:item-itemEl']")
    public WebElement link_DesktopMenuActionsNewAssignedActReminderWriteOffAttempedAboveThreshold;

    @FindBy(xpath = "//span[contains(@id,'DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:') and contains(@id, ':NewActivityMenuItemSet_Category:') and contains(text(), 'Activity request')]")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestActRequest;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:1:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestReviewBankInfo;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:2:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestReviewBankInfo2;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:3:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestReviewDisburseError;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:4:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestReviewDoc;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:5:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestReviewDoc2;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:NewAssignedActivity:NewActivityMenuItemSet:2:NewActivityMenuItemSet_Category:6:item']")
    public WebElement link_DesktopMenuActionsNewAssignedActRequestUnknownDocType;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:AccountDetailMenuActions_Report-itemEl']")
    public WebElement link_DesktopMenuActionsReports;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:AccountDetailMenuActions_Report:AccountDetailMenuActions_DailyBalanceReport-itemEl']")
    public WebElement link_DesktopMenuActionsReportsAllDayBalance;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:AccountDetailMenuActions_Report:AccountDetailMenuActions_AgingReport']")
    public WebElement link_DesktopMenuActionsReportsAgingReport;

    @FindBy(xpath = "//a[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_UserPreferences-itemEl']")
    public WebElement link_DesktopMenuActionsPreferences;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopActivities']")
    public WebElement link_DesktopMenuMyActivities;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopQueues']")
    public WebElement link_DesktopMenuMyQueues;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopApprovals']")
    public WebElement link_DesktopMenuApprovalStatuses;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopTroubleTickets']")
    public WebElement link_DeskMenuMyTroubleTickets;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopDelinquencies']")
    public WebElement link_DesktopMenuMyDelinquencies;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopDisbursements']")
    public WebElement link_DesktopMenuDisbursements;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_LienholderMultiPayments']")
    public WebElement link_DesktopMenuLienholderMultiPayments;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopAgencyItems']")
    public WebElement link_DesktopMenuMyAgencyItems;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopHeldCharges']")
    public WebElement link_DesktopMenuHeldCharges;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopUnattachedDocumentWorkLists']")
    public WebElement link_DesktopMenuUnattachedDocuments;

    @FindBy(xpath = "//select[contains(@id, 'ListPaging')]")
    public WebElement select_DesktopMenuPaging;

    @FindBy(xpath = "//input[contains(@id, '_Checkbox')]")
    public WebElement input_DesktopTablesCheckbox;

	@FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopSuspensePayments']")
	public WebElement link_DesktopMenuSuspensePayments;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void clickMenuActions() {
        
        waitUntilElementIsClickable(link_MenuActions);
        link_MenuActions.click();
    }

    public void clickDesktopMenuActionsNewTransaction() {
        clickWhenVisible(link_DesktopMenuActionsNewTransaction);
    }

    public void clickDesktopMenuActionsNewTransactionDisbursement() {
        clickMenuActions();
        
        clickDesktopMenuActionsNewTransaction();
        
        clickWhenVisible(link_DesktopMenuActionsNewTransactionDisbursement);
        
    }

    public void clickDesktopMenuActionsNewPayment() {
        clickWhenVisible(link_DesktopMenuActionsNewPayment);
    }

    public void clickDesktopMenuActionsMultiplePayment() {
        int count = 1;
        Boolean multiPayment = false;
        while (count < 3 && multiPayment == false) {
            System.out.println("Count is: " + count);
            count++;

            clickMenuActions();
            
            hoverOver(link_DesktopMenuActionsNewPayment);
            
            link_DesktopMenuActionsNewPayment.click();
            
            hoverOver(link_DesktopMenuActionsNewPaymentMultiplePayment);
            
            clickWhenVisible(link_DesktopMenuActionsNewPaymentMultiplePayment);
            multiPayment = true;
        }

    }

    public void clickDesktopMenuActionsMultipleAccountLienholderPayment() {
        clickMenuActions();
        
        hoverOver(link_DesktopMenuActionsNewPayment);
        
        link_DesktopMenuActionsNewPayment.click();
        
        hoverOver(link_DesktopMenuActionsNewPaymentMultipleAccountLienholderPayment);
        
        clickWhenVisible(link_DesktopMenuActionsNewPaymentMultipleAccountLienholderPayment);
    }

    public void clickDesktopMenuActionsCountyCashPayment() {
        clickMenuActions();
        
        hoverOver(link_DesktopMenuActionsNewPayment);
        
        link_DesktopMenuActionsNewPayment.click();
        
        hoverOver(link_DesktopMenuActionsNewPmtCountCashPayment);
        
        clickWhenVisible(link_DesktopMenuActionsNewPmtCountCashPayment);
    }

    public void clickDesktopMenuActionsNexusPayment() {
        clickMenuActions();
        
        hoverOver(link_DesktopMenuActionsNewPayment);
        
        link_DesktopMenuActionsNewPayment.click();
        
        hoverOver(link_DesktopMenuActionsNewPmtNexusPayment);
        
        clickWhenVisible(link_DesktopMenuActionsNewPmtNexusPayment);
    }

    public void clickDesktopMenuActionsElectronicPayment() {
        clickMenuActions();
        
        hoverOver(link_DesktopMenuActionsNewPayment);
        
        link_DesktopMenuActionsNewPayment.click();
        
        hoverOver(link_DesktopMenuActionsNewPmtElectronicPaymentEntry);
        
        clickWhenVisible(link_DesktopMenuActionsNewPmtElectronicPaymentEntry);
    }

    public void clickDesktopMenuActionsPaymentRequest() {
        clickMenuActions();
        clickDesktopMenuActionsNewPayment();
        clickWhenVisible(link_DesktopMenuActionsNewPmtPaymentRequest);
    }

    public void clickDesktopMenuActionsFee() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionFee);
    }

    public void clickDesktopMenuActionsTransfer() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionTransfer);
    }

    public void clickDesktopMenuActionsWriteOff() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        // System.out.println("\nit's my test\n");
        clickWhenVisible(link_DesktopMenuActionsNewTransactionWriteOff);
    }

    public void clickDesktopMenuActionsNegativeWriteOff() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionNegativeWriteOff);
    }

    public void clickDesktopMenuActionsChargeReversal() {
        clickMenuActions();
        
        hoverOver(link_DesktopMenuActionsNewTransaction);
        
        link_DesktopMenuActionsNewTransaction.click();
        
        hoverOver(link_DesktopMenuActionsNewTransactionChargeReversal);
        
        link_DesktopMenuActionsNewTransactionChargeReversal.click();
    }

    public void clickDesktopMenuActionsWriteOffReversal() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionWriteOffReversal);
    }

    public void clickDesktopMenuActionsNegativeWriteOffRev() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionNegativeWriteOffReversal);
    }

    public void clickDesktopMenuActionsTransferFundRev() {
        clickMenuActions();
        clickDesktopMenuActionsNewTransaction();
        clickWhenVisible(link_DesktopMenuActionsNewTransactionTransferFundRev);
    }

    public void clickDesktopMenuActionsNewAssignedAct() {
        clickWhenVisible(link_DesktopMenuNewAssignedActivity);
    }

    public void clickDesktopMenuActionsApproval() {
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActApproval);
    }

    public void clickDesktopMenuActionsReminder() {
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActReminder);
    }

    public void clickDesktopMenuActionsRequest() {
        clickMenuActions();
        
        clickDesktopMenuActionsNewAssignedAct();
        
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequest);
    }

    public void clickDesktopMenuActionsReviewDelinquency() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickDesktopMenuActionsApproval();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq);
    }

    public void clickDesktopMenuActionsReviewDelinquency2() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickDesktopMenuActionsApproval();
        clickWhenClickable(link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq2);
    }

    public void clickDesktopMenuActionsReviewDelinquency3() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickDesktopMenuActionsApproval();
        clickWhenClickable(link_DesktopMenuActionsNewAssignedActApprovalReviewDelinq3);
    }

    public void clickDesktopMenuActionsActivityReminder() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        // clickDesktopMenuActionsReminder();
        hoverOver(link_DesktopMenuActionsNewAssignedActReminder);
        clickWhenClickable(link_DesktopMenuActionsNewAssignedActReminderActivityReminder);
    }

    public void clickDesktopMenuActionsNotification() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickDesktopMenuActionsReminder();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActReminderNotification);
    }

    public void clickDesktopMenuActionsWriteOffAttemptAboveThreshold() {
        clickMenuActions();
        
        clickDesktopMenuActionsNewAssignedAct();
        
        clickDesktopMenuActionsReminder();
        
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActReminderWriteOffAttempedAboveThreshold);
        
    }

    public void clickDesktopMenuActionsActivityRequest() {

        clickMenuActions();
        
        hoverOver(link_DesktopMenuNewAssignedActivity);
        
        link_DesktopMenuNewAssignedActivity.click();
        
        hoverOver(link_DesktopMenuActionsNewAssignedActRequest);
        
        link_DesktopMenuActionsNewAssignedActRequest.click();
        
        hoverOver(link_DesktopMenuActionsNewAssignedActRequestActRequest);
        
        link_DesktopMenuActionsNewAssignedActRequestActRequest.click();
    }

    public void clickDesktopMenuActionsReviewBankInfo() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestReviewBankInfo);
    }

    public void clickDesktopMenuActionsReviewBankInfo2() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestReviewBankInfo2);
    }

    public void clickDesktopMenuActionsReviewDisburseError() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestReviewDisburseError);
    }

    public void clickDesktopMenuActionsReviewDoc() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestReviewDoc);
    }

    public void clickDesktopMenuActionsReviewDoc2() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestReviewDoc2);
    }

    public void clickDesktopMenuActionsUnknownDocType() {
        clickMenuActions();
        clickDesktopMenuActionsNewAssignedAct();
        clickWhenVisible(link_DesktopMenuActionsNewAssignedActRequestUnknownDocType);
    }

    public void clickDesktopMenuActionsReports() {
        clickWhenVisible(link_DesktopMenuActionsReports);
    }

    public void clickDesktopMenuActionsAllDayBal() {
        clickMenuActions();
        clickDesktopMenuActionsReports();
        clickWhenVisible(link_DesktopMenuActionsReportsAllDayBalance);
    }

    public void clickDesktopMenuActionsAgeReport() {
        clickMenuActions();
        clickDesktopMenuActionsReports();
        clickWhenVisible(link_DesktopMenuActionsReportsAgingReport);
    }

    public void clickDesktopMenuActionsPreferences() {
        clickMenuActions();
        clickWhenVisible(link_DesktopMenuActionsPreferences);
    }

    public void clickDesktopMenuMyActivities() {
        clickWhenVisible(link_DesktopMenuMyActivities);
        
    }

    public void clickDesktopMenuMyQueues() {
        clickWhenVisible(link_DesktopMenuMyQueues);
        
    }

    public void clickDesktopMenuApprovalStatuses() {
        clickWhenVisible(link_DesktopMenuApprovalStatuses);
        
    }

    public void clickDesktopMenuMyTroubleTickets() {
        clickWhenVisible(link_DeskMenuMyTroubleTickets);
        
    }

    public void clickDesktopMenuMyDelinquencies() {
        clickWhenVisible(link_DesktopMenuMyDelinquencies);
        
    }

    public void clickDesktopMenuDisbursements() {
        clickWhenVisible(link_DesktopMenuDisbursements);
        
    }

    public void clickDesktopMenuLienholderMultiPayments() {
        clickWhenVisible(link_DesktopMenuLienholderMultiPayments);
        
    }

    public void clickDesktopMenuMyAgencyItems() {
        clickWhenVisible(link_DesktopMenuMyAgencyItems);
        
    }

    public void clickDesktopMenuHeldCharges() {
        clickWhenVisible(link_DesktopMenuHeldCharges);
        
    }

    public void clickDesktopMenuUnattachedDocuments() {
        clickWhenVisible(link_DesktopMenuUnattachedDocuments);
        
    }

    public void selectPage(Integer pageNum) {
        waitUntilElementIsClickable(select_DesktopMenuPaging);
        new Select(select_DesktopMenuPaging).selectByVisibleText(pageNum.toString());

    }

    public void clickTableCheckbox(int checkboxNum) {
        Integer theRealCheckboxNum = new Integer(checkboxNum - 1);
        waitUntilElementIsClickable(input_DesktopTablesCheckbox);
        input_DesktopTablesCheckbox.findElement(By.xpath("//input[contains(@id, ':" + theRealCheckboxNum.toString() + ":')]")).click();

    }

	public void clickDesktopMenuSuspensePayments() {
		clickWhenVisible(link_DesktopMenuSuspensePayments);
		
	}

    public void clickDesktopMenuActionsCountyCashPaymentBad() {
        // TODO Auto-generated method stub

    }

}
