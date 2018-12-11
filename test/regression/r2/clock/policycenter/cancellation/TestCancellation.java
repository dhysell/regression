package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
@QuarantineClass
public class TestCancellation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;
	private String insFirstName = "Test";
	private String insLastName = "Auto";
	private String driverFirstName = "testDr";
	private Underwriters uw;	
	
	public void generateIssuedPA() throws Exception {
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		person.setFirstName(driverFirstName);
		driversList.add(person);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setComprehensive(true);
		toAdd.setCostNew(10000.00);
		toAdd.setCollision(true);
		toAdd.setAdditionalLivingExpense(true);
		vehicleList.add(toAdd);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -5))
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName(insFirstName, insLastName)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
	}
	
	@Test()
	public void cancelPaAgent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generateIssuedPA();
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1));
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickCancelPolicy();
        StartCancellation startCancel = new StartCancellation(driver);
		List<String> insuredSource = startCancel.getSource();
		if(!(insuredSource.size() == 2 && insuredSource.get(0).contains("<none>") && insuredSource.get(1).contains("Insured"))){
			Assert.fail("Agents Source Cancellation Reasons should contain none and insured.");
		}
		startCancel.setSourceReasonAndExplanation(CancellationSourceReasonExplanation.InsuredPassedAway);
        startCancel.clickStartCancellation();
        startCancel.clickNext();
		startCancel.clickSubmitOptionsCancelNow();
	}
	
	@Test()
	public void cancelPaUW() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generateIssuedPA();
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 2));
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickCancelPolicy();
        StartCancellation startCancel = new StartCancellation(driver);
		startCancel.verifyReasonExplanation();
		ArrayList<String> effectiveDateErrorList = new ArrayList<String>();
		if(effectiveDateErrorList.size()>0){
			for(String error : effectiveDateErrorList){
				System.out.println(error + "\n");
			}
		}
		
		startCancel.setSourceReasonAndExplanation(CancellationSourceReasonExplanation.InsuredPassedAway);
        startCancel.clickStartCancellation();
        startCancel.clickSubmitOptionsCancelNow();
//		startCancel.cancelPolicy(CancellationSourceReasonExplanation.CitationsOfListedDrivers, "UW Cancel", today, true);
		if(effectiveDateErrorList.size()>0){
			Assert.fail("Ensure the Reasons have the correct default cancellation date");
		}
	}
	
	@Test
	public void cancelPaFlat() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generateIssuedPA();
		Date today = new Date();
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        StartCancellation cancelMe = new StartCancellation(driver);
		cancelMe.cancelPolicy(CancellationSourceReasonExplanation.CloseDidNotHappen, "Insured Cancelled", today, true);
	}
	
	@Test
	public void changeCancelledPolicy() throws Exception{
		cancelPaUW();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        StartPolicyChange changePolicy = new StartPolicyChange(driver);
		changePolicy.startPolicyChange("Change", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1));
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehicles = new GenericWorkorderVehicles_Details(driver);
		vehicles.editVehicleByVehicleNumber(1);
		vehicles.selectCommunity(CommutingMiles.WorkSchool10PlusMiles);
		vehicles.clickOK();
        changePolicy = new StartPolicyChange(driver);
		changePolicy.quoteAndIssue();
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickPolicyContractVehicles();
        vehicles = new GenericWorkorderVehicles_Details(driver);
		if(!vehicles.getVehicleCommutingMiles().equals(CommutingMiles.WorkSchool10PlusMiles.getValue())){
			Assert.fail("The policy should reflect the change issued effective before the cancel.");
		}
		
		
	}
}
