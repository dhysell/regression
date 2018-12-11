package previousProgramIncrement.pi2_062818_090518.f277_BillingCenterLeinholderCleanUp;

import repository.bc.account.BCAccountMenu;
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
* @Requirement 	US7976 - LH Buckets - Revisit Account Actions Menu on LH Accounts
* 				1. "New Payment" option located under the lienholder > "Actions" button > "New Payment". 
					Ensure that "Payment Request" is hidden from the drop down. (Req. 17-15-01.4)
				2. "New Transaction" option is located under the lienholder > "Actions" button > "New Transaction".
					Ensure that "Disbursement" is not able for selections on the drop down menu from the Actions Menu. (Req. 17-15-02.2)
					Ensure that "Transfer" is not able for selections on the drop down menu from the Actions Menu. (Req. 17-15-02.3)
					Ensure that "Negative Write-Off" is not able for selections on the drop down menu from the Actions Menu. (Req. 17-15-02.4)
					Ensure that "Negative Write-Off Reversal" is not able for selections on the drop down menu from the Actions Menu. (Req. 17-15-02.5)
					Ensure that "Recapture" is not able for selections on the drop down menu from the Actions Menu. (Req. 17-15-02.6)
					Ensure that users must be able to select "Write-Off" from the account actions menu on the Lienholder account. (Req. 17-15-02.8)
* 				

* @DATE July 5th, 2018
* 
* */
public class US7976DisableLHMenuItems extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private String lienholderNumber;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	
	//Just Quote the policy	
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
		driver.quit();
	}
	@Test
	public void verifyRemovedLinks() throws Exception{
		quotePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		
		//verify that Disbursement, Transfer, Negative write-off,  Negative write-off reversal and Payment Request are removed from the Actions list
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		try{
			accountMenu.clickAccountMenuActionsNewTransactionDisbursement();
			Assert.fail("Actions->Disbursements should not be available for LH");
		}catch (Exception e) {
			getQALogger().info("Actions->Disbursements is not available for LH which is expected.");
		}
		
		try{
			accountMenu.clickActionsNewTransactionTransfer();
			Assert.fail("Actions->Transfer should not be available for LH");
		}catch (Exception e) {
			getQALogger().info("Actions->Transfer is not available for LH which is expected.");
		}
		
		try{
			accountMenu.clickActionsNewTransactionNegativeWriteoff();
			Assert.fail("Actions->Negative Writeoff should not be available for LH");
		}catch (Exception e) {
			getQALogger().info("Actions->Negative Writeoff is not available for LH which is expected.");
		}
		
		try{
			accountMenu.clickActionsNewTransactionNegativeWriteoffReversal();
			Assert.fail("Actions->Negative Writeoff Reversal should not be available for LH");
		}catch (Exception e) {
			getQALogger().info("Actions->Negative Writeoff Reversal is not available for LH which is expected.");
		}
		
		try{
			accountMenu.clickAccountMenuActionsNewPaymentRequest();
			Assert.fail("Actions->Payment Request should not be available for LH");
		}catch (Exception e) {
			getQALogger().info("Actions->Payment Request is not available for LH which is expected.");
		}
	}
}
