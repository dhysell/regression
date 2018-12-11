package regression.r2.noclock.policycenter.renewaltransition.subgroup1;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4246 : Driver Information (marital status, license number and license state) is lost on Policy Change
 * @Description - Generated Squire III Auto Issuance with additional driver and assign him to vehicle in policy change and quote
 * , also added scenario where additional driver was added and assigned him to vehicle in policy change and quoted,
 * also done for Renewal and Rewrite jobs
 * @DATE Nov 29, 2016
 */
@QuarantineClass
public class TestDriverInfoLostOnRenewal extends BaseTest {
    private GeneratePolicy renewalPolicyObj;
    private Underwriters uw;

    private WebDriver driver;

    //Renewal job
    @Test()
    public void testGenerateRenewal() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        renewalPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withPolTermLengthDays(80)
                .withInsFirstLastName("Test", "Renewal")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testGenerateRenewal"})
    public void verifyValidationMessagesForRenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), renewalPolicyObj.squire.getPolicyNumber());
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(renewalPolicyObj);
        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        sideMenu.clickSideMenuHouseholdMembers();

        Contact newMember = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "NewDriver", Gender.Female, DateUtils.convertStringtoDate("01/01/1970", "MM/dd/yyyy"));
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(renewalPolicyObj.basicSearch, newMember.getFirstName(), newMember.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(newMember.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
        householdMember.selectNotNewAddressListingIfNotExist(renewalPolicyObj.pniContact.getAddress());
        householdMember.setNewPolicyMemberAlternateID("Test" + StringsUtils.generateRandomNumberDigits(6));
        householdMember.clickRelatedContactsTab();
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickOK();

        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.clickNext();

        // adding driver
        sideMenu.clickSideMenuPADrivers();
        addExistingDriverWithInfo(newMember.getLastName());
        sideMenu.clickSideMenuPAVehicles();
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            guidewireHelpers.clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            addExistingDriverWithInfo(newMember.getLastName());
            sideMenu.clickSideMenuPAVehicles();
        }
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.setVIN("2C3CCABG8EH323885");
        vehiclePage.setModelYear(2000);
        vehiclePage.setMake("Honda");
        vehiclePage.setModel("Accord");
        vehiclePage.selectCommunity(CommutingMiles.Pleasure1To2Miles);
        
        vehiclePage.selectGaragedAtZip("ID");
        vehiclePage.selectDriverToAssign(newMember);
        vehiclePage.clickOK();

        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();
        sideMenu.clickSideMenuPAModifiers();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//		risk.Quote();
        risk.performRiskAnalysisAndQuote(renewalPolicyObj);
        //        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
//		if(quote.isPreQuoteDisplayed()){
//			quote.clickPreQuoteDetails();
//			//			risk.clickUWIssuesTab();
//			risk.specialApproveAll();
//			risk.approveAll();
//			risk.Quote();	
//			//		}

        ErrorHandling validationResults = new ErrorHandling(driver);
        if (validationResults.checkIfValidationResultsExists()) {
            Assert.fail("Validation results should not exists");
        }

        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

    private void addExistingDriverWithInfo(String name) throws GuidewireNavigationException {
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(name);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(Gender.Female);
        paDrivers.setOccupation("QA");
        paDrivers.setLicenseNumber("AB123456C");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");

        paDrivers.clickOk();
        GenericWorkorder wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();
    }

}
