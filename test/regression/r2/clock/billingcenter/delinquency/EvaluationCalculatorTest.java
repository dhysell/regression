package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountEvaluation;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author jqu
 * @Requirement Define Evaluation Calculator
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20User%20Interface/09%20-%20Account%20Evaluation%20Screen/17-09%20Account%20Evaluation%20Screen.docx">Evaluation Calculator</a>
 * @DATE Oct 19, 2015
 */
public class EvaluationCalculatorTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String lienholderNumber1, lienholderNumber2, lienholderLoanNumber1, lienholderLoanNumber2;
	private double lien1Premium;
	private BCAccountMenu acctMenu;
	private Date newDate;
	private AccountEvaluation acctEvaluation;
	private int delinquencyCount, partialCancelCount, errorCount=0;
	private boolean delinquencyHistory;
	private Underwriters uwUser;
	private ARUsers arUser;

    private void makePartialCancel(int bldgNum, String transactionDescription) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.uwUser.getUnderwriterUserName(), this.uwUser.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		if (transactionDescription == null) {
			policyChangePage.removeBuildingByBldgNumber(bldgNum);
		} else {
			removeBuildingByBldgNumber(bldgNum, transactionDescription);
		}
	}
    
    private void removeBuildingByBldgNumber(int bldgNum, String policyChangeDescription) throws Exception {
    	StartPolicyChange policyChangePage = new StartPolicyChange(driver);
    	policyChangePage.startPolicyChange(policyChangeDescription, null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.removeBuildingByBldgNumber(bldgNum);
        policyChangePage.quoteAndIssue();
    }
	
	private boolean verifyPartialCancelInBillingCenter(String accountNumber, String lienholderLoanNumber, String transactionDescription) throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuCharges();
        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilChargesFromTransactionDescriptionArrive(120, transactionDescription);
		boolean cancelExists = false;
		if (lienholderLoanNumber != null) {
			cancelExists = charges.verifyChargeAsPartialCancel(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), accountNumber, TransactionType.Policy_Change, null, transactionDescription,  lienholderLoanNumber);
		} else {
			cancelExists = charges.verifyChargeAsPartialCancel(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), accountNumber, TransactionType.Policy_Change, null, transactionDescription,  null);
		}
		return cancelExists;
	}
	
	@Test
	public void generate() throws Exception {
		// customizing location and building-3 buildings 1 insured with 2 different liens

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
				
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);		
		
		ArrayList<AdditionalInterest> loc1LNBldg3AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg3AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg3AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg3AdditionalInterests.add(loc1LNBldg3AddInterest);
		PolicyLocationBuilding loc1Bldg3 = new PolicyLocationBuilding();
		loc1Bldg3.setAdditionalInterestList(loc1LNBldg3AdditionalInterests);
		locOneBuildingList.add(loc1Bldg3);		
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));	
		
		// customizing the BO Line, uncheck the Equipment Breakdown
		AddressInfo address = new AddressInfo();
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities);
		PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		boLine.setAdditionalCoverageStuff(additionalCoverageStuff);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
		additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Real Additional Insured",AdditionalInsuredRole.CertificateHolderOnly, address));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("TestEvaluationCalculator")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(boLine)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		lienholderNumber1 = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(1)
				.getAdditionalInterestList()
				.get(0)
				.getLienholderNumber(); 
		lienholderNumber2 = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(2)
				.getAdditionalInterestList()
				.get(0)
				.getLienholderNumber();
		lienholderLoanNumber1 = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(1)
				.getAdditionalInterestList()
				.get(0)
				.getLoanContractNumber();
		lienholderLoanNumber2 = myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(2)
				.getAdditionalInterestList()
				.get(0)
				.getLoanContractNumber();
		lien1Premium=myPolicyObj.busOwnLine.locationList.get(0)
				.getBuildingList()
				.get(1)
				.getAdditionalInterestList()
				.get(0).getAdditionalInterestPremiumAmount();
	}
	
	@Test(dependsOnMethods = { "generate" })	
	public void makeInsuredDelinquent() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);			
		newDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 11);		
		ClockUtils.setCurrentDates(driver, newDate);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredDelinquent" })
    public void verifyEvaluationCount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuEvaluation();
		acctEvaluation = new AccountEvaluation(driver);
		delinquencyHistory = acctEvaluation.verifyDelinquencyHistory(newDate, myPolicyObj.accountNumber, OpenClosed.Open);		
		delinquencyCount = acctEvaluation.getNumberOfDelinquencies();		
		if(!delinquencyHistory || delinquencyCount!=1){
			System.out.println("The delinquency history or the count after insured delinquency is not correct.");
			errorCount++;
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyEvaluationCount" })
    public void payOneLienAndMakeTheOtherOneDelinquent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges acctChg = new AccountCharges(driver);
		acctChg.clickDefaultPayerNumberHyperlink(lienholderNumber1);
        //pay first lien, not pay the other lien
		newDate = DateUtils.dateAddSubtract(newDate, DateAddSubtractOptions.Day, 21);
		ClockUtils.setCurrentDates(driver, newDate);
        NewDirectBillPayment directBillPmt = new NewDirectBillPayment(driver);
        directBillPmt.makeLienHolderPaymentExecuteWithoutDistribution(lien1Premium, myPolicyObj.busOwnLine.getPolicyNumber(), lienholderLoanNumber1);
        new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "payOneLienAndMakeTheOtherOneDelinquent" })
    public void verifyEvaluationAfterOneLienGoDelinquent() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuEvaluation();
		acctEvaluation=new AccountEvaluation(driver);
		delinquencyHistory=acctEvaluation.verifyDelinquencyHistory(newDate, lienholderNumber2, OpenClosed.Open);//the second lien is delinquent		
		delinquencyCount=acctEvaluation.getNumberOfDelinquencies();		
		if(!delinquencyHistory || delinquencyCount!=2){
			System.out.println("The delinquency history or the count after one lienholder goes delinquent is not correct.");
			errorCount++;
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyEvaluationAfterOneLienGoDelinquent" })	
	public void makeInsuredAndLienPartialCancel() throws Exception {
		this.uwUser = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter);
		//lienholder partial cancel
		makePartialCancel(3, null);
		new GuidewireHelpers(driver).logout();
		boolean cancelExists = verifyPartialCancelInBillingCenter(lienholderNumber2, lienholderLoanNumber2, "remove a building");
		if (!(cancelExists)) {
			System.out.println("The partial cancel for the first lienholder did not reflect in Billing Center");
			errorCount++;
			cancelExists = false;
		}
		//insured partial cancel
		makePartialCancel(1, "remove a building 2");
		cancelExists = verifyPartialCancelInBillingCenter(myPolicyObj.accountNumber, null, "remove a building 2");
		if (!(cancelExists)) {
			System.out.println("The partial cancel for the first lienholder did not reflect in Billing Center");
			errorCount++;
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredAndLienPartialCancel" })	
	public void verifyEvaluationAfterInsuredAndLienPartialCancel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuEvaluation();
		acctEvaluation=new AccountEvaluation(driver);		
		partialCancelCount=acctEvaluation.getNumberOfPartialCancellations();		
		if(partialCancelCount!=2){
			System.out.println("The partial cancellation count after insured partial cancellation is not correct.");
			errorCount++;
		}		
		if (errorCount>0) {
			if (errorCount == 1) {
				Assert.fail("found " + errorCount + " error, please see output for details.");
			} else {
				Assert.fail("found " + errorCount + " errors, please see output for details.");
			}
		}
		new GuidewireHelpers(driver).logout();
	}	
}


