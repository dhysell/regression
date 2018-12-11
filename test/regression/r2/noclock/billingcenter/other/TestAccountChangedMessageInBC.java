package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchMenu;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueueStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
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
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * Test is not executing it due to DE4351: Invalid Quote message for BOP Policy Issuance
 */

/**
 * @Author nvadlamudi
 * @Requirement :DE4115: Null designated address in account changed message
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 11, 2016
 */
public class TestAccountChangedMessageInBC extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObject;
    private Underwriters uw;
    private String BCAccountAddress1, BCAccountAddress2;

    @Test()
    private void testIssueBOP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine()
                .withInsFirstLastName("Test", "BOP")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueBOP"})
    private void testIssuePLPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
		coverages.setUnderinsured(false);
		coverages.setUninsuredLimit(UninsuredLimit.CSL300K);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		person.setFirstName("Test"+StringsUtils.generateRandomNumberDigits(8));
		person.setLastName("Country");
		driversList.add(person);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setComprehensive(true);
		toAdd.setCostNew(10000.00);
		toAdd.setCollision(true);
		toAdd.setDriverPL(person);
		toAdd.setAdditionalLivingExpense(true);
		vehicleList.add(toAdd);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);
		squirePersonalAuto.setPrimaryInsuredAsDriver(false);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, person.getFirstName(), person.getLastName(),
                AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }});

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myProperty;

        myPolicyObject.squire = mySquire;
        myPolicyObject.lineSelection.add(LineSelection.PropertyAndLiabilityLinePL);
        myPolicyObject.lineSelection.add(LineSelection.PersonalAutoLinePL);
        myPolicyObject.addLineOfBusiness(ProductLineType.Squire, GeneratePolicyType.PolicySubmitted);

        //		sqPolObj = new GeneratePolicy.Builder(driver)
        //				.withSquire(mySquire)
        //				.withProductType(ProductLineType.Squire)
        //				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
        //				.withInsPersonOrCompany(ContactSubType.Company)
        //				.withBOPPolicyUsedForSquire(bopPolicyObj,true)
        //				.withInsPrimaryAddress(insuredAddress)
        //				.withPaymentPlanType(PaymentPlanType.Annual)
        //				.withDownPaymentType(PaymentType.Cash)
        //				.build(GeneratePolicyType.PolicySubmitted);
    }

    @Test(dependsOnMethods = {"testIssuePLPolicy"})
    private void testSquireIssueWithChangedAddress() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickActivitySubject("Submitted Full Application");

        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        activityPopupPage.clickCloseWorkSheet();

        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyCoverages();

		sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickPolicyInfoPrimaryNamedInsured();
        AddressInfo newAddress = new AddressInfo();
		newAddress.setLine1("123 N Main St");
		newAddress.setCity("Pocatello");
		newAddress.setState(State.Idaho);
		newAddress.setZip("83201");
		newAddress.setType(AddressType.Home);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListingIfNotExist(newAddress);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.setReasonForContactChange("Testing purpose Added");
		householdMember.clickUpdate();


        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        //Handles prequote issues for PL while they work on the
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
			risk.approveAll_IncludingSpecial();
			qualificationPage.clickQuote();
		}

        sidemenu.clickSideMenuQuote();
		quote.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

		new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testSquireIssueWithChangedAddress"})
    public void checkEventMessages() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
		new Login(driver).login("su", "gw");


        TopMenuAdministrationPC administrationMenu = new TopMenuAdministrationPC(driver);
		administrationMenu.clickMessageQueues();

        AdminEventMessages messageQueues = new AdminEventMessages(driver);
		if (messageQueues.checkMessageQueueStatus(MessageQueue.Billing_System, MessageQueueStatus.Started)) {
			int failedMessageCount = messageQueues.getMessageQueueColumnCount(MessageQueue.Billing_System, "Failed");
			if (failedMessageCount > 0) {
				Assert.fail("There was an error in the messaging queues for PAS. Test Failed.");
			}
		} else {
			messageQueues.resumeQueue(MessageQueue.Billing_System);
			driver.navigate().refresh();
			int failedMessageCount = messageQueues.getMessageQueueColumnCount(MessageQueue.Billing_System, "Failed");
			if (failedMessageCount > 0) {
				Assert.fail("There was an error in the messaging queues for PAS. Test Failed.");
			}
		}

        new GuidewireHelpers(driver).logout();
	}

    @Test(dependsOnMethods = {"checkEventMessages"})
    private void testGetMailingAddressFromBC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObject.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuSummary();
        BCAccountSummary summary = new BCAccountSummary(driver);
        BCAccountAddress1 = summary.getBillingAddress();

        BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickSearchTab();

        BCSearchMenu searchMenu = new BCSearchMenu(driver);
        searchMenu.clickSearchMenuAccounts();


        BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
        accountSearchBC.setBCSearchAccountsAccountNumber(myPolicyObject.accountNumber);
        accountSearchBC.clickSearch();
        accountSearchBC.clickAccountNumberInRow(2);
        acctMenu.clickBCMenuSummary();
        BCAccountAddress2 = summary.getBillingAddress();
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"testGetMailingAddressFromBC"})
    private void testValidateBCAddressWithPCMailingAddress() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObject.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        if (!acctSummary.getAddress().contains(BCAccountAddress1)) {
            Assert.fail("Unexpected Mailing address : " + BCAccountAddress1 + "in BC for Policy Number : " + this.myPolicyObject.busOwnLine.getPolicyNumber());
        }
        if (!acctSummary.getAddress().contains(BCAccountAddress2)) {
            Assert.fail("Unexpected Mailing address : " + BCAccountAddress2 + "in BC for Policy Number : " + this.myPolicyObject.squire.getPolicyNumber());
        }
    }

}
