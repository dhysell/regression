package regression.r2.noclock.mini;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import random.MiniRegression;
public class MiniRegression_QA2andIT2_BOPSquireAuto extends BaseTest {

    private MiniRegression myMini = null;

    @BeforeClass
    public void beforeClass() {
        boolean debugMode = false;
        String debugEmail = "chofman@idfbins.com";
        String environmentToCheck = "DEV2";
        String environmentsToDeployTo = "QA2 and IT2";

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


    @Test(enabled = true)
    public void testSquireAuto_CreatePolicyIssued() throws Exception {

        myMini.testSquireAuto_CreatePolicyIssued_Guts();

    }

    @Test(enabled = true, dependsOnMethods = {"testSquireAuto_CreatePolicyIssued"})
    public void testSquireAuto_VerifyPolicyinBC() throws Exception {

        myMini.testSquireAuto_VerifyPolicyinBC_Wrapper();

    }

//    @Test(enabled = true, dependsOnMethods = {"testBOP_VerifyPolicyinBC", "testSquireAuto_VerifyPolicyinBC"})
//    public void successfulTest() throws Exception {
//
//        sendEmail(myMini.getSuccessEmailToList(), myMini.getSuccessEmailSubject(), myMini.getSuccessEmailBody(), null);
//
//    }

}