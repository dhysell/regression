package regression.r2.noclock.policycenter.other;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderMatchingContacts;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import regression.r2.noclock.contactmanager.search.SearchNumber;

/**
 * @Author sbroderick
 * @Requirement : A form of ID is now required
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20PolicyCenter%20Common%20-%20ClientContacts%20-%20Contacts%20Within%20a%20Policy%20Transaction.xlsm">PC8 - PolicyCenter Common - ClientContacts - Contacts Within a Policy Transaction</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Common%20Product%20Model%20Requirements%20for%20PL%20and%20CL.xlsx">Common Product Model Requirements for PL and CL</a>
 * @Description
 * @DATE May 15, 2017
 * <p>
 * Acceptance Criteria:
 * Ensure that requirements under the title "Create Account Screen - Company Type Contact" are working properly
 * Ensure that requirements under the title "Create Account Screen - Company Type Contact - Block Screen Validation Message" are working properly
 * Ensure that requirements under the title "Create Account Screen - Person Type Contact" are working properly
 * Ensure that requirements under the title "Create Account Screen - Person Type Contact - Block Screen Validation Message" are working properly
 * Ensure that requirements under the title "Contact Details screen - Company Type Contact" are working properly
 * Ensure that requirements under the title "Contact Details screen - Person Type Contact" are working properly
 * Ensure that requirements under the title "Contact Details screen - EditContactInfo permission - Home Office" are working properly
 * Ensure that requirements under the title "Contact Details screen - COEditPNIInfo permission - County Office" are working properly
 * Ensure that requirements under the title "Contact Details screen - Criteria for Exact and Potential Match - For any create contact screen" are working properly
 * Ensure that requirements under the title "Contact Details screen - Matching Contacts Screen for Company" are working properly
 * Ensure that requirements under the title "Contact Details screen - Matching Contacts Screen for Person"
 * Ensure that requirements under the title "Contact Details screen - Matching Contacts Screen for Person - Existing Informational Message" are working properly
 * Ensure that requirements under the title "Contact Details screen - Matching Contacts Screen for Person - Existing Warning Message" are working properly
 * Ensure that requirements under the title "Contact Details screen - Alternate ID" are working properly
 * Ensure that requirements under the title "Contact Details screen - Adding a SSN on Existing Contact That Already Exists in AB - Person Type Contact" are working properly
 */
@QuarantineClass
public class CreateAccount extends BaseTest {

    private CreateNew createNew = CreateNew.Create_New_Always;
    private ContactSubType insPersonOrCompany = ContactSubType.Person;
    private String companyName = "Demis Pick and Pull";
    private String insFirstName = "Demi";
    private String insMiddleName = "Vonae";
    private String insLastName = "Broderick";
    private AddressInfo address = new AddressInfo();
    //	private String tin = "312978465";
    private String ssn = "312978465";
    //	private String alternateID = "312465987";
    private Date insDOB;
    //	private Object taxIDNumber;
    private AdvancedSearchResults searchResults = null;

    private WebDriver driver;

    public void loginAndStartNewSubmission() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        System.out.println("loginAndStartNewSubmission");
        Agents agent = AgentsHelper.getRandomAgent();
        Login login = new Login(driver);
        login.login(agent.getAgentUserName(), agent.getAgentPassword());
        if (new Login(driver).accountLocked()) {
            agent = AgentsHelper.getRandomAgent();
            login.login(agent.getAgentUserName(), agent.getAgentPassword());
        }
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);

        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, createNew, insPersonOrCompany, insLastName, insFirstName, insMiddleName, companyName, address.getCity(), address.getState(), address.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        if (insPersonOrCompany.equals(ContactSubType.Person)) {
            if (this.insMiddleName != null) {
                createAccountPage.setPersonMiddleName(this.insMiddleName);
            }
        }
        insDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -29);
        // DOB is now a required field when creating an account
        if (this.insPersonOrCompany.equals(ContactSubType.Person)) {
            createAccountPage.setDOB(this.insDOB);
        }

        createAccountPage.fillOutPrimaryAddressFields(this.address);
    }

    public void setSensitiveInformation(String tin, String ssn, String altID) {
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);

        if (tin != null && tin.length() > 8) {
            createAccountPage = new SubmissionCreateAccount(driver);
            if (ssn != null && insPersonOrCompany == ContactSubType.Person) {
                createAccountPage.setSubmissionCreateAccountBasicsSSN(ssn);
            }

            if (tin != null && insPersonOrCompany == ContactSubType.Company) {
                createAccountPage.setSubmissionCreateAccountBasicsTIN(tin);
            }
        }

        if (ssn != null && ssn.length() > 8) {
            createAccountPage = new SubmissionCreateAccount(driver);
            if (ssn != null && insPersonOrCompany == ContactSubType.Person) {
                createAccountPage.setSubmissionCreateAccountBasicsSSN(ssn);
            }

            if (tin != null && insPersonOrCompany == ContactSubType.Company) {
                createAccountPage.setSubmissionCreateAccountBasicsTIN(tin);
            }
        }

        if (altID != null) {
            createAccountPage.setSubmissionCreateAccountBasicsAltID(altID);
        }
    }

    public void clickUpdate() throws Exception {
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.clickSubmissionCreateAccountUpdate();
        ErrorHandlingHelpers errorHandlingHelpers = new ErrorHandlingHelpers(driver);
        if (errorHandlingHelpers.errorExists()) {
            if (errorHandlingHelpers.getErrorMessage().contains("A contact with the specified TIN already exists")) {
                createAccountPage.clickSubmissionCreateAccountUpdate();
            }
        }

        if (createAccountPage.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            createAccountPage.getSubmissionCreateAccountAddressNotFound();
            createAccountPage.updateAddressStandardization();
        }
        if (!createAccountPage.finds(By.xpath("//*[contains(text(), 'An error has occurred while standardizing this address')]")).isEmpty()) {
            Assert.fail("ERROR WITH ADDRESS STANDARDIZATION");
        }
    }

    public void getAbPersonContact(String contactLastName) throws Exception {

        SearchNumber search = new SearchNumber();
        driver = search.getToSearch();
        AdvancedSearchAB searchContact = new AdvancedSearchAB(driver);
        //need to set search results as class properties.
        this.searchResults = searchContact.getSearchResultWithSSN(contactLastName);
        this.insPersonOrCompany = ContactSubType.Person;
        this.insFirstName = searchResults.getFirstName();
        this.insLastName = searchResults.getLastNameOrCompanyName();
        this.insMiddleName = searchResults.getMiddleName();
        this.address = searchResults.getAddress();

        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
        String ssn = basics.getSsn();
        this.ssn = ssn;
    }

    public void verifyMessages(String validationMessage) {
        GenericWorkorderMatchingContacts matchingContactsPage = new GenericWorkorderMatchingContacts(driver);
        ArrayList<String> matchingPageMessages = matchingContactsPage.getMessages();

        for (String message : matchingPageMessages) {
            if (message.equals(validationMessage)) {

            }
        }
    }

    @Test
    public void testSSNMatch() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.ContactManager);
        System.out.println("Starting test: testSSNMatch");
        SearchNumber search = new SearchNumber();
        search.getToSearch();
        AdvancedSearchAB searchContact = new AdvancedSearchAB(driver);
        //need to set search results as class properties.
        this.searchResults = searchContact.getSearchResultWithSSN("Hofman");
        this.insPersonOrCompany = ContactSubType.Person;
        this.insFirstName = searchResults.getFirstName();
        this.insLastName = searchResults.getLastNameOrCompanyName();
        this.insMiddleName = searchResults.getMiddleName();
        this.address = searchResults.getAddress();

        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
        String ssn = basics.getSsn();
        this.ssn = ssn;
