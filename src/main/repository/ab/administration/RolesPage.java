package repository.ab.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbRole;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import java.util.ArrayList;

public class RolesPage extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public RolesPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id,'Roles:RolesScreen:RolesLV') and not(contains(@id, '-body'))]")
    private WebElement div_RolesTable;

    @FindBy(xpath = "//div[contains(@id,'RoleDetailPopup:RoleDetailScreen:RoleDetailDV:RolePrivilegesLV')]")
    private WebElement div_PermissionTable;

    @FindBy(xpath = "//a[contains(@id,'RoleDetailPopup:__crumb__') and contains(., 'Roles')]")
    private WebElement link_ReturnToRoles;

    @FindBy(xpath = "//span[contains(@id,'RoleDetailPopup:RoleDetailScreen:RoleDetail_UserCardTab-btnEl')]")
    private WebElement link_Users;

    @FindBy(xpath = "//span[contains(@id,'RoleDetailPopup:RoleDetailScreen:RoleDetail_RolesCardTab-btnEl')]")
    private WebElement link_Basics;

    @FindBy(xpath = "//div[contains(@id,'RoleDetailPopup:RoleDetailScreen:RoleUsersLV')]")
    private WebElement text_UserTable;
    
    private WebElement link_RolesPageRoles(String role) {
    	return find(By.xpath("//a[contains(@id,':Name') and contains(., '"+ role +"')]"));
    }


    @FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
    private WebElement editBox_PageBox;


    private void clickReturnToRoles() {
        
        clickWhenClickable(link_ReturnToRoles);
        
    }

    private void clickUsers() {
        
        clickWhenClickable(link_Users);
        

    }

    private void clickBasics() {
        
        clickWhenClickable(link_Basics);
        
    }
    
    private void clickRole(String role) {
    	clickWhenClickable(link_RolesPageRoles(role));
    	waitUntilElementIsNotVisible(link_RolesPageRoles(role), 2000);
        }

    public AbRole scrapeRole() throws Exception {
        
        AbRole newRole = new AbRole();
        String roleName;
        String description;
        //change back to 1 after done debugging.
        for (int i = 12; i <= tableUtils.getRowCount(div_RolesTable); i++) {
            roleName = tableUtils.getCellTextInTableByRowAndColumnName(div_RolesTable, i, "Name");
            description = tableUtils.getCellTextInTableByRowAndColumnName(div_RolesTable, i, "Description");
            newRole.setRole(roleName);
            newRole.setRoleDescription(description);
            System.out.println("The current role is: " + newRole.getRole());
            System.out.println("The current role description is: " + newRole.getRoleDescription());
            tableUtils.clickLinkInTableByRowAndColumnName(div_RolesTable, i, "Name");
            ArrayList<AbPermissions> perms = scrapePermissions(newRole);
            ArrayList<AbUsers> users = scrapeUsers(newRole);
            if (perms.size() > users.size()) {
                for (int lcv = 0; lcv < perms.size(); lcv++) {
                    if (lcv >= users.size()) {
                        AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(lcv), newRole, null);
                    } else {
                        AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(lcv), newRole, users.get(lcv));
                    }
                }
            } else {
                for (int x = 0; x < users.size(); x++) {
                    if (x >= perms.size()) {
                        AbUserHelper.addPermRoleUserIfNotInDataBase(null, newRole, users.get(x));
                    } else {
                        AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(x), newRole, users.get(x));
                    }
                }
            }
        }
        return newRole;
    }

    private ArrayList<AbPermissions> scrapePermissions(AbRole role) {
        int permissionPageCount = tableUtils.getNumberOfTablePages(div_PermissionTable);
        int currentPermissionCount = tableUtils.getRowCount(div_PermissionTable);
        AbPermissions currentPerm = new AbPermissions();
        String perm;
        String permCode;
        String permDescription;
        ArrayList<AbPermissions> permissions = new ArrayList<AbPermissions>();
        boolean permCodeFound = false;
        
        for (int lcv = 1; lcv <= permissionPageCount; lcv++) {
            if (checkIfElementExists("//input[contains(@id,':_ListPaging-inputEl')]", 2000)) {
                clickWhenClickable(editBox_PageBox);
                setText(editBox_PageBox, String.valueOf(lcv));
            }
            
            currentPermissionCount = tableUtils.getRowCount(div_PermissionTable);

            for (int i = 1; i <= currentPermissionCount; i++) {

                perm = tableUtils.getCellTextInTableByRowAndColumnName(div_PermissionTable, i, "Permission");
                permCode = tableUtils.getCellTextInTableByRowAndColumnName(div_PermissionTable, i, "Code");
                permDescription = tableUtils.getCellTextInTableByRowAndColumnName(div_PermissionTable, i, "Description");

                currentPerm.setPermission(perm);
                currentPerm.setPermissionCode(permCode);
                currentPerm.setPermissionDescription(permDescription);

                System.out.println("The current Permission is: " + perm);
                System.out.println("The current permission code is: " + permCode);
                System.out.println("The current permission description is: " + permDescription + "\r\n");

                permissions.add(new AbPermissions(perm, permCode, permDescription));
            }
        }
        return permissions;
    }

    public ArrayList<AbUsers> scrapeUsers(AbRole role) {
        ArrayList<AbRole> roleSet = new ArrayList<AbRole>();
        roleSet.add(role);
        ArrayList<AbUsers> users = new ArrayList<AbUsers>();
        clickUsers();
        ArrayList<String> usersNames = tableUtils.getAllCellTextFromSpecificColumn(text_UserTable, "User Name");
        for (String name : usersNames) {
            String[] firstLastName = name.split(" ");
            AdminMenu admin = new AdminMenu(driver);
            admin.getUsersPage();
            repository.ab.administration.UsersPage usersPage = new UsersPage(driver);
            users.add(usersPage.getSearchResultsByName(firstLastName[0], firstLastName[firstLastName.length - 1]));
        }
        AdminMenu admin = new AdminMenu(driver);
        admin.getToRolesPage();
        return users;
    }

    public ArrayList<AbUsers> loginAndScrapeUsersOfRole(String role) throws Exception {
        AbUsers admin = AbUserHelper.getRandomDeptUser("Admin");
        new Login(driver).login(admin.getUserName(), admin.getUserPassword());
        repository.ab.topmenu.TopMenuAB topMenu = new repository.ab.topmenu.TopMenuAB(driver);
        topMenu.clickAdministrationTab();
        AdminMenu adminMenu = new AdminMenu(driver);
        adminMenu.getToRolesPage();
        
        waitUntilElementIsVisible(div_RolesTable);
        int roleRow = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(div_RolesTable, "Name", role));
        tableUtils.clickLinkInTableByRowAndColumnName(div_RolesTable, roleRow, "Name");
