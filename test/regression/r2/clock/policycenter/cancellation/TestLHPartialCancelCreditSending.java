package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPaymentsTransfers;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test verifies that when LH credit is transferred to the insured after a cancel, that it is only transferred once.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/128071644704d/detail/defect/169670924164">Rally Defect DE6758</a>
 * @Description
 * @DATE Dec 20, 2017
 */
@QuarantineClass
public class TestLHPartialCancelCreditSending extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private Underwriters underwriter = new Underwriters();
	private GeneratePolicy myPolicyObj = null;
	private String policyCancelDescription = "Policy Cancellation";
	private Date cancellationDate = null;

	@Test
	public void generatePolicy() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		AdditionalInterest loc2Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc2Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageC().setAdditionalValue(250000);
		propertyCoverages.getCoverageC().setValuationMethod(ValuationMethod.ActualCashValue);
		location2Property1.setPropertyCoverages(propertyCoverages);
		location2Property1.setBuildingAdditionalInterest(loc2Property1AdditionalInterest);
		locTwoPropertyList.add(location2Property1);
		locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
		
		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Full", "LH Cancel Sq")
                .withMembershipDuesForPNIPaidByLienholder(loc1Property1AdditionalInterest)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
    public void makeDownPayments() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), this.myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(this.myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());
        newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(this.myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.propertyAndLiability.locationList.get(1).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "makeDownPayments" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 45);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
	public void cancelPolicyInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.underwriter = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.underwriter.getUnderwriterUserName(), this.underwriter.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartCancellation cancellation = new StartCancellation(driver);
		this.cancellationDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, this.policyCancelDescription, this.cancellationDate, true);
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "cancelPolicyInPolicyCenter" })
    public void verifyChargesInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();


        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPaymentsTransfers();

        checkTransfersPage (0);
		checkTransfersPage (1);
		
		new GuidewireHelpers(driver).logout();
	}
	
	private void checkTransfersPage (int locationNumber) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		columnRowKeyValuePairs.put("Transaction Date", DateUtils.dateFormatAsString("MM/dd/yyyy", this.cancellationDate));
		columnRowKeyValuePairs.put("Source Account", this.myPolicyObj.squire.propertyAndLiability.locationList.get(locationNumber).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());
		columnRowKeyValuePairs.put("Source Loan Number", this.myPolicyObj.squire.propertyAndLiability.locationList.get(locationNumber).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		columnRowKeyValuePairs.put("Target Account", this.myPolicyObj.accountNumber);
        columnRowKeyValuePairs.put("Target Policy", this.myPolicyObj.squire.getPolicyNumber());

        AccountPaymentsTransfers transfers = new AccountPaymentsTransfers(driver);
		List <WebElement> possibleRowMatches = new TableUtils(driver).getRowsInTableByColumnsAndValues(transfers.getTransfersTable(), columnRowKeyValuePairs);
		if (possibleRowMatches.size() > 1) {
			Set<Double> amountsPerRow = new HashSet<Double>();
			for (WebElement roWebElement : possibleRowMatches) {
				if (!amountsPerRow.add(NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(transfers.getTransfersTable(), new TableUtils(driver).getRowNumberFromWebElementRow(roWebElement), "Amount")))) {
					Assert.fail("There was a duplicate transfer for the lienholder to the insured. This should not happen. Test failed.");
				}
			}
		}
	}
}