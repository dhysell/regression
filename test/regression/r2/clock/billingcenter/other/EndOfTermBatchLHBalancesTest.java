package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonHistory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
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
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description US8977: End-of-Term Batch: LH Balances
 * @DATE September 20, 2016
 */
@QuarantineClass
public class EndOfTermBatchLHBalancesTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private String loanNumber="LN11111";
	private Date currentDate;
	private double onSetAmount;
	private String lienNumber;
	private double underThresholdAmt= NumberUtils.generateRandomNumberInt(1, 4);
	private double overThresholdAmt=NumberUtils.generateRandomNumberInt(50, 100);
	
	private void makeLHPaymentAndRunBatch(double amount){
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
		directBillPmt.makeDirectBillPaymentExecute(myPolicyObj.accountNumber, loanNumber, amount);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.FBM_End_Of_Term_Batch_Process);
	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();	
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();			
		locOneBuildingList.add(loc1Bldg1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PolicyLevelDibursementTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	
		lienNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
	}	
	@Test(dependsOnMethods = { "generate" })	
	public void payInsuredAndMoveClock() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
        directBillPmt.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		//move clock 
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 11);
		ClockUtils.setCurrentDates(cf, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "payInsuredAndMoveClock" })
    public void cancelLienholder() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange change = new StartPolicyChange(driver);
		change.removeBuildingByBldgNumber(2);		
	}
	@Test(dependsOnMethods = { "cancelLienholder" })
	public void veriyBatchOnBelowAndOverThresholdAmount() throws Exception {
		String historyItem=lienNumber+ " to account "+myPolicyObj.fullAccountNumber+". Amount: $"+underThresholdAmt;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		acctMenu = new BCAccountMenu(driver);
		//wait for LH cancellation to come
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		try{
            charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, this.myPolicyObj.busOwnLine.getPolicyNumber());
		}catch(Exception e){
            Assert.fail("doesn't find the " + TransactionType.Policy_Change.getValue() + " for " + myPolicyObj.busOwnLine.getPolicyNumber());
		}
		onSetAmount=charge.getChargeAmount(lienNumber, "remove a building");
		//make payment on LH
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienNumber);
		makeLHPaymentAndRunBatch(underThresholdAmt+onSetAmount);
		//the under threshold amount should be moved to insured after run batch process				
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		acctMenu.clickBCMenuHistory();
		BCCommonHistory history = new BCCommonHistory(driver);
		history.verifyHistoryTable(currentDate, historyItem);
		//make a over threshold payment for the lienholder and verify the disbursement after run the batch process
		search.searchAccountByAccountNumber(lienNumber);
		makeLHPaymentAndRunBatch(overThresholdAmt+onSetAmount);
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		try{
			disbursement.getDisbursementsTableRow(currentDate, null, null, null, null, null, overThresholdAmt, null, null, null);
		}catch(Exception e){
			Assert.fail("doesn't find the disbursement for the over threshold amount for the lienholder");
		}
	}
}
