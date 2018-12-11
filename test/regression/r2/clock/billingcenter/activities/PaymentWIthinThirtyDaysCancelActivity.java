package regression.r2.clock.billingcenter.activities;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
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
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sbroderick
 * @Requirement :
 * @Description : DE4926   	Payment within 30 days cancel activity isn't being created correctly.
 * 1. Have a policy non-renew non-pay cancel
 * 2. After the cancel is complete, perform any policy change
 * 3. Move the clock < 30 days from the cancel date
 * 4. In BillingCenter, receive a payment. BC will send a message "PAS_CanceledPolicyPayment "
 * 5. PC will respond with "No canceled period found for account 027426-008"
 * Actual: PC is only checking that the latest period is a cancellation, but it's not (because of the policy change)
 * Expected: PC should check if the policy is currently cancelled, whether or not that was the last job
 */
@QuarantineClass
public class PaymentWIthinThirtyDaysCancelActivity extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy renewalPolicy = null;
	
	@Test
    public void testPaymentwithinThirtyDaysCancelActivity() throws Exception {
        generatePolicy();
		double renewalAmount = renewPolicy();
		driver.quit();
		changePolicy();
		payRenewal(renewalAmount);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(renewalPolicy.underwriterInfo.getUnderwriterUserName(), renewalPolicy.underwriterInfo.underwriterPassword, renewalPolicy.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
		if(!summaryPage.checkIfActivityExists("Payment within 30 days of cancel")) {
			Assert.fail("The Payment within 30 days of cancel activity should exist on the policy.");
		}
	}

    public void generatePolicy() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.renewalPolicy = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PayerActivity")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolTermLengthDays(51)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.ACH_EFT)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
	}
	
	public double renewPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.renewalPolicy.underwriterInfo.underwriterUserName, this.renewalPolicy.underwriterInfo.underwriterPassword, this.renewalPolicy.accountNumber);
        StartRenewal renewMe = new StartRenewal(driver);
		renewMe.renewPolicyManually(RenewalCode.Renew_Good_Risk, renewalPolicy);
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 51));
		runBatchJobs(cf);
		return getRenewalPremium();
	}
	
	public void runBatchJobs(Config driverConfiguration) {
		BatchHelpers batchHelpers = new BatchHelpers(driverConfiguration);
		batchHelpers.runBatchProcess(BatchProcess.PC_Workflow);
		batchHelpers.runBatchProcess(BatchProcess.Invoice);
		batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	public void changePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienObj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
        driver.quit();
		
        cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.renewalPolicy.underwriterInfo.underwriterUserName, this.renewalPolicy.underwriterInfo.underwriterPassword, this.renewalPolicy.accountNumber);
//        StartPolicyChange changeMe = new StartPolicyChange(driver);
		addAdditionalInterest(myContactLienObj.companyName, myContactLienObj.addresses.get(0), myContactLienObj.lienNumber);
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 9));
		driver.quit();
	}
	
	public void addAdditionalInterest(String companyName, AddressInfo address, String lienholderNumber) throws Exception {
		StartPolicyChange changeMe = new StartPolicyChange(driver);
        AdditionalInterest newInterest = new AdditionalInterest(companyName, address);
        newInterest.setLienholderNumber(lienholderNumber);
        addAdditionalInterest(newInterest);
    }
	
	public void addAdditionalInterest(AdditionalInterest newInterest) throws Exception {
		StartPolicyChange changeMe = new StartPolicyChange(driver);
		changeMe.startPolicyChange("Add New Interest on Building 2", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickBuildingsBuildingEdit(1);
        building.addAdditionalInterest(true, newInterest);
        building.clickOK();
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
        changeMe.quoteAndIssue();
    }
	
	public void payRenewal(double amount) throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUser();
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), renewalPolicy.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment makePayment = new NewDirectBillPayment(driver);
        makePayment.makeDirectBillPaymentExecute(amount, renewalPolicy.busOwnLine.getPolicyNumber(), PaymentInstrumentEnum.Cash, true);
		runBatchJobs(cf);
		driver.quit();
	}
	
	public double getRenewalPremium() throws Exception {
		new Login(driver).loginAndSearchPolicyByAccountNumber(renewalPolicy.underwriterInfo.underwriterUserName, renewalPolicy.underwriterInfo.underwriterPassword, renewalPolicy.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
		return summaryPage.getRenewalPremium(TransactionType.Renewal); 
	}
	
	
}
