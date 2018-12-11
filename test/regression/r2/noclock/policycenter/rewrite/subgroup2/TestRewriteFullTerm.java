/**
 *
 */
package regression.r2.noclock.policycenter.rewrite.subgroup2;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardInlandMarine;
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
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author rvanama
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Aug 15, 2016
 */
@QuarantineClass
public class TestRewriteFullTerm extends BaseTest {
    private GeneratePolicy SquPolicy = null;
    private GeneratePolicy StdFrPolicy = null;
    private GeneratePolicy StdInlndMarPolicy = null;
    private GeneratePolicy PumPolicy = null;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        SquPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Auto", "RewriteFull")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), SquPolicy.accountNumber);
        testCancelPolicy(SquPolicy.squire.getPolicyNumber());
        testRewriteFullTermExists(SquPolicy.squire.getPolicyNumber());
        testRewriteFullTerm(SquPolicy);
    }

    @Test()
    private void testIssueStandardIMPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        ArrayList<InlandMarine> inlandMarineCoverageSelection = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection.add(InlandMarine.FarmEquipment);

        // FPP
        // Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip1.setAdditionalInterests(loc2Bldg1AdditionalInterests);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        StdInlndMarPolicy = new GeneratePolicy.Builder(driver)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withInsFirstLastName("StandardIM", "RewriteFulltm")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), StdInlndMarPolicy.accountNumber);
        testCancelPolicy(StdInlndMarPolicy.standardInlandMarine.getPolicyNumber());
        testRewriteFullTermExists(StdInlndMarPolicy.standardInlandMarine.getPolicyNumber());
        testRewriteFullTerm(StdInlndMarPolicy);
    }

    @Test()
    private void testIssueStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(25);
        locationsList.add(propertyLocation);

        StdFrPolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("StandardFire", "RewriteFulltm")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), StdFrPolicy.accountNumber);

        testCancelPolicy(StdFrPolicy.standardFire.getPolicyNumber());
        testRewriteFullTermExists(StdFrPolicy.standardFire.getPolicyNumber());
        testRewriteFullTerm(StdFrPolicy);
    }

    @Test()
    private void testIssuePersonalUmbrellaPolicy() throws Exception {
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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        PumPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "RewriteFullTerm")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        PumPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        PumPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), PumPolicy.squireUmbrellaInfo.getPolicyNumber());
        testCancelPolicy(PumPolicy.squireUmbrellaInfo.getPolicyNumber());
        testRewriteFullTermExists(PumPolicy.squireUmbrellaInfo.getPolicyNumber());
        testRewriteFullTerm(PumPolicy);
    }


    private void testCancelPolicy(String accountNumber) {

        PolicySummary summary = new PolicySummary(driver);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(accountNumber);
        String errorMessage = "";
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null) {
            errorMessage = "Cancellation is not success!!!";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }


    }

    private void testRewriteFullTermExists(String polNumber) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), polNumber);

        // rewriteFullTerm available when cancel date is effective date.
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();

        if (!policyMenu.checkRewriteFullTermExist()) {
            throw new Exception("Rewrite FullTerm not available after cancellation when effective date = cancel date.");
        }

    }


    private void testRewriteFullTerm(GeneratePolicy pol) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), guidewireHelpers.getPolicyNumber(pol));
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTerm(pol);
        testRenewalRateDate(pol, TransactionType.Rewrite);
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement: Rate Date must match the effective Date
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20CancelReinstateRewrite%20-%20Rewrite%20All%20Together.xlsx">Link Text</a>
     * @Description
     * @DATE Jun 6, 2017
     */
    private void testRenewalRateDate(GeneratePolicy myPol, TransactionType transactionType) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPol.underwriterInfo.getUnderwriterUserName(), myPol.underwriterInfo.getUnderwriterPassword(), new GuidewireHelpers(driver).getPolicyNumber(myPol));
        PolicySummary summary = new PolicySummary(driver);
        Date rewriteEffectiveDate = summary.getCompletedTransactionEffectiveDate(transactionType);
        summary.clickCompletedTransactionByType(transactionType);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo pInfo = new GenericWorkorderPolicyInfo(driver);
        Date rateDate = pInfo.getPolicyInfoRateDate();
        if (!rateDate.equals(rewriteEffectiveDate)) {
            Assert.fail(transactionType.getValue() + " should have the Policy Rate Date be the same as the " + transactionType.getValue() + " Effective Date.");
        }
    }

}
