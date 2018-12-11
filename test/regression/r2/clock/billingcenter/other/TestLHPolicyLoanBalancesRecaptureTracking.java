package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonHistory;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.bc.wizards.NewRecaptureWizard;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionNumber;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
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
 * @Requirement This test creates a policy for tracking lienholder balances. It makes an excess payment on the LH and disburses the excess. Then, it
 * returns payment, creating the need for a recapture. It then does a recapture, ensuring that policy loan balances are tracked correctly.
 * 01/04/2018 - Edited test to track new Policy recaptures per new requirements listed below...
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/38120282624">Rally Story US5195</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/55718231081">Rally Story US7794</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-03%20LH%20Buckets%20-%20Policy%20Loan%20Balances%20Screen.docx">Requirements Documentation</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/12%20-%20Recapture%20Transaction/17-12%20Recapture%20Transaction.docx?Web=1">Recapture Requirements Documentation</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-01%20Recapture%20Activity.docx?Web=1">Recapture Activities Requirements Documentation</a>
 * @Description
 * @DATE Jun 8, 2016 Edited Jan 4, 2018
 */
public class TestLHPolicyLoanBalancesRecaptureTracking extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderName = null;
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderExcessAmount = 150.00;
	private double lienholderPaymentAmount = 0;
	private double lienholderPaymentTracking = this.lienholderExcessAmount;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

		AddressInfo addIntTest = new AddressInfo();
		addIntTest.setLine1("PO Box 711");
		addIntTest.setCity("Pocatello");
		addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711
		
		ArrayList<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		addressInfoList.add(addIntTest);
		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.withAddresses(addressInfoList)
				.build(GenerateContactType.Company);		
		
		driver.quit();
		
		AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, addIntTest);		
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
		
		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LH Transaction Test")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		this.lienholderName = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();
		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
	public void payLienholderAmountPlusExcess() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        this.lienholderPaymentAmount = this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium() + this.lienholderExcessAmount;
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderPaymentAmount);
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "payLienholderAmountPlusExcess" })
	public void verifyPolicyLoanBalancesScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

		AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		WebElement policyLoanBalancesRow = null;
		int policyLoanBalancesRowNumber = 0;
		try {
            policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), null, this.lienholderPaymentAmount, null, null, this.lienholderExcessAmount, this.lienholderExcessAmount);
			policyLoanBalancesRowNumber = new TableUtils(driver).getHighlightedRowNumber(policyLoanBalances.getPolicyLoanBalancesTable());
		} catch (Exception e) {
			Assert.fail("The Charges, Total Cash, and Excess Cash cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		new BasePage(driver).clickWhenClickable(policyLoanBalancesRow);

		policyLoanBalances.clickActionsDisbursementLink(policyLoanBalancesRowNumber);

		CreateAccountDisbursementWizard disbursementWizard = new CreateAccountDisbursementWizard(driver);
		disbursementWizard.createAccountDisbursement(100.00, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DisbursementReason.Overpayment);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		
		accountMenu.clickBCMenuCharges();
		accountMenu.clickAccountMenuPolicyLoanBalances();

		this.lienholderPaymentTracking = this.lienholderPaymentTracking - 100.00;
		try {
            policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), null, this.lienholderPaymentAmount, -100.00, null, this.lienholderPaymentTracking, this.lienholderPaymentTracking);
		} catch (Exception e) {
			Assert.fail("The Charges, Total Cash, Excess Cash, disbursements, and transfers cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		policyLoanBalances.clickPolicyLoanBalancesDisbursementsTab();

		try {
			policyLoanBalances.getAccountPolicyLoanBalancesDisbursementsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, DisbursementStatus.Sent, null, 100.00, this.lienholderName, null, null);
		} catch (Exception e) {
			Assert.fail("The Line item expected for the recently made Disbursement was not found. Test Failed.");
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPayments();

		AccountPayments accountPayments = new AccountPayments(driver);
        accountPayments.reversePaymentAtFault(null,this.lienholderPaymentAmount, this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), this.lienholderExcessAmount, PaymentReturnedPaymentReason.InsufficientFunds);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

		policyLoanBalances = new AccountPolicyLoanBalances(driver);
		try {
            policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), this.myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), null, -100.00, null, -100.00, null);
		} catch (Exception e) {
			Assert.fail("The Charges, Balance Outstanding, disbursements, and total cash cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		new BasePage(driver).clickWhenClickable(policyLoanBalancesRow);
		policyLoanBalances.clickActionsRecaptureLink(policyLoanBalancesRowNumber);

		NewRecaptureWizard recaptureWizard = new NewRecaptureWizard(driver);
        recaptureWizard.performRecapture(this.myPolicyObj.busOwnLine.getPolicyNumber(), 100.00);

		policyLoanBalances = new AccountPolicyLoanBalances(driver);
		try {
            policyLoanBalancesRow = policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, (this.lienholderPaymentAmount - 50.00), (this.lienholderPaymentAmount - 50.00), 100.00, -100.00, null, null, null);
		} catch (Exception e) {
			Assert.fail("The Charges, Balance Outstanding, Payments, and Disbursements cells did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		new BasePage(driver).clickWhenClickable(policyLoanBalancesRow);
		
		policyLoanBalances.clickPolicyLoanBalancesChargesTab();

		Date currentBillingCenterDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		try {
            policyLoanBalances.getAccountPolicyLoanBalancesChargesTableRow(currentBillingCenterDate, this.lienholderNumber, TransactionNumber.Policy_Recapture_Charged, ChargeCategory.Policy_Recapture, null, null, null, null, null, 100.00, null, null, this.lienholderLoanNumber, null, null, null);
		} catch (Exception e) {
			Assert.fail("The Charges Detail Table did not include the correct line for the POLICY Recapture as expected. Test Failed.");
		}

		BCSearchAccounts searchAccounts = new BCSearchAccounts(driver);
		searchAccounts.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuCharges();

		AccountCharges accountCharges = new AccountCharges(driver);
		try {
            accountCharges.getChargesOrChargeHoldsPopupTableRow(currentBillingCenterDate, this.lienholderNumber, TransactionNumber.Policy_Recapture_Charged, ChargeCategory.Policy_Recapture, null, null, null, null, null, 100.00, null, null, this.lienholderLoanNumber, null, null, null);
		} catch (Exception e) {
			Assert.fail("The Charges Table on the insured account did not include the correct line for the POLICY Recapture (With the LH as the default payer) as expected. Test Failed.");
		}

        menu = new BCAccountMenu(driver);
		menu.clickBCMenuHistory();

		BCCommonHistory accountHistory = new BCCommonHistory(driver);
        if (!accountHistory.verifyHistoryTable(currentBillingCenterDate, "Policy Recapture Charge created for Amount $100.00 on Policy " + this.myPolicyObj.busOwnLine.getPolicyNumber() + ".")) {
			Assert.fail("The POLICY recapture did not appear in the history table as expected. Test failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}