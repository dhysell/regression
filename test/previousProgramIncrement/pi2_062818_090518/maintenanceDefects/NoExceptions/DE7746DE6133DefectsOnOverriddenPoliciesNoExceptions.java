package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.NoExceptions;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsPromisedPayments;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @Requirement DE6133 Exception was received when exiting delinquency manually
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/defect/136836041372">DE6133 Exception was received when exiting delinquency manually</a>
 * @DATE Aug 09, 2018
 */


@Test(groups = {"ClockMove"})
public class DE7746DE6133DefectsOnOverriddenPoliciesNoExceptions extends BaseTest {

    private WebDriver driver;
    private GeneratePolicy generatePolicy;
    private ARUsers arUser = new ARUsers();


    @Test ()
    public void generatePolicy() throws Exception {
        Config pc = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(pc);

        PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
        myProperty.getPropertyCoverages().getCoverageA().setLimit(350000);

        ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
        propertyList.add(myProperty);
        PolicyLocation myLocation = new PolicyLocation(propertyList);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(myLocation);
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationList;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.propertyAndLiability.liabilitySection.setGeneralLiabilityLimit(Property.SectionIIGeneralLiabLimit.Limit_500000_CSL);

        generatePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withSquire(mySquire)
                .withInsFirstLastName("DE7746", "DE6133")
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        generatePolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
        driver.quit();

    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesPaymentsInBCAndTestDelinquencyTriggers() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.generatePolicy.accountNumber);

        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        bcAccountMenu.clickAccountMenuPaymentsPromisedPayments();
        new AccountPaymentsPromisedPayments(driver).waitUntilBindPromisedPaymentsArrive(240);

        bcAccountMenu.clickBCMenuSummary();
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(generatePolicy.squire.getPremium().getDownPaymentAmount()+generatePolicy.squireUmbrellaInfo.getPremium().getDownPaymentAmount(),generatePolicy.squire.getPolicyNumber());
        ClockUtils.setCurrentDates(driver,DateAddSubtractOptions.Day,1);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);

        ClockUtils.setCurrentDates(driver,DateAddSubtractOptions.Day,20);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
        
        bcAccountMenu.clickBCMenuDocuments();
        BCCommonDocuments bcCommonDocuments = new BCCommonDocuments(driver);
        bcCommonDocuments.clickDocumentNameInTable(null,DocumentType.Monthly_Notice_Of_Withdrawal,null,null,null,null);
        Assert.assertTrue(bcCommonDocuments.getTextInRelatedToField().contains(generatePolicy.squire.getPolicyNumber()),"tag alone policy number was not assigned ");

    }
}
