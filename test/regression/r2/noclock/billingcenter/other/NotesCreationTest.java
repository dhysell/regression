package regression.r2.noclock.billingcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonNotes;
import repository.bc.common.actions.BCCommonActionsNewNote;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyNotes;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.PolicyNotes.RelatedTo;
import repository.gw.enums.PolicyNotes.Topic;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class NotesCreationTest extends BaseTest {
	private WebDriver driver;
	private BCTopMenu menu;
	private Topic topic;
	private RelatedTo relatedTo;
	private RelatedTo relatedToInTbl;
	private String acctNum, subject, topicInTbl, subjectInTbl;
	private BCAccountMenu acctMenu;
	private BCCommonActionsNewNote acctNewNote;
	private BCCommonActionsNewNote policyNewNote;
	private String pcNumber;
	private BCPolicyMenu pcMenu;
	private ARUsers arUser = new ARUsers();
	
	// randomly find an existing account
	@Test
	public void findAccountNumber() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		BCSearchAccounts schAccts=new BCSearchAccounts(driver);
		acctNum= schAccts.findAccountInGoodStanding("08-");

		System.out.println("account number is : "+acctNum);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "findAccountNumber" })	
	public void testAccountActionsNewNotes() throws Exception {
		subject="Account level note";	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActionsNewNote();
		acctNewNote = new BCCommonActionsNewNote(driver);
		relatedTo = acctNewNote.randomSetNewNoteRelatedTo();
		topic = acctNewNote.randomSetNewNoteTopic();
		acctNewNote.setNewNoteSubject(subject);
		acctNewNote.setNewNoteText(subject);
		acctNewNote.clickUpdate();
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "testAccountActionsNewNotes" })
	public void verifyAccountNotesScreenAndNotesFlag() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);
		//check notes flag
		menu = new BCTopMenu(driver);
		if(!menu.notesFlagExist()){
			Assert.fail("notes flag doesn't exist\n");
		}
		acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuAccountNotes();
		BCCommonNotes acctNotes = new BCCommonNotes(driver);
		//new note always displays in the first row
		topicInTbl = acctNotes.getNotesTopic(1);
		relatedToInTbl = acctNotes.getNotesRelatedTo(1);
		subjectInTbl=acctNotes.getNotesSubject(1);
		if (!topicInTbl.equals(topic.getValue()) || !relatedToInTbl.equals(relatedTo) || !subjectInTbl.equals(subject)) {
			Assert.fail("found wrong account note.\n");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	//test notes for Policy level	
	@Test(dependsOnMethods = { "verifyAccountNotesScreenAndNotesFlag" })
	public void createNewNoteForPolicy() throws Exception {
		subject="policy level note";
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);

		BCAccountSummary acctSum = new BCAccountSummary(driver);
		pcNumber=acctSum.getPolicyNumberFromOpenPolicyStatusTable(acctNum.substring(0, 5));
		acctSum.clickPolicyNumberInOpenPolicyStatusTable(pcNumber);
		pcMenu = new BCPolicyMenu(driver);
		pcMenu.clickBCMenuActionsNewNote();
		policyNewNote = new BCCommonActionsNewNote(driver);
		relatedTo = policyNewNote.randomSetNewNoteRelatedTo();
		topic = policyNewNote.randomSetNewNoteTopic();
		policyNewNote.setNewNoteSubject(subject);
		policyNewNote.setNewNoteText(subject);
		policyNewNote.clickUpdate();
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "createNewNoteForPolicy" })	
	public void verifyPolicyNotesScreenAndNotesFlag() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), pcNumber);
		// check notes flag
		menu = new BCTopMenu(driver);
		if(!menu.notesFlagExist()){
			Assert.fail("notes flag doesn't exist\n");
		}
		pcMenu = new BCPolicyMenu(driver);
		pcMenu.clickPolicyNotes();
        PolicyNotes pcNotes = new PolicyNotes(driver);
		//new note always displays in the first row		
		topicInTbl = pcNotes.getNotesTopic(1);
		relatedToInTbl = pcNotes.getNotesRelatedTo(1);
		subjectInTbl=pcNotes.getNotesSubject(1);
		if (!topicInTbl.equals(topic.getValue()) || !relatedToInTbl.equals(relatedTo) || !subjectInTbl.equals(subject)) {
			Assert.fail("found wrong policy note.\n");
		}
		new GuidewireHelpers(driver).logout();
	}
	//policy level notes display in Account Notes screen
	@Test(dependsOnMethods = { "verifyPolicyNotesScreenAndNotesFlag" })
	public void checkPolicyNoteInAccountNotesScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuAccountNotes();
		BCCommonNotes acctNotes = new BCCommonNotes(driver);
		//new note always displays in the first row
		topicInTbl = acctNotes.getNotesTopic(1);
		relatedToInTbl = acctNotes.getNotesRelatedTo(1);
		subjectInTbl=acctNotes.getNotesSubject(1);
		if (!topicInTbl.equals(topic.getValue()) || !relatedToInTbl.equals(relatedTo) || !subjectInTbl.equals(subject)) {
			Assert.fail("found wrong policy note in Account Notes screen.\n");
		}
		new GuidewireHelpers(driver).logout();
	}
}
