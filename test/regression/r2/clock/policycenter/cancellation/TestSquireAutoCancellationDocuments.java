package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE5178: Cancel with a lessor/lienholder and no cancel for lessor/lienholder in documents
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/101083786196/tasks"></a>
 * @Description
 * @DATE Apr 26, 2017
 */
@QuarantineClass
public class TestSquireAutoCancellationDocuments extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySqAutoOnlyPol;
	private ARUsers arUser;

	@Test
	public void testIssueSquireAutoOnlyPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.TenK, true, UninsuredLimit.Fifty, false, UnderinsuredLimit.Fifty);
				
		ArrayList<AdditionalInterest> vehAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest vehAddInterest = new AdditionalInterest(ContactSubType.Company);
		vehAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		vehAddInterest.setLienholderNumber("1917617");
		vehAddInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIIIAuto);
		vehAdditionalInterests.add(vehAddInterest);
				
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();	
		toAdd.setAdditionalInterest(vehAdditionalInterests);
		toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		toAdd.setCollisionDeductible(Comprehensive_CollisionDeductible.OneThousand1000);
		vehicleList.add(toAdd);
		
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        mySqAutoOnlyPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withInsFirstLastName("Cancel", "Docs")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"testIssueSquireAutoOnlyPol"})
	private void testCloseAllTroubleTicketsRunInvoices() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqAutoOnlyPol.squire.getPolicyNumber());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		// Move clock 1 day
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		// Run Inoice due
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		// Run Workflows
		new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        BCTopMenuPolicy topMenuStuff = new BCTopMenuPolicy(driver);
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(mySqAutoOnlyPol.squire.getPolicyNumber());

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required
		new GuidewireHelpers(driver).logout();

	}

	@Test(dependsOnMethods = { "testCloseAllTroubleTicketsRunInvoices" })
	public void testPolicyForCancellationDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySqAutoOnlyPol.squire.getPolicyNumber());

        PolicySummary pcSummary = new PolicySummary(driver);
		List<WebElement> list = pcSummary.getPendingPolicyTransaction(TransactionType.Cancellation);
		
		if(list.size() > 0){
            SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuToolsDocuments();
            PolicyDocuments docs = new PolicyDocuments(driver);
			docs.selectRelatedTo("Cancellation");
			docs.clickSearch();
            boolean vehAdditonalInterest = false, insuredName = false;
			for(String name: docs.getDocumentNameAddress()){
				if(name.contains(this.mySqAutoOnlyPol.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getCompanyName())){
					vehAdditonalInterest = true;
				}
				if(name.contains(this.mySqAutoOnlyPol.pniContact.getFirstName())){
					insuredName = true;
				}
			}
			
			if(!vehAdditonalInterest || !insuredName){
				Assert.fail("Non Payment Cancellation not displayed for Insured and Additional Interest.");
			}
		}else{
			Assert.fail("Pending Cancellation for A/R No-Payment is not displayed.");
		}
	}
}
