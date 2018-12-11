package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.wizards.NewCreditWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreditType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;

/**
* @Author JQU
* @Requirement 	US14176 -- Voiding a "bank fee" should not go into unapplied funds - Spike research reversing adhoc credit with a neg credit (Aurelia is going to Vinod)
* 							Log into BillingCenter.
							Click into an insured account.
							Click the "Actions" button > "Credit".
							For the field "Credit Type" select "Bank Fee" and enter in your amount.
							Click the "Next" button then click the "Finish" button.
							Log in a Tina Nelson (tnelson) because has permission to void.
							Go back to the account that you just created.
							Go to the screen "Disbursements" located on the side bar.
							Click on the disbursement you just created.
							Click the "Edit" button.
							Then click the "Void" button.
* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame2.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/11%20-%20Ad%20Hoc%20Credit%20Transactions/17-11%20Ad%20Hoc%20Credit.docx&action=default">17-11 Ad Hoc Credit</a>
* @DATE August 15, 2018
*/
public class US14176VoidingBankFeeShouldNotGoIntoUnappliedFunds extends BaseTest{
	private GeneratePolicy myPolicyPL = null;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	private double bankFee = NumberUtils.generateRandomNumberInt(50, 200);
	private String specialUserName = "tnelson"; //she has permission to void disbursement.
	private void generatePolicy() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();			
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();		
		
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("BankFee", "Voided")
				.build(GeneratePolicyType.PolicyIssued);	
        driver.quit();
	}
	@Test
	public void createBankFee() throws Exception {		
		generatePolicy();
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyPL.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuActionsNewTransactionCredit();
		NewCreditWizard creditWizard = new NewCreditWizard(driver);
		creditWizard.setCreditType(CreditType.Bank_Fee);
		creditWizard.setAmount(bankFee);
		creditWizard.clickNext();
		creditWizard.clickFinish();
		//verify disbursement
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		assertTrue(disbursement.verifyDisbursement(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, myPolicyPL.accountNumber, null, null, bankFee, null, null, null), "The bank fee of $" +
		bankFee + " should be disbursed.");		
	}
	@Test(dependsOnMethods = {"createBankFee"}) 
	public void voidBankFeeAndVerify() throws Exception {	        
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(specialUserName, arUser.getPassword(), myPolicyPL.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		disbursement.clickEdit();
		disbursement.clickAccountDisbursementsVoid();
		//click ok on Void Disbursement screen
		disbursement.clickOK();
		//verify that the voided Bank fee is not going to unapplied fund
		accountMenu.clickBCMenuSummary();
		BCAccountSummary summary = new BCAccountSummary(driver);
		
		System.out.println("policy number is : "+ myPolicyPL.squire.getPolicyNumber());
		
		if(summary.getUnappliedFundByPolicyNumber(myPolicyPL.squire.getPolicyNumber())>0){
			Assert.fail("Voided BankFee went to policy's unapplied fund which is not expected.");
		}
	}
}
