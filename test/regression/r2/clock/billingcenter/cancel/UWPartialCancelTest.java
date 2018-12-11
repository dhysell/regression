package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
@QuarantineClass
public class UWPartialCancelTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInv;
	// set values for change premium, change date,
	public Float additionalPremium = null;
	public Date changeDate = null;
	private Date downpaymentDate, moveToDate;	
	private ARUsers arUser = new ARUsers();

	@Test
	public void generate() throws Exception {
		// customizing location and building- one location with two buildings
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());

		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(),	locOneBuildingList));

		// customizing the BO Line, including additional insureds, uncheck the
		// Equipment Breakdown
		AddressInfo address = new AddressInfo();
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities);
		PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(false, false);
		boLine.setAdditionalCoverageStuff(additionalCoverageStuff);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
		additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "dafigudhfiuhdafg", AdditionalInsuredRole.CertificateHolderOnly, address));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PartialCancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(boLine)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		Underwriters underwriter = myPolicyObj.underwriterInfo;
		underwriter.getUnderwriterUserName();		
	}

	// go to BC, run invoice and move the clock for 1 week, and run invoicedue.
	@Test(dependsOnMethods = { "generate" }, enabled = true)
	public void runBatchprocessesAndMoveClock() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for trouble ticket to come
		acctMenu.clickAccountMenuInvoices();
		acctMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets TT = new BCCommonTroubleTickets(driver);
		TT.waitForTroubleTicketsToArrive(60);

        acctInv = new AccountInvoices(driver);
		downpaymentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		moveToDate = DateUtils.dateAddSubtract(downpaymentDate,	DateAddSubtractOptions.Day, 7);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        // move clock for 1 week (< 10 days of trouble ticket's hold period)
		ClockUtils.setCurrentDates(cf, moveToDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}

	// go to PC to make the UW partial cancel of the insured
	@Test(dependsOnMethods = { "runBatchprocessesAndMoveClock" }, enabled = true)
    public void removeInsuredInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		// policyChangePage.startPolicyChange("remove the insured",
		// DateUtils.dateFormatAsString("MM/dd/yyyy", moveToDate));
		policyChangePage.removeBuildingByBldgNumber(1);
    }

	// verify delinquency status/reason
	@Test(dependsOnMethods = { "removeInsuredInPC" }, enabled = true)
	public void verifyDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinq = new BCCommonDelinquencies(driver);
		String delinqReason = acctDelinq.getDelinquencyReason(DateUtils.dateFormatAsString("MM/dd/yyyy", moveToDate));
		if (!delinqReason.equals("Underwriting Partial Cancel"))
			throw new GuidewireException("BillingCenter: ",	"incorrect delinquency reason.");
	}

	// verify that the downpayment invoice/due dates don't change after partial
	// cancel
	@Test(dependsOnMethods = { "verifyDelinquency" }, enabled = true)
	public void verifyDownPaymentDates() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		int errorCount = 0;
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInv = new AccountInvoices(driver);
		List<Date> dates = acctInv.getListOfDueDates();
		for (int i = 0; i < dates.size(); i++) {
			if (!dates.get(0).equals(downpaymentDate))
				errorCount++;
		}
		if (errorCount > 0) {
			Assert.fail("downpayment invoice/due dates are changed.");
		}
	}

	@Test(dependsOnMethods = { "verifyDownPaymentDates", "verifyDelinquency" }, enabled = true)
	public void checkLoopLetters() throws Exception	{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		// move 1 day to past due of the shortage invoice
		moveToDate = DateUtils.dateAddSubtract(moveToDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(cf, moveToDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		moveToDate = DateUtils.dateAddSubtract(moveToDate, DateAddSubtractOptions.Month, 1);
		ClockUtils.setCurrentDates(cf, moveToDate);
		System.out.println("the current date is " + DateUtils.dateFormatAsString("MM/dd/yyyy", moveToDate));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDocuments();
        BCCommonDocuments acctDoc = new BCCommonDocuments(driver);
		boolean foundLoopLetter = acctDoc.verifyDocument(moveToDate, "Balance Due Partial Cancel");
		System.out.println("found or not?  " + foundLoopLetter);
		if (!foundLoopLetter) {
			throw new GuidewireException("BillingCenter: ", "loop letter is not correctly created.");
		}
	}
}
