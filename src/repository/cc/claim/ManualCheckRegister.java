package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.desktop.DesktopSidebarCC;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ClaimsUsers;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.WaitUtils;
import repository.gw.login.Login;

import java.util.ArrayList;
import java.util.List;

public class ManualCheckRegister extends BasePage {

    private WaitUtils waitUtils;
    private WebDriver driver;

    public ManualCheckRegister(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//a[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV_tb:Edit']")
    public WebElement button_Edit;

    @FindBy(xpath = "//a[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV_tb:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV_tb:Cancel']")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV_tb:Add']")
    public WebElement button_Add;

    @FindBy(xpath = "//a[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV_tb:Remove']")
    public WebElement button_Remove;

    private String selectRandomUser() {
        select_Users().selectByVisibleTextRandom();
        return select_Users().getText();
    }

    public List<String> findCheckNumbers(String checkBookOwner, String checkType, String company)
            throws GuidewireException {
        String beginningCheckNum = "";
        String endingCheckNum = "";
        String baseXpath = "//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV-body']//tr";
        String begCheckColumn = "6";
        String endCheckColumn = "7";

        selectSpecific_CheckType(checkType);
        

        if (checkBookOwner.equalsIgnoreCase("random")) {
            checkBookOwner = selectRandomUser();
        } else {
            selectSpecific_User(checkBookOwner);
        }

        
        selectSpecific_Company(company);
        
        List<WebElement> tableRows = finds(By.xpath(baseXpath));
        // If a user has multiple check books then choose the second check book,
        // If the user only has one check book use that one, otherwise the user
        // doesn't have a check book and we won't be able to write a check as
        // her.
        if (tableRows.size() >= 2 && find(By.xpath("//tr[@data-recordindex='1']//td[5]")).getText().equalsIgnoreCase("")) {
            beginningCheckNum = find(By.xpath("//tr[@data-recordindex='1']//td[" + begCheckColumn + "]")).getText();
            endingCheckNum = find(By.xpath("//tr[@data-recordindex='1']//td[" + endCheckColumn + "]")).getText();
        } else if (tableRows.size() == 1 || !find(By.xpath("//tr[@data-recordindex='1']//td[5]")).getText().equalsIgnoreCase("")) {
            beginningCheckNum = find(By.xpath("//tr[@data-recordindex='0']//td[" + begCheckColumn + "]")).getText();
            endingCheckNum = find(By.xpath("//tr[@data-recordindex='0']//td[" + endCheckColumn + "]")).getText();
        } else {
            throw new GuidewireException(driver.getCurrentUrl(), "It doesn't seem that the user: "
                    + checkBookOwner + " has a:" + checkType + " check book checked out to them");
        }

        List<String> checkNums = new ArrayList<String>();
        checkNums.add(beginningCheckNum);
        checkNums.add(endingCheckNum);

        return checkNums;

    }

    public int getColumnByHeader(String colHeader) {
        List<WebElement> columns = finds(By.xpath(
                "//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV']/div[starts-with(@id,'headercontainer')]/div/div/div"));
        int location = -1;
        int count = 1;

        for (WebElement column : columns) {
            if (colHeader.contains(column.getText())) {
                location = count;
                System.out.println(column.getText() + " " + count);
            }
            count++;
        }
        return location;
    }

    public void processCheckData() {
        List<WebElement> mcrTable = finds(
                By.xpath("//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV']"));
        if (mcrTable.size() > 0) {
            for (WebElement singleTable : mcrTable) {
                System.out.println(singleTable.getAttribute("id"));
            }
            int rows = finds(By
                    .xpath("//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV']/div/div/table/tbody/tr"))
                    .size();
            int completedCol = getColumnByHeader("Completed Date");
            int desciptionCol = getColumnByHeader("Missing Check Number Description");

            for (int i = 0; i < rows; i++) {
                WebElement completedVal = find(By
                        .xpath("//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV']/div/div/table/tbody/tr["
                                + (i + 1) + "]/td[" + completedCol + "]"));
                WebElement descriptionVal = find(By
                        .xpath("//div[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV']/div/div/table/tbody/tr["
                                + (i + 1) + "]/td[" + desciptionCol + "]"));
                if (!descriptionVal.getText().equalsIgnoreCase(" ")) {
                    System.out.println(completedVal.getText());
                    System.out.println(descriptionVal.getText());
                }
            }
        } else {
            System.out.println("Manual Check Register is Missing.");
        }
        
    }

    public Guidewire8Select select_CheckType() {
        return new Guidewire8Select(driver,"//table[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV:checktypes-triggerWrap']");
    }

    public Guidewire8Select select_Companies() {
        return new Guidewire8Select(driver,"//table[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV:companies-triggerWrap']");
    }

    public Guidewire8Select select_MissingChecks() {
        return new Guidewire8Select(driver,"//table[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV:missing-triggerWrap']");
    }

    // HELPERS
    // ==============================================================================

    public Guidewire8Select select_Status() {
        return new Guidewire8Select(driver,"//table[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV:status-triggerWrap']");
    }

    public Guidewire8Select select_Users() {
        return new Guidewire8Select(driver,"//table[@id='ManualCheckRegister:ManualCheckRegisterScreen:Register:ManualCheckRegisterLV:assigned-triggerWrap']");
    }

    @FindBy(css = "span[id*='ManualCheckRegisterScreen']")
    private WebElement pageHeader;

    public void selectRandom_CheckType() {
        select_CheckType().selectByVisibleTextRandom();
    }

    public void selectRandom_Company() {
        select_Companies().selectByVisibleTextRandom();
    }

    public void selectRandom_MissingCheck() {
        select_MissingChecks().selectByVisibleTextRandom();
    }

    public void selectRandom_Status() {
        select_Status().selectByVisibleTextRandom();
    }

    public void selectRandom_User() {
        select_Users().selectByVisibleTextRandom();
    }

    public void selectSpecific_CheckType(String type) {
        select_CheckType().selectByVisibleText(type);
    }

    public void selectSpecific_Company(String company) {
        select_Companies().selectByVisibleText(company);
    }

    public void selectSpecific_MissingCheck(String check) {
        waitUtils.waitUntilElementIsVisible(pageHeader, 5000);
        select_MissingChecks().selectByVisibleText(check);
    }

    public void selectSpecific_Status(String status) {
        select_Status().selectByVisibleText(status);
    }

    public void selectSpecific_User(String user) {
        select_Users().selectByVisibleText(user);
    }

    // This method and subs create the checkbook object
    private int firstCheck;
    private int lastCheck;

    public void manualCheckBook(String checkBookOwner, String checkType, String companyCheckBook) throws Exception {

        DesktopSidebarCC sideBar = new DesktopSidebarCC(this.driver);
        ManualCheckRegister checkRegister = new ManualCheckRegister(this.driver);
        Login login = new Login(this.driver);
        // Login as "Assistant to the VP of Claims" to access the manual check register (Role: checkFixAll).
        login.login(ClaimsUsers.jgross.toString(), "gw");
        
        sideBar.clickManualcheckRegisterLink();
        
        List<String> begAndEndCheckNums = checkRegister.findCheckNumbers(checkBookOwner, checkType, companyCheckBook);
        this.setFirstCheck((Integer.parseInt(begAndEndCheckNums.get(0))));
        this.setLastCheck((Integer.parseInt(begAndEndCheckNums.get(1))));

        
        new GuidewireHelpers(driver).logout();
    }

    public void manualCheckBook(String checkType, String companyCheckBook) throws Exception {

        DesktopSidebarCC sideBar = new DesktopSidebarCC(this.driver);
        ManualCheckRegister checkRegister = new ManualCheckRegister(this.driver);
        Login login = new 	Login(this.driver);
        // Login as "Assistant to the VP of Claims" to access the manual check register (Role: checkFixAll).
        login.login(ClaimsUsers.jgross.toString(), "gw");
        
        sideBar.clickManualcheckRegisterLink();
        
        List<String> begAndEndCheckNums = checkRegister.findCheckNumbers("random", checkType, companyCheckBook);
        this.setFirstCheck((Integer.parseInt(begAndEndCheckNums.get(0))));
        this.setLastCheck((Integer.parseInt(begAndEndCheckNums.get(1))));

        
        new GuidewireHelpers(driver).logout();
    }

    public int getFirstCheck() {
        return firstCheck;
    }

    public void setFirstCheck(int firstCheck) {
        this.firstCheck = firstCheck;
    }

    public int getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(int lastCheck) {
        this.lastCheck = lastCheck;
    }
}

