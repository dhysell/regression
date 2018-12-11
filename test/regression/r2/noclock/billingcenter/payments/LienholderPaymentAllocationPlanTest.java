package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.YesOrNo;

import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsLienholderMutipleAccountPaymentWorkflow;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchMenu;
import repository.bc.search.BCSearchPayment;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Requirement Test Lienholder Payment Allocation Plan and Search->Payment for Multiple Account Lienholder Payment
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/08%20-%20Distributions/08-01%20LH%20Payment%20Allocation%20Plan.docx">Cash Only Status File Marker</a>*
 * @Description
 * @DATE Dec 10, 2015
 */
public class LienholderPaymentAllocationPlanTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj1, myPolicyObj2;	
	private double totalAmount=500;	
	private String loanNumber1="LN12345", loanNumber2="LN00001", accountForExtraAmt, lienholderNumber;
	private Map<String, Double> policyLoanNumAndAppliedAmount;
	private ARUsers arUser = new ARUsers();
	private Date currentDate;
	private ArrayList<Double> lienholderPremiumList = new ArrayList<>();
    private BCTopMenu menu;
	
	private GeneratePolicy generatePolicy(String insuredName, ArrayList<PolicyLocation> locationsList) throws Exception{
        return new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName(insuredName)
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	//This method generates policy1 which has 1 random lienholder, it then passes this lienholder info to the setters of policy2 and generates policy2 which has two same lienholder numbers but with different loan numbers. policy1's lienholder has the same loan number as one of the lienholders in policy2.
	@Test
	public void generateTwoPolicies() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		ArrayList<AdditionalInterest> loc2LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		

		//randomly create a lienholder for policy1
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber1);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);		
		locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));
		
		// Generate policy1 with one lienholder and its loan number is "LN12345"		
		this.myPolicyObj1=generatePolicy("PaymentAllocationPlanPolicy1",locationsList);
		//get the lienholder info and pass it to policy2
		AdditionalInterest lienholderInfo=myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0);
		AddressInfo lienholderAddressInfo=myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress();
		
		// Generate policy2 with two same lienholder numbers as policy1 and their loan numbers are "LN12345" and "LN00001"
		AddressInfo macuAddress = new AddressInfo();
		macuAddress.setLine1(lienholderAddressInfo.getLine1());
		macuAddress.setCity(lienholderAddressInfo.getCity());
		macuAddress.setState(lienholderAddressInfo.getState());
		macuAddress.setZip(lienholderAddressInfo.getZip());
		AdditionalInterest loc2LNBldg1AddInterest = null;
		if ((myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyOrInsured()).equals(ContactSubType.Person)) {
			loc2LNBldg1AddInterest = new AdditionalInterest(lienholderInfo.getPersonFirstName(), lienholderInfo.getPersonLastName(), macuAddress);
		} else {
			loc2LNBldg1AddInterest = new AdditionalInterest(lienholderInfo.getLienholderNameFromPolicyCenter(), macuAddress);
		}
		loc2LNBldg1AddInterest.setAddress(macuAddress);		
		
		loc2LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2LNBldg1AddInterest.setLoanContractNumber(loanNumber2);
		loc2LNBldg1AdditionalInterests.add(loc2LNBldg1AddInterest);
		PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
		loc2Bldg1.setAdditionalInterestList(loc2LNBldg1AdditionalInterests);
		locTwoBuildingList.add(loc2Bldg1);
		//overwrite locationsList and the location has two buildings:loc1Bldg1 and loc1Bldg2
		locationsList.set(0,new PolicyLocation(new AddressInfo(), locOneBuildingList));
		locationsList.add(new PolicyLocation(new AddressInfo(true), locTwoBuildingList));
		this.myPolicyObj2=generatePolicy("PaymentAllocationPlanPolicy2",locationsList);			

		lienholderNumber=myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();		
		policyLoanNumAndAppliedAmount = new LinkedHashMap<String, Double>() {	
		private static final long serialVersionUID = 1L;
		{
            this.put(myPolicyObj1.busOwnLine.getPolicyNumber() + loanNumber1, new Double(NumberUtils.generateRandomNumberInt(20, 50)));
            this.put(myPolicyObj2.busOwnLine.getPolicyNumber() + loanNumber1, new Double(NumberUtils.generateRandomNumberInt(51, 80)));
            this.put(myPolicyObj2.busOwnLine.getPolicyNumber() + loanNumber2, new Double(NumberUtils.generateRandomNumberInt(81, 150)));
        }
        };

        lienholderPremiumList.add(myPolicyObj1.busOwnLine.getPremium().getTotalAdditionalInterestPremium());
		lienholderPremiumList.add(myPolicyObj2.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount());
		lienholderPremiumList.add(myPolicyObj2.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount());		
	}
	
	@Test(dependsOnMethods = { "generateTwoPolicies" })	
	public void makeMultipleAccountLienholderPayment() throws Exception {
				
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		//randomly find an account to apply the excess amount 
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchAccounts mySearch=new BCSearchAccounts(driver);
		accountForExtraAmt= mySearch.findAccountInGoodStanding("08-");
		System.out.println("acctount number is : "+accountForExtraAmt);		
		menu.clickDesktopTab();
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultipleAccountLienholderPayment();
		DesktopActionsLienholderMutipleAccountPaymentWorkflow lienPayment = new DesktopActionsLienholderMutipleAccountPaymentWorkflow(driver);
		lienPayment.setAmount(totalAmount);
		lienPayment.clickNext();
		for(Object key: policyLoanNumAndAppliedAmount.keySet()){
			lienPayment.fillOutNextLineOnPolicyPaymentsTable(policyLoanNumAndAppliedAmount.get(key), null, null, key.toString().substring(0, 12), key.toString().substring(12));
			totalAmount-=policyLoanNumAndAppliedAmount.get(key);
		}
		lienPayment.clickNext();
		lienPayment.fillOutNextLineOnAdditionalPaymentsTable(accountForExtraAmt, totalAmount, null, null, null);
		lienPayment.clickNext();
		lienPayment.clickFinish();
		//run batch process 'New Payment' to apply the payments
		new BatchHelpers(cf).runBatchProcess(BatchProcess.New_Payment);
	}
	
	@Test(dependsOnMethods = { "makeMultipleAccountLienholderPayment" })
	public void verifyPaymentAllocationInLienholderScreens() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);		
		currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPolicyLoanBalances();
		AccountPolicyLoanBalances loanBanlance = new AccountPolicyLoanBalances(driver);
		for(Object key: policyLoanNumAndAppliedAmount.keySet()){
			try{
				loanBanlance.getAccountPolicyLoanBalancesTableRow(key.toString().substring(0, 12), key.toString().substring(12), null, null, policyLoanNumAndAppliedAmount.get(key), null, null, null, null);
			}catch(Exception e){
				Assert.fail("Didn't find the allocated payment for "+ key.toString().substring(3, 9) + "+ loan number "+key.toString().substring(12));
			}
		}
	}
	
	@Test(dependsOnMethods = { "verifyPaymentAllocationInLienholderScreens" })		
	public void verifySearchPayment() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());		
		menu=new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchMenu searchMenu=new BCSearchMenu(driver);
		searchMenu.clickSearchMenuPayments();
		BCSearchPayment searchPayment = new BCSearchPayment(driver);
		searchPayment.setBCSearchPaymentsAccountNumber(lienholderNumber);

		searchPayment.clickBCSearchPaymentsDateCriteriaPicker("Latest Created Date");
		searchPayment.selectBCSearchPaymentsDateFromDatePicker(DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 1));
		searchPayment.clickSearch();
		for(Object key: policyLoanNumAndAppliedAmount.keySet()){			
			if(searchPayment.getBCSearchPaymentsSearchResultsTableRow(currentDate, policyLoanNumAndAppliedAmount.get(key), lienholderNumber, null, null, YesOrNo.Yes, PaymentInstrumentEnum.Check, null, null, null, null)==null)
				Assert.fail("couldn't find the Mulitple Account Lienholder Payment.");
		}				
	}
}
