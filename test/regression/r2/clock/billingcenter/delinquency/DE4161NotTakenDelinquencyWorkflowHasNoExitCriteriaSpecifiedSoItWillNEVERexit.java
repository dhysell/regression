package regression.r2.clock.billingcenter.delinquency;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
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

/**
 * @Author sreddy
 * @Requirement : DE4161   DE4161NotTakenDelinquencyWorkflowHasNoExitCriteriaSpecifiedSoItWillNEVERexit
 * @Description : 1. Issue a policy with Annual Payment plan and pay Lien and Insured amounts
 * 2. Move clock 30 days close to pol expiry date and renew
 * 3. Make it Not taken delinquent
 * 4. Make payment and check the delinquency gets closed
 * Expected: Delinquency should be closed
 * @DATE April 7, 2017
 */
public class DE4161NotTakenDelinquencyWorkflowHasNoExitCriteriaSpecifiedSoItWillNEVERexit extends BaseTest {
	private WebDriver driver;
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
    private GeneratePolicy myPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private String lienholderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienholderLoanPremiumAmount;
    private double insDelAmoount = 0.00;
    private double lhDelAmoount = 0.00;

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

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makeInsuredDownPayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makeInsuredDownPayment"})
    public void moveClocks() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
    }

    @Test(dependsOnMethods = {"moveClocks"})
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

    @Test(dependsOnMethods = {"payLienholderAmount"})
    public void runInvoiceDueAndCheckForNoDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDelinquencies();
        //BCCommonDelinquencies  acctDelinquency = AccountFactory.getBCCommonDelinquenciesPage();
        //Assert.assertFalse(acctDelinquency.verifyDelinquencyStatus(OpenClosed.Open, null, null, null), "Delinquency Exist Which is not Expected");
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 285);//   340 days nearly
        new GuidewireHelpers(driver).logout();
    }


    @Test(dependsOnMethods = {"runInvoiceDueAndCheckForNoDelinquency"})
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


    @Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
    public void runInvoiceDueAndCheckForNotTakenDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 41); // beyond expiration date
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
        Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.NotTakenPartial,null, startDatex), "Not Taken Delinquency Exist as Expected");

        insDelAmoount = acctDelinquency.getDelinquentAmount(OpenClosed.Open, this.myPolicyObj.accountNumber, this.myPolicyObj.busOwnLine.getPolicyNumber() + "-2", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
        lhDelAmoount = acctDelinquency.getDelinquentAmount(OpenClosed.Open, this.lienholderNumber, this.myPolicyObj.busOwnLine.getPolicyNumber() + "-2", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceDueAndCheckForNotTakenDelinquency"})
    public void makePaymentToExitDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, insDelAmoount, myPolicyObj.busOwnLine.getPolicyNumber());
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lhDelAmoount);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
        Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Closed, DelinquencyReason.NotTakenPartial,null, startDatex), "Not Taken Delinquency Closed as Expected");

    }
}
