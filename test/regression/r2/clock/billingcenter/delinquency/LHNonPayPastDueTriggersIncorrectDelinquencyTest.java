package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
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
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Requirement DE4072 Both LH partial delinquency and Insured Partial delinquency created when there should be just LH partial delinquency (insured is paid)
 * @Steps: The defect is: when a lienholder partial delinquency should be triggered, it is triggering both lh partial and insured partial, even when the later should not be triggered, since the insured is paid.
 * Steps to reproduce:
 * 1. One or more policy, the policy has lien and insured paid charges.
 * 2. Insured paid full, lien doesn't pay.
 * 3. Should trigger lien partial, but is triggering both lien partial and insured partial.
 * @DATE October 12, 2016
 */
public class LHNonPayPastDueTriggersIncorrectDelinquencyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private String LHNubmer;

	@AfterMethod(alwaysRun=true)
	public void tearDown() throws Exception {

	}	
	@Test
	public void generate() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);
		locOneBuildingList.add(building1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber("LN11111");
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsCompanyName("LHPastDue")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		LHNubmer=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
	}
	@Test(dependsOnMethods = { "generate" })	
	public void payInsuredNotPayLHAndVerifyDelinqueny() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        Date LHDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		//wait for trouble ticket
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets TT = new BCCommonTroubleTickets(driver);
		TT.waitForTroubleTicketsToArrive(60);		
		//pay insured Down and second invoice
		policyMenu.clickTopInfoBarAccountNumber();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		//make LH Billed
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(LHDueDate, DateAddSubtractOptions.Day, -20));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move clock pass due of LH
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(LHDueDate, DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		//verify LH delinquency exist
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, LHNubmer, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null);
		}catch(Exception e){
			Assert.fail("doesn't find the Delinquency for the lienholder");
		}
		//verify Insured delinquency not exist
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDuePartialCancel, myPolicyObj.accountNumber, null, null, null, null,null);
			Assert.fail("Insured delinquency should not be triggered.");
		}catch(Exception e){
			getQALogger().info("Insured delinquency is not triggered which is expected.");
		}		
	}
}
