package regression.r2.noclock.policycenter.documents;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import regression.r2.noclock.policycenter.submission_bop.TestBind;

@QuarantineClass
public class TestFormInferenceSubmissionIssuance extends BaseTest {

    public GeneratePolicy bindObject = null;
    public GeneratePolicy issuanceObject = null;

    private WebDriver driver;

    @Test
    public void testFormsAtSubmission() throws Exception {

        TestBind bound = new TestBind();

        bound.testBasicBindInsuredOnly();

        this.bindObject = bound.myPolicyObjInsuredOnly;

        String errorMessage = "";

        if (this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected().size() > 0) {
            errorMessage = errorMessage + "ERROR: Documents for Submision In UserInterface But Not in Expected - " + this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface() + "\n";
        }

        if (this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface().size() > 0) {
            errorMessage = errorMessage + "ERROR: Documents for Submision In Expected But Not in UserInterface - " + this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface() + "\n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testFormsAtSubmission"})
    public void testFormsAtIssuance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.bindObject.convertTo(driver, GeneratePolicyType.PolicyIssued);

        String errorMessage = "";

        if (this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected().size() > 0) {
            errorMessage = errorMessage + "ERROR: Documents for Issuance In UserInterface But Not in Expected - " + this.issuanceObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface() + "\n";
        }

        if (this.bindObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface().size() > 0) {
            errorMessage = errorMessage + "ERROR: Documents for Issuance In Expected But Not in UserInterface - " + this.issuanceObject.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface() + "\n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testFormsAtIssuance"})
    public void testFormsAtIssuance_EquipmentBreakdown() {

        String errorMessage = "";

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInExpectedNotInUserInterface().contains("Equipment Breakdown Enhancement Endorsement")) {
            errorMessage = errorMessage + "ERROR: Documents for EquipBreakdown In Expected But Not in UserInterface" + "\n";
        }

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInUserInterfaceNotInExpected().contains("Equipment Breakdown Enhancement Endorsement")) {
            errorMessage = errorMessage + "ERROR: Documents for EquipBreakdown In UserInterface But Not in Expected" + "\n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testFormsAtIssuance"})
    public void testFormsAtIssuance_ProtectiveDevices() {

        String errorMessage = "";

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInExpectedNotInUserInterface().contains("Protective Device Letter")) {
            errorMessage = errorMessage + "ERROR: Documents for ProtectiveDevices In Expected But Not in UserInterface" + "\n";
        }

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInUserInterfaceNotInExpected().contains("Protective Device Letter")) {
            errorMessage = errorMessage + "ERROR: Documents for ProtectiveDevices In UserInterface But Not in Expected" + "\n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testFormsAtIssuance"})
    public void testFormsAtIssuance_ProtectiveSafeguards() {

        String errorMessage = "";

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInExpectedNotInUserInterface().contains("Protective Safeguards")) {
            errorMessage = errorMessage + "ERROR: Documents for ProtectiveSafeguards In Expected But Not in UserInterface" + "\n";
        }

        if (this.bindObject.policyForms.actualExpectedDocDifferencesIssuance.getInUserInterfaceNotInExpected().contains("Protective Safeguards")) {
            errorMessage = errorMessage + "ERROR: Documents for ProtectiveSafeguards In UserInterface But Not in Expected" + "\n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }

}
