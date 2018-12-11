package regression.r3.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonSummary;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityStatus;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author jqu
 * @Description: US5504 Cancellation: Configure Full Cancel UW Delinquency Process
 * @Steps: Test UW Full Cancel delinquency process on account with two policies (one policy is overridden by the other one.) Also test documents and activities
 * @DATE May 23rd, 2016
 */
public class FullCancelUWDelinquencyWithOverrideTest extends BaseTest {
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private Agents agent;
    private ARUsers arUser = new ARUsers();
    private Date currentDate;
    private BCAccountMenu acctMenu;
    private String delinquencyAlert;
    private String bannerMessage = "Underwriting Full Cancel";
    private String activityLinkText = "Review Delinquency";
    private AccountCharges charges;
    private BCCommonDocuments document;
    private WebDriver driver;

    private void moveClockAndRunWorkflow(int daysToMove, boolean runInvoiceDue) throws Exception {
        currentDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, daysToMove);
        ClockUtils.setCurrentDates(driver, currentDate);
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        if (runInvoiceDue)
            batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.BC_Workflow);
    }

    private void verifyBalanceDueDocument(DocumentType docType, Date date) {
        document = new BCCommonDocuments(driver);
        try {
            document.getDocumentsTableRow(docType.getValue(), docType, null, null, date, null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " doesn't find the " + docType.getValue());
        }
    }

    //generate two policies on same account
    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine());
            this.setVehicleList(new ArrayList<Vehicle>() {{
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }});
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(new ArrayList<Contact>() {{
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                }});
            }});
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        cppPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("FullCanceUWDelinquency")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println("company name is " + cppPolicyObj.pniContact.getCompanyName());
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        //set small amount, then Policy Change to increase the coverage.
        building.setBppLimit(50000);
        building.setBuildingLimit(50000);
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(cppPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(cppPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(cppPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(cppPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(cppPolicyObj.pniContact.getAddress().getZip());

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(cppPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(cppPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(cppPolicyObj.polOrgType)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withMembershipDuesOnAllInsureds()
                .withPaymentPlanType(cppPolicyObj.paymentPlanType)
                .withDownPaymentType(cppPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //payoff down payment for CPP and BOP
    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void payDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeInsuredDownpayment(cppPolicyObj, cppPolicyObj.commercialPackage.getPremium().getDownPaymentAmount(), cppPolicyObj.commercialPackage.getPolicyNumber());
        directPay.makeInsuredDownpayment(bopPolicyObj, bopPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), bopPolicyObj.busOwnLine.getPolicyNumber());
        currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentDate);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
    }

    //add coverage to CPP and BOP
    @Test(dependsOnMethods = {"payDownPayment"})
    public void changePolicyToAddCoverage() throws Exception {
        //add a vehicle to CPP to increase its coverage
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Vehicle newVehicle = new Vehicle();
        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(cppPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(cppPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setState(cppPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(cppPolicyObj.pniContact.getAddress().getZip());
        newVehicle.setGaragedAt(insuredAddress);
        newVehicle.setVehicleType(VehicleType.Miscellaneous);
        newVehicle.setMake("Antique Auto1");
        newVehicle.setModel("Truck");
        newVehicle.setBodyType(BodyType.AntiqueAutos);
        newVehicle.setLeasedToOthers(true);
        policyChangePage.addVehicleToCPP(true, newVehicle, "add a vehicle", currentDate);

        //add coverage to BOP
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        policyChangePage.changeBuildingCoverage(1, 200000, 200000);
    }

    @Test(dependsOnMethods = {"changePolicyToAddCoverage"})
    public void overrideBOP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        //wait for the policy changes to come
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(10000, currentDate, TransactionType.Policy_Change, cppPolicyObj.commercialPackage.getPolicyNumber());
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(10000, currentDate, TransactionType.Policy_Change, bopPolicyObj.busOwnLine.getPolicyNumber());
        //override BOP with CPP
        BCSearchPolicies search = new BCSearchPolicies(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        BCPolicySummary summary = new BCPolicySummary(driver);
        summary.overrideInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        //move clock to couple of days before next Due date
        currentDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(cppPolicyObj.commercialPackage.getEffectiveDate(), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, -2);
        ClockUtils.setCurrentDates(driver, currentDate);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
    }

    @Test(dependsOnMethods = {"overrideBOP"})
    public void cancelCPP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "cancel cpp", currentDate, true);
    }

    //shouldn't trigger UW delinquency if not all policies in the invoice stream go delinquent
    @Test(dependsOnMethods = {"cancelCPP"})
    public void verifyUWDelinquencyAfterCancelOnePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        //wait for CPP cancellation to come
        acctMenu.clickBCMenuCharges();
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(10000, currentDate, TransactionType.Cancellation, cppPolicyObj.commercialPackage.getPolicyNumber());
        //verify that the UW delinquency is NOT triggered (since BOP is not delinquent in the same invoice stream)
        acctMenu.clickBCMenuSummary();
        BCCommonSummary acctSummary = new BCCommonSummary(driver);
        try {
            delinquencyAlert = acctSummary.getDelinquencyAlertMessage();
            if (delinquencyAlert.contains(bannerMessage))
                Assert.fail(driver.getCurrentUrl() + " the UW cancel should not be triggered if not all policies in the same invoice stream are delinquent.");
        } catch (Exception e) {
            System.out.println("Doesn't find the Underwrithing Full cancel which is expected");
        }
    }

    @Test(dependsOnMethods = {"verifyUWDelinquencyAfterCancelOnePolicy"})
    public void cancelBOP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "cancel bop", currentDate, true);
    }

    @Test(dependsOnMethods = {"cancelBOP"})
    public void verifyUWDelinquencyAfterCancelBothPolicies() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        //wait for BOP cancellation to come
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(10000, currentDate, TransactionType.Cancellation, bopPolicyObj.busOwnLine.getPolicyNumber());
        //verify that the UW delinquency is triggered
        acctMenu.clickBCMenuSummary();
        BCCommonSummary acctSummary = new BCCommonSummary(driver);
        try {
            delinquencyAlert = acctSummary.getDelinquencyAlertMessage();
            if (!delinquencyAlert.contains(bannerMessage))
                Assert.fail(driver.getCurrentUrl() + " doesn't find the UW cancel delinquency message.");
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " the UW cancel should be triggered if all policies in the same invoice stream are delinquent.");
        }
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        //the delinquent target should be the lead policy (CPP policy number in this test)
        try {
            delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel, cppPolicyObj.accountNumber, null, cppPolicyObj.commercialPackage.getPolicyNumber(), currentDate, null, null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + "doesn't find the UW cancel delinquency");
        }
    }

    @Test(dependsOnMethods = {"verifyUWDelinquencyAfterCancelBothPolicies"})
    public void verifyBalanceDueLettersAndActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        //verify the trigger of first balance due notice
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        moveClockAndRunWorkflow(1, true);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDocuments();
        verifyBalanceDueDocument(DocumentType.First_Notice_Balance_Due, currentDate);

        //verify the trigger of final balance due notice
        moveClockAndRunWorkflow(21, false);
        document.clickSearch();
        verifyBalanceDueDocument(DocumentType.Final_Notice_Balance_Due, currentDate);
        //verify the Review Delinquency Activity
        moveClockAndRunWorkflow(21, false);
        acctMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
        try {
            activity.clickActivityTableSubject(currentDate, null, null, ActivityStatus.Open, activityLinkText);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " doesn't find the Review Delinquency Activity on inception date + 43 days.");
        }
        //click "Write Off" button
        activity.clickWriteoff();

        //verify the delinquency is closed
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        try {
            delinquency.getDelinquencyTableRow(OpenClosed.Closed, DelinquencyReason.UnderwritingFullCancel, cppPolicyObj.accountNumber, null, cppPolicyObj.commercialPackage.getPolicyNumber(), null, null, 0.0);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " the policy is not sucessfully written off.");
        }
    }
}
