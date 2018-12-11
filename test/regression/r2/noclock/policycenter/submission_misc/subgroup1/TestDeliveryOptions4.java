package regression.r2.noclock.policycenter.submission_misc.subgroup1;

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
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderDeliveryOption;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author skandibanda
 * @Requirement : US12141: Additional Interest Delivery option validation
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/128071644704d/detail/userstory/143971432508">Rally Story</a>
 * @DATE Oct 03, 2017
 */
public class TestDeliveryOptions4 extends BaseTest {
    private GeneratePolicy myStdAutoObj;

    private WebDriver driver;

//    //squire SectionI policy
//    @Test
//    public void testGenerateSquireFullApp() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
//
//        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
//        propLoc.setPlNumAcres(10);
//        propLoc.setPlNumResidence(5);
//        locationsList.add(propLoc);
//
//        myPolicyObj = new GeneratePolicy.Builder(driver)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
//                .withCreateNew(CreateNew.Create_New_Always)
//                .withInsPersonOrCompany(ContactSubType.Person)
//                .withSquireEligibility(SquireEligibility.FarmAndRanch)
//                .withInsFirstLastName("Guy", "Auto")
//                .withPolicyLocations(locationsList)
//                .withPolOrgType(OrganizationType.Individual)
//                .build(GeneratePolicyType.FullApp);
//
//        validateDeliveryOption(myPolicyObj);
//    }
//
//    //Standard fire policy
//    @Test
//    public void testGenerateStdFireFullApp() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
//        property.setpropertyType(PropertyTypePL.DwellingPremises);
//
//        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty();
//        property1.setpropertyType(PropertyTypePL.DwellingPremisesCovE);
//
//        locOnePropertyList.add(property);
//        locOnePropertyList.add(property1);
//
//        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//        locToAdd.setPlNumResidence(12);
//        locToAdd.setPlNumAcres(12);
//        locationsList.add(locToAdd);
//
//        myStdFireObj = new GeneratePolicy.Builder(driver)
//                .withProductType(ProductLineType.StandardFire)
//                .withLineSelection(LineSelection.StandardFirePL)
//                .withCreateNew(CreateNew.Create_New_Always)
//                .withInsPersonOrCompany(ContactSubType.Person)
//                .withInsFirstLastName("Test", "Delivery")
//                .withInsAge(26)
//                .withPolOrgType(OrganizationType.Individual)
//                .withPolicyLocations(locationsList)
//                .build(GeneratePolicyType.FullApp);
//
//        validateDeliveryOption(myStdFireObj);
//    }
//
//
//    public void validateDeliveryOption(GeneratePolicy pol) throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), pol.accountNumber);
//        //        editPolicyTransaction();
//
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
//
//        //Adding lien Holder
//        sideMenu.clickSideMenuSquirePropertyDetail();
//        propDet.clickViewOrEditBuildingButton(1);
//        //        AddressInfo bankAddress = new AddressInfo();
//        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
//        loc2Bldg1AddInterest.setAddress(bankAddress);
//        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
//
//        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
//        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
//        //        SearchAddressBookPC search = new SearchAddressBookPC(driver);
//        search.searchAddressBookByCompanyName(loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
//        //        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
//        additionalInterests.setAdditionalInterestsLoanNumber("1234");
//
//        DeliveryOptionType optionType = DeliveryOptionType.Attention;
//        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
//        deliveryOptions.addDeliveryOption(optionType);
//        //        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
//        String companyName = additionalInterests.getAdditionalInterestsName();
//        //        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        if(overrideAddressStandardization()) {
//        	additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        }
//        //        propDet.clickOk();
//        //        propDet.clickViewOrEditBuildingButton(2);
//        additionalInterests.addExistingAdditionalInterest(companyName);
//        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
//        additionalInterests.setAdditionalInterestsLoanNumber("1234");
//
//        optionType = DeliveryOptionType.DBA;
//        deliveryOptions.addDeliveryOption(optionType);
//        //        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
//        //        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        //
//        if (!ErrorHandlingHelpers.getErrorMessage().contains("Please add/remove the following Delivery Options [Attention]"))
//            Assert.fail("When we select different delivery option  'Please add/remove the following Delivery Options [Attention]'  validation message should be displayed.");
//
//    }
//
//    //Squire Auto policy
//    @Test
//    public void testGenerateSquireAutoFullApp() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.TenK);
//        coverages.setUninsuredLimit(UninsuredLimit.OneHundred);
//
//        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
//        squirePersonalAuto.setCoverages(coverages);
//
//        Squire mySquire = new Squire(SquireEligibility.City);
//        mySquire.squirePA = squirePersonalAuto;
//
//        mySquireAutoObj = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PersonalAutoLinePL)
//                .withInsFirstLastName("Test", "Delivery")
//                .build(GeneratePolicyType.FullApp);
//
//        validateVehicleDeliveryOption(mySquireAutoObj);
//
//    }

