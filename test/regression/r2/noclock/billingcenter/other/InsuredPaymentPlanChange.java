package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

 /**
* @Author sreddy
* @Requirement : DE4683   **HOT FIX** Insured Payment Plan Change Changes LH Invoices
* @Description : 1. Issue a policy with lien charges and Quarterly Payment plan
*                2. On BC change Insured payment plan to Annual
*                3. Verify that the Lien Account Invoice did not change              
* @DATE Jan 24, 2017
*/

 public class InsuredPaymentPlanChange extends BaseTest {
	private WebDriver driver;
 	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
 	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
 	private ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <AdditionalInterest>();
 	private GeneratePolicy myPolicyObj = null;
 	private ARUsers arUser = new ARUsers();
 	private String lienAccountNumber = null;
 	private String lienholderLoanNumber = "CHG77722";

 	@Test
 	public void generatePolicy() throws Exception { 		
 		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
 		loc1Bldg1.setYearBuilt(2010);
 		loc1Bldg1.setClassClassification("storage");
 
 		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
 		loc1Bldg2.setYearBuilt(2010);
 		loc1Bldg2.setClassClassification("storage");
 		
 		
 		AddressInfo addIntTest = new AddressInfo();
 		addIntTest.setLine1("PO Box 711");
 		addIntTest.setCity("Pocatello");
 		addIntTest.setState(State.Idaho);
 		addIntTest.setZip("83204");//-0711
 		
 		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
 		loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Always);
 		loc1Bld1AddInterest.setAddress(addIntTest);
 		loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
 		loc1Bld1AddInterest.setLoanContractNumber(lienholderLoanNumber);
 		loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
 		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
 		
 		this.locOneBuildingList.add(loc1Bldg1);
 		this.locOneBuildingList.add(loc1Bldg2);
 		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

 		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
 		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
 				.withInsPersonOrCompany(ContactSubType.Company)
 				.withInsCompanyName("DE4683 xx1")
 				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
 				.withPolOrgType(OrganizationType.Partnership)
 				.withPolicyLocations(locationsList)
 				.withPaymentPlanType(PaymentPlanType.Quarterly)
 				.withDownPaymentType(PaymentType.Cash)
 				.build(GeneratePolicyType.PolicyIssued);
 		this.lienAccountNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
 	}
 	
 	
	@Test(dependsOnMethods = { "generatePolicy" })
 	public void changeInsuredPaymentPlan() throws Exception {
			
 		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
 		Config cf = new Config(ApplicationOrCenter.BillingCenter);
 		driver = buildDriver(cf);
 		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienAccountNumber);

        BCPolicyMenu polMenu = new BCPolicyMenu(driver);
        BCAccountMenu accMenu = new BCAccountMenu(driver);
        AccountInvoices accInvoicePage = new AccountInvoices(driver);

		accMenu.clickAccountMenuInvoices();
		int invoiceCntBefore = accInvoicePage.getInvoiceTableRowCount();
		BCSearchPolicies policySearchBC = new BCSearchPolicies(driver);
		policySearchBC.searchPolicyByAccountNumber(this.myPolicyObj.accountNumber);

		polMenu.clickPaymentSchedule();
		PolicyPaymentSchedule paymntSchedule = new PolicyPaymentSchedule(driver);
		paymntSchedule.clickEditSchedule();
		paymntSchedule.selectNewPaymentPlan(PaymentPlanType.Annual);
		paymntSchedule.clickExecute();


		BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
		accountSearchBC.searchAccountByAccountNumber(this.lienAccountNumber);		
		accMenu.clickAccountMenuInvoices();
		int invoiceCntAfter = accInvoicePage.getInvoiceTableRowCount();
				
		if(invoiceCntBefore == invoiceCntAfter ){
			System.out.println("As Expected LH Invoice did not change when Insured plan changed from  " + PaymentPlanType.Quarterly + " to " + PaymentPlanType.Annual);
		}
	
 		new GuidewireHelpers(driver).logout();
 	}
 	
}