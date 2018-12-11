package repository.pc.administration;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.UsersRole;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class RolesPC extends BasePage {

	private TableUtils tableUtils;
	public RolesPC(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	@FindBy(xpath = "//div[@id='Roles:RolesScreen:RolesLV']")
	private WebElement tableWrapper_Roles;

	@FindBy(xpath = "//span[contains(@id,'RoleDetailPage:RoleDetailScreen:RoleDetail_UsersCardTab-btnEl')]")
	private WebElement link_RoleUsers;

	@FindBy(xpath = "//div[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV')]")
	private WebElement tableDiv_UsersTable;


	public void clickRole(String roleName) throws Exception {
		boolean found = false;
		if (tableUtils.hasMultiplePages(tableWrapper_Roles)) {
			int numPages = tableUtils.getNumberOfTablePages(tableWrapper_Roles);
			int pageOn = 1;
			while (!found && pageOn <= numPages) {
				found = tableUtils.checkIfLinkExistsInTable(tableWrapper_Roles, roleName);
				if (found) {
					break;
				} else {
					tableUtils.clickNextPageButton(tableWrapper_Roles);
					pageOn++;
				}
			}
		} else {
			found = tableUtils.checkIfLinkExistsInTable(tableWrapper_Roles, roleName);
		}

		if (!found) {
			throw new Exception("ERROR: Role with the name '" + roleName + "' does not exist in the table");
		} else {
			tableUtils.clickLinkInTable(tableWrapper_Roles, roleName);
		}
	}

	public List<UsersRole> getRoleTypeDescription() {
		List<UsersRole> returnList = new ArrayList<UsersRole>();
		int numPages = tableUtils.getNumberOfTablePages(tableWrapper_Roles);
		int pageOn = 1;
		while (pageOn <= numPages) {
			List<WebElement> roles = tableWrapper_Roles.findElements(By.xpath("./div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/div/table/tbody/child::tr"));
			for(WebElement foo : roles) {
				UsersRole newRole = new UsersRole();
				newRole.setName(foo.findElement(By.xpath(".//child::td[2]/div/a")).getText());
				newRole.setType(foo.findElement(By.xpath(".//child::td[3]/div")).getText());
				newRole.setDescription(foo.findElement(By.xpath(".//child::td[4]/div")).getText());
				returnList.add(newRole);
			}
			tableUtils.clickNextPageButton(tableWrapper_Roles);
			pageOn++;
		}
		return returnList;
	}


	public void clickRoleEdit() {
		super.clickEdit();
	}

	@FindBy(xpath = "//div[@id='RoleDetailPage:RoleDetailScreen:RoleDetailDV:RolePrivilegesLV']")
	private WebElement tableWrapper_RoleEditPermissions;


	public boolean addNewPermission(String permissionName, boolean createNewRow) {
		if (createNewRow) {
			super.clickAdd();
		}

		int toTest = tableUtils.getNextAvailableLineInTable(tableWrapper_RoleEditPermissions);

		boolean found = tableUtils.selectValueForSelectInTable(tableWrapper_RoleEditPermissions, toTest, "Permission", permissionName);

		return found;
	}


	public void clickRoleUpdate() {
		super.clickUpdate();
	}

	@FindBy(xpath = "//a[@id='RoleDetailPage:RoleDetailPage_UpLink']")
	private WebElement button_UpToRoles;


	public void clickUpToRoles() {
		clickWhenClickable(button_UpToRoles);
	}


	public void clickRoleUsers() {
		clickWhenClickable(link_RoleUsers);
	}


	public ArrayList<String> getUsersNames() {
		return tableUtils.getAllCellTextFromSpecificColumn(tableDiv_UsersTable, "Name");
	}

}
