package regression.r2.noclock.policycenter.documents;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
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
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.RelatedInterests;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
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
 * @Requirement : DE4630: PL - pending cancel not showing property/coverage for Additional Interest
 * @RequirementsLink <a href="http://rally1.rallydev.com/#/33274298124d/detail/defect/84500670876 "</a>
 * @Description
 * @DATE Feb 13, 2017
 */
@QuarantineClass
public class TestCreateDocumentWithAdditionalInterests extends BaseTest {
    private GeneratePolicy myPolicyObjPL;

    private String WaterCraftAdditionalInterestLastName = "Water" + StringsUtils.generateRandomNumberDigits(6);
    private String PropoertyDetailsAdditionalInterestLastName = "Prop" + StringsUtils.generateRandomNumberDigits(10);
    private String FPPAdditionalInterestLastName = "FPP" + StringsUtils.generateRandomNumberDigits(10);
    private String VehicleAdditionalInterestLastName = "VEH" + StringsUtils.generateRandomNumberDigits(10);
    private String RecEquipmentAdditionalInterestLastName = "Rec" + StringsUtils.generateRandomNumberDigits(10);
    private String FarmEquipmentAdditionalInterestLastName = "Farm" + StringsUtils.generateRandomNumberDigits(10);

    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testIssueSquirePolicyWithAdditionalInterest() throws Exception {
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
        SquireLiability liability = new SquireLiability();

        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(100);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIICowCoverage);

        SectionIICoverages livestockcoverages = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

