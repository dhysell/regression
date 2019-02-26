package regression.r2.noclock.claimcenter.fnol;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.table.UITable;
import repository.cc.framework.gw.element.table.UITableRow;
import repository.cc.framework.init.Environments;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;
import repository.gw.helpers.NumberUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Auto extends BaseOperations {

    @BeforeTest
    public void setup() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        storage.put("policyNumber", "01-195607-01");
    }

    @Test()
    public void firstNoticeOfLoss() {

        interact.withElement(CCIDs.NavBar.CLAIM_ARROW).clickTabArrow();
        interact.withElement(CCIDs.NavBar.NEW_CLAIM).click();
        String rootNumber = storage.get("policyNumber").substring(3, 9);
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
        interact.withTexbox(CCIDs.Claim.BasicInformation.EMAIL).fillIfEmpty("qawizpro@idfbins.com");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();

        interact.withTable(CCIDs.Claim.BasicInformation.INCIDENTS_INVOLVED).clickRandomCheckbox();

        interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        if (interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).isPresent()) {
            interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).click();
            interact.withElement(CCIDs.Claim.BasicInformation.NEXT).click();
        }

        interact.withTexbox(CCIDs.Claim.AddClaimInformation.LOSS_DESCRIPTION).fill("Auto Test.");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_CAUSE).select("Collision and Rollover");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOSS_ROUTER).select("Major Incident");
        interact.withSelectBox(CCIDs.Claim.AddClaimInformation.LOCATION).selectRandom();
        interact.withElement(CCIDs.Claim.AddClaimInformation.FINISH).click();

        String claimNumber = interact.withElement(CCIDs.Claim.NewClaimSaved.CLAIM_NUMBER_TEXT).screenGrab().substring(6, 26);
        storage.put("claimNumber", claimNumber);

    }
}
