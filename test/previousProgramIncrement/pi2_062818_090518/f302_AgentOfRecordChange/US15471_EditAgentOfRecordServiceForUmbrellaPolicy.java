package previousProgramIncrement.pi2_062818_090518.f302_AgentOfRecordChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.RoleAgentChange;

import java.util.ArrayList;

public class US15471_EditAgentOfRecordServiceForUmbrellaPolicy extends BaseTest {

	private GeneratePolicy myPolObj;	
	WebDriver driver;
	
	
	/**
	* @Author jlarsen
	* @Team ACHIEVERS
	* @Requirement 
	* @RequirementsLink <a href="http:// ">Link Text</a>
	* @Description ENSURE USERS WITH THE AGENT CHANGE ROLE CAN EDIT AGENTS ON A POLICY CHANGE
	* @DATE Sep 20, 2018
	* @throws Exception
	*/
	@Test
	public void editAgentOfRecordAndService() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		RoleAgentChange agentChangeUser = RoleAgentChange.getRandom();
		
		PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
		myProperty.getPropertyCoverages().getCoverageA().setLimit(350000);
		
		ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
		propertyList.add(myProperty);
		PolicyLocation myLocation = new PolicyLocation(propertyList);
		ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
		locationList.add(myLocation);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationList;

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.propertyAndLiability.liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);


		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.build(GeneratePolicyType.PolicyIssued);
		
		myPolObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
		new Login(driver).login(agentChangeUser.getUserName(), agentChangeUser.getPassword());
		new SearchPoliciesPC(driver).searchPolicyByPolicyNumber(myPolObj.squireUmbrellaInfo.getPolicyNumber());
		new StartPolicyChange(driver).startPolicyChange("change agent", null);
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//input[contains(@id, ':ProducerCodeOfRecord-inputEl')]")).isEmpty(), "AGENT OF RECORD WAS NOT EDITABLE ON POLICY CHANGE");
		softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//input[contains(@id, ':ProducerCode-inputEl')]")).isEmpty(), "AGENT OF SERVICE WAS NOT EDITABLE ON POLICY CHANGE");
		
		softAssert.assertAll();
		
	}
	
	
	
	
	
	
	
	
}
