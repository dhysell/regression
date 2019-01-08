package guidewire.claimcenter.nonEpicFeatures.F91_CopartIntegreationForSalvageAssignmentInCC;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;

/**
 * @Author Denver Hysell
 * @Requirement US16412 - Ensures that a CoPart assignment can be created, edited, and cancelled.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/userstory/252717937100">Rally Link</a>
 * @Description : Opens an existing claim, chooses the proper  incident, then creates, edits, and cancels a CoPart Assignment.
 * @DATE 10/29/2018
 */

public class US16412_CopartTesting extends BaseOperations {

    @BeforeMethod
    public void setupTest() {
        copartTestSetup();
    }

    @Test(enabled = false)
    public void createCopartAssignmentTest() {
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.OWNER_BUY_BACK_NO).click();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.SALVAGE_ASSIGNMENT).select("ClaimCenter");
        getProQuoteTest();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.PRIMARY_DAMAGE).select("ALL OVER");
        Assert.assertTrue(interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.VISIT_COPART_WEBSITE_BUTTON).isPresent(), "The \"Visit Copart Website\" button is missing.");
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.INSPECT_FOR_REPAIRABLE_TOTAL_LOSS_YES).click();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.TYPE_OF_LOSS).selectRandom();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.RESERVE).select("No");
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.PICKUP_ADDRESS).click();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.CONTACT_PERSON).select(interact.withElement(CCIDs.Claim.INSURED_NAME).screenGrab());
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.PICKUP_LOCATION).selectRandom();

        if (!interact.withOptionalElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.CONTACT_PHONE).isPresent()) {
            interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.CONTACT_PERSON_PICKER).click();
            interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.VIEW_CONTACT_DETAILS).click();
            interact.withElement(CCIDs.Claim.Contact.Basics.EDIT_BUTTON).click();
            interact.withTexbox(CCIDs.Claim.Contact.Basics.MOBILE).fill("5555555555");
            interact.withSelectBox(CCIDs.Claim.Contact.Basics.PRIMARY_PHONE).select("Mobile");
            interact.withElement(CCIDs.Claim.Contact.Basics.OK_BUTTON).click();
            interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.INSPECT_FOR_REPAIRABLE_TOTAL_LOSS_YES).click();
        }

        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.CREATE_COPART_ASSIGNMENT_BUTTON).click();
        interact.waitUntilElementVisible(CCIDs.Claim.Incidents.VehicleIncident.EDIT_BUTTON, 45);
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VEHICLE_SALVAGE_TAB).click();

        try {
            interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.LOT_NUMBER).isPresent();
        } catch (Exception e) {
            Assert.fail("Copart Assignment Failed.");
        }
    }

    @Test(enabled = false)
    public void editCopartAssignmentTest() {
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.EDIT_BUTTON).click();
        String newSelection = "BURN - INTERIOR";
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.PRIMARY_DAMAGE).select(newSelection);
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.UPDATE_COPART_ASSIGNMENT).click();
        interact.waitUntilElementVisible(CCIDs.Claim.Incidents.VehicleIncident.EDIT_BUTTON, 45);
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VEHICLE_SALVAGE_TAB).click();
        String actualSelection = interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.PRIMARY_DAMAGE_ELEMENT).screenGrab();
        Assert.assertTrue(newSelection.equalsIgnoreCase(actualSelection), "Copart Edit does not appear to be successful.");
    }

    @Test(enabled = false)
    public void cancelCopartAssignmentTest() {
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.CANCEL_SERVICE_BUTTON).click();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartCancelAssignment.CANCEL_REASON).selectRandom();
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartCancelAssignment.UPDATE_BUTTON).click();
        if (interact.withOptionalElement(CCIDs.ERROR_MESSAGE).isPresent()) {
            interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartCancelAssignment.CANCEL_REASON).select("Cancelled On Phone");
            interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartCancelAssignment.UPDATE_BUTTON).click();
        }

        Assert.assertTrue(interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.ASSIGNMENT_STATUS).screenGrab().equalsIgnoreCase("Cancelled"),
                "Status Description: " + interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.CopartAssignmentDetails.STATUS_DESCRIPTION).screenGrab());
    }

    @Test(enabled = false)
    public void getProQuoteTest() {
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.PROQUOTE_STYLE).selectRandom();
        interact.withSelectBox(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.PROQUOTE_COPART_PRIMAY_DAMAGE).select("ALL OVER");
        interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.GET_PROQUOTE_BUTTON).click();
        Assert.assertTrue(interact.withElement(CCIDs.Claim.Incidents.VehicleIncident.VehicleSalvage.PROQUOTE_RESPONSE_PROQUOTE).isPresent(), "ProQuote Response is missing.");
    }

    @Test
    public void copartEndToEndTest() {
        createCopartAssignmentTest();
        editCopartAssignmentTest();
        cancelCopartAssignmentTest();
    }
}