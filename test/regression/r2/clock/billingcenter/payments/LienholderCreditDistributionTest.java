package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.YesOrNo;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPaymentsCreditDistributions;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.CreateDisbursementWizard;
import repository.bc.wizards.NewTransferWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TransferReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
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
 * @Requirement This test is intended to test the following functionalities for Lienholder Payments Credit Distribution: Payment details, Negative Writeoff/Negative Wirteoff Reversal, Transfer, and Disbursements
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-04%20LH%20Payment%20Tracking%20-%20Credit%20Distributions.docx">LH Payment Tracking - Credit Distributions</a>
 * @DATE Nov 30, 2015
 */
@QuarantineClass
public class LienholderCreditDistributionTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private BCAccountMenu acctMenu;	
	private ARUsers arUser = new ARUsers();
	private double testAmount1=20, transferAmount=30, disburseAmount=50;
	private double lienPremiumChange;
	private String lienholderNumber, loanNumber="LN54321";
	private AccountPaymentsCreditDistributions creditDistribution;
	private Date currentDate;

	@Test
	public void Generate() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		//create random additional interest
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest();		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("TestLienholderPaymentCreditDistribution")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();		
	}
	
	//move the clock and reduce the coverage of the lien so some credit will be sent to BillingCenter.
	@Test(dependsOnMethods = { "Generate" })		
	public void moveClockAndReduceLNCoverage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(2, 100000, 100000);
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
	
	@Test(dependsOnMethods = { "moveClockAndReduceLNCoverage" })	
	public void verifyBasicInfoOfCreditDistribution() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		creditDistribution=new AccountPaymentsCreditDistributions(driver);		
		
		if(!creditDistribution.verifyPaymentCreditDistribution(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange)){
			Assert.fail("didn't find the lienholder credit in lienholder Payments Credit Distribution scree.");
		}
		else{
			creditDistribution.clickRowOfCreditDistribution(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange);
            //verify Payment Details Tab
			creditDistribution.clickPaymentDetailsTab();
			if(!creditDistribution.verifyPaymentDetails(currentDate, myPolicyObj.accountNumber, lienholderNumber, lienPremiumChange))//accountNumber
				Assert.fail("found error in the Payment Details table.");
			
			//ATTENTION: SUSPENSE ITEMS ARE NO LONGER USED. THIS CHECK FOR THE LH WILL NEED TO TAKE PLACE THROUGH OTHER MEANS!!!
			
			/*//check Suspense Items Tab
			creditDistribution.clickSuspenseItemsTab();
			if(!creditDistribution.verifySuspenseItem(myPolicyObj.policyNumber, lienPremiumChange*(-1), null)) //policyNumber
				throw new GuidewireBillingCenterException(getCurrentURL(), lienholderNumber, "found error in the Suspense Items table.");*/
		}			
	}
	
	@Test(dependsOnMethods = { "verifyBasicInfoOfCreditDistribution" })		
	public void testNegativeWriteOffAndReversal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		creditDistribution=new AccountPaymentsCreditDistributions(driver);
		creditDistribution.makeNegativeWriteoff(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange, testAmount1);
		//verify the negative write off
		creditDistribution.clickRowOfCreditDistribution(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange);
		creditDistribution.clickNegativeWriteOffTab();
		if(!creditDistribution.verifyNegativeWriteoff(currentDate, testAmount1, arUser.getFirstName()+" " +arUser.getLastName(), YesOrNo.No))		
			Assert.fail("didn't find the negative writeoff item");
		
		//ATTENTION: SUSPENSE ITEMS ARE NO LONGER USED. THIS CHECK FOR THE LH WILL NEED TO TAKE PLACE THROUGH OTHER MEANS!!!
		
		/*creditDistribution.clickSuspenseItemsTab();
		if(!creditDistribution.verifySuspenseItem(myPolicyObj.policyNumber, NumberUtils.round((lienPremiumChange*(-1)-testAmount1),2), null)) //policyNumber
			throw new GuidewireBillingCenterException(getCurrentURL(), lienholderNumber, "found error in the Suspense Items table.");*/
		//make negative writeoff reversal
		creditDistribution.makeNegativeWriteoffReversal(currentDate, lienPremiumChange, testAmount1*(-1), testAmount1);
		//verify negative writeoff table 
		creditDistribution.clickNegativeWriteOffTab();
		if(!creditDistribution.verifyNegativeWriteoff(currentDate, testAmount1, arUser.getFirstName()+" " +arUser.getLastName(), YesOrNo.Yes)) 		
			Assert.fail("didn't find the negative writeoff reversal item");		
	}
	
	@Test(dependsOnMethods = { "testNegativeWriteOffAndReversal" })		
	public void testTransferAndReversal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		//randomly find an account to transfer
		BCTopMenu menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchAccounts mySearch=new BCSearchAccounts(driver);
		String targetAccount= mySearch.findAccountInGoodStanding("08-");
		System.out.println("acctount number is : "+targetAccount);
		//make the transfer
		mySearch.searchAccountByAccountNumber(lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		creditDistribution=new AccountPaymentsCreditDistributions(driver);
		creditDistribution.clickActionsTransfer(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange);
        NewTransferWizard transfer = new NewTransferWizard(driver);
		transfer.createNewTransfer(1, targetAccount, null, transferAmount, null, null, TransferReason.Other);
        //verify the transfer
		creditDistribution.clickTransferTab();
		if(!creditDistribution.verifyTransfer(currentDate, transferAmount, targetAccount, TransferReason.Other))
			Assert.fail("didn't find the transfer item");
		//reverse the transfer and verify
		creditDistribution.reverseTransfer(currentDate, transferAmount, targetAccount, TransferReason.Other);
		if(!creditDistribution.verifyTransfer(currentDate, transferAmount*(-1), targetAccount, TransferReason.Other))
			Assert.fail("didn't find the reversed transfer item");
	}
	
	@Test(dependsOnMethods = { "testTransferAndReversal" })		
	public void testCreateDisbursement() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		
		//Create disbursement		
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		creditDistribution=new AccountPaymentsCreditDistributions(driver);
		creditDistribution.clickActionsCreateDisbursement(currentDate, PaymentInstrumentEnum.Unapplied_fund_Account, lienPremiumChange);
		CreateDisbursementWizard disburse = new CreateDisbursementWizard(driver);
		disburse.createDisbursement(disburseAmount);
        //verify the disbursement
		creditDistribution.clickDisbursementsTab();
		if(!creditDistribution.verifyDisbursement(DisbursementStatus.Approved, disburseAmount))
			Assert.fail("didn't find the disbursement item");
		
		//ATTENTION: SUSPENSE ITEMS ARE NO LONGER USED. THIS CHECK FOR THE LH WILL NEED TO TAKE PLACE THROUGH OTHER MEANS!!!
		
		/*//verify the disbursement in Suspense Tab
		creditDistribution.clickSuspenseItemsTab();
		if(!creditDistribution.verifySuspenseItem(myPolicyObj.policyNumber, disburseAmount, "Approved"))
			throw new GuidewireBillingCenterException(getCurrentURL(), lienholderNumber, "didn't find the disbursement item in Suspense Tab.");*/
		
		//reject the disbursement and verify
		creditDistribution.clickDisbursementsTab();
		creditDistribution.clickDisbursement(DisbursementStatus.Approved, disburseAmount);
        creditDistribution.rejectDisbursement();

		//ATTENTION: SUSPENSE ITEMS ARE NO LONGER USED. THIS CHECK FOR THE LH WILL NEED TO TAKE PLACE THROUGH OTHER MEANS!!!
		
		/*creditDistribution.clickSuspenseItemsTab();
		if(creditDistribution.verifySuspenseItem(myPolicyObj.policyNumber, disburseAmount, "Approved"))
			throw new GuidewireBillingCenterException(getCurrentURL(), lienholderNumber, "found incorrect disbursement item in Suspense Tab.");*/
	}
}
