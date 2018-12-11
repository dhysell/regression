package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description   US11959: Stop the use of "Masquerade as Cancel" and use "Is Partial Cancel" flag where necessary in Invoicing
* 					"Masquerade as Cancel" is sent from PC in the billing instruction which could include any combination of partial cancel, payer change, and policy change. BC will treat ALL the charges within the billing instruction as partial cancel. BC will need to disregard looking at "Masquerade As Cancel" flag and only look at the partial cancel flag to identify the charges that are canceling.
					Requirements:
					11-08 Cancellation Installment Scheduling (See 11-08-09, 10, & 11)
					Acceptance Criteria
					The partial cancel flag will be on earned positive and canceling negative charge
					Invoicing will no longer look at the Masquerade as Cancel flag for invoicing
					The unearned charges to the new payer will follow the policy change installment scheduling
					The positive charges w/ partial cancel charge is to be treated as cancellation charge and invoice in one invoice as lump sum amount
* 					
* @DATE October 5, 2017
*/
public class UseIsPartialCancelFlagInInvoicingTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();	
	private BCAccountMenu acctMenu;
	private String originalLoanNumber="LN12345", newLoanNumber="LN11111";
	private String originalLHNumber, newLHNumber;
	private double LHPremium;
	private String LHName;
	private AccountCharges charge;
	
	@Test 
	public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);
		locOneBuildingList.add(building1);
		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(originalLoanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("MoveLHChargeToInsured")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		originalLHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		LHName = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getCompanyName();		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void removeLHAndAddNewOne() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
		ArrayList<AdditionalInterest> newInterestList = new ArrayList<AdditionalInterest>();		
		AdditionalInterest newInterest = new AdditionalInterest(ContactSubType.Company);
		newInterest.setNewContact(CreateNew.Create_New_Always);
		newInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		newInterest.setLoanContractNumber(newLoanNumber);		
		newInterestList.add(newInterest);
		//remove old Lienholder, add a new Lienholder
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).setAdditionalInterestList(newInterestList);		
		removeAdditionalInterestAddNewOne(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj, 1, 2, LHName,  newInterest);	
		newLHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();		
	}
	
	
	
	public void removeAdditionalInterestAddNewOne(Date effectiveDate, GeneratePolicy policy, int locationNumber, int buildingNumber, String originalAdditionalInterestName, AdditionalInterest newAdditionalInterest) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		removeAdditionalInterestPrivateMethod(effectiveDate, locationNumber, buildingNumber, originalAdditionalInterestName);
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.addAdditionalInterest(true, newAdditionalInterest);
        buildings.clickOK();
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        switch (policy.productType) {
            case Businessowners:
                locationList = policy.busOwnLine.locationList;
                break;
            case CPP:
                locationList = policy.commercialPackage.locationList;
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                locationList = policy.squire.propertyAndLiability.locationList;
                break;
//		case StandardFL:
//			locationList = policy.standardFireAndLiability.getLocationList();
//			break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                locationList = policy.standardFire.getLocationList();
                break;
            case StandardLiability:
                locationList = policy.standardLiability.getLocationList();
                break;
        }
//        if (!policy.locationList.get(locationNumber - 1).getBuildingList().isEmpty()) {//For BOP Buildings List
        for (AdditionalInterest additionalInterest : locationList.get(locationNumber - 1).getBuildingList().get(buildingNumber - 1).getAdditionalInterestList()) {
            if ((additionalInterest.getCompanyName().contains(originalAdditionalInterestName)) || (additionalInterest.getPersonFullName().contains(originalAdditionalInterestName))) {
                locationList.get(locationNumber - 1).getBuildingList().get(buildingNumber - 1).getAdditionalInterestList().remove(additionalInterest);
                locationList.get(locationNumber - 1).getBuildingList().get(buildingNumber - 1).getAdditionalInterestList().add(newAdditionalInterest);
            }
        }
//        } else { // For PL Buildings List
//            for (AdditionalInterest additionalInterest : policy.locationList.get(locationNumber - 1).getBuildingListPL().get(buildingNumber - 1).getAdditionalInterestList()) {
//                if ((additionalInterest.getCompanyName().contains(originalAdditionalInterestName)) || (additionalInterest.getPersonFullName().contains(originalAdditionalInterestName))) {
//                    policy.locationList.get(locationNumber - 1).getBuildingListPL().get(buildingNumber - 1).getAdditionalInterestList().remove(additionalInterest);
//                    policy.locationList.get(locationNumber - 1).getBuildingListPL().get(buildingNumber - 1).getAdditionalInterestList().add(newAdditionalInterest);
//                }
//            }
//        }

        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.fillOutPayerAssignmentPage(policy);
        policyChangePage.quoteAndIssue();
    }
	
	
	private void removeAdditionalInterestPrivateMethod(Date effectiveDate, int locationNumber, int buildingNumber, String originalAdditionalInterestName) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Remove Additional Interest", effectiveDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsLocationsRow(locationNumber);
        buildings.clickBuildingsBuildingEdit(buildingNumber);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.removeAdditionalInterest(originalAdditionalInterestName);
    }
	
	
	
	
	@Test(dependsOnMethods = { "removeLHAndAddNewOne" })	
	public void verifyPartialCancelFromChangingLH() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		//wait for policy change to come
        charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		//verify "partial cancel" is set yes for old LH canceling negative charges			
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(null, originalLHNumber, null, null, TransactionType.Policy_Change, null, null, null, null, LHPremium*(-1), null, true, originalLoanNumber, null, null, null);			
		}catch(Exception e){
			Assert.fail("couldn't find the cancelling negative charges for old LH from policy change.");
		}
	}
	@Test(dependsOnMethods = { "verifyPartialCancelFromChangingLH" })
    public void payInsuredAndMoveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);		
	}
	@Test(dependsOnMethods = { "payInsuredAndMoveClock" })
    public void removeLHBuilding() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(2);
	}
	@Test(dependsOnMethods = { "removeLHBuilding" })
	public void verifyCancellingChargeAndEarnedPremium() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		//wait for sencond policy change to come
        charge = new AccountCharges(driver);
        charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(60, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, myPolicyObj.busOwnLine.getPolicyNumber());
		
		//get LH earned premium amount
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		double LHEarnedPremium= NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceTableCellValue("Amount", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.LienholderOnset, null, null, null, null));
		
		acctMenu.clickBCMenuCharges();
		//verify "Partial Cancel" is set yes for canceling negative charge from the second LH removing
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), newLHNumber, null, null, TransactionType.Policy_Change, null, null, null, null, LHPremium*(-1), null, true, newLoanNumber, null, null, null);			
		}catch(Exception e){
			Assert.fail("couldn't find the cancelling credit from removing LH building.");
		}
		//verify "partial cancel" is set yes for earned charges 
		try{
			charge.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj.accountNumber, null, null, TransactionType.Policy_Change, null, null, null, null, LHEarnedPremium, null, true, null, null, null, null);			
		}catch(Exception e){
			Assert.fail("couldn't find the cancelling credit from LH change.");
		}
	}
}
