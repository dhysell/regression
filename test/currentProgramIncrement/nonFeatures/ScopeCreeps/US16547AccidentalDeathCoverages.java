package currentProgramIncrement.nonFeatures.ScopeCreeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;

import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement US16547
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/256670055828">US16547</a>
 * @Description
 *
 * As an underwriter I want to be able to choose to have a policy member not be charged for Accidental Death (AD).
 *
 * Additional coverages on section III has option to check a box if AD is wanted on policy. This needs to add up all policy members (free & charged persons). Currently this isn't including the free minors but changing to include.
 * Need a box on policy members for each person that has ability to "exclude driver from AD coverage". The field reads "Accidental Death" and has a checkbox. Not available on minors that are free. The checkbox is automatically checked if Accidental Death coverage is chosen on section III. An underwriter I want to have ability to uncheck the box and remove AD from that policy member.
 *
 * Steps to get there:
 * Have a policy with sect 1 and 2 and 3.
 * On policy members list the parent/or any person that won't be a driver of the PNI as an additional insured on sect 2 only
 * Make sure AD coverage is selected on section III.
 * As UW choose to remove the AD charge on a policy member (NOTE: this is not limited to only the person that is an additional insured)
 *
 * Acceptance Criteria:
 * Should only be editable by role of underwriter. No county office users should have this field
 * When the "accidental death" coverage is chosen on section III, the quantity should be the total of all policy members with AD (included and charged)
 * All policy members should have the "Exclude Accidental Death" box . And defaults to not checked, If we want the policy member to be excluded, we check the checkbox.
 * When a policy member gets checked to Exclude AD coverage then the quantity total on section III should adjust accordingly
 * The line review, quote screen and cost change screens should all reflect the appropriate quantities for AD
 * Declarations should show AD for all persons, included and charged
 * @DATE November 28, 2018
 */
public class US16547AccidentalDeathCoverages extends BaseTest {

    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    private Contact majorPolicyMember;
    private Contact minorPolicyMember;

