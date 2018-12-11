package repository.pc.contact;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class ContactSidebarPC extends BasePage {

    public ContactSidebarPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[contains(@id, 'ContactFile_Details')]")
    private WebElement link_Details;

    @FindBy(xpath = "//td[contains(@id, 'ContactFile_Accounts')]")
    private WebElement link_Accounts;

    @FindBy(xpath = "//td[contains(@id, 'ContactFile_Policies')]")
    private WebElement link_Policies;

    @FindBy(xpath = "//td[contains(@id, 'ContactFile_WorkOrders')]")
    private WebElement link_Transactions;

    @FindBy(xpath = "//td[contains(@id, 'ContactFile_Billing')]")
    private WebElement link_Billing;


    public void clickDetails() {
    	clickWhenClickable(link_Details);
    }


    public void clickAccounts() {
        clickWhenClickable(link_Accounts);
    }


    public void clickPolicies() {
        clickWhenClickable(link_Policies);
    }


    public void clickTransactions() {
        clickWhenClickable(link_Transactions);
    }


    public void clickBilling() {
        clickWhenClickable(link_Billing);
    }

}
