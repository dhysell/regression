//Steve Broderick

//This tests initiates a phone Change in PC and checks AB.
//9-10-2015 This test now checks to ensure the phone number is displayed correctly in ContactManager

package regression.r2.noclock.contactmanager.changes;


import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChangeReason;
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
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuSearchPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class PhoneChangePC extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyBoundObj = null;
	private AddressInfo insPrimaryAddress = new AddressInfo();
	private String newBusinessPhone = "2082394368x465";
	private String newBusinessPhoneInUI = "208-239-4368 x465";
	private String businessPhoneFormatted = "208-239-4369 x987";
	private String processorLogin = "";
	private AbUsers numberingLogin;

	@Test(description = "creates the Policy")
	public void issuePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		insPrimaryAddress.setPhoneBusiness("2082394369x987");
		insPrimaryAddress.setPhoneWork("2082394369x1234");

		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicyBoundObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Dwight", "Shrewt")
				.withPolOrgType(OrganizationType.Joint_Venture)
				.withInsPrimaryAddress(insPrimaryAddress)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.ACH_EFT)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "issuePolicy" }, description = "Changes Phone Number via Policy Change.")
	public void policyChangePhone() throws Exception {

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
		this.numberingLogin = AbUserHelper.getRandomDeptUser("Numbering");
		lp.login(numberingLogin.getUserName(), numberingLogin.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
		search.searchByAccountNumberClickName(myPolicyBoundObj.accountNumber, myPolicyBoundObj.pniContact.getFirstName());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		String businessPhone = contactPage.getBusinessPhone();
		if (!businessPhoneFormatted.equals(businessPhone)) {
			Assert.fail("The Business Phone did not format correctly in AB");
		}
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
		Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);

        Login logMeIn = new Login(driver);
		logMeIn.login(myPolicyBoundObj.agentInfo.getAgentUserName(), myPolicyBoundObj.agentInfo.getAgentPassword());

        TopMenuSearchPC getToSearch = new TopMenuSearchPC(driver);
		getToSearch.clickAccounts();

		SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(myPolicyBoundObj.accountNumber);

        PolicyMenu changePolicy = new PolicyMenu(driver);
		changePolicy.clickMenuActions();
		changePolicy.clickChangePolicy();

        StartPolicyChange addressChange = new StartPolicyChange(driver);
		addressChange.setDescription("Phone Number Change");
		addressChange.setEffectiveDate(pcDate);
        addressChange.clickPolicyChangeNext();

        GenericWorkorderPolicyInfo getPolicyInfo = new GenericWorkorderPolicyInfo(driver);
		getPolicyInfo.changePolicyInfoBusinessPhone(newBusinessPhone);

        GenericWorkorderQuote quoteMe = new GenericWorkorderQuote(driver);
		quoteMe.clickQuote();

        GenericWorkorder bindMe = new GenericWorkorder(driver);
		bindMe.clickGenericWorkorderSubmitOptionsSubmit();

        UWActivityPC sendActivity = new UWActivityPC(driver);
		sendActivity.setText("Issue the Phone Change");
		sendActivity.setChangeReason(ChangeReason.AddressMailing);
		sendActivity.clickSendRequest();

        GenericWorkorderComplete getBackToPolicy = new GenericWorkorderComplete(driver);
		getBackToPolicy.clickAccountNumber();

		// get processor assigned then login as them.
        AccountSummaryPC myAccountSummary = new AccountSummaryPC(driver);
		String processor = myAccountSummary.getStringAssignedTo("Submitted policy change");
		processorLogin = StringsUtils.getUserNameFromFirstLastName(processor);
	}

	@Test(dependsOnMethods = { "policyChangePhone" }, description = "Issues Phone Number Change as processor.")
	public void issuePhoneChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        // TopInfo logMeOut = new TopInfo(driver);
		// logMeOut.clickTopInfoLogout();

        Login logMeIn = new Login(driver);
		logMeIn.login(processorLogin, "gw");

        TopMenuSearchPC getToSearch = new TopMenuSearchPC(driver);
		getToSearch.clickAccounts();

		SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(myPolicyBoundObj.accountNumber);

        // PolicySummary summaryPage = PolicyFactory.getPolicySummaryPage();
		// summaryPage.getAddress();

        SideMenuPC clickPolicyInfo = new SideMenuPC(driver);
		clickPolicyInfo.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo verifyPhone = new GenericWorkorderPolicyInfo(driver);
		String businessPhoneOnPolicy = verifyPhone.getPolicyInfoBusinessPhone();

		if (!businessPhoneOnPolicy.equals(newBusinessPhoneInUI)) {
			throw new Exception("The Business phone was changed in AB.  This change is not in PC");
		}

	}

	@Test(dependsOnMethods = { "issuePhoneChange" }, description = "Checks AB for Phone Change")
    public void checkPhoneChangeAB() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
		logMeIn.login(numberingLogin.getUserName(), numberingLogin.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
		menu.clickSearchTab();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
		advancedSearch.searchByAccountNumberClickName(myPolicyBoundObj.accountNumber, myPolicyBoundObj.pniContact.getFirstName());

        ContactDetailsBasicsAB newContactPage = new ContactDetailsBasicsAB(driver);
//		String contactPageTitle = newContactPage.getContactPageTitle();
		String businessPhone = newContactPage.getBusinessPhone();
		if (!businessPhone.equals(newBusinessPhoneInUI)) {
			Assert.fail("The Business Phone Number was changed in PC and not found in AB.");
		}

	}

}
