package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
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
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author sreddy
 * @Requirement : https://rally1.rallydev.com/#/68999021356d/detail/defect/106060352512
 * @Description 1. Issue a Quarterly Squaire/SEMIANNUAL Policy and make down payment
 *              2. Delinquent the Last Invoice and make a Positive policy change(Increase Coverage)
 *              3. Verify that the positive policy change do not exit the Delinquency
 * @DATE June 2, 2017
 */
public class PositivePolicyChangeCausingNonPayDelinquencyToExit extends BaseTest {
    private GeneratePolicy mySQPolicyObjPL = null;
    private WebDriver driver;
	private Underwriters uw;
	private ARUsers arUser ;

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


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.Country)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("DE5279", "x")
                .withPaymentPlanType(PaymentPlanType.Semi_Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println(mySQPolicyObjPL.squire.getPolicyNumber());

    }


	@Test (dependsOnMethods = { "generatePolicy" }) 
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySQPolicyObjPL, mySQPolicyObjPL.squire.getPremium().getDownPaymentAmount(), mySQPolicyObjPL.squire.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
    public void makeFirstInvoiceDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySQPolicyObjPL.accountNumber);

		int xx = 170;
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, xx);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySQPolicyObjPL, mySQPolicyObjPL.squire.getPremium().getDownPaymentAmount() * 0.7, mySQPolicyObjPL.squire.getPolicyNumber());
		xx = 20;
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, xx);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null,startDatex), "Delinquency Expected");

	}


    @Test(dependsOnMethods = {"makeFirstInvoiceDue"})
	public void positivePolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.squire.getPolicyNumber());
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changePLPropertyCoverage(200000, currentSystemDate);

    }

	@Test (dependsOnMethods = { "positivePolicyChange" })
    public void verifyDelinquencyNotExitedAfterPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), mySQPolicyObjPL.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies acctDelinquency = new BCCommonDelinquencies(driver);
		Date startDatex = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyByReason(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, null,startDatex), "Delinquency Not Closed As Expected");

    }

}