        liability.getSectionIICoverageList().add(livestockcoverages);

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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);


        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicles;
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Create", "Document")
                .build(GeneratePolicyType.FullApp);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.getAgentUserName(),
                this.myPolicyObjPL.agentInfo.getAgentPassword(), this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        addNewPolicyMember("PropertyDetails", this.PropoertyDetailsAdditionalInterestLastName);
        addNewPolicyMember("FPP", this.FPPAdditionalInterestLastName);
        addNewPolicyMember("Vehicle", this.VehicleAdditionalInterestLastName);
        addNewPolicyMember("Rec", this.RecEquipmentAdditionalInterestLastName);
        addNewPolicyMember("Watercraft", this.WaterCraftAdditionalInterestLastName);
        addNewPolicyMember("Farm", this.FarmEquipmentAdditionalInterestLastName);

        // property Details Additional Interest
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.addExistingAdditionalInterest(this.PropoertyDetailsAdditionalInterestLastName);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        propertyDetail.clickOk();

        // FPP Additional Interest
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverage.clickFarmPersonalProperty();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.clickFPPAdditionalInterests();
        GenericWorkorderSquireFPPAdditionalInterest fppInts = new GenericWorkorderSquireFPPAdditionalInterest(driver);
        fppInts.addExistingOtherContactsAdditionalInterest(this.FPPAdditionalInterestLastName);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        // Vehicle Additional Interest
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.addExistingAutoAdditionalInterest(this.VehicleAdditionalInterestLastName, "Lessor");
        vehiclePage.clickOK();
        // recreational equipment Additional interest
        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.addExistingAdditionalInterest(this.RecEquipmentAdditionalInterestLastName,
                AdditionalInterestType.LienholderPL);
        recreationalEquipment.clickOK();

        // Watercraft
        sideMenu.clickSideMenuIMWatercraft();
        GenericWorkorderSquireInlandMarine_Watercraft watercraft = new GenericWorkorderSquireInlandMarine_Watercraft(driver);
        watercraft.clickEditButton();
        watercraft.addWatercraftExistingAdditionalInterest(this.WaterCraftAdditionalInterestLastName,
                AdditionalInterestType.LienholderPL);
        watercraft.clickOK();

        // Farm Equipment
        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquipment.clickEditButton();
        farmEquipment.addFarmEquipmentOtherAdditionalInterest(this.FarmEquipmentAdditionalInterestLastName,
                AdditionalInterestType.LienholderPL);
        farmEquipment.clickOk();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(this.myPolicyObjPL);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            risk.handleBlockSubmit(this.myPolicyObjPL);
        }

        guidewireHelpers.logout();


        guidewireHelpers.setPolicyType(myPolicyObjPL, GeneratePolicyType.FullApp);
        this.myPolicyObjPL.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePolicyWithAdditionalInterest"})
    private void testCreatePendingCancel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickPendingCancel();
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date cancelEffectiveDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 45);
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.setSource("Underwriting");
        cancelPol.setCancellationReason("Lack of underwriting information");
        cancelPol.setExplanation("not receiving insured�s signature");
        cancelPol.setEffectiveDate(cancelEffectiveDate);
        cancelPol.clickNextButton();
        String errorMessage = "";
        RelatedInterests relatedInterests = new RelatedInterests(driver);

        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.PropoertyDetailsAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "Property Details Additional Interest is not displayed. \n";
        }

        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.FPPAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "FPP Additional Interest is not displayed. \n";
        }

        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.VehicleAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "Vehicle Additional Interest is not displayed. \n";
        }

        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.FarmEquipmentAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "Farm Equipment Additional Interest is not displayed. \n";
        }


        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.WaterCraftAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "Watercraft Additional Interest is not displayed. \n";
        }

        if (relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.RecEquipmentAdditionalInterestLastName) <= 0) {
            errorMessage = errorMessage + "Recreational Equipment Additional Interest is not displayed. \n";
        }

        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.PropoertyDetailsAdditionalInterestLastName));
        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.FPPAdditionalInterestLastName));
        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.VehicleAdditionalInterestLastName));
        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.FarmEquipmentAdditionalInterestLastName));
        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.WaterCraftAdditionalInterestLastName));
        relatedInterests.selectRelatedInterestByRow(relatedInterests.getRelatedInterestDisplayedRowNumberByName(this.RecEquipmentAdditionalInterestLastName));
        relatedInterests.clickAddRelatedAdditionalInterestsButton();
        relatedInterests.clickFinishButton();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    @Test(dependsOnMethods = {"testCreatePendingCancel"})
    private void testCreateDocumentFromTemplate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        createDocumentFromTemplateWithAdditionalInterestName(PropoertyDetailsAdditionalInterestLastName, this.myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getpropertyType().getValue());
        createDocumentFromTemplateWithAdditionalInterestName(FPPAdditionalInterestLastName, "FPP");
        createDocumentFromTemplateWithAdditionalInterestName(VehicleAdditionalInterestLastName, this.myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getMake());
        createDocumentFromTemplateWithAdditionalInterestName(FarmEquipmentAdditionalInterestLastName, "FarmEquipment");
        createDocumentFromTemplateWithAdditionalInterestName(WaterCraftAdditionalInterestLastName, "Watercraft");
        createDocumentFromTemplateWithAdditionalInterestName(RecEquipmentAdditionalInterestLastName, "Recreation");

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectDocumentType(DocumentType.Additional_Interest_Cancel.getValue());
        docs.clickSearch();

        boolean propertAdditonalInterest = false;
        boolean fppAdditionalInterest = false;
        boolean vehicleAdditionalInterest = false;
        boolean farmEquipAdditionalInterest = false;
        boolean watercraftAdditionalInterest = false;
        boolean recAdditionalInterest = false;

        for (String name : docs.getDocumentNameAddress()) {
            if (name.contains(this.PropoertyDetailsAdditionalInterestLastName)) {
                propertAdditonalInterest = true;
            }
            if (name.contains(this.FPPAdditionalInterestLastName)) {
                fppAdditionalInterest = true;
            }

            if (name.contains(this.RecEquipmentAdditionalInterestLastName)) {
                recAdditionalInterest = true;
            }

            if (name.contains(this.WaterCraftAdditionalInterestLastName)) {
                watercraftAdditionalInterest = true;
            }
            if (name.contains(this.FarmEquipmentAdditionalInterestLastName)) {
                farmEquipAdditionalInterest = true;
            }
            if (name.contains(this.VehicleAdditionalInterestLastName)) {
                vehicleAdditionalInterest = true;
            }
        }
        String errorMessage = "";

        if (!propertAdditonalInterest) {
            errorMessage = errorMessage + "Property Details Additional Interest is not displayed. \n";
        }

        if (!fppAdditionalInterest) {
            errorMessage = errorMessage + "FPP Additional Interest is not displayed. \n";
        }

        if (!vehicleAdditionalInterest) {
            errorMessage = errorMessage + "Vehicle Additional Interest is not displayed. \n";
        }

        if (!farmEquipAdditionalInterest) {
            errorMessage = errorMessage + "Farm Equipment Additional Interest is not displayed. \n";
        }


        if (!watercraftAdditionalInterest) {
            errorMessage = errorMessage + "Watercraft Additional Interest is not displayed. \n";
        }

        if (!recAdditionalInterest) {
            errorMessage = errorMessage + "Recreational Equipment Additional Interest is not displayed. \n";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }


    }

    private void createDocumentFromTemplateWithAdditionalInterestName(String name, String item) {
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();

        actions.click_CreateNewDocumentFromTemplate();
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.searchDocumentTemplateByName(this.myPolicyObjPL.productType, "Additional Interest Termination Notice");
        doc.selectRelatedTo("Policy");
        doc.selectDocumentType(DocumentType.Additional_Interest_Cancel);
        doc.selectReceiver(name);
        doc.sendArbitraryKeys(Keys.TAB);
        if (doc.checkAdditionalInterestsItemExists()) {
            doc.selectAdditionalInterestItem(item);
        }

        doc.clickUpdate();
    }

    private void addNewPolicyMember(String firstName, String lastName) throws Exception {
        // Adding policy member
        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(true, firstName, lastName,
                this.myPolicyObjPL.pniContact.getAddress().getLine1(), this.myPolicyObjPL.pniContact.getAddress().getCity(),
                this.myPolicyObjPL.pniContact.getAddress().getState(), this.myPolicyObjPL.pniContact.getAddress().getZip(),
                CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        householdMember.setSSN("1" + StringsUtils.generateRandomNumberDigits(8));
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.setNewPolicyMemberAlternateID(lastName);

        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickBasicsContactsTab();
        householdMember.clickOK();
    }

}
