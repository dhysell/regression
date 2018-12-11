package scratchpad.sai.sgunda;


import repository.bc.account.BCAccountMenu;
import repository.bc.wizards.ChargeReversalWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChargeCategory;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class NewTR3 extends BaseTest {


    private WebDriver driver;

    @Test
    public void SquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber("sbaker", "gw", "299923-001");
        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        ChargeReversalWizard chargeReversalWizard = new ChargeReversalWizard(driver);

        bcAccountMenu.clickAccountMenuActionsNewTransactionChargeReversal();
        chargeReversalWizard.clickSearch();
        chargeReversalWizard.clickSelectButtonInChargeReversalTable(null, ChargeCategory.Membership_Dues.getValue(), null, null);
        chargeReversalWizard.clickFinish();
    }

}
