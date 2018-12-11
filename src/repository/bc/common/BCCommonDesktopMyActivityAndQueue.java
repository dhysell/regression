package repository.bc.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityFilter;
import repository.gw.enums.Priority;
import repository.gw.enums.Status;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class BCCommonDesktopMyActivityAndQueue extends BasePage{
	private WebDriver driver;
    private TableUtils tableUtils;

    public BCCommonDesktopMyActivityAndQueue(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
   
    @FindBy(xpath = "//div[@id='DesktopQueues:ActivityLVRef:ActivityLV' or @id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV']")
    public WebElement table_DesktopMyActivityMyQueues;
    
    @FindBy(xpath = "//a[contains(@id, 'Buttons_CompleteButton')]")
    public WebElement button_DesktopMyActivitiesComplete;
    
    public Guidewire8Select select_MyActivitiesStatusFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DesktopActivitiesLV:DesktopActivitiesFilter-triggerWrap')]");
    }
    
    public void setMyActivitiesStatusFilter(ActivityFilter statusToSelect) {
        try {
            select_MyActivitiesStatusFilter().selectByVisibleText(statusToSelect.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public int getNumberPages(){
		 int numberPages =1;
		 try{
			 numberPages = tableUtils.getNumberOfTablePages(table_DesktopMyActivityMyQueues);
		 }catch (Exception e) {
			// TODO: handle exception
		}
		 return numberPages;
	 }
   
   public void goToNextPage(){
		 try{
			 table_DesktopMyActivityMyQueues.findElement(By.xpath(".//span[contains(@class, '-page-next')]")).click();
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
   public WebElement getMyActivityOrMyQueueTableRow(Date openedDate, Date dueDate, Priority priority, Status status, String subject, String troubleTicketNumber, String account, String policyPeriod) {
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
       return tableUtils.getRowInTableByColumnsAndValues(table_DesktopMyActivityMyQueues, columnRowKeyValuePairs);
   }
   
   public void setCheckBox (Date openedDate, Date dueDate, Priority priority, Status status, String subject, String troubleTicketNumber, String account, String policyPeriod, boolean setCheckboxTrueFalse) {
		WebElement theRow =getMyActivityOrMyQueueTableRow(openedDate, dueDate, priority, status, subject, troubleTicketNumber, account, policyPeriod);
		tableUtils.setCheckboxInTable(table_DesktopMyActivityMyQueues, tableUtils.getRowNumberFromWebElementRow(theRow), setCheckboxTrueFalse);		
   }
   public void clickLinkInActivityOrQueueTable(String linkText, Date openedDate, Date dueDate, Priority priority, Status status, String subject, String troubleTicketNumber, String account, String policyPeriod){
	   WebElement tableRow = getMyActivityOrMyQueueTableRow(openedDate, dueDate, priority, status, subject, troubleTicketNumber, account, policyPeriod);
       tableUtils.clickLinkInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, tableUtils.getRowNumberFromWebElementRow(tableRow), linkText);
   }
   
   public void clickActivityCompleteButton(){
	   try{
		   super.clickCompleteWithoutPopup();
	   }catch(Exception e){
		   
	   }
	   try{
		   clickWhenVisible(button_DesktopMyActivitiesComplete);
	   }catch(Exception e){
		   
	   }
   }
   
   public String getSubject(int rowNumber){
	   return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, rowNumber, "Subject");
   }
   
   public String getAccountNumber(int rowNumber){
	   return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, rowNumber, "Account");
   }
   public String getPolicyPeriod(int rowNumber){
	   return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, rowNumber, "Policy Period");
   }
   
   public Date getOpenDate(int rowNumber) throws ParseException{
	   return DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, rowNumber, "Opened"), "MM/dd/yyyy");
   }
   
   public void clickAccountNumber(int rowNumber){
	   tableUtils.clickLinkInTableByRowAndColumnName(table_DesktopMyActivityMyQueues, rowNumber, "Account");
   }
   public void sortTableByTile(String headerColumnName){
	   tableUtils.sortByHeaderColumn(table_DesktopMyActivityMyQueues, headerColumnName);
   }
   
   public int getTableRowCount(){
	   return tableUtils.getRowCount(table_DesktopMyActivityMyQueues);
   }
   
}
