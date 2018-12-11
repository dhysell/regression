package regression.r3.noclock.mini;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.helpers.EmailUtils;
import com.idfbins.driver.BaseTest;
import random.MiniRegression;

public class MiniRegression_CPPAuto_CPPGL extends BaseTest {

    private MiniRegression myMini = null;

    @BeforeClass
    public void beforeClass() {
        boolean debugMode = false;
        String debugEmail = "?";
        String environmentToCheck = "?";
        String environmentsToDeployTo = "?";

        myMini = new MiniRegression(debugMode, debugEmail, environmentToCheck);
    }



    @Test(enabled = true)
    public void testCPPAuto_CreatePolicyIssued() throws Exception {

        myMini.testCPPAuto_CreatePolicyIssued_Guts();

    }

    @Test(enabled = true, dependsOnMethods = {"testCPPAuto_CreatePolicyIssued"})
    public void testCPPAuto_VerifyPolicyinBC() throws Exception {

        myMini.testCPPAuto_VerifyPolicyinBC_Wrapper();

    }


    @Test(enabled = true)
    public void testCPPGL_CreatePolicyIssued() throws Exception {

        myMini.testCPPGeneralLiability_CreatePolicyIssed_Guts();

    }

    @Test(enabled = true, dependsOnMethods = {"testCPPAuto_CreatePolicyIssued"})
    public void testCPPGL_VerifyPolicyinBC() throws Exception {

        myMini.testCPPGeneralLiability_VerifyPolicyinBC_Wrapper();

    }


    @Test(enabled = true, dependsOnMethods = {"testBOP_VerifyPolicyinBC", "testSquireAuto_VerifyPolicyinBC", "testCPPAuto_VerifyPolicyinBC"})
    public void successfulTest() {

        new EmailUtils().sendEmail(myMini.getSuccessEmailToList(), myMini.getSuccessEmailSubject(), myMini.getSuccessEmailBody(), null);

    }

}