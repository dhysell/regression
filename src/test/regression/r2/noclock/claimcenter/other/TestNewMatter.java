package regression.r2.noclock.claimcenter.other;

import repository.gw.enums.ClaimsUsers;
import com.idfbins.driver.BaseTest;

/**
 * @Author iclouser
 * @Requirement US3735 Add Exposure To Matter Page
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/10552165958d/detail/userstory/29572156280">
 * Link Rally Story</a>
 * @Description
 * @DATE Oct 22, 2015
 */
public class TestNewMatter extends BaseTest {

    private String matterName = "Clouser Litigation";

    private ClaimsUsers user = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "01-090396-01";

  /*  @Test
    public void fnolTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        this.claimNumber = myFNOLObj.claimNumber;

    }

    @Test(dependsOnMethods = {"fnolTest"})
    public void exposures() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Random")
                .build();

        System.out.println(exposureObj.getClaimNumber());

    }


    @Test(dependsOnMethods = {"exposures"})
    public void testNewMatter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        Matters newMatter = new Matters(driver);
        SummaryOverview sumOverview = new SummaryOverview(driver);

        String exposureName;

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);


        List<SummaryOverviewExposures> exposures = sumOverview.getExposuresList();
        if (exposures.size() == 0) {
            throw new Exception("There didnt' seem to be any exposures on this policy.");
        }
        exposureName = exposures.get(0).getType().getText();
        actionsMenu.clickActionsButton();

        actionsMenu.clickNewMatter();

        newMatter.selectNewMatter();

        newMatter.setName(matterName);

        newMatter.randomOwner();

        newMatter.selectSpecific_Exposure(exposureName);

        newMatter.clickUpdateButton();

        newMatter.validateExposureInMatter(exposureName);

    }

    @Test(dependsOnMethods = {"testNewMatter"})
    public void editMatter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        Matters newMatter = new Matters(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickLitigationLink();


        // Find Matter that was created by the first test by name.

        driver.findElement(By.linkText(matterName)).click();

        newMatter.clickEditMatter();

        newMatter.selectEResolution();

        newMatter.sendFinalSettlementDate("07/09/2015");

        newMatter.clickUpdateEdit();

    }

    @Test(dependsOnMethods = {"editMatter"})
    public void closeMatter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        Matters newMatter = new Matters(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickLitigationLink();


        // Find Matter that was created by the first test by name.

        driver.findElement(By.linkText(matterName)).click();

        newMatter.ClickDetailsClose();

        newMatter.sendNote("Final notes to close the matter");

        newMatter.selectCloseResolution();

        newMatter.clickCloseUpdate();

        newMatter.validateMatterClosed();

    }*/
}
