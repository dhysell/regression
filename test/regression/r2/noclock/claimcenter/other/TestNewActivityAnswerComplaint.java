package regression.r2.noclock.claimcenter.other;

import repository.gw.enums.ClaimsUsers;
import com.idfbins.driver.BaseTest;
public class TestNewActivityAnswerComplaint extends BaseTest {
    private String matterName = "Test Litigation";

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";

    private String claimNumber = "01038503022016091902";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

   /* @Test
    public void newFnolAndExposure() throws Exception {
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

        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Random")
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    @Test(dependsOnMethods = {"newFnolAndExposure"})
    public void createInhouseClaim() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(user.toString(), password);
        ActionsMenu actions = new ActionsMenu(driver);
        TopMenu topMenu = new TopMenu(driver);

        topMenu.goToClaimByClaimNumber(claimNumber);

        String expectedClaimNumber = actions.createInHouseClaimWithOrWithoutNotes(YesOrNo.No);
        boolean inhouseCreated = expectedClaimNumber.equalsIgnoreCase(topMenu.gatherClaimNumber());
        Assert.assertTrue(inhouseCreated, "Something went wrong in creating the in-house claim");
        this.claimNumber = topMenu.gatherClaimNumber();
    }


    *//**
     * @throws Exception
     * @Author iclouser
     * @Requirement New Activity Answer Complaint
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/62126031138">Rally Story</a>
     * @Description In the litigation matter for In House Counsel, when date served is selected create an activity
     * "Answer Complaint" assign to In House Counsel Supervisor- Due 10 calendar days after the Date Served start date.
     * @DATE Sep 23, 2016
     *//*
    @Test(dependsOnMethods = {"createInhouseClaim"})
    public void createMatterAndTestActivity() throws Exception {
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

        newMatter.clickEditMatter();

        String dol = topMenu.gatherDOLString();

        Date expectedDate = newMatter.addDateServedStatusLine(dol);

        SideMenuCC sideMenu = new SideMenuCC(driver);
        WorkplanCC wp = sideMenu.clickWorkplanLink();

        wp.validateActivityHasCorrectDate("Answer Complaint", expectedDate);


    }
*/

}
