package regression.r2.noclock.policycenter.cancellation;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorder;
import persistence.globaldatarepo.entities.Agents;
public class PolicyNotTaken extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String accountNumber;
	private String agentUserName;
	private String agentPassword;

	public Guidewire8Select select_NotTakenReason() {
		return new Guidewire8Select(driver, "//table[contains(@id,':RejectReason-triggerWrap')]");
	}

	@Test
	public void generate() throws Exception {

		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Policy NotTaken")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.QuickQuote);

		accountNumber = myPolicyObj.accountNumber;
		Agents agent = myPolicyObj.agentInfo;
		agentUserName = agent.getAgentUserName();
		agentPassword = agent.getAgentPassword();
	}

	/**
	 * DE2496 Finds if the item 'Policy not-taken' is still in the Not Taken
	 * Reason list.
	 * 
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "generate" })
	public void testPolicyNotTaken() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchJob(agentUserName, agentPassword, accountNumber);

        GenericWorkorder genericWorkOrder = new GenericWorkorder(driver);
		genericWorkOrder.clickNotTaken();

		Guidewire8Select notTakenReason = select_NotTakenReason();
		boolean isFound = notTakenReason.isItemInList("Policy not-taken");
		if (isFound) {
			Assert.fail("'Policy not-taken' should not be a reason in the Not Taken dropdown list.");
		}
	}

}
