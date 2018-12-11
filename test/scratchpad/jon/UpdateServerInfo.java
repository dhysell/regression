package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ServerID;
import persistence.globaldatarepo.entities.ServerCenterInfo;

public class UpdateServerInfo extends BaseTest {


    @SuppressWarnings("serial")
    List<String> serverList = new ArrayList<String>() {{
        this.add("QA3");
        this.add("DEV3");
        this.add("IT");
        this.add("DEV");
        this.add("UAT");
        this.add("QA2");
        this.add("IT2");
        this.add("UAT2");
        this.add("DEV2");
    }};

    List<ApplicationOrCenter> centers = new ArrayList<ApplicationOrCenter>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(ApplicationOrCenter.PolicyCenter);
            this.add(ApplicationOrCenter.BillingCenter);
            this.add(ApplicationOrCenter.ContactManager);
            this.add(ApplicationOrCenter.ClaimCenter);
        }
    };

    private WebDriver driver;

    @Test
    public void updateServerInfo() throws Exception {
        for (String environment : serverList) {
            for (ApplicationOrCenter center : centers) {
                if (center.equals(ApplicationOrCenter.ClaimCenter)) {
                    //environments don't have a Claims Center
                    if (environment.contains("QA3") || environment.contains("DEV3")) {
                        continue;
                    }
                }
                Config cf = new Config(center, environment);
                driver = buildDriver(cf);
                new Login(driver).login("su", "gw");

                TopInfo topInfoStuff = new TopInfo(driver);
                topInfoStuff.clickTopInfoBuildInfo();
                String revision = topInfoStuff.getSVNRevision();
                String codeBase = null;
                if (center.equals(ApplicationOrCenter.PolicyCenter)) {
                    codeBase = topInfoStuff.getSVNBranch();
                }

                ServerCenterInfo.updateServerInfo(ServerID.valueOf(environment), center, revision, codeBase);

                revision = null;
                codeBase = null;
                driver.quit();
            }
        }
    }


}
