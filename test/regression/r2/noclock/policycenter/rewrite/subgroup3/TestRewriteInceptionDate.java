package regression.r2.noclock.policycenter.rewrite.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

@QuarantineClass
public class TestRewriteInceptionDate extends BaseTest {
    private GeneratePolicy myInitialSQPol, myCurrentSQPol;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testIssueInitialSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle secondVeh = new Vehicle();
        secondVeh.setEmergencyRoadside(true);
        vehicleList.add(secondVeh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);
        imTypes.add(InlandMarine.PersonalProperty);
        imTypes.add(InlandMarine.Watercraft);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
                PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        // Personal Property
        PersonalProperty pprop2 = new PersonalProperty();
        pprop2.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop2.setYear(2011);
        pprop2.setMake("Testmake2");
        pprop2.setModel("Testmodel2");
        pprop2.setVinSerialNum("456456456");
        pprop2.setDescription("Testdescription2");
        pprop2.setLimit(1234);
        pprop2.setDeductible(PersonalPropertyDeductible.Ded25);
        ArrayList<String> pprop2AdditIns = new ArrayList<String>();
        pprop2AdditIns.add("First Guy2");
        pprop2AdditIns.add("Second Guy2");
        pprop2.setAdditionalInsureds(pprop2AdditIns);
        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop2);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);

        // Cargo
        Vin vin = VINHelper.getRandomVIN();
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()),
                vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("3123");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);

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
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -8);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myInitialSQPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Rewrite", "Inception")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueInitialSquirePolicy"})
    private void testcreateSquireFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300_500_300);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Watercraft);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
                PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myCurrentSQPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Inception", "Rewrite")
                .withPolTermLengthDays(79)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testcreateSquireFullApp"})
    private void testAddCommissionInceptionDateToSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myCurrentSQPol.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        // Commission
        polInfo.clickInceptionDateByRowAndColumnHeader(2, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(2, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        polInfo.clickInceptionDateByRowAndColumnHeader(3, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(3, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        polInfo.clickInceptionDateByRowAndColumnHeader(4, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(4, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.performRiskAnalysisAndQuote(myCurrentSQPol);

        if (quote.hasBlockBind()) {
            riskAnalysis.handleBlockSubmit(myCurrentSQPol);
        }

        new GuidewireHelpers(driver).logout();


        new GuidewireHelpers(driver).setPolicyType(myCurrentSQPol, GeneratePolicyType.FullApp);
        myCurrentSQPol.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testAddCommissionInceptionDateToSquire"})
    private void testRewriteSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myCurrentSQPol.squire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        testCancelPolicy(myCurrentSQPol.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        StartRewrite rewritePage = new StartRewrite(driver);

        rewritePage.rewriteFullTermGuts(myCurrentSQPol);
        sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        if (!polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(myCurrentSQPol.squire.getEffectiveDate())) {
            errorMessage = errorMessage + "Inception Date is not correct! \n";
        }

        if (!polInfo.getOverrideCommissionValue(2) && polInfo.getOverrideCommissionReason(2) == "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 2 is cleared.\n";
        }

        if (!polInfo.getOverrideCommissionValue(3) && polInfo.getOverrideCommissionReason(3) == "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 3 is cleared.\n";
        }

        if (!polInfo.getOverrideCommissionValue(4) && polInfo.getOverrideCommissionReason(4) == "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 4 is cleared.\n";
        }

        polInfo.setTransferedFromAnotherPolicy(true);

        polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.myInitialSQPol.squire.getPolicyNumber());
        Date valExpectedSection1InceptionDate = this.myInitialSQPol.squire.getEffectiveDate();
        Date valSection1VInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
        if (valSection1VInceptionDate.equals(DateUtils.getDateValueOfFormat(valExpectedSection1InceptionDate, "MM/dd/yyyy"))) {
            System.out.println("Expected Policy Inception Date : " + valSection1VInceptionDate + " are displayed.");
        } else {
            errorMessage = errorMessage + "Expected Policy Inception Date : " + valExpectedSection1InceptionDate
                    + " are not displayed.  \n";
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testIssueInitialSquirePolicy"})
    public void createUmbrelate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myInitialSQPol.squireUmbrellaInfo = squireUmbrellaInfo;
        myInitialSQPol.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);


        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myInitialSQPol.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        // Commission
        polInfo.clickInceptionDateByRowAndColumnHeader(1, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(1, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.performRiskAnalysisAndQuote(myInitialSQPol);

        if (quote.hasBlockBind()) {
            riskAnalysis.handleBlockSubmit(myInitialSQPol);
        }

        guidewireHelpers.logout();

        guidewireHelpers.setPolicyType(myInitialSQPol, GeneratePolicyType.FullApp);
        myInitialSQPol.convertTo(driver, GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"createUmbrelate"})
    private void testUmbrellaRewriteFullTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myInitialSQPol.squireUmbrellaInfo.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        testCancelPolicy(myInitialSQPol.squireUmbrellaInfo.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTermGuts(myInitialSQPol);
        sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);

        if (!polInfo.getOverrideCommissionValue(1) && polInfo.getOverrideCommissionReason(1) == "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 1 is cleared.\n";
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    private void testCancelPolicy(String policyNumber) {

        PolicySummary summary = new PolicySummary(driver);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(policyNumber);
        String errorMessage = "";

        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not success!!!";

        if (errorMessage != "")
            Assert.fail(errorMessage);

    }
}
