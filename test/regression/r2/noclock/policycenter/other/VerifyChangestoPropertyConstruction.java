
package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.Wiring;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.globaldatarepo.entities.Agents;

/**
 * @Author rvanama
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 */
public class VerifyChangestoPropertyConstruction extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }


    @Test
    public void multipleProperties() throws Exception {
        // Creating quote
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.FullApp);
        Agents agent = myPolicyObjPL.agentInfo;

        // Login and open the same account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        // Adding multiple properties without and with owner
        addSection1Properties();

        covC();

        // Validating Coverage page for owner name for all the properties
        //updateAndValidatePropertyCoverages(myPolicyObjPL.insFirstName);

    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private void updateAndValidatePropertyCoverages(String insFirstName) throws Exception {

        boolean testFailed = false;
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        int rowCount = propertyCoverages.getPropertyTableRowCount();
        ArrayList<String> buildingNumbers = new ArrayList<String>();
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String ownerName = propertyCoverages.getPropertyTableSpecificColumnByRow(currentRow, "Owner").trim();
            String buildingNumber = propertyCoverages.getPropertyTableSpecificColumnByRow(currentRow, "Bldg. #");
            if (ownerName.isEmpty()) {
                buildingNumbers.add(buildingNumber);
            } else
                continue;

        }

        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        for (String buidling : buildingNumbers) {
            propertyDetails.clickViewOrEditBuildingButton(Integer.parseInt(buidling));
            propertyDetails.AddExistingOwner();
            propertyDetails.clickOk();
        }

        // Validate owner name
        sideMenu.clickSideMenuSquirePropertyCoverages();
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String ownerName = propertyCoverages.getPropertyTableSpecificColumnByRow(currentRow, "Owner");
            if (ownerName.contains(insFirstName))
                System.out.println("Expected Owner Name :  " + ownerName + " is displayed.");
            else {
                testFailed = true;
                errorMessage = errorMessage + "Expected Owner Name :  " + ownerName + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * @throws GuidewireNavigationException
     */
    private void addSection1Properties() throws GuidewireNavigationException {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        // Adding property details without owner
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetails.clickAdd();
        propertyDetails.setPropertyType(PropertyTypePL.DwellingUnderConstruction);

        propertyDetails.AddExistingOwner();
        propertyDetails.setDwellingVacantRadio(false);
        propertyDetails.setUnits(NumberOfUnits.OneUnit);
        propertyDetails.setWoodFireplaceRadio(false);
        propertyDetails.setSwimmingPoolRadio(false);
        propertyDetails.setWaterLeakageRadio(false);
        propertyDetails.setExoticPetsRadio(false);
        //propertyDetails.setDwellingVacantRadio(false);
        propertyDetails.clickPropertyConstructionTab();

        enterConstructionProptectionPageDetails();

    }

    private void enterConstructionProptectionPageDetails() {
        // Enter construction

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(Integer.valueOf(DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "yyyy")));
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setBathClass(KitchenBathClass.Basic);
        constructionPage.setSquareFootage(1700);
        constructionPage.setStories(NumberOfStories.One);
        constructionPage.setBasementFinished("90");
        constructionPage.setGarage(Garage.AttachedGarage);
        constructionPage.setLargeShed(false);
        constructionPage.setCoveredPorches(false);
        constructionPage.setFoundationType(FoundationType.FullBasement);
        constructionPage.setRoofType(RoofType.WoodShingles);
        constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
        constructionPage.setPlumbing(Plumbing.Copper);
        constructionPage.setWiring(Wiring.Copper);
        constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
        constructionPage.setAmps(100);
        constructionPage.setKitchenClass(KitchenBathClass.Basic);
        constructionPage.clickOK();
		
		/*constructionPage.clickProtectionDetails();
		GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.setSprinklerSystemType(SprinklerSystemType.Full);
		protectionPage.setFireExtinguishers(true);
		protectionPage.setSmokeAlarm(true);
		protectionPage.setNonSmoker(true);
		protectionPage.setDeadBoltLocks(true);
		protectionPage.setDefensibleSpace(true);
		protectionPage.clickOK();*/

    }

    public void covC() throws Exception {
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coveragePage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coveragePage.clickSpecificBuilding(1, 1);
        String covCLimit = coveragePage.getCoverageCLimit();
        if (!(covCLimit.contains("- $"))) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjPL.accountNumber + "Please ensure the Coverage C Limit is displayed for Coverage A Properties.");
        }
    }

    /**

     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Multipleprops")
                .build(policyType);
        new GuidewireHelpers(driver).logout();

        return myPolicyObjPL;
    }

}



