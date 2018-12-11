package scratchpad.steve.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ActivtyRequestType;
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
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.AdminActivities;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.helpers.AdminActivitiesHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class CSRActivity extends BaseTest {

    private GeneratePolicy myPolicyObjInsuredAndLien = null;
    private String environmentToOverride = "UAT";
    private GeneratePolicyType policyType = GeneratePolicyType.PolicyIssued;
    private CSRs csr;
    private AdminActivities policyInfo;
    private WebDriver driver;

    @Test(description = "creates csr activity so it can escalate")
    public void policyLoaderAllCSR() {
//		ArrayList<String> region = new ArrayList<String>();
//		region.add("Reilly Agency");
//		region.add("Palmer Agency");
//		region.add("Biggs Agency");
//		region.add("Rae Agency");
//		region.add("Zemaitis Agency");
//		region.add("Schmitt Agency");
//		region.add("Open Agency");
//		region.add("Watson Agency");
//		for(String csrAgency: region){
//			List<CSRs> csrsInAgency = CSRsHelper.getAllCSRinAgency(csrAgency);
        ArrayList<String> allCSR = new ArrayList<String>();
        allCSR.add("cabegglen");
//			allCSR.add("cbankhead");  Not Active Employee.
//			allCSR.add("");  Diane Anderson is not in UAT yet							
//			allCSR.add("sacevedo"); //not in UAT
        allCSR.add("cashcraft");
        allCSR.add("kashcraft");
        allCSR.add("lchristensen2");
        allCSR.add("mchristensen");
        allCSR.add("lcochran");
        allCSR.add("mcoffin");
        allCSR.add("wdaniel");
        allCSR.add("bedmonds");
        allCSR.add("mgodinez");
        allCSR.add("agrubaugh");
        allCSR.add("rguzman");
        allCSR.add("rhoyle");
        allCSR.add("sjonak");
        allCSR.add("hlomeli");
        allCSR.add("kmadrid");
        allCSR.add("smoncur");
        allCSR.add("lnewbold");
        allCSR.add("lphilps");
        allCSR.add("brice");
        allCSR.add("hsailsbery");
        allCSR.add("dsmith");
        allCSR.add("psmith");
        allCSR.add("jsweeten");
        allCSR.add("mwaddoups");
        allCSR.add("jahern");
        allCSR.add("dandersen");
        allCSR.add("abenkula");
        allCSR.add("ccarmarena");
        allCSR.add("kcook");
        allCSR.add("mcorbridge");
        allCSR.add("sgomez");
        allCSR.add("binskeep");
        allCSR.add("lkeller");
        allCSR.add("alockwood");
        allCSR.add("lmccurdy");
        allCSR.add("kreel");
        allCSR.add("trumfelt");
        allCSR.add("ksmith");
        allCSR.add("jsparrow");
        allCSR.add("ksparrow");
        allCSR.add("jtilley");
        allCSR.add("nyoung");
        allCSR.add("aarchuleta");
        allCSR.add("mashbrook");
        allCSR.add("mberg");
        allCSR.add("dbryant");
        allCSR.add("ldarbin");
        allCSR.add("jgaskins");
        allCSR.add("charrold");
        allCSR.add("chernandez");
        allCSR.add("ljohnson");
        allCSR.add("sloiselle");
        allCSR.add("alopez");
        allCSR.add("jlott");
        allCSR.add("pmahaffy");
        allCSR.add("cmcbride");
        allCSR.add("jortiz");
        allCSR.add("croberts");
        allCSR.add("ascott");
        allCSR.add("asilva");
        allCSR.add("kismith");
        allCSR.add("hspear");
        allCSR.add("rwalker");
        allCSR.add("mwilliams");
        allCSR.add("tyoung");
        allCSR.add("kbarbour");
        allCSR.add("scarlson");
        allCSR.add("ddowns");
        allCSR.add("kjelinek");
        allCSR.add("rkuprienko");
        allCSR.add("blillibridge");
        allCSR.add("bkmcclure");
        allCSR.add("spatterson");
        allCSR.add("apayton");
        allCSR.add("bruggles");
        allCSR.add("jsimpson");
        allCSR.add("bthompson");

        for (String csrUserName : allCSR) {

            try {
                this.csr = CSRsHelper.getCSRByUserName(csrUserName);
                String regionNum = "";
                if (this.csr.getCsrregion() == null || this.csr.getCsrregion().equals("")) {
                    Assert.fail(driver.getCurrentUrl() + "Unable to create Activity for CSR with no Region.");
                } else {
                    regionNum = this.csr.getCsrregion().substring(this.csr.getCsrregion().indexOf("Region "), this.csr.getCsrregion().indexOf("Region ") + 8);
                    regionNum = regionNum.substring(regionNum.length() - 1, regionNum.length());
                }
                this.policyInfo = getAdminActivityRecord(regionNum);
                System.out.println("Starting Activity for " + this.csr.getCsruserName() + ".");
                createCSRActivity();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Uh Oh! Activity for csr: " + csr.getCsruserName() + " failed.");
            }
        }
    }

    //	@Test
    public void agentActivityForAgent() {
        ArrayList<String> agents = new ArrayList<String>();

        agents.add("cbird");

        for (String agent : agents) {
            try {
                Agents activityAgent = AgentsHelper.getAgentByUserName(agent);
                createAgentActivity(activityAgent);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Uh Oh! Activity for Agent: " + agent + "failed.");
            }
        }

    }

    @SuppressWarnings("unused")
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
                .withAgent(agent)
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
        AdminActivitiesHelper.addAcctInfo(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.accountNumber, "BOP", driver.getCurrentUrl(), policyType.toString());
    }

    public void createCSRActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);


        new Login(driver).loginAndSearchAccountByAccountNumber(policyInfo.getAgent(), AgentsHelper.getAgentByUserName(policyInfo.getAgent()).getAgentPassword(), policyInfo.getAccountNumber());

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
        AdminActivitiesHelper.addCsrActivityData(policyInfo.getAgent(), policyInfo.getAccountNumber(), policyInfo.getPolicyType(), policyInfo.getEnvironment(), policyInfo.getPolicyStatus(), this.csr.getCsruserName(), DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        new GuidewireHelpers(driver).logout();

        //update

    }

    public void createAgentActivity(Agents agent) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);

        AdminActivities activityInfo = AdminActivitiesHelper.getPolicyRowByAgent(agent.getAgentUserName());
        new Login(driver).loginAndSearchAccountByAccountNumber(activityInfo.getBindActivity(), UnderwritersHelper.getUnderwriterByUserName(activityInfo.getBindActivity()).getUnderwriterPassword(), activityInfo.getAccountNumber());

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

    private AdminActivities getAdminActivityRecord(String region) throws Exception {
        AdminActivities activityRecord;
        List<Agents> pcAgent = AgentsHelper.getAllAgentsInRegion(region);
        Collections.shuffle(pcAgent);
        List<AdminActivities> policies = AdminActivitiesHelper.getBoundPolicies(region);
        Collections.shuffle(policies);

        for (Agents ag : pcAgent) {
            for (AdminActivities result : policies) {
                if (ag.getAgentUserName().equals(result.getAgent())) {
                    activityRecord = result;
                    return activityRecord;
                }
            }
        }
        return null;
    }

}
