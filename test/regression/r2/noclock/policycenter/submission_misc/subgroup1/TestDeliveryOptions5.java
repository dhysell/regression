package regression.r2.noclock.policycenter.submission_misc.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderDeliveryOption;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author skandibanda
 * @Requirement : US12141: Additional Interest Delivery option validation
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/128071644704d/detail/userstory/143971432508">Rally Story</a>
 * @DATE Oct 03, 2017
 */
public class TestDeliveryOptions5 extends BaseTest {
    private GeneratePolicy mySquireIMObj;

    private WebDriver driver;

    //Squire Section IV
    @Test
    public void testSquireInlandMarine_FullApp_RecreationalEquipment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);


        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "500", PAComprehensive_CollisionDeductible.Fifty50, "Testing Purpose"));

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.RecreationalEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        mySquireIMObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Guy", "RecEquip")
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchJob(mySquireIMObj.agentInfo.getAgentUserName(), mySquireIMObj.agentInfo.getAgentPassword(), mySquireIMObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuIMCoveragePartSelection();
        sideMenu.clickSideMenuIMRecreationVehicle();

        //Changing Recreational Equipment Detectable
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);

        //Adding lien Holder
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchAddressBookByCompanyName(mySquireIMObj.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, loc2Bldg1AddInterest.getAddress().getState(), null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAdditionalInterestsLoanNumber("1234");

        DeliveryOptionType optionType = DeliveryOptionType.DBA;
        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
        deliveryOptions.addDeliveryOption(optionType);
        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
        String companyName = additionalInterests.getAdditionalInterestsName();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        recreationalEquipment.clickOK();
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(2);
        additionalInterests.addExistingAdditionalInterest(companyName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAdditionalInterestsLoanNumber("1234");

        optionType = DeliveryOptionType.Attention;
        deliveryOptions.addDeliveryOption(optionType);
        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        if (!new ErrorHandlingHelpers(driver).getErrorMessage().contains("Please add/remove the following Delivery Options [DBA]"))
            Assert.fail("When we select different delivery option  'Please add/remove the following Delivery Options [Attention]'  validation message should be displayed.");
    }

    public void validateVehicleDeliveryOption(GeneratePolicy pol) throws Exception {
        new Login(driver).loginAndSearchJob(pol.agentInfo.getAgentUserName(), pol.agentInfo.getAgentPassword(), pol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu.clickSideMenuPAVehicles();
        Vehicle vehicle = new Vehicle();
        // Add Vehicle
        GenericWorkorderVehicles_Details vehicleDetails = new GenericWorkorderVehicles_Details(driver);
        vehicleDetails.createVehicleManually();
        vehicleDetails.selectVehicleType(VehicleTypePL.PassengerPickup);
        vehicleDetails.setVIN(vehicle.getVin());
        vehicleDetails.setModelYear(vehicle.getModelYear());
        vehicleDetails.setMake(vehicle.getMake());
        vehicleDetails.setModel(vehicle.getModel());
        vehicleDetails.selectCommunity(CommutingMiles.Pleasure1To2Miles);
        vehicleDetails.selectGaragedAt(pol.squire.squirePA.getVehicleList().get(0).getGaragedAt());
        vehicleDetails.setNoDriverAssigned(true);
        vehicleDetails.clickOK();
        vehicleDetails.clickGenericWorkorderSaveDraft();
        sideMenu.clickSideMenuPAVehicles();

        vehicleDetails.editVehicleByVehicleNumber(1);

        //Adding lien Holder
        AddressInfo bankAddress = new AddressInfo();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchAddressBookByCompanyName(pol.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAdditionalInterestsLoanNumber("1234");
        additionalInterests.sendArbitraryKeys(Keys.TAB);
        DeliveryOptionType optionType = DeliveryOptionType.DBA;
        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
        deliveryOptions.addDeliveryOption(optionType);
        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
        String companyName = additionalInterests.getAdditionalInterestsName();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        vehicleDetails.clickOK();

        vehicleDetails.editVehicleByVehicleNumber(2);
        additionalInterests.addExistingAdditionalInterest(companyName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAdditionalInterestsLoanNumber("1234");

        optionType = DeliveryOptionType.Attention;
        deliveryOptions.addDeliveryOption(optionType);
        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        if (!new ErrorHandlingHelpers(driver).getErrorMessage().contains("Please add/remove the following Delivery Options [DBA]"))
            Assert.fail("When we select different delivery option  'Please add/remove the following Delivery Options [Attention]'  validation message should be displayed.");

    }
}
