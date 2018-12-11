package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/229452649636">US15400 Remove mint oil, honey & bees from FPP worksheet</a>
 * @Description As an agent, PA, CSR, SA, coder or underwriter I want to see the FPP worksheet no longer include choices for mint oil, honey or bees. These are excluded under FPP and need to be on section IV or a standard fire policy.
 * Will need to query policies that began in PC and have this coverage to get adjusted.
 * <p>
 * Steps to get there:
 * <p>
 * Start a new submission on country/F&R that has FPP coverage.
 * Add FPP and section IV to policy.
 * Also have a standard fire commodity policy.
 * <p>
 * Acceptance criteria:
 * <p>
 * Ensure that on the FPP drop down lists there is no longer a choice for mint oil, honey or bees.
 * Ensure that these items are available on section IV and on standard fire commodity policy for the mint oil and honey. NOTE: Bees would not be on commodity policy.
 * @DATE July 26, 2018
 */

public class US15400_MintHoneyBeeFPP extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;


	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		PersonalPropertyScheduledItem myItem = new PersonalPropertyScheduledItem();
		myItem.setParentPersonalPropertyType(PersonalPropertyType.Jewelry);
		myItem.setType(PersonalPropertyScheduledItemType.Ring);
		myItem.setDescription("This is a ring");
		myItem.setAppraisalDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
		myItem.setPhotoUploadDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
		myItem.setLimit(25000);

		PersonalProperty myPersonalProperty = new PersonalProperty();
		myPersonalProperty.setType(PersonalPropertyType.Jewelry);
		myPersonalProperty.setLimit(25000);
		myPersonalProperty.setDeductible(PersonalPropertyDeductible.Ded5Perc);
		myPersonalProperty.getScheduledItems().add(myItem);
		myInlandMarine.personalProperty_PL_IM.add(myPersonalProperty);
		myInlandMarine.inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

		Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
		mySquire.inlandMarine = myInlandMarine;

		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withInsFirstLastName("ScopeCreeps", "NonFeature")
				.isDraft()
				.build(GeneratePolicyType.FullApp);
	}

	@Test(enabled = true)
	public void CheckFPPAndIMForBeesMintOil() throws Exception {


		generatePolicy();



		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);


		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
		GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new GenericWorkorderSquireFarmPersonalProperty(driver);
		GenericWorkorderStandardIMCoverageSelection pcIMCoverageSelectionPage = new GenericWorkorderStandardIMCoverageSelection(driver);
		GenericWorkorderSquireInlandMarine_PersonalProperty pcIMPersonalPropertyPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);


		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
		pcFarmPersonalPropertyPage.checkCoverageD(true);
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Miscellaneous);
		pcFarmPersonalPropertyPage.clickAdd(FarmPersonalPropertyTypes.Miscellaneous);

		ArrayList<String> itemsInList = pcFarmPersonalPropertyPage.getItemsByType(FarmPersonalPropertyTypes.Miscellaneous);

		Boolean mint = itemsInList.contains("Honey/Mint Oil");
		Boolean bees = itemsInList.contains("Portable Buildings & Bee Houses");

		// Acceptance criteria: These options are removed
		softAssert.assertFalse(mint, "Honey/Mint Oil - was showing, it should be gone!");
		softAssert.assertFalse(bees, "Portable Buildings & Bee Houses - was showing, it should be gone!");

		pcFarmPersonalPropertyPage.checkCoverageD(false);

		pcSideMenu.clickSideMenuIMCoveragePartSelection();
		pcIMCoverageSelectionPage.checkCoverage(true, "Personal Property");
		pcSideMenu.clickSideMenuIMPersonalEquipment();
		pcIMPersonalPropertyPage.clickAdd();

		List<String> ppTypes = pcIMPersonalPropertyPage.getTypeValues();

		Boolean beeContainers = ppTypes.contains("Bee Containers");
		softAssert.assertTrue(beeContainers, "Bee Containers - was not showing, it should be there!");		

		softAssert.assertAll();
	}
}




















