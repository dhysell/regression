package guidewire.claimcenter.nonEpicFeatures.F524_LargeLossNotification_PersonalLinesAndBOP;

import gwclockhelpers.ApplicationOrCenter;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.element.table.UITable;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.cc.framework.gw.pc.PCOperations;
import repository.cc.framework.gw.pc.pages.PCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.helpers.NumberUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class US3972_CCtoPC_LargeLossNotifications extends BaseOperations {

    @BeforeMethod
    public void setup() throws Exception {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        storage.put("policyNumber", "01-213320-01");
        firstNoticeOfLoss();
        cc.logout();
        exposuresTest();
        reservesTest();
        cc.logout();
        this.getDriver().quit();
    }

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private String claimNumber;

    // FNOL
    private void firstNoticeOfLoss() {
        interact.withElement(CCIDs.NavBar.CLAIM_ARROW).clickTabArrow();
        interact.withElement(CCIDs.NavBar.NEW_CLAIM).click();
        String rootNumber = storage.get("policyNumber").substring(3, 9);
        storage.put("rootNumber", rootNumber);
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.POLICY_ROOT_NUMBER).fill(rootNumber);
        interact.withElement(CCIDs.Claim.SearchOrCreatePolicy.SEARCH).click();

        interact.withTable(CCIDs.Claim.SearchOrCreatePolicy.POLICY_RESULTS).getRowWithText(storage.get("policyNumber")).clickSelectButton();
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.DATE_OF_LOSS).fill(LocalDate.now().format(DateTimeFormatter.ofPattern("MMddyyyy")));

        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.AUTO).click();

        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.NEXT).click();

        String errorMessage = interact.withOptionalElement(CCIDs.ERROR_MESSAGE).screenGrab();
        if (!errorMessage.equalsIgnoreCase("")) {
            Assert.fail(errorMessage);
        }

        String insuredName = interact.withElement(CCIDs.Claim.INSURED_NAME).screenGrab();

        interact.withSelectBox(CCIDs.Claim.BasicInformation.NAME).select(insuredName);
        interact.withSelectBox(CCIDs.Claim.BasicInformation.RELATION_TO_INSURED).select("Self");
        String defaultPhone = "2085555555";
        interact.withTexbox(CCIDs.Claim.BasicInformation.BUSINESS).fillIfEmpty(defaultPhone);
        interact.withTexbox(CCIDs.Claim.BasicInformation.WORK).fillIfEmpty(defaultPhone);
        interact.withTexbox(CCIDs.Claim.BasicInformation.HOME).fillIfEmpty(defaultPhone);
        interact.withTexbox(CCIDs.Claim.BasicInformation.MOBILE).fillIfEmpty(defaultPhone);
        interact.withTexbox(CCIDs.Claim.BasicInformation.FAX).fillIfEmpty(defaultPhone);
        interact.withSelectBox(CCIDs.Claim.BasicInformation.PRIMARY_PHONE).select("Mobile");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        interact.withTexbox(CCIDs.Claim.BasicInformation.EMAIL).fillIfEmpty("qawizpro@idfbins.com");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();

        UITable incidentsInvolved = interact.withTable(CCIDs.Claim.BasicInformation.INCIDENTS_INVOLVED);
        while (!incidentsInvolved.isPresent() && incidentsInvolved.getRows().size() > 0) {
            try {
                this.getDriver().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<UITableRow> incidentRows = incidentsInvolved.getRows();

        boolean selected = false;
        int count = 0;

        while (!selected && count < 30) {
            try {
                interact.withElement(CCIDs.ESCAPE_CLICKER).click();
                incidentRows.get(NumberUtils.generateRandomNumberInt(0, incidentRows.size() - 1)).clickCheckBox();
                selected = true;
            } catch (Exception e) {
                System.out.println("Attempting to select incident.");
            }
            count++;
        }

        interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        if (interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).isPresent()) {
            interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).click();
            interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        }

        String lossDescription = "Auto Test.";
        String lossCause = "Collision and Rollover";
        String lossRouter = "Major Incident";

        interact.withTexbox(CCIDs.Claim.AddClaimInformation.LOSS_DESCRIPTION).fill(lossDescription);
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_CAUSE).select(lossCause);
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_ROUTER).select(lossRouter);
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOCATION).selectRandom();
        interact.withElement(CCIDs.Claim.AddClaimInformation.FINISH).click();

        String claimNumber = interact.withElement(CCIDs.Claim.NewClaimSaved.CLAIM_NUMBER_TEXT).screenGrab().substring(6, 26);
        this.claimNumber = claimNumber;
        storage.put("claimNumber", claimNumber);
    }

    // Exposures
    private void exposuresTest() throws Exception {
        GenerateExposure exposureObj = new GenerateExposure.Builder(this.getDriver())
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Collision")
                .build();
        storage.put("coverageType", exposureObj.getExposureCoverage());
        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    private void reservesTest() {
        cc.createReserve(storage.get("coverageType"), "55000");
        cc.approveActivity("Review and approve reserve change", storage.get("claimNumber"));
    }

    @Test()
    public void checkPCMessages() {
        this.initOn(ApplicationOrCenter.PolicyCenter, Environments.DEV);

        PCOperations pc = new PCOperations(new UIActions(this.getDriver()));
        pc.loginAs("panderson", "gw");
        interact.withElement(PCIDs.SearchTab.SEARCH_TAB).click();
        interact.withTexbox(PCIDs.Search.ACCOUNT_NUMBER).fill("213320");
        interact.withElement(PCIDs.Search.SEARCH_BUTTON).click();
        interact.withTable(PCIDs.Search.ACCOUNTS_TABLE).getRowWithText("213320").getCell(1).clickLink();
        if(!interact.withTable(PCIDs.Policy.ACTIVITIES_TABLE).getRowWithText("Large loss alert on Policy 01-213320-01").isPresent()){
            Assert.fail("Could not find the large loss notification in Policy Center.");
        }
    }
}
