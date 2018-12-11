package regression.r2.noclock.billingcenter.charges;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class TestMembershipExpirationDateChange extends BaseTest {
	private ARUsers arUser;
    private GeneratePolicy myPolicyObj;
    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
		
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Membership", "Charges")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
    }
    
    @Test(dependsOnMethods = { "generatePolicy" })
	public void makePolicyCashDownPayment() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
    	try {
			this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		} catch (Exception e) {
			System.out.println(e);
			Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
		}
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		BCAccountMenu accountMenu= new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		BCCommonCharges accountCharges = new BCCommonCharges(driver);
		accountCharges.waitUntilIssuanceChargesArrive();
		
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
    }
    
    @Test(dependsOnMethods = { "makePolicyCashDownPayment" })
	public void changeExpirationDate() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), new GuidewireHelpers(driver).getPolicyNumber(myPolicyObj));
    	
    	StartPolicyChange policyChange = new StartPolicyChange(driver);
    	
    	Date dateToChangeTo = DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Month, 1);
    	policyChange.changeExpirationDate(DateUtils.dateAddSubtract(dateToChangeTo, DateAddSubtractOptions.Month, 1), "Change Expiration Date");
    }
    
    @Test(dependsOnMethods = { "changeExpirationDate" })
	public void verifyThatNoChargesAreSentToBC() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
    	
    	BCAccountMenu accountMenu= new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		BCCommonCharges charges = new BCCommonCharges(driver);
		try {
			charges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), myPolicyObj.accountNumber, null, null, TransactionType.Policy_Change, "Membership Charg", null, null, null, null, null, null, null, null, null, null);
			Assert.fail("A charge appeared for the expiration date change on the membership dues. This should not happen.");
		} catch (Exception e) {
			System.out.println("There was not a charge sent to BC from the policy change for the membership dues. This is expected.");
		}
	}
}
