package repository.cc.framework.gw.element.guidewirebatch;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import repository.cc.framework.gw.ab.pages.ABIDs;
import repository.cc.framework.gw.bc.pages.BCIDs;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.Identifier;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.guidewireworkflow.GWWorkflow;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.cc.framework.gw.pc.pages.PCIDs;

import java.sql.Connection;

public class GWBatchServer {

    private WebDriver driver;
    private Connection connection;
    private String batchName;
    private ApplicationOrCenter applicationOrCenter;
    private String env;
    private UIActions interact;

    public GWBatchServer(WebDriver driver, Connection connection, ApplicationOrCenter applicationOrCenter, String env) throws Exception {
        this.driver = driver;
        this.connection = connection;
        this.interact = new UIActions(this.driver);
        this.applicationOrCenter = applicationOrCenter;
        this.env = env;

        if (this.env.equalsIgnoreCase("UAT")) {
            this.driver.navigate().to(this.driver.getCurrentUrl().replace("8", "8b"));
        }

        switch (applicationOrCenter) {
            case PolicyCenter:
                interact.withTexbox(PCIDs.Login.USER_NAME).fill("su");
                interact.withTexbox(PCIDs.Login.PASSWORD).fill("gw");
                interact.withElement(PCIDs.Login.LOGIN_BUTTON).click();
                break;
            case BillingCenter:
                interact.withTexbox(BCIDs.Login.USER_NAME).fill("su");
                interact.withTexbox(BCIDs.Login.PASSWORD).fill("gw");
                interact.withElement(BCIDs.Login.LOGIN_BUTTON).click();
                break;
            case ContactManager:
                interact.withTexbox(ABIDs.Login.USER_NAME).fill("su");
                interact.withTexbox(ABIDs.Login.PASSWORD).fill("gw");
                interact.withElement(ABIDs.Login.LOG_IN).click();
                break;
            case ClaimCenter:
                this.interact.withTexbox(CCIDs.Login.USER_NAME).fill("su");
                this.interact.withTexbox(CCIDs.Login.PASSWORD).fill("gw");
                this.interact.withElement(CCIDs.Login.LOG_IN).click();
                break;
            default:
                throw new Exception("Unknown application or center: " + applicationOrCenter.getName());
        }

        this.interact.waitUntilElementVisible(new Identifier(By.id("TabBar:DesktopTab")), 10);
        (new Actions(driver)).sendKeys(Keys.chord(Keys.ALT, Keys.SHIFT, "t")).perform();
    }

    public GWBatch getBatch(String batchName) {
        this.interact.withElement(new Identifier(By.id("ServerTools:MenuLinks:ServerTools_BatchProcessInfo"))).click();
        UITableRow rowWithText = interact.withTable(new Identifier(By.id("BatchProcessInfo:BatchProcessScreen:BatchProcessesLV-body"))).getRowWithText(batchName);
        return new GWBatch(this.driver, this.connection, rowWithText, batchName);
    }

    public GWWorkflow getWorkflow(String batchName) {
        this.interact.withElement(new Identifier(By.id("ServerTools:MenuLinks:ServerTools_WorkQueueInfo"))).click();
        UITableRow rowWithText = interact.withTable(new Identifier(By.id("WorkQueueInfo:WorkQueueInfoScreen:WorkQueueInfoLV-body"))).getRowWithText(batchName);
        return new GWWorkflow(this.driver, this.connection, rowWithText, batchName);
    }

}
