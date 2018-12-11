package regression.r2.noclock.policycenter.renewaltransition.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US4772: PL - Issue Renewal
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20Renewal%20-%20Issue%20Renewal.xlsx
 * ">PC8 - HO - Renewal - Issue Renewal</a>
 * @Description
 * @DATE Sep 19, 2016
 */
@QuarantineClass
public class TestPLIssueRenewals extends BaseTest {
    private GeneratePolicy mySquirePolicyObjPL = null;
    private GeneratePolicy myStandardLiabPol;
    private GeneratePolicy myStandardIMPol;
    private GeneratePolicy myStandardFirePol;
    private GeneratePolicy my50DaysSquirePolicyObjPL;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testissueSquirePolExpires80Days() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.HarvestersHeaders, FPPFarmPersonalPropertySubTypes.CirclePivots, FPPFarmPersonalPropertySubTypes.WheelLines);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        mySquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Renew", "Quote")
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        loginAndClosePreRenewal(mySquirePolicyObjPL, false, mySquirePolicyObjPL.squire.getPolicyNumber());
    }

    @Test()
    private void testissueSquirePolExpires50Days() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        my50DaysSquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Renew", "Quote")
                .withPolTermLengthDays(50)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        loginAndClosePreRenewal(my50DaysSquirePolicyObjPL, true, my50DaysSquirePolicyObjPL.squire.getPolicyNumber());
    }

    @Test()
    private void testIssueStandardFirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);


        myStandardFirePol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withPolTermLengthDays(80)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Renew", "Quote")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        loginAndClosePreRenewal(myStandardFirePol, false, myStandardFirePol.standardFire.getPolicyNumber());
    }

    @Test()
    private void testIssueStandardIMPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);

        // FPP
        // Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip1.setAdditionalInterests(loc2Bldg1AdditionalInterests);

        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myStandardIMPol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withPolTermLengthDays(50)
                .withInsFirstLastName("SIMRenew", "Quote")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        loginAndClosePreRenewal(myStandardIMPol, true, myStandardIMPol.standardInlandMarine.getPolicyNumber());
    }

    @Test()
    private void testIssueUmbrellaPolRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages();
        coverages.setLiability(LiabilityLimit.CSL300K);
        coverages.setUninsured(true);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);

        SquirePersonalAuto sAuto = new SquirePersonalAuto();
        sAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = sAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myUmbrellaPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(80)
                .withInsFirstLastName("SQ", "Renew")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myUmbrellaPol.squireUmbrellaInfo = squireUmbrellaInfo;
        myUmbrellaPol.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

        loginAndClosePreRenewal(myUmbrellaPol, false, myUmbrellaPol.squire.getPolicyNumber());
        guidewireHelpers.logout();
        loginAndClosePreRenewal(myUmbrellaPol, false, myUmbrellaPol.squireUmbrellaInfo.getPolicyNumber());
    }

    @Test()
    private void testIssueStandardLiabilityPolRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myStandardLiabPol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Fire", "Renew")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPolTermLengthDays(50)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