    //Standard Auto Policy
    @Test(enabled = true)
    public void testGenerateStandardAutoFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myStdAutoObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Guy", "Auto")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

        validateVehicleDeliveryOption(myStdAutoObj);
    }

//    //Squire Section IV
//    @Test
//    public void testSquireInlandMarine_FullApp_RecreationalEquipment() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//        locToAdd.setPlNumAcres(11);
//        locationsList.add(locToAdd);
//
//        SquireLiability myLiab = new SquireLiability();
//        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);
//
//
//        // Rec Equip
//        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.Snowmobile, "500", PAComprehensive_CollisionDeductible.Fifty50, "Testing Purpose"));
//
//        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
//        imCoverages.add(InlandMarine.RecreationalEquipment);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//
//        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
//        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
//        myInlandMarine.recEquipment_PL_IM = recVehicle;
//
//        Squire mySquire = new Squire(SquireEligibility.Country);
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//        mySquire.inlandMarine = myInlandMarine;
//        
//        mySquireIMObj = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
//                .withInsFirstLastName("Guy", "RecEquip")
//                .build(GeneratePolicyType.FullApp);
//
//        loginAndSearchJob(mySquireIMObj.agentInfo.getAgentUserName(), mySquireIMObj.agentInfo.getAgentPassword(), mySquireIMObj.accountNumber);
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//
//        editPolicyTransaction();
//        sideMenu.clickSideMenuIMCoveragePartSelection();
//        sideMenu.clickSideMenuIMRecreationVehicle();
//
//        //Changing Recreational Equipment Detectable
//        GenericWorkorderSquireIMRecEquipment recreationalEquipment = new GenericWorkorderSquireIMRecEquipment();
//        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
//
//        //Adding lien Holder
//        AddressInfo bankAddress = new AddressInfo();
//        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
//        loc2Bldg1AddInterest.setAddress(bankAddress);
//        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
//
//        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
//        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
//        //        SearchAddressBookPC search = new SearchAddressBookPC(driver);
//        search.searchAddressBookByCompanyName(loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
//        //        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
//        additionalInterests.setAdditionalInterestsLoanNumber("1234");
//
//        DeliveryOptionType optionType = DeliveryOptionType.DBA;
//        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
//        deliveryOptions.addDeliveryOption(optionType);
//        //        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
//        String companyName = additionalInterests.getAdditionalInterestsName();
//        //        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        //        recreationalEquipment.clickOK();
//        //        recreationalEquipment.clickLinkInRecreationalEquipmentTable(2);
//        additionalInterests.addExistingAdditionalInterest(companyName);
//        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
//        additionalInterests.setAdditionalInterestsLoanNumber("1234");
//
//        optionType = DeliveryOptionType.Attention;
//        deliveryOptions.addDeliveryOption(optionType);
//        //        deliveryOptions.clickAddToSelectedDeliveryOptionsByText(optionType.getTypeValue());
//        //        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
//        //
//        if (!ErrorHandlingHelpers.getErrorMessage().contains("Please add/remove the following Delivery Options [DBA]"))
//            Assert.fail("When we select different delivery option  'Please add/remove the following Delivery Options [Attention]'  validation message should be displayed.");
//    }

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
