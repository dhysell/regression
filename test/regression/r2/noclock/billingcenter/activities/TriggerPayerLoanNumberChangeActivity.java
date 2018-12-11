package regression.r2.noclock.billingcenter.activities;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.common.BCCommonActivities;
import repository.bc.policy.BCPolicyMenu;
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
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

 /**
* @Author sreddy
* @Requirement : DE4654: Payer Loan Number Change Not Triggered
* @Description : Create a policy with Lien charges and Issue
*                Move clock for a month and do a policy change to edit loan number
*                On BC check for activity on policy level
* @DATE Jan 24, 2017
*/
//@QuarantineClass
 public class TriggerPayerLoanNumberChangeActivity extends BaseTest {
	private WebDriver driver;
 	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
 	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
 	private ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <AdditionalInterest>();
 	private GeneratePolicy myPolicyObj = null;
 	private String policyChangeDescription = "Change Loan number";
 	private ARUsers arUser = new ARUsers();
 	private String lienholderName = null;
 	private String lienholderLoanNumberNew = "CHG777";

 	@Test
 	public void generatePolicy() throws Exception { 		
 		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
 		loc1Bldg1.setYearBuilt(2010);
 		loc1Bldg1.setClassClassification("storage");
 		
 		AddressInfo addIntTest = new AddressInfo();
 		addIntTest.setLine1("PO Box 711");
 		addIntTest.setCity("Pocatello");
 		addIntTest.setState(State.Idaho);
 		addIntTest.setZip("83204");//-0711
 		
 		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
 		loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Always);
 		loc1Bld1AddInterest.setAddress(addIntTest);
 		loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
 		loc1Bld1AddInterest.setLoanContractNumber("");
 		loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
 		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
 		
 		this.locOneBuildingList.add(loc1Bldg1);
 		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

 		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
 		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
 				.withInsPersonOrCompany(ContactSubType.Company)
 				.withInsCompanyName("PayerLoanChange Test")
 				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
 				.withPolOrgType(OrganizationType.Partnership)
 				.withPolicyLocations(locationsList)
 				.withPaymentPlanType(PaymentPlanType.Quarterly)
 				.withDownPaymentType(PaymentType.Cash)
 				.build(GeneratePolicyType.PolicyIssued);
 		this.lienholderName = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();
 	}

 	@Test(dependsOnMethods = { "generatePolicy" })
 	public void changeLoanNumber() throws Exception {

 		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
 		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
 		driver = buildDriver(cf);
 		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());

		TopMenuPC topMenu = new TopMenuPC(driver);
 		topMenu.clickSearchTab();

        SearchPoliciesPC searchPoliciesPage = new SearchPoliciesPC(driver);
 		searchPoliciesPage.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
 		policyChange.changeLoanNumber(1, 1, this.lienholderName, this.lienholderLoanNumberNew, this.policyChangeDescription, DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));		
 		new GuidewireHelpers(driver).logout();
 	}

 	@Test(dependsOnMethods = { "changeLoanNumber" })
    public void verifyPolicyLoanPayerChangeActivity() throws Exception {
 		Config cf = new Config(ApplicationOrCenter.BillingCenter);
 		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        BCPolicyMenu menu = new BCPolicyMenu(driver);
		menu.clickBCMenuActivities();

		BCCommonActivities activities = new BCCommonActivities(driver);
		Assert.assertTrue(activities.isActivity("Loan Number / Payer Change On Policy"), "Loan Number / Payer Change Activity Not Triggered");
		
 		new GuidewireHelpers(driver).logout();		
 	}
 }

