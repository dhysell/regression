package repository.gw.generate;

import org.openqa.selenium.WebDriver;
import repository.cc.claim.Exposures;
import repository.cc.claim.Reserve;
import repository.cc.claim.WorkplanCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.GenerateClaimType;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

public class GenerateClaim extends BasePage {

    public class Builder {
        private String creatorUserName = null;
        private String creatorPassword = null;
        private String claimType = null;
        private String claimNumber = null;
        private String typeOfExposure = "Random"; // Chooses a coverage type randomly if not specified in the builder object.
        private Boolean isICDtest = null; // Used for special test case, ICD codes.
        private WebDriver driver;

        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public GenerateClaim build(repository.gw.enums.GenerateClaimType typeToGenerate) throws Exception {
            return new GenerateClaim(typeToGenerate, this);
        }

        public Builder withClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
            return this;
        }

        public Builder withClaimType(String claimType) {
            this.claimType = claimType;
            return this;
        }

        public Builder withCreatorUserNamePassword(String creatorUserName, String creatorPassword) {
            this.creatorUserName = creatorUserName;
            this.creatorPassword = creatorPassword;
            return this;
        }

        public Builder withTypeOfExposure(String exposureType) {
            this.typeOfExposure = exposureType;
            return this;
        }

        public Builder withIsICDtest(boolean isICDtest) {
            this.isICDtest = isICDtest;
            return this;
        }
    }

    public repository.gw.enums.GenerateClaimType currentClaimType = null;
    public String creatorUserName = null;
    public String creatorPassword = null;
    public String rootNumber = null;
    public String claimType = null;
    public String claimNumber = null;
    public String policyType = null;
    public String policyNumber = null;
    public String typeOfExposure = null;
    public boolean isICDtest = false;
    private WebDriver driver;

    public GenerateClaim(GenerateClaimType typeToGenerate, Builder builderDetails) throws Exception {
        super(builderDetails.driver);
        this.driver = builderDetails.driver;
        switch (typeToGenerate) {
            case Exposures:
                exposures(builderDetails);
                break;
            case Reserves:
                reserves(builderDetails);
                break;
            default:
                break;
        }
    }

    private void exposures(Builder builderStuff) throws Exception {

        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.claimType = builderStuff.claimType;
        this.typeOfExposure = builderStuff.typeOfExposure;
        this.isICDtest = builderStuff.isICDtest;
        this.driver = builderStuff.driver;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        login.login(this.creatorUserName, this.creatorPassword);

        TopMenu topMenu = new TopMenu(this.driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickWorkplanLink();

        Exposures exposures = new Exposures(this.driver);
        exposures.buildExposure(typeOfExposure, isICDtest);

        WorkplanCC workplan = new WorkplanCC(this.driver);
        workplan.completeFraudQuestionnaireActivity();

        new GuidewireHelpers(driver).logout();
    }

    private void reserves(Builder builderStuff) throws GuidewireException {

        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.driver = builderStuff.driver;

        repository.gw.login.Login login = new Login(this.driver);
        login.login(this.creatorUserName, this.creatorPassword);

        TopMenu topMenu = new TopMenu(this.driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        Exposures exposures = new Exposures(this.driver);
        int rows = exposures.getNumberOfExposures();

        System.out.println(rows);

        Reserve reserves = new Reserve(this.driver);

        for (int i = 0; i < rows; i++) {
            reserves.setReserveLines();
        }

        reserves.approveReserves(claimNumber);
    }
}
