package repository.gw.generate;


import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsDBA;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.gw.enums.*;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.ContactEFTData;
import repository.gw.generate.custom.DBA;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateContact {

    public AbUsers creator = null;
    public String firstName = "New";
    public String lastName = "Contact";
    public String middleName = null;
    public repository.gw.enums.PersonSuffix suffixName = repository.gw.enums.PersonSuffix.None;
    public String companyName = null;
    public String alternateName = null;
    public String formerName = null;
    public String tin = null;
    public String ssn = null;
    public ArrayList<repository.gw.enums.ContactRole> roles;
    public InternetAddress emailMain = null;
    public InternetAddress emailAlternate = null;
    public Gender gender = null;
    public String driversLicense = null;
    public State driverState = null;
    public Date dateOfBirth = null;
    public boolean membershipOnly = false;
    public ArrayList<ContactEFTData> efts;
    public String accountNumber = null;
    public repository.gw.enums.ContactMembershipType membershipType = repository.gw.enums.ContactMembershipType.Other;
    public Agents agent = null;
    public URL website = null;
    private String importFirstName = null;
//    private String importMiddleName = null;
    private String importLastNameOrCompanyName = null;
    public repository.gw.enums.PersonSuffix importSuffixName = null;
    private String importAddressLine1 = null;
    public List<DBA> dba;
    private boolean acctNumberNeeded;
    public ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
    public String comments = null;
    private repository.gw.enums.GenerateContactType typeToGenerate;
    private ArrayList<Date> duesYears = new ArrayList<>();
    private boolean withGeneratedLienNumber;
    public String lienNumber;
//    private String vendorNumber;
    private repository.gw.enums.VendorType vendorType;
//	private boolean withTimeStamp;

    private WebDriver driver;

    public GenerateContact(repository.gw.enums.GenerateContactType typeToGenerate, Builder builderDetails) throws Exception {
        this.typeToGenerate = typeToGenerate;
        this.driver = builderDetails.webDriver;
        switch (typeToGenerate) {
            case Person:
                person(true, builderDetails);
                break;
            case Company:
                company(false, builderDetails);
                break;
        }
    }

    private void checkParametersLoginAndGetToSearch(boolean isPerson, Builder builderStuff) throws Exception {
        this.creator = builderStuff.creator;
        this.firstName = builderStuff.firstName;
        this.lastName = builderStuff.lastName;
        this.companyName = builderStuff.companyName;
        this.alternateName = builderStuff.alternateName;
        this.formerName = builderStuff.formerName;
        this.tin = builderStuff.tin;
        this.ssn = builderStuff.ssn;
        this.roles = builderStuff.roles;
        this.emailMain = builderStuff.emailMain;
        this.emailAlternate = builderStuff.emailAlternate;
        this.dateOfBirth = builderStuff.dateOfBirth;
        this.gender = builderStuff.gender;
        this.comments = builderStuff.comments;
        this.efts = builderStuff.efts;
        boolean acctNumberNeeded = builderStuff.generateAccountNumber;
        this.accountNumber = builderStuff.acctNum;
        this.membershipType = builderStuff.membershipType;
        this.agent = builderStuff.agent;
        this.membershipOnly = builderStuff.membershipOnly;
        this.website = builderStuff.website;
        this.driversLicense = builderStuff.driversLicense;
        this.driverState = builderStuff.driversLicenseState;
        this.dba = builderStuff.dba;
        this.importFirstName = builderStuff.importFirstName;
//        this.importMiddleName = builderStuff.importMiddleName;
        this.importLastNameOrCompanyName = builderStuff.importLastNameOrCompanyName;
        this.importSuffixName = builderStuff.importSuffixName;
        this.importAddressLine1 = builderStuff.importAddressLine1;
        this.addresses = builderStuff.addresses;
        boolean unique = builderStuff.unique;
        this.duesYears = builderStuff.duesYears;
        this.withGeneratedLienNumber = builderStuff.withGeneratedLienNumber;
        this.vendorType = builderStuff.vendorType;
//        this.vendorNumber = builderStuff.vendorNumber;
//		this.withTimeStamp = builderStuff.withTimeStamp;


        if (!roles.isEmpty()) {

            for (repository.gw.enums.ContactRole role : roles) {
                switch (role) {
                    case Vendor:
                        if (vendorType == null) {
                            Assert.fail("When creating a vendor a type is required.");
                        }
                        break;
                    case Agency:
                        Assert.fail("We aren't set up to create an Agency with Generate.");
                        break;
                    case Bank:

                    case ClaimParty:

                    case County:
                        Assert.fail("We aren't set up to create a County with Generate.");

                    case Dealer:

                    case Finance:

                    case Lienholder:

                    default:

                        break;
                }

            }
        }

        if (this.creator == null) {
            this.creator = AbUserHelper.getRandomDeptUser("Policy Services");
        }

        if (isPerson) {
            if (firstName == null) {
                firstName = "Test";
            }

            if (lastName == null) {
                lastName = "Guy";
            }
            if (unique) {
                String dateString = repository.gw.helpers.DateUtils.dateFormatAsString("yyMMddHHmmss", new Date());
                lastName = lastName + "-" + dateString;
                if (lastName.length() >= 30) {
                    lastName = lastName.substring(0, 28);
                }
            }
        } else {
            if (companyName == null) {
                companyName = "Test Company";
            }
            if (unique) {
                String dateString = repository.gw.helpers.DateUtils.dateFormatAsString("yyMMddHHmmss", new Date());
                companyName = companyName + "-" + dateString;
                if (companyName.length() >= 30) {
                    companyName = companyName.substring(0, 28);
                }
            }
        }

        if (this.addresses.size() < 1) {
            addresses.add(new AddressInfo(true));
        }

        if (membershipType == null) {
            membershipType = repository.gw.enums.ContactMembershipType.Other;
        }

        this.acctNumberNeeded = acctNumberNeeded;

        repository.gw.login.Login lp = new Login(driver);
        lp.login(this.creator.getUserName(), this.creator.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
    }

    private void fillOutContactBasics() throws Exception {
        ContactDetailsBasicsAB newContact = new ContactDetailsBasicsAB(driver);
        
        /*if(this.firstName != null || this.firstName.equals("")) {
        	newContact.setContactDetailsBasicsFirstName(this.firstName);
        }*/

        if (this.tin != null) {
            newContact.setContactDetailsBasicsTIN(this.tin);
        }

        if (this.ssn != null) {
            newContact.setContactDetailsBasicsSSN(this.ssn);
        }

        if (this.roles.size() > 0) {
            for (repository.gw.enums.ContactRole role : this.roles) {
                newContact.addContactDetailsBasicsRole(role);
                switch (role) {
                    case Vendor: //Need to add code to add vendor number and Type.
                        newContact.setVendorType("Other");
                        newContact.clickGenerateVendorNumber();
                        break;
                case Lienholder : this.lienNumber = (this.withGeneratedLienNumber) ? newContact.clickGenerateLienNumber() : "";
                				break;

                    default:

                        break;

                }
            }
        }

        newContact = new ContactDetailsBasicsAB(driver);

        if (this.emailMain != null) {
            newContact.setMainEmail(this.emailMain.getAddress());
        }
        if (this.emailAlternate != null) {
            newContact.setAltEmail(this.emailAlternate.getAddress());
        }

        if (this.dateOfBirth != null) {
            newContact.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", this.dateOfBirth));
        }

        if (importLastNameOrCompanyName == null && importFirstName == null) {

            // if(numberingTable){ end if line 235 ish
            // TODO: Implement if Statement to ascertain if user is of the
            // Numbering group.
            // If so use addAddress object, if not use newContact Object.
        	// I should make better notes.
            ContactDetailsAddressesAB addAddress = new ContactDetailsAddressesAB(driver);

            if (creator.getUserDepartment().equals("Policy Services") || creator.getUserTitle().contains("Guidewire Service")) {
                addAddress = new ContactDetailsAddressesAB(driver);
                newContact = new ContactDetailsBasicsAB(driver);
                newContact.clickContactDetailsBasicsAddressLink();
                // addAddress.setNewContactBasicsLocationNumber("" +
                // this.primaryAddress.getNumber());

                addAddress.addAddresses(this.addresses);
                PageLinks tabLinks = new PageLinks(driver);
                tabLinks.clickContactDetailsBasicsLink();
            } else {

                ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
                basicsPage.setAddresses(this.addresses);
            }


            // newContact.setNewContactDetailsBasicsCountry(primaryAddress.getCountry());
            // TODO: Look into how PolicyCenter has countries because they may vary
            // ... for right now we assume USA.

        } else {
//			newContact.clickNewContactDetailsBasicsAddressLink();
            newContact.importAddresses(importLastNameOrCompanyName, importFirstName, importAddressLine1);  
            newContact.clickContactDetailsBasicsEditLink();
        }
        if (this.accountNumber == null && this.acctNumberNeeded) {
            newContact = new ContactDetailsBasicsAB(driver);
            this.accountNumber = newContact.clickContactDetailsBasicsAccountNumberGenerateLink();
        } else if ((!(accountNumber == null)) && accountNumber.matches("\\d")) {
            newContact = new ContactDetailsBasicsAB(driver);
            newContact.setContactDetailsBasicsAccountNumber(this.accountNumber);
        }

        /*
         * if(this.efts.size() > 0) { for (ContactEFTData eft : this.efts) {
         * newContact.clickNewContactDetailsBasicsEFTAddButton();
         * newContact.setNewContactDetailsBasicsEFTRow(eft); //TODO: Add this
         * method to the INewContactBasics stuff to be able to actually call it
         * } }
         */

        newContact.setMembershipType(this.membershipType);
/*        if (this.accountNumber == null && this.acctNumberNeeded) {
            this.accountNumber = newContact.clickNewContactDetailsBasicsGenerateLink();
        }
*/
        if (agent != null) {
            newContact.clickContactDetailsBasicsAgentSearchLink();
            newContact.setAgent(this.agent.getAgentNum(),
                    this.agent.getAgentFirstName() + " " + this.agent.getAgentLastName());
        }
        if (dba != null && dba.size()>0) {
            newContact.clickContactDetailsBasicsDBAsLink();
            ContactDetailsDBA dbaPage = new ContactDetailsDBA(driver);
            for (DBA myDba : this.dba) {
                dbaPage.addDBA(myDba.getDBA());
            }
            dbaPage = new ContactDetailsDBA(driver);
            dbaPage.clickContactDetailsBasicLink();
        }

        if (typeToGenerate.equals(repository.gw.enums.GenerateContactType.Person) && duesYears != null) {
            ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
            basicsPage.clickContactDetailsBasicsMembershipOnlyYes();
            basicsPage.clickContactDetailsBasicsPaidDuesLink();
            ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
            for (Date duesDate : duesYears) {
                duesPage = new ContactDetailsPaidDuesAB(driver);
                duesPage.addDues(duesDate, CountyIdaho.Bannock, "Paid");
            }
        }

        // newContact.setNewContactBasicsWebsite(this.website.toString());
        // TODO: Add this method to the INewContactBasics stuff to be able to
        // actually call it

    }

    private void person(boolean isPerson, Builder builderStuff) throws Exception {
    	String[] nameArray = new String[3];
        checkParametersLoginAndGetToSearch(isPerson, builderStuff);

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
        if(builderStuff.unique) {
        	nameArray = new StringsUtils().getUniqueName(builderStuff.firstName,builderStuff.middleName, builderStuff.lastName);
        	this.lastName = nameArray[2];
        	this.middleName = nameArray[1];
        	this.firstName = nameArray[0];
        }
        search.searchAndCreateNewPerson(this.lastName);
        
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        if(this.firstName != null || this.firstName.equals("")) {
        	basicsPage.setContactDetailsBasicsFirstName(this.firstName);
        }
        
        fillOutContactBasics();

        ContactDetailsBasicsAB newContact = new ContactDetailsBasicsAB(driver);
        newContact.clickUpdate();
        //This handles AddressStandardization in ContactManager.
        repository.gw.errorhandling.ErrorHandling resultsPopup = new repository.gw.errorhandling.ErrorHandling(driver);
        if (resultsPopup.checkIfValidationResultsExists()) {
            resultsPopup.button_Clear().click();
            newContact = new ContactDetailsBasicsAB(driver);
            newContact.clickUpdate();
        }
        new GuidewireHelpers(driver).waitForPostBack();
        new GuidewireHelpers(driver).waitForPostBack();
//        new GuidewireHelpers(driver).logout();
    }

    private void company(boolean isPerson, Builder builderStuff) throws Exception {

        checkParametersLoginAndGetToSearch(isPerson, builderStuff);

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
        search.searchAndCreateNewCompany(this.companyName);
        ContactDetailsBasicsAB newContact = new ContactDetailsBasicsAB(driver);

        fillOutContactBasics();

        newContact = new ContactDetailsBasicsAB(driver);
        newContact.clickUpdate();

        //This handles AddressStandardization in ContactManager.
        repository.gw.errorhandling.ErrorHandling resultsPopup = new ErrorHandling(driver);
        if (resultsPopup.checkIfValidationResultsExists()) {
            resultsPopup.button_Clear().click();
            newContact = new ContactDetailsBasicsAB(driver);
            newContact.clickUpdate();
        }

//		AddressStandardizationError AddressStandardizationPage = AddressStandardizationErrorFactory
//				.getAddressStandardizationErrorPage();
//		boolean AddressStandardizePage = AddressStandardizationPage
//				.checkAddressStandardizationErrorOverrideLinkExists();
//		if (AddressStandardizePage) {
//			newContact = ContactFactory.getNewContactPage();
//			newContact.clickNewContactDetailsBasicsUpdateButton();
//		}
        new GuidewireHelpers(driver).waitForPostBack();
        new GuidewireHelpers(driver).waitForPostBack();
//        new GuidewireHelpers(driver).logout();
    }

    public static class Builder {

        public ArrayList<DBA> dba;
        private String acctNum;
        private AbUsers creator = null;
        private String firstName = "New";
        private String middleName = null;
        private String lastName = "Contact";
        private repository.gw.enums.PersonSuffix suffixName = repository.gw.enums.PersonSuffix.None;
        private String companyName = null;
        private String tin = null;
        private String ssn = null;
        private ArrayList<repository.gw.enums.ContactRole> roles = new ArrayList<repository.gw.enums.ContactRole>();
        private InternetAddress emailMain = null;
        private InternetAddress emailAlternate = null;
        private AddressInfo primaryAddress = null;
        private ArrayList<ContactEFTData> efts = new ArrayList<ContactEFTData>();
        private repository.gw.enums.ContactMembershipType membershipType = null;
        private Agents agent = null;
        private URL website = null;
        private String importFirstName;
        private String importMiddleName;
        private String importLastNameOrCompanyName;
        private repository.gw.enums.PersonSuffix importSuffixName = repository.gw.enums.PersonSuffix.None;
        private String importAddressLine1;
        private ArrayList<Date> duesYears;
        private boolean generateAccountNumber;
        private boolean unique;
        private ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
        private String alternateName;
        private String formerName;
        private Gender gender;
        private Date dateOfBirth;
        private boolean withMembershipOnly;
        private String driversLicense;
        private State driversLicenseState;
        private String comments;
        private boolean membershipOnly;
        private boolean withGeneratedLienNumber;
        private boolean withTimeStamp;
        private repository.gw.enums.VendorType vendorType;
        private String vendorNumber;
        private WebDriver webDriver;

        public Builder(WebDriver driver) {
            this.webDriver = driver;
        }

        public Builder withCreator(AbUsers _creator) {
            this.creator = _creator;
            return this;
        }

        public Builder withFirstLastName(String firstName, String middleName, String lastName, repository.gw.enums.PersonSuffix suffixName) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.suffixName = suffixName;
            return this;
        }

        public Builder withFirstLastName(String firstName, String middleName, String lastName) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            return this;
        }

        public Builder withFirstLastName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }

        public Builder withCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder withGeneratedLienNumber(boolean _withGeneratedLienNumber) {
        	this.withGeneratedLienNumber = _withGeneratedLienNumber;
        	return this;
        }
        
        public Builder withTimeStamp(boolean _withTimeStamp) {
        	this.withTimeStamp = _withTimeStamp;
        	return this;
        }

        public Builder withAlternateName(String altName) {
            this.alternateName = altName;
            return this;
        }

        public Builder withFormerName(String formerName) {
            this.formerName = formerName;
            return this;
        }

        public Builder withTIN(String tin) {
            this.tin = tin;
            return this;
        }

        public Builder withSSN(String ssn) {
            this.ssn = ssn;
            return this;
        }

        public Builder withRoles(ArrayList<ContactRole> roles) {
            this.roles = roles;
            return this;
        }

        public Builder withEmailMain(String email) throws AddressException {
            this.emailMain = new InternetAddress(email);
            return this;
        }

        public Builder withEmailAlternate(String email) throws AddressException {
            this.emailAlternate = new InternetAddress(email);
            return this;
        }

        public Builder withPrimaryAddress(AddressInfo primaryAddress) {
            this.addresses = new ArrayList<AddressInfo>();
            this.addresses.add(primaryAddress);
            return this;
        }

        public Builder withAddresses(ArrayList<AddressInfo> _addresses) {
            this.addresses = _addresses;
            return this;
        }

        public Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withDOB(Date dob) {
            this.dateOfBirth = dob;
            return this;
        }

        public Builder withEFTs(ArrayList<ContactEFTData> efts) {
            this.efts = efts;
            return this;
        }

        public Builder withAccountNumber(String _accountNumber) {
            this.acctNum = _accountNumber;
            return this;
        }

        public Builder withGenerateAccountNumber(boolean _accountNumber) {
            this.generateAccountNumber = _accountNumber;
            return this;
        }

        public Builder withDuesYears(ArrayList<Date> _years) {
            this.duesYears = _years;
            return this;
        }

        public Builder withMembershipType(ContactMembershipType membershipType) {
            this.membershipType = membershipType;
            return this;
        }


        public Builder withMembershipOnly(boolean membershipOnly) {
            this.membershipOnly = membershipOnly;
            return this;
        }

        public Builder withAgent(String agentUserName) throws Exception {
            this.agent = AgentsHelper.getAgentByUserName(agentUserName);
            return this;
        }

        public Builder withWebsite(String website) throws MalformedURLException {
            this.website = new URL(website);
            return this;
        }

        public Builder withDL(String dlNumber, State state) {
            this.driversLicense = dlNumber;
            this.driversLicenseState = state;
            return this;
        }

        public Builder withImportContact(String _firstName, String _middleName, String _lastName, PersonSuffix _suffixName,
                                         String _addressLine1) {
            this.importFirstName = _firstName;
            this.importMiddleName = _middleName;
            this.importLastNameOrCompanyName = _lastName;
            this.importSuffixName = _suffixName;
            this.importAddressLine1 = _addressLine1;
            return this;
        }

        public Builder withImportContact(String _firstName, String _middleName, String _lastName,
                                         String _addressLine1) {
            this.importFirstName = _firstName;
            this.importMiddleName = _middleName;
            this.importLastNameOrCompanyName = _lastName;
            this.importAddressLine1 = _addressLine1;
            return this;
        }

        public Builder withImportContact(String _importCompanyName, String _addressLine1) {
            this.importLastNameOrCompanyName = _importCompanyName;
            this.importAddressLine1 = _addressLine1;
            return this;
        }

        public Builder withDba(ArrayList<DBA> _dba) {
            this.dba = _dba;
            return this;

        }

        public Builder withUniqueName(boolean unique) {
            this.unique = unique;
            return this;
        }

        public Builder withVendorNumber(String vendorNumber) {
            this.vendorNumber = vendorNumber;
            return this;
        }

        public Builder withVendorType(VendorType type) {
            this.vendorType = type;
            return this;
        }

        public Builder withComments(String comments) {
            this.comments = comments;
            return this;
        }

        public GenerateContact build(GenerateContactType typeToGenerate) throws Exception {
            return new GenerateContact(typeToGenerate, this);
        }

    }

}
