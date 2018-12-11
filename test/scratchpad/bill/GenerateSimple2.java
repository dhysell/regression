package scratchpad.bill;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateSimple2 extends BaseTest {

    //Instance Data
    private GeneratePolicy myPolicy;
    private StringBuilder userName;
    private StringBuilder userPass;
    private StringBuilder uwUserName;
    private StringBuilder uwPassword;
    private StringBuilder accountNumber;
    private StringBuilder policyNumber;
    //	private ARUsers arUser;
    private String arUsername;
    private String arPassword;

    private WebDriver driver;

    @Test(description = "Create Policy")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locTwoBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));


        this.myPolicy = new GeneratePolicy.Builder(driver)
                .withPolTermLengthDays(112)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                //Change based on how far the test needs to go
                .build(GeneratePolicyType.FullApp);

        userName = new StringBuilder(myPolicy.agentInfo.getAgentUserName());
        userPass = new StringBuilder(myPolicy.agentInfo.getAgentPassword());
        uwUserName = new StringBuilder(myPolicy.underwriterInfo.getUnderwriterUserName());
        uwPassword = new StringBuilder(myPolicy.underwriterInfo.getUnderwriterPassword());
        accountNumber = new StringBuilder(myPolicy.accountNumber);
        policyNumber = new StringBuilder(myPolicy.busOwnLine.getPolicyNumber());

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Under Writer: " + uwUserName);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @Description This will pay Insured's part of the policy off, and is only needed if going past Full Application
     * Set true is being used
     */
    @Test(
            enabled = false,
            description = "Makes Insured Payment on account",
            dependsOnMethods = {"createPolicy"})
    public void makeInsuredDownPayment() throws Exception {

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

        arUsername = arUser.getUserName();
        arPassword = arUser.getPassword();
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUsername, arPassword);

        DesktopActionsMultiplePayment makePayment = new DesktopActionsMultiplePayment(driver);
        makePayment.makeInsuredMultiplePaymentDownpayment(myPolicy, myPolicy.busOwnLine.getPolicyNumber());

    }

    @Test(
            enabled = false,
            description = "Second Part of Test",
            dependsOnMethods = {"createPolicy"})
    public void testName() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName.toString(), userPass.toString(), accountNumber.toString());

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuToolsDocuments();
    }

}
