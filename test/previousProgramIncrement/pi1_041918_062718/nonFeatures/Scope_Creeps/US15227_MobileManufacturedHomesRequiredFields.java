package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/223196847604">US15227</a>
 * @Description 
 As an agent/PA/UW I want to add a mobile home or manufactured home and not be required to fill in model and serial #. These fields are still available to fill in but are not required.

Steps to get there:

    Start new submission and add buildings as mobile and manufactured, all types (Residence, Dwelling, Vacation, Coverage A & Coverage E).
    Be required to fill in year, construction type, make, size, measurement, number of stories, basement, garage, foundation, shed question and porch question.

Acceptance criteria:

    Ensure that Serial Number and Model are available but no longer required.
    Ensure works on new submission, policy change and all editable rewrite jobs (not rewrite remainder of term). 
 * @DATE May 31, 2018
 */

public class US15227_MobileManufacturedHomesRequiredFields extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	@Test(enabled = true)
	public void verifyRequiredFieldsMobileManufacturedNewSubmission() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.isDraft()
					.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
//		GenericWorkorderSquirePropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyDetail();
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		pcPropertyDetailPage.clickAdd();
		pcPropertyDetailDetails.setPropertyType(PropertyTypePL.ResidencePremises);
		pcPropertyDetailDetails.clickPropertyConstructionTab();
		pcPropertyConstructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		
//		Acceptance criteria: Make sure that Serial #, Make, Model are all present, but not required
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Serial Number"), "Serial # was not present!");
        softAssert.assertFalse(new GuidewireHelpers(driver).isRequiredField("Serial Number"), "Serial Number was required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Make"), "Make was not present!");
        softAssert.assertFalse(new GuidewireHelpers(driver).isRequiredField("Make"), "Make was required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Model"), "Model was not present!");
        softAssert.assertFalse(new GuidewireHelpers(driver).isRequiredField("Model"), "Model was required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Size"), "Size was not present!"); 
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Size"), "Size was not required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Basement"), "Basement was not present!"); 
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Basement"), "Basement was not required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Garage"), "Garage was not present!"); 
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Garage"), "Garage was not required!");
		
		softAssert.assertTrue(new GuidewireHelpers(driver).isAvailable("Foundation"), "Foundation was not present!"); 
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Foundation"), "Foundation was not required!");
		softAssert.assertAll();
	}




}




















