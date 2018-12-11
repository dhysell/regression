package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/221186777432">US15159</a>
 * @Description As an agent, PA or UW I want to be able to add GPS equipment to the Tools category under FPP.
 * This will allow the coverage to have the equipment breakdown coverage.
 * Currently GPS is under Machinery. It needs to be moved.
 * <p>
 * Steps to get there:
 * 1. Start new submission as country or F&R policy.
 * 2. Have FPP on policy and add GPS to the Tools category. (make sure GPS is no longer an option under Machinery)
 * 3. Submit policy
 * <p>
 * Acceptance criteria:
 * Ensure that user is able to add GPS to Tools on FPP and not under Machinery
 * @DATE May 17, 2018
 */
public class US15159_GPSMove extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withSquireEligibility(SquireEligibility.FarmAndRanch)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withDownPaymentType(PaymentType.Cash)
					.withPolOrgType(OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.isDraft()
					.build(GeneratePolicyType.FullApp);	
        driver.quit();
	}

	@Test(enabled = true)
	public void EnsureGPSOnToolsNotMachinery() throws Exception {
		generatePolicy();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new GenericWorkorderSquireFarmPersonalProperty(driver);
		SoftAssert softAssert = new SoftAssert();
		
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
		pcFarmPersonalPropertyPage.checkCoverageD(true);
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Tools);
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Machinery);
		
		// Here we will select an item type to see if it contains GPS. If not, it will contain <none>
		pcFarmPersonalPropertyPage.selectItemTypeInPropertyType(FarmPersonalPropertyTypes.Machinery, FPPFarmPersonalPropertySubTypes.GPS);
		pcFarmPersonalPropertyPage.addItem(FPPFarmPersonalPropertySubTypes.GPS, 1, 1000, "GPS #1");
		String toolsItemTypeText = pcFarmPersonalPropertyPage.getItemTypeByPropertyTypeAndRowNumber(FarmPersonalPropertyTypes.Tools, 1);
		String machineryItemTypeText = pcFarmPersonalPropertyPage.getItemTypeByPropertyTypeAndRowNumber(FarmPersonalPropertyTypes.Machinery, 1);
		
		// Acceptance criteria says that GPS should be removed from Machinery and added to Tools
		softAssert.assertTrue(toolsItemTypeText.contains("GPS"), "GPS was not found under tools, it should be here");
		softAssert.assertFalse(machineryItemTypeText.contains("GPS"), "GPS was found under machinery, it should not be here");
		
		pcFarmPersonalPropertyPage.removeItem(FarmPersonalPropertyTypes.Machinery, 1);
		pcFarmPersonalPropertyPage.unselectCoverages(FarmPersonalPropertyTypes.Machinery);
		
		pcFarmPersonalPropertyPage.selectCoverageType(FPPCoverageTypes.BlanketInclude);
		pcFarmPersonalPropertyPage.selectDeductible(FPPDeductible.Ded_500);
		pcSideMenu.clickSideMenuRiskAnalysis();
		
		pcWorkOrder.clickGenericWorkorderQuote();
		softAssert.assertAll();
	}




}




















