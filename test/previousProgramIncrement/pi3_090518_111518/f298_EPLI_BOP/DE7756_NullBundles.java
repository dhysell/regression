package previousProgramIncrement.pi3_090518_111518.f298_EPLI_BOP;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import scratchpad.evan.SideMenuPC;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE USER DOES NOT GET A NULL BUNDLE EXCEPTION WHEN REMOVING AND ADDING EPLI IN SAME TRANSACTION.
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class DE7756_NullBundles extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;
	
	
	
	@Test(enabled = true)
	public void checkForNullBundle() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObj);
		new GuidewireHelpers(driver).editPolicyTransaction();
		new SideMenuPC(driver).clickSideMenuBusinessownersLine();
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();

		new GuidewireHelpers(driver).editPolicyTransaction();
		new SideMenuPC(driver).clickSideMenuBusinessownersLine();
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(true);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		
		Assert.assertFalse(new ErrorHandling(driver).validationMessageExists("IllegalStateException: Attempt to access bean of type \"BusinessOwnersExcl\""), "RECIEVED NULL BINDLE EXCEPTION AFTER REMOVING AND ADDING EPLI IN ONE TRANSACTION.");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
