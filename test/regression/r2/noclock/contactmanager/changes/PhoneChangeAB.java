//Steve Broderick
//This tests initiates a phone Change in AB and checks PC.

package regression.r2.noclock.contactmanager.changes;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.BusinessIncomeOrdinaryPayroll;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class PhoneChangeAB extends BaseTest {
	private WebDriver driver;
	// Instance Data
	private AbUsers abUser;
	private String newBusinessPhone = "8012501289";
	private String businessPhoneToVerify = newBusinessPhone.substring(0, 3) + "-" + newBusinessPhone.substring(3, 6)
			+ "-" + newBusinessPhone.substring(6);
	public GeneratePolicy myPolicyObj;

	@Test
	public void generateIssuedPolicy() throws Exception {

		// customizing the BO Line, including additional insureds
		// PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new
		// PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);

		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuildingAdditionalCoverages addCoverages = new PolicyLocationBuildingAdditionalCoverages();
		addCoverages.setBusinessIncomeOrdinaryPayrollCoverage(true);
		addCoverages.setBusinessIncomeOrdinaryPayrollType(BusinessIncomeOrdinaryPayroll.Days90);
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		building.setAdditionalCoveragesStuff(addCoverages);
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PhoneChangeAB").withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline).withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		System.out.println("The Account Number is " + myPolicyObj.accountNumber);

		// //Issue with No Changes
		// this.myPolicyObj.generateIssuanceNoChanges(this.myPolicyObj);
		// System.out.println(this.myPolicyObj.currentPolicyType.toString());
	}

	@Test(dependsOnMethods = { "generateIssuedPolicy" })
	public void changePhoneAB() throws Exception {
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login lp = new Login(driver);
		lp.login(this.abUser.getUserPassword(), this.abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();

        AdvancedSearchAB bankSearch = new AdvancedSearchAB(driver);
		bankSearch.searchCompanyByName(myPolicyObj.pniContact.getCompanyName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());

        ContactDetailsBasicsAB myContact = new ContactDetailsBasicsAB(driver);
		myContact.clickContactDetailsBasicsEditLink();
		myContact.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB changeAddress = new ContactDetailsAddressesAB(driver);
		changeAddress.setContactDetailsAddressesBusinessPhone(newBusinessPhone);
		changeAddress.clickContactDetailsAddressesUpdate();
	}

	@Test(dependsOnMethods = { "changePhoneAB" })
	public void checkPhoneAB() throws Exception {

		// TopInfo logMeOut =
        // new TopInfo(driver);
		// logMeOut.clickTopInfoLogout();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login lp = new Login(driver);
		lp.login(this.abUser.getUserPassword(), this.abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();

        AdvancedSearchAB compSearch = new AdvancedSearchAB(driver);
		compSearch.searchCompanyByName(myPolicyObj.pniContact.getCompanyName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());

        ContactDetailsBasicsAB myContact = new ContactDetailsBasicsAB(driver);
		myContact.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB changeAddress = new ContactDetailsAddressesAB(driver);
		String businessPhone = changeAddress.getContactDetailsAddressesBusinessPhone();
		if (myPolicyObj.pniContact.getAddress().getPhoneBusiness().equals(businessPhone)) {
			Assert.fail("The business phone number has not changed ");
		}
	}

	@Test(dependsOnMethods = { "checkPhoneAB" })
	public void checkPhonePC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(abUser.getUserName(), abUser.getUserPassword(), myPolicyObj.accountNumber);
        AccountSummaryPC acctSummaryPage = new AccountSummaryPC(driver);
        acctSummaryPage.clickPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());

        SideMenuPC clickPolicyInfo = new SideMenuPC(driver);
		clickPolicyInfo.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo verifyPhone = new GenericWorkorderPolicyInfo(driver);
		String businessPhoneOnPolicy = verifyPhone.getPolicyInfoBusinessPhone();
		System.out.println(newBusinessPhone);
		System.out.println(businessPhoneToVerify);
		if (!businessPhoneOnPolicy.equals(businessPhoneToVerify)) {
			Assert.fail("The Business phone was changed in AB.  This change is not in PC. \r\n The Phone on the policy is " + businessPhoneOnPolicy + ". \r\n The phone that should be on the policy is: "+ businessPhoneToVerify);
		}
	}
}
