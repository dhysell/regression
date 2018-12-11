package regression.r2.noclock.policycenter.submission_memdues;

import java.util.Date;

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
import repository.gw.enums.MembershipType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderMembershipCommodities;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US13310: Membership - Policy change
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Membership%20Dues/PC8%20-%20Common%20-%20Membership%20-%20Policy%20Change.xlsx">PC8 - Common - Membership - Policy Change</a>
 * @Description : Validate membershipdues policy change
 * @DATE Mar 14, 2018
 */
@QuarantineClass
public class TestMembershipDuesPolicyChange extends BaseTest {

    private GeneratePolicy memPolObj;

    private WebDriver driver;

    @Test
    public void testIssueMembershipDuesPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        memPolObj = new GeneratePolicy.Builder(driver).withMembershipType(MembershipType.Regular)
                .withProductType(ProductLineType.Membership).withInsFirstLastName("Membership", "Dues")
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueMembershipDuesPol"})
    private void testValidateMembershipDuesChanges() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(memPolObj.agentInfo.getAgentUserName(),
                memPolObj.agentInfo.getAgentPassword(), memPolObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String errorMessage = "";

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

        //Policy Info Screen:
        //Ensure that Agent & PA can change Membership type.
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setPolicyInfoMembershipType(MembershipType.Regular);

        //Commodities Screen:
        //Ensure that everything that was editable during new a submission should be editable during a policy change for Agent, PA, UW, UW Supervisor & Federation user.
        sideMenu.clickSideMenuMembershipCommodities();
        GenericWorkorderMembershipCommodities commodities = new GenericWorkorderMembershipCommodities(driver);
        commodities.setAcresPlanted(Commodities.AcresPlanted.Alfalfahay, 5);

        //Ensure that for existing member when membership type is changed from Associate to Regular system should automatically check/select Gem State Producer on members screen basics tab.
        sideMenu.clickSideMenuMembershipMembers();
        GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);

        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "Regular -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        if (!membershipMembers.isGemStateProducerCheckboxChecked()) {
            errorMessage = errorMessage + "Regular - Membership Dues - Gem State Producer Checkbox is not selected \n";
        }
        membershipMembers.clickOK();
        //Ensure that for newly added member when membership type is changed from Associate to Regular system should automatically check/select Gem State Producer on members screen basics tab, provided such action is part of same transaction.
        Contact newPerson = new Contact();
        newPerson.setFirstName("NewTest" + StringsUtils.generateRandomNumberDigits(6));
        newPerson.setLastName("Person" + StringsUtils.generateRandomNumberDigits(8));
        AddressInfo newAddress = new AddressInfo(false);
        newAddress.setType(AddressType.Home);

        membershipMembers.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(memPolObj.basicSearch, newPerson.getFirstName(), newPerson.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "NewMember - Regular -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        //There is a future user story to fix the below validation
		/*if (!membershipMembers.isGemStateProducerCheckboxChecked()) {
			errorMessage = errorMessage + "NewMember - Regular - Membership Dues - Gem State Producer Checkbox is not selected \n";
		}*/

        //Ensure that while processing policy change following fillable/selectable/changeable fields can be added/edited/removed/changed by Agent, PA, UW, UW Supervisor & Federation users.
        //Inception Date, County, Current Dues Status, Renewal Dues Status, FB Quarterly, Gem State Producer
        membershipMembers.setMembershipInceptionDate(currentSystemDate);
        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Bannock);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.setFBQuarterlyCheckbox(false);
        membershipMembers.setGemStateProducerCheckbox(false);
        membershipMembers.clickOK();

        //Ensure that for existing member when membership type is changed from Regular to Associate system does not automatically uncheck/unselect Gem State Producer on members screen basics tab.
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.setPolicyInfoMembershipType(MembershipType.Associate);
        sideMenu.clickSideMenuMembershipMembers();
        membershipMembers.clickEditMembershipMember(this.memPolObj.pniContact);
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "Associate -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        //There is a future user story to fix the below validation
		
		/*if (membershipMembers.isGemStateProducerCheckboxChecked()) {
			errorMessage = errorMessage + "Associate - Membership Dues - Gem State Producer Checkbox is selected \n";
		}*/
        membershipMembers.clickOK();

        //Ensure that for newly added member when membership type is changed from Regular to Associate system should automatically uncheck/unselect Gem State Producer on members screen basics tab, provided such action is part of same transaction.
        Contact newComp = new Contact();
        newComp.setCompanyName("NewComp" + StringsUtils.generateRandomNumberDigits(9));
        membershipMembers.clickSearch();
        addressBook.searchAddressBookByCompanyName(memPolObj.basicSearch, newComp.getCompanyName(), newAddress, CreateNew.Create_New_Always);
        if (!membershipMembers.isFBQuarterlyCheckboxChecked()) {
            errorMessage = errorMessage + "NewMember - Associate -  Membership Dues - FB quarterly Checkbox is not selected \n";
        }

        if (membershipMembers.isGemStateProducerCheckboxChecked()) {
            errorMessage = errorMessage + "NewMember - Associate - Membership Dues - Gem State Producer Checkbox is selected \n";
        }
        membershipMembers.selectMembershipMemberAddressListing(newAddress);

        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Bannock);
        membershipMembers.clickOK();


        //	Members Screen � Member Related Contacts tab
        //Ensure that everything that was editable during new a submission should be editable during a policy change for Agent, PA, UW, UW Supervisor & Federation user.
        sideMenu.clickSideMenuMembershipMembers();
        membershipMembers.clickEditMembershipMember(memPolObj.pniContact);
        membershipMembers.clickMemberRelatedContactsTab();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        ContactRelationshipToMember contactRelation = (guidewireHelpers.getRandBoolean()) ? ContactRelationshipToMember.Spouse : ((guidewireHelpers.getRandBoolean()) ? ContactRelationshipToMember.Parent_Guardian : ContactRelationshipToMember.Child_Ward);
        Contact relatedContact = new Contact("NewTest" + StringsUtils.generateRandomNumberDigits(6), "Added" + StringsUtils.generateRandomNumberDigits(6));
        relatedContact.setContactRelationshipToPNI(contactRelation);
        membershipMembers.addMembershipDuesRelatedContacts(memPolObj.basicSearch, relatedContact);
        membershipMembers.clickOK();

        //Members Screen � Dues History tab
        //Ensure that everything that was editable during new a submission should be editable during a policy change for Agent, PA, UW, UW Supervisor & Federation user.
        membershipMembers.clickEditMembershipMember(memPolObj.pniContact);
        membershipMembers.clickDuesHistoryTab();
        membershipMembers.addMembershipDuesHistory(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), CountyIdaho.Bannock);
        membershipMembers.clickOK();

        membershipMembers.clickGenericWorkorderSaveDraft();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

        guidewireHelpers.logout();


    }

    @Test(dependsOnMethods = {"testValidateMembershipDuesChanges"})
    private void testValidateMembershipDuesChangesByUW() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), memPolObj.membership.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickPendingTransaction(TransactionType.Policy_Change);
        //Policy Info Screen:
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        Agents agentInfo = AgentsHelper.getRandomAgent();

        //Ensure that UW Supervisor & Federation user can change Agent of Record & Agent of Service.
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setAgentOfService(agentInfo);

        //Ensure that UW, UW Supervisor & Federation user can change Membership type and written date.
        polInfo.setPolicyInfoWrittenDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.quoteAndIssue();
        new GuidewireHelpers(driver).logout();
    }
}
