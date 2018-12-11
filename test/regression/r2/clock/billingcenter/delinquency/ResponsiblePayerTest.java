package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class ResponsiblePayerTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String accountNumber;	
	private String uwUserName, uwPwd;
	private BCAccountMenu acctMenu;
	private String lienholderNumber;
	private String lienholderLoanNumber;
	private double lienPremium;
	private BCCommonDelinquencies acctDelinquency;
	private boolean delinquencyStatus;
	public Date currentDate, newDate, partialCancelDate;
	private ARUsers arUser = new ARUsers();
	
	private void verifyReponsiblePayer(OpenClosed openorClosed, String acctNum, Date date) {
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
		delinquencyStatus= acctDelinquency.verifyDelinquencyStatus(openorClosed, acctNum, null, date);
		if (!delinquencyStatus)
			Assert.fail("BillingCenter: incorrect delinquency.");
	}

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
		locationsList.add(new PolicyLocation(new AddressInfo(),
				locOneBuildingList));

		// customizing the BO Line, including additional insureds, uncheck the
		// Equipment Breakdown
		AddressInfo address = new AddressInfo();
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(
				SmallBusinessType.SelfStorageFacilities);
		PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(
				false, true);
		boLine.setAdditionalCoverageStuff(additionalCoverageStuff);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
		additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(
				ContactSubType.Company, "dafigudhfiuhdafg",
				AdditionalInsuredRole.CertificateHolderOnly, address));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("ChgPayerTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(boLine)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)				
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

        System.out.println(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObj).toString());
		accountNumber = myPolicyObj.accountNumber;
		System.out.println("Account number is : "+accountNumber);
		Underwriters underwriter = myPolicyObj.underwriterInfo;
		uwUserName = underwriter.getUnderwriterUserName();
		uwPwd=underwriter.getUnderwriterPassword();
		lienholderNumber = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(1)
				.getAdditionalInterestList()
				.get(0)
				.getLienholderNumber(); // it's thirteen digits number
		lienholderLoanNumber = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(1)
				.getAdditionalInterestList()
				.get(0)
				.getLoanContractNumber();
        lienPremium = myPolicyObj.busOwnLine.getPremium().getTotalAdditionalInterestPremium();
	}

	@Test(dependsOnMethods = { "generate" })	
	public void moveClockAndPayLienHoler() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		newDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 11);
		ClockUtils.setCurrentDates(cf, newDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due); // Insured is due
															// upaid
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
        directBillPmt.makeDirectBillPaymentExecute(this.myPolicyObj.busOwnLine.getPolicyNumber(), lienholderLoanNumber, lienPremium);
	}

	@Test(dependsOnMethods = { "moveClockAndPayLienHoler" })
    public void verifyDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), accountNumber);	
		verifyReponsiblePayer(OpenClosed.Open, accountNumber, newDate);		
	}
	
	@Test(dependsOnMethods = { "verifyDelinquency" })
    public void move20DaysAndCancelInsured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date finalDate = DateUtils.dateAddSubtract(newDate, DateAddSubtractOptions.Day, 20);
		ClockUtils.setCurrentDates(cf, finalDate);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPwd, accountNumber);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(1);
	}
	
	@Test(dependsOnMethods = { "move20DaysAndCancelInsured" })
    public void verifyDelinquencyAgain() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), accountNumber);	
		verifyReponsiblePayer(OpenClosed.Open, accountNumber, newDate);	
	}	
}

