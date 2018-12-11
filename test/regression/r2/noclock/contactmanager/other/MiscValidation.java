package regression.r2.noclock.contactmanager.other;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsDBA;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TaxReportingOption;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.DBA;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

@QuarantineClass
public class MiscValidation extends BaseTest {
	private WebDriver driver;
    private AbUsers abUser;
    private GenerateContact myContactObj;
    private ArrayList<DBA> myDBAs = new ArrayList<DBA>();
    private DBA newDBA = new DBA("theScripter");
    private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
    private String firstName = "Reverend";
    private String lastName = "Martin";
    private String middleName = "Bill";
    private GeneratePolicy newPolicy = null;



    @Test()
    public void makeContactMultipleAddress() throws Exception {

        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<AddressInfo> contactAddresses = new ArrayList<AddressInfo>();
        for (int i = 1; i < 6; i++) {
            contactAddresses.add(new AddressInfo(true));
        }
        contactAddresses.get(0).setIsPrimaryAddress(true);

        myDBAs.add(newDBA);

        this.rolesToAdd.add(ContactRole.Vendor);
        this.rolesToAdd.add(ContactRole.Lienholder);

        this.myContactObj = new GenerateContact.Builder(driver)
                .withCreator(abUser)
                .withFirstLastName(this.firstName, this.middleName, this.lastName)
                .withAddresses(contactAddresses)
                .withDba(this.myDBAs)
                .withGenerateAccountNumber(true)
                .build(GenerateContactType.Person);

        System.out.println("accountNumber: " + myContactObj.accountNumber);
    }

    @Test(dependsOnMethods = {"makeContactMultipleAddress"})
    public void changeAddressType() throws Exception {
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName,
                myContactObj.addresses.get(0).getLine1());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        String pageTitle = contactPage.getContactPageTitle();
        if (!pageTitle.equals(myContactObj.firstName + " " + myContactObj.lastName)) {
            Assert.fail(driver.getCurrentUrl() + "The page title does not match the name of the contact that was to be created.");
        }
        contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
        contactPage.clickContactDetailsBasicsCancel(OkCancel.OK);
        contactPage.clickContactDetailsBasicsEditLink();

        contactPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        addressPage.updateAddressTypeInAddressTable(this.myContactObj.addresses.get(0).getLine1(), AddressType.Other);
        System.out.println(myContactObj.accountNumber);
    }

    @Test()
    public void testLoanInfoTypes() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults lienholderResults = searchMe.getRandomLienholderByName(ContactSubType.Contact, "abc");
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPg = new ContactDetailsAddressesAB(driver);
        addressPg.updateAddressTypeInAddressTable(lienholderResults.getAddress().getLine1(), AddressType.Lienholder);
        addressPg.clickContactDetailsAddressesUpdate();
        addressPg.clickContactDetailsAdressesEditLink();
        addressPg.setAllLoanTypeInfoTrue();
        addressPg.clickContactDetailsAddressesUpdate();
    }

    @Test()
    public void changeTaxInfo() throws Exception {
        this.abUser = AbUserHelper.getRandomDeptUser("Claims");
        if (abUser.getUserName().equals("dalley")) {
            changeTaxInfo();
        }
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        //investigate no search results. Will need a massive try catch.
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.selectTaxReportingAs(TaxReportingOption.None);
        Assert.assertEquals(contactPage.getTaxInfoTin(), "false");
        contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.removeContactDetailsBasicsRole(ContactRole.Vendor);
        contactPage.clickContactDetailsBasicsUpdateLink();
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.addContactDetailsBasicsRole(ContactRole.Vendor);
        contactPage.selectTaxReportingAs(TaxReportingOption.TIN);
        contactPage.clickContactDetailsBasicsUpdateLink();
    }

    @Test(dependsOnMethods = {"makeContactMultipleAddress"})
    public void minorValidations() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName,
                myContactObj.addresses.get(0).getLine1());
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -1 * 16);
        contactPage.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", dob));
        String minorText = contactPage.validateMinorChild(DateUtils.dateFormatAsString("MM/dd/yyyy", dob));
        if (!minorText.equals("Minor Child (" + DateUtils.getDifferenceBetweenDatesAbsoluteValue(DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager), dob, DateDifferenceOptions.Year) + ")")) {
            Assert.fail("Please check the Minor designation and ensure it is processed correctly.");
        }
        contactPage.clickContactDetailsBasicsUpdateLink();
        String scrappedName = contactPage.getContactDetailsBasicsContactName();
        if (!scrappedName.contains("(" + DateUtils.getDifferenceBetweenDatesAbsoluteValue(DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager), dob, DateDifferenceOptions.Year) + ")")) {
            Assert.fail("The age is not listed by the name of the account holder.");
        }
    }

    @Test(dependsOnMethods = {"makeContactMultipleAddress"})
    public void testSuffix() throws Exception {
        this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName,
                myContactObj.addresses.get(0).getLine1());
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        List<String> suffixes = contactPage.getContactDetailsBasicsSuffix();
        boolean found = false;
        for (String suffix : suffixes) {
            if (suffix.equals("C.N.P.")) {
                found = true;
            }
        }
        org.testng.Assert.assertFalse(!found, "The C.N.P. suffix should be found in the suffix drop down.");
    }

    public void changeDOB(String dob) {
        ContactDetailsDBA clickBasicsTab = new ContactDetailsDBA(driver);
        clickBasicsTab.clickContactDetailsBasicLink();
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsEditLink();
        contactPage.setDateOfBirth(dob);
        contactPage.clickContactDetailsBasicsUpdateLink();
    }

    @Test
    public void noPolicyNotification() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Bldg2AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc1Bldg2AddInterest.setFirstMortgage(true);
        loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
        loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        newPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "AdditionalInterest")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.abUser = AbUserHelper.getRandomDeptUser("Number");

        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.searchByFirstLastName(newPolicy.pniContact.getFirstName(), newPolicy.pniContact.getLastName(),
                newPolicy.pniContact.getAddress().getLine1());

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Year, -19)));
        basicsPage.clickContactDetailsBasicsUpdateLink();
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.clickContactDetailsBasicsAccountsLink();


        ContactDetailsAccountAB acctPage = new ContactDetailsAccountAB(driver);
        acctPage.changeStatus("Cancelled", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
        acctPage.clickUpdate();

        String noPoliciesExist = basicsPage.getNoPolicyNotification();

        Assert.assertTrue(noPoliciesExist.contains("No active policies exist for this contact"));

    }
}
	

