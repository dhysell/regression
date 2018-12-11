package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;

/**
* @Author sbroderick
* @Requirement When an address is updated in the Policy Info page, the address will update to the Policy Members page and Membership Dues pages
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B8A4385BF-52BC-48B4-9E5C-89F9C1DDFC7C%7D&file=US16734%20-%20CM%20-%20Mailing%20Address%20Update.docx&action=default&mobileredirect=true">US16734 - CM - Mailing Address Update</a>
* @Description When an address is updated in the Policy Info page, the address will update to the Policy Members page and Membership Dues pages
* @DATE Oct 30, 2018
*/
public class US16734_MailingAddressUpdate extends BaseTest{
	
	
	@Test
	public void testAddressChangeCarryOver() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    
	    PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Wild", "Bear", AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
	    ani.setNewContact(CreateNew.Create_New_Always);
	    ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<>();
	    aniList.add(ani);  
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
		
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
	
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		
		
		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Mr", "Pni")
				.withANIList(aniList)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        
        polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        AddressInfo newPNIAddress = new AddressInfo(true);
        polInfo.changeAddress(newPNIAddress);
        polInfo.clickUpdate();
        polInfo.clickANI();
        
        GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);       
		ArrayList<String> aniAddresses = aniPage.getAddressListings();
		boolean found = false;
		for(String address : aniAddresses) {
			if(address.contains(newPNIAddress.getLine1())){
				found = true;
				aniPage.setContactEditAddressListing(newPNIAddress.getLine1());
			}
		}
		Assert.assertTrue(found, "The Primary Named insureds address was not found in the ANI's address List. Address line 1 is:" + myPolicy.pniContact.getAddress().getLine1());
		aniPage.clickUpdate();
		sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        
        GenericWorkorderPolicyMembers policyMembers = new GenericWorkorderPolicyMembers(driver);
        policyMembers.clickPolicyHolderMembersByName(myPolicy.pniContact.getFullName());
        AddressInfo policyMemberAddress = policyMembers.getDesignatedAddress();
        Assert.assertTrue(policyMemberAddress.getLine1().contains(newPNIAddress.getLine1()), "The PNI Address on the Policy Members page must match the PNI's changed address.");
        policyMembers.clickOK();
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();
        
        GenericWorkorderMembershipMembers members = new GenericWorkorderMembershipMembers(driver);
        members.clickEdit();
        AddressInfo membershipAddress = members.getDesignatedAddress();
        Assert.assertTrue(membershipAddress.getLine1().contains(newPNIAddress.getLine1()), "The address on the Membership page must match the new PNI address.");
        
	}
}
