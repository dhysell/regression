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
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocations;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy coverages.
 * @Description Coverages tested:
 * Auto Increase Blg. Limit %
 * Money and Securities
 * Outdoor Signs
 * Water Backup and Sump Overflow BP 04 53
 * Additional Insured - Lessor Of Leased Equipment BP 04 16
 * Additional Insured - Managers Or Lessor Of Premises BP 04 02
 * Additional Insured - Co-owner Of Insured Premises BP 04 11
 * Additional Insured - Mortgagee Assignee Or Receiver BP 04 09
 * Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased BP 04 10
 * @DATE Mar 6, 2018
 */
public class BOP_CoveragesLocations extends BaseTest {
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
	 * @Author ecoleman
	 * @Description - Testing Coverages within Locations
	 * @DATE Mar 21, 2018
	 * @throws Exception
	 */
	@Test(enabled=true)
	public void testBOPLocationCoverages() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderLocations pcLocationsList = new GenericWorkorderLocations(driver);
		PolicyLocationAdditionalInsured policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();		
		
		pcSideMenu.clickSideMenuLocations();
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Auto Increase Blg. Limit %"), "Auto Increase Blg. Limit % | Was not required");
		
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.LessorOfLeasedEquipment);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		pcLocationsList.addAdditionalInsuredLocation(true, policyLocationAdditionalInsured);
		
		// TODO: Look into page not loading properly after addAddtionalInsuredLocation (Why the delays are required)
		policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.ManagersOrLessorsOrPremises);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		pcLocationsList.addAdditionalInsuredLocation(true, policyLocationAdditionalInsured);
		
		policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.CoOwnerOfInsuredPremises);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		pcLocationsList.addAdditionalInsuredLocation(true, policyLocationAdditionalInsured);
		
		policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		pcLocationsList.addAdditionalInsuredLocation(true, policyLocationAdditionalInsured);
		
		policyLocationAdditionalInsured = new PolicyLocationAdditionalInsured();
		policyLocationAdditionalInsured.setAiRole(AdditionalInsuredRole.OwnersOrOtherInterests);
		policyLocationAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		pcLocationsList.addAdditionalInsuredLocation(true, policyLocationAdditionalInsured);
		
		pcLocationsList.clickEdit();
		pcLocationsList.clickLocationsAdditionalCoverages();
		
		// Blank Assert to copy
		// softAssert.assertTrue(new GuidewireHelpers(driver).isRequired(""), " | Was not required when additional insured of that type was added");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Money and Securities"), "Money and Securities | Was not electable");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Outdoor Signs"), "Outdoor Signs | Was not electable");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Water Backup and Sump Overflow BP 04 53"), "Water Backup and Sump Overflow BP 04 53 | Was not electable");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Additional Insured - Lessor Of Leased Equipment BP 04 16"), "Additional Insured - Lessor Of Leased Equipment BP 04 16 | Was not required when additional insured of that type was added");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Additional Insured - Managers Or Lessor Of Premises BP 04 02"), "Additional Insured - Managers Or Lessor Of Premises BP 04 02 | Was not required when additional insured of that type was added");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Additional Insured - Co-owner Of Insured Premises BP 04 11"), "Additional Insured - Co-owner Of Insured Premises BP 04 11 | Was not required when additional insured of that type was added");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Additional Insured - Mortgagee Assignee Or Receiver BP 04 09"), "Additional Insured - Mortgagee Assignee Or Receiver BP 04 09 | Was not required when additional insured of that type was added");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased BP 04 10"), "Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased BP 04 10 | Was not required when additional insured of that type was added");
		softAssert.assertAll();
	}
	
}
