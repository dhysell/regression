package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;

/**
 * @Author jlarsen
 * @Requirement 
 * Jewelry Photo Update Required 
Custom Farming Amount Update Required (US14608 creates the estimated receipts for this year field)
Farm Truck/Show Car Update Required
Requirements link(s): PC8 - Common - CommonAdmin - Policy File - Summary - UI Mockup tab
User Statement:
As an agent, I want the activities to have the target date (80 days) before renewal effective date and the escalation date (10 days) after the target date. Therefore the required information can be obtained before the renewal date.
Steps to get there:
Create policies with:
Jewelry that has an appraisal date least 6 years prior to renewal date
Custom farming
Farm truck and/or Show Car
Move to renewal -80
Check for activity to agent
Move the to -62.
Check for agent's target activity.
Move clock to -60.
Check the agent's escalation activity.
Activities can be found on the policy summary screen.
Acceptance Criteria:
Ensure that activities are triggered, for agent, at day -80.
Ensure that agent's target date triggers 18 days later (-62).
Ensure that escalated activities are triggered, for agent, 20 days (-60) later.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/211179103192">US14611</a>
 * @Description Revisit on Pre-Renewal Activities to Agent's Target and Escalation Dates
 * @DATE May 7, 2018
 */
public class US14611_PreRenewalActivityTargetAndEscalationDates extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;


	@Test(enabled = true)
	public void renewPolicyAndVerifyActivites() throws Exception {
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

        List<Activity> myAPolicySummary = new PolicySummary(driver).getCurrentActivites_FullInfo();
		for(Activity activity : myAPolicySummary) {
			switch(activity.getSubject()) {
			case "Jewelry Photo Update Required":
			case "Custom Farming Amount Update Required":
			case "Farm Truck/Show Car Update Required":
				int targetDate = DateUtils.getDifferenceBetweenDatesAbsoluteValue(myPolicyObject.squire.getExpirationDate(), DateUtils.convertStringtoDate(activity.getTargetDate(), "MM/dd/yyyy"), DateDifferenceOptions.Day);
				softassert.assertTrue((targetDate == 62), activity + " TARGET DATE IS NOT SET TO -62 DAYS. IS SET TO: " + targetDate);
				int escalationDate = DateUtils.getDifferenceBetweenDatesAbsoluteValue(myPolicyObject.squire.getExpirationDate(), DateUtils.convertStringtoDate(activity.getEscalationDate(), "MM/dd/yyyy"), DateDifferenceOptions.Day);
				softassert.assertTrue((escalationDate == 60), activity + " ESCALATION DATE IS NOT SET TO -60 DAYS. IS SET TO: " + targetDate);
				break;
			default:
				break;
			}
		}

		softassert.assertAll();
	}
}
