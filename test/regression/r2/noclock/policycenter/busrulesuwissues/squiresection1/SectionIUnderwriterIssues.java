package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.SectionI_UnderwriterIssues;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
public class SectionIUnderwriterIssues extends BaseTest {
	private WebDriver driver;
    GeneratePolicy myPolicy;


	@Test
	@Parameters({"uwIssue"})
	public void testSectionIUWIssues(@Optional("PR005_Additionalguncoveragehighlimit") String uwIssue) throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SectionI_UnderwriterIssues myUWIssues = SectionI_UnderwriterIssues.valueOfName(uwIssue);

        myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(createGenerateObject(myUWIssues))
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("SectI", "UWIssues")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.getUWIssues()
				.build(GeneratePolicyType.FullApp);
		
		
		assertTrue(myPolicy.allUWIssuesAfterFA.isInList(returnUWIssueText(myUWIssues)).equals(myUWIssues.getUWIssueType()), "Foobar");
	}
	
	private String returnUWIssueText(SectionI_UnderwriterIssues myUWIssues) {
		switch(myUWIssues) {
		case PR001_10000Deductible:
			return myUWIssues.getLongDescription().replace("<<Deductible>>", myPolicy.squire.propertyAndLiability.section1Deductible.getValue());
		case PR002_PropertyValueGreaterThan750000:
			for(PolicyLocation location : myPolicy.squire.propertyAndLiability.locationList) {
				for(PLPolicyLocationProperty property : location.getPropertyList()) {
					if(property.getUWIssue().equals(myUWIssues)) {
						return myUWIssues.getLongDescription().replace("<<property number>>", String.valueOf(property.getPropertyNumber()));
					}
				}
			}
			break;
		case PR003_Only4RentalUnitsAllowed:
			return myUWIssues.getLongDescription();
		case PR004_Morethan1rentalunitperproperty:
			for(PolicyLocation location : myPolicy.squire.propertyAndLiability.locationList) {
				for(PLPolicyLocationProperty property : location.getPropertyList()) {
					if(property.getUWIssue().equals(myUWIssues)) {
						return myUWIssues.getLongDescription().replace("<<property number>>", String.valueOf(property.getPropertyNumber())).replace("<<location number>>", String.valueOf(location.getNumber()));
					}
				}
			}
			break;
		case PR005_Additionalguncoveragehighlimit:
			for(PolicyLocation location : myPolicy.squire.propertyAndLiability.locationList) {
				for(PLPolicyLocationProperty property : location.getPropertyList()) {
					if(property.getUWIssue().equals(myUWIssues)) {
						return myUWIssues.getLongDescription().replace("<<Property number>>", String.valueOf(property.getPropertyNumber())).replace("<<location number>>", String.valueOf(location.getNumber()));
					}
				}
			}
			break;
		case PR006_CovApropertypriorto1954:
			for(PolicyLocation location : myPolicy.squire.propertyAndLiability.locationList) {
				for(PLPolicyLocationProperty property : location.getPropertyList()) {
					if(property.getUWIssue().equals(myUWIssues)) {
						return myUWIssues.getLongDescription().replace("<<property number>>", String.valueOf(property.getPropertyNumber())).replace("<<location number>>", String.valueOf(location.getNumber()));
					}
				}
			}
			break;
		case PR007_Propertyover20yearsandCovE:
			break;
		case PR008_Addpropertyover1500000:
			break;
		case PR009_AddingEndorsement209:
			break;
		case PR010_Vacantproperty:
			break;
		case PR011_SquirePropertyshouldhaveatleastoneResidencePremisesCondominiumResidencePremisesResidencePremisesCovEContentsDwellingUnderConstructionorDwellingUnderConstructionCovE:
			break;
		case PR022_RemovingorChangetoLivestock:
			break;
		case PR023_RemovingorChangetoCommodities:
			break;
		case PR051_UnclassifiedCovABuilding:
			break;
		case PR053_Squirechange:
			break;
		case PR054_Removingsection:
			break;
		case PR056_ANIchangetopolicy:
			break;
		case PR057_PNIorANIremoved:
			break;
		case PR059_Policymembercontactchange:
			break;
		case PR060_Newpropertyadded:
			break;
		case PR061_Existingpropertyremoved:
			break;
		case PR064_Propertydetailchange:
			break;
		case PR065_PropertyAdditionalinsuredchange:
			break;
		case PR067_SectionIdeductibleincrease:
			break;
		case PR068_Coveragechange:
			break;
		case PR069_LiabilitylimitonFR:
			break;
		case PR070_SectionIIcoveragedecrease:
			break;
		case PR071_AccessYesremoved:
			break;
		case PR085_TrelliswithWeightofIceandSnow:
			break;
		case PR087_BlockBindTrellisedHops:
			break;
		case PR090_Defensibleornot:
			break;
		case PR095_CLUEPropertynotordered:
			break;
		case PR097_NoPropertyDetailentered:
			break;
		}
		
		return null;
	}


	private Squire createGenerateObject(SectionI_UnderwriterIssues uwIssue) {
		Squire mySquire = new Squire();
		switch(uwIssue) {
		case PR001_10000Deductible:
			mySquire.propertyAndLiability.section1Deductible = (new GuidewireHelpers(driver).getRandBoolean())? SectionIDeductible.TwentyFiveThousand: SectionIDeductible.TenThousand;
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().setLimit(201000);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setUWIssue(uwIssue);
			break;
		case PR002_PropertyValueGreaterThan750000:
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().setLimit(751000);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setUWIssue(uwIssue);
			break;
		case PR003_Only4RentalUnitsAllowed:
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setNumberOfUnits(NumberOfUnits.FourUnits);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setpropertyType(PropertyTypePL.DwellingPremises);
			PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty();
			myProperty.setNumberOfUnits(NumberOfUnits.TwoUnits);
			myProperty.setpropertyType(PropertyTypePL.DwellingPremises);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().add(myProperty);
			break;
		case PR004_Morethan1rentalunitperproperty:
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setNumberOfUnits(NumberOfUnits.FourUnits);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setUWIssue(uwIssue);
			break;
		case PR005_Additionalguncoveragehighlimit:
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().setGuns(true);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().setGuns_IncreaseTheftLimit(200001);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setUWIssue(uwIssue);
			break;
		case PR006_CovApropertypriorto1954:
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setYearBuilt(1953);
			mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).setUWIssue(uwIssue);
			break;
		case PR007_Propertyover20yearsandCovE:
			break;
		case PR008_Addpropertyover1500000:
			break;
		case PR009_AddingEndorsement209:
			break;
		case PR010_Vacantproperty:
			break;
		case PR011_SquirePropertyshouldhaveatleastoneResidencePremisesCondominiumResidencePremisesResidencePremisesCovEContentsDwellingUnderConstructionorDwellingUnderConstructionCovE:
			break;
		case PR022_RemovingorChangetoLivestock:
			break;
		case PR023_RemovingorChangetoCommodities:
			break;
		case PR051_UnclassifiedCovABuilding:
			break;
		case PR053_Squirechange:
			break;
		case PR054_Removingsection:
			break;
		case PR056_ANIchangetopolicy:
			break;
		case PR057_PNIorANIremoved:
			break;
		case PR059_Policymembercontactchange:
			break;
		case PR060_Newpropertyadded:
			break;
		case PR061_Existingpropertyremoved:
			break;
		case PR064_Propertydetailchange:
			break;
		case PR065_PropertyAdditionalinsuredchange:
			break;
		case PR067_SectionIdeductibleincrease:
			break;
		case PR068_Coveragechange:
			break;
		case PR069_LiabilitylimitonFR:
			break;
		case PR070_SectionIIcoveragedecrease:
			break;
		case PR071_AccessYesremoved:
			break;
		case PR085_TrelliswithWeightofIceandSnow:
			break;
		case PR087_BlockBindTrellisedHops:
			break;
		case PR090_Defensibleornot:
			break;
		case PR095_CLUEPropertynotordered:
			break;
		case PR097_NoPropertyDetailentered:
			break;
		}

		return mySquire;
	}











}

















