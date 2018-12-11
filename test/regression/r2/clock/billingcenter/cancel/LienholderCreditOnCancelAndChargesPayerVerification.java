package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonSummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description Verify wax on/off after policy cancellation (all charges billed to lienholder for the policy). Verify Charges screen payers with the payers in Invoice Items table.
 * @RequirementsLink <a href="http://projects.idfbins.com/gwintegrations/Shared%20Documents/PC%20BC%20CDM%20Integration/PC-BC%20Requirements/PC2BCintegration.vsd">Cancellation Requirement</a>
 * @Test Environment: QA/IT
 * @DATE Nov 12, 2015
 */
@QuarantineClass
public class LienholderCreditOnCancelAndChargesPayerVerification extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private String lienholderNumber, lienholderLoanNumber;
	private ARUsers arUser = new ARUsers();
	private Date newDate;
	private BCAccountMenu acctMenu;
	private double earnedPremium;

	@Test
	public void generate() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		// Generate a policy with one building, billed to lienholder
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		AddressInfo macuAddress = new AddressInfo();
		macuAddress.setLine1("PO Box 691010");
		macuAddress.setCity("San Antonio");
		macuAddress.setState(State.Texas);
		macuAddress.setZip("78269-1010");
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest("Mountain America", macuAddress);
		loc1LNBldg2AddInterest.setAddress(macuAddress);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LHCancelCredit")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		lienholderLoanNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
	}
	
	@Test(dependsOnMethods = { "generate" })
    public void payoffTotalPremiumByLienhoder() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		
		//get the invoice number of the lienholder
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		
		//move clock for 11 days and payoff the total premium by lienholders
        AccountCharges acctChg = new AccountCharges(driver);
		acctChg.clickDefaultPayerNumberHyperlink(lienholderNumber);
        newDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Day, 11);
		ClockUtils.setCurrentDates(driver, newDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
        directBillPmt.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), lienholderLoanNumber, myPolicyObj.busOwnLine.getPremium().getTotalNetPremium());
        new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payoffTotalPremiumByLienhoder" })
    public void moveClockAndCancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		newDate=DateUtils.dateAddSubtract(newDate, DateAddSubtractOptions.Day, 10);
		ClockUtils.setCurrentDates(cf, newDate);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

        StartCancellation cancelPolicy = new StartCancellation(driver);
		cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Cancel Policy", newDate, true);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();
        PolicySummary pcSum = new PolicySummary(driver);
		earnedPremium=pcSum.getTotalPremium();		
	}
	@Test(dependsOnMethods = { "moveClockAndCancelPolicy" })	
	public void verifyWaxOnOffAndChargePayers() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges acctChg = new AccountCharges(driver);
		if(acctChg.waitUntilPolicyCancellationArrive(10000)){
			//verify Charge Payers with Invoice Items Payers
			if(!acctChg.verifyChargePayersWithInvoiceItemsPayers()){
				throw new GuidewireException("BilingCenter: ", "found unmatched Default Payer and Invoice Items payer.");
			}			
			//verify Unapplied Fund
			acctMenu.clickBCMenuSummary();
            BCCommonSummary acctSum = new BCCommonSummary(driver);
			if(acctSum.getDefaultUnappliedFundsAmount()!=0){
				throw new GuidewireException("BilingCenter: ", "got wrong unapplied fund.");
			}			
			//verify invoice
			acctMenu.clickAccountMenuInvoices();
            AccountInvoices acctInv = new AccountInvoices(driver);
			if(!acctInv.verifyInvoice(InvoiceType.LienholderOnset, InvoiceStatus.Planned, earnedPremium)){
				throw new GuidewireException("BilingCenter: ", "didn't find the Lienholder Onset invoice row.");
			}			
		}
	}
}
