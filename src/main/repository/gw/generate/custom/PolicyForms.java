package repository.gw.generate.custom;

import repository.gw.enums.DocFormEvents;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DocFormUtils;
import repository.pc.workorders.generic.GenericWorkorderForms;

import java.util.ArrayList;

public class PolicyForms {
    public ArrayList<repository.gw.enums.DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreation = new ArrayList<repository.gw.enums.DocFormEvents.PolicyCenter>();
    public ArrayList<repository.gw.enums.DocFormEvents.PolicyCenter> eventsHitDuringIssuanceCreation = new ArrayList<repository.gw.enums.DocFormEvents.PolicyCenter>();
    public ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHit = new ArrayList<String>();
    public ArrayList<String> formsOrDocsExpectedBasedOnIssuanceEventsHit = new ArrayList<String>();
    public ArrayList<String> formsOrDocsActualFromUISubmission = new ArrayList<String>();
    public ArrayList<String> formsOrDocsActualFromUIIssuance = new ArrayList<String>();
    public repository.gw.generate.custom.ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmission = null;
    public ActualExpectedDocumentDifferences actualExpectedDocDifferencesIssuance = null;

    public PolicyForms() {
    }

    public void updateFullAppForms(repository.gw.generate.GeneratePolicy policy) {
        switch (policy.squire.squireEligibility) {
            case City:
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_Liab_CitySquirePolicy);
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_Liab_CitySquirePolicyDeclarations);
                break;
            case Country:
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_Liab_CountrySquirePolicy);
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_Liab_CountrySquirePolicyDeclarations);
                break;
            case CountryIneligibleCustomFarmingCoverage:
                break;
            case CustomAuto:
                break;
            case FarmAndRanch:
                break;
            default:
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_FarmRanchSquirePolicyBooklet);
                this.eventsHitDuringSubmissionCreation.add(repository.gw.enums.DocFormEvents.PolicyCenter.Squire_FarmRanchPolicyDeclarations);
                break;
        }

    }


    public void updateIssuedPolicyforms(GeneratePolicy policy) throws Exception {
        GenericWorkorderForms formsPage = new GenericWorkorderForms(policy.getWebDriver());
        this.formsOrDocsActualFromUIIssuance = formsPage.getFormDescriptionsFromTable();
        this.eventsHitDuringIssuanceCreation.add(DocFormEvents.PolicyCenter.BOP_Job_Issuance);
        this.formsOrDocsExpectedBasedOnIssuanceEventsHit = repository.gw.helpers.DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringIssuanceCreation);
        this.actualExpectedDocDifferencesIssuance = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUIIssuance, this.formsOrDocsExpectedBasedOnIssuanceEventsHit);
    }


}











