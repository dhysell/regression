package scratchpad.swathi.templates;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author swathiAkarapu
 * @Requirement
 * @RequirementsLink <a href=""></a>
 * @Description ~~RALLY TEXT HERE~~
 * @DATE August 2, 2018
 */
public class PolicyChangeTemplate extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    public void generatePolicyWithAgent(Agents agent) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);
    }




    @Test(enabled = true)
    public void ChangePropertyDetailsWithSA() throws Exception {
        Agents agent= AgentsHelper.getAgentByUserName("tgallup");
        generatePolicyWithAgent(agent);
        String saUsername = "jgallup";

        new Login(driver).loginAndSearchAccountByAccountNumber(saUsername,
                "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        BasePage basePage = new BasePage(driver);
        UWActivityPC activity = new UWActivityPC(driver);
        GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);

        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        policyChangePage.startPolicyChange("change property details", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setSquareFootage(4000);
        pcWorkOrder.clickOK();
        pcWorkOrder.clickGenericWorkorderQuote();

        pcSideMenu.clickSideMenuRiskAnalysis();
        risk.clickRequestApproval();
        activity.setText("Sending this over stuff and stuff");
        activity.setNewNoteSubject("Please Special Approve this Stuff!!");
        // activity.setChangeReason(ChangeReason.AnyOtherChange);
        activity.clickSendRequest();
        pcCompletePage.clickViewYourPolicy();


    }


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAuto spa = new SquirePersonalAuto();
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = spa;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("policy", "change")
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void testPolicyChangeWithAddingDriver() throws Exception {
        generatePolicy();

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        SoftAssert softAssert = new SoftAssert();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("Agent policy Change", currentSystemDate);

        sideMenu.clickSideMenuPolicyInfo();
        String firstName = "NewMem" + StringsUtils.generateRandomNumberDigits(7);
        addNewhouseholdMember(myPolicyObject.basicSearch, firstName,
                "Added" + StringsUtils.generateRandomNumberDigits(7), this.myPolicyObject.pniContact.getAddress(),
                DateUtils.dateAddSubtract(currentSystemDate,
                        DateAddSubtractOptions.Year, -25));

        sideMenu.clickSideMenuPADrivers();
        paDrivers.addExistingDriver(firstName);



    }





    private void addNewhouseholdMember(boolean basicSearch, String firstName, String lastName, AddressInfo address, Date dob)
            throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address.getLine1(), address.getCity(),
                address.getState(), address.getZip(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(dob);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setSSN("2" + StringsUtils.generateRandomNumberDigits(8).replace("00", "34"));
        householdMember.setNewPolicyMemberAlternateID(firstName);
        householdMember.selectNotNewAddressListingIfNotExist(address);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
    }
}