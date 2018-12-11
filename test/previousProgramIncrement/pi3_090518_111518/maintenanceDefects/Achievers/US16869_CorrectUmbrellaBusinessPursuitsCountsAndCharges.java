package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Achievers;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;

public class US16869_CorrectUmbrellaBusinessPursuitsCountsAndCharges extends BaseTest {


	@Test(enabled=true)
	public void umbrellaChargeForResidentsOver1() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = createPropertyLiability(driver);
		ArrayList<SquireLiablityCoverageIncidentalOccupancyItem> foo = new ArrayList<SquireLiablityCoverageIncidentalOccupancyItem>();
		foo.add(new SquireLiablityCoverageIncidentalOccupancyItem("Howdy", mySquire.propertyAndLiability.locationList.get(0)));
		SectionIICoverages boo = new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, foo);
		mySquire.propertyAndLiability.liabilitySection.getSectionIICoverageList().add(boo);

		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.FullApp);

		myPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(myPolicy);
		
		new SideMenuPC(driver).clickSideMenuQuote();
		String quantity1 = new GenericWorkorderQuote(driver).getDescriptionQuantity("Each Additional Owner Occupied Residence Over 1");
		String premium1 = new GenericWorkorderQuote(driver).getDescriptionPremium("Each Additional Owner Occupied Residence Over 1");
		String quantity2 = new GenericWorkorderQuote(driver).getDescriptionQuantity("Each Additional Office Premises");
		String premium2 = new GenericWorkorderQuote(driver).getDescriptionPremium("Each Additional Office Premises");
		String quantity3 = new GenericWorkorderQuote(driver).getDescriptionQuantity("Each Incidental Occupancy");
		String premium3 = new GenericWorkorderQuote(driver).getDescriptionPremium("Each Incidental Occupancy");
		
		
		softAssert.assertTrue((Integer.valueOf(quantity1)-1) * 10 == Integer.valueOf(premium1.replace("-", "0").replace("$", "").replace(".00", "")), "PREMIUM CHARGED FOR Each Additional Owner Occupied Residence Over 1 DOES NOT MATCH EXPECTED. FOUND| " + premium1 + " EXPECTED| " + (Integer.valueOf(quantity1)-1) * 10);
		softAssert.assertTrue((Integer.valueOf(quantity2)) * 10 == Integer.valueOf(premium2.replace("-", "0").replace("$", "").replace(".00", "")), "PREMIUM CHARGED FOR Each Additional Office Premises DOES NOT MATCH EXPECTED. FOUND| " + premium2 + " EXPECTED| " + (Integer.valueOf(quantity2)) * 10);
		softAssert.assertTrue((Integer.valueOf(quantity3)) * 18 == Integer.valueOf(premium3.replace("-", "0").replace("$", "").replace(".00", "")), "PREMIUM CHARGED FOR Each Incidental Occupancy DOES NOT MATCH EXPECTED. FOUND| " + premium3 + " EXPECTED| " + (Integer.valueOf(quantity3)) * 18);

		softAssert.assertAll();
	}


	private SquirePropertyAndLiability createPropertyLiability(WebDriver driver) {
		GuidewireHelpers foo = new GuidewireHelpers(driver);
		boolean residencePremises = foo.getRandBoolean();
		boolean vacationHome = foo.getRandBoolean();
		boolean condominiumResidencePremises = foo.getRandBoolean();
		boolean condominiumVacationHome = foo.getRandBoolean();
		boolean residencePremisesCovE = foo.getRandBoolean();
		boolean vacationHomeCovE = foo.getRandBoolean();
		boolean condoVacationHomeCovE = foo.getRandBoolean();
		boolean dwellingUnderConstructionCovE = foo.getRandBoolean();
		boolean contents = foo.getRandBoolean();

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		locationsList.add(new PolicyLocation(locOnePropertyList));
		PLPolicyLocationProperty locationOnePropertyFreeOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOnePropertyFreeOne);
		SquireLiability liabilitySection = new SquireLiability(); 
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		myPropertyAndLiability.liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

		if(residencePremises) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
			locOnePropertyList.add(locationOneProperty);
		}
		if(vacationHome) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
			locOnePropertyList.add(locationOneProperty);
		}
		if(condominiumResidencePremises) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.CondominiumResidencePremise);
			locOnePropertyList.add(locationOneProperty);
		}
		if(condominiumVacationHome) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome);
			locOnePropertyList.add(locationOneProperty);
		}
		if(residencePremisesCovE) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
			locOnePropertyList.add(locationOneProperty);
		}
		if(vacationHomeCovE) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.VacationHomeCovE);
			locOnePropertyList.add(locationOneProperty);
		}
		if(condoVacationHomeCovE) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.CondoVacationHomeCovE);
			locOnePropertyList.add(locationOneProperty);
		}
		if(dwellingUnderConstructionCovE) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
			locOnePropertyList.add(locationOneProperty);
		}
		if(contents) {
			PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.Contents);
			locOnePropertyList.add(locationOneProperty);
		}
		
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.FarmOffice);
		locOnePropertyList.add(locationOneProperty);

		return myPropertyAndLiability;
	}
}






















