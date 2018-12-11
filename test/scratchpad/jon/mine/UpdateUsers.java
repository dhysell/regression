package scratchpad.jon.mine;
/*
 * Jon larsen 5/14/2015
 * this Class is to update the database tables for SA, PA, and CSR data
 * It get needed information from role searches and user info searches
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

//BEFORE RUNNING TEST!
/*
 * need to manually delete all the rows in the database Master tables
 * Otherwise this will just add the users to the table
 *
 */


import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.PcRolesBak;
import persistence.globaldatarepo.entities.PcRolesPermissionsBak;
import persistence.globaldatarepo.entities.PcuserUserRolesBak;
import persistence.globaldatarepo.entities.PcuserUsersBak;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.LienHolderHelper;
import persistence.globaldatarepo.helpers.PAsHelper;
import persistence.globaldatarepo.helpers.PcProcessorHelper;
import persistence.globaldatarepo.helpers.SAsHelper;
import persistence.globaldatarepo.helpers.TownshipRangeHelper;
import persistence.helpers.StringsUtils;

@SuppressWarnings("unused")
public class UpdateUsers extends BaseTest {

    private List<String> userList = new ArrayList<String>();
    private List<String> leinHolderList = new ArrayList<String>();
    @SuppressWarnings("serial")
    private List<String> alphabet = new ArrayList<String>() {{
        this.add("a");
        this.add("b");
        this.add("c");
        this.add("d");
        this.add("e");
        this.add("f");
        this.add("g");
        this.add("h");
        this.add("i");
        this.add("j");
        this.add("k");
        this.add("l");
        this.add("m");
        this.add("n");
        this.add("o");
        this.add("p");
        this.add("q");
        this.add("r");
        this.add("s");
        this.add("t");
        this.add("u");
        this.add("v");
        this.add("w");
        this.add("x");
        this.add("y");
        this.add("z");
    }};

    private WebDriver driver;

    @Test(enabled = false)
    public void getAllRoles() throws Exception {
        List<String> roleList = getListOfRoles();

        for (String roleitem : roleList) {
            PcRolesBak.createNewRole(roleitem);
        }

    }


    private List<String> getListOfRoles() throws Exception {
        List<String> roleList = new ArrayList<String>();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.login("su", "gw");

        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickRoles();
        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());

        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            List<WebElement> rolesList = driver.findElements(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a"));
            for (WebElement role : rolesList) {
                roleList.add(role.getText());
            }
            nextPage();
        }

