package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentScheduleItemsToProcess;
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
public class PaymentScheduleFromQuarterlyToMonthlyOnAllitems extends BaseTest {
	private WebDriver driver;
 	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
 	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
 	private ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <AdditionalInterest>();
 	private GeneratePolicy myPolicyObj = null;
 	private ARUsers arUser = new ARUsers();
 	private String lienholderLoanNumber = "DE4415";

 	@Test //(dependsOnMethods = { "changePaymentPlanQuarterlyToMonthly" })
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
 				.withInsCompanyName("ZZ xx1")
 				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
 				.withPolOrgType(OrganizationType.Partnership)
 				.withPolicyLocations(locationsList)
 				.withPaymentPlanType(PaymentPlanType.Quarterly)
 				.withDownPaymentType(PaymentType.Cash)
 				.build(GeneratePolicyType.PolicyIssued);
 	}
 	
 
	@Test  (dependsOnMethods = { "generatePolicy" })
 	public void changePaymentPlanQuarterlyToMonthly() throws Exception {
			
 		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
 		Config cf = new Config(ApplicationOrCenter.BillingCenter);
 		driver = buildDriver(cf);
 		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber); //this.myPolicyObj.accountNumber

        BCPolicySummary polSummary = new BCPolicySummary(driver);
 		polSummary.clickEdit();
 		//polSummary.clickPaymentInstrumentNew();
 		//polSummary.clickPolicySummaryPaymentInstrumentNew();
 		polSummary.setNewPaymentInstrument();
 		polSummary.clickUpdate();
        BCPolicyMenu polMenu = new BCPolicyMenu(driver);
		polMenu.clickPaymentSchedule();

		PolicyPaymentSchedule paymntSchedule = new PolicyPaymentSchedule(driver);
		paymntSchedule.clickEditSchedule();
		paymntSchedule.setRadioItemsToProcess(PaymentScheduleItemsToProcess.All_Items);
		paymntSchedule.selectNewPaymentPlan(PaymentPlanType.Monthly);
		paymntSchedule.clickExecute();

		System.out.println("As Expected No Invalid Server Response");
 		new GuidewireHelpers(driver).logout();
 	}
 	 
}