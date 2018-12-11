package regression.r2.noclock.policycenter.documents.standardinlandmarine;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireIMExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE Mar 20, 2017
 */
public class TestStdIMRenewalFormInference extends BaseTest {
    private GeneratePolicy stdIMPolObj;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void testCreateSIMIssuance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.FarmEquipment);
        imCoverages.add(InlandMarine.PersonalProperty);

        // Farm Equipment
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(scheduledItem1);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip1);
        // PersonalProperty
        PersonalProperty ppropTool = new PersonalProperty();
        ppropTool.setType(PersonalPropertyType.Tools);
        ppropTool.setDeductible(PersonalPropertyDeductible.Ded25);
        PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
        tool.setDescription("Big Tool");
        tool.setLimit(5000);

        ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
        tools.add(tool);

        ppropTool.setScheduledItems(tools);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setTools(ppropTool);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = imCoverages;
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        stdIMPolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Inference", "IMForm")
                .withPolTermLengthDays(79)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testCreateSIMIssuance"})
    public void testStdIMRenewalForms() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdIMPolObj.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(stdIMPolObj);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdIMPolObj.accountNumber);
        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(stdIMPolObj.accountNumber, "003");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        guidewireHelpers.editPolicyTransaction();
        sideMenu.clickSideMenuStandardIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
        farmEquipment.clickEditButton();
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.searchAndAddExistingCompanyAsAdditionalInterest(stdIMPolObj.basicSearch, AdditionalInterestType.LessorPL);

        farmEquipment.addFarmEquipmentScheduledItemExistingAdditionalInterest(additionalInterests.getAdditionalInterestsName(), AdditionalInterestType.LessorPL);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentEndorsementNamedPerils);
        //this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_LeasingEndorsement);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentEndorsementScheduleNamedPerils);

        farmEquipment.addScheduledItemAdditionalInsured("First Guy");
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_FarmEquipmentAdditionalInsuredEndorsement);

        farmEquipmentPage.clickOk();

        //Personal Property
        sideMenu.clickSideMenuStandardIMPersonalProperty();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();
        personalProperty.clickAddAdditionalInsured();
        personalProperty.setNameAdditionalInsured("First Guy");
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_MiscellaneousArticlesEndorsement);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_MiscellaneousArticlesScheduleEndorsement);
        personalProperty.clickOk();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        sideMenu.clickSideMenuIMExclusionsAndConditions();
        GenericWorkorderSquireIMExclusionsConditions exclusionsConditions = new GenericWorkorderSquireIMExclusionsConditions(driver);
        exclusionsConditions.addSpecialEndorsementForIM405("Testing purpose added during Automation");

        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_IM_SpecialEndorsementForIM405);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdIM_StandardInlandMarinePolicyDeclarations);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + stdIMPolObj.accountNumber;
        boolean testfailed = false;
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + " ERROR: Documents for Renewal In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }
        sideMenu.clickSideMenuQuote();
        StartRenewal renewal = new StartRenewal(driver);
        renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);

        guidewireHelpers.logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testStdIMRenewalForms"})
    private void testValidateStdIMRenewalDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdIMPolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Renewal");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Inland Marine", "Farm Equipment Endorsement (Named Perils)", "Farm Equipment Endorsement Schedule (Named Perils)",
                "Miscellaneous Articles Endorsement", "Miscellaneous Articles Schedule Endorsement", "Farm Equipment Additional Insured Endorsement",  /*"Leasing Endorsement",*/ "Standard Inland Marine Policy Declarations",
                "Notice of Policy on Privacy"};

        boolean testFailed = false;
        String errorMessage = "Account Number: " + stdIMPolObj.accountNumber;
        for (String document : documents) {
            boolean documentFound = false;
            for (String desc : descriptions) {
                if (desc.equals(document)) {
                    documentFound = true;
                    break;
                }
            }
            if (!documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected document : '" + document + "' not available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }
}
