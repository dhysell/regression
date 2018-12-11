package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;

import java.util.ArrayList;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/defect/223277920040">DE7529</a>
 * @Description Environment(s): UAT
 * User(s): afogle, hsavage, mmcorbridge
 * Account/Policy: 112542
 * <p>
 * Scenario 1
 * <p>
 * Transition in a policy with children between 14 and 17
 * Go to the policy members screen
 * Go to the drivers screen and remove the child
 * Go back to the policy members screen and try to remove the child
 * <p>
 * Scenario 2
 * <p>
 * Create a new policy with section III and children between 14 and 17, do not add any of them to the drivers screen
 * Quote, submit, issue.
 * Start the renewal job
 * Go to the policy members screen
 * Go to the drivers screen and remove the child
 * Go back to the policy members screen and try to remove the child
 * <p>
 * Actual: the child is being re added to the drivers screen as soon as the user returns to the policy members screen
 * Expected: the child stays off of the drivers screen and is able to be removed from the policy members screen (adding them when exiting the policy members screen rather than entering it)
 * <p>
 * Requirements link(s):
 * Stack trace: (store in stikked)
 * <p>
 * See attached screenshots.
 * @DATE May 31, 2018
 */

public class DE7529_Remove14To17Child extends BaseTest {
    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void EnsureRemovalOfChild14To17() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<Contact> policyMembers = new ArrayList<Contact>();

//			Contact ThirteenChild = new Contact("Thirteen", "child", Gender.Male, gw.helpers.DateUtils.dateAddSubtract(gw.helpers.DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -13));
//			ThirteenChild.setContactRelationshipToPNI(ContactRelationshipToMember.Child_Ward);
//			policyMembers.add(ThirteenChild); 
//			
//		    Contact FourteenChild = new Contact("Fourteen", "child", Gender.Male, gw.helpers.DateUtils.dateAddSubtract(gw.helpers.DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -14));
//		    FourteenChild.setContactRelationshipToPNI(ContactRelationshipToMember.Child_Ward);
//			policyMembers.add(FourteenChild); 
//			
        Contact FifteenChild = new Contact("Fifteen", "child", Gender.Male, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -15));
        FifteenChild.setRelationToInsured(RelationshipToInsured.Child);
        policyMembers.add(FifteenChild);
//			
//			Contact SixteenChild = new Contact("Sixteen", "child", Gender.Male, gw.helpers.DateUtils.dateAddSubtract(gw.helpers.DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -16));
//			SixteenChild.setContactRelationshipToPNI(ContactRelationshipToMember.Child_Ward);
//			policyMembers.add(SixteenChild);
//
//			Contact SeventeenChild = new Contact("Seventeen", "child", Gender.Male, gw.helpers.DateUtils.dateAddSubtract(gw.helpers.DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -17));
//			SeventeenChild.setContactRelationshipToPNI(ContactRelationshipToMember.Child_Ward);
//			policyMembers.add(SeventeenChild);
//			
//			Contact EighteenChild = new Contact("Eighteen", "child", Gender.Male, gw.helpers.DateUtils.dateAddSubtract(gw.helpers.DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -18));
//			EighteenChild.setContactRelationshipToPNI(ContactRelationshipToMember.Child_Ward);
//			policyMembers.add(EighteenChild);
//			
        Squire mySquire = new Squire();
        mySquire.policyMembers = policyMembers;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -300))
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);

//        driver.quit();
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
//        Config bcConfig = new Config(ApplicationOrCenter.BillingCenter);
//        WebDriver bcDriver = buildDriver(bcConfig);

        try {
			new Login(driver).loginAndSearchAccountByAccountNumber("su", "gw", myPolicyObject.accountNumber);
		} catch (Exception e) {
			//TO TRY AGAIN IF IT DIDN'T FIND THE ACCOUUNT. MESSAGES MAY BE SLOW. 
			new GuidewireHelpers(driver).logout();
			new Login(driver).loginAndSearchAccountByAccountNumber("su", "gw", myPolicyObject.accountNumber);
		}
        
        
        
        
        new BCAccountMenu(driver).clickBCMenuCharges();
        if (!new AccountCharges(driver).waitUntilIssuanceChargesArrive()) {
            new GuidewireHelpers(driver).logout();
//            driver.quit();
            Assert.fail("Charges from Policy Issuance did not arrive within 2 minutes.");

        } else {
            new GuidewireHelpers(driver).logout();
//            driver.quit();
        }


//        cf = new Config(ApplicationOrCenter.PolicyCenter);
//        driver = buildDriver(cf);
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

        new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        GenericWorkorderPolicyMembers pcPolicyMembersPage = new GenericWorkorderPolicyMembers(driver);
        GenericWorkorderSquireAutoDrivers pcSquireDriversPage = new GenericWorkorderSquireAutoDrivers(driver);


        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
        pcPolicyMenu.clickRenewPolicy();
        new InfoBar(driver).clickInfoBarPolicyNumber();
        new PolicySummary(driver).clickPendingTransaction(TransactionType.Renewal);
        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickPolicyContractSectionIIIAutoLine();
        pcSideMenu.clickSideMenuPACoverages();
        new GuidewireHelpers(driver).editPolicyTransaction();

        pcSideMenu.clickSideMenuHouseholdMembers();
        pcSideMenu.clickSideMenuPADrivers();

        pcSquireDriversPage.setCheckBoxInDriverTableByDriverName("Fifteen child");
        pcSquireDriversPage.clickRemoveButton();
        pcSideMenu.clickSideMenuHouseholdMembers();

        pcPolicyMembersPage.clickRemoveMember("Fifteen child");
        pcSideMenu.clickSideMenuPADrivers();

        // Acceptance criteria: Ensure child has not been re-added to the drivers page
        Assert.assertFalse(pcSquireDriversPage.checkIfDriverExists("Fifteen child"), "Fifteen child still exists");
    }
}




















