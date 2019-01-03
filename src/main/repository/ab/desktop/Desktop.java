package repository.ab.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.generate.custom.AbUserActivity;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Desktop extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public Desktop(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    //Activities

    //Elements on the Page

    @FindBy(xpath = "//div[@id='DesktopActivities:DesktopActivitiesScreen:1']")
    static private WebElement div_DesktopActivities;

    @FindBy(xpath = "//table[contains(@id,'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesFilter-triggerWrap')]")
    static private WebElement table_DesktopActivitiesSelect;

    private Guidewire8Select select_DesktopActivitiesFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesFilter-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id,'DesktopActivities:DesktopActivitiesScreen:DesktopActivities_CompleteButton-btnEl')]")
    static private WebElement button_DesktopActivitiesComplete;

    @FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
    private WebElement input_PageSelector;


    //Methods
    public void selectActivityFilter(AbActivitySearchFilter filter) {
        
        waitUntilElementIsVisible(table_DesktopActivitiesSelect);
        Guidewire8Select activityFilter = select_DesktopActivitiesFilter();
        activityFilter.selectByVisibleText(filter.getValue());
    }

    public void clickComplete() {
        
        clickWhenClickable(button_DesktopActivitiesComplete);
        
    }

    public ArrayList<String> getActivityFilterOptions() {
        
        waitUntilElementIsVisible(table_DesktopActivitiesSelect);
        Guidewire8Select activityFilter = select_DesktopActivitiesFilter();
        return activityFilter.getListItems();
    }

    public AbUserActivity getRandomActivityInfo() {
        int rowCount = tableUtils.getRowCount(div_DesktopActivities);
        int randomNumber = NumberUtils.generateRandomNumberInt(0, rowCount, 0);
        String subject = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Subject");
        String name = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Name");
        String description = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Description");
        String activityDueDate = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Due");
        String priority = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Priority");
        String status = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Status");
        String activityCompletedDate = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Completed");
        String assignedTo = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Assigned To");
        return new AbUserActivity(subject, name, description, activityDueDate, priority, status, activityCompletedDate, assignedTo);
    }

    public AbUserActivity completeRandomActivity() {
        int rowCount = tableUtils.getRowCount(div_DesktopActivities);
        int randomNumber = NumberUtils.generateRandomNumberInt(1, rowCount, 0);
        System.out.println("Your Random number is " + randomNumber);
        String subject = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Subject");
        String name = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Name");
        String description = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Description");
        String activityDueDate = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Due");
        String priority = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Priority");
        String status = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Status");
        String activityCompletedDate = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Completed");
        String assignedTo = tableUtils.getCellTextInTableByRowAndColumnName(div_DesktopActivities, randomNumber, "Assigned To");

        HashMap<String, String> valuePairs = new HashMap<String, String>();
        valuePairs.put("Name", name);
//		valuePairs.put("Description", description);
        valuePairs.put("Subject", subject);
        WebElement row = tableUtils.getRowInTableByColumnsAndValues(div_DesktopActivities, valuePairs);
        System.out.println("The Row WebElement has the text of: " + row.getText());
        tableUtils.setCheckboxInTable(div_DesktopActivities, tableUtils.getRowNumberFromWebElementRow(row), true);
        clickComplete();
        return new AbUserActivity(subject, name, description, activityDueDate, priority, status, activityCompletedDate, assignedTo);
    }


    public boolean activityExistsInTable(AbUserActivity activity) {
        TopMenuAB topMenu = new TopMenuAB(driver);
        topMenu.clickDesktopTab();
        
        HashMap<String, String> activityMap = new HashMap<String, String>();
        activityMap.put("Due", activity.getActivityDueDate());
        activityMap.put("Assigned To", activity.getAssignedTo());
        activityMap.put("Name", activity.getName());
        activityMap.put("Subject", activity.getSubject());
        selectActivityFilter(AbActivitySearchFilter.AllOpen);

        int maxPage = new TableUtils(driver).getNumberOfTablePages(find(By.cssSelector("div[id$=':DesktopActivitiesLV']")));
        if (checkIfElementExists(input_PageSelector, 3)) {
            for (int i = 1; i <= maxPage; i++) {
                setTextHitEnter(input_PageSelector, String.valueOf(i + 1));
                if (tableUtils.verifyRowExistsInTableByColumnsAndValues(div_DesktopActivities, activityMap)) {
                    return true;
                }
            }
            return false;
        }
        return tableUtils.verifyRowExistsInTableByColumnsAndValues(div_DesktopActivities, activityMap);
    }


    //Queues


    //Names

}
