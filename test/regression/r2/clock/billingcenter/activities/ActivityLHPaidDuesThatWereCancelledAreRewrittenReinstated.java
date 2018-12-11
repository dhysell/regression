package regression.r2.clock.billingcenter.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsTransfers;
import repository.bc.common.BCCommonActivities;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactRole;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sreddy
 * @Requirement :
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/262e9f1f-5f8a-47da-8a01-dc8fd22fc99e">Rally User Story US10588</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-09%20Cancelled%20LH%20paid%20Membership%20Dues%20Charges%20Reinstated%20Rewritten.docx">Cancelled LH Paid Dues Requirements</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-05%20Activities%20List.xlsx">Activities List</a>
 * @Description : US10588   	Activity: LH paid dues that were cancelled are rewritten / reinstated
 * 1. Issue a Lien policy with membership dues are paid by Lien
 * 2. On BC make Lien Payment
 * 3. Cancel Policy From PC
 * 4. Membership Due amount is automatically moved to Insured
 * 5. Reinstate policy
 * Expected : should generate above activity
 * @DATE May 07, 2017
 */
public class ActivityLHPaidDuesThatWereCancelledAreRewrittenReinstated extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = "LN12345";
	private String activitySubject="Lienholder paid dues that were cancelled are rewritten / reinstated.";
	
	private Login login = new Login(driver);
	private TableUtils tableUtils = new TableUtils(driver);

	@Test  
	public void generatePolicy() throws Exception {


        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);
        driver.quit();


        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setLoanContractNumber(this.lienholderLoanNumber);

		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		myPolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.City).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Cancel", "LH")
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        System.out.println("PolicyNumber - " + myPolicyObj.squire.getPolicyNumber());
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

	}
	
	@Test (dependsOnMethods = { "generatePolicy" })  
	public void payLienholderAmount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);
		login.loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);	
		BCAccountMenu acctMenu = new BCAccountMenu(driver);	
		acctMenu.clickBCMenuCharges();
		AccountCharges accountCharges = new AccountCharges(driver);
		WebElement getTable= accountCharges.getChargesOrChargeHoldsPopupTable();
        WebElement webEle = tableUtils.getRowInTableByColumnNameAndValue(getTable, "Loan Number",lienholderLoanNumber);
        String leinAccountNumber = tableUtils.getCellTextInTableByRowAndColumnName(getTable, tableUtils.getRowNumberFromWebElementRow(webEle), "Default Payer");
        this.lienholderNumber = leinAccountNumber;
        System.out.println(leinAccountNumber);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), leinAccountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, 100);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 11);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 10);
	}


	@Test(dependsOnMethods = { "payLienholderAmount" })
	public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        
        login.loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Messup on Agent's Part", null,true);

	}


	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
	public void verifyFundTranseferScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        
		login.loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPayments();
		accountMenu.clickAccountMenuPaymentsTransfers();		
		AccountPaymentsTransfers lhFundTransferPage = new AccountPaymentsTransfers(driver);
		
		try {
            WebElement myRow = lhFundTransferPage.getAccountPaymentsTransfersTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, this.lienholderNumber, myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, null, null, null, null);
				String transferedAmount = tableUtils.getCellTextInTableByRowAndColumnName(lhFundTransferPage.getTransfersTable(), tableUtils.getRowNumberFromWebElementRow(myRow), "Amount");
				System.out.println("On Cancellation Funds from LienAccount moved to InsuredAccount :- " + transferedAmount);
			} catch (Exception e) {

				Assert.fail("Funds Did not move from lien holder Account to Insured on Cancellation");
			}
		
	}
	
	@Test(dependsOnMethods = { "verifyFundTranseferScreen" })
	public void ReinstatePolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        
        login.loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
        StartReinstate reinstatePolicy = new StartReinstate(driver);
		reinstatePolicy.reinstatePolicy(ReinstateReason.Other, "Reinstate Please");

	}
	
	
	@Test(dependsOnMethods = { "ReinstatePolicyInPolicyCenter" })
	public void verifyActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        
        login.loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
		
		BCTopMenuPolicy topMenuStuff = new BCTopMenuPolicy(driver);
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());
		
		BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuActivities();

		Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		
		BCCommonActivities activity = new BCCommonActivities(driver);
		try{
			activity.clickActivityTableSubject(currentDate, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 3), null, ActivityStatus.Open, activitySubject);
		}catch(Exception e){
			Assert.fail("Did Not Trigger The Activity : "+ activitySubject);
		}
		
		//verify the activity details
		Map<String, String> paymentReceivedOnPolicyWithCollectionsWriteOff = new LinkedHashMap<String, String>() {	
			private static final long serialVersionUID = 1L;
			{
				this.put("Subject", activitySubject);
				this.put("Description", "Payment or credit received on a policy that has a past due amount written off with a reason of collections. The write off may need to be reversed and TSI may need to be notified.");
				this.put("Priority", "Normal");
				this.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 7)));
				this.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10)));
				this.put("Account", myPolicyObj.fullAccountNumber);
                this.put("Policy Period", myPolicyObj.squire.getPolicyNumber() + "-1");
				this.put("Assigned to", "AR Supervisor Farm Bureau - Disbursement Specialist Farm Bureau");
				this.put("Status", "Open");					
		}};
		
		if(!activity.verifyActivityInfo(paymentReceivedOnPolicyWithCollectionsWriteOff)) {
			Assert.fail(activitySubject + " : Activity verification failed.");
		}

	}
}
