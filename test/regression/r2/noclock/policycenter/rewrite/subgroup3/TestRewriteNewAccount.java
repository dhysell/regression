package regression.r2.noclock.policycenter.rewrite.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartNameChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.NamesHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * US13621: Don't force user to click all Risk Analysis tabs to Quote or Submit
 * This test will fail as the latest code is not in REGR environment.
 */

/**
 * @Author skandibanda
 * @Requirement : US9047 : [Part II] PL - Rewrite New Account
 * @Description -  Testing Rewrite New Account is only available if the old policy is cancelled
 * by home office w/ reason - Renumbering to another policy,Reason Explanation - Name change, Test UW and Supervisor Permissions to Rewrite New Account
 * @DATE Sep 02, 2016
 */
@QuarantineClass
public class TestRewriteNewAccount extends BaseTest {
    public GeneratePolicy myPolicyObj, squirePolicyObj, stdFirePolicyObj, otherPolicyObj, newPoliciicyObj, standardFirePolicy;
    private Agents agentInfo;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testGenerateSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        this.agentInfo = AgentsHelper.getRandomAgent();

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Account1", "Squire")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Rewrite New Account is only available if the old policy is cancelled with speficied reasons
    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    public void testRewriteNewAccount() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);

        testCancelPolicy(myPolicyObj.accountNumber);
        testIssueStandardFirePolicy();
        testRewriteNewAccountPolicyWithAgent(standardFirePolicy);
        testRewriteNewAccountPolicy(standardFirePolicy, myPolicyObj);
    }

    private void testIssueStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(2);
        locationsList.add(propertyLocation);

        this.agentInfo = AgentsHelper.getRandomAgent();

        standardFirePolicy = new GeneratePolicy.Builder(driver)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("StandardFire", "Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Cancel Policy with reasons
    private void testCancelPolicy(String accountNumber) {

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

    }

    //Rewrite New Account With Agent
    private void testRewriteNewAccountPolicyWithAgent(GeneratePolicy pol) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(pol.agentInfo.getAgentUserName(), pol.agentInfo.getAgentPassword(), pol.accountNumber);
        ActionsPC actions = new ActionsPC(driver);

        actions.click_Actions();
        if (actions.checkRewritePoliciesToThisAccountExists())
            Assert.fail("Rewrite Policies to this Account Action should not exists for agent.");

    }

    //Rewrite New Account with UW
    private void testRewriteNewAccountPolicy(GeneratePolicy stdPol, GeneratePolicy squirePol) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdPol.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(squirePol.accountNumber, stdPol.accountNumber);

        //Verify Expected activity displayed

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(squirePol.accountNumber);


        AccountSummaryPC summary = new AccountSummaryPC(driver);
        String currenActivitySubject = summary.getActivitySubject(1);
        String expectedActivitySubject = "Rewrite to new account " + stdPol.accountNumber + " from source account " + squirePol.accountNumber + " for policy #" + myPolicyObj.squire.getPolicyNumber();

        if (!expectedActivitySubject.equals(currenActivitySubject)) {
            Assert.fail("Expected Activity subject not displayed");
        }


        SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
        jobSearchPC.searchJobByAccountNumberSelectProduct(stdPol.accountNumber, ProductLineType.Squire);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		/*sideMenu.clickSideMenuPolicyInfo();

		ActivityPopup activityPopupPage = ActivityFactory.getActivityPopupPage();		
		activityPopupPage.clickCloseWorkSheet();


		sideMenu.clickSideMenuQualification();
		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireGeneralFullTo(false);
		qualificationPage.setSquireAutoFullTo(false);*/

        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);
        policyInfo.clickPolicyInfoPrimaryNamedInsured();

        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickUpdate();
        policyInfo.selectAddExistingOtherContactAdditionalInsured(squirePol.pniContact.getFirstName());
        GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
        ani.selectAdditionalInsuredRelationship(RelationshipToInsured.Friend);
        ani.selectAddtionalInsuredAddress(stdPol.pniContact.getAddress().getLine1());
        ani.clickUpdate();
        policyInfo.setMembershipDues(squirePol.pniContact.getFirstName(), true);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        int row = houseHold.getPolicyHouseholdMembersTableRow(stdPol.pniContact.getFirstName());
        houseHold.clickPolicyHouseHoldTableCell(row, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        AddressInfo address = new AddressInfo("6315 W YORK ST", "", "BOISE", State.Idaho, "837047573", CountyIdaho.Ada, "United States", AddressType.Home);
        householdMember.selectNotNewAddressListingIfNotExist(address);
        householdMember.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(stdPol);
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPACoverages();
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.clickUWIssuesTab();
        risk.approveAll_IncludingSpecial();

        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }


    //Rewrite New Account option wont exist when Policy cancelled with Other reasons
    @Test()
    public void testGenerateSquirePolicyNewAccount() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        this.agentInfo = AgentsHelper.getRandomAgent();

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Account1", "Squire")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testGenerateSquirePolicyNewAccount"})
    public void testRewriteNewAccountWithOtherReason() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);
        testCancelPolicyWithOtherReason(squirePolicyObj.accountNumber);

        testIssueStandardFirePolicyRewrite();
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirePolicyObj.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        ActionsPC actions = new ActionsPC(driver);

        actions.click_Actions();
        actions.click_RewritePoliciesToThisAccount();

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.setAccountAccountNumber(squirePolicyObj.accountNumber);
        search.clickSearch();
        rewritePage.clickSelect();
        if (rewritePage.checkRewritePoliciesCheckBoxExists())
            Assert.fail("Rewrite policies checkbox exists for policy cancelled  with other reason" + rewritePage.checkRewritePoliciesCheckBoxExists());

    }


    private void testIssueStandardFirePolicyRewrite() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(5);
        locationsList.add(propertyLocation);

        stdFirePolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("StandardFire", "Policy").withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Cancel Policy with other reasons
    private void testCancelPolicyWithOtherReason(String accountNumber) {

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

    }

    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    private void sendToAB() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.underwriterUserName, myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartNameChange nameChange = new StartNameChange(driver);
        //changePNI(GeneratePolicy myPolicyObj, GenerateContactType contactType, String newFirstName, String newLastNameOrCompanyName)
        Names newName = NamesHelper.getRandomName();
        GenerateContact newContact = nameChange.changePNI(myPolicyObj, GenerateContactType.Person, newName.getFirstName(), newName.getLastName());
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicyByAccountNumber(this.myPolicyObj.underwriterInfo.underwriterUserName, this.myPolicyObj.underwriterInfo.underwriterPassword, this.myPolicyObj.accountNumber);
        testCancelPolicy(this.myPolicyObj.accountNumber);
        StartRewrite rewrite = new StartRewrite(driver);
        rewrite.rewriteNewAccount(myPolicyObj, newContact);
    }
}


