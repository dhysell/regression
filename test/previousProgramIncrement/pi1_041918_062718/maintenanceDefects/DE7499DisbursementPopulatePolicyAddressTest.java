package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.wizards.NewCreditWizard;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.CreditType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.pc.contact.ContactEditPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;
/**
* @Author JQU
* * @Requirement 	US14917 - Verify suspense payment activity works for both insured and LH
				Issue a policy, do a policy change to update the policy address
				BC account and policy should show two different addresses.
				Issue a disbursement to the insured 
				Scenario-----
				In BillingCenter log into an account.
				Click on the "Actions" button.
				Then click on "New Transaction" > "Credit"
				You are now on the screen "New Credit Wizard - Step 1 of 2" on the field "Credit Type" you can select any option form the drop down.
				Enter in the amount you wish to apply the credit for in the field "Amount".
				Then click the "Next" button.
				Then click the "Finish" button.
				You will be taken back to the "Disbursements" screen.
* @RequirementsLink <a href=""></a>
* @DATE May 18, 2018*/
public class DE7499DisbursementPopulatePolicyAddressTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private AddressInfo newAddress = new AddressInfo();
	private double creditAmount = NumberUtils.generateRandomNumberInt(20, 500);
	 
	private void generatePolicy() throws Exception {	
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();				
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);	
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();

//		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;      

		myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Disbursement", "Address")
				.build(GeneratePolicyType.PolicyIssued);
//		driver.quit();
	}
	
	@Test
	public void changePolicyAddress() throws Exception{
		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        generatePolicy();
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        String line1 = "144 Wilson St.";
        String city = "Pocatello";
        State state = State.Idaho;
        String zip = "83201";
        
        newAddress.setLine1(line1);
        newAddress.setCity(city);
        newAddress.setState(state);
        newAddress.setZip(zip);
//        StartPolicyChange change = new StartPolicyChange(driver);
        changeContactInfo(newAddress, myPolicyObj);       
	}
	
	
	public void changeContactInfo(AddressInfo newAddress, GeneratePolicy myPolicyObj) throws Exception {
		StartPolicyChange change = new StartPolicyChange(driver);
		change.startPolicyChange("Change Contact Info", null);

//        try {
        	change.clickWhenClickable(change.changePrimaryInsuredContact);
        	ContactEditPC editContact = new ContactEditPC(driver);
        	newAddress.setType(AddressType.Business);
        	editContact.setNewAddress(newAddress);
/*            String newButtonXpath= "//a[contains(@id, ':SelectedAddress:SelectedAddressMenuIcon') or contains(@id, ':AddressListingForNewContactsMenuIcon') or contains(@id, ':AddressListingMenuIcon')]";
            WebElement newButton = change.find(By.xpath(newButtonXpath));
            change.clickWhenClickable(newButton);
            change.clickWhenClickable(change.find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
           
            change.setWholeAddress(newAddress);

            Guidewire8Select addressType = change.select_AddressType();
            addressType.selectByVisibleTextPartial(AddressType.Business.getValue());
*/
            try {
            	change.setChangeReason("Test Policy Change");
            } catch (Exception e) {
            }
            // clickOK();
            change.clickUpdate();

            change.quoteAndIssue();

            myPolicyObj.pniContact.setAddress(newAddress);
//        } catch (Exception e) {
//            System.out.println("Error occured on 'changeContactInfo'.");
//            change.clickWithdrawTransaction();
//            e.printStackTrace();
//            Assert.fail("Error occured while trying change Contact Info.");
//        }
    }
	
	
	
	
	
	@Test(dependsOnMethods = { "changePolicyAddress" })	
	public void createCreditDisbursementAndVerifyAddress() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);			
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		//create credit
		accountMenu.clickAccountMenuActionsNewTransactionCredit();
		NewCreditWizard creditWizard = new NewCreditWizard(driver);
		creditWizard.createCredit(myPolicyObj.accountNumber, CreditType.Other, creditAmount);
		
		//verify credit disbursement address		
		accountMenu.clickAccountMenuDisbursements();
		AccountDisbursements disbursement = new AccountDisbursements(driver);
		assertTrue(disbursement.verifyDisbursement(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null, null, myPolicyObj.accountNumber, null, null, creditAmount, myPolicyObj.pniContact.getCompanyName(), null, null),
				"Couldn't find the disbursement from the credit.");
		
		String address1 = disbursement.getAddressLine1();
		String city = disbursement.getAddressCity();
		String state = disbursement.getAddressState();
		String zip = disbursement.getAddressPostalCode();				
		getQALogger().info("address1 from disbursement screen is:  "+ address1);
		getQALogger().info("city from disbursement screen is:  "+ city);
		getQALogger().info("state from disbursement screen is:  "+ state);
		getQALogger().info("zip from disbursement screen is:  "+ zip);
		if(!address1.contains(newAddress.getLine1()) || !city.contains(newAddress.getCity()) || !state.contains(newAddress.getState().getName()) || !zip.contains(newAddress.getZip())){
			Assert.fail("The address in UI is not the same as the policy new address.");
		}		
	}
}