package previousProgramIncrement.pi1_041918_062718.f164_PolicyChangePaymentPreview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import repository.gw.helpers.BillingCenterHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author sgunda
* @Requirement US15020 - Payment Preview Policy Change (PC, earned/unearned)
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/userstory/216643797644">US15020 - Payment Preview Policy Change (PC, earned/unearned)</a>
* @DATE May 11, 2018
*/


@Test(groups = {"ClockMove"})
public class US15020PaymentPreviewPolicyChangeNoExceptions extends BaseTest {
	public GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private AccountInvoices acctInvoice;
	private Date policyChangeDate;
	private List<Double> paymentPreviewAmount , invoicesAmount ;
	private double propertyLimit ;
	private WebDriver driver;


	private List<Double> quoteAndGetInvoicesListAndIssue() throws Exception {
		StartPolicyChange policyChange = new StartPolicyChange(driver);
		List<Double> invoiceAmounts;
		GenericWorkorder genericWO = new GenericWorkorder(driver);
		genericWO.clickGenericWorkorderQuote();
		ErrorHandlingHelpers errorHandlingHelpers = new ErrorHandlingHelpers(driver);
		if (errorHandlingHelpers.errorExistsValidationResults()) {
			policyChange.systemOut("There was an error on the validation results page. going back to trigger automatic fixes. This was the error (for debugging purposes): " + errorHandlingHelpers.getValidationResultsErrorText());
			genericWO.clickGenericWorkorderBack();
			genericWO.clickGenericWorkorderQuote();
		}

		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.hasBlockBind()) {
			GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
			try {
				riskAnalysis.handleBlockSubmitForPolicyChangeWithSameLogin(driver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();

		GenericWorkorderPayment getLinkPreviewLink = new GenericWorkorderPayment(driver);
		getLinkPreviewLink.clickPreviewPayment();

		invoiceAmounts = getLinkPreviewLink.getListOfInvoiceAmounts();
		getLinkPreviewLink.clickReturnToPayment();

		policyChange.clickIssuePolicy();

		return invoiceAmounts;
	}


	@Test
    public void generate() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();
		
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		
		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withInsFirstLastName("US15020","Preview")
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.ACH_EFT)
				.build(GeneratePolicyType.PolicyIssued);

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		new BillingCenterHelper(driver).loginAsRandomARUserAndVerifyIssuancePolicyPeriod(myPolicyObj);
        
    }

	@Test(dependsOnMethods = { "generate" })
   	public void doAPolicyChangeToCreateShortage() throws Exception {	
    	
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		 if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)%2 ==0) {
			 propertyLimit = 200000.00;
	     }else {	
	    	 propertyLimit = 50000.00;
	     }
		paymentPreviewAmount = changePLPropertyCoverageAndGetPreviewInvoiceList(propertyLimit, policyChangeDate);

    }
    
    private List<Double> changePLPropertyCoverageAndGetPreviewInvoiceList(double propertyLimit, Date effectiveDate) throws Exception {
    	StartPolicyChange policyChange = new StartPolicyChange(driver);
    	List<Double> getInvoiceList ;
    	policyChange.startPolicyChange("change coverage", effectiveDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        propertyCoverages.setCoverageALimit(propertyLimit);
       // propertyCoverages.setCoverageCLimit(propertyLimit);
        getInvoiceList = quoteAndGetInvoicesListAndIssue();
       
        return getInvoiceList ;
    }
    

    @Test(dependsOnMethods = { "doAPolicyChangeToCreateShortage" })	
	public void verifyInvoice() throws Exception {			
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        Assert.assertTrue(new AccountCharges(driver).waitUntilChargesFromPolicyContextArrive(TransactionType.Policy_Change), "Policy change charges has not make it to BC, test can not continue");
		
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		invoicesAmount = acctInvoice.getListOfInvoiceDueAmounts();
		if(!invoicesAmount.equals(paymentPreviewAmount)) {
			System.out.println("Amounts from Invoice page in BC are : "+ invoicesAmount);
			System.out.println("Amounts from Payment preview page in PC are " + paymentPreviewAmount);
			Assert.fail("Preview amount and invoice amount does not match , Please investigate with above printed amount ");
		} 
	}
    
}
