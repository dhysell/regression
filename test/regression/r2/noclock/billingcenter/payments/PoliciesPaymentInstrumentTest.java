package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.common.BCCommonPaymentInstrument;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.driverConfiguration.Config;
import repository.gw.enums.BankAccountType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.RoutingNumbers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.RoutingNumbersHelper;

/**
* @Author jqu
* @Description US6012 Error validation on Policy Payment Instrument Screen
* 				Verify that the policies can create Payment Instruments correctly, and the Payment Instruments display in Policy summary page in correct format (i.e. 				Bus Chk (1234) Washington Federal)
* 				Verify the policies don't get each other's Payment Instrument.
* 				Verify that after override, the overriden policy gets the lead policy's Payment Instruments.
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/17%20-%20UI%20-%20Desktop%20Screens/13%20-%20Policy%20Payment%20Instrument/17-13%20Policy%20Payment%20Instrument.docx">Policy Payment Instrument</a>
* @Test Environment: 
* @DATE Jan 27, 2016
*/
public class PoliciesPaymentInstrumentTest extends BaseTest {
//	private GeneratePolicy myPolicyObj;		
	private WebDriver driver;
	private BCPolicySummary bcSummary;
	private ARUsers arUser = new ARUsers();
	private String policyNumber1, policyNumber2;
	private String bankAcctHoler="Jessica";
	private List<String> routingNumberList=new ArrayList<>();
	private List<String>bankNameList=new ArrayList<>();
	private List<String>bankAccountNumList=new ArrayList<>();
	private List<String>instrumentDisplayed=new ArrayList<>();
	
	//find 4 set of routing numbers and bank names, first two is for policy1 and the last two for policy2
    private void createPaymentInstruments(String policyNumber, BankAccountType type) {
		int i, j;
		if(policyNumber.equals(policyNumber1)){
			i=0;
			j=2;
		}
		else{
			i=2;
			j=routingNumberList.size();
		}
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		policySearchPC.searchPolicyByPolicyNumber(policyNumber);

        bcSummary = new BCPolicySummary(driver);
		while(i<j){
			bcSummary.clickEdit();
			bcSummary.clickPaymentInstrumentNew();
			BCCommonPaymentInstrument instrument = new BCCommonPaymentInstrument(driver);
			instrument.setPaymentMethod(PaymentInstrumentEnum.ACH_EFT);

			instrument.clickCopyPrimaryContactDetails();

			instrument.setAccountType(type);
			instrument.setBankAccountHoldersName(bankAcctHoler);
			instrument.setRoutingNumber(routingNumberList.get(i));
			instrument.setBankAccountNumber(bankAccountNumList.get(i));
			//create Payment Instruments list that should display in Policy Summary
			instrumentDisplayed.add(BankAccountType.Business_Checking.getBCValue()+" ("+(bankAccountNumList.get(i).substring(bankAccountNumList.get(i).length()-4)+") "+bankNameList.get(i)));

			instrument.clickOK();
			bcSummary.clickUpdate();
			//verify that after update, the payment instrument is in correct format
			//uncomment this part after Payment Instrument names are standardized
//			String defaultPmtInstrument=pcSummary.getPaymentInstrumentValue();					
//			if(!defaultPmtInstrument.toLowerCase().contains(instrumentDisplayed.get(i).toLowerCase())){
//				throw new GuidewireException(getCurrentURL(), policyNumber+" got wrong payment instrument after creating it.");
//			}
			i++;
		}
	}
	//create an account with two policies
	@Test
	public void generate() throws Exception {			
		
		policyNumber1= "08-246535-05";				
		policyNumber2= "08-246535-06";	
		
		List<RoutingNumbers> allRoutingNumber= RoutingNumbersHelper.getAllRoutingNumbers();
		
		int randomInt=NumberUtils.generateRandomNumberInt(50, 500);
		//randomly find 4 routing numbers and their institute names
		for(int i=0;i<4; i++){			
			routingNumberList.add(allRoutingNumber.get(randomInt+i*5).getRoutingNumber());			
			//use routing numbers as account numbers
			bankAccountNumList.add(allRoutingNumber.get(randomInt+i*5).getRoutingNumber());
			String institutionName=allRoutingNumber.get(randomInt+i*5).getInstitutionName();
			if(institutionName.contains(",")){
				institutionName=institutionName.substring(0, institutionName.indexOf(","));
			}
			if(institutionName.contains(" N.A.")){
					institutionName=institutionName.replace(" N.A.", "");
			}
			if(institutionName.contains(" CO.")){
				institutionName=institutionName.replace(" CO.", "");
			}
			if(institutionName.substring(institutionName.length()-3).equals(" NA")){
				institutionName=institutionName.replace(" NA", "");
			}
			if(institutionName.contains("  ")){
				institutionName=institutionName.replace("  ", " ");
			}	
			if(institutionName.contains("FIRST")){
				institutionName=institutionName.replace("FIRST", "1st");
			}
			if(institutionName.contains("First")){
				institutionName=institutionName.replace("First", "1st");
			}
			bankNameList.add(institutionName);			
		}
	}
//	@Test(dependsOnMethods = { "generate" })	
	public void createNewPaymentInstrumentsForPolicies() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		
		createPaymentInstruments(policyNumber1, BankAccountType.Business_Checking);
		createPaymentInstruments(policyNumber2, BankAccountType.Personal_Checking);
	}
//	@Test(dependsOnMethods = { "createNewPaymentInstrumentsForPolicies" })		
public void verifyPoliciesGetDifferentPaymentInstruments() throws Exception {
	Config cf = new Config(ApplicationOrCenter.BillingCenter);
	driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), policyNumber1);
    bcSummary = new BCPolicySummary(driver);
		bcSummary.clickEdit();
		//verify if each policy2's payment instrument list element is in policy1's list, if yes, then throw exception
		try{
			for(int i=2; i< instrumentDisplayed.size();i++){
				bcSummary.selectPaymentInstrument(instrumentDisplayed.get(i));
			}
			Assert.fail(policyNumber1+" got wrong payment instrument from "+policyNumber2);
		}catch(Exception e){
			getQALogger().info(policyNumber1+ " doesn't get "+policyNumber2 + "'s payment instrument, which is expected.");
		}
	}
	//overrid policy2 by policy1, then policy2 should get the payment instrument list of Policy
//	@Test(dependsOnMethods = { "verifyPoliciesGetDifferentPaymentInstruments" })		
	public void makeOverrideAndVerify() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber("su", "gw", policyNumber2);
        bcSummary = new BCPolicySummary(driver);
		bcSummary.updateInvoicingOverride();
		PolicySummaryInvoicingOverrides chgStream = new PolicySummaryInvoicingOverrides(driver);
		chgStream.selectOverridingInvoiceStream(policyNumber1);
		chgStream.clickNext();
		chgStream.clickFinish();
		//click away from Summary then clicy back to make the screen updated
        BCPolicyMenu bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuHistory();
		bcMenu.clickBCMenuSummary();
		//verify policy2's payment instrument list, they should be the same as policy1's
		bcSummary.clickEdit();
		//verify if each policy2's payment instrument list element is in policy1's list, if yes, then throw exception
		try{
			for(int i=0; i< 2;i++){
				bcSummary.selectPaymentInstrument(instrumentDisplayed.get(i));
			}			
		}catch(Exception e){
			Assert.fail(policyNumber2+" didn't get "+policyNumber1+"'s payment instrument list.");
		}
	}
}
