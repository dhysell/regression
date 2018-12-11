package regression.r2.noclock.mini;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.helpers.EmailUtils;
import com.idfbins.driver.BaseTest;
import random.MiniRegression;
public class MiniRegression_QA_BOP extends BaseTest {

    private MiniRegression myMini = null;

    @BeforeClass
    public void beforeClass() {
        boolean debugMode = false;
        String debugEmail = "chofman@idfbins.com";
        String environmentToCheck = "DEV";
        String environmentsToDeployTo = "QA";

        myMini = new MiniRegression(debugMode, debugEmail, environmentToCheck);
    }

    @Test(enabled = true)
    public void testBOP_CreatePolicyIssued() throws Exception {

        myMini.testBOP_CreatePolicyIssued_Guts();

    }

    @Test(enabled = true, dependsOnMethods = {"testBOP_CreatePolicyIssued"})
    public void testBOP_VerifyPolicyinBC() throws Exception {

        myMini.testBOP_VerifyPolicyinBC_Wrapper();

    }

    @Test(enabled = true, dependsOnMethods = {"testBOP_VerifyPolicyinBC"})
    public void successfulTest() {

        new EmailUtils().sendEmail(myMini.getSuccessEmailToList(), myMini.getSuccessEmailSubject(), myMini.getSuccessEmailBody(), null);

    }

}