package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.UnderwriterIssues_BOP;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;

import java.util.ArrayList;
import java.util.List;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description UNDERWRITING ISSUES DEALING WITH EPLI
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_UnderWriterIssues extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void epli() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding("63921"));

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.isDraft()
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObj);
		new SideMenuPC(driver).clickSideMenuRiskAnalysis();
		
		List<UnderwriterIssues_BOP> uwIssueList = new ArrayList<UnderwriterIssues_BOP>();
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR12);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR13);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR14);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR6);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR7);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR8);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR9);
		uwIssueList.add(UnderwriterIssues_BOP.BOPBR18);
		
		
		for(UnderwriterIssues_BOP uwIssue : uwIssueList) {
			setupPolicyFourUWIssue(uwIssue);
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
			if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
				new GenericWorkorderQuote(driver).clickPreQuoteDetails();
			}
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			FullUnderWriterIssues uwIsseues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
			softAssert.assertTrue(uwIsseues.isInList(uwIssue.getLongDescription().replace("${Location #}", "1").replace("${Building #}", "1")).equals(uwIssue.getIssuetype()), uwIssue + " - " + uwIssue.getShortDescription() + "WAS NOT TRIGGERED WHEN REQUIREMENTS WERE MET");
			softAssert.assertTrue(uwIsseues.isInList(UnderwriterIssues_BOP.BOPBR20.getLongDescription().replace("${Location #}", "1").replace("${Building #}", "1")).equals(UnderwriterIssues_BOP.BOPBR20.getIssuetype()), UnderwriterIssues_BOP.BOPBR20 + " - " + UnderwriterIssues_BOP.BOPBR20.getShortDescription() + "WAS NOT TRIGGERED WHEN REQUIREMENTS WERE MET");
			if(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':JobLabel-btnInnerEl')]/span[contains(text(), 'Draft')]")).isEmpty()) {
				new GuidewireHelpers(driver).editPolicyTransaction();
			}
		}
		
		softAssert.assertAll();
		
		
	}
	
	
	
	
	private void setupPolicyFourUWIssue(UnderwriterIssues_BOP uwIssue) throws Exception {
		switch(uwIssue) {
		case BOPBR12:
			//when class code is 63811, 63821, 63981, 63991 and deductible is 25,000
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("63811", "63821", "63981", "63991");
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(0);
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			break;
		case BOPBR13:
			//when totaly employees >100 and building class is not 63921, 63931
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.SeventyFiveThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand);
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("59325");
			int numberOfFulltimeEmployees13 = NumberUtils.generateRandomNumberInt(0, 110);
			int numberOfPartTimeEmployees13 = (110 - numberOfFulltimeEmployees13)*2;
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(numberOfFulltimeEmployees13);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(numberOfPartTimeEmployees13);
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			break;
		case BOPBR14:
			//when total employees >100 buildng class code is 63921, 63931
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.SeventyFiveThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand);
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("63921", "63931");
			int numberOfFulltimeEmployees14 = NumberUtils.generateRandomNumberInt(0, 110);
			int numberOfPartTimeEmployees14 = (110 - numberOfFulltimeEmployees14)*2;
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(numberOfFulltimeEmployees14);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(numberOfPartTimeEmployees14);
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
			if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
				new GenericWorkorderQuote(driver).clickPreQuoteDetails();
			}
			break;
		case BOPBR6:
			//when IDCW 31 2005 is selected display
			break;
		case BOPBR7:
			//when aggragate limit is 500,000 or 1,000,000
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("59325");
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(0);
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			break;
		case BOPBR8:
			//When class code 69151, 69161, 69171, 09611, 09621, 09631, 09641, 09651, 09661, 09001, 09031, 09051, 09071, 09091, 09111, 09131, 09151, 09161, 09181, 09191, 09201, 09221, 
			//09241, 09251, 09421, 09431, 09441, 09011, 09041, 09061, 09081, 09101, 09121, 09141, 09171, 09211, 09231, 09261 is on the policy and a 
			//deductible limit of $10,000 or $25,000 and if the liability limit of 500,000 or 1,000,000 is selected for the IDBP 31 2005 then display the message.
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("69151", "69161", "69171", "09611", "09621", "09631", "09641", "09651", "09661", 
					"09001", "09031", "09051", "09071", "09091", "09111", "09131", "09151", "09161", "09181", "09191", "09201", "09221",
					"09241", "09251", "09421", "09431", "09441", "09011", "09041", "09061", "09081", "09101", "09121", "09141", "09171", "09211", "09231", "09261");
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(true);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(0);
			
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			new GenericWorkorderSupplemental(driver).handleSupplementalQuestions2(myPolicyObj.busOwnLine.locationList, false, false, myPolicyObj);
			break;
		case BOPBR9:
			//when building class code is 63761 or 63771
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("63761", "63771");
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			break;
		case BOPBR18:
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(true);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
			myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(0);
			myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setClassCode("63931");
			new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
			new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
			new GenericWorkorder(driver).clickGenericWorkorderQuote();//to trigger first validation message
			break;
		}
	}
	
	
	
	
	
}
