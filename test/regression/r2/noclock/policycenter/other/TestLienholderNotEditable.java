package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderModifiers;

public class TestLienholderNotEditable extends BaseTest {

    //Instance Data
    GeneratePolicy myPolicyBoundObj = null;
    String userName;
    String userPass;
    String accountNumber;
    String additionalInterestName = "D L Evans Bank";
    AddressInfo additionalInterestAddress = new AddressInfo();
    private WebDriver driver;

    @Test(
            enabled = true,
            description = "Set up account with Lienholder D L Evans Bank on it.")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.additionalInterestAddress.setLine1("12 E Bullion St Ste A");
        this.additionalInterestAddress.setCity("Hailey");
        this.additionalInterestAddress.setState(State.valueOfName("Idaho"));
        this.additionalInterestAddress.setZip("83333");

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new PolicyLocationBuilding() {{
                    this.setAdditionalInterestList(
                            new ArrayList<AdditionalInterest>() {
                                private static final long serialVersionUID = 1L;

                                {
						this.add(new AdditionalInterest(additionalInterestName, additionalInterestAddress) {{
//								this.setAdditionalInterestType(AdditionalInterestType.Mortgagee);
							}});
                                        }
                            }); // END BUILDING ADDITIONAL INTREST LIST
                }});
            }
        };
//		locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicyBoundObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolicyBoundObj.agentInfo.getAgentUserName();
        userPass = myPolicyBoundObj.agentInfo.getAgentPassword();
        accountNumber = myPolicyBoundObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @Author bmartin
     * @Requirement 1.4.4.AI.26 - Additional Interest on Buildings
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx">
     * "Quick Quote and Full App"</a>
     * @Description Checks Additional Interest page to make sure Policy, Contact, or Role (PCR) information is not editable.
     * @DATE Feb 8, 2016
     */
    @Test(
            enabled = true,
            dependsOnMethods = {"createPolicy"},
            description = "Checks Additional Interest page to make sure Policy, Contact, or Role (PCR) information is not editable")
    public void testLienholderNotEditable() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(userName, userPass, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuBuildings();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderBuildings myBuildings = new GenericWorkorderBuildings(driver);
        myBuildings.clickBuildingsBuildingEdit(1);

        GenericWorkorderAdditionalInterests myAddInterest = new GenericWorkorderAdditionalInterests(driver);
        myAddInterest.clickBuildingsPropertyAdditionalInterestsLink(additionalInterestName);

        myAddInterest.checkIfLeinHolderPCRIsEditable();
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement 1.4.6.UW - Complete Modifiers - UW View
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx">
     * "Quick Quote and Full App"</a>
     * @Description Checks Modifiers Page while logged in as and Agent and makes sure they can't edit the Justification section under Loss Experience and Additional Risk Elements.
     * @DATE May 6, 2016
     */
    @Test(
            enabled = true,
            dependsOnMethods = {"createPolicy"},
            description = "Checks Modifiers Page while logged in as and Agent and makes sure they can't edit the Justification section under Loss Experience and Additional Risk Elements")
    public void testModifiersNotEditable() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(userName, userPass, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers myModifiers = new GenericWorkorderModifiers(driver);
        myModifiers.checkIfModifiersIsEditable();
    }

}
