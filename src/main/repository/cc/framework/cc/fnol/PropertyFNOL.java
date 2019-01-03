package repository.cc.framework.cc.fnol;

import repository.cc.framework.ClaimCenterActions;
import repository.cc.framework.cc.constants.LossCauses;
import repository.cc.framework.cc.constants.LossRouters;
import repository.cc.framework.cc.constants.PhoneNumberTypes;
import repository.cc.framework.utils.helpers.CCIDs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PropertyFNOL extends FNOL {

    private String claimReporterMobilePhoneNumber = "208-555-5555";
    private String claimReporterPrimaryPhoneType = PhoneNumberTypes.MOBILE;
    private String claimReporterEmail = "qawizpro@idfbins.com";
    private String lossDescription = "Default Property FNOL Test";
    private String lossCause = LossCauses.EXPLOSION;
    private String lossRouter = LossRouters.MAJOR_INCIDENT;

    public PropertyFNOL(ClaimCenterActions actions) {
        super(actions);
    }

    private static String getRandomPolicyNumber(ClaimCenterActions actions) {
        // Get an auto policy number from DB for testing
        return actions.db.getRandomPropertyPolicyNumber();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void create() {
        super.build();
        this.actions.storage.store("policyNumber", getRandomPolicyNumber(this.actions));
        searchOrCreatePolicy(this.actions.storage.get("policyNumber").toString());
        fillBasicInformation();
        fillLossDetailInformation();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void searchOrCreatePolicy(String policyNumber) {
        super.actions.findElement(CCIDs.NewClaim.SearchOrCreatePolicy.Elements.POLICY_ROOT_NUMBER).fill(policyNumber.split("-")[1]);
        super.actions.findElement(CCIDs.NewClaim.SearchOrCreatePolicy.Elements.SEARCH_BUTTON).click();
        super.actions.findTable(CCIDs.NewClaim.SearchOrCreatePolicy.Elements.SEARCH_RESULTS).findRowWithText(policyNumber).clickSelect();
        super.actions.findElement(CCIDs.NewClaim.SearchOrCreatePolicy.Elements.DATE_OF_LOSS).fill(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        super.actions.findElement(CCIDs.NewClaim.SearchOrCreatePolicy.Elements.PROPERTY_RADIO).click();
        super.actions.findElement(CCIDs.NewClaim.Elements.FNOL_WIZARD_NEXT).click();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void fillBasicInformation() {
        this.actions.storage.store("insuredName", this.actions.screenGrab(CCIDs.Claim.Elements.INSURED_NAME));
        this.actions.findSelect(CCIDs.NewClaim.BasicInformation.Elements.NAME).select(this.actions.storage.get("insuredName").toString());
        this.actions.findElement(CCIDs.NewClaim.BasicInformation.Elements.MOBILE).fill(this.claimReporterMobilePhoneNumber);
        this.actions.findSelect(CCIDs.NewClaim.BasicInformation.Elements.PRIMARY_PHONE).select(this.claimReporterPrimaryPhoneType);
        this.actions.findElement(CCIDs.NewClaim.BasicInformation.Elements.EMAIL).fill(this.claimReporterEmail);
        this.actions.findTable(CCIDs.NewClaim.BasicInformation.Elements.VEHICLES_INVOLVED).clickRandomCheckbox();
        this.actions.findElement(CCIDs.NewClaim.Elements.FNOL_WIZARD_NEXT).click();

        // Close duplicate claim warning
        if (this.actions.findElement(CCIDs.NewClaim.BasicInformation.Elements.DUPLICATE_CLAIMS) != null) {
            this.actions.findElement(CCIDs.NewClaim.BasicInformation.Elements.CLOSE_BUTTON).click();
            this.actions.findElement(CCIDs.NewClaim.Elements.FNOL_WIZARD_NEXT).click();
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void fillLossDetailInformation() {
        this.actions.findElement(CCIDs.NewClaim.AddClaimInformation.Elements.LOSS_DESCRIPTION).fill(this.lossDescription);
        this.actions.findSelect(CCIDs.NewClaim.AddClaimInformation.Elements.LOSS_CAUSE).select(this.lossCause);
        this.actions.findSelect(CCIDs.NewClaim.AddClaimInformation.Elements.LOSS_ROUTER).select(this.lossRouter);
        this.actions.findSelect(CCIDs.NewClaim.AddClaimInformation.Elements.LOCATION).selectRandom();
        this.actions.findElement(CCIDs.NewClaim.AddClaimInformation.Elements.CITY).fillIfEmpty("Pocatello");
        this.actions.findSelect(CCIDs.NewClaim.AddClaimInformation.Elements.STATE).selectIfNotSet("Idaho");
        this.actions.findElement(CCIDs.NewClaim.Elements.FNOL_WIZARD_FINISH).click();
        this.actions.storage.store("claimNumber", this.actions.screenGrab(CCIDs.NewClaim.NewClaimSaved.Elements.CLAIM_NUMBER).substring(6, 26));
    }

    // Builder
    public PropertyFNOL withClaimReporterMobilePhoneNumber(String phoneNumberWithDashes) {
        this.claimReporterMobilePhoneNumber = phoneNumberWithDashes;
        return this;
    }

    public PropertyFNOL withClaimReporterPrimaryPhoneType(String phoneType) {
        this.claimReporterPrimaryPhoneType = phoneType;
        return this;
    }

    public PropertyFNOL withClaimReporterEmail(String claimReporterEmail) {
        this.claimReporterEmail = claimReporterEmail;
        return this;
    }

    public PropertyFNOL withLossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
        return this;
    }

    public PropertyFNOL withLossCause(String lossCause) {
        this.lossCause = lossCause;
        return this;
    }

    public PropertyFNOL withLossRouter(String lossRouter) {
        this.lossRouter = lossRouter;
        return this;
    }
}
