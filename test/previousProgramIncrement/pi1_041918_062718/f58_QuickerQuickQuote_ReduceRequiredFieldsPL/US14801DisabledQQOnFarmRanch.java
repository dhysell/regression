package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement: US14801: Disable QQ on F&R policies
 * @RequirementsLink <a href=
 *                   "https://rally1.rallydev.com/#/203558458764d/detail/userstory/212672267280">
 *                   US14801</a>
 * @Description: Validate users are not allowed to use quote Farm& Ranch
 *               policies
 * @DATE May 3, 2018
 */
public class US14801DisabledQQOnFarmRanch extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myQQPolObj;
	private String val1 = "To proceed with Farm & Ranch, please Convert to Full App. Otherwise, please review your answers to the eligibility questions";

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

		myQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("FRQQ", "US14801")
				.withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.QuickQuote);

		new Login(driver).loginAndSearchSubmission(myQQPolObj.agentInfo.getAgentUserName(),
				myQQPolObj.agentInfo.getAgentPassword(), myQQPolObj.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireEligibility();
		GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.chooseEligibility(SquireEligibility.FarmAndRanch);
		eligibilityPage.clickNext();
		Assert.assertTrue(
				new GuidewireHelpers(driver).errorMessagesExist()
						&& new GuidewireHelpers(driver).containsErrorMessage(val1),
				"Expected validation " + new GuidewireHelpers(driver).getFirstErrorMessage() + " is not displayed.");

		// A blocking page validation appears on Squire Eligibility that informs
		// the user they must convert to full app or change eligibility answers
		// Your answers indicate a Farm & Ranch policy is needed. To proceed
		// with Farm & Ranch, please Convert to Full App. Otherwise, please
		// review your answers to the eligibility questions.
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
		Assert.assertTrue(risk.getValidationMessagesText().contains(val1),
				"Expected validation " + val1 + " is not displayed.");
		risk.clickClearButton();

		// If the user changes the eligibility answers to be NOT F&R, they are
		// able to continue on QQ
		eligibilityPage.chooseEligibility(SquireEligibility.Country);
		risk.performRiskAnalysisAndQuoteQQ(myQQPolObj);		
	}


	@Test
	public void testCreateSquireFAWithFarmRanch() throws Exception {
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

		Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		myQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("FRQQ", "US14801")
				.withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.FullApp);
	}
}
