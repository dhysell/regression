package regression.r2.clock.policycenter.reinstate;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ReinstateReason;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import regression.r2.clock.billingcenter.cancel.UWPartialCancelTest;

/**
 * @Author sbroderick
 * @Requirement It is generally considered a bug when a NullPointer is received.
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/9877319305d/detail/defect/43818534522">
 * Rally Defect 2942</a>
 * @Description NullPointer received after clicking Reinstate.
 * @DATE Nov 13, 2015
 */
@QuarantineClass
public class UWReinstate extends BaseTest{
	private WebDriver driver;
	private UWPartialCancelTest uwReinstate;

	@Test
	public void createPolicy() throws Exception {
		this.uwReinstate = new UWPartialCancelTest();
		uwReinstate.generate();
		uwReinstate.runBatchprocessesAndMoveClock();
	}

	@Test(dependsOnMethods = { "createPolicy" })
    public void cancelPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(
				uwReinstate.myPolicyObj.underwriterInfo.getUnderwriterUserName(),
				uwReinstate.myPolicyObj.underwriterInfo.getUnderwriterPassword(),
				uwReinstate.myPolicyObj.accountNumber);
        StartCancellation cancelPolicy = new StartCancellation(driver);
		cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "gone", currentDate,
				true);

	}

	@Test(dependsOnMethods = { "cancelPolicy" })
    public void Reinstate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(
				uwReinstate.myPolicyObj.underwriterInfo.getUnderwriterUserName(),
				uwReinstate.myPolicyObj.underwriterInfo.getUnderwriterPassword(),
				uwReinstate.myPolicyObj.accountNumber);
        StartReinstate reinstatePolicy = new StartReinstate(driver);
		reinstatePolicy.reinstatePolicy(ReinstateReason.Other, "Reinstate Please");
	}

}
