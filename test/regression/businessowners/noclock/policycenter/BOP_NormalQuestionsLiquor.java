package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy Normal Questions.
 * @Description Questions tested:
 * Does applicant provide written employee rules/guidelines regarding sales to minors, sales to intoxicated persons, arrangements for transportation of intoxicated persons from the premises, etc.?
 * Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply.
 * Does applicant sponsor or provide 2 for 1 drinks or free alcoholic drinks?
 * Western Community is not able to offer liquor legal coverage when the applicant does not have written employee rules regarding the sale to minors or intoxicated persons. Refer the coverage to brokerage.
 * Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage.
 * Western Community is not able to offer liquor legal coverage when the applicant sponsors 2 for 1 drinks or free alcoholic drinks. Please refer coverage to brokerage.
 * BOP Liquor Liability Coverage BP 04 89
 * @DATE Mar 29, 2018
 */
public class BOP_NormalQuestionsLiquor extends BaseTest {
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
	 * @Description - Testing Liquor Liability Coverage Question Set within Business Owners Line
	 * @DATE Mar 27, 2018
	 * @throws Exception
	 */
	@Test(enabled=true)
	public void testBusinessOwnersLiquorQuestions() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderBusinessownersLineIncludedCoverages businessOwnersLineIncludedCoveragePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
		GenericWorkorderBusinessownersLineAdditionalCoverages pcBusinessOwnersLineAdditionalCoveragePage = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
		GenericWorkorderSupplemental pcWorkorderSupplementalPage = new GenericWorkorderSupplemental(driver);
		GenericWorkorderQualification pcWorkorderQualificationPage = new GenericWorkorderQualification(driver);
		
		businessOwnersLineIncludedCoveragePage.clickBusinessownersLine_AdditionalCoverages();		
		pcBusinessOwnersLineAdditionalCoveragePage.addLiquorLiability(true);		
		pcSideMenu.clickSideMenuSupplemental();
		
		//Assert.assertTrue(new GuidewireHelpers(driver).isRequired(""), " |  Was not on page");
		//softAssert.assertTrue(new GuidewireHelpers(driver).isRequired(""), " |  Was not required");
		// If these 3 questions aren't here, something is wrong, so hard assert failure is needed
		Assert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant provide written employee rules/guidelines regarding sales to minors, sales to intoxicated persons, arrangements for transportation of intoxicated persons from the premises, etc.?"), "Does applicant provide written employee rules/guidelines regarding sales to minors, sales to intoxicated persons, arrangements for transportation of intoxicated persons from the premises, etc.? |  Was not on page");
		Assert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply."), "Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply. |  Was not on page");
		Assert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant sponsor or provide 2 for 1 drinks or free alcoholic drinks?"), "Does applicant sponsor or provide 2 for 1 drinks or free alcoholic drinks? |  Was not on page");

		pcWorkorderSupplementalPage.clickGenericWorkorderFullApp();
		pcWorkorderQualificationPage.setFullAppAllTo(false);
		pcSideMenu.clickSideMenuSupplemental();
		
		pcWorkorderSupplementalPage.handleSupplementalQuestions();
		pcWorkorderSupplementalPage.setWrittenRules(false);
		pcWorkorderSupplementalPage.setDancersAthleticEvents(false);
		pcWorkorderSupplementalPage.setSponsor2For1Drinks(false);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western Community is not able to offer liquor legal coverage when the applicant does not have written employee rules regarding the sale to minors or intoxicated persons. Refer the coverage to brokerage."), "Western Community is not able to offer liquor legal coverage when the applicant does not have written employee rules regarding the sale to minors or intoxicated persons. Refer the coverage to brokerage. |  Was not on page");
		
		pcWorkorderSupplementalPage.setWrittenRules(true);
		pcWorkorderSupplementalPage.setDancersAthleticEvents(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply."), "At least 1 question associated with \\\"Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply.\\\" is required | Was not on page");
		
		pcWorkorderSupplementalPage.setExoticDancersCheckBox(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage."), "Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setExoticDancersCheckBox(false);
		pcWorkorderSupplementalPage.setAthleticEventsCheckBox(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage."), "Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setExoticDancersCheckBox(false);
		pcWorkorderSupplementalPage.setAthleticEventsCheckBox(false);
		pcWorkorderSupplementalPage.setFireamsOnPremisesCheckBox(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage."), "Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setExoticDancersCheckBox(false);
		pcWorkorderSupplementalPage.setAthleticEventsCheckBox(false);
		pcWorkorderSupplementalPage.setFireamsOnPremisesCheckBox(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage."), "Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setExoticDancersCheckBox(true);
		pcWorkorderSupplementalPage.setAthleticEventsCheckBox(true);
		pcWorkorderSupplementalPage.setFireamsOnPremisesCheckBox(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage."), "Western community is not able to offer liquor liability coverage when the applicant has exotic dancers, athletic contests or firearms on the premises. Please refer the coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setDancersAthleticEvents(false);
		pcWorkorderSupplementalPage.setSponsor2For1Drinks(true);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Western Community is not able to offer liquor legal coverage when the applicant sponsors 2 for 1 drinks or free alcoholic drinks. Please refer coverage to brokerage."), "Western Community is not able to offer liquor legal coverage when the applicant sponsors 2 for 1 drinks or free alcoholic drinks. Please refer coverage to brokerage. | Was not on page");
		
		pcWorkorderSupplementalPage.setSponsor2For1Drinks(false);
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertFalse(new GuidewireHelpers(driver).isRequired("BOP Liquor Liability Coverage BP 04 89"), "BOP Liquor Liability Coverage BP 04 89 | Was on page when you shouldn't be on that page");
	}
}