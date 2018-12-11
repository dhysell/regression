package regression.r3.clock.billingcenter.troubletickets;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Status;
import repository.gw.enums.TroubleTicketFilter;
import repository.gw.enums.TroubleTicketType;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/****************ready to commit**************************************************
 * @Author jqu
 * @Description: US6607 Promised Payment - Move promised payment to lead policy
 * @Steps: verify hard stop on overridden policy with Promised Payment TT; Verify TT closes on 90% payment threshold; verify TT closes on 10 days without payment;
 * 			Verify delinquency triggers after TT closes; Verify invoice is carried forward during TT hold period.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/16%20-%20Trouble%20Tickets/16-01%20Promised%20Payment%20Trouble%20Ticket.docx">Promised Payment Trouble Ticket</a>
 * @DATE June 15, 2016
 */
@QuarantineClass
public class PromisedPaymentTroubleTickectOnPolicyLevelTest extends BaseTest {
    private Agents agent;
    private float buildingLimit = 50000;
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private Date currentDate;
    private BCAccountMenu acctMenu;
    private String hardStopMsgOnOverride = "Can not override invoice stream for this policy period because it currently has holds.";

    private WebDriver driver;

    private void waitForTroubleTicketToArrive(int secondToWait, String policyNumber, BCPolicyMenu policyMenu) {
        policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
        if (!troubleTicket.waitForTroubleTicketsToArrive(secondToWait)) {
            Assert.fail(driver.getCurrentUrl() + "doesn't receive the trouble ticket for policy " + policyNumber);
        }
    }

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
        this.cppPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("MultiPolicy Creation")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding insuredBldg = new PolicyLocationBuilding();
        insuredBldg.setBuildingLimit(buildingLimit);
        insuredBldg.setBppLimit(buildingLimit);
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
                .withPolOrgType(OrganizationType.LLC)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withMembershipDuesOnAllInsureds()
                .withPaymentPlanType(cppPolicyObj.paymentPlanType)
                .withDownPaymentType(cppPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(cppPolicyObj.toString());
        System.out.println(bopPolicyObj.toString());
    }

    //verify TT on policy level and the hard stop on overriding policy with TT
    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void verifyTroubleTicketAndHardStopOnOverride() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        waitForTroubleTicketToArrive(30, cppPolicyObj.commercialPackage.getPolicyNumber(), policyMenu);
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        waitForTroubleTicketToArrive(30, bopPolicyObj.busOwnLine.getPolicyNumber(), policyMenu);
        //override BOP with CPP, verify the hard stop
        policyMenu.clickBCMenuSummary();
        BCPolicySummary summary = new BCPolicySummary(driver);
        summary.updateInvoicingOverride();
        PolicySummaryInvoicingOverrides invoiceStreamOverride = new PolicySummaryInvoicingOverrides(driver);
        invoiceStreamOverride.selectOverridingInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        invoiceStreamOverride.clickNext();
        try {
            String warningMsg = new GuidewireHelpers(driver).getFirstErrorMessage();
            if (!warningMsg.contains(hardStopMsgOnOverride)) {
                Assert.fail(driver.getCurrentUrl() + " doesn't find the correct hard stop message.");
            }

        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " doesn't find the warning when trying to override the policy with holds");
        }
    }

    @Test(dependsOnMethods = {"verifyTroubleTicketAndHardStopOnOverride"})
    public void payThresholdToCloseTTAndOverrideAgain() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.accountNumber);
        //pay threshold amount of BOP's down payment and verify that its TT closes
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeInsuredDownpayment(bopPolicyObj, NumberUtils.round(bopPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() * 0.91, 2), bopPolicyObj.busOwnLine.getPolicyNumber());
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        //verify that the TT closes after the payment
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
        troubleTicket.setTroubleTicketFilter(TroubleTicketFilter.All);
        try {
            troubleTicket.getTroubleTicketTableRow(null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, Status.Closed, TroubleTicketType.PromisePayment);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + "The trouble ticket is not closed after payment on " + bopPolicyObj.busOwnLine.getPolicyNumber());
        }
        //verify the override after BOP's TT is closed
        //override BOP with CPP
        policyMenu.clickBCMenuSummary();
        BCPolicySummary summary = new BCPolicySummary(driver);
        summary.overrideInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        //reverse the override
        summary.reverseInvoiceStreamOverride();
    }

    //not pay CPP and move 10 days, it should trigger the delinquency
    @Test(dependsOnMethods = {"payThresholdToCloseTTAndOverrideAgain"})
    public void verifyTTCloseOn10thDay() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentDate);
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 9);
        ClockUtils.setCurrentDates(driver, currentDate);
        batchHelpers.runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
        //verify the delinquency on CPP
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        try {
            delinquency.getDelinquencyTableRow(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null, null, cppPolicyObj.commercialPackage.getPolicyNumber(), currentDate, null, null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + "Past Due Full Cancel should be triggered for " + bopPolicyObj.busOwnLine.getPolicyNumber());
        }
    }
}
