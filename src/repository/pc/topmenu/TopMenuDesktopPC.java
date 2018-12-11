package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuDesktopPC extends TopMenuPC {

    public TopMenuDesktopPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopActivities-itemEl')]")
    public WebElement My_Activities;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopAccounts-itemEl')]")
    public WebElement My_Accounts;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopSubmissions-itemEl')]")
    public WebElement My_Submissions;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopRenewals-itemEl')]")
    public WebElement My_Renewals;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopOtherWorkOrders-itemEl')]")
    public WebElement My_Other_Policy_Transactions;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopAssignableQueues-itemEl')]")
    public WebElement My_Queues;

    @FindBy(xpath = "//a[contains(@id, '_DesktopProofOfMail-itemEl')]")
    public WebElement link_ProofOfMail;


    public void clickMyActivities() {
        clickDesktopArrow();
        clickWhenVisible(My_Activities);
    }


    public void clickMyAcccounts() {
        clickDesktopArrow();
        clickWhenVisible(My_Accounts);
    }


    public void clickMySubmissions() {
        clickDesktopArrow();
        clickWhenVisible(My_Submissions);
    }


    public void clickMyRenewals() {
        clickDesktopArrow();
        clickWhenVisible(My_Renewals);
    }


    public void clickMyOtherPolicyTransactions() {
        clickDesktopArrow();
        clickWhenVisible(My_Other_Policy_Transactions);
    }


    public void clickMyQueues() {
        clickDesktopArrow();
        clickWhenVisible(My_Queues);
    }


    public void clickMyOtherWorkOrders() {
        // TODO Auto-generated method stub

    }


    public void clickProofOfMail() {
        clickDesktopArrow();
        clickWhenVisible(link_ProofOfMail);
    }


    public void clickBulkAgentChange() {
        // TODO Auto-generated method stub

    }
}
