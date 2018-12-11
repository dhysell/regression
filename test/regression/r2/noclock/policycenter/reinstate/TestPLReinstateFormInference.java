package regression.r2.noclock.policycenter.reinstate;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
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
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US4787: PL - Reinstate Policy
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jun 24, 2016
 */
public class TestPLReinstateFormInference extends BaseTest {
    private GeneratePolicy mySQPolicyObjPL = null;
    private GeneratePolicy myStandardFirePol;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testCancelSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
                ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        prop.setPolicyLocationBuildingAdditionalInterestArrayList(loc2Bldg1AdditionalInterests);
        locOnePropertyList.add(prop);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Reinstate")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);
        testCancelSquirePolicy(mySQPolicyObjPL.accountNumber);
    }

    private void testCancelSquirePolicy(String accountNumber) {
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(accountNumber);
        String errorMessage = "";
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not done!!!";

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testCancelSquirePolicy"})
    public void testReinstatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        boolean testFailed = false;
        String errorMessage = "";
        StartReinstate reinstatePolicy = new StartReinstate(driver);

        if (!reinstatePolicy.getStartReinstatementEffectiveDate().equals(DateUtils.dateFormatAsString("MM/dd/yyyy", mySQPolicyObjPL.squire.getEffectiveDate()))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Effective is not displayed\n ";
        }

        if (!reinstatePolicy.getStartReinstatementExpirationDate().equals(DateUtils.dateFormatAsString("MM/dd/yyyy", mySQPolicyObjPL.squire.getExpirationDate()))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Expiration is not displayed\n ";
        }

        String currentDate = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd/yyyy");

        if (!reinstatePolicy.getStartReinstatementRateAsOfDate().contains(currentDate)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Rate As of Date is not displayed\n ";
        }
        reinstatePolicy.setReinstateReason("Payment received");
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.setDescription("Testing purpose");
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Reinstate_Job_ReinstateNoticeUW);

        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Reinstate_Job_ReinstateNoticeAdditionalInterestUW);

        reinstatePolicy.quoteAndIssue();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testFailed = true;
            errorMessage = errorMessage + "ERROR: Documents Policy - In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }

    @Test()
    private void testCancelStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
                ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        prop.setPolicyLocationBuildingAdditionalInterestArrayList(loc2Bldg1AdditionalInterests);
        locOnePropertyList.add(prop);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        myStandardFirePol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("Test", "Reinstate")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePol.accountNumber);
        testCancelSquirePolicy(myStandardFirePol.accountNumber);
    }

    @Test(dependsOnMethods = {"testCancelStandardFirePolicy"})
    private void testReinstateStandardIMPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePol.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        boolean testFailed = false;
        String errorMessage = "";
        StartReinstate reinstatePolicy = new StartReinstate(driver);

        if (!reinstatePolicy.getStartReinstatementEffectiveDate().equals(DateUtils.dateFormatAsString("MM/dd/yyyy", myStandardFirePol.standardFire.getEffectiveDate()))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Effective is not displayed\n ";
        }

        if (!reinstatePolicy.getStartReinstatementExpirationDate().equals(DateUtils.dateFormatAsString("MM/dd/yyyy", myStandardFirePol.standardFire.getExpirationDate()))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Expiration is not displayed\n ";
        }

        String currentDate = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd/yyyy");

        if (!reinstatePolicy.getStartReinstatementRateAsOfDate().contains(currentDate)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Reinstate Policy  - Rate As of Date is not displayed\n ";
        }
        reinstatePolicy.setReinstateReason("Payment received");
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.setDescription("Testing purpose");
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Reinstate_Job_ReinstateNoticeUW);

        reinstatePolicy.quoteAndIssue();

        new GuidewireHelpers(driver).logout();


        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testFailed = true;
            errorMessage = errorMessage + "ERROR: Documents Policy - In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        if (testFailed) {
            Assert.fail(errorMessage);
        }
    }
}
