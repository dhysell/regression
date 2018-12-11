package scratchpad.steve.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AdminActivitiesHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.GeneratedPoliciesHelper;

/*This specific test class was made to test Admin Data Changes.
 *
 */
@SuppressWarnings({"unused"})
public class ActivityTest extends BaseTest {

    private GeneratePolicy myPolicyObjInsuredAndLien = null;
    private Agents agentPolicy;
    private String environmentToOverride = "UAT";
    private ArrayList<String> allAgentUsernames = new ArrayList<String>();
    private GeneratePolicyType policyType = GeneratePolicyType.FullApp;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private ArrayList<String> producers;
    private WebDriver driver;

    @Test(description = "creates the Policy", enabled = false)
    public void policyLoaderAllAgents() throws Exception {
        List<Agents> allAgents = AgentsHelper.getAllAgents();
        Collections.shuffle(allAgents);

        for (Agents agent : allAgents) {
            try {
                this.agentPolicy = agent;
                policyLoader(agentPolicy);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("policyLoaderAllRandom() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
            }
        }
    }

    @Test(description = "creates policy by list of Agents", enabled = true)
    public void policyLoaderAgent() {

        ArrayList<String> producer = new ArrayList<String>();
        producer.add("bmerrill");
        producer.add("bmcclure");
        producer.add("cpullman");
        producer.add("kperry");
        producer.add("ccox");
        producer.add("rbird");
        producer.add("dshill");
        producer.add("pzemaitis");
        producer.add("smalmstrom");
        producer.add("dcolpron");
        producer.add("dteuscher");
        producer.add("dguthrie");
        producer.add("rpitchford");
        producer.add("bstowell");
        producer.add("tburke");
        producer.add("bterry");
        producer.add("nfunk");
        producer.add("tferguson");
        producer.add("wdinning");
        producer.add("lfry");
        producer.add("cpullman");
        producer.add("jgardner");
        producer.add("rprice");
        producer.add("kfronk");
        producer.add("rchase");
        producer.add("ehardy");
        producer.add("jhart");
        producer.add("gmoses");
        producer.add("skunz");
        producer.add("sbarrett");
        producer.add("masker");
        producer.add("jpetersen");
        producer.add("gmtaylor");
        producer.add("srogers");
        producer.add("squinn");
        producer.add("rbeckner");
        producer.add("bcrofoot");
        producer.add("krichardson");
        producer.add("tgallup");
        producer.add("tkasper");
        producer.add("kdegn");
        producer.add("whungate");
        producer.add("jhill");
        producer.add("manderson");
        producer.add("swidmer");
        producer.add("jcook");
        producer.add("bbarton");
        producer.add("bguzman");
        producer.add("rellis");
        producer.add("tnichols");
        producer.add("ddambra");
        producer.add("rsilvers");
        producer.add("jleach");
        producer.add("nhazelbaker");
        producer.add("cvanbiezen");
        producer.add("mbrodine");
        producer.add("pjohnson");
        producer.add("hgrothous");
        producer.add("ccollier");
        producer.add("lbothof");
        producer.add("pshank");
        producer.add("rrex");
        producer.add("pdewitt");


        for (int i = producer.size() - 1; i > 0; i--) {
            try {
                this.agentPolicy = AgentsHelper.getAgentByUserName(producer.get(i));
                if (!(AdminActivitiesHelper.getPoliciesRowByAgent(producer.get(i)).size() > 0)) {
                    policyLoader(AgentsHelper.getAgentByUserName(producer.get(i)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("policyLoaderAllRandom() OOPS: This one failed (" + this.agentPolicy.getAgentUserName() + ")... try the next one");
            }
        }
    }

    @Test(description = "creates policy by list of Agents", enabled = false)
    public void policyLoaderRandomAgent() throws Exception {

        for (int i = 0; i < 11; i++) {
            policyLoader(AgentsHelper.getRandomAgent());
        }
    }


    private void policyLoader(Agents agent) throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
//		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

//		AddressInfo bankAddress = new AddressInfo();
//		bankAddress.setLine1("3696 S 6800 W");
//		bankAddress.setCity("West Valley City");
//		bankAddress.setState(State.Utah);
//		bankAddress.setZip("84128");

//		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company, "Bank of Mom and Dad", bankAddress);
//		loc2Bldg1AddInterest.setSocialSecurityTaxNum(Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999999)));
//		loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
//		loc2Bldg1AddInterest.setAddress(bankAddress);
//		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Building);
//		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
//		loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);
        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);


        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
                .withAgent(agentPolicy)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Training Policy")
                .withPolTermLengthDays(10)
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(policyType);

        System.out.println(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObjInsuredAndLien).toString());
        System.out.println("Policy with account Number: " + myPolicyObjInsuredAndLien.accountNumber + " was created by " + myPolicyObjInsuredAndLien.agentInfo.getAgentUserName());

        GeneratedPoliciesHelper.createNewAgentPolicyData(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.accountNumber, "BOP", driver.getCurrentUrl(), policyType.toString());

        AdminActivitiesHelper.addAcctInfo(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.accountNumber, "BOP", driver.getCurrentUrl(), policyType.toString());
        new GuidewireHelpers(driver).logout();
        testRequestApproval();
        agentBind();
//		documentsActivity();
//		agentChange();
//		addCert();
    }


    public void testRequestApproval() throws Exception {
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
        guidewireHelpers.logout();
        uwApproval(uw);
    }


    public void uwApproval(Underwriters uw) throws Exception {
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


    public void agentBind() throws Exception {
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
        System.out.println("Bound Application Activity went to: " + uw.getUnderwriterUserName());
        AdminActivitiesHelper.updateBoundActivity(myPolicyObjInsuredAndLien.accountNumber, "Bound", uw.getUnderwriterUserName());
        uwIssue(uw);
        new GuidewireHelpers(driver).logout();
    }

    public void uwIssue(Underwriters uw) throws Exception {
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Submitted Full Application");
        GenericWorkorder woIssue = new GenericWorkorder(driver);
        woIssue.clickGenericWorkorderQuote();
        woIssue.clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
        AdminActivitiesHelper.updateIssueActivity(myPolicyObjInsuredAndLien.accountNumber, "Issued");
        new GuidewireHelpers(driver).logout();
    }


//	public void agentChange() throws Exception{
//		loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
////		PolicyBusinessownersLineAdditionalInsured newBOLInsured = new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Bank of Mom and Dad", AdditionalInsuredRole.OwnersOrOtherInterests, new AddressInfo("3696 South 6800 West","","West Valley City", State.Utah,"84128","","United States", AddressType.Business));
//		StartPolicyChange change = new StartPolicyChange(driver);
//		change.startPolicyChange("UW Change", DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter));
////		change.addAdditionalInsuredBOLByAgent(newBOLInsured);
//		change.quoteAndBind(ChangeReason.AdditionalInsuredAdded, "Almost Changed");
//		GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
//		woComplete.clickAccountNumber();
//		IAccountSummary summary = AccountFactory.getAccountSummaryPage();
//		Underwriters uw = summary.getAssignedToUW("Bound policy change");
//		System.out.println("Bound policy change Activity went to: " + uw.getUnderwriterUserName());
//		AdminActivitiesHelper.updateChangeActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
//		uwIssueChange(uw);
//	}


//	public void documentsActivity() throws Exception{
//		
//		loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		IActions actions = ActionsFactory.getActionsMenu();
//		actions.click_Actions();
//		actions.click_NewDocument();
//		actions.click_LinkToExistingDoc();
//		systemOut("Uploading Doc Type: " + DocumentType.Photos);
//		//		NewDocument doc = ActionsFactory.getNewDocument();
//		doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
//		//		doc.selectRelatedTo("Account");
//		//		doc.selectDocumentType(DocumentType.Photos);
//		//		doc.clickUpdate();
//		//		logout();
//		IAccountSummary summary = AccountFactory.getAccountSummaryPage();
//		loginAndSearchAccountByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		Underwriters uw = summary.getAssignedToUW("Photos");
//		System.out.println("Bound Application Activity went to: " + uw.getUnderwriterUserName());
//		AdminActivitiesHelper.updateDocActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
//		summary.clickActivitySubject("Photos");
//		//		logout();
//		loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		summary = AccountFactory.getAccountSummaryPage();
//		summary.clickActivitySubject("Photos");
//		ActivityPopup activityPopup = ActivityFactory.getActivityPopupPage();
//		activityPopup.clickCompleteButton();
//		logout();
//	}


//	public void addCert() throws Exception{
//		Date today = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);
//		loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		StartPolicyChange addCert = new StartPolicyChange(driver);
//		addCert.startPolicyChange("Add Cert", today);
//		addCert.quoteAndBind(ChangeReason.CertificateOnlyRequest, "Added Cert");
//		GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
//		woComplete.clickAccountNumber();
//		IAccountSummary summary = AccountFactory.getAccountSummaryPage();
//		Underwriters uw = summary.getAssignedToUW("Bound policy change");
//		System.out.println("Bound policy change Activity went to: " + uw.getUnderwriterUserName());
//		AdminActivitiesHelper.updateAssistantChangeActivity(myPolicyObjInsuredAndLien.accountNumber, uw.getUnderwriterUserName());
//		uwIssueChange(uw);
//	}

//	public void uwIssueChange(Underwriters uw){
//		logout();
//		loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		IAccountSummary summary = AccountFactory.getAccountSummaryPage();
//		summary.clickActivitySubject("Bound policy change");
//		GenericWorkorder woIssue = new GenericWorkorder(driver);
////		woIssue.clickGenericWorkorderQuote();
//		woIssue.clickIssuePolicyButton();
//		logout();
//	}

    //Must sign in next day to check renewal.
//	@Test(dependsOnMethods = {"agentBind"})
//	public void getRenewalAssignTo(){
//		login("su", "gw");
//		runBatchProcess(BatchProcess.Policy_Renewal_Start);
//		logout();
//		loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);
//		IAccountSummary acctPage = AccountFactory.getAccountSummaryPage();
//		acctPage.
//		
//	}

}
