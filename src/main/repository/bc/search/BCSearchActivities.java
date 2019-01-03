package repository.bc.search;

import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.ActivityStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class BCSearchActivities extends BasePage {

    private WebDriver driver;

    public BCSearchActivities(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    private Guidewire8Select select_BCSearchActivitiesActivityPattern() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ActivitySearchDV:ActivityPatternCriterion-triggerWrap')]");
    }

    private Guidewire8Select select_BCSearchActivitiesSubtype() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivitySearchDV:ActivitySubtypeCriterion-triggerWrap')]");
    }

    private Guidewire8Select select_BCSearchActivitiesAssignedToUser() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivitySearchScreen:ActivitySearchDV:AssignedToUserCriterion-triggerWrap')]");
    }

    private Guidewire8Select select_BCSearchActivitiesAssignedToQueue() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivitySearchScreen:ActivitySearchDV:AssignedToQueueCriterion-triggerWrap')]");
    }

    private Guidewire8Select select_BCSearchActivitiesStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,':StatusCriterion-triggerWrap')]");
    }

    private Guidewire8Select select_BCSearchActivitiesApproved() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivitySearchScreen:ActivitySearchDV:ApprovedCriterion-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='ActivitySearch:ActivitySearchScreen:ActivitySearchResultsLV']")
    private WebElement table_BCSearchActivitiesSearchResults;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public WebElement getSearchActivitiesSearchResultsTable() {
        return table_BCSearchActivitiesSearchResults;
    }

    public boolean bcSearchActivitiesSearchResultsTableExist() {
        return table_BCSearchActivitiesSearchResults.isDisplayed();
    }

    public void setSearchActivitiesActivityPattern(String activityPattern) {
        select_BCSearchActivitiesActivityPattern().selectByVisibleText(activityPattern);
    }

    public void setSearchActivitiesSubtype(String type) {
        select_BCSearchActivitiesSubtype().selectByVisibleText(type);
    }

    public void setSearchActivitiesAssignedToUser(String user) {
        select_BCSearchActivitiesAssignedToUser().selectByVisibleText(user);
    }

    public void setSearchActivitiesAssignedToQueue(ActivityQueuesBillingCenter queue) {
        select_BCSearchActivitiesAssignedToQueue().selectByVisibleText(queue.getValue());
    }

    public void setSearchActivitiesStatus(ActivityStatus status) {
        select_BCSearchActivitiesStatus().selectByVisibleText(status.getValue());
    }

    public void setSearchActivitiesApproved(YesOrNo yesOrNo) {
        select_BCSearchActivitiesApproved().selectByVisibleText(yesOrNo.getValue());
    }

    public boolean verifyActivitiesTable(Date openDate, String assignedTo) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Opened", DateUtils.dateFormatAsString("MM/dd/yyyy", openDate));
        columnRowKeyValuePairs.put("Assigned To", assignedTo);
        return new TableUtils(getDriver()).getRowsInTableByColumnsAndValues(table_BCSearchActivitiesSearchResults, columnRowKeyValuePairs).size() >= 1;
    }
}
