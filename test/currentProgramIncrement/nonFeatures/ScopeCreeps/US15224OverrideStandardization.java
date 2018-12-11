package currentProgramIncrement.nonFeatures.ScopeCreeps;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.AddressTemp2;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;

import java.util.ArrayList;

/**
 * @Author swathiAkarapu
 * @Requirement US15224 , US16582
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/222929286796">US15224</a>
 * <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/257620029116">US16582</a>
 * @Description
 *US15224
 * ----------
 * As a home office user (underwriter) I need to be able to edit the county and override the standardization on locations so that if the standardizing is not accurate I can adjust it.
 *
 * Steps to get there:
 *
 * Start squire policy using an address that borders 2 counties.
 * Standardize address on section II and see that the county is wrong.
 *
 * Acceptance criteria:
 *
 * Ensure that user (with underwriting permission) can edit the county and override the standardization process as needed.
 * Ensure that works on a policy change. Keeps the county on original location but a new location could be editable also.
 * Ensure that the field is editable on new submission and on transitions and any editable job (by underwriter only)
 * Ensure after the request approval comes back to the agent/PA, the auto-issuance "Issue Policy" option is still available to the agent/PA
 * US16582
 * ------------
 * Steps to get there:
 * Start squire policy using an address that borders 2 counties.
 * Standardize address on section II and see that the county is wrong
 * Acceptance criteria:
 * Ensure that the override yes/no options are only available after the address has been standardized
 * Ensure that the standardize address button is grayed out if the override has been selected as "yes"
 * Ensure that user with underwriting permission can edit the county and state and override the standardization process as needed using the radio button (rather than a checkbox)
 * Ensure that this works on a policy change. Keep the county on original location but a new location could be editable also
 * Ensure that the state and county fields are editable on new submission and on transitions and any editable job by underwriter only. RNT, RFT, Rewrite New Account, Copy Submission
 * Ensure after a request approval comes back to an agent/PA, the auto-issuance "Issue Policy" option is still available for agent/PA.
 * @DATE September 27
 */
