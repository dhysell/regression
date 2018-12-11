package regression.r2.clock.billingcenter.cancel;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.ChargeObject;
import repository.pc.policy.PolicyChargesToBC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestLHPartialCancelSquire extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private List<ChargeObject> chargesList = null;
	private String policyChangeDescription = "Remove Lienholder";
	private double delinquentAmount;

	@Test
	public void generatePolicy() throws Exception {


		ArrayList<GenerateContact> generateContacts= new ArrayList<>();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<repository.gw.enums.ContactRole> lienOneRolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		lienOneRolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

		GenerateContact lienOne = new GenerateContact.Builder(driver)
				.withCompanyName("LH From Hell")
				.withRoles(lienOneRolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(repository.gw.enums.GenerateContactType.Company);

		generateContacts.add(lienOne);
		driver.quit();

		cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);

		ArrayList<repository.gw.enums.ContactRole> lienTwoRolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		lienTwoRolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

		GenerateContact lienTwo = new GenerateContact.Builder(driver)
				.withCompanyName("LH From Heaven")
				.withRoles(lienTwoRolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);

		generateContacts.add(lienTwo);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePolicyWithTwoLiens(null ,null,null,null,null,generateContacts);

	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Month, 1);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

		repository.gw.helpers.ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(repository.gw.enums.OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

        this.delinquentAmount = delinquency.getDelinquentAmount(repository.gw.enums.OpenClosed.Open, null, myPolicyObj.squire.getPolicyNumber(), repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
		boolean partialNonPayCancelFound = summaryPage.verifyPendingCancellation(repository.gw.helpers.DateUtils.dateAddSubtract(repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, this.delinquentAmount, 120);
		if (!partialNonPayCancelFound) {
			Assert.fail("The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyPartialNonPayCancelInPolicyCenter" })
    public void moveClocks2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 20);
	}

	@Test(dependsOnMethods = { "moveClocks2" })
	public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Partial_Nonpay_Cancel);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Partial_Nonpay_Cancel_Documents);

        TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();

        SearchPoliciesPC searchPoliciesPage = new SearchPoliciesPC(driver);
        searchPoliciesPage.searchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());

		//verify that policy is cancelled in PC
		//gather charges that were sent to BC


        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange(this.policyChangeDescription, repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.clickBuildingsLocationsRow(2);
        buildings.removeBuildingByBldgNumber(1);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
		locations.removeLocationByLocNumber(2);

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        PolicySummary policySummary = new PolicySummary(driver);
		String transactionNumber = policySummary.getTransactionNumber(repository.gw.enums.TransactionType.Policy_Change, this.policyChangeDescription);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuChargesToBC();
		

        PolicyChargesToBC chargesToBC = new PolicyChargesToBC(driver);
		this.chargesList = chargesToBC.getChargesToBC(transactionNumber);

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "removeLienholderCoveragesInPolicyCenter" })
	public void verifyChargesInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();


        AccountCharges accountCharges = new AccountCharges(driver);
		for (int i = 0; i < this.chargesList.size(); i++) {
			String payerNumber = null;
			if (this.chargesList.get(i).getLinePayer().equalsIgnoreCase("Insured")) {
				payerNumber = myPolicyObj.accountNumber;
			} else {
				payerNumber = this.chargesList.get(i).getLinePayer();
			}
			try {
                accountCharges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), payerNumber, null, repository.gw.enums.ChargeCategory.Premium, repository.gw.enums.TransactionType.Policy_Change, null, null, null, myPolicyObj.squire.getPolicyNumber(), chargesList.get(i).getLineAmount(), this.policyChangeDescription, null, null, null, payerNumber, payerNumber);
			} catch (Exception e) {
				Assert.fail("The charge from the Lienholder Partial cancel for the account " + payerNumber + " in the amount of " + this.chargesList.get(i).getLineAmount() + " was not found in the charges table. Test failed.");
			}
		}
	}
}