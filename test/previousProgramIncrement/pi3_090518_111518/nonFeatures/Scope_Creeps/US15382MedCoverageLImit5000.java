package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement US15382
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/229067082732">US15382</a>
 * @Description
 * As an agent/PA (and UW), I want the med limit to default to 5,000.
 *
 * Steps to get there:
 * Start a new submission
 * Coverages screen-- see medical limit for section II or III (should default to 5,000)
 * Acceptance criteria:
 * Ensure that on a new submission the medical limit field for section II and/or section III defaults to 5,000
 * Med limit field should match between section II and III
 * Ensure the med limit field on quick quotes defaults to 5,000 also
 * Ensure that on existing policies that don't have 5,000 medical deduct stay as they are
 * @DATE October 9, 2018
 */
public class US15382MedCoverageLImit5000 extends BaseTest {
    private WebDriver driver;
    private  AddressInfo  address;
    private String pniLastName;
    public GeneratePolicy myPolicyObject = null;


    @Test
    public void verifyMedicalLimitOnQuickQuoteSecI() throws Exception {
        createAccountAndSelectLine(repository.gw.enums.ProductLineType.Squire);
        fillEligibility();
        repository.pc.workorders.generic.GenericWorkorderLineSelection lineSelection = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(false);
        lineSelection.checkSquireInlandMarine(false);
        lineSelection.clickNext();
        fillPolicyInfo();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation pLocations = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        PolicyLocation location = new PolicyLocation();
        location.setAddress(address);
        pLocations.addNewOrSelectExistingLocationQQ(location);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
        propertyDetail.fillOutPLPropertyQQ(true, property, repository.gw.enums.Property.SectionIDeductible.OneThousand, location);
        propertyDetail.clickNext();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionIICoverage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(
                driver);

        Assert.assertEquals(sectionIICoverage.getMedicalLimit(), repository.gw.enums.Property.SectionIIMedicalLimit.Limit_5000.getValue() , "SecII  Medical  default Limit is NOT 5000  ");
    }
    @Test
    public void verifyMedicalLimitOnQuickQuoteSecIII() throws Exception {
        createAccountAndSelectLine(repository.gw.enums.ProductLineType.Squire);
        fillEligibility();

        repository.pc.workorders.generic.GenericWorkorderLineSelection lineSelection = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalPropertyLine(false);
         lineSelection.clickNext();
        fillPolicyInfo();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(pniLastName);
        paDrivers.selectMaritalStatus(repository.gw.enums.MaritalStatus.Single);
        paDrivers.selectGender(Gender.Male);
        paDrivers.clickOk();
        paDrivers.clickNext();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages secIII=  new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        Assert.assertEquals(secIII.getMedicalLimit(), repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.FiveK.getValue() , "SecIII  Medical  default Limit is NOT 5000  ");
    }

    private void fillEligibility() {
        repository.pc.workorders.generic.GenericWorkorderSquireEligibility eligibilityPage = new repository.pc.workorders.generic.GenericWorkorderSquireEligibility(driver);
        eligibilityPage.chooseCity();
        eligibilityPage.clickNext();
    }

    private void fillPolicyInfo() {
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo policyInfoPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        policyInfoPage.setPolicyInfoRatingCounty("Bannock");
        policyInfoPage.clickNext();
    }


    public void createAccountAndSelectLine(repository.gw.enums.ProductLineType lineType) throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
         address = new AddressInfo();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());

        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();


        pniLastName = "deafultValue";
        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        AddressInfo addressInfo = new AddressInfo();
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, repository.gw.enums.CreateNew.Create_New_Always,
                repository.gw.enums.ContactSubType.Person, pniLastName, "Mr", null, null, addressInfo.getCity(), addressInfo.getState(),
                addressInfo.getZip());


        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.fillOutPrimaryAddressFields(address);
        createAccountPage.setDOB(DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -35));
        createAccountPage.setSubmissionCreateAccountBasicsSSN(StringsUtils.getValidSSN());
        createAccountPage.clickCreateAccountUpdate();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(repository.gw.enums.QuoteType.QuickQuote, lineType);
    }


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void testPolicyChangeWithAddingAutoLineAndVerifyDefaultMedLimit() throws Exception {


        generatePolicy();
        Underwriters uw= UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        sideMenu.clickSideMenuSquirePropertyCoverages();


        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionIICoverage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(
                driver);

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        sectionIICoverage.setMedicalLimit(repository.gw.enums.Property.SectionIIMedicalLimit.Limit_2000);
        sideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.approveAll();
        risk.clickReleaseLock();
        new GuidewireHelpers(driver).logout();
      new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        pcAccountSummaryPage.clickCurrentActivitiesSubject("Underwriter has reviewed this job");

        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuPayment();
        repository.pc.workorders.generic.GenericWorkorderPayment payment = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
        payment.fillOutPaymentPage(myPolicyObject);
        sideMenu.clickSideMenuForms();
        payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
        repository.pc.workorders.generic.GenericWorkorderComplete completePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        completePage.clickPolicyNumber();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);


        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("Policy Change", currentSystemDate);
        sideMenu.clickSideMenuLineSelection();
        repository.pc.workorders.generic.GenericWorkorderLineSelection lineSelection = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(true);

        sideMenu.clickSideMenuQualification();
        repository.pc.workorders.generic.GenericWorkorderQualification qualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
        qualificationPage.setSquireAutoFullTo(false);
        qualificationPage.setSquireGeneralFullTo(false);
        sideMenu.clickSideMenuPADrivers();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);
        paDrivers.addExistingDriver(myPolicyObject.pniContact.getLastName());
        paDrivers.selectMaritalStatus(repository.gw.enums.MaritalStatus.Single);
        paDrivers.selectGender(Gender.Male);
        paDrivers.setOccupation("Testing");
        paDrivers.setLicenseNumber("XW304757A");
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(repository.gw.enums.CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(false);
        paDrivers.setHowLongWithoutCoverage(repository.gw.enums.HowLongWithoutCoverage.NewDriver);
        paDrivers.clickOrderMVR();
        paDrivers.clickMotorVehicleRecord();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord motorRecord = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        motorRecord.selectMVRIncident(1, repository.gw.enums.SRPIncident.Waived);
        paDrivers.clickOK();
        sideMenu.clickSideMenuPAVehicles();
        repository.pc.workorders.generic.GenericWorkorderVehicles_Details vehicle = new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver);
        vehicle.createVehicleManually();
        vehicle.createGenericVehicle(repository.gw.enums.Vehicle.VehicleTypePL.PrivatePassenger);
        vehicle.selectGaragedAtZip("ID");
        vehicle.selectDriverToAssign(myPolicyObject.pniContact.getLastName());
        vehicle.clickOK();

        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        Assert.assertEquals(coveragePage.getMedicalLimit(), repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwoK.getValue() , "SecIII  Medical  default Limit is NOT 2000  ");
    }

    }
