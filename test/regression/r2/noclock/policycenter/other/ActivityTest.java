package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.AdminActivities;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.RegionCounty;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AdminActivitiesHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.RegionCountyHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*This specific test class was made to test Admin Data Changes.
 *
 */
@QuarantineClass
public class ActivityTest extends BaseTest {

    private GeneratePolicy myPolicyObjInsuredAndLien = null;
    private Agents agentPolicy;
    private GeneratePolicyType policyType = GeneratePolicyType.FullApp;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";

    //use this if you want to create policies for every agent.

    //	@Test(description="creates the Policy", enabled = false)
    //	public void policyLoaderAllAgents() throws Exception {
    //		List<Agents> allAgents = AgentsHelper.getAllAgents(false);
    //		Collections.shuffle(allAgents);
    //
    //		for (Agents agent : allAgents) {
    //			try {
    //				this.agentPolicy = agent;
    //				policyLoader(agentPolicy);
    //			} catch (Exception e) {
    //				e.printStackTrace();
    //				System.err.println("policyLoaderAllRandom() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
    //			}
    //		}
    //	}

    @Test(enabled = true)
    public void policyLoaderRegion1() throws Exception {

        List<Agents> agentsInRegion = AgentsHelper.getAllAgentsInRegion("1");
        policyLoader(agentsInRegion.get(NumberUtils.generateRandomNumberInt(0, agentsInRegion.size())), "Region 1");
    }

    @Test(enabled = true)
    public void policyLoaderRegion2() throws Exception {

        List<Agents> agentsInRegion = AgentsHelper.getAllAgentsInRegion("2");
        policyLoader(agentsInRegion.get(NumberUtils.generateRandomNumberInt(0, agentsInRegion.size())), "Region 2");
    }

    @Test(enabled = true)
    public void policyLoaderRegion3() throws Exception {

        List<Agents> agentsInRegion = AgentsHelper.getAllAgentsInRegion("3");
        policyLoader(agentsInRegion.get(NumberUtils.generateRandomNumberInt(0, agentsInRegion.size())), "Region 3");
    }

    @Test(enabled = true)
    public void policyLoaderRegion4() throws Exception {

        List<Agents> agentsInRegion = AgentsHelper.getAllAgentsInRegion("4");
        policyLoader(agentsInRegion.get(NumberUtils.generateRandomNumberInt(0, agentsInRegion.size())), "Region 4");
    }

    //	@Test(description = "creates policy by list of Agents", enabled = false)
    //	public void policyLoaderRandomAgent() throws Exception{
    //
    //		for(int i = 0; i<11; i++){
    //			policyLoader(AgentsHelper.getRandomAgent());
    //		}
    //	}


    private void policyLoader(Agents agent, String region) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();

        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
                .withAgent(agentPolicy)
                .withInsPersonOrCompany(ContactSubType.Company)
                //				.withInsCompanyName("Training Policy")
                .withInsCompanyName("Activity " + region)
                .withPolTermLengthDays(10)
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(policyType);

        System.out.println("Policy with account Number: " + myPolicyObjInsuredAndLien.accountNumber + " was created by " + myPolicyObjInsuredAndLien.agentInfo.getAgentUserName());

        //		GeneratedPoliciesHelper.createNewAgentPolicyData(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.accountNumber, "BOP", Configuration.getWebDriver().getCurrentUrl(), policyType.toString());

