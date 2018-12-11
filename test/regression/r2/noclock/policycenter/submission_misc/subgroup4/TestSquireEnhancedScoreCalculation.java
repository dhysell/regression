package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Description : Script will generate Squire Policy, Order CBR Report and
 * validate enhanced score for Insured Age under 55, Over 55, No
 * Hit and 0 Credit Score scenarios US5447 - [Part II] PL -
 * Enhanced score calculation at new business
 * @DATE May 16, 2016
 */
public class TestSquireEnhancedScoreCalculation extends BaseTest {

    private GeneratePolicy cbrPolicy = null;
    double enhancedScore = 0.0;
    private Agents agentInfo;
    private Underwriters uw;

    private WebDriver driver;

    private GeneratePolicy generatePolicy(GeneratePolicyType quoteType, boolean person, boolean prefillPersonal,
                                          boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty)
            throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
        lines.add(LineSelection.PersonalAutoLinePL);
        this.agentInfo = AgentsHelper.getRandomAgent();
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK);
        coverages.setUninsuredLimit(UninsuredLimit.OneHundred);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("Test", "EnhanedScore")
                .withAgent(agentInfo)
                .withPolOrgType(OrganizationType.Individual).build(quoteType);
        return polToReturn;
    }

    @Test
    public void testGenerate_CBR() throws Exception {

        this.cbrPolicy = generatePolicy(GeneratePolicyType.FullApp, true, false, false, true, false, false, false);
        createAddtionalNamedInsured();
    }

    private void createAddtionalNamedInsured() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.cbrPolicy.agentInfo.getAgentUserName(), this.cbrPolicy.agentInfo.getAgentPassword(), this.cbrPolicy.accountNumber);

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        addAdditionalNamedInsured(cbrPolicy.basicSearch, "PEGGY", "ABLUMHORST", "320 RONGLYN AVE", "IDAHO FALLS", State.Idaho, "834014039", AddressType.Home);
    }

    @Test(enabled = true, dependsOnMethods = {"testGenerate_CBR"})
    public void testCBR_ValidateOrderReportByUsers() throws Exception {

        int roundedEnhancedScore = 0;
        // UW supervisor
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.cbrPolicy.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        addAdditionalNamedInsured(cbrPolicy.basicSearch, "LORI", "ABLUMHORST", "320 RONGLYN AVE", "IDAHO FALLS", State.Idaho, "83401",
                AddressType.Home);

        // set DOB
        Date Dob = DateUtils.convertStringtoDate("7/8/1986", "MM/DD/YYYY");
        Date dob = Dob;
        additionalInsured.setDOB(DateUtils.dateFormatAsString("MM/dd/YYYY", dob));
        //		additionalInsured.setSSN("2"+StringsUtils.generateRandomNumberDigits(8));
        additionalInsured.setSSN(StringsUtils.getValidSSN());
        additionalInsured.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
        if (insScore.isEditInsuranceOrderDetailsDisplayed()) {
            insScore.clickEditInsuranceReport();
        }
        insScore.selectCreditReportIndividual("ABLUMHORST");
        insScore.clickOrderReport();

        Date cDOB = dob;
        Date currentSystemDate = DateUtils.getCenterDate(driver, gwclockhelpers.ApplicationOrCenter.PolicyCenter);
        int age = DateUtils.getDifferenceBetweenDates(currentSystemDate, cDOB, DateDifferenceOptions.Year);

        // check Insured's enhanced score under <55
        if (age <= 55 && insScore.getInsuranceStatus().equalsIgnoreCase("Credit report received with Reason code(s)")) {
            enhancedScore = (((56 - age) * 0.005) + 1) * insScore.getInsuranceScore();
            roundedEnhancedScore = (int) Math.round(enhancedScore);
            if (insScore.getEnhancedInsuranceScore() == roundedEnhancedScore) {
                System.out.println("Insured is under 55");
            } else {
                Assert.fail("Enhance score mismatch(Insured is under 55) : " + insScore.getEnhancedInsuranceScore());
            }
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        // Agent
        new Login(driver).loginAndSearchSubmission(this.cbrPolicy.agentInfo.getAgentUserName(),
                this.cbrPolicy.agentInfo.getAgentPassword(), this.cbrPolicy.accountNumber);
        sideMenu.clickSideMenuPLInsuranceScore();
        if (insScore.isEditInsuranceOrderDetailsDisplayed()) {
            Assert.fail("Agent will not able to reorder : " + this.cbrPolicy.agentInfo.getAgentUserName());
        }

        sideMenu.clickSideMenuRiskAnalysis();
        guidewireHelpers.logout();
    }

    @Test(enabled = true, dependsOnMethods = {"testCBR_ValidateOrderReportByUsers"})
    public void testCBR_ValidateEnhancedScore() throws Exception {

        // Insured is 55 and over
        Insured55andOver();

        // Insured Credit Score 0
        validationZeroScore();

        // For No Hit
        validationNoHit();

    }

    private void Insured55andOver() throws Exception {

        // UW supervisor
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.cbrPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        // Insured is over 55
        addAdditionalNamedInsured(cbrPolicy.basicSearch, "ROBERT", "ADLER", "25110 THE DRIVING LN", "CALDWELL", State.Idaho, "83607",
                AddressType.Home);

        // set DOB
        Date Dob = DateUtils.convertStringtoDate("3/5/1939", "MM/DD/YYYY");
        Date dob = Dob;

        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.setDOB(DateUtils.dateFormatAsString("MM/dd/YYYY", dob));
        additionalInsured.clickOK();

        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
        sideMenu.clickSideMenuPLInsuranceScore();
        if (insScore.isEditInsuranceOrderDetailsDisplayed()) {
            insScore.clickEditInsuranceReport();
            insScore.selectCreditReportIndividual("ADLER");
            insScore.clickOrderReport();
            insScore.clickCurrentInsuranceScoreGeneral();
        }

        Date currentSystemDate = DateUtils.getCenterDate(driver, gwclockhelpers.ApplicationOrCenter.PolicyCenter);
        int age = DateUtils.getDifferenceBetweenDates(currentSystemDate, dob, DateDifferenceOptions.Year);
        if (age >= 56) {
            if (insScore.getInsuranceScore() == insScore.getEnhancedInsuranceScore()) {
                System.out.println("Insured is 56 and over ");
            } else {
                Assert.fail("Enhance score mismatch(Insured is over 55) : " + insScore.getEnhancedInsuranceScore());
            }
        }
    }

    private void validationNoHit() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.cbrPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        addAdditionalNamedInsured(cbrPolicy.basicSearch, "RONNIE", "WILKIEWICZ", "320 RONGLYN AVE", "IDAHO FALLS", State.Idaho, "836511575",
                AddressType.Home);

        // set DOB
        Date Dob = DateUtils.convertStringtoDate("6/1/1964", "MM/DD/YYYY");
        Date dob = Dob;

        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.setDOB(DateUtils.dateFormatAsString("MM/dd/YYYY", dob));
        additionalInsured.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
        insScore.clickEditInsuranceReport();
        insScore.selectCreditReportIndividual("WILKIEWICZ");
        insScore.clickOrderReport();
        insScore.clickCurrentInsuranceScoreGeneral();

        if (insScore.getFlag().equals("N") && insScore.getEnhancedInsuranceScore() == 0) {
            System.out.println("Status M or N and Flag is N - Insured Enhanced Score is 0 and No Hit");
        } else {
            Assert.fail("LexisNexis Can't Score or No hit : " + insScore.getEnhancedInsuranceScore() + "Flag - "
                    + insScore.getFlag() + "Status - " + insScore.getInsuranceStatus());
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();

    }

    private void validationZeroScore() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.cbrPolicy.accountNumber);

        //GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        addAdditionalNamedInsured(cbrPolicy.basicSearch, "JOHN", "FLYNN", "928 DIXON ST", "BILLINGS", State.Montana, "591052312",
                AddressType.Home);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.setDOB("12/14/1984");
        additionalInsured.clickOK();

        // currently it is not required to enter membership due and will be removed once user story is ready

        //policyInfo.selectMembershipDuesCounty("JOHN", "Ada");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
        insScore.clickEditInsuranceReport();
        insScore.selectCreditReportIndividual("FLYNN");
        insScore.clickOrderReport();
        insScore.clickCurrentInsuranceScoreGeneral();


        if (insScore.getFlag().equals("I")) {
            if (insScore.getEnhancedInsuranceScore() == 0.0) {
                System.out.println("Status U and Flag I - No Score Received");
            } else {
                Assert.fail("Insured score is not 0 : " + insScore.getEnhancedInsuranceScore() + "Flag - "
                        + insScore.getFlag() + "Status - " + insScore.getInsuranceStatus());
            }
        }
    }

    private void addAdditionalNamedInsured(boolean basicSearch, String firstName, String lastName, String address1, String city, State state,
                                           String zip, AddressType atype) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address1, city, state, zip,
                CreateNew.Create_New_Always);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.selectAdditionalInsuredRelationship(RelationshipToInsured.Partner);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.setAddressListing("New...");
        additionalInsured.setAddressLine1(address1);
        additionalInsured.setContactEditAddressCity(city);
        additionalInsured.setContactEditAddressState(state);
        additionalInsured.setZipCode(zip);
        additionalInsured.selectAddressType(atype);
        additionalInsured.sendArbitraryKeys(Keys.TAB);
        additionalInsured.setSSN("5" + StringsUtils.generateRandomNumberDigits(8));
        additionalInsured.clickRelatedContactsTab();
        additionalInsured.clickContactDetailsTab();
        additionalInsured.clickOK();

        new GuidewireHelpers(driver).duplicateContacts();
        if (namedInsured.isSelectMatchingContactDisplayed()) {
            namedInsured.clickSelectMatchingContact();
            namedInsured.setReasonForContactChange("Testing");
            additionalInsured.clickOK();
        }

    }
}
