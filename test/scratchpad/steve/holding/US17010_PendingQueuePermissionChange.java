package scratchpad.steve.holding;

import java.time.LocalDate;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.PendingChangesAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.cc.enums.PolicyType;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class US17010_PendingQueuePermissionChange extends BaseTest{
	private GenerateFNOL myFNOLObj;
    private ClaimsUsers user = ClaimsUsers.abatts;
    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-168144-01";
    private LocalDate dateOfLoss = LocalDate.now();
	
	
	@Test
	public void testHistoryItemForPendingQueue() throws Exception {
				
		 Config cf = new Config(ApplicationOrCenter.ClaimCenter, "dev");
	     WebDriver driver = buildDriver(cf);
	     this.myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(user.toString(), user.getPassword())
            .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
            .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
            .withPolicyType(PolicyType.CITY)
            .withDateOfLoss(dateOfLoss)
            .withVerifiedPolicy(false)
            .build(GenerateFNOLType.Auto);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
        
    	AbUsers loginUser = AbUserHelper.getRandomDeptUser("Claims");
    	Login login = new Login(driver);
    	login.login(loginUser.getUserName(), loginUser.getUserPassword());
        PendingChangesAB changesPage = new PendingChangesAB(driver);
        changesPage.getToPendingChanges("Claims");
        changesPage.clickCreates();
        Assert.assertTrue(changesPage.clickContact(myFNOLObj.getPartiesInvolved().get(0).getFirstName() + " " + myFNOLObj.getPartiesInvolved().get(0).getLastName(), user.getName()), "The created contact should be found in the Claims Pending Queue.");
        changesPage.clickApprove(OkCancel.OK);
        new GuidewireHelpers(driver).logout();
        new AdvancedSearchAB(driver).loginAndSearchContact(loginUser, myFNOLObj.getPartiesInvolved().get(0).getFirstName(), myFNOLObj.getPartiesInvolved().get(0).getLastName(), myFNOLObj.getPartiesInvolved().get(0).getAddressOne(), State.Idaho);
        new ContactDetailsBasicsAB(driver).clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
        System.out.println("The user is " + user.getName());
        System.out.println(DateUtils.dateFormatAsString("EEE, MMM d, yyyy", new Date()));
        System.out.println("Contact accepted by "+loginUser.getUserFirstName()+ " "+loginUser.getUserLastName()+" via Claims Pending Queue");
        Assert.assertTrue(historyPage.verifyHistoryItemExists("Pending Create Approved", user.getName(), DateUtils.dateFormatAsString("EEE, MMM d, yyyy", new Date()), "Contact accepted by "+loginUser.getUserFirstName()+ " "+loginUser.getUserLastName()+" via Claims Pending Queue", false), "When a Claims Pending Queue Contact is accepted into ContactManager, a history Item must be created.");
	}
	
	@Test
	public void testPolicyServicesUnableToSeePendingQueue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
	    WebDriver driver = buildDriver(cf);
		AbUsers loginUser = AbUserHelper.getRandomDeptUser("Policy");
    	Login login = new Login(driver);
    	login.login(loginUser.getUserName(), loginUser.getUserPassword());
    	TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        SidebarAB sidebar = new SidebarAB(driver);
        Assert.assertFalse(sidebar.pendingChangedExists(), "For Policy Services the Claims Pending Queue should not exist.");
	}
}
