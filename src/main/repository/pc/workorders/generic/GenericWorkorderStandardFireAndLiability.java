package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.gw.enums.DocFormEvents;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardFireAndLiability extends GenericWorkorder {

    private WebDriver driver;

    public GenericWorkorderStandardFireAndLiability(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutStandardFire(GeneratePolicy policy) throws Exception {
        fillOutStandardFireLocationsAndProperty(policy);
        fillOutPLStandardFirePropertyAndLliabilty_ExclusionsAndConditions(policy);

        repository.pc.workorders.generic.GenericWorkorderPayerAssignment payerassignmentPage = new GenericWorkorderPayerAssignment(driver);
        payerassignmentPage.fillOutPayerAssignmentPage(policy);
    }

    public void fillOutStandardLiability(GeneratePolicy policy) throws Exception {
        fillOutStandardLiabilityLocationsAndProperty(policy);
        fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdLiab(policy);
    }


    private void fillOutStandardFireLocationsAndProperty(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuSquireProperty();
//		for (PolicyLocation location : policy.standardFireAndLiability.getLocationList()) {
        for (PolicyLocation location : policy.standardFire.getLocationList()) {
            policy.squire.summedNumAcres = policy.squire.summedNumAcres + location.getPlNumAcres();
            location.setNumber(plProperty.addNewOrSelectExistingLocationQQ(location));

            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation locs = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
            if (!locs.isPrimaryLocationSet()) {
                locs.setLocationRowToPrimary(1);
            }//END IF

            for (PLPolicyLocationProperty property : location.getPropertyList()) {
                propertyDetail.fillOutPLPropertyQQ(policy.basicSearch, property, policy.squire.propertyAndLiability.section1Deductible, location);
            }//END FOR
        }//END FOR

    }//END fillOutPLLocationsAndPropertyStandardFireLiab


    private void fillOutStandardLiabilityLocationsAndProperty(GeneratePolicy policy) {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation(driver);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuSquireProperty();
//		for (PolicyLocation location : policy.standardFireAndLiability.getLocationList()) {
        for (PolicyLocation location : policy.standardLiability.getLocationList()) {
            policy.squire.summedNumAcres = policy.squire.summedNumAcres + location.getPlNumAcres();
            location.setNumber(plProperty.addNewOrSelectExistingLocationFA(location.getPlLocationType(), location.getAddress(), location.getPlNumAcres(), location.getPlNumResidence()));

            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation locs = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
            if (!locs.isPrimaryLocationSet()) {
                locs.setLocationRowToPrimary(1);
            }//END IF
        }//END FOR

    }//END fillOutPLLocationsAndPropertyStandardFireLiab


    private void fillOutPLStandardFirePropertyAndLliabilty_ExclusionsAndConditions(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages propertyCoverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        propertyCoverages.fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdFire(policy);
    }


    private void fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdLiab(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

//		section2Covs.setGeneralLiabilityLimit(policy.standardFireAndLiability.liabilitySection.getGeneralLiabilityLimit());
        section2Covs.setGeneralLiabilityLimit(policy.standardLiability.liabilitySection.getGeneralLiabilityLimit());
//		if (policy.standardFireAndLiability.liabilitySection.getGeneralLiabilityLimit().getValue().contains("CSL")) {
        if (policy.standardLiability.liabilitySection.getGeneralLiabilityLimit().getValue().contains("CSL")) {
            if (policy.squire.summedNumAcres >= 5) {
                policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdLiab_AddGeneralLiabilityLimitsCSLGreaterThanEqualTo5Acres);
            } else {
                policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdLiab_AddGeneralLiabilityLimitsCSLLessThan5Acres);
            }//END ELSE

        }//END IF
//		section2Covs.setMedicalLimit(policy.standardFireAndLiability.liabilitySection.getMedicalLimit());
        section2Covs.setMedicalLimit(policy.standardLiability.liabilitySection.getMedicalLimit());

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickCoveragesExclusionsAndConditions();

    }//END fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdLiab


}