//		this.tin = null;
//		this.alternateID = null;
        loginAndStartNewSubmission();
        setSensitiveInformation(null, this.ssn, null);
        clickUpdate();
        verifyMessages("SSN");

        GenericWorkorderMatchingContacts matchingContactsPage = new GenericWorkorderMatchingContacts(driver);
        if (matchingContactsPage.exactMatch(this.ssn.substring(7)) < 1) {
            Assert.fail("There should be at least one exact match, please check the search results.");
        }
        matchingContactsPage = new GenericWorkorderMatchingContacts(driver);
        matchingContactsPage.clickReturnToCreateAccount();

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.clickCreateAccountUpdate();

        matchingContactsPage = new GenericWorkorderMatchingContacts(driver);
        if (matchingContactsPage.exactMatch(this.ssn.substring(7)) < 1) {
            Assert.fail("There should be at least one exact match, please check the search results.");
        }

    }

    @Test
    public void ssnTinAltIdRequired() throws Exception {
        System.out.println("Starting test: ssnTinAltIdRequired");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        this.insPersonOrCompany = ContactSubType.Person;
        loginAndStartNewSubmission();
        clickUpdate();
        ErrorHandling errorHandling = new ErrorHandling(driver);
        List<WebElement> errors = errorHandling.getValidationMessages();
        boolean found = false;
        for (WebElement error : errors) {
            if (error.getText().contains("At least one of the following fields has to be filled out: SSN, TIN, or Alternate ID")) {
                found = true;
            }
        }
        if (!found) {
            Assert.fail("An error message asking for an SSN, TIN, or alt ID should sport the page.");
        }
    }

    @Test(description = "Test Company business rules.")
    public void companyCreateAccount() throws Exception {
        System.out.println("Starting test: companyCreateAccount");
        this.insPersonOrCompany = ContactSubType.Company;
        loginAndStartNewSubmission();
        clickUpdate();
        ErrorHandling errorHandling = new ErrorHandling(driver);
        List<WebElement> errors = errorHandling.getValidationMessages();
        boolean found = false;
        for (WebElement error : errors) {
            if (error.getText().contains("At least one of the following fields has to be filled out: TIN or Alternate ID")) {
                found = true;
            }
        }
        if (!found) {
            Assert.fail("An error message asking for an TIN, or alt ID should sport the page.");
        }
    }


    @Test
    public void testANI() throws Exception {
        System.out.println("Starting testANI");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(1);
        resPrem.setPlNumResidence(1);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myCountryPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Ani", "Rules")
                .build(GeneratePolicyType.QuickQuote);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchJob(myCountryPolicyObjPL.agentInfo.getAgentUserName(), myCountryPolicyObjPL.agentInfo.getAgentPassword(), myCountryPolicyObjPL.accountNumber);
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuPolicyInfo();
        new GuidewireHelpers(driver).editPolicyTransaction();
        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).clicknPolicyInfoAdditionalNamedInsuredsSearch();
        PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person);
        ani.setNewContact(CreateNew.Create_New_Always);
        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).createAniNoID(true, ani);
        String message = new GenericWorkorder(driver).getValidationResults();
        if (!message.equals("At least one of the following fields has to be filled out: SSN, TIN, or Alternate ID.")) {
            Assert.fail("Check the ANI Message for no SSN, TIN or Alt ID.");
        }
    }
}
