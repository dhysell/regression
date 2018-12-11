package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import regression.r2.noclock.policycenter.submission_bop.TestBind;
public class VerifyPayments extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjInsuredOnly = null;

	@Test
	public void Generate() throws Exception {

		TestBind bound = new TestBind();
		bound.testBasicBindInsuredOnly();
		this.myPolicyObjInsuredOnly = bound.myPolicyObjInsuredOnly;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObjInsuredOnly.convertTo(driver, GeneratePolicyType.PolicyIssued);


	}

	@Test(dependsOnMethods = { "Generate" })
	public void makePolicyCashDownPayment() throws Exception {
		billingCenterLoginAndFindPolicy(myPolicyObjInsuredOnly);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);	

		if (!(myPolicyObjInsuredOnly.downPaymentType == PaymentType.ACH_EFT)
                || !(myPolicyObjInsuredOnly.downPaymentType == PaymentType.Credit_Debit) || !(myPolicyObjInsuredOnly.paymentPlanType == PaymentPlanType.Monthly)) {
            BCAccountMenu accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();

            NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
            newPayment.makeDirectBillPaymentExecute(myPolicyObjInsuredOnly.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObjInsuredOnly.busOwnLine.getPolicyNumber());

            new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
        }

		new GuidewireHelpers(driver).logout();
	}

    private void billingCenterLoginAndFindPolicy(GeneratePolicy policy) throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		Login loginPage = new Login(driver);
		loginPage.login("sbrunson", "gw");

		BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
		accountTopMenu.menuAccountSearchAccountByAccountNumber(policy.accountNumber);
	}

	public boolean testNPPPayments(PaymentType type, PaymentInstrumentEnum instrument) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy squirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
			.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
			.withInsFirstLastName("Test", "Section1")
			.withPaymentPlanType(PaymentPlanType.Annual)
			.withDownPaymentType(type)
			.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

        cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
	    billingCenterLoginAndFindPolicy(squirePolicyObjPL);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
	    accountMenu.clickAccountMenuPayments();
		AccountPayments paymentsPage = new AccountPayments(driver);
	    paymentsPage.waitUntilBindPaymentsArrive(100);
        return paymentsPage.verifyPayment(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), instrument, squirePolicyObjPL.squire.getPremium().getDownPaymentAmount());

    }

	@Test
	public void testCreditCardPayments() throws Exception {
		if(!testNPPPayments(PaymentType.Credit_Debit, PaymentInstrumentEnum.Credit_Debit)) {
			Assert.fail("The Credit Card Payment should exist in BillingCenter");
		}
	}

    @Test
	public void testACHPayments() throws Exception {
		if(!testNPPPayments(PaymentType.ACH_EFT, PaymentInstrumentEnum.ACH_EFT)) {
			Assert.fail("The Credit Card Payment should exist in BillingCenter");
		}	    
	}

}
