package regression.r2.noclock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.search.BCSearchMenu;
import repository.bc.search.BCSearchPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE4008  Search payments screen, narrowing down by policy number
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/20%20-%20UI%20-%20Search%20Screens/20-01%20Payments%20Search%20Screen.docx ">Link Text</a>
 * @Description If you narrow your search down by policy number it will not show all payments made on that policy number.
 * @DATE Mar 14, 2017
 */
public class SearchPaymentScreenNarrowingDownByPolicyNumber extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private double ExecuteWithOutDisAmt=NumberUtils.generateRandomNumberInt(10, 50);
	GeneratePolicy myPolicyObj;
	
	
	@Test
	public void issueSquirePolicy() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		
		locToAdd.setPlNumAcres(10);
		locationsList.add(locToAdd);
		
		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("SearchPayment", "Scrn")
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
	}
	
	@Test(dependsOnMethods = { "issueSquirePolicy" })
	public void downPayment() throws Exception {	 		 
		//Downpayment with ExecuteWithDistribution and normal payment with ExecuteWithoutDistribution
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        directPayment.makeDirectBillPaymentExecuteWithoutDistribution(ExecuteWithOutDisAmt, myPolicyObj.squire.getPolicyNumber());
				
	}
	
	@Test(dependsOnMethods = { "downPayment" })	
	public void verifyPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();
		BCSearchMenu searchMenu=new BCSearchMenu(driver);
		searchMenu.clickSearchMenuPayments();
		BCSearchPayment payment = new BCSearchPayment(driver);
        payment.setBCSearchPaymentsPolicyNumber(myPolicyObj.squire.getPolicyNumber());
		payment.clickSearch();

		WebElement paymentTable = payment.getBCSearchPaymentsSearchResultsTable();
		WebElement paymentWithoutDistributionRow = new TableUtils(driver).getRowInTableByColumnNameAndValue(paymentTable, "Amount", Double.toString(ExecuteWithOutDisAmt));
		if (paymentWithoutDistributionRow != null) {
			String distributedYesOrNo = new TableUtils(driver).getCellTextInTableByRowAndColumnName(paymentTable,
					new TableUtils(driver).getRowNumberFromWebElementRow(paymentWithoutDistributionRow), "Distributed");
			//System.out.println(distributedYesOrNo);
			Assert.assertEquals(distributedYesOrNo, "No", "Distributed type is not NO");
			
		}
		else {
			Assert.fail("Row not found");
		}
				
	}

}
