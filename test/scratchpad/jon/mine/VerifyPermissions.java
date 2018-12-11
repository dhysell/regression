package scratchpad.jon.mine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StrBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;

public class VerifyPermissions extends BaseTest {

    List<String> missingPermissions = new ArrayList<String>();

    List<String> roles = new ArrayList<String>();
    @SuppressWarnings("serial")
    List<String> roles2 = new ArrayList<String>() {{
        this.add("Underwriter");
        this.add("Underwriter Assistant");
        this.add("PL Underwriter Assistant");
        this.add("Underwriter Supervisor");
    }};

    //PERMISSIONS LIST (Please do not mess with this list)
    List<String> permissionsDEV2 = new ArrayList<String>();
    List<String> permissionsQA2 = new ArrayList<String>();
    List<String> permissionsIT2 = new ArrayList<String>();
    List<String> permissionsUAT2 = new ArrayList<String>();
    List<String> permissionsDEV = new ArrayList<String>();
    List<String> permissionsQA = new ArrayList<String>();
    List<String> permissionsIT = new ArrayList<String>();
    List<String> permissionsUAT = new ArrayList<String>();
    List<String> permissionsBHILTBRAND = new ArrayList<String>();
    List<String> permissionsJLARSEN = new ArrayList<String>();
    List<String> permissionsCOR2 = new ArrayList<String>();
    List<String> permissionsBMARTIN = new ArrayList<String>();
    List<String> permissionsBHILTBRANDMAINTENANCE = new ArrayList<String>();
    List<String> permissionsJLARSENMAINTENANCE = new ArrayList<String>();
    List<String> permissionsCHOFMANMAINTENANCE = new ArrayList<String>();
    List<String> permissionsBMARTINMAINTENANCE = new ArrayList<String>();
    List<String> permissionsPRD = new ArrayList<String>();

    //ENVIRONMENTS TO CHECK (Please comment out environments you DO NOT want to check)
    @SuppressWarnings("serial")
    List<String> environmentList = new ArrayList<String>() {{
        this.add("DEV2");
//		this.add("QA2");
//		this.add("IT2");
//		this.add("UAT2");
//		this.add("DEV");
//		this.add("QA");
//		this.add("IT");
//		this.add("UAT");
//		this.add("BHILTBRAND");
//		this.add("JLARSEN");
        this.add("COR2");
//		this.add("BMARTIN");
//		this.add("BHILTBRANDMAINTENANCE");
//		this.add("JLARSENMAINTENANCE");
//		this.add("CHOFMANMAINTENANCE");
//		this.add("BMARTINMAINTENANCE");
//		this.add("PRD");
    }};

    private WebDriver driver;


    @Test
    public void getPermissions() throws Exception {

        getRoleList();

        for (String role : roles) {
            resetLists();
            for (String environment : environmentList) {
                Config cf = new Config(ApplicationOrCenter.PolicyCenter, environment);
                driver = buildDriver(cf);
                Login login = new Login(driver);
                if (!driver.getCurrentUrl().contains("pc/pc")) {
                    login.login("su", "gw");
                } else {
                    login.login("emessaging", "gwqa");
                }
                TopMenuAdministrationPC topmenu = new TopMenuAdministrationPC(driver);
                topmenu.clickRoles();

                selectRole(role);

                if (!topmenu.finds(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]")).isEmpty()) {
                    WebElement pageNumber = topmenu.find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                    WebElement maxPages = topmenu.find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
                    String maxpage = maxPages.getText().replace("of ", "");

                    while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {

                        pageNumber = topmenu.find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
                        List<WebElement> permissionsList = topmenu.finds(By.xpath("//div[contains(@id, ':RolePrivilegesLV-body')]/div/table/tbody/child::tr/child::td[2]/div"));

                        addPermissions(environment, permissionsList);

                        nextPage();
                    }//end while
                } else {
                    List<WebElement> permissionsList = topmenu.finds(By.xpath("//div[contains(@id, ':RolePrivilegesLV-body')]/div/table/tbody/child::tr/child::td[2]/div"));
                    if (!permissionsList.isEmpty()) {
                        addPermissions(environment, permissionsList);
                    }
                }//end else
            }//end for environments

            compareLists(role);

        }//end for roles

    }//end getQAPermissions()


    private void selectRole(String role) {

        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = driver.findElement(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().replace("of ", "");

        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
            if (driver.findElements(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).size() > 0) {
                driver.findElement(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).click();
                break;
            } else {
                nextPage();
            }
        }
    }

    private void resetLists() {
        permissionsDEV2 = new ArrayList<String>();
        permissionsQA2 = new ArrayList<String>();
        permissionsIT2 = new ArrayList<String>();
        permissionsUAT2 = new ArrayList<String>();
        permissionsDEV = new ArrayList<String>();
        permissionsQA = new ArrayList<String>();
        permissionsIT = new ArrayList<String>();
        permissionsUAT = new ArrayList<String>();
        permissionsBHILTBRAND = new ArrayList<String>();
        permissionsJLARSEN = new ArrayList<String>();
        permissionsCOR2 = new ArrayList<String>();
        permissionsBMARTIN = new ArrayList<String>();
        permissionsBHILTBRANDMAINTENANCE = new ArrayList<String>();
        permissionsJLARSENMAINTENANCE = new ArrayList<String>();
        permissionsCHOFMANMAINTENANCE = new ArrayList<String>();
        permissionsBMARTINMAINTENANCE = new ArrayList<String>();
        permissionsPRD = new ArrayList<String>();
    }


