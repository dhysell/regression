package regression.r2.noclock.contactmanager.other;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsAccountAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class AccountPage extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private AbUsers user = null;

    @Test
    public void testPolicyNumberRemoval() throws Exception{
    	createPolicy();
    	this.user = AbUserHelper.getRandomDeptUser("Policy Services");		
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContactByAcct(user, myPolicyObj.accountNumber, myPolicyObj.pniContact.getLastName());
//		loginAndSearchContactByAcctNum(user, myPolicyObj.accountNumber, myPolicyObj.insFirstName);

        ContactDetailsBasicsAB myContact = new ContactDetailsBasicsAB(driver);
		myContact.clickContactDetailsBasicsEditLink();
		myContact.clickContactDetailsBasicsAccountsLink();

        ContactDetailsAccountAB acctPage = new ContactDetailsAccountAB(driver);
		acctPage.removeAccount(myPolicyObj.accountNumber);
		acctPage.clickUpdate();
		String msg = acctPage.getValidationError();
		System.out.println(msg);
		Assert.assertTrue(msg.contains("policy number as 10 digits are require"));
    }

    public void createPolicy() throws Exception {
    	// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("New", "Policy")				
				.withPolOrgType(OrganizationType.Individual)
                .withMembershipDuesOnPNI()
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
		
    }    
}

