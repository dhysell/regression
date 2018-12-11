package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCAdministrationMenu extends BasePage {

	public BCAdministrationMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='Admin:MenuLinks-body']/div/table[starts-with(@id,'treeview-')]")
	public WebElement table_AdministrationSidebarMenu;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecurity;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_UserSearch']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecurityUsers;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Groups']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecurityGroups;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Roles']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecurityRoles;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_SecurityZones']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecuritySecurityZones;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_AuthorityLimitProfiles']")
	public WebElement link_AdministrationSidebarMenuUsersAndSecurityAuthorityLimitProfile;

	@FindBy(xpath = "//span[text()='Business Settings']")
	public WebElement link_AdministrationSidebarMenuBusinessSettings;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_BusinessSettings:BusinessSettings_ChargePatterns']")
	public WebElement link_AdministrationSidebarMenuChargePatterns;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_BusinessSettings:BusinessSettings_CommissionPlans']")
	public WebElement link_AdministrationSidebarMenuCommissionPlans;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_BusinessSettings:BusinessSettings_ActivityPatterns']")
	public WebElement link_AdministrationSidebarMenuActivityPatterns;

	@FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring']")
	public WebElement link_AdministrationSidebarMonitoring;

	// -------------------------------------------------------
	// Helper Methods for Above Elements - Trouble Tickets Page
	// -------------------------------------------------------

	public WebElement getAdministrationSidebarMenuTable() {
		waitUntilElementIsVisible(table_AdministrationSidebarMenu);
		return table_AdministrationSidebarMenu;
	}

	public void clickAdministrationMenuBusinessSettings() {
		clickWhenVisible(link_AdministrationSidebarMenuBusinessSettings);
	}

	public void clickAdminMenuUsersAndSecurity() {
		clickWhenVisible(link_AdministrationSidebarMenuUsersAndSecurity);
	}

	public void clickAdminMenuUsersAndSecurityUsers() {
		clickAdminMenuUsersAndSecurity();
		hoverOver(link_AdministrationSidebarMenuUsersAndSecurityUsers);
		link_AdministrationSidebarMenuUsersAndSecurityUsers.click();
	}

	public void clickAdminMenuUsersAndSecurityGroups() {
		clickWhenVisible(link_AdministrationSidebarMenuUsersAndSecurityGroups);
	}

	public void clickAdminMenuUsersAndSecurityRoles() {
		clickWhenVisible(link_AdministrationSidebarMenuUsersAndSecurityRoles);
	}

	public void clickAdminMenuUsersAndSecurityZones() {
		clickWhenVisible(link_AdministrationSidebarMenuUsersAndSecuritySecurityZones);
	}

	public void clickAdminMenuUsersAndSecurityAuthorityLimitProfile() {
		clickWhenVisible(link_AdministrationSidebarMenuUsersAndSecurityAuthorityLimitProfile);
	}

	public void clickAdministrationMenuChargePatterns() {
		clickWhenVisible(link_AdministrationSidebarMenuChargePatterns);
	}

	public void clickAdministrationMenuCommissionPlans() {
		clickWhenVisible(link_AdministrationSidebarMenuCommissionPlans);
	}

	public void clickAdministrationMenuActivityPatterns() {
		clickWhenVisible(link_AdministrationSidebarMenuActivityPatterns);
	}

	public void clickAdminMenuMonitoring() {
		clickWhenVisible(link_AdministrationSidebarMonitoring);
	}

}
