package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentRestriction;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement There were some significant changes to the Policy Summary Screen in BC. This test makes sure they stay that way.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/04%20-%20Policy%20Summary%20Screen/04-01%20Policy%20Summary%20Screen.docx">Screen Requirements and Mockup</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/13%20-%20Policy%20Payment%20Instrument/17-13%20Policy%20Payment%20Instrument.docx">Payment Requirements and Mockup</a>
 * @Description
 * @DATE Nov 6, 2015
 */
public class TestPolicySummaryInformation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL = null;
	private String accountNumber = "";
	public ARUsers arUser = new ARUsers();

    @Test
    private void generatePolicy() throws Exception {
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

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("ABC", "x")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }


	@Test (dependsOnMethods = { "generatePolicy" }) 
	public void createNewActivityAtAccountLevel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		//this.accountNumber = invoiceSearch.searchForAccountByInvoiceAndAmountRange("00", 0, 1500, InvoiceStatus.Billed);
		this.accountNumber = mySQPolicyObjPL.accountNumber;
		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		policySearch.searchPolicyByAccountNumber(this.accountNumber);


        try {

        } catch (Exception e) {
			Assert.fail("The dropdown for the Payment Restriction on the summary page was not available. This should not be the case.");
		}

        BCPolicySummary policySummary = new BCPolicySummary(driver);
		try {
			policySummary.clickPaymentRestrictionArrow();
			policySummary.clickPaymentRestrictionUpdate();
		} catch (Exception e) {
			Assert.fail("The update option for the Payment Restriction on the summary page was not available. This most likely caused because of a missing invoice stream on the insured. Please investigate.");
		}

		policySummary.setPaymentRestriction(PaymentRestriction.None);
		policySummary.setPaymentRestriction(PaymentRestriction.None_One_NSF);
		policySummary.setPaymentRestriction(PaymentRestriction.Cash_Only);
		policySummary.setPaymentRestriction(PaymentRestriction.Cash_Only_Warning);
		policySummary.clickCancel();

        policySummary.clickEdit();


        policySummary.setPaymentInstrumentToResponsive();
		policySummary.setNewPaymentInstrument();

        policySummary.verifyWriteOffAmountsMatch();
		policySummary.getDelinquentAmount();

        policySummary.clickCancel();

        new GuidewireHelpers(driver).logout();
	}

}
