package regression.r3.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author jqu
 * @Description US6705 Invoices screen after Policies override reversal
 * After policies override and the override reversal, the Invoices should keep the same as what they look like before the override.
 * The Due Date and Invoice Type should keep unchanged.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/Supporting%20Work%20Items/Overriding%20Invoice%20Streams%20-%20Changes%20to%20Invoicing.xlsx">General Installment Scheduling: Please note requirements 11-01-11 through 13 and 11-01-16 and 11-01-18</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/19%20-%20UI%20-%20Policy%20Screens/19-06%20Override%20Policy%20Invoice%20Stream.docx"> Override Policy Invoice Stream: Please note requirements 19-06-04 through 05, and 19-06-10</a>
 * @DATE March 17nd, 2016
 */
@QuarantineClass
public class InstallmentAfterPolicyOverrideReversalTest extends BaseTest {
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private Agents agent;
    private ARUsers arUser = new ARUsers();
    private Date currentSystemDate;
    private List<Date> bopDueDateShouldBe, bopDueDateAfterOverrideReversal, cppDueDateShouldBe, cppDueDateAfterOverrideReversal;
    private List<InvoiceType> cppInvTypeShouldBe, cppInvTypeAfterOverrideReversal, bopInvTypeShouldBe, bopInvTypeAfterOverrideReversal;
    private BCAccountMenu acctMenu;
    private AccountInvoices acctInvoice;
    private double cppDownPayment, bopDownPayment;
    private WebDriver driver;

    private void payDownPayment(String policyNumber, double downpaymentAmt) {
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeDirectBillPaymentExecute(downpaymentAmt, policyNumber);
    }

    private void gotoPolicy(String policyNumber) {
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByPolicyNumber(policyNumber);
    }

    private void makeOverrideAndReverseIt(String leadPolicyNumber) {
        BCPolicySummary pcSummary = new BCPolicySummary(driver);
        pcSummary.overrideInvoiceStream(leadPolicyNumber);
        //reverse the override
        pcSummary.reverseInvoiceStreamOverride();
        pcSummary.clickAccountNumberLink();
    }

    private void getListsAfterOverrideReversalAndVerify() {
        acctMenu.clickAccountMenuInvoices();
        acctInvoice.setInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        cppInvTypeAfterOverrideReversal = acctInvoice.getListOfInvoiceTypes();
        cppDueDateAfterOverrideReversal = acctInvoice.getListOfDueDates();
        acctInvoice.setInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        bopInvTypeAfterOverrideReversal = acctInvoice.getListOfInvoiceTypes();
        bopDueDateAfterOverrideReversal = acctInvoice.getListOfDueDates();
        //for each policy, its due date list and Invoice Type list should be the same before override and after override reversal.
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.verifyLists(cppInvTypeShouldBe, cppInvTypeAfterOverrideReversal);
        guidewireHelpers.verifyLists(cppDueDateShouldBe, cppDueDateAfterOverrideReversal);
        guidewireHelpers.verifyLists(bopInvTypeShouldBe, bopInvTypeAfterOverrideReversal);
        guidewireHelpers.verifyLists(bopDueDateShouldBe, bopDueDateAfterOverrideReversal);
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
                .withInsCompanyName("OverrideReversalInstallmentTest")
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
        locOneBuildingList.add(new PolicyLocationBuilding());
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

    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void testAfterIssuance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        //need to close the trouble ticket, otherwise the hold will stop the override
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
        if (!troubleTicket.waitForTroubleTicketsToArrive(300)) {
            Assert.fail(driver.getCurrentUrl() + "doesn't receive the trouble ticket for policy " + cppPolicyObj.commercialPackage.getPolicyNumber());
        }
        troubleTicket.closeFirstTroubleTicket();

        BCSearchPolicies search = new BCSearchPolicies(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        policyMenu.clickBCMenuTroubleTickets();
        if (!troubleTicket.waitForTroubleTicketsToArrive(300)) {
            Assert.fail(driver.getCurrentUrl() + "doesn't receive the trouble ticket for policy " + bopPolicyObj.busOwnLine.getPolicyNumber());
        }
        troubleTicket.closeFirstTroubleTicket();

        //go to account level
        policyMenu.clickTopInfoBarAccountNumber();
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        //get the Due Date list and Invoice Type list for the two policies before override
        acctInvoice.setInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        cppInvTypeShouldBe = acctInvoice.getListOfInvoiceTypes();
        cppDueDateShouldBe = acctInvoice.getListOfDueDates();
        cppDownPayment = acctInvoice.getInvoiceDueAmountByDueDate(currentSystemDate);
        acctInvoice.setInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        bopInvTypeShouldBe = acctInvoice.getListOfInvoiceTypes();
        bopDueDateShouldBe = acctInvoice.getListOfDueDates();
        bopDownPayment = acctInvoice.getInvoiceDueAmountByDueDate(currentSystemDate);
        //override CPP policy with BOP policy, then reverse the override
        gotoPolicy(cppPolicyObj.commercialPackage.getPolicyNumber());
        makeOverrideAndReverseIt(bopPolicyObj.busOwnLine.getPolicyNumber());
        //get the new Due Date list and Invoice Type list after override reversal and verify with the lists before override
        getListsAfterOverrideReversalAndVerify();
        //pay off down payments and make them due
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);
        payDownPayment(cppPolicyObj.commercialPackage.getPolicyNumber(), cppDownPayment);
        payDownPayment(bopPolicyObj.busOwnLine.getPolicyNumber(), bopDownPayment);
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
    }

