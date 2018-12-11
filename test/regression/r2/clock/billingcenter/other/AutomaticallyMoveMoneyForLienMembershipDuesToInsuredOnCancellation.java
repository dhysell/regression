package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsTransfers;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sreddy
 * @Requirement : US10589   AutomaticallyMoveMoneyForLienMembershipDuesToInsuredOnCancellation
 * @Description : 1. Issue a Lien policy with membership dues are paid by Lien
 * 2. On BC make Lien Payment
 * 3. Cancel Policy From PC
 * Expected : Membership Due amount is automatically moved to Insured
 * @DATE Mar 07, 2017
 */
public class AutomaticallyMoveMoneyForLienMembershipDuesToInsuredOnCancellation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;

	private ARUsers arUser = new ARUsers();
	private String lienholderNumber = null;
	private String lienholderLoanNumber = "LN12345";
	int numDaysToMove = 0;

	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber(this.lienholderLoanNumber);

		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
		lines.add(LineSelection.PropertyAndLiabilityLinePL);

        myPolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.City)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Cancel", "LH")
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        System.out.println("PolicyNumber - " + myPolicyObj.squire.getPolicyNumber());
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
	}

	
	
	@Test (dependsOnMethods = { "generatePolicy" })  //payLienholderAmount
    public void payLienholderAmount() throws Exception {
		numDaysToMove = 21;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, numDaysToMove);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges accountCharges = new AccountCharges(driver);
		WebElement getTable= accountCharges.getChargesOrChargeHoldsPopupTable();
        WebElement webEle = new TableUtils(driver).getRowInTableByColumnNameAndValue(getTable, "Loan Number",lienholderLoanNumber);
        String leinAccountNumber = new TableUtils(driver).getCellTextInTableByRowAndColumnName(getTable, new TableUtils(driver).getRowNumberFromWebElementRow(webEle), "Default Payer");
        this.lienholderNumber = leinAccountNumber;
        getQALogger().info(leinAccountNumber);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), leinAccountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, 100);
		numDaysToMove = 11;
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, numDaysToMove);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		numDaysToMove = 10;
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, numDaysToMove);

	}


	@Test(dependsOnMethods = { "payLienholderAmount" })
    public void CancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		//cancelPol.cancelPolicy(CancellationSourceReasonExplanation.BoundOnAccident, "Messup on Agent's Part", null,true);
		//cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherNSF, "Messup on Agent's Part", null,true);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Messup on Agent's Part", null,true);
		
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = { "CancelPolicyInPolicyCenter" })
    public void verifyFundTranseferScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPayments();
        accountMenu.clickAccountMenuPaymentsTransfers();

        AccountPaymentsTransfers lhFundTransferPage = new AccountPaymentsTransfers(driver);
		
		try {
            WebElement myRow = lhFundTransferPage.getAccountPaymentsTransfersTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, this.lienholderNumber, myPolicyObj.squire.getPolicyNumber(), this.lienholderLoanNumber, null, null, null, null);
				String transferedAmount = new TableUtils(driver).getCellTextInTableByRowAndColumnName(lhFundTransferPage.getTransfersTable(), new TableUtils(driver).getRowNumberFromWebElementRow(myRow), "Amount");
				getQALogger().info("On Cancellation Funds from LienAccount moved to InsuredAccount :- " + transferedAmount);
			} catch (Exception e) {

				Assert.fail("Funds Did not move from lien holder Account to Insured on Cancellation");
			}
		
		new GuidewireHelpers(driver).logout();		
	}
}
