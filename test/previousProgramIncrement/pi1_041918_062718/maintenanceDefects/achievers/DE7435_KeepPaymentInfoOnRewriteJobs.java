package previousProgramIncrement.pi1_041918_062718.maintenanceDefects.achievers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.rewrite.StartRewrite;

/**
* @Author jlarsen
* @Requirement 
* Have a policy issued and canceled.
Start rewrite full or new term within 45 days of the effective cancellation date.
Work rewrite and quote.
Go to payment screen and check for payment information.
Should see that payment information was brought back.
Remove that payment information.
Save job and leave job. 
Return to the job and go to payment screen.
Actual: On the payment screen you see the payment information that was brought back again.
Expected: The payment information should only be brought back on job creation, NOT every time you go to payment screen.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/214467293184">DE7435</a>
* @Description Bringing Back Payment Information for Rewrite New/Full Term on Job Creation
* @DATE May 3, 2018
*/
public class DE7435_KeepPaymentInfoOnRewriteJobs extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	@Test(enabled=true)
	public void checkPaymentInfo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -25))
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.build(GeneratePolicyType.PolicyIssued);
		
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		StartCancellation cancelation = new StartCancellation(driver);
		cancelation.cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHA YOU DON'T HAVE INSURANCE", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -5), true);
		cancelation.clickViewPolicyLink();
		StartRewrite rewrite = new StartRewrite(driver);
		rewrite.startRewriteNewTerm();
		////////////////////////////////
		//QUALIFICATIONS AND NEXT
        new SideMenuPC(driver).clickSideMenuQualification();
		new GenericWorkorderQualification(driver).fillOutFullAppQualifications(myPolicyObject);
		rewrite.visitAllPages(myPolicyObject);
        rewrite.clickGenericWorkorderQuote();
        new SideMenuPC(driver).clickSideMenuPayment();
		List<WebElement> downPayment  = driver.findElements(By.xpath("//div[contains(@id, ':PaymentInfotLV-body')]/div/table/tbody/child::tr"));
		Assert.assertFalse(downPayment.isEmpty(), "DOWN PAYMENT WAS NOT READDED AFTER IT WAS REMOVED DURING FIRST VISIT TO THE PAYMENT PAGE");
        new GenericWorkorderPayment(driver).removePaymentInfo();
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        new SideMenuPC(driver).clickSideMenuPayment();
		//VERIFY PAYMENT STILL REMOVED.
		List<WebElement> foo  = driver.findElements(By.xpath("//div[contains(@id, ':PaymentInfotLV-body')]/div/table/tbody/child::tr"));
		Assert.assertTrue(foo.isEmpty(), "DOWN PAYMENT WAS READDED AFTER IT WAS REMOVED OFF OF REWRITE NEW TERM JOB.");
		
	}
}



















