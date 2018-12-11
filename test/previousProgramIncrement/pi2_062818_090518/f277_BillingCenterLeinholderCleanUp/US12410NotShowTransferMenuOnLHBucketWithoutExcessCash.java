package previousProgramIncrement.pi2_062818_090518.f277_BillingCenterLeinholderCleanUp;

import repository.bc.account.AccountPolicyLoanBalances;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;

/**
* @Author JQU
* @Requirement 	US12410 - Do not send membership Transactions records to federation for Dues charges that are already in effect
* 				Steps to get there:
					Go into a lienholder account.
					On the side bar click on "Policy Loan Balances".
					Click on a bucket that does not have "Excess Cash".
				Notes:
					When there is no excess cash on a bucket then on the button "Actions" > "Transfer" hide the transfer option. We are doing this because we do not want users trying to transfer money when there is no money. Also when they try to transfer money and there is no excess cash an error is thrown.		

* @DATE  July 2nd, 2018
* 
* */
public class US12410NotShowTransferMenuOnLHBucketWithoutExcessCash extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private String lienholderNumber;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	private double LHPremium;
	private String loanNumber = "LN12345";
	private double extraAmount = NumberUtils.generateRandomNumberInt(20, 200);
	
	private void quotePolicy() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		
		
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
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Property1AdditionalInterest.setLoanContractNumber(loanNumber);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "AdditionalInterest")
				.build(GeneratePolicyType.PolicyIssued);		
		lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
		LHPremium = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
		driver.quit();
	}
	@Test
	public void verifyTransferLinkShowOrHideAccordingToExcessCashAmount() throws Exception{
		quotePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPolicyLoanBalances();
		AccountPolicyLoanBalances loanBalance = new AccountPolicyLoanBalances(driver);
		
		try{
			loanBalance.clickActionsTransferLink(1);
			Assert.fail("Actions->Transfer link should not be available for LH without Excess cash.");
		}catch (Exception e) {
			getQALogger().info("Actions->Transfer link is not available for LH without Excess cash, which is expected.");
		}
	
		//pay LH with extra money and verify again
		
		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);		
		directPayment.makeDirectBillPaymentExecute(myPolicyObj.accountNumber, loanNumber, LHPremium+extraAmount);	
		
		//verify Transfer menu on LH bucket with excess cash.
		try{
			loanBalance.clickActionsTransferLink(1);			
		}catch (Exception e) {
			Assert.fail("Actions->Transfer link should be available for LH with Excess cash.");
		}		
	}
}
