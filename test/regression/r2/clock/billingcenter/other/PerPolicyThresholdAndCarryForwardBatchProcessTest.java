package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyChargesToBC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description -- Do not bill lienholders for policies that have less than $20 owing. Change the threshold to be per policy not per invoice. Added FBMCarryForward batch process.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Requirements/Batches/FBMCarryForward%20Batch.docx">FBMCarryForward Batch</a>
 * @Test Environment:
 * @DATE
 */
@QuarantineClass
public class PerPolicyThresholdAndCarryForwardBatchProcessTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String loanNumber1="LN11111", loanNumber2="LN22222", lienholderNumber;
	private double greatThanThreshholdAmt=20, lessThanThresholdAmt=15;	
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private double lienPremiumChange, lienPremium1, lienPremium2;
	private float buildingBPPLimit=100000;
	private ARUsers arUser = new ARUsers();
	
	//generate a policy with two same LHs, the two LHs have different Loan numbers 
	@Test
	public void generatePolicyWithTwoLHs() throws Exception {			

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();	
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);			
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber1);			
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		//get additional interest address from LH1 and pass it to LH2
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
				.withInsCompanyName("ThresholdFBMCF")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();			
		lienPremium1=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		lienPremium2=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();				
	}
	
	@Test(dependsOnMethods = { "generatePolicyWithTwoLHs" })	
	public void payLienholdersAndRunFBMCarryForward() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		//verify LH invoice after generate
		try{				
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Planned, lienPremium1+lienPremium2, lienPremium1+lienPremium2);
		}catch(Exception e){
			Assert.fail("The initial LH invoice is not correct");
		}
		//move 11 days and run Invoice to make LHs Billed
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 11);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		//make LH payment
        NewDirectBillPayment billPmt = new NewDirectBillPayment(driver);
        billPmt.setAmount(lienPremium1 + lienPremium2 - lessThanThresholdAmt - greatThanThreshholdAmt);
        billPmt.clickOverrideDistribution();
        //LH1 has nonCarryForwardAmt left after payment($15 which is < $19.99 threshold);
		//LH2 has carryForwardAmt left after payment($20 which is > $19.99 threshold); 
		billPmt.setOverrideAmount(null, null, loanNumber1, null, null, null, lienPremium1, lienPremium1-lessThanThresholdAmt);
        billPmt.setOverrideAmount(null, null, loanNumber2, null, null, null, lienPremium2, lienPremium2 - greatThanThreshholdAmt);
        billPmt.clickExecute();
		//verify LH invoice again after payment
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Billed, lienPremium1+lienPremium2, lessThanThresholdAmt+greatThanThreshholdAmt);
		}catch(Exception e){
			Assert.fail("The LH invoice is not correct after the payment.");
		}
		
		//move clock to 2 days after its Due Date and run FBMCarryForward			
        Date LHDueDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(LHDueDate, DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);

		//verify LH invoice after Run FBM_Carry_forward
		//LH1 with LN11111 should have lessThanThresholdAmt which is NOT carried forward
		//LH2 with LN2222 should have greatThanThreshholdAmt carried forward	
		//click away and click back so Invoice will be updated
		//click away and back to update Invoice screen
		acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuInvoices();
        try {
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Carried_Forward, null, lessThanThresholdAmt);
		}catch(Exception e){
			Assert.fail("didn't find the non-carried forward row after running FBMCarryForward.");
		}		
		try{
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, greatThanThreshholdAmt);				
		}catch(Exception e){
			Assert.fail("didn't find the Shortage row of the carried forward greaterThanThresholdAmt after running FBMCarryForward.");
		}
	}
	
	//add coverage to LH with LN11111 so the combination charges > $19.99 threshold
	@Test(dependsOnMethods = { "payLienholdersAndRunFBMCarryForward" })		
	public void addCoverageToFirstLienholder() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());//
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		//add coverage to the LH with loan number of LN11111
		policyChangePage.changeBuildingCoverage(1, buildingBPPLimit+5000, buildingBPPLimit+5000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();

		//get lien premium change in PC->ChargeToBC screen
        PolicySummary summary = new PolicySummary(driver);
		String transactionNumber=summary.getTransactionNumber(TransactionType.Policy_Change, "change coverage");
        SideMenuPC menu = new SideMenuPC(driver);
		menu.clickSideMenuChargesToBC();
        PolicyChargesToBC chgToBC = new PolicyChargesToBC(driver);
		lienPremiumChange=chgToBC.getAmount(transactionNumber, lienholderNumber);
	}
	
	@Test(dependsOnMethods = { "addCoverageToFirstLienholder" })	
	public void runFBMCarryForwardAgainAndVerify() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		//verify that the policy change received in BC
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		try{
			charge.waitUntilChargesFromPolicyContextArrive(3000, TransactionType.Policy_Change);
		}catch(Exception e){
			Assert.fail("The policy change is not received in BC.");
		}
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);
		//click away and click back so Invoice will be updated
		acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuInvoices();
        try {
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Carried_Forward, null, 0.0);
		}catch(Exception e){
			Assert.fail("didn't find the Carried Forward row after Policy Change and FBMCarryForward batch process.");
		}		
		try{			
			acctInvoice.getAccountInvoiceTableRow(null, null, null, InvoiceType.Shortage, null, InvoiceStatus.Planned, null, greatThanThreshholdAmt+lienPremiumChange+lessThanThresholdAmt);
		}catch(Exception e){
			Assert.fail("didn't find the new Shortage row of the carried forward amount after running FBMCarryForward.");
		}			
	}
}
