package scratchpad.brett;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateMembershipOnlyPolicy extends BaseTest {
    public ARUsers arUser;
    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("Membership", "Policy")
                .withProductType(ProductLineType.Membership)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickAccountArrow();

        BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
        topMenuStuff.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);


        System.out.println("insLastName: " + myPolicyObj.pniContact.getLastName());
        System.out.println("insFirstName: " + myPolicyObj.pniContact.getFirstName());
        System.out.println("insCompanyName: " + myPolicyObj.pniContact.getCompanyName());
        System.out.println("accountNumber: " + myPolicyObj.accountNumber);
        System.out.println("effectiveDate: " + myPolicyObj.membership.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObj.membership.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObj.membership.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObj.membership.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObj.membership.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObj.membership.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObj.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObj.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObj.membership.getPolicyNumber());
    }
}
