package scratchpad.denver;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

public class CheckCreator extends BaseOperations {
   /* @BeforeTest
    public void setupTest() {
        this.initOnWithBatchRuns(ApplicationOrCenter.ClaimCenter, Environments.DEV3);
        cc.loginAs(ClaimsUsers.abatts);

        boolean isSuccessful = false;
        int count = 0;
        while (!isSuccessful && count < 20) {
            try {
                String randomClaim = interact.withDB.getRandomOpenVehicleClaim();
                randomClaim = "01033492012015110601";
                cc.accessClaim(randomClaim);
                isSuccessful = true;
                System.out.println(randomClaim);
            } catch (Exception e) {
                isSuccessful = false;
                System.out.println("Setup failed... Trying Another Claim.");
                count++;
            }
        }
    }

    @Test()
    public void createSystemCheck() {

        for (int i = 0; i < 5; i++) {


            // Create a field or draft check
            interact.withElement(CCIDs.Claim.ActionsMenu.ACTIONS_BUTTON).click();
            interact.withElement(CCIDs.Claim.ActionsMenu.CHECK).click();
            interact.withElement(CCIDs.Claim.ActionsMenu.REGULAR_PAYMENT).click();
            interact.withSelectBox(CCIDs.Claim.SystemCheckWizard.NAME).select(interact.withElement(CCIDs.Claim.INSURED_NAME).screenGrab());
            interact.withElement(CCIDs.ESCAPE_CLICKER);
            interact.withSelectBox(CCIDs.Claim.SystemCheckWizard.TYPE).select("Insured");

            interact.withElement(CCIDs.Claim.SystemCheckWizard.NEXT_BUTTON).click();

            interact.withSelectBox(CCIDs.Claim.SystemCheckWizard.RESERVE_LINE).select("(3) 3rd Party Bodily Injury - John Gibson 1955; Indemnity/Auto Bodily Injury");
            interact.withSelectBox(CCIDs.Claim.SystemCheckWizard.PAYMENT_TYPE).select("Partial");

            interact.withTable(CCIDs.Claim.SystemCheckWizard.PAYMENT_LINE_ITEMS).getRows().get(1).getCell(1).click();
            interact.withTexbox(CCIDs.Claim.SystemCheckWizard.LINE_ITEM_TYPE).fill("Indemnity");
            interact.withElement(CCIDs.ESCAPE_CLICKER).click();
            interact.withTable(CCIDs.Claim.SystemCheckWizard.PAYMENT_LINE_ITEMS).getRows().get(1).getCell(2).click();
            interact.withTexbox(CCIDs.Claim.SystemCheckWizard.LINE_ITEM_CATEGORY).fill("Subrogation Payment");
            interact.withTable(CCIDs.Claim.SystemCheckWizard.PAYMENT_LINE_ITEMS).getRows().get(1).getCell(4).click();
            interact.withTexbox(CCIDs.Claim.SystemCheckWizard.AMOUNT).fill(String.valueOf(NumberUtils.generateRandomNumberInt(5, 20)));
            interact.withElement(CCIDs.Claim.SystemCheckWizard.NEXT_BUTTON).click();
            interact.withElement(CCIDs.Claim.SystemCheckWizard.FINISH_BUTTON).click();

            // Approve check
            interact.withElement(CCIDs.Claim.SideMenu.WORKPLAN).click();
            interact.withSelectBox(CCIDs.Claim.Workplan.ACTIVITIES).select("All open activities");
            interact.withTable(CCIDs.Claim.Workplan.ACTIVITIES_TABLE).getRowWithText("Review and approve new payment").getCell(7).clickLink();
            interact.withElement(CCIDs.Claim.ApprovalPopup.APPROVE).click();
        }

        // TODO

        // Run batches
        batchServer.getBatch("Financials Escalation");
        batchServer.getBatch("Ledger Files");
    }*/

    @BeforeMethod
    public void setupTest() {
        this.initOnWithBatchRuns(ApplicationOrCenter.ClaimCenter, Environments.UAT);
        cc.loginAs(ClaimsUsers.abatts);

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

        batchServer.getBatch("Financials Escalation");
        batchServer.getBatch("Ledger Files");

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


    }

}
