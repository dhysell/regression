package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TroubleTicketFilter;
import repository.gw.enums.TroubleTicketType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sgunda
* @Requirement Closing trouble ticket throws OOTB exception if there is due amount from previous term only, and the tt holds delinquency
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/defect/218725260640">DE7481 - Closing trouble ticket throws OOTB exception if there is due amount from previous term only, and the tt holds delinquency</a>
* @DATE Apr 28, 2018
*/

@Test(groups = {"ClockMove"})
public class ClosingToubleTicketThrowsOOTBExceptionOnFarmAndRanchPolicyNoExceptions extends BaseTest {
	
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private Date policyChangeDate,invoiceDate,invoiceDueDate;

    @Test
    public void generateFarmAndRanch() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("DE7481", "FarmAndRanch")
                .withPolOrgType(OrganizationType.Individual)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);  
        
    }
    
    @Test(dependsOnMethods = { "generateFarmAndRanch" })	
	public void payDownpayment() throws Exception {			
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber()); 
        
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        policyChangeDate = DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -65);
        ClockUtils.setCurrentDates(driver, policyChangeDate);	
	}
    
    @Test(dependsOnMethods = { "payDownpayment" })	
   	public void doAPolicyChangeToCreateShortage() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.changePLPropertyCoverage(160000.0, policyChangeDate);
    }
    @Test(dependsOnMethods = { "doAPolicyChangeToCreateShortage" })	
   	public void makeShortageBilledAndDue() throws Exception {	
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
	     acctMenu.clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
	     Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Policy_Change), "Policy Change charges didn't make it to BC, Test can not continue");

	     acctMenu.clickAccountMenuInvoices();

        acctInvoice = new AccountInvoices(driver);
	     invoiceDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.Shortage);
	     invoiceDueDate = acctInvoice.getInvoiceDueDateByInvoiceType(InvoiceType.Shortage);
	     
	     ClockUtils.setCurrentDates(driver, invoiceDate);
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

	     acctMenu.clickBCMenuCharges();
	     ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(invoiceDueDate, DateAddSubtractOptions.Day, 1));
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	     acctMenu.clickAccountMenuInvoices();
	     Assert.assertTrue(acctInvoice.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Due, null), "Invoice did not go Due, Test can not continue");

	     acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquencies = new BCCommonDelinquencies(driver);
	     Assert.assertFalse(delinquencies.verifyDelinquencyExists(null, null, myPolicyObj.accountNumber, null, myPolicyObj.squire.getPolicyNumber(), null, null, null),"Delinquency should not trigger for a Farm and Ranch shortage, Test can not continue");
   
	     ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -25));	
    }
	
    @Test(dependsOnMethods = {"makeShortageBilledAndDue"})
	public void issueRenewal() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

	}
  
    @Test(dependsOnMethods = { "issueRenewal" })
    public void closeTTWithOutMakingPayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
	     
		 acctMenu.clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
	     Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");

	     acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
	     invoiceDate = acctInvoice.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment);
	     invoiceDueDate = acctInvoice.getInvoiceDueDateByInvoiceType(InvoiceType.RenewalDownPayment);
	     
	     ClockUtils.setCurrentDates(driver, invoiceDate);
	     new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

	     acctMenu.clickBCMenuCharges();
	     acctMenu.clickAccountMenuInvoices();
	     Assert.assertTrue(acctInvoice.verifyInvoice(InvoiceType.RenewalDownPayment, InvoiceStatus.Billed, null), "Invoice did not go billed, which wont create TT for holding Deliquency");

	     acctMenu.clickAccountMenuPolicies();
        AccountPolicies accountPolicy = new AccountPolicies(driver);
	     accountPolicy.clickPolicyNumber(myPolicyObj.squire.getPolicyNumber().concat("-2"));

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTickets = new BCCommonTroubleTickets(driver);
	     troubleTickets.clickTroubleTicketNumberBySubjectOrType(TroubleTicketType.RenewalGracePeriod);

        BCCommonTroubleTicket troubleTicket = new BCCommonTroubleTicket(driver);
	     try {
			troubleTicket.clickTroubleTicketCloseButton();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception caught, had some trouble closing TT");
		}
	     
	     policyMenu.clickBCMenuSummary();
	     policyMenu.clickBCMenuTroubleTickets();
	     
	     troubleTickets.setTroubleTicketFilter(TroubleTicketFilter.All);
	     troubleTickets.clickTroubleTicketNumberBySubjectOrType(TroubleTicketType.RenewalGracePeriod);
	     Assert.assertTrue(troubleTicket.verifyIfTroubleTicketIsInClosedStatus(), "TT didnot close after we closed the Trouble Ticket");
    }

}
