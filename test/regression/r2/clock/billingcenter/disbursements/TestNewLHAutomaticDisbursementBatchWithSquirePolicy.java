package regression.r2.clock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactRole;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement The new LH Automatic Disbursement batch needs to automatically pull money in the LH unapplied bucket and create disbursements from it.
 * It also holds off any more disbursements from that bucket until the original disbursement is sent out.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/52167866315">Rally Story US7228</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-08%20LH%20Account%20Disbursement%20Requirements.docx">Requirements Documentation</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/127876448272d/detail/defect/135345371648">Rally Defect DE6096</a>
 * @Description This test creates a policy and then makes payments to unapplied funds. It then disburses it and makes sure that it doesn't create other disbursements until that one leaves.
 * @DATE Jul 20, 2017
 */
public class TestNewLHAutomaticDisbursementBatchWithSquirePolicy extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = null;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);
		
		AddressInfo additionalInterestAddress = new AddressInfo();
		additionalInterestAddress.setLine1("PO Box 711");
		additionalInterestAddress.setCity("Pocatello");
		additionalInterestAddress.setState(State.Idaho);
		additionalInterestAddress.setZip("83204");
		
		ArrayList<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		addressInfoList.add(additionalInterestAddress);
		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.withAddresses(addressInfoList)
				.build(GenerateContactType.Company);
		
		driver.quit();
		
		AdditionalInterest locationOnePropertyTwoAddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, additionalInterestAddress);
		locationOnePropertyTwoAddInterest.setNewContact(CreateNew.Create_New_Always);
		locationOnePropertyTwoAddInterest.setAddress(additionalInterestAddress);
		locationOnePropertyTwoAddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		locationOnePropertyTwoAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOnePropertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOnePropertyOne.setOwner(true);
		
		PLPolicyLocationProperty locationOnePropertyTwo = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOnePropertyTwo.setBuildingAdditionalInterest(locationOnePropertyTwoAddInterest);
		locationOnePropertyTwo.setOwner(true);
		
		locOnePropertyList.add(locationOnePropertyOne);
		locOnePropertyList.add(locationOnePropertyTwo);
		PolicyLocation locationOne = new PolicyLocation(locOnePropertyList);
		locationOne.setPlNumResidence(2);
		locationsList.add(locationOne);
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("AutoDisb", "LH")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		this.lienholderNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
		this.lienholderLoanNumber = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void payLienholderInvoice() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(this.myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, this.myPolicyObj.squire.getPremium().getTotalAdditionalInterestPremium());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();

        AccountPolicyLoanBalances policyLoanBalances = new AccountPolicyLoanBalances(driver);
		try {
            policyLoanBalances.getAccountPolicyLoanBalancesTableRow(myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, myPolicyObj.squire.getPremium().getTotalAdditionalInterestPremium(), null, this.myPolicyObj.squire.getPremium().getTotalAdditionalInterestPremium(), null, null, null, null);
		} catch (Exception e) {
			Assert.fail("The Payments cell did not update as expected on the Policy Loan Balances Table. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payLienholderInvoice" })
    public void payLessThanTwentyThreshold() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecuteWithoutDistribution(this.myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, 19.99);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuDisbursements();

        AccountDisbursements disbursements = new AccountDisbursements(driver);
		int disbursementsTableRowCount = disbursements.getDisbursementTableRowCount();
		
		if (disbursementsTableRowCount > 0) {
			Assert.fail("There was a disbursement generated after running the batch process, but the amount available to disburse was under the LH threshhold. This should not have created a disbursement. Test Failed.");
		}

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecuteWithoutDistribution(this.myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, .01);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Lienholder_Automatic_Disbursement);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();
        accountMenu.clickAccountMenuDisbursements();


        disbursements = new AccountDisbursements(driver);
		disbursementsTableRowCount = disbursements.getDisbursementTableRowCount();
		
		if (disbursementsTableRowCount < 1) {
			Assert.fail("There was no disbursement generated after running the batch process, but the amount available to disburse was over the LH threshhold. This should have created a disbursement. Test Failed.");
		}
		new GuidewireHelpers(driver).logout();
	}
}
