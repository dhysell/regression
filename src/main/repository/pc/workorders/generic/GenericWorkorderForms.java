package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.LineSelection;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderForms extends GenericWorkorder {

	private TableUtils tableUtils;
	 
    public GenericWorkorderForms(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div/a[contains(@id,'RenewalWizard:PostQuoteWizardStepSet:FormsScreen:FormsDV:FormsLV')]")
    private List<WebElement> links_Preview_Obsolete;

    @FindBy(xpath = "//div[contains(@id,'FormsScreen:FormsDV:FormsLV') or contains(@id, ':PolicyFileFormsDV')]")
    private WebElement div_tableWrapper;


    public void clickPolicyChangeNext() {
        super.clickPolicyChangeNext();
    }


    public void setRequiredBOPForms(GeneratePolicy policy) {


    }


    public void updateFormsLists(GeneratePolicy policy) throws Exception {

        policy.policyForms.formsOrDocsActualFromUISubmission = getFormDescriptionsFromTable();

        switch (policy.productType) {
            case Businessowners:
                policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.BOP_Job_Submission);
                for (PolicyLocation location : policy.busOwnLine.locationList) {
                    for (PolicyLocationBuilding building : location.getBuildingList()) {
                        if (building.isSprinklered()) {
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.BOP_AddProtectiveDevices);
                        }
                    }
                }
                break;
            case CPP:
                break;
            case PersonalUmbrella:
                switch (policy.squire.squireEligibility) {
                    case City:
                        break;
                    case Country:
                        break;
                    case CountryIneligibleCustomFarmingCoverage:
                        break;
                    case CustomAuto:
                        break;
                    case FarmAndRanch:
                        break;
                }
                break;
            case Squire:
                break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                if (policy.lineSelection.contains(LineSelection.StandardFirePL)) {
                    policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdFire_Job_Submission);
                }
                break;
            case StandardLiability:
                if (policy.lineSelection.contains(LineSelection.StandardLiabilityPL)) {
                    if (policy.squire.summedNumAcres >= 5) {
                        policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdLiab_Job_SubmissionGreaterThanEqualTo5Acres);
                    } else {
                        policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdLiab_Job_SubmissionLessThan5Acres);
                    }
                }
                break;
        }
        policy.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit = DocFormUtils.getListOfDocumentNamesForListOfEventNames(policy.policyForms.eventsHitDuringSubmissionCreation);
        policy.policyForms.actualExpectedDocDifferencesSubmission = DocFormUtils.compareListsForDifferences(policy.policyForms.formsOrDocsActualFromUISubmission, policy.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);

    }


    public ArrayList<String> getFormDescriptionsFromTable() {

        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(div_tableWrapper, "Description");

        if (tableUtils.hasMultiplePages(div_tableWrapper)) {
        	tableUtils.clickNextPageButton(div_tableWrapper);
            int numPages = tableUtils.getNumberOfTablePages(div_tableWrapper);
            for (int page = 2; page <= numPages; page++) {
                toReturn.addAll(tableUtils.getAllCellTextFromSpecificColumn(div_tableWrapper, "Description"));
                if (page < numPages) {
                	tableUtils.clickNextPageButton(div_tableWrapper);
                }
            }
        }

        return toReturn;
    }


    public void verifyFormExists_Obsolete(String description) {
        if (!div_tableWrapper.getText().contains(description)) {
            Assert.fail("ERROR: " + description + " is NOT Present");
        }
    }


    public void verifyFormDeclarationNotExists_Obsolete(String description) {
        if (div_tableWrapper.getText().contains(description)) {
        	Assert.fail("ERROR: " + description + " is Present");
        }
    }


    public void verifyPreviewButtonExists_Obsolete() {
        if (links_Preview_Obsolete.size() > 0) {
        	Assert.fail("ERROR: Preview is present on Forms Page.");
        }

    }


    public void verifyFormIsMissing_Obsolete(String description) {
        if (div_tableWrapper.getText().contains(description)) {
        	Assert.fail("ERROR: " + description + " is Present");
        }
    }


//    public void clickEditPolicyTransaction() {
//        super.clickEditTransaction();
//    }


    public void setProtectiveDevicesForms(GeneratePolicy policy) {
        int protectiveDevicesFound = 0;
        int protectiveSafeguardsFound = 0;
        for (PolicyLocation loc : policy.busOwnLine.locationList) {
            for (PolicyLocationBuilding bldg : loc.getBuildingList()) {
                if (bldg.isSprinklered()) {
                    protectiveDevicesFound++;
                    protectiveSafeguardsFound++;
                }
            }
        }
        if (protectiveDevicesFound > 0) {
            policy.policyForms.eventsHitDuringIssuanceCreation.add(DocFormEvents.PolicyCenter.BOP_AddProtectiveDevices);
        }
        if (protectiveSafeguardsFound > 0) {
            policy.policyForms.eventsHitDuringIssuanceCreation.add(DocFormEvents.PolicyCenter.BOP_AddProtectiveSafeguards);
        }
    }


}
