package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.ISSupportUsers;
import persistence.globaldatarepo.helpers.ISSupportUsersHelper;

public class US15742_HideInsuranceScore extends BaseTest {
	
	private GeneratePolicy myPolicyObject;
	private WebDriver driver;
	
	@Test
	public void verifyInsurancScoreIsHidden() throws Exception {
		
		SoftAssert softAssert = new SoftAssert();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle newVeh = new Vehicle();
		newVeh.setEmergencyRoadside(true);
		vehicleList.add(newVeh);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
                .withLexisNexisData(true, false, false, true, true, true, false)
                .build(GeneratePolicyType.PolicyIssued);
        
        ISSupportUsers isSupport = ISSupportUsersHelper.getRandomUser();
        new Login(driver).loginAndSearchPolicyByAccountNumber(isSupport.getUserName(), isSupport.getPassword(), myPolicyObject.accountNumber);
        new PolicySummary(driver).clickCompletedTransactionByType(TransactionType.Issuance);
        new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScore-labelEl')]")).isEmpty(), "IS SUPPORT USER WAS NOT ABLE TO VIEW INSURANCE SCORE");
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScoreFiltered-labelEl')]")).isEmpty(), "IS SUPPORT USER WAS NOT ABLE TO VIEW ENHANCED INSURANCE SCORE");
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(text(), 'Reason Codes')]")).isEmpty(), "IS SUPPORT USER WAS NOT ABLE TO VIEW REASON CODES");
        new GuidewireHelpers(driver).logout();
        
        new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
        new StartPolicyChange(driver).startPolicyChange("JUST LOOKING", null);
		new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
		softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScore-labelEl')]")).isEmpty(), "UNDERWRITER WAS ABLE TO VIEW INSURANCE SCORE");
        softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScoreFiltered-labelEl')]")).isEmpty(), "UNDERWRITER WAS ABLE TO VIEW ENHANCED INSURANCE SCORE");
        softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(text(), 'Reason Codes')]")).isEmpty(), "UNDERWRITER WAS ABLE TO VIEW REASON CODES");
        new GuidewireHelpers(driver).logout();
        
        new Login(driver).loginAndSearchPolicyByAccountNumber(getTRTeamMemeber(), "gw", myPolicyObject.accountNumber);
        new PolicySummary(driver).clickCompletedTransactionByType(TransactionType.Issuance);
        new SideMenuPC(driver).clickSideMenuPLInsuranceScore();
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScore-labelEl')]")).isEmpty(), "TR TEAM USER WAS NOT ABLE TO VIEW INSURANCE SCORE");
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(@id, ':CreditReportScoreFiltered-labelEl')]")).isEmpty(), "TR TEAM USER WAS NOT ABLE TO VIEW ENHANCED INSURANCE SCORE");
        softAssert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//label[contains(text(), 'Reason Codes')]")).isEmpty(), "TR TEAM USER WAS NOT ABLE TO VIEW REASON CODES");
		
		
		softAssert.assertAll();
	}
	
	
	private String getTRTeamMemeber() {
		List<String> transitionTeam = new ArrayList<String>();
		transitionTeam.add("lekopp");
		transitionTeam.add("kbalginy");
		transitionTeam.add("ssimnitt");
		transitionTeam.add("hharper");
		transitionTeam.add("dstallings");
		
		return transitionTeam.get(NumberUtils.generateRandomNumberInt(0, transitionTeam.size()-1));
	}
	
	
	
	
}

