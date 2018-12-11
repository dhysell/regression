package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.globaldatarepo.entities.Underwriters;

/**
 * @Author drichards
 * @Requirement DE2851
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/9877319305d/detail/defect/40695570999">
 * DE2851 in Rally</a>
 * @Description Tests to see if the Withdraw Work order button has been removed
 * from a regular Renewal
 * @DATE Jan 4, 2016
 */
@QuarantineClass
public class RenewalWithdrawRemoved extends BaseTest {

    private GeneratePolicy myPolicyObj;
    private String policyNumber;
    private String uwUserName;
    private String uwPassword;

    private WebDriver driver;

    @Test
    public void generate() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Remove", "Withdraw")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolTermLengthDays(51)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

        policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();
        Underwriters underwriter = myPolicyObj.underwriterInfo;
        uwUserName = underwriter.getUnderwriterUserName();
        uwPassword = underwriter.getUnderwriterPassword();
    }

    @Test(dependsOnMethods = {"generate"}, enabled = true)
    public void checkRenewalWithdrawOption() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uwUserName, uwPassword, policyNumber);

        StartRenewal renew = new StartRenewal(driver);
        renew.startRenewal();

        GenericWorkorder wo = new GenericWorkorder(driver);
        if (wo.checkWithdrawPolicy()) {
            Assert.fail("The Withdraw Work Order button in Close Options should have been removed");
        }

    }

}
