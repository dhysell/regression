package scratchpad.ian.scratchpads;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.helpers.WaitUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;

public class ReportInForceAutoByCount extends BaseTest {

    @DataProvider(name = "data")
    public Object[][] getData() {

        Object[][] obj = new Object[710][1];
        try {
            FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\iclouser\\Documents\\Inforce Auto Counts By County (Take 2).xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataTypeSheet = workbook.getSheetAt(0);
//            Iterator<Row> iterator = dataTypeSheet.iterator();
//            iterator.next();

            for (int i = 5; i <= 714; i++) {
                HashMap<String, List<Integer>> policyMap = new HashMap<>();
                List<Integer> list = new ArrayList<>();
                list.add((int) dataTypeSheet.getRow(i).getCell(2).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(3).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(4).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(5).getNumericCellValue());

                policyMap.put(dataTypeSheet.getRow(i).getCell(0).getStringCellValue(), list);
                obj[i - 5][0] = policyMap;
            }

            workbook.close();
        } catch (Exception e) {

        }

        return obj;
    }

    SoftAssert softAssert = new SoftAssert();

    private WebDriver driver;

    @Test(dataProvider = "data")
    public void doTheTHing(Map<Object, Object> map) throws Exception {

        System.out.println(map.keySet().toString());
        System.out.println(map.values());
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
//        Configuration.getWebDriver().get("http://fbmsigw-uat01.idfbins.com/pc/PolicyCenter.do");

        new Login(driver).login("bswindle", "gw");
        String policy = (String) map.keySet().toArray()[0];
        try {

            SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
//                policySearchPC.searchPolicyByPolicyNumberAndTerm(entry.getKey(), PolicyTermStatus.InForce);
            policySearchPC.searchPolicyByPolicyNumber(policy);
            WaitUtils waitUtils = new WaitUtils(driver);
            waitUtils.waitUntilElementIsVisible(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']"));
            policySearchPC.find(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).clear();
            policySearchPC.find(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).sendKeys("12/31/2017");
            policySearchPC.find(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).sendKeys(Keys.TAB);
//                waitUtils.waitUntilElementNotClickable(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"), 60);

            new WebDriverWait(driver, 60).until(ExpectedConditions.not(ExpectedConditions.attributeToBe(By.cssSelector("body"), "class", "x-mask")));
            waitUtils.waitUntilElementIsClickable(By.cssSelector("span[id^='PolicyFile_Summary:Policy_SummaryScreen']"));
            //                waitUtils.waitUntilElementIsClickable(By.xpath("//span[contains(text(),'Section III - Auto Line')]")).click();
            policySearchPC.clickElementByCordinates(policySearchPC.find(By.xpath("//span[contains(text(),'Section III - Auto Line')]")), 0, 0);
            SideMenuPC sideMenuPC = new SideMenuPC(driver);
            sideMenuPC.clickElementByCordinates(sideMenuPC.find(By.cssSelector("td[id$='_Vehicles']")), 5, 5);
            waitUtils.waitUntilElementIsVisible(By.cssSelector("div[id$=':VehiclesLV-body']"));


            int liab = 0;
            int compColl = 0;
            int liabComp = 0;
            int inForce = 0;

            List<WebElement> list = sideMenuPC.finds(By.cssSelector("div[id$=':VehiclesLV-body'] tr"));
            inForce = list.size();
            for (WebElement ele : list) {
                // if liab == yes and comp == no liab++\
                if (ele.findElement(By.xpath(".//td[9]")).getText().equalsIgnoreCase("yes") && ele.findElement(By.xpath(".//td[10]")).getText().equalsIgnoreCase("no")) {
                    liab++;
                }
                // if liab == no  and comp == yes compColl++;
                else if (ele.findElement(By.xpath(".//td[9]")).getText().equalsIgnoreCase("no") && !ele.findElement(By.xpath(".//td[10]")).getText().equalsIgnoreCase("no")) {
                    compColl++;
                }
                // if liab == yes and comp == yes liabComp++;
                else if (ele.findElement(By.xpath(".//td[9]")).getText().equalsIgnoreCase("yes") && !ele.findElement(By.xpath(".//td[10]")).getText().equalsIgnoreCase("no")) {
                    liabComp++;
                }

            }
            List<Integer> valsAslist = Arrays.asList(liab, compColl, liabComp, inForce);
            List<Integer> expectedVals = (List<Integer>) map.values().toArray()[0];
            softAssert.assertEquals(expectedVals, valsAslist, "Expected values: " + expectedVals + " but got: " + valsAslist + " for policy number: " + policy);
        } catch (Exception e) {
            System.out.println("Something went wrong with verifying: " + policy);
        }
        softAssert.assertAll();
    }

}
