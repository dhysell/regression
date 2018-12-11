package regression.r2.noclock.contactmanager.other;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.ClaimVendorType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class RequiredPhoneForVendors extends BaseTest {
	private WebDriver driver;
	private AbUsers claimsUser = null;
	private AbUsers policyServices = null;
	
	
	@Test
	public void testRequirePhoneForVendorsUsers() throws Exception{
		this.claimsUser = AbUserHelper.getRandomDeptUser("Claim");
		//The Claims user must have the Edit tax info permission.  Doug Alley does not.		
		if(this.claimsUser.getUserName().equals("dalley")){
			testRequirePhoneForVendorsUsers();
		}
		this.policyServices = AbUserHelper.getRandomDeptUser("Number");
		testRequirePhoneForVendors(claimsUser);
		testRequirePhoneForVendors(policyServices);
		
	}
	
	public void testRequirePhoneForVendors(AbUsers user) throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		
		new Login(driver).login(user.getUserName(), user.getUserPassword());
		int lcv = 1;
		while (lcv < 10){

            TopMenuAB getToSearch = new TopMenuAB(driver);
			getToSearch.clickSearchTab();

            AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
			searchMe.getVendor("J & J", lcv);

            ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
			if(contactPage.getVendorType().equals(ClaimVendorType.AutoTowingVendor.getValue()) ||contactPage.getVendorType().equals(ClaimVendorType.AutobodyRepairShop.getValue()) || contactPage.getVendorType().equals(ClaimVendorType.AutoTowingVendor.getValue()) ){
				if(contactPage.checkForErrorMessage("Missing phone on 1099 address - please contact claims to add the phone number to the 1099 addres")){
					changeSomething(user);
					break;
				}
			}
			lcv++;
		}
	}
	
	public void changeSomething(AbUsers user){
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		if(contactPage.checkForErrorMessage("Missing phone on 1099 address - please contact claims to add the phone number to the 1099 address")){
			contactPage.clickContactDetailsBasicsEditLink();
			if(user.getUserDepartment().contains("Claim")){
				contactPage.setBackupWithholdingDate1(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager));
			} else {
				String name = contactPage.getContactDetailsBasicsContactName();
				contactPage.setContactDetailsBasicsName(name);
			}
			String name = contactPage.getContactDetailsBasicsContactName();
			contactPage.setContactDetailsBasicsName(name);
			contactPage.clickContactDetailsBasicsUpdateLink();
            ErrorHandling validationResults = new ErrorHandling(driver);
			if(validationResults.checkIfValidationResultsExists()){
				Assert.assertTrue(validationResults.validationMessageExists("1099 address needs at least one phone number (Business, Work, Home, Mobile"),"Validation Results message should popup and require a phone.");
			}
            contactPage = new ContactDetailsBasicsAB(driver);
			if(user.getUserDepartment().contains("Number")){
				contactPage.clickContactDetailsBasicsEditLink();
			} else {
				contactPage.clickContactDetailsBasicsCancel();	
			}
			/*if(user.getUserRole().equals("Claims"))
			contactPage.clickContactDetailsBasicsCancel();	*/	
		}
	}
	
	@Test
	public void testRequired1099Address() throws Exception{
		this.claimsUser = AbUserHelper.getRandomDeptUser("Claim");
		this.policyServices = AbUserHelper.getRandomDeptUser("Number");
		if(!required1099Address(this.claimsUser)){
			Assert.fail("A 1099 Address Required Validation Message should be seen after removing the 1099 Address.");
		}
		if(required1099Address(this.policyServices)){
			Assert.fail("A 1099 Address Required Validation Message should be seen after removing the 1099 Address.");
		}
	}
	
	public boolean required1099Address(AbUsers user) throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		new Login(driver).login(user.getUserName(), user.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.getVendor("J & J", 1);
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.clickContactDetailsAdressesEditLink();
		addressPage.setContactDetailsAddressType(AddressType.Mailing);
		addressPage.clickContactDetailsAddressesUpdate();
        ErrorHandling handleErrors = new ErrorHandling(driver);
		List<WebElement> validationResults = handleErrors.getValidationMessages();
		boolean address1099Error = false;
		for(WebElement result : validationResults){
			if(result.getText().contains("1099 Address Required")){
				address1099Error = true;
			}
		}
        addressPage = new ContactDetailsAddressesAB(driver);
		if(new GuidewireHelpers(driver).checkIfElementExists("//*[contains(@id,':ABAddressesLV_tb:Edit-btnEl')]", 1000)){
			addressPage.clickContactDetailsAdressesEditLink();
		}
		addressPage.setContactDetailsAddressType(AddressType.TenNinetyNine);
		addressPage.clickContactDetailsAddressesUpdate();
		return address1099Error;		
	}

}
