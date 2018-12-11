package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;

/**
 * @Author sbroderick
 * @Requirement If no pre-renewal stoppers exist, then no pre-renewal Activities
 * should exist.
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/9877319305d/detail/defect/45203107180">
 * Rally Defect DE 2980</a>
 * @Description We have been seeing "Pre-Renewal Activities" on renewals without
 * any pre-renewal stoppers in production.
 * @DATE Dec 16, 2015
 */
public class PrerenewalDirectionActivity extends BaseTest {

    private GeneratePolicy newPolicy;

    private WebDriver driver;

    @Test(description = "Generates Policy", enabled = true)
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // customizing location and building
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationAdditionalInsured> locAddInsuredList = new ArrayList<PolicyLocationAdditionalInsured>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), true, locAddInsuredList, locOneBuildingList));


        // GENERATE POLICY
        newPolicy = new GeneratePolicy.Builder(driver)
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withInsCompanyName("Training Policy")
                .withInsPrimaryAddress(new AddressInfo(true))
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPolTermLengthDays(51)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Credit_Debit)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(description = "Test Renewal Paperwork Activity", dependsOnMethods = "generatePolicy")
    public void testRenewalActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login myloginPage = new Login(driver);
        myloginPage.login("su", "gw");

        myloginPage.pressAltShiftT();
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Policy_Renewal_Start);

        new GuidewireHelpers(driver).logout();

        myloginPage.loginAndSearchPolicyByAccountNumber(this.newPolicy.underwriterInfo.getUnderwriterUserName(), this.newPolicy.underwriterInfo.getUnderwriterPassword(), this.newPolicy.accountNumber);

        PolicySummary summaryPage = new PolicySummary(driver);
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (!preRenewalDirectionExists) {
                Assert.fail(driver.getCurrentUrl() +
                        newPolicy.accountNumber + "An Activity was found with no preRenewal Direction.");
            }
        }
    }
}
