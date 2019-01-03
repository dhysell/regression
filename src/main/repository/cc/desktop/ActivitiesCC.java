package repository.cc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class ActivitiesCC extends BasePage {

    private WaitUtils waitUtils;

    public ActivitiesCC(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV']/div/div/div/div[contains(text(),'of')]")
    WebElement numberOfPagesElement;

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    WebElement insurdedsNameElement;

    @FindBy(xpath = "//a[@data-qtip='Next Page']")
    public WebElement button_NextPage;

    @FindBy(xpath = "//a[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivities_ApproveButton']")
    public WebElement button_Approve;

    @FindBy(xpath = "//span[@id='UserVacationWorksheet:UserVacationScreen:ttlBar']")
    public WebElement vactionStatus;

    @FindBy(xpath = "//a[@id='UserVacationWorksheet:UserVacationScreen:Update']")
    public WebElement button_Update;

    @FindBy(css = "span[id*='DesktopActivities:DesktopActivitiesScreen']")
    private WebElement headerActivities;

    public void waitForPageToLoad() {
        waitUtils.waitUntilElementIsVisible(headerActivities, 15);
    }

    public void approveReserveAndChecks(String insured, String claimNumber, String dueDate, String approveType) {

        String searchString = "";

        if (approveType.equalsIgnoreCase("Check")) {
            searchString = "Review and approve new payment";
        }
        if (approveType.equalsIgnoreCase("Reserve")) {
            searchString = "Review and approve reserve change";
        }

        List<WebElement> pagesElement = finds(By.xpath("//div[contains(text(),'of')]"));
        int numPages = 1;

        if (pagesElement.size() > 1) {
            try {
                numPages = Integer.parseInt(pagesElement.get(0).getText().replaceAll("[^0-9]", ""));
            } catch (Exception e) {
                numPages = 1;
            }

        }

        for (int i = 1; i < numPages; i++) {

            List<WebElement> rowArray = finds(By.xpath(
                    "//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body']/div/table/tbody/tr"));

            for (WebElement row : rowArray) {
                String rowNum = row.getAttribute("data-recordindex");
                String subject = find(By.xpath("//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body']/div/table/tbody/tr[@data-recordindex='"
                        + rowNum + "']/td[6]/div/a")).getText();
                String claim = find(By.xpath("//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body']/div/table/tbody/tr[@data-recordindex='"
                        + rowNum + "']/td[7]/div/a"))
                        .getText();
                if (subject.equalsIgnoreCase(searchString) && claimNumber.equalsIgnoreCase(claim)) {
                    find(By.xpath("//div[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body']/div/table/tbody/tr[@data-recordindex='"
                            + rowNum + "']/td[1]/div/img")).click();
                }
            }

            

            try {
                clickWhenClickable(find(By.xpath("//a[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivities_ApproveButton']")));
            } catch (Exception e) {

            }
            if (i < numPages) {
                
                clickWhenClickable(find(By.xpath("//a[@data-qtip='Next Page']")));
                
            }

        }

    }

    // HELPERS
    // ==============================================================================

    public void clickApproveButton() {
        clickWhenClickable(button_Approve);
    }

    public void clickNextPageButton() {
        clickWhenClickable(button_NextPage);
    }

    public Guidewire8Select select_VactionStatus() {
        return new Guidewire8Select(
                getDriver(),"//table[@id='UserVacationWorksheet:UserVacationScreen:UserVacationDV:VacationStatus-triggerWrap']");
    }

    public void selectSpecificVactionStatus(String status) {
        select_VactionStatus().selectByVisibleText(status);
    }

    public boolean validateActivityExists(String activityName) {

        boolean activityExists = false;
        List<WebElement> activitiesTable = finds(By.xpath("//a[contains(text(),'" + activityName + "')]"));

        if (activitiesTable.size() > 0) {
            activityExists = true;
        }

        return activityExists;
    }

}
