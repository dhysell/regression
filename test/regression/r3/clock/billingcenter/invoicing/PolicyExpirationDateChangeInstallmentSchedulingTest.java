package regression.r3.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
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
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author jqu
 * @Description US5498 Policy Change Installment Scheduling - For lead policy invoice stream
 * Change current logic to use invoice stream day of month. TEST EXPIRATION DATE CHANGES
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-07%20Policy%20Change%20Installment%20Scheduling.docx">Policy Change Installment Scheduling</a>
 * @Test Environment:
 * @DATE March 1st, 2016
 */
@QuarantineClass
public class PolicyExpirationDateChangeInstallmentSchedulingTest extends BaseTest {
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private Agents agent;
    private ARUsers arUser = new ARUsers();
    private Date currentSystemDate, newExpirationDate;
    private List<Date> cppDueDateListFromBC, cppDueDateListShouldBe;
    private List<Date> bopDueDateListFromBC, bopDueDateListShouldBe;
    private WebDriver driver;

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
                .withInsCompanyName("ExpDateChgInstallmentTest")
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
    public void verifyDateListOnPolicyLevel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);
        AccountInvoices acctInvoice = new AccountInvoices(driver);
        //verify the due date list of policy1
        cppDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        cppDueDateListShouldBe = acctInvoice.calculateListOfDueDates(cppPolicyObj.commercialPackage.getEffectiveDate(), cppPolicyObj.commercialPackage.getExpirationDate(), PaymentPlanType.Monthly);
        guidewireHelpers.verifyLists(cppDueDateListFromBC, cppDueDateListShouldBe);
        //verify the due date list of policy2
        bopDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        bopDueDateListShouldBe = acctInvoice.calculateListOfDueDates(bopPolicyObj.busOwnLine.getEffectiveDate(), bopPolicyObj.busOwnLine.getExpirationDate(), PaymentPlanType.Monthly);
        guidewireHelpers.verifyLists(bopDueDateListFromBC, bopDueDateListShouldBe);

        //move clock and make the down payment due
        currentSystemDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentSystemDate);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
    }

    //change expiration date for policy1
    @Test(dependsOnMethods = {"verifyDateListOnPolicyLevel"})
    public void changeCPPPolicyExpirationDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        newExpirationDate = DateUtils.dateAddSubtract(cppPolicyObj.commercialPackage.getExpirationDate(), DateAddSubtractOptions.Day, 2);
        policyChangePage.changeExpirationDate(newExpirationDate, cppPolicyObj.commercialPackage.getPolicyNumber() + "Expiration Date Change");
    }

    //verify policy1's date list after its expiration date is changed and verify that policy2 is not affected by policy1's change
    @Test(dependsOnMethods = {"changeCPPPolicyExpirationDate"})
    public void verifyAfterChangeCPPPolicyExpirationDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        //wait until the policy change comes to BC
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.cppPolicyObj.commercialPackage.getPolicyNumber());
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInvoice = new AccountInvoices(driver);
        //calculate policy1's Due date list after its expiration date is changed
        cppDueDateListShouldBe = acctInvoice.calculateListOfDueDates(DateUtils.dateAddSubtract(newExpirationDate, DateAddSubtractOptions.Year, -1), newExpirationDate, PaymentPlanType.Monthly);
        //down payment due date is not changed
        cppDueDateListShouldBe.set(0, DateUtils.getDateValueOfFormat(cppPolicyObj.commercialPackage.getEffectiveDate(), "MM/dd/yyyy"));
        cppDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.verifyLists(cppDueDateListFromBC, cppDueDateListShouldBe);
        //verify that Policy2's Dates are not changed after Policy1's expiration date change
        bopDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        guidewireHelpers.verifyLists(bopDueDateListFromBC, bopDueDateListShouldBe);
    }

    //change policy2's expiration date
    @Test(dependsOnMethods = {"verifyAfterChangeCPPPolicyExpirationDate"})
    public void changeBOPPolicyExpirationDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        newExpirationDate = DateUtils.dateAddSubtract(cppPolicyObj.commercialPackage.getExpirationDate(), DateAddSubtractOptions.Day, 5);
        policyChangePage.changeExpirationDate(newExpirationDate, bopPolicyObj.busOwnLine.getPolicyNumber() + "Expiration Date Change");
    }

    //verify policy2's date list after its expiration date is changed and verify that policy1 is not affected by policy2's change
    @Test(dependsOnMethods = {"changeBOPPolicyExpirationDate"})
    public void verifyAfterChangeBOPPolicyExpirationDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        //wait until the policy change comes to BC
        acctMenu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.bopPolicyObj.busOwnLine.getPolicyNumber());
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInvoice = new AccountInvoices(driver);
        //calculate policy1's Due date list after its exp. date is changed
        bopDueDateListShouldBe = acctInvoice.calculateListOfDueDates(DateUtils.dateAddSubtract(newExpirationDate, DateAddSubtractOptions.Year, -1), newExpirationDate, PaymentPlanType.Monthly);
        //down payment due date is not changed
        bopDueDateListShouldBe.set(0, DateUtils.getDateValueOfFormat(bopPolicyObj.busOwnLine.getEffectiveDate(), "MM/dd/yyyy"));
        bopDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber());
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.verifyLists(bopDueDateListFromBC, bopDueDateListShouldBe);
        //verify that Policy1's Dates are not changed after Policy2's expiration date change
        cppDueDateListFromBC = acctInvoice.getListOfDueDatesByInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        guidewireHelpers.verifyLists(cppDueDateListFromBC, cppDueDateListShouldBe);
    }
}
