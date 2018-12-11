package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

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
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.renewal.StartRenewal;

/**
* @Author jlarsen
* @Requirement As an Underwriter supervisor, I want additional discounts to be removed automatically from the modifiers at the Renewal.
Steps to get there:
Create a policy with additional discounts
start renewal
verify additional discounts removed from the modifiers.
Acceptance Criteria:
Ensure Additional discounts are removed automatically from the modifiers at the renewal.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/211179068640">US14610</a>
* @Description Automatically remove "Additional Discounts" on modifiers at Renewal
* @DATE May 17, 2018
*/
public class US14610_ClearSpecialUnderwriterAdjustmentsAtRenewal_P1S3 extends BaseTest {
	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;
	
	@Test(enabled = true)
	public void removeAdditionalDiscountsAtRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		PLPolicyLocationProperty propertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		propertyOne.getPropertyCoverages().getCoverageA().setLimit(56000);

		locOnePropertyList.add(propertyOne);
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
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission("lraschke", "gw", myPolicyObject.accountNumber);//I KNOW HARD CODING IS BAD BUT IT IS A PERSON WITH A SPECIFIC PERMISSION THAT CAN EDIT THIS FIELD :( 
		new GuidewireHelpers(driver).editPolicyTransaction();

        new SideMenuPC(driver).clickSideMenuModifiers();
        new GenericWorkorderModifiers(driver).setModifiersSpecialUnderwritingAdjustmentCreditDebit(NumberUtils.generateRandomNumberInt(1, 10));
        new GenericWorkorderModifiers(driver).setModifiersSpecialUnderwritingAdjustmentJustification("jawef ajekflawjkf aksdnzsjkdfna ksdfnasdknf");
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        new GuidewireHelpers(driver).logout();
		
		myPolicyObject.convertTo(driver, GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		new StartRenewal(driver).startRenewal();
        new SideMenuPC(driver).clickSideMenuModifiers();
		Assert.assertTrue(driver.findElements(By.xpath("//div[contains(text(), 'Special Underwriting Adjustment')]")).isEmpty(), "Special Underwriting Adjustment: MODIFYER WAS NOT CLEARED OUT AT RENEWAL.");
	}
}
