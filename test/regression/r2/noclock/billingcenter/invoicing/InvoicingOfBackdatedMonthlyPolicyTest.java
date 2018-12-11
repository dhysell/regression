package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description   DE6591: Monthly policy backdated less than 15 days causes the first two invoices to be bundled together
* 					A monthly policy that is backdated, say 13 days, causes the first two invoices (not the down) to be bundled together even though the first invoice has enough time to bill.
* 					
* @DATE October 11, 2017
*/
public class InvoicingOfBackdatedMonthlyPolicyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy stdFirePolicy;
	private ARUsers arUser = new ARUsers();	

	@Test
	public void generateStandardFire() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

		int randNumber=NumberUtils.generateRandomNumberInt(1, 15);
        this.stdFirePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, -randNumber))
				.withInsFirstLastName("Guy", "Stdfire")				
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);						
	}	
	@Test(dependsOnMethods = { "generateStandardFire" })
	public void spreadDuesAndPayDownPayment() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), stdFirePolicy.accountNumber);

		//verify down payment date
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        invoice.verifyInvoice(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, InvoiceType.NewBusinessDownPayment, null, InvoiceStatus.Planned, stdFirePolicy.standardFire.getPremium().getDownPaymentAmount(), stdFirePolicy.standardFire.getPremium().getDownPaymentAmount());
		//verify second invoice dates
        Date oneMonthAfterEffectiveDate = DateUtils.dateAddSubtract(stdFirePolicy.standardFire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		if(DateUtils.dateAddSubtract(oneMonthAfterEffectiveDate, DateAddSubtractOptions.Day, -15).after(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter))){
			invoice.verifyInvoice(DateUtils.dateAddSubtract(oneMonthAfterEffectiveDate, DateAddSubtractOptions.Day, -15), oneMonthAfterEffectiveDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, null, null);
		}else{
            Date twoMonthsAfterEffectiveDate = DateUtils.dateAddSubtract(stdFirePolicy.standardFire.getEffectiveDate(), DateAddSubtractOptions.Month, 2);
			invoice.verifyInvoice(DateUtils.dateAddSubtract(twoMonthsAfterEffectiveDate, DateAddSubtractOptions.Day, -15), twoMonthsAfterEffectiveDate, null, InvoiceType.Scheduled, null, InvoiceStatus.Planned, null, null);			
		}		
	}
}
