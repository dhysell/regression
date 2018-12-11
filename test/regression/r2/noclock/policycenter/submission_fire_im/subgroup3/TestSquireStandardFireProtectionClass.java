package regression.r2.noclock.policycenter.submission_fire_im.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

/**
 * @Author skandibanda
 * @Description :  US7887 - [Part II] PL - Protection Class
 * @DATE May 31, 2016
 */
public class TestSquireStandardFireProtectionClass extends BaseTest {
    private GeneratePolicy mySQPolicyObj;
    private GeneratePolicy myStandardFirePolicyObj;

    private WebDriver driver;

    @Test(dependsOnMethods = {"createSquirePolicy"})
    public void checkSquireProtectionClass() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(mySQPolicyObj.agentInfo.getAgentUserName(), mySQPolicyObj.agentInfo.getAgentPassword(), mySQPolicyObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        //adding non-standardize location
        AddressInfo newAddress = new AddressInfo(ProtectionClassCode.Prot8);


        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickNewLocation();
        propertyLocations.selectLocationAddress("New");
        propertyLocations.addLocationInfoFA(newAddress, 2, 2);
        propertyLocations.clickStandardizeAddress();
        propertyLocations.clickOK();

        //validating in property details for the standardize and non standardize address
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        boolean testFailed = false;
        String errorMessage = "";
        if (!propertyDetail.getAutoPublicProtectionClassCode().equals(propertyDetail.getManualPublicProtectionClassCode())) {
            testFailed = true;
            errorMessage = "Expected Manual Proptection Class Code : " + propertyDetail.getAutoPublicProtectionClassCode() + " is not displayed.";
        }
        propertyDetail.sendArbitraryKeys(Keys.TAB);

        propertyDetail.clickOk();

        propertyDetail.highLightPropertyLocationByNumber(2);

        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        propertyDetail.fillOutPropertyDetails_FA(property1);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
        constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);

        propertyDetail.clickPropertyInformationDetailsTab();
        //User have to set longitude and longitude through "openMap" for non-standardized address which added to location
        //propertyDetail.setPropertyLogitude(longitude);
        //propertyDetail.setPropertyLatitude(latitude);
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot5);
        protectionPage.clickOK();

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }

    @Test()
    public void createSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);


        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.inlandMarine = myInlandMarine;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Test", "Prot")
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"createStandardFirePolicy"})
    public void checkStandardFireProtectionClass() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myStandardFirePolicyObj.agentInfo.getAgentUserName(), myStandardFirePolicyObj.agentInfo.getAgentPassword(), myStandardFirePolicyObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        //adding non-standardize location
        AddressInfo newAddress = new AddressInfo();
        newAddress.setLine1("123 Main St");
        newAddress.setCity("Pocatello");
        newAddress.setZip("83201");
        newAddress.setCounty(CountyIdaho.Bannock);

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickNewLocation();
        propertyLocations.selectLocationAddress("New");
        propertyLocations.addLocationInfoFA(newAddress, 2, 2);
        propertyLocations.clickStandardizeAddress();
        try {
            propertyLocations.clickLocationInformationOverride();
        } catch (Exception e) {
        }
        propertyLocations.clickOK();

        //validating in property details for the standardize and non standardize address
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        boolean testFailed = false;
        String errorMessage = "";
        if (!propertyDetail.getAutoPublicProtectionClassCode().equals(propertyDetail.getManualPublicProtectionClassCode())) {
            testFailed = true;
            errorMessage = "Expected Manual Proptection Class Code : " + propertyDetail.getAutoPublicProtectionClassCode() + " is not displayed.";
        }
        propertyDetail.sendArbitraryKeys(Keys.TAB);

        propertyDetail.clickOk();

        propertyDetail.highLightPropertyLocationByNumber(2);

        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        propertyDetail.fillOutPropertyDetails_FA(property1);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsFA(property1);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        propertyDetail.clickPropertyInformationDetailsTab();
        //User have to set longitude and longitude through "openMap" for non-standardized address which added to location
        //propertyDetail.setPropertyLogitude(longitude);
        //propertyDetail.setPropertyLatitude(longitude);
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot5);
        protectionPage.clickOK();

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }

    @Test()
    public void createStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(PropertyTypePL.DwellingPremises);
        property.setConstructionType(ConstructionTypePL.Frame);
        property.setFoundationType(FoundationType.FullBasement);
        locOnePropertyList.add(property);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);

        myStandardFirePolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Modif")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

    }
}
