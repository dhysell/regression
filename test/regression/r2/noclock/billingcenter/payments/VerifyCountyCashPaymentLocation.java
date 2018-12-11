package regression.r2.noclock.billingcenter.payments;

import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsCountyCashPayment;
import repository.bc.search.BCSearchMenu;
import repository.bc.search.BCSearchPayment;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;



/**
* @Author sgunda
* @Requirement 	DE5040 County Cash Payment Location Not Set
* @Description 	Check payment made on the county multiple payment screen for the Chubbuck office did not have the payment location set when viewing the payment from the account payment screen or on the payment search screen.
	
				The payment location should be county office and the ref should show the county and office number.
* @DATE Mar 13, 2017
*/
public class VerifyCountyCashPaymentLocation extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();		
	private String policyNumber = null;
	private CountyIdaho county = CountyIdaho.Ada;
	private String officeName="Eagle Office";
	private double paymentAmount=NumberUtils.generateRandomNumberInt(20, 100);
	private String warningMsgShouldBe="Missing required field \"Office #\"", bannerMsg;
	private String dateWarningMsg="Please change the payment date to match the Postmarked Date";
	private String officeRefNumber = "001-003 (Ada - Eagle Office)";
	
	@Test
	//find existing account number

	public void findRandomPolicynumber() throws Exception {
		this.arUser= ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		Calendar cal = Calendar.getInstance();
		String companyIs;
		
		switch (cal.get(Calendar.DAY_OF_MONTH) % 2) {
		case 0:
		try{
			this.policyNumber = policySearch.findPolicyInGoodStanding("275", null, PolicySearchPolicyProductType.Squire);
			companyIs = "IDFBINS";
			}
		catch(Exception e){
			System.out.println("No Good Standing Farm Bureau policy found");
			throw new SkipException("No good standing policy found , So this this test ends here ");
		}
			break;
		case 1:
		try{		
			this.policyNumber = policySearch.findPolicyInGoodStanding("171", null, PolicySearchPolicyProductType.Business_Owners);
			companyIs = "WCINS";

		}		
		catch(Exception e){
			System.out.println("No Good Standing Western community policy found");
			throw new SkipException("No good standing policy found , So this this test ends here ");
		}
			break;
		default:
			try {
				this.policyNumber = policySearch.findPolicyInGoodStanding("171", null, PolicySearchPolicyProductType.Business_Owners);
				companyIs = "WCINS";

		}		
		catch(Exception e){
			System.out.println("No Good Standing Western community policy found");
			throw new SkipException("No good standing policy found , So this this test ends here ");
			}
			break;
		}
		
		System.out.println(companyIs.concat(" policy number is = ").concat(this.policyNumber));
	}
	@Test(dependsOnMethods = { "findRandomPolicynumber" })	
	public void makeCountyCashPayment() throws Exception {		
		this.arUser= ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsCountyCashPayment();
		DesktopActionsCountyCashPayment countyCash=new DesktopActionsCountyCashPayment(driver);
		int nextAvailableLine=countyCash.getNextAvailableLineInTable("Policy #");
		//fill out all other fields but office # and verify the warning and "stop"
		countyCash.setCountyCashPolicyNumber(nextAvailableLine, policyNumber);
		System.out.println(policyNumber);
		countyCash.setCountyCashPaymentAmount(nextAvailableLine, paymentAmount);
		countyCash.selectCountyCashTableCountyCode(nextAvailableLine, county);
		countyCash.clickDesktopActionsCountyCashPmtNext();
		//after clicking Next, should have a hard stop and won't go to next page which has the Finish button
		try{
			countyCash.clickDesktopActionsCountyCashPmtFinish();
			Assert.fail("office number is empty, clicking Next button should be hard stopped.");
		}catch(Exception e){
			getQALogger().info("Clicking Next button is hard stopped when office number is empty, and this is expected.");
		}
		try{
			bannerMsg=new GuidewireHelpers(driver).getFirstErrorMessage();
			if(bannerMsg.contains(dateWarningMsg)){
				countyCash.setDate(nextAvailableLine,  DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
				countyCash.clickDesktopActionsCountyCashPmtNext();
				bannerMsg = new GuidewireHelpers(driver).getFirstErrorMessage();
			}			
			if(!bannerMsg.contains(warningMsgShouldBe)){
				Assert.fail("didn't find office number requirement warning.");
			}
		}catch(Exception e){
			Assert.fail("didn't find banner message.");
		}
		//fill out office number and finish the payment
		countyCash.selectCountyCashTableOfficeNumber(nextAvailableLine, officeName);
		countyCash.clickDesktopActionsCountyCashPmtNext();
		countyCash.clickDesktopActionsCountyCashPmtFinish();
	}
	
	
	@Test (dependsOnMethods = { "makeCountyCashPayment" })	
	public void verifyPayment() throws Exception {
		this.arUser= ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();
		BCSearchMenu searchMenu=new BCSearchMenu(driver);
		searchMenu.clickSearchMenuPayments();
		BCSearchPayment payment = new BCSearchPayment(driver);
		payment.setBCSearchPaymentsPolicyNumber(policyNumber);
		payment.clickSearch();

		WebElement paymentTable = payment.getBCSearchPaymentsSearchResultsTable();
		WebElement paymentInCountyOffice = new TableUtils(driver).getRowInTableByColumnNameAndValue(paymentTable, "Amount", Double.toString(paymentAmount));
		if (paymentInCountyOffice != null) {
			String offRefNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(paymentTable,
					new TableUtils(driver).getRowNumberFromWebElementRow(paymentInCountyOffice), "Ref #");
			
			System.out.println(offRefNumber);
			Assert.assertEquals(offRefNumber, officeRefNumber, "Distributed type is not NO");
		}
		else {
			Assert.fail("Row not found");
		}
	}
	
}
