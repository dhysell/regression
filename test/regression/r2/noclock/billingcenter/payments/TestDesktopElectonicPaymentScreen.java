package regression.r2.noclock.billingcenter.payments;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsElectronicPaymentEntry;
import repository.bc.search.BCSearchPayment;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuSearch;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that payments made through the electronic payment screen show up with the correct payment
* location (i.e. - Nexus or Public Website) when searching for those payments.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/118154494444">Rally Defect DE5577</a>
* @Description 
* @DATE Jun 09, 2017
*/
public class TestDesktopElectonicPaymentScreen extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	private String policyNumber1 = null;
	private String policyNumber2 = null;
	
	@Test(enabled=true)
	public void verifyPolicyLoanBalancesPage() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber1 = policySearch.findPolicyInGoodStanding("259", null, null);
		this.policyNumber2 = policySearch.findPolicyInGoodStanding("259", null, null);
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsElectronicPayment();
		

		DesktopActionsElectronicPaymentEntry electronicPaymentsPage = new DesktopActionsElectronicPaymentEntry(driver);
		electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(policyNumber1, PaymentInstrumentEnum.ACH_EFT, PaymentLocation.WebSite, 20.00);
		electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(policyNumber2, PaymentInstrumentEnum.Credit_Debit, PaymentLocation.NexusPayment, 15.00);
		electronicPaymentsPage.clickNext();
		electronicPaymentsPage.clickFinish();


		BCTopMenuSearch topMenuSearch = new BCTopMenuSearch(driver);
		topMenuSearch.clickPayments();


		BCSearchPayment paymentSearch = new BCSearchPayment(driver);
		paymentSearch.clickSearch();


		new TableUtils(driver).sortByHeaderColumn(paymentSearch.getBCSearchPaymentsSearchResultsTable(), "Payment Date");
		try {
			paymentSearch.getBCSearchPaymentsSearchResultsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), 20.00, null, this.policyNumber1, null, null, PaymentInstrumentEnum.ACH_EFT, null, null, PaymentLocation.WebSite, null);
		} catch (Exception e) {
			Assert.fail("The Payment search results row containing the payment made for 20 dollars to policy # " + this.policyNumber1 + " and marked as from the public website was not found. Test failed.");
		}
		
		try {
			paymentSearch.getBCSearchPaymentsSearchResultsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), 15.00, null, this.policyNumber2, null, null, PaymentInstrumentEnum.Credit_Debit, null, null, PaymentLocation.NexusPayment, null);
		} catch (Exception e) {
			Assert.fail("The Payment search results row containing the payment made for 15 dollars to policy # " + this.policyNumber2 + " and marked as from Nexus Payment Portal was not found. Test failed.");
		}
		new GuidewireHelpers(driver).logout();
	}
}
