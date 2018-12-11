package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AddressType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description US5435: Disbursements - Change default name & address for intercompany transfers
 * US6854: Disbursements - Changes to Billing Address must update open disbursements
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-04%20Account%20Free-Form%20Disbursements.docx">See requirement 07-04-11</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-02%20Account%20Disbursement%20Requirements.docx">See req 07-02-17, 18, and user interface references.</a>
 * @DATE April 20th, 2016
 */
@QuarantineClass
public class DisbursementBillingAddressChangeAndDefaultNameSwitchingTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;	
	private double disburseAmount=NumberUtils.generateRandomNumberInt(30, 100);
	private String loanNumber="LN11111";
	private String lienholderNumber;	
	private AccountDisbursements disbursement;
	private String farmbureauAddress="275 Tierra Vista Drive, Pocatello Idaho 83201-5813";		
	private String payTo="whoever";
	
	private AddressInfo newBillingAddress = new AddressInfo() {		
		{
			this.setLine1("123 Stillwater Dr");
			this.setCity("Idaho Falls");	
			this.setState(State.Idaho);
			this.setZip("83404-8296");			
	}};	
	public boolean waitUntilNewAddressArrive(int secondsToWait, AccountDisbursements disburse){		
		String addressLine1 = disburse.getAddressLine1();		
		boolean found = false;
		long secondsRemaining = secondsToWait;
		int delayInterval = 1;
		while (!found && (secondsRemaining > 0)) {
			if(addressLine1.equals(newBillingAddress.getLine1())) {
				found = true;
				break;
			} else {
				secondsRemaining -= delayInterval * 1000;
                BCAccountMenu acctMenuStuff = new BCAccountMenu(driver);
				acctMenuStuff.clickBCMenuSummary();
				acctMenuStuff.clickAccountMenuDisbursements();
				addressLine1 = disburse.getAddressLine1();				
			}
		}		
		return found;
	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, false);
		myLineAddCov.setEmployeeDishonestyCoverage(true);
		myLineAddCov.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
		myLineAddCov.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Annually);
		myLineAddCov.setEmpDisLimit(EmpDishonestyLimit.Dishonest5000);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("FreeFormBox")
                .withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();			
	}
	@Test(dependsOnMethods = { "generate" })	
		public void createDisbursementForInsured() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getInsuredPremium() + disburseAmount, myPolicyObj.busOwnLine.getPolicyNumber());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
		//verify the disbursement
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
        try {
            disbursement.getDisbursementsTableRow(null, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, disburseAmount, null, null, null);
		}catch(Exception e){
			Assert.fail("doesn't find the disbursement.");
		}		
	}
	@Test(dependsOnMethods = { "createDisbursementForInsured" })
    public void changeBillingNameInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
		changeContactInfo(newBillingAddress, AddressType.Billing);
	}
	
	
	
	public void changeContactInfo(AddressInfo newAddress, AddressType addressTypeToSelect) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Change Contact Info", null);

        try {
        	policyChangePage.clickWhenClickable(policyChangePage.changePrimaryInsuredContact);
            WebElement newButton = policyChangePage.find(By.xpath("//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]"));
            policyChangePage.clickWhenClickable(newButton);
            policyChangePage.clickWhenClickable(policyChangePage.find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));

            try {
            	policyChangePage.setChangeReason("Test Policy Change");
            } catch (Exception e) {
            }
            // clickOK();
            policyChangePage.clickUpdate();
            if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
            	policyChangePage.clickUpdate();
            }
            policyChangePage.quoteAndIssue();
        } catch (Exception e) {
            System.out.println("Error occured on 'changeContactInfo'.");
            policyChangePage.clickWithdrawTransaction();
            e.printStackTrace();
            Assert.fail("Error occured while trying change Contact Info.");
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	@Test(dependsOnMethods = { "changeBillingNameInPolicyCenter" })	
	public void verifyNewAddressOnDisbursement() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
		if(waitUntilNewAddressArrive(10000, disbursement)){			
			if(!(disbursement.getAddressCity().equals(newBillingAddress.getCity())) || !(disbursement.getAddressState().equals(newBillingAddress.getState().toString())) || !(disbursement.getAddressPostalCode().equals(newBillingAddress.getZip()))){
				Assert.fail("the new billing address is incorrect");
			}
			
		}else{
			Assert.fail("the billing address is not updated");
		}		
	}
	//for automatic disbursement, if choosing the reason as Intercompany Transfer, the address should automatically change to FB address	
	@Test(dependsOnMethods = { "verifyNewAddressOnDisbursement" })	
	public void verifyAddressChangeOnInterCompany() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
		disbursement.clickAccountDisbursementsEdit();
		disbursement.selectDisbursementReason(DisbursementReason.IntercompanyTransfer);
		disbursement.setPayTo(payTo);
		disbursement.clickAccountDisbursementsUpdate();
		if(!farmbureauAddress.contains(disbursement.getAddressLine1()) || !farmbureauAddress.contains(disbursement.getAddressCity()) || !farmbureauAddress.contains(disbursement.getAddressState()) || !farmbureauAddress.contains(disbursement.getAddressPostalCode())){
			Assert.fail("the disbursement address is not changed to farm bureau address after Intercompany Tranfer is selected.");
		}	
	}
	//verify the address change on Inter company transfer reason for LH (manual disbursement)
	@Test(dependsOnMethods = { "verifyNewAddressOnDisbursement" })	
	public void verifyAddressChangeOnInterCompanyOnLH() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
		//make a payment to unapplied fund for LH
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.setAmount(disburseAmount);
		directPayment.clickOverrideDistribution();
		directPayment.clickExecuteWithoutDistribution();
		//open the disbursement wizard
		acctMenu.clickAccountMenuActionsNewTransactionDisbursement();
		CreateAccountDisbursementWizard disbursementWizard = new CreateAccountDisbursementWizard(driver);
		disbursementWizard.setCreateAccountDisbursementWizardReasonFor(DisbursementReason.IntercompanyTransfer);
		disbursementWizard.setCreateAccountDisbursementWizardAmount(disburseAmount);
		disbursementWizard.setCreateAccountDisbursementWizardDueDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		disbursementWizard.setCreateAccountDisbursementWizardPayTo(payTo);
		disbursementWizard.clickNext();
		disbursementWizard.clickFinish();
		acctMenu.clickAccountMenuDisbursements();
		disbursement = new AccountDisbursements(driver);
		disbursement.getDisbursementsTableRow(null, null, null, "Default", DisbursementStatus.Approved, null, disburseAmount, payTo, null, null).click();

		if(!farmbureauAddress.contains(disbursement.getAddressLine1()) || !farmbureauAddress.contains(disbursement.getAddressCity()) || !farmbureauAddress.contains(disbursement.getAddressState()) || !farmbureauAddress.contains(disbursement.getAddressPostalCode())){
			Assert.fail("the disbursement address is not changed to farm bureau address after Intercompany Tranfer is selected.");
		}	
	}
}
