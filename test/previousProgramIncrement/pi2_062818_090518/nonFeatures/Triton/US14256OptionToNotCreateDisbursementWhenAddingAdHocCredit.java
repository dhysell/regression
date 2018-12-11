package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.search.BCSearchPolicies;
import repository.bc.wizards.NewCreditWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreditType;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
/**
* @Author JQU
* @Requirement 	US14256 -- Allow users an option to not create a disbursement when adding an AdHoc credit
* 				Ensure that a checkbox is view able on the UI 17-11-11
				Ensure that when checking the box the credit does not generate a disbursement 17-11-11			
* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/11%20-%20Ad%20Hoc%20Credit%20Transactions/17-11%20Ad%20Hoc%20Credit.docx&action=default">17-11 Ad Hoc Credit</a>
* @DATE August 16, 2018
*/
public class US14256OptionToNotCreateDisbursementWhenAddingAdHocCredit extends BaseTest{
	private WebDriver driver;
	private String policyNumber = null;
	private double bankFeeNotToDisburse=NumberUtils.generateRandomNumberInt(20, 200);
	private double bankFeeToDisburse=NumberUtils.generateRandomNumberInt(201, 300);
	private ARUsers arUser = new ARUsers();
	private void createCredit(double creditAmount, boolean createDisburse){				
		NewCreditWizard creditWizard = new NewCreditWizard(driver);
		creditWizard.setCreditType(CreditType.Bank_Fee);
		creditWizard.setAmount(creditAmount);		
		creditWizard.setCreateDisbursement(createDisburse);
		creditWizard.clickNext();
		creditWizard.clickFinish();
	}
	@Test
	public void findRandomPolicynumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.findPolicyInGoodStanding("2965", null, PolicySearchPolicyProductType.Squire);
		System.out.println(policyNumber);
	}
	@Test(dependsOnMethods = { "findRandomPolicynumber" })	
	public void testNotCreateDisbursement() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), policyNumber.substring(3, 9));
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionCredit();		
		createCredit(bankFeeNotToDisburse, false);
		//verify disbursement
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		assertFalse(disbursement.verifyDisbursement(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, policyNumber.substring(3, 9), DisbursementStatus.Sent, null, bankFeeNotToDisburse, null, null, null), "Disbursement should not be created for this bank fee.");
	}
	//Create Disbursement is checked by default.
	@Test(dependsOnMethods = { "testNotCreateDisbursement" })	
	public void testCreateDisbursement() throws Exception {				
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), policyNumber.substring(3, 9));
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionCredit();		
		createCredit(bankFeeToDisburse, true);
		
		//verify disbursement
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		assertTrue(disbursement.verifyDisbursement(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, policyNumber.substring(3, 9), DisbursementStatus.Sent, null, bankFeeToDisburse, null, null, null), "Disbursement should be created for this bank fee.");
	}
}
