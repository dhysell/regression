package regression.r2.clock.billingcenter.renewals;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
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
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;


/**
* @Author bhiltbrand
* @Requirement This test is to verify that a fully lien-billed policy that is not paid on renewal triggers the correct delinquencies.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/109320567860">Rally Defect DE5358</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/126036938208">Rally Defect DE5819</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/134445887780">Rally Defect DE6060</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-07%20Not%20Taken%20Cancel%20Delinquency.docx">Renewal Not Taken Delinquency Requirements</a>
* @Description 
* @DATE Jul 12, 2017
*/
public class TestLHRenewalNotTaken extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	public BCAccountMenu acctMenu;
	Underwriters uw;
	GeneratePolicy myPolicyObj;
	
	@Test
    public void generatePolicy() throws Exception {
		
		AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolTermLengthDays(50)
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Downpayment", "Shortage")
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeLienholderDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeLienholderDownPayment" })
	public void manuallyRenewPolicy() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 4);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void runBatchProcesses() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}
	
	@Test(dependsOnMethods = { "runBatchProcesses" })
    public void moveClocks2() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 25);		
	}
	
	@Test(dependsOnMethods = { "moveClocks2" })
    public void runBatchProcesses2() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}
	
	@Test(dependsOnMethods = { "runBatchProcesses2" })
    public void moveClocks3() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 21);		
	}
	
	@Test(dependsOnMethods = { "moveClocks3" })
    public void runInvoiceDueAndTriggerNotTakenDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDelinquencies();

        BCCommonDelinquencies accountDelinquencies = new BCCommonDelinquencies(driver);
		if (accountDelinquencies.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NotTaken, this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber(), this.myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), 0.00, 0.00)) {
			Assert.fail("A zero-dollar Not-Taken delinquency was opened on the LH. This should never happen. Test Failed.");
		}
		
		//Test for DE5819.
		if (accountDelinquencies.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NotTakenPartial, this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber(), this.myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter),null, null)) {
			Assert.fail("A Not-Taken Partial delinquency was opened on the LH. This should only be a Not-taken delinquency. Test Failed.");
		}
		
		//NOTE: NEED TO GET THE CORRECT DELINQUENT AMOUNT TO CHECK WHEN DELINQUENCIES SHOW THE WAY THEY ARE SUPPOSED TO.
		if (!accountDelinquencies.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.NotTaken, this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber(), this.myPolicyObj.accountNumber, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter),null, null)) {
			Assert.fail("The expected Not-Taken delinquency was not found on the LH. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	
	
	
	
	
	
	
	
}