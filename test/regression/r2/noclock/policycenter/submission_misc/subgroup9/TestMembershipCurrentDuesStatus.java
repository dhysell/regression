package regression.r2.noclock.policycenter.submission_misc.subgroup9;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MembershipCurrentMemberDuesStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author nvadlamudi
 * @Requirement : US13298: "Add Dues" dropdown instead of checkbox
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Membership%20Dues/PC8%20-%20Common%20-%20Membership%20Dues.xlsx">
 * PC8 - Common- Membership Dues</a>
 * @Description
 * @DATE Feb 12, 2018
 */
@QuarantineClass
public class TestMembershipCurrentDuesStatus extends BaseTest {
    private GeneratePolicy mySquirePolicy;
    private GeneratePolicy mySiblingPol;

    private WebDriver driver;

    @Test()
    public void testIssueSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolicyLocations(locationsList)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.Membership)
                .withInsFirstLastName("Test", "SqForSib").withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePol"})
    private void testSiblingPolMembershipDues() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL75K,
                MedicalLimit.TenK, true, UninsuredLimit.CSL75K, false, UnderinsuredLimit.CSL75K);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        mySiblingPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withSiblingPolicy(mySquirePolicy, "Dues", "Sibling")
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchSubmission(mySiblingPol.agentInfo.getAgentUserName(), mySiblingPol.agentInfo.getAgentPassword(),
                mySiblingPol.accountNumber);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();
        GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);
        membershipMembers.clickEdit();
        String errorMessage = "";
        if (!membershipMembers.getMembershipMembersCurrentStatus().equals(MembershipCurrentMemberDuesStatus.Waived)) {
            errorMessage = errorMessage + "Sibling Contact - Unexpected membership Dues status : "
                    + membershipMembers.getMembershipMembersCurrentStatus() + " displayed.";
        }
        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.clickOK();
        // Validating charged on Another policy
        membershipMembers.searchMembershipMember(mySquirePolicy.basicSearch, mySquirePolicy.pniContact);
        if (!membershipMembers.getMembershipMembersCurrentStatus().equals(MembershipCurrentMemberDuesStatus.Charged_On_Another_Policy)) {
            errorMessage = errorMessage + "Searching Another policy PNI - Unexpected membership Dues status : "
                    + membershipMembers.getMembershipMembersCurrentStatus() + " displayed.";
        }
        membershipMembers.clickOK();

        // Ensure a date picker field is available when Paid Out of State is
        // selected as current dues status. And its not a required field.
        Contact anotherContact = new Contact();
        anotherContact.setFirstName("Paid" + StringsUtils.generateRandomNumberDigits(6));
        anotherContact.setLastName("OutOfSt" + StringsUtils.generateRandomNumberDigits(8));
        membershipMembers.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(mySquirePolicy.basicSearch, anotherContact.getFirstName(), anotherContact.getLastName(), null,
                null, null, null, CreateNew.Create_New_Always);
        AddressInfo newAddress = new AddressInfo(false);
        newAddress.setType(AddressType.Home);
        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Bannock);
        membershipMembers.selectMembershipMembersCurrentStatus(MembershipCurrentMemberDuesStatus.Paid_Out_Of_State);
        membershipMembers.sendArbitraryKeys(Keys.TAB);
        membershipMembers.setOutOfStatePaidDate(new Date());
        membershipMembers.clickOK();

        // Ensure that when any option other than charged is chosen, the dues
        // fee is not charged.
        Contact anoContact = new Contact();
        anoContact.setFirstName("Another" + StringsUtils.generateRandomNumberDigits(6));
        anoContact.setLastName("Honorary" + StringsUtils.generateRandomNumberDigits(8));
        membershipMembers.clickSearch();
        addressBook.searchAddressBookByFirstLastName(mySquirePolicy.basicSearch, anoContact.getFirstName(), anoContact.getLastName(), null, null,
                null, null, CreateNew.Create_New_Always);
        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Boise);
        membershipMembers.selectMembershipMembersCurrentStatus(MembershipCurrentMemberDuesStatus.Honorary);
        membershipMembers.clickOK();

        Contact brandNewCharge = new Contact();
        anoContact.setFirstName("New" + StringsUtils.generateRandomNumberDigits(6));
        anoContact.setLastName("Brand" + StringsUtils.generateRandomNumberDigits(8));
        membershipMembers.clickSearch();
        addressBook.searchAddressBookByFirstLastName(mySquirePolicy.basicSearch, anoContact.getFirstName(), anoContact.getLastName(), null, null,
                null, null, CreateNew.Create_New_Always);
        membershipMembers.selectMembershipMemberAddressListing(newAddress);
        membershipMembers.setMemberDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -30));
        membershipMembers.selectMembershipMemberGender(Gender.Male);
        membershipMembers.selectMembershipMembersDuesCounty(CountyIdaho.Boise);
        membershipMembers.selectMembershipMembersCurrentStatus(MembershipCurrentMemberDuesStatus.Charged);
        membershipMembers.clickOK();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.clickGenericWorkorderQuote();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickMembershiLineTab();
        if (quote.getMembershipMemberNetAmountByMemberName(anoContact.getFirstName()) > 0) {
            errorMessage = errorMessage + "Unexpected Honorary Contact - membership Dues amount : "
                    + quote.getMembershipMemberNetAmountByMemberName(anoContact.getFirstName());
        }

        if (quote.getMembershipMemberNetAmountByMemberName(anotherContact.getFirstName()) > 0) {
            errorMessage = errorMessage + "Unexpected Paid Out of State Contact - membership Dues amount : "
                    + quote.getMembershipMemberNetAmountByMemberName(anotherContact.getFirstName());
        }

        if (quote.getMembershipMemberNetAmountByMemberName(this.mySquirePolicy.pniContact.getFirstName()) > 0) {
            errorMessage = errorMessage + "Unexpected Another Squire policy PNI - membership Dues amount : "
                    + quote.getMembershipMemberNetAmountByMemberName(this.mySquirePolicy.pniContact.getFirstName());
        }

        if (quote.getMembershipMemberNetAmountByMemberName(brandNewCharge.getFirstName()) <= 0) {
            errorMessage = errorMessage + "Unexpected Charged Membership Dues amount : "
                    + quote.getMembershipMemberNetAmountByMemberName(brandNewCharge.getFirstName());
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

    }

}