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
import repository.pc.workorders.generic.GenericWorkorder;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy Pre-Qualification Question Set.
 * @Description Questions tested:
 * Does applicant handle or sell any products related to the aircraft or space industry?
 * Does applicant handle or sell ammunition, firearms, explosives or fireworks?
 * Have any operations, past or present, involved storing, treating, discharging, applying, disposing or transporting of hazardous materials or chemicals?
 * Are recreations facilities (for example arcades, golf courses, ski lifts, stables, marinas, swimming beaches or other similar activities) present?
 * Are there any boats, docks or floats owned, hired or leased to or operated by the applicant?
 * Does applicant sell or distribute products outside of the United States or Canada?
 * Has applicant or any members and officers been convicted of any felony?
 * Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years?
 * Has applicant had any losses in the last 3 years?
 * Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years?
 * Are products of others sold or re-packaged under applicant
 * s products sold under the label of others?
 * Has the applicant discontinued or recalled any products or operations?
 * Is equipment loaned or rented to others?
 * Are employees leased to others?
 * Does applicant sponsor any sporting or social events?
 * Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises?
 * Does the applicant have other operations or locations not included on this policy?
 * Does applicant do any installation, repair, service work or demonstrations off the premises?
 * Does applicant need cyber insurance coverage of employment practices liability?
 * Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years?
 * Has applicant had any losses in the last 3 years?
 * Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years?
 * Are products of others sold or re-packaged under applicant
 * s products sold under the label of others?
 * Has the applicant discontinued or recalled any products or operations?
 * Is equipment loaned or rented to others?
 * Are employees leased to others?
 * Does applicant sponsor any sporting or social events?
 * Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises?
 * Does the applicant have other operations or locations not included on this policy?
 * Does applicant do any installation, repair, service work or demonstrations off the premises?
 * Does applicant need cyber insurance coverage of employment practices liability?
 * @DATE Mar 27, 2018
 */
