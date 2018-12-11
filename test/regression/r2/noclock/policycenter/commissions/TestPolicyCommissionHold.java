package regression.r2.noclock.policycenter.commissions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * There is a issue with OK button commissions so this test is failing.
 * https://www.flowdock.com/app/idaho-farm-bureau/policy-center-pl/threads/6H8ble1Tr9Kxn8sYHTr_Vl_pqyI
 * US7810: COMMON - New requirements on Edit Commission Holds button on Account File Summary
 */
/**
 * @Author
 * @Requirement :US7742 : PL - Create ability to hold commission
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/55479396354">Rally Story</a>
 * @Description : Check UW Supervisor can hold the commission and remove holds,also check for image logo displayed
 * @DATE June 16, 2016
 */
@QuarantineClass
public class TestPolicyCommissionHold extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squirePolicyObj = null;
	private Underwriters uw;

	@Test 
	public void testGenerateSquirePolicy() throws Exception {

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = myAuto;

        this.squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withInsFirstLastName("Test", "Squire")
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)	
				.build(GeneratePolicyType.PolicyIssued);
		}
	
	@Test (dependsOnMethods = {"testGenerateSquirePolicy"})
	public void validateCommissionHolds() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		   
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword() , squirePolicyObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickEditCommissionHolds();

        boolean testFailed = false;
		String errorMessage = "";
		
		//check for policy number exists
        if (!accountSummaryPage.getPolicyNumber().equals(squirePolicyObj.squire.getPolicyNumber()) && accountSummaryPage.getPolicyHolds().equals("No")) {
			testFailed = true;
			errorMessage = errorMessage + "policy number "+accountSummaryPage.getPolicyNumber() +"policy hold? status "+accountSummaryPage.getPolicyHolds()+" is not displayed. \n";	
		}
		
		//check commission hold
		if(!accountSummaryPage.selectPolicyHolds().equals(accountSummaryPage.getPolicyHolds())){
			testFailed = true;
			errorMessage = errorMessage + "Commissions Holds? not successul"+ accountSummaryPage.getPolicyHolds()+"\n";			
		}
		
		//check commission held image logo exist top left corner
		if(!accountSummaryPage.checkHoldImageStatus()){
			testFailed = true;
			errorMessage = errorMessage + "Commissions Holds Image logo is not displayed "+ accountSummaryPage.checkHoldImageStatus()+"\n";			
		}
		
		//remove commission hold
		if(accountSummaryPage.selectPolicyHolds().equals(accountSummaryPage.getPolicyHolds())){
			testFailed = true;
			errorMessage = errorMessage + "Commissions Holds? not successul"+ accountSummaryPage.getPolicyHolds()+"\n";			
		}
		
		if(testFailed)
			Assert.fail(errorMessage);
	}
	
	
}