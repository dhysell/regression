package regression.r2.noclock.policycenter.rewrite.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CommuteType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4088: PL - Rewrite New Account Activity not sent when rewritten
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 14, 2016
 */
@QuarantineClass
public class TestRewriteNewAccountActivity extends BaseTest {
    private GeneratePolicy stdFirePolicyObj;
    private Agents agentInfo;
    private GeneratePolicy squirePolicyObj;
    private Underwriters uw;
    private WebDriver driver;


    @Test()
    public void testCreateStanrdFireQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(2);
        locationsList.add(propertyLocation);

        stdFirePolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withAgent(agentInfo)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("SFire", "NewAccount")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testCreateStanrdFireQQ"})
    private void testIssueSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        this.agentInfo = AgentsHelper.getRandomAgent();

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Sq", "CancelAccount")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testCancelSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testCancelSquirePolicy"})
    private void testRewriteToAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirePolicyObj.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(squirePolicyObj.accountNumber, stdFirePolicyObj.accountNumber);

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(squirePolicyObj.accountNumber);

        AccountSummaryPC summary = new AccountSummaryPC(driver);
        String expectedActivitySubject = "Rewrite to new account " + stdFirePolicyObj.accountNumber + " from source account " + squirePolicyObj.accountNumber + " for policy #" + squirePolicyObj.squire.getPolicyNumber();

        boolean partialNonPayCancelFound = summary.verifyCurrentActivity(expectedActivitySubject, 120);
        if (!partialNonPayCancelFound) {
            Assert.fail(driver.getCurrentUrl() + squirePolicyObj.accountNumber + "The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
        }
        new GuidewireHelpers(driver).logout();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement : The below test is added as part of Inception date requirement and DE4341: Add contacts to Policy members
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description
     * @DATE Nov 17, 2016
     */

    @Test(dependsOnMethods = {"testRewriteToAccount"})
    private void testInceptionDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirePolicyObj.accountNumber);
        String errorMessage = "";
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        //DE4339 : PL- Rewrite new Account Membership  dues not added automatically
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        if (!policyInfo.getMembershipDuesNameByRowNumber(1).contains(stdFirePolicyObj.pniContact.getLastName())) {
            errorMessage = errorMessage + "Primary Named Insured is not added as Membership Due \n";
        }

        if (!policyInfo.getMembershipDueSelectedByName(stdFirePolicyObj.pniContact.getLastName())) {
            errorMessage = errorMessage + "Primary Named Insured checkbox is selected in Membership Due Table \n";
        }
        //DE4342: Rewrite New Account - enhanced Score not working
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(stdFirePolicyObj);
        sideMenu.clickSideMenuPLInsuranceScore();
        if (creditReport.getEnhancedInsuranceScore() >= 0) {
            System.out.println("Enhanced Score is displayed : " + creditReport.getEnhancedInsuranceScore());
        } else {
            errorMessage = errorMessage + "Enhanced Score is not working on Rewrite New Account \n ";
        }
        sideMenu.clickSideMenuPolicyInfo();

        // Insured is over 55
        addAdditionalNamedInsured(stdFirePolicyObj.basicSearch, "ROBERT", "ADLER", "25110 THE DRIVING LN", "CALDWELL", State.Idaho, "83607", AddressType.Home);

        // set DOB
        Date Dob = DateUtils.convertStringtoDate("3/5/1939", "MM/DD/YYYY");
        Date dob = Dob;

        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.setDOB(DateUtils.dateFormatAsString("MM/dd/YYYY", dob));
        additionalInsured.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        creditReport.clickEditInsuranceReport();
        creditReport.selectCreditReportIndividual("ADLER");
        creditReport.clickOrderReport();
        if (creditReport.getFlag().equals("N") && creditReport.getEnhancedInsuranceScore() == 0) {
            System.out.println("Status M or N and Flag is N - Insured Enhanced Score is 0 and No Hit");
        } else {
            errorMessage = errorMessage + "LexisNexis Can't Score or No hit : " + creditReport.getEnhancedInsuranceScore() + "Flag - " + creditReport.getFlag() + "Status - " + creditReport.getInsuranceStatus();
        }

        //DE4341: Add policy members code
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        for (int currentMember = 1; currentMember <= household.getPolicyHouseholdMembersTableRowCount(); currentMember++) {
            if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains(stdFirePolicyObj.pniContact.getLastName())) {
                household.clickPolicyHouseHoldTableCell(currentMember, "Name");
                break;
            }
        }
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListing(stdFirePolicyObj.pniContact.getAddress().getLine1());
        householdMember.clickOK();
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(stdFirePolicyObj.pniContact.getLastName());
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(Gender.Male);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.setOccupation("Testing purpose");
        paDrivers.setLicenseNumber("ABCD4545");
        paDrivers.sendArbitraryKeys(Keys.TAB);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.clickOk();

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.selectDriverToAssign(stdFirePolicyObj.pniContact.getLastName());
        vehiclePage.clickOK();

        sideMenu.clickSideMenuPADrivers();
        paDrivers.setCheckBoxInDriverTableByDriverName(squirePolicyObj.pniContact.getFirstName());
        paDrivers.clickRemoveButton();

        sideMenu.clickSideMenuHouseholdMembers();
        household = new GenericWorkorderPolicyMembers(driver);
        try {
            household.clickRemoveMember(squirePolicyObj.pniContact.getFirstName());
        } catch (Exception e) {
        }
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);

        if (addressBook.searchAddressBookByPersonDetails(squirePolicyObj.pniContact.getFirstName(), squirePolicyObj.pniContact.getLastName(), null, null, null) != null) {
            addressBook.clickSelectButton();

            householdMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
            householdMember.selectNotNewAddressListingIfNotExist(stdFirePolicyObj.pniContact.getAddress());
            householdMember.clickRelatedContactsTab();
            householdMember.clickOK();
        } else {
            Assert.fail("Not able to add New Contact from Cancelled account ");
        }


        /*
         * DE4826 DoB changes not updating rated age on rewrite new account
         */

        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTable(1);
        String oldRatedAge = paDrivers.getDriverRatedAge();
        Date newDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -22);
        paDrivers.setAutoDriversDOB(newDOB);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        if (paDrivers.getDriverRatedAge().contains(oldRatedAge)) {
            errorMessage = errorMessage + "Expected Rated age is not displayed for new Date of Birth : " + newDOB + ". \n";
        }

        Date anotherDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -45);
        paDrivers.setAutoDriversDOB(anotherDOB);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        if (paDrivers.getDriverRatedAge().contains(oldRatedAge)) {
            errorMessage = errorMessage + "Expected Rated age is not displayed for new Date of Birth : " + anotherDOB + ". \n";
        }

        paDrivers.clickOk();

        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        if (!polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter))) {
            errorMessage = errorMessage + "Expected Inception date is not displayed. \n";
        }

        polInfo.setTransferedFromAnotherPolicy(true);
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.squirePolicyObj.squire.getPolicyNumber());
        Date valExpectedSection1InceptionDate = this.squirePolicyObj.squire.getEffectiveDate();
        Date valSection1VInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
        if (valSection1VInceptionDate.equals(valExpectedSection1InceptionDate)) {
            System.out.println("Expected Policy Inception Date : " + valSection1VInceptionDate + " are displayed.");
        } else {
            errorMessage = errorMessage + "Expected Policy Inception Date : " + valExpectedSection1InceptionDate + " are not displayed.  \n";
        }

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();


        if (errorMessage != "")
            Assert.fail(errorMessage);


    }


    private void addAdditionalNamedInsured(boolean basicSearch, String firstName, String lastName, String address1, String city, State state, String zip, AddressType atype) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
		
		new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, firstName, lastName, address1, city, state, zip, CreateNew.Create_New_Always);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.selectAdditionalInsuredRelationship(RelationshipToInsured.Partner);
        additionalInsured.selectAddressListing("New");
        additionalInsured.setAddressLine1(address1);
        additionalInsured.setContactEditAddressCity(city);
        additionalInsured.setContactEditAddressState(state);
        additionalInsured.setZipCode(zip);
        additionalInsured.selectAddressType(atype);
        additionalInsured.sendArbitraryKeys(Keys.TAB);
        additionalInsured.setSSN(StringsUtils.generateRandomNumberDigits(9));
        additionalInsured.clickRelatedContactsTab();
        additionalInsured.clickContactDetailsTab();
        additionalInsured.clickOK();

        new GuidewireHelpers(driver).duplicateContacts();
        if (namedInsured.isSelectMatchingContactDisplayed()) {
            namedInsured.clickSelectMatchingContact();
            namedInsured.setReasonForContactChange("Testing");
            additionalInsured.clickOK();
        }


        GenericWorkorder wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();

    }
}
