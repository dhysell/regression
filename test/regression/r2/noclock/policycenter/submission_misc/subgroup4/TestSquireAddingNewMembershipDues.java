package regression.r2.noclock.policycenter.submission_misc.subgroup4;


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
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author nvadlamudi
 * @Requirement :DE4301 - Search for contacts on membership dues, DE4255: Policy Members -- Can't Add Contact if Just in Membership Dues and Other Issues
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 14, 2016
 */
@QuarantineClass
public class TestSquireAddingNewMembershipDues extends BaseTest {
    private GeneratePolicy myPolicyObj;
    private Contact newPerson, anotherPerson;

    private WebDriver driver;

    @Test()
    public void testSquireCityQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "NewMember")
                .build(GeneratePolicyType.QuickQuote);
    }


    @Test(dependsOnMethods = {"testSquireCityQQ"})
    private void testAddNewMembers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        newPerson = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Removed", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/yyyy"));

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
        houseHoldMember.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObj.basicSearch, newPerson.getFirstName(), newPerson.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));

        householdMember.setDateOfBirth(newPerson.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObj.pniContact.getAddress());
        houseHoldMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuHouseholdMembers();
        houseHoldMember.clickRemoveMember(newPerson.getLastName());
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickMemberShipDuesAddButton();
        addressBook.searchAddressBookByFirstLastName(myPolicyObj.basicSearch, newPerson.getFirstName(), newPerson.getLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
        policyInfo.selectMembershipDuesCounty(newPerson.getFirstName(), "Ada");
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.performRiskAnalysisAndQuote(myPolicyObj);
        sideMenu.clickSideMenuRiskAnalysis();
    }

    @Test(dependsOnMethods = {"testAddNewMembers"})
    private void testAddNewMembershipPolicyMember() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        anotherPerson = new Contact("TestMem" + StringsUtils.generateRandomNumberDigits(6), "Adding", Gender.Female, DateUtils.convertStringtoDate("01/01/1985", "MM/dd/yyyy"));
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickMemberShipDuesAddButton();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObj.basicSearch, anotherPerson.getFirstName(), anotherPerson.getLastName(), null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderPolicyInfoAdditionalNamedInsured aInsured = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        aInsured.setEditAdditionalNamedInsuredAddressListing(myPolicyObj.pniContact.getAddress());
/*		sendArbitraryKeys(Keys.TAB);
		common.setAddress(myPolicyObj.pniContact.getAddress().getLine1());
		common.setCity(myPolicyObj.pniContact.getAddress().getCity());
				common.setState(myPolicyObj.pniContact.getAddress().getState());
		common.setZip(myPolicyObj.pniContact.getAddress().getZip());
		common.setAddressType(myPolicyObj.pniContact.getAddress().getType());
*/
        aInsured.sendArbitraryKeys(Keys.TAB);
        aInsured.setEditAdditionalNamedInsuredAlternateID(anotherPerson.getLastName());
        aInsured.clickOK();
        policyInfo.selectMembershipDuesCounty(anotherPerson.getLastName(), "Ada");
        new GuidewireHelpers(driver).clickNext();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
        houseHoldMember.clickSearch();
        addressBook.searchAddressBookByFirstLastName(myPolicyObj.basicSearch, anotherPerson.getFirstName(), anotherPerson.getLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);

        householdMember.setDateOfBirth(anotherPerson.getDob());
        houseHoldMember.sendArbitraryKeys(Keys.TAB);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);

        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObj.pniContact.getAddress());
        houseHoldMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
        sideMenu.clickSideMenuPolicyInfo();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();		

    }


    @Test(dependsOnMethods = {"testAddNewMembershipPolicyMember"})
    private void testRemoveMembershipPolicyMemberAndValidate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
        houseHoldMember.clickRemoveMember(this.anotherPerson.getLastName());
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setMembershipDues(this.anotherPerson.getLastName(), true);
        policyInfo.removeMembershipDuesContact(this.newPerson.getLastName());
        sideMenu.clickSideMenuHouseholdMembers();

        if (houseHoldMember.getPolicyHouseholdMembersTableRow(this.newPerson.getLastName()) > 0) {
            Assert.fail("Contact removed from membership due does not exists in policy member too");
        }
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuQuote();
    }

}
