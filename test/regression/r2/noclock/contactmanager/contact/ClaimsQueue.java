package regression.r2.noclock.contactmanager.contact;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.PendingChangesAB;
import repository.ab.topmenu.TopMenuAB;
import repository.cc.claim.WorkplanCC;
import repository.cc.entities.InvolvedParty;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import regression.r2.noclock.claimcenter.fnol.UnverifiedPolicyFNOL;

/**
 * @Author sbroderick
 * @Requirement Contacts coming from CC except Vendors are stored in a Claims Queue
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Navigation%20-%20Desktop%20Tab.xlsx">Requirements Spreadsheet</a>
 * @Description Contacts coming from CC except Vendors are stored in a Claims Queue
 * @DATE Oct 19, 2017
 */

public class ClaimsQueue extends BaseTest {

    private GenerateFNOL unverifiedFNOL = null;
    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-150564-01";
    private String coverageNeeded = "Dwelling";

    public GenerateFNOL dwellingPDFnol() throws Exception {
    	String involvedPartyFirstName = "Brett";
    	String involvedPartyLastName = "Hiltbrand";
    	String involvedPartyRole = "Insured";
    	String involvedPartyAddressLineOne = "382 Lavender St";
    	String involvedPartyCity = "Chubbuck";
    	State involvedPartyState = State.Idaho;
    	String involvedPartyZip = "83202";
    	
    	//Delete Brett Hiltbrand
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        cf.setEnv("dev");
        WebDriver driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Claims");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults searchResults = searchMe.searchByFirstNameLastNameAnyAddress(involvedPartyFirstName, involvedPartyLastName);
        if(searchResults != null) {
        	ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        	contactPage.clickContactDetailsBasicsDeleteLink();
        }

        driver.quit();
        cf = new Config(ApplicationOrCenter.ClaimCenter);
        cf.setEnv("dev");
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withLOB(ClaimSearchLineOfBusiness.City_Squire_Policy_Property)
                .withAdress(address)
                .withtopLevelCoverage(coverageNeeded)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);


        InvolvedParty insured = new InvolvedParty.Builder()
                .withFirstName(involvedPartyFirstName)
                .withLastName(involvedPartyLastName)
                .withRole(involvedPartyRole)
                .withAddressOne(involvedPartyAddressLineOne)
                .withCity(involvedPartyCity)
                .withState(involvedPartyState.getName())
                .withZip(involvedPartyZip)
                .build();
        myFNOLObj.getPartiesInvolved().add(insured);

        driver.quit();
        return myFNOLObj;
    }
    
    @Test
    public void testClaimsQueue() throws Exception {
        GenerateFNOL myFNOLObj = dwellingPDFnol();
        checkAB(myFNOLObj);
        checkCCForRejectedActivity(myFNOLObj);
    }

    @Test
    public void unverifiedFNOL() throws Exception {
        UnverifiedPolicyFNOL unverifiedFnolObj = new UnverifiedPolicyFNOL();
        unverifiedFnolObj.unverifiedPolicyFnol();
        this.unverifiedFNOL = unverifiedFnolObj.getFNOL();
        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);
        rejectContact(unverifiedFNOL);
        checkCCForRejectedActivity(unverifiedFNOL);
    }

    @Test
    public void acceptUnverifiedFNOL() throws Exception {
        UnverifiedPolicyFNOL unverifiedFnolObj = new UnverifiedPolicyFNOL();
        unverifiedFnolObj.unverifiedPolicyFnol();
        this.unverifiedFNOL = unverifiedFnolObj.getFNOL();
        checkAB(unverifiedFNOL);
        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);
    	AbUsers loginUser = AbUserHelper.getRandomDeptUser("Claims");
    	Login login = new Login(driver);
    	login.login(loginUser.getUserName(), loginUser.getUserPassword());
        PendingChangesAB changesPage = new PendingChangesAB(driver);
        changesPage.getToPendingChanges("Claims");
        changesPage.clickCreates();
        Assert.assertTrue(changesPage.clickContact(unverifiedFNOL.getPartiesInvolved().get(0).getFirstName() + " " + unverifiedFNOL.getPartiesInvolved().get(0).getLastName(), user.getName()), "The created contact should be found in the Claims Pending Queue.");
        String contents = changesPage.clickApprove(OkCancel.OK);
        if (!contents.contains("Are you sure you want to Approve")) {
            Assert.fail("Before a contact is approved a popup message should appear.");
        }
        driver.quit();
    }

    public GenerateFNOL getUnverifiedFNOL() {
    	return this.unverifiedFNOL;
    }
    
    @Test
    public void acceptThenEditUnverifiedFNOL() throws Exception {
        UnverifiedPolicyFNOL unverifiedFnolObj = new UnverifiedPolicyFNOL();
        unverifiedFnolObj.unverifiedPolicyFnol();
        this.unverifiedFNOL = unverifiedFnolObj.getFNOL();
        checkAB(unverifiedFNOL);
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        PendingChangesAB changesPage = new PendingChangesAB(driver);
        changesPage.getToPendingChanges("Claims");
        changesPage.clickCreates();
        Assert.assertTrue(changesPage.clickContact(unverifiedFNOL.getPartiesInvolved().get(0).getFirstName() + " " + unverifiedFNOL.getPartiesInvolved().get(0).getLastName(), user.getName()), "The created contact should be found in the Claims Pending Queue.");
        String contents = changesPage.clickApproveThenEdit(OkCancel.OK);
        if (!contents.contains("Are you sure you want to Approve")) {
            Assert.fail("Before a contact is approved a popup message should appear.");
        }
        driver.quit();
    }

    public void checkAB(GenerateFNOL fnol) throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Claims");
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        if (advancedSearch.loginAndSearchContact(abUser, fnol.getPartiesInvolved().get(0).getFirstName(), fnol.getPartiesInvolved().get(0).getLastName(), fnol.getPartiesInvolved().get(0).getAddressOne(), State.Idaho)) {
            Assert.fail("The contact was found within ContactManager.  New Claims Contacts should go to the queue.");
        }
        driver.quit();
    }

    public void rejectContact(GenerateFNOL fnol) throws Exception {
    	checkAB(fnol);
        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);
        PendingChangesAB changesPage = new PendingChangesAB(driver);
        changesPage.getToPendingChanges("Claims");
        changesPage.clickCreates();
        Assert.assertTrue(changesPage.clickContact(fnol.getPartiesInvolved().get(0).getFirstName() + " " + fnol.getPartiesInvolved().get(0).getLastName(), user.getName()), "The created contact should be found in the Claims Pending Queue.");
        changesPage.clickReject("More Information Required");
        driver.quit();
    }

    public void checkCCForRejectedActivity(GenerateFNOL fnol) throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        new Login(driver).login(user.toString(), this.password);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(fnol.claimNumber);
        SideMenuCC sidebar = new SideMenuCC(driver);
        sidebar.clickWorkplanLink();
        WorkplanCC workplan = new WorkplanCC(driver);
        //Was turned off by CC.
        Assert.assertFalse(workplan.completeActivity("Pending create rejected by "), "An activity should be created for rejected contacts.  See "+fnol.getPartiesInvolved().get(0).getFirstName()+" "+fnol.getPartiesInvolved().get(0).getLastName());
        driver.quit();
    }


}
