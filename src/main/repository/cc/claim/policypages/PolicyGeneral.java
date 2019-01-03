package repository.cc.claim.policypages;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.enums.Coverages;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PolicyGeneral extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;
    private TableUtils tableUtils;

    public PolicyGeneral(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void refreshPolicyWithContacts() {
        refreshPolicy();
    }

    @FindBy(xpath = "//a[contains(@id,'_RefreshPolicyButton')]")
    private WebElement button_RefreshPolicy;

    @FindBy(xpath = "//a[contains(@id,'ClaimPolicyGeneralScreen:ViewPC')]")
    private WebElement button_ViewInPolicyCenter;

    @FindBy(xpath = "//a[contains(@id,'ClaimPolicyGeneralScreen:ViewInPC')]")
    private WebElement button_ViewInDecPages;

    @FindBy(xpath = "//div[contains(@id,':EffectiveDate-inputEl')]")
    private WebElement input_EffectiveDate;

    @FindBy(xpath = "//div[contains(@id,':ExpirationDate-inputEl')]")
    private WebElement input_ExpirationDate;

    @FindBy(css = "span[id*=':ClaimPolicyGeneral_EditButton-btnInnerEl']")
    private WebElement buttonEdit;

    @FindBy(css = "span[id*=':EditablePropertyPolicyCoveragesLV_tb:Add-btnInnerEl']," +
            "[id*=':EditableGeneralLiabilityPolicyCoveragesLV_tb:Add-btnInnerEl']")
    private WebElement buttonAddPolicyLevelCoverages;

    @FindBy(css = "div[id*=':PolicyCoverageListDetail:EditablePropertyPolicyCoveragesLV'],[id*='PolicyCoverageListDetail:EditableGeneralLiabilityPolicyCoveragesLV']")
    private WebElement tablePolicyLevelCoverages;

    @FindBy(css = "input[id*=':Other_VerifiedPolicy_true-inputEl']")
    private WebElement radioVerifiedPolicyTrue;

    public void clickVerifiedPolicyTrue() {
        clickWhenClickable(radioVerifiedPolicyTrue, 5000);
    }


    public void buildPolicyLevelCoverage(Coverages selectedCoverage) {
        int row = tableUtils.getRowCount(tablePolicyLevelCoverages);
        setCoverageRow(selectedCoverage, row);
        setCoverageType(selectedCoverage, row);
        setDeductible(new BigDecimal(250.00), row);
        setExposureLimit(new BigDecimal(25000.00), row);
        setIncidentLimit(new BigDecimal(50000.00), row);
        clickUpdate();
    }

    public boolean confirmCoverageAdded(Coverages selectedCoverage) {
        waitUtils.waitUntilElementIsVisible(tablePolicyLevelCoverages, 5000);
        List<WebElement> found = tablePolicyLevelCoverages.findElements(By.xpath("//div[contains(text(),'"+selectedCoverage.getText()+"')]"));
        return found != null && found.size() > 0;
    }

    private void doubleClickTableCell(int row, int cell) {
        WebElement deductibleCell = this.driver.findElement(By.cssSelector("div[id*='ClaimPolicyGeneral:ClaimPolicyGeneralScreen:" +
                "PolicyGeneralPanelSet:PolicyCoverageListDetail:EditableGeneralLiabilityPolicyCoveragesLV-body'] tr:nth-child(" + row + ") td:nth-child(" + cell + ")"));

        Actions actions = new Actions(this.driver);
        actions.moveToElement(deductibleCell).doubleClick().build().perform();
    }

    private void setDeductible(BigDecimal amount, int row) {
        doubleClickTableCell(row, 4);
        sendArbitraryKeys(amount.setScale(2, RoundingMode.HALF_UP).toString());
        this.driver.findElement(By.id("QuickJump-inputEl")).click();
    }

    private void setExposureLimit(BigDecimal amount, int row) {
        doubleClickTableCell(row, 5);
        sendArbitraryKeys(amount.setScale(2, RoundingMode.HALF_UP).toString());
        this.driver.findElement(By.id("QuickJump-inputEl")).click();
    }

    private void setIncidentLimit(BigDecimal amount, int row) {
        doubleClickTableCell(row, 6);
        sendArbitraryKeys(amount.setScale(2, RoundingMode.HALF_UP).toString());
        this.driver.findElement(By.id("QuickJump-inputEl")).click();
    }

    private void setCoverageRow(Coverages selectedCoverage, int row) {
        WebElement typeCell = tablePolicyLevelCoverages.findElement(By.cssSelector("div[id*='CoverageListDetail'] tr:nth-child("+row+") td:nth-child(2)"));

        Actions action = new Actions(driver);
        action.doubleClick(typeCell).perform();
        action.doubleClick(typeCell).perform();

        tablePolicyLevelCoverages.findElement(By.cssSelector("input[id*='simplecombo-']")).clear();
        tablePolicyLevelCoverages.findElement(By.cssSelector("input[id*='simplecombo-']")).sendKeys(selectedCoverage.getText());
        tablePolicyLevelCoverages.findElement(By.cssSelector("input[id*='simplecombo-']")).sendKeys(Keys.ENTER);
        waitForPostBack();
    }

    private void setCoverageType(Coverages selectedCoverage, int row) {
        WebElement typeCell = tablePolicyLevelCoverages.findElement(By.cssSelector("div[id*='PolicyCoveragesLV'] tr:nth-child("+row+") td:nth-child(3)"));

        Actions action = new Actions(driver);
        action.doubleClick(typeCell).perform();
        action.doubleClick(typeCell).perform();
        tablePolicyLevelCoverages.findElement(By.cssSelector("input[id*='simplecombo-']")).clear();
        tablePolicyLevelCoverages.findElement(By.cssSelector("input[id*='simplecombo-']")).sendKeys(selectedCoverage.getCoverageType());
        clickProductLogo();
    }

    public void clickAddPolicyLevelCoverage() {
        clickWhenClickable(buttonAddPolicyLevelCoverages, 5000);
    }

    public void clickEditButton() {
        clickWhenClickable(buttonEdit);
    }

    @FindBy(xpath = "//span[contains(text(),'With Contacts')]")
    private WebElement linkWithContacts;

    @FindBy(xpath = "//span[contains(@id,'PolicyRefreshWizard:Finish-btnInnerEl')]")
    private WebElement buttonFinish;

    private void clickWithContacts() {
        clickWhenClickable(linkWithContacts);
    }

    private void clickFinishButton() {
        waitUtils.waitUntilElementIsVisible(buttonFinish, 20);
        clickWhenClickable(buttonFinish);
    }

    public PolicyGeneral refreshPolicy() {
        clickWhenClickable(button_RefreshPolicy);
        clickWithContacts();
        clickFinishButton();
        return this;
    }

    public String clickViewInPolicyCenterButton() {
        clickWhenClickable(button_ViewInPolicyCenter);

        

        String mainCCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains("Guidewire PolicyCenter (Service Account CC)")) {
                systemOut("Switching to Policy Center Window");
                break;
            }
        }
        return mainCCWindow;
    }

    public String clickViewDecPagesButton() {
        clickWhenClickable(button_ViewInDecPages);

        

        String mainCCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains("ImageRight Virtual Desktop")) {
                systemOut("Switching to Image Right Window");
                break;
            }
        }
        return mainCCWindow;
    }

    public LocalDate getPolicyEffectiveDate() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
        return LocalDate.parse(this.waitUtils.waitUntilElementIsVisible(input_EffectiveDate).getText(), dtf);
    }

    public LocalDate getPolicyExpirationDate() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
        return LocalDate.parse(this.waitUtils.waitUntilElementIsVisible(input_ExpirationDate).getText(), dtf);
    }

    public long gatherInfoAndCalculateIncreasedReplacementCostAmount(String locationAddress, String coveredRisk, String coverageType) {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickPolicyLink();
        
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
        LocalDate effectiveDate = getPolicyEffectiveDate();
        
        LocalDate expirationDate = getPolicyExpirationDate();
        LocalDate dol = LocalDate.parse(find(By.xpath("//a[@id='Claim:ClaimInfoBar:LossDate']//span[@class='infobar_elem_val']")).getText(), dtf);

        repository.cc.claim.policypages.PolicyLocations locations = new repository.cc.claim.policypages.PolicyLocations(this.driver);

        double coverageLimit = locations.getCoverageExposureLimit(locationAddress, coveredRisk, coverageType);

        double autoIncreasePercentage = locations.getAutoIncreasePercentage(locationAddress, coveredRisk, coverageType, "Auto Increase");

        double daysBetweenDolandEffDate = Days.daysBetween(effectiveDate, dol).getDays();

        int daysInYear = Days.daysBetween(effectiveDate, expirationDate).getDays();

        double calculatedVal = (coverageLimit * autoIncreasePercentage) * (daysBetweenDolandEffDate / daysInYear);

        return Math.round(calculatedVal);


    }

    /**
     * This function gets the deductible, exposure limit, and incident limit for a given coverage
     *
     * @return deductible amount, exposure limit amount, incident limit amount
     */
    public List<String> gatherCoverageLimitAmounts(String locationAddress, String coveredRisk, String coverageType) {
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickPolicyLink();
        repository.cc.claim.policypages.PolicyLocations locations = new PolicyLocations(this.driver);
        return locations.getCoverageLimitAmounts(locationAddress, coveredRisk, coverageType);
    }

}
