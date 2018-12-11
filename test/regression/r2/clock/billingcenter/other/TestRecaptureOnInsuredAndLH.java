package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.enums.State;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountDisbursements;
import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.bc.wizards.NewRecaptureWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GenerateContactType;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE5929 - GunitFix: Creating Recapture from lienholder buckets throws null pointer exception
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-01%20Recapture%20Activity.docx">Link Text<Requirements Link/a>
 * @DATE July 25, 2017
 */
public class TestRecaptureOnInsuredAndLH extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;
	private double lienholderPaymentAmount, insuredPaymentAmount;
	private double insuredExcessAmount = 500.00;
	private double lienholderExcessAmount = 500.00;
	private double manualDisbursementAmountInsured;
	private double manualDisbursementAmountLH ;
	private double recaptureAmountInsured;
	private double recaptureAmountLH ;
	private BCAccountSummary acctSummary;
	private BCAccountMenu acctMenu;
	private AccountPayments payment;
	private AccountDisbursements disbursements;
	private AccountCharges acctCharges;
    private CreateAccountDisbursementWizard accountDisbursement;
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
	private Date recaptureDate;
	private SoftAssert softAssert;
	
	private void gotoAccount(String accountNumber){

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(accountNumber);
		
	}
	

	@Test
	public void generateInsuredAndLH() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy")- NumberUtils.generateRandomNumberInt(0, 50);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");

		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");

		ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
		rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

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
		loc1Bld2AddInterest.setNewContact(repository.gw.enums.CreateNew.Create_New_Always);
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
							.withInsCompanyName("Recap Insured LH")
							.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
							.withPolOrgType(repository.gw.enums.OrganizationType.Partnership).withPolicyLocations(locationsList)
							.withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual).withDownPaymentType(repository.gw.enums.PaymentType.Cash)
							.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	
	}

	@Test(dependsOnMethods = { "generateInsuredAndLH" })
	public void payInsuredDownPaymentPlusExcess() throws Exception {

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        insuredPaymentAmount = myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + insuredExcessAmount;
        newPayment.makeInsuredDownpayment(myPolicyObj, insuredPaymentAmount, myPolicyObj.busOwnLine.getPolicyNumber());

		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	

	@Test(dependsOnMethods = { "payInsuredDownPaymentPlusExcess" })
    public void createDisbursementOnInsured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        acctSummary = new BCAccountSummary(driver);
        manualDisbursementAmountInsured = acctSummary.getUnappliedFundByPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewTransactionDisbursement();

        accountDisbursement = new CreateAccountDisbursementWizard(driver);
        accountDisbursement.createAccountDisbursement(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.myPolicyObj.busOwnLine.getPolicyNumber(), this.manualDisbursementAmountInsured, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), repository.gw.enums.DisbursementReason.Overpayment);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();

        disbursements = new AccountDisbursements(driver);
        if (!disbursements.verifyDisbursements(myPolicyObj.busOwnLine.getPolicyNumber(), repository.gw.enums.DisbursementStatus.Approved, this.manualDisbursementAmountInsured)) {
			Assert.fail("The manually created disbursement for the Insured did not appear in the disbursements table in the status expected.");
		}
		
	}

	@Test(dependsOnMethods = { "createDisbursementOnInsured" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 16);
	}

	@Test(dependsOnMethods = { "moveClocks" })
    public void payLienholderAmountPlusExcess() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.lienholderNumber);

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        this.lienholderPaymentAmount = this.lienholderLoanPremiumAmount + this.lienholderExcessAmount;
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderPaymentAmount);
		
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 16);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payLienholderAmountPlusExcess" })
    public void createDisbursementOnLH() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.lienholderNumber);

        acctSummary = new BCAccountSummary(driver);
		manualDisbursementAmountLH = acctSummary.getDefaultUnappliedFundsAmount();

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuActionsNewTransactionDisbursement();

        accountDisbursement = new CreateAccountDisbursementWizard(driver);
        accountDisbursement.createAccountDisbursement(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.manualDisbursementAmountLH, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), repository.gw.enums.DisbursementReason.Overpayment);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();

        disbursements = new AccountDisbursements(driver);
		if (!disbursements.verifyDisbursements("Default", repository.gw.enums.DisbursementStatus.Approved, this.manualDisbursementAmountLH)) {
			Assert.fail("The manually created disbursement for the LienHolder did not appear in the disbursements table in the status expected.");
		}
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Disbursement);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Automatic_Disbursement);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.FBM_Lienholder_Automatic_Disbursement);
			
	}
	
	@Test(dependsOnMethods = { "createDisbursementOnLH" })
    public void reversePayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();

        payment = new AccountPayments(driver);
		payment.reversePayment(insuredPaymentAmount, null, null, repository.gw.enums.PaymentReversalReason.Payment_Moved);
		
		gotoAccount(this.lienholderNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();

        payment = new AccountPayments(driver);
		payment.reversePayment(this.lienholderPaymentAmount, null, null, repository.gw.enums.PaymentReversalReason.Payment_Moved);
		
	}
	@Test(dependsOnMethods = { "reversePayment" })
    public void createRecapture() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.lienholderNumber);

        acctMenu = new BCAccountMenu(driver);
        acctSummary = new BCAccountSummary(driver);
		recaptureAmountLH= acctSummary.getDefaultUnappliedFundsAmount();
		
		if(manualDisbursementAmountLH!=recaptureAmountLH*(-1)) 
		{
			Assert.fail("Manual disbursed amount does not not match the negative unapplied amount");

		}
		
		recaptureDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);	
		
		/*
		 *     LH  Recapture		
		*/
		
		acctMenu.clickAccountMenuPolicyLoanBalances();
        AccountPolicyLoanBalances acctLoanBal = new AccountPolicyLoanBalances(driver);
		acctLoanBal.clickActionsRecaptureLink(1);

        NewRecaptureWizard recaptureWiz = new NewRecaptureWizard(driver);
		recaptureWiz.performRecapture(recaptureAmountLH*(-1));
		
		acctMenu.clickBCMenuSummary();
        acctSummary = new BCAccountSummary(driver);

		recaptureAmountLH= acctSummary.getDefaultUnappliedFundsAmount();
		if(recaptureAmountLH!=0){
			Assert.fail("Recapture not sucessful for LH");
		}
		
		acctMenu.clickBCMenuCharges();
        acctCharges = new AccountCharges(driver);
        acctCharges.verifyCharges(recaptureDate, repository.gw.enums.ChargeCategory.Policy_Recapture, null, recaptureAmountLH,null,null,null,null,null);
		
		/*
				INSURED Recapture		
		 */
		
		gotoAccount(this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        recaptureAmountInsured = acctSummary.getUnappliedFundByPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());
		
		if(manualDisbursementAmountInsured!=recaptureAmountInsured*(-1)) 
		{
			Assert.fail("Manual disbursed amount does not not match the negative unapplied amount");
		}
		
		recaptureDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);	
		acctMenu.clickAccountMenuActionsNewNewTransactionRecapture();

        recaptureWiz = new NewRecaptureWizard(driver);
        recaptureWiz.performRecapture(myPolicyObj.busOwnLine.getPolicyNumber(), recaptureAmountInsured * (-1));
		
		acctMenu.clickBCMenuSummary();

        recaptureAmountInsured = acctSummary.getUnappliedFundByPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());
		if(recaptureAmountInsured!=0){
			Assert.fail("Recapture not sucessful for insured");
		}
		
		acctMenu.clickBCMenuCharges();
        acctCharges = new AccountCharges(driver);
        acctCharges.verifyCharges(recaptureDate, repository.gw.enums.ChargeCategory.Policy_Recapture, myPolicyObj.busOwnLine.getPolicyNumber(), recaptureAmountInsured,null,null,null,null,null);
		
	}
	
	/**
	* @throws Exception 
	 * @Author sgunda
	* @Requirement DE5528 - Invoices with Lien Owned Charges (Recapture) Not Going Due 
	* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-01%20Recapture%20Activity.docx">Link Text<Requirements Link/a>
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/116215050868">Link Text<Rally Link/a>	
	* @DATE Oct 01, 2017
	*/
	
	@Test(dependsOnMethods = { "createRecapture" })
    public void makeInvoicesDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        AccountInvoices invoice = new AccountInvoices(driver);
		int numberOfRows = invoice.getInvoiceTableRowCount();
		Date invoiceDate = invoice.getListOfInvoiceDates().get(numberOfRows-1);
		Date invoiceDueDate = invoice.getListOfDueDates().get(numberOfRows-1);
		
		gotoAccount(this.lienholderNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);
				
		ClockUtils.setCurrentDates(cf, invoiceDate);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		
		ClockUtils.setCurrentDates(cf, invoiceDueDate);
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "makeInvoicesDue" })
    public void verifyInvoicesDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(),arUser.getPassword(), this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		boolean insuredInvoiveWentDue= invoice.verifyInvoice(repository.gw.enums.InvoiceType.Shortage, repository.gw.enums.InvoiceStatus.Due, this.insuredExcessAmount);
		
		gotoAccount(this.lienholderNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		boolean LHInvoiveWentDue= invoice.verifyInvoice(repository.gw.enums.InvoiceType.Shortage, repository.gw.enums.InvoiceStatus.Due, this.lienholderExcessAmount);
		
		softAssert = new SoftAssert();
		softAssert.assertTrue(insuredInvoiveWentDue, "Insured Invoice didn't go Due");
		softAssert.assertTrue(LHInvoiveWentDue, "Lienholder Invoice didn't go Due");
		softAssert.assertAll();
		
	}
	
		
}
