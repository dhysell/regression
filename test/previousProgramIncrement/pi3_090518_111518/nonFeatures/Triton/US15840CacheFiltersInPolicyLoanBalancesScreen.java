package previousProgramIncrement.pi3_090518_111518.nonFeatures.Triton;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Requirement 	US15840-Cache filters in Policy Loan Balances screen
* 				Ensure that the field "Policy" saves the selected option. (Req. 12-03-15)
				Ensure that the field "Loan Number" saves the selected option. (Req. 12-03-16)				
* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame2.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-03%20LH%20Buckets%20-%20Policy%20Loan%20Balances%20Screen.xlsx&action=default"></a>
* @DATE September 15, 2018
*/
public class US15840CacheFiltersInPolicyLoanBalancesScreen extends BaseTest{
	private GeneratePolicy myPolicyPL1 = null, myPolicyPL2 = null;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	private String lienholderNumber;
	private String loanNumberPL1 = "LN12345", loanNumberPL2 = "LN222333";
	private double lienholderPremium1, lienholderPremium2;
	private void generateBOPWithLH() throws Exception {			
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
        driver.quit();        
        
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsListPL = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
		
		AdditionalInterest loc1Property1AdditionalInterestPL = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterestPL.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterestPL.setLoanContractNumber(loanNumberPL1);
		loc1Property1AdditionalInterestPL.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterestPL.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterestPL);
		locOnePropertyList.add(location1Property1);
		locationsListPL.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsListPL;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyPL1 = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
				.withInsFirstLastName("Cache", "Filters")
				.build(GeneratePolicyType.PolicyIssued);		
		lienholderNumber = myPolicyPL1.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		lienholderPremium1 = myPolicyPL1.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsListPL2 = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList2 = new ArrayList<PLPolicyLocationProperty>();	
		
		AdditionalInterest loc1Property1AdditionalInterestPL2 = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterestPL2.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterestPL2.setLoanContractNumber(loanNumberPL2);
		loc1Property1AdditionalInterestPL2.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterestPL2.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1PL2 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1PL2.setBuildingAdditionalInterest(loc1Property1AdditionalInterestPL2);
		locOnePropertyList2.add(location1Property1PL2);
		locationsListPL2.add(new PolicyLocation(locOnePropertyList2));		

		SquireLiability liabilitySectionPL2 = new SquireLiability();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiabilityPL2 = new SquirePropertyAndLiability();
        myPropertyAndLiabilityPL2.locationList = locationsListPL2;
        myPropertyAndLiabilityPL2.liabilitySection = liabilitySectionPL2;        

        Squire mySquire2 = new Squire();
        mySquire2.propertyAndLiability = myPropertyAndLiabilityPL2;

        myPolicyPL2 = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire2)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
				.withInsFirstLastName("Cache", "Filters2")
				.build(GeneratePolicyType.PolicyIssued);		
		lienholderNumber = myPolicyPL2.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		lienholderPremium2 = myPolicyPL2.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		driver.quit();   
	}	
	@Test
	public void verifyCache() throws Exception {	
		generateBOPWithLH();		

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		//wait for the second policy to come
		accountMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(60, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Issuance, myPolicyPL2.accountNumber);
		
		//verify when All Policies/Loan numbers are set 
		accountMenu.clickAccountMenuPolicyLoanBalances();		
		AccountPolicyLoanBalances loanBalance = new AccountPolicyLoanBalances(driver);
		
		assertTrue(loanBalance.verifyPolicyLoanBalance(myPolicyPL1.accountNumber, loanNumberPL1, lienholderPremium1, lienholderPremium1, null, null, null, null, null), "There should be a item for policy "+myPolicyPL1.squire.getPolicyNumber()+" with Loan Number of"+loanNumberPL1+" and Charges of $"+lienholderPremium1);;
		assertTrue(loanBalance.verifyPolicyLoanBalance(myPolicyPL2.accountNumber, loanNumberPL2, lienholderPremium2, lienholderPremium2, null, null, null, null, null), "There should be a item for policy "+myPolicyPL2.squire.getPolicyNumber()+" with Loan Number of"+loanNumberPL2+" and Charges of $"+lienholderPremium2);;
		//select policy 1 and verify
		loanBalance.selectPolicyFilter(myPolicyPL1.accountNumber);
		loanBalance.selectLoanNumberFilter(loanNumberPL1);
		accountMenu.clickBCMenuSummary();
		accountMenu.clickAccountMenuPolicyLoanBalances();
		assertTrue(loanBalance.verifyPolicyLoanBalance(myPolicyPL1.accountNumber, loanNumberPL1, lienholderPremium1, lienholderPremium1, null, null, null, null, null), "There should be a item for policy "+myPolicyPL1.squire.getPolicyNumber()+" with Loan Number of"+loanNumberPL1+" and Charges of $"+lienholderPremium1);;
		assertTrue(loanBalance.getPolicyLoanBalancesTableRowCount()==1, "There should be one row in Loan Balances tabel with Loan Number of"+loanNumberPL1+" and Charges of $"+lienholderPremium1);
		
		//select policy 2 and verify
		loanBalance.selectPolicyFilter(myPolicyPL2.accountNumber);
		loanBalance.selectLoanNumberFilter(loanNumberPL2);
		accountMenu.clickBCMenuSummary();
		accountMenu.clickAccountMenuPolicyLoanBalances();
		assertTrue(loanBalance.verifyPolicyLoanBalance(myPolicyPL2.accountNumber, loanNumberPL2, lienholderPremium2, lienholderPremium2, null, null, null, null, null), "There should be a item for policy "+myPolicyPL2.squire.getPolicyNumber()+" with Loan Number of"+loanNumberPL2+" and Charges of $"+lienholderPremium2);;
		assertTrue(loanBalance.getPolicyLoanBalancesTableRowCount()==1, "There should be one row in Loan Balances tabel with Loan Number of"+loanNumberPL2+" and Charges of $"+lienholderPremium2);
	}
}

