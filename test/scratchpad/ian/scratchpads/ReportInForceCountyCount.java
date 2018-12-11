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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class ReportInForceCountyCount extends BaseTest {
	private WebDriver driver;
	
    @Override
    public void tearDownDriver() {

    }


    @DataProvider(name = "data", parallel = true)
    public Object[][] getData() {

        Object[][] obj = new Object[2714][1];
        try {
            FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\iclouser\\Documents\\Inforce Count By County Lines.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataTypeSheet = workbook.getSheetAt(0);
//            Iterator<Row> iterator = dataTypeSheet.iterator();
//            iterator.next();

            for (int i = 1715; i <= 4429; i++) {
                HashMap<String, List<Integer>> policyMap = new HashMap<>();
                List<Integer> list = new ArrayList<>();
                list.add((int) dataTypeSheet.getRow(i).getCell(2).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(3).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(4).getNumericCellValue());
                list.add((int) dataTypeSheet.getRow(i).getCell(5).getNumericCellValue());

                policyMap.put(dataTypeSheet.getRow(i).getCell(1).getStringCellValue(), list);
                obj[i - 1715][0] = policyMap;
            }

            workbook.close();
        } catch (Exception e) {

        }
        System.out.println(obj);
        return obj;
    }


    @Test(dataProvider = "data")
    public void testThings(Map<Object, Object> map) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "uat");
        driver = buildDriver(cf);
        BasePage basePage = new BasePage(driver);

        String policy = (String) map.keySet().toArray()[0];

        SoftAssert softAssert = new SoftAssert();
        try {
            new Login(driver).login("bswindle", "gw");
            basePage.clickWhenClickable(By.id("TabBar:SearchTab"), 30);
            basePage.clickWhenClickable(By.cssSelector("td[id$=':Search_PolicySearch']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("input[id$=':PolicyNumberCriterion-inputEl']")).sendKeys(policy);
            basePage.clickWhenClickable(By.cssSelector("a[id$=':Search']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("div[id$=':PolicySearch_ResultsLV-body']"));
            basePage.clickWhenClickable(By.cssSelector("a[id$='PolicyNumber']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).clear();
            basePage.find(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).sendKeys("3/31/18");
            basePage.find(By.cssSelector("input[id$='PolicyPeriodEffDateID_Arg-inputEl']")).sendKeys(Keys.TAB);
            basePage.waitUntilElementIsClickable(By.cssSelector("span[id^='PolicyFile_Summary:Policy_SummaryScreen']"));
            basePage.clickWhenClickable(By.cssSelector("td[id$='PolicyMenuItemSet_Pricing']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("a[id$='PolicyFile_Pricing_CumulativeCardTab']"));


            int sectionOneAndTwo = 0;
            int sectionThree = 0;
            int sectionFour = 0;

            boolean hasOneAndTwo = basePage.finds(By.xpath("//span[contains(text(),'Sections I & II - Property & Liability Line')]")).size() > 0;
            boolean hasThree = basePage.finds(By.xpath("//span[contains(text(),'Section III - Auto Line')]")).size() > 0;
            boolean hasFour = basePage.finds(By.xpath("//span[contains(text(),'Section IV - Inland Marine Line')]")).size() > 0;

            sectionOneAndTwo = hasOneAndTwo ? 1 : 0;
            sectionFour = hasFour ? 1 : 0;

            if (hasThree && (hasOneAndTwo || hasFour)) {
                basePage.clickWhenClickable(By.cssSelector("span[id$='1:policyLineCardTabTab-btnInnerEl']"));
                basePage.waitUntilElementIsClickable(By.cssSelector("div[id*='PersonalAutoLinePanelSet'][id$='-body']"));
                sectionThree = basePage.finds(By.cssSelector("div[id*='PersonalAutoLinePanelSet'][id$='-body'] tr")).size();
            } else {
                basePage.waitUntilElementIsClickable(By.cssSelector("div[id*='PersonalAutoLinePanelSet'][id$='-body']"));
                sectionThree = basePage.finds(By.cssSelector("div[id*='PersonalAutoLinePanelSet'][id$='-body'] tr")).size();
            }

            List<Integer> valsAslist = Arrays.asList(sectionOneAndTwo, sectionOneAndTwo, sectionThree, sectionFour);
            List<Integer> expectedVals = (List<Integer>) map.values().toArray()[0];
            softAssert.assertEquals(expectedVals, valsAslist, "Expected values: " + expectedVals + " but got: " + valsAslist + " for policy number: " + policy);
            logout(driver);

        } catch (Exception e) {
            System.out.println("Something went wrong with verifying: " + policy);
            if (driver != null) {
                driver.quit();
            }
            softAssert.assertAll();
        }
        if (driver != null) {
            driver.quit();
        }
        softAssert.assertAll();

    }

    public void logout(WebDriver driver) {

        BasePage basePage = new BasePage(driver);
        basePage.waitUntilElementIsVisible(By.xpath("//span[contains(@id, ':TabLinkMenuButton-btnEl')]"));

        basePage.clickWhenClickable(By.xpath("//span[contains(@id, ':TabLinkMenuButton-btnEl')]"));

        basePage.clickWhenClickable(By.xpath("//div[contains(@id,'TabBar:LogoutTabBarLink')]"));
        if (basePage.isAlertPresent()) {
            basePage.selectOKOrCancelFromPopup(OkCancel.OK);
        }
    }
}
