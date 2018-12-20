package currentProgramIncrement.nonFeatures.Aces;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;

public class DE8269_CloseActivityValidationRuleChange extends BaseOperations {
    @BeforeMethod
    public void setupTest() {
        super.randomClosedClaim();
    }

    @Test
    public void closeActivityValidationTest() {
        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.REOPEN_CLAIM).click();
        interact.withTexbox(CCIDs.Claim.ReopenClaim.NOTE).fill("Reopen for testing.");
        interact.withSelectBox(CCIDs.Claim.ReopenClaim.REASON).select("New information");
        interact.withElement(CCIDs.Claim.ReopenClaim.REOPEN_CLAIM).click();

        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
/*        interact.withElement(CCIDs.Claim.ActionsMenu.GENERAL).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.REVIEW_BI_EVALUATION).click();*/
        interact.withElement(CCIDs.Claim.ActionsMenu.NEW_MAIL).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.REVIEW_NEW_MAIL).click();
        interact.withElement(CCIDs.Claim.NewActivityInClaim.MANDATORY_YES).click();
        interact.withElement(CCIDs.Claim.NewActivityInClaim.UPDATE).click();
        interact.withElement(CCIDs.Claim.SideMenu.WORKPLAN).click();

        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.CLOSE_CLAIM).click();
        interact.withTexbox(CCIDs.Claim.CloseClaim.NOTES).fill("Test closing claim with mandatory activities.");
        interact.withSelectBox(CCIDs.Claim.CloseClaim.OUTCOME).select("Completed");
        interact.withElement(CCIDs.Claim.CloseClaim.CLOSE_CLAIM).click();

        if (interact.withOptionalElement(CCIDs.Claim.ValidationResults.VALIDATION_RESULTS_MESSAGES).isPresent()) {
            String testString = interact.withOptionalElement(CCIDs.Claim.ValidationResults.VALIDATION_RESULTS_MESSAGES).screenGrab();
            Assert.assertTrue(testString.trim().equalsIgnoreCase("This claim has open activities. To close the claim, you must first complete or skip all open activities. Please complete or skip these activities before closing the claim."));
        } else {
            Assert.fail("Expected Validation Result is not present.");
        }
        System.out.println("");
    }
}
