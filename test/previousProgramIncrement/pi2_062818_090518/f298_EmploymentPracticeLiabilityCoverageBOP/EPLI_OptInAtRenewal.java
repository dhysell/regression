package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLine;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE EPLI IS OPT IN ON RENEAL IF IT IS NOT CURRENTLY ON THE POLICY
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_OptInAtRenewal extends BaseTest {

	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void epli_OptInAtRenewal() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding());
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.withPolTermLengthDays(79)
				.build(GeneratePolicyType.PolicyIssued);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new PolicySummary(driver).clickPendingTransaction(TransactionType.Renewal);
		new GuidewireHelpers(driver).editPolicyTransaction();
		new SideMenuPC(driver).clickSideMenuBusinessownersLine();
		new GenericWorkorderBusinessownersLine(driver).clickBusinessownersLine_AdditionalCoverages();
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Employment Practices Liability Insurance"), "EPLI WAS NOT ELECTABLE DURING EDIT RENEWAL WHEN IT WAS NOT SELECTED ON THE SUBMISSION");
		
		softAssert.assertAll();
	}
}





















