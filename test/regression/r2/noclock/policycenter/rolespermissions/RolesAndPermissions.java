/*
 * Jon Larsen 5/15/2015
 * Roles and Permissions Test is to log in as different Users of Different roles and test
 * that they can perform their required tasks
 * Not all task are currently covered as they will be added as time goes on
 */
package regression.r2.noclock.policycenter.rolespermissions;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import repository.gw.enums.Building.BoxType;
import repository.gw.enums.Building.CauseOfLoss;
import repository.gw.enums.Building.ConstructionType;
import repository.gw.enums.Building.FireBurglaryAlarmGrade;
import repository.gw.enums.Building.FireBurglaryResponseType;
import repository.gw.enums.Building.FireBurglaryTypeOfSystem;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.OccupancyInterestType;
import repository.gw.enums.Building.ParkingLotSidewalkCharacteristics;
import repository.gw.enums.Building.PercAreaLeasedToOthers;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.SafetyEquipment;
import repository.gw.enums.Building.SqFtPercOccupied;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.PopUpWindow;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountContactsPC;
import repository.pc.account.AccountSummaryPC;
import repository.pc.account.AccountsSideMenuPC;
import repository.pc.actions.ActionsPC;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.entities.PAs;
import persistence.globaldatarepo.entities.SAs;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.NamesHelper;
import persistence.globaldatarepo.helpers.PAsHelper;
import persistence.globaldatarepo.helpers.SAsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class RolesAndPermissions extends BaseTest {

    private Agents agentInfo = null;
    private Underwriters underwriter = null;
    private CSRs csr = null;
    private SAs sa = null;
    private PAs pa = null;
    private GeneratePolicy myPolicyObj = null;

    private String policyssn = "";
    public String taxID = "";

    private WebDriver driver;

    // generate new policy to test against
    @Test(enabled = true)
    public void generatePolicy() throws Exception {

        // BUSINESS OWNERS LINE
        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
                false, true);
        myLineAddCov.setEmployeeDishonestyCoverage(true);
        myLineAddCov.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
        myLineAddCov.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Annually);
        myLineAddCov.setEmpDisLimit(EmpDishonestyLimit.Dishonest5000);

        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities);
        boLine.setAdditionalCoverageStuff(myLineAddCov);
        // END BUSINESS OWNERS LINE

        // LOCATIONS
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        // END LOCATIONS

        // BUILDINGS
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding myBuilding = new PolicyLocationBuilding();
        myBuilding.setUsageDescription("Pool 1");
        myBuilding.setNamedInsuredOwner(false);
        myBuilding.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
        myBuilding.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
        myBuilding.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
        myBuilding.setClassClassification("storage");
        // myBuilding.setClassCode(59325);

        myBuilding.setBuildingLimit(5000);
        myBuilding.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
        myBuilding.setBuildingCauseOfLoss(CauseOfLoss.Special);
        myBuilding.setBuildingRoofExlusiongEndorsement(false);
        myBuilding.setBuildingWindstormHailLossesToRoofSurfacing(false);

        myBuilding.setBppLimit(5000);
        myBuilding.setBppValuationMethod(ValuationMethod.ReplacementCost);
        myBuilding.setBppCauseOfLoss(CauseOfLoss.Special);

        myBuilding.setExteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);
        myBuilding.setInteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Good);

        ArrayList<ParkingLotSidewalkCharacteristics> parkingLotCharacteristicsList = new ArrayList<ParkingLotSidewalkCharacteristics>();
        parkingLotCharacteristicsList.add(ParkingLotSidewalkCharacteristics.Potholes);
        parkingLotCharacteristicsList.add(ParkingLotSidewalkCharacteristics.RaisedSunkenSurfaces);
        myBuilding.setParkingLotCharacteristicsList(parkingLotCharacteristicsList);

        ArrayList<SafetyEquipment> safetyEquipmentList = new ArrayList<SafetyEquipment>();
        safetyEquipmentList.add(SafetyEquipment.HandRailinThreeOrMoreSteps);
        safetyEquipmentList.add(SafetyEquipment.NonSlipSurfaces);
        myBuilding.setSafetyEquipmentList(safetyEquipmentList);

        myBuilding.setExitsProperlyMarked(true);
        myBuilding.setNumFireExtinguishers(12);
        myBuilding.setExposureToFlammablesChemicals(false);
        myBuilding.setExposureToFlammablesChemicalsDesc("acids");

        myBuilding.setConstructionType(ConstructionType.MasonryNonCombustible);
        myBuilding.setYearBuilt(1990);
        myBuilding.setNumStories(2);
        myBuilding.setNumBasements(1);
        myBuilding.setTotalArea("2564");
        // SET BASEMENT FINISHED SQ FEET
        // SET BASEMENT UNFINISHED SQ FEET
        myBuilding.setSprinklered(true);
        myBuilding.setPhotoYear(2000);
        myBuilding.setCostEstimatorYear(2000);

        myBuilding.setRoofingType(RoofingType.Aluminum);
        myBuilding.setFlatRoof(false);
        myBuilding.setRoofCondition(RoofCondition.NoIssues);
        myBuilding.setYearRoofReplaced(2000);

        myBuilding.setWiringType(WiringType.Romex);
        myBuilding.setBoxType(BoxType.CircuitBreaker);
        myBuilding.setYearLastMajorWiringUpdate(2000);
        myBuilding.setWiringUpdateDesc("Bens Kite");

        myBuilding.setYearLastMajorHeatingUpdate(2000);
        myBuilding.setHeatingUpdateDesc("Camp Fire");

        myBuilding.setYearLastMajorPlumbingUpdate(2000);
        myBuilding.setPlumbingUpdateDesc("Out House");

        myBuilding.setExistingDamage(false);
        myBuilding.setExistingDamageDesc("Hammers");

        myBuilding.setFireBurglaryTypeOfSystem(FireBurglaryTypeOfSystem.FireBurglary);
        myBuilding.setFireBurglaryResponseType(FireBurglaryResponseType.PrivateMonitored);
        myBuilding.setFireBurglaryAlarmGrade(FireBurglaryAlarmGrade.SecurityServiceWithTimingDevice);
        myBuilding.setAlarmCertificate("Omni");

        myBuilding.setInsuredPropertyWithin100Ft(false);
        myBuilding.setInsuredPropertyWithin100FtPolicyHolderName("Bob the Builder");
        myBuilding.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
        // END BUILDING

        locOneBuildingList.add(myBuilding);
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Names accountName = NamesHelper.getRandomName();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName(accountName.getFirstName(), accountName.getLastName())
                .withInsPrimaryAddress(new AddressInfo(true))
                .withPolOrgType(OrganizationType.Individual)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Restaurants))
                .withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Credit_Debit).build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)
                || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
            underwriter = myPolicyObj.underwriterInfo;
        }

        if (myPolicyObj.pniContact.getSocialSecurityNumber() != null || myPolicyObj.pniContact.getSocialSecurityNumber() != "") {
            policyssn = myPolicyObj.pniContact.getSocialSecurityNumber();
        }

        if (myPolicyObj.pniContact.getTaxIDNumber() != null || myPolicyObj.pniContact.getTaxIDNumber() != "") {
            this.taxID = myPolicyObj.pniContact.getTaxIDNumber();
        }

    }

    // Test basic functions of a CSR
    // Will fail until DE2708 is complete
    //jlarsen - test disable for new CSR permissions. Test probably needs rewritten :(
    @Test(enabled = false, dependsOnMethods = {"generatePolicy"})
    public void testCSR() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        if (myPolicyObj != null) {
            this.agentInfo = myPolicyObj.agentInfo;
            csr = CSRsHelper.getCSRByAgency(agentInfo.getAgencyName());
        }

        if (csr == null) {
            csr = CSRsHelper.getRandomCSR();
        }
        new Login(driver).login(this.csr.getCsruserName(), "gw");
        System.out.println(this.csr.getCsruserName());

        // validate Actions New Payment
        ActionsPC actions = new ActionsPC(driver);

        actions.click_Actions();
        // new payment link create a pop-up window
        PopUpWindow PUWAbout = new PopUpWindow(driver, actions.link_NewPayment);
        PUWAbout.closePopUp();
        PUWAbout.returnFocusToOriginalWindow();

        // validate only My Activities, My Renewals, My Other WorkOrders on side
        // menu
        DesktopSideMenuPC desktopSidebar = new DesktopSideMenuPC(driver);
        desktopSidebar.clickMyRenewals();
        desktopSidebar.clickMyOtherWorkOrders();
        desktopSidebar.clickMyActivities();

        // validate only Desktop, Search, Account, Policy tabs available
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        topMenu.clickAccountTab();
        topMenu.clickPolicyTab();
        topMenu.clickDesktopTab();

        // validate cannot create new policy
        try {
            TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
            menuPolicy.clickNewSubmission();
            Assert.fail("CSR: " + this.csr.getCsrfirstName() + " " + this.csr.getCsrlastName() + " has the ability to create a new polcy.");
        } catch (Exception e) {
        }

        // CREATE NEW ACTIVITY "DIGITAL PICTURES"
        // pop-up error when trying to search for an existing policy
        if (myPolicyObj != null) {
            // VERIFY SENSATIVE INFORMATION
            // account summary
            SearchPoliciesPC search = new SearchPoliciesPC(driver);
            search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
            AccountSummaryPC summary = new AccountSummaryPC(driver);
            verifySSN(summary.getSSN());

            // account contacts
            AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
            sidebar.clickContacts();
            AccountContactsPC contact = new AccountContactsPC(driver);
            verifySSN(contact.getSSN());

        }
        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoLogout();
    }

    // Test basic functions of an Agent
    @Test(enabled = true, dependsOnMethods = {"generatePolicy"})
    public void testAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.agentInfo = myPolicyObj.agentInfo;
        new Login(driver).login(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword());

        // VERIFY SENSATIVE
        // ACCOUNT SUMMARY SSN
        SearchAccountsPC searchAccount = new SearchAccountsPC(driver);
        searchAccount.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        verifySSN(summary.getSSN());

        // ACCOUNT CONTACTS SSN
        AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
        sidebar.clickContacts();

        AccountContactsPC contact = new AccountContactsPC(driver);
        verifySSN(contact.getSSN());

        // POLICY SUMMARY SSN
        TopMenuPC pcTop = new TopMenuPC(driver);
        pcTop.clickSearchTab();

        // policy summary
        SearchPoliciesPC searchP = new SearchPoliciesPC(driver);
        searchP.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
        PolicySummary policySummary = new PolicySummary(driver);
        verifySSN(policySummary.getSSN());

        // POLICY CONTACTS SSN

        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoLogout();
    }

    // Test basic functions of a Service Assistant
    @Test(enabled = true, dependsOnMethods = {"generatePolicy"})
    public void testSA() throws Exception {
        Boolean hasAgent = true;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        if (myPolicyObj != null) {
            this.agentInfo = myPolicyObj.agentInfo;
            sa = SAsHelper.getSAInfoByAgent(agentInfo.getAgentFirstName() + " " + agentInfo.getAgentLastName());
        }

        if (sa == null) {
            this.sa = SAsHelper.getRandomSA();
            hasAgent = false;
        }

        new Login(driver).login(sa.getSauserName(), "gw");
        System.out.println(sa.getSauserName());

        // validate only My Activities on side menu
        DesktopSideMenuPC desktopSidebar = new DesktopSideMenuPC(driver);
        desktopSidebar.clickMyActivities();
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        // validate only Desktop, Search, Administration, Account, Policy tabs
        // available
        topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        topMenu.clickAccountTab();
        topMenu.clickPolicyTab();
        topMenu.clickDesktopTab();

        // tests for is the SA is associated to the agent that created the
        // policy
        if (hasAgent == true) {

            // VERIFY SENSATIVE INFORMATION
            // account summary
            SearchAccountsPC search = new SearchAccountsPC(driver);
            search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
            AccountSummaryPC summary = new AccountSummaryPC(driver);
            verifySSN(summary.getSSN());

            // account contacts
            AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
            sidebar.clickContacts();
            AccountContactsPC contact = new AccountContactsPC(driver);
            verifySSN(contact.getSSN());

            // policy summary
            SearchPoliciesPC searchPolicy = new SearchPoliciesPC(driver);
            searchPolicy.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
            PolicySummary policySummary = new PolicySummary(driver);
            verifySSN(policySummary.getSSN());

        }

        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoLogout();
    }

    // Test basic functions of a Production Assistant
    @Test(enabled = true, dependsOnMethods = {"generatePolicy"})
    public void testPA() throws Exception {
        Boolean hasAgent = true;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        if (myPolicyObj != null) {
            agentInfo = myPolicyObj.agentInfo;
            pa = PAsHelper.getPAInfoByAgent(agentInfo.getAgentFirstName() + " " + agentInfo.getAgentLastName());
        }

        if (pa == null) {
            this.pa = PAsHelper.getRandomPA();
            hasAgent = false;
        }

        new Login(driver).login(pa.getPauserName(), "gw");
        System.out.println(pa.getPauserName());

        // validate only My Activities, My Submissions, My Renewals, My Other
        // WorkOrders on side menu
        DesktopSideMenuPC desktopSidebar = new DesktopSideMenuPC(driver);
        desktopSidebar.clickMyRenewals();
        desktopSidebar.clickMySubmissions();
        desktopSidebar.clickMyOtherWorkOrders();
        desktopSidebar.clickMyActivities();

        // validate only Desktop, Search, Account, Policy tabs available
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        topMenu.clickAccountTab();
        topMenu.clickPolicyTab();
        topMenu.clickDesktopTab();

        // Do stuff if PA is associated with the policy agent
        if (hasAgent == true) {

            // VERIFY SENSATIVE INFORMATION
            // account summary
            SearchAccountsPC search = new SearchAccountsPC(driver);
            search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
            AccountSummaryPC summary = new AccountSummaryPC(driver);
            verifySSN(summary.getSSN());

            // account contacts
            AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
            AccountContactsPC contact = new AccountContactsPC(driver);
            verifySSN(contact.getSSN());

            // policy summary
            SearchPoliciesPC searchPolicy = new SearchPoliciesPC(driver);
            searchPolicy.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
            sidebar.clickSummary();

            PolicySummary policySummary = new PolicySummary(driver);
            verifySSN(policySummary.getSSN());
        }

        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoLogout();
    }

    // Test basic functions of an UnderWriter
    @Test(enabled = true, dependsOnMethods = {"generatePolicy"})
    public void testUnderwriter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        if (underwriter == null) {
            this.underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter);
        }
        new Login(driver).login(this.underwriter.getUnderwriterUserName(), this.underwriter.getUnderwriterPassword());

        // VERIFY SENSATIVE INFORMATION
        // account summary
        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        verifySSN(summary.getSSN());

        // account contacts
        AccountsSideMenuPC sidebar = new AccountsSideMenuPC(driver);
        sidebar.clickContacts();
        AccountContactsPC contact = new AccountContactsPC(driver);
        verifySSN(contact.getSSN());

        // policy summary
        SearchPoliciesPC searchPolicy = new SearchPoliciesPC(driver);
        searchPolicy.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
        PolicySummary policySummary = new PolicySummary(driver);
        verifySSN(policySummary.getSSN());

        TopInfo Links = new TopInfo(driver);
        Links.clickTopInfoLogout();

    }

    private void verifySSN(String ssn) {
        Assertion fail = new Assertion();
        fail.assertEquals(ssn, policyssn.substring(0, 3) + "-" + policyssn.substring(3, 5) + "-" + policyssn.substring(5, policyssn.length()));
        System.out.println(ssn);
    }

}
