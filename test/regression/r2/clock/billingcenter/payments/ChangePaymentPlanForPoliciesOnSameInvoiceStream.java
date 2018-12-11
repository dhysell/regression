package regression.r2.clock.billingcenter.payments;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE4760 Changing payment plan on lead policy doesn't fall down to tag along policy
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/19%20-%20UI%20-%20Policy%20Screens/19-04%20Payment%20Schedule%20-%20Change%20Payment%20Plan.docx">Link Text</a>
 * @DATE Mar 28, 2017
 */
public class ChangePaymentPlanForPoliciesOnSameInvoiceStream extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj = null;
    public ARUsers arUser;
    private BCAccountMenu acctMenu;
    SoftAssert softAssert;

    @Test
    public void issueSquire() throws Exception {
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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("DE4760", "Test")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(myPolicyObj.squire.getPolicyNumber());

    }

    @Test(dependsOnMethods = {"issueSquire"})
    public void downpaymentSquire() throws Exception {
        arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
        //Moving clock
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 60);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"downpaymentSquire"})
    public void issueUmbrella() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        myPolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        getQALogger().info(myPolicyObj.squireUmbrellaInfo.getPolicyNumber());

    }

    @Test(dependsOnMethods = {"issueUmbrella"})
    public void changePaymentPlan() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        //Checking invoices before changing payment plan
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        AccountInvoices tableData = new AccountInvoices(driver);
        WebElement invoiceTable = tableData.getAccountInvoicesTable();
        int invoiceCountQuat = new TableUtils(driver).getRowCount(invoiceTable);
        System.out.println("Number of invoices before changing payment plan = " + invoiceCountQuat);

        //Change payment plan Quarterly to Annual
        acctMenu.clickBCMenuSummary();
        BCAccountSummary acctSummary = new BCAccountSummary(driver);
        acctSummary.clickPolicyNumberInOpenPolicyStatusTable(myPolicyObj.squire.getPolicyNumber());
        BCPolicyMenu polMenu = new BCPolicyMenu(driver);
        polMenu.clickPaymentSchedule();
        PolicyPaymentSchedule paymntSchedule = new PolicyPaymentSchedule(driver);
        paymntSchedule.clickEditSchedule();
        paymntSchedule.selectNewPaymentPlan(PaymentPlanType.Annual);
        paymntSchedule.clickExecute();
        polMenu.clickTopInfoBarAccountNumber();
        acctMenu.clickAccountMenuInvoices();
        int invoiceCountQuatToAnnual = new TableUtils(driver).getRowCount(invoiceTable);
        System.out.println("Number of invoices after changing payment plan to Annual from Quarterly = " + invoiceCountQuatToAnnual);


        //Change payment plan Annual to Quarterly
        acctMenu.clickBCMenuSummary();
        acctSummary.clickPolicyNumberInOpenPolicyStatusTable(myPolicyObj.squire.getPolicyNumber());
        polMenu.clickPaymentSchedule();
        paymntSchedule.clickEditSchedule();
        paymntSchedule.selectNewPaymentPlan(PaymentPlanType.Quarterly);
        paymntSchedule.clickExecute();
        polMenu.clickTopInfoBarAccountNumber();
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        int invoiceCountAnnualToQuat = new TableUtils(driver).getRowCount(invoiceTable);
        System.out.println("Number of invoices after changing payment plan to Quarterly from Annual = " + invoiceCountAnnualToQuat);

        softAssert = new SoftAssert();

        softAssert.assertTrue(invoiceCountQuat == 5, "Umbrella doesn't has same invoice stream as Squire or something wrong");

        softAssert.assertTrue(invoiceCountQuatToAnnual < invoiceCountQuat, "Changing payment plan to Annual from Quarterly was not sucessful");

        softAssert.assertTrue(invoiceCountAnnualToQuat == invoiceCountQuat, "Changing payment plan to Quarterly from Annual was not sucessful");

        softAssert.assertAll();

    }

}
