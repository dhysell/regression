package scratchpad.denver;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.bc.pages.BCIDs;
import repository.cc.framework.gw.element.guidewirebatch.GWBatch;
import repository.cc.framework.init.Environments;
import repository.gw.enums.BatchProcess;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @Author Denver Hysell
 * @Requirement //TODO :Defect or User Story Number - Short description of requirements
 * @RequirementsLink //TODO <a href="Rally Link"Link> Text</a>
 * @Description : //TODO Description of what the test is doing
 * @DATE 10/1/2018
 */

public class scratchpad extends BaseOperations {
    @BeforeMethod()
    public void setUp() {
        super.initOnWithBatchRuns(ApplicationOrCenter.BillingCenter, Environments.DEV);
    }

    @Test
    public void scratchPad(){
        interact.withTexbox(BCIDs.Login.USER_NAME).fill("skane");
        interact.withTexbox(BCIDs.Login.PASSWORD).fill("gw");
        interact.withElement(BCIDs.Login.LOGIN_BUTTON).click();

        GWBatch batch = batchServer.getBatch(BatchProcess.Invoice.getServerToolsValue());
        batchServer.getWorkflow("Invoice").start();
        batch.start();
        while(batch.isRunning()){
            // do nothing
        }


    }
}