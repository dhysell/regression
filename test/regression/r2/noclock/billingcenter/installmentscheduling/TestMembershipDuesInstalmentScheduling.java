package regression.r2.noclock.billingcenter.installmentscheduling;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement Membership dues must distribute a certain way and be paid in a certain order. This test makes sure that happens.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/41439116573">Rally Story US5512</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/18%20-%20UI%20-%20Account%20Screens/18-01%20Account%20Charges%20Screen.docx">Requirements Documentation For Charges Screen.</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/119808138580">Rally Story US11300</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-06%20Membership%20Dues%20Installment%20Scheduling.docx">Requirements Documentation For Membership Dues Installment Scheduling</a>
* @Description 
* @DATE Jan 20, 2016 Updated on 09/15/2017 to reflect the reversal of everything in this test.
*/
public class TestMembershipDuesInstalmentScheduling extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private int chosenPolicyLength = 0;
	
	public List<Integer> policyLengthOptions = new ArrayList<Integer>(){
		private static final long serialVersionUID = 1L;
	{
		this.add(365);
		this.add(180);
		this.add(455);
		this.add(300);
	}};

	
	@Test
	public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.chosenPolicyLength = NumberUtils.getRandomIntFromList(policyLengthOptions);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withPolTermLengthDays(this.chosenPolicyLength)
			.withInsPersonOrCompany(ContactSubType.Person)
			.withInsFirstLastName("MembDues", "Test")
			.withPolOrgType(OrganizationType.Individual)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withMembershipDuesOnAllInsureds()
			//.withRandomPaymentPlanType()
			.withPaymentPlanType(PaymentPlanType.Quarterly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void verifyMemebershipDuesScheduling() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);

		for (int i = 1; i <= new TableUtils(driver).getRowCount(invoices.getAccountInvoicesTable()); i++) {
			invoices.clickAccountInvoicesTableRowByRowNumber(i);
			try {
				if (i == 1) {
                    invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
				} else {
					try {
                        invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
						Assert.fail("A line in the invoices table other than the first line contained a line item for membership dues. This should not be the case.");
					} catch (Exception e) {
						getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
					}
				}
			} catch (Exception e) {
				Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected. Test Failed.");
			}
		}
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        multiPayment.makeMultiplePayment(myPolicyObj.busOwnLine.getPolicyNumber(), null, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured());
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
        invoices = new AccountInvoices(driver);

        invoices = new AccountInvoices(driver);
		invoices.clickAccountInvoicesTableRowByRowNumber(1);
		try {
            invoices.getAccountInvoiceChargesTableRow(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null);
		} catch (Exception e) {
			Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected, with membership dues paid first. Test Failed.");
		}
	}
	
	@Test(dependsOnMethods = { "verifyMemebershipDuesScheduling" })
	public void extendPolicyTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Month, 3), "Extend Policy");
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "extendPolicyTerm" })
	public void verifyLongerInvoicing() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);
		double membershipDuesAlreadyPaid = invoices.getMembershipDuesAlreadyPaid();
        if (myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured() - membershipDuesAlreadyPaid < 0) {
			Assert.fail("The amount paid for membership dues was more than what was originally charged. This should never happen.");
		}

		int j = 0;
		for (int i = 1; i <= new TableUtils(driver).getRowCount(invoices.getAccountInvoicesTable()); i++) {
			invoices.clickAccountInvoicesTableRowByRowNumber(i);
			if (invoices.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
				try {
					if (j == 0) {
                        invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
					} else {
						try {
                            invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
							Assert.fail("A line in the invoices table other than the first line contained a line item for membership dues. This should not be the case.");
						} catch (Exception e) {
							getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
						}
					}
				} catch (Exception e) {
					if (invoices.getInvoiceDueAmountByRowNumber(i) < 0) {
						getQALogger().info("The invoice item in question is negative, indicating a credit from a policy change. No membership dues will appear on this line.");
						if (j == 0) {
							j--;
						}
                    } else if (membershipDuesAlreadyPaid == myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured()) {
						getQALogger().info("The membership dues have all already been paid. The lack of a membership dues line item on the invoices table is expected.");
					} else {
						Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected. Test Failed.");
					}
				}
			}
			j++;
		}
	}
		
	@Test(dependsOnMethods = { "verifyLongerInvoicing" })
	public void shortenPolicyTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Month, -3), "Shorten Policy");
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "shortenPolicyTerm" })
	public void verifyShorterInvoicing() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);
		double membershipDuesAlreadyPaid = invoices.getMembershipDuesAlreadyPaid();
        if (myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured() - membershipDuesAlreadyPaid < 0) {
			Assert.fail("The amount paid for membership dues was more than what was originally charged. This should never happen.");
		}

		int j = 0;
		for (int i = 1; i <= new TableUtils(driver).getRowCount(invoices.getAccountInvoicesTable()); i++) {
			invoices.clickAccountInvoicesTableRowByRowNumber(i);
			if (invoices.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
				try {
					if (j == 0) {
                        invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
					} else {
						try {
                            invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, this.myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
							Assert.fail("A line in the invoices table other than the first line contained a line item for membership dues. This should not be the case.");
						} catch (Exception e) {
							getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
						}
					}
				} catch (Exception e) {
					if (invoices.getInvoiceDueAmountByRowNumber(i) < 0) {
						getQALogger().info("The invoice item in question is negative, indicating a credit from a policy change. No membership dues will appear on this line.");
						if (j == 0) {
							j--;
						}
                    } else if (membershipDuesAlreadyPaid == myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured()) {
						getQALogger().info("The membership dues have all already been paid. The lack of a membership dues line item on the invoices table is expected.");
					} else {
						Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected. Test Failed.");
					}
				}
			}
			j++;
		}
		new GuidewireHelpers(driver).logout();
	}
}
