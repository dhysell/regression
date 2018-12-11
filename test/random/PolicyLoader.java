package random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.GeneratedPolicies;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.GeneratedPoliciesHelper;

public class PolicyLoader extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjInsuredAndLien = null;
	private String regionToStartWith = "Region 4 Northern Idaho";
	private String agentUsernameToStartWith = "darmstrong";
	private String environmentToOverride = "UAT";
	private PaymentPlanType paymentPlanType = PaymentPlanType.Monthly;
	private ArrayList<String> allAgencyNames = new ArrayList<String>();
	private ArrayList<String> allAgentUsernames = new ArrayList<String>();
	private String bcUser = "sbrunson";
	private String bcPassword = "gw";
	
	public PolicyLoader() throws Exception {
		setAllAgentUsernames(AgentsHelper.getAllAgentUsernames());
	}

	public ArrayList<String> getAllAgentUsernames() {
		return allAgentUsernames;
	}

	public void setAllAgentUsernames(ArrayList<String> allAgentUsernames) {
		this.allAgentUsernames = allAgentUsernames;
	}

	public String getAgentUsernameToStartWith() {
		return agentUsernameToStartWith;
	}

	public void setAgentUsernameToStartWith(String agentUsernameToStartWith) {
		this.agentUsernameToStartWith = agentUsernameToStartWith;
	}

	public ArrayList<String> getAllAgencyNames() {
		return allAgencyNames;
	}

	public void setAllAgencyNames(ArrayList<String> allAgencyNames) {
		this.allAgencyNames = allAgencyNames;
	}

	public String getAgencyToStartWith() {
		return regionToStartWith;
	}

	public void setAgencyToStartWith(String agencyToStartWith) {
		this.regionToStartWith = agencyToStartWith;
	}

	public String getEnvironmentToOverride() {
		return environmentToOverride;
	}

	public void setEnvironmentToOverride(String environmentToOverride) {
		this.environmentToOverride = environmentToOverride;
	}
	
	@Test(enabled = false)
	public void policyLoaderByAgent() throws Exception {
		if(this.allAgentUsernames.contains(this.agentUsernameToStartWith)) {
			Agents toUse = AgentsHelper.getAgentByUserName(this.agentUsernameToStartWith);
			try {
				policyLoader(toUse);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("policyLoaderByAgent() OOPS: This one failed (" + this.agentUsernameToStartWith + ")");
			}
		} else {
			System.out.println("ERROR: The agent \"" + this.agentUsernameToStartWith + "\" does not exist.");
		}
		
	}
	
	@Test(enabled = false)
	public void policyLoaderByRegion() throws Exception {
		if(this.allAgencyNames.contains(this.regionToStartWith)) {
			List<Agents> agencyAgents = AgentsHelper.getAllAgentsInRegion(this.regionToStartWith);

			for (Agents agent : agencyAgents) {
				try {
					policyLoader(agent);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("policyLoaderByAgency() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
				}
			}
		} else {
			System.out.println("ERROR: The agency \"" + this.regionToStartWith + "\" does not exist.");
		}
	}
	
	@Test(enabled = false)
	public void policyLoaderAllAgents() throws Exception {
		List<Agents> allAgents = AgentsHelper.getAllAgents();
		Collections.shuffle(allAgents);

		for (Agents agent : allAgents) {
			try {
				policyLoader(agent);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("policyLoaderAllRandom() OOPS: This one failed (" + agent.getAgentUserName() + ")... try the next one");
			}
		}
	}
	
	@Test(enabled = false)
	public void policyLoaderByAgencyThenAllRandom() throws Exception {
		
		policyLoaderByRegion();
		
		policyLoaderAllAgents();
	}
	

	private void policyLoader(Agents agent) throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
/*		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

		AddressInfo bankAddress = new AddressInfo();
		bankAddress.setLine1("3696 S 6800 W");
		bankAddress.setCity("West Valley City");
		bankAddress.setState(State.Utah);
		bankAddress.setZip("84128");

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company, "Bank of Mom and Dad", bankAddress);
		loc2Bldg1AddInterest.setSocialSecurityTaxNum(Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999999)));
		loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc2Bldg1AddInterest.setAddress(bankAddress);
		loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Building);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
*/
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
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(paymentPlanType)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

        getQALogger().info(new GuidewireHelpers(driver).getCurrentPolicyType(myPolicyObjInsuredAndLien).toString());
        getQALogger().info("Policy with account Number: " + myPolicyObjInsuredAndLien.accountNumber + " was created by " + myPolicyObjInsuredAndLien.agentInfo.getAgentUserName());
        GeneratedPoliciesHelper.createNewAgentPolicyData(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.busOwnLine.getPolicyNumber(), "BOP", driver.getCurrentUrl(), GeneratePolicyType.PolicyIssued.toString());
	}
	
	@Test(enabled = false)
	public void printAgencyNamesToConsole() {
		for(String aName : this.allAgencyNames) {
			System.out.println(aName);
		}
	}
	
	private void bcMultiPay() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter, environmentToOverride);
		driver = buildDriver(cf);
		
		new Login(driver).login(bcUser, bcPassword);
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();

    }
	
	@Test(enabled = false)
	public void multipay() throws Exception {
		bcMultiPay();
        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		String acct = null;
		for (GeneratedPolicies policy : GeneratedPoliciesHelper.getAllPolicies()) {
			if(policy.getAccountNumber().length() > 7){
				acct = policy.getAccountNumber().substring(3, 9) + "-008";
			} else {
				acct = policy.getAccountNumber() + "-008";
			}
			
			int newestRow = multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(acct, null, 1500.00);
			if (newestRow >= 30) {
				finishMultipay();
				bcMultiPay();
				newestRow = 0;
			}
		}
		finishMultipay();
		
	}
	
	private void finishMultipay() {
        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		multiplePaymentsPage.clickNext();


		String errorMessage = null;
		if (new GuidewireHelpers(driver).errorMessagesExist()) {
			errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
			if (!(errorMessage.equals("Account # : Must specify a target for payment: Account #, Policy #, Invoice, or Producer.") || errorMessage.equals("Policy # : Must enter a Policy"))) {
				Assert.fail("The error message displayed was not the message expected for not filling out the policy number. The message displayed was \"" + errorMessage + "\". Test failed.");
			}
		}

        multiplePaymentsPage.clickFinish();
		
        new GuidewireHelpers(driver).logout();
	}
	
	@Test(enabled = false)
	public void multipayByAccount() throws Exception {
		ArrayList<String> accounts = new ArrayList<String>();
		accounts.add("");
				
		bcMultiPay();
        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
		String acct = null;
		for (String acctToPay : accounts) {
			if(acctToPay.length() > 7){
				acct = acctToPay.substring(3, 9) + "-008";
			} else {
				acct = acctToPay + "-008";
			}
            multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
			int newestRow = multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(acct, null, 1500.00);
			if (newestRow >= 30) {
				finishMultipay();
				bcMultiPay();
				newestRow = 0;
			}
		}
		finishMultipay();
		
	}
}