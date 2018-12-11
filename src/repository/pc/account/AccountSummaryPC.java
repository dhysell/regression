package repository.pc.account;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountSummaryPC extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public AccountSummaryPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id,'AccountFile_SummaryScreen:EditAccount-btnEl')]")
    public WebElement button_EditAccount;

    @FindBy(xpath = "//span[contains(@id, 'AccountFile_SummaryScreen:EditCommissionHolds-btnEl')]")
    public WebElement button_EditCommissionHolds;

    @FindBy(xpath = "//span[contains(@id, ':CommissionHoldsInputSet:AccountCHsLV_tb:Process-btnInnerEl')]")
    public WebElement button_EditCommissionOK;

    public Guidewire8Select select_WorkOrdersinProgress() {
        return new Guidewire8Select(driver, "//select[contains(@id, 'WorkOrdersCompletenessFilter')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_ActivitiesLV')]")
    public WebElement table_CurrentActivities;

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_ActivitiesLV-body')]/div/table")
    public WebElement table_CurrentActivitiesBody;

    public Guidewire8Select select_CurrentActivitiesPaging() {
        return new Guidewire8Select(driver, "//select[contains(@id, 'AccountFile_Summary_ActivitiesLV:_ListPaging')]");
    }

    public WebElement link_CurrentActivitiesSubject(String activitySubject) {
        String xPath = "//div[contains(@id, ':AccountFile_Summary_ActivitiesLV-body') or contains(@id, ':DesktopActivitiesScreen:DesktopActivitiesLV-body')]//a[contains(text(), '" + activitySubject + "')]";
        if(finds(By.xpath(xPath)).isEmpty()) {
        	Assert.fail("ACTIVTY WITH SUBJECT OF " + activitySubject + " WAS NOT FOUND TO CLICK......SORRY, PLEASE TRY AGAIN TOMORROW");
        }
        WebElement link_ActivitySubject = find(By.xpath(xPath));
        return link_ActivitySubject;
    }

    public WebElement link_CurrentActivitiesSubjectUWFullName(String activitySubject, String uwFullName) {
        WebElement link_ActivitySubject;
        try {
            String xPath = "//td[contains(., '" + uwFullName + "')]/preceding-sibling::td//a[contains(text(), '" + activitySubject + "')]";
            link_ActivitySubject = find(By.xpath(xPath));
        } catch (Exception e) {
            String xPath = "//td[contains(., ' ')]/preceding-sibling::td//a[contains(text(), '" + activitySubject + "')]";
            link_ActivitySubject = find(By.xpath(xPath));
        }
        return link_ActivitySubject;
    }

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Summary_PolicyTermsLV')]")
    public WebElement table_PolicyTerms;

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_WorkOrdersLV')]")
    public WebElement table_PolicyTransactions;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Summary_PolicyTermsLV')]/div/table")
    public WebElement table_PolicyNumbersTableBody;

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_WorkOrdersLV-body')]/div/table")
    public WebElement table_WorkOrdersTableBody;

    public Guidewire8Select select_PolicyNumbersPaging() {
        return new Guidewire8Select(driver, "//select[contains(@id, 'AccountFile_Summary_PolicyTermsLV:_ListPaging')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_BasicInfoDV:OrgType')]")
    public WebElement textbox_OrganizationType;

    public WebElement link_PolicyNumberSpecific(String policyNumber) {
        WebElement link_PolicyNumberSpecific = table_PolicyNumbersTableBody.findElement(By
                .xpath("//a[contains(@id, 'PolicyTermsLV:0:PolicyNumber') and contains (., '" + policyNumber + "')]"));
        return link_PolicyNumberSpecific;
    }

    @FindBy(xpath = "//div[contains(@id, ':OfficialIDInputSet:OfficialIDDV_SSN')]")
    public WebElement textbox_SSN;

    @FindBy(xpath = "//div[text() = 'Agent']/parent::td/preceding-sibling::td[1]/div")
    public WebElement textbox_AgentName;

    @FindBy(xpath = "//div[text() = 'Agent']/parent::td/preceding-sibling::td[2]/div")
    public WebElement textbox_AgentNumber;

    @FindBy(xpath = "//div[text() = 'Agency Manager']/parent::td/preceding-sibling::td[1]/div")
    public WebElement textbox_AgencyManager;

    @FindBy(xpath = "//div[text() = 'Agency Manager']/parent::td/preceding-sibling::td[2]/div")
    public WebElement textbox_AgencyManagerNumber;

    @FindBy(xpath = "//div[contains(@id, 'AccountFile_Summary_BasicInfoDV:Name')]")
    public WebElement textbox_AccountName;

    @FindBy(xpath = "//div[contains(@id, ':AccountNumber-inputEl')]")
    public WebElement textbox_AccountNumber;

    @FindBy(xpath = "//div[contains(@id, ':CommissionHoldsInputSet:AccountCHsLV-body')]/div/table/child::tbody/tr/child::td[2]/div")
    public WebElement textbox_AccountPolicyNumber;

    @FindBy(xpath = "//div[contains(@id, ':CommissionHoldsInputSet:AccountCHsLV-body')]/div/table/child::tbody/tr/child::td[3]/div")
    public WebElement textbox_Hold;

    @FindBy(xpath = "//div[contains(@id, ':CommissionHoldsInputSet:AccountCHsLV-body')]/div/table/child::tbody/tr/child::td[1]/div")
    public WebElement checkbox_selectHold;

    @FindBy(xpath = "//span[contains(@id, ':AccountFileInfoBar:CommissionHold-btnInnerEl')]/img")
    public WebElement img_HoldStatus;

    @FindBy(xpath = "//div[contains(@id, 'GlobalAddressInputSet:AddressSummary')]")
    public WebElement textbox_AccountAddress;

    @FindBy(xpath = "//div[contains(@id, ':AddressShortInputSet:AddressType-inputEl')]")
    public WebElement textbox_AccountAddressType;

    @FindBy(xpath = "//span[contains(@id, ':EditAccountScreen:Cancel-btnEl') or contains(@id, ':AccountCHsLV_tb:Cancel-btnInnerEl')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//div[contains(@id, ':AccountFile_Summary_PolicyTermsLV')]")
    public WebElement table_AccountPolicyTerms;

    /*
     * @FindBy(xpath=
     * "//a[@id='AccountFile_Summary:AccountFile_SummaryScreen:AccountFile_Summary_PolicyTermsLV:0:PolicyNumber']")
     * public WebElement link_PolicyNumber(String policyNumber);
     */
    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickCurrentActivitiesSubjectUWFullName(String activitySubject, String uwFullName) {
        clickWhenClickable(link_CurrentActivitiesSubjectUWFullName(activitySubject, uwFullName));
    }


    public void clickCurrentActivitiesSubject(String activitySubject) {
        tableUtils.clickLinkInTable(table_CurrentActivities, activitySubject);
    }

    public int getNumberOfActivities() {
        return tableUtils.getRowCount(table_CurrentActivities);
    }


    public void clickPolicyNumber(String policyNumber) {
        // waitUntilElementIsClickable(link_PolicyNumber);
        link_PolicyNumberSpecific(policyNumber).click();
        // link_PolicyNumberSpecific(policyNumber).click();
        waitForPostBack();
    }


    public void clickActivitySubject(String activitySubject) {
        clickWhenClickable(link_CurrentActivitiesSubject(activitySubject));
    }


    public void clickEditAccount() {
        waitUntilElementIsClickable(button_EditAccount);
        button_EditAccount.click();
    }


    public void clickEditCommissionHolds() {
        waitUntilElementIsClickable(button_EditCommissionHolds);
        button_EditCommissionHolds.click();
    }


    public boolean checkHoldImageStatus() {
        boolean holdStatus = false;
        try {
            if (img_HoldStatus.isDisplayed())
                holdStatus = true;
            return holdStatus;
        } catch (Exception e) {
        }

        return holdStatus;
    }


    public String selectPolicyHolds() {
        waitUntilElementIsClickable(checkbox_selectHold);
        checkbox_selectHold.click();

        waitUntilElementIsClickable(button_EditCommissionOK);
        button_EditCommissionOK.click();

        return textbox_Hold.getText();
    }


    public void selectWorkOrdersInProgress(String allOpenComplete) {
        select_WorkOrdersinProgress().selectByVisibleText(allOpenComplete);
    }


    public void clickCancel() {
        waitUntilElementIsClickable(button_Cancel);
        button_Cancel.click();
    }

    //TODO These clickWorkorderMethods need to be re-written to use tableUtils.

    public void clickWorkOrderbyJobType(TransactionType jobType) {
        List<WebElement> workorders = table_WorkOrdersTableBody.findElements(By.xpath(".//tbody/child::tr/child::td/div[contains(., '" + jobType.getValue() + "')]/ancestor::tr/td/div/a[contains(@id, ':WorkOrderNumber')]"));
        if (!workorders.isEmpty()) {
            clickWhenClickable(workorders.get(0));
        } else {
            Assert.fail("NO WORK ORDER WAS FOUND TO CLICK");
        }
        waitForPostBack();
    }


    public void clickWorkOrderbySuffix(String suffix) {
        List<WebElement> workorders = table_WorkOrdersTableBody.findElements(By.xpath(".//tbody/child::tr/child::td/div/a[contains(@id, ':WorkOrderNumber') and (contains(text(), '" + suffix + "'))]"));
        if (!workorders.isEmpty()) {
            clickWhenClickable(workorders.get(0));
        } else {
            Assert.fail("NO WORK ORDER WAS FOUND TO CLICK");
        }
    }


    public void clickWorkOrderbyProduct(ProductLineType product) {
        List<WebElement> workorders = table_WorkOrdersTableBody.findElements(By.xpath(".//tbody/child::tr/child::td/div[contains(., '" + product.getValue() + "')]/parent::td/parent::tr/td/div/a[contains(@id, ':WorkOrderNumber')]"));
        if (!workorders.isEmpty()) {
            clickWhenClickable(workorders.get(0));
        } else {
            Assert.fail("NO WORK ORDER WAS FOUND TO CLICK");
        }
    }


    public String getSSN() {
        return textbox_SSN.getText();
    }


    public String getOrgType() {
        return textbox_OrganizationType.getText();
    }

    // CHANGE LATER TO RETURN AN ARRAY WITH ALL THE AGENT INFO

    public String getAgentInfo() {
        return textbox_AgentName.getText();
    }


    public String getAgentName() {
        return textbox_AgentName.getText();
    }


    public String getAgentNumber() {
        return textbox_AgentNumber.getText();
    }


    public String getAgencyManagerName() {
        return textbox_AgencyManager.getText();
    }


    public String getAgencyManagerNumber() {
        return textbox_AgencyManagerNumber.getText();
    }


    public String getAccountName() {
        return textbox_AccountName.getText();
    }


    public String getAccountNumber() {
        return textbox_AccountNumber.getText();
    }


    public String getPolicyNumber() {
        return textbox_AccountPolicyNumber.getText();
    }


    public String getPolicyHolds() {
        return textbox_Hold.getText();
    }


    public String getAddress() {
        return textbox_AccountAddress.getText();
    }


    public String getAddressType() {
        return textbox_AccountAddressType.getText();
    }


    public boolean isPolicyInForce(String policyNumber) {
        boolean isInForce = true;
        WebElement test = null;
        try {
            
            if (test == find(By.xpath("//tbody/tr/td/div/a[contains(., '" + policyNumber + "')] | //tbody/tr/td/div[contains(., 'In Force')]"))) {
                isInForce = true;
            }
        } catch (Exception e) {
            isInForce = false;
            e.printStackTrace();
        }
        return isInForce;
    }

    public boolean isPolicySubmitted(String policyNumber) {
        boolean isInForce = true;
        WebElement test = null;
        try {
            
            if (test == find(By.xpath("//tbody/tr/td/div/a[contains(., '" + policyNumber + "')] | //tbody/tr/td/div[contains(., 'Submitted')]"))) {
                isInForce = true;
            }
        } catch (Exception e) {
            isInForce = false;
            e.printStackTrace();
        }
        return isInForce;
    }


    public Agents getAssignedTo(String subject) {
        List<WebElement> currentActivities = table_CurrentActivitiesBody
                .findElements(By.xpath(".//child::tr/child::td/div/a[contains(text(), '" + subject
                        + "')]/parent::div/parent::td/following-sibling::td[1]/div"));
        if (currentActivities.size() > 0) {
            String[] split = currentActivities.get(0).getText().split(" ");
            try {
                return AgentsHelper.getAgentByName(split[0], split[split.length - 1]);
            } catch (Exception e) {
                return new Agents(null, split[0], split[1], split[0].substring(0, 1) + split[split.length - 1], "gw",
                        null, null, null);
            }
        }
        return null;
    }


    public Underwriters getAssignedToUW(String subject) {
        
        waitUntilElementIsVisible(table_CurrentActivitiesBody);
        List<WebElement> currentActivities = table_CurrentActivitiesBody.findElements(By.xpath(".//child::tr/child::td/div/a[contains(text(), '" + subject + "')]/parent::div/parent::td/following-sibling::td[1]/div"));
        //jlarsen
        //tempish fix
        if (currentActivities.isEmpty()) {
            currentActivities = table_CurrentActivitiesBody.findElements(By.xpath(".//child::tr/child::td/div/a[contains(text(), 'Bound Full Application')]/parent::div/parent::td/following-sibling::td[1]/div"));
        }
        if (currentActivities.size() > 0) {
            String[] split = currentActivities.get(0).getText().split(" ");
            try {
                return UnderwritersHelper.getUnderwriterInfoByFirstLastName(split[0], split[split.length - 1]);
            } catch (Exception e) {
                Assert.fail(split[0] + " " + split[split.length - 1] + " DOES NOT EXIST IN UW DATABASE TABLE");
            }

        }
        systemOut("Unable to find Activity with subject of: " + subject);
        systemOut("RETURNING NULL UNDERWRITER");
        return null;
    }

    public Boolean checkUWExists(String uwFirstName, String uwLastName) {
        try {
            UnderwritersHelper.getUnderwriterInfoByFirstLastName(uwFirstName, uwLastName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getStringAssignedTo(String subject) {
        List<WebElement> currentActivities = table_CurrentActivitiesBody
                .findElements(By.xpath(".//child::tr/child::td/div/a[contains(text(), '" + subject
                        + "')]/parent::div/parent::td/following-sibling::td[1]/div"));
        if (currentActivities.size() > 0) {
            return currentActivities.get(0).getText();
        }
        return null;
    }


    public boolean verifyCancellationInPolicyCenter(String policyNumber, int secondsToWait) {
        
        boolean found = false;
        String statusGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Status");
        String policyGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Policy #");
        String typeGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Type");

        List<WebElement> policyTransactionRows = table_PolicyTransactions.findElements(By.xpath(".//tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'Canceling')]/parent::tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + typeGridColumnID + "') and contains(.,'Cancellation')]"));
        if (policyTransactionRows.size() < 1) {
            policyTransactionRows = table_PolicyTransactions.findElements(By.xpath(".//tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'Canceling')]/parent::tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + typeGridColumnID + "') and contains(.,'Canceling')]"));
        }
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (policyTransactionRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the cancellation to be available in PolicyCenter again.
                secondsRemaining = secondsRemaining - delayInterval;
                repository.pc.account.AccountsSideMenuPC sideMenu = new repository.pc.account.AccountsSideMenuPC(driver);
                sideMenu.clickContacts();
                
                sideMenu.clickSummary();
                

                policyGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Policy #");
                statusGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Status");
                typeGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTransactions, "Type");
                policyTransactionRows = table_PolicyTransactions.findElements(By.xpath(".//tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'Canceling')]/parent::tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + typeGridColumnID + "') and contains(.,'Cancellation')]"));
                if (policyTransactionRows.size() < 1) {
                    policyTransactionRows = table_PolicyTransactions.findElements(By.xpath(".//tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'Canceling')]/parent::tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + typeGridColumnID + "') and contains(.,'Canceling')]"));
                }
            }
        }
        return found;
    }


    public boolean verifyPolicyStatusInPolicyCenter(String policyNumber, PolicyTermStatus status, int secondsToWait) {
        boolean found = false;
        String policyGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTerms, "Policy #");
        String statusGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTerms, "Status");

        List<WebElement> policyTermsRows = table_PolicyTerms.findElements(By.xpath(".//tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'" + status.getValue() + "')]"));
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (policyTermsRows.size() > 0) {
                found = true;
            } else {
                found = false;
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the policy status again.
                secondsRemaining = secondsRemaining - delayInterval;
                repository.pc.account.AccountsSideMenuPC sideMenu = new repository.pc.account.AccountsSideMenuPC(driver);
                sideMenu.clickContacts();
                sideMenu.clickSummary();

                policyGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTerms, "Policy #");
                statusGridColumnID = tableUtils.getGridColumnFromTable(table_PolicyTerms, "Status");
                policyTermsRows = table_PolicyTerms.findElements(By.xpath(".//tr/td[contains(@class,'" + policyGridColumnID + "') and contains(.,'" + policyNumber + "')]/parent::tr/td[contains(@class,'" + statusGridColumnID + "') and contains(.,'" + status + "')]"));
            }
        }

        return found;
    }


    public boolean verifyCurrentActivity(String activity, int secondsToWait) {
        boolean found = false;
        String subjectGridColumnID = tableUtils.getGridColumnFromTable(table_CurrentActivities, "Subject");

        List<WebElement> activityRows = table_CurrentActivities.findElements(By.xpath(".//tr/td[contains(@class,'" + subjectGridColumnID + "') and contains(.,'" + activity + "')]"));
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (activityRows.size() > 0) {
                found = true;
            } else {
                found = false;
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the current activity again.
                secondsRemaining = secondsRemaining - delayInterval;
                repository.pc.account.AccountsSideMenuPC sideMenu = new AccountsSideMenuPC(driver);
                sideMenu.clickContacts();
                
                sideMenu.clickSummary();
                

                subjectGridColumnID = tableUtils.getGridColumnFromTable(table_CurrentActivities, "Subject");
                activityRows = table_CurrentActivities.findElements(By.xpath(".//tr/td[contains(@class,'" + subjectGridColumnID + "') and contains(.,'" + activity + "')]"));
            }
        }

        return found;
    }


    public String getActivityAssignedTo(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CurrentActivities, row, "Assigned To");
    }


    public ArrayList<String> getActivityAssignedTo(String subject) {
        List<WebElement> myRows = tableUtils.getRowsInTableByColumnNameAndValue(table_CurrentActivities, "Subject", subject);
        ArrayList<String> toReturn = new ArrayList<String>();
        for (WebElement row : myRows) {
            String toAdd = tableUtils.getCellTextInTableByRowAndColumnName(table_CurrentActivities, tableUtils.getRowNumberFromWebElementRow(row), "Assigned To");
            toReturn.add(toAdd);
        }
        return toReturn;
    }


    public String getActivitySubject(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CurrentActivities, row, "Subject");
    }


    public Date getActivityDueDate(String subject) {
        int row = tableUtils.getRowNumberInTableByText(table_CurrentActivities, subject);
        try {
            return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_CurrentActivities, row, "Due Date"), "MM/dd/yyyy");
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getPolicyTermExpirationDateByStatus(PolicyTermStatus status) {
        int row = tableUtils.getRowNumberInTableByText(table_AccountPolicyTerms, status.getValue());
        try {
            return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_AccountPolicyTerms, row, "Expiration Date"), "MM/dd/yyyy");
        } catch (ParseException e) {
            return null;
        }

    }



    public void clickAccountSummaryPolicyTermByStatus(PolicyTermStatus status) {
        int row = tableUtils.getRowNumberInTableByText(table_AccountPolicyTerms, status.getValue());
        tableUtils.clickLinkInSpecficRowInTable(table_AccountPolicyTerms, row);
        
    }
    
    public void clickPolicyTerm_ByProduct(ProductLineType product) {
    	int row = tableUtils.getRowNumberInTableByText(table_AccountPolicyTerms, product.getValue());
    	tableUtils.clickLinkInSpecficRowInTable(table_AccountPolicyTerms, row);
    }


    public void clickAccountSummaryPolicyTermByLatestStatus() {
        int row = tableUtils.getRowCount(table_AccountPolicyTerms);
        tableUtils.clickLinkInSpecficRowInTable(table_AccountPolicyTerms, row);
        
    }


    public ArrayList<String> getActivitySubjectFromTable() {

        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(table_CurrentActivities, "Subject");

        if (tableUtils.hasMultiplePages(table_CurrentActivities)) {
            tableUtils.clickNextPageButton(table_CurrentActivities);
            int numPages = tableUtils.getNumberOfTablePages(table_CurrentActivities);
            for (int page = 2; page <= numPages; page++) {
                toReturn.addAll(tableUtils.getAllCellTextFromSpecificColumn(table_CurrentActivities, "Subject"));
                if (page < numPages) {
                	tableUtils.clickNextPageButton(table_CurrentActivities);
                }
            }
        WebElement previousButton = table_CurrentActivities.findElement(By.xpath(".//div[contains(@id, 'gpaging')]/a[@data-qtip='Previous Page']"));
        clickWhenClickable(previousButton);
        }

        return toReturn;
    }


    public void clickAccountSummaryPendingTransactionByStatus(String status) {
        int row = tableUtils.getRowNumberInTableByText(table_PolicyTransactions, status);
        tableUtils.clickLinkInSpecficRowInTable(table_PolicyTransactions, row);
        
    }


    public void clickAccountSummaryPendingTransactionByProduct(ProductLineType product) {
        int row = tableUtils.getRowNumberInTableByText(table_PolicyTransactions, product.getValue());
        tableUtils.clickLinkInSpecficRowInTable(table_PolicyTransactions, row);
    }
    
    public String getAccountSummaryPendingTransactionDetailByText(String text, String columnName) {
    	waitUntilElementIsVisible(table_PolicyTransactions, 1);
        int row = tableUtils.getRowNumberInTableByText(table_PolicyTransactions, text);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicyTransactions, row, columnName);
    }
}
