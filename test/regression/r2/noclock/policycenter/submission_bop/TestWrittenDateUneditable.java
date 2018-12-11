package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

public class TestWrittenDateUneditable extends BaseTest {

    //Instance Data
    GeneratePolicy myPolicy = null;
    String userName;
    String password;
    String uwUserName;
    String accountNumber;

    private WebDriver driver;

    @Test(enabled = true,
            description = "Create Policy")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicy = new GeneratePolicy.Builder(driver)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                //Change based on how far the test needs to go
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolicy.agentInfo.getAgentUserName();
        password = myPolicy.agentInfo.getAgentPassword();
        uwUserName = uwUserNames.get(NumberUtils.generateRandomNumberInt(0, (uwUserNames.size() - 1)));
        accountNumber = myPolicy.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("UnderWriter: " + uwUserName);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement 1.4.1.PI.2 - Complete Policy Info
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx">Quick Quote and Full App</a>
     * @Description Check to make sure Under Writer is unable to edit the Written Date. Only UW Supervisors have permission to edit Written Date.
     * @DATE Feb 23, 2016
     */
    @Test(enabled = true,
            description = "Check to make sure Written date is uneditable.",
            dependsOnMethods = {"createPolicy"})
    public void checkWrittenDateUneditable() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(uwUserName, password, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuPolicyInfo();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.checkIfWrittenDateIsEditable();
    }

    List<String> uwUserNames = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("atubbs");
            this.add("wlinscheid");
            this.add("mshepherd");
            this.add("kbailey");
            this.add("pdye");
            this.add("fviolanti");
        }
    };

}
