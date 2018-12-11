package previousProgramIncrement.pi1_041918_062718.f131_CreateNewWayToEnterMoneyInBillingcenter;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
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
* @Requirement 	US14815 - Lienholder suspense payments* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-12%20Suspense%20Payment.xlsx?web=1">Suspense Payments</a>
* 
* @Requirement 	US15099 - Suspense Payment UI changes					
					Acceptance criteria:						
						Ensure that on the screen "Edit Suspense Payment" the field "Amount" is non-editable. (Req 12.F.24)
						Ensure that on the screen "New Suspense Payment" the field "Amount" is editable. (Req 12.F.3)
						Ensure that on the screen "Edit Suspense Payment" the field "Payment Method" is non-editable. (Req 12.F.25)
						Ensure that on the screen "New Suspense Payment" the field "Payment Method" is editable. (Req 12.F.4)
						Ensure that on the screen "Edit Suspense Payment" the field "County" is non-editable. (Req 12.F.28)
						Ensure that on the screen "Edit Suspense Payment" the field "Count Office" is non-editable. (Req 12.F.29)
						Ensure that on the screen "New Suspense Payment" the field "Policy #" is not available at all. This field should be hidden on this screen. (Req 12.F.14)
						Ensure that on the screen "Edit Suspense Payment" the field "Policy #" is available at the specified time in the requirements. (Req 12.F.14)
						Ensure that on the screen "New Suspense Payment" the field "Reference" name has been changed to "Reference/Loan #". (Req 12.F.13)
						Ensure that on the screen "Edit Suspense Payment" the field "Reference" name has been changed to "Reference/Loan #". (Req 12.F.26)
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-12%20Suspense%20Payment.xlsx?web=1">12-12 Suspense Payment</a>
* 
* 
** @Requirement 	US15161 - Set correct payment location when suspense payment is applied
						Create a new suspense payment
						Fill out the require information.
						For the field "Payment Method" select one of the following options: "Cash", "Check", "Check - Additional Interest", "Check - Cash Equivalent", or "Check - Title Company".
						Do not fill out the field "Count" or "County Office".
						Select your payer account.
						After creating the suspense payment apply it to the policy.
						Go the account that has the policy that the money was applied to.
						Click on the side bar "Payments".
						From there you will check the column "Payment Location".
					Acceptance criteria:
						Ensure that the Payment Location will display "BillingCenter" when the payment instrument reads "Cash", "Check", "Check - Additional Interest", "Check - Cash Equivalent", or "Check - Title Company" and there is no "County" selected when the money is applied. You will find this on the "Payments" screen after the money is applied. (Req 12-12-13)
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-12%20Suspense%20Payment.xlsx?web=1">12-12 Suspense Payment</a>
* 
* @DATE US14815, US15099 and US15161 were combined on October 8th, 2018
*/
public class US14815_15099_15161InsuredAndLHSuspensePayments extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private int LHSuspenseAmount = NumberUtils.generateRandomNumberInt(20, 150);
	private double insuredSuspenseAmount = NumberUtils.generateRandomNumberDigits(2);
	private int newSuspenseAmount = NumberUtils.generateRandomNumberInt(20, 100);
	private String lienholderNumber;
	private ARUsers arUser = new ARUsers();
	private String errorMessage = null;
	private WebDriver driver;
	private Config cf;
	private String errorMsg1 = "Amount : Missing required field";
    private String errorMsg2 = "Payer Account # : Missing required field";
    private CreateNewSuspensePaymentWizard suspensePmtWizard;
    private BCDesktopMenu desktopMenu;
    private DesktopSuspensePayments suspensePayment;
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
	private void generateLienholder() throws Exception {			
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
        lienholderNumber = myContactLienLoc1Obj.lienNumber+"-001-01";
        
        
        System.out.print("lienholder number is : "+ lienholderNumber);
	}
	private void generatePolicy() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)				
				.withInsFirstLastName("Edit", "Suspense")				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	//test US14814 Lienholder suspense payment test
	@Test
	public void createSuspensePaymentForLHAndReverse() throws Exception{
		
		cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
		generateLienholder();

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());	
		Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(currentDate);
		suspensePmtWizard.setSuspensePaymentAmount(LHSuspenseAmount);
		String selectedMethod = suspensePmtWizard.selectPaymentMethodRandomAndGetValue();
		if(selectedMethod.equals(PaymentInstrumentEnum.ACH_EFT.getValue()) || selectedMethod.equals(PaymentInstrumentEnum.Credit_Debit.getValue())){
			suspensePmtWizard.selectPaymentLocationRandom();
		}
		suspensePmtWizard.setAccountNumber(lienholderNumber);		
		suspensePmtWizard.clickCreate();					
		//verify the suspense payment
	
		assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Open, lienholderNumber, (double)LHSuspenseAmount), "didn't find the suspense payment with amount $"+LHSuspenseAmount+"for Lienholder "+lienholderNumber);;
		
		//reverse the suspense payment and verify
		suspensePayment.reversePayment(currentDate, null, SuspensePaymentStatus.Open, null,lienholderNumber, null, null, null, null, (double)LHSuspenseAmount, null, true);
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Reversed);
		assertTrue(suspensePayment.verifySuspensePayment(currentDate, null, SuspensePaymentStatus.Reversed, null, lienholderNumber, null, null, null, null, (double)LHSuspenseAmount, null, null), "didn't find the reversed payment with amount $"+LHSuspenseAmount);	
		assertTrue(findPaymentRow(suspensePayment, null, SuspensePaymentStatus.Reversed, lienholderNumber, (double)LHSuspenseAmount), "didn't find the Reversed suspense payment with amount $"+LHSuspenseAmount+" for Lienholder "+lienholderNumber);;
	}
	//test US15099 Edit Suspense Payment Screen
	@Test(dependsOnMethods = {"createSuspensePaymentForLHAndReverse"})
	public void verifyEditSuspensePayment() throws Exception {	
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
		generatePolicy();


        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));

        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create suspense payment
		suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
		suspensePmtWizard.setSuspensePaymentAmount(insuredSuspenseAmount);
		suspensePmtWizard.setAccountNumber(myPolicyObj.fullAccountNumber);
		suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);		
		
		suspensePmtWizard.clickCreate();		
		
		GuidewireHelpers gwHelper = new GuidewireHelpers(driver);
	        if (new GuidewireHelpers(driver).errorMessagesExist()) {
	            errorMessage = gwHelper.getFirstErrorMessage();
	            int count = 0;
	            while (errorMessage.contains(errorMsg1) || errorMessage.contains(errorMsg2) && count < 10) {
	               
	                if (errorMessage.contains(errorMsg1)) {
	                	suspensePmtWizard.setSuspensePaymentAmount(newSuspenseAmount);
	                	suspensePmtWizard.clickCreate();	
	                    try {
	                        errorMessage = gwHelper.getFirstErrorMessage();
	                    } catch (Exception e) {
	                        break;
	                    }
	                }
	
	                if (errorMessage.contains(errorMsg2)) {
	                	suspensePmtWizard.setAccountNumber(myPolicyObj.fullAccountNumber);
	                	suspensePmtWizard.clickCreate();
	                	try {
	                        errorMessage = gwHelper.getFirstErrorMessage();
	                    } catch (Exception e) {
	                        break;
	                    }
	                }
	                count++;
	            }
	        }
		
		//verify the suspense payment	
	    assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Open, myPolicyObj.accountNumber, (double)insuredSuspenseAmount), "didn't find the suspense payment with amount $"+insuredSuspenseAmount+"for account "+myPolicyObj.accountNumber);;
	    //verify the policy is sent to BC before Edit the suspense payment
	    BCSearchAccounts bcSearch = new BCSearchAccounts(driver);
	    bcSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);
	    BCAccountMenu accountMenu = new BCAccountMenu(driver);
	    accountMenu.clickBCMenuCharges();
	    AccountCharges charge = new AccountCharges(driver);
	    charge.waitUntilChargesFromPolicyContextArrive(30, TransactionType.Issuance);
	    
	    BCTopMenu topMenu = new BCTopMenu(driver);
	    topMenu.clickDesktopTab();
	    desktopMenu.clickDesktopMenuSuspensePayments();
	    //test Edit Suspense Payment screen
        suspensePayment.clickEditInRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, insuredSuspenseAmount, null);
		try{
			suspensePmtWizard.setSuspensePaymentAmount(newSuspenseAmount);
			double amountFromUI = suspensePmtWizard.getSuspenseAmount();
			System.out.println("the suspense amount is :" + amountFromUI);
			if(amountFromUI != insuredSuspenseAmount)
				Assert.fail("Amount should be not editable in Edit Suspense Payment screen.");
		}catch (Exception e) {
			getQALogger().info("Amount is not editable which is expected.");
		}
		
		try{
			suspensePmtWizard.selectPaymentMethodRandom();
			Assert.fail("Payment Method should be not editable in Edit Suspense Payment screen.");
		}catch (Exception e) {
			getQALogger().info("Payment Method is not editable which is expected.");
		}
		
		try{
			suspensePmtWizard.selectPaymentCountyRandom();
			Assert.fail("County should be not editable in Edit Suspense Payment screen.");
		}catch (Exception e) {
			getQALogger().info("County is not editable which is expected.");
		}
		try{
			suspensePmtWizard.selectPaymentCountyOfficeRandom();
			Assert.fail("County Office should be not editable in Edit Suspense Payment screen.");
		}catch (Exception e) {
			getQALogger().info("County Office is not editable which is expected.");
		}
		suspensePmtWizard.selectPolicyNumber(myPolicyObj.accountNumber);
		suspensePmtWizard.clickUpdateAndApply();
		//verify applied suspense payment
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Applied);		
		int pageNumber=0;
		boolean foundRow = false;
		while(pageNumber<suspensePayment.getNumberPages() && !foundRow){
			try{
				suspensePayment.getSuspensePaymentTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Applied, null, myPolicyObj.accountNumber, null, null, null, null, (double)insuredSuspenseAmount, null);
				foundRow = true;
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				suspensePayment.goToNextPage();
			}			
		}
		assertTrue(foundRow, "didn't find the suspense payment with amount $" + insuredSuspenseAmount + " for "+ myPolicyObj.accountNumber);
    }
	@Test(dependsOnMethods = {"verifyEditSuspensePayment"})	
	public void applySuspensePaymentAndVerifyPaymentLocation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
        suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);		
		suspensePmtWizard.setSuspensePaymentDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter));
		suspensePmtWizard.setSuspensePaymentAmount(newSuspenseAmount);		
		suspensePmtWizard.setAccountNumber(myPolicyObj.fullAccountNumber);
		String selectedMethod = suspensePmtWizard.selectPaymentMethodRandomAndGetValue();
		while(selectedMethod.equals(PaymentInstrumentEnum.ACH_EFT.getValue()) || selectedMethod.equals(PaymentInstrumentEnum.Credit_Debit.getValue())){
			selectedMethod = suspensePmtWizard.selectPaymentMethodRandomAndGetValue();
		}		
		suspensePmtWizard.clickCreate();		
		
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Open, myPolicyObj.accountNumber, (double)newSuspenseAmount), "didn't find the suspense payment with amount $"+newSuspenseAmount);
		
		suspensePayment.clickApplyInRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)newSuspenseAmount, null);
		suspensePayment.clickApplyButton();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		//verify applied payment		
		suspensePayment.setSuspensePaymentStatus(SuspensePaymentStatus.Applied);

		assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Applied, myPolicyObj.accountNumber, (double)newSuspenseAmount), "didn't find the applied suspense payment with amount $"+newSuspenseAmount);

		//verify account payment screen
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPaymentsPayments();
        AccountPayments payment = new AccountPayments(driver);
		assertTrue(payment.verifyPaymentAndClick(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, null, null, null, null, PaymentLocation.BillingCenter, null, "Default", (double)newSuspenseAmount, null, null), "didn't find the expected payment with amount $"+newSuspenseAmount);
		}
}
