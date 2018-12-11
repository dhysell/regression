package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description US4329: Disbursement editing page will have a free form box, a button to copy apron details, and a button to clear apron details.
 * The contents of the free form box will be sent to SunGard to appear on the check apron.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-04%20Account%20Free-Form%20Disbursements.docx">Account Free-Form Disbursements</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-05%20Account%20Check%20Apron%20Mapping.xlsx">Account Check Apron Mapping</a>
 * @DATE April 1st, 2016
 */
@QuarantineClass
public class AccountLevelDisbursementFreeFormBoxTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private double lienPremium;
	private String loanNumber="LN11111";
	private String lienholderNumber;
	private String specialHandling="whatever";
	private String warningMsg="Apron Details Text Required";
	private AccountDisbursements disbursement;

	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, false);
		  myLineAddCov.setEmployeeDishonestyCoverage(true);
		  myLineAddCov.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
		  myLineAddCov.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Annually);
		  myLineAddCov.setEmpDisLimit(EmpDishonestyLimit.Dishonest5000);
		  PolicyBusinessownersLine myLine = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		  myLine.setAdditionalCoverageStuff(myLineAddCov);
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("FreeFormBox")
                .withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myLine)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		//the following two methods to get lienholder premium return 0!
//		lienPremium=myPolicyObj.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
//		lienPremium=myPolicyObj.totalAdditionalInterestPremium;
        lienPremium = myPolicyObj.busOwnLine.getPremium().getTotalNetPremium() - myPolicyObj.busOwnLine.getPremium().getInsuredPremium();
	}
	@Test(dependsOnMethods = { "generate" })	
		public void payInsuredAndLHWithExtraAmount() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		double extraAmount = 50;

		//pay insured
		DesktopActionsMultiplePayment makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeMultiplePayment(myPolicyObj.busOwnLine.getPolicyNumber(), PaymentInstrumentEnum.Cash, myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + extraAmount);

		//pay lienholder, and create suspense item with the extra amount
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber, lienPremium + extraAmount);
	}
	//only need to verify 2 lines of the free form box info for insured
	//the other 4 lines are blank for insured
	@Test(dependsOnMethods = { "payInsuredAndLHWithExtraAmount" })	
	public void createDisbursementAndVerifyFreeFormBoxOnInsured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
		//get info from Apron Details area
		String disburseReason = disbursement.getDisbursementReason();
		if(!disbursement.getFreeFormWholeDetails().equals(disburseReason))
			Assert.fail("The free form info is incorrect before edit.");		
		disbursement.clickAccountDisbursementsEdit();
		//clear the form and verify the warning msg for blank form
		disbursement.clickCopyClearApronDetails();
		disbursement.clickAccountDisbursementsUpdate();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			if(!new GuidewireHelpers(driver).containsErrorMessage(warningMsg)) {
				Assert.fail("doesn't find the corrent warning message.");	
			}
		} else {
			Assert.fail("doesn't find the warning message for the blank form.");
		}
		disbursement.setDisbursementSpecialHandling(specialHandling);
		disbursement.clickCopyClearApronDetails();
		disbursement.clickAccountDisbursementsUpdate();
		if(!disbursement.getFreeFormWholeDetails().replace(" ", "").replace("\n", "").equals(disburseReason +specialHandling))
			Assert.fail("the free form info is incorrect after edit for the insured");		
	}
	//need to verify 6 lines of the free form box info for lienholder.
	@Test(dependsOnMethods = { "createDisbursementAndVerifyFreeFormBoxOnInsured" })
	public void createDisbursementAndVerifyFreeFormBoxOnLandholders() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
		//get the free form lines info should be
		List<String> freeFormLinesInfoShouldBe=new ArrayList<>();
		freeFormLinesInfoShouldBe.add(disbursement.getDisbursementReason());
		freeFormLinesInfoShouldBe.add("");
		freeFormLinesInfoShouldBe.add("Lien # "+lienholderNumber);
		freeFormLinesInfoShouldBe.add("");		
		//get the real free form info	
		disbursement.clickAccountDisbursementsEdit();
		List<String> realFormLinesInfo = new ArrayList<>();
		realFormLinesInfo.add(disbursement.getFreeFormDisbursementReason());
		realFormLinesInfo.add(disbursement.getFreeFormInsuredName());
		realFormLinesInfo.add(disbursement.getFreeFormAccountAndLoanNumber());
		realFormLinesInfo.add(disbursement.getFreeFormChargeGroup());
		realFormLinesInfo.add(disbursement.getFreeFormDetailedDisbursementReason());
		
		new GuidewireHelpers(driver).verifyLists(freeFormLinesInfoShouldBe, realFormLinesInfo);
		
		//clear the form and verify the warning
		disbursement.clickCopyClearApronDetails();
		disbursement.clickAccountDisbursementsUpdate();
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			if(!new GuidewireHelpers(driver).containsErrorMessage(warningMsg)) {
				Assert.fail("doesn't find the corrent warning message.");	
			}
		} else {
			Assert.fail("doesn't find the warning message for the blank form.");
		}
		//set special handling and verify the form info again
		disbursement.setDisbursementSpecialHandling(specialHandling);
		disbursement.clickCopyClearApronDetails();
		disbursement.clickAccountDisbursementsUpdate();
		String wholeFormInfo = disbursement.getFreeFormWholeDetails();
		freeFormLinesInfoShouldBe.add(specialHandling);
		for(int i=0; i< freeFormLinesInfoShouldBe.size();i++){
			if(!wholeFormInfo.contains(freeFormLinesInfoShouldBe.get(i))){
				Assert.fail("the free form box doesn't contain '"+freeFormLinesInfoShouldBe.get(i)+"' which it should.");
			}
		}		
	}
}
