package regression.r2.noclock.billingcenter.invoicing;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import regression.r2.noclock.policycenter.issuance.TestIssuance;
public class TestInvoicing extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null; 
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;

	@Test
	public void generate() throws Exception {
		TestIssuance issued = new TestIssuance();
		issued.testBasicIssuanceInsuredOnly();
		this.myPolicyObj = issued.myPolicyObjInsuredOnly;
	}
	@Test(dependsOnMethods = { "generate" })
	public void testIssuanceInvoicingAmountsAndDates() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        AccountInvoices acctInv = new AccountInvoices(driver);
        acctInv.verifyInvoiceAmount(this.myPolicyObj.accountNumber, new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).getTotalNetPremium(), new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).getMembershipDuesAmount(), this.myPolicyObj.paymentPlanType);
        acctInv.verifyInvoiceAndDueDate(this.myPolicyObj.busOwnLine.getEffectiveDate(), this.myPolicyObj.busOwnLine.getExpirationDate(), this.myPolicyObj.paymentPlanType);
	}	
}
