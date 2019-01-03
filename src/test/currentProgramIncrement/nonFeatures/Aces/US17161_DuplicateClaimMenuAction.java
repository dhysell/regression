package currentProgramIncrement.nonFeatures.Aces;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;

/**
 * @Author Denver Hysell
 * @Requirement US17161 - Checks functionality of the Duplicate Claim page.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/userstory/270144131364">Rally Link</a>
 * @Description : Tests all duplicate claim options then confirms that a split claim was created
 * @DATE 12/03/2018
 */

public class US17161_DuplicateClaimMenuAction extends BaseOperations {

    @BeforeMethod
    public void setupTest() {
        super.randomClaimTestSetup();
        if (interact.withElement(CCIDs.Claim.ADJUSTER).screenGrab().startsWith("Safelite")) {
            setupTest();
        }
    }

    @Test
    public void testRunner() {
        duplicateClaimCoverage();
        duplicateClaimInHouse();
        duplicateClaimSpecialInvestigation();
    }

    @Test(enabled = false)
    public void duplicateClaimCoverage() {

        // Navigate to the duplicate claim menu
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String originalClaimNumber = cc.retrieveClaimNumber();
        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM).click();

        // Create split claim.
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_COVERAGE_SPLIT).click();

        try {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_COVERAGE_SPLIT_NOTES).click();
            System.out.println("Create Split Claim With Notes.");
        } catch(Exception e) {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_COVERAGE_SPLIT_NO_NOTES).click();
            System.out.println("Create Split Claim Without Notes.");
        }

        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String splitClaimNumber = cc.retrieveClaimNumber();

        System.out.println("Original Claim Number: " + originalClaimNumber);
        System.out.println("Split Claim Number:    " + splitClaimNumber);

        Assert.assertTrue(!originalClaimNumber.equalsIgnoreCase(splitClaimNumber), "A new claim was not created.");
    }

    @Test(enabled = false)
    public void duplicateClaimInHouse() {

        // Navigate to the duplicate claim menu
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String originalClaimNumber = cc.retrieveClaimNumber();
        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM).click();

        // Create split claim.
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_INHOUSE).click();

        try {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_INHOUSE_NOTES).click();
            System.out.println("Create Split Claim With Notes.");
        } catch(Exception e) {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_INHOUSE_NO_NOTES).click();
            System.out.println("Create Split Claim Without Notes.");
        }

        interact.withConfirmationWindow().clickOkButton();
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String splitClaimNumber = cc.retrieveClaimNumber();

        System.out.println("Original Claim Number: " + originalClaimNumber);
        System.out.println("Split Claim Number:    " + splitClaimNumber);

        Assert.assertTrue(!originalClaimNumber.equalsIgnoreCase(splitClaimNumber), "A new claim was not created.");
    }

    @Test(enabled = false)
    public void duplicateClaimSpecialInvestigation() {

        // Navigate to the duplicate claim menu
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String originalClaimNumber = cc.retrieveClaimNumber();
        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM).click();

        // Create split claim.
        interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_SPECIAL_INVESTIGATION).click();

        try {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_SPECIAL_INVESTIGATION_NOTES).click();
            System.out.println("Create Split Claim With Notes.");
        } catch(Exception e) {
            interact.withElement(CCIDs.Claim.ActionsMenu.DUPLICATE_CLAIM_SPECIAL_INVESTIGATION_NO_NOTES).click();
            System.out.println("Create Split Claim Without Notes.");
        }

        interact.withConfirmationWindow().clickOkButton();
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        String splitClaimNumber = cc.retrieveClaimNumber();

        System.out.println("Original Claim Number: " + originalClaimNumber);
        System.out.println("Split Claim Number:    " + splitClaimNumber);

        Assert.assertTrue(!originalClaimNumber.equalsIgnoreCase(splitClaimNumber), "Creation of new claim encountered an Error: " + interact.withOptionalElement(CCIDs.ERROR_MESSAGE).screenGrab());
    }
}
