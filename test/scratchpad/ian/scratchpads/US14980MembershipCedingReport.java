package scratchpad.ian.scratchpads;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class US14980MembershipCedingReport extends BaseTest {
	private WebDriver driver;
	
    @Override
    public void tearDownDriver() {

    }

    @DataProvider(name = "data", parallel = false)
    public Object[][] getData() {

        // row 7  - 1987 contains the data
        // Size to the rows you need 1987 - 7 = 1980
        int startRow = 6; //  n - 1  to start at that row number in excel
        int endRow = 1986; // n-1 1986 to end at row 1987
        int totalRows = endRow + 1 - startRow;


        Object[][] obj = new Object[totalRows][1];
        try {
            // Read in Membership report.
            FileInputStream excelFile = new FileInputStream(new File("C:\\Users\\iclouser\\Documents\\Membership Report.xlsx"));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataTypeSheet = workbook.getSheetAt(0);

            for (int i = startRow; i <= endRow; i++) {
                HashMap<String, List<String>> policyMap = new HashMap<>();
                List<String> list = new ArrayList<>();

                // Get Cell data and put into a list
                list.add(dataTypeSheet.getRow(i).getCell(3).getStringCellValue()); // First Name
                list.add(dataTypeSheet.getRow(i).getCell(5).getStringCellValue()); // Last Name
                list.add(dataTypeSheet.getRow(i).getCell(7).getStringCellValue()); // County
                list.add(dataTypeSheet.getRow(i).getCell(9).getStringCellValue()); // Address line 1
                list.add(dataTypeSheet.getRow(i).getCell(11).getStringCellValue()); // City
                list.add(dataTypeSheet.getRow(i).getCell(12).getStringCellValue()); // State
                list.add(dataTypeSheet.getRow(i).getCell(13).getStringCellValue()); // Zip Code

                // Get Member number so we can search that account. Add values that need verified into a list.
                policyMap.put(dataTypeSheet.getRow(i).getCell(2).getStringCellValue(), list);
                obj[i - startRow][0] = policyMap;
            }

            workbook.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(obj);
        return obj;
    }


    @Test(dataProvider = "data")
    public void testThings(Map<Object, Object> map) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "REGR05");
        driver = buildDriver(cf);
        driver.get("http://fbmsigw-prd01.idfbins.com/pc");
        BasePage basePage = new BasePage(driver);

        String policy = (String) map.keySet().toArray()[0];
        List<String> memberShip = (List<String>) map.values().toArray()[0];
        SoftAssert softAssert = new SoftAssert();
        try {
            new Login(driver).login("TeamReportTesting", "Titnp4tr.w2fb");
            basePage.clickWhenClickable(By.id("TabBar:SearchTab"), 45);
            basePage.clickWhenClickable(By.cssSelector("td[id$=':Search_PolicySearch']"), 45);
            basePage.waitUntilElementIsClickable(By.cssSelector("input[id$=':AccountNumber-inputEl']")).sendKeys(policy);
            basePage.clickWhenClickable(By.cssSelector("a[id$=':Search']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("div[id$=':PolicySearch_ResultsLV-body']"), 45);
            basePage.clickWhenClickable(By.cssSelector("a[id$='PolicyNumber']"));
            basePage.waitUntilElementIsClickable(By.cssSelector("span[id^='PolicyFile_Summary:Policy_SummaryScreen']"), 45);
            basePage.clickWhenClickable(By.cssSelector("td[id$=':PolicyMenuItemSet_PolicyInfo']"), 45);
            basePage.waitUntilElementIsClickable(By.cssSelector("div[id$=':TermType-inputEl']"), 45);

            ArrayList<String> valsAslist = verifyMembershipInfo(driver, memberShip.get(0), memberShip.get(1));

            softAssert.assertEquals(memberShip, valsAslist, "Expected values: " + memberShip + " but got: " + valsAslist + " for policy number: " + policy);
            logout(driver);

        } catch (Exception e) {
            System.out.println("Something went wrong with verifying: " + policy);
            if (driver != null) {
                driver.quit();
            }
            softAssert.assertTrue(false, e.getMessage());
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


    public ArrayList<String> verifyMembershipInfo(WebDriver driver, String firstName, String lastName) {

        ArrayList<String> memberInfo = new ArrayList<>();

        BasePage bp = new BasePage(driver);

        String county = bp.find(By.xpath("//a[contains(text(),'" + firstName + "') and contains(text(),'" + lastName + "')]/../../..//td[7]/div")).getText() + " County Farm Bureau";

        try {
            bp.clickWhenClickable(By.xpath("//a[contains(text(),'" + firstName + "') and contains(text(),'" + lastName + "')]"));
        } catch (Exception e) {
            Assert.fail("Couldn't find the namne: " + firstName + " " + lastName);
        }

        memberInfo.add(bp.waitUntilElementIsClickable(bp.find(By.cssSelector("div[id$=':FirstName-inputEl']"))).getText()); // First Name
        memberInfo.add(bp.find(By.cssSelector("div[id$=':LastName-inputEl']")).getText()); // Last Name
        memberInfo.add(county); // Add county
        String address = bp.find(By.cssSelector("div[id$=':AddressListing-inputEl']")).getText();
        String[] addressSplit = address.split(",");


        memberInfo.add(addressSplit[0].trim()); // Address Line 1
        memberInfo.add(addressSplit[1].trim()); // City
        memberInfo.add(addressSplit[2].trim().substring(0, 2)); // State

        String zip = bp.find(By.cssSelector("div[id$=':AddressSummary-inputEl']")).getText().split(",")[1].trim().split(" ")[1].split("-")[0];
        // .split(" ")[1].split("-")[1];
        memberInfo.add(zip.trim()); // zip

        return memberInfo;
    }

}
