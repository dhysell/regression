package repository.pc.search;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.pc.account.AccountSummaryPC;
import repository.pc.topmenu.TopMenuPC;

public class SearchOtherJobsPC extends BasePage {

    private WebDriver driver;

    public SearchOtherJobsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, ':SubmissionNumber-inputEl')]")
    private WebElement editbox_JobNumber;

    @FindBy(xpath = "//a[contains(@id, ':0:SubmissionNumbe')]")
    private WebElement link_SubmissionSearchResults;


    public void setJobNumber(String jobNumber) {
        waitUntilElementIsClickable(editbox_JobNumber);
        editbox_JobNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_JobNumber.sendKeys(jobNumber);
    }

    public void searchJobByAccountNumberAndJobType(String accountNumber, TransactionType jobType) {

        repository.pc.search.SearchAccountsPC accountSearchPC = new repository.pc.search.SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickWorkOrderbyJobType(jobType);
    }


    public void searchJobByJobNumber(String jobNumber) {
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        SearchSidebarPC sidebar = new SearchSidebarPC(driver);
        sidebar.clickOtherJobs();
        setJobNumber(jobNumber);
        clickSearch();
        clickWhenClickable(link_SubmissionSearchResults);
    }

    //These methods may need to be deleted.

    public void searchJobByAccountNumber(String accountNumber, String jobSuffix) {
        // method 1: look for Other Jobs in the sidebar

        // method 2: search accounts and click on the job on the summary screen
        repository.pc.search.SearchAccountsPC accountSearchPC = new repository.pc.search.SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickWorkOrderbySuffix(jobSuffix);
    }


    public void searchJobByAccountNumberSelectProduct(String accountNumber, ProductLineType product) {
        // method 1: look for Other Jobs in the sidebar

        // method 2: search accounts and click on the job on the summary screen
        repository.pc.search.SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickWorkOrderbyProduct(product);
    }
}
