package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Triton;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	DE7981 -- BC-Cancels generate when applying suspense payments
* @Requirements 
* 				Acceptance criteria:
					Ensure that a cancel does not trigger when a payment is applied through the Suspense Payment. (Req. 12-12-9.1)
				Steps to get there:
					Create a payment to the''suspense payments" screen before the policy is submitted.
					A trouble ticket opens when the policy is submitted.
					Move the clock a few days run invoice and invoice due
					Apply the payment to the policy from the �suspense payments� screen.
					When payment is applied, it closes the trouble ticket
					The payment is sitting in unapplied funds.
					A cancel generates because it does not see the money in UF
					I distributed the payment onto the policy and a rescind generates
				Actual: Money applied from suspense goes into Unapplied Funds
				Expected: Money applied from suspense should go directly to the policy.
* @DATE October 22, 2018*/
@Test(groups = {"ClockMove"})
public class DE7981BCCancelsGenerateWhenApplyingSuspensePayments extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	private Date currentDate;	
	private WebDriver driver;
	private Config cf;
	private double suspenseAmount = NumberUtils.generateRandomNumberInt(500, 1000);
	
	private boolean findPaymentRow(DesktopSuspensePayments suspensePayment, Date date, SuspensePaymentStatus status, String accountNumber, double amount){
		int pageNumber=0;
		boolean foundRow = false;
		while(pageNumber<suspensePayment.getNumberPages() && !foundRow){
			try{
				suspensePayment.getSuspensePaymentTableRow(date, null, status, null, accountNumber, null, null, null, null, (double)amount, null);
				foundRow = true;
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				suspensePayment.goToNextPage();
			}			
		}
		return foundRow;
	}
	private void generatePolicy() throws Exception {		
		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();

		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
		// Farm Equipment
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
				"Manly Farm Equipment", 100000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

	
        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Apply", "SuspPmt")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);       
	}

	@Test
	public void createSuspensePaymetAndIssuePolicy() throws Exception {	
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);		
		generatePolicy();
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);		

		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
        suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		suspensePmtWizard.setSuspensePaymentAmount(suspenseAmount);
		suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);		
		suspensePmtWizard.setAccountNumber(myPolicyObj.accountNumber+"-001");	
		suspensePmtWizard.clickCreate();
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, null, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, suspenseAmount), "Couldn't find the suspense payment with amount of $"+suspenseAmount);
		//issue the policy
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
	}
    @Test(dependsOnMethods = {"createSuspensePaymetAndIssuePolicy"})
	public void waitTTAndRunBatchProcesses() throws Exception {	
    	cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);	
        BCPolicyMenu bcPolicyMenu = new BCPolicyMenu(driver);
        bcPolicyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTicket tt = new BCCommonTroubleTicket(driver);
        tt.waitForTroubleTicketsToArrive(60);
        bcPolicyMenu.clickTopInfoBarAccountNumber();
        
        //move clock and run batch processes
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 3);
        BatchHelpers batchHelper = new BatchHelpers(driver);
        batchHelper.runBatchProcess(BatchProcess.Invoice);
        batchHelper.runBatchProcess(BatchProcess.Invoice_Due);
    }        //edit and apply suspense payment
    @Test(dependsOnMethods = {"waitTTAndRunBatchProcesses"})
   	public void editAndApplySuspensePayment() throws Exception {	
       	cf = new Config(ApplicationOrCenter.BillingCenter);
   		driver = buildDriver(cf);
   		
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuSuspensePayments();
        DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);		
		suspensePayment.clickEditInRow(null, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, suspenseAmount, null);
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);		
		suspensePmtWizard.selectPolicyNumber(myPolicyObj.accountNumber);
		suspensePmtWizard.clickUpdateAndApply();
		
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Applied);	
		desktopMenu.clickDesktopMenuMyActivities();
		desktopMenu.clickDesktopMenuSuspensePayments();
		assertTrue(findPaymentRow(suspensePayment, currentDate,SuspensePaymentStatus.Applied, myPolicyObj.accountNumber, suspenseAmount), "didn't find the suspense payment with amount $"+myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount());
		//verify in account payment screen		
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPaymentsPayments();
        AccountPayments payment = new AccountPayments(driver);
		assertTrue(payment.verifyPaymentAndClick(null, null, null, null, null, null, null, null, null, myPolicyObj.accountNumber, suspenseAmount, null, null), "didn't find the expected payment with amount $"+suspenseAmount);
		//verify the Due amount is paid
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		invoice.verifyInvoice(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Due, myPolicyObj.standardInlandMarine.getPremium().getDownPaymentAmount(), 0.0);		
    }
}
