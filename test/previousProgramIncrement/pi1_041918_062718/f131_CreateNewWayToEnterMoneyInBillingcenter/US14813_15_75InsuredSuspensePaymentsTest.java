package previousProgramIncrement.pi1_041918_062718.f131_CreateNewWayToEnterMoneyInBillingcenter;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.CreateDisbursementWizard;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Requirement US14813 - Insured suspense payments
 * US14815 - Reverse suspense payments
 * US14875 - Apply Suspense Payments to County, NPP, and ACH
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-12%20Suspense%20Payment.xlsx?web=1">Suspense Payments</a>
 * @DATE April 24, 2018
 */
public class US14813_15_75InsuredSuspensePaymentsTest extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	private int suspenseAmount1 = NumberUtils.generateRandomNumberInt(20, 50);
	private int suspenseAmount2 = NumberUtils.generateRandomNumberInt(51, 70);
	private int suspenseAmount3 = NumberUtils.generateRandomNumberInt(81, 100);
	private Date currentDate;	
	private String refLoanNumber = "Whatever";
	private WebDriver driver;

	private void createSuspensePayment(CreateNewSuspensePaymentWizard suspensePmtWizard, Date paymentDate, double amount, String accountNumber, String policyNumber){
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(paymentDate);
		suspensePmtWizard.setSuspensePaymentAmount(amount);
		suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);

		if(policyNumber != null){
			suspensePmtWizard.setAccountNumberFromPicker(accountNumber);
			suspensePmtWizard.selectPolicyNumber(policyNumber);
		}else{
			suspensePmtWizard.setAccountNumber(accountNumber);
		}
	
		suspensePmtWizard.clickCreate();
	}
	private void verifyPaymentInAccountPaymentScreen(Date date, String unappliedFund, double amount){
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPaymentsPayments();
        AccountPayments payment = new AccountPayments(driver);
		assertTrue(payment.verifyPaymentAndClick(currentDate, currentDate, null, null, null, null, null, null, null, unappliedFund, amount, null, null), "didn't find the expected payment with amount $"+amount);
	}	 
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
	private GeneratePolicy generatePolicy() throws Exception {		
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
                .withInsFirstLastName("Payment", "Suspense")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.QuickQuote);
        return myPolicyObj;
	}

	@Test
	public void createSuspensePaymetOnQuotedPolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
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
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount1, myPolicyObj.accountNumber+"-001", null);
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, (double)suspenseAmount1), "Couldn't find the suspense payment with amount of $"+suspenseAmount1);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
	}
    @Test(dependsOnMethods = {"createSuspensePaymetOnQuotedPolicy"})
	public void editAndApplySuspensePayment() throws Exception {	
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);		

        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		//wait for the account to come to BC
		int i=0;
		boolean foundAccount = false;
		while(!foundAccount && (i<50)){
			try{
				BCSearchAccounts search = new BCSearchAccounts(driver);
				search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
				foundAccount =true;
			}catch (Exception e) {
				i++;
			}
		}
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
        DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, suspenseAmount1), "Couldn't find the suspense payment with amount of $"+suspenseAmount1 +"for account "+ myPolicyObj.accountNumber);
		//edit, and applied payment created on quoted policy
		suspensePayment.clickEditInRow(currentDate, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)suspenseAmount1, null);
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentReferenceLoanNumber(refLoanNumber);
		suspensePmtWizard.selectPolicyNumber(myPolicyObj.accountNumber);
		suspensePmtWizard.clickUpdateAndApply();
		
		/////need to remove the following two lines after a BC suspense payment screen issue is fixed
		desktopMenu.clickDesktopMenuMyActivities();
		desktopMenu.clickDesktopMenuSuspensePayments();
		
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Applied);	
		assertTrue(findPaymentRow(suspensePayment, currentDate,SuspensePaymentStatus.Applied, myPolicyObj.accountNumber, suspenseAmount1), "didn't find the suspense payment with amount $"+suspenseAmount2);
		//verify in account payment screen		
		verifyPaymentInAccountPaymentScreen(currentDate, myPolicyObj.accountNumber, (double)suspenseAmount1);
	}

    @Test(dependsOnMethods = { "editAndApplySuspensePayment" })
	public void createSuspensePaymentAndApply() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount3, myPolicyObj.fullAccountNumber, null);		
		
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, suspenseAmount3), "didn't find the suspense payment with amount $"+suspenseAmount3);
		suspensePayment.clickApplyInRow(currentDate, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)suspenseAmount3, null);
		suspensePayment.clickApplyButton();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		//verify applied payment		
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Applied);
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Applied, myPolicyObj.accountNumber, suspenseAmount3), "didn't find the applied suspense payment with amount $"+suspenseAmount3);
		//verify in account payment screen
		verifyPaymentInAccountPaymentScreen(currentDate, "Default", (double)suspenseAmount3);
	}
	@Test(dependsOnMethods = { "createSuspensePaymentAndApply" })
	public void createSuspensePaymentToReverse() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObj = generatePolicy();
		myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount1, myPolicyObj.fullAccountNumber, null);
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, suspenseAmount1), "didn't find the suspense payment with amount $"+suspenseAmount1);
		//reverse the suspense payment and verify
		suspensePayment.reversePayment(currentDate, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)suspenseAmount1, null, true);
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Reversed);
		
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Reversed, myPolicyObj.accountNumber, suspenseAmount1), "didn't find the reversed suspense payment with amount $"+suspenseAmount1);		
	}
	@Test(dependsOnMethods = { "createSuspensePaymentToReverse" })
	public void createSuspensePaymentToDisburse() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount1, myPolicyObj.fullAccountNumber, null);
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Open, myPolicyObj.accountNumber, suspenseAmount1), "didn't find the suspense payment with amount $"+suspenseAmount1);
				
		//create disbursement and verify
		suspensePayment.setCheckBox(currentDate, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)suspenseAmount1, null, true);
		suspensePayment.clickCreateDisbursement();
		CreateDisbursementWizard disbursementWizard = new CreateDisbursementWizard(driver);
		disbursementWizard.setDisbursementDueDate(currentDate);
		disbursementWizard.selectReasonForDisbursement(DisbursementReason.Other);
		disbursementWizard.clickNext();
		disbursementWizard.clickFinish();
		//verify the disbursement
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Disbured);
		assertTrue(findPaymentRow(suspensePayment, currentDate, SuspensePaymentStatus.Disbured, null, suspenseAmount1), "didn't find the disbursed suspense payment with amount $"+suspenseAmount1);		
	}	
}
