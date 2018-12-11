package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
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
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

/**
* @Author nvadlamudi
 * @Requirement :DE7592:***HOT FIX*** Default IRC on Manufactured/Modular home, US15818: **HOTFIX** UW ability to edit IRC
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/229726689868"></a>
* @Description Validate Increased Replacement Cost is displayed for construction type: Modular/Manfactured
 * 				Validating field not editable for Agent and underwriter
 * 			08/02/2018 : Added changes based on US15838
* @DATE Jun 15, 2018
*/
public class DE7592DefaultIRCOnManufacturedModular  extends BaseTest {
	private GeneratePolicy myPolicyObj;
	private WebDriver driver;

	@Test
    public void testCheckingCoverageIRCForModularManufactured() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setConstructionType(ConstructionTypePL.Frame);
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		myPolicyObj = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("IRCMan", "DE7592")
				.withPolOrgType(OrganizationType.Individual)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(),myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.clickPropertyConstructionTab();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.clickOK();		
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        constructionPage.clickGenericWorkorderSaveDraft();
     
        Assert.assertTrue(!coverages.IncreasedReplacementCostExists(), "Account Number: '"+ myPolicyObj.accountNumber +"' - Unexpected Increased Replacement Cost  is Modular/Manufactured Construction Type.");
        new GuidewireHelpers(driver).logout();
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        Assert.assertTrue(!coverages.IncreasedReplacementCostExists(), "UW LoginAccount Number: '"+ myPolicyObj.accountNumber +"' - Unexpected Increased Replacement Cost  is Modular/Manufactured Construction Type.");
       
	}
	
}
