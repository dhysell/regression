package regression.r2.noclock.policycenter.renewaltransition.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US9438: Common - Disable renew from actions menu before 80 days from renewal
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Feb 15, 2017
 */
public class TestPLActionRenewalOptionAvailability3 extends BaseTest {
    private GeneratePolicy mySquirePolObjAt80;
    private Underwriters uw;

    private WebDriver driver;

//    @Test()
//    public void testIssueSquirePolicyBefore80Days() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        // Farm Equipment
//        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
//                "Manly Farm Equipment", 1000);
//        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
//        farmEquip.add(farmThing);
//        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm,
//                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
//        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
//        allFarmEquip.add(imFarmEquip);
//
//        // Rec Equip
//        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
//        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
//                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
//
//        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
//        imTypes.add(InlandMarine.FarmEquipment);
//        imTypes.add(InlandMarine.RecreationalEquipment);
//
//        SquireLiability myLiab = new SquireLiability();
//        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
//
//        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.HarvestersHeaders);
//
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//        locToAdd.setPlNumAcres(11);
//        locationsList.add(locToAdd);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = myLiab;
//        myPropertyAndLiability.squireFPP = squireFPP;
//
//        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
//        myInlandMarine.recEquipment_PL_IM = recVehicle;
//        myInlandMarine.farmEquipment = allFarmEquip;
//        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
//
//        Squire mySquire = new Squire(SquireEligibility.Country);
//        mySquire.inlandMarine = myInlandMarine;
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//        mySquirePolObjBefore80 = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
//                .withInsFirstLastName("Renew", "Actions")
//                .withPolTermLengthDays(78)
//                .withPaymentPlanType(PaymentPlanType.Annual)
//                .withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.PolicyIssued);
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        //        // Login with UW
//        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePolObjBefore80.squire.getPolicyNumber());
//
//        //        PolicyMenu policyMenu = new PolicyMenu(driver);
//        if (!policyMenu.checkRenewPolicyOption()) {
//            Assert.fail("Renew Policy option does not exists for less than 80 days");
//        }
//        //
//    }
//
//    @Test()
//    public void testIssueSquirePolicyAfter80Days() throws Exception {
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        SquireLiability myLiab = new SquireLiability();
//        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
//
//        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//        locToAdd.setPlNumAcres(11);
//        locationsList.add(locToAdd);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = myLiab;
//
//        Squire mySquire = new Squire(SquireEligibility.City);
//        mySquire.squirePA = new SquirePersonalAuto();
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//        mySquirePolObjAfter80 = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
//                .withInsFirstLastName("Renew", "Actions")
//                .withPolTermLengthDays(88)
//                .withPaymentPlanType(PaymentPlanType.Annual)
//                .withDownPaymentType(PaymentType.Cash)
//                .build(GeneratePolicyType.PolicyIssued);
//        Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        //        // Login with UW
//        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePolObjAfter80.squire.getPolicyNumber());
//
//        //        PolicyMenu policyMenu = new PolicyMenu(driver);
//        if (policyMenu.checkRenewPolicyOption()) {
//            Assert.fail("Renew Policy option exists for more than 80 days");
//        }
//        //    }

    @Test()
    public void testIssueSquirePolicyAt80Days() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

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

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquirePolObjAt80 = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Renew", "Actions")
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        new GuidewireHelpers(driver).logout();

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePolObjAt80.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        if (!policyMenu.checkRenewPolicyOption()) {
            Assert.fail("Renew Policy option does not exists at 80 days");
        }
    }
}
