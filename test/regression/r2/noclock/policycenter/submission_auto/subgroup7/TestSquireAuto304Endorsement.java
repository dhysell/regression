package regression.r2.noclock.policycenter.submission_auto.subgroup7;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePACoverages;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US10740: Changes to 304 endorsement functionality
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20QuoteApplication%20-%20Exclusions%20and%20Conditions.xlsx">Link Text</a>
 * @Description
 * @DATE Mar 23, 2017
 */
@QuarantineClass
public class TestSquireAuto304Endorsement extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private String excludedDriverFirstName = "Excluded";
    private Underwriters uw;

    private WebDriver driver;

    @Test()

    public void testSquireAutoGenerateFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        person.setFirstName(excludedDriverFirstName);
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setComprehensive(true);
        toAdd.setCostNew(10000.00);
        toAdd.setCollision(true);
        toAdd.setAdditionalLivingExpense(true);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "304Endo")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testSquireAutoGenerateFullApp"})
    private void testCheckExcludedDriverUWIssue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObjPL.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(excludedDriverFirstName);
        paDrivers.setExcludedDriverRadio(true);
        paDrivers.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();

        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
        if (uwIssues.isInList("Excluded Driver without 304").equals(UnderwriterIssueType.NONE)) {
            Assert.fail("Expected error Bind Issue : 'Excluded Driver without 304' is not displayed.");
        }

        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquirePACoverages coverages = new GenericWorkorderSquirePACoverages(driver);

        // Exclusions and Conditions
        coverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.addDriverExclusionEndorsement304(excludedDriverFirstName);

        if (paExclusions.getDriverToExcludeRowByName(excludedDriverFirstName) != 1) {
            Assert.fail("Expected : " + excludedDriverFirstName + " is not added to the Excluded Driver - 304 Endorsement");
        }
        quote.clickSaveDraftButton();
    }

    @Test(dependsOnMethods = {"testCheckExcludedDriverUWIssue"})
    private void testAddExistingNewContactExcludedDriver() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquirePACoverages coverages = new GenericWorkorderSquirePACoverages(driver);

        // Exclusions and Conditions
        coverages.clickCoverageExclusionsTab();
        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.clickDriverToExlcudeSearch();

        Contact newExcluded = new Contact("New" + StringsUtils.generateRandomNumberDigits(6), "ExcludedDriver",
                Gender.Female, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"));
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, newExcluded.getFirstName(), newExcluded.getLastName(), null, null,
                null, null, CreateNew.Create_New_Always);

        SubmissionCreateAccount submissionCreateAccount = new SubmissionCreateAccount(driver);

        submissionCreateAccount.setDOB(newExcluded.getDob());
        submissionCreateAccount.setSubmissionCreateAccountBasicsPrimaryAddressAddressLine1(this.myPolicyObjPL.pniContact.getAddress().getLine1());
        submissionCreateAccount.setSubmissionCreateAccountBasicsPrimaryAddressCity(this.myPolicyObjPL.pniContact.getAddress().getCity());

        submissionCreateAccount.sendArbitraryKeys(Keys.TAB);
        submissionCreateAccount.setSubmissionCreateAccountBasicsPrimaryAddressState(this.myPolicyObjPL.pniContact.getAddress().getState().getName());
        submissionCreateAccount.setSubmissionCreateAccountBasicsPrimaryAddressZipCode(this.myPolicyObjPL.pniContact.getAddress().getZip());
        submissionCreateAccount.setSubmissionCreateAccountBasicsPrimaryAddressAddressType(this.myPolicyObjPL.pniContact.getAddress().getType().getValue());
        submissionCreateAccount.clickUpdate();
        if (paExclusions.getDriverToExcludeRowByName(newExcluded.getFirstName()) < 1) {
            Assert.fail("Expected : " + newExcluded.getFirstName()
                    + " is not added to the Excluded Driver - 304 Endorsement");
        }
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuPACoverages();

        coverages.clickCoverageExclusionsTab();
        paExclusions.checkDriverToExcludeContactByName(newExcluded.getFirstName());
        paExclusions.clickDriverToExcludeDriverRemove();

        risk.Quote();

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        polInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuPACoverages();

        coverages.clickCoverageExclusionsTab();
        paExclusions.clickDriverToExlcudeSearch();
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, newExcluded.getFirstName(), newExcluded.getLastName(), null, null,
                null, null, CreateNew.Do_Not_Create_New);

        if (paExclusions.getDriverToExcludeRowByName(newExcluded.getFirstName()) < 1) {
            Assert.fail("Expected : " + newExcluded.getFirstName()
                    + " is not added to the Excluded Driver - 304 Endorsement");
        }
        quote.clickSaveDraftButton();

        risk.Quote();

    }

}
