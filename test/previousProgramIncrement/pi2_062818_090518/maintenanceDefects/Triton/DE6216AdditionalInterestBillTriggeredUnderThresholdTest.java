package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Triton;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDocuments;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
/**
* @Author JQU
* @Requirement 	DE6216 - Additional Interest bill printing blank invoice
				Steps to get there:
					Create and issue a policy.
					Then do a policy change the same day the policy was created, by removing some coverages or objects, thus creating a credit.
					Look to see a if an invoice with the "Invoice Type" of "Shortage" is created.
					Now check to see if the document "Additional Interest Regular Bill" has printed because it should not.
					Actual: Interest bill is printing for credit/under threshold invoices with no information on the bill. 
					Expected: Bill should not print at all				
* 
* 
* @DATE July 10th, 2018
*/
public class DE6216AdditionalInterestBillTriggeredUnderThresholdTest extends BaseTest{
	 private WebDriver driver;
	 private GeneratePolicy myPolicyObj;
	 private ARUsers arUser = new ARUsers();
	 private String LHNubmer;

    
    private void generatePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

    	GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
    	driver.quit();
    	
    	ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1LNBldg1AdditionalInterests.add(loc1Property1AdditionalInterest);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setBuildingLimit(150000);
        loc1Bldg1.setBppLimit(150000);
        loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
        locOneBuildingList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        BankAccountInfo bankAccountInfo = new BankAccountInfo();
        bankAccountInfo.setAccountNumber("535535");
		

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("LH Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withBankAccountInfo(bankAccountInfo)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        LHNubmer=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();

        driver.quit();
    }
    @Test
	public void verifyLHChargesAttIssuance() throws Exception {
		generatePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.fullAccountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		BCCommonCharges charge = new BCCommonCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Issuance);
    }
    @Test(dependsOnMethods = {"verifyLHChargesAttIssuance"})
    public void changeCoverageOnBuildingOne() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.changeBOPBuildingCoverage(1, 80000.00, 80000.00);
        new GuidewireHelpers(driver).logout();
    }
    @Test(dependsOnMethods = { "changeCoverageOnBuildingOne" })	
	public void verifyCreditNotTriggerBill() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), LHNubmer);
        //verify the shortage invoice
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		BCCommonCharges charge = new BCCommonCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		
		Assert.assertTrue(invoice.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, null, null, null),
				"Didn't find the Shortage invoice.");
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments document = new BCCommonDocuments(driver);
		Assert.assertFalse(document.verifyDocument("Additional Interest Regular Bill", null, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null), 
				"Didn't find the document "+"\"Additional Interest Regular Bill\""+" triggered by the shortage invoice which is expected.");
		
    }   
}
