package regression.r3.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.CancellationEvent;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test is designed to verify a few aspects of delinquency and full cancels on insureds. It takes 2 policies on the same
 * account and randomly combines invoice streams. It then verifies that the correct amount and types of delinquencies are kicked off under
 * various circumstances, and that we receive the correct activities in Policy Center.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/41439116510">Rally Story US5508</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-01%20Insured%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Requirements Documentation</a>
 * @Description
 * @DATE May 20, 2016
 */
@QuarantineClass
public class TestDelinquencyCancelSPA extends BaseTest {
    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy multiPolicyObj = null;
    private Agents agent;
    private Date targetDate = null;
    private double firstPolicyDelinquentAmount = 0;
    private double secondPolicyDelinquentAmount = 0;
    private ARUsers arUser;
    private boolean combineInoviceStreams = getRandomBoolean();
    private boolean manuallyExitDelinquency = getRandomBoolean();

    private boolean getRandomBoolean() {
        Random rnd = new Random();
        return rnd.nextBoolean();
    }

    private WebDriver driver;

    // ///////////////////
    // Main Test Methods//
    // ///////////////////

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> driverList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(driverList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Del Driven Cancel")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        PolicyLocationBuilding defaultBuilding = new PolicyLocationBuilding();

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locOneBuildingList.add(defaultBuilding);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(myPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(myPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(myPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(myPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(myPolicyObj.pniContact.getAddress().getZip());

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.multiPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(myPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(myPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(OrganizationType.LLC)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(myPolicyObj.paymentPlanType)
                .withDownPaymentType(myPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void runInvoiceWithoutMakingDownpayment() throws Exception {
        /*
         * This method runs the invoice batch process and clears the promised
         * payment trouble ticket to prepare for going delinquent
         */
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        if (this.combineInoviceStreams) {
            Config cf = new Config(ApplicationOrCenter.BillingCenter);
            driver = buildDriver(cf);
            new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), multiPolicyObj.busOwnLine.getPolicyNumber());
            BCPolicySummary policySummary = new BCPolicySummary(driver);
            policySummary.overrideInvoiceStream(myPolicyObj.commercialPackage.getPolicyNumber());

            BCTopMenuAccount topMenuAccount = new BCTopMenuAccount(driver);
            topMenuAccount.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);
        } else {
            billingCenterLoginAndFindPolicy();
        }

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
        troubleTicket.closeFirstTroubleTicket();

        if (this.combineInoviceStreams) {
            try {
                troubleTicket = new BCCommonTroubleTickets(driver);
                troubleTicket.closeFirstTroubleTicket();
            } catch (Exception e) {
                //do nothing
            }
        }

        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceWithoutMakingDownpayment"})
    public void moveClocks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
    }

    @Test(dependsOnMethods = {"moveClocks"})
    public void runInvoiceDueAndCheckDelinquency() throws Exception {
        billingCenterLoginAndFindPolicy();

        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();

        this.targetDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.commercialPackage.getPolicyNumber(), targetDate);

        if (!firstPolicyDelinquencyFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "The first Delinquency was either non-existant or not in an 'open' status.");
        }

        if (!this.combineInoviceStreams) {
            boolean secondPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, multiPolicyObj.accountNumber, multiPolicyObj.busOwnLine.getPolicyNumber(), targetDate);

            if (!secondPolicyDelinquencyFound) {
                Assert.fail(driver.getCurrentUrl() + multiPolicyObj.accountNumber + "The second Delinquency was either non-existant or not in an 'open' status.");
            }
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceDueAndCheckDelinquency"})
    public void verifyCancelationCompletionInPolicyCenter() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(myPolicyObj.commercialPackage.getPolicyNumber(), 120);
        if (!pendingCancelFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
        }

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 20);

        new BatchHelpers(driver).runBatchProcess(BatchProcess.PC_Workflow);

        summaryPage = new AccountSummaryPC(driver);
        boolean firstPolicyCancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(myPolicyObj.commercialPackage.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
        if (!firstPolicyCancelledStatusFound) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "The first policy had not entered delinquency after 5 minutes of waiting.");
        }

        if (!this.combineInoviceStreams) {
            boolean secondPolicyCancelledStatusFound = summaryPage.verifyPolicyStatusInPolicyCenter(multiPolicyObj.busOwnLine.getPolicyNumber(), PolicyTermStatus.Canceled, 300);
            if (!secondPolicyCancelledStatusFound) {
                Assert.fail(driver.getCurrentUrl() + multiPolicyObj.accountNumber + "The second policy had not entered delinquency after 5 minutes of waiting.");
            }
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"verifyCancelationCompletionInPolicyCenter"})
    public void verifyDelinquencyAndAttemptToManuallyExitDelinquencyInBillingCenter() throws Exception {
        billingCenterLoginAndFindPolicy();

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();


        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);

        verifyDelinquencyEventCompletion(CancellationEvent.CancellationBillingInstructionReceived);

        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.BC_Workflow);

