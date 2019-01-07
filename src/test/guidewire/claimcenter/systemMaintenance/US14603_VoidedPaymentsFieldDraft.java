package guidewire.claimcenter.systemMaintenance;

import gwclockhelpers.ApplicationOrCenter;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;

import java.util.HashMap;

/**
 * @Author Denver Hysell
 * @Requirement
 * @RequirementsLink &lt;a href="http:// "&gt;Link Text&lt;/a&gt;
 * @Description  Covers US14603_VoidedPaymentsFieldDraft and US14604 Recoded (Transfer) Checks
 * @DATE 06/04/2018
 */


public class US14603_VoidedPaymentsFieldDraft extends BaseOperations {

    @BeforeTest
    public void setupTest() {
        this.initOnWithBatchRuns(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        try {
            cc.loginAs(ClaimsUsers.abatts);
        } catch (Exception e) {
            cc.loginAs(ClaimsUsers.abatts);
        }

        // Ensure Send file to ftp is off
        interact.withElement(CCIDs.NavBar.ADMINISTRATION).click();
        interact.withElement(CCIDs.Administration.SideMenu.UTILITIES).click();
        interact.withElement(CCIDs.Administration.SideMenu.UTILITIES_SCRIPT_PARAMETERS).click();
        interact.withSelectBox(CCIDs.Administration.ScriptParameters.BEGINNING_WITH).select("Beginning with 'S'");
        interact.withTable(CCIDs.Administration.ScriptParameters.SCRIPT_PARAMETERS_TABLE).getRowWithText("SendFileToFTPServer")
                .getCell(0).clickLink();
        interact.withElement(CCIDs.Administration.EditScriptParameter.EDIT).click();
        interact.withElement(CCIDs.Administration.EditScriptParameter.VALUE_NO).click();
        interact.withElement(CCIDs.Administration.EditScriptParameter.UPDATE).click();

        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String randomClaim = interact.withDB.getRandomClaimWithVoidedFieldOrDraftCheck();
                cc.accessClaim(randomClaim);
                isSuccessful = true;
                System.out.println(randomClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying another Claim.");
                count++;
            }
        }
    }

    @Test()
    public void createAndVoidIssuedCheck() {

        // Create a field or draft check
        interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.CHECK).click();
        interact.withElement(CCIDs.Claim.ActionsMenu.FIELD_CHECK_DRAFT).click();
        interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payees.PRIMARY_PAYEE_NAME).select(interact.withElement(CCIDs.Claim.INSURED_NAME).screenGrab());
        interact.withElement(CCIDs.ESCAPE_CLICKER);
        interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payees.TYPE).select("Insured");

        HashMap checkRegistryEntry = interact.withDB.getRandomManualCheckRegistryEntry();
        int checkNumber = Integer.parseInt((String) checkRegistryEntry.get("BegCheckNumber"));
        int endCheckNum = Integer.parseInt((String) checkRegistryEntry.get("EndCheckNumber"));

        if (checkRegistryEntry.get("Name").equals("Draft")) {
            interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payees.CHECK_TYPE).select("Draft");
        } else {
            interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payees.CHECK_TYPE).select("Field Check");
        }

        boolean isCheckValid = false;
        String checkString = String.valueOf(checkNumber);

        do {
            if (checkString.length() < 6) {
                checkString = "0" + checkString;
            }
            interact.withTexbox(CCIDs.Claim.ManualCheckWizard.Payees.CHECK_NUMBER).fill(String.valueOf(checkString));
            interact.withElement(CCIDs.ESCAPE_CLICKER);
            interact.withElement(CCIDs.Claim.ManualCheckWizard.Payees.NEXT).click();

            if (!interact.withOptionalElement(CCIDs.Claim.ManualCheckWizard.Payees.ERROR_MESSAGE).isPresent() ||
                    interact.withOptionalElement(CCIDs.Claim.ManualCheckWizard.Payees.ERROR_MESSAGE).screenGrab().isEmpty() ||
                    interact.withOptionalElement(CCIDs.Claim.ManualCheckWizard.Payees.ERROR_MESSAGE).screenGrab().equalsIgnoreCase("")) {
                isCheckValid = true;
            } else checkNumber++;
        } while (!isCheckValid && checkNumber != endCheckNum);

        interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payments.RERSERVE_LINE).selectRandom();
        interact.withSelectBox(CCIDs.Claim.ManualCheckWizard.Payments.PAYMENT_TYPE).selectRandom();

        interact.withTable(CCIDs.Claim.ManualCheckWizard.Payments.LINE_ITEMS).getRows().get(1).getCell(1).click();
        interact.withTexbox(CCIDs.Claim.ManualCheckWizard.Payments.TYPE).fill("Indemnity");

        interact.withOptionalSelectBox(CCIDs.Claim.ManualCheckWizard.Payments.CATEGORY).selectRandom();



        interact.withTable(CCIDs.Claim.ManualCheckWizard.Payments.LINE_ITEMS).getRows().get(1).getCell(4).click();
        interact.withTexbox(CCIDs.Claim.ManualCheckWizard.Payments.AMOUNT).fill(String.valueOf(NumberUtils.generateRandomNumberInt(5, 200)));
        interact.withElement(CCIDs.Claim.ManualCheckWizard.Payments.NEXT).click();
        interact.withElement(CCIDs.Claim.ManualCheckWizard.Instructions.FINISH).click();

        // Approve check
        interact.withElement(CCIDs.Claim.SideMenu.WORKPLAN).click();
        interact.withSelectBox(CCIDs.Claim.Workplan.ACTIVITIES).select("All open activities");
        System.out.println("Review and approve new payment " + checkRegistryEntry.get("Name") + " - " + checkString);
        interact.withTable(CCIDs.Claim.Workplan.ACTIVITIES_TABLE).getRowWithText("Review and approve new payment " + checkRegistryEntry.get("Name") + " - " + checkString).getCell(7).clickLink();
        interact.withElement(CCIDs.Claim.ApprovalPopup.APPROVE).click();

        // Run batches
        batchServer.getBatch("Financials Escalation");
        batchServer.getBatch("Ledger Files");

        // Navigate to check and mark as void
        interact.withElement(CCIDs.Claim.SideMenu.FINANCIALS).click();
        interact.withElement(CCIDs.Claim.SideMenu.FINANCIALS_CHECKS).click();
        interact.withTable(CCIDs.Claim.Financials.Checks.FINANCIALS_CHECKS).getRowWithText(checkString).getCell(0).clickLink();
        interact.withElement(CCIDs.Claim.CheckDetails.VOID_STOP).click();
        interact.withTexbox(CCIDs.Claim.VoidOrStopCheck.REASON_FOR_VOID_STOP).fill("Test Void Checks");
        interact.withElement(CCIDs.Claim.VoidOrStopCheck.VOID).click();
        interact.withConfirmationWindow().clickOkButton();

        // Confirm pending void
        Assert.assertTrue(interact.withTable(CCIDs.Claim.CheckDetails.PAYMENTS).getRowWithText("Pending void").isPresent(), "Check is not in 'Pending Void' status.");
    }
}
