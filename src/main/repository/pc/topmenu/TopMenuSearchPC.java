package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuSearchPC extends TopMenuPC {

    public TopMenuSearchPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab:Search_AccountSearch-itemEl')]")
    public WebElement Accounts;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab:Search_ActivitySearch-itemEl')]")
    public WebElement Activities;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab:Search_ContactSearch-itemEl')]")
    public WebElement Contacts;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab:Search_PolicySearch-itemEl')]")
    public WebElement Policies;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab:Search_ProducerCodeSearch-itemEl')]")
    public WebElement Producer_Codes;

    @FindBy(xpath = "//div[contains(@id, 'TabBar:SearchTab:Search_SubmissionSearch')]")
    public WebElement submissions;


    public void clickAccounts() {
        clickSearchArrow();
        clickWhenVisible(Accounts);
    }


    public void clickActivities() {
        clickSearchArrow();
        clickWhenVisible(Activities);
    }


    public void clickContacts() {
        clickSearchArrow();
        clickWhenVisible(Contacts);
    }


    public void clickPolicies() {
        clickSearchArrow();
        clickWhenVisible(Policies);
    }


    public void clickProducerCodes() {
        clickSearchArrow();
        clickWhenVisible(Producer_Codes);
    }


    public void clickSubmissions() {
        clickSearchArrow();
        clickWhenClickable(submissions);

    }
}
