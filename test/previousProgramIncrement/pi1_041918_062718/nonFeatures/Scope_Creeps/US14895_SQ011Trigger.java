package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/213539821420">US14895</a>
 * @Description As an underwriter I want the validation for CBRs (sq011) to trigger at issuance also, so that a CBR isn't missed at issuance if it is edited after submission, allowing our insureds to have accurate score and rate information as a result.
 * Requirements: Policy Level Forms Product Model Spreadsheet
 * Steps to get there:
 * Create a new personal lines policy, Submit Policy
 * On issuance job, click "edit insurance report" and leave page (click onto another page)
 * Quote and Issue the policy
 * Acceptance criteria:
 * Ensure that SQ011 triggers when an underwriter quotes the issuance of a policy.
 * This should apply to City Squire, Country Squire, Farm and Ranch, and Standard Fire policies.
 * @DATE Apr 23, 2018
 */
public class US14895_SQ011Trigger extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;


	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);	
		// Acceptance criteria: Here we are randomizing policy type to test all required (City, Country, Farm&Ranch, Fire)
        //TODO : As Submit option only available on Farm and Ranch So we  hardcoding the  switch case 1 to execute always.
		int RandomPolicy =1; //NumberUtils.generateRandomNumberInt(1,2);
//		For Testing purposes
//		int RandomPolicy = 1;
		System.out.println("RandomPolicy = " + RandomPolicy + " ; 1 = property&liability, 2 = standard fire");
		switch (RandomPolicy) {
		case 1:
			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
			locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
			locationsList.add(new PolicyLocation(locOnePropertyList));
			SquireLiability liabilitySection = new SquireLiability();
			SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
			myPropertyAndLiability.locationList = locationsList;
			myPropertyAndLiability.liabilitySection = liabilitySection;
			Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
			mySquire.propertyAndLiability = myPropertyAndLiability;

            myPolicyObject = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(OrganizationType.Individual)
					.withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
					.build(GeneratePolicyType.PolicySubmitted);
			break;
		case 2:			
			locationsList = new ArrayList<PolicyLocation>();
			locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
			locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
			locationsList.add(new PolicyLocation(locOnePropertyList));
			StandardFireAndLiability fireLiabilitySection = new StandardFireAndLiability();			
			fireLiabilitySection.setLocationList(locationsList);

            myPolicyObject = new GeneratePolicy.Builder(driver)
				.withStandardFire(fireLiabilitySection)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withDownPaymentType(PaymentType.Cash)
				.withPolOrgType(OrganizationType.Individual)
				.withInsFirstLastName("ScopeCreeps", "US14895")
				.build(GeneratePolicyType.PolicySubmitted);
			break;
		}
		driver.quit();
	}

		
	@Test
	public void verifySR011Validation() throws Exception {
		generatePolicy();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// TODO: Watch stability of this login, had issues when using "loginAndSearchPolicyByAccountNumber"
		new Login(driver).loginAndSearchPolicyByPolicyNumber("msanchez", "gw", new GuidewireHelpers(driver).getPolicyNumber(myPolicyObject));

        PolicySummary pcPolicySummary = new PolicySummary(driver);
        GenericWorkorderInsuranceScore pcInsuranceScorePage = new GenericWorkorderInsuranceScore(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
		
		
		pcPolicySummary.clickActivity("Submitted Full Application");
		pcSideMenu.clickSideMenuPLInsuranceScore();
		pcInsuranceScorePage.clickEditInsuranceReport();
		pcInsuranceScorePage.selectCreditReportIndividual("ScopeCreeps");
		pcInsuranceScorePage.clickGenericWorkorderQuote();
		
		// Acceptance criteria: SQ011 being triggered is asserted here
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Policy: Credit Report is required to quote policy. (SQ011)"), "SQ011 VALIDATION MESSAGE NOT ON SCREEN!");
	}
}