        return roleList;
    }


    @Test(enabled = false)
    public void getPermissionsForRoles() throws Exception {
        List<String> roleList = getListOfRoles();

        for (String role : roleList) {

            String permission = "";
            String code = "";
            String description = "";

            //get role permissions
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickRoles();
            WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
            WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
            String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());

            while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
                if (driver.findElements(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).size() > 0) {
                    driver.findElement(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).click();
                    break;
                } else {
                    nextPage();
                }
            }

            if (!driver.findElements(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]")).isEmpty()) {
                pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
                maxpage = maxPages.getText().replace("of ", "");

                while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {

                    pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                    List<WebElement> permissionsList = driver.findElements(By.xpath("//div[contains(@id, ':RolePrivilegesLV-body')]/div/table/tbody/child::tr"));

                    for (WebElement permission1 : permissionsList) {
                        permission = permission1.findElement(By.xpath(".//child::td[2]/div")).getText();

                        PcRolesPermissionsBak.createNewRolePermission(role, permission);
                    }

                    nextPage();
                }//end while
            } else {
                List<WebElement> permissionsList = driver.findElements(By.xpath("//div[contains(@id, ':RolePrivilegesLV-body')]/div/table/tbody/child::tr"));
                if (!permissionsList.isEmpty()) {
                    for (WebElement permission1 : permissionsList) {
                        permission = permission1.findElement(By.xpath(".//child::td[2]/div")).getText();

                        PcRolesPermissionsBak.createNewRolePermission(role, permission);
                    }
                }
            }//end else


        }
    }


    @Test(enabled = false)
    public void getLeinHoldersFromBC() throws Exception {

        userList = new ArrayList<String>();

        String userName = null;
        String associationName = null;

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("sbrunson", "gw");

        getLeinHolders();
    }


    private void getLeinHolders() throws Exception {
        BCTopMenu menuBC = new BCTopMenu(driver);
        menuBC.clickAccountTab();

        List<WebElement> foo = driver.findElements(By.xpath("//input[contains(@id, ':AccountNumberCriterion-inputEl')]"));

        WebElement accountNumber = driver.findElement(By.xpath("//input[contains(@id, ':AccountNumberCriterion-inputEl')]"));
        menuBC.setText(accountNumber, "98");
        driver.findElement(By.xpath("//a[contains(@id, ':SearchLinksInputSet:Search')]")).click();

        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());


        pageNumber = driver.findElement(By.xpath("//input[contains(@id, '_ListPaging-inputEl')]"));
        maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        maxpage = maxPages.getText().replace("of ", "");

        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
            List<WebElement> leinHolderName = driver.findElements(By.xpath("//div[contains(@id, 'AccountSearchResultsLV-body')]/descendant::tbody/child::tr/child::td[4]/div"));
            for (WebElement element : leinHolderName) {
                leinHolderList.add(element.getText());
            }
            nextPage();
        }

        //update LH table
        for (String lh : leinHolderList) {
            driver.findElement(By.xpath("//a[contains(@id, 'SearchLinksInputSet:Reset')]")).click();
            WebElement lhName = driver.findElement(By.xpath("//input[contains(@id, ':AccountNameCriterion-inputEl')]"));
            menuBC.setText(lhName, lh);
            driver.findElement(By.xpath("//a[contains(@id, ':SearchLinksInputSet:Search')]")).click();
            //get lh number
            List<WebElement> numbers = driver.findElements(By.xpath("//div[contains(@id, ':AccountSearchResultsLV-body')]/descendant::tbody/child::tr/child::td[3]/div/a"));
            for (WebElement number : numbers) {
                LienHolderHelper.saveLeinHolderInfo(number.getText(), lh);
            }

        }


    }

    @Test(enabled = false)
    public void getPAUsers() throws Exception {

        userList = new ArrayList<String>();

        String userName = null;
        String associationName = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("Production Assistant");

        for (String user : userList) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (userSearch(user)) {
                userName = getUserName();
                associationName = getAssociation();
                String[] split = user.split(" ");
                System.out.println("Adding: " + userName + " to the database");
                PAsHelper.createNewPA(user, userName, split[0], split[split.length - 1], associationName);

            }
        }
    }

    @Test(enabled = false)
    public void getSAUsers() throws Exception {

        userList = new ArrayList<String>();
        String userName = null;
        String associationName = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("Service Associate");

        for (String user : userList) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (userSearch(user) && isActive()) {
                userName = getUserName();
                associationName = getAssociation();
                String[] split = user.split(" ");
                System.out.println("Adding: " + userName + " to the database");
                SAsHelper.createNewSA(user, userName, split[0], split[split.length - 1], associationName);

            }
        }
    }

    @Test(enabled = false)
    public void getCSRUsers() throws Exception {

        userList = new ArrayList<String>();
        String userName = null;
        String associationName = null;
        String region = null;
        String county = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV2");
        driver = buildDriver(cf);


        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("CSR");

        for (String user : userList) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (user.contains("Janeth Ortiz") || user.contains("Barbara McClure") || user.contains("Michelle Coffin") || user.contains("Betty Inskeep") || user.contains("Christa Bankhead") || user.contains("CSR Temp")) {
                continue;
            }


            if (userSearch(user) && isActive()) {
                userName = getUserName();
                //associationName = getAgency();
                region = getRegion();
                county = getUserCounty();
                String[] split = user.split(" ");
                System.out.println("Adding: " + userName + " to the database");
                CSRsHelper.createNewCSR(split[0], split[split.length - 1], userName, associationName, region, "gw", county);

            }
        }
    }

    @Test(enabled = false)
    public void updateAgentCounty() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.login("su", "gw");

        List<Agents> allAgents = AgentsHelper.getAllAgents();

        for (Agents user : allAgents) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (userSearch(user.getAgentFullName())) {
                String agentCounty = getAgentCounty(user).substring(6);
                agentCounty = agentCounty.substring(0, agentCounty.length() - 7);
                System.out.println("Adding: " + agentCounty + " to the Agents database table");
                AgentsHelper.updateAgentCounty(user, agentCounty.trim());

            }
        }
    }


    @Test(enabled = false)
    public void getUnderwriterUsers2() throws Exception {

        String uwFirstName = null;
        String uwLastName = null;
        String uwUserName = null;
        List<String> uwAccess = new ArrayList<String>();
        List<String> uwRole = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("Underwriter");

        for (String user : userList) {
            //excluded users
            switch (user) {
                case "":
                    continue;
            }
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            if (userSearch(user) && isActive()) {
                uwFirstName = driver.findElement(By.xpath("//div[contains(@id, ':FirstName-inputEl')]")).getText();
                uwLastName = driver.findElement(By.xpath("//div[contains(@id, ':LastName-inputEl')]")).getText();
                if (!isUnderwriter(user)) {
                    continue;
                }
                uwUserName = getUserName();
                uwAccess = getAccess();
                uwRole = getRoles();

                String line = "Commercial";
                for (String access : uwAccess) {
                    if (access.contains("Personal")) {
                        line = "Personal";
                        break;
                    }
                }

                String title = "Underwriter";
                for (String role : uwRole) {
                    if (role.contains("Underwriting Supervisor")) {
                        title = "Underwriting Supervisor";
                        break;
                    }
                }
                //				UnderwritersHelper.createNewUnderwriterUser(uwFirstName, uwLastName, uwUserName, "gw", line, title, null);

            }
        }

    }


    private boolean isUnderwriter(String user) {

        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));
        String jobTitle = driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:Title-inputEl')]")).getText();
        return jobTitle.contains("Underwrit");
    }

    @Test(enabled = false)
    public void getUnderwriterUsers() throws Exception {

        String uwFirstName = null;
        String uwLastName = null;
        String uwUserName = null;
        List<String> uwAccess = new ArrayList<String>();
        List<String> uwRole = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("Underwriter");

        for (String user : userList) {
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (userSearch(user) && isActive()) {
                uwFirstName = driver.findElement(By.xpath("//div[contains(@id, ':FirstName-inputEl')]")).getText();
                uwLastName = driver.findElement(By.xpath("//div[contains(@id, ':LastName-inputEl')]")).getText();
                uwUserName = getUserName();
                uwAccess = getAccess();
                uwRole = getRoles();

                for (String access : uwAccess) {
                    for (String role : uwRole) {
                        switch (access) {
                            case "Gliege Agency":
                            case "Commercial Coders":
                            case "Commercial Processors":
                            case "Commercial Proofreaders":
                            case "Underwriting Supervisors":
                            case "Underwriting Assistants":
                            case "Document Assignment":
                            case "Special Handling":
                            case "Biggs Agency":
                            case "Schmitt Agency":
                            case "Reilly Agency":
                            case "Zemaitis Agency":
                            case "Watson Agency":
                            case "Rae Agency":
                            case "CSRs":
                            case "CSR":
                            case "Boise Area Coders":
                            case "Northern Idaho Area Coders":
                            case "Palmer Agency":
                            case "Personal Lines Processing Clerks":
                            case "Personal Lines Proofreaders":
                            case "Pocatello Area Coders":
                            case "Commercial Accounts Receivable":
                            case "Proof of Mail":
                            case "Proofreader":
                            case "Processor":
                            case "IS Support":
                            case "BC Screens":
                                break;
                            default:
                                switch (access) {
                                    case "Boise Area New Business Underwriters":
                                    case "Boise Area Underwriters":
                                    case "Northern Idaho Area Coders":
                                    case "Northern Idaho Area New Business Underwriters":
                                    case "Northern Idaho Area Underwriters":
                                    case "Northern Idaho Area Underwriting Assistants":
                                    case "Personal Lines Audit Underwriters":
                                    case "Personal Lines New Business Underwriters":
                                    case "Personal Lines Transition Renewal":
                                    case "Personal Lines Underwriting":
                                    case "Personal Lines Underwriting Assistants":
                                    case "Personal Lines Underwriting Supervisors":
                                    case "Pocatello Area":
                                    case "Pocatello Area New Business Underwriters":
                                    case "Pocatello Area Underwriters":
                                    case "Pocatello Area Underwriting Assistants":
//								UnderwritersHelper.createNewUnderwriterUser(uwFirstName, uwLastName, uwUserName, "gw", "Personal", role, access);
                                        break;
                                    default:
//								UnderwritersHelper.createNewUnderwriterUser(uwFirstName, uwLastName, uwUserName, "gw", "Commercial", role, access);
                                        break;
                                }
                                break;
                        }

                    }
                }

            }
        }
    }

    @Test(enabled = false)
    public void updateProcessors() throws Exception {
        userList = new ArrayList<String>();
        String id = null;
        String userName = null;
        String processorFirstName = null;
        String processorLastName = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");


        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickUsersAndSecurityMenu();
        Guidewire8Select selectProcessor = new Guidewire8Select(driver, "//table[contains(@id, ':UserSearchDV:UserType-triggerWrap')]");
        selectProcessor.selectByVisibleText("Processing");
        driver.findElement(By.xpath("//a[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search')]")).click();
        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());
        int rowCount = 0;
        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            driver.findElement(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV')]"));
            rowCount = new TableUtils(driver).getRowCount(driver.findElement(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV')]")));
            for (int i = 0; i < rowCount; i++) {
                processorFirstName = driver.findElement(By.xpath("//div[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/div/descendant::tbody/tr[contains(@data-recordindex, '" + i + "')]/td[3]/div/a")).getText();
                userName = driver.findElement(By.xpath("//div[contains(@id,'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/div/descendant::tbody/tr[contains(@data-recordindex, '" + i + "')]/td[4]/div")).getText();
                String[] parsedName = separateName(processorFirstName);

                PcProcessorHelper.createNewProcessor(userName, parsedName[0], parsedName[parsedName.length - 1], "gw");
            }
            nextPage();
        }


    }

    public String[] separateName(String fullName) {
        return fullName.split(" ");
    }


    @Test(enabled = false)
    public void updateAllUsers() throws Exception {
        List<PcuserUsersBak> allUsers = PcuserUsersBak.getAllUsers();
        String firstName = null;
        String middleName = null;
        String lastName = null;
        String suffix = null;
        String alternateName = null;
        String jobTitle = null;
        String active = null;
        String userType = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.login("su", "gw");

        for (PcuserUsersBak user : allUsers) {
            firstName = null;
            middleName = null;
            lastName = null;
            suffix = null;
            alternateName = null;
            jobTitle = null;
            active = null;
            userType = null;

            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            userSearchByUserName(user);
            firstName = driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).getText();
            middleName = driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")).getText();
            lastName = driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")).getText();
            suffix = driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:Suffix-inputEl')]")).getText();
            alternateName = driver.findElement(By.xpath("//div[contains(@id, ':UserDetailInputSet:AlternateName-inputEl')]")).getText();
            active = driver.findElement(By.xpath("//div[contains(@id, ':UserDetailInputSet:Active-inputEl')]")).getText();
            userType = driver.findElement(By.xpath("//div[contains(@id, ':UserDetailScreen:UserDetailDV:UserType-inputEl')]")).getText();

            new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, ':UserDetail_ProfileCardTab-btnEl')]")));
            jobTitle = driver.findElement(By.xpath("//div[contains(@id, 'UserProfileDV:Title-inputEl')]")).getText();

            firstName = (firstName.equals("")) ? null : firstName;
            middleName = (middleName.equals("")) ? null : middleName;
            lastName = (lastName.equals("")) ? null : lastName;
            suffix = (suffix.equals("")) ? null : suffix;
            alternateName = (alternateName.equals("")) ? null : alternateName;
            jobTitle = (jobTitle.equals("")) ? null : jobTitle;
            active = (active.equals("")) ? null : active;
            userType = (userType.equals("")) ? null : userType;

            PcuserUsersBak.updateUser(user.getUserName(), firstName, middleName, lastName, suffix, alternateName, jobTitle, active, userType);

        }
    }

    @Test(enabled = false)
    public void updateUserRoles() throws Exception {
        List<PcuserUsersBak> allUsers = PcuserUsersBak.getAllUsers();
        String firstName = null;
        String middleName = null;
        String lastName = null;
        String suffix = null;
        String alternateName = null;
        String jobTitle = null;
        String active = null;
        String userType = null;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        for (PcuserUsersBak user : allUsers) {

            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();

            userSearchByUserName(user);

            topMenu.clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, ':UserDetail_RolesCardTab-btnEl')]")));
            List<WebElement> userRoles = driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserRolesLV')]/div/table/tbody/child::tr"));

            for (WebElement role : userRoles) {
                String role1 = role.findElement(By.xpath(".//child::td[2]/div/a")).getText();
                PcuserUserRolesBak.createNewUserRole(user.getUserName(), role1);
            }
        }
    }

    private boolean userSearchByUserName(PcuserUsersBak user) {

        WebElement fName = driver.findElement(By.xpath("//input[contains(@id, 'UserSearchDV:Username-inputEl')]"));
        fName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        fName.sendKeys(user.getUserName());
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);
        driver.findElement(By.xpath("//a[contains(@id, ':SearchAndResetInputSet:SearchLinksInputSet:Search')]")).click();

        if (driver.findElements(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).size() > 0) {
            driver.findElement(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).click();
            return true;
        } else {
            return false;
        }

    }

    @Test(enabled = false)
    public void getAllPCUsers() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickUserSearch();


        for (String letter : alphabet) {

            driver.findElement(By.xpath("//input[contains(@id, 'GlobalPersonNameInputSet:FirstName-inputEl')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            driver.findElement(By.xpath("//input[contains(@id, 'GlobalPersonNameInputSet:FirstName-inputEl')]")).sendKeys(letter);
            topMenu.clickWhenClickable(driver.findElement(By.xpath("//a[contains(@id, ':SearchLinksInputSet:Search')]")));
            if (letter.equals("e")) {
            }
            List<WebElement> pageNumberList = driver.findElements(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
            List<WebElement> maxPagesList = driver.findElements(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));

            try {

                WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
                String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());

                while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
                    String name = "";
                    String userName = "";
                    pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));

                    List<WebElement> resultsList = driver.findElements(By.xpath("//div[contains(@id, ':UserSearchResultsLV-body')]/div/table/tbody/child::tr"));

                    for (WebElement user : resultsList) {
                        name = user.findElement(By.xpath("./child::td[3]/div/a")).getText();
                        userName = user.findElement(By.xpath("./child::td[4]/div")).getText();
                        String[] splitName = name.split(" ");
                        if (splitName.length == 2) {
                            PcuserUsersBak.createNewUser(userName, splitName[0], null, splitName[1], null, null, null, null, null);
                        } else if (splitName.length == 3) {
                            PcuserUsersBak.createNewUser(userName, splitName[0], splitName[1], splitName[2], null, null, null, null, null);
                        } else {
                            System.out.println(name + " NEEDS TO BE MANUALLY ENTERED");
                        }
                    } //end for

                    nextPage();
                } //END WHILE
            } catch (Exception e) {
                String name = "";
                String userName = "";
                List<WebElement> resultsList = driver.findElements(By.xpath("//div[contains(@id, ':UserSearchResultsLV-body')]/div/table/tbody/child::tr"));

                for (WebElement user : resultsList) {
                    name = user.findElement(By.xpath("./child::td[3]/div/a")).getText();
                    userName = user.findElement(By.xpath("./child::td[4]/div")).getText();
                    String[] splitName = name.split(" ");
                    if (splitName.length == 2) {
                        PcuserUsersBak.createNewUser(userName, splitName[0], null, splitName[1], null, null, null, null, null);
                    } else if (splitName.length == 3) {
                        PcuserUsersBak.createNewUser(userName, splitName[0], splitName[1], splitName[2], null, null, null, null, null);
                    } else {
                        System.out.println(name + " NEEDS TO BE MANUALLY ENTERED");
                    }
                } //end for
            }
        }//END FOR
    }//END PC USERS


    private List<String> getAccess() {
        List<String> returnList = new ArrayList<String>();
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")));
        List<WebElement> groupList = driver.findElements(By.xpath("//div[contains(@id, ':UserGroupsLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
        for (WebElement group : groupList) {
            returnList.add(group.getText());
        }
        return returnList;
    }

    private List<String> getRoles() {
        List<String> returnList = new ArrayList<String>();
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, ':UserDetail_RolesCardTab-btnEl')]")));
        if (!driver.findElements(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]")).isEmpty()) {
            WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
            WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
            String maxpage = maxPages.getText().replace("of ", "");
            while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
                pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                List<WebElement> rolesList = driver.findElements(By.xpath("//div[contains(@id, ':UserRolesLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
                for (WebElement group : rolesList) {
                    returnList.add(group.getText());
                }
                nextPage();
            }
        } else {
            List<WebElement> rolesList = driver.findElements(By.xpath("//div[contains(@id, ':UserRolesLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
            for (WebElement group : rolesList) {
                returnList.add(group.getText());
            }
        }
        return returnList;
    }


    private String getRegion() {
        driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")).click();
        if (driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Region')]")).size() > 0) {
            String returnText = driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Region')]")).getText();
            return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Region')]")).getText();
        } else {
            return null;
        }
    }

    @Test(enabled = true)
    public void getAgentsUsers() throws Exception {
        userList = new ArrayList<String>();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");

        getUserList("Agent");

        for (String user : userList) {
            String userName = null;
            String agentFirstName = null;
            String agentMiddleName = null;
            String agentLastName = null;
            String agentNumber = null;
            String agentSA = null;
            String agentPA = null;
            String agentRegion = null;
            String agentCounty = null;
            String agentPreferredName = null;
            TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
            topMenu.clickUserSearch();
            if (userSearch(user) && isAgent() && isActive()) {
                userName = getUserName();

                agentFirstName = getFirstName2();
                agentMiddleName = getMiddleName2();
                agentLastName = getLastName2();

                agentPreferredName = getPreferredName();
                agentNumber = getAgentNumber();
                agentSA = getAgentSA();
                agentPA = getAgentPA();
                agentRegion = getUserRegion();
                agentCounty = getUserCounty().replace("County", "").trim();

                //USERS TO EXCLUDE
                if (userName.equals("brae") ||
                        userName.equals("kfowles") ||
                        userName.equals("dmartin") ||
                        userName.equals("bbmcclure") ||
                        userName.equals("btrumble") ||
                        userName.equals("mwells") ||
                        userName.equals("aanderson") ||
                        userName.equals("bashcraft") ||
                        userName.equals("kborgen") ||
                        userName.equals("dpaynter") ||
                        userName.equals("lwest")) {
                    //thoopes
                    continue;
                }


                if (!userName.contains("r1") && !userName.contains("r2") && !userName.contains("r3") && !userName.contains("r4") && !userName.contains("noncommissionable") && !userName.contains("unassigned") && !userName.contains("catch")) {
                    if (userName != null && agentNumber != null && agentRegion != null) {
                        String[] split = user.split(" ");
                        System.out.println("Adding: " + userName + " to the database");
                        //jlarsen for some reason was swapping the SA and PA in the database. so I swapped the order
                        //AgentsHelper.createNewAgentsUser(agentNumber, split[0], split[split.length-1], userName, "gw", agencyName, agencyManager, agentPA, agentSA);
                        try {
                            AgentsHelper.createNewAgentsUser(agentNumber, agentFirstName, agentMiddleName, agentLastName, userName, "gw", agentRegion, agentSA, agentPA, agentCounty, agentPreferredName);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//						AgentsHelper.createNewAgentsUser(agentNumber, split[0], (split.length > 2)? split[1] : null, split[split.length - 1], userName, "gw", agencyName, agencyManager, agentSA, agentPA);
                    }
                }

            }
        }
    }


    private String getPreferredName() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:AlternateName-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:AlternateName-inputEl')]")).getText();
        } else {
            return null;
        }
    }


    private String getFirstName2() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).getText();
        } else {
            return null;
        }
    }

    private String getMiddleName2() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")).getText();
        } else {
            return null;
        }
    }

    private String getLastName2() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")).getText();
        } else {
            return null;
        }
    }

    private String getUserCounty() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'ProfileCardTab-btnEl')]")));
        List<WebElement> agentcounty = driver.findElements(By.xpath("//div[contains(@id, ':CountyList-inputEl')]"));
        if (agentcounty.isEmpty()) {
            return null;
        } else {
            return agentcounty.get(0).getText().replaceAll("[\\d]", "").replace(" - ", "");
        }
    }


    //get list of all users in an existing Role
    private void getUserList(String role) {
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickRoles();
        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());

        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            if (driver.findElements(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).size() > 0) {
                driver.findElement(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).click();
                break;
            } else {
                nextPage();
            }
        }

        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'RoleDetailScreen:RoleDetail_UsersCardTab-btnEl')]")));
        pageNumber = driver.findElement(By.xpath("//input[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV:_ListPaging-inputEl')]"));
        maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
