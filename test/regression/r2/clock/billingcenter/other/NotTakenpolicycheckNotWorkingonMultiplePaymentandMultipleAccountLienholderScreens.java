package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.RenewalCode;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

//import pc.policy.PolicySummary;
/**
 * @Author sreddy
 * @Requirement : DE4695   Not Taken policy check not working on Multiple Payment and Multiple Account Lien holder Screens
 * @Description : 1. Issue a policy with Annual Payment plan and pay Lien and Insured amounts
 * 2. Move clock 30 days close to pol expiry date and renew
 * 3. Make it Not taken delinquent
 * 4. Make payment from Multiple payment  and multiple account lien holder screens
 * Expected: should check for Not taken warning message
 * @DATE Mar 20, 2017
 */
@QuarantineClass
public class NotTakenpolicycheckNotWorkingonMultiplePaymentandMultipleAccountLienholderScreens extends BaseTest {
	private WebDriver driver;
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;
	private double lienholderLoanPremiumAmount;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		AddressInfo addIntTest = new AddressInfo();
		addIntTest.setLine1("PO Box 711");
		addIntTest.setCity("Pocatello");
		addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711
		
		
		AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
		loc1Bld2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
				
		this.locOneBuildingList.add(loc1Bldg1);
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("DE4695")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
		this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
	public void payLienholderAmount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);

		new GuidewireHelpers(driver).logout();
		
	}

	@Test(dependsOnMethods = { "payLienholderAmount" })
	public void runInvoiceDueAndCheckForNoDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 285);//   340 days nearly
		new GuidewireHelpers(driver).logout();
	}

	
	@Test(dependsOnMethods = { "runInvoiceDueAndCheckForNoDelinquency" })
	public void renewlPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
			summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

			boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
			if (preRenewalDirectionExists) {
				preRenewalPage.clickViewInPreRenewalDirection();
				preRenewalPage.closeAllPreRenewalDirectionExplanations();
				preRenewalPage.clickClosePreRenewalDirection();
				preRenewalPage.clickReturnToSummaryPage();
            }
        }
        StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObj);	
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = { "renewlPolicyInPolicyCenter" })
	public void runInvoiceDueAndCheckForNotTakenDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day,41); // beyond expiration date
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.NotTakenPartial,null,startDatex ), "Not Taken Delinquency Exist as Expected");

		new GuidewireHelpers(driver).logout();
	}		
	
	@Test(dependsOnMethods = { "runInvoiceDueAndCheckForNotTakenDelinquency" })
	public void verifyNotTakenPolicyCheckOnMultipaymentScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		BCTopMenu topMenu1 = new BCTopMenu(driver);
		topMenu1.clickDesktopTab();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();

        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);

        multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(this.myPolicyObj.busOwnLine.getPolicyNumber(), PaymentInstrumentEnum.Check, 20.00);
		multiplePaymentsPage.clickNext();
        String errorMessage = null;
        String expectedWarningMessage = "Date : Policy " + this.myPolicyObj.busOwnLine.getPolicyNumber() + " has a pending Not Taken Cancel. Please change the payment date to match the Postmarked date.";
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
               errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage(); 
               Assert.assertTrue(errorMessage.equals(expectedWarningMessage), "Expected Warning Message displayed");

        } else {
            Assert.fail("Not Taken policy check not working on Multiple Payment. Test failed.");
        }
		
		new GuidewireHelpers(driver).logout();		
	}
}
