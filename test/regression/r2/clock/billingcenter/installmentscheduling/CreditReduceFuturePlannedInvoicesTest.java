package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
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
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Description DE6667 -- Credit reduced future planned invoices amounts
* 					Steps: 	Issue policy with insured and Lienholder
							on same day, payoff LH and insured Down Payment
							move 10 days
							make policy change, reduce LH coverage, then bill all LH premium to insured
* @DATE 
*/
public class CreditReduceFuturePlannedInvoicesTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private String loanNumber="LN12345";
	private String LHNumber;
	private ARUsers arUser = new ARUsers();
	private double LHPremium;
	private double newLimit = 100000;
	private List<Date> invoiceDateList = new ArrayList<Date>();
	
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
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding building2 = new PolicyLocationBuilding();
		building2.setBuildingLimit(250000);
		building2.setBppLimit(250000);
		building2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(building2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("MoveLHChargeToInsured")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolDuesCounty(CountyIdaho.Bonneville)				
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		LHNumber = myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void payInsuredAndLH() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoiceDateList = invoice.getListOfInvoiceDates();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.accountNumber);

        BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(LHNumber);			

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment = new NewDirectBillPayment(driver);
		newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber, LHPremium);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
	}
	@Test(dependsOnMethods = { "payInsuredAndLH" })
	private void reduceLHCoverageAndChangePayer() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("change coverage", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		//reduce LH coverage
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
		building.clickBuildingsBuildingEdit(2);
        building.setBuildingLimit(newLimit);
        building.setBuildingPersonalPropertyLimit(newLimit);
        building.clickOK();
        //bill LH premium to Insured
		sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.resetAllCoveragesBackToInsured();
		policyChangePage.quoteAndIssue();
	}
	@Test(dependsOnMethods = { "reduceLHCoverageAndChangePayer" })
	public void verifyInvoicesInBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		invoiceDateList.remove(0); //the first invoice was already paid
		for(Date eachInvDate: invoiceDateList){
			invoice.getAccountInvoiceTableRow(eachInvDate, null, null, null, null, InvoiceStatus.Planned, null, null).click();
            int invoiceChargesTableRowCount = invoice.getInvoiceChargesTableRowCount();
			for(int i=1; i<= invoiceChargesTableRowCount;i++){
				if(NumberUtils.getCurrencyValueFromElement(invoice.getInvoiceChargesTableCellValue(i, "Paid Amount")) > 0){
					Assert.fail("Planned Invoice got payment before it's changed to Billed.");
				}					
			}
		}		
	}
}
