package regression.r2.noclock.policycenter.submission_misc.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement :DE4261, DE5068 Standard Liability -- Additional Interests from
 * Standard Fire are Editable and Should Not Be
 * @Description : Create Standard Fire policy with Additional Interest as Line
 * Holder, assign LH as payer under payer assignment screen and
 * verifying Additional Interest is added under Liability policy
 * and also checking LH can't be edited under liability policy,
 * checking for activity is generated and verifying the validation
 * message to removal of additional interest when LH removed from
 * Fire policy
 * @DATE Nov 17, 2016
 */
@QuarantineClass
public class TestStdLiabilityAdditionalInterest extends BaseTest {
    private GeneratePolicy myPolicyObject;
    private Underwriters uw;
    private String stdFirepolicy;

    private WebDriver driver;

    @Test
    public void testStandardLiabilityWithStdFire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        property.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locOnePropertyList.add(property);

        PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
        property2.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        locOnePropertyList.add(property2);
        PolicyLocation loc = new PolicyLocation(locOnePropertyList);
        loc.setPlNumResidence(2);
        locationsList.add(loc);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("Guy", "Stdfire")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

//		stdFireLiab_Liability_PolicyObj = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withAgent(myPolicyObject.agentInfo)
//				.withStandardFirePolicyUsedForStandardLiability(myPolicyObject, true)
//				.withInsPersonOrCompany(ContactSubType.Person)
//				.withPolOrgType(OrganizationType.Individual)
//				.build(GeneratePolicyType.FullApp);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testStandardLiabilityWithStdFire"})
    public void testVerifyStdLiabilityAdditionalInterestNotEditable() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(),
                myPolicyObject.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickAdditionalInterest();
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        String compName = myPolicyObject.standardFire.getLocationList().get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName();
        additionalInterests.addExistingFromStandardFire(compName);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsLink(compName);
        additionalInterests.checkIfLeinHolderPCRIsEditable();
        additionalInterests.clickReturnToCoverages();
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, compName);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        paymentPage.fillOutPaymentPage(myPolicyObject);
        GenericWorkorder workorder = new GenericWorkorder(driver);
        workorder.clickGenericWorkorderSubmitOptionsSubmit();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        stdFirepolicy = completePage.getPolicyNumber();
        new GuidewireHelpers(driver).logout();


        // Bind and Issue Liability policy
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        login.loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickCloseWorkSheet();
        sideMenu.clickSideMenuRiskAnalysis();
        risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuPayment();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.issuePolicy(IssuanceType.NoActionRequired);
        quote.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testVerifyStdLiabilityAdditionalInterestNotEditable"})
    public void verifyActivityTriggeredAfterPolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObject.accountNumber);

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        // add 10 days to current date
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 30);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("Fire policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        // Change Current Payer to Insured
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, "Insured", true, false);
        payerAssignment.setPayerAssignmentBillMembershipDues(myPolicyObject.pniContact.getFirstName(), true, false, "Insured");

        // Quote And Bind Policy Change
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
        change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

        // Validate Lien change on Standard Fire Activity Generated
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObject.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        if (!accountSummaryPage.getActivitySubject(1).contains("Lien change on Standard Fire")) {
            Assert.fail(driver.getCurrentUrl() + "Lien change on Standard Fire Activity not triggered");
        }

    }

    // Remove Additional Interest from Standard Fire policy and verifying
    // activity generated/validation message displayed under Liability policy
    // for removal of Addtional interest
    @Test(dependsOnMethods = {"verifyActivityTriggeredAfterPolicyChange"})
    public void testRemoveAdditionalInterestAndValidate() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdFirepolicy);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        // add 10 days to current date
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 30);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("Liability policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();
        // Quote Policy Change
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        boolean testFailed = false;
        String errorMessage = "";

        if (!risk.getValidationMessagesText().contains(
                "no longer exists as a payer on any of the Standard Fire policies on the account. The additional interest must be removed in order to quote/submit the policy.")) {
            testFailed = true;
            errorMessage = errorMessage
                    + "Section II: The additional interest no longer exists as a payer on any of the Standard Fire policies on the account. The additional interest must be removed in order to quote/submit the policy validation message should be displayed.";
        }
        if (testFailed) {
            Assert.fail(errorMessage);
        }

    }
}
