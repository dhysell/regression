package previousProgramIncrement.pi1_041918_062718.maintenanceDefects.achievers;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;

/**
* @Author jlarsen
* @Requirement Create squire policy.
On section III mark one of the drivers as excluded and add 304 driver exclusion to the policy.
Issue policy
Start umbrella policy. Enter all info. Click save draft.
Actual: Received DBNullConstraintException and unable to proceed.
Expected: Should be able to save, quote and issue umbrella without receiving the DBNullConstraintException.

(Also unable to remove the excluded driver from the umbrella policy even after doing a policy change and removing the exclusion from the driver on the squire policy)
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description DBNullConstraintException on umbrella policy when there is an excluded driver
* @DATE May 8, 2018
*/
public class DE7373_UmbrellaDBNullException extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;
 
	@Test(enabled = true)
	public void noMoreDBNulls() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property.getPropertyCoverages().getCoverageA().setLimit(301000);
		
		
		locOnePropertyList.add(property);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		
		Contact excludedDriver = new Contact("Excluded", "Driver");
		excludedDriver.setAge(driver, 34);
		excludedDriver.setExcludedDriver(true);

		SquirePersonalAuto myPersonalAuto = new SquirePersonalAuto();
		myPersonalAuto.addToDriversList(excludedDriver);
		

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.squirePA = myPersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.build(GeneratePolicyType.PolicyIssued);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);
		squireUmbrellaInfo.setEffectiveDate(myPolicyObject.squire.getEffectiveDate());
		squireUmbrellaInfo.setDraft(true);
		myPolicyObject.squireUmbrellaInfo = squireUmbrellaInfo;
		myPolicyObject.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);
		new Login(driver).loginAndSearchSubmission(myPolicyObject);
		new SideMenuPC(driver).clickSideMenuPolicyInfo();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		Assert.assertTrue(driver.findElements(By.xpath("//*[contains(text(), 'DBNullConstraintException:')]")).isEmpty(), "RECIEVED DB NULL MESSAGE AFTER CLICKING SAVE DRAFT.");
	}
}
