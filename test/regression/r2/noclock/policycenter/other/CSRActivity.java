package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.CSRsHelper;

@QuarantineClass
public class CSRActivity extends BaseTest {

    private GeneratePolicy myPolicyObjInsuredAndLien = null;
    private CSRs csr;


    /**
     * @author Steve Broderick
     * @Requirement When a CSR Activity Escalates, the Escalate Activity should go to the CSR.
     * @DATE - 8/5/2016
     */


    @Test(description = "Creates Escalated CSR Activity, then verifies the Activity Escalation went to the proper CSR Supervisor.")
    public void csrRegion1Activity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        String sup = null;
        String assignTo = null;
        this.csr = CSRsHelper.getCSRByRegion("1");
        if (this.csr.getCsrregion() == null || this.csr.getCsrregion().equals("")) {
            Assert.fail(driver.getCurrentUrl() + "Unable to create Activity for CSR with no Region.");
        }

        policyLoader(driver, "Region 1");
        System.out.println("Starting Activity for " + this.csr.getCsruserName() + ".");
        createCSRActivity(driver);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Activity_Escalation);
        String today = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd");
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        assignTo = summaryPage.getActivityAssignment("Activity Escalated on " + today + " - Get Missing Information, User: " + this.csr.getCsrfirstName() + " " + this.csr.getCsrlastName());
        sup = this.csr.getCsrregion();
        if (sup.contains("1")) {
            sup = "Michelle Coffin";
        } else if (sup.contains("2")) {
            sup = "Betty Inskeep";
        } else if (sup.contains("3")) {
            sup = "Janeth Ortiz";
        } else if (sup.contains("4")) {
            sup = "Barbara Mcclure";
        } else {
            sup = "Michelle Coffin";
        }

        if (!assignTo.equals(sup)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjInsuredAndLien.accountNumber + "Please ensure " + csr.getCsruserName() + " goes to the right supervisor");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(description = "Creates Escalated CSR Activity, then verifies the Activity Escalation went to the proper CSR Supervisor.")
    public void csrRegion2Activity() throws Exception {
        String sup = null;
        String assignTo = null;
        this.csr = CSRsHelper.getCSRByRegion("2");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        if (this.csr.getCsrregion() == null || this.csr.getCsrregion().equals("")) {
            Assert.fail(driver.getCurrentUrl() + "Unable to create Activity for CSR with no Region.");
        }

        policyLoader(driver, "Region 2");
        System.out.println("Starting Activity for " + this.csr.getCsruserName() + ".");
        createCSRActivity(driver);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Activity_Escalation);
        String today = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd");
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        assignTo = summaryPage.getActivityAssignment("Activity Escalated on " + today + " - Get Missing Information, User: " + this.csr.getCsrfirstName() + " " + this.csr.getCsrlastName());
        sup = this.csr.getCsrregion();
        if (sup.contains("1")) {
            sup = "Michelle Coffin";
        } else if (sup.contains("2")) {
            sup = "Betty Inskeep";
        } else if (sup.contains("3")) {
            sup = "Janeth Ortiz";
        } else if (sup.contains("4")) {
            sup = "Barbara Mcclure";
        } else {
            sup = "Michelle Coffin";
        }

        if (!assignTo.equals(sup)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjInsuredAndLien.accountNumber + "Please ensure " + csr.getCsruserName() + " goes to the right supervisor");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(description = "Creates Escalated CSR Activity, then verifies the Activity Escalation went to the proper CSR Supervisor.")
    public void csrRegion3Activity() throws Exception {
        String sup = null;
        String assignTo = null;
        this.csr = CSRsHelper.getCSRByRegion("3");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        if (this.csr.getCsrregion() == null || this.csr.getCsrregion().equals("")) {
            Assert.fail(driver.getCurrentUrl() + "Unable to create Activity for CSR with no Region.");
        }

        policyLoader(driver, "Region 3");
        System.out.println("Starting Activity for " + this.csr.getCsruserName() + ".");
        createCSRActivity(driver);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Activity_Escalation);
        String today = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd");
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        assignTo = summaryPage.getActivityAssignment("Activity Escalated on " + today + " - Get Missing Information, User: " + this.csr.getCsrfirstName() + " " + this.csr.getCsrlastName());
        sup = this.csr.getCsrregion();
        if (sup.contains("1")) {
            sup = "Michelle Coffin";
        } else if (sup.contains("2")) {
            sup = "Betty Inskeep";
        } else if (sup.contains("3")) {
            sup = "Janeth Ortiz";
        } else if (sup.contains("4")) {
            sup = "Barbara Mcclure";
        } else {
            sup = "Michelle Coffin";
        }

        if (!assignTo.equals(sup)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjInsuredAndLien.accountNumber + "Please ensure " + csr.getCsruserName() + " goes to the right supervisor");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(description = "Creates Escalated CSR Activity, then verifies the Activity Escalation went to the proper CSR Supervisor.")
    public void csrRegion4Activity() throws Exception {
        String sup = null;
        String assignTo = null;
        this.csr = CSRsHelper.getCSRByRegion("4");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        if (this.csr.getCsrregion() == null || this.csr.getCsrregion().equals("")) {
            Assert.fail(driver.getCurrentUrl() + "Unable to create Activity for CSR with no Region.");
        }

        policyLoader(driver, "Region 4");
        System.out.println("Starting Activity for " + this.csr.getCsruserName() + ".");
        createCSRActivity(driver);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Activity_Escalation);
        String today = DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd");
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        assignTo = summaryPage.getActivityAssignment("Activity Escalated on " + today + " - Get Missing Information, User: " + this.csr.getCsrfirstName() + " " + this.csr.getCsrlastName());
        sup = this.csr.getCsrregion();
        if (sup.contains("1")) {
            sup = "Michelle Coffin";
        } else if (sup.contains("2")) {
            sup = "Betty Inskeep";
        } else if (sup.contains("3")) {
            sup = "Janeth Ortiz";
        } else if (sup.contains("4")) {
            sup = "Barbara Mcclure";
        } else {
            sup = "Michelle Coffin";
        }

        if (!assignTo.equals(sup)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjInsuredAndLien.accountNumber + "Please ensure " + csr.getCsruserName() + " goes to the right supervisor");
        }
        new GuidewireHelpers(driver).logout();
    }

    public void createCSRActivity(WebDriver driver) throws Exception {


        new Login(driver).loginAndSearchPolicyByAccountNumber(this.myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), this.myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), this.myPolicyObjInsuredAndLien.accountNumber);

        ActionsPC actions = new ActionsPC(driver);
        actions.requestActivity(ActivtyRequestType.GetMissingInformation);

        GenericActivityPC activity = new GenericActivityPC(driver);
        activity.setSubject("Escalation Email Activity");
        activity.setText("this activity is to test the Activities functionality");
        activity.setTargetDueDate(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -4)));
        activity.setAssignTo(csr.getCsruserName(), "CSR");
        activity.clickOK();

        //verify activity was created
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        sideMenu.clickSideMenuToolsSummary();

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickActivity("Get Missing Information");
        //		AdminActivitiesHelper.addCsrActivityData(policyInfo.getAgent(), policyInfo.getAccountNumber(), policyInfo.getPolicyType(), policyInfo.getEnvironment(), policyInfo.getPolicyStatus(), this.csr.getCsruserName(), DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter));

        new GuidewireHelpers(driver).logout();

        //update

    }

    private void policyLoader(WebDriver driver, String region) throws Exception {

        List<Agents> regionAgents = AgentsHelper.getAllAgentsInRegion(region);
        Agents randomAgent = regionAgents.get(NumberUtils.generateRandomNumberInt(0, regionAgents.size()));

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
                .withAgent(randomAgent)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName(region)
                .withPolTermLengthDays(10)
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        new GuidewireHelpers(driver).logout();

    }
}
