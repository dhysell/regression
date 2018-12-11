package regression.r2.noclock.policycenter.rolespermissions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.NamesHelper;

@QuarantineClass
public class DuplicateContacts extends BaseTest {

    private WebDriver driver;

    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description verify that the correct warning appear when trying to create
     * a contact with a duplicate SSN/TIN of antoher Person/Company
     * The first time this test is run after an environment reset
     * the test will fail due to that SSN/TIN is not in use yet.
     * @DATE Dec 1, 2015
     */

    // verify that there are contact in Contact Manager with the test SSN/TIN
    @Test()
    public void verifyContactsWithSSNTIN() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        new Login(driver).login("kharrild", "gw");
        TopMenuAB topmenu = new TopMenuAB(driver);
        topmenu.clickSearchTab();

        AdvancedSearchAB advSearch = new AdvancedSearchAB(driver);
        advSearch.tinSearch(ContactSubType.Person, "285105825");
        if (advSearch.getSearchResults("999").size() < 0) {
            System.out.println("CREATE PERSON CONTACT WITH TIN");
        }

        advSearch.tinSearch(ContactSubType.Company, "285105825");
        if (advSearch.getSearchResults("999").size() < 0) {
            System.out.println("CREATE COMANY CONTACT WITH TIN");
        }

        advSearch.ssnSearch("285105825");
        if (advSearch.getSearchResults("999").size() < 0) {
            System.out.println("CREATE PERSON CONTACT WITH SSN");
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test()
    public void duplicatePersonSSN() throws Exception {
        System.out.println("Verify that User cannot create account with an existing SSN/TIN. \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Agents newAgent = AgentsHelper.getRandomAgent();
        Names newName = NamesHelper.getRandomName();
        AddressInfo newAddress = new AddressInfo(true);

        Login login = new Login(driver);
        login.login(newAgent.getAgentUserName(), newAgent.getAgentPassword());

        // TEST DUPLICATE PERSON WITH SAME SSN
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, newName.getLastName(), newName.getFirstName(), null, newName.getCompanyName(), newAddress.getCity(), newAddress.getState(), newAddress.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);

        createAccountPage.setSubmissionCreateAccountBasicsSSN("285105825");

        SubmissionCreateAccount createAccountPage2 = new SubmissionCreateAccount(driver);
        createAccountPage2.fillOutPrimaryAddressFields(newAddress);
        createAccountPage2.clickSubmissionCreateAccountUpdate();

        if (createAccountPage2.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            createAccountPage2.getSubmissionCreateAccountAddressNotFound();
            createAccountPage2.updateAddressStandardization();
        }

        if (createAccountPage2.finds(By.xpath("//div[contains(text(), 'A contact with the specified SSN/TIN already exists in ContactManager.  Select it below or contact Numbering if this is in error.')]")).size() <= 0) {
            Assert.fail(driver.getCurrentUrl() + "Did not get error message/screen that indicates the same SSN/TIN is already in use.");
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test()
    public void duplicatePersonTIN() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Agents newAgent = AgentsHelper.getRandomAgent();
        Names newName = NamesHelper.getRandomName();
        AddressInfo newAddress = new AddressInfo(true);

        Login login = new Login(driver);
        login.login(newAgent.getAgentUserName(), newAgent.getAgentPassword());

        // TEST DUPLICATE PERSON WITH SAME TIN
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always,
                ContactSubType.Person, newName.getLastName(), newName.getFirstName(), null, newName.getCompanyName(),
                newAddress.getCity(), newAddress.getState(), newAddress.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);

        createAccountPage.setSubmissionCreateAccountBasicsTIN("285105825");

        SubmissionCreateAccount createAccountPage2 = new SubmissionCreateAccount(driver);
        createAccountPage2.fillOutPrimaryAddressFields(newAddress);
        createAccountPage2.clickSubmissionCreateAccountUpdate();

        if (createAccountPage2.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            createAccountPage2.getSubmissionCreateAccountAddressNotFound();
            createAccountPage2.updateAddressStandardization();
        }

        if (createAccountPage2.finds(By.xpath("//div[contains(text(), 'A contact with the specified TIN already exists in ContactManager. Please contact Numbering if this is in error.')]")).size() <= 0) {
            Assert.fail(driver.getCurrentUrl() + "Did not get error message/screen that indicates the same SSN/TIN is already in use.");
        }

        new GuidewireHelpers(driver).logout();

    }

    @Test()
    public void duplicateCompanyTIN() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Agents newAgent = AgentsHelper.getRandomAgent();
        Names newName = NamesHelper.getRandomName();
        AddressInfo newAddress = new AddressInfo(true);

        Login login = new Login(driver);
        login.login(newAgent.getAgentUserName(), newAgent.getAgentPassword());

        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always,
                ContactSubType.Company, newName.getLastName(), newName.getFirstName(), null, newName.getCompanyName(),
                newAddress.getCity(), newAddress.getState(), newAddress.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);

        createAccountPage.setSubmissionCreateAccountBasicsTIN("285105825");

        SubmissionCreateAccount createAccountPage2 = new SubmissionCreateAccount(driver);
        createAccountPage2.fillOutPrimaryAddressFields(newAddress);
        createAccountPage2.clickSubmissionCreateAccountUpdate();

        if (createAccountPage2.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            createAccountPage2.getSubmissionCreateAccountAddressNotFound();
            createAccountPage2.updateAddressStandardization();
        }

        if (createAccountPage2.finds(By.xpath("//div[contains(text(), 'A contact with the specified TIN already exists in ContactManager. Please contact Numbering if this is in error.')]")).size() <= 0) {
            Assert.fail(driver.getCurrentUrl() + "Did not get error message/screen that indicates the same SSN/TIN is already in use.");
        }

        new GuidewireHelpers(driver).logout();

    }

}
