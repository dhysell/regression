package regression.r2.noclock.claimcenter.fnol;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.generate.cc.GenerateFNOL;
import gwclockhelpers.ApplicationOrCenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ResidentialGlass extends BaseOperations {

    @BeforeTest
    public void setup() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        cc.loginAs(ClaimsUsers.abatts);
        storage.put("policyNumber", "01-288627-01");
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
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.PROPERTY_RESIDENTIAL_GLASS).click();
        interact.withTexbox(CCIDs.Claim.SearchOrCreatePolicy.NEXT).click();

        String errorMessage = interact.withOptionalElement(CCIDs.ERROR_MESSAGE).screenGrab();
        if (!errorMessage.equalsIgnoreCase("")) {
            Assert.fail(errorMessage);
        }

        String insuredName = interact.withElement(CCIDs.Claim.INSURED_NAME).screenGrab();

        interact.withSelectBox(CCIDs.Claim.AutoERSorGlass.NAME).select(insuredName);
        interact.withSelectBox(CCIDs.Claim.AutoERSorGlass.RELATION_TO_INSURED).select("Self");
        interact.withTexbox(CCIDs.Claim.AutoERSorGlass.LOSS_DESCRIPTION).fill("Residential Glass Test.");
        interact.withSelectBox(CCIDs.Claim.AutoERSorGlass.LOSS_CAUSE).select("Breakage of Residence Glass");

        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        interact.withTable(CCIDs.Claim.AutoERSorGlass.INCIDENTS_INVOLVED).clickRandomCheckbox();

        interact.withElement(CCIDs.Claim.AutoERSorGlass.FINISH).click();

        if (interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).isPresent()) {
            interact.withOptionalElement(CCIDs.Claim.BasicInformation.DuplicateClaims.CLOSE).click();
            interact.withElement(CCIDs.Claim.AutoERSorGlass.FINISH).click();
        }

        String claimNumber = interact.withElement(CCIDs.Claim.NewClaimSaved.CLAIM_NUMBER_TEXT).screenGrab().substring(6, 26);
        storage.put("claimNumber", claimNumber);

    }
}
