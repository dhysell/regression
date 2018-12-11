package regression.r2.clock.billingcenter.cancel;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE6814 - BC not sending LH cancelling charges for TR and Regular Renewal not-taken when there are renewal charges not in down
 * Transition Renewal policy- City Squire- insured charges only  secs 1, 2 and 3- annually paid policy
 * Make a policy change after renewal invoice goes billed.- change sec 1- and 2 to LH billed- renewal and LH  invoices should be due the same day
 * Do not pay anything move clock to day after renewal date. run inv/inv due.  Let policy flat cancel
 * <p>
 * ACTUAL:  Got "Non Renew Non Payment cancel" document- (see attached) with insured charges only.  Document also says the LH name on the document.  "We have not received the required amount "from Idaho Central Credit Union"
 * We also got the termination of coverage for the additional interest, this is correct.
 * EXPECTED: The  insureds document needs to have the total amount due, insured and LH charges combined.  "Designated Payee" needs to print on the document instead of the LH name.
 * @DATE Dec 20, 2017
 */
public class BCNotSendingLHCancellationCharges extends BaseTest {
	private WebDriver driver;
    private ARUsers arUser;
    private BCAccountMenu acctMenu;
    private AccountInvoices acctInvoice;
    private GeneratePolicy myPolicyObj;
    private DesktopActionsMultiplePayment makePayment;


    private Date renewalInvoiceDate;
    private Date renewalInvoiceDueDate;
    private String lienHolderNumber;
    private String loanNumber = "TEST-DE6814";

    private void gotoAccount(String accountNumber) {
        BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(accountNumber);
    }


    @Test
    public void SquirePolicy() throws Exception {

    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);

        locToAdd.setPlNumAcres(30);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = property;
        mySquire.squireEligibility = SquireEligibility.City;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("DE6814", "Renewal")
                .withPolTermLengthDays(100)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"SquirePolicy"})
    public void makeDownPaymentAndMoveClockToRenew() throws Exception {

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(myPolicyObj, myPolicyObj.squire.getPolicyNumber());

        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Day, 10));
        BatchHelpers batchHelpers = new BatchHelpers(cf);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);

        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -50));

        new GuidewireHelpers(driver).logout();

    }


    @Test(dependsOnMethods = {"makeDownPaymentAndMoveClockToRenew"})
    public void renewlPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

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
    public void billInsuredRenewelBillInvoice() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges acctCharges = new AccountCharges(driver);
        acctCharges.waitUntilChargesFromPolicyContextArrive(180, TransactionType.Renewal);
        acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
        renewalInvoiceDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment);
        renewalInvoiceDueDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment);

        ClockUtils.setCurrentDates(driver, renewalInvoiceDate);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickBCMenuCharges();
        acctMenu.clickAccountMenuInvoices();
        Assert.assertTrue(acctInvoice.verifyInvoice(renewalInvoiceDate, null, null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Billed, null, null), "Renewal Down Payment invoice didn't get billed");

    }

    @Test(dependsOnMethods = {"billInsuredRenewelBillInvoice"})
    public void addLienToSectionOneAndTwo() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
        additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
        additionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        additionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        additionalInterest.setLoanContractNumber(loanNumber);

//        StartPolicyChange policyChange = new StartPolicyChange(driver);
        addAdditionInterestAndBillAllForSquire(additionalInterest, myPolicyObj.squire.getExpirationDate());
        new GuidewireHelpers(driver).logout();
    }
    
    
    private void addAdditionInterestAndBillAllForSquire(AdditionalInterest newInterest, Date effectiveDate) throws Exception {
    	StartPolicyChange policyChange = new StartPolicyChange(driver);
    	policyChange.startPolicyChange("Add New Interest on Building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);
		additionalInterest.fillOutAdditionalInterest(true, newInterest);
        propertyDetail.clickOk();
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllCoverages(newInterest.getLienholderPayerAssignmentString());
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
        policyChange.quoteAndIssue();
    }
    
    

    @Test(dependsOnMethods = {"addLienToSectionOneAndTwo"})
    public void verifyInvoiceDatesAndMakeThemDue() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges acctCharges = new AccountCharges(driver);
        acctCharges.waitUntilChargesFromPolicyContextArrive(180, TransactionType.Policy_Change);
        lienHolderNumber = acctCharges.getLienHolderNumberWithLoanNumber(loanNumber);

        gotoAccount(lienHolderNumber);
        acctMenu.clickAccountMenuInvoices();

        Assert.assertTrue(acctInvoice.verifyInvoice(renewalInvoiceDate, null, null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, null, null), "Renewal Down Payment invoice has a wrong date");
        Assert.assertTrue(acctInvoice.verifyInvoice(null, renewalInvoiceDueDate, null, InvoiceType.RenewalDownPayment, null, InvoiceStatus.Planned, null, null), "Renewal Down Payment invoice has a wrong date");

        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -10));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(renewalInvoiceDueDate, DateAddSubtractOptions.Day, 1));
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

    }

    @Test(dependsOnMethods = {"verifyInvoiceDatesAndMakeThemDue"})
    public void testPCCancellation() {


    }


}
