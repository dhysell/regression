package currentProgramIncrement.nonFeatures.NoExceptions;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonCharges;
import repository.bc.wizards.ChargeReversalWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChargeCategory;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BillingCenterHelper;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


public class US16767_LimitARUsersToReverseDuesNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private boolean permissionBroke;

    @Test
    public void testGenerateMembershipOnlyPolicy() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicyHelper(driver).generateMembershipOnlyPolicy(null,null,null,null,null);
    }

    @Test(dependsOnMethods = { "testGenerateMembershipOnlyPolicy" })
    public void testReverseDuesWithUsers() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical_Advanced, ARCompany.Personal);

        BillingCenterHelper bcHelper = new BillingCenterHelper(driver);
        bcHelper.loginAndVerifyIssuancePolicyPeriodAsARUser(generatePolicy ,arUser);
        new InfoBar(driver).clickInfoBarAccountNumber();

        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        bcAccountMenu.clickBCMenuCharges();
        BCCommonCharges bcCommonCharges = new BCCommonCharges(driver);
        Assert.assertTrue(bcCommonCharges.verifyCharges(null,ChargeCategory.Membership_Dues, null, null,generatePolicy.accountNumber, null, null, null, null),"No Membership Dues found, test can't continue ");

        bcAccountMenu.clickAccountMenuActionsNewTransactionChargeReversal();
        ChargeReversalWizard chargeReversalWizard = new ChargeReversalWizard(driver);
        chargeReversalWizard.clickSearch();
        try {
            chargeReversalWizard.clickSelectButtonInChargeReversalTable(null, ChargeCategory.Membership_Dues.getValue(), null, null);
            chargeReversalWizard.setChargeReversalReason("Customer Request");
            chargeReversalWizard.setChargeReversalDescription("Reversing this charge as Billing Clerical Advanced");
            chargeReversalWizard.clickFinish();
            permissionBroke = true;
            getQALogger().info("Billing Clerical Advanced was able to reverse dues on this account where they should not have permission for please investigate ");
            Assert.fail("Billing Clerical Advanced was able to reverse dues on this account where they should not have permission for please investigate ");
        } catch (Exception e){
            e.printStackTrace();
            permissionBroke = false;
        }
        new GuidewireHelpers(driver).logout();

        // Test with Charge_Manipulator role to reverse dues
        arUser = ARUsersHelper.getRandomARUserByRole(ARUserRole.Charge_Manipulator);
        bcHelper.loginAndSearchAccountByGeneratePolicyObject(arUser.getUserName(),arUser.getPassword(),generatePolicy);

        bcAccountMenu.clickAccountMenuActionsNewTransactionChargeReversal();
        chargeReversalWizard.clickSearch();
        try{
            chargeReversalWizard.clickSelectButtonInChargeReversalTable(null, ChargeCategory.Membership_Dues.getValue(), null, null);
            chargeReversalWizard.setChargeReversalReason("Customer Request");
            chargeReversalWizard.setChargeReversalDescription("Reversing this charge as Charge_Manipulator");
            chargeReversalWizard.clickFinish();
        } catch (Exception e){
            e.printStackTrace();
            permissionBroke = true;
            getQALogger().info("Charge Manipulator failed to reverse dues on this account, please investigate ");
        }

        bcAccountMenu.clickBCMenuCharges();
        bcCommonCharges.clickChargeRow(null, null, null, ChargeCategory.Membership_Dues_Reversed, null, null, null, null, null, null, null, null, null, null, null, null);
        bcCommonCharges.clickReversalInformation();
        Assert.assertFalse(permissionBroke , "Test failed , please check the log to verify the reason of failure");
    }

    }
