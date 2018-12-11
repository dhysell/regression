package regression.r2.noclock.billingcenter.policychange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
@QuarantineClass
public class PolicyChangeCreditDistributionTest extends BaseTest {
	private WebDriver driver;
    public GeneratePolicy myPolicyObj;
    private String acctNumber, pcNumber;
    public Date expDate, effDate;
    private double costChg;
    private int i;
    private String policyChangeDescription = "change coverage";
    private ARUsers arUser = new ARUsers();

    @Test
    public void generate() throws Exception {
        //generate a policy with monthly payment plan
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("PolicyChangeCreditTest")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        acctNumber = myPolicyObj.accountNumber;
        pcNumber = myPolicyObj.busOwnLine.getPolicyNumber();
        expDate = myPolicyObj.busOwnLine.getExpirationDate();
        effDate = myPolicyObj.busOwnLine.getEffectiveDate();
    }

    @Test(dependsOnMethods = {"generate"})
    public void reduceInsuredCoverage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), pcNumber);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.changeBuildingCoverage(1, 100000, 100000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();
        PolicySummary pcSum = new PolicySummary(driver);
        costChg = pcSum.getTransactionPremium(null, policyChangeDescription);
        getQALogger().info("The cost change after policy change is  :" + costChg);
    }

    @Test(dependsOnMethods = {"reduceInsuredCoverage"})
    public void verifyCreditDistributionInBC() throws Exception {
    	this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        int errorCount = 0;
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        AccountCharges acctChg = new AccountCharges(driver);

        acctChg.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.acctNumber);
        acctChg.clickChargeContextCenterConnectHyperlink(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj.accountNumber, myPolicyObj.busOwnLine.getPolicyNumber(), policyChangeDescription);
        List<Double> calculatedCrDistribution = acctChg.calculateCreditDistribution(PaymentPlanType.Monthly, costChg, effDate, expDate);
        List<Double> getCreditDistributionFromBC = acctChg.getListOfPaidAmountsInInvoiceItemsTable();
        for (i = calculatedCrDistribution.size() - 1; i >= 0; i--) {
            if (calculatedCrDistribution.get(i) * (-1) != getCreditDistributionFromBC.get(i) * (-1)) {
                System.out.println("The credit distribution on " + i + "th invoice is not correct. They are " + calculatedCrDistribution.get(i) + " and " + getCreditDistributionFromBC.get(i));
                errorCount++;
            }
        }
        if (errorCount > 0) {
            Assert.fail("The credit distribution is not correct, see the log for detail.");
        }
    }
}
