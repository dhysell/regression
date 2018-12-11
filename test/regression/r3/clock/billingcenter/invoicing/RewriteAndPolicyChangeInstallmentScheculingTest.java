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
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
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
 * @Description US5499 Rewrite Installment Scheduling - For Policy Level Billing
 * 1) Test Rewrite Remainder of Term and Rewrite New Term
 * 2) Change current logic to use invoice stream day of month.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-10%20Rewrite%20Installment%20Scheduling.docx">Rewrite Installment Scheduling</a>
 * @Test Environment:
 * @DATE March 8, 2016
 */
@QuarantineClass
public class RewriteAndPolicyChangeInstallmentScheculingTest extends BaseTest {
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private Agents agent;
    private ARUsers arUser = new ARUsers();
    private Date currentSystemDate;
    private List<Date> cppDueDateListFromBC, cppDueDateListShouldBe;
    private List<Date> bopDueDateListFromBC, bopDueDateListShouldBe;
    private Date rewriteDownPaymentDate;
    private AccountInvoices acctInvoice;
    private BCAccountMenu acctMenu;
    private WebDriver driver;

    private List<Date> verifyDateListsBetweenBCAndCalculation(String policyNumber, Date effectiveDate, Date expirationDate) throws Exception {
        List<Date> dueDateListFromBC, dueDateListShouldBe;
        dueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(policyNumber);
        dueDateListShouldBe = acctInvoice.calculateListOfDueDates(effectiveDate, expirationDate, PaymentPlanType.Monthly);
        new GuidewireHelpers(driver).verifyLists(dueDateListFromBC, dueDateListShouldBe);
        return dueDateListShouldBe;
    }

    private void payDownPayment(String policyNumber, double downpaymentAmt) {
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeDirectBillPaymentExecute(downpaymentAmt, policyNumber);
    }