    private void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        majorPolicyMember = new Contact();
        majorPolicyMember.setRelationshipAB(null);
        minorPolicyMember = new Contact();
        minorPolicyMember.setRelationshipAB(null);
        minorPolicyMember.setDob(driver , DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -14));
        PolicyInfoAdditionalNamedInsured additionalNamedInsured = new PolicyInfoAdditionalNamedInsured(majorPolicyMember);
        PolicyInfoAdditionalNamedInsured additionalNamedInsured1 = new PolicyInfoAdditionalNamedInsured(minorPolicyMember);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withANIList(additionalNamedInsured,additionalNamedInsured1)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("AD", "Coverages")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);

    }

    @Test(enabled = true)
    public void verifyAccidentalDeathCoveragesCount() throws Exception {
        generatePolicy();
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderPolicyMembers household = new repository.pc.workorders.generic.GenericWorkorderPolicyMembers(driver);
        SoftAssert sf = new SoftAssert();
        
        verifyCheckBoxNotEditableWithAgentLogin(pcSideMenu, pcAccountSummaryPage, household, sf);

        verifyExcludeAccidentalDeathCheckBoxEditableForUW(pcSideMenu, pcAccountSummaryPage, household, sf);

        sf.assertAll();
    }

    private void verifyExcludeAccidentalDeathCheckBoxEditableForUW(SideMenuPC pcSideMenu, AccountSummaryPC pcAccountSummaryPage, repository.pc.workorders.generic.GenericWorkorderPolicyMembers household, SoftAssert sf) throws Exception {
        Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.clickAdditionalCoveragesTab();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalCoverages = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
        String count = additionalCoverages.setAccidentalDeath(true);

        sf.assertEquals(count, "2", "count is not 2 when Policy member is not opt out (as no minor count not included)");
        pcSideMenu.clickSideMenuHouseholdMembers();
        household.editHouseHoldMember(minorPolicyMember);
        sf.assertFalse (household.isExcludeAccidentalDeathExist() , "exclude Accidental death option not available for Minor");
        household.clickOK();
        household.editHouseHoldMember(majorPolicyMember);
        //opt out the AD coverage
        household.checkBoxExcludeAccidentalDeath(true);
        household.clickOK();
        pcSideMenu.clickSideMenuPACoverages();
        coveragePage.clickAdditionalCoveragesTab();
        count = additionalCoverages.getAccidentalDeathCount();
        sf.assertEquals(count, "1", "count is not 1 when Policy member is opt out on Coverages (as minor count not included)");
        pcSideMenu.clickSideMenuSquireLineReview();
        repository.pc.workorders.generic.GenericWorkorderLineReviewPL lineReview = new repository.pc.workorders.generic.GenericWorkorderLineReviewPL(driver);
        sf.assertEquals(lineReview.getLineReviewAccidentalDeath(), Double.valueOf("1"), "count is not 1 when Policy member is opt out On Line Review (as minor count not included)");
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);

        String accidentalDeath =quotePage.getSectionIICoveragesPremiumQuantity("Accidental Death");
        String accidentalDeathIncluded =quotePage.getSectionIICoveragesPremiumQuantity("Accidental Death (included)");
        sf.assertEquals(accidentalDeath, "1", "count is not 1 on Quote Page");
        sf.assertEquals(accidentalDeathIncluded, "1", "count is not 1  (as minor count not included) on Quote page");
    }

    private void verifyCheckBoxNotEditableWithAgentLogin(SideMenuPC pcSideMenu, AccountSummaryPC pcAccountSummaryPage, repository.pc.workorders.generic.GenericWorkorderPolicyMembers household, SoftAssert sf) throws Exception {
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickSideMenuHouseholdMembers();
        household.editHouseHoldMember(majorPolicyMember);
        sf.assertFalse (household.isExcludeAccidentalDeathExist() , "Check box is displaying for Agent But Shouldn't");
        new GuidewireHelpers(driver).logout();
    }

    private void generatePolicyWithPropertyAndAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        majorPolicyMember = new Contact();
        majorPolicyMember.setRelationshipAB(null);
        minorPolicyMember = new Contact();
        minorPolicyMember.setRelationshipAB(null);
        minorPolicyMember.setDob(driver , DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -14));

        PolicyInfoAdditionalNamedInsured additionalNamedInsured = new PolicyInfoAdditionalNamedInsured(majorPolicyMember);
        PolicyInfoAdditionalNamedInsured additionalNamedInsured1 = new PolicyInfoAdditionalNamedInsured(minorPolicyMember);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL , repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withANIList(additionalNamedInsured , additionalNamedInsured1)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("AD", "Coverages")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);

    }

    @Test(enabled = true)
    public void verifyAccidentalDeathCoveragesWithRemoveAndAddAutoLine() throws Exception {
        generatePolicyWithPropertyAndAuto();
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderPolicyMembers household = new repository.pc.workorders.generic.GenericWorkorderPolicyMembers(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers drivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers(driver);
        repository.pc.workorders.generic.GenericWorkorderVehicles vehicles = new repository.pc.workorders.generic.GenericWorkorderVehicles(driver);
        SoftAssert sf = new SoftAssert();

        Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);

        pcSideMenu.clickSideMenuPAVehicles();
        repository.pc.workorders.generic.GenericWorkorderVehicles vehicalTab = new repository.pc.workorders.generic.GenericWorkorderVehicles(driver);
        vehicalTab.removeAllVehicles();
        pcSideMenu.clickSideMenuPADrivers();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
        paDrivers.setCheckBoxInDriverTable(1);
        paDrivers.clickRemoveButton();
        pcSideMenu.clickSideMenuLineSelection();
        repository.pc.workorders.generic.GenericWorkorderLineSelection lineSelection = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(false);
        lineSelection.clickNext();
        pcSideMenu.clickSideMenuHouseholdMembers();
        household.editHouseHoldMember(minorPolicyMember);
        sf.assertFalse (household.isExcludeAccidentalDeathExist() , "exclude Accidental death option not available for not Auto policy");
        household.clickOK();
        household.editHouseHoldMember(majorPolicyMember);
        sf.assertFalse (household.isExcludeAccidentalDeathExist() , "exclude Accidental death option not available for not Auto policy");
        household.clickOK();
        pcSideMenu.clickSideMenuLineSelection();
        lineSelection.checkPersonalAutoLine(true);
        lineSelection.clickNext();
        pcSideMenu.clickSideMenuPADrivers();
        //DRIVERS
        drivers.fillOutDriversQQ(myPolicyObject);
        pcSideMenu.clickSideMenuPACoverages();
        //COVERAGES
        repository.pc.workorders.generic.GenericWorkorderSquirePACoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages(driver);
        coverages.fillOutSquireAutoCoverages(myPolicyObject);
        coverages.clickAdditionalCoveragesTab();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalCoverages = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
        String count = additionalCoverages.setAccidentalDeath(true);
        pcSideMenu.clickSideMenuPAVehicles();
        //VEHICLES
        vehicles.fillOutVehicles_QQ(myPolicyObject);
        pcSideMenu.clickSideMenuHouseholdMembers();
        household.editHouseHoldMember(minorPolicyMember);
        sf.assertFalse (household.isExcludeAccidentalDeathExist() , "exclude Accidental death option not available for Minor");
        household.clickOK();
        household.editHouseHoldMember(majorPolicyMember);
        //opt out the AD coverage
        household.checkBoxExcludeAccidentalDeath(true);
        household.clickOK();
        pcSideMenu.clickSideMenuPACoverages();
        coverages.clickAdditionalCoveragesTab();
        count = additionalCoverages.getAccidentalDeathCount();
        sf.assertEquals(count, "1", "count is not 1 when Policy member is opt out on Coverages");
        pcSideMenu.clickSideMenuSquireLineReview();
        repository.pc.workorders.generic.GenericWorkorderLineReviewPL lineReview = new repository.pc.workorders.generic.GenericWorkorderLineReviewPL(driver);
        sf.assertEquals(lineReview.getLineReviewAccidentalDeath(), Double.valueOf("1"), "count is not 1 when Policy member is opt out On Line Review");
        sf.assertAll();
    }



}
