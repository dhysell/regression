package previousProgramIncrement.pi3_090518_111518.f276_BC_Document_Cleanup;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment;
import repository.gw.enums.InlandMarineTypes;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author JQU
* * @Requirement 	DE7869--Invoice should not have generated when an invoice goes billed and due the same day
* * @Acceptance criteria:
			Ensure that a shortage invoice does not print if the policy is cancelled. (Req. 13-03-11.A)
** @Steps to get there:
			Cancel a policy, on a policy that has earned premium.
Actual: Shortage bill printed
Expected : When a shortage invoice whose invoice date and payment due date are the same goes billed, a document should not be triggered.  (See requirement 13-03-11 E.)	
					
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/13%20-%20Printed%20Documents/03%20-%20Shortage%20Bill/13-03%20Shortage%20Bill%20Document%20Change%20Request.docx&action=default">Shortage Bill Document Change Request</a>
* @DATE September 10, 2018*/
@Test(groups = {"ClockMove"})
public class DE7869BillShouldNotGeneratedWhenShortageInvoiceDueSameDay extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private Underwriters uw;
	private BatchHelpers batchHelper;
	private void generatePolicy() throws Exception {

		 ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarineTypes.InlandMarine>();

         inlandMarineCoverageSelection_PL_IM.add(InlandMarineTypes.InlandMarine.FarmEquipment);
         IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler","Manly Farm Equipment", 30000);
         ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
         farmEquip.add(scheduledItem1);
         FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipment.IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm,IMFarmEquipment.IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
         ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
         allFarmEquip.add(imFarmEquip1);

         StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
         myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
         myStandardInlandMarine.farmEquipment = allFarmEquip;

         Config cf = new Config(ApplicationOrCenter.PolicyCenter);
 		driver = buildDriver(cf);
         
         myPolicyObj = new GeneratePolicy.Builder(driver)
                 .withProductType(ProductLineType.StandardIM)
                 .withLineSelection(LineSelection.StandardInlandMarine)
                 .withStandardInlandMarine(myStandardInlandMarine)
                 .withCreateNew(CreateNew.Create_New_Always)
                 .withInsPersonOrCompany(ContactSubType.Person)
                 .withInsFirstLastName("Shortage", "Bill")
                 .withPolOrgType(OrganizationType.Individual)
                 .withPaymentPlanType(PaymentPlanType.Monthly)
                 .withDownPaymentType(PaymentType.ACH_EFT)
                 .build(GeneratePolicyType.PolicyIssued);  
         driver.quit();
	}
	@Test
	public void payDownPayment() throws Exception {	
		generatePolicy();		
		
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		batchHelper = new BatchHelpers(cf);
		batchHelper.runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		batchHelper.runBatchProcess(BatchProcess.Invoice_Due);				
	}
	@Test(dependsOnMethods = {"payDownPayment"})
	public void increasePolicyPremiumByAddingFarmEquipment() throws Exception {	
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.standardInlandMarine.getPolicyNumber());
		
		StartPolicyChange policyChange = new StartPolicyChange(driver);

		IMFarmEquipmentScheduledItem newFarmItem = new IMFarmEquipmentScheduledItem("Circle Sprinkler","Manly Farm Equipment", 200000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquipList = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquipList.add(newFarmItem);
        FarmEquipment newFarmEquipToAdd = new FarmEquipment(IMFarmEquipment.IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,IMFarmEquipment.IMFarmEquipmentDeductible.Fifty, true, false, "Farm Equipment", farmEquipList);
  
		policyChange.addFarmEquipmentToStdIM(newFarmEquipToAdd, "Add Farm Equipment");		
	}
	@Test(dependsOnMethods = {"increasePolicyPremiumByAddingFarmEquipment"})
	public void verifyPolicyChange() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		BCCommonCharges	charge = new BCCommonCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(TransactionType.Policy_Change);
		//move days and then cancel policy to create Shortage Invoice
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 7);
	}
	@Test(dependsOnMethods = {"verifyPolicyChange"})
	public void cancelPolicy() throws Exception {			
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.standardInlandMarine.getPolicyNumber());
		StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancel policy", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);		
	}
	@Test(dependsOnMethods = {"cancelPolicy"})
	public void verifyShortageBillNotGenerated() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		batchHelper = new BatchHelpers(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		BCCommonCharges	charge = new BCCommonCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);
		//verify Shortage Invoice with same Invoice/Due dates is created
		accountMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);
		invoice.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.Shortage, null, null, null, null);
		batchHelper.runBatchProcess(BatchProcess.Invoice);
		accountMenu.clickBCMenuDocuments();
		BCCommonDocuments document = new BCCommonDocuments(driver);
		Assert.assertFalse(document.verifyDocument("Shortage Bill", DocumentType.Shortage_Bill, null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null), "The Shortage Bill should not be created if the Shortage Invoice and Due on same day.");				
	}
}
