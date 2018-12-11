package previousProgramIncrement.pi1_041918_062718.f144_FixRegularRenewalDefects;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyPreRenewalDirections;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.ClockUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.workorders.renewal.StartRenewal;

/**
 * @Author jlarsen
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/182525081448d/detail/defect/104341535324">DE5253</a>
 * @Description When viewing the details for a pre-renewal direction, the Opened Date changes to display the current date.  If you aren't inside details the true opened date shows.
 * @DATE Apr 18, 2018
 */
/////////////DONE//////////////////
@Test(groups= {"ClockMove"})
public class DE5253_ViewAndDetailPreRenewalDirectionOpenDate_Clock extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;
	
	@Test(enabled = true)
	public void verifyCorrctPrerenewalDate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
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
				.withInsFirstLastName("PreRenewal", "DE5253")
				.withPolOrgType(OrganizationType.Individual)
				.withPolTermLengthDays(79)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		new StartRenewal(driver).startRenewal();
		
		//MOVE CLOCK A FEW DAYS
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 5);
		
		new InfoBar(driver).clickInfoBarPolicyNumber();
		PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.clickViewPreRenewalDirections();
		PolicyPreRenewalDirections preRenewalListOpenDate = preRenewalPage.getPreRenewalDirections();
		String editPreRenewalOpenDate = driver.findElement(By.xpath("//div[contains(@id, ':PreRenewalInputSet:0-body')]/div/table/tbody/tr/child::td[5]/div")).getText();
		Assert.assertTrue(preRenewalListOpenDate.getDirectionList().get(0).getOpenedDate().equals(editPreRenewalOpenDate), "OPEN DATES ON PRE-RENEWAL LIST PAGE AND EDIT PAGE DID NOT MATCH. LISTPAGE: " + editPreRenewalOpenDate + " EDIT PAGE: " + preRenewalListOpenDate.getDirectionList().get(0).getOpenedDate());

	}




}




















