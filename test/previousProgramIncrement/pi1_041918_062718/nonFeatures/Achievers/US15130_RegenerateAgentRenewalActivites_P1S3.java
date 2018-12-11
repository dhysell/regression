package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyPreRenewalDirections;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;

/**
* @Author jlarsen
* @Requirement User Statement:
As an AGENT I want Pre-Renewal Activities/Automated Pre-Renewal Directions that are still open to be removed on a canceled policy and to regenerate if the policy is reinstated so that I do not have two sets of activities/directions to handle (and the original set may no longer be applicable anyway).

Steps to Get There:
Have a PL policy with agent Pre-Renewal Activities/Automated Pre-Renewal Directions that are still open on it
Cancel the policy
Check that the agent Pre-Renewal Activities/Automated Pre-Renewal Directions that were not closed no longer exist
Reinstate the policy
Check that the agent Pre-Renewal Activities/Automated Pre-Renewal Directions fire off 

Acceptance Criteria:
Ensure that the first set of agent Pre-Renewal Activities/Automated Pre-Renewal Directions that were still open are removed from the policy when the policy cancels
Ensure that a new set of agent Pre-Renewal Activities/Automated Pre-Renewal Directions are generated when the policy is reinstated
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/220610002928">US15130</a>
* @Description Agent's Renewal Activities need to be removed when a policy is canceled, brought back if it's reinstated
* @DATE May 17, 2018
*/
public class US15130_RegenerateAgentRenewalActivites_P1S3 extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;
	
	@Test(enabled = true)
	public void reinstateAgentActivitesUponReinstate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.getSectionIICoverageList().add(new SectionIICoverages(SectionIICoveragesEnum.CustomFarming, 1001, 0));

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;

		SquirePersonalAuto myPersonalAuto = new SquirePersonalAuto();
		myPersonalAuto.addVehicle(VehicleTypePL.ShowCar);

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.squirePA = myPersonalAuto;

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolTermLengthDays(80)
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new StartRenewal(driver).startRenewal();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new PolicyPreRenewalDirection(driver).clickViewPreRenewalDirections();
		PolicyPreRenewalDirections firstPreRenewalDirections = new PolicyPreRenewalDirection(driver).getPreRenewalDirections();
		new PolicyPreRenewalDirection(driver).clickReturnToSummaryPage();
		List<Activity> renewalActivities = new PolicySummary(driver).getCurrentActivites_BasicInfo();

		new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHHAHAHAH NO INSURANCE FOR YOU. GO TO GEICO", centerDate, true);
		
		new GuidewireHelpers(driver).clickWhenClickable(driver.findElement(By.xpath("//div[contains(@id, ':JobCompleteDV:ViewPolicy-inputEl')]")));
		new PolicyPreRenewalDirection(driver).clickViewPreRenewalDirections();
		PolicyPreRenewalDirections canceledPreRenewalDirections = new PolicyPreRenewalDirection(driver).getPreRenewalDirections();
		new PolicyPreRenewalDirection(driver).clickReturnToSummaryPage();
		List<Activity> canceledRenewalActivities = new PolicySummary(driver).getCurrentActivites_BasicInfo();
		
		new StartReinstate(driver).reinstatePolicy(ReinstateReason.Other, "FINE WE WILL GIVE YOUR INSURANCE BACK");
		new InfoBar(driver).clickInfoBarPolicyNumber();

		new PolicyPreRenewalDirection(driver).clickViewPreRenewalDirections();
		PolicyPreRenewalDirections reinstatePreRenewalDirections = new PolicyPreRenewalDirection(driver).getPreRenewalDirections();
		new PolicyPreRenewalDirection(driver).clickReturnToSummaryPage();
		List<Activity> reinstateRenewalActivities = new PolicySummary(driver).getCurrentActivites_BasicInfo();
		
		SoftAssert softAssert = new SoftAssert();
		
		//VERIFY THERE ARE PRE-RENEWAL DIRECTIONS TO TEST WITH
		softAssert.assertFalse(firstPreRenewalDirections.getDirectionList().isEmpty(), "THERE WERE NO PRE-RENEWAL DIRECTIONS CREATED WITH THE RENEWAL. UPDATE GENERATE TO INCLUDE SOME FOR THIS TEST.");
		
		//VERIFY THERE ARE RENEWAL ACTIVITIES TO TEST WITH
		boolean found = false;
		for(Activity activity : renewalActivities) {
			if(activity.getSubject().equals("Pre-Renewal Review for Squire")) {
				found = true;
				break;
			}
		}
		if(!found) {
			Assert.fail("THER WAS NO PRE-RENEWAL UNDERWRITER ACTIVITY. SO THIS TEST IS TESTING NOTHING. HHEHEHEH, SHOULD PROBABLY FIX THAT");
		}
		
		//VERIFY IF THERE WERE PRE-RENEWAL DIRECTIONS THAT THEY WERE REMOVED
		if(!firstPreRenewalDirections.getDirectionList().isEmpty()) {
			softAssert.assertTrue(canceledPreRenewalDirections.getDirectionList().isEmpty(), "UPON CANCELLATION, THE PRE-RENEWAL DIRECTIONS DID NOT GET REMOVED.");
		}
		
		//VERIFY UW ACTIVTY ONLY WAS REMOVED.
		found = false;
		for(Activity activity : canceledRenewalActivities) {
			if(activity.getSubject().equals("Pre-Renewal Review for Squire")) {
				found = true;
				break;
			}
		}
		if(found) {
			Assert.fail("PRE-RENEWAL ACTIVITY WAS NOT REMOVED AFTER POLICY WAS CANCELED.");
		}
		
		//VERIFY THAT PRE-RENEWAL DIRECTIONS RETRIGGERED AFTER CANCELATION WAS RECINDED
		if(!firstPreRenewalDirections.getDirectionList().isEmpty()) {
			softAssert.assertFalse(reinstatePreRenewalDirections.getDirectionList().isEmpty(), "ON POLICY REINSTATE PRE-RENEWAL DIRECTIONS WERE NOT RECREATED.");
		}
		
		//VERIFY THAT UW ACTIVITY WAS RECREATED AFTER POLICY WAS REINSTATED.
		found = false;
		for(Activity activity : reinstateRenewalActivities) {
			if(activity.getSubject().equals("Pre-Renewal Review for Squire")) {
				found = true;
				break;
			}
		}
		if(!found) {
			Assert.fail("PRE-RENEWAL ACTIVITY WAS NOT RETRIGGERED AFTER POLICY WAS REINSTATED.");
		}
		
		softAssert.assertAll();
	}
}
