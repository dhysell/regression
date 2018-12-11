package regression.r2.clock.billingcenter.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.AdditionalInterestBilling;
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
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
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
* @Description: US7273 Create Activity for Loan Number/Payer Changes
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/52730597658">User Story US7273</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/9877319305d/detail/userstory/56065217626">User Story US7813</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-05%20Activities%20List.xlsx">Activities List Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-06%20Loan%20Number%20Payer%20Change%20Activity.docx">Activites Contents Requirements</a>
* @Steps: Steps:
			Generate a policy and set the LH as the payer. Change LH's loan number, verify the loan number change activity. 
			Change the payer from LH to insured, verify the payer change activity.
* @DATE July 11, 2016 Updated 06/08/2017 by bhiltbrand
*/
public class PayerOrLoanNumberChangeActivityTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;		
	private Date currentDate;	
	private String lienNumber, lienName;
	private String loanNumber1=("LN" + StringsUtils.generateRandomNumberDigits(5)), loanNumber2=("LN" + StringsUtils.generateRandomNumberDigits(5));
	private String loanNumberChangeDescription="Change Loan Number";	
	private ARUsers arUser = new ARUsers();
	private String activitySubject="Loan Number / Payer Change On Policy ";
	private String policyPeriod, chargeGroup;
	
	@Test
	public void generate() throws Exception {		

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		
		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);		
		loc1Bldg1AddInterest.setLoanContractNumber(loanNumber1);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PayerOrLoanNumChangeActivity")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		

		lienNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		lienName=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();			
	}
	@Test(dependsOnMethods = { "generate" })
	public void moveClockAndChangeLoanNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 10);
		ClockUtils.setCurrentDates(driver, currentDate);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeLoanNumber(1, 1, lienName, loanNumber2, loanNumberChangeDescription, currentDate);	
	}
	@Test(dependsOnMethods = { "moveClockAndChangeLoanNumber" })
	public void verifyLoanNumberChangeActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		currentDate=DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		//wait for policy change to come
        AccountCharges charge = new AccountCharges(driver);
        charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, myPolicyObj.busOwnLine.getPolicyNumber());
		policyPeriod = charge.getPolicyPeriod(this.lienNumber, this.loanNumberChangeDescription);
		chargeGroup=charge.getChargeGroup(this.lienNumber, this.loanNumberChangeDescription);
		charge.clickDefaultPayerNumberHyperlink(lienNumber);
        //find the activity
		acctMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
        try {
			activity.clickActivityTableSubject(currentDate, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10), null, ActivityStatus.Open, activitySubject);
		}catch(Exception e){
			Assert.fail("Could not find the loan number change activity for " + lienNumber);
		}
		
		//verify the activity details
		Map<String, String> loanNumberChangeInfoToVerify = new LinkedHashMap<String, String>() {	
			private static final long serialVersionUID = 1L;
			{
                this.put("Subject", activitySubject + myPolicyObj.busOwnLine.getPolicyNumber());
				this.put("Description", "Old Payer: " + lienNumber + "\n" + " Old Loan Number: " + loanNumber1 + "\n" + " New Payer: " + lienNumber + "\n" + " New Loan Number: " + loanNumber2 + "\n" + " Old Charge Groups:" + " " + chargeGroup + "\n" + " New Charge Groups:" + " " + chargeGroup);	
				this.put("Priority", "High");
				this.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10)));
				this.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 17)));
				this.put("Account", lienNumber);
				this.put("Policy Period", policyPeriod);
				this.put("Assigned to", "AR General Western Community - AR Western Community");
				this.put("Status", "Open");					
		}};
		
		if(!activity.verifyActivityInfo(loanNumberChangeInfoToVerify))
			Assert.fail("Loan number change activity verification failed for "+lienNumber);		
	}
	@Test(dependsOnMethods = { "verifyLoanNumberChangeActivity" })
	public void moveClockAndChangePayerFromLHToInsured() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		currentDate=DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 3);
		ClockUtils.setCurrentDates(driver, currentDate);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.resetPayerAssignmentToInsured();	
	}
	@Test(dependsOnMethods = { "moveClockAndChangePayerFromLHToInsured" })
	public void verifyPayerChangeActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		//wait for policy change to come
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, currentDate, TransactionType.Policy_Change, this.lienNumber);
		charge.clickDefaultPayerNumberHyperlink(lienNumber);
        //find the activity
		acctMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
        try {
			activity.clickActivityTableSubject(currentDate, DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10), null, ActivityStatus.Open, activitySubject);
		}catch(Exception e){
			Assert.fail("doesn't fine the loan number change activity for "+lienNumber);
		}		
		//verify the activity details
		Map<String, String> payerChangeInfoToVerify = new LinkedHashMap<String, String>() {	
			private static final long serialVersionUID = 1L;
			{
                this.put("Subject", activitySubject + myPolicyObj.busOwnLine.getPolicyNumber());
				this.put("Description", "Old Payer: "+lienNumber+"\n"+" Old Loan Number: "+loanNumber2+"\n"+" New Payer: "+myPolicyObj.fullAccountNumber+"\n"+" New Loan Number: "+"\n"+" Old Charge Groups:"+" "+chargeGroup);
				this.put("Priority", "High");
				this.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 10)));
				this.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 17)));
				this.put("Account", lienNumber);
				this.put("Policy Period", policyPeriod);
				this.put("Assigned to", "AR General Western Community - AR Western Community");
				this.put("Status", "Open");					
		}};
		
		if(!activity.verifyActivityInfo(payerChangeInfoToVerify))
			Assert.fail("Payer change activity verification failed.");
	}
}
