package repository.pc.workorders.renewal;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.enums.BatchProcess;
import repository.gw.helpers.BatchHelpers;

public class RenewalStartTransitionRenewal extends BasePage {

    public RenewalStartTransitionRenewal(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void startTransitionRenewal() {
    	new BatchHelpers(getDriver()).runBatchProcess(BatchProcess.Transition_Renewal);
        sleep(10); //Might require a 10 second wait after triggering the batch, but I'm not entirely sure.
    }

}
