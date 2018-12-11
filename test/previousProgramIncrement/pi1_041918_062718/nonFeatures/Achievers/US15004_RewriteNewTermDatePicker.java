package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.workorders.cancellation.StartCancellation;

/**
* @Author jlarsen
* @Requirement Start Rewrite New Term with a date picker screen (LOB change)
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/215672871636">US15004</a>
* @Description User Statement:

As a PL Underwriter I need to be able to start a rewrite new term into the future without permission issues/screen limitations so that we can process a Line of Business change for the insured

Requirement: System should allow the user (UW only) to select the effective date of the rewrite in future while processing a rewrite new term for a line of business change.

Steps to get there:
Create a new PL squire policy (start as a B with section 3 only)
Bind and Issue the policy
As the PL Underwriter, start a cancellation with an effective date into the future (not too far, next week is good)
Issue the cancel and then from the actions button in the cancelled policy term; select "rewrite new term"
Acceptance criteria:

Ensure rewrite new term into the future works without any permission issues or screen limitations
TEST ALL LINES PL AND BOP
* @DATE Apr 27, 2018
*/
public class US15004_RewriteNewTermDatePicker extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;


	@Test(enabled=true)
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
		coverages.setAccidentalDeath(true);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person1 = new Contact();
		driversList.add(person1);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		toAdd.setEmergencyRoadside(true);
		toAdd.setAdditionalLivingExpense(true);
		toAdd.setDriverPL(person1);
		toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		vehicleList.add(toAdd);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.build(GeneratePolicyType.PolicyIssued);
        
        Date systemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
        StartCancellation cancelation = new StartCancellation(driver);
		cancelation.cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHAHAH YOU GOT CANCELED", DateUtils.dateAddSubtract(systemDate, DateAddSubtractOptions.Day, 5), true);
		new GuidewireHelpers(driver).clickWhenClickable(driver.findElement(By.xpath("//div[contains(@id, ':JobCompleteScreen:JobCompleteDV:ViewPolicy-inputEl')]")));
		new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Summary')]", 10000, "UNABLE TO GET TO POLICY SUMMARY PAGE AFTER CLICKING VIEW POLICY ON JOB COMPLETE PAGE");

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteNewTerm();
        
        Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//input[contains(@id, 'StartPolicyChangeDV:EffectiveDate-inputEl')]"), "REWRITE NEW TERM DID NOT START WITH THE DATE PICKER PAGE.");

	}
}
