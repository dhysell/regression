package regression.r2.noclock.policycenter.submission_memdues;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Commodities;
import repository.gw.enums.ContactRelationshipToMember;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.MembershipCurrentMemberDuesStatus;
import repository.gw.enums.MembershipRenewalMemberDuesStatus;
import repository.gw.enums.MembershipType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderMembershipCommodities;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US13283: Federation - UI screens - member
 * @RequirementsLink <a href= "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Membership%20Dues/PC8%20-%20Common%20-%20Membership%20Dues.xlsx"> PC8 - Common- Membership Dues</a>
 * @Description
 * @DATE Feb 14, 2018
 */
@QuarantineClass
public class TestMembershipDuesMemberScreenValidations extends BaseTest {

    private GeneratePolicy memPolObj;

    private WebDriver driver;

//    @Test
//    public void testCreateMembershipDuesFA() throws Exception {
//        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//        driver = buildDriver(cf);
//        memPolObj = new GeneratePolicy.Builder(driver)
//        		.withProductType(ProductLineType.Membership)
//        		.withInsFirstLastName("Membership", "Dues")
//        		.build(GeneratePolicyType.FullApp);
//    }

    @Test//(dependsOnMethods = {"testCreateMembershipDuesFA"})
    public void testValidateMemberBasicTab() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
        memPolObj = new GeneratePolicy.Builder(driver)
        		.withProductType(ProductLineType.Membership)
        		.withInsFirstLastName("Membership", "Dues")
        		.build(GeneratePolicyType.FullApp);
        
        
        new Login(driver).loginAndSearchSubmission(memPolObj.agentInfo.getAgentUserName(), memPolObj.agentInfo.getAgentPassword(), memPolObj.accountNumber);
        String errorMessage = "";
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();
        GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);

        // Ensure that the PNI is automatically added
        // Ensure that Inception date defaults to the effective date of the
        // transaction and is editable
        membershipMembers.clickEditMembershipMember(memPolObj.pniContact);
        if (!membershipMembers.getMembershipInceptionDate().equals(memPolObj.membership.getEffectiveDate())) {
            errorMessage = errorMessage + "Inception Date is not matching with Policy Effective Date \n";
        }

        // Ensure that the county defaults to the designated address of the PNI
        if (!membershipMembers.getMembershipMembersDuesCounty().equals(CountyIdaho.valueOfName(memPolObj.pniContact.getAddress().getCounty()))) {
            errorMessage = errorMessage + "Membership Dues - County is not matching with Policy PNI address \n";
        }

        // Ensure that drop down options for Current Dues Status and Renewal
        // Dues Status are as per requirements
        if (!membershipMembers.getMembershipMembersCurrentStatus().equals(MembershipCurrentMemberDuesStatus.Charged)) {
            errorMessage = errorMessage + "Default Membership Dues - Current Dues status : Charged is not shown \n";
        }

        if (!membershipMembers.getMembershipMembersRenewalStatus().equals(MembershipRenewalMemberDuesStatus.No_Change)) {
            errorMessage = errorMessage + "Default Membership Dues - Renewal Dues status: No Change is not shown \n";
        }

        // Ensure that when membership type is Associate only FB quarterly is
        // checked.
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "Default Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        if (membershipMembers.isGemStateProducerCheckboxChecked()) {
            errorMessage = errorMessage + "Default Membership Dues - Gem State Producer Checkbox is selected \n";
        }

        membershipMembers.clickOK();

        // Ensure that Current Dues Status is displayed in Dues Status Column
        if (!membershipMembers.getMembershipMembersTableCellValue(membershipMembers.getMembershipMembersTableRow(this.memPolObj.pniContact.getFullName()), "Dues Status").equals(MembershipCurrentMemberDuesStatus.Charged.getValue())) {
            errorMessage = errorMessage + "Members Table -  Membership Dues - Current Dues status : Charged is not shown \n";
        }

        if (!membershipMembers.getMembershipMembersTableCellValue(membershipMembers.getMembershipMembersTableRow(this.memPolObj.pniContact.getFullName()), "Account").equals(memPolObj.accountNumber)) {
            errorMessage = errorMessage + "Members Table -  Account Number is not displayed correctly \n";
        }

        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setPolicyInfoMembershipType(MembershipType.Regular);
        sideMenu.clickSideMenuMembershipCommodities();

        GenericWorkorderMembershipCommodities commodities = new GenericWorkorderMembershipCommodities(driver);
        commodities.setAcresPlanted(Commodities.AcresPlanted.Alfalfahay, 5);

        // Ensure that when membership type is regular both FB quarterly and Gem
        // State Producer are checked.
        // Ensure that when membership type is changed from Associate to Regular
        // the Gem State Producer is automatically checked
        sideMenu.clickSideMenuMembershipMembers();
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "Regular -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        if (!membershipMembers.isGemStateProducerCheckboxChecked()) {
            errorMessage = errorMessage + "Regular - Membership Dues - Gem State Producer Checkbox is not selected \n";
        }
        membershipMembers.clickOK();

        // Ensure that when membership type is changed from Regular to Associate
        // the Gem State Producer is automatically unchecked.
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.setPolicyInfoMembershipType(MembershipType.Associate);
        sideMenu.clickSideMenuMembershipMembers();
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "Associate -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        if (membershipMembers.isGemStateProducerCheckboxChecked()) {
            errorMessage = errorMessage + "Associate - Membership Dues - Gem State Producer Checkbox is selected \n";
        }
        membershipMembers.clickOK();

        membershipMembers.removeMembershipMember(memPolObj.pniContact.getLastName());
        // Ensure all required fields for person & company throws validation
        // error when not entered
        Contact newPerson = new Contact();
        newPerson.setFirstName("NewTest" + StringsUtils.generateRandomNumberDigits(6));
        newPerson.setLastName("Person" + StringsUtils.generateRandomNumberDigits(8));
        membershipMembers.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(memPolObj.basicSearch, newPerson.getFirstName(), newPerson.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        membershipMembers.clickOK();

        String val1 = "Invalid Address, please check";
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val1)) {
            errorMessage = errorMessage + "Address Mandatory validation is not displayed. \n";
        }
        AddressInfo newAddress = new AddressInfo(false);
        newAddress.setType(AddressType.Home);
        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.clickOK();

        String val2 = "Date of Birth : Missing required field";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val2)) {
            errorMessage = errorMessage + "DOB Mandatory validation is not displayed. \n";
        }

        String val3 = "Gender : Missing required field";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val3)) {
            errorMessage = errorMessage + "Gender Mandatory validation is not displayed. \n";
        }

        String val4 = "County : Missing required field";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val4)) {
            errorMessage = errorMessage + "County Mandatory validation is not displayed. \n";
        }

        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Bannock);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.clickOK();
        
        membershipMembers.removeMembershipMember(newPerson.getLastName());

        Contact newComp = new Contact();
        newComp.setCompanyName("NewComp" + StringsUtils.generateRandomNumberDigits(9));
        membershipMembers.clickSearch();
        addressBook.searchAddressBookByCompanyName(memPolObj.basicSearch, newComp.getCompanyName(), newAddress, CreateNew.Create_New_Always);
        membershipMembers.clickOK();
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val1)) {
            errorMessage = errorMessage + "Company - Address Mandatory validation is not displayed. \n";
        }

        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.clickOK();
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val4)) {
            errorMessage = errorMessage + "Company - County Mandatory validation is not displayed. \n";
        }

        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Bannock);
        membershipMembers.clickOK();

        membershipMembers.clickGenericWorkorderSaveDraft();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

        guidewireHelpers.logout();
    }

    @Test//(dependsOnMethods = {"testValidateMemberBasicTab"})
    public void testValidateRelatedToContactsTab() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        memPolObj = new GeneratePolicy.Builder(driver)
        		.withProductType(ProductLineType.Membership)
        		.withInsFirstLastName("Membership", "Dues")
        		.build(GeneratePolicyType.FullApp);
        new Login(driver).loginAndSearchSubmission(memPolObj.agentInfo.getAgentUserName(), memPolObj.agentInfo.getAgentPassword(), memPolObj.accountNumber);
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();
        GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        membershipMembers.clickMemberRelatedContactsTab();
        membershipMembers.clickAdd();

        membershipMembers.clickBasicMembershipRecordTab();
        membershipMembers.clickMemberRelatedContactsTab();

        // Able to remove the membership related contacts
        membershipMembers.removeMembershipRelatedContactsByRow(1);

        // Validating able to add new related contacts
        String firstName = "NewTest" + StringsUtils.generateRandomNumberDigits(6);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        ContactRelationshipToMember contactRelation = (guidewireHelpers.getRandBoolean()) ? ContactRelationshipToMember.Spouse : ((guidewireHelpers.getRandBoolean()) ? ContactRelationshipToMember.Parent_Guardian : ContactRelationshipToMember.Child_Ward);
        Contact relatedContact = new Contact(firstName, "Added" + StringsUtils.generateRandomNumberDigits(6));
        relatedContact.setContactRelationshipToPNI(contactRelation);

        membershipMembers.addMembershipDuesRelatedContacts(memPolObj.basicSearch, relatedContact);
        membershipMembers.clickOK();
        membershipMembers.clickGenericWorkorderSaveDraft();
        membershipMembers.clickMemberRelatedContactsTab();
        membershipMembers.clickMembershipMemberRowByName(this.memPolObj.pniContact.getFullName());

        if (!membershipMembers.getRelatedContactsRelationshipByName(firstName).equals(contactRelation)) {
            errorMessage = errorMessage + "Entered Related Contacts - Relationship is not displayed";
        }
        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

        guidewireHelpers.logout();
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement : US13848: UI screen Members - paid dues tab
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Membership%20Dues/PC8%20-%20Common%20-%20Membership%20Dues.xlsx">PC8 - Common- Membership Dues</a>
     * @Description
     * @DATE Feb 15, 2018
     */
    @Test//(dependsOnMethods = {"testValidateRelatedToContactsTab"})
    private void testValidatePaidDuesTab() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        memPolObj = new GeneratePolicy.Builder(driver)
        		.withProductType(ProductLineType.Membership)
        		.withInsFirstLastName("Membership", "Dues")
        		.build(GeneratePolicyType.FullApp);
        new Login(driver).loginAndSearchSubmission(memPolObj.agentInfo.getAgentUserName(), memPolObj.agentInfo.getAgentPassword(), memPolObj.accountNumber);
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();

        GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);

        // Add Dues History button will add an addition row, and year and county column fields are editable and required fields
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.addMembershipDuesHistory(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), CountyIdaho.Bannock);
        membershipMembers.clickOK();
        // The check box to remove records will only be available on manually added records. When the check box is checked, the "Remove Dues History" button becomes available.
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.removeDuesHistoryByYear(DateUtils.dateFormatAsString("yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        membershipMembers.clickOK();
        // required fields
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.clickAddDuesHistory();
        membershipMembers.clickOK();
        String val1 = "Year : Missing required field";
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val1)) {
            errorMessage = errorMessage + "Year Mandatory validation is not displayed. \n";
        }

        String val2 = "County : Missing required field";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val2)) {
            errorMessage = errorMessage + "County Mandatory validation is not displayed. \n";
        }
        membershipMembers.removeDuesHistoryByRow(membershipMembers.getMembershipDuesHistoryRows());
        membershipMembers.clickOK();
        
        membershipMembers.removeMembershipMember(memPolObj.pniContact.getLastName());

        // Checking 10years record hard coding the contact name
        // Clicking 'View Available Records' takes user to Attach Dues History pop up
        Contact newPerson = new Contact();
        newPerson.setFirstName("Judith");
        newPerson.setLastName("Jenquine");
        newPerson.setMiddleName("A");
        membershipMembers.searchMembershipMember(memPolObj.basicSearch, newPerson);
        membershipMembers.selectMembershipMemberGender(Gender.Female);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.clickViewAvailableRecords();

        String accountNum = membershipMembers.getAttachDuesHistoryAccount();
        membershipMembers.setAttachDuesHistoryAccount(memPolObj.accountNumber);
        membershipMembers.clickImportDuesHistoryFetchUpdates();
        // If record found but no dues history then display message, "Record for < Account Number> was found. No dues history exists for this record".
        String val3 = "Record for " + memPolObj.accountNumber + " was found. No dues history exists for this record";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains(val3)) {
            errorMessage = errorMessage + "No Dues History validation is not displayed. \n";
        }

        // Under Dues Year Columns - Display last 10 years of records (if available).
        membershipMembers.setAttachDuesHistoryAccount(accountNum);
        membershipMembers.clickImportDuesHistoryFetchUpdates();
        membershipMembers.clickOK();

        // Click OK will take user back to Members screen Dues History tab.
        membershipMembers.importDuesHistoryByIndividualRecordByName(newPerson.getFirstName().toUpperCase());
        // Display "Remove All Imported Dues History", clicking "Remove All Imported Dues History" will remove imported history records only
        int duesHistoryRows = membershipMembers.getMembershipDuesHistoryRows();
        if (duesHistoryRows > 0) {
            membershipMembers.clickRemoveAllImportedDuesHistory();

            if (membershipMembers.getMembershipDuesHistoryRows() > 0) {
                errorMessage = errorMessage + "Clicking Removing all imported Dues History not removing the records.\n";
            }
        }

        membershipMembers.clickOK();

        membershipMembers.clickEditMembershipMember(newPerson);
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.importDuesHistoryByIndividualRecordByName(newPerson.getFirstName().toUpperCase());
        membershipMembers.clickOK();

        membershipMembers.clickGenericWorkorderSaveDraft();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

        guidewireHelpers.logout();
    }


    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Requirement : US13847: Membership Policy info screen - Testing
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Membership%20Dues/PC8%20-%20Common%20-%20Membership%20Dues.xlsx">PC8 - Common- Membership Dues</a>
     * @Description
     * @DATE Feb 20, 2018
     */
    @Test//(dependsOnMethods = {"testValidatePaidDuesTab"})
    public void testValidatepoliyInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        memPolObj = new GeneratePolicy.Builder(driver)
        		.withProductType(ProductLineType.Membership)
        		.withInsFirstLastName("Membership", "Dues")
        		.build(GeneratePolicyType.FullApp);
        new Login(driver).loginAndSearchSubmission(memPolObj.agentInfo.getAgentUserName(), memPolObj.agentInfo.getAgentPassword(), memPolObj.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);

        //Verify that if Agent creates the policy, the Agent field has the same Agent
        String agentExpected = memPolObj.agentInfo.agentPreferredName + " (" + memPolObj.agentInfo.getAgentNum() + ")";
        if (!agentExpected.equals(polInfo.getPolicyInfoAgent())) {
            errorMessage = errorMessage + "The Membership Policy Agent does not match the Policy Agent. \n";
        }

        //Verify that the letter D is displayed on Info bar   - not able to validate symbol so just validating the type
        InfoBar infoBar = new InfoBar(driver);
        if (!infoBar.getInfoBarProductLineType().equals(memPolObj.productType)) {
            errorMessage = errorMessage + "The Membership Policy type is not shown correctly in the Info bar. \n";
        }

        //Verify that "Annual" is the only option for "Term Type"
        for (String type : polInfo.getTermTypeOptions()) {
            if (!type.contentEquals("Annual")) {
                errorMessage = errorMessage + "The Membership Policy - other Term types are displayed. \n";
            }
        }
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        GenericWorkorder genericWorkorder = new GenericWorkorder(driver);
        //Verify that "Next" button, "Quote" button, "Save Draft" button, and "Close Options" buttons are displayed
        if (!genericWorkorder.checkCloseOptionsExists()) {
            errorMessage = errorMessage + "The Membership Policy - Close Options -> Not Taken is not displayed \n";
        }


        if (!genericWorkorder.checkGenericWorkorderNextExists()) {
            errorMessage = errorMessage + "The Membership Policy - Next button is not displayed \n";
        }

        if (!genericWorkorder.isQuotable()) {
            errorMessage = errorMessage + "The Membership Policy - Quote button is not enabled \n";
        }

        if (!genericWorkorder.checkIfElementExists(genericWorkorder.find(By.xpath("//span[contains(@id, 'JobWizardToolbarButtonSet:Draft-btnEl')]")), 100)) {
            errorMessage = errorMessage + "The Membership Policy - Save Draft button is not displayed \n";
        }

        genericWorkorder.clickGenericWorkorderSaveDraft();
        guidewireHelpers.logout();

        //Verify that that user can choose agent if created by user other than Agent

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), memPolObj.accountNumber);
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        polInfo = new GenericWorkorderPolicyInfo(driver);
        Agents agentInfo = AgentsHelper.getRandomAgent();
        polInfo.setAgentOfService(agentInfo);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
        guidewireHelpers.logout();
    }
}
