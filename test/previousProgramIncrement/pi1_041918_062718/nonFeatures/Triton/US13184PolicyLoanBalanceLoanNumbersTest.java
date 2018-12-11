package previousProgramIncrement.pi1_041918_062718.nonFeatures.Triton;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.CountyIdaho;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
/**
* @Author JQU
* @Requirement 	US13184 -- Make loan "number" case insensitive for Policy Loan Balance purpose
* 							Acceptance Criteria
								Ensure that when creating a loan number that is not capitalized and a capitalized one already exist in BillingCenter does not create a new loan bucket. (Req 12-03-14)
								Ensure that it is added to the existing loan bucket. (Req 12-03-14)
							Steps to get there:
								Create a policy change by adding a property with a lienholder on it and make that the loan number is capitalized.
								Then issue the policy change
								Create another policy change on the same policy with the same lienholder on it by adding another property and make sure to use the same loan number and make it lower case.
								This issue the policy change
								Now proceed to the screen "Policy Loan Balances".				
					
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-03%20LH%20Buckets%20-%20Policy%20Loan%20Balances%20Screen.docx?web=1">12-03 LH Buckets - Policy Loan Balances Screen</a>
* @DATE June 01, 2018*/
public class US13184PolicyLoanBalanceLoanNumbersTest extends BaseTest {	
	private WebDriver driver;
	private Config cf;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String LHNumber, LHName;
	private double firstLHPremium, secondLHPremium;
	private String allChars = "ABCDEFGHIGKLMNOPQRSTUVWXYZ";
	private String upperCaseLoanNumber, lowerCaseLoanNumber;
	public String generateRandomChars(String candidateChars, int length) {
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    return sb.toString();
	}
	private void generatePolicy() throws Exception {
		
		upperCaseLoanNumber = generateRandomChars(allChars, 6);
		lowerCaseLoanNumber = upperCaseLoanNumber.toLowerCase();
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(50000);
		building1.setBppLimit(50000);			
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(upperCaseLoanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));		
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LoanNumber")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolDuesCounty(CountyIdaho.Bonneville)				
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		LHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		firstLHPremium = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		LHName = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName();		
	}
	@Test
	public void addPropertyWithLienholder() throws Exception {
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber());       
        AddressInfo address = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress();
        AdditionalInterest additionalInterest = new AdditionalInterest(LHName, address);
       additionalInterest.setLoanContractNumber(lowerCaseLoanNumber);
       additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
       addLienholderBuildingOnLocation(1, additionalInterest, myPolicyObj);
       //get second LH premium
       GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
       complete.clickViewYourPolicy();
       PolicySummary policySummary = new PolicySummary(driver);
       secondLHPremium = policySummary.getTransactionPremium(TransactionType.Policy_Change, null);       
	}	
	public PolicyLocationBuilding addLienholderBuildingOnLocation(int locationNumber, AdditionalInterest additionalInterest, GeneratePolicy policyObj) throws Exception {
		StartPolicyChange change = new StartPolicyChange(driver);
		ArrayList<AdditionalInterest> additionalInterestList = new ArrayList<AdditionalInterest>();
        additionalInterest.setLoanContractNumber(additionalInterest.getLoanContractNumber().toUpperCase());
        additionalInterestList.add(additionalInterest);
        PolicyLocationBuilding building = new PolicyLocationBuilding(additionalInterestList);

        change.startPolicyChange("Add LH Building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        PolicyLocationBuilding newBuilding = buildingsPage.addBuildingOnLocation(true, locationNumber, building);
        policyObj.busOwnLine.locationList.get(locationNumber-1).getBuildingList().add(building);
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();

        change.systemOut("new building number is "+newBuilding.getNumber());
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
//        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(locationNumber, newBuilding.getNumber(), newBuilding.getAdditionalInterestList().get(0).getLienholderPayerAssignmentString(), true, false);
        payerAssignment.setPayerAssignmentBillCoveragesAsRequired(policyObj);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
        change.quoteAndIssue();
        return newBuilding;
    }
	
	
	
	
	
	@Test(dependsOnMethods = { "addPropertyWithLienholder" })
	public void verifyCancellation() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);			
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Policy_Change);
		
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(LHNumber);
		accountMenu.clickAccountMenuPolicyLoanBalances();
		AccountPolicyLoanBalances loanBalance = new AccountPolicyLoanBalances(driver);
		loanBalance.getAccountPolicyLoanBalancesTableRow(myPolicyObj.accountNumber, upperCaseLoanNumber, firstLHPremium+secondLHPremium, null, null, null, null, null, null).click();
		assertTrue(loanBalance.verifyPolicyLoanBalancesChargesTableRow(null, LHNumber, null, null, TransactionType.Issuance, null, null, null, myPolicyObj.accountNumber, firstLHPremium, null, null, upperCaseLoanNumber, null, null, null), 
				"couldn't find the first expected lienholder charge of $"+firstLHPremium+" for "+LHNumber);
		assertTrue(loanBalance.verifyPolicyLoanBalancesChargesTableRow(null, LHNumber, null, null, TransactionType.Policy_Change, null, null, null, myPolicyObj.accountNumber, secondLHPremium, null, null, upperCaseLoanNumber, null, null, null), 
				"couldn't find the second expected lienholder charge of $"+secondLHPremium+" for "+LHNumber);		
	}
}
