package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.ARUsers;

/**
* @Author JQU
* @ Description: DE6532 -- UW Partial Cancel Delinquency opened when it should not have
* 				Create a policy that is partially insured billed and partially LH billed 
				Bind
				monthly payment plan 
				Pay down payment with credit card 
				move clock 2 days 
				run invoice and invoice due 
				issue policy 
				move clock 15 days 
				run invoice and invoice due
				move clock 1 day 
				run invoice and invoice due 
				do a policy change to change the loan number on the LH billed items 
				move clock 1 day 
				run invoice invoice due/  workflow in PC
				delinquency was created and partial cancel balance due was sent when it should not have been
* @DATE November 10, 2017
* */
public class UWPartialCancelDelinquencyOpenedTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private String originalLoanNumber = "LN11111";
	private String newLoanNumber = "LN22222";
	private String LHName;
	
	@Test
	public void generateBind() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);
		locOneBuildingList.add(building1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(originalLoanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("MoveLHChargeToInsured")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicySubmitted);
		
		LHName=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();
	}
	@Test(dependsOnMethods = { "generateBind" })
	public void moveClockRunBatch() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		//move 2 days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 2);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);			
	}
	@Test(dependsOnMethods = { "moveClockRunBatch" })	
	public void issuePolicy() throws Exception {		
		//issue the policy
		myPolicyObj.policyIssuedGuts();
	}
	@Test(dependsOnMethods = { "issuePolicy" })
    public void moveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);	
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "moveClock" })
	public void changeLoanNumber() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.changeLoanNumber(1, 2, LHName, newLoanNumber, "Change Loan Number", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));			
	}
	@Test(dependsOnMethods = { "changeLoanNumber" })
	public void verifyDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		WebElement theRow = delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.UnderwritingPartialCancel, null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null);
		if(theRow != null)
			Assert.fail("Trigged delinquency when it should not.");		
	}
}