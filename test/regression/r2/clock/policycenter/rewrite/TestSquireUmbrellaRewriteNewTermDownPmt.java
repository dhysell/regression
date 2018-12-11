package regression.r2.clock.policycenter.rewrite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.summary.BCAccountSummary;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * US13621: Don't force user to click all Risk Analysis tabs to Quote or Submit
 * This test will fail as the latest code is not in REGR environment.
 */
/**
 * @Author skandibanda
 * @Requirement :DE5289:Rewrite new term on Umbrella should require down if unapplied amount in BC < Squire down + Umbrella down
 * @Description :
 * @DATE May 12, 2017
 */
@QuarantineClass
public class TestSquireUmbrellaRewriteNewTermDownPmt extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser;
	private Underwriters uw;
	private GeneratePolicy mySqPolicy;
	WebElement invoiceRow;
	private double secondInvoiceAmt, rewriteDPAmt;
	private double  unappliedAmount;
	
	@Test()
	public void testIssueUmbrellaPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySqPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("SUSAN", "REDWINE")
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_5000000);

        mySqPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        mySqPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
	}				
	
	@Test(dependsOnMethods = {"testIssueUmbrellaPol"})
	public void makeInsuredDownPayment() throws Exception {			
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySqPolicy.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySqPolicy, mySqPolicy.squire.getPremium().getDownPaymentAmount() + mySqPolicy.squireUmbrellaInfo.getPremium().getDownPaymentAmount(), mySqPolicy.squire.getPolicyNumber());

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();

        AccountInvoices myInvoices = new AccountInvoices(driver);
        List<Date> calculatedDueDate = myInvoices.calculateListOfDueDates(mySqPolicy.squire.getEffectiveDate(), mySqPolicy.squire.getExpirationDate(), PaymentPlanType.Quarterly);
			
		invoiceRow = myInvoices.getAccountInvoiceTableRow(null, calculatedDueDate.get(1),  null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, null, null);
		String secondInvoiceAmount = new TableUtils(driver).getCellTextInTableByRowAndColumnName(myInvoices.getAccountInvoicesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(invoiceRow), "Amount").replace("$", "");
		secondInvoiceAmt = Double.parseDouble(secondInvoiceAmount);
		
		// Move clock 93 days to make 1st installment invoice due, make it delinquent
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 93);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
	
		new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);
        // delay required
	
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required
	}
	  
	@Test(dependsOnMethods = {"makeInsuredDownPayment"})
	private void testPCPendingCancellationAndMoveClock() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolicy.squire.getPolicyNumber());

        PolicySummary pcSummary = new PolicySummary(driver);
		List<WebElement> pendingTrans = pcSummary.getPendingPolicyTransaction(TransactionType.Cancellation);
		if(pendingTrans.size() > 0){
			String transactionEffDate = pcSummary.getPendingPolicyTransactionByColumnName(TransactionType.Cancellation.getValue(), "Trans Eff Date");
			int noOfDays = DateUtils.getDifferenceBetweenDates(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateUtils.convertStringtoDate(transactionEffDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
			ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, (noOfDays+3));
            new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
            ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 40);
		}
		else{
			Assert.fail("Cancellation is not shown in PC!!!!!!!");
		}
		
	}
		
	@Test (dependsOnMethods = {"testPCPendingCancellationAndMoveClock"})
	 public void testRewriteNewTermSquirePolicy() throws Exception {
		
		//make installment payment due prior cancellation
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.mySqPolicy.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySqPolicy, secondInvoiceAmt, mySqPolicy.squire.getPolicyNumber());
        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuSummary();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolicy.accountNumber);

		StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startRewriteNewTerm();
//		PolicyMenu policyMenu = new PolicyMenu(driver);
				  
		//rewriteNewTerm 
