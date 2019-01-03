package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//common repository for Account Activities screen and Policy Activities screen
public class BCCommonActivities extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCCommonActivities(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    @FindBy(xpath = "//div[@id='AccountActivitiesPage:1' or @id= 'PolicyPeriodActivitiesPage:0']")
    public WebElement table_ActivityResults;

    @FindBy(xpath = "//div[contains(@id, ':LoanPayerChangeDetailsLV')]")
    public WebElement table_LoanPayerChanges;

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV_Subject-inputEl')]")
    public WebElement editbox_ActivitySubject;

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV_TargetDate-inputEl')]")
    public WebElement editbox_ActivityDueDate;

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV_EscalationDate-inputEl')]")
    public WebElement editbox_ActivityEscalationDate;

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV:EdgeEntitiesInputSet:account-inputEl')]")
    public WebElement editbox_ActivityAccount;

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV:EdgeEntitiesInputSet:policyPeriod')]")
    public WebElement editbox_ActivityPolicyPeriod;

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV_LoanNumber-inputEl')]")
    public WebElement label_OldLoanNumber;

    @FindBy(xpath = "//textarea[contains(@id, ':ActivityDetailDV:ActivityDetailDV_Description-inputEl')]")
    public WebElement textarea_ActivityDescription;

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV_AssignActivity-inputEl')]")
    public WebElement label_ActivityAssignedTo;   

    @FindBy(xpath = "//div[contains(@id, ':ActivityDetailDV_Status-inputEl')]")
    public WebElement label_ActivityStatus;

    @FindBy(xpath = "//a[contains(@id, ':ActivityDetailToolbarButtons_WriteoffButton')]")
    public WebElement button_ActivityWriteOff;

    Guidewire8Select select_ActivityFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityFilter-triggerWrap')]");
    }

    Guidewire8Select select_ActivityPattern() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityPattern-triggerWrap')]");
    }

    Guidewire8Select select_SubType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountActivitySubtypesFilter-triggerWrap')]");
    }

    Guidewire8Select select_Priority() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityDetailDV_Priority-triggerWrap')]");
    }
    
    Guidewire8Select select_AssignedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityDetailDV_AssignActivity-triggerWrap')]");
    }

    ///////////////////////////////////////
    // Helper Methods for Above Elements //
    ///////////////////////////////////////

    public void selectActivityFilter(ActivityFilter filter) {
        Guidewire8Select mySelect = select_ActivityFilter();
        mySelect.selectByVisibleText(filter.getValue());
    }

    public void selectActivityPattern(ActivityPattern pattern) {
        Guidewire8Select mySelect = select_ActivityPattern();
        mySelect.selectByVisibleText(pattern.getValue());
    }

    public void selectActivitySubType(ActivitySubFilter subType) {
        Guidewire8Select mySelect = select_SubType();
        mySelect.selectByVisibleText(subType.getValue());
    }

    public List<WebElement> getActivityTableRows(Date openDate, Date dueDate, Priority priority, ActivityStatus status, String subject) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (openDate != null) {
            columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", openDate));
        }
        if (dueDate != null) {
            columnRowKeyValuePairs.put("Due", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
        }
        if (priority != null) {
            columnRowKeyValuePairs.put("Priority", priority.getValue());
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (subject != null) {
            columnRowKeyValuePairs.put("Subject", subject);
        }
        try {
            return tableUtils.getRowsInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean isActivity(String activitySubject) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Status", ActivityStatus.Open.getValue());
        columnRowKeyValuePairs.put("Subject", activitySubject);
        return tableUtils.getRowsInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs).size() > 0;
    }

    public boolean verifyActivityTable(Date date, OpenClosed status, String subject) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (date != null) {
            columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (subject != null) {
            columnRowKeyValuePairs.put("Subject", subject);
        }
        return tableUtils.getRowsInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs).size() >= 1;
    }

    public void clickActivityTableSubject(Date date, OpenClosed status, String subject) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        columnRowKeyValuePairs.put("Status", status.getValue());
        columnRowKeyValuePairs.put("Subject", subject);
        tableUtils.clickLinkInTableByRowAndColumnName(table_ActivityResults, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs)), "Subject");
    }

    public String getOldLoanNumber() {
    	String loanNumber;
    	try{
    		loanNumber = label_OldLoanNumber.getText();
    	}catch (Exception e) {
    		loanNumber = null;
		}
        return loanNumber;
    }

    public boolean verifyLoanPayerChanges(Date date, String chargeGroup,Double amount,String newPayer,String newLoanNumber) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if(date!=null) {
            columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
        }
        if(chargeGroup!=null) {
            columnRowKeyValuePairs.put("Charge Group", chargeGroup);
        }
        if(amount!=null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
        }
        if(newPayer!=null) {
            columnRowKeyValuePairs.put("New Payer", newPayer);
        }
        if(newLoanNumber!=null) {
            columnRowKeyValuePairs.put("New Loan Number", newLoanNumber);
        }
        return tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_LoanPayerChanges, columnRowKeyValuePairs))>0;
    }

    public void clickActivityTableSubject(WebElement rowContainingActivity) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_ActivityResults, tableUtils.getRowNumberFromWebElementRow(rowContainingActivity), "Subject");
    }

    public String getActivitySubject() {
        return editbox_ActivitySubject.getAttribute("value");
    }

    public String getActivityDescription() {
        return textarea_ActivityDescription.getText();
    }

    public Priority getActivityPriority() {
        return Priority.valueOf(select_Priority().getText());
    }

    public Date getDueDate() {
        Date dateToReturn = null;
        try {
            dateToReturn = DateUtils.convertStringtoDate(editbox_ActivityDueDate.getAttribute("value"), "MM/dd/yyyy");
        } catch (ParseException e) {
            Assert.fail("Error Parsing String to convert to Date.");
        }
        return dateToReturn;
    }

    public Date getEscalationDate() {
        Date dateToReturn = null;
        try {
            dateToReturn = DateUtils.convertStringtoDate(editbox_ActivityEscalationDate.getAttribute("value"), "MM/dd/yyyy");
        } catch (ParseException e) {
            Assert.fail("Error Parsing String to convert to Date.");
        }
        return dateToReturn;
    }

    public String getAccount() {
        return editbox_ActivityAccount.getAttribute("value");
    }

    public String getPolicyPeriod() {
        return editbox_ActivityPolicyPeriod.getAttribute("value");
    }

    public String getAssignedTo() {
    	try{
    		return label_ActivityAssignedTo.getText();
    	}catch(Exception e){
    		return select_AssignedTo().getText();
    	}
    }
    public ActivityStatus getActivityStatus() {
        return ActivityStatus.valueOf(label_ActivityStatus.getText());
    }

    public void clickWriteoff() {
        button_ActivityWriteOff.click();
    }

    public boolean verifyActivityInfo(Map<String, String> infoToVerifyWithUI) {
        boolean passVerification = true;
        Map<String, String> activityScreenInfo = new HashMap<String, String>();
        if(getActivitySubject()!=null){
            activityScreenInfo.put("Subject", getActivitySubject());
        }
        if(getActivityDescription()!=null){
            activityScreenInfo.put("Description", getActivityDescription());
        }
        if(getActivityPriority()!=null){
            activityScreenInfo.put("Priority", getActivityPriority().getValue());
        }
        if(getDueDate()!=null){
            activityScreenInfo.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", getDueDate()));
        }
        if(getEscalationDate()!=null){
            activityScreenInfo.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", getEscalationDate()));
        }
       if(getAccount()!=null){
            activityScreenInfo.put("Account", getAccount());
        }
       if(getPolicyPeriod()!=null){
            activityScreenInfo.put("Policy Period", getPolicyPeriod());
        }
       if(getAssignedTo()!=null){
            activityScreenInfo.put("Assigned To", getAssignedTo());
        }
       if(getActivityStatus()!=null){
            activityScreenInfo.put("Status", getActivityStatus().getValue());
        }
        if(getOldLoanNumber()!=null){
            activityScreenInfo.put("Old Loan Number", getOldLoanNumber());
        }
        for (Object key : infoToVerifyWithUI.keySet()) {
            System.out.println(key + " - " + infoToVerifyWithUI.get(key));
            if (activityScreenInfo.containsKey(key)) {
                System.out.println(" \n" + "The info from UI are: " + key + " - " + activityScreenInfo.get(key));
                if (!activityScreenInfo.get(key).contains(infoToVerifyWithUI.get(key))) {
                    System.out.println(key + " verification failed.");
                    passVerification = false;
                }
            }
        }
        return passVerification;
    }

    public void clickActivityTableSubject(Date openDate, Date dueDate, Priority priority, ActivityStatus status, String subject) {
        WebElement rowContainingActivity = null;
        boolean found = false;
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (openDate != null) {
            columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", openDate));
        }
        if (dueDate != null) {
            columnRowKeyValuePairs.put("Due", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
        }
        if (priority != null) {
            columnRowKeyValuePairs.put("Priority", priority.getValue());
        }
        if (status != null) {
            columnRowKeyValuePairs.put("Status", status.getValue());
        }
        if (subject != null) {
            columnRowKeyValuePairs.put("Subject", subject);
        }
        if (tableUtils.hasMultiplePages(table_ActivityResults)) {
            Integer numPages = tableUtils.getNumberOfTablePages(table_ActivityResults);
            Integer pageCounter = 1;
            while ((pageCounter <= numPages) && (!found)) {
                tableUtils.setTablePageNumber(table_ActivityResults, pageCounter);
                try {
                    rowContainingActivity = tableUtils.getRowInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs);
                    found = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pageCounter++;
            }
        } else {
            rowContainingActivity = tableUtils.getRowInTableByColumnsAndValues(table_ActivityResults, columnRowKeyValuePairs);
            found = true;
        }
        if (found) {
            tableUtils.clickLinkInSpecficRowInTable(table_ActivityResults, tableUtils.getRowNumberFromWebElementRow(rowContainingActivity));
        } else {
            Assert.fail("No Result Was Found in the My Activities Pages When Searching for that Specific Activity");
        }
    }

    public void sortWithOpenedDate() {
        tableUtils.sortByHeaderColumn(table_ActivityResults, "Opened");
        tableUtils.sortByHeaderColumn(table_ActivityResults, "Opened");

    }
    
    public String setAssignedToRandomly(){
    	return select_AssignedTo().selectByVisibleTextRandom();
    }
}
