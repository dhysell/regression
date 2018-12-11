package previousProgramIncrement.pi1_041918_062718.f127_BuildEquipmentBreakdownIntoPCForPersonallines;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;

/**
* @Author bhiltbrand
* @Requirement This test is used to create a farm and ranch policy and then verify that equipment breakdown is set by default on the policy.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558478112/detail/userstory/212636125056">US14759</a>
* @Description 
* @DATE May 22, 2018
*/
public class US14759_EquipmentBreakdownOptOut extends BaseTest {	
	public GeneratePolicy myPolicyObject = null;
	private int alfalfaMillCoverageLimit = 128000;
	private int toolsLimit = 32500;
	private int irrigationEquipmentLimit = 50000;
	private WebDriver driver;

	private void generateFarmAndRanch() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.AlfalfaMill));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);
        
        this.myPolicyObject = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("US14759", "FarmAndRanch")
                .withPolOrgType(OrganizationType.Individual)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
    }
	
	@Test(enabled = true)
    public void verifyEquipmentBreakdown() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generateFarmAndRanch();
		this.alfalfaMillCoverageLimit = this.myPolicyObject.getLocationList(myPolicyObject).get(0).getPropertyList().get(1).getPropertyCoverages().getCoverageA().getLimit();
		new Login(driver).loginAndSearchJob(this.myPolicyObject.agentInfo.agentUserName, this.myPolicyObject.agentInfo.agentPassword, this.myPolicyObject.accountNumber);
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new GenericWorkorderSquireFarmPersonalProperty(driver);
		
		new GuidewireHelpers(driver).editPolicyTransaction();
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
		pcFarmPersonalPropertyPage.checkCoverageD(true);
		pcFarmPersonalPropertyPage.selectCoverageType(FPPCoverageTypes.Schedule);
		pcFarmPersonalPropertyPage.selectDeductible(FPPDeductible.Ded_500);
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.IrrigationEquipment);
		if (!pcFarmPersonalPropertyPage.verifyEquipmentBreakdownIncludedExists(FarmPersonalPropertyTypes.IrrigationEquipment)) {
			Assert.fail("The Equipment Breakdown Included label did not show up on Irrigation Equipment. This should be here. Test Failed.");
		}
		if (!pcFarmPersonalPropertyPage.getEquipmentBreakdownIncludedValue(FarmPersonalPropertyTypes.IrrigationEquipment)) {
			Assert.fail("The Equipment Breakdown Included label was not 'Yes' on Irrigation Equipment. This Equipment Breakdown should now be opt-out, and no changes were made to the policy to opt-out. Test Failed.");
		}
		pcFarmPersonalPropertyPage.addItem(FPPFarmPersonalPropertySubTypes.CirclePivots, 2, this.irrigationEquipmentLimit, "Some Sprinklers");
		
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Tools);
		if (!pcFarmPersonalPropertyPage.verifyEquipmentBreakdownIncludedExists(FarmPersonalPropertyTypes.Tools)) {
			Assert.fail("The Equipment Breakdown Included label did not show up on Tools. This should be here. Test Failed.");
		}
		if (!pcFarmPersonalPropertyPage.getEquipmentBreakdownIncludedValue(FarmPersonalPropertyTypes.Tools)) {
			Assert.fail("The Equipment Breakdown Included label was not 'Yes' on Tools. This Equipment Breakdown should now be opt-out, and no changes were made to the policy to opt-out. Test Failed.");
		}
		pcFarmPersonalPropertyPage.addItem(FPPFarmPersonalPropertySubTypes.PowerTools, 2, this.toolsLimit, "It's over 9000!!!");
		
		pcFarmPersonalPropertyPage.selectCoverages(FarmPersonalPropertyTypes.Machinery);
		if (pcFarmPersonalPropertyPage.verifyEquipmentBreakdownIncludedExists(FarmPersonalPropertyTypes.Machinery)) {
			Assert.fail("The Equipment Breakdown Included label showed up on Machinery. This should NOT be here. Test Failed.");
		}
		pcFarmPersonalPropertyPage.unselectCoverages(FarmPersonalPropertyTypes.Machinery);
		
		pcPropertyDetailCoveragesPage.clickPropertyDetailCoverages();
		if (!pcPropertyDetailCoveragesPage.isEquipmentBreakdownChecked()) {
			Assert.fail("The Equipment Breakdown check box was not checked, yet it should have been. Test Failed.");
		}
		
		double equipmentBreakdownLimit = pcPropertyDetailCoveragesPage.getEquipmentBreakdownLimit();
		if ((this.irrigationEquipmentLimit + this.alfalfaMillCoverageLimit + this.toolsLimit) != equipmentBreakdownLimit) {
			Assert.fail("The automatically calculated limit for Equipment Breakdown in the UI did not match what we put in as limits in the test. Test Failed.");
		}
		
		pcPropertyDetailCoveragesPage.setEquipmentBreakdown(false);
		String popupMessage = new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		if (!popupMessage.contains("Equipment Breakdown Coverage will be removed from all items including eligible Buildings, Irrigation Equipment, and Tools on both FPP and Section IV. Are you sure you want to remove it?")) {
			Assert.fail("The popup message when de-selecting equipment breakdown did not contain the expected content.");
		}
	}
}



























