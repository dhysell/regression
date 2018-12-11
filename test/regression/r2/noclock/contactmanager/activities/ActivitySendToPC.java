package regression.r2.noclock.contactmanager.activities;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactWorkPlanAB;
import repository.ab.newactivity.SendActivityToPC;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AbActivitySearchFilter;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author sbroderick
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 23, 2016
 */

@QuarantineClass
public class ActivitySendToPC extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObjInsuredOnly = null;
    private AbUsers user;

    @Test
    public void testActivitySentToPC() throws Exception {
        createPolicy();
        sendActivityToPC();
        checkActivityInPC();
    }

    public void createPolicy() throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();

        AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
        locOnePropertyList.add(location1Property1);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        AdditionalInterest loc2Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc2Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        location2Property1.setBuildingAdditionalInterest(loc2Property1AdditionalInterest);
        locTwoPropertyList.add(location2Property1);
        locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));

        SquireLiability liabilitySection = new SquireLiability();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjInsuredOnly = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Test", "Premium")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    public void sendActivityToPC() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.user = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
        logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
        search.searchByAccountNumber(this.myPolicyObjInsuredOnly.accountNumber);

        //Checks for activities from PC
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.clickSidebarContactWorkplanLink();

        ContactWorkPlanAB workPlan = new ContactWorkPlanAB(driver);
        workPlan.checkActivity(AbActivitySearchFilter.AllOpen, "review the membership dues", "Review Contact");
        sideMenu = new SidebarAB(driver);
        sideMenu.sendActivityToPC();

        SendActivityToPC sendPCActivity = new SendActivityToPC(driver);
        sendPCActivity.sendActivityToFirstPolicy("Here is an activity from AB for you!", "Activity from AB");

        testAccountInfo();
    }

    public void checkActivityInPC() throws Exception {
        Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjInsuredOnly.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        if (!summaryPage.checkIfActivityExists("Activity from AB")) {
            Assert.fail("The free form activity, created in AB, should exist on this policy.");
        }
    }

    /**
     * @Author sbroderick
     * @Requirement Add a status field in the accounts tab - Status types are in-force, cancelled
     * Add a cancel date in the accounts tab if the policy is cancelled.
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537d/detail/userstory/74648074284">Rally Story</a>
     * @Description
     * @DATE Dec 23, 2016
     */

   
    public void testAccountInfo() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.user = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
        logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB search = new AdvancedSearchAB(driver);
        search.clickReset();
        search.searchByAccountNumber(this.myPolicyObjInsuredOnly.accountNumber);
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB acctPage = new ContactDetailsAccountAB(driver);
        acctPage.changeStatusToCancelled();

    }
}
