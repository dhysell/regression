package previousProgramIncrement.pi1_041918_062718.f122_CSRPermissionsInBillingcenter_AllowPARC;

import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyActivities;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.PARCActivtyType;
import repository.gw.enums.PolicyCompany;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @Requirement F122 - CSR Permission in BC for Creating "PARC" Activities
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/portfolioitem/feature/209416850724">F122 - CSR Permission in BC for Creating "PARC" Activities</a>
 * @DATE May 29, 2018
 */

/**
 * @Author sgunda
 * @Requirement US15255 - CSR BC Screen View-Only
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/userstory/224238742852">US15255 - CSR BC Screen View-Only</a>
 * @DATE Jun 11, 2018
 */

@Test(groups = {"BillingCenter","sgunda"})
public class CSRPermissionsAndPARCActivitiesNoExceptions extends BaseTest {
	
	
	private WebDriver driver;
    private ARUsers csrUser , arUser = new ARUsers();
    private BCSearchPolicies searchPolicy;
    private String policyNumber , policyPeriod= null;
    private PolicyCompany policyCompany;
    private ActivityQueuesBillingCenter activityQueuesBillingCenter;
    private PARCActivtyType parcActivityTypeToCreate;
    private boolean didPermissionsBreak = false;
    private String permissionsErrorLog ;


    private void getFBMISOrWCINSPolicyNumber() throws Exception {
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        searchPolicy = new BCSearchPolicies(driver);

        switch (dayOfMonth % 2) {
            case 0:
                try {
                    policyNumber = searchPolicy.findPolicyInGoodStanding("2901", "01-", PolicySearchPolicyProductType.Squire);
                    policyPeriod = new BCPolicySummary(driver).getPolicyNumber();
                    System.out.println("Farm Bureau Mutual policy number is " + policyNumber + " and on policy period " + policyPeriod );
                    policyCompany = PolicyCompany.Farm_Bureau;
                } catch (Exception e) {
                    e.printStackTrace();
                    Assert.fail("Sorry, no good standing Farm Bureau account found. Test cannot continue.");
                  //  throw new SkipException("Sorry, no good standing Farm Bureau account found. Test cannot continue.");
                }
                break;
            case 1:
                try {
                    policyNumber = searchPolicy.findPolicyInGoodStanding("290", "08-", PolicySearchPolicyProductType.Business_Owners);
                    policyPeriod = new BCPolicySummary(driver).getPolicyNumber();
                    System.out.println("Western community insurance policy number is " + policyNumber+ " and on policy period " + policyPeriod);
                    policyCompany = PolicyCompany.Western_Community;
                } catch (Exception e) {
                    e.printStackTrace();
                    Assert.fail("Sorry, no good standing Western community account found. Test cannot continue.");			}
                break;
            default:
                try {
                    policyNumber = searchPolicy.findPolicyInGoodStanding("290", "08-", PolicySearchPolicyProductType.Business_Owners);
                    policyPeriod = new BCPolicySummary(driver).getPolicyNumber();
                    System.out.println("Western community insurance policy number is " + policyNumber + " and on policy period " + policyPeriod);
                    policyCompany = PolicyCompany.Western_Community;
                } catch (Exception e) {
                    e.printStackTrace();
                    Assert.fail("Sorry, no good standing Western community account found. Test cannot continue.");
                }
                break;
        }
    }


