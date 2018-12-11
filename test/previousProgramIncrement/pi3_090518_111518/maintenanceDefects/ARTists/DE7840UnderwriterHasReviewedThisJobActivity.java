package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

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
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;


/**
* @Author nvadlamudi
* @Requirement :DE7840: �underwriter has reviewed this job� activity changing owner instead of completing
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/248499859216">Link Text</a>
* @Description : Verify that The 'underwriter has reviewed this job' activity is closed
* @DATE Sep 11, 2018
*/
public class DE7840UnderwriterHasReviewedThisJobActivity extends BaseTest {
  
    @Test
    public void testValidateUnderwriterHasReviewedActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;


        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Activity", "Review")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
        		myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

        PolicySummary polSummary = new PolicySummary(driver);
        Assert.assertTrue(!polSummary.checkIfActivityExists("Underwriter has reviewed this job"), "Still activity 'Underwriter has reviewed this job' exists.");
		
        
    }
 
}
