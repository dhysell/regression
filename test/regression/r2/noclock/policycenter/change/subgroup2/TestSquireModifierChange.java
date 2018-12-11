package regression.r2.noclock.policycenter.change.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US12271: Modifiers not adjusting for policy changes on a
 *              transition renewal
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Modifiers.xlsx">
 *                   requirements</a>
 * @Description
 * @DATE Oct 11, 2017
 */
public class TestSquireModifierChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL;
	private Underwriters uw;

	@Test()
	public void testIssueSquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		driversList.add(person);

		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle firstVeh = new Vehicle();
		Vehicle secondVeh = new Vehicle();
		secondVeh.setNoDriverAssigned(true);
		vehicleList.add(secondVeh);
		vehicleList.add(firstVeh);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        SquirePropertyAndLiability myProeprty = new SquirePropertyAndLiability();
        myProeprty.locationList = locationsList;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myProeprty;


        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Modifier", "Change")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePolicy" })
	private void testRemoveVehicleValidateModifier() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySQPolicyObjPL.squire.getPolicyNumber());

		// First Change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First Policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(driver);
		vehiclePage.removeAllVehicles();
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.setCheckBoxInDriverTable(1);
		paDrivers.clickRemoveButton();

        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		sideMenu.clickSideMenuLineSelection();
		lineSelection.checkPersonalAutoLine(false);

		sideMenu.clickSideMenuPAModifiers();

		// validate modifiers
        GenericWorkorderModifiers modify = new GenericWorkorderModifiers(driver);

		if (!modify.getSquirePolicyModififierEligibilityByCategory("Package").equals("No")) {
			Assert.fail("Expected Modifier Eligibility 'No' for Category 'Package' is not displayed.");
		}

		if (!modify.getSquirePolicyModifierDiscountSurchargeByCategory("Package").equals("0")) {
			Assert.fail("Expected Modifier DiscountSurchage '0' for Category 'Package' is not displayed.");
		}

	}
}
