package regression.r2.noclock.policycenter.submission_misc.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

/*
 * DE7040 : created for Change Policy for Underwriter activity is not showing up
 */

/**
 * @Author skandibanda
 * @Requirement :DE4136: Issuance Error
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/69078107020">DE4136</a>
 * @Description : Issue Squire with I, II, III & IV, Standard Fire and Liability policies  and add policy change
 * and validate Cost Change Details Page for effective dates,annual premiums and amounts
 * @DATE Oct 18, 2016
 */
@QuarantineClass
public class TestStandardLiability extends BaseTest {
    private GeneratePolicy myPLPolicyobj;

    private WebDriver driver;

    @Test()
    public void testGenerateStandardLiability() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation());

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        myPLPolicyobj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
    }

    @Test(dependsOnMethods = {"testGenerateStandardLiability"})
    public void verifyIssuanceError() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(myPLPolicyobj.underwriterInfo.getUnderwriterUserName(), myPLPolicyobj.underwriterInfo.getUnderwriterPassword(), myPLPolicyobj.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickActivityCancel();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.approveAll();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sidemenu.clickSideMenuQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        quotePage.issuePolicy(IssuanceType.FollowedByPolicyChange);
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myPLPolicyobj.standardLiability.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickActivity("Change Policy for Underwriter");
        activityPopupPage.clickCloseWorkSheet();

        sidemenu.clickSideMenuPolicyReview();

        ErrorHandlingHelpers errorHandlingHelpers = new ErrorHandlingHelpers(driver);

        if (errorHandlingHelpers.errorExists())
            Assert.fail(errorHandlingHelpers.getErrorMessage() + "Error Exist");
    }

    @Test()
    private void testStandardLiabilityFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation location = new PolicyLocation();
        location.setPlNumAcres(12);
        location.setPlNumResidence(2);
        locationsList.add(location);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        GeneratePolicy standardliab = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("TestFA", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchSubmission(standardliab.agentInfo.getAgentUserName(), standardliab.agentInfo.getAgentPassword(), standardliab.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        String errorMessage = "";
        if (quote.getQuoteTotalMembershipDues() > 45) {
            errorMessage = errorMessage + "Membership Dues amount is not displayed correctly for PNI \n ";
        }
        Contact anotherPerson = new Contact("TestMem" + StringsUtils.generateRandomNumberDigits(9), "New", Gender.Female, DateUtils.convertStringtoDate("01/01/1985", "MM/dd/yyyy"));
        new GuidewireHelpers(driver).editPolicyTransaction();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.clickMemberShipDuesAddButton();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(standardliab.basicSearch, anotherPerson.getFirstName(), anotherPerson.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderPolicyInfoAdditionalNamedInsured aInsured = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        aInsured.setEditAdditionalNamedInsuredAddressListing(standardliab.pniContact.getAddress());
/*		sendArbitraryKeys(Keys.TAB);
		common.setAddress(standardliab.pniContact.getAddress().getLine1());
		common.setCity(standardliab.pniContact.getAddress().getCity());
		common.setState(standardliab.pniContact.getAddress().getState());
		common.setZip(standardliab.pniContact.getAddress().getZip());
		common.setAddressType(standardliab.pniContact.getAddress().getType());
				sendArbitraryKeys(Keys.TAB);
*/
        aInsured.clickOK();
        polInfo.setMembershipDues(anotherPerson.getFirstName(), true);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(standardliab);
        risk.handleBlockSubmit(standardliab);

        sideMenu.clickSideMenuQuote();
        if (quote.getQuoteTotalMembershipDues() < 45) {
            errorMessage = errorMessage + "Membership Dues amount is not displayed correctly after adding another Membership due \n ";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    public GeneratePolicy getStandardLiabilityPolicyObj() {
        return this.myPLPolicyobj;
    }
}
