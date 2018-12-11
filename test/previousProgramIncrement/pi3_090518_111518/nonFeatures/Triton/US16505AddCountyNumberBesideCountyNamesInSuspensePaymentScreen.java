package previousProgramIncrement.pi3_090518_111518.nonFeatures.Triton;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	US16505 - Add County number beside county names in suspense payment screen
* @Requirements 
* 				Acceptance criteria:
					Ensure that the county number is displayed behind the county name in the drop down. (Req. 12-12-F-21-1 through 12-12-F-21-45)
				Steps to get there:
					Select suspense payment from desktop
					Click New
					Enter date, amount, check/cash for payment method
					On the county drop down you will see all counties listed
* @DATE October 19, 2018*/
public class US16505AddCountyNumberBesideCountyNamesInSuspensePaymentScreen extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	private int suspenseAmount = NumberUtils.generateRandomNumberInt(20, 300);		
	private WebDriver driver;
	private Config cf;
	private void generatePolicy() throws Exception {		
		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();

		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
		// Farm Equipment
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
				"Manly Farm Equipment", 100000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

	
        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Country", "Number")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);        
	}
	@Test
	public void createSuspensePaymentAndVerifyCountyNumber() throws Exception{
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);	
		generatePolicy();

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());	
		Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
		CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(currentDate);
		suspensePmtWizard.setSuspensePaymentAmount(suspenseAmount);
		if(suspenseAmount%2 ==0){
			suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);
		}else{
			suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Check);
		}
		String countySelected = suspensePmtWizard.selectPaymentCountyRandom();
		if(!countySelected.contains("(") || !countySelected.contains(")")){
			Assert.fail("Didn't find County number beside county names");
		}
		int countyNumber =Integer.parseInt(countySelected.substring(countySelected.indexOf("(")+1, countySelected.indexOf("(")+4));
		if(countyNumber>100 || countyNumber<=0){
			Assert.fail("County number is not correct.");
		}	
		suspensePmtWizard.selectPaymentCountyOfficeRandom();
		suspensePmtWizard.setAccountNumber(myPolicyObj.fullAccountNumber);		
		suspensePmtWizard.clickCreate();					
		//verify the suspense payment
		
		int pageNumber=0;
		boolean foundRow = false;
		while(pageNumber<suspensePayment.getNumberPages() && !foundRow){
			try{
				suspensePayment.getSuspensePaymentTableRow(null, null, SuspensePaymentStatus.Open, null, myPolicyObj.accountNumber, null, null, null, null, (double)suspenseAmount, null);
				foundRow = true;
				
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				suspensePayment.goToNextPage();
			}			
		}
	
		assertTrue(foundRow, "didn't find the suspense payment with amount $"+suspenseAmount+"for Lienholder "+myPolicyObj.accountNumber);;
		
	}
}
