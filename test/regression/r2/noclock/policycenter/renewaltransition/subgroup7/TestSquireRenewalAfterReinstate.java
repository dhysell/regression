package regression.r2.noclock.policycenter.renewaltransition.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4364 : Renewal can't finish after Reinstate, DE4731: Renewal quote is giving nullpointerException:null
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jan 13, 2017
 */
@QuarantineClass
public class TestSquireRenewalAfterReinstate extends BaseTest {
    private GeneratePolicy myPolicyObjPL = null;
    private Underwriters uw;
    private GeneratePolicy mySQPolObj;

    private WebDriver driver;

    // The below 5 tests are for completing the renewal after reinstate

    @Test()
    public void testIssueSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePersonalAutoCoverages sqpaCoverages = new SquirePersonalAutoCoverages(LiabilityLimit.ThreeHundredHigh,
                MedicalLimit.TenK, true, UninsuredLimit.ThreeHundred, false, UnderinsuredLimit.ThreeHundred);
        sqpaCoverages.setAccidentalDeath(true);

        // Vehicle List
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(sqpaCoverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Reinstate", "Renewal")
                .withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withPolTermLengthDays(60)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePol"})
    private void testCancelCurrentPolicyTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        String errorMessage = "";

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObjPL.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(myPolicyObjPL.accountNumber);

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null) {
            errorMessage = errorMessage + "Cancellation is not done for current term !!!\n";
        }

    }

    @Test(dependsOnMethods = {"testCancelCurrentPolicyTerm"})
    public void testReinstatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        StartReinstate reinstatePolicy = new StartReinstate(driver);

        reinstatePolicy.setReinstateReason("Payment received");
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.setDescription("Testing purpose");
        reinstatePolicy.quoteAndIssue();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testReinstatePolicy"})
    private void testRunBatchProcess() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        // run the batch job to get renewal job
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);
    }

    @Test(dependsOnMethods = {"testRunBatchProcess"})
    private void testRenewalAfterReInstate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObjPL.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObjPL);


        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

    // The below tests are for DE4731: Renewal quote is giving nullpointerException:null
    // After Renewal started, cancelled the policy and Reinstated

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement :DE4731: Renewal quote is giving nullpointerException:null
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Feb 4, 2017
     */
    @Test()
    private void TestIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePersonalAutoCoverages sqpaCoverages = new SquirePersonalAutoCoverages(LiabilityLimit.ThreeHundredHigh,
                MedicalLimit.TenK, true, UninsuredLimit.ThreeHundred, false, UnderinsuredLimit.ThreeHundred);
        sqpaCoverages.setAccidentalDeath(true);

        // Vehicle List
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(sqpaCoverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;


        mySQPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Renewal", "Reinstate")
                .withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withPolTermLengthDays(79)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);


        // run the batch job to get renewal job
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Policy_Renewal_Start);
    }

    @Test(dependsOnMethods = {"TestIssueSquirePolicy"})
    private void testCancelReinstatePolicyTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        String errorMessage = "";

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySQPolObj.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(mySQPolObj.accountNumber);

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null) {
            errorMessage = errorMessage + "Cancellation is not done for current term !!!\n";
        }

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        StartReinstate reinstatePolicy = new StartReinstate(driver);

        reinstatePolicy.setReinstateReason("Payment received");
        cancelPol.setDescription("Testing purpose");
        reinstatePolicy.quoteAndIssue();


        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testCancelReinstatePolicyTerm"})
    public void testQuoteRenewalTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySQPolObj.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, mySQPolObj);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

}
