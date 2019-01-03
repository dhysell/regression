package repository.cc.framework.gw.element.guidewirebatch;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.table.UITableRow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class GWBatch implements IGWBatch {

    private WebDriver driver;
    private UITableRow batchRow;
    private Connection connection;
    private String batchName;
    private UIActions interact;

    public GWBatch(WebDriver driver, Connection connection, UITableRow batchRow, String batchName) {
        this.driver = driver;
        this.batchRow = batchRow;
        this.connection = connection;
        this.interact = new UIActions(this.driver);
        interact.initializeDatabase(connection);
        this.batchName = batchName;
    }

    protected HashMap<String, String> getStatus() {
        String center = this.driver.getCurrentUrl().split("/")[3];
        ResultSet resultSet = this.interact.withDB.executeQuery("select top 1 * from " + center + "_processhistory where ProcessType = (select id from " + center + "tl_batchprocesstype where name = '" + this.batchName + "') order by StartDate DESC");
        try {
            if (resultSet.next()) {
                HashMap<String, String> statusMap = new HashMap<>();
                statusMap.put("OpsPerformed", resultSet.getString(1));
                statusMap.put("StartDate", resultSet.getString(2));
                statusMap.put("FailureReason", resultSet.getString(3));
                statusMap.put("CompleteDate", resultSet.getString(5));
                statusMap.put("FailedOps", resultSet.getString(6));
                statusMap.put("RanToCompletion", resultSet.getString(9));
                statusMap.put("Scheduled", resultSet.getString(12));
                return statusMap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean start() {
        try {
            this.batchRow.clickButtonWithText("Run");
            return getStatus().get("StartDate") != null;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to start Batch.");
            return false;
        }
    }

    @Override
    public boolean stop() {
        try {
            this.batchRow.clickButtonWithText("Stop");
            return getStatus().get("CompletedDate") != null;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to stop Batch.");
            return false;
        }
    }

    @Override
    public boolean isRunning() {
        return getStatus().get("CompleteDate") == null;
    }

    @Override
    public int operationsPerformed() {
        return Integer.valueOf(getStatus().get("OpsPerformed"));
    }

    @Override
    public boolean isScheduled() {
        return Integer.valueOf(getStatus().get("Scheduled")) == 1;
    }

    @Override
    public LocalDateTime lastRunDate() {
        return LocalDateTime.parse(getStatus().get("CompleteDate"));
    }

    /**
     * @return returns the batch runtime in milliseconds.
     */
    @Override
    public long lastRunDuration() {
        HashMap<String, String> status = getStatus();
        return ChronoUnit.MILLIS.between(LocalDateTime.parse(status.get("StartDate")), LocalDateTime.parse(status.get("CompleteDate")));
    }

    @Override
    public String failureReason() {
        return getStatus().get("FailureReason");
    }
}
