package repository.bc.administration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.UserRole;
import repository.gw.helpers.TableUtils;

public class AdminUserDetails extends BasePage {

	private TableUtils tableUtils;
	
	public AdminUserDetails(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='UserDetailPage:UserDetailScreen:UserDetailDV:UserRolesLV']")
	public WebElement table_AdminUserRoles;

	@FindBy(xpath = "//span[@id='UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnInnerEl']")
	public WebElement tab_AdminUserDetailsBasics;

	@FindBy(xpath = "//span[@id='UserDetailPage:UserDetailScreen:UserDetail_AuthorityLimitsCardTab-btnInnerEl']")
	public WebElement tab_AdminUserDetailsAuthorityLimits;

	@FindBy(xpath = "//span[@id='UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnInnerEl']")
	public WebElement tab_AdminUserDetailsProfile;

	@FindBy(xpath = "//input[contains(@id,':WorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
	public WebElement editbox_AdminUserDetailsProfileTabWorkPhone;

	// -------------------------------------------------------
	// Helper Methods for Above Elements for Roles page
	// -------------------------------------------------------
	//
	// public boolean checkIfElementExists(long checkTime) {
	// return super.checkIfElementExists(editBox_CommonPageBox, checkTime);
	// }

	public void selectNewUserRole(UserRole roleToSelect) {
		tableUtils.selectValueForSelectInTable(table_AdminUserRoles, tableUtils.getNextAvailableLineInTable(table_AdminUserRoles, "Description"), "Name", roleToSelect.getValue());
	}

	public void clickProfileTab() {
		clickWhenVisible(tab_AdminUserDetailsProfile);
	}

	public void setProfileWorkPhone(String phoneNumber) {
		clickProfileTab();
		editbox_AdminUserDetailsProfileTabWorkPhone.sendKeys(Keys.CONTROL + "a");
		
		editbox_AdminUserDetailsProfileTabWorkPhone.sendKeys(phoneNumber);
	}

	public void addNewUserRole(UserRole newRoleToAdd) {
		clickEdit();
		
		clickAdd();
		
		selectNewUserRole(UserRole.PCViewOnly);
		
		clickProfileTab();
		
		setProfileWorkPhone("5555555555");
		
		clickUpdate();
	}

	public void removeUserRole(UserRole newRoleToRemove) {
		clickEdit();
		
		tableUtils.setCheckboxInTable(table_AdminUserRoles, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdminUserRoles, "Name", newRoleToRemove.getValue())), true);
		
		clickRemove();
		
		clickUpdate();
	}

}
