package repository.ab.administration;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.gw.login.Login;

public class AdminMenu extends BasePage {

     private WebDriver driver;
    public AdminMenu(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[contains(@id,'Admin:MenuLinks:Admin_UsersAndSecurity')]")
    private WebElement link_AdminMenuUsersAndSecurity;

    @FindBy(xpath = "//td[contains(@id, 'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_AdminUserSearchPage')]")
    private WebElement link_AdminMenuUsersAndSecurityUsers;

    @FindBy(xpath = "//td[contains(@id, 'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_AdminGroupSearchPage')]")
    private WebElement link_AdminMenuUsersAndSecurityGroups;

    @FindBy(xpath = "//td[contains(@id, 'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Roles')]")
    private WebElement link_AdminMenuUsersAndSecurityRoles;

    @FindBy(xpath = "//td[contains(@id, 'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Regions')]")
    private WebElement link_AdminMenuUsersAndSecurityRegions;

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring']")
    private WebElement link_AdminMenuMonitoring;

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring:Monitoring_MessagingDestinationControlList']")
    private WebElement link_AdminMenuMonitoringMessageQueues;

    public void clickAdministration() {
        repository.ab.topmenu.TopMenuAB topMenu = new TopMenuAB(driver);
        topMenu.clickAdministrationTab();
    }

    public void clickAdminMenuMonitoringMessageQueues() {
        clickWhenClickable(link_AdminMenuMonitoring);
        clickWhenClickable(link_AdminMenuMonitoringMessageQueues);
    }

    public void getUsersPage() {
        clickWhenClickable(link_AdminMenuUsersAndSecurity);
        clickWhenClickable(link_AdminMenuUsersAndSecurityUsers);
    }

    public void getToRolesPage() {
        clickAdministration();
        clickWhenClickable(link_AdminMenuUsersAndSecurity);
        clickWhenClickable(link_AdminMenuUsersAndSecurityRoles);
    }

    public void getToEventMessages(AbUsers user) {
        Login login = new Login(getDriver());
        login.login(user.getUserName(), user.getUserPassword());
        clickAdministration();
        clickAdminMenuMonitoringMessageQueues();
    }


}
