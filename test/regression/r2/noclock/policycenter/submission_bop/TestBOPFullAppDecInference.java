package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

/**
 * @Author nvadlamudi
 * @Requirement :DE5161: BOP Declarations inferrring in Submission Full App
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/99767636352">Link Text</a>
 * @Description
 * @DATE Apr 20, 2017
 */
@QuarantineClass
public class TestBOPFullAppDecInference extends BaseTest {
    private GeneratePolicy bopPolicyObj;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();


    private WebDriver driver;

    @Test()
    public void testGenerateBusinessownersPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsCompanyName("Test BOP")
                .withBusinessownersLine()
                .build(GeneratePolicyType.PolicyIssued);

        eventsHitDuringSubmissionCreationPlusLocal.addAll(bopPolicyObj.policyForms.eventsHitDuringSubmissionCreation);
        formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal.addAll(bopPolicyObj.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);
        formsOrDocsActualFromUISubmissionPlusLocal.addAll(bopPolicyObj.policyForms.formsOrDocsActualFromUISubmission);
        actualExpectedDocDifferencesSubmissionPlusLocal.setInExpectedNotInUserInterface(bopPolicyObj.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface());
        actualExpectedDocDifferencesSubmissionPlusLocal.setInUserInterfaceNotInExpected(bopPolicyObj.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected());

    }

    @Test(dependsOnMethods = {"testGenerateBusinessownersPolicy"})
    private void testValidateBOPFullAppForms() {

        if (actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
            Assert.fail("ERROR: Documents for Issuance In UserInterface But Not in Expected - " + actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected());
        }

        if (actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            Assert.fail("ERROR: Documents for Issuance In Expected But Not in UserInterface - " + actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface());
        }
    }
}
