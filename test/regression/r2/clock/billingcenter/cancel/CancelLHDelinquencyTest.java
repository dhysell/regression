package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
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
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description: DE3525 Insured delinquency incorrectly shows as policy canceled after policy change to remove LH payer building
* @Steps: Steps:
			Create a policy with 2 buildings; one with insured as payer and one with LH as payer. Make sure Equipment breakdown is checked on Locations tab.
			Move clock/run cycles to trigger both Insured and Lien partial delinquency
			Create and issue a policy change to remove the LH payer building
			When cancellation charges are sent to BC, Lien partial cancel will be closed.
			It also incorrectly marks the Insured Delinquency as 'Canceled inPAS' (verify on Delinquency tab).
* @DATE May 4, 2016
*/
public class CancelLHDelinquencyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private BCPolicyMenu policyMenu;
	private Date currentDate;
	private String lienBuildingName="Lienholder Building";
	private String loanNumber="LN12345";
	private String lienNumber;
	
	//generate policy with lienholder
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		loc1Bldg2.setUsageDescription(lienBuildingName);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("CancelLHDelinquencyTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
	}
	@Test(dependsOnMethods = { "generate" })
	public void triggerInsuredAndLHDelinquency() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets tt = new BCCommonTroubleTickets(driver);
		if(!tt.waitForTroubleTicketsToArrive(60)){
			throw new GuidewireException("BillingCenter: ",	"doesn't find the Promised Payment Trouble Ticket.");
		}
		tt.closeFirstTroubleTicket();
		policyMenu.clickTopInfoBarAccountNumber();
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		//make insured Due
		BatchHelpers batchHelpers = new BatchHelpers(cf);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);
		currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
		ClockUtils.setCurrentDates(driver, currentDate);
		batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
		//make lienholder due
		currentDate=DateUtils.dateAddSubtract(currentDate,	DateAddSubtractOptions.Month, 1);				
		ClockUtils.setCurrentDates(driver, currentDate);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);
		batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
		//verify the insured and lienholder delinquency
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, lienNumber, loanNumber, myPolicyObj.accountNumber, currentDate, null,null);
		}catch(Exception e){
			throw new GuidewireException("BillingCenter: ",	"doesn't find the correct lienholder delinquency.");
		}
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDuePartialCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, null, null,null);
		}catch(Exception e){
			throw new GuidewireException("BillingCenter: ",	"doesn't find the correct delinquency for the insured.");
		}		
	}
	@Test(dependsOnMethods = { "triggerInsuredAndLHDelinquency" })
    public void removeLH() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartPolicyChange change = new StartPolicyChange(driver);
		change.removeBuildingByBldgDescription(lienBuildingName, "Remove Lienhodler");
	}
	@Test(dependsOnMethods = { "removeLH" })
	public void verifyDelinquencyAfterLHCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, this.lienNumber);
		acctMenu.clickBCMenuDelinquencies();
		//verify that the lienholder delinquency is closed
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		try{
			delinquency.getDelinquencyTableRow(OpenClosed.Closed, DelinquencyReason.PastDueLienPartialCancel, lienNumber, loanNumber, myPolicyObj.accountNumber, currentDate, null,null);
		}catch(Exception e){
			throw new GuidewireException("BillingCenter: ",	"lienholder delinquency is not closed.");
		}
	}
}
