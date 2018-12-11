package scratchpad.steve.holding;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;

import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;

import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;

import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Nov 15, 2018
* 
* 	Ensure that the drop down is available
	Ensure that you can choose one of the new addresses
	Ensure that after the new office number is selected that billing center updates with the correct information
	Ensure that the office number doesn't change from what was selected 
	Ensure that the payer assignment screen is updated to the correct office number after the change happens (screen shot before and after) 
*/
public class US17086_PermissionChangeToEnableLienAddressChange extends BaseTest {
	private GenerateContact lienholder = null;
	
	
	@Test
	public void testTransitionTeamAbleToEditAddress() throws Exception {
		
	     Config cf = new Config(ApplicationOrCenter.ContactManager);
	     WebDriver driver = buildDriver(cf);

        ArrayList<ContactRole> lienOneRolesToAdd = new ArrayList<ContactRole>();
        lienOneRolesToAdd.add(ContactRole.Lienholder);
        
        ArrayList<AddressInfo> addresses = new ArrayList<>();
        addresses.add(new AddressInfo(true));
        addresses.add(new AddressInfo(true));
        addresses.add(new AddressInfo(true));
        
        GenerateContact lienOne = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(lienOneRolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .withAddresses(addresses)
                .build(GenerateContactType.Company);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		
		AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(lienOne.companyName, addresses.get(0));
		propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Payer", "Assign")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	

		AbUsers abUser = AbUserHelper.getRandomUserByTitle("Transition Specialist");

		Login login = new Login(driver);
		login.loginAndSearchPolicyByAccountNumber(abUser.getUserName(), abUser.getUserPassword(), myPolicyObj.accountNumber);
		StartPolicyChange changeMe = new StartPolicyChange(driver);
		changeMe.startPolicyChange("Change Lienholder Address", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();

		GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
		building.clickEdit();
		
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		aiPage.clickBuildingsPropertyAdditionalInterestsLink(lienOne.companyName);
		aiPage.setPropertyAdditionalInterestAddressListing(lienOne.addresses.get(lienOne.addresses.size()-1).getLine1());
		Assert.assertTrue(aiPage.getPropertyAdditionalInterestAddressListing().contains(lienOne.addresses.get(lienOne.addresses.size()-1).getLine1()), "The Additional Interest Dropdown should exist for Transition Specialists and the Transition Specialist should be able to change the Lienholders address.");
			
		
		
		
		
//		Assert.assertTrue(testAddressIsEditable(), "Transition Team should be able to change the Lienholder address.");
	}
}
