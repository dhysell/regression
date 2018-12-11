package previousProgramIncrement.pi2_062818_090518.f97_QuickerQuickQuote_ContactChanges;

import repository.ab.search.AdvancedSearchAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import services.enums.Broker;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import regression.r2.noclock.contactmanager.other.AcctNumber;

import java.util.ArrayList;

/**
 * @Author sbroderick
 * @Requirement Account Numbers will be created in PC not AB
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/232630256744">Move Account # Creation into PC</a>
 * @Description Ensure that the account number is checking Nexus data to see if it already exists
 * Ensure that the account number is checking AB data to see if it already exists
 * Ensure that the account number sequence is continued from what we have
 * @DATE Jul 10, 2018
 */
public class US15517AccountNumberCreation extends BaseTest {
    GeneratePolicy myPolicy = null;
    String brokerCode = null;

    //Ensure that the account Number is checking nexus data for existing accounts
    @Test(dependsOnMethods = {"ensureAccountNumberIsGeneratedFromSeed"})
    public void checkNexusBeforeGeneratingAccountNumber() throws Exception {
        AcctNumber acctTestClass = new AcctNumber();
        String statusCode = acctTestClass.getMemberNumberStatusCode(this.myPolicy.accountNumber, Broker.DEV);
        if (!statusCode.equals("100")) {
            Assert.fail("System Generated Account Numbers should not be found in Nexus.");
        }
    }


    //Ensure that the account Number is being checked for in AB
    @Test(dependsOnMethods = {"ensureAccountNumberIsGeneratedFromSeed"})
    public void checkAccountNumberDoesNotExistInAB() throws Exception {
       
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        cf.setEnv("dev");
        WebDriver driver = buildDriver(cf);

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        ArrayList<AdvancedSearchResults> searchResults = searchMe.searchAccountGetResults(AbUserHelper.getRandomDeptUser("Policy Servic"), myPolicy.accountNumber);
        if (searchResults.size() > 1) {
            Assert.fail("When a contact is created is should receive it's own account number.  Check AB to see if a duplicate account number was received.");
        }
    }

    //Ensure the account Number is greater than the last one generated.
    @Test
    public void ensureAccountNumberIsGeneratedFromSeed() throws Exception {
        generatePolicy();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        cf.setEnv("dev");
        WebDriver driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy newPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("US15518", "Newby")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.QuickQuote);

        driver.quit();

        if (Integer.parseInt(this.myPolicy.accountNumber) >= Integer.parseInt(newPolicy.accountNumber)) {
            Assert.fail("Ensure that " + this.myPolicy.accountNumber + " is less than " + newPolicy.accountNumber + ".");
        }
    }

    @Test
    public void testDE7719GeneratedAccountInAbIsFoundInPC() throws Exception {
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withGenerateAccountNumber(true)
                .build(GenerateContactType.Person);

        driver.quit();
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Agents agent = AgentsHelper.getRandomAgent();
        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        if (new Login(driver).accountLocked()) {
            agent = new Login(driver).loginAsRandomAgent();
        }
        new TopMenuPolicyPC(driver).clickNewSubmission();

        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);

        Assert.assertFalse(searchPC.searchResultsExistWithOtherNameButSameAccountString(myContactObj.accountNumber, myContactObj.firstName, myContactObj.lastName), "There should not be multiple results with the same account number.");
    }

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "dev");
        WebDriver driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("US15518", "Newby")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        driver.quit();
    }


}
