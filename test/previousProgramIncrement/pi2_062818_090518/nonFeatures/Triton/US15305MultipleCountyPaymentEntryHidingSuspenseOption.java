package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsCountyCashPayment;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.CountyIdaho;
import repository.driverConfiguration.Config;
import repository.gw.enums.MultiPaymentType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Requirement US15305 -- Multiple Payment Screens Entry hiding the "Suspense" option in the column "Type".
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-02%20County%20Payment%20Entry%20Wizard.docx">17-02 County Payment Entry Wizard</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-01%20Multiple%20Payment%20Entry%20Wizard.docx&action=default">17-01 Multiple Payment Entry Wizard</a>
 * @Description
 * @DATE July 17, 2018
 */
public class US15305MultipleCountyPaymentEntryHidingSuspenseOption extends BaseTest {
    private ARUsers arUser;
    private String policyNumber = null;
    private String errorMessage = null;
    private String errorMsg1 = "Please change the payment date to match the Postmarked Date";
    private String errorMsg2 = "Formatted Policy numbers must have format 12-123456-12";
    private String errorMsg3 = "Please use the policy picker to select the policy";

    @Test(enabled = true)
    public void verifyMultiplePaymentEntry() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        WebDriver driver = buildDriver(cf);
        new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        this.policyNumber = policySearch.findPolicyInGoodStanding("259", null, null);

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsMultiplePayment();

        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
        int newestRow = multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(null, null, (double) NumberUtils.generateRandomNumberInt(20, 100));
        try {
            multiplePaymentsPage.setMultiPaymentPaymentType(newestRow, MultiPaymentType.Suspense);
            Assert.fail("Suspense should be hidden.");
        } catch (Exception e) {
            System.out.println("Couldn't find Suspense payment type which is expected.");
        }

        multiplePaymentsPage.setMultiPaymentPolicyNumber(newestRow, this.policyNumber);
        multiplePaymentsPage.clickNext();
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
            errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
            if (errorMessage.contains(errorMsg3)) {
                Assert.fail("There was an error complaining about the policy number not being found after manually entering it. This should not happen and is a failure related to DE4162. Test Failed.");
            }
            if (errorMessage.contains(errorMsg1)) {
                multiplePaymentsPage.setMultiPaymentPaymentDate(1, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
                multiplePaymentsPage.clickNext();
            }
        }
        multiplePaymentsPage.clickFinish();
    }

    @Test(dependsOnMethods = {"verifyMultiplePaymentEntry"})
    public void verifyCountyPaymentEntry() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        WebDriver driver = buildDriver(cf);
        new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsCountyCashPayment();

        DesktopActionsCountyCashPayment countyPayment = new DesktopActionsCountyCashPayment(driver);

        countyPayment.setCountyCashPolicyNumber(1, policyNumber);

        countyPayment.setCountyCashPaymentAmount(1, (double) NumberUtils.generateRandomNumberInt(100, 200));
        countyPayment.selectCountyCashTableCountyCode(1, CountyIdaho.Ada);
        countyPayment.selectCountyCashTableOfficeNumber(1, "1");
        countyPayment.clickNext();

        GuidewireHelpers gwHelper = new GuidewireHelpers(driver);
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
            errorMessage = gwHelper.getFirstErrorMessage();
            int count = 0;
            while (errorMessage.contains(errorMsg1) || errorMessage.contains(errorMsg2) && count < 10) {
                if (errorMessage.contains(errorMsg3)) {
                    Assert.fail("There was an error complaining about the policy number not being found after manually entering it. This should not happen and is a failure related to DE4162. Test Failed.");
                }
                if (errorMessage.contains(errorMsg2)) {
                    countyPayment.setCountyCashPolicyNumberFromPicker(1, policyNumber.substring(3, 9));
                    countyPayment.selectCountyCashTableCountyCode(1, CountyIdaho.Ada);
                    countyPayment.selectCountyCashTableOfficeNumber(1, "1");
                    countyPayment.clickNext();
                    try {
                        errorMessage = gwHelper.getFirstErrorMessage();
                    } catch (Exception e) {
                        break;
                    }
                }

                if (errorMessage.contains(errorMsg3)) {
                    countyPayment.setDate(1, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
                    countyPayment.clickNext();
                }
                count++;
            }
        }
        countyPayment.clickFinish();
    }
}

