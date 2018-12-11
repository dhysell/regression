package regression.r2.noclock.policycenter.submission_auto.subgroup4;

import java.util.ArrayList;

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
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
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
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquireAutoDriverRatedAge extends BaseTest {

    public GeneratePolicy myPolicyObjPL = null;
    public String firstName = "Will" + StringsUtils.generateRandomNumberDigits(6);
    public String lastName = "Angela";

    private WebDriver driver;

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// "
     * rally1.rallydev.com/#/33274298124d/detail/userstory/
     * 51709864286</a>
     * @Description
     * @DATE Mar 15, 2016
     */
    @Test
    public void ratedAgeValidations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // create a policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.QuickQuote);
        Agents agent = myPolicyObjPL.agentInfo;

        // Opening account with agent login
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjPL.accountNumber);

        // Edit Quick Quote Policy Members and Driver Details with agent login
        //Note : Rated age is no longer editable in QQ
        //editHouseHoldAndDriverDOBRatedAge();

        // select Full App
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireAutoFullTo(false);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.updatePLHouseholdMembersFA(myPolicyObjPL);
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReporting(myPolicyObjPL);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(driver);
        drivers.fillOutDriversFA(myPolicyObjPL);

        // Adding new policy member and driver, validating Rated Age is not
        // editable
        addhouseholdMembersDrivers();

        // Save draft and Login with UW
        quote.clickSaveDraftButton();
        new GuidewireHelpers(driver).logout();
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        // validate Rated age is editable in driver screen with UW login
        validatePolicyMemberDriverDetails();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Login with UW and make sure Rated age is editable in
     * driver screen
     * @DATE Mar 15, 2016
     */
    private void validatePolicyMemberDriverDetails() throws Exception {
        boolean testFailed = false;
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);

        for (int currentMember = 1; currentMember <= household.getPolicyHouseholdMembersTableRowCount(); currentMember++) {
            if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains("Will")) {
                household.clickPolicyHouseHoldTableCell(currentMember, "Name");
                break;
            } else
                continue;
        }
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);

        if (householdMember.getReadOnlyRatedAge())
            System.out.println("Expected Rated Age field is readonly");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Rated Age field  is editable";
        }
        householdMember.clickCancel();

        // Driver Screen
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        sideMenu.clickSideMenuPADrivers();
        paDrivers.selectDriverTableSpecificRowByRow(firstName + " " + lastName);
        String updatedRatedAge = paDrivers.getDriverRatedAge();

        // Editing driver details
        paDrivers.clickEditButtonInDriverTableByDriverName(firstName + " " + lastName);
        paDrivers.setDriverRatedAge("" + (Integer.parseInt(updatedRatedAge) + 1));
        paDrivers.clickOk();
        paDrivers.selectDriverTableSpecificRowByRow(firstName + " " + lastName);
        if (paDrivers.getDriverRatedAge().equals("" + (Integer.parseInt(updatedRatedAge) + 1)))
            System.out.println("Expected changed Rated Age : " + Integer.parseInt(updatedRatedAge) + 1 + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected changed Rated Age : " + Integer.parseInt(updatedRatedAge) + 1 + " is not displayed.";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }


    /**
     * @throws GuidewireException
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description: adding new policy member, driver with agent login in full
     * app
     * @DATE Mar 15, 2016
     */
    private void addhouseholdMembersDrivers() throws GuidewireException, Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        boolean testFailed = false;
        String errorMessage = "";

        String newDOB = "02/02/1981";

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, firstName, lastName, null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate(newDOB, "MM/dd/YYYY"));
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());

        householdMember.sendArbitraryKeys(Keys.TAB);

        if (householdMember.getReadOnlyRatedAge())
            System.out.println("Expected Rated Age field is readonly");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Rated Age field  is editable";
        }
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

        // Add driver
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver("Angela");
        paDrivers.selectMaritalStatus(MaritalStatus.Married);
        paDrivers.selectGender(Gender.Female);
        paDrivers.setOccupation("Software");
        paDrivers.setLicenseNumber("AB123456C");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.setAutoDriversDOB(DateUtils.convertStringtoDate(newDOB, "MM/dd/YYYY"));
        if (paDrivers.getDriverRatedAge() != null)
            System.out.println("Expected Rated Age field is readonly");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Rated Age field  is editable";
        }
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.clickOk();

        //***************************************************************************************//
        //NOTE : This is a temp fix for Discard UnSaved error, snippet will be removed once fixed.
        sideMenu.clickSideMenuPACoverages();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            sideMenu.clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            paDrivers.addExistingDriver("Angela");
            paDrivers.selectMaritalStatus(MaritalStatus.Single);
            paDrivers.selectGender(Gender.Female);
            paDrivers.setOccupation("QA");
            paDrivers.setLicenseNumber("AB123456C");
            paDrivers.selectLicenseState(state);
            paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            paDrivers.setPhysicalImpairmentOrEpilepsy(false);
            paDrivers.selectDriverHaveCurrentInsurance(true);
            paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
            paDrivers.clickOk();
            sideMenu.clickSideMenuPACoverages();
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description : Creating quick quote to validate Rated Age field
     * @DATE Mar 15, 2016
     */
    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
        toAdd.setAdditionalLivingExpense(true);
        toAdd.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
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
                .withInsFirstLastName("Test", "Ratedage")
                .build(policyType);

        return myPolicyObjPL;
    }

}
