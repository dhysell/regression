package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

public class AdminUsers extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
	public AdminUsers(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver = driver;
		this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='UserSearch:UserSearchScreen:UserSearchResultsLV']")
	public WebElement table_AdminUsers;

	@FindBy(xpath = "//input[@id='UserSearch:UserSearchScreen:UserSearchDV:UsernameCriterion-inputEl']")
	public WebElement editbox_AdminUsersUserName;

	@FindBy(xpath = "//input[@id='UserSearch:UserSearchScreen:UserSearchDV:GroupNameCriterion-inputEl']")
	public WebElement editbox_AdminUsersGroupName;

	public Guidewire8Select comboBox_AdminUserRoleFilter() throws Exception {
		return new Guidewire8Select(driver, "//table[contains(@id,':UserSearchDV:Role-triggerWrap')]");
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements for Roles page
	// -------------------------------------------------------
	//
	// public boolean checkIfElementExists(long checkTime) {
	// return super.checkIfElementExists(editBox_CommonPageBox, checkTime);
	// }

	public void setAdminUsersRole(String role) throws Exception {
		comboBox_AdminUserRoleFilter().selectByVisibleText(role);
	}

	public void setAdminUsersName(String userName) {
		editbox_AdminUsersUserName.sendKeys(userName);
	}

	public int getAdminUsersTableRowCount() {
		return tableUtils.getRowCount(table_AdminUsers);
	}

	public String getAdminUsersTableUsername(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AdminUsers, rowNumber, "User name");
	}

	public void clickAdminUsersTableDisplayName(String userName) {
		tableUtils.clickLinkInTableByRowAndColumnName(table_AdminUsers, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdminUsers, "User name", userName)), "Display Name");
	}

	public void clickAdminUsersTableDisplayName(int rowNumber) {
		tableUtils.clickLinkInTableByRowAndColumnName(table_AdminUsers, rowNumber, "Display Name");
	}

	public String getRoleByRowNumber(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AdminUsers, rowNumber, "Roles");
	}

}