    private void checkAccountLevelPermissions() throws Exception {
    	BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        System.out.println(" Testing Account Screen");
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuSummary,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuSummary");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuPayments,1000)){
            didPermissionsBreak=true;
            System.out.println(" screen failed");
            System.out.println(" Account level screens the failed : link_AccountMenuPayments" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuCharges,1000)){
            didPermissionsBreak=true;
            System.out.println(" screen failed");
            System.out.println(" Account level screens the failed : link_MenuCharges" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuDelinquencies,1000)){
            didPermissionsBreak=true;
            System.out.println(" screen failed");
            System.out.println(" Account level screens the failed : link_MenuDelinquencies" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuPolicies,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_AccountMenuPolicies" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuInvoices,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_AccountMenuInvoices" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuTroubleTickets,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuTroubleTickets" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuDocuments,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuDocuments" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuDelinquencies,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuDelinquencies" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuActivities,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuActivities" );
        }
        
        
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuTransactions,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuTransactions" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuContacts,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuContacts" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuHistory,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuHistory" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuAccountNotes,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_AccountMenuAccountNotes" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuLedger,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuLedger" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuJournal,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuJournal" );
        }
    }

    private void checkPolicyLevelPermissions() throws Exception {
        permissionsErrorLog = "CSR are able to access the screen which they do not have permissions for, Please check the log ";
        BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
        System.out.println(" Testing Policy Screen");
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuSummary,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuSummary");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuCharges,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuCharges");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_PaymentSchedule,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_PaymentSchedule");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuTroubleTickets,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuTroubleTickets");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuDocuments,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuDocuments");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuDelinquencies,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuDelinquencies");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuActivities,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuActivities");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_PolicyNotes,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_PolicyNotes");
        }


        if(new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuTransactions,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuTransactions");
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuContacts,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuContacts");
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuHistory,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuHistory");
        }
       if(new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuLedger,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuLedger");
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcPolicyMenu.link_MenuJournal,1000)){
            didPermissionsBreak=true;
            System.out.println(" Account level screens the failed : link_MenuJournal");
        }
    }

    private void checkLienHolderScreenPermissions() throws Exception {
    	BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        System.out.println(" Testing LienHolder Screen");
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuSummary,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuSummary");
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuPolicyLoanBalances,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuPolicyLoanBalances" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuPayments,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuPayments" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuCharges,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuCharges" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuDisbursements,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuDisbursements" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuPolicies,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuPolicies" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuInvoices,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuInvoices" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuTroubleTickets,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuTroubleTickets" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuDocuments,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuDocuments" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuDelinquencies,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuDelinquencies" );
        }
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuActivities,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuActivities" );
        }


        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuTransactions,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuTransactions" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuContacts,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuContacts" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuHistory,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuHistory" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_AccountMenuAccountNotes,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_AccountMenuAccountNotes" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuLedger,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuLedger" );
        }
        if(new GuidewireHelpers(driver).checkIfElementExists(bcAccountMenu.link_MenuJournal,1000)){
            didPermissionsBreak=true;
            System.out.println(" LienHolder screens the failed : link_MenuJournal" );
        }
    }


    @Test
    public void findGoodStandingAccountPolicyAndCreateAndCreatePARC() throws Exception {

    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("su","gw");

        getFBMISOrWCINSPolicyNumber();
        new GuidewireHelpers(driver).logout();

        this.csrUser = ARUsersHelper.getRandomARUserByRole(ARUserRole.CSR);
        login.loginAndSearchPolicyByPolicyNumber(this.csrUser.getUserName(),this.csrUser.getPassword(),policyNumber);
        BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
        parcActivityTypeToCreate = PARCActivtyType.Other_PARC_Message;
        getQALogger().info("Random PARC activity that is been created is " +  parcActivityTypeToCreate.getValue());
        bcPolicyMenu.clickAndCreatePARCActivity(parcActivityTypeToCreate);
    }

    @Test(dependsOnMethods = {"findGoodStandingAccountPolicyAndCreateAndCreatePARC"})
    public void loginAsARAndApproveInGeneralQueue() throws Exception {
        if(policyCompany==PolicyCompany.Farm_Bureau){
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical , ARCompany.Personal);
            activityQueuesBillingCenter = ActivityQueuesBillingCenter.ARGeneralFarmBureau;
        }else {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical , ARCompany.Commercial);
            activityQueuesBillingCenter = ActivityQueuesBillingCenter.ARGeneralWesternCommunity;
       }
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.login(this.arUser.getUserName(),this.arUser.getPassword());
        new BCTopMenu(driver).clickDesktopTab();
        BCDesktopMenu desktopMenu= new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuMyQueues();
        BCDesktopMyQueues myQueue= new BCDesktopMyQueues(driver);
        myQueue.setMyQueuesFilter(activityQueuesBillingCenter);
        myQueue.clickMyQueuesResultTableTitleToSort("Opened");
        myQueue.clickMyQueuesResultTableTitleToSort("Opened");
       try {
           myQueue.clickCheckboxInTableByLinkText(parcActivityTypeToCreate.getValue());
           myQueue.clickAssignSelectedToMe();
           desktopMenu.clickDesktopMenuMyActivities();
           BCDesktopMyActivities desktopAct = new BCDesktopMyActivities(driver);
           desktopAct.clickActivityTableTitleToSort("Opened");
           desktopAct.clickActivityTableTitleToSort("Opened");
           desktopAct.clickLinkInActivityResultTable(parcActivityTypeToCreate.getValue());
           desktopAct.clickActivityApprove();
       } catch (Exception e){
           e.printStackTrace();
           Assert.fail("PARC activity was not found or AR was not able to respond to PARC activity");
       }
    }

    @Test(dependsOnMethods = {"loginAsARAndApproveInGeneralQueue"})
    public void loginAndSeeActivityAsCSR() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.login(this.csrUser.getUserName(),this.csrUser.getPassword());
        new BCTopMenu(driver).clickDesktopTab();
        BCDesktopMenu desktopMenu= new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuMyActivities();
        BCDesktopMyActivities desktopAct= new BCDesktopMyActivities(driver);
        desktopAct.clickActivityTableTitleToSort("Opened");
        desktopAct.clickActivityTableTitleToSort("Opened");
        try{
            desktopAct.clickLinkInActivityResultTable(parcActivityTypeToCreate.getValue() + " for PolicyPeriod : "+ policyPeriod + " has been approved");
            desktopAct.clickCompleteWithoutPopup();
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Was not able to find the PARC response AR from activity or was not able complete the response activity");
        }
   }
    @Test(dependsOnMethods = {"loginAsARAndApproveInGeneralQueue"})
    public void testPermissionsToScreens() throws Exception {
        //test Permissions on screens
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
    	new Login(driver).loginAndSearchPolicyByPolicyNumber(this.csrUser.getUserName(),this.csrUser.getPassword(),policyNumber);
        checkPolicyLevelPermissions();
        new BCPolicyMenu(driver).clickTopInfoBarAccountNumber();
        checkAccountLevelPermissions();
        String lienAccountNumber = new BCSearchAccounts(driver).findLienholderAccountInGoodStanding("980");
        new BCSearchAccounts(driver).searchAccountByAccountNumber(lienAccountNumber);
        checkLienHolderScreenPermissions();
        Assert.assertFalse(didPermissionsBreak,permissionsErrorLog);
    }
}