package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
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
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @Requirement US14912 - Insurance policy w/ Membership line Delinquency - Insured & Lien - Underwriting (Automate)
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/userstory/213782786320">Link Text<US14912/a>
 */


public class UWFullCancellationOnLienAndInsuredBilledPolicy extends BaseTest{
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private WebDriver driver;
	
		
	@Test
	public void squirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
				
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
		additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		additionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);	
		additionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		
		PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location2Property1.setBuildingAdditionalInterest(additionalInterest);
		locTwoPropertyList.add(location2Property1);
		locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
		
		SquireLiability liabilitySection = new SquireLiability();
		
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withSquireEligibility(SquireEligibility.City)	
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolicyLocations(locationsList)
				.withInsFirstLastName("Partial", "LH Cancel Sq")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
	}
	
	@Test(dependsOnMethods = { "squirePolicy" })
	public void runBatches() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 20);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "runBatches" })
	public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());
		
		StartCancellation startCancellation = new StartCancellation(driver);
		startCancellation.cancelPolicy(Cancellation.CancellationSourceReasonExplanation.OtherInsuredRequest, "CancelToTriggerUWF", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),true); 
	}
	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
	public void checkBillingCenterChargesAndTestUWF() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
		
		acctMenu = new BCAccountMenu(driver);
	    acctMenu.clickBCMenuCharges();
	    BCCommonCharges charges = new BCCommonCharges(driver); 
		charges.waitUntilChargesFromPolicyContextArrive(240, TransactionType.Cancellation);

	    acctMenu.clickAccountMenuInvoices();
	    AccountInvoices invoices = new AccountInvoices(driver);
	    Date invoiceDate = invoices.invoiceDate(null, null, null, null, InvoiceStatus.Planned, null, null);
	    Date dueDate = invoices.dueDate(null, null, null, null, InvoiceStatus.Planned, null, null);
	    
	    String invoiceNumber = invoices.getInvoiceNumber(invoiceDate, dueDate, null,null);
		ClockUtils.setCurrentDates(driver, invoiceDate );
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
    	ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(dueDate, DateAddSubtractOptions.Day, 1));
    	new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
	    
		acctMenu.clickBCMenuCharges();
	    acctMenu.clickAccountMenuInvoices();
	    Assert.assertTrue(invoices.verifyInvoice(null, null, invoiceNumber, null, null, InvoiceStatus.Due, null, null), "Cancellation Credit Invoice didn't go due");
	    
	    acctMenu.clickBCMenuDelinquencies();
	    BCCommonDelinquencies delinquencies = new BCCommonDelinquencies(driver);
	    Assert.assertTrue(delinquencies.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.UnderwritingFullCancel , null,null), "Didn't trigger Underwriting Full Cancel");
	}
	
		
}	