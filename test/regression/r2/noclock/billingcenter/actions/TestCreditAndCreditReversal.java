package regression.r2.noclock.billingcenter.actions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreditReversalWizard;
import repository.bc.wizards.NewCreditWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreditType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class TestCreditAndCreditReversal extends BaseTest {
	private WebDriver driver;
	private String accountNumber = "";
	private String policyNumber = "";
	private ARUsers arUser;
	
	//PLEASE BE AWARE THIS TEST REQUIRES AN ENVIRONMENT THAT HAS HAD THE ACCOUNT LEVEL INVOICE STREAMS CONVERTED TO POLICY LEVEL!
	/**
	* @Author bhiltbrand
	* @Requirement Users should have the ability to issue and reverse Ad-Hoc credits as needed against Accounts.
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/11%20-%20Ad%20Hoc%20Credit%20Transactions/17-11%20Ad%20Hoc%20Credit.docx">Issue and Reverse Credits</a>
	* @DATE Oct 16, 2015
	* @throws Exception
	*/
	
	@Test
	public void findAccountAndCreateCredit() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		this.accountNumber = accountSearch.findDelinquentAccount();

        BCAccountMenu sideMenu = new BCAccountMenu(driver);
		sideMenu.clickAccountMenuPolicies();
		

        AccountPolicies policies = new AccountPolicies(driver);
		this.policyNumber = policies.getPolicyNumberByRowNumber(1);
		this.policyNumber = this.policyNumber.substring(0, 12);
		

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuActionsNewTransactionCredit();


		NewCreditWizard newCreditWizard = new NewCreditWizard(driver);
		
		for (String listItem:newCreditWizard.getUnappliedFundDropdownList()) {
			if(listItem.equalsIgnoreCase("default")) {
				Assert.fail("The Unapplied Funds Dropdown contained a \"default\" option. This is incorrect and should not exist.");
			} else if (listItem.equalsIgnoreCase("<none>")) {
				Assert.fail("The Unapplied Funds Dropdown contained a \"<none>\" option. This is incorrect and should not exist. "
						+ "This probably indicates that either no policy is attached to the account, or that the billing method has "
						+ "not yet been converted to Policy-Level Billing. The account was " + this.accountNumber + ". This will most "
						+ "likely be an issue until invoicing streams conversion can pick up on invoice streams that don't have planned "
						+ "invoices left to bill. Please run the test again to get a different account.");
			}
		}
		
		newCreditWizard.setUnappliedFund(this.policyNumber);
		
		for (String listItem:newCreditWizard.getCreditTypeDropdownList()) {
			if (!listItem.equalsIgnoreCase(CreditType.None.getValue()) && !listItem.equalsIgnoreCase(CreditType.Bank_Fee.getValue()) && !listItem.equalsIgnoreCase(CreditType.Reinstatement_Fee.getValue()) && !listItem.equalsIgnoreCase(CreditType.Other.getValue())) {
				Assert.fail("The Credit Type Dropdown contained an option other than what is listed in requirements. The option was " + listItem + ".");
			}
		}
		newCreditWizard.setCreditType(CreditType.Bank_Fee);
		newCreditWizard.setAmount(253.00);
		newCreditWizard.clickNext();
		

		newCreditWizard.clickFinish();
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test (dependsOnMethods = { "findAccountAndCreateCredit" })
    public void createCreditReversal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickAccountMenuActionsNewTransactionCreditReversal();


		CreditReversalWizard creditReversal = new CreditReversalWizard(driver);
		creditReversal.setMinimumAmount(253.00);
		creditReversal.clickSearch();
		creditReversal.selectCreditToReverseByRowNumber(1);

		if(!new GuidewireHelpers(driver).containsErrorMessage("Warning: Reversing a credit might cause unapplied to go negative!")) {
			Assert.fail("The warning message regarding the possibility of causing unapplied funds to go negative was not found.");
		}
		
		creditReversal.clickFinish();
		
		new GuidewireHelpers(driver).logout();
	}
}
