/**
 * test disbursements from suspense payments and accounts, queue assignment and approval for disbursement as well
 * By:Jessica Qu
 * 
 */
package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.BCDesktopMyActivities;
import repository.bc.desktop.BCDesktopMyQueues;
import repository.bc.desktop.DesktopDisbursements;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import regression.r2.noclock.policycenter.issuance.TestIssuance;
@QuarantineClass
public class DisbursementsAndQueueAssignmentTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private String payTo="Jessica";//, notes="mydisbursement";
	private double suspensePmtAmt=111.23;
	private ARUsers arUser = new ARUsers();
	
    public DisbursementsAndQueueAssignmentTest() {
		super();		
	}
	
	private ArrayList<String> bothBillingClericalAndManager = new ArrayList<String>() {	
		private static final long serialVersionUID = 1L;
		{
			this.add("bcarling");
			this.add("sbrunson");
			this.add("hhill");
			this.add("alemmon");
			this.add("skane");
			this.add("asnorris");
			this.add("jlowrey");
			this.add("agilmore");
						
		}};	
		
	private boolean userIsBillingManager(String userName){
		boolean found=false;
		for(int i=0;i<bothBillingClericalAndManager.size();i++){
			if(userName.equals(bothBillingClericalAndManager.get(i))){
				found=true;
				break;
			}
		}
		return found;		
	}
	
	private void loginBC(ARUserRole role) throws Exception{
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(role, ARCompany.Commercial);
		//if role==Billing_Clerical, keep finding the this.arUser until it is only a clerical but not both billing clerical and billing manager. (i.e. asnorris is both clerical and manager)
		if(role==ARUserRole.Billing_Clerical){
			while(userIsBillingManager(arUser.getUserName())){
				this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(role, ARCompany.Commercial);
			}
		}
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		System.out.println("teh role is "+arUser.getUserRole());
		new Login(driver).login(arUser.getUserName(),arUser.getPassword());	
	}
	
		
	@Test 
	public void testCreateDisbursementFromDesktop() throws Exception {		
		//login as a clerical
		loginBC(ARUserRole.Billing_Clerical);		
		
		//ATTENTION: YOU WILL NEED TO FIND A NEW WAY TO CREATE AN AMOUNT TO DISBURSE HERE. SUSPENSE ITEMS ARE NO LONGER HOW LH PAYMENTS ARE MADE!!!
		
		Date bcDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		/*IDesktopMenu desktopMenu= DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create suspense payment
		IDesktopSuspensePayments suspensePmt=DesktopFactory.getDesktopSuspensePaymentsPage();
		suspensePmt.clickNew();
				INewSuspensePaymentWizard newSusPmt= WizardFactory.getNewSuspensePaymentWizard();
		Date bcDate = DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter);		
		newSusPmt.setPaymentDate(bcDate);
				newSusPmt.setNewSuspensePaymentAmount(suspensePmtAmt);
				newSusPmt.setNewSuspensePaymentNotes(notes);
				newSusPmt.setPaymentInstrument(PaymentInstrument.Cash);

		newSusPmt.clickUpdate();
				suspensePmt.clickCheckBoxInSuspensePaymentTableByText(notes);
				suspensePmt.clickCreateDisbursement();
		*/
		//create disbursement for this suspense payment
		CreateAccountDisbursementWizard createDisbursement = new CreateAccountDisbursementWizard(driver);
		Date dueDate= DateUtils.dateAddSubtract(bcDate, DateAddSubtractOptions.Day, 10);	
		createDisbursement.setCreateAccountDisbursementWizardDueDate(dueDate);
		createDisbursement.setCreateAccountDisbursementWizardReasonFor(DisbursementReason.Other);
		createDisbursement.setCreateAccountDisbursementWizardPayTo(payTo);

		createDisbursement.setState(State.Idaho);
		createDisbursement.setZip("83202");
		createDisbursement.setAddressLine1("1st Street");
		createDisbursement.setCity("Pocatello");
		createDisbursement.clickNext();
		createDisbursement.clickFinish();
	}

	@Test(dependsOnMethods = {"testCreateDisbursementFromDesktop"})
	public void testAssignAccountDisbursementsRequiringApproval() throws Exception {		
		//login as a manager
		loginBC(ARUserRole.Billing_Manager);
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();
		BCDesktopMyQueues myQueue = new BCDesktopMyQueues(driver);

		myQueue.setMyQueuesFilter(ActivityQueuesBillingCenter.ARSupervisorWesternCommunity);
		// looking for the disbursement in the queue and Assign it to manager
		String textInQueue = "Approval Required: Disbursement of "	+ StringsUtils.currencyRepresentationOfNumber(suspensePmtAmt) + " to " + payTo;
		myQueue.clickMyQueuesResultTableTitleToSort("Opened");
		myQueue.clickMyQueuesResultTableTitleToSort("Opened");

		myQueue.clickCheckboxInTableByLinkText(textInQueue);
		myQueue.clickAssignSelectedToMe();

		//approve the disbursement in My Activities
		desktopMenu.clickDesktopMenuMyActivities();
		BCDesktopMyActivities desktopAct= new BCDesktopMyActivities(driver);	
		desktopAct.clickActivityTableTitleToSort("Opened");
		desktopAct.clickActivityTableTitleToSort("Opened");
		desktopAct.clickLinkInActivityResultTable(textInQueue);
		desktopAct.clickActivityApprove();
		//looking for the disbursement in Disbursements table
		desktopMenu.clickDesktopMenuDisbursements();
		DesktopDisbursements dsktpDisburse = new DesktopDisbursements(driver);
		dsktpDisburse.clickDisbursementsTableTitleToSort("Date Created");
		dsktpDisburse.clickDisbursementsTableTitleToSort("Date Created");
		boolean findOrNot = dsktpDisburse.cellExistInDisbursementTableByText(StringsUtils.currencyRepresentationOfNumber(suspensePmtAmt));
		if(!findOrNot) {
			Assert.fail("Disbursement is not created successfully.");
		}
	}
	@Test
	public void testAccountDisbursementsCreation() throws Exception {	
		//generate a policy
		TestIssuance issued = new TestIssuance();
		issued.testBasicIssuanceInsuredOnly();
		this.myPolicyObj = issued.myPolicyObjInsuredOnly;	
		//login BC as a manager
		loginBC(ARUserRole.Billing_Manager);	
		double amount= 99.99;
		BCSearchAccounts theSch=new BCSearchAccounts(driver);
		theSch.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPmt = new NewDirectBillPayment(driver);
		newPmt.setAmount(amount);
		newPmt.clickExecuteWithoutDistribution();
		acctMenu.clickAccountMenuActionsNewTransactionDisbursement();
		Date bcDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		Date dueDate=DateUtils.dateAddSubtract(bcDate, DateAddSubtractOptions.Day, 3);
		CreateAccountDisbursementWizard createAcctDisburse = new CreateAccountDisbursementWizard(driver);
		createAcctDisburse.setCreateAccountDisbursementWizardAmount(amount);
		createAcctDisburse.setCreateAccountDisbursementWizardDueDate(dueDate);
		createAcctDisburse.setCreateAccountDisbursementWizardReasonFor(DisbursementReason.Other);
		createAcctDisburse.setAddressLine1("1st street");
		createAcctDisburse.setCity("Pocatello");
		createAcctDisburse.setZip("83202");
		createAcctDisburse.setState(State.Idaho);
		createAcctDisburse.clickNext();
		createAcctDisburse.clickFinish();
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements acctDisbmt = new AccountDisbursements(driver);
		acctDisbmt.clickRowInDisbursementsTableByText(StringsUtils.currencyRepresentationOfNumber(amount));
		acctDisbmt.clickAccountDisbursementsEdit();
		acctDisbmt.clickAccountDisbursementsReject();
	}

}
