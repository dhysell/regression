package repository.bc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class BCDesktopMyActivities extends BasePage {
	
	private WebDriver driver;
    private TableUtils tableUtils;

    public BCDesktopMyActivities(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    public Guidewire8Select select_MyActivitiesStatusFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DesktopActivitiesLV:DesktopActivitiesFilter-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV']")
    public WebElement table_DesktopMyActivities;

    @FindBy(xpath = "//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body']/div/table[starts-with(@id,'gridview-')]")
    public WebElement table_DesktopMyActivitiesBody;

    // the buttons and other elements after clicking the link in My Activities
    // Result table
    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtonSet_UpdateButton']")
    public WebElement button_DesktopMyActivitiesUpdate;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtonSet_ApproveButton']")
    public WebElement button_DesktopMyActivitiesApprove;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtonSet_RejectButton']")
    public WebElement button_DesktopMyActivitiesReject;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtonSet_RejectWithHold']")
    public WebElement button_DesktopMyActivitiesRejectAndHoldFutureAutomaticDisbursements;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtonSet_CancelButton']")
    public WebElement button_DesktopMyActivitiesCancel;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtons_CreateEmail']")
    public WebElement button_DesktopMyActivitiesCreateEmail;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtons_SendToTSIButton']")
    public WebElement button_DesktopMyActivitiesSendToTSI;

    @FindBy(xpath = "//a[@id='ActivityDetailWorksheet:ActivityDetailScreen:ActivityDetailToolbarButtonSet:ActivityDetailToolbarButtons_WriteoffButton']")
    public WebElement button_DesktopMyActivitiesWriteOff;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setMyActivitiesStatusFilter(String statusToSelect) {
        try {
            select_MyActivitiesStatusFilter().selectByVisibleText(statusToSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebElement getMyActivitiesTable() {
        waitUntilElementIsClickable(table_DesktopMyActivitiesBody);
        return table_DesktopMyActivitiesBody;
    }

    public void clickMyActivitiesTableCheckbox(int rowNum) {
        tableUtils.setCheckboxInTable(getMyActivitiesTable(), rowNum, true);

    }

    public void clickLinkInActivityResultTable(String linktextInTable) {
        tableUtils.clickLinkInTable(table_DesktopMyActivitiesBody, linktextInTable);
    }

    public boolean cellExistInActivityTableByLinkText(String cellText) {
        return new TableUtils(getDriver()).checkIfLinkExistsInTable(table_DesktopMyActivitiesBody, cellText);
    }

    public void clickActivityTableTitleToSort(String tableTitle) {
        tableUtils.sortByHeaderColumn(table_DesktopMyActivities, tableTitle);
    }

    // helpers for the elements after clicking the link in My Activities result
    // table

    public void clickActivityUpdate() {
        clickWhenVisible(button_DesktopMyActivitiesUpdate);
    }

    public void clickActivityApprove() {
        clickWhenVisible(button_DesktopMyActivitiesApprove);
    }

    public void clickActivityReject() {
        clickWhenVisible(button_DesktopMyActivitiesReject);
    }

    public void clickActivityRejectAndHoldFutureAutomaticDisbursements() {
        clickWhenVisible(button_DesktopMyActivitiesRejectAndHoldFutureAutomaticDisbursements);
    }

    public void clickActivityCancel() {
        clickWhenVisible(button_DesktopMyActivitiesCancel);
    }

    public void clickActivityCreateEmail() {
        clickWhenVisible(button_DesktopMyActivitiesCreateEmail);
    }

    public void clickActivitySendToTSI() {
        clickWhenVisible(button_DesktopMyActivitiesSendToTSI);
    }

    public void clickActivityWriteOff() {
        clickWhenVisible(button_DesktopMyActivitiesWriteOff);
    }

    public void completeActivity(Date openedDate, String activityName, String accountNumber) {
        clickActivityTableTitleToSort("Opened");
        clickActivityTableTitleToSort("Opened");

        String openedGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyActivities, "Opened");
        String subjectGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyActivities, "Subject");
        String accountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyActivities, "Account");

        WebElement reviewDelinquencyRow = table_DesktopMyActivities.findElement(By.xpath(".//tr/td[contains(@class,'" + openedGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", openedDate) + "')]/parent::tr/td[contains(@class,'" + subjectGridColumnID + "') and contains(.,'" + activityName + "')]/parent::tr/td[contains(@class,'" + accountGridColumnID + "') and contains(.,'" + accountNumber + "')]"));
        WebElement reviewDelinquencyCheckbox = reviewDelinquencyRow.findElement(By.xpath(".//parent::tr/td/div/a[contains(., '" + activityName + "')]"));
        reviewDelinquencyCheckbox.click();

        clickActivitySendToTSI();
    }

}
