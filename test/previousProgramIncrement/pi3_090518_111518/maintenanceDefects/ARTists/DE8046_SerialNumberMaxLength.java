package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;

public class DE8046_SerialNumberMaxLength extends BaseTest {
	
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;
	
	@Test(enabled = true)
	public void renewPolicyAndVerifyActivites() throws Exception {
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


		SquireInlandMarine myInlandMarine = new SquireInlandMarine();
		PersonalPropertyScheduledItem myItem = new PersonalPropertyScheduledItem();
		myItem.setParentPersonalPropertyType(PersonalPropertyType.TelephoneEquipment);
		myItem.setDescription("Big @$$ rock!!!");
		myItem.setYear(2018);
		myItem.setMake("It Goes Ring Ring");
		myItem.setModel("Might Be The Blue One");
		myItem.setVinSerialNum(StringsUtils.generateRandomNumberDigits(30));
		myItem.setLimit(25000);

		PersonalProperty myPersonalProperty = new PersonalProperty();
		myPersonalProperty.setType(PersonalPropertyType.TelephoneEquipment);
		myPersonalProperty.setLimit(25000);
		myPersonalProperty.setDeductible(PersonalPropertyDeductible.Ded100);
		myPersonalProperty.getScheduledItems().add(myItem);
		myInlandMarine.personalProperty_PL_IM.add(myPersonalProperty);
		myInlandMarine.inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.inlandMarine = myInlandMarine;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
				.build(GeneratePolicyType.QuickQuote);
        
        new Login(driver).loginAndSearchSubmission(myPolicyObject);
        new SideMenuPC(driver).clickSideMenuStandAloneIMPersonalProperty();
        String serialNumber = new GuidewireHelpers(driver).find(By.xpath("//label[contains(text(), 'VIN/Serial #')]/parent::td/following-sibling::td/div")).getText();
        Assert.assertTrue(serialNumber.length() == 20, "SERIAL NUMBER DID NOT GET REDUCED TO 20 CHARACTERS WHEN A VALUE OF 30 CHARACTERS WAS ADDED. NUMBER ENTERED: " + myItem.getVinSerialNum() + " NUMBER FOUND: " + serialNumber);
        Assert.assertTrue(myItem.getVinSerialNum().startsWith(serialNumber), "CONCADINATED SERIAL NUMBER DID NOT START WITH SERIAL NUMBER ENTERED. NUMBER ENTERED: " + myItem.getVinSerialNum() + " NUMBER FOUND: " + serialNumber);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}