    //move clock to next invoice, override and reverse it and verify
    @Test(dependsOnMethods = {"testAfterIssuance"})
    public void testInMidTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 20);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        //override CPP policy with BOP policy and then reverse it
        gotoPolicy(cppPolicyObj.commercialPackage.getPolicyNumber());
        makeOverrideAndReverseIt(bopPolicyObj.busOwnLine.getPolicyNumber());
        //get the new Due Date list and Invoice Type list after override reversal and verify with the lists before override
        getListsAfterOverrideReversalAndVerify();
    }

    //cancel both policies, rewrite them and then override/reversal after rewrite
    @Test(dependsOnMethods = {"testInMidTerm"})
    public void cancelBothPolicies() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        //cancel BOP policy
        StartCancellation cancelPolicy = new StartCancellation(driver);
        cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Cancel Policy", currentSystemDate, true);
        new GuidewireHelpers(driver).logout();
        //the two policies may have different underwriters
        new Login(driver).loginAndSearchPolicyByPolicyNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        //cancel CPP policy
        cancelPolicy = new StartCancellation(driver);
        cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Cancel Policy", currentSystemDate, true);
    }

    @Test(dependsOnMethods = {"cancelBothPolicies"})
    public void verifyTheCancellationInBC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        //wait for the two policy cancellations coming to BC
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Cancellation, this.cppPolicyObj.commercialPackage.getPolicyNumber());
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Cancellation, this.cppPolicyObj.commercialPackage.getPolicyNumber());
    }

    //Move a couple of days and rewrite both policies
    //After the rewrite the new invoices due dates will keep the same as before cancellation
    @Test(dependsOnMethods = {"verifyTheCancellationInBC"})
    public void rewriteRemainerTerm() throws Exception {
        //move couples of days, then rewrite them
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 3);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        //rewrite CPP policy
        StartRewrite rewritePol = new StartRewrite(driver);
        rewritePol.rewriteRemainderOfTerm(cppPolicyObj);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicyByPolicyNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        //rewrite BOP policy
        rewritePol = new StartRewrite(driver);
        rewritePol.rewriteRemainderOfTerm(bopPolicyObj);
    }

    @Test(dependsOnMethods = {"rewriteRemainerTerm"})
    public void verifyOverrideReversalAfterRewrite() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Rewrite, this.bopPolicyObj.busOwnLine.getPolicyNumber());
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Rewrite, this.cppPolicyObj.commercialPackage.getPolicyNumber());
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        //get the Due Date list and Invoice Type list for the two policies before override
        acctInvoice.setInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        cppInvTypeShouldBe = acctInvoice.getListOfInvoiceTypes();
        cppDueDateShouldBe = acctInvoice.getListOfDueDates();
        acctInvoice.setInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        bopInvTypeShouldBe = acctInvoice.getListOfInvoiceTypes();
        bopDueDateShouldBe = acctInvoice.getListOfDueDates();
        //override one policy to the other one
        gotoPolicy(cppPolicyObj.commercialPackage.getPolicyNumber());
        makeOverrideAndReverseIt(bopPolicyObj.busOwnLine.getPolicyNumber());
        //get the new Due Date list and Invoice Type list after override reversal and verify with the lists before override
        getListsAfterOverrideReversalAndVerify();
    }
}
