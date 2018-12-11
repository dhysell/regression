package regression.r2.noclock.policycenter.rewrite.subgroup4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
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
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : PL - Issue Rewrite
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Aug 24, 2016
 */
public class TestPLIssueRewite extends BaseTest {
    private GeneratePolicy mySQPolicyObjPL = null;
    private GeneratePolicy myStandardFirePol = null;
    private GeneratePolicy myStandardIMPol = null;
    private Underwriters uw;


    private WebDriver driver;

    @Test()
    public void testRewrite() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        System.out.println("\n***********************************");
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (dayOfMonth % 2 == 0) {
            if (guidewireHelpers.getRandBoolean()) {
                System.out.println("RUNNING TEST | testIssueSquirePolicy");
                System.out.println("***********************************\n");
                testIssueSquirePolicy();
            } else {
                System.out.println("RUNNING TEST | testIssueStandardFirePolicy");
                System.out.println("***********************************\n");
                testIssueStandardFirePolicy();
            }
        } else {
            if (guidewireHelpers.getRandBoolean()) {
                System.out.println("RUNNING TEST | testIssueStandardIMPolicy");
                System.out.println("***********************************\n");
                testIssueStandardIMPolicy();
            } else {
                System.out.println("RUNNING TEST | testIssuePersonalUmbrellaPolicy");
                System.out.println("***********************************\n");
                testIssuePersonalUmbrellaPolicy();
            }
        }
    }


    @Test(enabled = false)
    private void testIssueSquirePolicy() throws Exception {


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

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Issue", "Rewrite")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);
        testCancelSquirePolicy(mySQPolicyObjPL.squire.getPolicyNumber());

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTerm(mySQPolicyObjPL);
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(mySQPolicyObjPL.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Rewrite, "") == null) {
            Assert.fail("Rewrite is not done!!!");
        }
    }

    @Test(enabled = false)
    private void testIssueStandardFirePolicy() throws Exception {

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
                .withInsFirstLastName("Issue", "Rewrite")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);


        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePol.accountNumber);
        testCancelSquirePolicy(myStandardFirePol.standardFire.getPolicyNumber());
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTerm(myStandardFirePol);
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(myStandardFirePol.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Rewrite, "") == null) {
            Assert.fail("Rewrite is not done!!!");
        }

    }

    @Test(enabled = false)
    private void testIssueStandardIMPolicy() throws Exception {


        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);

        // FPP
        //Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        imFarmEquip1.setAdditionalInterests(loc2Bldg1AdditionalInterests);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myStandardIMPol = new GeneratePolicy.Builder(driver)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withInsFirstLastName("Issue", "Rewrite")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);


        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardIMPol.accountNumber);
        testCancelSquirePolicy(myStandardIMPol.standardInlandMarine.getPolicyNumber());
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.rewriteFullTerm(myStandardIMPol);
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(myStandardIMPol.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Rewrite, "") == null) {
            Assert.fail("Rewrite is not done!!!");
        }

    }

    @Test(enabled = false)
    private void testIssuePersonalUmbrellaPolicy() throws Exception {


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

        GeneratePolicy myumbrellaSquire = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Issue", "Rewrite")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myumbrellaSquire.squireUmbrellaInfo = squireUmbrellaInfo;
        myumbrellaSquire.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);


        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myumbrellaSquire.squireUmbrellaInfo.getPolicyNumber());
        testCancelSquirePolicy(myumbrellaSquire.squireUmbrellaInfo.getPolicyNumber());
        StartRewrite rewritePage = new StartRewrite(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        rewritePage.rewriteFullTermGuts(myumbrellaSquire);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.approveAll();
        rewritePage.clickIssuePolicy();
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myumbrellaSquire.squireUmbrellaInfo.getPolicyNumber());
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Rewrite, "") == null) {
            Assert.fail("Rewrite is not done!!!");
        }
    }

    private void testCancelSquirePolicy(String policyNumber) {
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "Testing Purpose", currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(policyNumber);
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, "Testing Purpose") == null) {
            Assert.fail("Cancellation is not done!!!");
        }
    }

}
