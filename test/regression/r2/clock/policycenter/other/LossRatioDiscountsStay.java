package regression.r2.clock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author sbroderick
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/128071644704d/detail/defect/182653317740">Rally Defect link</a>
 * @Description While I can't find requirements to state that the loss ratio discount should be applied at cancellation if applied at issuance, we should do this.
 * @DATE Dec 13, 2017
 */
@QuarantineClass
public class LossRatioDiscountsStay extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy cancelSquirePolObj, reinstateSquirePolObj, rewriteNewSquirePolObj, rewriteRemainderSquirePolObj, renewalSquirePolObj;

	private GeneratePolicy generateSquirePolicy() throws Exception {

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
			location2Property1.setBuildingAdditionalInterest(loc2Property1AdditionalInterest);
			locTwoPropertyList.add(location2Property1);
			locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
			
			SquireLiability liabilitySection = new SquireLiability();
			
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        return new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
					.withInsFirstLastName("Test", "Premium")
					.withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withPaymentPlanType(PaymentPlanType.Annual)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);		
		

	}
	
	private void futureDatedPolicyChange(GeneratePolicy policy) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(policy);
        StartPolicyChange changeMe = new StartPolicyChange(driver);
		PolicyInfoAdditionalNamedInsured newNamedInsured = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person);
//		changeMe.addAdditionalNamedinsured(newNamedInsured, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 53));
		
		changeMe.startPolicyChange("Add New Named Insured", null);
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).addAdditionalNamedInsuredNoStandardizing(true, newNamedInsured);
        new GenericWorkorder(driver).clickNext();
        changeMe.quoteAndIssue();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(policy);
        StartCancellation cancelMe = new StartCancellation(driver);
		cancelMe.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Insured Requested Cancel.", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);		
	}

    private void moveClock(int days) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
	}

/*  This method is commented in case it is decided to change the test and implement a BC cancel.
	private void runBcBatches() {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		runBatchProcess(BatchProcess.Invoice);
		runBatchProcess(BatchProcess.Invoice_Due);
		runBatchProcess(BatchProcess.BC_Workflow);
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
	}
*/
	
	private void checkPremium(GeneratePolicy policy, TransactionType transactionType) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(policy.underwriterInfo.underwriterUserName, policy.underwriterInfo.underwriterLastName, policy.squire.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
		double premiumByType = summaryPage.getTransactionPremium(transactionType, null);
		double issuancePremium = summaryPage.getTransactionPremium(TransactionType.Issuance, null);
		if(premiumByType>issuancePremium) {
			Assert.fail("Check the premium amounts, especially when the "+ transactionType.getValue() + " is greater than the issuance premium.");
		}
	}
		
	@Test
	public void testCancellation() throws Exception {	
		this.cancelSquirePolObj = generateSquirePolicy();
		futureDatedPolicyChange(cancelSquirePolObj);
		checkPremium(this.cancelSquirePolObj, TransactionType.Cancellation);
	}
	
	@Test
	public void testReinstate() throws Exception {
		this.reinstateSquirePolObj = generateSquirePolicy();
		futureDatedPolicyChange(reinstateSquirePolObj);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.reinstateSquirePolObj.underwriterInfo.underwriterUserName, this.reinstateSquirePolObj.underwriterInfo.underwriterPassword, this.reinstateSquirePolObj.squire.getPolicyNumber());
		StartReinstate reinstatePolicy = new StartReinstate(driver);
		reinstatePolicy.reinstatePolicy(ReinstateReason.Payment_Received, "Done");	
		checkPremium(this.reinstateSquirePolObj, TransactionType.Reinstatement);
	}
	
	@Test
	public void testRewriteNew() throws Exception {
		this.rewriteNewSquirePolObj = generateSquirePolicy();
		futureDatedPolicyChange(rewriteNewSquirePolObj);
		moveClock(32);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.rewriteNewSquirePolObj.underwriterInfo.underwriterUserName, this.rewriteNewSquirePolObj.underwriterInfo.underwriterPassword, this.rewriteNewSquirePolObj.squire.getPolicyNumber());
		StartRewrite rewriteMe = new StartRewrite(driver);
		rewriteMe.rewriteNewTerm(rewriteNewSquirePolObj);
		checkPremium(this.reinstateSquirePolObj, TransactionType.Rewrite_New_Term);
		
	}
	
	@Test
	public void testRewriteRemainder() throws Exception {
		this.rewriteRemainderSquirePolObj = generateSquirePolicy();
		futureDatedPolicyChange(rewriteRemainderSquirePolObj);
		moveClock(2);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.rewriteRemainderSquirePolObj.underwriterInfo.underwriterUserName, this.rewriteRemainderSquirePolObj.underwriterInfo.underwriterPassword, this.rewriteRemainderSquirePolObj.squire.getPolicyNumber());
		StartRewrite rewriteMe = new StartRewrite(driver);
		rewriteMe.rewriteNewTerm(rewriteRemainderSquirePolObj);
		checkPremium(this.reinstateSquirePolObj, TransactionType.Rewrite);
		
	}
	
	@Test
	public void testRenewal() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
			
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locTwoPropertyList.add(location2Property1);
		locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.renewalSquirePolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Test", "Premium")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolTermLengthDays(51)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);
		
		makePolicyRenewalPayment(renewalSquirePolObj);
		moveClock(21);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		checkPremium(renewalSquirePolObj, TransactionType.Renewal);	
	}

    public void makePolicyRenewalPayment(GeneratePolicy policy) throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		Login loginPage = new Login(driver);
		loginPage.login("sbrunson", "gw");

		BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
		accountTopMenu.menuAccountSearchAccountByAccountNumber(policy.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(new GuidewireHelpers(driver).getPolicyPremium(policy).getDownPaymentAmount(), policy.squire.getPolicyNumber());

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		new GuidewireHelpers(driver).logout();
	}


}
