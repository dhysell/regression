package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
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
import repository.gw.helpers.GuidewireHelpers;
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
 * @Description DE3432 LH return premium not limited to paying off charges with same policy and loan number
 * @Steps: Generate a policy with two same LHs with different Loan numbers. Pay insured, not pay LHs. Move clock to pass LH Due Date and run batches.
 * Verify the account gets two delinquencies on LHs with two loan numbers. Go to PolicyCenter, remove LH1. Verify that LH2 doesn't get returned premium from LH1's cancellation.
 * @Test Environment:
 * @DATE April 11, 2016
 */
@QuarantineClass
public class LHReturnPremiumNotPayOtherLHsTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String loanNumber1="LN11111", loanNumber2="LN22222", lienholderNumber;	
	private BCAccountMenu acctMenu;	
	private double lienPremium;
	private float buildingBPPLimit=100000;
	private ARUsers arUser = new ARUsers();
	private Date currentDate;
	private BCSearchAccounts search;
	private String policyChgDescription="Cancel "+loanNumber1;
	private String buildingDescription="Building to Remove";
	
	private void verifyLHDelinquency(OpenClosed status, DelinquencyReason reason, String loanNumber, Date startDate, Double amount){
        BCCommonDelinquencies delinq = new BCCommonDelinquencies(driver);
		try{
			delinq.getDelinquencyTableRow(status, reason, lienholderNumber, loanNumber, null, startDate, null,amount);
		}catch(Exception e){
			Assert.fail("Didn't find the correct delinquecny for lienholder "+lienholderNumber+" with loan number "+ loanNumber1);
		}
	}
	//generate a policy with two same LHs, the two LHs have different Loan numbers 
	@Test
	public void generatePolicyWithTwoLHs() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();			
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();	
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber1);			
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		
		AddressInfo additionalInsuredAddress = new AddressInfo();
		additionalInsuredAddress.setLine1(loc1LNBldg1AddInterest.getAddress().getLine1());
		additionalInsuredAddress.setCity(loc1LNBldg1AddInterest.getAddress().getCity());
		additionalInsuredAddress.setState(loc1LNBldg1AddInterest.getAddress().getState());
		additionalInsuredAddress.setZip(loc1LNBldg1AddInterest.getAddress().getZip());
		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(loc1LNBldg1AddInterest.getCompanyName(), additionalInsuredAddress);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber2);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setBuildingLimit(buildingBPPLimit);
		loc1Bldg1.setBppLimit(buildingBPPLimit);
		loc1Bldg1.setUsageDescription(buildingDescription);
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setBuildingLimit(buildingBPPLimit);
		loc1Bldg2.setBppLimit(buildingBPPLimit);
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);	
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LHDelinquency")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();	
		//lienholder1 and lienholder2 have the same premium
		lienPremium=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();			
	}
	
	@Test(dependsOnMethods = { "generatePolicyWithTwoLHs" })	
	public void payInsuredAndMakeLHsDelinquent() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate=DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//pay insured
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getInsuredPremium(), myPolicyObj.busOwnLine.getPolicyNumber());
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move to LH invoice date and run Invoice to make it Billed
		currentDate=DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//move to 1 day after LH due date and run InvoiceDue to make it Due
        currentDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, currentDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify the two LH delinquencies 
		search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);	
		acctMenu.clickBCMenuDelinquencies();
		verifyLHDelinquency(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, loanNumber1, null, lienPremium);
		verifyLHDelinquency(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, loanNumber2, null, lienPremium);
		new GuidewireHelpers(driver).logout();
	}
	//remove LH whose loan number is loanNumber1
	@Test(dependsOnMethods = { "payInsuredAndMakeLHsDelinquent" })
    public void cancelOneLH() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.removeBuildingByBldgDescription(buildingDescription, policyChgDescription);
        new GuidewireHelpers(driver).logout();
    }
	//verify that the LH return premium doesn't apply to the other LH which has a different Loan number
	@Test(dependsOnMethods = { "cancelOneLH" })
    public void verifyChargesAfterLHCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//verify insured side
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		//wait for the LH partial cancellation coming to BC
        charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, this.myPolicyObj.busOwnLine.getPolicyNumber());
		//verify that loanNumber2 doesn't have the cancellation return premium from loanNumber1
		try{
            charge.getChargesOrChargeHoldsPopupTableRow(currentDate, lienholderNumber, null, null, TransactionType.Policy_Change, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), null, policyChgDescription, null, loanNumber2, "Cancellation", null, null);
			Assert.fail("the lienholder with loan number= "+ loanNumber2+ "got the return premium from the other LH's Cancellation.");
		}catch(Exception e){
			getQALogger().info("the lienholder with loan number= "+ loanNumber2+ " didn't get the return premium from the other LH's Cancellation which is expected.");
		}
		//verify that LH1's Delinquency closed after cancellation, LH2 should keep open
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinq = new BCCommonDelinquencies(driver);
		try{
			delinq.getDelinquencyTableRow(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, lienholderNumber, loanNumber1, null, null, null, null);
		}catch(Exception e){
			Assert.fail("Didn't find the correct delinquecny for lienholder "+lienholderNumber+" with loan number "+ loanNumber1);
		}
		try{
			delinq.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, lienholderNumber, loanNumber2, null, null, null, null);
		}catch(Exception e){
			Assert.fail("Didn't find the correct delinquecny for lienholder "+lienholderNumber+" with loan number "+ loanNumber2);
		}
		//verify lienholder side
		search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienholderNumber);
		acctMenu.clickBCMenuCharges();		
		try{
			//check if loanNumber2 has the cancellation return premium
            charge.getChargesOrChargeHoldsPopupTableRow(currentDate, lienholderNumber, null, null, TransactionType.Policy_Change, null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), null, policyChgDescription, null, loanNumber2, "Cancellation", null, null);
			Assert.fail("the lienholder with loan number= "+ loanNumber2+ "got the return premium from the other LH");
		}catch(Exception e){
			getQALogger().info("the lienholder with loan number= "+ loanNumber2+ "didn't get the return premium from the other LH which is expected.");
		}
		new GuidewireHelpers(driver).logout();
	}	
}
