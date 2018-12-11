package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sreddy
* @Requirement : DE4542PaymentRequestNotAnOptionOnAccountActionsMenu 
* @Description 1. Issue a Quarterly Squaire Policy and make down payment
*              2. Cancel the policy in PC
*              3. verify the availability of the menu option for different roles
* @DATE April 5, 2017
*/
@QuarantineClass
public class DE4542PaymentRequestNotAnOptionOnAccountActionsMenu extends BaseTest {
	private WebDriver driver;
	 private GeneratePolicy mySQPolicyObjPL = null;

	private ARUsers arUser ;
	
	
 @Test 
 private void generatePolicy() throws  Exception{
		 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		 driver = buildDriver(cf);


		 ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
			locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
			PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
			locToAdd.setPlNumAcres(11);
			locationsList.add(locToAdd);
			
			SquireLiability myLiab = new SquireLiability();
			myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

     SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
     myPropertyAndLiability.locationList = locationsList;
     myPropertyAndLiability.liabilitySection = myLiab;

     SquireInlandMarine myInlandMarine = new SquireInlandMarine();

     Squire mySquire = new Squire(SquireEligibility.Country);
     mySquire.propertyAndLiability = myPropertyAndLiability;
     mySquire.inlandMarine = myInlandMarine;

     mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withInsFirstLastName("DE4542", "x")
					.withPaymentPlanType(PaymentPlanType.Quarterly)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			
	}
 
 
	@Test (dependsOnMethods = { "generatePolicy" }) 
	public void makeInsuredDownPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySQPolicyObjPL, mySQPolicyObjPL.squire.getPremium().getDownPaymentAmount(), mySQPolicyObjPL.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();	
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
    public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Messup on Agent's Part", null,true);
		new GuidewireHelpers(driver).logout();
	}

	
	@Test (dependsOnMethods = { "CancelPolicyInPolicyCenter" }) 
	public void VerifyPaymentRequestOptionForBillingManager() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		
		try {
			
			accountMenu.clickAccountMenuActionsNewPaymentRequest();
		} catch (Exception e) {
			Assert.fail("Payment Request Link not found on Cancellation");
		}

		new GuidewireHelpers(driver).logout();	
	}


	@Test (dependsOnMethods = { "VerifyPaymentRequestOptionForBillingManager" }) 
	public void VerifyPaymentRequestOptionForClerical() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		
		try {
			
			accountMenu.clickAccountMenuActionsNewPaymentRequest();
		} catch (Exception e) {
			Assert.fail("Payment Request Link not found on Cancellation");
		}

		new GuidewireHelpers(driver).logout();	
	}

	@Test (dependsOnMethods = { "VerifyPaymentRequestOptionForClerical" }) 
	public void VerifyPaymentRequestOptionForClericalAdvanced() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical_Advanced, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		
		try {
			
			accountMenu.clickAccountMenuActionsNewPaymentRequest();
		} catch (Exception e) {
			Assert.fail("Payment Request Link not found on Cancellation");
		}

		new GuidewireHelpers(driver).logout();	
	}
	
}
