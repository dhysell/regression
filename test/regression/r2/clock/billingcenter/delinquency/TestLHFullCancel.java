package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import repository.gw.enums.GenerateContactType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.generate.GenerateContact;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test verifies that a specific scenario discussed below is resolved.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/188487638856">Rally Defect DE7025</a>
 * @RequirementsLink <a href="https://www.flowdock.com/app/idaho-farm-bureau/bc-production-issues/threads/BQ5zsxKtp1r27Onq5pdl5RQl2AO">Flowdock coversation</a>
 * @Description
 * @DATE Jan 5, 2018
 */
public class TestLHFullCancel extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj = null;
    private Date targetDate = null;
    private ARUsers arUser;
    private String lienholderNumber;

    // ///////////////////
    // Main Test Methods//
    // ///////////////////

    @Test
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
        rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);
        driver.quit();

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        AdditionalInterest additionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));

        additionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_All);
        additionalInterest.setNewContact(repository.gw.enums.CreateNew.Create_New_Only_If_Does_Not_Exist);
        additionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);

        PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        location1Property1.setBuildingAdditionalInterest(additionalInterest);

        locOnePropertyList.add(location1Property1);

        PolicyLocation locationOne = new PolicyLocation(locOnePropertyList);
        locationOne.setPlNumResidence(2);
        locationsList.add(locationOne);

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                .withInsFirstLastName("Non-Pay", "CancelLH")
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
        this.lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyChargesInBC() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
        charges.waitUntilIssuanceChargesArrive();
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"verifyChargesInBC"})
    public void moveClocksAndSendLHDelinquent() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Month, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"moveClocksAndSendLHDelinquent"})
    public void verifyDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();

        this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean delinquencyFound = delinquency.verifyDelinquencyStatus(repository.gw.enums.OpenClosed.Open, this.lienholderNumber, null, targetDate);

        if (!delinquencyFound) {
            Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"verifyDelinquency"})
    public void makeLHPaymentAndVerifyDelinquencyExits() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.lienholderNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
        accountSearch.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuDelinquencies();

        BCCommonDelinquencies accountDelinquencies = new BCCommonDelinquencies(driver);
        boolean delinquencyFound = accountDelinquencies.verifyDelinquencyStatus(repository.gw.enums.OpenClosed.Closed, this.lienholderNumber, null, targetDate);

        if (!delinquencyFound) {
            Assert.fail("The Delinquency did not close as expected.");
        }

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makeLHPaymentAndVerifyDelinquencyExits"})
    public void moveClocksACoupleDays() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);
    }

    @Test(dependsOnMethods = {"moveClocksACoupleDays"})
    public void reversePaymentAndCheckDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.lienholderNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickAccountMenuPayments();

        AccountPayments accountPayments = new AccountPayments(driver);
        accountPayments.reversePaymentAtFault(null,this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), null, null, PaymentReturnedPaymentReason.InsufficientFunds);

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
        accountSearch.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);

        menu = new BCAccountMenu(driver);
        menu.clickBCMenuDelinquencies();

        if (new GuidewireHelpers(driver).isAlertPresent()) {
            String alertText = new GuidewireHelpers(driver).getPopupTextContents();
            if (alertText.contains("Invalid server response.")) {
                Assert.fail("There was an Invalid Server Response when attempting to enter the delinquency page. Test Failed.");
            } else {
                Assert.fail("There was a popup error in BC, but the message was not what was expected. The error was: " + alertText + " . Test failed.");
            }
        }

        new GuidewireHelpers(driver).logout();
    }

}
