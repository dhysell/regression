package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.helpers.DateAddSubtractOptions;
import com.idfbins.testng.helpers.DateUtils;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Description : DE4591--15-day invoice lead time for Semi-Annual Policy
 * 					Bind a 12-month semi-annual standard fire policy
					Bind a 12-month annual standard liability policy
					Bind a 12-month monthly standard inland marine policy
					Do not override invoice streams
					Actual Result: An Invoice on the first policy has a 15-day lead time on the scheduled invoice
					Expected Result: The scheduled invoice for a semi-annual payment plan should have a 20-day lead time
 * @DATE July 7, 2017
 */
public class LeadTimeDefectOnStdFireStdLiabilityStdIM extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObject;
	private ARUsers arUser = new ARUsers();	

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

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
				.withInsFirstLastName("Guy", "Stdfire")
				.withPaymentPlanType(PaymentPlanType.Semi_Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);	
		driver.quit();
		
		// GENERATE POLICY
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
		locationsList1.add(new PolicyLocation());

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList1);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicySubmitted);

//		new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withStandardFirePolicyUsedForStandardLiability(this.myPolicyObject, true)
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicySubmitted);
	}
	
	@Test(dependsOnMethods = { "generateStandardFireAndLiability" })
	public void generatetandardInlandMarine_FarmEquipment() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);

		// FPP		
		//Scheduled Item for 1st FPP
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 500000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.Hundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myPolicyObject.standardInlandMarine = myStandardInlandMarine;
        myPolicyObject.lineSelection.add(LineSelection.StandardInlandMarine);
        myPolicyObject.paymentPlanType = PaymentPlanType.Monthly;
        myPolicyObject.downPaymentType = PaymentType.Cash;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardIM, GeneratePolicyType.PolicySubmitted);

//		stdIMPolicy = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardIM)
//				.withStandardInlandMarine(myStandardInlandMarine)
//				.withStandardFirePolicyUsedForStandardIM(this.myPolicyObject, true)
//				.withInsFirstLastName("farm", "equipment")
//				.withPaymentPlanType(PaymentPlanType.Monthly)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicySubmitted);		
	}
	@Test(dependsOnMethods = { "generatetandardInlandMarine_FarmEquipment" })
	public void verifyLeadTimeForStdFire() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObject.accountNumber);
		//wait the last policy to come to BillingCenter
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges acctCharge = new AccountCharges(driver);
        acctCharge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(60, repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Issuance, myPolicyObject.standardInlandMarine.getPolicyNumber());
		//verify lead time for standard fire is 20 days
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices acctInvoice = new AccountInvoices(driver);
        acctInvoice.setInvoiceStream(myPolicyObject.standardFire.getPolicyNumber());
		Date nextInvoiceDueDate = DateUtils.dateAddSubtract(repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 6);
		acctInvoice.verifyInvoice(DateUtils.dateAddSubtract(nextInvoiceDueDate, DateAddSubtractOptions.Day, -20), nextInvoiceDueDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, null, null);
	}
}