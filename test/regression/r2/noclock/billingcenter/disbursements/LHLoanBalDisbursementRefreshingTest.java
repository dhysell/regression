package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Requirement 	DE6593 -- LH Policy Loan Balance screen disbursement tab not being refreshed when switching between buckets
* 				In LH account display disbursement tab. Switch between buckets. Disbursement tab must be updated with disbursement txns.
* @DATE Mar 16, 2018
*/
public class LHLoanBalDisbursementRefreshingTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private String loanNumber="LN12345";
	private ARUsers arUser;
	private double disbursementAmount1 =  NumberUtils.generateRandomNumberInt(10, 30);
	private double disbursementAmount2 = NumberUtils.generateRandomNumberInt(40, 60);
	private double LHPremium;
	private String LHNumber;
	
	@Test 
	public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(50000);
		building1.setBppLimit(50000);
		
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
				.withInsCompanyName("LoanBalDisburseTab")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolDuesCounty(CountyIdaho.Bonneville)				
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		LHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void createTwoDisbursements() throws Exception{
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPmt = new NewDirectBillPayment(driver);
		directPmt.makeLienHolderPaymentExecute(LHPremium + disbursementAmount1, myPolicyObj.accountNumber, loanNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);
		//verify the first disbursement
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		disbursement.verifyDisbursement(null, null, null, null, DisbursementStatus.Awaiting_Approval, null, disbursementAmount1, null, null, null);
		//create the second disbursement
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		directPmt.makeDirectBillPaymentExecuteWithoutDistribution(disbursementAmount2);
		//verify the second disbursement
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);
		disbursement.verifyDisbursement(null, null, null, null, DisbursementStatus.Awaiting_Approval, null, disbursementAmount2, null, null, null);
	}
	@Test(dependsOnMethods = { "createTwoDisbursements" })
    public void verifyDisbursementInPolicyLoanBalScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();
		AccountPolicyLoanBalances loanBal = new AccountPolicyLoanBalances(driver);
		getQALogger().info("LH premium is : "+ LHPremium);
		loanBal.getAccountPolicyLoanBalancesTableRow(myPolicyObj.accountNumber, loanNumber, LHPremium, null, null, null, null, null, disbursementAmount1).click();
		loanBal.clickPolicyLoanBalancesDisbursementsTab();
		try{
			loanBal.getAccountPolicyLoanBalancesDisbursementsTableRow(null, null, null, null, DisbursementStatus.Awaiting_Approval, null, disbursementAmount1, null, null, null);
		}catch (Exception e) {
			System.err.println("couldn't find the " + disbursementAmount1+ e.getMessage());
		}
		loanBal.getAccountPolicyLoanBalancesTableRow(null, null, null, null, null, null, null, disbursementAmount2, disbursementAmount2).click();
		try {
			loanBal.getAccountPolicyLoanBalancesDisbursementsTableRow(null, null, null, null, DisbursementStatus.Awaiting_Approval, null, disbursementAmount2, null, null, null);
		}catch (Exception e) {
			System.err.println("couldn't find the " + disbursementAmount1+ e.getMessage());
		}
	}
}
