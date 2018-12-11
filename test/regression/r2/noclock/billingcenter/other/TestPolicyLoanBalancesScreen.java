package regression.r2.noclock.billingcenter.other;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.bc.wizards.NewTransferWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.enums.DisbursementClosedStatusFilter;
import repository.gw.enums.DisbursementStatusFilter;
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
 * @Requirement New Policy Loan Balances Screen for Lienholders in BC has various UI requirements.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-03%20LH%20Buckets%20-%20Policy%20Loan%20Balances%20Screen.docx">Requirements Documentation</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52167153078">Rally Story US7216</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52167297490">Rally Story US7217</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52166500544">Rally Story US7191</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52167516088">Rally Story US7222</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-03%20LH%20Buckets%20-%20Policy%20Loan%20Balances%20Screen.docx">Requirements Documentation For Actions Buttons</a>
 * @Description This test verifies that the new Policy Loan Balances Screen contains the required items as stated in the requirements documentation.
 * @DATE Apr 4, 2016 Updated Apr 7, 2016 with new test for Actions Buttons.
 */
public class TestPolicyLoanBalancesScreen extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	
	@Test
	public void verifyPolicyLoanBalancesPage() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickSearchTab();

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.findRecentAccountInGoodStanding(null, AccountType.Lienholder);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		try {
			accountMenu.clickAccountMenuPolicyLoanBalances();
		} catch (Exception e) {
			Assert.fail("The \"Policy Loan Balances\" page was not available. Test Failed.");
		}

		AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		boolean found = false;
		int i = 0;
		while ((i < 5) && (!found)) {
			try {
				List<String> policyFilterListContents = policyLoanBalances.getPolicyFilterListContents();
				String randomPolicyForPolicyFilter = StringsUtils.getRandomStringFromList(policyFilterListContents, "All Policies");
				found = true;
				policyLoanBalances.selectPolicyFilter(randomPolicyForPolicyFilter);
			} catch (Exception e) {
				e.printStackTrace();
				i++;
				getQALogger().info("Unable to find a suitable LH account. Searching for another one.");
				
				topMenu = new BCTopMenu(driver);
				topMenu.clickSearchTab();

				accountSearch = new BCSearchAccounts(driver);
				accountSearch.findRecentAccountInGoodStanding(null, AccountType.Lienholder);

                accountMenu = new BCAccountMenu(driver);
				accountMenu.clickAccountMenuPolicyLoanBalances();
			}
		}
		if(!found) {
			Assert.fail("An error occurred when trying to filter the Policy loan Balanaces Screen by Policy Number. Test Failed.");
		}
		

		try {
			List<String> loanNumberFilterListContents = policyLoanBalances.getloanNumberFilterListContents();
			List<String> loanNumbersFromTable = new TableUtils(driver).getAllCellTextFromSpecificColumn(policyLoanBalances.getPolicyLoanBalancesTable(), "Loan Number");
			boolean nullLoanNumber = false;
			for (String loanNumber : loanNumbersFromTable) {
				if (loanNumber.equals(" ")) {
					nullLoanNumber = true;
					break;
				}
			}
			boolean nullOptionFound = false;
			if (nullLoanNumber) {
				for (String loanNumberFromFilter : loanNumberFilterListContents) {
					if (loanNumberFromFilter.equals("<None>")) {
						nullOptionFound = true;
						break;
					}
				}
				if (!nullOptionFound) {
					Assert.fail("There were null loan numbers in the table of policies, but the \"<None>\" option to select them was not found in the Loan Number Filter. Test failed.");
				}
			}
			String randomLoanNumberForLoanNumberFilter = StringsUtils.getRandomStringFromList(loanNumberFilterListContents, "All Loan Numbers");
			policyLoanBalances.selectLoanNumberFilter(randomLoanNumberForLoanNumberFilter);
		} catch (Exception e) {
			Assert.fail("An error occurred when trying to filter the Policy loan Balanaces Screen by Loan Number. Test Failed.");
		}
		
		try {
			policyLoanBalances.clickClearFilters();
		} catch (Exception e) {
			Assert.fail("The attempt to click the clear filters button failed. Test Failed.");
		}
		
		try {
			policyLoanBalances.clickPolicyLoanBalancesChargesTab();
		} catch (Exception e) {
			Assert.fail("The \"Charges\" tab was not available. Test Failed.");
		}
		
		try {
			policyLoanBalances.clickPolicyLoanBalancesPaymentsTab();
		} catch (Exception e) {
			Assert.fail("The \"Payments\" tab was not available. Test Failed.");
		}
		
		try {
			policyLoanBalances.clickPolicyLoanBalancesDisbursementsTab();
		} catch (Exception e) {
			Assert.fail("The \"Disbursements\" tab was not available. Test Failed.");
		}
		

		try {
			policyLoanBalances.selectAccountPolicyLoanBalancesDisbursementsDateRangeFilter(DisbursementClosedStatusFilter.Closed_Last_Sixty_Days);
			policyLoanBalances.selectAccountPolicyLoanBalancesDisbursementsStatusFilter(DisbursementStatusFilter.Reapplied_Only);
		} catch (Exception e) {
			Assert.fail("The filters on the Disbursements tab did not work as expected. Test Failed.");
		}
		
		try {
			policyLoanBalances.clickPolicyLoanBalancesTransfersTab();
		} catch (Exception e) {
			Assert.fail("The \"Transfers\" tab was not available. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test (dependsOnMethods = { "verifyPolicyLoanBalancesPage" })
	public void testPolicyLoanBalancesPageActionsMenus() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickSearchTab();

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.findRecentAccountInGoodStanding(null, AccountType.Lienholder);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

		AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		int randomRow = new TableUtils(driver).getRandomRowFromTable(policyLoanBalances.getPolicyLoanBalancesTable());
		String policyNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesTable(), randomRow, "Policy");
		String loanNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(policyLoanBalances.getPolicyLoanBalancesTable(), randomRow, "Loan Number");
		if (loanNumber.equals(" ")) {
			loanNumber = "";
		}
		
		try {
			policyLoanBalances.clickActionsNewDirectBillPaymentLink(randomRow);
		} catch (Exception e) {
			Assert.fail("The attempt to click on the New Direct Bill Payment link on the actions menu failed. Test Failed.");
		}

        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
		String policyNumberFromDirectBillPaymentScreen = directBillPayment.getPolicyNumber();
		if (!policyNumberFromDirectBillPaymentScreen.equals(policyNumber)) {
			Assert.fail("The Policy Number field did not populate with the correct policy number. Test Failed.");
		}
		
		String loanNumberFromDirectBillPaymentScreen = directBillPayment.getLoanNumber();
		if (!loanNumberFromDirectBillPaymentScreen.equals(loanNumber)) {
			Assert.fail("The Loan Number field did not populate with the correct loan number. Test Failed.");
		}
		
		if (directBillPayment.checkIfPolicyNumberIsEditable()) {
			Assert.fail("The Policy Number was editable. In this instance, this should not be the case. Test Failed.");
		}
		
		if (directBillPayment.checkIfLoanNumberIsEditable()) {
			Assert.fail("The Loan Number was editable. In this instance, this should not be the case. Test Failed.");
		}
		
		directBillPayment.clickCancel();

		try {
			policyLoanBalances.clickActionsDisbursementLink(randomRow);
		} catch (Exception e) {
			Assert.fail("The attempt to click on the disbursement link on the actions menu failed. Test Failed.");
		}

		CreateAccountDisbursementWizard accountDisbursementWizard = new CreateAccountDisbursementWizard(driver);
		String policyNumberFromDisbursementScreen = accountDisbursementWizard.getCreateAccountDisbursementWizardPolicyNumber();
		if (!policyNumberFromDisbursementScreen.equals(policyNumber)) {
			Assert.fail("The Policy Number field did not populate with the correct policy number. Test Failed.");
		}
		
		String loanNumberFromDisbursementScreen = accountDisbursementWizard.getCreateAccountDisbursementWizardLoanNumber();
		if (!loanNumberFromDisbursementScreen.equals(loanNumber)) {
			Assert.fail("The Loan Number field did not populate with the correct loan number. Test Failed.");
		}
		
		accountDisbursementWizard.clickCancel();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);

		try {
			policyLoanBalances.clickActionsTransferLink(randomRow);
		} catch (Exception e) {
			Assert.fail("The attempt to click on the Transfer link on the actions menu failed. Test Failed.");
		}

		NewTransferWizard transferWizard = new NewTransferWizard(driver);
		String policyNumberFromTransfersScreen = transferWizard.getNewTransferWizardPolicyNumber();
		if (!policyNumberFromTransfersScreen.equals(policyNumber)) {
			Assert.fail("The Policy Number field did not populate with the correct policy number. Test Failed.");
		}
		
		String loanNumberFromTransfersScreen = transferWizard.getNewTransferWizardLoanNumber();
		if (!loanNumberFromTransfersScreen.equals(loanNumber)) {
			Assert.fail("The Loan Number field did not populate with the correct loan number. Test Failed.");
		}
		
		transferWizard.clickAdd();
		transferWizard.setTransferTableTargetAccount(1, "randomInsured");
		try {
			transferWizard.selectTransferTableTargetPolicyNumber(1, "random");
		} catch (Exception e) {
			Assert.fail("The Target Policy drop-down in the transfer table either was inaccessible or did not have any information contained therein. Test Failed.");
		}
		try {
			transferWizard.selectTransferTableTargetLoanNumber(1, "random");
		} catch (Exception e) {
			Assert.fail("The Target Loan drop-down in the transfer table either was inaccessible or did not have any information contained therein. Test Failed.");
		}
		
		transferWizard.clickCancel();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);

		//The recapture link doesn't show up in the menu unless there is first a disbursement and a reversed corresponding charge. Therefore, this section is invalid, and is covered
		//in other policy loan balances screen tests. Leaving this block here for posterity.
		/*try {
			policyLoanBalances.clickActionsRecaptureLink(randomRow);
		} catch (Exception e) {
			Assert.fail("The attempt to click on the Recapture link on the actions menu failed. Test Failed.");
		}

		NewRecaptureWizard recaptureWizard = new NewRecaptureWizard(driver);
		String policyNumberFromRecaptureScreen = recaptureWizard.getNewRecaptureWizardPolicyNumber();
		if (!policyNumberFromRecaptureScreen.equals(policyNumber)) {
			Assert.fail("The Policy Number field did not populate with the correct policy number. Test Failed.");
		}
		
		String loanNumberFromRecaptureScreen = recaptureWizard.getNewRecaptureWizardLoanNumber();
		if (!loanNumberFromRecaptureScreen.equals(loanNumber)) {
			Assert.fail("The Loan Number field did not populate with the correct loan number. Test Failed.");
		}
		
		transferWizard.clickCancel();
		selectOKOrCancelFromPopup(OkCancel.OK);
		*/
		
		new GuidewireHelpers(driver).logout();
	}

}
