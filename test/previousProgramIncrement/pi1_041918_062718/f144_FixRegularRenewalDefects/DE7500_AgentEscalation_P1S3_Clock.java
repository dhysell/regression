package previousProgramIncrement.pi1_041918_062718.f144_FixRegularRenewalDefects;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyActivities;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import persistence.globaldatarepo.entities.RegionSupervisors;

/**
 * @Author jlarsen
 * @Requirement 
 * Create policies with:
Jewelry that has a photo date least 6 years prior to renewal date
Custom farming
Farm truck and/or Show Car
Use at least 2 agents, each from a different region
Move to renewal -80
Check for activity to agent; one for each item triggered on the policy (there should also be a pre-renewal direction for each)
Move clock to renewal -60 and run 'Activity Escalation' batch.
Check to make sure there is an escalated activity for the agent's Regional Executive tied to each of the original activities 
Actual: Escalated activites were being assigned to Patty Anderson, regardless of region
Expected: Escalated activities should be assigned to the agent's Regional Executive
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/219780626168">DE7500</a>
 * @Description Fix who agent escalated activites go to (Regional Executive) on Custom Farming, Show Car/Farm Truck, and Jewelry activities
 * @DATE May 17, 2018
 */
@Test(groups = {"ClockMove"})
public class DE7500_AgentEscalation_P1S3_Clock extends BaseTest{
	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

	@Test(enabled = true)
	public void activityEscalationToCorrectUser() throws Exception {
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

		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		PersonalPropertyScheduledItem myItem = new PersonalPropertyScheduledItem();
		myItem.setParentPersonalPropertyType(PersonalPropertyType.Jewelry);
		myItem.setType(PersonalPropertyScheduledItemType.Ring);
		myItem.setDescription("Big @$$ rock!!!");
		myItem.setAppraisalDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
		myItem.setPhotoUploadDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
		myItem.setLimit(25000);

		PersonalProperty myPersonalProperty = new PersonalProperty();
		myPersonalProperty.setType(PersonalPropertyType.Jewelry);
		myPersonalProperty.setLimit(25000);
		myPersonalProperty.setDeductible(PersonalPropertyDeductible.Ded5Perc);
		myPersonalProperty.getScheduledItems().add(myItem);
		myInlandMarine.personalProperty_PL_IM.add(myPersonalProperty);
		myInlandMarine.inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;
		mySquire.squirePA = myPersonalAuto;

		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolTermLengthDays(80)
				.build(GeneratePolicyType.PolicyIssued);


		SoftAssert softassert = new SoftAssert();

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject);

		PolicyActivities policyActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_FullInfo());
		String escalationDate = "";
		boolean activityFound = false;
		for(Activity activity : policyActivities.getPolicyActivityList()) {
			if(activityFound) {
				break;
			}
			switch(activity.getSubject()) {
			case "Custom Farming Amount Update Required":
			case "Farm Truck/Show Car Update Required":
			case "Jewelry Photo Update Required":
				escalationDate = activity.getEscalationDate();
				activityFound = true;
				break;
			default:
				break;
			}
		}
		
		if(!activityFound) {
			Assert.fail("NO ACTIVITY WAS FOUND TO GET ESCALATION DATE OFF OF. TEST CANNOT CONTINUE.");
		}
		
		new GuidewireHelpers(driver).logout();
		ClockUtils.setCurrentDates(driver, DateUtils.convertStringtoDate(escalationDate, "MM/dd/yyyy"));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
		new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObject);

		PolicyActivities policyEscalatedActivities = new PolicyActivities( new PolicySummary(driver).getCurrentActivites_BasicInfo());
		RegionSupervisors supervisor = new RegionSupervisors().getRegionalSupervisor_ByNumber(Integer.valueOf(myPolicyObject.agentInfo.getAgentRegion().replaceAll("\\D", "")));
		for(Activity activity : policyActivities.getPolicyActivityList()) {
			switch(activity.getSubject()) {
			case "Custom Farming Amount Update Required":
			case "Farm Truck/Show Car Update Required":
			case "Jewelry Photo Update Required":
				for(Activity excalatedActivity : policyEscalatedActivities.getPolicyActivityList()) {
					if(excalatedActivity.getSubject().contains(activity.getSubject()) && excalatedActivity.getSubject().contains("Activity Escalated")) {
						softassert.assertTrue(excalatedActivity.getAssignedTo().contains(supervisor.getLastName()), 
								"ACTIVTY: " + excalatedActivity.getSubject() + " DID NOT ESCALATE TO CORRECT REGIONAL EXEC. FOUND: " + excalatedActivity.getAssignedTo() + "SUPPOSED TO ASSIGNED TO: " + excalatedActivity.getAssignedTo());
					}
				}
				break;
			default:
				break;
			}

		}

		softassert.assertAll();
	}
}






















