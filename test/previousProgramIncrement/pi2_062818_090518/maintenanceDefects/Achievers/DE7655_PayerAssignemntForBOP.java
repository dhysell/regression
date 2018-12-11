package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Achievers;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE THE USER CAN ACCESS THE PAYER ASSIGNMENT PAGE AFTER ADDING A SECND LOCATION
* @DATE Sep 20, 2018
*/
public class DE7655_PayerAssignemntForBOP extends BaseTest {

	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void epli() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		ArrayList<AdditionalInterest> foo = new ArrayList<AdditionalInterest>();
		foo.add(new AdditionalInterest(ContactSubType.Company));
		foo.get(0).setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding("63921"));
		boLine.locationList.get(0).getBuildingList().get(0).setAdditionalInterestList(foo);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(boLine)
				.build(GeneratePolicyType.PolicyIssued);
		
		
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(1).getBuildingList().add(new PolicyLocationBuilding("63921"));
		boLine.locationList.get(1).getBuildingList().get(0).setAdditionalInterestList(foo);
		
		
		new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObj);
		new StartPolicyChange(driver).startPolicyChange("ADD LOCATION", null);
		
		new SideMenuPC(driver).clickSideMenuLocations();
		new GenericWorkorderLocations(driver).clickLocationsNewLocation();
		new GenericWorkorderLocations(driver).addNewLocationAndBuildings(true, boLine.locationList.get(1), false, false);
		
		Assert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//td[contains(@id,'LOBWizardStepGroup:PayerAssignment')]/parent::tr")).isEmpty(), "PAYER ASSIGNMENT WAS NOT AVAILABLE AFTER ADDING A SECOND LOCATION DURING A POLICY CHANGE");
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
