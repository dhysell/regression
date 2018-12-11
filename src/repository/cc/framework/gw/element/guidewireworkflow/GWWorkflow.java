package repository.cc.framework.gw.element.guidewireworkflow;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import repository.cc.framework.gw.element.guidewirebatch.GWBatch;
import repository.cc.framework.gw.element.table.UITableRow;

import java.sql.Connection;

public class GWWorkflow extends GWBatch {

    private UITableRow batchRow;

    public GWWorkflow(WebDriver driver, Connection connection, UITableRow batchRow, String batchName) {
        super(driver, connection, batchRow, batchName);
        this.batchRow = batchRow;
    }

    @Override
    public boolean start() {
        try {
            this.batchRow.clickButtonWithText("Run Writer");
            return super.getStatus().get("StartDate") != null;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to start Batch.");
            return false;
        }
    }

    @Override
    public boolean stop() {
        try {
            this.batchRow.clickButtonWithText("Stop Executor");
            return getStatus().get("CompletedDate") != null;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to stop Batch.");
            return false;
        }
    }

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }
}
