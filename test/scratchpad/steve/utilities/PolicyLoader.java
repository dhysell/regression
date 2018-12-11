package scratchpad.steve.utilities;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.GeneratedPolicies;
import persistence.globaldatarepo.helpers.GeneratedPoliciesHelper;

public class PolicyLoader extends BaseTest {

    public GeneratePolicy myPolicyObjInsuredAndLien = null;
    private GeneratePolicyType policyType = GeneratePolicyType.FullApp;
    private WebDriver driver;


    @Test
    public void policyLoaderByAgency() {
//		List<Agents> agencyAgents = AgentsHelper.getAllAgentsInAgency("", false);
//
//		for (Agents agent : agencyAgents) {
//			try {
//				policyLoader(agent);
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("policyLoaderByAgency() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
//			}
//		}
    }

    @Test(dependsOnMethods = "policyLoaderByAgency")
    public void policyLoaderByRandomAgent() {
        //List<Agents> allAgents = AgentsHelper.getAllAgents(false);
        //Collections.shuffle(allAgents);

//		for (Agents agent : allAgents) {
//			try {
//				policyLoader(agent);
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("policyLoaderByRandomAgent() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
//			}
//		}	
    }

    @SuppressWarnings("unused")
    private void policyLoader(Agents agent) throws Exception {
        if (GeneratedPoliciesHelper.getPoliciesByUserName(agent.getAgentUserName()).size() <= 0) {
            generateNewPolicy(agent);

            GeneratedPoliciesHelper.createNewAgentPolicyData(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.busOwnLine.getPolicyNumber(), "BOP", driver.getCurrentUrl(), policyType.toString());
        }
    }

    private void generateNewPolicy(Agents agent) throws Exception {
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        AddressInfo bankAddress = new AddressInfo();
        bankAddress.setLine1("3696 S 6800 W");
        bankAddress.setCity("West Valley City");
        bankAddress.setState(State.Utah);
        bankAddress.setZip("84128");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Bank of Mom and Dad", bankAddress);
        loc2Bldg1AddInterest.setSocialSecurityTaxNum(Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999999)));
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
        loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);
        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
        driver = buildDriver(cf);

        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Training Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(policyType);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        System.out.println(guidewireHelpers.getCurrentPolicyType(myPolicyObjInsuredAndLien).toString());
        System.out.println("Policy with account Number: " + myPolicyObjInsuredAndLien.accountNumber + " was created by " + myPolicyObjInsuredAndLien.agentInfo.getAgentUserName());

        guidewireHelpers.logout();
    }

    @Test(enabled = false)
    public void multipay() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter, "UAT");
        driver = buildDriver(cf);

        new Login(driver).login("su", "gw");

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();


        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsMultiplePayment();

        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);

        for (GeneratedPolicies policy : GeneratedPoliciesHelper.getAllPolicies()) {
            String acct = policy.getAccountNumber() + "-008";
            int newestRow = multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(acct, null, 1500.00);
            if (newestRow == 0) {
                multiplePaymentsPage.clickAdd();
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(acct, null, 1500.00);
            }
        }

        multiplePaymentsPage.clickNext();


        String errorMessage = null;
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist()) {
            errorMessage = guidewireHelpers.getFirstErrorMessage();
            if (!(errorMessage.equals("Account # : Must specify a target for payment: Account #, Policy #, Invoice, or Producer.") || errorMessage.equals("Policy # : Must enter a Policy"))) {
                Assert.fail("The error message displayed was not the message expected for not filling out the policy number. The message displayed was \"" + errorMessage + "\". Test failed.");
            }
        }

        multiplePaymentsPage.clickFinish();

        guidewireHelpers.logout();
    }
}