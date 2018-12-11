package regression.r2.noclock.policycenter.submission_bop;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class TestBind extends BaseTest {

    public GeneratePolicy myPolicyObjInsuredOnly = null;
    public GeneratePolicy myPolicyObjInsuredAndLien = null;
    public TestFullApp fa;
    private WebDriver driver;

    @Test
    public void testBasicBindInsuredOnly() throws Exception {

        TestFullApp fa = new TestFullApp();
        fa.testBasicFullAppInsuredOnly();
        this.myPolicyObjInsuredOnly = fa.myPolicyObjInsuredOnly;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredOnly.convertTo(driver, GeneratePolicyType.PolicySubmitted);
    }

    @Test
    public void testBasicBindInsuredAndLien() throws Exception {

        TestFullApp fa = new TestFullApp();
        fa.testBasicFullAppInsuredAndLien();
        this.myPolicyObjInsuredAndLien = fa.myPolicyObjInsuredAndLien;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredAndLien.convertTo(driver, GeneratePolicyType.PolicySubmitted);
    }

}
