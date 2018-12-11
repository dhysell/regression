package regression.r2.noclock.policycenter.change.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

/**
* @Author nvadlamudi
* @Requirement :DE4013: Common -  Expiration Date change UW issues triggering
* @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/63379912013">Link Text</a>
* @Description 
* @DATE Mar 1, 2017
*/
public class TestStandardFirePolicyChangeWithExpiryDate extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;

	@Test()
	public void testGenerateStandardFireIssuance() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        myPolicyObj = new GeneratePolicy.Builder(driver)
        		.withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("STDFire", "UWIssue")
				.withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPolTermLengthDays(80)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"testGenerateStandardFireIssuance"})
	private void testPolicyChangeUWIssue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
	  	new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

	  	Date newExpiryDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 85);
		
	  	//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickExpirationDateChange();
        policyChangePage.setDescription("Testing Purpose");
        policyChangePage.clickPolicyChangeNext();
        policyChangePage.setExpirationDate(newExpiryDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();

		boolean messageFound = false;
		for (int i = 0; i < risk.getUWIssuesList().size(); i++) {
			String currentUWIssueText = risk.getUWIssuesList().get(i).getText();
			if ((!currentUWIssueText.contains("Blocking Bind"))) {

				if (currentUWIssueText.contains("Expiry Date Change")) {
					messageFound = true;
					break;
				}
			}
		}
		
		if (messageFound) {
			throw new Exception("The message 'Expiry Date Change' was found in any of the block binds.");
		}	
		
	}
}
