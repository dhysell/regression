package previousProgramIncrement.pi1_041918_062718.f144_FixRegularRenewalDefects;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.WaitUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.renewal.StartRenewal;

/**
* @Author jlarsen
* @Requirement 
* Have an existing PL or create a new PL policy with custom farming added.
Move the clock to renewal date -80
Run the 'Policy Renewal Start' batch
The activity for the agent should trigger and assign to the agent on file
Actual: The activity is tied to the last bound policy change
Expected: The activity should tie to the renewal job that it is triggered from
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/210867882812">DE7396</a>
* @Description Renewal activity for custom farming reciepts is not tieing to the renewal job
* @DATE Apr 19, 2018
*/
public class DE7396_RenewalActivtyLinkJob extends BaseTest {
	
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	@Test(enabled = true)
	public void verifyActivtyJobTie() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

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

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolTermLengthDays(79)
				.build(GeneratePolicyType.PolicyIssued);

        Login login = new Login(driver);
        login.loginAndSearchPolicy_asUW(myPolicyObject);

        new StartRenewal(driver).startRenewal();
        new StartRenewal(driver).quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObject);
        new InfoBar(driver).clickInfoBarPolicyNumber();
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicy_asAgent(myPolicyObject);
        new PolicySummary(driver).clickActivity("Custom Farming Amount Update Required");
        new WaitUtils(driver).waitUntilElementIsVisible(By.xpath("//span[contains(@id, ':JobLabel-btnInnerEl')]/span"), 3);
		String linkedJob = driver.findElement(By.xpath("//span[contains(@id, ':JobLabel-btnInnerEl')]/span")).getText();
		assertTrue(linkedJob.contains("Renewal"), "JOB LINKED TO CUSTOM FARMING AGENT ACTIVITY ON RENEWAL IS NOT LIKED TO THE RENEWAL JOB. LINKED TO JOB: " + linkedJob);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
















