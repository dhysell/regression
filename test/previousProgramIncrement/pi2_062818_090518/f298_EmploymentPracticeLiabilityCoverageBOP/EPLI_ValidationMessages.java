package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description EPLI VALIDATION MESSAGES
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_ValidationMessages extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void epli_validationMessages() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding("63921"));

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(boLine)
				.isDraft()
				.build(GeneratePolicyType.QuickQuote);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObj);
		new SideMenuPC(driver).clickSideMenuRiskAnalysis();
		
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new SideMenuPC(driver).clickSideMenuBuildings();
		new GenericWorkorder(driver).clickNext();
		softAssert.assertTrue(new GenericWorkorder(driver).validationMessagesContains("Legal services are not eligible for limits in excess of $250,000. Please correct or contact Brokerage for increased limits."));
		
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand);
		int numberOfFulltimeEmployees = NumberUtils.generateRandomNumberInt(0, 110);
		int numberOfPartTimeEmployees = (110 - numberOfFulltimeEmployees)*2;
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(numberOfFulltimeEmployees);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(numberOfPartTimeEmployees);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new SideMenuPC(driver).clickSideMenuBuildings();
		new GenericWorkorder(driver).clickNext();
		softAssert.assertTrue(new GenericWorkorder(driver).validationMessagesContains("Legal Services with over 100 employees are not eligible. Please refer to Brokerage for coverage."));
		
		softAssert.assertAll();
	}
}
