public class US15224OverrideStandardization extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("UW", "Standardization")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.QuickQuote);
    }

    @Test
    public void verfiyUWCanOverrideStandardizationButNotAgent() throws Exception {
        generatePolicy();
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuPropertyLocations();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation propertyLocation = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation(driver);
        propertyLocation.clickEditLocation(myPolicyObject.squire.propertyAndLiability.locationList.get(0).getNumber());
        softAssert.assertTrue( propertyLocation.isAddressStandardizeOverrideExist(),"Override Standardization  should not exist for underwriter but should  exist  for  UW :"+uw.getUnderwriterUserName());
        propertyLocation.setOverideAddressStandazation(true);
        AddressTemp2 newAddress = AddressTemp2.getRandomAddress();
        while(propertyLocation.getCounty().equalsIgnoreCase(newAddress.getCounty())){
            newAddress = AddressTemp2.getRandomAddress();
        }
        propertyLocation.selectCounty(newAddress.getCounty());
       // propertyLocation.clickStandardizeAddress();
        propertyLocation.clickOK();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        softAssert.assertTrue( guidewireHelpers.isOnPage("//span[text()='Locations']"),"UNABLE TO GET TO LOCATION ");

        propertyLocation.clickEditLocation(myPolicyObject.squire.propertyAndLiability.locationList.get(0).getNumber());
        softAssert.assertEquals(propertyLocation.getCounty(),newAddress.getCounty(), "After click on StandardizeAddress is  changed the county . New County:"+newAddress.getCounty()+" UI County :"+propertyLocation.getCounty() +"for UW "+uw.getUnderwriterUserName());
        propertyLocation.setOverideAddressStandazation(false);
        softAssert.assertTrue( guidewireHelpers.isAlertPresent(), "Message Box not displayed if selected back to NO");
        softAssert.assertEquals(guidewireHelpers.getPopupTextContents(),"The address will need to be standardized again which may cause State and County to change, are you sure you want to disable the override?" , "Alert message not matching");
        guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        guidewireHelpers.logout();
        // Agent should not have override  option
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPropertyLocations();
        propertyLocation.clickEditLocation(myPolicyObject.squire.propertyAndLiability.locationList.get(0).getNumber());
        softAssert.assertFalse( propertyLocation.isAddressStandardizeOverrideExist(),"Override Standardization  should exist for Agent but should not exist ");
        softAssert.assertAll();
    }

    public void generateIssuePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        repository.gw.generate.custom.PLPolicyLocationProperty property = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(property);
        repository.gw.generate.custom.PolicyLocation locToAdd = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList);
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        repository.gw.generate.custom.PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(property1);
        repository.gw.generate.custom.PolicyLocation locToAdd1 = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList1);
        locationsList.add(locToAdd);
        locationsList.add(locToAdd1);
        repository.gw.generate.custom.SquireLiability myLiab = new repository.gw.generate.custom.SquireLiability();
        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("UW", "Standardization")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }



    @Test
    public void verfiyUWCanOverrideStandardization_policyChange() throws Exception {
      generateIssuePolicy();
      new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);




        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("policy Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcSideMenu.clickSideMenuPropertyLocations();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation propertyLocation = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation(driver);
        propertyLocation.clickEditLocation(2);
        softAssert.assertTrue( propertyLocation.isAddressStandardizeOverrideExist(),"Override Standardization  should not exist for under writer but should  exist ");
        propertyLocation.setOverideAddressStandazation(true);
        AddressTemp2 newAddress = AddressTemp2.getRandomAddress();
        while(propertyLocation.getCounty().equalsIgnoreCase(newAddress.getCounty())){
            newAddress = AddressTemp2.getRandomAddress();
        }
        propertyLocation.selectCounty(newAddress.getCounty());
        propertyLocation.clickOK();
        propertyLocation.clickEditLocation(2);
        softAssert.assertTrue(propertyLocation.getCounty().equalsIgnoreCase(newAddress.getCounty()), "After click on StandardizeAddress is  changed the county . UI county" +propertyLocation.getCounty() + "  updated  county"+ newAddress.getCounty());
        softAssert.assertAll();
    }

    private void generatePolicyFull() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("UW", "Standardization")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }
    /**
     * Ensure after the request approval comes back to the agent/PA, the auto-issuance "Issue Policy" option is still available to the agent/PA
     */
    @Test
    public void verfiyUWOverrideAddressAndAfterRequestApprovalAgentCanAutoIssue() throws Exception {
        generatePolicyFull();
        Underwriters uw= UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuPropertyLocations();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation propertyLocation = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation(driver);
        propertyLocation.clickEditLocation(myPolicyObject.squire.propertyAndLiability.locationList.get(0).getNumber());
       propertyLocation.setOverideAddressStandazation(true);
        AddressTemp2 newAddress = AddressTemp2.getRandomAddress();
        while(propertyLocation.getCounty().equalsIgnoreCase(newAddress.getCounty())){
            newAddress = AddressTemp2.getRandomAddress();
        }
        propertyLocation.selectCounty(newAddress.getCounty());

        propertyLocation.clickOK();
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction constructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.editbox_MSYear.clear();
        propertyDetail.clickOk();
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
       // pcSideMenu.clickSideMenuSquirePropertyCLUE();
        pcSideMenu.clickSideMenuSquirePropertyGlLineReview();
        pcSideMenu.clickSideMenuLineSelection();
        pcSideMenu.clickSideMenuMembership();
        pcSideMenu.clickSideMenuMembershipMembershipType();
        pcSideMenu.clickSideMenuMembershipMembers();
        pcSideMenu.clickSideMenuModifiers();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk_UWIssues.handleBlockSubmit(myPolicyObject);
        new GuidewireHelpers(driver).logout();
        //after request approval - Auto issue by Agent
       new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcSideMenu.clickSideMenuPayment();
        repository.pc.workorders.generic.GenericWorkorderPayment payment = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
        payment.fillOutPaymentPage(myPolicyObject);
        pcSideMenu.clickSideMenuForms();
        payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
        repository.pc.workorders.generic.GenericWorkorderComplete completePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        myPolicyObject.squire.setPolicyNumber(completePage.getPolicyNumber());
        completePage.clickPolicyNumber();
        PolicySummary polSummary = new PolicySummary(driver);
        Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(repository.gw.enums.TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");
    }

}
