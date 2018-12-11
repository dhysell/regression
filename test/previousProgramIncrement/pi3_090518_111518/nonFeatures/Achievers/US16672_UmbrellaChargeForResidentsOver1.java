package previousProgramIncrement.pi3_090518_111518.nonFeatures.Achievers;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

public class US16672_UmbrellaChargeForResidentsOver1 extends BaseTest {


	@Test(enabled=false)
	public void umbrellaChargeForResidentsOver1() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		PolicyLocation secondLocation = new PolicyLocation();
		secondLocation.getPropertyList().clear();
		locationsList.add(secondLocation);

		SquireLiability liabilitySection = new SquireLiability();      

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		myPropertyAndLiability.liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.FullApp);
		
		myPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicy.agentInfo.agentUserName, myPolicy.agentInfo.agentPassword, myPolicy.accountNumber);


	}















}
