package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
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
import repository.gw.enums.InvoiceType;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author jqu
* @Description   US11958: Stop the use of "Masquerade as Cancel" and use "Is Partial Cancel" flag where necessary in Delinquency
* 					The delinquency will look for the partial cancel flag = true instead of masquerade as cancel flag to trigger delinquency.
* 					
* @DATE October 10, 2017
*/
public class IsPartialCancelFlagOnDelinquency extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private BCAccountMenu acctMenu;
	private String loanNumber="LN12345";
	private String LHNumber;
	private double LHPremium;	
	private Date pastDuePartialCancelDate;
	private BCCommonDelinquencies delinquency;
	private AccountCharges charge;
	
	@Test 
	public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(250000);
		building1.setBppLimit(250000);
		locOneBuildingList.add(building1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Always);
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
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("MoveLHChargeToInsured")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		LHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void triggerPastDuePartialCancel() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		pastDuePartialCancelDate=DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//clock TT to trigger the delinquency
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		tt.closeFirstTroubleTicket();
		//verify the delinquency
		policyMenu.clickTopInfoBarAccountNumber();
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
		delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.PastDuePartialCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null);
		//move a few days, then remove insured building
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 5);
	}
	@Test(dependsOnMethods = { "triggerPastDuePartialCancel" })
    public void removeInsuredBuilding() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(1);
	}
	@Test(dependsOnMethods = { "removeInsuredBuilding" })
	public void verifyPartialCancelFlagOnPastDuePartialCancel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for policy change to come
		acctMenu.clickBCMenuCharges();
        charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		//verify the partial cancel flag
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), myPolicyObj.accountNumber, null, null, TransactionType.Policy_Change, null, null, null, null, null, null, true, null, null, null, null);
			
		}catch(Exception e){
			Assert.fail("couldn't find the flag for partial cancel of Insured.");
		}		
		//pay delinquent amount to close the delinquency
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
        double delinquencyAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), pastDuePartialCancelDate);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeDirectBillPaymentExecute(delinquencyAmount, myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	@Test(dependsOnMethods = { "verifyPartialCancelFlagOnPastDuePartialCancel" })	
	public void addInsuredBack() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        
		addBuildingOnLocation(1);
	}
	
	
	private PolicyLocationBuilding addBuildingOnLocation(int locationNumber) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        policyChangePage.startPolicyChange("Add New Building on Location", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        PolicyLocationBuilding newBuilding = buildingsPage.addBuildingOnLocation(true, locationNumber, building);
        policyChangePage.quoteAndIssue();
        return newBuilding;
    }
	
	
	
	
	
	
	
	@Test(dependsOnMethods = { "addInsuredBack" })
    public void triggerPastDueLHPartialCancel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for policy change of adding back insured to come
		acctMenu.clickBCMenuCharges();
        charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, myPolicyObj.accountNumber);
		//pay insured, not pay LH 
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getInsuredPremium(), myPolicyObj.accountNumber);
		//make LH pass due date
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//verify past due LH partial cancel
		acctMenu.clickBCMenuDelinquencies();
        delinquency = new BCCommonDelinquencies(driver);
		delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, null, loanNumber, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null);		
	}
	@Test(dependsOnMethods = { "triggerPastDueLHPartialCancel" })
    public void removeLHBuilding() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(2);
	}
	@Test(dependsOnMethods = { "removeLHBuilding" })
	public void verifyDelinquencyAndIsPartialCancelFlag() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		//wait for policy change of adding back insured to come
		acctMenu.clickBCMenuCharges();
        charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(60, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, myPolicyObj.accountNumber);
		//get LH earned premium (LH onset)
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		double LHEarnedPremium= NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceTableCellValue("Amount", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, InvoiceType.LienholderOnset, null, null, null, null));
		acctMenu.clickBCMenuCharges();
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), LHNumber, null, null, TransactionType.Policy_Change, null, null, null, null, LHPremium*(-1), null, true, loanNumber, null, null, null);			
		}catch(Exception e){
			Assert.fail("couldn't find the cancelling credit from removing LH building.");
		}
		//verify "is partial cancel" is set yes for earned charges 
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), myPolicyObj.accountNumber, null, null, TransactionType.Policy_Change, null, null, null, null,  LHEarnedPremium, null, true, null, null, null, null);			
		}catch(Exception e){
			Assert.fail("couldn't find the earned premium");
		}			
	}
}
