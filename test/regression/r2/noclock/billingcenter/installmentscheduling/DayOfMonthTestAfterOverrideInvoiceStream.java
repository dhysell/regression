package regression.r2.noclock.billingcenter.installmentscheduling;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.driverConfiguration.Config;
import repository.gw.exception.GuidewireException;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import helpers.DateAddSubtractOptions;
import helpers.DateUtils;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author jqu
* @Name US5494 New Business Installment Scheduling
* @Description 	1) Override invoice stream day of month with policy period exp date if different from account level day of month (use lead policy expiration date)
*				2) Change current logic to use invoice stream day of month.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-02%20New%20Business%20Installment%20Scheduling.docx"></a>
* @Test Environment: Only works in Dinesh's vm now since our testing environment doesn't have multiple policies
* @DATE Jan. 22, 2016
*/
public class DayOfMonthTestAfterOverrideInvoiceStream extends BaseTest {
	//since we can not generate account with multiple policies, have to hard code for the variables.
	//private GeneratePolicy myPolicyObj = null;
	private WebDriver driver;
	private BCAccountMenu acctMenu;	
	private AccountInvoices acctInvoice;
	private BCPolicySummary pcSum;
	private PolicySummaryInvoicingOverrides chgStream;	
	private ARUsers arUser = new ARUsers();	
	private String accountNumber, policyNumber1, policyNumber2, policy1ExpDateStr, policy2ExpDateStr;
	private Date policy1ExpDate, policy2ExpDate;
	private List<Date> dueDateListFromUI;
	
	private void verifyPoliciesDatesAfterGenerate(Date policyExpDate, String policyNumber) throws GuidewireException{
		dueDateListFromUI= acctInvoice.getListOfDueDatesByInvoiceStream(policyNumber.substring(0, 7));
		int size=dueDateListFromUI.size();
		//the day in month of policy1's New DownPayment is not equal to the other 11 invoices' day in month
		if(policyNumber.equals(policyNumber2))
			size=dueDateListFromUI.size()-1;
		for(int i=1;i<=size;i++){
			Date eachDueDate=DateUtils.dateAddSubtract(policyExpDate, DateAddSubtractOptions.Month, -i);			
			if(!(eachDueDate.equals(dueDateListFromUI.get(dueDateListFromUI.size()-i))))			
				Assert.fail("found incorrect invoice Due Date for policy "+ policyNumber);
		}
	}
	@Test
	public void generate() throws Exception {		
		//need to generate an account with two policies (monthly), and the two policies have different expiration dates. The two policies have the same effective date
		accountNumber="100022";
		policyNumber1= "1000222-1";
		policy1ExpDateStr="01/25/2017";
		policy1ExpDate=DateUtils.convertStringtoDate(policy1ExpDateStr, "MM/dd/yyyy");
		policyNumber2= "1000223-1";		
		policy2ExpDateStr="01/27/2017";	
		policy2ExpDate=DateUtils.convertStringtoDate(policy2ExpDateStr, "MM/dd/yyyy");
	} 	
//	@Test(dependsOnMethods = { "generate" })	
	public void verifyDueDatesAfterGenerate() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		
		verifyPoliciesDatesAfterGenerate(policy1ExpDate, policyNumber1);
		verifyPoliciesDatesAfterGenerate(policy2ExpDate, policyNumber2);
	}
	//policy1 is overriden by policy2
	//after overrride, all invoice/due dates become policy2' invoice/due dates
//	@Test(dependsOnMethods = { "verifyDueDatesAfterGenerate" })		
	public void makeOverrideAndVerifyDates() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), policyNumber1);
        pcSum = new BCPolicySummary(driver);
		pcSum.updateInvoicingOverride();
		chgStream = new PolicySummaryInvoicingOverrides(driver);
		chgStream.selectOverridingInvoiceStream(policyNumber2.substring(0, 7));
		chgStream.clickNext();
		chgStream.clickFinish();
		InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		List<Date> dueDateListAfterOverride=acctInvoice.getListOfDueDates();
		if(dueDateListAfterOverride.size()!=dueDateListFromUI.size())
			Assert.fail("The due date list size after policy override is not correct.");
		for(int i=dueDateListAfterOverride.size()-1; i>=0; i--){
			if(!dueDateListAfterOverride.get(i).equals(dueDateListFromUI.get(i))){
				getQALogger().error("The Due Date after policy override is "+dueDateListAfterOverride.get(i));
				getQALogger().error("while the Due Date before policy override is "+dueDateListFromUI.get(i));
				Assert.fail("The due date list policy override is not correct.");
			}
		}
	}
}
