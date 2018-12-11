package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.RoleAgentChange;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author swathiAkarapu
 * @Requirement US16097
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/249417166448">US16097</a>
 * @Description
 * As all users, including claims as they would like this to help them, I want to see the region code and agency code on the policy info screen to make paying claims, and predictive analytics work, easier.
 *
 * These fields have nothing to do with rating. These are informational only, but should match the agent and the location.  The region is where agent works and the county is where the insured's primary address is.
 *
 * Steps to get there:
 * Have a new submission policy
 * Go to the policy info screen
 * Acceptance criteria:
 * Ensure that agency code and region code are accurate for the underwriting area of the policy
 * Ensure that the agency code and region code fields are not editable
 * Ensure that on an agent change within the county and an agent change outside of the county the fields update with the new, correct information
 *
 * @DATE September 17
 */
public class US16097AgencyAndUWCodeOnPolicyInfo extends BaseTest {

    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("PInfo", "AInfo")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void verifyAgencyAndUWCodes() throws Exception {
        generatePolicy();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        pcSideMenu.clickSideMenuPolicyInfo();

        // Acceptance criteria:
        SoftAssert sa= new SoftAssert();
        sa.assertEquals(polInfo.getAgentOfRecord(), getAgentOfRecord(myPolicyObject.agentInfo), "Agent of record is not matching");
        sa.assertNotNull(polInfo.getCoutyOfRecord(), "County of record present");
        sa.assertAll();
    }

    private String getAgentOfRecord(Agents agentInfo) {
        return new StringBuilder().append(agentInfo.agentPreferredName).append(" (").append(agentInfo.getAgentNum()).append(")").append(" (").append(agentInfo.getAgentRegion()).append(getRegionCode(agentInfo)).append(")").toString();
    }

    private String getRegionCode(Agents agentInfo) {
        return new StringBuilder().append(" 0").append(agentInfo.getAgentRegion().replaceAll("\\D+", "")).append("0").toString();
    }

    @Test(enabled = true)
    public void verifyAgencyAndUWCodesWhenChageAgentWithInCounty() throws Exception {
        generatePolicy();

        RoleAgentChange agentChangeRoleLogin= RoleAgentChange.getRandom();;
        new Login(driver).loginAndSearchPolicyByAccountNumber(agentChangeRoleLogin.getUserName(), agentChangeRoleLogin.getPassword(),
                myPolicyObject.accountNumber);

        Agents agentInfo = AgentsHelper.getRandomAgentFromCounty(myPolicyObject.agentInfo.getAgentCounty());
        while (StringUtils.equals(agentInfo.getAgentNum(), myPolicyObject.agentInfo.getAgentNum())) {
            agentInfo = AgentsHelper.getRandomAgentFromCounty(myPolicyObject.agentInfo.getAgentCounty());
        }

        // start policy change
        GenericWorkorderPolicyInfo polInfo =  new GenericWorkorderPolicyInfo(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change Agent with in County", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
         String countyOfRecordBeforeChange=  polInfo.getCoutyOfRecord();
        polInfo.setAgentOfService(agentInfo);
        policyChangePage.quoteAndIssue();
//        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
//        pcWorkOrder.clickGenericWorkorderQuote(); 
//        pcWorkOrder.clickIssuePolicyButton();
        GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        sideMenu.clickSideMenuPolicyInfo();

        // Acceptance criteria:
        SoftAssert sa= new SoftAssert();
        sa.assertEquals(polInfo.getAgentOfRecord(), getAgentOfRecord(agentInfo), "Agent of record is not matching when Agent Changed with in county");
        sa.assertEquals(polInfo.getCoutyOfRecord(), countyOfRecordBeforeChange, "County of record is changing when  Agent changed with in county");
        sa.assertAll();
    }


    @Test(enabled = true)
    public void verifyAgencyAndUWCodesWhenChageAgentOutSideCounty() throws Exception {
        generatePolicy();

        RoleAgentChange agentChangeRoleLogin= RoleAgentChange.getRandom();;
        new Login(driver).loginAndSearchPolicyByAccountNumber(agentChangeRoleLogin.getUserName(), agentChangeRoleLogin.getPassword(),
                myPolicyObject.accountNumber);

        Agents agentInfo = AgentsHelper.getRandomAgentOutSideCounty(myPolicyObject.agentInfo.getAgentCounty());
        // start policy change
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("change Agent out Side County", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        String countyOfRecordBeforeChange=  polInfo.getCoutyOfRecord();
        polInfo.setAgentOfService(agentInfo);
        policyChangePage.quoteAndIssue();
//        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
//        pcWorkOrder.clickGenericWorkorderQuote();
//        pcWorkOrder.clickIssuePolicyButton();
        GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
        pcCompletePage.clickViewYourPolicy();
        sideMenu.clickSideMenuPolicyInfo();
      
        // Acceptance criteria:
        SoftAssert sa= new SoftAssert();
        sa.assertEquals(polInfo.getAgentOfRecord(), getAgentOfRecord(agentInfo), "Agent of record is not matching when  Agent changed out Side of County");
        sa.assertEquals(polInfo.getCoutyOfRecord(),countyOfRecordBeforeChange, "County of record is changing when  Agent changed out Side of County");
        sa.assertAll();
    }


}