//		this.myStandardLiabPol = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withStandardFirePolicyUsedForStandardLiability(myStandardLiabPol, true)
//				.withPolTermLengthDays(50)
//				.withPaymentPlanType(PaymentPlanType.Quarterly)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);

        myStandardLiabPol.standardLiability = myStandardLiability;
        myStandardLiabPol.lineSelection.add(LineSelection.StandardLiabilityPL);
        myStandardLiabPol.standardLiability.setPolTermLengthDays(50);
        myStandardLiabPol.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);
        guidewireHelpers.logout();
        loginAndClosePreRenewal(myStandardLiabPol, true, myStandardLiabPol.standardLiability.getPolicyNumber());
    }

    private void loginAndClosePreRenewal(GeneratePolicy pol, boolean Day50Renewal, String policyNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), policyNumber);
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        if (Day50Renewal && (pol.productType.equals(ProductLineType.StandardFire) || pol.productType.equals(ProductLineType.StandardLiability) || pol.productType.equals(ProductLineType.StandardIM))) {
            System.out.println("Renewal transaction completed!");
        } else {
            renewal.waitForPreRenewalDirections();

            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            preRenewalPage.closePreRenewalExplanations(pol);
        }

        PolicySummary policySummary = new PolicySummary(driver);
        boolean testFailed = false;
        String errorMessage = "";
        if (Day50Renewal) {
            if (!policySummary.isTransactionComplete(TransactionType.Renewal, policyNumber.replace("-", "") + "-0003")) {
                testFailed = true;
                errorMessage = errorMessage + "Expected Current Transaction : is not displayed. \n";
            }
        } else {
            policySummary.clickPendingTransaction(TransactionType.Renewal);

            new GuidewireHelpers(driver).editPolicyTransaction();
            renewPolicy(pol, Day50Renewal, policyNumber);
        }

        if (testFailed) {
            Assert.fail(errorMessage);
        }

    }

    private void renewPolicy(GeneratePolicy pol, boolean Day50Renewal, String policyNumber) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            riskAnaysis.handleBlockSubmit(pol);
            SideMenuPC sideMenuStuff = new SideMenuPC(driver);
            sideMenuStuff.clickSideMenuQuote();
        }
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        try {
            StartRenewal renewal = new StartRenewal(driver);
            renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
        } catch (Exception e) {
            boolean testFailed = false;
            String errorMessage = "";
            GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
            if (!guidewireHelpers.errorMessagesExist() || ((guidewireHelpers.errorMessagesExist()) && (!guidewireHelpers.getFirstErrorMessage().contains("Renewal Code : Missing required field")))) {
                testFailed = true;
                errorMessage = errorMessage
                        + "Expected page validation : 'Renewal Code : Missing required field' is not displayed. /n";
            }

            GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
            guidewireHelpers.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

            if (!Day50Renewal) {
                if (!completePage.getJobCompleteMessage().contains("Your Renewal (#" + policyNumber.replace("-", "")
                        + "-0003) is in the process of being renewed.")) {
                    testFailed = true;
                    errorMessage = errorMessage + "Expected message : 'Your Renewal (#" + policyNumber.replace("-", "")
                            + "-0003) has been escalated' is not displayed. \n";
                }
            }

            if (!completePage.getViewYourPolicyLinkText().contains("View your policy")) {
                testFailed = true;
                errorMessage = errorMessage + "Expected message : 'View Your Policy...' is not displayed. \n";
            }

            if (!completePage.getViewCurrentJobText().contains("View your renewal")) {
                testFailed = true;
                errorMessage = errorMessage + "Expected message : 'View your renewal...' is not displayed. \n";
            }

            if (!completePage.isViewDesktopLinkExists()) {
                testFailed = true;
                errorMessage = errorMessage + "Expected message : 'View Desktop' is not displayed. \n";
            }

            String jobLevel = "";
            if (Day50Renewal) {
                jobLevel = "Renewal (Submitted)";

            } else {
                jobLevel = "Renewal (Renewing)";

            }
            if (!completePage.getCurrentJobLevel().contains(jobLevel)) {
                testFailed = true;
                errorMessage = errorMessage + "Expected Job Level : '" + jobLevel + "' is not displayed. \n";
            }

            completePage.clickViewYourPolicy();
            PolicySummary summaryPage = new PolicySummary(driver);

            if (!Day50Renewal
                    && !summaryPage.getPendingPolicyTransactionByColumnName(TransactionType.Renewal.getValue(), "Status")
                    .contains("Renewing")) {
                testFailed = true;
                errorMessage = errorMessage + "Expected Pending Transaction status is not displayed. \n";
            }

            if (testFailed)
                Assert.fail(errorMessage);

        }
    }
}