//		clickUsers();
        AbRole newRole = new AbRole();
        newRole.setRole(role);
        return scrapeUsers(newRole);
    }
    
    public AbRole updateRole(String role) throws Exception {
    	new Login(driver).login("su", "gw");
    	repository.ab.topmenu.TopMenuAB topMenu = new TopMenuAB(driver);
		topMenu.clickAdministrationTab();
		AdminMenu adminMenu = new AdminMenu(driver);
		adminMenu.getToRolesPage();
		RolesPage rolesPage = new RolesPage(driver);
		AbRole newRole = new AbRole();
		waitUntilElementIsVisible(By.xpath("//a[contains(., '"+role+"')]"));
    	int i = tableUtils.getRowNumberInTableByText(div_RolesTable, role);
    	String roleName = tableUtils.getCellTextInTableByRowAndColumnName(div_RolesTable, tableUtils.getRowNumberInTableByText(div_RolesTable, role), "Name");
        String description = tableUtils.getCellTextInTableByRowAndColumnName(div_RolesTable, tableUtils.getRowNumberInTableByText(div_RolesTable, role), "Description");
        newRole.setRole(roleName);
        newRole.setRoleDescription(description);
        System.out.println("The current role is: " + newRole.getRole());
        System.out.println("The current role description is: " + newRole.getRoleDescription());
//        AbPermissionRoleHelper.removeRolePermissions(newRole.getRole());
 //       AbUserRoleHelper.removeUserRoles(newRole.getRole());
        tableUtils.clickLinkInTableByRowAndColumnName(div_RolesTable, i, "Name");
        ArrayList<AbPermissions> perms = scrapePermissions(newRole);
        ArrayList<AbUsers> users = scrapeUsers(newRole);
//        AbPermissionRoleHelper.removeRolePermissions(newRole.getRole());
//        AbUserRoleHelper.removeUserRoles(newRole.getRole());
        if(perms.size()>users.size()) {
        	for(int lcv = 0; lcv<perms.size(); lcv++) {
         		if(lcv>=users.size()) {
         			AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(lcv), newRole, null);
         		} else {
         			AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(lcv), newRole, users.get(lcv));
         		}		
         	}
         } else {
         	for(int x = 0; x < users.size(); x++) {
         		if(x>= perms.size()) {
         			AbUserHelper.addPermRoleUserIfNotInDataBase(null, newRole, users.get(x));
         		}else {
         			AbUserHelper.addPermRoleUserIfNotInDataBase(perms.get(x), newRole, users.get(x));
         		}
         	}
         }
         return newRole;
    }
}
