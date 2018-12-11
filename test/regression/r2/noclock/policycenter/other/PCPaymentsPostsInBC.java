package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sbroderick
 * @Requirement :10754 Maintenance - Update Payment Schema to include the new Payment Division
 * @Description :  Outgoing payments now have a field that will allow client systems to indicate a payment�s division (think: IDFBINS, WCINS, IDFBFS, etc.).
 * This happens on the back end and we should see no change to payments as we currently process payments on the front end.
 * @DATE April 4, 2017
 */
@QuarantineClass
public class PCPaymentsPostsInBC extends BaseTest {

    private GeneratePolicy myPolicy = null;
    private ARUsers arUser = null;

    private WebDriver driver;

    @Test(description = "Create Policy and Make Credit Card Payment")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicy = new GeneratePolicy.Builder(driver)
                .withPolTermLengthDays(81)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Credit_Debit)
                //Change based on how far the test needs to go
                .build(GeneratePolicyType.PolicyIssued);

        verifyPayment();
    }


    public void verifyPayment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicy.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPayments();

        AccountPayments payment = new AccountPayments(driver);
        Assert.assertEquals(payment.getPaymentAmount(1), this.myPolicy.busOwnLine.getPremium().getDownPaymentAmount(), "Ensure that the downpayment amount in PC matches the payment received in BC on account " + myPolicy.accountNumber + ".");

        new GuidewireHelpers(driver).logout();
    }

    @Test
    public void makeACH() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        BankAccountInfo bankAcctInfo = new BankAccountInfo();

        this.myPolicy = new GeneratePolicy.Builder(driver)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .withBankAccountInfo(bankAcctInfo)
                //Change based on how far the test needs to go
                .build(GeneratePolicyType.PolicyIssued);

        driver.quit();
        verifyPayment();
        getACHInfo();
    }


    public void getACHInfo() {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicy.accountNumber);
        BCPolicySummary summaryPage = new BCPolicySummary(driver);
        Assert.assertTrue(summaryPage.getPaymentInstrumentValue().contains(this.myPolicy.bankAccountInfo.getBankName().toUpperCase()), "The Payment Instrument should come over from PC.");
    }
}
