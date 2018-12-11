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
 * @Description testing Business Owner Policy Conditions.
 * @Description Coverages tested:
 * Affiliate and Subsidiary Definition Endorsement IDBP 31 2001
 * Amendment Of Insured Contract Definition BP 05 98
 * Condominium Association Coverage BP 17 01
 * Condominium Commercial Unit-Owners Coverage BP 17 02
 * Idaho Changes BP 01 83
 * Liability Manuscript Endorsement IDBP 31 2003
 * Mobile Equipment Modification Endorsement IDBP 31 2004
 * Pollutants Definition Endorsement IDBP 31 0002
 * Property Manuscript Endorsement IDBP 31 1003
 * Premium Rounding and Waiver Endorsement IDCW 31 0001
 * Advisory Notice To Policyholders IL P 001
 * @DATE Mar 27, 2018
 */
public class BOP_Conditions extends BaseTest {

	private GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

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
	 * @Description - Testing Conditions within Business Owners Line
	 * @DATE Mar 27, 2018
     */
	@Test(enabled=true)
    public void testBusinessOwnersLineConditions() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderBusinessownersLineIncludedCoverages businessOwnersLineIncludedCoveragePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
		
		
		pcSideMenu.clickSideMenuBusinessownersLine();		
		businessOwnersLineIncludedCoveragePage.clickBusinessownersLine_ExclusionsConditions();
		
		GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
		softAssert.assertTrue(gwHelpers.isRequired("Affiliate and Subsidiary Definition Endorsement IDBP 31 2001"), "Affiliate and Subsidiary Definition Endorsement IDBP 31 2001 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Amendment Of Insured Contract Definition BP 05 98"), "Amendment Of Insured Contract Definition BP 05 98 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Condominium Association Coverage BP 17 01"), "Condominium Association Coverage BP 17 01 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Condominium Commercial Unit-Owners Coverage BP 17 02"), "Condominium Commercial Unit-Owners Coverage BP 17 02 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Idaho Changes BP 01 83"), "Idaho Changes BP 01 83 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Liability Manuscript Endorsement IDBP 31 2003"), "Liability Manuscript Endorsement IDBP 31 2003 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Mobile Equipment Modification Endorsement IDBP 31 2004"), "Mobile Equipment Modification Endorsement IDBP 31 2004 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Pollutants Definition Endorsement IDBP 31 0002"), "Pollutants Definition Endorsement IDBP 31 0002 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Property Manuscript Endorsement IDBP 31 1003"), "Property Manuscript Endorsement IDBP 31 1003 | Was not required");
		softAssert.assertTrue(gwHelpers.isRequired("Premium Rounding and Waiver Endorsement IDCW 31 0001"), "Premium Rounding and Waiver Endorsement IDCW 31 0001 | Was not required");

		// TODO: Look into XPATH creation and add helper function to deal with the apostrophe
		//softAssert.assertTrue(isRequired("U.S. Treasury Department\'s Office Of Foreign Assets Control (\'OFAC\') Advisory Notice To Policyholders IL P 001"), "U.S. Treasury Department's Office Of Foreign Assets Control ('OFAC') Advisory Notice To Policyholders IL P 001 | Was not required");
		// Until then, using partial match
		softAssert.assertTrue(gwHelpers.isRequired("Advisory Notice To Policyholders IL P 001"), "U.S. Treasury Department's Office Of Foreign Assets Control ('OFAC') Advisory Notice To Policyholders IL P 001 | Was not required");
		
		softAssert.assertAll();
	}
}