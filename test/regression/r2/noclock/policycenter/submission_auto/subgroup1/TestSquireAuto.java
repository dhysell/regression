package regression.r2.noclock.policycenter.submission_auto.subgroup1;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePACoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquireAuto extends BaseTest {

    private GeneratePolicy generateQQAuto;


    private WebDriver driver;

    @Test
    public void testSquireAuto_QuickQuote() throws Exception {

        generateQQAuto = generateAuto(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testSquireAuto_QuickQuote"})
    public void testSquireAuto_ExclusionsConditionsTab() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.generateQQAuto.accountNumber);

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPACoverages();

        GenericWorkorderSquirePACoverages coveragePage = new GenericWorkorderSquirePACoverages(driver);
        coveragePage.clickExclusionsAndConditions();

        GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        paExclusions.addAdditionalInsuredEndorsement("AddInsured LLC");
        paExclusions.addSpecialEndorsementForAuto("Cor drives this");

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
    }

    @Test(dependsOnMethods = {"testSquireAuto_ExclusionsConditionsTab"})
    public void testSquireAuto_SelectDriverToNull() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.generateQQAuto.accountNumber);

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        //Adding new person
        Contact person = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "NewPe", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/yyyy"));
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(generateQQAuto.basicSearch, person.getFirstName(), person.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(person.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.selectNotNewAddressListingIfNotExist(generateQQAuto.pniContact.getAddress());
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

        //Adding Driver
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(person.getLastName());
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(person.getGender());
        paDrivers.setOccupation("Software");
        paDrivers.setLicenseNumber("AB123456C");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");

        paDrivers.clickOk();

        //***************************************************************************************//
        //NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
        sideMenu.clickSideMenuPACoverages();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            guidewireHelpers.clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.addExistingDriver(person.getLastName());
            paDrivers.selectMaritalStatus(MaritalStatus.Single);
            paDrivers.selectGender(person.getGender());
            paDrivers.setOccupation("Software");
            paDrivers.setLicenseNumber("AB123456C");
            paDrivers.selectLicenseState(state);
            paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
            paDrivers.selectDriverHaveCurrentInsurance(true);
            paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");

            paDrivers.clickOk();
            sideMenu.clickSideMenuPACoverages();
        }
        //***************************************************************************************//

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
        vehiclePage.selectDriverToAssign(person);
        vehiclePage.selectGaragedAtZip(generateQQAuto.squire.squirePA.getVehicleList().get(0).getGaragedAt().getCity());
        vehiclePage.selectDriverToAssign("none");
        vehiclePage.clickOK();

        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("The vehicle must either have a driver assigned or have unassigned driver checked"))) {
            Assert.fail("Expected page validation : 'The vehicle must either have a driver assigned or have unassigned driver checked' is not displayed.");
        }

//		vehiclePage.setNoDriverAssigned(true);
//		//		vehiclePage.clickOK();
//		//
//        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
//		quote.clickSaveDraftButton();
//		quote.clickQuote();
    }

    private GeneratePolicy generateAuto(GeneratePolicyType type) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        return new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Auto", type.toString())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(type);
    }
}