        AdminActivitiesHelper.addAcctInfo(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.accountNumber, "BOP", driver.getCurrentUrl(), policyType.toString());
        new GuidewireHelpers(driver).logout();
        testRequestApproval(driver);
        agentBind(driver);
        documentsActivity(driver);
        agentChange(driver);
        //addCert();
    }


    public void testRequestApproval(WebDriver driver) throws Exception {
        new Login(driver).loginAndSearchJob(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuQualification();
        GenericWorkorderQualification questions = new GenericWorkorderQualification(driver);
        questions.clickBOP_Recreation(true);
        questions.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickPreQuoteDetails();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.requestApproval();
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
        woComplete.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        Underwriters uw = summary.getAssignedToUW("Approval Requested");
        System.out.println("Approval Requested Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateRequestApprovalActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
        verifyActivityAssignment(uw.getUnderwriterUserName());
        guidewireHelpers.logout();
        uwApproval(driver, uw);
    }


    public void uwApproval(WebDriver driver, Underwriters uw) throws Exception {
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Approval Requested");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.approveAll();
        risk.clickReleaseLock();
        new GuidewireHelpers(driver).logout();
    }


    public void agentBind(WebDriver driver) throws Exception {
        new Login(driver).loginAndSearchJob(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        GenericWorkorder fullApp = new GenericWorkorder(driver);
        fullApp.clickGenericWorkorderQuote();
        fullApp.clickPolicyChangeNext();
        fullApp.clickPolicyChangeNext();
        GenericWorkorderPayment pay = new GenericWorkorderPayment(driver);
        double paymentAmount = pay.getDownPaymentAmount();
        pay.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, paymentAmount);
        pay.SubmitOnly();
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
        woComplete.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        Underwriters uw = summary.getAssignedToUW("Submitted Full Application");
        System.out.println("Submitted Full Application Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateBoundActivity(myPolicyObjInsuredAndLien.accountNumber, "Bound", uw.getUnderwriterUserName());
        verifyActivityAssignment(uw.getUnderwriterUserName());
        uwIssue(driver, uw);

    }

    public void uwIssue(WebDriver driver, Underwriters uw) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Submitted Full Application");
        GenericWorkorder woIssue = new GenericWorkorder(driver);
        woIssue.clickGenericWorkorderQuote();
        woIssue.clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
        AdminActivitiesHelper.updateIssueActivity(myPolicyObjInsuredAndLien.accountNumber, "Issued");
        guidewireHelpers.logout();
    }


    public void agentChange(WebDriver driver) throws Exception {
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        StartPolicyChange change = new StartPolicyChange(driver);
        change.startPolicyChange("UW Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        change.quoteAndSubmit(ChangeReason.AdditionalInsuredAdded, "Almost Changed");
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
        woComplete.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        Underwriters uw = summary.getAssignedToUW("Submitted policy change");
        System.out.println("Submitted policy change Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateChangeActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
        verifyActivityAssignment(uw.getUnderwriterUserName());
        uwIssueChange(driver, uw);
    }


    public void documentsActivity(WebDriver driver) throws Exception {

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Doc Type: " + DocumentType.Photos);
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(DocumentType.Photos);
        doc.clickUpdate();
        guidewireHelpers.logout();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        login.loginAndSearchAccountByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        Underwriters uw = summary.getAssignedToUW("Photos");
        if (uw == null) {
            Assert.fail("Assigned UW was returned as null. Test cannot continue.");
        }
        System.out.println("Submitted Application Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateDocActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
        verifyActivityAssignment(uw.getUnderwriterUserName());
        summary.clickActivitySubject("Photos");
        guidewireHelpers.logout();
        login.loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
        summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Photos");
        ActivityPopup activityPopup = new ActivityPopup(driver);
        activityPopup.clickCompleteButton();
        guidewireHelpers.logout();
    }


    public void addCert(WebDriver driver) throws Exception {
        Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
        StartPolicyChange addCert = new StartPolicyChange(driver);
        addCert.startPolicyChange("Add Cert", today);
        addCert.quoteAndSubmit(ChangeReason.CertificateOnlyRequest, "Added Cert");
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
        woComplete.clickAccountNumber();
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        Underwriters uw = summary.getAssignedToUW("Submitted policy change");
        System.out.println("Submitted policy change Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateAssistantChangeActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
        verifyActivityAssignment(uw.getUnderwriterUserName());
        uwIssueChange(driver, uw);
    }

    public void uwIssueChange(WebDriver driver, Underwriters uw) {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Submitted policy change");
        GenericWorkorder woIssue = new GenericWorkorder(driver);
        woIssue.clickIssuePolicyButton();
        guidewireHelpers.logout();
    }

    public void createAgentActivity(WebDriver driver, Agents agent) throws Exception {

        AdminActivities activityInfo = AdminActivitiesHelper.getPolicyRowByAgent(agent.getAgentUserName());
        new Login(driver).loginAndSearchPolicyByAccountNumber(activityInfo.getBindActivity(), UnderwritersHelper.getUnderwriterByUserName(activityInfo.getBindActivity()).getUnderwriterPassword(), activityInfo.getAccountNumber());

        ActionsPC actionsMenu = new ActionsPC(driver);
        actionsMenu.requestActivity(ActivtyRequestType.GetMissingInformation);

        GenericActivityPC activity = new GenericActivityPC(driver);
        activity.setSubject("Escalation Email Activity");
        activity.setText("this activity is to test the Activities functionality");
        activity.setTargetDueDate(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)));
        activity.clickOK();

        //verify activity was created
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        sideMenu.clickSideMenuToolsSummary();

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickActivity("Get Missing Information");
        AdminActivitiesHelper.updateWithAgentActivity(activityInfo.getAccountNumber(), DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        new GuidewireHelpers(driver).logout();

    }

    public void verifyActivityAssignment(String uwUserName) throws Exception {
        String county = myPolicyObjInsuredAndLien.agentInfo.getAgentCounty();
        RegionCounty countyInfo = RegionCountyHelper.getCountyInfo(county);
        String expectedUW = UnderwritersHelper.getUnderwriterInfoByFullName(countyInfo.getUnderWriter()).getUnderwriterUserName();

        System.out.println("The agents County is: " + county);
        System.out.println("The underwriter associated with the county is " + countyInfo.getUnderWriter());

        if (!uwUserName.equals(expectedUW)) {
            Assert.fail(this.myPolicyObjInsuredAndLien.accountNumber + "Ensure the UW assigned to the activity = the underwriter assigned to the county.\nAssigned UW: " + uwUserName + "\nExpected UW: " + expectedUW);
        }
    }
}
