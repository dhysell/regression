package regression.r2.noclock.policycenter.change.subgroup9;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
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

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.workorders.submission.SubmissionProductSelection;

/**
 * @Author skandibanda
 * @Requirement : DE4463 : Umbrella Quote validation not triggering
 * @Description -User should not be able to select Umbrella until they have changed the effective date
 * to when the squire actually qualifies to have an umbrella
 * @DATE Dec 14, 2016
 */
public class TestUmbrellaQuoteValidation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy polToReturn;

	// Create a squire auto only policy with liability limits 100/300/100
	@Test()
	public void testGenerateSquireAutoOnlyIssuance() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withProductType(ProductLineType.Squire)				
				.withPolOrgType(OrganizationType.Individual)
				.withInsFirstLastName("Test", "Auto")	
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	
	}

	//verify  user can't select Umbrella on New Submission
	@Test (dependsOnMethods = { "testGenerateSquireAutoOnlyIssuance" })
    public void verifyUmbrellaProductSelectButtonNotExists() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(polToReturn.agentInfo.getAgentUserName(),polToReturn.agentInfo.getAgentPassword(),polToReturn.accountNumber);
        ActionsPC actions = new ActionsPC(driver);

		actions.click_Actions();
		actions.click_NewSubmission();

		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		if(selectProductPage.checkSelectButtonExists(ProductLineType.PersonalUmbrella)){
			Assert.fail("Umbrella product Select button should not exist when Squire policy has SectionIII Auto only.");
		}
	}
	
}