    public void compareLists(String role) {
        StrBuilder roleOutputStream = new StrBuilder();
        for (String environment : environmentList) {
            roleOutputStream.append(environment + " Permision Total: " + setList(environment).size());
            roleOutputStream.appendNewLine();
        }

        for (String env : environmentList) {
            for (String permission : setList(env)) {
                for (String environment : environmentList) {
                    System.out.println(env + " -> " + environment + " | " + permission);
                    if (setList(environment).indexOf(permission) < 0) {
                        System.out.println(permission + " in " + env + " Was not found in " + environment);
                        missingPermissions.add(permission + " in " + env + " Was not found in " + environment);
                    }
                }//end environment for
            }//end permissions for
            roleOutputStream.appendNewLine();
        }//end string for

        if (missingPermissions.size() > 0) {
            roleOutputStream.append("MISSING PERMISSIONS FOR " + role);
            roleOutputStream.appendNewLine();
            for (String missing : missingPermissions) {
                roleOutputStream.append(missing);
                roleOutputStream.appendNewLine();
            }

            new File("C://dev/Missing Permisions").mkdirs();
            BufferedWriter output = null;
            try {
                File file = new File("C://dev/Missing Permisions/" + role + ".txt");
                output = new BufferedWriter(new FileWriter(file));
                output.write(roleOutputStream.toString());
                roleOutputStream.clear();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        missingPermissions.clear();


    }//end compareLists


    private List<String> setList(String env) {

        switch (env) {
            case "DEV2":
                return permissionsDEV2;
            case "QA2":
                return permissionsQA2;
            case "IT2":
                return permissionsIT2;
            case "UAT2":
                return permissionsUAT2;
            case "DEV":
                return permissionsDEV;
            case "QA":
                return permissionsQA;
            case "IT":
                return permissionsIT;
            case "UAT":
                return permissionsUAT;
            case "BHILTBRAND":
                return permissionsBHILTBRAND;
            case "JLARSEN":
                return permissionsJLARSEN;
            case "COR2":
                return permissionsCOR2;
            case "BMARTIN":
                return permissionsBMARTIN;
            case "BHILTBRANDMAINTENANCE":
                return permissionsBHILTBRANDMAINTENANCE;
            case "JLARSENMAINTENANCE":
                return permissionsJLARSENMAINTENANCE;
            case "CHOFMANMAINTENANCE":
                return permissionsCHOFMANMAINTENANCE;
            case "BMARTINMAINTENANCE":
                return permissionsBMARTINMAINTENANCE;
            case "PRD":
                return permissionsPRD;
            default:
                return null;
        }


    }


    private void getRoleList() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV2");
        driver = buildDriver(cf);
        new Login(driver).login("su", "gw");

        TopMenuAdministrationPC topmenu = new TopMenuAdministrationPC(driver);
        topmenu.clickRoles();

        WebElement pageNumber = topmenu.find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        WebElement maxPages = topmenu.find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
        String maxpage = maxPages.getText().replace("of ", "");
        while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {

            pageNumber = topmenu.find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
            List<WebElement> rolesList = topmenu.finds(By.xpath("//div[contains(@id, ':RolesLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));

            for (WebElement role : rolesList) {
                roles.add(role.getText());
            }

            nextPage();
        }//end while


    }


    private void addPermissions(String environment, List<WebElement> permissionsList) {

        for (WebElement permission : permissionsList) {
            switch (environment) {
                case "DEV2":
                    permissionsDEV2.add(permission.getText());
                    break;
                case "QA2":
                    permissionsQA2.add(permission.getText());
                    break;
                case "IT2":
                    permissionsIT2.add(permission.getText());
                    break;
                case "UAT2":
                    permissionsUAT2.add(permission.getText());
                    break;
                case "DEV":
                    permissionsDEV.add(permission.getText());
                    break;
                case "QA":
                    permissionsQA.add(permission.getText());
                    break;
                case "IT":
                    permissionsIT.add(permission.getText());
                    break;
                case "UAT":
                    permissionsUAT.add(permission.getText());
                    break;
                case "BHILTBRAND":
                    permissionsBHILTBRAND.add(permission.getText());
                    break;
                case "JLARSEN":
                    permissionsJLARSEN.add(permission.getText());
                    break;
                case "COR2":
                    permissionsCOR2.add(permission.getText());
                    break;
                case "BMARTIN":
                    permissionsBMARTIN.add(permission.getText());
                    break;
                case "BHILTBRANDMAINTENANCE":
                    permissionsBHILTBRANDMAINTENANCE.add(permission.getText());
                    break;
                case "JLARSENMAINTENANCE":
                    permissionsJLARSENMAINTENANCE.add(permission.getText());
                    break;
                case "CHOFMANMAINTENANCE":
                    permissionsCHOFMANMAINTENANCE.add(permission.getText());
                    break;
                case "BMARTINMAINTENANCE":
                    permissionsBMARTIN.add(permission.getText());
                    break;
                case "PRD":
                    permissionsPRD.add(permission.getText());
                    break;
            }//end switch
        }//end for
    }//end addPermissions


    private void nextPage() {
        WebElement pageNumber = driver.findElement(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
        pageNumber.click();
        pageNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        pageNumber.sendKeys(Integer.toString(Integer.parseInt(pageNumber.getAttribute("value")) + 1));
        pageNumber.sendKeys(Keys.ENTER);
    }


}








