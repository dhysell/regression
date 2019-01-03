package repository.pc.search;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class SearchSidebarPC extends BasePage {

    public SearchSidebarPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[(@id='Search:MenuLinks:Search_LienholderBillingSearch')]")
    public WebElement link_LienholderBilling;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_JobSearch')]")
    private WebElement link_OtherJobs;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_SubmissionSearch')]")
    private WebElement link_Submissions;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_PolicySearch')]")
    private WebElement link_Policies;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_ContactSearch')]")
    private WebElement link_Contacts;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_ActivitySearch')]")
    private WebElement link_Activities;

    @FindBy(xpath = "//td[contains(@id, 'Search:MenuLinks:Search_AccountSearch') or contains(@id, 'MenuLinks:SearchGroup_AccountSearch')]")
    private WebElement link_Accounts;

    public void clickAccounts() {
        clickWhenClickable(link_Accounts);
    }

    public void clickActivites() {
        clickWhenClickable(link_Activities);
    }


    public void clickContacts() {
    	clickWhenClickable(link_Contacts);
    }


    public void clickPolicies() {
    	clickWhenClickable(link_Policies);
    }


    public void clickSubmissions() {
    	clickWhenClickable(link_Submissions);
    }


    public void clickOtherJobs() {
    	clickWhenClickable(link_OtherJobs);
    }


    public void clickLienholderBilling() {
    	clickWhenClickable(link_LienholderBilling);
    }

}
