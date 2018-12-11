package regression.r2.noclock.billingcenter.disbursements;

/*import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import bc.desktop.DesktopFactory;
import bc.desktop.interfaces.DesktopDisbursements;
import bc.desktop.interfaces.IDesktopMenu;
import bc.desktop.interfaces.IDesktopSuspensePayments;
import bc.wizards.WizardFactory;
import bc.wizards.interfaces.CreateAccountDisbursementWizard;
import bc.wizards.interfaces.INewSuspensePaymentWizard;
import gw.enums.AdditionalInterestBilling;
import gw.enums.BusinessownersLine.SmallBusinessType;
import gw.enums.ContactSubType;
import gw.enums.CreateNew;
import gw.enums.DisbursementReason;
import gw.enums.GeneratePolicyType;
import gw.enums.OrganizationType;
import gw.enums.PaymentInstrument;
import gw.enums.PaymentPlanType;
import gw.enums.PaymentType;
import gw.enums.Status;
import gw.exception.GuidewireException;
import gw.generate.GeneratePolicy;
import gw.generate.custom.AdditionalInterest;
import gw.generate.custom.AddressInfo;
import gw.generate.custom.PolicyBusinessownersLine;
import gw.generate.custom.PolicyLocation;
import gw.generate.custom.PolicyLocationBuilding;
import gw.helpers.DateUtils;
import gw.helpers.NumberUtils;
import gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
*//**
* @Author jqu
* @Description US4330:  Suspense Payment Disbursements -- Provide Free Form Box on Editing Page for Suspense Payment Disbursements on insured and LH
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-06%20Suspense%20Payment%20Free-Form%20Disbursements.docx">Suspense Payment Free-Form Disbursements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-07%20Suspense%20Payment%20Check%20%20Apron%20Mapping.xlsx">Suspense Payment Check Apron Mapping</a>
* @DATE April 1st, 2016
* 
* ATTENTION: THIS TEST MAY STILL BE RELEVANT IN THAT WE SHOULD STILL BE VERIFYING THAT THE FREE-FORM TEXT BOX ON THE DISBURSEMENT PAGE IS AVAILABLE AND WORKING
* CORRECTLY. HOWEVER, WE NO LONGER SET UP LIENHOLDER DISBURSEMENTS THROUGH SUSPENSE ITEMS. THIS TEST WILL NEED A COMPLETE RE-WRITE!!!
*//*
import com.idfbins.driver.BaseTest;
@QuarantineClass
public class SuspensePaymentDisbursementFreeFormBoxTest extends BaseTest{
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();
	private double disburseAmount=NumberUtils.generateRandomNumberInt(20, 100);
	private String loanNumber="LN11111";
	private String lienholderNumber;
	private String specialHandling="whatever";	
	private String warningMsg="Apron Details Text Required";		
	private Date currentDate;
	private String transactionNumber;
	private DisbursementReason disburseReason=DisbursementReason.Overpayment;
	private String suspensePaymentDisbDetails="Detailed Disbursement Reason";
	private ArrayList<String> freeFormLinesInfoShouldBe=new ArrayList<String>();
	private String companyName="insuredName";
	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

	}
	private AddressInfo myAddress = new AddressInfo() {		
		{
			this.setLine1("123 5th St.");
			this.setCity("Pocatello");	
			this.setState(State.Idaho);
			this.setZip("83406-1357");			
	}};		
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);		
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));				

		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName(companyName)
				.withPolOrgType(OrganizationType.Partnership)
				.withPolDuesCounty(CountyIdaho.Bonneville)								
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		myPolicyObj.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();	
	}	
	@Test(dependsOnMethods = { "generate" })
	public void createSuspensePaymentDisbursementForInsured() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		currentDate=DateUtils.getCenterDate(ApplicationOrCenter.BillingCenter);
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		login(arUser.getUserName(), arUser.getPassword());
		IDesktopMenu desktopMenu=DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create Desktop Suspense Payment
		IDesktopSuspensePayments suspensePmt=DesktopFactory.getDesktopSuspensePaymentsPage();
		suspensePmt.clickNew();
				INewSuspensePaymentWizard suspensePayment=WizardFactory.getNewSuspensePaymentWizard();
		suspensePayment.createSuspensePayment(currentDate, disburseAmount, PaymentInstrument.Cash, null, myPolicyObj.accountNumber, null, null, null, null, null);
		//Disburse the Suspense Payment
				suspensePmt.setStatusFilter(Status.Open);
				WebElement suspensePaymentRow;
		suspensePaymentRow=suspensePmt.getSuspensePaymentsTableRow(currentDate, null, Status.Open, null, myPolicyObj.accountNumber, null, null, disburseAmount, null);
				int rowNumber=TableUtils.getRowNumberFromWebElementRow(suspensePaymentRow);
		transactionNumber=suspensePmt.getSuspensePaymentTransactionNumberByRowNumber(rowNumber);
				suspensePmt.clickSuspensePaymentTableCheckbox(rowNumber);
				suspensePmt.clickCreateDisbursement();
		CreateAccountDisbursementWizard disburseWizard=new CreateAccountDisbursementWizard(driver);
		disburseWizard.setCreateAccountDisbursementWizardDueDate(currentDate);
		disburseWizard.setCreateAccountDisbursementWizardReasonFor(disburseReason);
				disburseWizard.setCreateAccountDisbursementWizardPayTo(companyName);
				disburseWizard.fillOutAddress(myAddress);
				disburseWizard.clickNext();
				disburseWizard.clickFinish();
	}
	@Test(dependsOnMethods = { "createSuspensePaymentDisbursementForInsured" })	
	public void verifyDesktopDisbursementFreeFormBoxForInsured() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		login(arUser.getUserName(), arUser.getPassword());

		IDesktopMenu desktopMenu=DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create Desktop Suspense Payment
		IDesktopSuspensePayments suspensePmt=DesktopFactory.getDesktopSuspensePaymentsPage();
		suspensePmt.setStatusFilter(Status.Disbursed);
		suspensePmt.sortBySuspensePaymentsTableTitle("Payment Date");		
		suspensePmt.sortBySuspensePaymentsTableTitle("Payment Date");		
		WebElement disbursedRow;		
		disbursedRow=suspensePmt.getSuspensePaymentsTableRow(currentDate, null, null, transactionNumber, null, null, null, disburseAmount, null);
		int rowNumber=TableUtils.getRowNumberFromWebElementRow(disbursedRow);
				suspensePmt.clickSuspensePaymentTableApplyToLink(rowNumber);
		//verify the Free form box info
		DesktopDisbursements desktopDisbursement=DesktopFactory.getDesktopDisbursementsPage();		
		if(!desktopDisbursement.getFreeFormWholeDetails().equals(disburseReason.getValue()))
			throw new GuidewireException(getCurrentURL(), "The free form info is incorrect before edit.");		
		desktopDisbursement.clickDesktopDisbursementsEdit();
				//clear the form and verify the warning msg for blank form
		desktopDisbursement.clickCopyClearApronDetails();
				desktopDisbursement.clickDesktopDisbursementsUpdate();
		try{
			String banner=desktopDisbursement.getTopBanner();
			if(!banner.contains(warningMsg))
				throw new GuidewireException(getCurrentURL(), "doesn't find the corrent warning message.");			
		}catch(Exception e){
			throw new GuidewireException(getCurrentURL(), "doesn't find the warning message for the blank form.");
		}		
		desktopDisbursement.setDisbursementSpecialHandling(specialHandling);
		desktopDisbursement.clickCopyClearApronDetails();
				desktopDisbursement.clickDesktopDisbursementsUpdate();
				///go to disbursement screen to verify again
		desktopMenu.clickDesktopMenuSuspensePayments();	
				suspensePmt.setStatusFilter(Status.Disbursed);
				suspensePmt.clickSuspensePaymentTableApplyToLink(rowNumber);
				if(!desktopDisbursement.getFreeFormWholeDetails().replace(" ", "").replace("\n", "").equals(disburseReason+specialHandling))
			throw new GuidewireException(getCurrentURL(), "the free form info is incorrect after edit for the insured");	
	}
	@Test(dependsOnMethods = { "verifyDesktopDisbursementFreeFormBoxForInsured" })
	public void createDesktopSuspensePaymentDisbursementForLH() throws Exception {				
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		login(arUser.getUserName(), arUser.getPassword());
		IDesktopMenu desktopMenu=DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create Desktop Suspense Payment for LH
		IDesktopSuspensePayments suspensePmt=DesktopFactory.getDesktopSuspensePaymentsPage();
		suspensePmt.clickNew();
				INewSuspensePaymentWizard suspensePayment=WizardFactory.getNewSuspensePaymentWizard();
		suspensePayment.createSuspensePayment(currentDate, disburseAmount, PaymentInstrument.Cash, null, lienholderNumber, myPolicyObj.accountNumber, loanNumber, companyName, myAddress.getLine1(), null);
		//Disburse the Suspense Payment
				suspensePmt.setStatusFilter(Status.Open);
				WebElement suspensePaymentRow;
		suspensePaymentRow=suspensePmt.getSuspensePaymentsTableRow(currentDate, null, Status.Open, null, lienholderNumber, null, null, disburseAmount, null);
				int rowNumber=TableUtils.getRowNumberFromWebElementRow(suspensePaymentRow);
		transactionNumber=suspensePmt.getSuspensePaymentTransactionNumberByRowNumber(rowNumber);
				suspensePmt.clickSuspensePaymentTableCheckbox(rowNumber);
				suspensePmt.clickCreateDisbursement();
				CreateAccountDisbursementWizard disburseWizard=new CreateAccountDisbursementWizard(driver);
		disburseWizard.setCreateAccountDisbursementWizardDueDate(currentDate);
		disburseWizard.setCreateAccountDisbursementWizardReasonFor(disburseReason);
				disburseWizard.setCreateAccountDisbursementWizardDisbursementDetail(suspensePaymentDisbDetails);
		disburseWizard.clickNext();
				disburseWizard.clickFinish();

		//get the free form lines info should be		
		freeFormLinesInfoShouldBe.add(disburseReason.getValue());
		freeFormLinesInfoShouldBe.add(companyName);
		freeFormLinesInfoShouldBe.add("Lien # "+lienholderNumber+" Loan # "+loanNumber);		
		freeFormLinesInfoShouldBe.add(myAddress.getLine1());		
		freeFormLinesInfoShouldBe.add(suspensePaymentDisbDetails);							
	}
	@Test(dependsOnMethods = { "createDesktopSuspensePaymentDisbursementForLH" })	
	public void verifyLHSuspensePaymentDisbursementFreeFormBox() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		login(arUser.getUserName(), arUser.getPassword());	

		IDesktopMenu desktopMenu=DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuSuspensePayments();
		//create Desktop Suspense Payment
		IDesktopSuspensePayments suspensePmt=DesktopFactory.getDesktopSuspensePaymentsPage();
		suspensePmt.setStatusFilter(Status.Disbursed);
		suspensePmt.sortBySuspensePaymentsTableTitle("Payment Date");		
		suspensePmt.sortBySuspensePaymentsTableTitle("Payment Date");
		WebElement disbursedRow;		
		disbursedRow=suspensePmt.getSuspensePaymentsTableRow(currentDate, null, null, transactionNumber, null, null, null, disburseAmount, null);
		int rowNumber=TableUtils.getRowNumberFromWebElementRow(disbursedRow);
				suspensePmt.clickSuspensePaymentTableApplyToLink(rowNumber);
		////get the real free form info and verify the Free form box info
		DesktopDisbursements desktopDisbursement=DesktopFactory.getDesktopDisbursementsPage();		
		desktopDisbursement.clickDesktopDisbursementsEdit();
				ArrayList<String> realFormLinesInfo=new ArrayList<String>();
		realFormLinesInfo.add(desktopDisbursement.getFreeFormDisbursementReason());
		realFormLinesInfo.add(desktopDisbursement.getFreeFormInsuredName());
		realFormLinesInfo.add(desktopDisbursement.getFreeFormAccountAndLoanNumber());
		realFormLinesInfo.add(desktopDisbursement.getFreeFormChargeGroup());
		realFormLinesInfo.add(desktopDisbursement.getFreeFormDetailedDisbursementReason());
		
		verifyLists(freeFormLinesInfoShouldBe, realFormLinesInfo);
		//clear the form and verify the warning
		desktopDisbursement.clickCopyClearApronDetails();
				desktopDisbursement.clickDesktopDisbursementsUpdate();
		try{
			String banner=desktopDisbursement.getTopBanner();
			if(!banner.contains(warningMsg))
				throw new GuidewireException(getCurrentURL(), "doesn't find the corrent warning message.");			
		}catch(Exception e){
			throw new GuidewireException(getCurrentURL(), "doesn't find the warning message for the blank form.");
		}	
		//set special handling and verify the form info again
		desktopDisbursement.setDisbursementSpecialHandling(specialHandling);
		desktopDisbursement.clickCopyClearApronDetails();
				desktopDisbursement.clickDesktopDisbursementsUpdate();
				///go to disbursement screen to verify again
		desktopMenu.clickDesktopMenuSuspensePayments();	
				suspensePmt.setStatusFilter(Status.Disbursed);
				suspensePmt.clickSuspensePaymentTableApplyToLink(rowNumber);

		String wholeFormInfo=desktopDisbursement.getFreeFormWholeDetails();
		freeFormLinesInfoShouldBe.add(specialHandling);
		for(int i=0; i< freeFormLinesInfoShouldBe.size();i++){
			if(!wholeFormInfo.contains(freeFormLinesInfoShouldBe.get(i))){
				throw new GuidewireException(getCurrentURL(), "the free form box doesn't contain '"+freeFormLinesInfoShouldBe.get(i)+"' which it should.");
			}
		}
	}		
}
*/