package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipsAB;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_AdditionalCoverages;
import repository.pc.workorders.renewal.StartRenewal;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author jlarsen
 * @Requirement Accidental Death Coverage checking for eligible members at Renewal
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20Renewal%20-%20Update%20Renewal.xlsx">Requiremtns link at the time of the story</a>
 * @Description Automatically add Accidental Death Coverage to the Renewal job if it was not previously on the policy; needs to look at 15 year old not 16 year old.
Requirement Link: PC8 - HO - Renewal - Update Renewal (UI Mockup tab)
User Statement: 
As the system, I want to make sure that eligible existing members age 15 years old at Renewal are included in the Accidental Death and Dismemberment Count so that PC/Underwriter can charge correctly.
Steps to get there: 
Pick the existing PL Policy with Section III  + ADD Coverage and the members who are of age less then 15
Move to Renewal - 80
Run the policy Renewal start batch
Check your Renewal job
Acceptance Criteria:
Ensure ADD count reflects eligible existing members on Renewal on Section III at the Additional Coverage tab.
Ensure that ADD premium reflects premium for eligible existing members on the Quote Screen.
 * @DATE Apr 25, 2018
 */
public class US14609_AccidentalDeathEligibleMembersUpdateAtRenewal extends BaseTest {
	public GeneratePolicy myPolicyObject = null;
    private int policyLengthDays = 79;
    private WebDriver driver;

	@Test
	public void renewPolicy() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
		coverages.setAccidentalDeath(true);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person1 = new Contact();
		Date bday = DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -15);
		bday = DateUtils.dateAddSubtract(bday, DateAddSubtractOptions.Day, 12);
		Contact person2 = new Contact();
		person2.setRelationshipAB(RelationshipsAB.Affiliation);
		person2.setDob(driver, bday);
		Contact person3 = new Contact();
		person3.setRelationshipAB(RelationshipsAB.Partner);
		Date bday2 = DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -15);
		bday2 = DateUtils.dateAddSubtract(bday2, DateAddSubtractOptions.Day, policyLengthDays);
		person3.setDob(driver, bday2);
		Contact person4 = new Contact();
		person4.setRelationshipAB(RelationshipsAB.ChildWard);
		Date bday4 = DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -15);
		bday4 = DateUtils.dateAddSubtract(bday4, DateAddSubtractOptions.Day, (policyLengthDays+2));
		person4.setDob(driver, bday4);
		driversList.add(person1);
		driversList.add(person2);
		driversList.add(person3);
		driversList.add(person4);

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
				.withPolTermLengthDays(policyLengthDays)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.build(GeneratePolicyType.PolicyIssued);
		
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
        new StartRenewal(driver).startRenewal();
        new SideMenuPC(driver).clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_AdditionalCoverages additionalCovergaes = new GenericWorkorderSquireAutoCoverages_AdditionalCoverages(driver);
		additionalCovergaes.clickAdditionalCoveragesTab();
		int policyAccidentalDeathCount = Integer.valueOf(myPolicyObject.squire.squirePA.getCoverages().getAccidentalDeathCount());
		int renewalADDCount = Integer.valueOf(additionalCovergaes.getAccidentalDeathCount());
		Assert.assertTrue(policyAccidentalDeathCount==(renewalADDCount-2), "ACCIDENTAL DEATH DID NOT INCREMENT WITH A NEW DRIVER TURNING 15 BEFORE THE START OF THE NEW TERM");
	}
}
