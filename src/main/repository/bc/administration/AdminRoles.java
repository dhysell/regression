package repository.bc.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class AdminRoles extends BasePage {

	public AdminRoles(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for Roles page
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='Roles:RolesScreen:RolesLV-body']/div/table[starts-with(@id,'gridview-')]")
	public WebElement table_AdminRoles;

	@FindBy(xpath = "//a[@id='Roles:RolesScreen:Roles_NewRoleButton']")
	public WebElement button_AdminRolesNewRole;

	@FindBy(xpath = "//a[@id='Roles:RolesScreen:Roles_DeleteButton']")
	public WebElement button_AdminRolesDelete;

	@FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
	private WebElement editBox_CommonPageBox;

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for New Role page
	// -----------------------------------------------
	@FindBy(xpath = "//input[@id='NewRole:RoleDetailScreen:RoleDetailDV:Name-inputEl']")
	public WebElement editbox_AdminNewRoleName;

	@FindBy(xpath = "//textarea[@id='NewRole:RoleDetailScreen:RoleDetailDV:Description-inputEl']")
	public WebElement editbox_AdminNewRoleDescription;

	// -------------------------------------------------------
	// Helper Methods for Above Elements for Roles page
	// -------------------------------------------------------
	//
	// public boolean checkIfElementExists(long checkTime) {
	// return super.checkIfElementExists(editBox_CommonPageBox, checkTime);
	// }

	public WebElement getAdminRolesTable() {
		waitUntilElementIsVisible(table_AdminRoles);
		return table_AdminRoles;
	}

	public void clickAdminRolesNewRoleButton() {
		clickWhenVisible(button_AdminRolesNewRole);
	}

	public void clickAdminRolesDeleteButton() {
		clickWhenVisible(button_AdminRolesDelete);
	}

	public void clickRolesTableCheckBoxBySubject(String subject) {
		table_AdminRoles.findElement(By.xpath("//a[text()='" + subject + "']/../../../td/div")).click();
	}

	public boolean multiplePagesExist(int checkTime) {
		return super.checkIfElementExists(editBox_CommonPageBox, checkTime);
	}

	public boolean checkIfCellExistsInTableByLinkText(String linkText) {
		return new TableUtils(getDriver()).checkIfLinkExistsInTable(table_AdminRoles, linkText);
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements for NEW Role page
	// -------------------------------------------------------

	public void setAdminRolesNewRoleName(String name) {
		waitUntilElementIsVisible(editbox_AdminNewRoleName);
		editbox_AdminNewRoleName.sendKeys(name);
	}

	public void setAdminRolesNewRoleDescription(String description) {
		waitUntilElementIsVisible(editbox_AdminNewRoleDescription);
		editbox_AdminNewRoleDescription.sendKeys(description);
	}

}
