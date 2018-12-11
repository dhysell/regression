package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
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
 * @Description testing Business Owner Policy coverages.
 * @Description Coverages tested:
 * Damage to Premises Rented To You
 * Premises Medical Expense
 * Electronic Data
 * Employee Dishonesty-Optional Coverage
 * Hired Auto BP 04 04
 * Insurance To Value BP 04 83
 * Liquor Liability BP 04 88
 * Non-owned Auto Liability BP 04 04
 * Beauty Salons and Barber Shop Professional Liability IDBP 31 2002
 * Additional Insured - Engineers, Architects, Or Surveyors BP 04 13
 * Additional Insured - Grantor Of Franchise BP 14 05
 * Additional Insured - State Or Political Subdivisions - Permits Relating To Premises BP 04 07
 * @DATE Mar 6, 2018
 */
public class BOP_CoveragesBusinessOwnersLine extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withProductType(ProductLineType.Businessowners)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsCompanyName("MMMBOP")
				.withLineSelection(LineSelection.Businessowners)
				.isDraft()
				.build(GeneratePolicyType.QuickQuote);
	}
	
	/**
	 * @Author ecoleman
	 * @Description - Testing Coverages within Business Owners Line
	 * @DATE Mar 21, 2018
	 * @throws Exception
	 */
	@Test(enabled=true)
	public void testBusinessOwnersLineCoverages() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderBusinessownersLineIncludedCoverages pcBusinessOwnersLineIncludedCoveragePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        PolicyBusinessownersLineAdditionalInsured policyBusinessOwnersLineAdditionalInsured = new PolicyBusinessownersLineAdditionalInsured();
        
        
        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        
		pcSideMenu.clickSideMenuBusinessownersLine();
        softAssert.assertTrue(gwHelpers.isRequired("Damage to Premises Rented To You"), "Damage to Premises Rented To You | Was not required when additional insured of that type was added");
        softAssert.assertTrue(gwHelpers.isRequired("Premises Medical Expense"), "Premises Medical Expense | Was not required when additional insured of that type was added");     
        
        policyBusinessOwnersLineAdditionalInsured.setAiRole(AdditionalInsuredRole.GrantorOfFranchise);
        policyBusinessOwnersLineAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
        pcBusinessOwnersLineIncludedCoveragePage.addAdditionalInsureds(true, policyBusinessOwnersLineAdditionalInsured);
        
        policyBusinessOwnersLineAdditionalInsured = new PolicyBusinessownersLineAdditionalInsured();
        policyBusinessOwnersLineAdditionalInsured.setAiRole(AdditionalInsuredRole.StateOrPoliticalSubdivisions);
        policyBusinessOwnersLineAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
        pcBusinessOwnersLineIncludedCoveragePage.addAdditionalInsureds(true, policyBusinessOwnersLineAdditionalInsured);
        
        pcBusinessOwnersLineIncludedCoveragePage.clickBusinessownersLine_AdditionalCoverages();
        
        softAssert.assertTrue(gwHelpers.isElectable("Electronic Data"), "Electronic Data | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Employee Dishonesty-Optional Coverage"), "Employee Dishonesty-Optional Coverage | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Hired Auto BP 04 04"), "Hired Auto BP 04 04 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Insurance To Value BP 04 83"), "Insurance To Value BP 04 83 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Liquor Liability BP 04 88"), "Liquor Liability BP 04 88 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Non-owned Auto Liability BP 04 04"), "Non-owned Auto Liability BP 04 04 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Beauty Salons and Barber Shop Professional Liability IDBP 31 2002"), "Beauty Salons and Barber Shop Professional Liability IDBP 31 2002 | Was not electable");        
        softAssert.assertTrue(gwHelpers.isElectable("Additional Insured - Engineers, Architects, Or Surveyors BP 04 13"), "Additional Insured - Engineers, Architects, Or Surveyors BP 04 13 | Was not electable");
        
        // TODO: vv These 2 are ELECTABLE in REQ document
        softAssert.assertTrue(gwHelpers.isRequired("Additional Insured - Grantor Of Franchise BP 14 05"), " | Additional Insured - Grantor Of Franchise BP 14 05 Was not required when additional insured of that type was added");
        softAssert.assertTrue(gwHelpers.isRequired("Additional Insured - State Or Political Subdivisions - Permits Relating To Premises BP 04 07"), " | Additional Insured - State Or Political Subdivisions - Permits Relating To Premises BP 04 07 Was not required when additional insured of that type was added");
        //       ^^ These 2 are ELECTABLE in REQ document
        
        // TODO: Find out how to select Yes on radio button for "Is Applicant in the business of selling or charging for liquor?" -> Then activate below assert
        //softAssert.assertTrue(isElectable("Liquor Liability Coverage BP 04 89"), "Liquor Liability Coverage BP 04 89 | Was not electable");
        softAssert.assertAll();
	}	
}