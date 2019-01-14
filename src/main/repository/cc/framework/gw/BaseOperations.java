package repository.cc.framework.gw;

import com.github.javafaker.Faker;
import com.idfbins.driver.BaseTest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import repository.cc.framework.gw.cc.CCOperations;
import repository.cc.framework.gw.cc.constants.CoverageTypes;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.cc.policyPlugin.PolicyPluginListener;
import repository.cc.framework.gw.database.DBConnectionPoint;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.guidewirebatch.GWBatchServer;
import repository.cc.framework.init.Environments;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class BaseOperations extends BaseTest {

    protected UIActions interact;
    protected PolicyPluginListener policyPluginListener;
    protected CCOperations cc;
    protected Faker faker;
    protected GWBatchServer batchServer;
    private WebDriver driver;
    private Connection connection;
    private String env;
    protected HashMap<String, String> storage;

    public void initOn(ApplicationOrCenter applicationOrCenter, String env) {
        try {
            Config cf = new Config(applicationOrCenter, env);
            this.driver = buildDriver(cf);
            this.interact = new UIActions(driver);
            this.env = env;
            this.connection = DriverManager.getConnection(Objects.requireNonNull(DBConnectionPoint.getConnectionTo(applicationOrCenter, env)).getConnectionString());
            this.faker = new Faker(new Locale("en-US"));
            this.interact.initializeDatabase(connection);
            this.cc = new CCOperations(this.interact);
            this.storage = new HashMap<>();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println(sqle.getLocalizedMessage());
            System.out.println(sqle.getErrorCode());
            System.out.println(sqle.getSQLState());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void initOnWithBatchRuns(ApplicationOrCenter applicationOrCenter, String env) {
        initOn(applicationOrCenter, env);
        WebDriver batchDriver = null;
        try {
            Config cf = new Config(applicationOrCenter, env);
            batchDriver = buildDriver(cf);
            batchServer = new GWBatchServer(batchDriver, connection, applicationOrCenter, env);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void randomClosedClaim() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String randomClaim = interact.withDB.getRandomClosedClaim();
                cc.accessClaim(randomClaim);
                isSuccessful = true;
                System.out.println(randomClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Claim.");
                count++;
            }
        }
    }

    public void randomClaimTestSetup() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String randomClaim = interact.withDB.getRandomIncidentClaim();
                cc.accessClaim(randomClaim);
                isSuccessful = true;
                System.out.println(randomClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Claim.");
                count++;
            }
        }
    }

    public void copartTestSetup() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String randomOpenVehicleClaim = interact.withDB.getRandomOpenVehicleClaim();
                cc.accessClaim(randomOpenVehicleClaim);

                // Set Phone number on insured
                interact.withElement(CCIDs.Claim.SideMenu.PARTIES_INVOLVED).click();
                interact.withSelectBox(CCIDs.Claim.Contacts.ROLES_FILTER).select("Insured");
                interact.withElement(CCIDs.Claim.Contacts.EDIT).click();
                interact.withTexbox(CCIDs.Claim.Contacts.WORK_PHONE).fill("2085555555");
                interact.withElement(CCIDs.ESCAPE_CLICKER).click();
                interact.withElement(CCIDs.Claim.Contacts.UPDATE).click();

                cc.accessVehicleIncidentFromExposure(new String[]{CoverageTypes.COLLISION_AND_ROLLOVER, CoverageTypes.LIABILITY_AUTO_PROPERTY_DAMAGE,
                        CoverageTypes.COMPREHENSIVE, CoverageTypes.LIABILITY_AUTO_BODILY_INJURY}, true);
                interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.Details.IS_THIS_VEHICLE_DAMAGED_NO).click();
                interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.Details.DRIVER_NAME).selectRandom();
                interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.Details.RELATION_TO_INSURED).select("Self");
                interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.TOTAL_LOSS_YES).click();
                interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VEHICLE_SALVAGE_TAB).click();
                isSuccessful = true;
                System.out.println(randomOpenVehicleClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Claim.");
                count++;
            }
        }
    }

    @AfterSuite
    public void cleanUp() {
        if (this.driver != null) {
            driver.quit();
        } else {
            System.out.println("Driver not found.");
        }

        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
