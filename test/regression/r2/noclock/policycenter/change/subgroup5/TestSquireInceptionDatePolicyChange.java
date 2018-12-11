package regression.r2.noclock.policycenter.change.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
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
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
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
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_CoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US9185: COMMON - Updates to Inception Date
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Inception%20Date.xlsx">
 * Link Text</a>
 * @Description : Requirement, inception date will be same as the transaction date of the job that the line was added in. This will apply to Umbrella policy level as well.
 * @DATE Nov 17, 2016
 * @throws Exception
 */
@QuarantineClass
public class TestSquireInceptionDatePolicyChange extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myInitialSQPol, myCurrentSQPol;
    private Underwriters uw;
    private Date changeDate;
    
    @Test()
    public void testIssueInitialSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
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
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

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
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -8);


        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myInitialSQPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Change", "Inception")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueInitialSquirePolicy"})
    public void testIssueCurrentSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
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
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myCurrentSQPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("ChangeNew", "Inception")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueCurrentSquirePolicy"})
    private void testSquirePolicyChangeWithNewSection() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myCurrentSQPol.agentInfo.getAgentUserName(),
                myCurrentSQPol.agentInfo.getAgentPassword(), myCurrentSQPol.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

        // add 10 days to current date
        changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();

        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkSquireInlandMarine(true);

        sideMenu.clickSideMenuIMCoveragePartSelection();

        GenericWorkorderSquireInlandMarine_CoverageSelection imSelection = new GenericWorkorderSquireInlandMarine_CoverageSelection(driver);
        imSelection.checkCoverage(true, InlandMarine.RecreationalEquipment.getValue());

        sideMenu.clickSideMenuIMRecreationVehicle();
        RecreationalEquipment latestrecEquip = new RecreationalEquipment(RecreationalEquipmentType.OffRoadMotorcycle,
                "6000", PAComprehensive_CollisionDeductible.TwentyFive25, "Test Automation");
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recEquipPage = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recEquipPage.recEquip(latestrecEquip);
        try {
            sideMenu.clickSideMenuSquirePropertyCoverages();
        } catch (Exception e) {
            sideMenu.clickSideMenuSquirePropertyCoverages();
        }
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        SectionIICoverages sectionIICoverages = new SectionIICoverages(SectionIICoveragesEnum.OffRoadMotorcycles, 0, 2);

        sectionII.addCoverages(sectionIICoverages);
        sectionII.setQuantity(sectionIICoverages);

        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQualificationIMLosses(false);
        qualificationPage.setSquireGeneralFullTo(false);

        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setTransferedFromAnotherPolicy(true);
        getQALogger().info(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)));
        String errorMessage = "";
        if (!changeDate.equals(polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine))) {
            errorMessage = errorMessage
                    + "Expected Section IV default date with policy change effective date is not displayed.";
        }

        polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.InlandMarine,
                this.myInitialSQPol.squire.getPolicyNumber());
        Date valExpectedSection1InceptionDate = this.myInitialSQPol.squire.getEffectiveDate();
        Date valSection1VInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine);
        if (valSection1VInceptionDate.equals(valExpectedSection1InceptionDate)) {
            getQALogger().info("Expected Section IV Inception Date : " + valSection1VInceptionDate + " are displayed.");
        } else {
            errorMessage = errorMessage + "Expected Section 1V Inception Date : " + valExpectedSection1InceptionDate
                    + " are not displayed.  \n";
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testSquirePolicyChangeWithNewSection"})
    private void testValidateInceptionDateWithUW() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myCurrentSQPol.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickWorkOrderbySuffix("0003");
        //

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setTransferedFromAnotherPolicy(false);
        polInfo.setTransferedFromAnotherPolicy(true);

        String errorMessage = "";
        polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.myInitialSQPol.squire.getPolicyNumber());
        Date valExpectedSection1InceptionDate = this.myInitialSQPol.squire.getEffectiveDate();
        Date valSection1VInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine);
        if (valSection1VInceptionDate.equals(valExpectedSection1InceptionDate)) {
            errorMessage = errorMessage + "UnExpected Section 1V Inception Date : " + valExpectedSection1InceptionDate
                    + " displayed.  \n";
        }

        if (!valSection1VInceptionDate.equals(this.changeDate)) {
            errorMessage = errorMessage + "Expected Section 1V Inception Date : " + DateUtils.getDateValueOfFormat(this.changeDate, "MM/dd/yyyy")
                    + " is not displayed.  \n";
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }
}
