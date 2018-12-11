package regression.r2.noclock.policycenter.documents.umbrella;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquirePropertyLiabilityExclusionsConditions;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/84420444948">Link Text</a>
 * @Description : Umbrella Forms for all Jobs
 * @DATE Mar 25, 2017
 */
@QuarantineClass
public class TestUmbrellaSubmissionFormInference extends BaseTest {

    private GeneratePolicy squireFullApp;
    private Underwriters uw;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();

    private WebDriver driver;

    @Test()
    public void testCreateSquireFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        SectionIICoverages landingstrip = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        liabilitySection.getSectionIICoverageList().add(landingstrip);


        ArrayList<String> namesDescs = new ArrayList<String>();
        namesDescs.add("test1descAndName");
        namesDescs.add("test2descAndName");
        SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions = new SquirePropertyLiabilityExclusionsConditions() {{
            this.setVendorAsAdditionalInsuredEndorsement_207(true);
            this.setVendorAsAdditionalInsuredEndorsement_207_VendorNameList(namesDescs);
        }};

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;
        myPropertyAndLiability.propLiabExclusionsConditions = propLiabExclusionsConditions;

        Squire mySquire = new Squire(SquireEligibility.random());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Sub", "Umbrella")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateSquireFullApp"})
    public void testCreateUmbrellaPolicyFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);

        eventsHitDuringSubmissionCreationPlusLocal.addAll(this.squireFullApp.policyForms.eventsHitDuringSubmissionCreation);
        formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal.addAll(this.squireFullApp.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);
        formsOrDocsActualFromUISubmissionPlusLocal.addAll(this.squireFullApp.policyForms.formsOrDocsActualFromUISubmission);
        actualExpectedDocDifferencesSubmissionPlusLocal.setInExpectedNotInUserInterface(this.squireFullApp.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface());
        actualExpectedDocDifferencesSubmissionPlusLocal.setInUserInterfaceNotInExpected(this.squireFullApp.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected());

    }

    @Test(dependsOnMethods = {"testCreateUmbrellaPolicyFullApp"})
    public void testUmbrellaChanges() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();


        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setPL_UmbrellaPolicyHagerty(true);

        sideMenu.clickSideMenuSquireUmbrellaCoverages();
        sideMenu.clickSideMenuSquireUmbrellaCoverages();

        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
        covs.clickExclusionsConditions();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);
        excConds.sendArbitraryKeys(Keys.TAB);
        if (squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)) {
            excConds.clickPersonalUmbrellaExcludedDriverEndorsement246(this.squireFullApp.squire.squirePA.getDriversList());
        } else {
            excConds.clickFarmAndRanchUmbrellaExcludedDriverEndorsement224(this.squireFullApp.squire.squirePA.getDriversList());
        }
        excConds.clickUmbrellaLimitationEndorsement270();

        ArrayList<UnderlyingInsuranceUmbrella> underlyingInsurances = new ArrayList<UnderlyingInsuranceUmbrella>();
        UnderlyingInsuranceUmbrella undLyingIns1 = new UnderlyingInsuranceUmbrella();
        undLyingIns1.setCompany("TestCompany1");
        undLyingIns1.setPolicyNumber("1234567");
        undLyingIns1.setLimitOfInsurance("TestLimit1");
        underlyingInsurances.add(undLyingIns1);
        excConds.addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(underlyingInsurances);

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        // TODO: Had to click twice because of error from Qualifications screen... change if changed by users/BAs
        risk.clickClearButton();
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();


        try {
            if (risk.isQuotable()) {
                risk.Quote();
                risk.clickClearButton();
                risk.Quote();
            }
        } catch (Exception e) {
        }

        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_PersonalLinesApplication);


        boolean testfailed = false;
        String errorMessage = "";
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);

        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + "ERROR: Documents for Submission In UserInterface But Not in Expected - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected() + "\n";
        }

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + "ERROR: Documents for Submission In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        sideMenu.clickSideMenuQuote();
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }


    }

}
