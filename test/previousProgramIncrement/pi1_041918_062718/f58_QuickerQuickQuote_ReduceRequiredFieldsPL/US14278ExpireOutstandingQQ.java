package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.desktop.DesktopMySubmissions;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.search.SearchSubmissionsPC;
import repository.pc.topmenu.TopMenuSearchPC;

import java.util.ArrayList;

/**
* @Author nvadlamudi
* @Requirement: US14278: STRETCH:  Expire outstanding QQ at 180 days
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764d/detail/userstory/202017575812">US14278: STRETCH:  Expire outstanding QQ at 180 days</a>
* @Description: Validate QQ and FA status after 179, 180 days 
* !Acceptance: 	Ensure that quotes that are 180 days old and older expire from an Agents/PAs Submission queue.  
*				Ensure that quotes that are fewer than 180 days old do not expire.
*				Ensure that a Full App does not get auto-expired.
* @DATE May 3, 2018
*/
@Test(groups = { "ClockMove" })
public class US14278ExpireOutstandingQQ extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myQQPolObj, myFAPolObj;

	@Test
	public void testCreateSquireQQ() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myQQPolObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("OSQQ", "US14278")
				.withPolOrgType(OrganizationType.Individual)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.QuickQuote);
	}

	@Test
	public void testCreateSquireFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
		locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
		propertyLocation.setPlNumAcres(10);
		propertyLocation.setPlNumResidence(2);
		locationsList.add(propertyLocation);

        myFAPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("SFire", "US14278")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.FullApp);
	}
	
	@Test(dependsOnMethods =  {"testCreateSquireQQ", "testCreateSquireFA"})
	public void testMoveClockAndCheckExpiredStatus() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
		
		//Ensure that quotes that are fewer than 180 days old do not expire.
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 179);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Quote_Expire);
		new Login(driver).loginAndSearchAccountByAccountNumber(myQQPolObj.agentInfo.getAgentUserName(), myQQPolObj.agentInfo.getAgentPassword(),
				myQQPolObj.accountNumber);

		String QQTrasNo = acctSummary.getAccountSummaryPendingTransactionDetailByText(ProductLineType.Squire.getValue(), "Transaction #");
		Assert.assertTrue(acctSummary.getAccountSummaryPendingTransactionDetailByText(ProductLineType.Squire.getValue(), "Status").contentEquals("Quoted"), "After 179 days, Quick Quote status is not in quoted for the account : '"+myQQPolObj.accountNumber+"'.");
		new GuidewireHelpers(driver).logout();
		
		//Validating for FA
		new Login(driver).loginAndSearchAccountByAccountNumber(myFAPolObj.agentInfo.getAgentUserName(), myFAPolObj.agentInfo.getAgentPassword(),
				myFAPolObj.accountNumber);
		acctSummary = new AccountSummaryPC(driver);
		String FATrasNo = acctSummary.getAccountSummaryPendingTransactionDetailByText("Standard Fire", "Transaction #");		
		Assert.assertTrue(acctSummary.getAccountSummaryPendingTransactionDetailByText("Standard Fire", "Status").contentEquals("Quoted"), "After 179 days, Full App status is not in quoted for the account : '"+myFAPolObj.accountNumber+"'.");
		new GuidewireHelpers(driver).logout();
		
		//Ensure that quotes that are 180 days old and older expire from an Agents/PAs Submission queue
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Quote_Expire);
		new Login(driver).login(myQQPolObj.agentInfo.getAgentUserName(), myQQPolObj.agentInfo.getAgentPassword());
		SearchSubmissionsPC search = new SearchSubmissionsPC(driver);
		search.searchSubmissionWithoutSelecting(myQQPolObj.accountNumber);
		Assert.assertTrue(search.getStatusOfSubmissions().get(0).contentEquals("Not-taken"), "After 180 days, Quick Quote status is not Not-taken for the account : '" + myQQPolObj.accountNumber + "'.");
		TopMenuSearchPC topMenu = new TopMenuSearchPC(driver);
		topMenu.clickDesktopTab();
		DesktopSideMenuPC sidebar = new DesktopSideMenuPC(driver);
        sidebar.clickMySubmissions();        
        DesktopMySubmissions mySub = new DesktopMySubmissions(driver);
        mySub.setDesktopMySubmissionsOptions("Completed in last 30 days");
        Assert.assertTrue(mySub.getSubmissionStatusByTransactionNo(QQTrasNo).contentEquals("Not-taken"), "After 180 days, Quick Quote status is not Not-taken for the account : '"+myQQPolObj.accountNumber+"' in Desktop -> My Submission");
        new GuidewireHelpers(driver).logout();
		
		//Validating for FA after 180 days
        new Login(driver).loginAndSearchAccountByAccountNumber(myFAPolObj.agentInfo.getAgentUserName(), myFAPolObj.agentInfo.getAgentPassword(),
		myFAPolObj.accountNumber);
		acctSummary = new AccountSummaryPC(driver);
		acctSummary.selectWorkOrdersInProgress("All");
		Assert.assertFalse(acctSummary.getAccountSummaryPendingTransactionDetailByText("Standard Fire", "Status").contentEquals("Quoted"), "After 180 days, Full App status is not in Not-Taken for the account : '"+myFAPolObj.accountNumber+"'.");
		topMenu.clickDesktopTab();
		sidebar = new DesktopSideMenuPC(driver);
        sidebar.clickMySubmissions();
        mySub = new DesktopMySubmissions(driver);
        Assert.assertFalse(mySub.getSubmissionStatusByTransactionNo(FATrasNo).contentEquals("Quoted"), "After 180 days, Full App status is not in Not-Taken for the account : '"+myFAPolObj.accountNumber+"' in Desktop -> My Submission");
	}
}
