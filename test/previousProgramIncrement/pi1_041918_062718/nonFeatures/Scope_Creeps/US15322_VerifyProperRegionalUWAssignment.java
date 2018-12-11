package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import repository.gw.enums.OrganizationType;
import repository.gw.generate.custom.Squire;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/226947761424">US15322</a>
 * @Description 
As a user I want all new business applications to be routed to the following persons/areas:

    Eastern Idaho Region go to Cindy Taft 
    North Idaho & Magic Valley Regions round robin between Melinda Sanchez & Tricia Hauser
    Treasure Valley Region round robin between Jessica Hurt & Kyndra Garrick

DEV needs to point the new submission at New Business Underwriters.

Requirements: Activity Patterns  (Refer row 81)

Steps to get there:

    Have new submission started by agent/PA.
    After agent submits application (with no block quote issues) the application should go to the proper person in UW

Acceptance criteria:

    Ensure every area goes to the correct UW user
    Ensure that after a block quote is handled by supervisor or Sr UW and sent back to the agent/PA that the submission goes to the correct UW when agent/PA submits (after the block quote is resolved)
 * @DATE June 12, 2018
 */

public class US15322_VerifyProperRegionalUWAssignment extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	@Test(enabled = true)
	public void VerifyActivitySentToRegion1UW() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
			
		Agents agent = AgentsHelper.getRandomAgentInRegion("Region 1");// CTAFT
		Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.FarmAndRanch);

		myPolicyObject = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(repository.gw.enums.ProductLineType.Squire)
					.withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
					.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
					.withAgent(agent)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.build(repository.gw.enums.GeneratePolicyType.PolicySubmitted);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);		
		
		// Acceptance criteria: Region 1 should be assigned to CTAFT 
		Assert.assertTrue(pcAccountSummaryPage.getAssignedToUW("Submitted Full Application").underwriterUserName.toLowerCase().contains("taft"), "Activity was not assigned to CTAFT!");
	}
	
	@Test(enabled = true)
	public void VerifyActivitySentToRegion2UW() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Agents agent = AgentsHelper.getRandomAgentInRegion("Region 2");// MSANCHEZ / THAUSER
		Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.FarmAndRanch);
		myPolicyObject = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(repository.gw.enums.ProductLineType.Squire)
					.withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
					.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
					.withAgent(agent)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.build(repository.gw.enums.GeneratePolicyType.PolicySubmitted);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);		
		
		// Acceptance criteria: Region 2 should be assigned to MSANCHEZ OR THAUSER 
		boolean criteria1 = pcAccountSummaryPage.getAssignedToUW("Submitted Full Application").underwriterUserName.toLowerCase().contains("sanchez");
		boolean criteria2 = pcAccountSummaryPage.getAssignedToUW("Submitted Full Application").underwriterUserName.toLowerCase().contains("hauser");
		Assert.assertTrue(criteria1 || criteria2, "Activity was not set to either MSANCHEZ OR THAUSER!");
	}
	
	@Test(enabled = true)
	public void VerifyActivitySentToRegion3UW() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Agents agent = AgentsHelper.getRandomAgentInRegion("Region 3");// KGARRICK / JHURT
		Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.FarmAndRanch);
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
					.withProductType(repository.gw.enums.ProductLineType.Squire)
					.withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(OrganizationType.Individual)
					.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
					.withAgent(agent)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.build(repository.gw.enums.GeneratePolicyType.PolicySubmitted);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);		
		
		// Acceptance criteria: Region 3 should be assigned to KGARRICK OR JHURT
		boolean criteria1 = pcAccountSummaryPage.getAssignedToUW("Submitted Full Application").underwriterUserName.toLowerCase().contains("garrick");
		boolean criteria2 = pcAccountSummaryPage.getAssignedToUW("Submitted Full Application").underwriterUserName.toLowerCase().contains("hurt");
		Assert.assertTrue(criteria1 || criteria2, "Activity was not set to either KGARRICK OR JHURT!");
	}
}


