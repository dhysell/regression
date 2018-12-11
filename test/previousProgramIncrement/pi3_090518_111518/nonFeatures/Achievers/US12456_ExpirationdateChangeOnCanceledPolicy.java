package previousProgramIncrement.pi3_090518_111518.nonFeatures.Achievers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;

public class US12456_ExpirationdateChangeOnCanceledPolicy extends BaseTest {

	GeneratePolicy myPolicyObj;
	WebDriver driver;
	
	@Test
	public void expirationDateChangeOnCanceledPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHAHA YOU GOT CANCELED", null, true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new ActionsPC(driver).click_Actions();
		List<WebElement> expDateChangeOption = new GenericWorkorder(driver).finds(By.xpath("//span[contains(@id, ':PolicyFileMenuActions_ExpirationDateChange')]"));
		Assert.assertFalse(!expDateChangeOption.isEmpty(), "EXPIRATION DATE CHANGE WAS AN AVAILABLE OPTION ON A CANCELED POLICY");
	}
}
