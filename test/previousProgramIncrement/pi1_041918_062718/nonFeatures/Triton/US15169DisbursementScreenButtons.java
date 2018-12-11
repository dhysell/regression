package previousProgramIncrement.pi1_041918_062718.nonFeatures.Triton;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopDisbursements;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.wizards.CreateDisbursementWizard;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;

/**
* @Author JQU
* * @Requirement 	US15169 - Set correct payment location when suspense payment is applied						
					Acceptance criteria:						
						On the "Desktop" and on the screen "Disbursements"
							Ensure that when a regular disbursement is selected only display the following buttons: "Update", "Cancel", "Approve", "Reject", "Reject And Hold Future Automatic Disbursements", and "Void". (Req 07-06-14)
							Ensure the when a suspense disbursement is selected only display the following buttons: "Update", "Cancel", "Approve", "Reject", "Reject And Hold Future Automatic Disbursements", and "Void". (Req 07-06-14)
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-06%20Suspense%20Payment%20Free-Form%20Disbursements.xlsx?web=1">12-12 Suspense Payment</a>
* @DATE May 20, 2018*/
@Test(groups = {"ClockMove"})
public class US15169DisbursementScreenButtons extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private double suspenseAmount = NumberUtils.generateRandomNumberDigits(2);
	private int regularDisburseAmount = NumberUtils.generateRandomNumberInt(20, 100);
	private Config mycf;
	private WebDriver myDriver;
	
	private void generatePolicy() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;
        myPolicyObj = new GeneratePolicy.Builder(myDriver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)				
				.withInsFirstLastName("Buttons", "Disbursement")				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);        
	}	
	@Test
	public void createRegularDisbursementAndVerify() throws Exception {	
		mycf = new Config(ApplicationOrCenter.PolicyCenter);
		myDriver = buildDriver(mycf);
		generatePolicy();
		
		myDriver.get(mycf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(myDriver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);			
		BCAccountMenu accountMenu = new BCAccountMenu(myDriver);
		//pay down payment with extra amount
		new BatchHelpers(myDriver).runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(myDriver);
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.standardFire.getPremium().getDownPaymentAmount()+regularDisburseAmount, myPolicyObj.accountNumber);
		//trigger disbursement
		new BatchHelpers(myDriver).runBatchProcess(BatchProcess.Automatic_Disbursement);
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(myDriver);
		assertTrue(disbursement.verifyDisbursement(null, null, null, myPolicyObj.accountNumber, null, null, (double)regularDisburseAmount, null, null, null), "Didn't find the regular disbursement.");
		disbursement.clickEdit();
		disbursement.setDisbursementsDueDate(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter));
		disbursement.clickUpdate();
		disbursement.clickEdit();
		disbursement.clickCancel();
		disbursement.clickEdit();
		disbursement.clickAccountDisbursementsApprove();
		
		new BatchHelpers(myDriver).runBatchProcess(BatchProcess.Disbursement);
		disbursement.clickEdit();
		disbursement.clickAccountDisbursementsVoid();		
		ClockUtils.setCurrentDates(myDriver, DateAddSubtractOptions.Day, 1);
	}
	
	@Test(dependsOnMethods = { "createRegularDisbursementAndVerify" })
    public void createSuspensePaymentToDisburse() throws Exception {
		mycf = new Config(ApplicationOrCenter.BillingCenter);
        myDriver = buildDriver(mycf);
		new Login(myDriver).login(arUser.getUserName(), arUser.getPassword());	
		BCDesktopMenu desktopMenu = new BCDesktopMenu(myDriver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create suspense payment
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(myDriver);
		suspensePayment.clickNew();
		CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(myDriver);		
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(myDriver);
		suspensePmtWizard.setSuspensePaymentDate(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter));
		suspensePmtWizard.setSuspensePaymentAmount(suspenseAmount);
		suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);		
		suspensePmtWizard.setAccountNumberFromPicker(myPolicyObj.accountNumber);		
		suspensePmtWizard.clickCreate();		
		
		//verify the suspense payment
        assertTrue(suspensePayment.verifySuspensePayment(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, suspenseAmount, null, null), "didn't find the suspense payment with amount $" + suspenseAmount);
		//create disbursement and verify
        suspensePayment.setCheckBox(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, suspenseAmount, null, true);
		suspensePayment.clickCreateDisbursement();
		CreateDisbursementWizard disbursementWizard = new CreateDisbursementWizard(myDriver);
		disbursementWizard.setDisbursementDueDate(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter));
		disbursementWizard.selectReasonForDisbursement(DisbursementReason.Other);
		disbursementWizard.clickNext();
		disbursementWizard.clickFinish();
		//verify the disbursement buttons
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Disbured);
		suspensePayment.goToLastPage();
        assertTrue(suspensePayment.verifySuspensePayment(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Disbured, null, null, null, null, null, null, suspenseAmount, null, null), "didn't find the disbursed suspense payment with amount $" + suspenseAmount);
        suspensePayment.clickApplyToLinkInRow(DateUtils.getCenterDate(myDriver, ApplicationOrCenter.BillingCenter), null, null, null, null, null, null, null, suspenseAmount, null);
		DesktopDisbursements disbursement = new DesktopDisbursements(myDriver);
		disbursement.clickEdit();
		disbursement.clickDesktopDisbursementsReject();
		new GuidewireHelpers(myDriver).selectOKOrCancelFromPopup(OkCancel.OK);
	}
}
