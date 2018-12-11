package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
* @Author jlarsen
* @Requirement User Statement:
As an agent I want a trigger for my 10-day binding authority (truly 15 days) that blocks me when I'm submitting a previously quoted policy that was dated 16 days or more in the past so that Effective date should always be within the 15 days of the current date.
Steps to Get There:
Start a quote
Move the clock 16 days so that you are 16 days past the quote date
Try to submit the policy
Make sure there is an Underwriting issue (Block Bind)
Change the date to be 15 or less from today's date
Requote and check that the Underwriting issue (Block Bind) is not there
Submit the quote
Test this also that you are able to submit when agent is outside 15 days and that you will receive and UW block
Acceptance Criteria:
Verify all below items work for PL policies
Ensure that the trigger is generated when the date the policy is submitted is more than 15 days from the effective date on a previously quoted policy.
Ensure that a trigger is creating an UW issue for submitting quote that is outside of his/her binding authority AND that it will:
Create a Block Bind if date is not changed.
Not create a Block Bind  when the date is changed to be within 15 days of current date
Ensure that if the effective date is set less than 15 days in the past, the UW issue doesn't trigger.
Ensure that the wording of the underwriting issue doesn't change (still says "10 day submitting authority")  
Dev - consider a SECOND TRIGGER FOR 10 DAY BINDING AUTHORITY 
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/203782116656">US14341</a>
* @Description Generate trigger when policy effective date is > 15 days from a previously quoted policy's effective date
* @DATE May 17, 2018
*/
@Test(groups= {"ClockMove"})
public class US14341_10DaySubmittedAthority_P1S3 extends BaseTest {
	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;
	
	//only submission
	@Test(enabled = true)
	public void activityTriggerOver15DaysOfEffectiveDate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		PLPolicyLocationProperty propertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		propertyOne.getPropertyCoverages().getCoverageA().setLimit(56000);

		locOnePropertyList.add(propertyOne);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolTermLengthDays(79)
				.build(GeneratePolicyType.FullApp);
		
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 20);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject);
		new GenericWorkorderPayment(driver).fillOutPaymentPage(myPolicyObject);
//        new GenericWorkorder(driver).checkGenericWorkorderSubmitOptionsIssuePolicyOption();
        new GenericWorkorder(driver).clickGenericWorkorderIssue(IssuanceType.Issue);
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		Assert.assertTrue(uwIssues.isInList("Quoted date falls outside of 10 day binding authority. Underwriting must approve this quote, please provide appropriate documentation or requote your policy.").equals(UnderwriterIssueType.BlockSubmit), "DID NOT GET 10 DAY SUBMIT UNDERWRITER ISSUES AFTER WAITING 20 DAYS TO SUBMIT THE POLICY.");
	}
}

