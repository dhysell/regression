package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentRestriction;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Description : DE5748-- Unable to manually take out of cash only status
 * 					Standard Fire, Standard Liability, Standard Inland Marine fully insured billed 
					All billed together on the same invoice stream 
					Manually put in cash only status on the lead policy
					Try to manually take out of cash only status on the lead policy (this doesn't work-throws error)
 * @DATE June 12, 2017
 */
public class TakeOffCashOnlyRestrictionFromLeadPolicyOfStandardLines extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObject;
	private ARUsers arUser = new ARUsers();				
	private BCPolicySummary policySummary;


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
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);	
		
		// GENERATE POLICY
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

//		this.stdLiabilityPolicy = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withStandardFirePolicyUsedForStandardLiability(myPolicyObject, true)
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
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
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
        myPolicyObject.stdFireLiabilityIM = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardIM, GeneratePolicyType.PolicySubmitted);

//		stdIMPolicy = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardIM)
//				.withLineSelection(LineSelection.StandardInlandMarine)
//				.withStandardInlandMarine(myStandardInlandMarine)
//				.withStandardFirePolicyUsedForStandardIM(myPolicyObject, true)
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicySubmitted);		
	}
	@Test(dependsOnMethods = { "generatetandardInlandMarine_FarmEquipment" })
	public void overrideStdLiabAndIMByStdFire() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObject.standardLiability.getPolicyNumber());
		//close Std Liability Trouble Ticket before override it.
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.waitForTroubleTicketsToArrive(100);
		troubleTicket.closeFirstTroubleTicket();
		
		//override std liability by std fire
		policyMenu.clickBCMenuSummary();
		policySummary = new BCPolicySummary(driver);
        policySummary.overrideInvoiceStream(this.myPolicyObject.standardFire.getPolicyNumber());
		
		//override std inland marine by std fire
		BCSearchPolicies searchPolicy=new BCSearchPolicies(driver);
        searchPolicy.searchPolicyByPolicyNumber(this.myPolicyObject.standardInlandMarine.getPolicyNumber());
        policySummary.overrideInvoiceStream(this.myPolicyObject.standardFire.getPolicyNumber());
	}
	@Test(dependsOnMethods = { "overrideStdLiabAndIMByStdFire" })
	public void setAndTakeOffCashOnlyRestrictionAndVerify() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObject.standardFire.getPolicyNumber());
		//set Payment Restriction to Cash_Only
        policySummary = new BCPolicySummary(driver);
		policySummary.selectPaymentRestriction(PaymentRestriction.Cash_Only);
		if (!policySummary.getPaymentRestrictionValue().equals(PaymentRestriction.Cash_Only))
			Assert.fail("The Payment Restriction is not set correctly.");
		//take off Cash_Only restriction and verify
		policySummary.selectPaymentRestriction(PaymentRestriction.None);
		if(!policySummary.getPaymentRestrictionValue().equals(PaymentRestriction.None))
			Assert.fail("The Payment Restriction is not taken off correctly.");
	}	
}
