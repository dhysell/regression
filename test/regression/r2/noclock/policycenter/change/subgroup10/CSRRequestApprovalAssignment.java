package regression.r2.noclock.policycenter.change.subgroup10;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.helpers.CSRsHelper;

@QuarantineClass
public class CSRRequestApprovalAssignment extends BaseTest {
	private WebDriver driver;
	// Instance Data

	private GeneratePolicy newPolicy;
	private CSRs myCSR;

	@Test(description = "Generates Policy", enabled = true)
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());

		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		// GENERATE POLICY
        newPolicy = new GeneratePolicy.Builder(driver).withPolOrgType(OrganizationType.Joint_Venture)
				.withInsCompanyName("CSR Policy Change").withPolicyLocations(locationsList).withPolTermLengthDays(360)
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Credit_Debit)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.build(GeneratePolicyType.PolicyIssued);

	}

	@SuppressWarnings("unused")
	private void getCSR() throws Exception {
		String region = newPolicy.agentInfo.getAgentRegion();
		region = region.substring(0, 8);
		this.myCSR = CSRsHelper.getCSRByRegion(region);
	}

	/**
	 * @Author sbroderick
	 * @Requirement When the CSR clicks Request Approval, the review and approve
	 *              activity is not assigning to anyone and is sitting there
	 *              until the CSR calls. Ensure CSR is able to assign the
	 *              Activity.
	 * @RequirementsLink <a href=
	 *                   "https://rally1.rallydev.com/#/9877319305d/detail/defect/46142384454">
	 *                   Link to Defect </a>
	 * @Description This test creates a policy, as a csr add an additional
	 *              insured with special wording, requests approval, assigns UW
	 *              Activity to UW.
	 * @DATE Nov 18, 2015
	 */

	@Test(dependsOnMethods = { "generatePolicy" })
	public void csrPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		String region = newPolicy.agentInfo.getAgentRegion();
		region = region.substring(0, 8);
		myCSR = CSRsHelper.getCSRByRegion(region);		
		new Login(driver).loginAndSearchPolicyByAccountNumber(myCSR.getCsruserName(), myCSR.getCsrPassword(), newPolicy.accountNumber);
		//loginToProductAndSearchPolicyByAccountNumber(ApplicationOrCenter.PolicyCenter, myCSR.getCsruserName(), myCSR.getCsrPassword(),
				//newPolicy.accountNumber);
        StartPolicyChange changePolicy = new StartPolicyChange(driver);
		PolicyBusinessownersLineAdditionalInsured newBOLInsured = new PolicyBusinessownersLineAdditionalInsured(
				ContactSubType.Company) {
			{
				this.setSpecialWording(true);
				this.setSpecialWordingDesc("This is special wording that will need UW approval");
			}
		};
//		changePolicy.addAdditionalInsuredBOLCSR(newBOLInsured);
		
		
		changePolicy.startPolicyChange("Add New Insured on BOL", null);
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLineIncludedCoverages bol = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        bol.addAdditionalInsureds(newPolicy.basicSearch, newBOLInsured, true);

        GenericWorkorder quoteMe = new GenericWorkorder(driver);
        quoteMe.clickGenericWorkorderQuote();
		
		
		
//		changePolicy.requestApproval(newPolicy, myCSR);
		GenericWorkorderRiskAnalysis_UWIssues uwIssuesTab = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        uwIssuesTab.handleBlockSubmitCSR(newPolicy, myCSR);

	}

}
