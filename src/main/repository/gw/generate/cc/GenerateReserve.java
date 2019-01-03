package repository.gw.generate.cc;

import org.openqa.selenium.WebDriver;
import repository.cc.claim.LossDetails;
import repository.cc.claim.Reserve;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.login.Login;

import java.util.List;


// TODO this is a WIP as Exposure work needs to be done before we can do that.
public class GenerateReserve extends BasePage {

    public static class Builder {

        private List<repository.gw.generate.cc.ReserveLine> reserveLines = null;
        private String creatorUserName = null;
        private String creatorPassword = null;
        private String claimNumber = null;
        private String coverageType = null;
        private WebDriver driver;


        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public GenerateReserve build() throws Exception {
            return new GenerateReserve(this);
        }

        public Builder withCoverageType(String coverageType) {
            this.coverageType = coverageType;
            return this;
        }

        public Builder withCreatorUserNamePassword(String creatorUserName, String creatorPassword) {
            this.creatorUserName = creatorUserName;
            this.creatorPassword = creatorPassword;
            return this;
        }

        public Builder withClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
            return this;
        }

        public Builder withReserveLines(List<repository.gw.generate.cc.ReserveLine> lines) {
            this.reserveLines = lines;
            return this;
        }

    }

    public String creatorUserName = null;
    public String creatorPassword = null;
    public String claimNumber = null;
    public String coverageType = null;
    public String reserveAmount = null;
    public String reserveComment = null;
    public String costCategory = null;
    public List<ReserveLine> reserveLines = null;
    private WebDriver driver;

    public GenerateReserve(Builder builderStuff) throws Exception {
        super(builderStuff.driver);
        this.driver = builderStuff.driver;

        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.reserveLines = builderStuff.reserveLines;
        this.coverageType = builderStuff.coverageType;
        this.driver = builderStuff.driver;


        new Login(this.driver).login(this.creatorUserName, this.creatorPassword);

        TopMenu topMenu = new TopMenu(this.driver);
        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickExposuresLink();

        int numExposures = reserveLines.size();

        Reserve reserves = new Reserve(this.driver);

        for (int i = 0; i < numExposures; i++) {

            reserves.setReserveLine(reserveLines.get(i), coverageType);
            sideMenu.clickExposuresLink();
        }

        reserves.approveReserves(claimNumber);

        sideMenu.clickLossDetailsLink();

        LossDetails lossDetails = new LossDetails(this.driver);
        lossDetails.addFaultRating();
    }

}
