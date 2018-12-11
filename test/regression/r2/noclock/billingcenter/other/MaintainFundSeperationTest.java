package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.summary.BCAccountSummary;
import repository.bc.search.BCSearchAccounts;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description US5474 	1) Only allow policy level billing with cash separation.
*						2) Disable editing of fields on screen for account vs policy level and funds separation.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/18%20-%20UI%20-%20Account%20Screens/18-02%20Account%20Summary%20Screen.docx">See requirements 18-02-18 and 19</a>
* @DATE April 1st, 2016
*/
public class MaintainFundSeperationTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private String lienholderNumber;
	private ARUsers arUser = new ARUsers();
	private String insuredBillingInfo="Policy/Group";	
	private BCAccountSummary summary;
	
	private void verifyInsuredAndLNBillingInfo(String accountNumber){		
		summary.clickEdit();
		try {
			summary.clickBillingInfoInvoiceByAccount();
			Assert.fail(accountNumber+"'s 'Invoice By' is editable which is unexpected.");
		}catch(Exception e){
			getQALogger().info(accountNumber+"'s 'Invoice By' is uneditable which is expected.");
		}
		try{
			summary.clickBillingInfoSeparateIncomingFundsByPolicyGroup();
			Assert.fail(accountNumber+"'s 'Separate Incoming Funds By' is editable which is unexpected.");
		}catch(Exception e){
			getQALogger().info(accountNumber+ "'s 'Separate Incoming Funds By'is uneditable which is expected.");
		}
	}	
	@Test
	public void generate() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PaymentAllocationPlanAndPolicyLevelDelinqPlanTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();		
	}
	@Test(dependsOnMethods = { "generate" })	
	public void verifyFundSeperation() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		summary = new BCAccountSummary(driver);
		//verify insured Billing Info
		if(!summary.getBillingInfoInvoiceBy().equals(insuredBillingInfo) || !summary.getBillingInfoSeparateIncomingFundsBy().equals(insuredBillingInfo)){
			Assert.fail("The insured has incorrect Billing Info.");
		}
		verifyInsuredAndLNBillingInfo(myPolicyObj.accountNumber);
		//verify Lienholder Billing Info
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienholderNumber);
		verifyInsuredAndLNBillingInfo(lienholderNumber);
	}
}
