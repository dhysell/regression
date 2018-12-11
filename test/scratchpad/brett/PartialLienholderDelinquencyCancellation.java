package scratchpad.brett;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
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
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class PartialLienholderDelinquencyCancellation extends BaseTest {
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
    private GeneratePolicy myPolicyObj = null;
    private Date targetDate = null;
    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int randomYear = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myLineAddCov.setEquipmentBreakdownCoverage(true);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(randomYear);
        loc1Bldg1.setClassClassification("storage");

        this.locOneBuildingList.add(loc1Bldg1);

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
        loc2Bldg1.setYearBuilt(randomYear);
        loc2Bldg1.setClassClassification("storage");

        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

        this.locTwoBuildingList.add(loc2Bldg1);
        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));
        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locTwoBuildingList));


        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Partial Lien Cancel")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makeInsuredDownPayment() throws Exception {
        billingCenterLoginAndFindPolicy();

        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);

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
    public void runInvoiceDueAndCheckDelinquency() throws Exception {
        billingCenterLoginAndFindPolicy();

        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();

        this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

        if (!delinquencyFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber() + "The Delinquency was either non-existant or not in an 'open' status.");
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceDueAndCheckDelinquency"})
    public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean partialNonPayCancelFound = summaryPage.verifyCurrentActivity("Partial Nonpay Cancel", 120);
        if (!partialNonPayCancelFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"verifyPartialNonPayCancelInPolicyCenter"})
    public void removeLienholderCoveragesInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchPoliciesPC searchPoliciesPage = new SearchPoliciesPC(driver);
        searchPoliciesPage.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.startPolicyChange("Remove Lienholder", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsLocationsRow(2);
        buildings.removeBuildingByBldgNumber(1);

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
        locations.removeLocationByLocNumber(2);

        policyChange = new StartPolicyChange(driver);
        policyChange.quoteAndIssue();

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"removeLienholderCoveragesInPolicyCenter"})
    public void verifyChargesInBillingCenter() throws Exception {
        billingCenterLoginAndFindPolicy();
//finish this test
    }

    private void billingCenterLoginAndFindPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
    }

}
