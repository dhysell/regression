package previousProgramIncrement.pi1_041918_062718.maintenanceDefects.achievers;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.Range;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.renewal.StartRenewal;

public class DE7548_2PercentInflationGuard extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObject;

	/**
	* @Author jlarsen
	* @Requirement 
	* @RequirementsLink <a href="http:// ">Link Text</a>
	* @Description 
	* @DATE Sep 20, 2018
	* @throws Exception
	*/
	@Test
	public void verify2PercentInflationTriggersProperly() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty propertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		propertyOne.getPropertyCoverages().getCoverageA().setLimit(200000);
		PLPolicyLocationProperty propertyTwo = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		propertyTwo.getPropertyCoverages().getCoverageA().setLimit(300000);
		PLPolicyLocationProperty propertyThree = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		propertyThree.getPropertyCoverages().getCoverageA().setLimit(400000);

		locOnePropertyList.add(propertyOne);
		locOnePropertyList.add(propertyTwo);
		locOnePropertyList.add(propertyThree);
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
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		new StartRenewal(driver).startRenewal();
		new SideMenuPC(driver).clickSideMenuSquirePropertyCoverages();
		double renewalCoverageA_B1 = Double.valueOf(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':HOCoveragesPanelSet:5-body')]/div/table/tbody/child::tr[1]/child::td[4]/div")).getText().replace("$", "").replaceAll(",", ""));
		double renewalCoverageA_B2 = Double.valueOf(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':HOCoveragesPanelSet:5-body')]/div/table/tbody/child::tr[2]/child::td[4]/div")).getText().replace("$", "").replaceAll(",", ""));
		double renewalCoverageA_B3 = Double.valueOf(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':HOCoveragesPanelSet:5-body')]/div/table/tbody/child::tr[3]/child::td[4]/div")).getText().replace("$", "").replaceAll(",", ""));
		SoftAssert softAssert = new SoftAssert();
		Range<Double> myRange1 = Range.closed(propertyOne.getPropertyCoverages().getCoverageA().getLimit()*1.019, propertyOne.getPropertyCoverages().getCoverageA().getLimit()*1.021);
		softAssert.assertTrue(myRange1.contains(renewalCoverageA_B1), "BUILDING ONE DID NOT INCREASE BY 2% ON RENEWAL JOB");

		Range<Double> myRange2 = Range.closed(propertyTwo.getPropertyCoverages().getCoverageA().getLimit()*1.019, propertyTwo.getPropertyCoverages().getCoverageA().getLimit()*1.021);
		softAssert.assertTrue(myRange2.contains(renewalCoverageA_B2), "BUILDING ONE DID NOT INCREASE BY 2% ON RENEWAL JOB");

		Range<Double> myRange3 = Range.closed(propertyThree.getPropertyCoverages().getCoverageA().getLimit()*1.019, propertyThree.getPropertyCoverages().getCoverageA().getLimit()*1.021);
		softAssert.assertTrue(myRange3.contains(renewalCoverageA_B3), "BUILDING ONE DID NOT INCREASE BY 2% ON RENEWAL JOB");

		softAssert.assertAll();
	}
}
