package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Watercraft;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4527: Common - Issues with Editing Existing Contact Name
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Feb 1, 2017
 */
@QuarantineClass
public class TestPLPolicyContactEditPermissions extends BaseTest {
    private GeneratePolicy myPolicyObjPL;

    private String WaterCraftAdditionalInterestLastName = "Test" + StringsUtils.generateRandomNumberDigits(6);
    private String WaterCraftAdditionalInterestCompanyName = "Test" + StringsUtils.generateRandomNumberDigits(10);

    private String ANILastName = "ANIPerson" + StringsUtils.generateRandomNumberDigits(8);
    private String ANICompanyName = "ANIOrg" + StringsUtils.generateRandomNumberDigits(9);

    private String MembershipLastName = "MEM" + StringsUtils.generateRandomNumberDigits(6);

    private String policyMemberLastName = "POLMPer" + StringsUtils.generateRandomNumberDigits(8);
    private String policyMemberCompanyName = "POLMOrg" + StringsUtils.generateRandomNumberDigits(9);

    private WebDriver driver;

    @Test()
    public void testCreateSquirePolWithAllSections() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

        // Property details
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
        prop1.setpropertyType(PropertyTypePL.ResidencePremises);
        prop1.setConstructionType(ConstructionTypePL.Frame);
        locOnePropertyList.add(prop1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(5);
        locationsList.add(locToAdd);

        // Farm Equipment

        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicles = new ArrayList<RecreationalEquipment>();
        RecreationalEquipment recVehicle = new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand");
        recVehicles.add(recVehicle);

        // Watercraft
        ArrayList<AdditionalInterest> watercraftAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest waterAddInterest = new AdditionalInterest("WaterCraft", this.WaterCraftAdditionalInterestLastName, new AddressInfo(false));
        waterAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        AdditionalInterest waterCompanyAddInterest = new AdditionalInterest(ContactSubType.Company);
        waterCompanyAddInterest.setCompanyName(this.WaterCraftAdditionalInterestCompanyName);
        waterCompanyAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        watercraftAdditionalInterests.add(waterAddInterest);
        watercraftAdditionalInterests.add(waterCompanyAddInterest);

        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);

        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
                PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(watercraftAdditionalInterests);

        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        // Section II coverages


        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(100);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);
        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIICowCoverage);
        SectionIICoverages livestock = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

        SquireLiability liability = new SquireLiability();
        liability.getSectionIICoverageList().add(livestock);


        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);
        imTypes.add(InlandMarine.Watercraft);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        // FPP
        SquireFPP squireFPP = new SquireFPP();
        SquireFPPTypeItem machinery = new SquireFPPTypeItem(squireFPP);
        machinery.setType(FPPFarmPersonalPropertySubTypes.Tractors);
        machinery.setValue(1000);

        ArrayList<SquireFPPTypeItem> listOfFPPItems = new ArrayList<SquireFPPTypeItem>();
        listOfFPPItems.add(machinery);
        squireFPP.setItems(listOfFPPItems);

        //ANI
        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test" + StringsUtils.generateRandomNumberDigits(8), this.ANILastName,
                AdditionalNamedInsuredType.Spouse, new AddressInfo(false));
        ani.setHasMembershipDues(true);
        ani.setNewContact(CreateNew.Create_New_Always);
        listOfANIs.add(ani);

        // Policy Members
        ArrayList<Contact> policyMembers = new ArrayList<Contact>();
        Contact polMember = new Contact("PolMember", this.policyMemberLastName, Gender.Male, DateUtils.convertStringtoDate("01/01/1980", "MM/dd/yyyy"));
        policyMembers.add(polMember);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicles;
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Contact", "EditPerm")
                .withANIList(listOfANIs)
                .build(GeneratePolicyType.QuickQuote);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        Login login = new Login(driver);
        login.loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.getAgentUserName(), this.myPolicyObjPL.agentInfo.getAgentPassword(), this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        //Additing AdditionalInsured Company
        PolicyInfoAdditionalNamedInsured aniCompany = new PolicyInfoAdditionalNamedInsured(ContactSubType.Company, this.ANICompanyName,
                AdditionalNamedInsuredType.Affiliate, new AddressInfo(false));
        aniCompany.setNewContact(CreateNew.Create_New_Always);

		new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByCompanyName(true, aniCompany.getCompanyName(), null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        editANIPage.setEditAdditionalNamedInsuredAddressListing(this.myPolicyObjPL.pniContact.getAddress());
/*		editANIPage.setEditAdditionalNamedInsuredAddressLine1(this.myPolicyObjPL.pniContact.getAddress().getLine1());
		editANIPage.setEditAdditionalNamedInsuredAddressCity(this.myPolicyObjPL.pniContact.getAddress().getCity());
		editANIPage.setEditAdditionalNamedInsuredAddressState(this.myPolicyObjPL.pniContact.getAddress().getState());
		editANIPage.setEditAdditionalNamedInsuredAddressZipCode(this.myPolicyObjPL.pniContact.getAddress().getZip());
		editANIPage.setEditAdditionalNamedInsuredAddressAddressType(this.myPolicyObjPL.pniContact.getAddress().getType());
*/
        editANIPage.setEditAdditionalNamedInsuredRelationship(this.myPolicyObjPL.aniList.get(0).getRelationshipToPNI());
        editANIPage.setEditAdditionalNamedInsuredAlternateID(aniCompany.getCompanyName());
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.clickOK();

        //Memebeship dues
        policyInfo.clickMemberShipDuesAddButton();
        addressBook.searchAddressBookByFirstLastName(true, "Membership", MembershipLastName, null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredAddressListing(this.myPolicyObjPL.pniContact.getAddress());
/*		common.setAddress(this.myPolicyObjPL.pniContact.getAddress().getLine1());
		common.setCity(this.myPolicyObjPL.pniContact.getAddress().getCity());
		common.setState(this.myPolicyObjPL.pniContact.getAddress().getState());
		common.setZip(this.myPolicyObjPL.pniContact.getAddress().getZip());
		common.setAddressType(this.myPolicyObjPL.pniContact.getAddress().getType());
		*/
        addressBook.sendArbitraryKeys(Keys.TAB);
        addressBook.clickOK();

        // Adding policy member
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        addressBook.searchAddressBookByCompanyName(this.myPolicyObjPL.basicSearch, this.policyMemberCompanyName, this.myPolicyObjPL.pniContact.getAddress(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(this.policyMemberCompanyName);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());

        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickBasicsContactsTab();
        householdMember.clickOK();

        // property Details Additional  Interest
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.addExistingAdditionalInterest(this.policyMemberLastName);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();


        //adding property Details company
        propertyDetail.addExistingAdditionalInterest(this.policyMemberCompanyName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        propertyDetail.clickOk();

        //FPP Additional Interest
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverage.clickFarmPersonalProperty();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.clickFPPAdditionalInterests();
        GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
        fppInts.addExistingOtherContactsAdditionalInterest(this.policyMemberLastName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();


        //FPP company name
        fppInts.addExistingOtherContactsAdditionalInterest(this.policyMemberCompanyName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();


        //Vehicle Additional Interest
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.addExistingAutoAdditionalInterest(this.policyMemberLastName, "Lessor");
        vehiclePage.addExistingAutoAdditionalInterest(this.policyMemberCompanyName, "Lessor");
        vehiclePage.clickOK();


        //recreational equipment Additional interest
        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.addExistingAdditionalInterest(this.policyMemberLastName, AdditionalInterestType.LienholderPL);
        recreationalEquipment.addExistingAdditionalInterest(this.policyMemberCompanyName, AdditionalInterestType.LienholderPL);
        recreationalEquipment.clickOK();

        //Watercraft
        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.addWatercraftExistingAdditionalInterest(this.policyMemberLastName, AdditionalInterestType.LienholderPL);
        watercraft.addWatercraftExistingAdditionalInterest(this.policyMemberCompanyName, AdditionalInterestType.LienholderPL);
        watercraft.clickOK();

        //Farm Equipment
        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.addFarmEquipmentExistingAdditionalInterest(this.policyMemberLastName, AdditionalInterestType.LienholderPL);
        farmEquipment.addFarmEquipmentExistingAdditionalInterest(this.policyMemberCompanyName, AdditionalInterestType.LienholderPL);
        farmEquipment.clickOk();

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

    }

    //VP Personal Lines able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithVPPersonalLines() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        validateContactEditablePermission();
    }

    //Underwriter Assistant able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithUWAssistant() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);

        validateContactEditablePermission();
    }

    //Coder and Underwriter able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithCoderAndUW() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);

        validateContactEditablePermission();
    }

    //Processor able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithProcessor() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        validateContactEditablePermission();
    }

    //Transition Team able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithTransitionTeam() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        validateContactEditablePermission();
    }

    //UW Supervisor able to edit contact details
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditWithUWSupervisor() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        validateContactEditablePermission();
    }

    //CSR not edit permission
    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testValidateContactEditAnotherAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber("torr", "gw", this.myPolicyObjPL.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickAccountSummaryPendingTransactionByStatus("Draft");
        validateContactNotEditablePermission();
    }

    private void validateContactEditablePermission() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";
        //Policy Info - ANI Person contact
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickANIContact(ANILastName);
        ANILastName = ANILastName + "Edit";
        enterPersonDetails("ANIFirst", "ANIMiddle", ANILastName);

        //Policy Info - ANI Company Contact
        policyInfo.clickANIContact(ANICompanyName);
        ANICompanyName = ANICompanyName + "Edit";
        enterCompanyDetails(ANICompanyName);

        //Policy Info - Membership
        policyInfo.clickMembershipContact(this.MembershipLastName);
        MembershipLastName = MembershipLastName + "Edit";
        enterPersonDetails("MemberFirst", "MemberMiddle", MembershipLastName);

        if (!policyInfo.checkANIContact(ANILastName)) {
            errorMessage = errorMessage + "Additional Named Insured : " + ANILastName + " is not displayed./n";
        }

        if (!policyInfo.checkANIContact(ANICompanyName)) {
            errorMessage = errorMessage + "Additional Named Insured : " + ANICompanyName + " is not displayed./n";
        }

        if (!policyInfo.checkMembershipContact(MembershipLastName)) {
            errorMessage = errorMessage + "Additional Named Insured : " + MembershipLastName + " is not displayed./n";
        }
        //Policy Members
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickPolicyHolderMembersByName(this.policyMemberLastName);
        policyMemberLastName = policyMemberLastName + "Edit";
        enterPersonDetails("PMFirst", "PMMiddle", policyMemberLastName);
        household.clickPolicyHolderMembersByName(this.policyMemberCompanyName);
        policyMemberCompanyName = policyMemberCompanyName + "Edit";
        enterCompanyDetails(policyMemberCompanyName);
        if (household.getPolicyHouseholdMembersTableRow(policyMemberLastName) == 0) {
            errorMessage = errorMessage + "Policy Member : " + policyMemberLastName + " is not displayed./n";
        }
        if (household.getPolicyHouseholdMembersTableRow(policyMemberCompanyName) == 0) {
            errorMessage = errorMessage + "Policy Member : " + policyMemberCompanyName + " is not displayed./n";
        }

        //Property Additional Interest
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);

        //person details
        propertyDetail.clickAdditionalInterestByName(this.policyMemberLastName);

        policyMemberLastName = "PDPerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("PDFirstName", "PDMiddleName", policyMemberLastName);
        propertyDetail.clickAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "PDORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!propertyDetail.checkAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "Property Details : " + policyMemberLastName + " is not displayed./n";
        }
        if (!propertyDetail.checkAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "Property Details : " + policyMemberCompanyName + " is not displayed./n";
        }
        propertyDetail.clickOk();

        // Coverage FPP
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverage.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.clickFPPAdditionalInterests();
        GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);

        fppInts.clickFPPAdditionalInterestByName(policyMemberLastName);
        policyMemberLastName = "FPPPerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("FPPFirstName", "FPPMiddleName", policyMemberLastName);

        fppInts.clickFPPAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "FPPORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!fppInts.checkFPPAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "FPP : " + policyMemberLastName + " is not displayed./n";
        }
        if (!fppInts.checkFPPAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "FPP : " + policyMemberCompanyName + " is not displayed./n";
        }

        // Driver details
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(this.myPolicyObjPL.pniContact.getLastName());
        String driverfirst = "Driver" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails(driverfirst, "DriverMiddleName", "DriverLastName");
        boolean driverChangeFound = false;
        for (int currentDriver = 1; currentDriver <= paDrivers.getDriverTableRowsCount(); currentDriver++) {
            if (paDrivers.getDriverTableColumnValueByRow(1, "Name").contains(driverfirst)) {
                driverChangeFound = true;
                break;
            }
        }

        if (!driverChangeFound) {
            errorMessage = errorMessage + "Driver : " + driverfirst + " is not displayed./n";
        }

        //Vehicle Additional Interest
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.clickVehicleAdditionalInterestByName(policyMemberLastName);
        policyMemberLastName = "VehiclePerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("VehicleFirstName", "VehicleMiddleName", policyMemberLastName);

        vehiclePage.clickVehicleAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "VehicleORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!vehiclePage.checkVehicleAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "Vehicle : " + policyMemberLastName + " is not displayed./n";
        }
        if (!vehiclePage.checkVehicleAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "Vehicle : " + policyMemberCompanyName + " is not displayed./n";
        }
        vehiclePage.clickOK();


        //recreational equipment Additional interest
        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);

        recreationalEquipment.clickRecEquipmentAdditionalInterestByName(policyMemberLastName);
        policyMemberLastName = "RecEquipPerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("RecEquipFirstName", "RecEquipMiddleName", policyMemberLastName);

        recreationalEquipment.clickRecEquipmentAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "RecEquipORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!recreationalEquipment.checkRecEquipmentAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "RecEquipment : " + policyMemberLastName + " is not displayed./n";
        }
        if (!recreationalEquipment.checkRecEquipmentAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "RecEquipment : " + policyMemberCompanyName + " is not displayed./n";
        }
        recreationalEquipment.clickOK();

        //Watercraft
        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.clickWatercraftAdditionalInterestByName(policyMemberLastName);
        policyMemberLastName = "WatercraftPerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("WatercraftFirstName", "WatercraftMiddleName", policyMemberLastName);

        watercraft.clickWatercraftAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "WatercraftORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!watercraft.checkWatercraftAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "Watercraft : " + policyMemberLastName + " is not displayed./n";
        }
        if (!watercraft.checkWatercraftAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "Watercraft : " + policyMemberCompanyName + " is not displayed./n";
        }
        watercraft.clickOK();

        //Farm Equipment
        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.clickFarmEquipmentAdditionalInterestByName(policyMemberLastName);
        policyMemberLastName = "FarmEquipPerson" + StringsUtils.generateRandomNumberDigits(6);
        enterPersonDetails("FarmEquipFirstName", "FarmEquipMiddleName", policyMemberLastName);

        farmEquipment.clickFarmEquipmentAdditionalInterestByName(this.policyMemberCompanyName);

        policyMemberCompanyName = "FarmEquipORG" + StringsUtils.generateRandomNumberDigits(7);
        enterCompanyDetails(policyMemberCompanyName);

        if (!farmEquipment.checkFarmEquipmentAdditionalInterestByName(policyMemberLastName)) {
            errorMessage = errorMessage + "FarmEquipment : " + policyMemberLastName + " is not displayed./n";
        }
        if (!farmEquipment.checkFarmEquipmentAdditionalInterestByName(policyMemberCompanyName)) {
            errorMessage = errorMessage + "FarmEquipment : " + policyMemberCompanyName + " is not displayed./n";
        }
        farmEquipment.clickOk();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    private void validateContactNotEditablePermission() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        String errorMessage = "";

        //Policy Members
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickPolicyHolderMembersByName(this.policyMemberLastName);

        if (household.checkFirstNameEditable() || household.checkLastNameEditable()) {
            errorMessage = errorMessage + "Policy Member : " + policyMemberLastName + " is editable./n";
        }
        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickReturnTo();
        household.clickPolicyHolderMembersByName(this.policyMemberCompanyName);

        if (household.checkCompanyNameEditable()) {
            errorMessage = errorMessage + "Policy Member : " + policyMemberCompanyName + " is editable./n";
        }
        genericWO.clickReturnTo();

        //Property Additional Interest
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        //person details
        propertyDetail.clickAdditionalInterestByName(this.policyMemberLastName);

        if (household.checkFirstNameEditable() || household.checkLastNameEditable()) {
            errorMessage = errorMessage + "Property Details : " + policyMemberLastName + " is editable./n";
        }

        genericWO.clickReturnTo();
        propertyDetail.clickAdditionalInterestByName(this.policyMemberCompanyName);

        if (household.checkCompanyNameEditable()) {
            errorMessage = errorMessage + "Property Details : " + policyMemberCompanyName + " is editable./n";
        }
        genericWO.clickReturnTo();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    private void enterPersonDetails(String firstName, String middleName, String lastName) {
        BasePage bp = new BasePage(driver);
        bp.setText(bp.find(By.cssSelector("input[id$=':FirstName-inputEl']")), firstName);
        bp.setText(bp.find(By.cssSelector("input[id$=':MiddleName-inputEl']")), middleName);
        bp.setText(bp.find(By.cssSelector("input{id$=':LastName-inputEl']")), lastName);
        bp.clickUpdate();
    }

    private void enterCompanyDetails(String companyName) {
        BasePage bp = new BasePage(driver);
        WebElement ele = bp.find(By.xpath("//input[contains(@id, ':Name-inputEl') or contains(@id, ':CompanyName-inputEl') or contains(@id, ':GlobalContactNameInputSet:Name-inputEl') or contains(@id, ':Keyword-inputEl"));
        bp.setText(ele, companyName);
        bp.clickUpdate();
    }
}
