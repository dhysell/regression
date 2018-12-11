package repository.pc.policy;


import com.idfbins.helpers.EmailUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireException;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.Activity;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;
import repository.pc.activity.GenericActivityPC;
import repository.pc.sidemenu.SideMenuPC;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PolicySummary extends BasePage {

	private TableUtils tableUtils;

	public PolicySummary(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }



	public List<Activity> getCurrentActivites_FullInfo() throws GuidewireNavigationException {
		List<Activity> returnList = getCurrentActivites_BasicInfo();
		for(Activity activity : returnList) {
			clickActivity(activity.getSubject());
			waitForPostBack();
			GenericActivityPC genericActitvy = new GenericActivityPC(getDriver());
			activity.setDescription(genericActitvy.getDescription());
			activity.setStatus(Status.getEnumFromStringValue(genericActitvy.getStatus()));
			activity.setMandatory(genericActitvy.getMandatory());
			activity.setRecurring(genericActitvy.getRecurring());
			activity.setReceivedDate(genericActitvy.getReceivedDate());
			activity.setTargetDate(genericActitvy.getTargetDate());
			activity.setEscalationDate(genericActitvy.getEscalationDate2());
			if(finds(By.xpath("//span[contains(@id, ':PolicyFileLabel-btnInnerEl')]")).isEmpty()) {
				new InfoBar(getDriver()).clickInfoBarPolicyNumber();
			}
		}
		return returnList;
	}

	public List<Activity> getCurrentActivites_BasicInfo() {
        waitForPostBack();
		int numberOfActivites = finds(By.xpath("//div[contains(@id, ':Policy_SummaryScreen:Policy_Summary_ActivitiesLV-body')]/div/table/tbody/child::tr")).size();
		List<Activity> returnList = new ArrayList<Activity>();
	
		for(int i= 1; i <= numberOfActivites; i++) {
			Activity myActivity = new Activity();
			myActivity.setTargetDate(find(By.xpath("//div[contains(@id, ':Policy_SummaryScreen:Policy_Summary_ActivitiesLV-body')]/div/table/tbody/child::tr[" + i + "]/child::td[1]/div")).getText());
			myActivity.setPriority(Priority.getEnumFromStringValue(find(By.xpath("//div[contains(@id, ':Policy_SummaryScreen:Policy_Summary_ActivitiesLV-body')]/div/table/tbody/child::tr[" + i + "]/child::td[2]/div")).getText()));
			myActivity.setSubject((find(By.xpath("//div[contains(@id, ':Policy_SummaryScreen:Policy_Summary_ActivitiesLV-body')]/div/table/tbody/child::tr[" + i + "]/child::td[3]/div/a")).getText()));
			myActivity.setAssignedTo(find(By.xpath("//div[contains(@id, ':Policy_SummaryScreen:Policy_Summary_ActivitiesLV-body')]/div/table/tbody/child::tr[" + i + "]/child::td[4]/div")).getText());
			returnList.add(myActivity);
		}
		return returnList;
    }

    @FindBy(xpath = "//*[contains(@id, ':Policy_Summary_TransactionsLV-body')]/div/table")
    public WebElement table_WorkOrders;

    @FindBy(xpath = "//div[contains(@id, 'Policy_Summary_AccountDV:Number-inputEl')]")
    public WebElement link_AccountNumber;

    @FindBy(xpath = "//div[contains(@id, ':Policy_Summary_PolicyDV:Name-inputEl')]")
    public WebElement link_PNIName;

    @FindBy(xpath = "//div[contains(@id, ':Policy_Summary_TransactionsLV')]")
    public WebElement table_WorkordersWrapper;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_PolicyDV:PolicyAddress:PolicyAddressDisplayInputSet:globalAddressContainer:GlobalAddressInputSet:AddressSummary-inputEl')]")
    public WebElement text_PolicySummaryAddressValue;

    @FindBy(xpath = "//div[contains(@id, ':Policy_Summary_DatesDV:PolicyPerCost-inputEl')]")
    public WebElement text_PolicySummaryTotalPremium;
    
    @FindBy(xpath = "//div[contains(@id, ':Policy_Summary_DatesDV:PolicyPerEffDate-inputEl')]")
    public WebElement text_PolicySummaryEffDate;

    @FindBy(xpath = "//div[contains(@id, ':OfficialIDInputSet:OfficialIDDV_SSN-inputEl')]")
    public WebElement textbox_SSN;

    @FindBy(xpath = "//div[contains(@id, ':Underwriter-inputEl')]")
    public WebElement text_PolicySummaryUnderWriter;
   
    @FindBy(xpath = "//div[contains(@id, ':Policy_Summary_JobsInProgressLV')]")
    public WebElement table_PendingPolicyTransactions;

    @FindBy(xpath = "//span[contains(@id, ':Policy_SummaryScreen:preRenewalDirectionWarning')]")
    public WebElement text_PreRenewalDirection;

    @FindBy(xpath = "//a[contains(@id, ':ViewPreRenewalDirections')]")
    public WebElement link_ViewPreRenewalDirections;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_ActivitiesLV')]")
    public WebElement table_PolicySummaryActivities;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_PendingCancelsLV')]")
    public WebElement table_PolicySummaryPendingCancels;

	@FindBy(xpath = "//div[contains(@id, 'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_TransactionsLV')]")
	public WebElement table_PolicySummaryTransactions;

	@FindBy(xpath = "//span[@id=\"PolicyFile_Summary:Policy_SummaryScreen:0\"]")
	public WebElement title_PolicySummaryScreen;


    public WebElement pending_transactions(int number) {
        return find(By.xpath("//a[contains(@id,'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_JobsInProgressLV:" + number + ":JobNumber')]"));
    }


    public void clickIssuanceTransaction(String number) {
        clickWhenClickable(find(By.xpath("//a[contains(text(), '" + number + "') and contains(text(), '0002')]")));
    }


    public boolean hasPreRenewalDirections() {
        return !finds(By.xpath("//span[contains(@id, ':Policy_SummaryScreen:preRenewalDirectionWarning')]"))
                .isEmpty();
    }


    public void clickViewPreRenewalDirection() {
        waitUntilElementIsClickable(link_ViewPreRenewalDirections);
        link_ViewPreRenewalDirections.click();
    }


    public String getSSN() {
        return textbox_SSN.getText();
    }


    public double getTotalPremium() {
    	waitUntilElementIsVisible(text_PolicySummaryTotalPremium);
		double premium = NumberUtils.getCurrencyValueFromElement(text_PolicySummaryTotalPremium.getText());
        return premium;
    }


    public boolean isTransactionComplete(TransactionType transactionType, String transactionNumber) {
        boolean isInForce = true;
        
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Type", transactionType.getValue());
        if (transactionNumber!=null) {
			columnRowKeyValuePairs.put("Transaction #", transactionNumber);
		}
		try {
        	if (tableUtils.getRowInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs) != null) {
                isInForce = true;
            }
        } catch (Exception e) {
            isInForce = false;
            e.printStackTrace();
        }
        return isInForce;
    }

    public Date getTransactionEffectiveDate(TransactionType transactionType, String comments) throws ParseException {
    	HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
    	if (transactionType != null) {
    		columnRowKeyValuePairs.put("Type", transactionType.getValue());
    }
    	if (comments != null) {
    		columnRowKeyValuePairs.put("Comment", comments);
    	}
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, tableUtils.getRowInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs), "Trans Eff Date"), "MM/dd/yyyy");
    }

    public double getTransactionPremium(TransactionType transactionType, String comments) {
    	HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
    	if (transactionType != null) {
    		columnRowKeyValuePairs.put("Type", transactionType.getValue());
    }
    	if (comments != null) {
    		columnRowKeyValuePairs.put("Comment", comments);
    	}
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, tableUtils.getRowInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs), "Premium"));
    }

    public String getTransactionNumber(TransactionType transactionType, String comments) {
    	HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
    	if (transactionType != null) {
    		columnRowKeyValuePairs.put("Type", transactionType.getValue());
    }
    	if (comments != null) {
    		columnRowKeyValuePairs.put("Comment", comments);
    	}
        return tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, tableUtils.getRowInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs), "Transaction #");
    }
    
    public String getTransactionNumberByEffectiveDate(TransactionType transactionType, Date effectiveDate) {
    	HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
    	if (transactionType != null) {
    		columnRowKeyValuePairs.put("Type", transactionType.getValue());
    	}
    	if (effectiveDate != null) {
    		columnRowKeyValuePairs.put("Trans Eff Date", DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
    	}
        return tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, tableUtils.getRowInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs), "Transaction #");
    }

    public boolean summaryPageExists() {
		return checkIfElementExists("//span[@id='PolicyFile_Summary:Policy_SummaryScreen:0']", 3000);    }


    public String getUnderWriter() {
        return text_PolicySummaryUnderWriter.getText();
    }


    public void clickActivity(String activityName) {
        tableUtils.clickLinkInTable(table_PolicySummaryActivities, activityName);
    }


    public boolean checkIfActivityExists(String activityName) {
    	if(tableUtils.getRowCount(table_PolicySummaryActivities) > 0){
    		return checkIfElementExists(tableUtils.getRowInTableByColumnNameAndValue(table_PolicySummaryActivities, "Subject", activityName), 3000);
    	}else{
    		return false;
    	}
    }

    public String getActivityAssignment(String activityName) throws GuidewireException {
        if (tableUtils.hasMultiplePages(table_PolicySummaryActivities)) {
            int maxPages = tableUtils.getNumberOfTablePages(table_PolicySummaryActivities);
            int lcv = 0;
            do {
                if (checkIfActivityExists(activityName)) {
                    return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicySummaryActivities, tableUtils.getRowInTableByColumnNameAndValue(table_PolicySummaryActivities, "Subject", activityName), "Assigned To");
                } else {
                    tableUtils.incrementTablePageNumber(table_PolicySummaryActivities);
                    lcv++;
                }
            } while (!checkIfActivityExists(activityName) && lcv < maxPages);
        }

        if (!checkIfActivityExists(activityName)) {
        	throw new GuidewireException(getCurrentUrl(), "The activity: " + activityName + " does not exist.");
//            Assert.fail("The activity does not exist.");
        } else {
            return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicySummaryActivities, tableUtils.getRowInTableByColumnNameAndValue(table_PolicySummaryActivities, "Subject", activityName), "Assigned To");
        }
    }


    public void clickAccountNumber() {
        clickWhenClickable(link_AccountNumber);
    }


    public String getAddress() {
        waitUntilElementIsVisible(text_PolicySummaryAddressValue);
        String address = text_PolicySummaryAddressValue.getText();
        return address;
    }


    public void clickPrimaryNamedInsured() {
        clickWhenClickable(link_PNIName);
    }

    // find the first row with the comment.

    public double getPremium(String comment) {
        String commentGridColumn = new TableUtils(getDriver()).getGridColumnFromTable(table_WorkordersWrapper, "Comment");
        String premiumGridColumn = new TableUtils(getDriver()).getGridColumnFromTable(table_WorkordersWrapper, "Premium");
        String premiumStr = table_WorkordersWrapper
                .findElement(By.xpath(".//tr/td[contains(@class,'" + commentGridColumn + "') and contains(.,'" + comment
                        + "')]/parent::tr/td[contains(@class,'" + premiumGridColumn + "')]"))
                .getText();
        double premiumAmt = Double.parseDouble(premiumStr.replaceAll("[^0-9,^.]", ""));
        if (premiumStr.contains("("))
            premiumAmt *= -1;
        return premiumAmt;
    }

    // find the first row with the comment.

    public double getPremiumByType(TransactionType transactionType) {
        String typeGridColumn = new TableUtils(getDriver()).getGridColumnFromTable(table_WorkordersWrapper, "Type");
        String premiumGridColumn = new TableUtils(getDriver()).getGridColumnFromTable(table_WorkordersWrapper, "Premium");
        String premiumStr = table_WorkordersWrapper
                .findElement(By.xpath(".//tr/td[contains(@class,'" + typeGridColumn + "') and contains(.,'" + transactionType.getValue()
                        + "')]/parent::tr/td[contains(@class,'" + premiumGridColumn + "')]"))
                .getText();
        double premiumAmt = Double.parseDouble(premiumStr.replaceAll("[^0-9,^.]", ""));
        if (premiumStr.contains("("))
            premiumAmt *= -1;
        return premiumAmt;
    }

    /*
     * (non-Javadoc)
     *
     * @see pc.policy.interfaces.PolicySummary#selectOpenTransaction(java.lang.
     * String, java.lang.String)
     */

    public void selectOpenTransaction(String changeReason) {
        tableUtils.clickLinkInSpecficRowInTable(table_PendingPolicyTransactions, tableUtils.getRowNumberInTableByText(table_PendingPolicyTransactions, changeReason));
    }


    public void clickPendingTransaction(TransactionType transactionType) {
        tableUtils.clickLinkInSpecficRowInTable(table_PendingPolicyTransactions, tableUtils.getRowNumberInTableByText(table_PendingPolicyTransactions, transactionType.getValue()));
    }


    public void clickPendingTransactionNumberByText(String text) {
        int row = tableUtils.getRowNumberInTableByText(table_PendingPolicyTransactions, text);
        tableUtils.clickLinkInTableByRowAndColumnName(table_PendingPolicyTransactions, row, "Transaction #");
        if (checkIfElementExists("//span[contains(@id, 'PleaseBePatientPopup')]", 2000)) {
            ArrayList<String> emailList = new ArrayList<String>();
            emailList.add("bhiltbrand@idfbins.com");
            emailList.add("iclouser@idfbins.com");
            new EmailUtils().sendEmail(emailList, "Operation Processing Page Hit", "We hit the operation processing page at" + LocalDateTime.now() + ". Please investigate.", null);
            Assert.fail("The dreaded \"Operation Processing\"  page showed up when clicking on the workorder. Attempting to quick fail the test!");
        }
    }


    public List<WebElement> getPendingPolicyTransaction(TransactionType transactionType) {
        return tableUtils.getRowsInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Type", transactionType.getValue());
    }


    public String getTransactionStatus(TransactionType transactionType) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, tableUtils.getRowInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Type", transactionType.getValue()), "Status");
    }


    public String getPendingPolicyTransactionByColumnName(String value, String columnName) {
        int row = tableUtils.getRowNumberInTableByText(table_PendingPolicyTransactions, value);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, row, columnName);
    }


    public Date getTransactionEffectiveDate(TransactionType transactionType) throws ParseException {
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, tableUtils.getRowInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Type", transactionType.getValue()), "Trans Eff Date"), "MM/dd/yyyy");
    }


    public Date getTransactionEffectivePeriod(TransactionType transactionType) throws ParseException {
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, tableUtils.getRowInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Type", transactionType.getValue()), "Period Eff Date"), "MM/dd/yyyy");
    }


    public String getTransactionNumber(TransactionType transactionType) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, tableUtils.getRowInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Type", transactionType.getValue()), "Transaction #");
    }


    public String getTransactionNumberByComment(String comment) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PendingPolicyTransactions, tableUtils.getRowInTableByColumnNameAndValue(table_PendingPolicyTransactions, "Comment", comment), "Transaction #");
    }


    public void clickpolicyPendingTransaction() {
        clickWhenClickable(pending_transactions(0));
    }


    public void clickCompletedTransaction(int number) {
        clickWhenClickable(find(By.xpath("//a[contains(@id,'PolicyFile_Summary:Policy_SummaryScreen:Policy_Summary_TransactionsLV:" + number + ":JobNumber')]")));
    }


    public boolean verifyPendingCancellation(Date cancellationEffectiveDate, Cancellation.CancellationStatus cancellationStatus, Boolean partialCancel, Double delinquentAmount, int secondsToWait) throws Exception {
        boolean found = false;
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (cancellationEffectiveDate != null) {
            columnRowKeyValuePairs.put("Cancellation Effective Date", DateUtils.dateFormatAsString("MM/dd/yyyy", cancellationEffectiveDate));
        }
        if (cancellationStatus != null) {
            columnRowKeyValuePairs.put("Status", cancellationStatus.getValue());
        }
        if (partialCancel != null) {
            if (partialCancel) {
                columnRowKeyValuePairs.put("Partial Cancel?", "Yes");
            } else {
                columnRowKeyValuePairs.put("Partial Cancel?", "No");
            }
        }
        if (delinquentAmount != null) {
            columnRowKeyValuePairs.put("Delinquent Amount", StringsUtils.currencyRepresentationOfNumber(delinquentAmount));
        }

        List<WebElement> pendingCancelRows = tableUtils.getRowsInTableByColumnsAndValues(table_PolicySummaryPendingCancels, columnRowKeyValuePairs);
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (pendingCancelRows.size() > 0) {
                found = true;
            } else {
                found = false;
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the cancelled policy again.
                secondsRemaining = secondsRemaining - delayInterval;
                SideMenuPC sideMenu = new SideMenuPC(getDriver());
                sideMenu.clickSideMenuToolsBilling();
                sideMenu.clickSideMenuToolsSummary();

                pendingCancelRows = tableUtils.getRowsInTableByColumnsAndValues(table_PolicySummaryPendingCancels, columnRowKeyValuePairs);
            }
        }

        return found;
    }


    public Date getCompletedTransactionEffectiveDate(TransactionType transactionType) throws ParseException {
		int row = tableUtils.getRowNumberInTableByText(table_WorkordersWrapper, transactionType.getValue());
        return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, row, "Period Eff Date"), "MM/dd/yyyy");
    }


    public String getCompletedTransactionNumberByType(TransactionType transactionType) {
        int row = tableUtils.getRowNumberInTableByText(table_WorkordersWrapper, transactionType.getValue());
        return tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, row, "Transaction #");
    }


    public void clickCompletedTransactionByType(TransactionType transactionType) {
        int row = tableUtils.getRowNumberInTableByText(table_WorkordersWrapper, transactionType.getValue());
        tableUtils.clickLinkInSpecficRowInTable(table_WorkordersWrapper, row);
    }


    public void clickPendingCancelTransactionLinkByText(String text) {
        int row = tableUtils.getRowNumberInTableByText(table_PolicySummaryPendingCancels, text);
        tableUtils.clickLinkInSpecficRowInTable(table_PolicySummaryPendingCancels, row);
    }

    public String getPendingCancelTransactionEffectiveDate(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicySummaryPendingCancels, row, "Cancellation Effective Date");
    }

    public String getPendingPolicyTransactionEffectiveDate(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicySummaryPendingCancels, row, "Cancellation Effective Date");
    }

    public double getRenewalPremium(TransactionType transactionType) {
        int row = tableUtils.getRowNumberInTableByText(table_WorkordersWrapper, transactionType.getValue());
        String premium = tableUtils.getCellTextInTableByRowAndColumnName(table_WorkordersWrapper, row, "Premium");
        return NumberUtils.getCurrencyValueFromElement(premium);
    }


    public boolean verifyPolicyTransaction(Date periodEffectiveDate, Date transactionEffectiveDate, Date transactionCloseDate, TransactionType transactionType, String comment, int secondsToWait) throws Exception {
        boolean found = false;
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (periodEffectiveDate != null) {
            columnRowKeyValuePairs.put("Period Eff Date", DateUtils.dateFormatAsString("MM/dd/yyyy", periodEffectiveDate));
        }
        if (transactionEffectiveDate != null) {
            columnRowKeyValuePairs.put("Trans Eff Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionEffectiveDate));
        }
        if (transactionCloseDate != null) {
            columnRowKeyValuePairs.put("Trans Close Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionCloseDate));
        }
        if (transactionType != null) {
            columnRowKeyValuePairs.put("Type", transactionType.getValue());
        }
        if (comment != null) {
            columnRowKeyValuePairs.put("Comment", comment);
        }

        List<WebElement> policyTransactionsRows = tableUtils.getRowsInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs);
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (policyTransactionsRows.size() > 0) {
                found = true;
            } else {
                found = false;
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check the policy transaction status again.
                secondsRemaining = secondsRemaining - delayInterval;
                SideMenuPC sideMenu = new SideMenuPC(getDriver());
                sideMenu.clickSideMenuToolsBilling();
                sideMenu.clickSideMenuToolsSummary();

                policyTransactionsRows = tableUtils.getRowsInTableByColumnsAndValues(table_WorkordersWrapper, columnRowKeyValuePairs);
            }
        }

        return found;
    }

    public boolean verifyPendingTransactions(Date periodEffDate, Date TransEffDate, TransactionStatus status,TransactionType type, String transactionNumber, int secondsToWait) throws Exception {
        boolean found = false;
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        if (periodEffDate != null) {
            columnRowKeyValuePairs.put("Period Eff Date", DateUtils.dateFormatAsString("MM/dd/yyyy", periodEffDate));
        }
        if (TransEffDate != null) {
            columnRowKeyValuePairs.put("Trans Eff Date", DateUtils.dateFormatAsString("MM/dd/yyyy", TransEffDate));
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (type != null) {
            columnRowKeyValuePairs.put("Type", type.getValue());
        }
        if (transactionNumber != null) {
            columnRowKeyValuePairs.put("Transaction #", transactionNumber);
        }
        List<WebElement> pendingTransactionsRows = tableUtils.getRowsInTableByColumnsAndValues(table_PendingPolicyTransactions, columnRowKeyValuePairs);
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (pendingTransactionsRows.size() > 0) {
                found = true;
            } else {
                found = false;
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check the pending policy transaction again.
                secondsRemaining = secondsRemaining - delayInterval;
                SideMenuPC sideMenu = new SideMenuPC(getDriver());
                sideMenu.clickSideMenuToolsBilling();
                sideMenu.clickSideMenuToolsSummary();

                pendingTransactionsRows = tableUtils.getRowsInTableByColumnsAndValues(table_PendingPolicyTransactions, columnRowKeyValuePairs);
            }
        }
        return found;
    }

}