    private void verifyDateListsBetweenBCAndCalculationAfterRewrite(List<Date> dateListFromBC, List<Date> calculatedDateList, Date rewriteDowpaymentDate, String policyNumber) {
        int i = dateListFromBC.size() - 1;
        int j = calculatedDateList.size() - 1;
        while (!dateListFromBC.get(i).equals(rewriteDowpaymentDate) && j > 0) {
            if (!dateListFromBC.get(i).equals(calculatedDateList.get(j))) {
                Assert.fail(driver.getCurrentUrl() + "for policy " + policyNumber + ": found wrong invoice installment: the due date should be " + calculatedDateList.get(j) + " while it is " + dateListFromBC.get(i));
            }
            i--;
            j--;
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
                .withInsCompanyName("RewriteInstallmentTest")
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
    public void verifyDatesOnPolicyLevel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        //verify the due date list of CPP policy
        verifyDateListsBetweenBCAndCalculation(cppPolicyObj.commercialPackage.getPolicyNumber(), cppPolicyObj.commercialPackage.getEffectiveDate(), cppPolicyObj.commercialPackage.getExpirationDate());
        double cppDownPayment = acctInvoice.getInvoiceDueAmountByDueDate(currentSystemDate);
        //verify the due date list of BOP policy
        //BOP policy will have "Rewrite Remainder of Term". After the rewrite the new invoices due date will keep the same as before cancellation
        //will pass bopDueDateListShouldBe to verifyDateListsBetweenBCAndCalculationAfterRewrite()
        bopDueDateListShouldBe = verifyDateListsBetweenBCAndCalculation(bopPolicyObj.busOwnLine.getPolicyNumber(), bopPolicyObj.busOwnLine.getEffectiveDate(), bopPolicyObj.busOwnLine.getExpirationDate());
        double bopDownPayment = acctInvoice.getInvoiceDueAmountByDueDate(currentSystemDate);
        //payoff down payment for the two policies
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);
        payDownPayment(cppPolicyObj.commercialPackage.getPolicyNumber(), cppDownPayment);
        payDownPayment(bopPolicyObj.busOwnLine.getPolicyNumber(), bopDownPayment);
        //move clock and make first invoice Due
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"verifyDatesOnPolicyLevel"})
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
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Cancellation, this.bopPolicyObj.busOwnLine.getPolicyNumber());
        //get new Due Date lists from Invoice screen
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        cppDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        bopDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        //all the future invoices of the two policies should roll up after cancellation
        if (cppDueDateListFromBC.size() > 2 || bopDueDateListFromBC.size() > 2)
            Assert.fail(driver.getCurrentUrl() + "invoices are incorrect after policies cancellation.");
    }

    //Move a couple of days and rewrite BOP policy by Rewrite Remainder Term
    //After the rewrite the new invoices due dates will keep the same as before cancellation
    @Test(dependsOnMethods = {"verifyTheCancellationInBC"})
    public void rewriteRemainerTerm() throws Exception {
        //move couples of days, then rewrite one policy by "Rewrite Remainder Term"
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 3);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        new Login(driver).loginAndSearchAccountByAccountNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        //rewrite the policies
        StartRewrite rewritePol = new StartRewrite(driver);
        rewritePol.rewriteRemainderOfTerm(bopPolicyObj);
    }

    @Test(dependsOnMethods = {"rewriteRemainerTerm"})
    public void verifyNewInstallmentsAfterRewriteRemainderTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Rewrite, this.bopPolicyObj.busOwnLine.getPolicyNumber());
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        bopDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        //verify the Rewrite Down Payment invoice. Its due date is one day after current date
        rewriteDownPaymentDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        try {
            acctInvoice.getAccountInvoiceTableRow(rewriteDownPaymentDate, rewriteDownPaymentDate, null, InvoiceType.RewriteDownPayment, null, InvoiceStatus.Planned, null, null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + "didn't find the Rewrite Remainer Term down payment.");
        }
        verifyDateListsBetweenBCAndCalculationAfterRewrite(bopDueDateListFromBC, bopDueDateListShouldBe, rewriteDownPaymentDate, bopPolicyObj.busOwnLine.getPolicyNumber());
    }

    //Rewrite New Term for CPP policy
    //after Rewrite New Term, the new invoices should have new due dates which are depend on rewrite date
    @Test(dependsOnMethods = {"verifyNewInstallmentsAfterRewriteRemainderTerm"})
    public void rewriteNewTermOnTheOtherPolicy() throws Exception {
        //move 1 month, then rewrite the other policy by "Rewrite New Term"
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 1);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        new Login(driver).loginAndSearchAccountByAccountNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        //rewrite the policies
        StartRewrite rewritePol = new StartRewrite(driver);
        rewritePol.rewriteNewTerm(LineSelection.CommercialAutoLineCPP);
    }

    @Test(dependsOnMethods = {"rewriteNewTermOnTheOtherPolicy"})
    public void verifyNewInstallmentsAfterRewriteNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        //verify the rewrite for CPP policy
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Rewrite, this.cppPolicyObj.commercialPackage.getPolicyNumber());
        acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        cppDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        //verify the Rewrite Down Payment. Its due date is one day after current date
        rewriteDownPaymentDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        try {
            acctInvoice.getAccountInvoiceTableRow(rewriteDownPaymentDate, rewriteDownPaymentDate, null, InvoiceType.RewriteDownPayment, null, InvoiceStatus.Planned, null, null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + "didn't find the Rewrite Remainer Term down payment.");
        }
        //calculate new due date list. New Expiration date for New Term is 1y+1d from current date
        Date newExpDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1), DateAddSubtractOptions.Year, 1);
        cppDueDateListShouldBe = acctInvoice.calculateListOfDueDates(DateUtils.dateAddSubtract(newExpDate, DateAddSubtractOptions.Year, -1), newExpDate, PaymentPlanType.Monthly);
        verifyDateListsBetweenBCAndCalculationAfterRewrite(cppDueDateListFromBC, cppDueDateListShouldBe, rewriteDownPaymentDate, cppPolicyObj.commercialPackage.getPolicyNumber());
    }
}
