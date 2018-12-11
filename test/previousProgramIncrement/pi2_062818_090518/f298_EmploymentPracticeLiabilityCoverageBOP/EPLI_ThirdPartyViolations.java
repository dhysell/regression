package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderQuote;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description EPLI THIRD PARTY VIOLATIONS AVAILABILITY
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_ThirdPartyViolations extends BaseTest {

	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void epli_ThirdPartyViolations() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding());
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.TwentyFiveThousand);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_ThirdPartyViolations(true);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated(0);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.build(GeneratePolicyType.QuickQuote);
		
		new Login(driver).loginAndSearchSubmission("atubbs", "gw", myPolicyObj.accountNumber);
		new SideMenuPC(driver).clickSideMenuQuote();
		double quoteValue = NumberUtils.getCurrencyValueFromElement(new GenericWorkorderQuote(driver).getDescriptionPremium("Third Party Violations"));
		
		softAssert.assertTrue(quoteValue == 10.65, "THIRD PARTY VIOLATION PREMIUM DID NOT MATCH SET VALUE: FOUND: " + quoteValue + " EXPECTED: " + boLine.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated());

		new GuidewireHelpers(driver).editPolicyTransaction();
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_Handrated(500);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated(1000);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.TwoHundredFiftyThousand);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(boLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		
		new SideMenuPC(driver).clickSideMenuQuote();
		quoteValue = NumberUtils.getCurrencyValueFromElement(new GenericWorkorderQuote(driver).getDescriptionPremium("Third Party Violations"));
		
		softAssert.assertTrue(quoteValue == boLine.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated(), "THIRD PARTY VIOLATION PREMIUM DID NOT MATCH SET VALUE 2: FOUND: " + quoteValue + " EXPECTED: " + boLine.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated());

		
		
		
		softAssert.assertAll();
		
	}
	
	
	
	
	
}
