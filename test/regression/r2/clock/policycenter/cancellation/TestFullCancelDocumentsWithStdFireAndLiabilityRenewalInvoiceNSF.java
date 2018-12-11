package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReversalReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TroubleTicketRelatedEntitiesOptions;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
@QuarantineClass
public class TestFullCancelDocumentsWithStdFireAndLiabilityRenewalInvoiceNSF extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObject;
    private ARUsers arUser;
    private Underwriters uw;
    private Date targetDate = null;
    private double combinedDownPaymentAmount, combinedRenewalDownPaymentAmount, combinedInvoicePaymentAmount = 0.00;

    @Test
    public void testIssueStandardFireWithLiabilityPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("Fire", "Renew")
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        
        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);

//        StdLibObj = new GeneratePolicy.Builder(driver)
//                .withProductType(ProductLineType.StandardLiability)
//                .withLineSelection(LineSelection.StandardLiabilityPL)
//                .withStandardFirePolicyUsedForStandardLiability(this.myPolicyObject, true)
//                .withStandardLiability(myStandardLiability)
//                .withPaymentPlanType(PaymentPlanType.Quarterly)
//                .withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueStandardFireWithLiabilityPol"})
    public void overrideInvoiceStreams() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObject.standardLiability.getPolicyNumber());

        //Close Trouble ticket for Standard Liability Policy and override invoice stream to the Standard Fire Policy.
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets policyTroubleTickets = new BCCommonTroubleTickets(driver);
        policyTroubleTickets.closeFirstTroubleTicket();

        policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuSummary();

        BCPolicySummary policySummary = new BCPolicySummary(driver);
        policySummary.overrideInvoiceStream(myPolicyObject.standardFire.getPolicyNumber());

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);

        //Add Standard Liability and Standard Inland Marine Policies to the Standard Fire Policy Trouble Ticket.
        policySearch = new BCSearchPolicies(driver);
        policySearch.searchPolicyByPolicyNumber(myPolicyObject.standardFire.getPolicyNumber());
        policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        policyTroubleTickets = new BCCommonTroubleTickets(driver);
        policyTroubleTickets.clickTroubleTicketNumberInTable(new TableUtils(driver).getRowCount(policyTroubleTickets.getTroubleTicketsTable()));
        policyTroubleTickets.addTroubleTicketRelatedEntities(TroubleTicketRelatedEntitiesOptions.Policies, myPolicyObject.standardLiability.getPolicyNumber());
        policyTroubleTickets.clickTroubleTicketCloseButton();
    }

    @Test(dependsOnMethods = {"overrideInvoiceStreams"})
    public void payDownPaymentAndMoveClocks() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObject.standardFire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        }
        acctMenu.clickBCMenuTroubleTickets();
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoices = new AccountInvoices(driver);
        combinedDownPaymentAmount = invoices.getInvoiceDueAmountByRowNumber(1);

        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Billed)) {
            acctMenu = new BCAccountMenu(driver);
            BCTopMenu topMenu = new BCTopMenu(driver);
            topMenu.clickDesktopTab();
            DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
            multiPayment.makeMultiplePayment(this.myPolicyObject.standardFire.getPolicyNumber(), PaymentInstrumentEnum.Check, this.combinedDownPaymentAmount);
        } else
            Assert.fail("running the invoice batch process  failed!!!!");

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

    }

    @Test(dependsOnMethods = {"payDownPaymentAndMoveClocks"})
    public void testCreateFireAndLiabilityRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        //Move clock 285 days to get Renew Policy option in Actions Menu
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 318);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.standardFire.getPolicyNumber());
        PolicyMenu pcMenu = new PolicyMenu(driver);
        pcMenu.clickRenewPolicy();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolicyObject);

        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(myPolicyObject.accountNumber, "003");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPLInsuranceScore();
        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObject);

        //Renew StdLiability Policy
        BCSearchPolicies search = new BCSearchPolicies(driver);
        search.searchPolicyByPolicyNumber(myPolicyObject.standardLiability.getPolicyNumber());
        pcMenu.clickRenewPolicy();
        preRenewalPage.closePreRenewalExplanations(myPolicyObject);
        searchJob.searchJobByAccountNumber(myPolicyObject.accountNumber, "003");
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObject);

    }

    @Test(dependsOnMethods = {"testCreateFireAndLiabilityRenewal"})
    public void NonpayRenewalInvoiceAndMoveClocks() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObject.accountNumber);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        AccountInvoices acctInvoice = new AccountInvoices(driver);
        Date invoiceDate2 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfInvoiceDates().get(1), "MM/dd/yyyy");
        combinedRenewalDownPaymentAmount = acctInvoice.getInvoiceDueAmountByRowNumber(2);
        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();
        ClockUtils.setCurrentDates(cf, invoiceDate2);

        DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        multiPayment.makeMultiplePayment(this.myPolicyObject.standardFire.getPolicyNumber(), PaymentInstrumentEnum.Check, this.combinedRenewalDownPaymentAmount);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(myPolicyObject.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();

        Date invoiceDate3 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfInvoiceDates().get(2), "MM/dd/yyyy");
        combinedInvoicePaymentAmount = acctInvoice.getInvoiceDueAmountByRowNumber(3);

        topMenu.clickDesktopTab();
        ClockUtils.setCurrentDates(cf, invoiceDate3);


        multiPayment.makeMultiplePayment(this.myPolicyObject.standardFire.getPolicyNumber(), PaymentInstrumentEnum.Check, combinedInvoicePaymentAmount);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        search.searchAccountByAccountNumber(myPolicyObject.accountNumber);
        accountMenu.clickAccountMenuInvoices();
        Date invoiceDueDate3 = DateUtils.getDateValueOfFormat(acctInvoice.getListOfDueDates().get(2), "MM/dd/yyyy");
        ClockUtils.setCurrentDates(cf, invoiceDueDate3);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

        //reverse the payment
        search.searchAccountByAccountNumber(myPolicyObject.accountNumber);

        acctMenu.clickAccountMenuPayments();
        AccountPayments payment = new AccountPayments(driver);
        //payment.reversePaymentAtFault(combinedInvoicePaymentAmount , null, null, PaymentReturnedCheckReason.Insufficient_Funds);
        payment.reversePayment(combinedInvoicePaymentAmount, null, null, PaymentReversalReason.Processing_Error_Did_Not_Go_To_Bank);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);


    }

    @Test(dependsOnMethods = {"NonpayRenewalInvoiceAndMoveClocks"})
    public void verifydelinquencytriggered() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObject.accountNumber);
        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();
        targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean FireDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObject.accountNumber, null, targetDate);

        if (!FireDelinquencyFound) {
            Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
        }
    }

    @Test(dependsOnMethods = {"verifydelinquencytriggered"})
    private void testValidateMidTermNonPayCancelDocument() throws Exception {
        testValidateMidTermNonPayCancelDocument(myPolicyObject.standardFire.getPolicyNumber());
        testValidateMidTermNonPayCancelDocument(myPolicyObject.standardLiability.getPolicyNumber());
    }

    private void testValidateMidTermNonPayCancelDocument(String PolicyNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), PolicyNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Cancellation");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"NSF Cancel Notice"};

        boolean testFailed = false;
        String errorMessage = "Policy Number: " + PolicyNumber;
        for (String document : documents) {
            boolean documentFound = false;
            for (String desc : descriptions) {
                if (desc.equals(document)) {
                    documentFound = true;
                    break;
                }
            }
            if (!documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected document : '" + document + "' not available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }


}


