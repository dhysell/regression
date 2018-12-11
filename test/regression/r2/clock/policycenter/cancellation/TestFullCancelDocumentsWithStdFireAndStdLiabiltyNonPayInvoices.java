package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
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
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
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
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10372: Document Payloads -- Full Cancel Documents with Multiple Policies.
 * @Description -
 * @DATE Aug 31, 2017
 */
public class TestFullCancelDocumentsWithStdFireAndStdLiabiltyNonPayInvoices extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;	
	private Underwriters uw;
	private Date targetDate = null;
    private GeneratePolicy myPolicyObject;
	private double combinedDownPaymentAmount = 0.00;

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
				.withInsFirstLastName("Fire", "Issuance")
				.withPolicyLocations(locationsList)				
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

//		StdLiabNonPayPol = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withStandardFirePolicyUsedForStandardLiability(this.stdFireNonPayPol, true)
//				.withPaymentPlanType(PaymentPlanType.Quarterly)
//				.withDownPaymentType(PaymentType.Cash)				
//				.build(GeneratePolicyType.PolicyIssued);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);
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

		//Add Standard Liability Policie to the Standard Fire Policy Trouble Ticket.
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
	public void NonpayDownPaymentInvoiceAndMoveClocks() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObject.accountNumber);

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

        acctMenu = new BCAccountMenu(driver);
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();
        DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        multiPayment.makeMultiplePayment(this.myPolicyObject.standardFire.getPolicyNumber(), PaymentInstrumentEnum.Check, this.combinedDownPaymentAmount);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(myPolicyObject.accountNumber);

		acctMenu.clickAccountMenuInvoices();
        AccountInvoices acctInvoice = new AccountInvoices(driver);
		Date invoiceDate = DateUtils.getDateValueOfFormat(acctInvoice.getListOfInvoiceDates().get(1), "MM/dd/yyyy");		
		ClockUtils.setCurrentDates(cf, invoiceDate);		

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		Date invoiceDueDate = DateUtils.getDateValueOfFormat(acctInvoice.getListOfDueDates().get(1), "MM/dd/yyyy");
		ClockUtils.setCurrentDates(cf, invoiceDueDate);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

	}

	@Test(dependsOnMethods = {"NonpayDownPaymentInvoiceAndMoveClocks"})
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

	private void testValidateMidTermNonPayCancelDocument(String policyNumber) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), policyNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Cancellation");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {"Mid Term Non Payment Cancel"};

		boolean testFailed = false;
		String errorMessage = "Policy Number: " + policyNumber;
		for (String document : documents) {	
			boolean documentFound = false;
			for(String desc: descriptions){
				if(desc.equals(document)){
					documentFound = true;
					break;
				}
			}

			if(!documentFound){
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '"+document+ "' not available in documents page. \n";
			}
		}
		if(testFailed)
			Assert.fail(errorMessage);
	}

}
