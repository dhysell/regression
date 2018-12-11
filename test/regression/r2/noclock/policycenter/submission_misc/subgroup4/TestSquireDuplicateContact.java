package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

@QuarantineClass
public class TestSquireDuplicateContact extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;

    private WebDriver driver;

    @Test()
    private void createPLAutoPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Coverage
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle newVeh = new Vehicle();
        newVeh.setEmergencyRoadside(true);
        vehicleList.add(newVeh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);

        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.FarmEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Propbusrules")
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"createPLAutoPolicy"})
    public void validateAdditionalInsuredDuplicateContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        checkInsuredNameAutomatic();
		new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        checkInsuredNameManual();
		new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        checkInsuredNameManualAutomatic(true);

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
    }

    @Test(dependsOnMethods = {"validateAdditionalInsuredDuplicateContact"})
    private void validateMembershipDuesDuplicateContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickMemberShipDuesAddButton();
        checkInsuredNameAutomatic();
        policyInfo.clickMemberShipDuesAddButton();
        checkInsuredNameManual();
        policyInfo.clickMemberShipDuesAddButton();
        checkInsuredNameManualAutomatic(false);

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

    }

    @Test(dependsOnMethods = {"validateMembershipDuesDuplicateContact"})
    private void validatePolicyMemberDupplicateContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();

        checkInsuredNameAutomatic();

        household.clickSearch();

        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1(),
                myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
                myPolicyObjPL.pniContact.getAddress().getZipNoDashes(), CreateNew.Create_New_Always);

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(myPolicyObjPL.pniContact.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.clickCheckForDuplicatesButton();
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        if (!namedInsured.isSelectMatchingContactDisplayed()) {
            Assert.fail(driver.getCurrentUrl() + "Manual Duplicate Failed!");
        }

        namedInsured.clickReturnToEditAdditionalInsured();
        additionalInsured.clickOK();
        additionalInsured.clickCancel();

        namedInsured.clickGenericWorkorderSaveDraft();

    }

    @Test(dependsOnMethods = {"validatePolicyMemberDupplicateContact"})
    private void testPropertyDetailsAIDuplicateContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        AdditionalInterest addInterest = new AdditionalInterest();
        addInterest.setPersonFirstName(myPolicyObjPL.pniContact.getFirstName());
        addInterest.setPersonLastName(myPolicyObjPL.pniContact.getLastName());
        addInterest.setAddress(myPolicyObjPL.pniContact.getAddress());

        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();

        checkInsuredNameAutomatic();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1(),
                myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
                myPolicyObjPL.pniContact.getAddress().getZipNoDashes(), CreateNew.Create_New_Always);

        GenericWorkorderAdditionalInterests interest = new GenericWorkorderAdditionalInterests(driver);
        interest.setPropertyAdditionalInterestAddressListing("New");
        interest.setContactEditAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
        interest.setContactEditAddressCity(myPolicyObjPL.pniContact.getAddress().getCity());
        interest.sendArbitraryKeys(Keys.TAB);
        interest.setContactEditAddressState(myPolicyObjPL.pniContact.getAddress().getState());
        interest.setContactEditAddressZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
        interest.sendArbitraryKeys(Keys.TAB);
        interest.selectBuildingsPropertyAdditionalInterestsInterestType(AdditionalInterestType.LessorPL.getValue());
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.clickCheckForDuplicatesButton();
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        if (namedInsured.isOverrideButtonDisplayed()) {
            namedInsured.clickOverrideButton();
            additionalInsured.clickOK();
        }

        if (!namedInsured.isSelectMatchingContactDisplayed()) {
            Assert.fail(driver.getCurrentUrl() + "Manual Duplicate Failed!");
        }
        namedInsured.clickReturnToEditAdditionalInsured();
        additionalInsured.clickRelatedContactsTab();

        additionalInsured.clickOK();

        if (namedInsured.isOverrideButtonDisplayed()) {
            namedInsured.clickOverrideButton();
            additionalInsured.clickOK();
        }

        namedInsured.clickReturnToEditAdditionalInsured();
        additionalInsured.clickCancel();

        addressBook.ClickReturnToPolicyInfo();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        propertyDetail.clickOk();

        propertyDetail.clickGenericWorkorderSaveDraft();
    }

    @Test(dependsOnMethods = {"testPropertyDetailsAIDuplicateContact"})
    private void testCheckFarmEquipAIDuplicateContact() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquip = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
        farmEquip.clickEditButton();
        farmEquip.clickSearchAdditionalInterests();
        checkInsuredNameAutomatic();
        farmEquip.clickSearchAdditionalInterests();

        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1(),
                myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
                myPolicyObjPL.pniContact.getAddress().getZipNoDashes(), CreateNew.Create_New_Always);

        GenericWorkorderAdditionalInterests interest = new GenericWorkorderAdditionalInterests(driver);
        interest.setPropertyAdditionalInterestAddressListing("New");
        interest.setContactEditAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
        interest.setContactEditAddressCity(myPolicyObjPL.pniContact.getAddress().getCity());
        interest.sendArbitraryKeys(Keys.TAB);
        interest.setContactEditAddressState(myPolicyObjPL.pniContact.getAddress().getState());
        interest.setContactEditAddressZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
        interest.sendArbitraryKeys(Keys.TAB);
        interest.selectBuildingsPropertyAdditionalInterestsInterestType(AdditionalInterestType.LessorPL.getValue());
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.clickCheckForDuplicatesButton();
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        if (!namedInsured.isSelectMatchingContactDisplayed()) {
            Assert.fail(driver.getCurrentUrl() + "Manual Duplicate Failed!");
        }
        namedInsured.clickReturnToEditAdditionalInsured();
        additionalInsured.clickRelatedContactsTab();

        additionalInsured.clickOK();
        if (namedInsured.isOverrideButtonDisplayed()) {
            namedInsured.clickOverrideButton();
            additionalInsured.clickOK();
        }
        namedInsured.clickReturnToEditAdditionalInsured();
        additionalInsured.clickCancel();

        addressBook.ClickReturnToPolicyInfo();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        additionalInsured.clickOK();

        namedInsured.clickGenericWorkorderSaveDraft();
    }

    private void checkInsuredNameManualAutomatic(boolean relation) {
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1(),
                myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
                myPolicyObjPL.pniContact.getAddress().getZipNoDashes(), CreateNew.Create_New_Always);

        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);

        if (relation) {
            namedInsured.selectAdditionalInsuredRelationship(RelationshipToInsured.Partner);
        }
        additionalInsured.selectAddressListing("New");
        additionalInsured.setAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
        additionalInsured.setContactEditAddressCity(myPolicyObjPL.pniContact.getAddress().getCity());
        additionalInsured.sendArbitraryKeys(Keys.TAB);
        additionalInsured.setContactEditAddressState(myPolicyObjPL.pniContact.getAddress().getState());
        additionalInsured.setZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
        additionalInsured.sendArbitraryKeys(Keys.TAB);

        additionalInsured.clickRelatedContactsTab();
        //additionalInsured.clickContactDetailsTab();

        additionalInsured.clickOK();
        if (namedInsured.isOverrideButtonDisplayed()) {
            namedInsured.clickOverrideButton();
            additionalInsured.clickOK();
        }

        if (!namedInsured.isSelectMatchingContactDisplayed()) {
            Assert.fail(driver.getCurrentUrl() + "Manual Duplicate Failed!");
        } else {
            namedInsured.clickReturnToEditAdditionalInsured();
            additionalInsured.clickCancel();
        }
    }

    private void checkInsuredNameManual() {
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1(),
                myPolicyObjPL.pniContact.getAddress().getCity(), myPolicyObjPL.pniContact.getAddress().getState(),
                myPolicyObjPL.pniContact.getAddress().getZipNoDashes(), CreateNew.Create_New_Always);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.selectAddressListing("New");
        additionalInsured.setAddressLine1(myPolicyObjPL.pniContact.getAddress().getLine1());
        additionalInsured.setContactEditAddressCity(myPolicyObjPL.pniContact.getAddress().getCity());
        additionalInsured.sendArbitraryKeys(Keys.TAB);
        additionalInsured.setContactEditAddressState(myPolicyObjPL.pniContact.getAddress().getState());
        additionalInsured.setZipCode(myPolicyObjPL.pniContact.getAddress().getZip());
        additionalInsured.sendArbitraryKeys(Keys.TAB);
        additionalInsured.selectAddressType(AddressType.Home);
        additionalInsured.setSSN("2" + StringsUtils.generateRandomNumberDigits(8).replace("00", "78"));

        additionalInsured.clickCheckForDuplicatesButton();
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        if (!namedInsured.isSelectMatchingContactDisplayed()) {
            Assert.fail(driver.getCurrentUrl() + "Manual Duplicate Failed!");
        } else {
            namedInsured.clickReturnToEditAdditionalInsured();
            additionalInsured.clickCancel();
        }
    }

    private void checkInsuredNameAutomatic() {
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByPersonDetails(myPolicyObjPL.pniContact.getFirstName(),
                myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getCity(),
                myPolicyObjPL.pniContact.getAddress().getState(), null);
        if (!addressBook.checkContactSearchFoundByFirstName(myPolicyObjPL.pniContact.getFirstName())) {
            Assert.fail(driver.getCurrentUrl() + "Automatic Duplicate Failed!");
        }
        addressBook.ClickReturnToPolicyInfo();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }

    }

}
