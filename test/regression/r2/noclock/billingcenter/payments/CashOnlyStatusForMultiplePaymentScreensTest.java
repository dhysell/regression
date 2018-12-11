package regression.r2.noclock.billingcenter.payments;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsCountyCashPayment;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.desktop.actions.DesktopActionsNexusPayment;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.enums.PaymentRestriction;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import regression.r2.noclock.policycenter.issuance.TestIssuance;

/**
 * @Author jqu
 * @Requirement Test warning messages in Multiple/Nexus/County payments for policy with Payment Restriction as "Cash Only". Test warning messages in Multiple/Nexus/County payments for cancelled policy.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/19%20-%20UI%20-%20Policy%20Screens/19-03%20Cash%20Only%20File%20Marker.docx">Cash Only Status File Marker</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-07%20Multiple%20Payment%20Entry%20Wizard.docx">Multiple Payment Entry Wizard</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-08%20County%20Cash%20Payment%20Entry%20Wizard.docx">County Cash Payment Entry Wizard</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-09%20Nexus%20Payment%20Entry%20Wizard.docx">Nexus Payment Entry Wizard</a>
 * @Description
 * @DATE Dec 8, 2015
 */
public class CashOnlyStatusForMultiplePaymentScreensTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();	
	private BCDesktopMenu desktopMenu;
	private boolean policyCancelled=false;
	private Date currentDate;
	private String cashonlyWarning="is in Cash Only Status";
	private String cancellationWarning="has a canceled Policy Period. Please change the payment date to match the Postmarked Date";	
	
	private Map<String, Double>  multiple_nexus_county_payments = new LinkedHashMap<String, Double>() {	
		private static final long serialVersionUID = 1L;
		{
			this.put("Multiple Payment Entry", new Double(NumberUtils.generateRandomNumberInt(20, 100)));
			this.put("County Payment Entry", new Double(NumberUtils.generateRandomNumberInt(30, 125)));
			this.put("Nexus Payment Entry", new Double(NumberUtils.generateRandomNumberInt(25, 150)));				
		}};
		
	private void makeMultipleNexusCountyPayments(String theWarningMsg) throws Exception{
		if(policyCancelled)
			currentDate=DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		for(Object key: multiple_nexus_county_payments.keySet()){
			switch (key.toString()) {
			case "Multiple Payment Entry":				
				desktopMenu.clickDesktopMenuActionsMultiplePayment();
				DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);

                multiPayment.setMultiPaymentPolicyNumber(1, myPolicyObj.squire.getPolicyNumber());
				multiPayment.setMultiPaymentPaymentAmount(1, multiple_nexus_county_payments.get(key));
				multiPayment.clickNext();

				if(policyCancelled){
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)|| multiPayment.getMultiPaymentPaymentDate(1)!=null){
						Assert.fail("Either didn't find the warning or the Date is not empty in Multiple Payment Screen.");
					}
					else{
						
						multiPayment.setMultiPaymentPaymentDate(1, currentDate);
						multiPayment.clickNext();					
						multiPayment.clickFinish();					
					}
				}else{
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)){
						Assert.fail("Didn't find the warning message in Multiple Payment Screen.");
					}
					else{
						multiPayment.clickFinish();
					}
				}
				break;
			case "County Payment Entry":				
				desktopMenu.clickDesktopMenuActionsCountyCashPayment();
				DesktopActionsCountyCashPayment countyPayment = new DesktopActionsCountyCashPayment(driver);
                countyPayment.setCountyCashPolicyNumber(1, myPolicyObj.squire.getPolicyNumber());
				countyPayment.setCountyCashPaymentAmount(1, multiple_nexus_county_payments.get(key));
				countyPayment.selectCountyCashTableCountyCode(1, CountyIdaho.Ada);
				countyPayment.selectCountyCashTableOfficeNumber(1, "Eagle Office");
				countyPayment.clickDesktopActionsCountyCashPmtNext();
				if (policyCancelled) {
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)||!countyPayment.getDate(1).equals(" ")){
						Assert.fail("Either didn't find the warning or the Date is not empty in County Payment Screen.");
					}
					else{
						
						countyPayment.setDate(1, currentDate);
						countyPayment.clickDesktopActionsCountyCashPmtNext();					
						countyPayment.clickDesktopActionsCountyCashPmtFinish();					
					}
				}else{
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)){
						Assert.fail("Didn't find warning message in County Payment Screen.");
					}
					else{
						countyPayment.clickDesktopActionsCountyCashPmtFinish();
					}
				}
				break;
				
			case "Nexus Payment Entry":				
				desktopMenu.clickDesktopMenuActionsNexusPayment();
                DesktopActionsNexusPayment nexusPayment = new DesktopActionsNexusPayment(driver);
                nexusPayment.setPolicyNumber(myPolicyObj.squire.getPolicyNumber(), 1);
				nexusPayment.setPaymentAmount(1, multiple_nexus_county_payments.get(key));
				nexusPayment.setPaymentLocationInTable(1, PaymentLocation.NexusPayment);
				nexusPayment.setPaymentMethodInTable(1, PaymentInstrumentEnum.ACH_EFT);
				nexusPayment.clickNext();
				if (policyCancelled) {
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)||!nexusPayment.getDate(1).equals(" ")){
						Assert.fail("Either didn't find the warning or the Date is not empty in Nexus Payment Screen.");
					}
					else{					
						nexusPayment.setDate(1, currentDate);
						nexusPayment.clickNext();					
						nexusPayment.clickFinish();					
					}
				}else{
					if(!new GuidewireHelpers(driver).getFirstErrorMessage().contains(theWarningMsg)){
						Assert.fail("Didn't find the warning message in Nexus Payment Screen.");
					}
					else{
						nexusPayment.clickFinish();
					}
				}
				break;
			}			
		}
	}
		
	@Test	
	public void generate() throws Exception {
		TestIssuance myIssuance= new TestIssuance();
		myIssuance.testBasicIssuanceInsuredOnly();
		myPolicyObj=myIssuance.myPolicyObjInsuredOnly;		
	}
	
	@Test (dependsOnMethods = { "generate" })	
	public void changeToCashOnlyStatusAndVerifyFlag() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());
		//change the Payment Restriction to "Cash Only"
        BCPolicySummary pcSum = new BCPolicySummary(driver);
		pcSum.selectPaymentRestriction(PaymentRestriction.Cash_Only);
		if(!pcSum.policyCashOnlyStatusFlagExist())
			Assert.fail("Didn't find the Cash Only Status flag in Policy Summary Screen.");		
	}
	
	@Test(dependsOnMethods = { "changeToCashOnlyStatusAndVerifyFlag" })	
	public void makeMultiple_Nexus_CountyPaymentsAndVerify() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(),arUser.getPassword());
		desktopMenu = new BCDesktopMenu(driver);
		makeMultipleNexusCountyPayments(cashonlyWarning);
	}
	
	@Test(dependsOnMethods = { "makeMultiple_Nexus_CountyPaymentsAndVerify" })		
	public void cancelThePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartCancellation cancelPolicy = new StartCancellation(driver);
		cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Cancel Policy", DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), true);	
		policyCancelled=true;
	}
	
	@Test(dependsOnMethods = { "cancelThePolicy" })		
	public void makePaymentsAndVerifyCancellationWarning() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);		
		//wait for cancellation to come before making payment
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);
		BCTopMenu topMenu=new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		desktopMenu = new BCDesktopMenu(driver);
		makeMultipleNexusCountyPayments(cancellationWarning);			
	}
}
