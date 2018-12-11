package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
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
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@QuarantineClass
public class TestRemoveSSNTINAlterateIDValidations extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private String ANILastName = "ANIPerson" + StringsUtils.generateRandomNumberDigits(8);
    private String ANICompanyName = "ANIOrg" + StringsUtils.generateRandomNumberDigits(9);

    private String MembershipLastName = "MEM" + StringsUtils.generateRandomNumberDigits(6);

    private String policyMemberLastName = "POLMPer" + StringsUtils.generateRandomNumberDigits(8);
    private String policyMemberCompanyName = "POLMOrg" + StringsUtils.generateRandomNumberDigits(9);

    private WebDriver driver;

    @Test()
    public void testCreateSquirePolWithAllSections() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

        // Property details
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
        prop1.setpropertyType(PropertyTypePL.ResidencePremises);
        prop1.setConstructionType(ConstructionTypePL.Frame);
        locOnePropertyList.add(prop1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(5);
        locationsList.add(locToAdd);

        // Section II coverages
        SquireLiability liability = new SquireLiability();

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        //ANI
        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test" + StringsUtils.generateRandomNumberDigits(8), this.ANILastName,
                AdditionalNamedInsuredType.Spouse, new AddressInfo(false));
        ani.setHasMembershipDues(true);
        ani.setNewContact(CreateNew.Create_New_Always);
        listOfANIs.add(ani);

        // Policy Members
        ArrayList<Contact> policyMembers = new ArrayList<Contact>();
        Contact polMember = new Contact("PolMember", this.policyMemberLastName, Gender.Male, DateUtils.convertStringtoDate("01/01/1980", "MM/dd/yyyy"));
        policyMembers.add(polMember);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.policyMembers = policyMembers;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Contact", "EditPerm")
                .withSocialSecurityNumber(StringsUtils.generateRandomNumberDigits(9))
                .withANIList(listOfANIs)
                .build(GeneratePolicyType.QuickQuote);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        Login login = new Login(driver);
        login.loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.getAgentUserName(), this.myPolicyObjPL.agentInfo.getAgentPassword(), this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        //Additing AdditionalInsured Company
        PolicyInfoAdditionalNamedInsured aniCompany = new PolicyInfoAdditionalNamedInsured(ContactSubType.Company, this.ANICompanyName, AdditionalNamedInsuredType.Affiliate, new AddressInfo(false));
        aniCompany.setNewContact(CreateNew.Create_New_Always);

		new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByCompanyName(this.myPolicyObjPL.basicSearch, aniCompany.getCompanyName(), null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        editANIPage.setEditAdditionalNamedInsuredAddressListing(this.myPolicyObjPL.pniContact.getAddress());
/*		editANIPage.setEditAdditionalNamedInsuredAddressLine1(this.myPolicyObjPL.pniContact.getAddress().getLine1());
		editANIPage.setEditAdditionalNamedInsuredAddressCity(this.myPolicyObjPL.pniContact.getAddress().getCity());
		editANIPage.setEditAdditionalNamedInsuredAddressState(this.myPolicyObjPL.pniContact.getAddress().getState());
		editANIPage.setEditAdditionalNamedInsuredAddressZipCode(this.myPolicyObjPL.pniContact.getAddress().getZip());
		editANIPage.setEditAdditionalNamedInsuredAddressAddressType(this.myPolicyObjPL.pniContact.getAddress().getType());
		editANIPage.setEditAdditionalNamedInsuredRelationship(this.myPolicyObjPL.aniList.get(0).getRelationshipToPNI());
*/
        editANIPage.setEditAdditionalNamedInsuredSSN(StringsUtils.generateRandomNumberDigits(9));
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredTIN(StringsUtils.generateRandomNumberDigits(9));
        editANIPage.setEditAdditionalNamedInsuredAlternateID(aniCompany.getCompanyName());
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.clickOK();

        //Memebeship dues
        policyInfo.clickMemberShipDuesAddButton();
        addressBook.searchAddressBookByFirstLastName(this.myPolicyObjPL.basicSearch, "Membership", MembershipLastName, null, null, null, null, CreateNew.Create_New_Always);
        addressBook.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredAddressListing(this.myPolicyObjPL.pniContact.getAddress());
/*		common.setAddress(this.myPolicyObjPL.pniContact.getAddress().getLine1());
		common.setCity(this.myPolicyObjPL.pniContact.getAddress().getCity());
		common.setState(this.myPolicyObjPL.pniContact.getAddress().getState());
		common.setZip(this.myPolicyObjPL.pniContact.getAddress().getZip());
		common.setAddressType(this.myPolicyObjPL.pniContact.getAddress().getType());
*/
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredSSN(StringsUtils.generateRandomNumberDigits(9));
        editANIPage.setEditAdditionalNamedInsuredTIN(StringsUtils.generateRandomNumberDigits(9));
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredAlternateID(MembershipLastName);
        editANIPage.clickOK();

        // Adding policy member
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        addressBook.searchAddressBookByCompanyName(this.myPolicyObjPL.basicSearch, this.policyMemberCompanyName, this.myPolicyObjPL.pniContact.getAddress(), CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(this.policyMemberCompanyName);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());

        householdMember.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredSSN(StringsUtils.generateRandomNumberDigits(9));

        editANIPage.setEditAdditionalNamedInsuredTIN(StringsUtils.generateRandomNumberDigits(9));
        householdMember.clickBasicsContactsTab();
        householdMember.clickOK();

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        guidewireHelpers.logout();
        guidewireHelpers.setPolicyType(myPolicyObjPL, GeneratePolicyType.QuickQuote);
        myPolicyObjPL.convertTo(driver, GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"testCreateSquirePolWithAllSections"})
    private void testcheckSSNTINAlternateIDValidations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo pi = new GenericWorkorderPolicyInfo(driver);
        pi.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        editANIPage.setEditAdditionalNamedInsuredSSN("");
        editANIPage.setEditAdditionalNamedInsuredAlternateID("");
        householdMember.clickUpdate();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        if (!risk.getValidationMessagesText().contains("At least one of the following fields has to be filled out: SSN, TIN, or Alternate ID.")) {
            Assert.fail("Check the ANI Message for no SSN, TIN or Alt ID.");
        }
        householdMember.clickCancel();
        //ANI Contact
        pi.clickANIContact(this.myPolicyObjPL.aniList.get(0).getPersonFirstName());
        editANIPage.setEditAdditionalNamedInsuredSSN("");
        editANIPage.setEditAdditionalNamedInsuredAlternateID("");
        householdMember.clickUpdate();
        if (!risk.getValidationMessagesText().contains("At least one of the following fields has to be filled out: SSN, TIN, or Alternate ID.")) {
            Assert.fail("Check the ANI Message for no SSN, TIN or Alt ID.");
        }
        householdMember.clickCancel();

        //ANI Company
        pi.clickANIContact(this.ANICompanyName);
        editANIPage.setEditAdditionalNamedInsuredSSN("");
        editANIPage.sendArbitraryKeys(Keys.TAB);

        editANIPage.setEditAdditionalNamedInsuredAlternateID("");
        editANIPage.sendArbitraryKeys(Keys.TAB);

        editANIPage.setEditAdditionalNamedInsuredTIN("");
        householdMember.clickUpdate();
        if (!risk.getValidationMessagesText().contains("At least one of the following fields has to be filled out: TIN or Alternate ID")) {
            Assert.fail("Check the ANI Message At least one of the following fields has to be filled out: TIN or Alternate ID failed");
        }
        householdMember.clickCancel();

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickPolicyHolderMembersByName(this.policyMemberCompanyName);

        editANIPage.setEditAdditionalNamedInsuredSSN("");
        editANIPage.setEditAdditionalNamedInsuredAlternateID("");
        editANIPage.sendArbitraryKeys(Keys.TAB);
        editANIPage.setEditAdditionalNamedInsuredTIN("");
        householdMember.clickOK();
        if (!risk.getValidationMessagesText().contains("At least one of the following fields has to be filled out: TIN or Alternate ID")) {
            Assert.fail("Check the ANI Message At least one of the following fields has to be filled out: TIN or Alternate ID failed");
        }
        householdMember.clickCancel();
    }

}
