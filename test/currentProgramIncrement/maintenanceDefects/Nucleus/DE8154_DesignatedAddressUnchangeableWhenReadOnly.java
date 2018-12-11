package currentProgramIncrement.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.contact.ContactDetailsPC;
import repository.pc.sidemenu.SideMenuPC;

/**
* @Author sbroderick
* @Requirement The designated address on a contact should not be able to be changed unless through a workorder.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B6417D3B8-8822-4FF8-8C7A-8BF671B3C8AD%7D&file=DE8154%20-%20CM%20-%20Designated%20Address%20can%20be%20changed%20when%20Policy%20is%20in%20Read%20Only%20Status.docx&action=default&mobileredirect=true">ContactManager Requirements Documentation</a>
* @Description 
* @DATE Nov 16, 2018
*/
public class DE8154_DesignatedAddressUnchangeableWhenReadOnly extends BaseTest{
	
	@Test
	public void testDesignatedAddressUnchangeableWhenPolicyIsReadOnly() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    		
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
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);	
		
		
		Login login = new Login(driver);
		login.loginAndSearchPolicyByAccountNumber(myPolicy.agentInfo.agentUserName, myPolicy.agentInfo.agentPassword, myPolicy.accountNumber);
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsContacts();
		
		ContactDetailsPC contactDetailsPage = new ContactDetailsPC(driver);
		Assert.assertFalse(contactDetailsPage.designatedAddressDropdownExists(),"The Designated Address should not be available to change without a workorder.");	
	}
}
