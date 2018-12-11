package regression.r2.noclock.policycenter.submission_bop;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class TestFullApp extends BaseTest {

    public GeneratePolicy myPolicyObjInsuredOnly = null;
    public GeneratePolicy myPolicyObjInsuredAndLien = null;
    public TestQuickQuote qq;
    private WebDriver driver;

    @Test
    public void testBasicFullAppInsuredOnly() throws Exception {

        TestQuickQuote qq = new TestQuickQuote();
        qq.testBasicQuickQuoteInsuredOnly();
        this.myPolicyObjInsuredOnly = qq.myPolicyObjInsuredOnly;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredOnly.convertTo(driver, GeneratePolicyType.FullApp);
    }

    @Test
    public void testBasicFullAppInsuredAndLien() throws Exception {

        TestQuickQuote qq = new TestQuickQuote();
        qq.testBasicQuickQuoteInsuredAndLien();
        this.myPolicyObjInsuredAndLien = qq.myPolicyObjInsuredAndLien;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredAndLien.convertTo(driver, GeneratePolicyType.FullApp);
    }
}
