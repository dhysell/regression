package regression.r2.noclock.billingcenter.payments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchMenu;
import repository.bc.search.BCSearchPayment;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPC;

/**
 * @Author sgunda
 * @Requirement DE5230  Payment location missing for payment applied from multiple payment screen
 * @DATE Apr 11, 2017
 */
public class VerifyPaymentLocationForPaymentsMadeOnMultiplePaymentScreen extends BaseTest {
	private WebDriver driver;
	private String policyNumber = null;
	private double paymentAmount=NumberUtils.generateRandomNumberInt(20, 100);
	
	@Test
	//find existing account number

	public void findRandomPolicynumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.findPolicyInGoodStanding("222", null, PolicySearchPolicyProductType.Business_Owners);
		System.out.println(policyNumber);
	}
	@Test(dependsOnMethods = { "findRandomPolicynumber" })	
	public void makeCountyCashPayment() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();
		DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		multiplePaymentsPage.makeMultiplePayment(policyNumber, PaymentInstrumentEnum.Cash, paymentAmount);

	}
	
	@Test (dependsOnMethods = { "makeCountyCashPayment" })	
	public void verifyPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();
		BCSearchMenu searchMenu=new BCSearchMenu(driver);
		searchMenu.clickSearchMenuPayments();
		BCSearchPayment payment = new BCSearchPayment(driver);
		payment.setBCSearchPaymentsPolicyNumber(policyNumber);
		payment.clickSearch();

		WebElement paymentTable = payment.getBCSearchPaymentsSearchResultsTable();
		WebElement paymentInMultiplePayment = new TableUtils(driver).getRowInTableByColumnNameAndValue(paymentTable, "Amount", Double.toString(paymentAmount));
		if (paymentInMultiplePayment != null) {
			String paymentLocation = new TableUtils(driver).getCellTextInTableByRowAndColumnName(paymentTable,
					new TableUtils(driver).getRowNumberFromWebElementRow(paymentInMultiplePayment), "Payment Location");
			
			System.out.println(paymentLocation);
			Assert.assertEquals(paymentLocation, "BillingCenter", "Distributed type is not NO");
		}
		else {
			Assert.fail("Row not found");
		}
	}

}
