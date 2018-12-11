package regression.r2.clock.billingcenter.disbursements;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.bc.search.BCSearchPolicies;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description DE6012-Manually created disbursement getting reapplied instead of disbursing
 * Steps:
 * 1. Create a Standard Fire, Liability and Inland Marine
 * 2. Semi-Annual Payment Plan
 * 3. Pay insured down payment
 * 4. Move the clock a couple of days and Pay the down payment again. ( This would happen if maybe wife paid, and then husband unknowingly paid the same bill again,  now they want one of the payments back)
 * 5. This puts excess money in unapplied funds under the lead policy.
 * 6. Create a manual disbursement sending the 2nd payment back.
 * 7. Run "disbursement Batch"
 * <p>
 * Actual Result: $4.00 installment fee was disbursed, but the rest of the money was re applied to the policies "unapplied fund"
 * Expected result: the full amount requested sent back to the insured.
 * @DATE August 3rd, 2017
 */
@QuarantineClass
public class DisbursementGettingReappliedTest extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObject;//,stdLiabilityPolicy,stdIMPolicy;
	private ARUsers arUser = new ARUsers();
	private double threePoliciesTotalDownPayment;

    @Test
	public void generateStandardFireAndLiability() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        this.myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
				.withInsFirstLastName("Guy", "Stdfire")				
				.withPaymentPlanType(PaymentPlanType.Semi_Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
		locationsList1.add(new PolicyLocation());
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList1);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);

//		stdLiabilityPolicy = new GeneratePolicy.Builder(driver)
//				.withStandardLiability(myStandardLiability)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardFirePolicyUsedForStandardLiability(this.myPolicyObject, true)
//				.withPaymentPlanType(PaymentPlanType.Semi_Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);				
    }


    @Test(dependsOnMethods = {"generateStandardFireAndLiability"})
	public void generatetandardInlandMarine_FarmEquipment() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);

        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 100000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.Hundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myPolicyObject.standardInlandMarine = myStandardInlandMarine;
        myPolicyObject.stdFireLiabilityIM = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardIM, GeneratePolicyType.PolicyIssued);

//		stdIMPolicy = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardIM)
//				.withStandardFirePolicyUsedForStandardIM(myPolicyObject, true)
//				.withStandardInlandMarine(myStandardInlandMarine)
//				.withPaymentPlanType(PaymentPlanType.Semi_Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);		

        threePoliciesTotalDownPayment = myPolicyObject.standardFire.getPremium().getDownPaymentAmount() + myPolicyObject.standardLiability.getPremium().getDownPaymentAmount() + myPolicyObject.standardInlandMarine.getPremium().getDownPaymentAmount();
	}
	//override stdLiability and Inland Marine by stdFire
	@Test(dependsOnMethods = { "generatetandardInlandMarine_FarmEquipment" })
	public void overrideAndPayDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObject.standardLiability.getPolicyNumber());
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		//close stdLiablityPolicy trouble ticket, otherwise override is not allowed
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTkt = new BCCommonTroubleTickets(driver);
		troubleTkt.waitForTroubleTicketsToArrive(60);
		troubleTkt.closeFirstTroubleTicket();
		policyMenu.clickBCMenuSummary();
		//override stdLiablityPolicy by stdFirePolicy
        BCPolicySummary policySummary = new BCPolicySummary(driver);
		policySummary.updateInvoicingOverride();
		PolicySummaryInvoicingOverrides invoiceOverrides = new PolicySummaryInvoicingOverrides(driver);
        invoiceOverrides.overrideInvoiceStream(myPolicyObject.standardFire.getPolicyNumber());

        BCSearchPolicies searchPolicy = new BCSearchPolicies(driver);
        searchPolicy.searchPolicyByPolicyNumber(myPolicyObject.standardInlandMarine.getPolicyNumber());
		//close stdIMPolicy trouble ticket, otherwise override is not allowed
		policyMenu.clickBCMenuTroubleTickets();
		troubleTkt.waitForTroubleTicketsToArrive(60);
		troubleTkt.closeFirstTroubleTicket();

        //override stdIMPolicy by stdFirePolicy
		policyMenu.clickBCMenuSummary();
		policySummary.updateInvoicingOverride();
        invoiceOverrides.overrideInvoiceStream(myPolicyObject.standardFire.getPolicyNumber());
	}
	
	@Test(dependsOnMethods = { "overrideAndPayDownPayment" })
    public void payDownPaymentAndManuallyCreateDisbursementAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObject.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
        directBillPmt.makeInsuredDownpayment(myPolicyObject, threePoliciesTotalDownPayment, myPolicyObject.standardFire.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 2);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//make another payment of the same amount
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        directBillPmt.makeDirectBillPaymentExecuteWithoutDistribution(threePoliciesTotalDownPayment, myPolicyObject.standardFire.getPolicyNumber());
		//create disbursement
		acctMenu.clickAccountMenuActionsNewTransactionDisbursement();
		CreateAccountDisbursementWizard disbmtWizard = new CreateAccountDisbursementWizard(driver);
        disbmtWizard.setCreateAccountDisbursementWizardPolicyNumberDropDown(myPolicyObject.standardFire.getPolicyNumber());
		disbmtWizard.setCreateAccountDisbursementWizardUnappliedFund(myPolicyObject.standardFire.getPolicyNumber());
		disbmtWizard.setCreateAccountDisbursementWizardAmount(threePoliciesTotalDownPayment);
		disbmtWizard.setCreateAccountDisbursementWizardDueDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
		disbmtWizard.setCreateAccountDisbursementWizardReasonFor(DisbursementReason.Overpayment);
		disbmtWizard.clickNext();
		disbmtWizard.clickFinish();
		//verify the disbursement
		acctMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
        disbursement.verifyDisbursement(null, null, null, myPolicyObject.standardFire.getPolicyNumber(), null, null, threePoliciesTotalDownPayment, null, null, null);
		//run Disbursement batch process and verify the disbursement again
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Disbursement);
		acctMenu.clickBCMenuSummary();
		acctMenu.clickAccountMenuDisbursements();
        disbursement.verifyDisbursement(null, null, null, myPolicyObject.standardFire.getPolicyNumber(), null, null, threePoliciesTotalDownPayment, null, null, null);
	}
}