//		maxpage = maxPages.getText().substring(maxPages.getText().length()-1, maxPages.getText().length());
        maxpage = maxPages.getText().replace("of ", "");

        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            pageNumber = driver.findElement(By.xpath("//input[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV:_ListPaging-inputEl')]"));
            createUserList();
            nextPage();
        }
    }

    private void createUserList() {
        List<WebElement> userNames = driver.findElements(By.xpath("//div[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a"));
        for (WebElement element : userNames) {
            userList.add(element.getText());
        }
    }

    private void nextPage() {
        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        new BasePage(driver).clickWhenClickable(pageNumber);
        pageNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        pageNumber.sendKeys(Integer.toString(Integer.parseInt(pageNumber.getAttribute("value")) + 1));
        pageNumber.sendKeys(Keys.ENTER);
    }

    //searches for user through the Administration Menu
    private Boolean userSearch(String user) {
        String[] split = StringsUtils.getStringSplitFromFullName(user);

        WebElement fName = driver.findElement(By.xpath("//input[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]"));
        fName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        fName.sendKeys(split[0]);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);
        WebElement lName = driver.findElement(By.xpath("//input[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]"));
        lName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        lName.sendKeys(split[split.length - 1]);
        driver.findElement(By.xpath("//a[contains(@id, ':SearchAndResetInputSet:SearchLinksInputSet:Search')]")).click();

        if (driver.findElements(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[6]/div/a[contains(text(), 'Crop')]")).isEmpty()) {
            if (driver.findElements(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).size() > 0) {
                driver.findElement(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).click();
                return true;
            } else {
                return false;
            }
        }
        return false;

    }


    private String getUserRegion() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetail_AccessCardTab-btnInnerEl')]")));
        if (driver.findElements(By.xpath("//a[contains(@id, 'UserDetail_AccessDV:UserGroupsLV:0:Group')]")).size() > 0) {
            return driver.findElement(By.xpath("//a[contains(@id, 'UserDetail_AccessDV:UserGroupsLV:0:Group')]")).getText();
        } else {
            return null;
        }
    }


    private String getUserActive() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':UserDetailInputSet:Active-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':UserDetailInputSet:Active-inputEl')]")).getText();
        } else {
            return null;
        }
    }


    //gets username from Basic Tab
    private String getUserName() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:Username-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:Username-inputEl')]")).getText();
        } else {
            return null;
        }
    }

    //get persons associated with user
    private String getAssociation() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, ':UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, ':UserDetailScreen:UserAssociationsLV-body')]/descendant::tbody/child::tr[1]/child::td/div[contains(text(), 'Agent')]/parent::td/preceding-sibling::td[1]/div")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, ':UserDetailScreen:UserAssociationsLV-body')]/descendant::tbody/child::tr[1]/child::td/div[contains(text(), 'Agent')]/parent::td/preceding-sibling::td[1]/div")).getText();
        } else {
            return null;
        }
    }

    //get agency associated with user
    private String getAgency() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Agency')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Agency')]")).getText();
        } else {
            return null;
        }
    }

    private String getAgentNumber() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AgentNumber-inputEl')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AgentNumber-inputEl')]")).getText();
        } else {
            return null;
        }
    }

    private Boolean isAgencyManger() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_RolesCardTab-btnEl')]")));
        return driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserRolesLV-body')]/div/table/child::tbody/child::tr/child::td/div/a[contains(text(), 'Agency Manager')]")).size() > 0;
    }

    private String getAgentSA() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Service Associate')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Service Associate')]/parent::td/parent::tr/child::td[2]/div")).getText();
        } else {
            return null;
        }
    }

    private String getAgentPA() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));
        if (driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Production Assistant')]")).size() > 0) {
            return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Production Assistant')]/parent::td/parent::tr/child::td[2]/div")).getText();
        } else {
            return null;
        }
    }

    private Boolean isAgent() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_RolesCardTab-btnEl')]")));
        List<WebElement> rolesList = driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserRolesLV-body')]/div/table/child::tbody/child::tr/child::td/div/a[contains(@id, 'RoleName')]"));
        if (rolesList.size() > 1) {
            for (WebElement role : rolesList) {
                if (!role.getText().equalsIgnoreCase("Agent")) {
                    return false;
                }
//				if(!role.getText().equalsIgnoreCase("Agent") && !role.getText().equalsIgnoreCase("Agency Manager")) {
//				}
            }
        }
        return true;
    }


    private Boolean isActive() {
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
        List<WebElement> rolesList = driver.findElements(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetailDV:UserDetailCommons:UserDetailInputSet:AccountLocked-inputEl')]"));
        if (rolesList.size() > 1) {
            return !rolesList.get(0).getText().equalsIgnoreCase("Yes");
        }
        return true;
    }

    private String getAgentCounty(Agents agent) {
//		driver.findElement(By.xpath("//div[contains(., '"+agent.getAgentUserName()+"')]/parent::td/preceding-sibling::td[1]/div/a[contains(., '"+agent.getAgentFirstName()+"') and contains(., '"+agent.getAgentLastName()+"')]")).click();
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));
        return driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:CountyList-inputEl')]")).getText();
    }

    private ArrayList<String> getCsrRegion() {
//		driver.findElement(By.xpath("//div[contains(., '"+agent.getAgentUserName()+"')]/parent::td/preceding-sibling::td[1]/div/a[contains(., '"+agent.getAgentFirstName()+"') and contains(., '"+agent.getAgentLastName()+"')]")).click();
        new BasePage(driver).clickWhenClickable(driver.findElement(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")));
        WebElement tableWebElement = driver.findElement(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV')]"));
        List<WebElement> regions = tableWebElement.findElements(By.xpath(".//a[contains(@id,'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV:')]"));
        ArrayList<String> regionsToReturn = new ArrayList<String>();
        for (WebElement region : regions) {
            regionsToReturn.add(region.getText());
        }
        return regionsToReturn;


    }

    @FindBy(xpath = "//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")
    public WebElement townshipRangeTableDiv;

    @Test(enabled = false)
    public void getTownshipRange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su", "gw");
        TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
        topMenu.clickTownshipRange();

        String county = "";
        String township = "";
        String townshipDirection = "";
        String range = "";
        String rangeDirection = "";
        TableUtils tableUtils = new TableUtils(driver);
        int pageCount = tableUtils.getNumberOfTablePages(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")));
        int rowCount = tableUtils.getRowCount(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")));

        for (int x = 0; x < pageCount; x++) {
            for (int i = 0; i < rowCount; i++) {
                county = getTownshipRangeCounty(i + 1);
                township = getTownship(i + 1);
                townshipDirection = getTownshipDirection(i + 1);
                range = getRange(i + 1);
                rangeDirection = getRangeDirection(i + 1);

                TownshipRangeHelper.createTownshipRange(county, township, townshipDirection, range, rangeDirection);
            }
            tableUtils.clickNextPageButton(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")));
        }

    }

    public String getTownshipRangeCounty(int row) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")), row, "County");
    }

    public String getTownship(int row) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")), row, "Township");
    }

    public String getTownshipDirection(int row) {
        return driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV-body')]/descendant::table/descendant::tr[" + row + "]/td[4]/div")).getText();
    }

    public String getRange(int row) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV')]")), row, "Range");
    }

    public String getRangeDirection(int row) {
        return driver.findElement(By.xpath("//div[contains(@id, 'TownshipRangePage:TownshipRangesLV-body')]/descendant::table/descendant::tr[" + row + "]/td[6]/div")).getText();
    }


}
