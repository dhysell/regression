package repository.bc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.Priority;
import repository.gw.enums.Status;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BCDesktopMyQueues extends BasePage {
	
	private WebDriver driver;
    private TableUtils tableUtils;

    public BCDesktopMyQueues(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    public Guidewire8Select select_MyQueuesFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ActivityLVRef:ActivityLV_tb:AvailableQueues-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='DesktopQueues:ActivityLVRef:ActivityLV']")
    public WebElement table_DesktopMyQueues;

    @FindBy(xpath = "//div[@id='DesktopQueues:ActivityLVRef:ActivityLV-body']/div/table[starts-with(@id,'gridview-')]")
    public WebElement table_DesktopMyQueuesResultsBody;

    @FindBy(xpath = "//a[@id='DesktopQueues:ActivityLVRef:ActivityLV_tb:assigneSelectedBtn']")
    public WebElement button_DesktopMyQueuesAssignSelectedToMe;

    @FindBy(xpath = "//a[@id='DesktopQueues:ActivityLVRef:ActivityLV_tb:DesktopActivities_AssignButton']")
    public WebElement button_DesktopMyQueuesAssign;

    @FindBy(xpath = "//a[@id='DesktopQueues:ActivityLVRef:ActivityLV_tb:NextItemButton']")
    public WebElement button_DesktopMyQueuesAssignNextInQueueToMe;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setMyQueuesFilter(ActivityQueuesBillingCenter queueToSelect) {
        select_MyQueuesFilter().selectByVisibleTextPartial(queueToSelect.getValue());
    }

    public void setMyQueuesFilter(int queueToSelect) {
        // TODO Auto-generated method stub

    }

    public void clickAssignSelectedToMe() {
        clickWhenVisible(button_DesktopMyQueuesAssignSelectedToMe);

    }

    public void clickAssign() {
        clickWhenVisible(button_DesktopMyQueuesAssign);

    }

    public void clickAssignNextInQueueToMe() {
        clickWhenVisible(button_DesktopMyQueuesAssignNextInQueueToMe);

    }

    public void clickCheckboxInTableByText(String textInTable) {
        tableUtils.setCheckboxInTableByText(table_DesktopMyQueuesResultsBody, textInTable, true);
    }

    public void clickCheckboxInTableByLinkText(String linktextInTable) {
        tableUtils.setCheckboxInTableByLinkText(table_DesktopMyQueuesResultsBody, linktextInTable, true);
    }

    public void clickLinkInQueueResultTable(String linktextInTable) {
        tableUtils.clickLinkInTable(table_DesktopMyQueuesResultsBody, linktextInTable);
    }

    public boolean cellExistInTableByLinkText(String cellText) {
        return tableUtils.checkIfLinkExistsInTable(table_DesktopMyQueuesResultsBody, cellText);
    }

    public boolean cellExistInTableByText(String cellText) {
        return tableUtils.checkIfLinkExistsInTable(table_DesktopMyQueuesResultsBody, cellText);
    }

    public void clickMyQueuesResultTableTitleToSort(String titleName) {
        tableUtils.sortByHeaderColumn(table_DesktopMyQueues, titleName);
    }


    public void selectAndAssignReviewDelinquencyActivity(ActivityQueuesBillingCenter activityQueue, Date openedDate, String activityName, String accountNumber) {
        setMyQueuesFilter(activityQueue);
        clickMyQueuesResultTableTitleToSort("Opened");
        clickMyQueuesResultTableTitleToSort("Opened");

        String openedGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Opened");
        String subjectGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Subject");
        String accountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Account");

        WebElement reviewDelinquencyRow = table_DesktopMyQueues.findElement(By.xpath(".//tr/td[contains(@class,'" + openedGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", openedDate) + "')]/parent::tr/td[contains(@class,'" + subjectGridColumnID + "') and contains(.,'" + activityName + "')]/parent::tr/td[contains(@class,'" + accountGridColumnID + "') and contains(.,'" + accountNumber + "')]"));
        WebElement reviewDelinquencyCheckbox = reviewDelinquencyRow.findElement(By.xpath(".//preceding-sibling::td[contains(@class, '-cell-checkcolumn')]"));
        reviewDelinquencyCheckbox.click();

        clickAssignSelectedToMe();
    }

    public void sortQueueTableByTitle(String title) {
        tableUtils.sortByHeaderColumn(table_DesktopMyQueues, title);
    }

    public boolean verifyMyQueueTable(Date openedDate, String subject, String account) {
        tableUtils.sortByHeaderColumn(table_DesktopMyQueues, "Opened");
        tableUtils.sortByHeaderColumn(table_DesktopMyQueues, "Opened");
        String dateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Opened");
        String subjectGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Subject");
        String accountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyQueues, "Account");
        String xpathToCheck = ".//tr/td[contains(@class,'" + dateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", openedDate) + "')]/parent::tr/td[contains(@class, '" + subjectGridColumnID + "') and contains(.,'" + subject + "')]/parent::tr/td[contains(@class, '" + accountGridColumnID + "') and contains(.,'" + account + "')]";
        List<WebElement> activitiesRows = table_DesktopMyQueues.findElements(By.xpath(xpathToCheck));
        return activitiesRows.size() >= 1;

    }
    public int getNumberPages(){
		 int numberPages =1;
		 try{
			 numberPages = tableUtils.getNumberOfTablePages(table_DesktopMyQueues);
		 }catch (Exception e) {
			// TODO: handle exception
		}
		 return numberPages;
	 }
    
    public void goToNextPage(){
		 try{
			 table_DesktopMyQueues.findElement(By.xpath(".//span[contains(@class, '-page-next')]")).click();
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
    public WebElement getMyQueueTableRow(Date openedDate, Date dueDate, Priority priority, Status status, String subject, String troubleTicketNumber, String account, String policyPeriod) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        
        if (openedDate != null) {
            columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", openedDate));
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
        if (troubleTicketNumber != null) {
            columnRowKeyValuePairs.put("Trouble Ticket #", troubleTicketNumber);
        }
       
        if (account != null) {
            columnRowKeyValuePairs.put("Account", account);
        }
        
        if (policyPeriod != null) {
            columnRowKeyValuePairs.put("Policy Period", policyPeriod);
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_DesktopMyQueues, columnRowKeyValuePairs);
    }
    
    public void setCheckBox (Date openedDate, Date dueDate, Priority priority, Status status, String subject, String troubleTicketNumber, String account, String policyPeriod, boolean setCheckboxTrueFalse) {
		WebElement theRow = getMyQueueTableRow(openedDate, dueDate, priority, status, subject, troubleTicketNumber, account, policyPeriod);
		tableUtils.setCheckboxInTable(table_DesktopMyQueues, tableUtils.getRowNumberFromWebElementRow(theRow), setCheckboxTrueFalse);		
}	

}
