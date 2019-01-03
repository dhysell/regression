package repository.cc.claim;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkplanCC extends BasePage {

    private WebDriver driver;
    private Login login;
    private TableUtils tableUtils;

    public WorkplanCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.login = new Login(driver);
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,'CompleteButton')]")
    private WebElement ButtonComplete;

    @FindBy(xpath = "//span[contains(text(),'Workplan')]")
    public WebElement LinkWorkPlan;

    public void clickWorkplanLink() {
        clickWhenClickable(LinkWorkPlan);
    }

    public void clickCompleteButton() {
        clickWhenClickable(ButtonComplete);
    }

    @FindBy(xpath = "//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV']")
    public WebElement table_WorkPlan;

    public boolean completeActivity(String activityName) {

        clickWorkplanLink();

        try {
            find(By.xpath("//a[contains(text(),'" + activityName + "')]")).click();
        } catch (Exception e) {
            System.out.println("Activity " + activityName + " was not found.");
            return false;
        }
        waitUntilElementIsClickable(ButtonComplete);
        clickCompleteButton();
        waitForPostBack();
        return true;
    }

    private void findActivityByNameAndClick(String activityName) /* throws GuidewireClaimCenterException */ {
        if (checkActivityExists(activityName)) {
            int row = tableUtils.getRowNumberInTableByText(table_WorkPlan, activityName);
            tableUtils.clickLinkInTableByRowAndColumnName(table_WorkPlan, row, "Subject");
        } else {
            //throw new GuidewireClaimCenterException(driver.getCurrentUrl(), "Activity " + activityName + " was not found.");
            System.out.println("Activity " + activityName + " was not found.");
        }
    }

    public void findAndApproveActivityByName(String activityName) {
        findActivityByNameAndClick(activityName);
        try {
            approveActivity();
        } catch (Exception e) {
            System.out.println("Approval Not Required.");
        }

    }

    @FindBy(css = "a[id*='ApprovalDetailWorksheet_ApproveButton']")
    private WebElement buttonApprove;

    private void approveActivity() {
        
        waitUntilElementIsVisible(buttonApprove);
        buttonApprove.click();
    }

    private boolean checkActivityExists(String activityName) {
        boolean activityExists = finds(By.xpath("//a[contains(text(),'" + activityName + "')]")).size() > 0;
        return activityExists;
    }

    public void completeActivityAsPersonAssignedTo(String activityName, String claimNumber) {

        if (checkActivityExists(activityName)) {
            

            int activityRowLocation;

            try {
                activityRowLocation = tableUtils.getRowNumberInTableByText(table_WorkPlan, activityName);
            } catch (Exception e) {
                activityRowLocation = 0;
            }

            if (activityRowLocation != 0) {
            String assignedTo = tableUtils.getCellTextInTableByRowAndColumnName(table_WorkPlan, activityRowLocation, "Assigned To");
            String logInAs = "";

            if (assignedTo.equals("Default Owner")) {
                logInAs = "abatts";
            } else {
                logInAs = ClaimsUsers.getEnumByString(assignedTo);
                if (logInAs == null) {
                    Assert.fail("The name " + assignedTo + " isn't in the ClaimUsers enum, please add them");
                }
            }

            new GuidewireHelpers(driver).logout();
            login.login(logInAs, "gw");

            TopMenu topMenu = new TopMenu(this.driver);
            topMenu.clickClaimTabArrow();
            
            topMenu.setClaimNumberSearch(claimNumber);

            SideMenuCC sideMenu = new SideMenuCC(this.driver);
            sideMenu.clickWorkplanLink();
            
            findAndApproveActivityByName(activityName);
            
            completeActivityAsPersonAssignedTo(activityName, claimNumber);
        } else {
            System.out.println("The activity named: " + activityName + " wasn't found.");
        }
    }
    }

    public void completeFraudQuestionnaireActivity() {
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        repository.cc.claim.SpecialInvestigationDetails spid = new SpecialInvestigationDetails(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);

        String activityName = "Complete Fraud Questionnaire";
        String assignedTo = null;
        String claimNumber = topMenu.gatherClaimNumber();

        if (checkActivityExists(activityName)) {
            int activityRowLocation = tableUtils.getRowNumberInTableByText(table_WorkPlan, activityName);
            assignedTo = tableUtils.getCellTextInTableByRowAndColumnName(table_WorkPlan, activityRowLocation, "Assigned To");

            new GuidewireHelpers(driver).logout();
            new Login(this.driver).login(ClaimsUsers.getEnumByString(assignedTo), "gw");
            topMenu.clickClaimTabArrow();
            topMenu.setClaimNumberSearch(claimNumber);

            sideMenu.clickLossDetailsLink();
            sideMenu.clickSpecialInvestigationLink();

            spid.specialInvestiagionDetails();
        }
    }

    public void confirmStaleCheckActivities(String StaleCheckActivityName) {
        List<WebElement> tableDivElements = finds(By.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV']"));

        for (WebElement tableDivElement : tableDivElements) {
            int row = tableUtils.getRowNumberInTableByText(tableDivElement, StaleCheckActivityName);

            String dueDate = tableUtils.getCellTextInTableByRowAndColumnName(tableDivElement, row, "Due");

            DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
            Date now = DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.ClaimCenter);
            Date date = null;

            try {
                date = format.parse(dueDate);
            } catch (ParseException e) {
                System.out.println("Unable to parse valid date.");
                e.printStackTrace();
            }

            int daysDiff = DateUtils.getDifferenceBetweenDates(now, date, DateDifferenceOptions.Day);

            if (daysDiff == 29) {
                tableUtils.setCheckboxInTable(tableDivElement, row, true);

                WorkplanCC workPlan = new WorkplanCC(this.driver);
                workPlan.clickCompleteButton();

            } else {
                WorkplanCC workPlan = new WorkplanCC(this.driver);
                workPlan.clickCompleteButton();

                System.out.println("ERROR - ACTIVITY DOES NOT MATCH DATE REQUIREMENTS, PLEASE REVIEW.");
            }
        }
    }

    public void validateActivityHasCorrectDate(String activityName, Date expectedDate) throws Exception {
        if (checkActivityExists(activityName)) {
            int row = tableUtils.getRowNumberInTableByText(table_WorkPlan, activityName);
            Date dateDue = DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_WorkPlan, row, "Due"), "MM/dd/yy");

            Assert.assertTrue(DateUtils.getDifferenceBetweenDates(dateDue, expectedDate, DateDifferenceOptions.Day) == 0, "Expected date and due date don't match");

        } else {
            Assert.fail("Activity " + activityName + " was not found.");
        }
    }


}