//		policyMenu.clickMenuActions();
//		//		policyMenu.clickRewriteNewTerm();
//

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquireEligibility();
		GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.clickNext();
		GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.clickNext();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.setSquireGeneralFullTo(false);

		qualificationPage.setSquireHOFullTo(false, "Testing purpose");
		qualificationPage.clickPL_GLLosses(false);
		qualificationPage.clickPL_GLHazard(false);
		qualificationPage.clickPL_GLManufacturing(false);
		qualificationPage.clickQualificationGLhorses(false);
		qualificationPage.clickPL_GLLivestock(false);
		qualificationPage.setSquireAutoFullTo(false);
		for (int i = 0; i < 8; i++) {
			eligibilityPage.clickNext();
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickSectionIICoveragesTab();

		coverages.clickCoveragesExclusionsAndConditions();
		for (int i = 0; i < 8; i++) {
			eligibilityPage.clickNext();
        }
		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
		rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

	 } 
	
	@Test (dependsOnMethods = {"testRewriteNewTermSquirePolicy"})
	 public void testUmbrellaRewriteNewTermPolicy() throws Exception {	
	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolicy.squireUmbrellaInfo.getPolicyNumber());

		StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startRewriteNewTerm();
//		PolicyMenu policyMenu = new PolicyMenu(driver);
//				  
//		//rewriteNewTerm 
//		policyMenu.clickMenuActions();
//		//		policyMenu.clickRewriteNewTerm();
//
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQualification();

        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
		quals.setUmbrellaQuestionsFavorably();
		quals.setPL_UmbrellaPolicyHagerty(true);
		quals.clickQualificationNext();
		sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuSquireUmbrellaCoverages();
		GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
		covs.clickExclusionsConditions();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
		
		ArrayList<UnderlyingInsuranceUmbrella> underlyingInsurances = new ArrayList<UnderlyingInsuranceUmbrella>();
		UnderlyingInsuranceUmbrella undLyingIns1 = new UnderlyingInsuranceUmbrella();
		undLyingIns1.setCompany("TestCompany1");
		undLyingIns1.setPolicyNumber("1234567");
		undLyingIns1.setLimitOfInsurance("TestLimit1");
		underlyingInsurances.add(undLyingIns1);
		excConds.addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(underlyingInsurances);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
		rewritePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }
	
	@Test (dependsOnMethods = { "testUmbrellaRewriteNewTermPolicy" })	
	public void verifyRewriteInvoice() throws Exception {
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), mySqPolicy.accountNumber);

        //get unapplied amount
        BCAccountSummary summary = new BCAccountSummary(driver);
        unappliedAmount = summary.getUnappliedFundByPolicyNumber(mySqPolicy.squire.getPolicyNumber());
		System.out.println("unappliedAmount " + unappliedAmount);
	
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Automatic_Disbursement);
        // delay required

        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices myInvoices = new AccountInvoices(driver);
		WebElement myRow = myInvoices.getAccountInvoiceTableRow(null, null, null, InvoiceType.RewriteDownPayment, null, InvoiceStatus.Planned, null, null);
		String rewriteDPAmount = new TableUtils(driver).getCellTextInTableByRowAndColumnName(myInvoices.getAccountInvoicesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(myRow), "Amount").replace("$", "");
		rewriteDPAmt = Double.parseDouble(rewriteDPAmount);					
		
		if(unappliedAmount < rewriteDPAmt) {
			System.out.println("Addtional payment required for squire+umbrella invoice" + rewriteDPAmount);
			
	/*		invoiceRow = myInvoices.getAccountInvoiceTableRow(null, null,  null, InvoiceType.RewriteDownPayment, null, InvoiceStatus.Billed, null, null);
			String secondInvoiceAmount = TableUtils.getCellTextInTableByRowAndColumnName(myInvoices.getAccountInvoicesTable(), TableUtils.getRowNumberFromWebElementRow(invoiceRow), "Due").replace("$", "");
			secondInvoiceAmt = Double.parseDouble(secondInvoiceAmount);
*/
            NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
            newPayment.makeInsuredDownpayment(mySqPolicy, secondInvoiceAmt, mySqPolicy.squire.getPolicyNumber());
		}
		else 
			Assert.fail("Rewrite Invoice not created as expected");		
	}	
}

	