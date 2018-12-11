package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsElectronicPaymentEntry;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/******
* @Author jqu
* @Description: US8034 PWS Multiple Payment Screen--PL A/R users must be able to enter payments for the public website on a desktop multiple payment screen. 
* We will change the Nexus Multiple Payment Screen to allow both NPP and PWS payments to be entered. 
* It will now be called the Electronic Multiple Payment Screen. It will allow users to change selected (like the county multiple payment screen) of both the payment method and the payment location
* @Steps: 
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/17-03%20Electronic%20Payment%20Entry%20Wizard.docx">17-03 Electronic Payment Entry Wizard</a>  
* @DATE November 01, 2016
*/
public class PWSPayment extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();		
	private double paymentAmount=123;
	private PaymentInstrumentEnum paymentInstrument=PaymentInstrumentEnum.ACH_EFT;
	private PaymentLocation paymentLocation=PaymentLocation.WebSite;

    @Test
	public void generate() throws Exception {
				
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();		
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("PaymentAllocationPlanAndPolicyLevelDelinqPlanTest")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
			
	}
	@Test(dependsOnMethods = { "generate" })	
	public void verifyPmtAllocPlanForNewInsuredAndLH() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		//make pws payment
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsElectronicPayment();
		DesktopActionsElectronicPaymentEntry electronicPmt = new DesktopActionsElectronicPaymentEntry(driver);
        electronicPmt.setPolicyNumber(1, myPolicyObj.busOwnLine.getPolicyNumber());
		electronicPmt.setPaymentMethodInTable(1, paymentInstrument);
		electronicPmt.setPaymentLocationInTable(1, paymentLocation);
		electronicPmt.setPaymentAmount(1, paymentAmount);
		electronicPmt.clickNext();
		electronicPmt.clickFinish();
		//verify the payment
		BCSearchAccounts search=new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		try{
			payment.getPaymentsAndCreditDistributionsTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, null, null, null, null, paymentLocation, null, null, paymentAmount, null, null);
		}catch(Exception e){
            Assert.fail("doesn't find the PWS payment " + "for " + myPolicyObj.busOwnLine.getPolicyNumber());
		}
	}

}
