package repository.bc.common;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;

import java.util.Date;
import java.util.List;

public class BCCommonTroubleTickets extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCCommonTroubleTickets(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Trouble Ticket Page
    // -----------------------------------------------

    @FindBy(xpath = "//div[contains(@id,'AccountDetailTroubleTickets:AccountDetailTroubleTicketsScreen:TroubleTicketsLV') or contains(@id,':PolicyDetailTroubleTicketsScreen:TroubleTicketsLV')]")
    public WebElement table_GenericTroubleTicket;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketsScreen:NewTroubleTicket')]")
    public WebElement button_TroubleTicketNew;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketDetailsPopup_CloseButton')]")
    public WebElement button_TroubleTicketClose;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketDetailsPopup_EditDetailsButton')]")
    public WebElement button_TroubleTicketEditDetails;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketTabbedDetailDV:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:relatedEntities')]")
    public WebElement button_TroubleTicketEditRelatedEntities;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketRelatedEntitiesPopup:TroubleTicketsRelatedEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddAccountsButton')]")
    public WebElement button_TroubleTicketEditRelatedEntitiesAddAccounts;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketRelatedEntitiesPopup:TroubleTicketsRelatedEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddPoliciesButton')]")
    public WebElement button_TroubleTicketEditRelatedEntitiesAddPolicies;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketRelatedEntitiesPopup:TroubleTicketsRelatedEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:TroubleTicketRelatedEntitiesDV_AddPolicyPeriodsButton')]")
    public WebElement button_TroubleTicketEditRelatedEntitiesAddPolicyPeriods;

    @FindBy(xpath = "//a[contains(@id,'TroubleTicketRelatedEntitiesPopup:TroubleTicketsRelatedEntitiesScreen:TroubleTicketRelatedEntitiesDV:TroubleTicketRelatedEntitiesLV_tb:Remove')]")
    public WebElement button_TroubleTicketEditRelatedEntitiesRemove;

    public Guidewire8Select comboBox_TicketStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DetailTroubleTicketsScreen:TroubleTicketsLV:Filter-triggerWrap')]");
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements - Trouble Tickets Page
    // -------------------------------------------------------

    public void clickNewTroubleTicketCancel() {
        super.clickCancel();
        
    }

    public void clickNewTroubleTicketNext() {
        super.clickNext();
        
    }

    public void clickNewTroubleTicketFinish() {
        super.clickFinish();
        
    }

    public void clickNewTroubleTicketUpdate() {
        super.clickUpdate();
        
    }

    public void setTroubleTicketFilter(TroubleTicketFilter filter) {
        comboBox_TicketStatus().selectByVisibleText(filter.getValue());
        
    }

    public void clickNewButton() {
        clickWhenVisible(button_TroubleTicketNew);
        
    }

    public void clickTroubleTicketCloseButton() {
        clickWhenVisible(button_TroubleTicketClose);
        
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }

    public void clickTroubleTicketEditDetailsButton() {
        clickWhenVisible(button_TroubleTicketEditDetails);
        
    }

    public void clickTroubleTicketEditRelatedEntitiesButton() {
        clickWhenClickable(button_TroubleTicketEditRelatedEntities);
        
    }

    public void clickTroubleTicketEditRelatedEntitiesAddAccountsButton() {
        clickWhenClickable(button_TroubleTicketEditRelatedEntitiesAddAccounts);
        
    }

    public void clickTroubleTicketEditRelatedEntitiesAddPoliciesButton() {
        clickWhenClickable(button_TroubleTicketEditRelatedEntitiesAddPolicies);
        
    }

    public void clickTroubleTicketEditRelatedEntitiesAddPolicyPeriodsButton() {
        clickWhenClickable(button_TroubleTicketEditRelatedEntitiesAddPolicyPeriods);
        
    }

    public void clickTroubleTicketEditRelatedEntitiesRemoveButton() {
        clickWhenClickable(button_TroubleTicketEditRelatedEntitiesRemove);
        
    }

    public WebElement getTroubleTicketsTable() {
        waitUntilElementIsVisible(table_GenericTroubleTicket);
        return table_GenericTroubleTicket;
    }

    public void clickTroubleTicketNumberInTable(int rowNumber) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_GenericTroubleTicket, rowNumber, "Ticket #");
        
    }

    public void clickTroubleTicketNumberBySubjectOrType(TroubleTicketType troubleTicketType) {
        tableUtils.clickLinkInTableByRowAndColumnName(table_GenericTroubleTicket, tableUtils.getRowNumberInTableByText(table_GenericTroubleTicket, troubleTicketType.getValue()), "Ticket #");
        
    }

    public String getTroubleTicketNumberInTable(int rowNumber) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_GenericTroubleTicket, rowNumber, "Ticket #");
    }

    public boolean waitForTroubleTicketsToArrive(int secondsToWait) {
		repository.bc.policy.BCPolicyMenu policyMenu = new repository.bc.policy.BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
		String gridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Subject");
        String xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'Promise Payment'))]";
        List<WebElement> issuanceRows = table_GenericTroubleTicket.findElements(By.xpath(xpathToCheck));

        boolean found = false;
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (issuanceRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for trouble tickets again.
                secondsRemaining = secondsRemaining - delayInterval;
                InfoBar infoBar = new InfoBar(getDriver());
                if (infoBar.infoBarPolicyNumberExists()) {
                    policyMenu.clickBCMenuSummary();
                    policyMenu.clickBCMenuTroubleTickets();
                } else {
                    repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver()); // Account Level Trouble Tickets
                    accountMenu.clickBCMenuSummary();
                    accountMenu.clickBCMenuTroubleTickets();
                }

                gridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Subject");
                xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'Promise Payment'))]";
                issuanceRows = table_GenericTroubleTicket.findElements(By.xpath(xpathToCheck));
            }
        }

        return found;
    }

    public boolean waitForTroubleTicketsToArrive(int secondsToWait, TroubleTicketType troubleTicketSubject) {
        String gridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Subject");
        String xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'" + troubleTicketSubject.getValue() + "'))]";
        List<WebElement> issuanceRows = table_GenericTroubleTicket.findElements(By.xpath(xpathToCheck));

        boolean found = false;
        int secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while ((found == false) && (secondsRemaining > 0)) {
            if (issuanceRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for trouble tickets again.
                secondsRemaining = secondsRemaining - delayInterval;
                InfoBar infoBar = new InfoBar(getDriver());
                if (infoBar.infoBarPolicyNumberExists()) {
                    repository.bc.policy.BCPolicyMenu policyMenu = new repository.bc.policy.BCPolicyMenu(getDriver()); // Policy Level Trouble Tickets
                    policyMenu.clickBCMenuSummary();
                    policyMenu.clickBCMenuTroubleTickets();
                } else {
                    repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver()); // Account Level Trouble Tickets
                    accountMenu.clickBCMenuSummary();
                    accountMenu.clickBCMenuTroubleTickets();
                }

                gridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Subject");
                xpathToCheck = ".//tr/td[(contains(@class,'" + gridColumnID + "'))]/div[(contains(.,'" + troubleTicketSubject.getValue() + "'))]";
                issuanceRows = table_GenericTroubleTicket.findElements(By.xpath(xpathToCheck));
            }
        }
        return found;
    }

    public void closeTroubleTicket(TroubleTicketType troubleTicketType) {
        InfoBar infoBar = new InfoBar(getDriver());
        
        if (infoBar.infoBarPolicyNumberExists()) {
            repository.bc.policy.BCPolicyMenu policyMenu = new repository.bc.policy.BCPolicyMenu(getDriver()); // Policy Level Trouble Tickets
            
            policyMenu.clickBCMenuTroubleTickets();
        } else {
            repository.bc.account.BCAccountMenu accountMenu = new repository.bc.account.BCAccountMenu(getDriver()); // Account Level Trouble Tickets
            accountMenu.clickBCMenuTroubleTickets();
        }

        boolean found = waitForTroubleTicketsToArrive(120, troubleTicketType);
        if (found) {
            
            clickTroubleTicketNumberInTable(1);

            clickTroubleTicketCloseButton();
        } else {
            Assert.fail("The Trouble Ticket did not show up in Billing Center after 2 minutes of waiting.");
        }
    }

    public void closeFirstTroubleTicket() {
        InfoBar infoBar = new InfoBar(getDriver());
        
        if (infoBar.infoBarPolicyNumberExists()) {
            repository.bc.policy.BCPolicyMenu policyMenu = new BCPolicyMenu(getDriver()); // Policy Level Trouble Tickets
            
            policyMenu.clickBCMenuTroubleTickets();
        } else {
            repository.bc.account.BCAccountMenu accountMenu = new BCAccountMenu(getDriver()); // Account Level Trouble Tickets
            accountMenu.clickBCMenuTroubleTickets();
        }

        boolean found = waitForTroubleTicketsToArrive(120);
        if (found) {
            
            clickTroubleTicketNumberInTable(1);

            clickTroubleTicketCloseButton();
        } else {
            Assert.fail("The Promise Payment Trouble Ticket did not show up in Billing Center after 2 minutes of waiting.");
        }
    }

    public WebElement getTroubleTicketTableRow(String ticketNumber, Date dueDate, Date openedDate, String assignedTo, Priority priority, Status status, TroubleTicketType troubleTicketSubject) {
        StringBuilder xpathBuilder = new StringBuilder();
        if (!(ticketNumber == null)) {
            String disbursementNumberGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Ticket #");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + disbursementNumberGridColumnID + "') and contains(.,'" + ticketNumber + "')]");
        }
        if (!(dueDate == null)) {
            String dateIssuedGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Due");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + dateIssuedGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate) + "')]");
        }
        if (!(openedDate == null)) {
            String dateRejectedGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Opened");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + dateRejectedGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", openedDate) + "')]");
        }
        if (!(assignedTo == null)) {
            String unappliedFundGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Assigned To");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + unappliedFundGridColumnID + "') and contains(.,'" + assignedTo + "')]");
        }
        if (!(priority == null)) {
            String disbursementStatusGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Priority");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + disbursementStatusGridColumnID + "') and contains(.,'" + priority.getValue() + "')]");
        }
        if (!(status == null)) {
            String trackingStatusGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Status");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + trackingStatusGridColumnID + "') and contains(.,'" + status.getValue() + "')]");
        }
        if (!(troubleTicketSubject == null)) {
            String disbursementAmountGridColumnID = tableUtils.getGridColumnFromTable(table_GenericTroubleTicket, "Subject");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + disbursementAmountGridColumnID + "') and contains(.,'" + troubleTicketSubject.getValue() + "')]");
        }

        xpathBuilder.replace(0, 9, ".//");
        WebElement tableRow = table_GenericTroubleTicket.findElement(By.xpath(xpathBuilder.toString()));
        return tableRow;
    }

    public void addTroubleTicketRelatedEntities(TroubleTicketRelatedEntitiesOptions troubleTicketsRelatedEntitiesOption, String entityNumber) {
        clickTroubleTicketEditRelatedEntitiesButton();
        switch (troubleTicketsRelatedEntitiesOption) {
            case Accounts:
                clickTroubleTicketEditRelatedEntitiesAddAccountsButton();
                
                repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
                accountSearch.setBCSearchAccountsAccountNumber(entityNumber);
                accountSearch.clickSearch();
                accountSearch.setAccountNumberCheckboxInResultsTable(entityNumber);
                
                accountSearch.clickSelectAccountsButton();
                
                clickUpdate();
                break;
            case Policies:
                clickTroubleTicketEditRelatedEntitiesAddPoliciesButton();
                repository.bc.search.BCSearchPolicies policySearch = new BCSearchPolicies(getDriver());
                policySearch.setBCSearchPoliciesPolicyNumber(entityNumber);
                policySearch.clickSearch();
                policySearch.setPolicyNumberCheckboxInResultsTable(entityNumber);
                
                policySearch.clickSelectPoliciesButton();
                
                clickUpdate();
                break;
            case PolicyPeriods:
                // Not Yet Implemented
                break;
        }
    }
}
