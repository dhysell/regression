package regression.r2.noclock.claimcenter.fullclaim.genliab;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.helpers.NumberUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GeneralLiabilityFullClaim extends BaseOperations {

    @BeforeTest()
    public void setupTests() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
    }

    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";
    private String claimNumber;

    // Check Specific Strings
    private CheckLineItemType paymentType = CheckLineItemType.INDEMNITY;
    private String paymentAmount = Integer.toString(NumberUtils.generateRandomNumberInt(5, 1000));
    private CheckLineItemCategory categoryType = CheckLineItemCategory.INDEMNITY;

    @Test
    public void firstNoticeOfLossTest() throws Exception {

        storage.put("policyNumber", "01-195607-01");

        interact.withElement(CCIDs.NavBar.CLAIM_ARROW).clickTabArrow();
        interact.withElement(CCIDs.NavBar.NEW_CLAIM).click();
        String rootNumber = storage.get("policyNumber").substring(3, 9);
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.POLICY_ROOT_NUMBER).fill(rootNumber);
        interact.withElement(CCIDs.Claim.SearchOrCreatePolicy.SEARCH).click();

        interact.withTable(CCIDs.Claim.SearchOrCreatePolicy.POLICY_RESULTS).getRowWithText(storage.get("policyNumber")).clickSelectButton();
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.DATE_OF_LOSS).fill(LocalDate.now().format(DateTimeFormatter.ofPattern("MMddyyyy")));
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.GENERAL_LIABILITY).click();
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
        interact.withTexbox(CCIDs.Claim.BasicInformation.EMAIL).fillIfEmpty("qawizpro@idfbins.com");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();

        interact.withTable(CCIDs.Claim.BasicInformation.INCIDENTS_INVOLVED).clickRandomCheckbox();

        interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        if (interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).isPresent()) {
            interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).click();
            interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        }

        interact.withTexbox(CCIDs.Claim.AddClaimInformation.LOSS_DESCRIPTION).fill("General Liability Test.");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_CAUSE).select("General Liability (including medical)");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_ROUTER).select("Liability Issue");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOCATION).selectRandom();
        interact.withElement(CCIDs.Claim.AddClaimInformation.FINISH).click();

        storage.put("claimNumber", interact.withElement(CCIDs.Claim.NewClaimSaved.CLAIM_NUMBER_TEXT).screenGrab().substring(6, 26));
        this.claimNumber = interact.withElement(CCIDs.Claim.NewClaimSaved.CLAIM_NUMBER_TEXT).screenGrab().substring(6, 26);

    }

    // Exposures
    @Test(dependsOnMethods = {"firstNoticeOfLossTest"})
    public void exposuresTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = buildDriver(cf);
        GenerateExposure exposureObj = new GenerateExposure.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withCoverageType("Random")
                .build();

        System.out.println(exposureObj.getClaimNumber());
    }

    // Reserves
    @Test(dependsOnMethods = {"exposuresTest"})
    public void reservesTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = buildDriver(cf);
        ArrayList<ReserveLine> lines = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        lines.add(line1);

        GenerateReserve myClaimObj = new GenerateReserve.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withClaimNumber(this.claimNumber)
                .withReserveLines(lines)
                .build();

        System.out.println(myClaimObj.claimNumber);
    }

    // Checks
    @Test(dependsOnMethods = {"reservesTest"})
    public void checksTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        WebDriver driver = buildDriver(cf);
        GenerateCheck myCheck = new GenerateCheck.Builder(driver)
                .withCreatorUserNamePassword(user, this.password)
                .withClaimNumber(this.claimNumber)
                .withPaymentType(paymentType)
                .withCategoryType(categoryType)
                .withPaymentAmount(paymentAmount)
                .build(GenerateCheckType.Regular);

        System.out.println(myCheck.claimNumber);
    }
}
