package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.RenewalCode;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
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
 * @Author sgunda
 * @Requirement DE5584 --  Carry Forward batch does not carry forward the shortage invoice which is less than threshold to renewal invoice
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-01%20General%20Installment%20Scheduling.docx">Link Text<General Installment Scheduling - 11-01-24/a>
 * @Description Wrote the test according to requirements(Threshold to carry the invoice item forward). If the day is even number it tests one scenario and if it is odd it tests another scenario.
 * @DATE Jun 14, 2017
 */
public class TestCarryForwardThresholdsForLH extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
    private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private String lienholderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienholderLoanPremiumAmount;
    private BCAccountMenu acctMenu;
    private AccountInvoices acctInvoice;
    private BCCommonDelinquencies acctDelinquency;
    private double renewalDownPaymentDueAmountLien, testToCreateNewInvoive = 20.01, testToCarryForwardOnToRenewalInvoice = 19.8, testAmt;

    private void gotoAccount(String accountNumber) {
        BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(accountNumber);
    }


    @Test
    public void generate() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
    	driver = buildDriver(cf);
        int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
        loc1Bldg2.setYearBuilt(2010);
        loc1Bldg2.setClassClassification("storage");

		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        AddressInfo addIntTest = new AddressInfo();
        addIntTest.setLine1("PO Box 711");
        addIntTest.setCity("Pocatello");
        addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711

		ArrayList<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		addressInfoList.add(addIntTest);
		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.withAddresses(addressInfoList)
				.build(GenerateContactType.Company);	        

		driver.quit();

        AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, addIntTest);
//        loc1Bld2AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc1Bld2AddInterest.setAddress(addIntTest);
        loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
        loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

        this.locOneBuildingList.add(loc1Bldg2);
        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("DE5584LHCarryForwardThreshold")
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
        this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();

    }


    @Test(dependsOnMethods = {"generate"})
    public void testToRun() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (day % 2 == 0) {
            testAmt = testToCarryForwardOnToRenewalInvoice;
            System.out.println(testToCarryForwardOnToRenewalInvoice + " Amount will be carry forward to Renewal invoice ");
        } else {
            testAmt = testToCreateNewInvoive;
            System.out.println(testToCreateNewInvoive + " Amount will create a delinqency");
        }
    }

    @Test(dependsOnMethods = {"testToRun"})
    public void payLienholderAmount() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 25);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, (this.lienholderLoanPremiumAmount - testAmt));

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"payLienholderAmount"})
    public void verifyCarryForwardOnIssuance() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        if (testAmt == testToCreateNewInvoive) {
            if (testAmt < lienholderLoanPremiumAmount * .90) {

                new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);
                acctMenu = new BCAccountMenu(driver);
                acctMenu.clickAccountMenuInvoices();

                acctInvoice = new AccountInvoices(driver);
                acctInvoice.verifyInvoice(null, (DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getEffectiveDate(), DateAddSubtractOptions.Month, 2)), null, null, null, null, testToCreateNewInvoive, null);
            } else {

                new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);

                gotoAccount(this.myPolicyObj.accountNumber);
                acctMenu = new BCAccountMenu(driver);
                acctMenu.clickBCMenuDelinquencies();

                acctDelinquency = new BCCommonDelinquencies(driver);
                Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(OpenClosed.Open, this.lienholderNumber, this.myPolicyObj.busOwnLine.getPolicyNumber(), null), "Delinquency not triggered ");
            }

            getQALogger().info("This test accomplished one of its test cases(Left over amount from New Business Down Payment will create a shortage or trigger delinquency), so test ends here today intentionally. Check back tomorrow if the other test case passes or not (Left over amount from New Business Down Payment Carry forward to renewal invoice)");
            throw new SkipException("This test accomplished one of its test cases(Left over amount from New Business Down Payment will create a shortage or trigger delinquency), so test ends here today intentionally. Check back tomorrow if the other test case passes or not (Left over amount from New Business Down Payment Carry forward to renewal invoice)");
        }

        new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
        int numberOfInv = acctInvoice.getInvoiceTableRowCount();
        boolean yesOrNo = numberOfInv != 1;

        Assert.assertFalse(yesOrNo, "Only one invoice is expected here ");

        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -50));
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"verifyCarryForwardOnIssuance"})
    public void renewlPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
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
    public void runBatches() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
        renewalDownPaymentDueAmountLien = acctInvoice.getInvoiceDueAmountByDueDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -1));

        new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Carry_Forward);

        acctMenu.clickBCMenuSummary();
        acctMenu.clickAccountMenuInvoices();
        acctInvoice.verifyInvoice(InvoiceType.RenewalDownPayment, InvoiceStatus.Planned, renewalDownPaymentDueAmountLien + testAmt);

    }

}
