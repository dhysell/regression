package repository.cc.topmenu;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.administration.AdminMenuCC;
import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.claim.searchpages.MedicareSearchsCC;
import repository.cc.claim.searchpages.SearchPageCC;
import repository.cc.desktop.DesktopSidebarCC;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.elements.Guidewire8TabArrow;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopMenu extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public TopMenu(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ---------------------------------------------
    // Recorded Elements

    // Start Desktop Tab

    @FindBy(css = "a[id$='DesktopTab']")
    private WebElement linkDesktopTab;

    @FindBy(xpath = "//a[@id='TabBar:DesktopTab']")
    public WebElement link_TopMenuDesktopTabArrow;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopActivities']/a")
    public WebElement link_DesktopActivities;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopClaims']/a")
    public WebElement link_DesktopClaims;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopExposures']/a")
    public WebElement link_DesktopExposures;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopQueuedActivities']/a")
    public WebElement link_DesktopQueues;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopCalendarGroup']/a")
    public WebElement link_DesktopCalendar;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DesktopCalendarGroup:DesktopCalendarGroup_Calendar']/a")
    public WebElement link_DesktopMyCalendar;

    @FindBy(xpath = "//div[@id='TabBar:DesktopTab:Desktop_DraftClaims']/a")
    public WebElement link_DesktopDraftClaims;

    @FindBy(xpath = "//a[@id='TabBar:ClaimTab:ClaimTab_FNOLWizard-itemEl']")
    public WebElement link_ClaimTabNewClaim;

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    public WebElement element_InsuredsName;

    public Guidewire8Select comboBox_DesktopMyActivities() {
        Guidewire8Select desktopActivities = new Guidewire8Select(
                driver, "//table[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV:DesktopActivitiesFilter-triggerWrap']");
        return desktopActivities;
    }

    @FindBy(css = "a[id*='TabLinkMenuButton']")
    private WebElement linkSystemGear;

    @FindBy(css = "a[id*='LogoutTabBarLink']")
    private WebElement linkLogout;

    private void clickLogoutLink() {
        clickWhenClickable(linkLogout);
    }


    public void logoutFromClaimCenter() {
        clickWhenClickable(linkSystemGear);
        clickLogoutLink();
        this.waitUtils.waitUntilElementIsNotVisible(linkSystemGear);
    }


    public void clickDesktopActivitiesComboBox() {
        clickWhenClickable(comboBox_DesktopMyActivities().getSelectButtonElement());
    }

    // End Desktop Tab

    // Start Search Tab

    @FindBy(xpath = "//a[@id='TabBar:SearchTab']")
    public WebElement link_TopMenuSearchTab;

    @FindBy(xpath = "//a[@id='TabBar:SearchTab']")
    public WebElement link_TopMenuSearchTabArrow;

    public Guidewire8TabArrow button_ClaimTabArrow() {
        Guidewire8TabArrow claimTabArrow = new Guidewire8TabArrow(driver, "//*[@id='TabBar:ClaimTab']");
        return claimTabArrow;
    }

    @FindBy(xpath = "//div[@id='TabBar:SearchTab:Search_ClaimSearchesGroup']/a")
    public WebElement link_SearchClaims;

    @FindBy(xpath = "//div[@id='TabBar:SearchTab:Search_ClaimSearchesGroup:ClaimSearchesGroup_SimpleClaimSearch']/a")
    public WebElement link_SearchClaimsSimple;

    @FindBy(xpath = "//div[@id='TabBar:SearchTab:Search_ClaimSearchesGroup:ClaimSearchesGroup_ClaimSearch']/a")
    public WebElement link_SearchClaimsAdvanced;

    @FindBy(xpath = "//div[@id='TabBar:SearchTab:Search_ActivitySearch']/a")
    public WebElement link_SearchActivities;

    @FindBy(css = "a[id*='TabBar:SearchTab:Search_CheckSearchesGroup-itemEl']")
    public WebElement link_SearchChecks;

    @FindBy(xpath = "//div[@id='TabBar:SearchTab:Search_RecoverySearch']/a")
    public WebElement link_SearchRecoveries;

    // End Search Tab

    // Start Claim Tab

    @FindBy(css = "span[id*='TabBar:ClaimTab-btnInnerEl']")
    private WebElement elementClaimNumber;

    public String getClaimNumber() {
        waitUtils.waitUntilElementIsVisible(elementClaimNumber, 10);
        String claimText = elementClaimNumber.getText();
        String claimNumber[] = claimText.split("[\\(||\\)]");
        return claimNumber[1];
    }

    @FindBy(xpath = "//a[(@id='TabBar:ClaimTab')]")
    public WebElement link_TopMenuClaimTab;

    @FindBy(xpath = "//a[(@id='TabBar:ClaimTab')]/span[contains(@id,':ClaimTab-btnWrap')]")
    public WebElement link_TopMenuClaimTabArrow;

    @FindBy(css = "input[id='TabBar:ClaimTab:ClaimTab_FindClaim-inputEl']")
    public WebElement editbox_TopMenuClaimNumberSearch;

    // End Claim Tab

    // Start Administration Tab

    @FindBy(xpath = "//a[(@id='TabBar:AdminTab')]")
    public WebElement link_TopMenuAdministrationTab;

    // End Administration Tab

    // Start Address Book Tab

    @FindBy(xpath = "//a[(@id='TabBar:AddressBookTab')]")
    public WebElement link_TopMenuAddressBookTab;

    @FindBy(xpath = "//a[(@id='TabBar:AddressBookTab')]")
    public WebElement link_TopMenuAddressBookTabArrow;

    @FindBy(xpath = "//div[(@id='TabBar:AddressBookTab:AddressBook_AddressBookSearchesGroup')]/a")
    public WebElement link_TopMenuAddressBookSearch;

    @FindBy(xpath = "//div[(@id='TabBar:AddressBookTab:AddressBook_AddressBookSearchesGroup:AddressBookSearchesGroup_SimpleABContactSearch')]/a")
    public WebElement link_TopMenuAddressBookSimple;

    @FindBy(xpath = "//div[(@id='TabBar:AddressBookTab:AddressBook_AddressBookSearchesGroup:AddressBookSearchesGroup_AddressBookSearch')]/a")
    public WebElement link_TopMenuAddressBookAdvanced;

    public String getInsuredsName() {
        String nameOfInsured = element_InsuredsName.getText();
        return nameOfInsured;
    }

    @FindBy(css = "span[id*='Claim:ClaimInfoBar:LossDate'] span[class*='infobar_elem_val']")
    private WebElement elementDateOfLoss;

    public String getDateOfLoss() {
        String dateOfLoss = elementDateOfLoss.getText();
        return dateOfLoss;
    }

    @FindBy(css = "span[id*='Claim:ClaimInfoBar:Losstype'] span[class*='infobar_elem_val']")
    private WebElement elementLossType;

    public String getLossType() {
        String lossType = elementLossType.getText();
        return lossType;
    }

    @FindBy(css = "span[id*='Claim:ClaimInfoBar:PolicyNumber'] span[class*='infobar_elem_val']")
    private WebElement elementPolicyNumber;

    public String getPolicyNumber() {
        String policyNumber = elementPolicyNumber.getText();
        return policyNumber;
    }

    // Click Desktop Tab
    public DesktopSidebarCC clickDesktopTab() {
        clickWhenClickable(linkDesktopTab);
        return new DesktopSidebarCC(this.driver);
    }

    public void clickDesktopTabArrow() {
        
        Actions build = new Actions(this.driver);
        build.moveToElement(link_TopMenuDesktopTabArrow, 80, 0).click().build().perform();
    }

    public void clickDesktopActivities() {
        clickWhenClickable(link_DesktopActivities);
    }

    public void clickDesktopClaims() {
        clickWhenClickable(link_DesktopClaims);
    }

    public void clickDesktopExposures() {
        clickWhenClickable(link_DesktopExposures);
    }

    public void clickDesktopQueues() {
        clickWhenClickable(link_DesktopQueues);
    }

    public void clickDesktopCalendar() {
        clickWhenClickable(link_DesktopCalendar);
    }

    public void clickDesktopMyCalendar() {
        clickWhenClickable(link_DesktopMyCalendar);
    }

    public void clickDesktopDraftClaims() {
        clickWhenClickable(link_DesktopDraftClaims);
    }

    // End Desktop Tab

    // Start Search Tab

    public repository.cc.claim.searchpages.SearchPageCC clickSearchTab() {
        try {
            waitUntilElementIsClickable(link_TopMenuSearchTab, 20);
            clickWhenClickable(link_TopMenuSearchTab);
        } catch (Exception e) {
            
            clickWhenClickable(link_TopMenuSearchTab);
        }
        waitUntilElementIsNotVisible(editbox_TopMenuClaimNumberSearch);
        return new SearchPageCC(this.driver);
    }

    public void clickSearchTabArrow() {
        Actions build = new Actions(this.driver);
        

        waitUtils.waitUntilElementIsClickable(link_TopMenuSearchTabArrow, 15);
        build.moveToElement(link_TopMenuSearchTabArrow, 70, 0).click().build().perform();
    }

    public void clickSearchClaims() {
        clickWhenClickable(link_SearchClaims);
    }

    public void hoverOverSearchClaims() {
        hoverOver(link_SearchClaims);
    }

    public void clickSearchClaimsSimple() {
        clickWhenClickable(link_SearchClaimsSimple);
    }

    private void clickSearchClaimsAdvanced() {
        clickWhenClickable(link_SearchClaimsAdvanced);
    }

    public repository.cc.claim.searchpages.AdvancedSearchCC clickClaimsAdvancedSearchLink() {
        clickSearchTabArrow();
        hoverOverSearchClaims();
        clickSearchClaimsAdvanced();

        return new AdvancedSearchCC(getDriver());
    }

    public void clickSearchActivities() {
        clickWhenClickable(link_SearchActivities);
    }

    public void clickSearchChecks() {
        clickWhenClickable(link_SearchChecks);
    }

    public void clickDesktopMyRecoveries() {
        clickWhenClickable(link_SearchRecoveries);
    }

    // End Search Tab

    // Start Claim Tab


    public void clickRandomClaim() {

        List<WebElement> claimLinks = finds(By.xpath("//span[contains(@id,':ClaimMenuClaim-textEl')]"));

        int randNum = NumberUtils.generateRandomNumberInt(0, claimLinks.size() - 1);

        claimLinks.get(randNum).click();

    }

    public void clickClaimTab() {
        clickWhenClickable(link_TopMenuClaimTab);
    }

    public void setClaimNumberSearch(String claimNumber) {

        waitUtils.waitUntilElementIsVisible(editbox_TopMenuClaimNumberSearch);
        clickWhenClickable(editbox_TopMenuClaimNumberSearch);
        
        editbox_TopMenuClaimNumberSearch.sendKeys(Keys.HOME);
        editbox_TopMenuClaimNumberSearch.sendKeys(claimNumber);
        editbox_TopMenuClaimNumberSearch.sendKeys(Keys.RETURN);
        waitUntilElementIsNotVisible(editbox_TopMenuClaimNumberSearch, 20);
    }

    public void searchClaimNumberFromDropdown(String claimNumber) {
        try {
            clickClaimTabArrow();
        } catch (Exception e) {
            clickClaimTabArrowInClaim();
        }

        setClaimNumberSearch(claimNumber);
    }

    public void clickClaimTabArrow() {

        Actions build = new Actions(this.driver);
        
        waitUntilElementIsClickable(link_TopMenuClaimTabArrow, 60);
        try {
            build.moveToElement(link_TopMenuClaimTabArrow, 60, 0).click().build().perform();
        } catch (Exception e) {
            
            build.moveToElement(link_TopMenuClaimTabArrow, 212, 0).click().build().perform();
        }

    }

    public void clickClaimTabArrowInClaim() {
        Actions build = new Actions(this.driver);
        
        build.moveToElement(link_TopMenuClaimTabArrow, 212, 0).click().build().perform();
    }

    public void clickNewClaimLink() {
        clickWhenClickable(link_ClaimTabNewClaim);
        waitUntilElementIsVisible(By.cssSelector("input[id$='policyNumber-inputEl']"));
    }


    public String gatherClaimNumber() {

        waitUtils.waitUntilElementIsVisible(element_InsuredsName, 10);

        String claimText = find(By.cssSelector("span[id='TabBar:ClaimTab-btnInnerEl']")).getText();

        String regexPattern = "\\d+";
        Pattern p = Pattern.compile(regexPattern);
        Matcher matcher = p.matcher(claimText);
        matcher.find();
        String claimNumber = matcher.group(0);
        return claimNumber;
    }

    public String gatherClaimOwnerName() {
        
        String ownerFullString = find(By.cssSelector("span[id$='Adjuster-btnInnerEl'] span.infobar_elem_val")).getText();
        String regexPattern = "^.*?(?=[\\(])";
        Pattern p = Pattern.compile(regexPattern);
        Matcher matcher = p.matcher(ownerFullString);
        matcher.find();
        return matcher.group(0);
    }


    public LocalDate gatherDOL() {
        String dol = find(By.xpath("//span[contains(@id,':LossDate-btnInnerEl')]//span[@class='infobar_elem_val']")).getText();
        String dateParts[] = dol.split("/");

        LocalDate dateOfLoss = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]));

        return dateOfLoss;
    }


    public String gatherDOLString() {
        String dol = find(By.xpath("//span[contains(@id,':LossDate-btnInnerEl')]//span[@class='infobar_elem_val']")).getText();

        return dol;
    }

    // End Claim Tab

    // Start Administration Tab

    public repository.cc.administration.AdminMenuCC clickAdministrationTab() {
        clickWhenClickable(link_TopMenuAdministrationTab);
        return new AdminMenuCC(this.driver);
    }

    // End Administration Tab

    // Start Address Book Tab

    public void clickAddressBookTab() {
        clickWhenClickable(link_TopMenuAddressBookTab);
    }

    public void clickAddressBookTabArrow() {
        Actions build = new Actions(this.driver);
        build.moveToElement(link_TopMenuAddressBookTab, 110, 0).click().build().perform();
    }

    public void clickAddressBookSearch() {
        clickWhenClickable(link_TopMenuAddressBookSearch);
    }

    public void clickAddressBookSimple() {
        clickWhenClickable(link_TopMenuAddressBookSimple);
    }

    public void clickAddressBookAdvanced() {
        clickWhenClickable(link_TopMenuAddressBookAdvanced);
    }

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:State-btnInnerEl']//span[@class='infobar_elem_val']")
    WebElement claimStatusElement;

    @FindBy(xpath = "//a[contains(@id,'MedicareSection111Search-itemEl')]")
    private WebElement linkMedicareSection111;


    public String getClaimStatus() {
        return claimStatusElement.getText();
    }


    public void goToClaimByClaimNumber(String cNum) {
        try {
            clickClaimTabArrowInClaim();
            setClaimNumberSearch(cNum);
        } catch (Exception e) {
            clickClaimTabArrow();
            setClaimNumberSearch(cNum);
        }
    }


    public repository.cc.claim.searchpages.MedicareSearchsCC searchMedicareSection111() {
        clickSearchTabArrow();
        hoverOverClaimsLink();
        clickMedicareSection111();
        return new MedicareSearchsCC(this.driver);
    }

    @FindBy(css = "a[id*='TabBar:SearchTab:Search_ClaimSearchesGroup-itemEl']")
    private WebElement linkClaims;

    private void hoverOverClaimsLink() {
        hoverOver(linkClaims);
    }

    private void clickMedicareSection111() {
        clickWhenClickable(linkMedicareSection111);
    }

    // Unsaved Work Links

    @FindBy(css = "a[id*='TabBar:UnsavedWorkTabBarLink']")
    private WebElement linkUnsavedWork;

    public void clickUnsavedWork() {
        waitUtils.waitUntilElementIsVisible(linkUnsavedWork);
        linkUnsavedWork.click();
    }

    public void clickUnsavedWorkText(String linkText) {
        WebElement link = find(By.xpath("//a[contains(text(),'"+linkText+"')]"));
        link.click();
    }
}