package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReasonNotTaken;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US9182: [Part II] Common - Copy Submission permission
 * @Description : 1. Check agent and UW permissions for copy submissions when subject job is quoted, not taken and issued
 * 2. policy was bound and issued then expired. If user tries to copy submission using the same effective date/expiration date
 * as the term that was expired, user will get the validation message
 * 3. PL UW Assistant should not have the permissions to create submission, bind submissions, or copy submission.
 * However, they should have the editMembershipDues permission.
 * @DATE Dec 01, 2016
 */
@QuarantineClass
public class TestCopySubmissionPermissions extends BaseTest {
    private GeneratePolicy myPolicyObj;
    private GeneratePolicy notTakenPolicyObj;
    private Underwriters uw;
    private GeneratePolicy myPolicyObjPL;
    private GeneratePolicy polToReturn;
    private GeneratePolicy myPolicyObjUW;

    private WebDriver driver;

    @Test
    public void copySubmissionQuickQuote() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.random());
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Driverslicense")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);
    }

    // copy submission with UW after QuickQuote
    @Test(dependsOnMethods = {"copySubmissionQuickQuote"})
    public void copySubmissionWithoutNotTakenPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        if (actions.copySubmissionExists())
            Assert.fail("Copy Submission option should not exists on an account that already has an open policy transaction for that policy type");
    }

    // copy submission with agent after QuickQuote
    @Test(dependsOnMethods = {"copySubmissionWithoutNotTakenPolicy"})
    public void copySubmissionWithoutNotTakenPolicyByAgent() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        if (actions.copySubmissionExists())
            Assert.fail("Copy Submission option should not exists on an account that already has an open policy transaction for that policy type");
    }

    // Issue the policy
    @Test(dependsOnMethods = {"copySubmissionWithoutNotTakenPolicy"})
    public void issuePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    // copy submission with UW after issuance
    @Test(dependsOnMethods = {"issuePolicy"})
    public void copySubmissionIssued() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        if (actions.copySubmissionExists())
            Assert.fail("Copy Submission option should not exists on an account that has an open policy for that policy type");
    }

    // copy submission with Agent after issuance
    @Test(dependsOnMethods = {"copySubmissionIssued"})
    public void copySubmissionIssuedByAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        if (actions.copySubmissionExists())
            Assert.fail("Copy Submission option should not exists on an account that has an open policy for that policy type");
    }

    //PL UW Assistant should not have the permissions to create submission, bind submissions, or copy submission. 
    // However, they should have the editMembershipDues permission. 
    @Test
    public void copySubmissionNotTakenQuickQuote() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.random());
        mySquire.squirePA = new SquirePersonalAuto();

        notTakenPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Driverslicense")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);
    }

    // mark policy as not taken
    @Test(dependsOnMethods = {"copySubmissionNotTakenQuickQuote"})
    public void policyNotTaken() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(notTakenPolicyObj.agentInfo.getAgentUserName(), notTakenPolicyObj.agentInfo.getAgentPassword(), notTakenPolicyObj.accountNumber);
        GenericWorkorderPolicyInfo info = new GenericWorkorderPolicyInfo(driver);
        info.clickEditPolicyTransaction();
        GenericWorkorder generic = new GenericWorkorder(driver);
        generic.clickNotTaken();

        generic.setReasonNotTakenCode(ReasonNotTaken.PricedTooHigh);
        generic.setNotTakenComments("They said Geico is better!");
        generic.clickButtonNotTaken();
    }

    // copy submission as agent
    @Test(dependsOnMethods = {"policyNotTaken"})
    public void copySubmissionAsAgent() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(notTakenPolicyObj.agentInfo.getAgentUserName(), notTakenPolicyObj.agentInfo.getAgentPassword(), notTakenPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_CopySubmission();
    }

    // copy submission as PL UW Assistant
    @Test(dependsOnMethods = {"policyNotTaken"})
    public void copySubmissionAsUWAssistant() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission("spayton", "gw", notTakenPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        if (actions.copySubmissionExists())
            Assert.fail("Copy Submission option should not exists for PL UW Assistant");
    }


    @Test
    public void testGenerateFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test" + StringsUtils.generateRandomNumberDigits(8), "Comp",
                AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
            {
                this.setNewContact(CreateNew.Create_New_Always);
            }
        });

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Driverslicense")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withANIList(listOfANIs)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateFullApp"})
    public void validateUWAssistantRoles() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login("spayton", "gw");

        String errormessage = "";

        //Verify New Submission Permission
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        if (menuPolicy.newSubmissionExists())
            errormessage = errormessage + "UW Assistant should not have permission to create New Submission";
        new GuidewireHelpers(driver).logout();

        //verify Bind Application Permission
        new Login(driver).loginAndSearchSubmission("spayton", myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);

        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        paymentPage.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);
        GenericWorkorder workorder = new GenericWorkorder(driver);

        if (workorder.SubmitOptionsButtonExists())
            errormessage = errormessage + "For UW Assistant Bind Options button should not exist";

        //verify PL UW Assistant memebersh
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        sideMenu.clickSideMenuPolicyInfo();

        if (polInfo.checkMembershipDuesAddButtonDisabled())
            errormessage = errormessage + "UW Assistant should have permission to Add Membership Dues permissions";

        if (polInfo.checkMembershipDuesRemoveButtonDisabled(myPolicyObjPL.aniList.get(0).getPersonFirstName()))
            errormessage = errormessage + "UW Assistant should have permission to Remove Membership Dues";

        if (errormessage != "")
            Assert.fail(errormessage);

    }

    //policy was bound and issued then expired. If user tries to copy submission using the same effective date/expiration date 
    // as the term that was expired, user will get the validation message
    @Test
    public void testGenerateSquireAutoExpiredPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -367);

        ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
        lines.add(LineSelection.PersonalAutoLinePL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Test", "Auto")
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoExpiredPolicy"})
    public void testCopySubmissionForExpiredPolicyAsAgent() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(polToReturn.agentInfo.getAgentUserName(), polToReturn.agentInfo.getAgentPassword(), polToReturn.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_CopySubmission();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.clickNext();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireAutoFullTo(false);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", polToReturn.squire.getEffectiveDate()));
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		new GuidewireHelpers(driver).clickNext();
        ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
        if (!error.areThereErrorMessages())
            Assert.fail("Period dates overlap an existing bound period error should display");


    }

    //same above scenario with UW role
    @Test
    public void testGenerateSquireAutoExpiredPolicyUW() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -367);

        ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
        lines.add(LineSelection.PersonalAutoLinePL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjUW = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Test", "Auto")
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoExpiredPolicyUW"})
    public void testCopySubmissionForExpiredPolicyAsUW() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjUW.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_CopySubmission();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.clickNext();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireAutoFullTo(false);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObjUW.squire.getEffectiveDate()));
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        new GuidewireHelpers(driver).clickNext();
        ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
        if (!error.areThereErrorMessages())
            Assert.fail("Period dates overlap an existing bound period error should display");

    }
}
