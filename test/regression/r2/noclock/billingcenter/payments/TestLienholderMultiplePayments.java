package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopLienholderMultiPayments;
import repository.bc.desktop.actions.DesktopActionsLienholderMutipleAccountPaymentWorkflow;
import repository.bc.search.BCSearchInvoices;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PolicyCompany;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
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
 * @Requirement AR Users need the ability to split lienholder checks out to multiple policies, lienholder locations, and to send payments elsewhere on one screen.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/LH%20Payments-Create%20Desktop%20Payments/12-04%20Lienholder%20Payments_Create%20from%20Desktop.docx">Business Requirements</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release 2 Requirements/12 - Payments/Supporting Documentation/LH Desktop Multiple Payment Screens.xlsx">Layout and Functionality</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/43531240987">Rally Story</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-04%20Multiple%20Account%20Lienholder%20Payment%20Screen.docx">US7259 Business Requirements</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52515993775">Rally Story US7259</a>
 * @Description This test goes to the new lienholder multiple account payments workflow page and makes various payments to test overall functionality. On 04/06/2016, functionality was added to
 * test that the Policy Number and Loan number are required when entering a payment. This is to avoid null pointer exceptions.
 * @DATE Oct 7, 2015 Updated 04/06/2016
 */
public class TestLienholderMultiplePayments extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser = new ARUsers();
	public double paymentAmount = 1450;
	public String policyNumber = null;
	
	public List<String> policyList = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
	{
		this.add("08-257577-01");
		this.add("08-126065-01");
		this.add("08-142249-01");
		this.add("08-057765-02");
		this.add("08-043822-09");		
	}};

	@Test
	public void testLienholderPaymentScreen() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
		invoiceSearch.findInvoiceByAccountAndAmountRange("98", 1000, 2000, InvoiceStatus.Due);

        AccountInvoices invoicesPage = new AccountInvoices(driver);
		this.policyNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(invoicesPage.getAccountInvoiceChargesTable(), 1, "Policy");
		this.policyNumber = this.policyNumber.substring(0, (this.policyNumber.length() - 2));
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultipleAccountLienholderPayment();
		

		DesktopActionsLienholderMutipleAccountPaymentWorkflow lienholderMultiplePayments = new DesktopActionsLienholderMutipleAccountPaymentWorkflow(driver);
		lienholderMultiplePayments.setAmount(-100);
		if (!lienholderMultiplePayments.isNextButtonDisabled()) {
			Assert.fail("The lienholder multiple payment amount used was negative, but was still allowed to go to the next page. This should never happen.");
		}
		lienholderMultiplePayments.setAmount(this.paymentAmount);
		lienholderMultiplePayments.clickNext();
		
		String paymentInstrumentCellValue = new TableUtils(driver).getCellTextInTableByRowAndColumnName(lienholderMultiplePayments.getDesktopActionsLienholderMultipleAccountPaymentsTable(), 1, "Payment Instrument");
		if (!paymentInstrumentCellValue.equals(PaymentInstrumentEnum.Check_Additional_Interest.getValue())) {
			Assert.fail("The Payment Instrument field did not default to " + PaymentInstrumentEnum.Check_Additional_Interest.getValue() + " As it should have. It defaulted to " + paymentInstrumentCellValue + ". Test failed.");
		}
		//Test for US7259
		int newestRow = lienholderMultiplePayments.fillOutNextLineOnPolicyPaymentsTable(15.00, null, null, null, null);
		lienholderMultiplePayments.clickNext();
		

		String errorMessage = null;
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();	
			if (!(errorMessage.equals("*Policy : Missing required field \"Policy\"") || errorMessage.equals("*Loan Number : Missing required field \"Choose Loan Number for Payment\""))) {
				Assert.fail("The error message displayed was not the message expected for not filling out the policy or loan number. The message displayed was \"" + errorMessage + "\". Test failed.");
			}
		} else {
			Assert.fail("The expected error banner never appeared after not filling out all required information and clicking the next button. Test failed.");
		}
		

		lienholderMultiplePayments.setPolicyNumber(this.policyNumber, newestRow);
		lienholderMultiplePayments.clickNext();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();	
			if (!(errorMessage.equals("*Policy : Missing required field \"Policy\"") || errorMessage.equals("*Loan Number : Missing required field \"Choose Loan Number for Payment\""))) {
				Assert.fail("The error message displayed was not the message expected for not filling out the policy or loan number. The message displayed was \"" + errorMessage + "\". Test failed.");
			}
		} else {
			Assert.fail("The expected error banner never appeared after not filling out all required information and clicking the next button. Test failed.");
		}

		lienholderMultiplePayments.setLoanNumber("random", newestRow);
		lienholderMultiplePayments.clickNext();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
			if (!errorMessage.contains("does not have an invoice stream. This payment will be applied to the account.")) {
				Assert.fail("There was an unhandled error. The error was \"" + errorMessage + "\". Test Failed.");
			}
		}
		
		lienholderMultiplePayments.clickBack();
		lienholderMultiplePayments.removeLineFromTable(this.policyNumber, 15.00, null);
		//End Test for US7259
		//Change the policies here to match data in a real environment when functionality is merged.
		lienholderMultiplePayments.fillOutNextLineOnPolicyPaymentsTable(300.00, null, PaymentInstrumentEnum.Cash, StringsUtils.getRandomStringFromList(policyList), "latestPolicyPeriod");
		lienholderMultiplePayments.fillOutNextLineOnPolicyPaymentsTable(650.00, null, PaymentInstrumentEnum.Check_Additional_Interest, StringsUtils.getRandomStringFromList(policyList), "latestPolicyPeriod");
		
		lienholderMultiplePayments.clickNext();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
			if (errorMessage.contains("has a canceled Policy Period. Please change the payment date to match the Postmarked Date.")) {
				throw new SkipException("One of the polies in the List @ policyList @ has an canceled policy period, If you are seeing this please go to the TestLienholderMultiplePayments test and add active polices which has LH.  The error was \"" + errorMessage + "\". Test Failed.");
			}
		}
		
		String twoHundredCharacterString = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qu";
		int currentRow = 0;
		try {
			currentRow = lienholderMultiplePayments.fillOutNextLineOnAdditionalPaymentsTable("random", 25.00, PaymentInstrumentEnum.Cash, (twoHundredCharacterString + "a"), true);
		} catch (Exception e) {
			Assert.fail("Unable to set all fields on the first additional payment. Test Failed.");
		}
		lienholderMultiplePayments.setPayRecaptureOption(false, currentRow);
		if (!new TableUtils(driver).getCellTextInTableByRowAndColumnName(lienholderMultiplePayments.getDesktopActionsLienholderMultipleAccountPaymentsTable(), currentRow, "Notes").equals(twoHundredCharacterString)) {
			Assert.fail("The check to ensure that 200 characters were visible on the notes section failed.");
		}
		
		lienholderMultiplePayments.fillOutNextLineOnAdditionalPaymentsTable("random", 100.00, null, "1:2:123 N. Main St, Chubbuck, ID 83202", null);
		lienholderMultiplePayments.fillOutNextLineOnAdditionalPaymentsTable("random", 125.00, null, "Intercompany Transfer to Farm Bureau for policy 01-123456-01", null);
		lienholderMultiplePayments.fillOutNextLineOnAdditionalPaymentsTable("random", 200.00, null, null, null);
		lienholderMultiplePayments.clickNext();
		
		if (lienholderMultiplePayments.getRemainingAmountToReconcile() > 0) {
			try {
				lienholderMultiplePayments.clickFinish();
				Assert.fail("The lienholder multiple payment workflow did not balance, but was still allowed to pass. This should never happen.");
			} catch (Exception e) {
				System.out.println("The Lienholder Multiple Payment workflow did not balance and will not continue unless it balances. Going back now to balance it.");
				lienholderMultiplePayments.clickBack();
				lienholderMultiplePayments.clickBack();
				lienholderMultiplePayments.setAmount(paymentAmount - lienholderMultiplePayments.getRemainingAmountToReconcile());
				lienholderMultiplePayments.clickNext();
				lienholderMultiplePayments.fillOutNextLineOnPolicyPaymentsTable(50.00, null, null, this.policyNumber, "random");
				if (lienholderMultiplePayments.notEqualWarningBannerExists()) {
					if (!(lienholderMultiplePayments.getNotEqualWarningBannerText().equals("Payment Received: $1,400.00 cannot be less than Applied Total: $1,450.00"))) {
						Assert.fail("The error message displayed was not the message expected for making payments for more than you entered on the first page. The message displayed was \"" + lienholderMultiplePayments.getNotEqualWarningBannerText() + "\". Test failed.");
					} else {
						getQALogger().info("The error message displayed was expected for making payments for more than you entered on the first page. Removing extra line now.");
						lienholderMultiplePayments.removeLineFromTable(this.policyNumber, 50.00, null);
					}
				} else {
					Assert.fail("The error message for making payments more than you entered on the first page never displayed. Test failed.");
				}
				lienholderMultiplePayments.clickNext();
				lienholderMultiplePayments.clickNext();
				lienholderMultiplePayments.clickFinish();
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	
	/**
	* @throws Exception 
	 * @Author bhiltbrand
	* @Requirement This test method contains a quick small test for US6090.
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-%2011%20Search%20LH%20Pymt.docx">Requirements Document</a>
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/46632298067">Rally Story</a>
	* @Description 
	* @DATE Dec 7, 2015
     */
	@Test (dependsOnMethods = { "testLienholderPaymentScreen" })
	public void verifyPaymentScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuLienholderMultiPayments();
		
		DesktopLienholderMultiPayments lienholderMultiPayments = new DesktopLienholderMultiPayments(driver);
		lienholderMultiPayments.selectLienholderMultiPaymentsCompany(PolicyCompany.Farm_Bureau);
		try {
			lienholderMultiPayments.clickLienholderMultiPaymentsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), 1400.00);
			Assert.fail("The correct payment record was found in the table, but the company chosen should have precluded it from filtered results. Test Failed.");
		} catch (Exception e) {
			getQALogger().info("The filtered results for company worked as expected.");
			lienholderMultiPayments.selectLienholderMultiPaymentsCompany(PolicyCompany.Western_Community);
			lienholderMultiPayments.setLienholderMultipPaymentsEarliestCreatedDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
			lienholderMultiPayments.clickLienholderMultiPaymentsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), 1400.00);
		}
		int paymentRows = lienholderMultiPayments.getLienholderMultiPaymentsBreakdownTableRowCount() - 1;
		if (!(paymentRows == 6)) {
			Assert.fail("The number of payments showing in the Lienholder Multi-Payments page did not match what was input in the workflow. Test Failed.");
		}
		new GuidewireHelpers(driver).logout();
	}
}
