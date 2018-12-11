package scratchpad.steve.utilities;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPaymentRequests;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class PaymentLoaderNPP extends BaseTest {

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
        driver.quit();
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
        driver.quit();
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

        verifyPayment();
        getACHInfo();
    }


    public void getACHInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
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
	
	/*@Test
	public void makeBCPayments() throws Exception{
		VerifyMonthlyInvoicingAfterClockMovingAndPolicyChange payBC = new VerifyMonthlyInvoicingAfterClockMovingAndPolicyChange();
		payBC.policyIssued();
		payDownInBC(payBC.getPolicyObject());
		verifyElectronicPayment();
		
	}*/

    public void verifyElectronicPayment() {

        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicy.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        AccountInvoices acctInv = new AccountInvoices(driver);
//		ArrayList<Date> bcInvDate = acctInv.getInvoiceDateFromBC();
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
        AccountPaymentsPaymentRequests payRequest = new AccountPaymentsPaymentRequests(driver);
//		double paymentRequestAmount = payRequest.getPaymentRequestAmount("Planned", bcInvDate.get(1), null, null);
        acctMenu.clickAccountMenuInvoices();
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
//		newPayment.makeDirectBillPaymentExecute(paymentRequestAmount, null, PaymentInstrument.ACH_EFT, false);
        //check that Payment Request is now Closed
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPaymentsPaymentRequests();
        payRequest = new AccountPaymentsPaymentRequests(driver);
//		payRequest.checkPaymentRequestStatus(bcInvDate.get(1), null, null, null, "Canceled");
    }

    public void payDownInBC(GeneratePolicy policyObj) {

        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }
        String arUsername = arUser.getUserName();
        String arPassword = arUser.getPassword();

        new Login(driver).loginAndSearchAccountByAccountNumber(arUsername, arPassword, policyObj.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInv = new AccountInvoices(driver);
//		ArrayList<Date> bcInvDate = acctInv.getInvoiceDateFromBC();
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(guidewireHelpers.getPolicyPremium(policyObj).getDownPaymentAmount(), null, PaymentInstrumentEnum.Credit_Debit, false);
        guidewireHelpers.logout();
    }
}
