package repository.gw.generate.cc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.claim.Exposures;
import repository.cc.claim.WorkplanCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

public class GenerateExposure extends BasePage {

    private String creatorUserName = null;
    private String creatorPassword = null;
    private String claimNumber = null;
    private String exposureNumber = null;
    private String incidentType = null;
    private String exposureCoverage = null;
    private boolean isICDtest = false; // Used for special test case, ICD codes.
    private WebDriver driver;

    public static class Builder {

        private String creatorUserName = null;
        private String creatorPassword = null;
        private String claimNumber = null;
        private String exposureCoverage = null;
        private boolean isICDtest = false; // Used for special test case, ICD codes.
        private WebDriver driver;

        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public GenerateExposure build() throws Exception {
            return new GenerateExposure(this);
        }

        public GenerateExposure.Builder withCreatorUserNamePassword(String creatorUserName, String creatorPassword) {
            this.creatorUserName = creatorUserName;
            this.creatorPassword = creatorPassword;
            return this;
        }

        public GenerateExposure.Builder withClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
            return this;
        }

        public GenerateExposure.Builder withCoverageType(String exposureCoverage) {
            this.exposureCoverage = exposureCoverage;
            return this;
        }

        public GenerateExposure.Builder withIsICDtest(boolean isICDtest) {
            this.isICDtest = isICDtest;
            return this;
        }
    }

    public GenerateExposure(GenerateExposure.Builder builderStuff) throws Exception {
        super(builderStuff.driver);
        this.driver = builderStuff.driver;

        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.exposureCoverage = builderStuff.exposureCoverage;
        this.isICDtest = builderStuff.isICDtest;
        repository.gw.login.Login login = new Login(this.driver);
        login.login(this.creatorUserName, this.creatorPassword);

        TopMenu topMenu = new TopMenu(this.driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickWorkplanLink();

        Exposures exposures = new Exposures(this.driver);
        exposures.buildExposure(exposureCoverage, isICDtest);

        WebElement tableEle = find(By.id("ClaimExposures:ClaimExposuresScreen:ExposuresLV"));

        repository.gw.helpers.TableUtils tableUtils = new TableUtils(driver);

        int lastRow = tableUtils.getRowCount(tableEle);

        this.exposureNumber = tableUtils.getCellTextInTableByRowAndColumnName(tableEle, lastRow, "#");
        this.incidentType = tableUtils.getCellTextInTableByRowAndColumnName(tableEle, lastRow, "Incident Type");
        this.exposureCoverage = tableUtils.getCellTextInTableByRowAndColumnName(tableEle, lastRow, "Coverage");

        WorkplanCC workplan = new WorkplanCC(this.driver);
        workplan.clickWorkplanLink();
        workplan.completeFraudQuestionnaireActivity();

    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public String getExposureNumber() {
        return exposureNumber;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getExposureCoverage() {
        return exposureCoverage;
    }
}