public class BOP_PreQualificationQuestions extends BaseTest {
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
	 * @Description - Testing Pre-Qualification Question Set within Business Owners Line
	 * @DATE Mar 27, 2018
     */
	@Test(enabled=true)
    public void testBusinessOwnersPreQualificationQuestions() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		GenericWorkorder pcGenericWorkorderPage = new GenericWorkorder(driver);
		
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant handle or sell any products related to the aircraft or space industry?"), "Does applicant handle or sell any products related to the aircraft or space industry? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant handle or sell ammunition, firearms, explosives or fireworks?"), "Does applicant handle or sell ammunition, firearms, explosives or fireworks? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Have any operations, past or present, involved storing, treating, discharging, applying, disposing or transporting of hazardous materials or chemicals?"), "Have any operations, past or present, involved storing, treating, discharging, applying, disposing or transporting of hazardous materials or chemicals? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Are recreations facilities (for example arcades, golf courses, ski lifts, stables, marinas, swimming beaches or other similar activities) present?"), "Are recreations facilities (for example arcades, golf courses, ski lifts, stables, marinas, swimming beaches or other similar activities) present? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Are there any boats, docks or floats owned, hired or leased to or operated by the applicant?"), "Are there any boats, docks or floats owned, hired or leased to or operated by the applicant? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant sell or distribute products outside of the United States or Canada?"), "Does applicant sell or distribute products outside of the United States or Canada? | Was not required");
		
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Has applicant or any members and officers been convicted of any felony?"), "Has applicant or any members and officers been convicted of any felony? | Was not required");
		// TODO: Find out how to click radio button and re-add
		//softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Please give details"), "Please give details | Was not required");
		
		
		// Assert that the FA section isn't visible while in QQ
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years?"), "Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Has applicant had any losses in the last 3 years?"), "Has applicant had any losses in the last 3 years? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years?"), "Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Are products of others sold or re-packaged under applicant"), "Are products of others sold or re-packaged under applicant's label? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("s products sold under the label of others?"), "Are any of applicant's products sold under the label of others? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Has the applicant discontinued or recalled any products or operations?"), "Has the applicant discontinued or recalled any products or operations? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Is equipment loaned or rented to others?"), "Is equipment loaned or rented to others? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Are employees leased to others?"), "Are employees leased to others? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant sponsor any sporting or social events?"), "Does applicant sponsor any sporting or social events? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises?"), "Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does the applicant have other operations or locations not included on this policy?"), "Does the applicant have other operations or locations not included on this policy? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant do any installation, repair, service work or demonstrations off the premises?"), "Does applicant do any installation, repair, service work or demonstrations off the premises? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant need cyber insurance coverage of employment practices liability?"), "Does applicant need cyber insurance coverage of employment practices liability? | Was not required");
		
		
		// FA Section
		pcGenericWorkorderPage.clickGenericWorkorderFullApp();
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant handle or sell any products related to the aircraft or space industry?"), "Does applicant handle or sell any products related to the aircraft or space industry? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant handle or sell ammunition, firearms, explosives or fireworks?"), "Does applicant handle or sell ammunition, firearms, explosives or fireworks? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Have any operations, past or present, involved storing, treating, discharging, applying, disposing or transporting of hazardous materials or chemicals?"), "Have any operations, past or present, involved storing, treating, discharging, applying, disposing or transporting of hazardous materials or chemicals? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Are recreations facilities (for example arcades, golf courses, ski lifts, stables, marinas, swimming beaches or other similar activities) present?"), "Are recreations facilities (for example arcades, golf courses, ski lifts, stables, marinas, swimming beaches or other similar activities) present? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Are there any boats, docks or floats owned, hired or leased to or operated by the applicant?"), "Are there any boats, docks or floats owned, hired or leased to or operated by the applicant? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant sell or distribute products outside of the United States or Canada?"), "Does applicant sell or distribute products outside of the United States or Canada? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Has applicant or any members and officers been convicted of any felony?"), "Has applicant or any members and officers been convicted of any felony? | Was not required");

		
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years?"), "Has any policy or coverage been declined, canceled, or non-renewed by the carrier during the prior 3 years? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Has applicant had any losses in the last 3 years?"), "Has applicant had any losses in the last 3 years? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years?"), "Has applicant had or is undergoing a foreclosure, repossession, bankruptcy judgement or lien during the past 3 years? | Was not required");
		
		// Partial match used since it contains an apostrophe
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Are products of others sold or re-packaged under applicant"), "Are products of others sold or re-packaged under applicant's label? | Was not required");
		
		// Partial match used since it contains an apostrophe
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("s products sold under the label of others?"), "Are any of applicant's products sold under the label of others? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Has the applicant discontinued or recalled any products or operations?"), "Has the applicant discontinued or recalled any products or operations? | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Is equipment loaned or rented to others?"), "Is equipment loaned or rented to others? | Was not required");
		// TODO: Find out how to click radio button and re-add
		//softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Please describe:"), "Please describe: | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Are employees leased to others?"), "Are employees leased to others? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant sponsor any sporting or social events?"), "Does applicant sponsor any sporting or social events? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises?"), "Does applicant subcontract any work to others apart from routine maintenance of owned or controlled premises? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does the applicant have other operations or locations not included on this policy?"), "Does the applicant have other operations or locations not included on this policy? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant do any installation, repair, service work or demonstrations off the premises?"), "Does applicant do any installation, repair, service work or demonstrations off the premises? | Was not required");
		
		// TODO: Broken, investigate
		//softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant have a professional exposure? For example, legal, accounting, advertising, draw plans designs"), "Does applicant have a professional exposure? For example, legal, accounting, advertising, draw plans designs or specifications, engineering, inspection services, medical, dental, health or therapeutic service, skin enhancement, beauty services, optometry, hearing aid services, body piercing, pharmacy, veterinary or funeral directors. | Was not required");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant need cyber insurance coverage of employment practices liability?"), "Does applicant need cyber insurance coverage of employment practices liability? | Was not required");		
		
		softAssert.assertAll();
	}
}