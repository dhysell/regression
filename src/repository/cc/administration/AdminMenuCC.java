package repository.cc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class AdminMenuCC extends BasePage {

    private  WebDriver driver;

    public AdminMenuCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring']")
    public WebElement link_AdminMenuMonitoring;

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring:Monitoring_MessagingDestinationControlList']")
    public WebElement link_AdminMenuMonitoringMessageQueues;

    @FindBy(css = "td[id*='Admin_UsersAndSecurity']")
    private WebElement link_UserAndSecurity;

    // Helpers
    public void clickAdminMenuMonitoryingMessageQueues() {
        clickWhenClickable(link_AdminMenuMonitoring);
        clickWhenClickable(link_AdminMenuMonitoringMessageQueues);
    }

    public repository.cc.administration.UserSearchCC clickUsersAndSecurity() {
        clickWhenClickable(link_UserAndSecurity);
        return new UserSearchCC(this.driver);
    }
}
