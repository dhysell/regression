/*
 * By: Jessica
 * Test: Account History 
 * */
package regression.r2.noclock.billingcenter.payments;

import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonHistory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class AccountHistoryTest extends BaseTest {
	private WebDriver driver;
	private Date openDate;
	private ARUsers arUser = new ARUsers();
	private BCSearchAccounts searchAccount;
	private String acctNumber= null;

    public String getFBMISOrWCINSAccountNumber() {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		searchAccount = new BCSearchAccounts(driver);

		switch (dayOfMonth % 2) {
		case 0:
			try {
				acctNumber = searchAccount.findRecentAccountInGoodStanding("01", AccountType.Insured);
				System.out.println("Farm Bureau Mutual account is " + acctNumber);
			} catch (Exception e) {
				throw new SkipException("Sorry, no good standing Farm Bureau account found. Test cannot continue.");
			}

			break;
		case 1:
			try {
				acctNumber = searchAccount.findRecentAccountInGoodStanding("08", AccountType.Insured);
				System.out.println("Western community insurance account is " + acctNumber);
			} catch (Exception e) {
				throw new SkipException("Sorry, no good standing Western community account found. Test cannot continue.");			}
			break;
		default:
			try {
				acctNumber = searchAccount.findRecentAccountInGoodStanding("08", AccountType.Insured);
				System.out.println("Western community insurance account is " + acctNumber);
			} catch (Exception e) {
				throw new SkipException("Sorry, no good standing Western community account found. Test cannot continue.");
			}
			break;

		}
		
		return acctNumber;

	}

	@Test
    public void findGoodStandingAccount() throws Exception {

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		getFBMISOrWCINSAccountNumber();
		
	}

	/**
	 * @author ryoung
	 * @Requirement -
	 * @Link - <a href="http://www.bing.com">Bing</a>
	 * @Description -
	 * @DATE - Sep 23, 2015
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "findGoodStandingAccount" })
	public void testAccountHistoryReview() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		openDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),	arUser.getPassword(), acctNumber);

        BCAccountMenu myAcctMenu = new BCAccountMenu(driver);
        myAcctMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment myPmt = new NewDirectBillPayment(driver);
		double amtToFill = 111.11;
		myPmt.makeDirectBillPaymentExecuteWithoutDistribution(amtToFill);
		myAcctMenu.clickBCMenuHistory();
		BCCommonHistory myAcctHistory = new BCCommonHistory(driver);
		if (!myAcctHistory.historyTableExists()) {
			Assert.fail("Could not find the Account History results table.\n");
		} else {
			String newItem = "Payment made on this account for amount " + amtToFill + " by " + arUser.getFirstName()+ " " + arUser.getLastName() + ".";
			if (!myAcctHistory.verifyHistoryTable(openDate, newItem)) {
				Assert.fail("Could not find new history item in the table.\n.");
			}
		}
	}
}
