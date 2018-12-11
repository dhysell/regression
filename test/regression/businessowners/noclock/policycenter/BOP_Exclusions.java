package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy Exclusions.
 * @Description Coverages tested:
 * Exclude Bio-Chem Terrorist w/cap BP 05 26
 * Abuse Or Molestation Exclusion BP 04 39
 * Communicable Disease Exclusion BP 14 86
 * Comprehensive Business Liability Exclusion BP 04 01
 * Employment - Related Practices Exclusion BP 04 17
 * Exclusion - Damage To Work Performed By Subcontractors on Your Behalf BP 14 19
 * Exclusion - Personal And Advertising Injury BP 04 37
 * Exclusion - Silica or Silica - Related Dust BP 05 17
 * Exclusion - Unmanned Aircraft BP 15 11
 * Exclusion - Year 2000 Computer - Related And Other Electronic Problems BP 10 05
 * Fungi Or Bacteria Exclusion BP 05 77
 * Limitation of Coverage to Designated Premises or Project BP 04 12
 * @DATE Mar 26, 2018
 */
public class BOP_Exclusions extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withProductType(ProductLineType.Businessowners)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsCompanyName("MMM BOP")
				.withLineSelection(LineSelection.Businessowners)
				.isDraft()
				.build(GeneratePolicyType.QuickQuote);
	}
	
	/**
	 * @throws Exception 
	 * @Author ecoleman
	 * @Description - Testing Exclusions within Business Owners Line
	 * @DATE Mar 26, 2018
     */
	@Test(enabled=true)
    public void testBusinessOwnersLineExclusions() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderBusinessownersLineIncludedCoverages businessOwnersLineIncludedCoveragePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
		
		
		pcSideMenu.clickSideMenuBusinessownersLine();		
		businessOwnersLineIncludedCoveragePage.clickBusinessownersLine_ExclusionsConditions();
		
		// Is under "OLD STUFF NO LONGER IN USE" but is actually in GW
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Exclude Bio-Chem Terrorist w/cap BP 05 26"), "Exclude Bio-Chem Terrorist w/cap BP 05 26 | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Abuse Or Molestation Exclusion BP 04 39"), "Abuse Or Molestation Exclusion BP 04 39 | Was not required");
		
		// Is Electable in REQ DOC
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Communicable Disease Exclusion BP 14 86"), "Communicable Disease Exclusion BP 14 86 | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Comprehensive Business Liability Exclusion BP 04 01"), "Comprehensive Business Liability Exclusion BP 04 01 | Was not electable");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Employment - Related Practices Exclusion BP 04 17"), "Employment - Related Practices Exclusion BP 04 17 | Was not required");
		
		// Is "Defined by script" in REQ doc, but ELE in GW
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Exclusion - Damage To Work Performed By Subcontractors on Your Behalf BP 14 19"), "Exclusion - Damage To Work Performed By Subcontractors on Your Behalf BP 14 19 | Was not required");

		// Is "Defined by script" in REQ doc, but REQ in GW
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Exclusion - Personal And Advertising Injury BP 04 37"), "Exclusion - Personal And Advertising Injury BP 04 37 | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Exclusion - Silica or Silica - Related Dust BP 05 17"), "Exclusion - Silica or Silica - Related Dust BP 05 17 | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Exclusion - Unmanned Aircraft BP 15 11"), "Exclusion - Unmanned Aircraft BP 15 11 | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Exclusion - Year 2000 Computer - Related And Other Electronic Problems BP 10 05"), "Exclusion - Year 2000 Computer - Related And Other Electronic Problems BP 10 05 | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Fungi Or Bacteria Exclusion BP 05 77"), "Fungi Or Bacteria Exclusion BP 05 77 | Was not required");
		
		// Is Electable in REQ DOC		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Limitation of Coverage to Designated Premises or Project BP 04 12"), "Limitation of Coverage to Designated Premises or Project BP 04 12 | Was not required");

		softAssert.assertAll();
	}
}
