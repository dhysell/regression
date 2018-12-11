package coreBusiness.delinquencies.renewaltermornottakeninsuredfullcancel;


import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.TransactionNumber;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Calendar;




public class TestDelinquenciesOnRenewalTerm extends BaseTest {

	private ARUsers arUser;
	private BCAccountMenu acctMenu;
	private BCCommonDelinquencies delinquency;
	private GeneratePolicy myPolicyObj;
	private WebDriver driver;

	@Test
	public void SquirePolicy() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy("InsuredThres", "NTaken",100, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash);

		Assert.assertNotNull(myPolicyObj,"Generate failed, please investigate from logs");
	}

	@Test(dependsOnMethods = {"SquirePolicy"})
	public void makeDownPaymentAndMoveClockToRenew() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());	

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -50));
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
	public void renewlPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);
	}

	@Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
	public void addCarryOverIfNeededAndMakeChargesDelinquent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();

		BCCommonCharges charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Renewal);

		if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)%2 != 0) {
			charges.clickCarryOverChargeAndCreateChargeAndVerifyIfCreatedOrNot(myPolicyObj.accountNumber, TransactionNumber.Premium_Charged, repository.gw.enums.TransactionType.Renewal, myPolicyObj.squire.getPolicyNumber(), repository.gw.enums.TrueFalse.True, 300);
		}

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -15));
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, myPolicyObj.squire.getExpirationDate());
		new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		acctMenu.clickBCMenuDelinquencies();
		delinquency = new BCCommonDelinquencies(driver);

		Assert.assertTrue(delinquency.verifyDelinquencyByReason(null, repository.gw.enums.DelinquencyReason.NotTaken,null, myPolicyObj.squire.getExpirationDate()), "Not Taken did not trigger, on Renewal non-pay");

		if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)%2 != 0) {
			new GuidewireHelpers(driver).logout();

			new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.PC_Workflow);
			new GuidewireHelpers(driver).logout();

			cf.setCenter(ApplicationOrCenter.BillingCenter);
			new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
			acctMenu.clickBCMenuCharges();
			charges.waitUntilChargesFromPolicyContextArrive(240, repository.gw.enums.TransactionType.Cancellation);
			acctMenu.clickBCMenuDelinquencies();
			Assert.assertTrue(delinquency.verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.UnderwritingFullCancel,null,myPolicyObj.squire.getExpirationDate()),"Underwriting Full Cancel has not trigger even after receiving cancellation charges from PC");
		}
	}

}
