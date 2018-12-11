package scratchpad.jon;

import java.util.EnumSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ReinstateReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.reinstate.StartReinstate;

public class GenerateCancelations extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    boolean testFailed = false;
    String failureString = "";
    private WebDriver driver;


    @Test
    public void cancelpolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", "164491");


//		List<CancellationSourceReasonExplanation> cancelationList = new ArrayList<CancellationSourceReasonExplanation>(EnumSet.allOf(CancellationSourceReasonExplanation.class));

        for (CancellationSourceReasonExplanation reason : EnumSet.allOf(CancellationSourceReasonExplanation.class)) {


            StartCancellation cancelation = new StartCancellation(driver);
            switch (reason.getReasonValue()) {
                case "NSF":
                case "non-payment":
                    cancelation.cancelPolicy(reason, reason.getExplanationValue() + " - The Fat Cat", null, true, 666);
                    break;
                default:
                    cancelation.cancelPolicy(reason, reason.getExplanationValue() + " - The Fat Cat", null, true);
                    break;
            }


            cancelation.clickWhenClickable(cancelation.find(By.xpath("//div[contains(@id, ':ViewPolicy-inputEl')]")));

            StartReinstate reinstate = new StartReinstate(driver);
            reinstate.reinstatePolicy(ReinstateReason.Other, reason.getReasonValue() + " - REINSTATE CAT");

            cancelation.clickWhenClickable(cancelation.find(By.xpath("//div[contains(@id, ':ViewPolicy-inputEl')]")));

        }
    }


}