        driver.navigate().refresh();
        verifyDelinquencyEventCompletion(CancellationEvent.SendBalanceDue);

        this.firstPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.commercialPackage.getPolicyNumber(), targetDate);

        if (!this.combineInoviceStreams) {
            this.secondPolicyDelinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, multiPolicyObj.accountNumber, multiPolicyObj.busOwnLine.getPolicyNumber(), targetDate);
        }

        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 21);

        batchHelpers.runBatchProcess(BatchProcess.BC_Workflow);

        driver.navigate().refresh();
        verifyDelinquencyEventCompletion(CancellationEvent.SendSecondBalanceDue);

        if (this.combineInoviceStreams) {
            if (this.manuallyExitDelinquency) {
                delinquency.clickExitDelinquencyButton();
            } else {
                System.out.println("The decision was made to not manually exit delinquency in this test run. Test will continue.");
            }

        } else {
            System.out.println("The invoice streams on this test run were not combined. The final test in this run is unnecessary.");
        }
    }

    @Test(dependsOnMethods = {"verifyDelinquencyAndAttemptToManuallyExitDelinquencyInBillingCenter"})
    public void makePayment() throws Exception {
        billingCenterLoginAndFindPolicy();

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();

        menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();


        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
        if (this.combineInoviceStreams) {
            directBillPayment.makeDirectBillPaymentExecute(firstPolicyDelinquentAmount, myPolicyObj.commercialPackage.getPolicyNumber());
        } else {
            directBillPayment.makeDirectBillPaymentExecute(firstPolicyDelinquentAmount, myPolicyObj.commercialPackage.getPolicyNumber());
            accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();
            directBillPayment.makeDirectBillPaymentExecute(secondPolicyDelinquentAmount, multiPolicyObj.busOwnLine.getPolicyNumber());
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makePayment"})
    public void verifyPaymentOnCancelledPolicyActivityInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        AccountSummaryPC accountSummary = new AccountSummaryPC(driver);
        boolean paymentActivityFound = accountSummary.verifyCurrentActivity("Payment within 30 days of cancel", 2000);

        if (!paymentActivityFound) {
            Assert.fail("The activity \"Payment within 30 days of cancel\" was not found on the account summary screen. Test failed.");
        }

        accountSummary.clickActivitySubject("Payment within 30 days of cancel");

        ActivityPopup activityPopup = new ActivityPopup(driver);
        String activityDescription = activityPopup.getActivityDescription();

        if (!activityDescription.contains("Payment of null on account # " + this.myPolicyObj.fullAccountNumber + " was received on")) {
            Assert.fail("The Payment activity for payment on a cancelled policy did not match the description expected. The description contained in the activity was: " + activityDescription);
        }
    }

    // //////////////////////////////////////////////
    // Private Methods Used to support Test Methods//
    // //////////////////////////////////////////////

    private void billingCenterLoginAndFindPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
    }

    private void verifyDelinquencyEventCompletion(CancellationEvent cancellationEvent) {
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean delinquencyStepFound = false;
        HashMap<String, Double> delinquencyVerificationFailureMap = null;
        TableUtils tableUtils = new TableUtils(driver);
        int tableRows = tableUtils.getRowCount(delinquency.getDelinquencyTable());
        for (int i = 1; i <= tableRows; i++) {
            tableUtils.clickRowInTableByRowNumber(delinquency.getDelinquencyTable(), i);
            delinquency = new BCCommonDelinquencies(driver);
            delinquencyStepFound = delinquency.verifyDelinquencyEventCompletion(cancellationEvent);

            if (!delinquencyStepFound) {
                if (delinquencyVerificationFailureMap == null) {
                    delinquencyVerificationFailureMap = new HashMap<String, Double>();
                }
                delinquencyVerificationFailureMap.put(tableUtils.getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), tableUtils.getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquency Target"), NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(delinquency.getDelinquencyTable(), new TableUtils(driver).getHighlightedRowNumber(delinquency.getDelinquencyTable()), "Delinquent Ammount")));
            }
        }

        if (delinquencyVerificationFailureMap != null) {
            String errorMessage = "The Delinquency Event \"" + cancellationEvent.getValue() + "\" never triggered./n";
            for (Map.Entry<String, Double> entry : delinquencyVerificationFailureMap.entrySet()) {
                errorMessage += "Delinquency Target: " + entry.getKey() + ", Delinquent Amount: " + StringsUtils.currencyRepresentationOfNumber(entry.getValue()) + "/n";
            }
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + errorMessage);
        }
    }
}
