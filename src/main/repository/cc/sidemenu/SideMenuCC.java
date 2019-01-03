package repository.cc.sidemenu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import repository.cc.administration.ScriptParametersCC;
import repository.cc.claim.FinancialsChecks;
import repository.cc.claim.policypages.PolicyGeneral;
import repository.cc.claim.policypages.PolicyLocations;
import repository.cc.desktop.ClaimSurveyEntry;
import repository.cc.search.LedgerSearch;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class SideMenuCC extends BasePage {

    WaitUtils waitUtils;
    WebDriver driver;

    public SideMenuCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public SideMenuCC onSideMenu() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 20);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[@id='Claim:MenuLinks:Claim_" +
                    "ClaimExposures']/div/span[text()='Exposures']")));
        } catch (Exception e) {
            Assert.fail("The SideMenu Page Failed to Load.");
        }

        return this;
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimExposures']/div/span[text()='Exposures']")
    public WebElement link_Exposures;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimFinancialsGroup']/div/span[text()='Financials']")
    public WebElement link_Financials;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimWorkplan']/div/span[text()='Workplan']")
    public WebElement link_Workplan;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimFinancialsGroup:ClaimFinancialsGroup_ClaimFinancialsSummary']/div/span[text()='Summary']")
    public WebElement link_FinancialsSummary;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimLossDetailsGroup']/div/span[text()='Loss Details']")
    public WebElement link_LossDetails;

    @FindBy(xpath = "//span[text()='Advanced Search']")
    public WebElement link_AdvancedSearch;

    @FindBy(xpath = "//td//span[text()='Notes']")
    public WebElement link_Notes;

    @FindBy(xpath = "//td//span[text()='Litigation/Arbitration']")
    public WebElement link_Litigation;

    @FindBy(xpath = "//td//span[text()='Documents']")
    public WebElement link_Documentation;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimFinancialsGroup:ClaimFinancialsGroup_ClaimFinancialsTransactions']//span[text()='Transactions']")
    public WebElement link_FinancialTransAction;

    @FindBy(xpath = "//span[contains(text(),'Special Investigation Details')]")
    public WebElement link_SpecialInvestigation;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimPartiesGroup']")
    public WebElement link_PartiesInvolved;

    @FindBy(xpath = "//td[contains(@id, 'Claim:MenuLinks:Claim_ClaimSnapshotGroup')]")
    public WebElement link_FNOLSnapshot;

    @FindBy(xpath = "//td[@id='Claim:MenuLinks:Claim_ClaimPolicyGroup']")
    public WebElement link_Policy;

    @FindBy(xpath = "//td[contains(@id,'_ClaimPolicyLocations')]")
    public WebElement link_PolicyLocations;

    @FindBy(css = "td[id$='_ClaimFinancialsChecks']")
    private WebElement link_FinancialsChecks;

    @FindBy(xpath = "//span[contains(text(),'Ledger Search')]")
    private WebElement linkLedgerSearch;

    @FindBy(xpath = "//span[contains(text(),'Claim Surveys')]")
    private WebElement linkClaimSurveys;

    public ClaimSurveyEntry clickClaimSurvey() {
        waitUtils.waitUntilElementIsClickable(linkClaimSurveys);
        linkClaimSurveys.click();

        return new ClaimSurveyEntry(getDriver());
    }

    public LedgerSearch clickLedgerSearchLink() {
        waitUtils.waitUntilElementIsClickable(linkLedgerSearch, 15);
        linkLedgerSearch.click();
        return new LedgerSearch(getDriver());
    }

    public void clickSpecialInvestigationLink() {
        clickWhenClickable(link_SpecialInvestigation);
    }

    public void clickAdvancedSearchLink() {
        clickWhenClickable(link_AdvancedSearch);
    }

    public repository.cc.claim.Documents clickDocuments() {
        clickWhenClickable(link_Documentation);
        return new repository.cc.claim.Documents(getDriver());
    }

    public repository.cc.claim.ExposuresTable clickExposuresLink() {
        waitUntilElementNotClickable(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
        clickWhenClickable(link_Exposures);

        repository.cc.claim.ExposuresTable exposuresTable = new repository.cc.claim.ExposuresTable(this.driver).onExposureTable();
        return exposuresTable;
    }


    public void clickFinancialsLink() {
        
        clickWhenClickable(link_Financials);
    }

    public void clickFinancialsSummaryLink() {
        clickWhenClickable(link_FinancialsSummary);
    }

    public repository.cc.claim.FinancialsTransactions clickFinancialTransactions() {
        clickFinancialsLink();
        
        clickWhenClickable(link_FinancialTransAction);

        return new repository.cc.claim.FinancialsTransactions(getDriver());
    }

    public repository.cc.claim.Matters clickLitigationLink() {
        clickWhenClickable(link_Litigation);
        return new repository.cc.claim.Matters(getDriver());
    }

    public repository.cc.claim.LossDetails clickLossDetailsLink() {
        
        try {
            clickWhenClickable(link_LossDetails);
        } catch (Exception e) {
            try {
                hoverOverAndClick(link_LossDetails);
            } catch (Exception e2) {
                System.out.println("Loss Details Link is not in Focus.");
            }
        }
        waitUntilElementIsClickable(By.cssSelector("a[id$='ClaimLossDetailsScreen:Edit']"),30);
        return new repository.cc.claim.LossDetails(this.driver);
    }

    public void clickNotesLink() {
        clickWhenClickable(link_Notes);
    }

    public repository.cc.claim.WorkplanCC clickWorkplanLink() {
        waitUtils.waitUntilElementIsVisible(link_Workplan);
        link_Workplan.click();
        return new repository.cc.claim.WorkplanCC(this.driver);
    }

    public void clickPartiesInvolved() {
        clickWhenClickable(link_PartiesInvolved);
    }

    public void clickFNOLSnapshot() {
        clickWhenClickable(link_FNOLSnapshot);
    }

    public repository.cc.claim.policypages.PolicyGeneral clickPolicyLink() {
        clickWhenClickable(link_Policy);
        return new PolicyGeneral(this.driver);
    }

    public repository.cc.claim.policypages.PolicyLocations clickPolicyLocations() {
        clickWhenClickable(link_PolicyLocations);
        
        return new PolicyLocations(this.driver);
    }

    public repository.cc.claim.FinancialsChecks clickFinancialsChecks() {
        clickWhenClickable(link_Financials);
        clickWhenClickable(link_FinancialsChecks);
        
        return new FinancialsChecks(this.driver);
    }

    @FindBy(css = "td[id*='Desktop:MenuLinks:Desktop_DesktopInvoicesGroup'] div span")
    private WebElement linkVendorInvoices;

    public void clickVendorInvoices() {
        waitUtils.waitUntilElementIsVisible(linkVendorInvoices, 15);
        linkVendorInvoices.click();
    }

    @FindBy(css = "td[id*='DesktopInvoicesGroup_DesktopFlagshipInvoice'] div span")
    private WebElement linkFlagship;

    public void clickFlagship() {
        waitUtils.waitUntilElementIsVisible(linkFlagship, 15);
        linkFlagship.click();
    }

    @FindBy(xpath = "//span[contains(text(),'Utilities')]")
    private WebElement linkUtilities;

    @FindBy(xpath = "//span[contains(text(),'Script Parameters')]")
    private WebElement linkScriptParameters;

    public ScriptParametersCC clickScriptParameters() {
        clickWhenClickable(linkUtilities);
        clickWhenClickable(linkScriptParameters);
        

        return new ScriptParametersCC(getDriver());
    }
}
