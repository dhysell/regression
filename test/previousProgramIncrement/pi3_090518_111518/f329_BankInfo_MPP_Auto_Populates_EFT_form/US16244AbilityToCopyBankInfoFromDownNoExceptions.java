package previousProgramIncrement.pi3_090518_111518.f329_BankInfo_MPP_Auto_Populates_EFT_form;

import repository.bc.policy.summary.BCPolicySummary;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class US16244AbilityToCopyBankInfoFromDownNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;

    @Test
    public void testGeneratePLAutoOnlyPolicy() throws  Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy = new GeneratePolicyHelper(driver).generatePLSectionIIIPersonalAutoLinePLPolicy("CopyBankInfo","Final",null,PaymentPlanType.Monthly,PaymentType.ACH_EFT);

    }

    @Test(dependsOnMethods = { "testGeneratePLAutoOnlyPolicy" })
    public void verifyBCGetTheCopiedBankInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(),arUser.getPassword(), generatePolicy.squire.getPolicyNumber());

        Assert.assertFalse(new BCPolicySummary(driver).getPaymentInstrumentValue().contains(PaymentInstrumentEnum.Responsive.getValue()),"Payment Instrument Value is still responsive, Please verify");

    }
}
