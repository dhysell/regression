package previousProgramIncrement.pi3_090518_111518.nonFeatures.Aces;

import repository.cc.framework.cc.constants.LossCauses;
import repository.cc.framework.init.ClaimCenterBaseTest;
import repository.cc.framework.init.Environments;
import repository.cc.framework.utils.helpers.CCIDs;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.GuidewireHelpers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @Author Denver Hysell
 * @Requirement US16446 - Ensure Roof Type is a required field when certain loss causes are selected
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558471292ud/detail/userstory/254228322580>Rally Link</a>
 * @Description : Creates a new claim with proper loss cause then edits incident to ensure field is required.
 * @DATE 9/27/2018
 */

public class US16446_RoofTypeDamage extends ClaimCenterBaseTest {

    @BeforeMethod
    public void beforeMethod() {
        super.initDriverOn(Environments.DEV);
        super.init();
    }

    @Test
    public void roofTypeDamage() {

        actions.login(ClaimsUsers.abatts);
        actions.propertyFNOL.withLossCause(LossCauses.WEIGHT_OF_SNOW_OR_SLEET_OR_ICE).create();

        // Click link to view new claim
        actions.findElement(CCIDs.NewClaim.NewClaimSaved.Elements.VIEW_NEW_CLAIM).click();

        // Navigate to loss details and edit incident
        actions.navigator.navigateTo(CCIDs.Claim.LossDetails.Navigation.STEPS);
        actions.findElement(CCIDs.Claim.LossDetails.Elements.EDIT_BUTTON).click();
        actions.findTable(CCIDs.Claim.LossDetails.Elements.PROPERTIES_INCIDENTS).clickFirstLink();
        String insuredName = actions.screenGrab(CCIDs.Claim.Elements.INSURED_NAME);
        actions.findSelect(CCIDs.Claim.Incidents.PropertyIncident.Elements.OWNER).select(insuredName);
        actions.findSelect(CCIDs.Claim.Incidents.PropertyIncident.Elements.MOLD_INVOLVED).select("No");
        actions.findElement(CCIDs.Claim.Incidents.PropertyIncident.Elements.OK_BUTTON).click();

        String errorMessage = actions.findElement(CCIDs.Claim.Incidents.PropertyIncident.Elements.ERROR_MESSAGE).getElement().getText();
        if (errorMessage.contains("Roof") && errorMessage.contains("Damage")) {
            actions.findSelect(CCIDs.Claim.Incidents.PropertyIncident.Elements.ROOF_DAMAGE_INVOLVED).select("Yes");
            actions.findElement(CCIDs.Claim.Incidents.PropertyIncident.Elements.OK_BUTTON).click();
        } else {
            Assert.fail("Roof Damage Involved select box may not be set as required.  Please check this field on a Property Incident page.");
        }

        new GuidewireHelpers(actions.getDriver()).logout();
        System.out.println(actions.storage.get("claimNumber"));
    }
}