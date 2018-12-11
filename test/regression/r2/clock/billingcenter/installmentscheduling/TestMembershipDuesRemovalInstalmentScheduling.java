//This test will fail until membership dues are assigned a charge group (Address Book UID) from Policy Center. Once work is completed there,
//I will come back and tweak this test.
package regression.r2.clock.billingcenter.installmentscheduling;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.StandardAddresses;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.StandardAddressesHelper;

/**
 * @Author bhiltbrand
 * @Requirement Membership dues must distribute a certain way and be paid in a certain order. This test makes sure that when dues are returned,
 * they are not spread out over remaining invoices, but instead returned as one charge.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/51465709039">Rally Story US7001</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-06%20Membership%20Dues%20Installment%20Scheduling.docx">Requirements Documentation</a>
 * @Description
 * @DATE Mar 20, 2016
 */
@QuarantineClass
public class TestMembershipDuesRemovalInstalmentScheduling extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();


	@Test
	public void generatePolicy() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		ArrayList<PolicyInfoAdditionalNamedInsured> additionalNamedInsuredList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		StandardAddresses standardAddress = StandardAddressesHelper.getRandomStandardAddress();
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setLine1(standardAddress.getAddress());
		addressInfo.setCity(standardAddress.getCity());
		addressInfo.setState(State.valueOfName(standardAddress.getState()));
		addressInfo.setZip(standardAddress.getZip());

		additionalNamedInsuredList.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Second", "Person", AdditionalNamedInsuredType.Partner, addressInfo) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
				this.setHasMembershipDues(true);
			}
		});

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("MembDues", "Test")
				.withPolOrgType(OrganizationType.Individual)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withMembershipDuesOnAllInsureds()
				.withANIList(additionalNamedInsuredList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
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
		int tableRowCount = new TableUtils(driver).getRowCount(invoices.getAccountInvoicesTable());
		for (int i = 1; i <= tableRowCount; i++) {
			invoices.clickAccountInvoicesTableRowByRowNumber(i);
			try {
				if (i == 1) {
					invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
				} else {
					try {
						invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
						Assert.fail("A line in the invoices table other than the first line contained a line item for membership dues. This should not be the case.");
					} catch (Exception e) {
						getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
					}
				}
				if (myPolicyObj.aniList.size() > 0) {
					for (PolicyInfoAdditionalNamedInsured additionalNamedInsured : myPolicyObj.aniList) {
						try {
							if (additionalNamedInsured.hasMembershipDues()) {
								if (i == 1) {
									invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, additionalNamedInsured.getPersonFullName(), null, ProductLineType.Businessowners, additionalNamedInsured.getMembershipDuesAmount(), null, null, null);
								} else {
									try {
										invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, additionalNamedInsured.getPersonFullName(), null, ProductLineType.Businessowners, additionalNamedInsured.getMembershipDuesAmount(), null, null, null);
										Assert.fail("A line in the invoices table other than the first line contained a line item for membership dues. This should not be the case.");
									} catch (Exception e) {
										getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
									}
								}
							}
						} catch (Exception e) {
							Assert.fail("The Invoice Charges Table did not contain the line for ANI membership dues as expected. Test Failed.");
						}
					}
				}
			} catch (Exception e) {
				Assert.fail("The Invoice Charges Table did not contain the line for PNI membership dues as expected. Test Failed.");
			}
		}

		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
		multiPayment.makeMultiplePayment(myPolicyObj.busOwnLine.getPolicyNumber(), null, myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);


		accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		invoices = new AccountInvoices(driver);

		invoices = new AccountInvoices(driver);
		invoices.clickAccountInvoicesTableRowByRowNumber(1);
		try {
			invoices.getAccountInvoiceChargesTableRow(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null);
			if (myPolicyObj.aniList.size() > 0) {
				for (PolicyInfoAdditionalNamedInsured additionalNamedInsured : myPolicyObj.aniList) {
					if (additionalNamedInsured.hasMembershipDues()) {
						try {
							invoices.getAccountInvoiceChargesTableRow(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, additionalNamedInsured.getPersonFullName(), null, ProductLineType.Businessowners, additionalNamedInsured.getMembershipDuesAmount(), additionalNamedInsured.getMembershipDuesAmount(), null, null);
						} catch (Exception e) {
							getQALogger().info("The invoice checked did not have a line for membership dues. This is correct in this instance.");
						}
					}
				}
			}
		} catch (Exception e) {
			Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected, with membership dues paid first. Test Failed.");
		}

		topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();


		desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		multiPayment = new DesktopActionsMultiplePayment(driver);
		multiPayment.makeMultiplePayment(myPolicyObj.busOwnLine.getPolicyNumber(), null, NumberUtils.round((myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() - myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount()), 2));
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyMemebershipDuesScheduling" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}

	@Test(dependsOnMethods = { "moveClocks" })
	public void removeMembershipDues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

		StartPolicyChange policyChange = new StartPolicyChange(driver);
		//		policyChange.removeMembershipDuesContact("Second Person");



		policyChange.startPolicyChange("Remove Membership Dues Contact", null);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.removeMembershipDuesContact("Second Person");

		policyChange.quoteAndIssue();





		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "removeMembershipDues" })
	public void verifyLongerInvoicing() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

		AccountInvoices invoices = new AccountInvoices(driver);
		double membershipDuesAlreadyPaid = invoices.getMembershipDuesAlreadyPaid();

		int j = 0;
		for (int i = 1; i <= new TableUtils(driver).getRowCount(invoices.getAccountInvoicesTable()); i++) {
			invoices.clickAccountInvoicesTableRowByRowNumber(i);
			if (invoices.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
				try {
					if (j == 0) {
						invoices.getAccountInvoiceChargesTableRow(null, null, myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Membership_Dues, null, myPolicyObj.pniContact.getFullName(), null, ProductLineType.Businessowners, myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmountForPrimaryNamedInsured(), null, null, null);
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
					} else if (membershipDuesAlreadyPaid == myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount()) {
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
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
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
					} else if (membershipDuesAlreadyPaid == myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount()) {
						getQALogger().info("The membership dues have all already been paid. The lack of a membership dues line item on the invoices table is expected.");
					} else {
						Assert.fail("The Invoice Charges Table did not contain the line for membership dues as expected. Test Failed.");
					}
				}
			}
			j++;
		}
	}
}
