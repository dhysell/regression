package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderComplete;

import java.util.ArrayList;
import java.util.Date;


/**
 * @Author swathiAkarapu
 * @Requirement US15823
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/239568123704">US15823</a>
 * @Description
 * As a county office user or home office user I need to be able to update/edit/change an address on a cancelled policy without having to reinstate, change and re-cancel.
 *
 * BOP already has this - make PL work the same.
 * Need an activity called "Submitted policy change on cancelled policy" to generate (same as BOP) and round robin by area for PL - same as all other policy changes in PL.
 *
 * Steps to get there:
 * Have a cancelled policy
 * As an agent/PA/CSR/SA or underwriter go on the cancelled policy
 * Start a policy change effective before the cancel date
 * Change the mailing address and quote
 * Acceptance criteria:
 * Ensure that each user (CSR, SA, PA, Agent for county offices, and Underwriters for home office) can make an address change on a cancelled policy (without having to reinstate and re-cancel)
 * Ensure that the "change on a cancelled policy" letter goes to documents for print and mailing
 * Ensure that these documents are addressed to the new address
 * Ensure that an activity called "Submitted policy change on cancelled policy" generates
 * Ensure that this activity round robins by area to UWs in PL (this is different than BOP pattern, same as all other PL activities/changes)
 * @DATE October 3, 2018
 */

public class US15823EditAddressOnCancelledPolicy extends BaseTest {


    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void verifyCancelAndEditAddress() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
         myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("cancel", "EditAdd")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(),myPolicyObject.accountNumber);


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();


        pcPolicyMenu.clickCancelPolicy();
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.OtherLackUWInfo);
        pcCancellationPage.setEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 20));
        pcCancellationPage.setDescription("test edit address after cancel");
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();
        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickChangePolicy();

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        SoftAssert softAssert = new SoftAssert();
        // start policy change
        Date policyChangeEffective = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 18);
        String changeNumber = policyChangePage.startPolicyChange("UW policy Change", policyChangeEffective);
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo polInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        AddressInfo newAddress= new AddressInfo(true);
        policyInfoContactPage.setDesignatedNewAddress(newAddress);
        policyInfoContactPage.clickUpdate();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();



        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickIssuePolicyButton();
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        /*PolicySummary polSummary = new PolicySummary(driver);
        boolean activityCancelled= false;
         String activityAssigned= null;

        PolicyActivities currentActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_BasicInfo());
           for(Activity activity : currentActivities.getPolicyActivityList()) {
            if(activity.getSubject().contains("Submitted policy change on cancelled policy")){
                activityCancelled = true;
                activityAssigned=activity.getAssignedTo();
                break ;
            }
        }
        String assignedToFirstName = activityAssigned.split(" ")[0];
        String assignedToLastName = activityAssigned.split(" ")[1];
        softAssert.assertTrue( pcAccountSummaryPage.checkUWExists(assignedToFirstName, assignedToLastName) ,"not assigned to underwriter");
        softAssert.assertTrue(activityCancelled , "Submitted policy change on cancelled policy - activity not found");*/

        new SideMenuPC(driver).clickSideMenuToolsDocuments();
        new PolicyDocuments(driver).selectRelatedTo("Policy Change", changeNumber);
        new PolicyDocuments(driver).clickSearch();


        ArrayList<String> nameAddress = new PolicyDocuments(driver).getDocumentNameAddress();
        boolean  newAddressFound = false;
        for (String current : nameAddress) {
            if (current.contains(newAddress.getLine1()) && current.contains(newAddress.getCity()) && current.contains(newAddress.getZip())) {
                newAddressFound = true;
            }
        }

        softAssert.assertTrue( newAddressFound ,"new Address not found on documents");


        softAssert.assertAll();
    }


}
