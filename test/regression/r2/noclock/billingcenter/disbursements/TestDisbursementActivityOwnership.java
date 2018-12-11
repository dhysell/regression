package regression.r2.noclock.billingcenter.disbursements;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonSummary;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.helpers.BatchHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Description DE6200 - Automatic disbursement activity going to wrong supervisor
 * Actual: The activity that is triggered due to automatic disbursement is going to wrong supervisor.
 * Expected: The activity should just sit in the queue and would be assigned to whoever picks the activity.
 * @DATE Oct 2nd, 2017
 */
public class TestDisbursementActivityOwnership extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private BCSearchAccounts searchAccount;
	private String acctNumberFBMIS=null , acctNumberWCINS=null;
	
	private void gotoAccount(String accountNumber){
		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(accountNumber);
	
	}
		
	@Test
	public void findAccountNumber() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
	
		searchAccount=new BCSearchAccounts(driver);
		try{
		acctNumberFBMIS= searchAccount.findAccountInGoodStanding("01-");
		System.out.println("Farm Bureau Mutual account is " + acctNumberFBMIS);
		}
		catch(Exception e){
			System.out.println("No Good Standing Farm Bureau account found");
		}
		
		try{		
		acctNumberWCINS= searchAccount.findAccountInGoodStanding("08-");
		System.out.println("Western community insurance account is " + acctNumberWCINS);
		}		
		catch(Exception e){
			System.out.println("No Good Standing Western community account found");
		}

												
	}	
	
	@Test(dependsOnMethods = { "findAccountNumber" })
	public void payExcessToGetDisbursement() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);

		if(acctNumberFBMIS!=null){
		gotoAccount(acctNumberFBMIS);
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		directBillPmt.makeDirectBillPaymentExecuteWithoutDistribution(3000);
		}
				
		if(acctNumberWCINS!=null){
		gotoAccount(acctNumberWCINS);
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		directBillPmt.makeDirectBillPaymentExecuteWithoutDistribution(3000);
		}
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
	}
	
	@Test(dependsOnMethods = { "payExcessToGetDisbursement" })
	public void verifyDisbursementActivityForFBMIS() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		AccountDisbursements disbursements = new AccountDisbursements(driver);
		BCCommonSummary acctSummary = new BCCommonSummary(driver);

		if(acctNumberFBMIS!=null){
			gotoAccount(acctNumberFBMIS);
			double disbursementAmountFBMIS= acctSummary.getDefaultUnappliedFundsAmount();
			acctMenu.clickAccountMenuDisbursements();
			disbursements.verifyDisbursement(null, null, null, "Default", null, null, disbursementAmountFBMIS, null, null, "AR Supervisor Farm Bureau");
		}
		
		if(acctNumberWCINS!=null){
			gotoAccount(acctNumberWCINS);
			double disbursementAmountWCINS= acctSummary.getDefaultUnappliedFundsAmount();
			acctMenu.clickAccountMenuDisbursements();
			disbursements.verifyDisbursement(null, null, null, "Default", null, null, disbursementAmountWCINS, null, null, "AR Supervisor Western Community");
		}
	}
	
	
}
